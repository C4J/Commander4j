<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>	
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
			
		<title>Validate DU Select</title>
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
			<form id="validateDUSelect" name="validateDUSelect" action="Process" method="post">
				
				<div class="form-group">
					<img src="./images/pallet_sscc.gif" width="50%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px">
				</div>
				
				<div class="form-group">
					<br>
				</div>
				
							
				<div class="divTable">
					<div class="divTableBody">
					  	<div class="divTableRow">
					      	<div class="divTableCell" align="right"><label for="validateOrder">Order</label></div>
					      	<div class="divTableCell">
					      			<%
					      			String validateOrder = (String) session.getAttribute("validateOrder");
									if (validateOrder == null)
										validateOrder = "";
					      			validateOrder = validateOrder.trim();
									%>
									<input type="text" id="validateOrder" name="validateOrder"  value="<%out.println(validateOrder);%>">
					      	</div>
					    </div>
					  	<div class="divTableRow">
					      	<div class="divTableCell" align="right"><label for="validateSSCC">SSCC :</label></div>
					      	<div class="divTableCell">
					      			<%
					      			String validateSSCC = (String) session.getAttribute("validateSSCC");
									if (validateSSCC == null)
										validateSSCC = "";
									validateSSCC = validateSSCC.trim();
									%>
									<input type="text" id="validateSSCC" name="validateSSCC"  value="<%out.println(validateSSCC);%>">
					      	</div>
					    </div>
					</div>
			    </div>
				
				<div class="form-group">
					<br>
				</div>	
						
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
				
				<div class="button-group">
					<button type="submit" class="submit-btn" onclick="document.validateDUSelect.button.value='Submit';document.validateDUSelect.submit();">Submit</button>
					<button type="button" class="cancel-btn"  onclick="document.validateDUSelect.button.value='Cancel';document.validateDUSelect.submit();">Cancel</button>
					<input type="hidden" id="button" name="button" value="Submit" /> 
					<input type="hidden" name="formName" value="validateDUSelect.jsp" /> 
					<input type="hidden" name="barcodeType" value="none" /> 
					<input type="hidden" name="barcodeLength" value="0" />
				</div>
				
			</form>
		</div>
					
		<script language="javascript" type="text/javascript">
		
			function focusIt() 
			{
				document.validateDUSelect.validateSSCC.focus();	
			}
	
			function goBack() 
			{
				window.history.back();
			}
		</script>
		
	</body>

</html>


