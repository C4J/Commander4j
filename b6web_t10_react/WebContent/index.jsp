<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
				
		<title>Welcome</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
		
		<%
			session.setAttribute("username","");
		%>
			    
	</head>

	<body onLoad = "focusIt()">
	
		<div class="container">	
		
		<h1><span style="color:blue">Commander</span><span style="color:red">4j</span></h1>
		
		<form id="index" name="index" action="Process" method="post">
			
			<h2>Version : <%=com.commander4j.app.JVersion.getProgramVersion() %></h2>
			
			<div class="form-group">
				<img src="./images/splash.gif" width="100%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px">
			</div>
			
			<div class="form-group">
				<br>
			</div>
			
			<h2>Host : <%=com.commander4j.bean.JServerName.getServername() %></h2>
			
			<div class="form-group">
				<br>
			</div>
			
			<div class="button-group">
				<button type="submit" class="submit-btn" name="buttonStart"	id="buttonStart" value="Start" onclick="document.index.button.value='Start';">Start</button>
			</div>
			
			<input type="hidden" name="formName" value="index.jsp" />
			<input type="hidden" id="button" name="button" value="Start" /> 
						
		</form>
	</div>
		
	<script type="text/javascript">
		
		function focusIt()
		{
			document.index.buttonStart.focus();
		}
						
	</script>
	
	</body>
</html>