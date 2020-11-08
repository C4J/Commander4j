<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>	
    <meta name="viewport" content="width=240"/>
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="expires" content="0">
	<META http-equiv="volume" content="0x1000">
	<META HTTP-Equiv="acceleratekey" content="all">
	<META HTTP-Equiv="scanner" Content="enabled">
	<META HTTP-Equiv="ean13" Content="enabled">
	<META HTTP-Equiv="code128-ean128" Content="true">
	<META HTTP-Equiv="code128-maxlength" Content="55">
	<META HTTP-Equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
	<META HTTP-Equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
	<META http-equiv="Volume" content="SetVolume:0xFFFF">
	<title>Validate DU Select</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="validateDUSelect" name="validateDUSelect" action="Process" method="post">
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("web_Barcode_Validate")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="238" align="center">
			<tr>
				<td style="width: 50%; text-align: right"><%=Lang.getText("lbl_Process_Order")%>&nbsp;</td>
				<td style="width: 50%; text-align: left">
					<%
					String validateOrder = (String) session.getAttribute("validateOrder");
					if (validateOrder == null)
						validateOrder = "";
					validateOrder = validateOrder.trim();
					%>
					<input tabindex="1" name="validateOrder" id="validateOrder" type="text" size="10" maxlength="10" value="<%out.println(validateOrder);%>"/>
				</td>
			</tr>
		</table>
		
		<br>
		<img src="./images/pallet_sscc.gif" width="150" style="display:block; margin-left:auto; margin-right:auto;">
				
		<table width="238" align="center">
			<tr>
				<td style="width: 30%; text-align: right"><%=Lang.getText("web_SSCC")%>&nbsp;</td>
				<td style="width: 70%; text-align: left">
					<%
						String sscc = (String) session.getAttribute("validateSSCC");
						if (sscc == null)
							sscc = "";
						sscc = sscc.trim();
					%>
					<input tabindex="1" name="validateSSCC" type="text" id="validateSSCC" size="20" maxlength="20" value="<%out.println(sscc);%>"/>
				</td>
			</tr>
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

		<table width="238" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="238" height="20" align="center">				
				    <input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"	onclick="document.validateDUSelect.button.value='Submit';document.validateDUSelect.submit();"/> &nbsp; 
					<input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"	onclick="document.validateDUSelect.button.value='Cancel';document.validateDUSelect.submit();"/> 
					<input type="hidden" id="button" name="button" value="Submit" /> 
					<input type="hidden" name="formName" value="validateDUSelect.jsp" />
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</td>
			</tr>
		</table>
	</form>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.validateDUSelect.validateOrder.focus();
		}

		function doScan(data, source, type, time, length) {
			document.validateDUSelect.button.value = 'Submit';
			document.validateDUSelect.validateSSCC.value = data;
			document.validateDUSelect.submit();
		}

		function goBack() {
			window.history.back();
		}
	</script>

</body>
</html>


