<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<script type="text/javascript" charset="utf-8" src="javascript/eb3/ebapi-modules.js"></script>	 
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
	
		<title>Host Select</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<form id="hosts" name="hosts" action="Process" method="post">
	
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
		
			<tr>
			 	<th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000">Hosts</font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="100%" height="20" align="left">
					<div style="margin-left: auto;overflow-y:scroll; margin-right: auto; border: 1px #000 solid; padding: 5px;width: 450px;height: 450px;">
					<%
						String hostList = (String) session.getAttribute("hostList");
						if (hostList == null)
							hostList = "";
						hostList = hostList.trim();
						out.println(hostList);
					%>
					</div>
				</td>
				<td width="2%" height="20"></td>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="100%" align="center">
			<tr>
				<td width="100%"><div align="center" style="color: green; background-color: #ffff42">
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
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="49%" height="20" align="right">
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
