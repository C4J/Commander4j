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

		<title>Pallet Information Display</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body>
	<form id="palletInfoDisplay" name="palletInfoDisplay" action="Process" method="post">
		<h2><%=Lang.getText("mod_FRM_PAL_INFO")%></h2>
		
		<table width="100%" align="center">
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("web_SSCC")%></div>
				</td>
				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("sscc"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Process_Order")%></div>
				</td>
				<td width="5%">

				</td>
				<td width="55%" >
					<%
						out.println(session.getAttribute("processOrder"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Material")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("material"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Location_ID")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("location"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Despatch_No")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("despatchNo"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Pallet_Status")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("palletStatus"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Batch")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("batch"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Batch_Status")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("batchStatus"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Pallet_Quantity")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("quantity"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Pallet_UOM")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("uom"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("web_DOM")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("dom"));
					%>
				</td>
			</tr>
			<tr>
				<td width="40%">
					<div align="right"><%=Lang.getText("lbl_Material_Batch_Expiry_Date")%></div>
				</td>

				<td width="5%">

				</td>
				<td width="55%">
					<%
						out.println(session.getAttribute("expiry"));
					%>
				</td>
			</tr>
		</table>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="100%" height="20" align="center">
					<input type="submit" tabindex='8' name="buttonExit" value="<%=Lang.getText("web_Exit")%>" id="buttonExit" onclick="document.palletInfoDisplay.button.value='Exit';" />
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="formName" value="palletInfoDisplay.jsp" /> <input type="hidden" id="button" name="button" value="Exit" />
	</form>
</body>
</html>
