<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="scanner" Content="autoenter">
<META HTTP-Equiv="acceleratekey" content="all">
<title>Printer Select</title>
<link href="commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">
<form id="printerSelect" name="printerSelect" action="Process" method="post">
<h2><%=Lang.getText("mod_FRM_CM_PRINTERS") %></h2><br>
<table width="100%" border="1" cellpadding="0" cellspacing="0"  align="center">
	<tr>
		<td>
		<%out.println(session.getAttribute("printerList"));%>
		</td>
	</tr>
</table>
<br/>
<table width="100%" border="1" cellpadding="0" cellspacing="0"  align="center">
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
