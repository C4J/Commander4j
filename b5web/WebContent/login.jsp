<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
	<title>Login</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<body onLoad="focusIt()">
	<form id="login" name="login" action="Process" method="post">

		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%="Logon"%></font></div></th>
			</tr>
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=(String) session.getAttribute("siteDescription")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><img src="./images/user-login-icon.gif" width="60" style="display:block; margin-left:auto; margin-right:auto;"></td>
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
					<input tabindex="1" name="username"	id="username" size="20" maxlength="20" type="text" value="<%out.println(username);%>" />
				</td>
			</tr>
			<tr>
				<td width="35%" height="20"><div align="right">Password :&nbsp;</div></td>
				<%
					String password = (String) session.getAttribute("password");
					if (password == null)
						password = "";
					password = password.trim();
				%>
				<td width="65%" height="20">
					<input tabindex="2" name="password"	id="password" size="20" maxlength="20" type="password" value="<%out.println(password);%>" />
				</td>
			</tr>
		</table>
		<br>
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

		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
				<td width="49" height="20" align="right">
					<input tabindex="3"	name="buttonSubmit" id="buttonSubmit" value="Submit" onclick="document.login.button.value='Submit';" type="submit">
				</td>
				<td width="2%" height="20"></td>
				<td width="49%" height="20" align="left">
					<input tabindex="4" name="buttonCancel" id="buttonCancel" value="Cancel" onclick="document.login.button.value='Cancel';" type="submit">
				</td>
			</tr>
		</table>

		<input type="hidden" name="selectedAction" value="validateLogon" /> 
		<input type="hidden" name="formName" value="login.jsp" /> 
		<input type="hidden" id="button" name="button" value="Submit" />

	</form>
	
	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.login.password.value = '';
			document.login.username.focus();
		}
	</script>
	
</body>
</html>
