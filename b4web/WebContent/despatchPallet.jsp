<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="scanner" Content="autoenter">
<META HTTP-Equiv="acceleratekey" content="all">
<title>Despatch Pallet</title>
<link href="commander.css" rel="stylesheet" type="text/css">
<%
	 String despatchNo = (String) session.getAttribute("despatchNo");
     despatchNo = despatchNo.trim();
%>
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body  onLoad = "focusIt()">
<form id="despatchPallet" name="despatchPallet" action="Process" method="post">
<h2>
<%=Lang.getText("lbl_Despatch_No") %>&nbsp;<%out.println(despatchNo);%>
</h2>
SSCC:
<br><p>
<%
	String sscc = (String) session.getAttribute("sscc");
	if (sscc == null) sscc = "";
	sscc = sscc.trim();
	out.println("<input tabindex=\"1\" name=\"sscc\" type=\"text\" id=\"sscc\" size=\"20\" maxlength=\"20\" value=\""+sscc+"\"/>");
	%>
</p>	
  <p>
<label>
<input name="addRemoveMode" accesskey="A" type="radio" id="radio" value="add" checked="checked" onClick="focusIt()"/>
<%=Lang.getText("web_Add") %>
<input  name="addRemoveMode" accesskey="R" type="radio" id="radio" value="remove"  onClick="focusIt()" />
<%=Lang.getText("web_Remove") %>
</label> 
</p>	
	<table width="153" height="54" border="0">
	<tr>
		
		
	</tr>
	<tr>
		<td><div align="left">Pallet Count :</div></td>
		<td>
<%
String palletCount = (String) session.getAttribute("despatchPalletCount");
if (palletCount == null) palletCount = "";
out.println(palletCount);
%></td>
	</tr>
	<tr>
		<td>
		<div align="left">
			<input tabindex='3' type="submit"	name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit") %>" onclick="document.despatchPallet.button.value='Submit';" />
		</div>
		</td>
		<td>
			<input tabindex='4' type="submit" name="buttonCancel"	id="buttonCancel" value="<%=Lang.getText("web_Cancel") %>" onclick="document.despatchPallet.button.value='Cancel';" />
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
<input type="hidden" name="formName" value="despatchPallet.jsp" />
<input type="hidden" id="button" name="button" value="Submit" />  
</p>
</form>
<script type="text/javascript">
function focusIt()
{
	document.despatchPallet.sscc.focus();
}
</script>
</body>
</html>
