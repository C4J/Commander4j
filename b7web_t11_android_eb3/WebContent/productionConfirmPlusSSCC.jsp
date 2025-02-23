<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<script type="text/javascript" charset="utf-8" src="scriptProperties.js"></script>	 
		<script type="text/javascript" charset="utf-8" src="ebapi-modules.js"></script>	 
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<title>Production Confirmation Plus</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="productionConfirmPlusSSCC" name="productionConfirmPlusSSCC" action="Process" method="post">
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("web_Prod_Dec_Confirm")%></font></div></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><img src="./images/pallet_sscc.gif" width="75%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		
		<table width="240px" align="center">
			<tr>
				<td style="width: 30%; text-align: right"><%=Lang.getText("web_SSCC")%>&nbsp;</td>
				<td style="width: 70%; text-align: left">
					<%
						String sscc = (String) session.getAttribute("sscc");
						if (sscc == null)
							sscc = "";
						sscc = sscc.trim();
					%>
					<input tabindex="1" name="sscc" type="text" id="sscc" size="20" maxlength="20" value="<%out.println(sscc);%>"/>
				</td>
			</tr>
		</table>

		<table width="240px" align="center">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width=50%  height="20"><div align="right">Confirmed</div></td>
				<td width=50%  height="20"><div align="left">
					<%
						String confirmCount = (String) session.getAttribute("confirmCount");
						out.println(" " + confirmCount + "");
					%>
				</div></td>
			</tr>
			
			<tr>
				<td>&nbsp;</td>
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
					<input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"	onclick="document.productionConfirmPlusSSCC.button.value='Submit';document.productionConfirmPlusSSCC.submit();">&nbsp; 
					<input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"	onclick="document.productionConfirmPlusSSCC.button.value='Cancel';document.productionConfirmPlusSSCC.submit();"> 
					<input type="hidden" id="button" name="button" value="Submit" /> 
					<input type="hidden" name="formName" value="productionConfirmPlusSSCC.jsp" /> 
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</td>
			</tr>
		</table>
	</form>
	
	<script language="javascript" type="text/javascript">
	
		function focusIt() {
			document.productionConfirmPlusSSCC.sscc.focus();
			initC4JBarcode();
			EB.Barcode.enable(getC4JBarcodeProperties(),scanReceived);
		}

		function goBack() {
			window.history.back();
		}	
		
        function scanReceived(params){

            if (params['data'] != "")
            {
                var data = params['data']
                document.getElementById("sscc").value = data;
                document.productionConfirmPlusSSCC.button.value='Submit';
                document.productionConfirmPlusSSCC.submit();
            }
        }
	</script>
</body>
</html>
