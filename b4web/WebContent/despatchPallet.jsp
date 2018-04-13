<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="expires" content="0">
<META http-equiv="volume" content="0x1000">
<META HTTP-Equiv="scanner" Content="enabled">
<META HTTP-Equiv="ean13" Content="enabled">
<META HTTP-Equiv="code128-ean128" Content="true">
<META HTTP-Equiv="code128-maxlength" Content="20">
<META HTTP-Equiv="scannernavigate" Content="Javascript:doScan('%s', '%s', %s, '%s', %s);">
<META HTTP-Equiv="scanner" Content="DecodeEvent:url('javascript:doScan('%s', '%s', %s, '%s', %s);')">
<META http-equiv="Volume" content="SetVolume:0xFFFF">
<META HTTP-Equiv="SIP" Content="Manual">
<META HTTP-Equiv="SIP" Content="Left:640;Top:0">
<META HTTP-Equiv="acceleratekey" content="all">

<title>Despatch Pallet</title>
<link href="style/commander.css" rel="stylesheet" type="text/css">
<%
	String despatchNo = (String) session.getAttribute("despatchNo");
	despatchNo = despatchNo.trim();
%>
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="despatchPallet" name="despatchPallet" action="Process" method="post">
		<h2><%=Lang.getText("lbl_Despatch_No")%>&nbsp;<%
			out.println(despatchNo);
		%>
		</h2>
		<br>
		<img src="./images/pallet_sscc.gif" width="150" style="display:block; margin-left:auto; margin-right:auto;">
		<br>
		<table style="width:100%;" align="center">
			<tr>
				<td style="width: 30%; text-align: right"><%=Lang.getText("web_SSCC")%></td>
				<td style="width: 70%; text-align: left">
					<%
						String sscc = (String) session.getAttribute("sscc");
						if (sscc == null)
							sscc = "";
						sscc = sscc.trim();
						out.println(
								"<input tabindex=\"1\" name=\"sscc\" type=\"text\" id=\"sscc\" size=\"20\" maxlength=\"20\" value=\""
										+ sscc + "\"/>");
					%>
				</td>
			</tr>
		</table>
		<table style="width: 100%" border="0">
			<tr>
				<td style="width: 50%; text-align: right"><label><input name="addRemoveMode" accesskey="A" type="radio" id="radio" value="add" checked="checked" onClick="focusIt()" /><%=Lang.getText("web_Add")%></label></td>
				<td style="width: 50%; text-align: left"><label><input name="addRemoveMode" accesskey="R" type="radio" id="radio" value="remove" onClick="focusIt()" /><%=Lang.getText("web_Remove")%></label></td>

			</tr>
			<tr>
		 	    <td style="width: 50%"></td>
				<td style="width: 50%"></td>  
		   </tr>			
			<tr>
		 	    <td style="width: 50%; text-align: right">Count :</td>
				<td style="width: 50%; text-align: left">
				 <%
					String palletCount = (String) session.getAttribute("despatchPalletCount");
					if (palletCount == null)
						palletCount = "";
					out.println(palletCount);
				%>
				</td>
			</tr>
		</table>
		<table style="width: 100%">
			<tr>
				<td style="width: 100%; text-align: left">
					<%
						String errormessage = (String) session.getAttribute("_ErrorMessage");
						if (errormessage == null)
							errormessage = "";
						errormessage = errormessage.trim();
						out.println(errormessage);
					%>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center"><input tabindex="3" type="button" name="buttonSubmit" id="buttonSubmit" value="<%=Lang.getText("web_Submit")%>"
					onclick="document.despatchPallet.button.value='Submit';document.despatchPallet.submit();">&nbsp; <input tabindex="4" type="button" name="buttonCancel" id="buttonCancel" value="<%=Lang.getText("web_Cancel")%>"
					onclick="document.despatchPallet.button.value='Cancel';document.despatchPallet.submit();"> <input type="hidden" id="button" name="button" value="Submit" /> <input type="hidden" name="formName" value="despatchPallet.jsp" /> <input
					type="hidden" name="barcodeType" value="none" /> <input type="hidden" name="barcodeLength" value="0" /></td>
			</tr>
		</table>

	</form>
	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.despatchPallet.sscc.focus();
		}

		function doScan(data, source, type, time, length) {
			document.despatchPallet.button.value = 'Submit';
			document.despatchPallet.sscc.value = data;
			document.despatchPallet.barcodeType.value = type;
			document.despatchPallet.barcodeLength.value = length;
			document.despatchPallet.submit();
		}

		function goBack() {
			window.history.back();
		}
	</script>
</body>
</html>
