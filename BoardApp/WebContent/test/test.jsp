<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if(request.getParameter("form1") != null) {
		out.println("form1: " + request.getParameter("form1"));
	}

	if(request.getParameter("form2") != null) {
		out.println("form2: " + request.getParameter("form2"));
	}
%>

<!-- #은 리로드가 되지 않는다. -->
<a href="#">test.jsp</a>