<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<title>Validate Barcode Error</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body>

	<div class="container">

		<form id="productionConfirmPlusError" name="productionConfirmPlusError" action="Process" method="post">
		
			<h1><%=Lang.getText("web_Barcode_Validate")%></h1>

			<div class="divTable">
			
			
				<div class="divTableBody">
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<label><%=Lang.getText("lbl_Process_Order")%></label>
						</div>
						<div class="divTableCell">
							<label><span style="color:blue">
								<%
								out.println(session.getAttribute("validateOrder"));
								%></span>
							</label>
						</div>
					</div>
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<div></div>
							<label><%=Lang.getText("lbl_Material")%></label>
						</div>
						<div class="divTableCell">
							<label><span style="color:blue">
								<%
								out.println(session.getAttribute("material"));
								%></span>
							</label>
						</div>
					</div>
				</div>
			</div>

			<label><span style="font-size:0.9em;color:blue"><%out.println(session.getAttribute("materialDescription"));%></span></label>
			
			<div class="form-group">
				<br>
			</div>
			
			<div class="divTable">
			
				 <div class="divTableHeading">
				    <div class="divTableRow">
				      <div class="divTableHead"  align="right"><label><span style="text-decoration: underline;">Source</span></label></div>
				      <div class="divTableHead"  align="left"><label><span style="text-decoration: underline;">GTIN</span></label></div>
				      <div class="divTableHead"  align="right"><label><span style="text-decoration: underline;">Variant</span></label></div>
				    </div>
				   <div class="divTableRow">
				      <div class="divTableHead"><label><br></label></div>
				    </div>
				  </div>
			
				<div class="divTableBody">
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<label>Order</label>
						</div>
						<div class="divTableCell">
							<label>
								<span style="color:green"> <%String materialDU_EAN = (String) session.getAttribute("materialDU_EAN");out.println(" " + materialDU_EAN + "");%></span>
							</label>
						</div>
						<div class="divTableCell">
							<label>
							    <span style="color:green"> <%String materialDU_VARIANT = (String) session.getAttribute("materialDU_VARIANT");out.println(" " + materialDU_VARIANT + "");%></span>
							</label>
						</div>
					</div>
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<label>Pallet</label>
						</div>
						<div class="divTableCell">
							<label>
								<span style="color: <%String palletGTINColor = (String) session.getAttribute("palletGTINColor");out.println(" " + palletGTINColor + "");%>"><%String palletGTIN = (String) session.getAttribute("palletGTIN");out.println(" " + palletGTIN + ""); %></span>
							</label>
						</div>
						<div class="divTableCell">
							<label>
								<span style="color: <%String palletVariantColor = (String) session.getAttribute("palletVariantColor");out.println(" " + palletVariantColor + "");%>"><%String palletVariant = (String) session.getAttribute("palletVariant");out.println(" " + palletVariant + ""); %></span>
							</label>
						</div>
					</div>
					<div class="divTableRow">
						<div class="divTableCell" align="right">
							<label>Case/Tray</label>
						</div>
						<div class="divTableCell">
							<label>
								<span style="color: <%String trayGTINColor = (String) session.getAttribute("trayGTINColor");out.println(" " + trayGTINColor + "");%>"><%String trayGTIN = (String) session.getAttribute("trayGTIN");out.println(" " + trayGTIN + ""); %></span>
							</label>
						</div>
						<div class="divTableCell">
							<label>
								<span style="color: <%String trayVariantColor = (String) session.getAttribute("trayVariantColor");out.println(" " + trayVariantColor + "");%>"><%String trayVariant = (String) session.getAttribute("trayVariant");out.println(" " + trayVariant + ""); %></span>
							</label>
						</div>
					</div>
	
				</div>
			</div>

			<div class="form-group">
				<br>
			</div>

			<div class="divTableFoot tableFootStyle">
				<div class="divTableRow">
					<div class="divTableCell"></div>
					<div class="divTableCell"></div>
				</div>
			</div>
			
			<div class="form-group">
				<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
			</div>

			<div class="form-group">
				<br>
			</div>	
			
			<div class="button-group">
				<button type="button" id="exitButton" name="exitButton" class="cancel-btn" onclick="document.productionConfirmPlusError.button.value='Exit';document.productionConfirmPlusError.submit();">Exit</button>
				<input type="hidden" name="formName" value="productionConfirmPlusError.jsp" />
				<input type="hidden" id="button" name="button" value="Submit" />
			</div>

	</form>

	</div>

</body>

</html>
