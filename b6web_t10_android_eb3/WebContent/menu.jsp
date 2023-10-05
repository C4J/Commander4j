<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<script type="text/javascript" charset="utf-8" src="ebapi-modules.js"></script>	 
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<title>Menu</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage"
		scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body>

	<form id="menus" name="menus" action="Process" method="post">
	
			<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			 	<th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("mod_root")%></font></div></th>
			</tr>
			</table>
			
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">

			<tr>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td height="20" align="left"></td>
				<td height="20" align="left">
					<div style="margin-left: auto;margin-right: auto; width: 480px;">
					<%
						String menuList = (String) session.getAttribute("menuList");
						if (menuList == null)
							menuList = "";
						menuList = menuList.trim();
						out.println(menuList);
					%>
					</div>
				</td>
				<td height="20" align="left"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
				<td width="49" height="20" align="right">
					<input tabindex="1"	name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>" onclick="document.menus.button.value='Submit';" type="submit">
				</td>
				<td width="2%" height="20"></td>
				<td width="49%" height="20" align="left">
					<input tabindex="2" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>" onclick="document.menus.button.value='Cancel';" type="submit">
				</td>
			</tr>
		</table>
		
		<br> 
		<label for="Submit"></label>
	    <input type="hidden" name="formName" value="menu.jsp" /> 
		<input type="hidden" id="button" name="button" value="Submit" />
		

	</form>
	
	<%
		String menuIndexFocus = (String) session.getAttribute("menuIndexFocus");
		out.println(menuIndexFocus);
	%>
		
</body>
</html>
