<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta charset="UTF-8">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<title>Despatch Header</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
	</head>

	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>

	<body onLoad="focusIt()">
		<form id="printerSelect" name="printerSelect" action="Process" method="post">
	
			<div class="container">
				<h1><%=Lang.getText("mod_FRM_CM_PRINTERS")%></h1>
				
				<div class="form-group">
				   <br>
			    </div>
				
				<div class="divTable">
				  
				  <div class="divTableBody">
				  	<div class="divTableRow">
				  	<div class="divTableCell"><label>Queue</label></div>
				      <div class="divTableCell"><label><%out.println(session.getAttribute("printerList"));%></label></div>
				    </div>
			     </div>
				</div>
				
				<div class="form-group">
					<br><br><br><br><br>
				</div>	
				
				<div class="button-group">
					<button type="submit" class="submit-btn" onclick="document.printerSelect.button.value='Submit';document.printerSelect.submit();">Select</button>
					<button type="button" class="cancel-btn"  onclick="document.printerSelect.button.value='Cancel';document.printerSelect.submit();">Cancel</button>
					<input type="hidden" name="formName" value="printerSelect.jsp" /> 
					<input type="hidden" id="button" name="button" value="Select" />				
			
				</div> 
			</div>
		</form>
		
		<script>
			function focusIt() 
			{
				document.despatchHeader.printerSelect.focus();
			}
		</script>

	</body>
</html>
