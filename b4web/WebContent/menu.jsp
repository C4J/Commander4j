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
<title>Menu</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">
<form id="menus" name="menus" action="Process" method="post">
<h2><%=Lang.getText("mod_root") %></h2>
<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="5%" height="20"></td>
	<td width="90%" height="20" align="left">
<%
String menuList = (String) session.getAttribute("menuList");
if (menuList == null) menuList = "";
menuList = menuList.trim();
out.println(menuList);
%>
	</td>
  	<td width="5%" height="20"></td>
  </tr>
</table><br>

<label for="Submit"></label>
<table  width="100%" border="1" cellpadding="0" cellspacing="0"  align="center">
<tr>
<td width="100%" height="20" align="center">
<input type="submit" name="buttonSubmit" value="<%=Lang.getText("web_Submit") %>" id="buttonSubmit" onclick="document.menus.button.value='Submit';" />&nbsp;
<input type="submit" name="buttonCancel" value="<%=Lang.getText("web_Cancel") %>" id="buttonCancel" onclick="document.menus.button.value='Cancel';" />
</td>
</tr>
</table>
<input type="hidden" name="formName" value="menu.jsp" />
<input type="hidden" id="button" name="button" value="Submit" />  
</form>
<%
String menuIndexFocus = (String) session.getAttribute("menuIndexFocus");
out.println(menuIndexFocus);
%>
</body>
</html>
