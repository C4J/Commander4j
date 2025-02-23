<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<title>Confirm Despatch</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
	</head>
	
	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
			<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
			<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
			<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>
	
	<body>
	
		<div class="container">	
		
			<h1><%=Lang.getText("dlg_Despatch_Confirm")%><%out.println(session.getAttribute("despatchNo"));%></h1>
			
			<form id="despatchConfirm" name="despatchConfirm" action="Process" method="post">
							
				<div class="form-group">
					<br>
				</div>
				
				<div class="form-group">
					<br>
				</div>	
				
				<div align="center">
					<label for="printSTNonConfirm"><%=Lang.getText("web_Print_STN")%></label>
					<input type="checkbox" name="printSTNonConfirm" id="printSTNonConfirm"/>
				</div>
				
				<div class="form-group">
					<br>
				</div>				
				
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
									
				<div class="button-group">
					<button type="button" class="submit-btn" onclick="document.despatchConfirm.button.value='Yes';document.despatchConfirm.submit();"><%=Lang.getText("web_Yes")%></button>
					<button type="button" class="cancel-btn" onclick="document.despatchConfirm.button.value='No';document.despatchConfirm.submit();"><%=Lang.getText("web_No")%></button>
				    <input type="hidden" name="formName" value="despatchConfirm.jsp" /> 
					<input type="hidden" id="button" name="button" value="Yes" />				
				</div> 
				
			</form>
			
		</div>
		
	</body>
	
</html>
