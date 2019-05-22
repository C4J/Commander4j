<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="scanner" Content="disabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<title>Despatch Header</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="despatchHeader" name="despatchHeader" action="Process" method="post">

	<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
			    <th bgcolor="#FFFFFF"><div align="center"><font color="#FF0000"><%=Lang.getText("lbl_Despatch_No")%>&nbsp;<%out.println(session.getAttribute("despatchNo"));%></font></div></th>
			</tr>
	</table>		
		
		<table width="238" align="center">
		
			<tr>
				<td width="35%" height="4"></td>
				<td></td>
				<td width="65%"></td>
			</tr>
			
			<tr>
				<td width="35%" height="20">
					<div align="right"><%=Lang.getText("web_From")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("despatchFromLocationCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%">
					<div align="right"><%=Lang.getText("web_To")%></div>
				</td>
				<td width="4">&nbsp;</td>
				<td width="65%">
					<%
						out.println(session.getAttribute("despatchToLocationCombo"));
					%>
				</td>
			</tr>
			
			<tr>
				<td width="35%"><div align="right"><%=Lang.getText("lbl_Trailer")%></div></td>
				<td>&nbsp;</td>
				<td width="65%"><input name="despatchTrailer" type="text" id="despatchTrailer" tabindex="3" value="<%out.println(session.getAttribute("despatchTrailer"));%>" size="15" maxlength="15"></td>
			</tr>
			
			<tr>
				<td width="35%" height="20"><div align="right"><%=Lang.getText("lbl_Haulier")%></div></td>
				<td>&nbsp;</td>
				<td width="65%"><input name="despatchHaulier" type="text" id="despatchHaulier" tabindex="4" value="<%out.println(session.getAttribute("despatchHaulier"));%>" size="15" maxlength="15"></td>
			</tr>
			<tr>
				<td width="35%" height="20"><div align="right"><%=Lang.getText("lbl_Load_No")%></div></td>
				<td>&nbsp;</td>
				<td width="65%"><input name="despatchLoadNo" type="text" id="despatchLoadNo" tabindex="5" value="<%out.println(session.getAttribute("despatchLoadNo"));%>" size="15" maxlength="15"></td>
			</tr>
			<tr>
				<td width="35%" height="20"><div align="right"><%=Lang.getText("lbl_Journey_Ref")%></div></td>
				<td>&nbsp;</td>
				<td width="65%"><input name="despatchJourneyRef" type="text" id="despatchJourneyRef" tabindex="6" value="<%out.println(session.getAttribute("despatchJourneyRef"));%>" size="15" maxlength="15"></td>
			</tr>
			<tr>
			<tr>
				<td width="35%"><div align="right"><%=Lang.getText("web_Count")%></div></td>
				<td></td>
				<td width="65%">
					<%
						String palletCount = (String) session.getAttribute("despatchPalletCount");
						if (palletCount == null)
							palletCount = "";
						out.println(palletCount);
					%>
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

		<table width="238" align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="100%" height="30" align="center"><input type="submit" tabindex='7' name="buttonAdd" value="<%=Lang.getText("web_Add_SSCC")%>" id="buttonAdd" onclick="document.despatchHeader.button.value='Add Pallets';" />&nbsp; <input
					type="submit" tabindex='8' name="buttonPrint" value="<%=Lang.getText("web_Print_STN")%>" id="buttonPrint" onclick="document.despatchHeader.button.value='Print STN';" /></td>
			</tr>
			<tr>
				<td width="100%" height="30" align="center"><input type="submit" tabindex="9" name="buttonConfirm" value="<%=Lang.getText("web_Confirm_STN")%>" id="buttonConfirm" onclick="document.despatchHeader.button.value='Confirm Despatch';">&nbsp;
					<input type="submit" tabindex="10" name="buttonExit" value="<%=Lang.getText("web_Exit")%>" id="buttonExit" onclick="document.despatchHeader.button.value='Exit';"></td>
			</tr>
		</table>
		
		<input type="hidden" name="formName" value="despatchHeader.jsp" /> <input type="hidden" id="button" name="button" value="Add Pallets" />
	</form>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.despatchHeader.despatchFromLocation.focus();
		}
	</script>

</body>
</html>
