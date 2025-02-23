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
	
	<title>Login</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">

	<div class="container">

		<h1><%=(String) session.getAttribute("siteDescription")%></h1>
		
			<br>
			<form id="login" name="login" action="Process" method="post">
			
			<div class="form-group">
				<img src="./images/user-login-icon.gif" width="60" style="display:block; margin-left:auto; margin-right:auto;">
			</div>
			
			<div class="form-group">
				<br>
			</div>			

				<div class="divTable">	  
				  <div class="divTableBody">
				  	<div class="divTableRow">
				      <div class="divTableCell"><label for="username">Username :</label> </div>
				      <%String username = (String) session.getAttribute("username");if (username == null) username = ""; username = username.trim();%>
				      <div class="divTableCell"><input type="text" id="username" name="username" value="<%out.println(username);%>"></div>
				    </div>
				  	<div class="divTableRow">
				      <div class="divTableCell" ><label for="password">Password :</label> </div>
				      <div class="divTableCell"><input type="password" id="password" name="password" value=""></div>
				    </div>
				  </div>
				</div>	
			
				<div class="form-group">
					<br>
				</div>
				
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
	
				<div class="form-group">
					<br>
				</div>
				
				<div class="button-group">
	
					<button type="submit" class="submit-btn" onclick="document.login.button.value='Submit';document.login.submit();">Login</button>
					<button type="button" class="cancel-btn" onclick="document.login.button.value='Cancel';document.login.submit();">Cancel</button>
	
					<input type="hidden" name="selectedAction" value="validateLogon" />
					<input type="hidden" name="formName" value="login.jsp" /> 
					<input type="hidden" id="button" name="button" value="Submit" />
				</div>

			</form>
	</div>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.login.password.value = '';
			document.login.username.focus();
		}
	</script>

</body>

</html>
