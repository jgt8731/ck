<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	try {
		Context initCtx = new InitialContext();
		
		// 1. initCtx객체의 lookup메소드를 이용해서 "java:comp/env"에 해당하는 InitialContext 객체를 찾아서...
		// envCtx에 삽입 ________________
// 		Context envCtx = (Context) new InitialContext("java:comp/env");
		// InitialContext()객체는 웹어플리케이션이 처음으로 배치될 때 설정되고,
		// 모든 설정된 자원은 JNDI namespace 공간의 java:comp/env 경로 부분에 놓이게 됩니다.
		// 그래서 접근을 이렇게 주소를 설정 해주어야 한다.
		
		// 2. envCtx의 lookup메소드를 이용해서 네임스페이스 공간에 등록된 "java:jspbeginner"에 해당하는
		// 커넥션풀 객체를 찾아서 ds변수에 저장
// 		DataSource ds = (DataSource) envCtx.lookup("jdbc/jspbeginner");
		
		DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/jspbeginner");
		
		Connection con = ds.getConnection();
		
		if(con != null) {
			out.println("연결성공");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
</body>
</html>
