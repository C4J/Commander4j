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
<title>Pallet Info</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">
<form id="palletInfo" name="palletInfo" action="Process" method="post">
<h2>
<%=Lang.getText("mod_FRM_PAL_INFO") %>
</h2><br>
    <%=Lang.getText("web_SSCC") %><br><%
	String sscc = (String) session.getAttribute("sscc");
	if (sscc == null) sscc = "";
	sscc = sscc.trim();
	out.println("<input tabindex=\"1\" name=\"sscc\" type=\"text\" id=\"sscc\" size=\"20\" maxlength=\"20\" value=\""+sscc+"\"/>");
	%>
	<br><br>
<table  width="100%" border="1" cellpadding="0" cellspacing="0"  align="center">
<tr>
<td width="100%"  height="20" align="center">
   <input tabindex='3' type="submit" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit") %>" onclick="document.palletInfo.button.value='Submit';" />  
   <input tabindex='4' type="submit" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel") %>" onclick="document.palletInfo.button.value='Cancel';" />
</td>
</tr>
</table>
<%
String errormessage = (String) session.getAttribute("_ErrorMessage");
if (errormessage == null) errormessage = "";
errormessage = errormessage.trim();
out.println("<p>"+errormessage+"</p>");
%>
<p>
	<input type="hidden" name="formName" value="palletInfo.jsp" />
	<input type="hidden" id="button" name="button" value="Submit" />  
</p>
</form>
<script language="javascript" type="text/javascript">
function focusIt()
{
	document.palletInfo.sscc.focus();
}
</script>
</body>
</html>
