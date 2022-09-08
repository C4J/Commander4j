<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="volume" content="0x1000">
	<meta http-equiv="scanner" Content="enabled">
	<meta http-equiv="ean13" Content="enabled">
	<meta http-equiv="code128-ean128" Content="true">
	<meta http-equiv="code128-maxlength" Content="20">
	<meta http-equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
	<meta http-equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
	<meta http-equiv="Volume" content="SetVolume:0xFFFF">
	<meta http-equiv="SIP" Content="Manual">
	<meta http-equiv="SIP" Content="Left:640;Top:0">
	<title>Waste Log</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="wasteLog" name="wasteLog" action="Process" method="post">
	
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("mod_FRM_ADMIN_WASTE_LOG")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
	   </table>		
		
		
		<table width="100%" align="center">
		
			<tr>
				<td width="35%" height="20">
					<div align="right"><%=Lang.getText("web_Waste_Transaction")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("wasteTransactionCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%" height="20">
					<div align="right"><%=Lang.getText("web_Waste_Location")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("wasteLocationCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%" height="20">
					<div align="right"><%=Lang.getText("web_Waste_Container")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("wasteContainerCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%">
					<div align="right"><%=Lang.getText("web_Waste_Material")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="45%">
					<%
						out.println(session.getAttribute("wasteMaterialCombo"));
					%>
				</td>

			</tr>
			
			<tr>
				<td width="35%">
					<div align="right"><%=Lang.getText("web_Waste_Reason")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("wasteReasonCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%"><div align="right"><%=Lang.getText("web_Waste_Process_Order")%></div></td>
				<td>&nbsp;</td>
					<%
						String wasteProcessOrder = (String) session.getAttribute("wasteProcessOrder");
						if (wasteProcessOrder == null)
							wasteProcessOrder = "";
						wasteProcessOrder = wasteProcessOrder.trim();
					%>
				<td width="65%"><input name="wasteProcessOrder" type="text" id="wasteProcessOrder" tabindex="3" value="<%out.println(session.getAttribute("wasteProcessOrder"));%>" size="15" maxlength="15"></td>
			</tr>
			
		</table>
		
		<table width="100%" align="center">
		<tr>
				<td width="35%" height="20"><div align="right"><%=Lang.getText("web_Waste_Quantity")%></div></td>
				<td>&nbsp;</td>
					<%
						String wasteQuantity = (String) session.getAttribute("wasteQuantity");
						if (wasteQuantity == null)
							wasteQuantity = "0";
						wasteQuantity = wasteQuantity.trim();
					%>
				<td width="45%"><input name="wasteQuantity" type="text" id="wasteQuantity" style="text-align: right" tabindex="4" value="<%out.println(session.getAttribute("wasteQuantity"));%>" size="12" maxlength="12"></td>
				
				<td width="20%" height="20"><div align="left">
					<%
						out.println(session.getAttribute("wasteMaterialUOM"));
					%>
				</div></td>
			</tr>
		</table>
		

		<table width="100%" align="center">
			<tr>
				<td width="35%" height="20"><div align="right"><%=Lang.getText("web_Waste_Scan")%></div></td>
				<td>&nbsp;</td>
				<%
					String wasteBarcode = (String) session.getAttribute("wasteBarcode");
					if (wasteBarcode == null)
							wasteBarcode = "";
					wasteBarcode = wasteBarcode.trim();
				%>
				<td style="width: 65%; text-align: left">
					<input tabindex="1" name="wasteBarcode" type="text" id="wasteBarcode" size="16" maxlength="35" value="<%out.println(wasteBarcode);%>"/>
				</td>
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

		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center">
				    <input tabindex="2" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"onclick="document.wasteLog.button.value='Submit';document.wasteLog.submit();"/>&nbsp; 
					<input tabindex="3" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"onclick="document.wasteLog.button.value='Cancel';document.wasteLog.submit();"/> 
					<input type="hidden" id="button" name="button" value="Submit" /> 
					<input type="hidden" name="formName" value="wasteLog.jsp" /> 
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</td>
			</tr>
		</table>

	</form>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.wasteLog.wasteBarcode.focus();
		}

		function doScan(data, source, type, time, length) {
			document.wasteLog.button.value = 'Submit';
			document.wasteLog.wasteBarcode.value = data;
			document.wasteLog.barcodeType.value = type;
			document.wasteLog.barcodeLength.value = length;
			document.wasteLog.submit();
		}

		function goBack() {
			window.history.back();
		}
	</script>

</body>
</html>
