<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.bean.boardv1.BoardDto"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><title>JSPBoard</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function fnList() {
		// keyField, keyWord값을 parameter로 넘겨준다.
		document.list.submit();
	}
</script>
</head>
<body>
<jsp:useBean id="dao" class="com.bean.boardv1.BoardDaoImpl" />
	
	<%
		int num = Integer.parseInt(request.getParameter("num"));
		String keyField = request.getParameter("keyField");
		String keyWord = request.getParameter("keyWord");
		
		BoardDto dto = dao.getInformation(num);
		
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
   </table>
  </td>
 </tr>
</table>

<center>
<br><br>
<table width=80% cellspacing=0 cellpadding=3>
 <tr>
  <td bgcolor=84F399 height=25 align=center>글쓰기</td>
 </tr>
</table>
<br>
<table width=80% cellspacing=0 cellpadding=3 align=center>
<form name=post method=post action="ReplyProc.jsp" >
<input type="hidden" name="pos" value="<%= dto.getPos() %>">
<input type="hidden" name="depth" value="<%= dto.getDepth() %>">
<input type="hidden" name="ip" value="<%= request.getRemoteAddr() %>">
 <tr>
  <td align=center>
   <table border=0 width=100% align=center>
    <tr>
     <td width=10%>성 명</td>
     <td width=90%><input type=text name=name size=10 maxlength=8></td>
    </tr>
    <tr>
	 <td width=10%>E-Mail</td>
	 <td width=90%><input type=text name=email size=30 maxlength=30></td>
    </tr>
    <tr>
     <td width=10%>홈페이지</td>
     <td width=90%><input type=text name=homepage size=40 maxlength=30></td>
    </tr>
    <tr>
     <td width=10%>제 목</td>
     <td width=90%><input type=text name=subject size=50 maxlength=30></td>
    </tr>
    <tr>
     <td width=10%>내 용</td>
     <td width=90%><textarea name=content rows=10 cols=50></textarea></td>
    </tr>
    <tr>
     <td width=10%>비밀 번호</td> 
     <td width=90% ><input type=password name=pass size=15 maxlength=15></td>
    </tr>
    <tr>
     <td colspan=2><hr size=1></td>
    </tr>
    <tr>
     <td><input type=submit value="등록" >&nbsp;&nbsp;
         <input type=reset value="다시쓰기">&nbsp;&nbsp;
     </td>
     <td><a href="#" onclick="fnList(); return false;">목록</a>&nbsp;&nbsp;
     <a href="#" onclick="javascript:history.back()">뒤로가기</a>
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