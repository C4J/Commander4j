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
		
		<title>System Information</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
		
		<script>
			var userAgent = navigator.userAgent;
			
			var identified = 0;
			var friendlyName = "";
			var script = "";
			
			var script = document.createElement('script');
	
		
			if (userAgent.indexOf("Firefox") > -1) 
			{
				identified = 1;
				script.src = 'firefox-script.js';
				friendlyName = "Firefox";
			} 
			
			if (userAgent.indexOf("Chrome") > -1) 
			{
				identified = 1;
				script.src = 'chrome-script.js';
				friendlyName = "Chrome";
			} 
			
			if (userAgent.indexOf("Safari") > -1) 
			{
				identified = 1;
				script.src = 'chrome-script.js';
				friendlyName = "Safari";
			} 
			
			if (identified == 0)
			{
				friendlyName = "Unknown";
				script.src = 'default-script.js';
			}
			
			document.head.appendChild(script);
			
		</script>
		
	</head>
	
	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>

	<body>
		<div class="container">	
			<form id="sysInfo" name="sysInfo" action="Process" method="post">
				<label id="browsername"></label>
				<p id="agentname"></p>
				<p id="dimensions"></p>
				<label><span style="font-size: 100%">100%</span></label><br>
				<label><span style="font-size: 100%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 100%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 90%">90%</span></label><br>
				<label><span style="font-size: 90%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 90%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 80%">80%</span></label><br>
				<label><span style="font-size: 80%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 80%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 70%">70%</span></label><br>
				<label><span style="font-size: 70%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 70%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 60%">60%</span></label><br>
				<label><span style="font-size: 60%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 60%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 50%">50%</span></label><br>
				<label><span style="font-size: 50%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 50%">1234567890123456789012345678901234567890</span></label><br>
				<label><span style="font-size: 40%">40%</span></label><br>
				<label><span style="font-size: 40%">.........1.........2.........3.........4</span></label><br>
				<label><span style="font-size: 40%">1234567890123456789012345678901234567890</span></label><br>
				
				<div class="button-group">
					<button type="submit" class="cancel-btn" onclick="document.sysInfo.button.value='Exit';document.sysInfo.submit();">Exit</button>
					<input type="hidden" id="button" name="button" value="Exit" /> 
					<input type="hidden" name="formName" value="sysInfo.jsp" /> 
				</div>
			</form>
		</div>
			
		<script>
			var w = window.innerWidth;
			var h = window.innerHeight;
					
			var x = document.getElementById("dimensions");
			x.innerHTML = "Screen width: " + w + ", height: " + h + ".";
					
			var y = document.getElementById("browsername");
			y.innerHTML = "Browser : " + friendlyName;
			
			var z = document.getElementById("agentname");
			z.innerHTML = "Agent : " + userAgent;
		</script>
	</body>
</html>