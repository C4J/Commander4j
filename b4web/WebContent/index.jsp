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
<META HTTP-Equiv="quitbutton" Content="hide">
<!-- <META HTTP-Equiv="quitbutton" Content="x=220"> -->
<!-- <META HTTP-Equiv="quitbutton" Content="y=1"> -->
<META HTTP-Equiv="battery" Content="show">
<!-- <META HTTP-Equiv="battery" Content="x=2"> -->
<!-- <META HTTP-Equiv="battery" Content="y=250"> -->
<META HTTP-Equiv="battery" Content="Bottom_GrowFromRight">
<META HTTP-Equiv="battery" Content="RGB:FF,00,00">   
<META HTTP-Equiv="signal" Content="show">
<!-- <META HTTP-Equiv="signal" Content="x=230"> -->
<!-- <META HTTP-Equiv="signal" Content="y=250"> -->
<META HTTP-Equiv="signal" Content="Bottom_GrowFromRight">
<META HTTP-Equiv="signal" Content="RGB:FF,00,00">
<META HTTP-Equiv="unattended" content="enabled">

<title>Welcome</title>
<link href="commander.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style5 {
	font-size: large
}
.style6 {
	color: #FF0000;
	font-size: x-large;
}
.style7 {font-size: x-large}
-->
</style>
</head>
<body  onLoad = "focusIt()">
<form id="index" name="index" action="Process" method="POST">

<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="height: 20px"></td>
	</tr>
	<tr>
		<td width="230" height="20" align="center" style="height: 77px">
		<p align="center" class="style5"><span class="style7">Commander</span><span
			class="style6">4j</span></p>

		</td>
	</tr>
	<tr>
		<td align="center" style="height: 34px">
		Version : 5.28
	</tr>
	<tr>
		<td align="center" style="height: 70px"><input type="submit" name="buttonStart"
			id="buttonStart" value="Start"
			onclick="document.index.button.value='Start';"></td>
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