<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head><title>JSPBoard</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<script type="text/javascript">
	function fnList() {
		// keyField, keyWord값을 parameter로 넘겨준다.
		document.list.submit();
	}
</script>

<body>
	<%--순서1. 글 조회수 증가, 글 상세보기 DB작업함 DAO객체 생성 --%>
	<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl" />
	
	<%
		int num = Integer.parseInt(request.getParameter("num"));
		String keyField = request.getParameter("keyField");
		String keyWord = request.getParameter("keyWord");
		BoardDto dto = dao.getBoard(num);
		
		String content = dto.getContent().replace("\n", "<br/>");
		
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	%>

<form action="List.jsp" name="list" method="post">
	<input type="hidden" name="keyField" value="<%= keyField %>" />
	<input type="hidden" name="keyWord" value="<%= keyWord %>" />
</form>

<br><br>
<table align=center width=70% border=0 cellspacing=3 cellpadding=0>
 <tr>
  <td bgcolor=9CA2EE height=25 align=center class=m>글읽기</td>
 </tr>
 <tr>
  <td colspan=2>
   <table border=0 cellpadding=3 cellspacing=0 width=100%> 
    <tr> 
	 <td align=center bgcolor=#dddddd width=10%> 이 름 </td>
	 <td bgcolor=#ffffe8><%= dto.getName() %></td>
	 <td align=center bgcolor=#dddddd width=10%> 등록날짜 </td>
	 <td bgcolor=#ffffe8><%= sFormat.format(dto.getRegdate()) %></td>
	</tr>
    <tr>
	 <td align=center bgcolor=#dddddd width=10%> 메 일 </td>
	 <td bgcolor=#ffffe8 ><%= dto.getEmail() %></td> 
	 <td align=center bgcolor=#dddddd width=10%> 홈페이지 </td>
	 <td bgcolor=#ffffe8 ><a href="http://" target="_new">http://<%= dto.getHomepage() %></a></td> 
	</tr>
    <tr> 
     <td align=center bgcolor=#dddddd> 제 목</td>
     <td bgcolor=#ffffe8 colspan=3><%= dto.getSubject() %></td>
   </tr>
   <tr> 
    <td colspan=4><%= content %></td>
   </tr>
   <tr>
    <td colspan=4 align=right>
     로 부터 글을 남기셨습니다./  조회수 : <%= dto.getCount() %>
    </td>
   </tr>
   </table>
  </td>
 </tr>
 <tr>
  <td align=center colspan=2> 
	<hr size=1>
	[ <a href="#" onclick="fnList(); return false;">목 록</a> | 
	<a href="Update.jsp?num=<%= num %>">수 정</a> |
	<a href="Delete.jsp?num=<%= num %>">삭 제</a> |
	<a href="Reply.jsp?num=<%= num %>&keyField=<%= keyField %>&keyWord=<%= keyWord %>">답 변</a>]<br>
  </td>
 </tr>
</table>
</body>
</html>
