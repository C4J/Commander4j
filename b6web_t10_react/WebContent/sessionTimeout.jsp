<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
			
		<title>Session Timeout</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
	</head>
	
	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>
	
	<body>
	
		<div class="container">
	
			<h1>Session Timeout</h1>
			
				<form id="sessionTimeout" name="sessionTimeout" action="Process" method="post">
		
					<br>
			
					<h2>
						Your session has disconnected due to inactivity.
					</h2>
		
					<br>
					
					<div class="button-group">
		
						<button type="submit" class="submit-btn" onclick="document.sessionTimeout.button.value='Restart';">Restart</button>
		
						<input type="hidden" name="selectedAction" value="Restart" />
						<input type="hidden" name="formName" value="sessionTimeout.jsp" /> 
						<input type="hidden" id="button" name="button" value="Restart" />
					</div>
	
				</form>
		</div>
	
	</body>

</html>