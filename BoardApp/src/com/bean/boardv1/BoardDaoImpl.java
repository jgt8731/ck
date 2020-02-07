package com.bean.boardv1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//DB작업 하는 자바빈클래스의 종류중 하나 !!
public class BoardDaoImpl implements IBoardDao {
											
	//DB작업 삼총사 변수 선언
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//커넥션풀을 담을 변수 선언
	private DataSource ds;
	
	//생성자 : 커넥션풀을 얻는 작업
	public BoardDaoImpl() {
		try{
			//1.Was서버와 연결된 BoardApp웹프로젝트의 모든 정보를 가지고 있는 컨텍스트 객체 생성
			Context init = new InitialContext();
			//2.연결된 WAS서버에서 DataSource(커넥션풀)검색해서 가져오기 
			ds = (DataSource)init.lookup("java:comp/env/jdbc/jspbeginner");
		}catch(Exception err){
			System.out.println("BoardDao()생성자에서 커넥션풀 얻기 실패 : " + err);
		}
	}
	
	//커넥션풀에 사용한 Connection객체를 반납하는 메소드
	public void freeResource(){
		if(con != null)try {con.close();} catch (SQLException e) {e.printStackTrace();}
		if(pstmt != null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(rs != null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	//DB에 우리가 작성한 글내용을 추가 하는  메소드 
	@Override
	public void insertBoard(BoardDto dto) {
		try{
			//DB연결 : DataSource(커넥션풀)에서 
			//미리데이터베이스와 연결된 Connection객체를 리턴 해온다
			con = ds.getConnection(); //DB연결 접속 객체 얻기 
			
			//주글규칙 :또다른 새글을 입력시 먼저 기존에 입력되어던 글들의 모든 pos값은 1씩 증가 시킨다.
			String sql = "update tblBoard set pos = pos + 1";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
			//주글규칙 :  처음입력되는 데이터는 무조건 pos와 depth 0 0 으로 입력한다.
			sql = "insert into tblBoard("
					   + "name,email,homepage,subject,"
					   + "content,pass,count,ip,regdate,pos,depth"
					   + ")values(?,?,?,?,?,?,0,?,?,0,0);";
						//count->0, pos->0 , depth->0
			//connection객체의 힘을빌려 !  insert구문을 DB에 실행할! 
			//PreparedStatement객체를 얻을 수 있는데..
			//이 PreparedStatement객체를 얻어 올떄는  ?????제외한 나머지 insert구문을
			//PreparedStatement객체에 저장하여!!
			//PreparedStatement객체 자체를 리턴 받아온다 ???
			// 아래의 메소드에서!! 
			//insert구문을 실행할 PreparedStatement객체얻기
			pstmt = con.prepareStatement(sql);
			//PreparedStatement객체에 나머지 ???????값들을 셋팅!!
			//어떻게? 현재의 메소드의 매개변수로 전달 받은 BoardDto객체를 사용하여
			//?????셋팅
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getHomepage());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setString(6, dto.getPass());
			pstmt.setString(7, dto.getIp());
			pstmt.setTimestamp(8, dto.getRegdate());
			
			pstmt.executeUpdate();
			 			
		}catch(Exception err){
			System.out.println("insertBoard()<-DB연결 또는 SQL구문 오류:"+ err);
		}finally {
			//커넥션풀에 자원들 반납
			freeResource();
		}
		
	}
	//DB에 글조회수 1증가 시키는 메소드 
	public void getCount(int num){
		try {
			//DataSource(커넥션풀)에서 DB연동 객체(Connection)객체 빌려오기
			con = ds.getConnection();
			//글내용을 읽었을떄는 ????  DB에 ~~ 글조회수를 증가 해야 한다!
			String sql = "update tblBoard set count = count + 1 where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}catch(Exception e){
			System.out.println(e);
		}finally{
			freeResource();
		}
			
	}
	
	
	//글번호를 전달 받아 그글번호에 해당하는 글정보를 검색하기 위한 메소드 
	@Override
	public BoardDto getBoard(int num) {
		//넘겨 받은 글번호를 통해서 DB로 select한 하나의 글을..
		//BoardDto객체에 저장하기 위해 BoardDto객체 생성
		BoardDto dto = new BoardDto();
		
		//DB연결 및  select구문을 잘못 작성하여!! 예외(에러)가 발생할수 있으므로..
		//일단 try ~ Catch 블럭을 만든다
		try {
			//글조회수 1증가 
			getCount(num);
			
			//DataSource(커넥션풀)에서 DB연동 객체(Connection)객체 빌려오기
			con = ds.getConnection();
		
			//넘겨 받은 글번호를 통해서 DB로 select 하는 SQL문 작성
			String sql = "select * from tblBoard where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//select 한 글정보를 resultSet임시객체에 저장 
			rs = pstmt.executeQuery();
			//select 한 글정보가  resultSet임시객체에 한줄이라도 저장되어 있다면?
			if(rs.next()){
			//resultSet임시객체에 저장되어 있는 검색한 글정보를 BoardDto객체에 저장하기
				dto.setContent(rs.getString("content"));
				dto.setCount(rs.getInt("count"));
				dto.setDepth(rs.getInt("depth"));
				dto.setEmail(rs.getString("email"));
				dto.setHomepage(rs.getString("homepage"));
				dto.setIp(rs.getString("ip"));
				dto.setName(rs.getString("name"));
				dto.setNum(rs.getInt("num"));
				dto.setPass(rs.getString("pass"));
				dto.setPos(rs.getInt("pos"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
				System.out.println("getBoard()메소드에서  DB연결 또는 SQL구문 오류 : " + e);
		} finally {
			//자원해제 
			freeResource();
		}
		return dto;
	}
	
	//글번호를 전달 받아 그글번호에 해당하는 글정보를 검색하기 위한 메소드 
	public BoardDto getBoardInfo(int num) {
		//넘겨 받은 글번호를 통해서 DB로 select한 하나의 글을..
		//BoardDto객체에 저장하기 위해 BoardDto객체 생성
		BoardDto dto = new BoardDto();
		
		//DB연결 및  select구문을 잘못 작성하여!! 예외(에러)가 발생할수 있으므로..
		//일단 try ~ Catch 블럭을 만든다
		try {
			//DataSource(커넥션풀)에서 DB연동 객체(Connection)객체 빌려오기
			con = ds.getConnection();
		
			//넘겨 받은 글번호를 통해서 DB로 select 하는 SQL문 작성
			String sql = "select * from tblBoard where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//select 한 글정보를 resultSet임시객체에 저장 
			rs = pstmt.executeQuery();
			//select 한 글정보가  resultSet임시객체에 한줄이라도 저장되어 있다면?
			if(rs.next()){
			//resultSet임시객체에 저장되어 있는 검색한 글정보를 BoardDto객체에 저장하기
				dto.setContent(rs.getString("content"));
				dto.setCount(rs.getInt("count"));
				dto.setDepth(rs.getInt("depth"));
				dto.setEmail(rs.getString("email"));
				dto.setHomepage(rs.getString("homepage"));
				dto.setIp(rs.getString("ip"));
				dto.setName(rs.getString("name"));
				dto.setNum(rs.getInt("num"));
				dto.setPass(rs.getString("pass"));
				dto.setPos(rs.getInt("pos"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
				System.out.println("getBoard()메소드에서  DB연결 또는 SQL구문 오류 : " + e);
		} finally {
			//자원해제 
			freeResource();
		}
		return dto;
	}
	
	
	//글수정 메소드 : 수정할 글정보를 담고 있는 BoardDto객체를  인자로 전달 받아...글수정
	@Override
	public void updateBoard(BoardDto dto) {
		try {
			//커넥션풀에서 커넥션객체 얻기 
			con = ds.getConnection();
			
			String sql = "update tblBoard set name=?, email=?, subject=?, "
					   + "content=? where num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getNum());
			
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//자원해제
			freeResource();
		}
	}
	
	//글삭제 메소드 
	@Override
	public void deleteBoard(int num) {
		try {
			con = ds.getConnection();
			
			String sql="delete from tblBoard where num=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			 //System.out.println(e);
			 //System.out.println(  e.getMessage() );  
		} finally {
			//자원해제
			freeResource();
		}
	}
	//답변글 등록하기
	//1.부모글보다 큰 pos는 1증가 시킴
	//2.답변글은 부모글의 pos에 1을 더해 준다.
	//3.부모글의 depth(들여쓰기)에 1을 더해준다.
	
	//부모글의 pos값을 인자로 전달받아 ...부모글보다 큰 pos는 1증가 시킴
	public void replyUpPos(int ParentPos){
		try {
			con = ds.getConnection();
			String sql = "update tblBoard set pos = pos + 1 where  pos > ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ParentPos);
			pstmt.executeUpdate();	
		} catch (Exception e) {
			System.out.println("replyUpPos메소드에서 오류 : " + e);
		}finally {
			//자원해제
			freeResource();
		}
		
	}
	
	//답변글을 작성하는 메소드 
	@Override 
	public void replyBoard(BoardDto dto) {//<---부모글 pos,depth + 답변글정보 
		try {
			con = ds.getConnection();
			//2.답변글은 부모글의 pos에 1을 더해 준다.
			int pos = dto.getPos() + 1;
			//3.부모글의 depth(들여쓰기)에 1을 더해준다
			int depth = dto.getDepth() + 1;
			
			String sql = "insert into tblBoard("
					   + "name,email,homepage,subject,"
					   + "content,pass,count,ip,regdate,pos,depth"
					   + ")values(?,?,?,?,?,?,0,?,?,?,?);";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getHomepage());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setString(6, dto.getPass());
			pstmt.setString(7, dto.getIp());
			pstmt.setTimestamp(8, dto.getRegdate());
			pstmt.setInt(9, pos);
			pstmt.setInt(10, depth);
			pstmt.executeUpdate(); //답변글 작성~ insert실행~ 
		
		} catch (Exception e) {
			System.out.println("rePlyBoard()에서 오류  : "+ e);
		} finally {
			freeResource();
		}

	}
	
	
	//DB에 있는 글들을 select해서 가져와서 전체 글목록 리스트를 게시판에 나타내기 위한 메소드 
	//List.jsp페이지에서 사용하는 메소드 
	@Override						//검색기준값 ,      검색어
	public Vector<BoardDto> getBoardList(String keyField, String keyWord) {
		
		Vector<BoardDto> v = new Vector<BoardDto>();
		
		String sql = "";
		
		try{
			//DataSource(커넥션풀)에서  DB와미리연결된 Connection객체를 빌려온다
			con = ds.getConnection(); //DB연결
			
			//검색어를 입력하지 않았다면?
			if(keyWord == null || keyWord.isEmpty()){
				//가장 최신글이 위로 올라오게 num필드값을 기준으로 하여 내림차순 정렬하여 글검색
//				sql = "select * from tblBoard order by num desc";
				sql = "select * from tblBoard order by pos asc";
			}else{//검색어를 입력 했다면.
//				sql = "select * from tblBoard where " + keyField  
//					+ " like '%" + keyWord + "%' order by num desc"; 
				
				sql = "select * from tblBoard where " + keyField  
						+ " like '%" + keyWord + "%' order by pos asc";
			}
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDto dto = new BoardDto();
				dto.setContent(rs.getString("content"));
				dto.setCount(rs.getInt("count"));
				dto.setDepth(rs.getInt("depth"));
				dto.setEmail(rs.getString("email"));
				dto.setHomepage(rs.getString("homepage"));
				dto.setIp(rs.getString("ip"));
				dto.setName(rs.getString("name"));
				dto.setNum(rs.getInt("num"));
				dto.setPass(rs.getString("pass"));
				dto.setPos(rs.getInt("pos"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setSubject(rs.getString("subject"));
				
				v.add(dto);
			}		
		}catch(Exception err){
			System.out.println(err);
		}finally {
			//자원해제 
			freeResource();
		}
		return v;
		
	}//geBoardList메소드 끝
	
	//들여쓰기를 위한 메소드 
	public String useDepth(int depth){ //들여쓰기 레벨값을 전달 받아..
		String result = "";
		
		for(int i=0; i<depth*3; i++)
			result += "&nbsp;";
		
		return result;
	}
	
	
	
	
	
}//BoardDao클래스 끝 














