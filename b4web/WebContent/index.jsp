<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="scanner" Content="enabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<META HTTP-Equiv="quitbutton" Content="show">
	<META HTTP-Equiv="quitbutton" Content="x=0">
	<META HTTP-Equiv="quitbutton" Content="y=300">
	<META HTTP-Equiv="battery" Content="show">
	<META HTTP-Equiv="battery" Content="Bottom_GrowFromRight">
	<META HTTP-Equiv="battery" Content="RGB:FF,00,00">   
	<META HTTP-Equiv="signal" Content="show">
	<META HTTP-Equiv="signal" Content="Bottom_GrowFromRight">
	<META HTTP-Equiv="signal" Content="RGB:FF,00,00">
	<META http-equiv="Volume" content="SetVolume:0xFFFF">
	<META HTTP-Equiv="wake" Content="wakeLock:Enabled;wifiLock:Enabled">
	<META HTTP-Equiv="unattended" content="enabled">
	
	<title>Welcome</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<body  onLoad = "focusIt()">
<form id="index" name="index" action="Process" method="POST">

<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">

	<tr>
		<td style="height: 20px"></td>
	</tr>
	<tr>
		<td width="100%" height="20" align="center" style="height: 97px">
		<p align="center"><span class="splashTitleCommander">Commander</span><span
			class="splashTitle4J">4j</span></p>

		</td>
	</tr>
	<tr>
		<td align="center" style="height: 50px" class="splashTitleVersion">
		Version : <%=com.commander4j.app.JVersion.getProgramVersion() %>
	</tr>
	<tr>
		<td align="center" style="height: 50px" class="splashTitleVersion">
		<small>Host : <%=com.commander4j.bean.JServerName.getServername() %></small>
	</tr>
	<tr>
		<td align="center" style="height: 80px">
			<input type="submit" name="buttonStart"	id="buttonStart" value="Start" 	onclick="document.index.button.value='Start';">
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