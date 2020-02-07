<%@page import="java.sql.Timestamp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%-- 순서1. Post.jsp페이지(글을 쓸 수 있는 페이지)에서 글쓴 내용이 한글이 있다면 인코딩 방식을 지정 --%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<%-- 순서2. 요청받은 글쓴 내용을  request영역에서 꺼내와서 BoardDto객체에 저장한다 --%>
<jsp:useBean id="dto" class="com.bean.boardv1.BoardDto" />
<jsp:setProperty property="*" name="dto"/>

<%-- 순서3. 추가! DB작업(insert)을 위한 DAO객체 생성 --%>
<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl" />

<%-- 순서4. 글쓰기 DB작업을 위해 글쓴 내용을 저장하고 있는 객체(BoardDto)를 dao클래스의 insertBoard()메소드에 전달하여 DB작업  --%>
<%
	dto.setRegdate(new Timestamp(System.currentTimeMillis()));
// 	dto.setIp(request.getRemoteAddr());
	dao.insertBoard(dto);
	response.sendRedirect("List.jsp");
%>
