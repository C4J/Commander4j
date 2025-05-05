<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<title>Pallet Information Display</title>
<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage"
	scope="page">
	<jsp:setProperty name="Lang" property="hostID"
		value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID"
		value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID"
		value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body>

	<div class="container">

		<form id="palletInfoDisplay" name="palletInfoDisplay" action="Process" method="post">
		
			<h1><%=Lang.getText("mod_FRM_PAL_INFO")%></h1>

			<div class="divTableBody">
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_SSCC")%></label>
					</div>
					<div class="divTableCell" >
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("sscc"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Process_Order")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("processOrder"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
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
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Location_ID")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("location"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Despatch_No")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("despatchNo"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Pallet_Status")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("palletStatus"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Batch")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("batch"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Batch_Status")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("batchStatus"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Pallet_Quantity")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("quantity"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Pallet_UOM")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("uom"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("web_DOM")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("dom"));
							%></span>
						</label>
					</div>
				</div>
				<div class="divTableRow">
					<div class="divTableCell" align="right">
						<label><%=Lang.getText("lbl_Material_Batch_Expiry_Date")%></label>
					</div>
					<div class="divTableCell">
						<label><span style="color:blue">
							<%
							out.println(session.getAttribute("expiry"));
							%></span>
						</label>
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
			
			<div class="button-group">
				<button type="button" id="exitButton" name="exitButton" class="cancel-btn" onclick="document.palletInfoDisplay.button.value='Exit';document.palletInfoDisplay.submit();">Exit</button>
				<input type="hidden" name="formName" value="palletInfoDisplay.jsp" />
				<input type="hidden" id="button" name="button" value="Submit" />
			</div>

	</form>

	</div>

</body>

</html>
