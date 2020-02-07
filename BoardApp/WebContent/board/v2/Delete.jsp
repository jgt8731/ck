<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head><title>JSPBoard</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script>
	function check() {
		if (document.form.pass.value == "") {
		alert("패스워드를 입력하세요.");
		form.pass.focus();
		return false;
		}
		document.form.submit();
	}
</script>
</head>
<body>

<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl"/>
<%
	if(request.getParameter("pass") != null) {
		int num = Integer.parseInt(request.getParameter("num"));
		
		BoardDto tmpDto = dao.getInformation(num);

		String storedPass = tmpDto.getPass();
		
		String paramPass = request.getParameter("pass");
		
		if(!paramPass.equals(storedPass)) {
%>	
			<script> alert("패스워드가 일치하지 않습니다!"); history.back(); </script>
<%	
		} else {
			dao.deleteBoard(num);
			response.sendRedirect("List.jsp");
		}
	}
%>

<center>
<br><br>
<table width=50% cellspacing=0 cellpadding=3>
 <tr>
  <td bgcolor=#dddddd height=21 align=center>
      사용자의 비밀번호를 입력해 주세요.</td>
 </tr>
</table>
<table width=70% cellspacing=0 cellpadding=2>
<form name=form method=post action="Delete.jsp" >
<input type="hidden" name="num" value="<%= request.getParameter("num") %>" />
 <tr>
  <td align=center>
   <table align=center border=0 width=91%>
    <tr> 
     <td align=center>  
	  <input type=password name="pass" size=17 maxlength=15>
	 </td> 
    </tr>
    <tr>
     <td><hr size=1 color=#eeeeee></td>
    </tr>
    <tr>
     <td align=center>
	  <input type=button value="삭제완료" onClick="check()"> 
      <input type=reset value="다시쓰기"> 
      <input type=button value="뒤로" onClick="history.back()">
	 </td>
    </tr> 
   </table>
  </td>
 </tr>
</form> 
</table>
</center>
</body>
</html>
