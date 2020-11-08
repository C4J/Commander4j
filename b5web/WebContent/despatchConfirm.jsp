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
	<title>Confirm Despatch</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">	
	<style type="text/css">
		label {
			font-size: 20pt;
			color: #0000FF;
			font-weight: bold;
		}
	</style>
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body>
	<form id="despatchConfirm" name="despatchConfirm" action="Process" method="post">

		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("dlg_Despatch_Confirm")%><%out.println(session.getAttribute("despatchNo"));%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><img src="./images/pallet_sscc.gif" width="150" style="display:block; margin-left:auto; margin-right:auto;"></td>
			</tr>
		</table>
		
		<table width="238" align="center">
			<tr>
				<td style="width: 30%; text-align: right"><input type="checkbox" name="printSTNonConfirm" id="printSTNonConfirm"/>&nbsp;</td>
				<td style="width: 70%; text-align: left"><label for="printSTNonConfirm"><%=Lang.getText("web_Print_STN")%></label></td>
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
		
		<table width="238" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="230" height="20" align="center">
					<input type="submit" tabindex='1' name="buttonYes" value="<%=Lang.getText("web_Yes")%>" id="buttonYes" onclick="document.despatchConfirm.button.value='Yes';" />&nbsp; 
					<input type="submit" tabindex='2' name="buttonNo" value="<%=Lang.getText("web_No")%>" id="buttonNo" onclick="document.despatchConfirm.button.value='No';" />
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="formName" value="despatchConfirm.jsp" /> <input type="hidden" id="button" name="button" value="Yes" />
		
	</form>
</body>
</html>
