<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<script type="text/javascript" charset="utf-8" src="javascript/eb3/ebapi-modules.js"></script>	 
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
	
		<title>Printer Select</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">

	<form id="printerSelect" name="printerSelect" action="Process" method="post">
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("mod_FRM_CM_PRINTERS")%></font></div></th>
			</tr>
			
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0"  align="center">
			<tr>
				<td>&nbsp;</td>
				<td>
					<div style="margin-left: auto ; margin-right: auto; border: none #000 solid; padding: 5px;width: 450px;height: 450px;">
						<%out.println(session.getAttribute("printerList"));%>
				</div>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0"  align="center" style="height: 90px; ">
			<tr>
				<td width="100%" height="20" align="center">
				<input type="submit" tabindex='8' name="buttonSelect" value="<%=Lang.getText("btn_Select") %>" id="buttonSelect" onclick="document.printerSelect.button.value='Select';" />&nbsp;
				<input type="submit" tabindex='9' name="buttonCancel" value="<%=Lang.getText("web_Cancel") %>" id="buttonCancel" onclick="document.printerSelect.button.value='Cancel';" />
				<input type="hidden" name="formName" value="printerSelect.jsp" /> 
				<input type="hidden" id="button" name="button" value="Select" />
				</td>	
			</tr>
		</table>
		
	</form>
	
	<script language="javascript" type="text/javascript">
	function focusIt()
	{
		document.printerSelect.selectedPrintQueue.focus();
	}
	</script>

</body>
</html>
