<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<META HTTP-Equiv="quitbutton" Content="hide">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="scanner" Content="autoenter">
<META HTTP-Equiv="acceleratekey" content="all">
<title>Host Select</title>
<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad = "focusIt()">
<form id="hosts" name="hosts" action="Process" method="post">

<h2>Hosts</h2>
<br>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="5%" height="20"></td>
	<td width="90%" height="20" align="left">
<%
String hostList = (String) session.getAttribute("hostList");
if (hostList == null) hostList = "";
hostList = hostList.trim();
out.println(hostList);
%>
	</td>
  	<td width="5%" height="20"></td>
  </tr>
</table><br>

<label for="Submit"></label>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
<td width="230" height="20" align="center">
<input type="submit" name="buttonSubmit" value="Submit" id="buttonSubmit" onclick="document.hosts.button.value='Submit';" />&nbsp;
<input type="submit" name="buttonCancel" value="Cancel" id="buttonCancel" onclick="document.hosts.button.value='Cancel';" />
</td>
</tr>
</table>
<input type="hidden" name="formName" value="hosts.jsp" />
<input type="hidden" id="button" name="button" value="Submit" /> 
<%
String errormessage = (String) session.getAttribute("_ErrorMessage");
if (errormessage == null) errormessage = "";
errormessage = errormessage.trim();
out.println("<p>"+errormessage+"</p>");
%>
</form>
<%
String hostIndexFocus = (String) session.getAttribute("hostIndexFocus");
out.println(hostIndexFocus);
%>
</body>
</html>
