<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
	
		<div class="container">	
			
			<h1><%=Lang.getText("web_Barcode_Validate")%></h1>
			
			<form id="validateDUPallet" name="validateDUPallet" action="Process" method="post">
			
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
				
	
				<label><span style="font-size:0.9em;color:blue"><%out.println(session.getAttribute("materialDescription"));%></span></label>
	
	
				<div class="form-group">
					<br>
				</div>
				
				<div class="form-group">
					<img src="./images/pallet_du.gif" width="50%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px">
				</div>
				
				<div class="form-group">
					<br>
				</div>
				
				<div class="divTable">
					<div class="divTableBody">
						 <div class="divTableRow">
						     <div class="divTableCell" align="right"><label for="palletDU"><%=Lang.getText("web_Scan_Barcode")%></label></div>
						     <div class="divTableCell">
								<input type="text" id="palletDU" name="palletDU"  value="">
						      </div>
						 </div>
					</div>
				</div>				
								
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
	
				<div class="form-group">
					<br>
				</div>	
						
				
				<div class="button-group">
					<button type="submit" class="submit-btn" onclick="document.validateDUPallet.button.value='Submit';document.validateDUPallet.submit();">Submit</button>
					<button type="button" class="cancel-btn"  onclick="document.validateDUPallet.button.value='Cancel';document.validateDUPallet.submit();">Cancel</button>
					<input type="hidden" id="button" name="button" value="Submit" /> 
					<input type="hidden" name="formName" value="validateDUPallet.jsp" /> 
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</div>
				
			</form>
		</div>
					
		<script language="javascript" type="text/javascript">
		
			function focusIt() 
			{
				document.validateDUPallet.palletDU.focus();	
			}
	
			function goBack() 
			{
				window.history.back();
			}	
		</script>
	</body>

</html>
