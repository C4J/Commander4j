<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=240"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="scanner" Content="disabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<title>Change Password</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="changePassword" name="changePassword" action="Process" method="post">
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%="Change Password"%></font></div></th>
			</tr>
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=(String) session.getAttribute("siteDescription")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>		
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">

			<tr>
				<td width="35%" height="20"><div align="right">User :&nbsp;</div></td>
				<%
					String username = (String) session.getAttribute("username");
					if (username == null)
						username = "";
					username = username.trim();
				%>
				<td width="65%" height="20">
					<input tabindex="1" name="username"	id="username" size="20" maxlength="20" type="text" readonly	value="<%out.println(username);%>" />
				</td>
			</tr>
			
			<tr>
		     	<td style="width: 30%; text-align: right">Current :&nbsp;</td>
		     	<td style="width: 70%; text-align: left">
		     	<input tabindex="1" name="password"	id="password" size="20" maxlength="20" type="password" value="" />
			</tr>
			
			<tr>
		     	<td style="width: 30%; text-align: right">New :&nbsp;</td>
		     	<td style="width: 70%; text-align: left">
		     	<input tabindex="2" name="newPassword1"	id="newPassword1" size="20" maxlength="20" type="password" value="" />
			</tr>
			
			<tr>
		     	<td style="width: 30%; text-align: right">Verify :&nbsp;</td>
		     	<td style="width: 70%; text-align: left">
		     	<input tabindex="3" name="newPassword2"	id="newPassword2" size="20" maxlength="20" type="password" value="" />
			</tr>	
			<tr>
				<td>&nbsp;</td>
			</tr>		
		</table>
		
		<table width="238" align="center">
			<tr>
				<td width="238"><div align="center" style="color: green; background-color: #ffff42">
					<%
						String errormessage = (String) session.getAttribute("_ErrorMessage");
						if (errormessage == null)
							errormessage = "";
						errormessage = errormessage.trim();
						out.println(errormessage);
					%>
				</div></td>
			</tr>
		</table>	
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center">
				<input tabindex="4" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"onclick="document.changePassword.button.value='Submit';document.changePassword.submit();"> &nbsp; 
				<input tabindex="5" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"onclick="document.changePassword.button.value='Cancel';document.changePassword.submit();">
				<input type="hidden" id="button" name="button" value="Submit" /> 
				<input type="hidden" name="formName" value="changePassword.jsp" />
				</td>
			</tr>
		</table>
	</form>
	
	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.changePassword.password.focus();
		}
	</script>
	
</body>
</html>
