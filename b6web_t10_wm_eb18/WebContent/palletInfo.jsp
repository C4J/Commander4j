<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta name="viewport" content="width=device-width"/>
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
	<title>Pallet Info</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />

</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
    <jsp:setProperty name="Lang" property="hostID" value="<%= session.getAttribute(\"selectedHost\") %>" />
    <jsp:setProperty name="Lang" property="sessionID" value="<%= session.getId() %>" />
    <jsp:setProperty name="Lang" property="languageID" value="<%= session.getAttribute(\"language\") %>" />
</jsp:useBean>

<body onLoad = "focusIt()">
<form id="palletInfo" name="palletInfo" action="Process" method="post">

	<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("mod_FRM_PAL_INFO")%></font></div></th>
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
				<td style="width: 30%; text-align: right"><%=Lang.getText("web_SSCC")%></td>
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
		
		<br>
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
			<td width="100%"  height="20" align="center">
			     <input tabindex='3' type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit") %>" onclick="document.palletInfo.button.value='Submit';document.palletInfo.submit();" />  
			     <input tabindex='4' type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel") %>" onclick="document.palletInfo.button.value='Cancel';document.palletInfo.submit();" />
			     <input type="hidden" id="button" name="button" value="Submit" />
				 <input type="hidden" name="formName" value="palletInfo.jsp" />
				 <input type="hidden" name="barcodeType" value="none" />
			     <input type="hidden" name="barcodeLength" value="0"  />
			</td>
		</tr>
	</table>

</form>
	<script language="javascript" type="text/javascript">

		function focusIt() {
			document.palletInfo.sscc.focus();
		}

		function doScan(data, source, type, time, length) {
			document.palletInfo.button.value = 'Submit';
			document.palletInfo.sscc.value = data;
			document.palletInfo.barcodeType.value = type;
			document.palletInfo.barcodeLength.value = length;
			document.palletInfo.submit();
		}

		function goBack() {
			window.history.back();
		}	
	</script>
</body>
</html>
