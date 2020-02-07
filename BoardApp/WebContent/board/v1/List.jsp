<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<link href="style.css" rel="stylesheet" type="text/css">
<script>
	function check(){
		if(document.search.keyWord.value == ""){
			alert("검색어를 입력하세요.");
			document.search.keyWord.focus();
			return;
		}
		document.search.submit();
	}
	
	/*추가2   처음으로 링크를 클릭했을때 호출되는 함수 역할 : Form태그 의 action속성에 작성한 페이지로 전송(이동)!하는 역할*/
	function fnList(num) {
		document.list.action = "List.jsp";
		document.list.submit();
	}
	
	function fnRead(num) {
		document.read.num.value = num;
		document.read.submit();
	}
</script>
<BODY>

<%-- 순서1. 추가! DB작업을 하기위해 DAO객체 생성 --%>
<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl"/>

<%!
	String keyWord = "", keyField = ""; // 검색기준값과 검색을 전달받아. 저장할 변수
%>

<%
	if(request.getParameter("keyWord") != null) {
		keyField = request.getParameter("keyField");
		keyWord = request.getParameter("keyWord");
	}
	/* 
		추가4. [처음으로]링크를 클릭했을 때 (List.jsp페이지를 재요청 했을때...)
		input태그의 hidden값으로 요청한 name속성이 reload인 값이 존재하고!!
		이 예제에선 없어도 똑같은 기능을 수행한다!!
	*/
	if(request.getParameter("reload") != null) {
		// 만약에 List.jsp페이지로 다시 요청 받은 값이 true와 같을 때.
		if(request.getParameter("reload").equals("true")) {
			keyWord = "";
		}
	}
	
	
	List<BoardDto> v = dao.getBoardList(keyField, keyWord);
	
	System.out.println("v:" + v);
%>
<%-- 순서2. 끝 ------------------------------------------------%>

<%--
	게시판 글리스트 중에서 하나의 글내용을 보기 위해 글제목을 클릭 했을때..
	Read.jsp로 선택한 글에 대한 글번호, 이 글을 검색한 검색기준값 + 검색어값 까지 같이 전달 함.
 --%>
<form action="Read.jsp" name="read" method="post">
	<input type="hidden" name="num" /> <%-- 글번호 --%>
	<input type="hidden" name="keyField" value="<%= keyField %>"/>
	<input type="hidden" name="keyWord" value="<%= keyWord %>"/>
</form>

<%-- 추가3. 현재 List.jsp페이지가 리로드 하는지 안하는지 구별하기 위한 값. true를 다시 List.jsp에 전달 --%>
<form name="list" method="post">

	<input type="hidden" name="reload" value="true" /> <%-- 사실 이 예제에선 없어도 똑같은 기능을 한다. --%>

</form>





<center><br>
<h2>JSP Board</h2>


<table align=center border=0 width=80%>
<tr>
	<td align=left>Total :  Articles(
		<font color=red>  1 / 10 Pages </font>)
	</td>
</tr>
</table>
<table align=center width=80% border=0 cellspacing=0 cellpadding=3>
<tr>
	<td align=center colspan=2>
		<table border=0 width=100% cellpadding=2 cellspacing=0>
			<tr align=center bgcolor=#D0D0D0 height=120%>
				<td> 번호 </td>
				<td> 제목 </td>
				<td> 이름 </td>
				<td> 날짜 </td>
				<td> 조회수 </td>
			</tr>
		<%-- 순서3 시작!! DB로부터 검색한 레코드 뿌려주기 --%>
		<%
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 만약에 벡터안에 글들이(BoardDto객체들)이 하나라도 존재하지 않으면...
			if(v.isEmpty()) {
		%>
			<tr>
				<td colspan="5">등록된 글이 없습니다.</td>
			</tr>
		<%
			} else {
				for(BoardDto dto : v) {
					int num = dto.getNum();
		%>
			<%-- 게시판 글제목을 클릭 했을 때... 글 하나에 대한 내용을 볼 수 있는 화면을 요청 해야 한다. --%>
			<tr align="center">
				<td><%= num %></td>
				<td><a href="#" onclick="fnRead('<%=num%>'); return false;"><%= dto.getSubject() %></a></td>
				<td><a href="mailto:<%= dto.getEmail() %>"><%= dto.getName() %></a></td>
				<td><%= format.format(dto.getRegdate()) %></td>
				<td><%= dto.getCount() %></td>
			</tr>
		<%
				}
			}
		%>
		
		<%-- 순서3 끝!!!!!!!!!!!!!!!!!!!!!!!!!!! --%>
		</table>
	</td>
</tr>
<tr>
	<td><BR><BR></td>
</tr>
<tr>
	<td align="left">Go to Page </td>
	<td align=right>
		<a href="Post.jsp">[글쓰기]</a>
		<a href="#" onclick="fnList(); return false;">[처음으로]</a>
	</td>
</tr>
</table>
<BR>
<form action="List.jsp" name="search" method="post">
	<table border=0 width=527 align=center cellpadding=4 cellspacing=0>
	<tr>
		<td align=center valign=bottom>
			<select name="keyField" size="1">
				<option value="name"> 이름
				<option value="subject"> 제목
				<option value="content"> 내용
			</select>

			<input type="text" size="16" name="keyWord" >
			<input type="button" value="찾기" onClick="check()">
			<input type="hidden" name="page" value= "0">
		</td>
	</tr>
	</table>
</form>
</center>	
</BODY>
</HTML>