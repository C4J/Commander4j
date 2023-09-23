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
			
		<title>Validate DU Barcode</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="validateDUPallet" name="validateDUPallet" action="Process" method="post">
		<h2><%=Lang.getText("web_Barcode_Validate")%></h2>
	
		<table width="100%" align="center">
			<tr>
				<td style="width: 50%; text-align: right"><%=Lang.getText("lbl_Process_Order")%></td>
				<td style="width: 50%; text-align: left">
					<%
						String validateOrder = (String) session.getAttribute("validateOrder");
						out.println(" " + validateOrder + "");
					%>
				</td>
			</tr>
			<tr>
				<td style="width: 50%; text-align: right"><%=Lang.getText("lbl_Material")%></td>
				<td style="width: 50%; text-align: left">
					<%
						String validateMaterial = (String) session.getAttribute("material");
						out.println(" " + validateMaterial + "");
					%>
				</td>
			</tr>
		</table>
		
		<table width="100%" align="center">
			<tr>
				<td style="width: 50%; text-align: left">
					<%
						String materialDescription = (String) session.getAttribute("materialDescription");
						out.println(" " + materialDescription + "");
					%>
				</td>

			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>

		<img src="./images/pallet_du.gif" width="75%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px">
		
		<table width="100%" align="center">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td style="width: 50%; text-align: right"><%=Lang.getText("web_Scan_Barcode")%></td>
				<td style="width: 50%; text-align: left">
					<input tabindex="1" type="text" name="palletDU" id="palletDU" size="20" maxlength="55" value="" />
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
					<input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"onclick="document.validateDUPallet.button.value='Submit';document.validateDUPallet.submit();"> &nbsp; 
					<input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"onclick="document.validateDUPallet.button.value='Cancel';document.validateDUPallet.submit();"> 
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

		function goBack() {
			window.history.back();
		}
	</script>
	
</body>
</html>
