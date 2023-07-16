<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="volume" content="0x1000">
	<meta http-equiv="scanner" Content="disabled">
	<meta http-equiv="ean13" Content="enabled">
	<meta http-equiv="code128-ean128" Content="true">
	<meta http-equiv="code128-maxlength" Content="55">
	<meta http-equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
	<meta http-equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
	<meta http-equiv="Volume" content="SetVolume:0xFFFF">
	<title>Validate Barcode Error</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="productionConfirmPlusError" name="productionConfirmPlusError" action="Process" method="post">

		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("web_Barcode_Validate")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="240px" align="center">
			<tr>
				<td style="width: 50%; text-align: left"><b><%=Lang.getText("lbl_Process_Order")%></b></td>
				<td style="width: 50%; text-align: left"><%String validateOrder = (String) session.getAttribute("validateOrder");out.println(" " + validateOrder + "");%></td>
			</tr>
		</table>
		
		<table width="240px" align="center">
			<tr>
				<td nowrap><%String materialDescription = (String) session.getAttribute("materialDescription");	out.println(" " + materialDescription + "");%></td>
			</tr>
		</table>
		<br>
		<table width="100%" align="center">
			<tr>
				<th></th>
				<td class="small"><b>EAN</b></td>
				<td class="small"><b>Variant</b></td>
			</tr>
			<tr>
				<td><font color="black"><b>Order</b></font></td>
				<td><font color="green"> <%String materialDU_EAN = (String) session.getAttribute("materialDU_EAN");out.println(" " + materialDU_EAN + "");%></font></td>
				<td>&nbsp;&nbsp;<font color="green"> <%String materialDU_VARIANT = (String) session.getAttribute("materialDU_VARIANT");out.println(" " + materialDU_VARIANT + ""); %></font></td>
			</tr>
			<tr>
				<td><font color="black"><b>Pallet</b></font></td>
				<td><font color="<%String palletGTINColor = (String) session.getAttribute("palletGTINColor");out.println(" " + palletGTINColor + "");%>"> <%String palletGTIN = (String) session.getAttribute("palletGTIN");out.println(" " + palletGTIN + ""); %></font></td>
				<td>&nbsp;&nbsp;<font color="<%String palletVariantColor = (String) session.getAttribute("palletVariantColor");out.println(" " + palletVariantColor + "");%>"> <%String palletVariant = (String) session.getAttribute("palletVariant");out.println(" " + palletVariant + ""); %></font></td>
			</tr>
			<tr>
				<td><font color="black"><b>Case/Tray</b></font></td>
				<td><font color="<%String trayGTINColor = (String) session.getAttribute("trayGTINColor");out.println(" " + trayGTINColor + "");%>"> <%String trayGTIN = (String) session.getAttribute("trayGTIN");out.println(" " + trayGTIN + ""); %></font></td>
				<td>&nbsp;&nbsp;<font color="<%String trayVariantColor = (String) session.getAttribute("trayVariantColor");out.println(" " + trayVariantColor + "");%>"> <%	String trayVariant = (String) session.getAttribute("trayVariant"); 	out.println(" " + trayVariant + ""); %>	</font></td>
			</tr>
		</table>

		<img src="./images/invalid.gif" width="40" style="display: block; margin-left: auto; margin-right: auto;">

		<table width="240px" align="center">
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

		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center">
					<input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("btn_Ok")%>"	onclick="document.productionConfirmPlusError.button.value='Submit';document.productionConfirmPlusError .submit();"> 
					<input type="hidden" id="button" name="button" value="Submit" /> 			
					<input type="hidden" name="formName" value="productionConfirmPlusError.jsp" /> 
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</td>
			</tr>
		</table>
	</form>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.productionConfirmPlusError.buttonSubmit.focus();
		}

		function doScan(data, source, type, time, length) {
			document.productionConfirmPlusError.button.value = 'Submit';
			document.productionConfirmPlusError.barcodeData.value = data;
			document.productionConfirmPlusError.barcodeType.value = type;
			document.productionConfirmPlusError.barcodeLength.value = length;
			document.productionConfirmPlusError.submit();
		}

		function goBack() {
			window.history.back();
		}
	</script>
</body>
</html>
