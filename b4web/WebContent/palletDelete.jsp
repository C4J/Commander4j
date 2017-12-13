<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="expires" content="0">
<META http-equiv="volume" content="0x1000">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="ean13" Content="enabled">
<META HTTP-Equiv="code128-ean128" Content="true">
<META HTTP-Equiv="code128-maxlength" Content="20">
<META HTTP-Equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
<META HTTP-Equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
<META http-equiv="Volume" content="0x1000">

<title>Pallet Delete</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">
<form id="palletDelete" name="palletDelete" action="Process" method="post">
<h2>
<%=Lang.getText("mod_FRM_ADMIN_PALLET_DELETE") %>
</h2>
<br>	
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="./images/pallet_sscc.gif">
<br>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=Lang.getText("web_SSCC") %><br>
  <%
	String sscc = (String) session.getAttribute("sscc");
	if (sscc == null) sscc = "";
	sscc = sscc.trim();
	out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input tabindex=\"1\" name=\"sscc\" type=\"text\" id=\"sscc\" size=\"20\" maxlength=\"20\" value=\""+sscc+"\"/>");
	%>
<br><br>
<p>
<%=Lang.getText("lbl_Deleted") %>
<%
String confirmCount = (String) session.getAttribute("deleteCount");
out.println(" "+confirmCount+"");
%>
</p>
<%
String errormessage = (String) session.getAttribute("_ErrorMessage");
if (errormessage == null) errormessage = "";
errormessage = errormessage.trim();
out.println("<p>"+errormessage+"</p>");
%>
<table width="100%" border="1" cellpadding="0" cellspacing="0"  align="center">
	 <tr>
	 <td width="100%" height="20" align="center">
	 <input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>" onclick="document.palletDelete.button.value='Submit';document.palletDelete.submit();">&nbsp; 
	 <input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>" onclick="document.palletDelete.button.value='Cancel';document.palletDelete.submit();"> 
	 <input type="hidden" id="button" name="button" value="Submit" />
	 <input type="hidden" name="formName" value="palletDelete.jsp" />
	 <input type="hidden" name="barcodeType" value="none" />
     <input type="hidden" name="barcodeLength" value="0"  /> 
	</td>	
	</tr>
</table>
</form>
	<script language="javascript" type="text/javascript">

		function focusIt() {
			document.palletDelete.sscc.focus();
		}

		function doScan(data, source, type, time, length) {
			document.palletDelete.button.value = 'Submit';
			document.palletDelete.sscc.value = data;
			document.palletDelete.barcodeType.value = type;
			document.palletDelete.barcodeLength.value = length;
			document.palletDelete.submit();
		}

		function goBack() {
			window.history.back();
		}	
	</script>
</body>
</html>
