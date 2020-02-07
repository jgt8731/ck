package com.bean.boardv1;

import java.util.Vector;

/*설명!!
 * DAO클래스는 DB작업하는 클래스 이다.
 * DAO클래스를 만들떄 팀프로젝트를 한다면....
 * 철수,영희 는 자유게시판을 만든다고 가정할떄..
 * 둘다~ 글수정 기능을 구현 해야한다.
 * 글수정 기능의 메소드이름을 집에가서 각각 달리 만들어 와서 소스를 합칠려고 할떄..
 * 누구의 메소드이름을 따를건지 정해 진것이 없으므로... 싸움.!!!
 * 그래서.. 하나의 메소드의 이름을 정해서!!(원칙을정해서) 사용한다면..
 * 글수정하는 메소드 기능을 쉽게 편리하게 찾아서 사용할수 있음 
 * 결론 : 어떠한 기준(원칙)을 정해줄수 있게 하는것이 인터페이스이다.!!
 * 인터페이스에는 추상메소드를 만들어 놓는다.
 */
public interface IBoardDao {//게시판 관련 기능의 추상메소드 틀 정의!
	//글쓰기 기능
	//입력하고자 하는 글의 정보(DTO객체)를 인자로 전달 받기 
	public void insertBoard(BoardDto dto);
	
	//글하나 상세보기 기능
	public BoardDto getBoard(int num);//상세볼 하나의 글에 대한 글번호 전달
	
	//글수정 기능
	public void updateBoard(BoardDto dto);//수정할 글의 정보(DTO객체) 전달 
	
	//글삭제 기능
	public void deleteBoard(int num);//삭제할 글번호 전달
	
	//글답변 달기 기능
	public void replyBoard(BoardDto dto);//답변할 글의 정보(DTO객체) 전달
	
	//검색기준값, 검색어 값에 의한 select한 결과물을? 글목록 리스트 나타내는 기능
	public Vector getBoardList(String keyField , String keyWord);

}





