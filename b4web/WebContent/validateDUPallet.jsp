<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="expires" content="0">
<META http-equiv="volume" content="0x1000">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="ean13" Content="enabled">
<META HTTP-Equiv="code128-ean128" Content="true">
<META HTTP-Equiv="code128-maxlength" Content="55">
<META HTTP-Equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
<META HTTP-Equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
<META http-equiv="Volume" content="0x1000">

<title>Validate Barcode</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="validateDUPallet" name="validateDUPallet" action="Process" method="post">
		<h2><%=Lang.getText("web_Barcode_Validate")%></h2>
		<p>
			&nbsp; &nbsp; <%=Lang.getText("lbl_Process_Order")%>
			<%
		     	String validateOrder = (String) session.getAttribute("validateOrder");
				out.println(" " + validateOrder + "");
			%>
			<br><br>
			<%
		     	String materialDescription = (String) session.getAttribute("materialDescription");
				out.println(" " + materialDescription + "");
			%>
		</p>
		<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img src="./images/pallet_du.gif">
		<br>
		<%=Lang.getText("web_Scan_Barcode")%><br>
		<input tabindex="1" type="text" name="palletDU" id="palletDU" size="55" maxlength="55" value=""/> 
		<br>	
		<%
			String errormessage = (String) session.getAttribute("_ErrorMessage");
			if (errormessage == null)
				errormessage = "";
			errormessage = errormessage.trim();
			out.println("<p>" + errormessage + "</p>");
		%>
		<table width="100%" border="1" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center">
				<input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>" onclick="document.validateDUPallet.button.value='Submit';document.validateDUPallet.submit();">
				&nbsp; 
				<input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>" onclick="document.validateDUPallet.button.value='Cancel';document.validateDUPallet.submit();"> 
				<input type="hidden" id="button" name="button" value="Submit" /> 
				<input type="hidden" name="formName" value="validateDUPallet.jsp" />
				<input type="hidden" name="barcodeType" value="none" />
				<input type="hidden" name="barcodeLength" value="0" />
				</td>
			</tr>
		</table>
	</form>
	
	<script language="javascript" type="text/javascript">

		function focusIt() {
			document.validateDUPallet.palletDU.focus();
		}

		function doScan(data, source, type, time, length) {
			document.validateDUPallet.button.value = 'Submit';
			document.validateDUPallet.palletDU.value = data;
			document.validateDUPallet.barcodeType.value = type;
			document.validateDUPallet.barcodeLength.value = length;
			document.validateDUPallet.submit();
		}

		function goBack() {
			window.history.back();
		}
		
	</script>
</body>
</html>
