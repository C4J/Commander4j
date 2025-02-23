<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<script type="text/javascript" charset="utf-8" src="ebapi-modules.js"></script>	 
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
				
		<title>Welcome</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
			    
	</head>

	<body onLoad = "focusIt()">
	
		<form id="index" name="index" action="Process" method="POST">
		
		<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
		
			<tr>
				<td width="100%" height="20" align="center" style="height: 30px">
				<p align="center"><span class="splashTitleCommander">Commander</span><span
					class="splashTitle4J">4j</span></p>
				</td>
			</tr>
			<tr>
				<td align="center" style="height: 50px" class="splashTitleVersion">
				Version : <%=com.commander4j.app.JVersion.getProgramVersion() %></td>
			</tr>
			<tr>
				<td><img src="./images/splash.gif" width="100%" style="display:block; margin-left:auto; margin-right:auto;max-width:300px">
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="center" style="height: 25px" class="splashTitleVersion">
				<small>Host : <%=com.commander4j.bean.JServerName.getServername() %></small>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="center" style="height: 25px">
					<input type="submit" class="button" name="buttonStart"	id="buttonStart" value="Start" 	onclick="document.index.button.value='Start';">
				</td>
			</tr>
		</table>
		
		<input type="hidden" name="formName" value="index.jsp" />
		<input type="hidden" id="button" name="button" value="Start" /> 
		
		</form> 
	
		 <script type="text/javascript">
		
			function focusIt()
			{
				
				document.index.buttonStart.focus();
			}
						
		</script>
	
	</body>
</html>