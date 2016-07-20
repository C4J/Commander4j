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
<title>Session Timeout</title>
<link href="commander.css" rel="stylesheet" type="text/css" />
</head>
<body>
<form id="sessionTimeout" name="sessionTimeout" action="Process" method="post">
<h2>Session Timeout</h2>
Your session has disconnected due to inactivity.
<br><p>
<input type="submit"  tabindex='1' name="buttonRestart" value="Restart" id="buttonRestart" onclick="document.sessionTimeout.button.value='Restart';" />
<input type="hidden" name="formName" value="sessionTimeout.jsp" />
<input type="hidden" id="button" name="button" value="Restart" />
</p>
</form>
</body>
</html>