<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	
	<title>Waste Log</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" 	value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">

	<form id="wasteLog" name="wasteLog" action="Process" method="post">
	
		<div class="container">
		
			<h1><%=Lang.getText("mod_FRM_ADMIN_WASTE_LOG")%></h1>

			<div class="divTable">

				<div class="divTableBody">
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<label><%=Lang.getText("web_Waste_Transaction")%></label>
						</div>
						<div class="divTableCell"><%out.println(session.getAttribute("wasteTransactionCombo"));%></div>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Location")%></label>
					</div>
					<div class="divTableCell"><%out.println(session.getAttribute("wasteLocationCombo"));%></div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Container")%></label>
					</div>
					<div class="divTableCell"><%out.println(session.getAttribute("wasteContainerCombo"));%></div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Material")%></label>
					</div>
					<div class="divTableCell"><%out.println(session.getAttribute("wasteMaterialCombo"));%></div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Reason")%></label>
					</div>
					<div class="divTableCell"><%out.println(session.getAttribute("wasteReasonCombo"));%></div>
				</div>

				<div class="divTableRow">
					<%
					String wasteProcessOrder = (String) session.getAttribute("wasteProcessOrder");
					if (wasteProcessOrder == null)
						wasteProcessOrder = "";
					%>
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Process_Order")%></label>
					</div>
					<div class="divTableCell">
						<input type="text" id="wasteProcessOrder" name="wasteProcessOrder" value="<%out.println(wasteProcessOrder);%>">
					</div>
				</div>

				<div class="divTableRow">
					<%
					String wasteQuantity = (String) session.getAttribute("wasteQuantity");
					if (wasteQuantity == null)
						wasteQuantity = "0";
					wasteQuantity = wasteQuantity.trim();
					%>
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Quantity")%></label>
					</div>
					<div class="divTableCell">
						<input type="text" name="wasteQuantity" id="wasteQuantity" value="<%out.println(wasteQuantity);%>">
					</div>
					<div class="divTableCell" align="left">
						<label><%out.println(session.getAttribute("wasteMaterialUOM"));%></label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_Waste_Scan")%></label>
					</div>
					<div class="divTableCell" align="left">
						<%
						String wasteBarcode = (String) session.getAttribute("wasteBarcode");
						if (wasteBarcode == null)
							wasteBarcode = "";
						wasteBarcode = wasteBarcode.trim();
						%>
						<input type="text" name="wasteBarcode" id="wasteBarcode" value="<%out.println(wasteBarcode);%>">
					</div>
				</div>

			</div>

			<div class="form-group">
				<label style="color: black; background-color: yellow">
					<%
					String errormessage = (String) session.getAttribute("_ErrorMessage");
					if (errormessage == null)
						errormessage = "";
					errormessage = errormessage.trim();
					out.println(errormessage);
					%>
				</label>
			</div>

			<div class="button-group">
				<button type="button" class="submit-btn" onclick="document.wasteLog.button.value='Submit';document.wasteLog.submit();"><%=Lang.getText("web_Submit")%></button>
				<button type="button" class="cancel-btn" onclick="document.wasteLog.button.value='Cancel';document.wasteLog.submit();"><%=Lang.getText("web_Cancel")%></button>
				<input type="hidden" id="button" name="button" value="Submit" /> 
				<input type="hidden" name="formName" value="wasteLog.jsp" /> 
				<input type="hidden" name="barcodeType" value="none" /> 
				<input type="hidden" name="barcodeLength" value="0" />
			</div>

		</div>

	</form>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.wasteLog.wasteBarcode.focus();
		}

		function goBack() {
			window.history.back();
		}
	</script>

</body>

</html>
