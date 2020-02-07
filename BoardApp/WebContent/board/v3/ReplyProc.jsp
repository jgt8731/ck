<%@page import="java.sql.Timestamp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="dto" class="com.bean.boardv1.BoardDto" />
<jsp:setProperty property="*" name="dto"/>

<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl" />
<%
	dto.setRegdate(new Timestamp(System.currentTimeMillis()));
// 	dto.setIp(request.getRemoteAddr());
	dao.replyBoard(dto);
	response.sendRedirect("List.jsp");
%>
