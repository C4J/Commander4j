<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=240"/>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="scanner" Content="disabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<title>Session Timeout</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<form id="sessionTimeout" name="sessionTimeout" action="Process" method="post">

		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">Session Timeout</font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>		

		<table width="238" align="center">
			<tr>
				<td width="238"><div align="center" style="color: green; background-color: #ffff42">
					Your session has disconnected due to inactivity.
				</div></td>
			</tr>
		</table>
		<br>
		<br>	
			
		<table width="238" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="238" height="20" align="center">
					<input type="submit" tabindex='1' name="buttonRestart" value="Restart" id="buttonRestart" onclick="document.sessionTimeout.button.value='Restart';" /> 
					<input type="hidden" name="formName" value="sessionTimeout.jsp" /> 
					<input type="hidden" id="button" name="button" value="Restart" />
				</td>
			</tr>
		</table>
		
	</form>
</body>
</html>