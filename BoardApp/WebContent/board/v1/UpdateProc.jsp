<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl" />

<jsp:useBean id="dto" class="com.bean.boardv1.BoardDto"/>
<jsp:setProperty property="*" name="dto"/>
<%
	int num = dto.getNum();

	BoardDto tmpDto = dao.getInformation(num);
	
	String storedPass =  tmpDto.getPass();
	
	String paramPass = dto.getPass();
	
	if(!paramPass.equals(storedPass)) {	// 패스워드가 일치하지 않으면 빽시킨다.
%>
		<script>alert('패스워드가 일치하지 않습니다!'); history.back();</script>
<%
	} else {
		dao.updateboard(dto);
		response.sendRedirect("List.jsp");
	}
%>
