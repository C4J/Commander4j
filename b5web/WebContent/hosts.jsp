<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=240"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="quitbutton" Content="hide">
	<META HTTP-Equiv="scanner" Content="disabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<title>Host Select</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<body onLoad="focusIt()">
	<form id="hosts" name="hosts" action="Process" method="post">
	
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			 	<th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">Hosts</font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="238" height="20" align="left">
					<%
						String hostList = (String) session.getAttribute("hostList");
						if (hostList == null)
							hostList = "";
						hostList = hostList.trim();
						out.println(hostList);
					%>
				</td>
				<td width="2%" height="20"></td>

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
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
				<td width="49" height="20" align="right">
					<input tabindex="1"	name="buttonSubmit" id="buttonSubmit" value="Submit" onclick="document.hosts.button.value='Submit';" type="submit">
				</td>
				<td width="2%" height="20"></td>
				<td width="49%" height="20" align="left">
					<input tabindex="2" name="buttonCancel" id="buttonCancel" value="Cancel" onclick="document.hosts.button.value='Cancel';" type="submit">
				</td>
			</tr>
		</table>

		<label for="Submit"></label> 
		<input type="hidden" name="formName" value="hosts.jsp" /> 
		<input type="hidden" id="button" name="button" value="Submit" />

	</form>
	<%
		String hostIndexFocus = (String) session.getAttribute("hostIndexFocus");
		out.println(hostIndexFocus);
	%>
</body>
</html>
