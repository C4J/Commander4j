<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="scanner" Content="autoenter">
<META HTTP-Equiv="acceleratekey" content="all">
<title>Login</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad = "focusIt()">
<form id="login" name="login" action="Process" method="post">
<h2>
    <%
	String siteDescription = "Logon [" + (String) session.getAttribute("siteDescription")+"]";
	out.println(siteDescription);
	%>
</h2>
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="./images/user-login-icon.gif">
<br>
<br>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="50%" height="20"><div align="right">User :&nbsp;&nbsp;</div></td>
    <%
	String username = (String) session.getAttribute("username");
	if (username == null) username = "";
	username = username.trim();
	out.println("<td width=\"50%\" height=\"20\"> <div align=\"left\"><input tabindex=\"1\" name=\"username\" id=\"username\"  type=\"text\"value=\""+username+"\"/></td>");
	%>
  </tr>
  <tr>
    <td width="50%" height="20"> <div align="right">Password :&nbsp;&nbsp;</div></td>
    <%
	String password = (String) session.getAttribute("password");
	if (password == null) password = "";
	password = password.trim();
	out.println("<td width=\"50%\" height=\"20\"> <div align=\"left\"><input tabindex=\"2\" name=\"password\" id=\"password\" type=\"password\" value=\""+password+"\"/></td>");
	%>
  </tr>
</table><br>
  
<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
  <tbody>
  <tr>
    <td width="49%" height="20"> <div align="right"><input tabindex="3" name="buttonSubmit" id="buttonSubmit" value="Submit" onclick="document.login.button.value='Submit';" type="submit"></div></td>
    <td width="2%" height="20"></td>
    <td width="49%" height="20"> <div align="left"><input tabindex="4" name="buttonCancel" id="buttonCancel" value="Cancel" onclick="document.login.button.value='Cancel';" type="submit"></div></td>
  </tr>
</tbody>
</table><br>  

<%
String errormessage = (String) session.getAttribute("_ErrorMessage");
if (errormessage == null) errormessage = "";
errormessage = errormessage.trim();
out.println("<p>"+errormessage+"</p>");
%>
<p>
  <input type="hidden" name="selectedAction" value="validateLogon" />
  <input type="hidden" name="formName" value="login.jsp" />
  <input type="hidden" id="button" name="button" value="Submit" />   
</p>
</form>
<script language="javascript" type="text/javascript">
function focusIt()
{
	document.login.username.focus();
}
</script>
</body>
</html>
