<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl"/>
<%
	int num = Integer.parseInt(request.getParameter("num"));
	
	BoardDto tmpDto = dao.getInformation(num);

	String storedPass = tmpDto.getPass();
	
	String paramPass = request.getParameter("pass");
	
	if(paramPass != null && !paramPass.equals(storedPass)) {
%>	
		<script> alert("패스워드가 일치하지 않습니다!"); history.back(); </script>
<%	
	} else {
		dao.deleteBoard(num);
		response.sendRedirect("List.jsp");
	}
%>