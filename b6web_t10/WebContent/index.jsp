<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		    
	    <meta name="viewport" content="width=device-width, initial-scale=1"/>
	    
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<meta http-equiv="scanner" Content="disabled">
		<meta http-equiv="scanner" Content="autoenter">
		
		<meta http-equiv="acceleratekey" content="all">
		<meta http-equiv="Volume" content="SetVolume:0xFFFF">
		<meta http-equiv="wake" Content="wakeLock:Enabled;wifiLock:Enabled">
		<meta http-equiv="unattended" content="enabled">

	    
	    <meta http-equiv="KeyState" Content="Visibility:Visible"/> 
	    <meta http-equiv="KeyState" Content="top:605">
	    
	    <meta http-equiv="HomeButton" Content="visibility:visible">
		<meta http-equiv="HomeButton" Content="left:2">
		<meta http-equiv="HomeButton" Content="top:605">
		<meta http-equiv="HomeButton" Content="color:#0078B4">
		
		<meta http-equiv="battery" Content="visibility:visible">
		<meta http-equiv="battery" Content="left:40">
		<meta http-equiv="battery" Content="top:615">
		<meta http-equiv="battery" Content="color:#0078B4">
		 
		<meta http-equiv="Signal" Content="visibility:visible">
		<meta http-equiv="Signal" Content="left:80">
		<meta http-equiv="Signal" Content="top:615">
		<meta http-equiv="Signal" Content="color:#0078B4">
		    
	    <meta http-equiv="QuitButton" Content="visibility:visible">
		<meta http-equiv="QuitButton" Content="left:114">
		<meta http-equiv="QuitButton" Content="top:605">
		<meta http-equiv="QuitButton" Content="color:#0078B4">

		
		<title>Welcome</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
			    
	</head>

	<body  onLoad = "focusIt()">
	
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