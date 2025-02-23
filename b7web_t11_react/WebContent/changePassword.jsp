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
	
	<title>Change Password</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
	<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">

	<div class="container">

		<h1>Change Password</h1>
		<h2><%=(String) session.getAttribute("siteDescription")%></h2>
			<br>
			<form id="changePassword" name="changePassword" action="Process" method="post">
					
				<div class="divTable">	  
				  <div class="divTableBody">
				  	<div class="divTableRow">
				      <div class="divTableCell"><label for="username">Username :</label> </div>
				      <%String username = (String) session.getAttribute("username");if (username == null) username = ""; username = username.trim();%>
				      <div class="divTableCell"><input type="text"	id="username" name="username" readonly value="<%out.println(username);%>"></div>
				    </div>
				  	<div class="divTableRow">
				      <div class="divTableCell" ><label for="password">Current :</label> </div>
				      <div class="divTableCell"><input type="password"	id="password" name="password" value=""></div>
				    </div>	    
				    <div class="divTableRow">
				      <div class="divTableCell"><label for="newPassword1">New :</label></div>
				      <div class="divTableCell"><input type="password" id="newPassword1" name="newPassword1" value=""></div>
				    </div>
				    <div class="divTableRow">
				      <div class="divTableCell"><label for="newPassword2">Verify :</label> </div>
				      <div class="divTableCell"><input type="password" id="newPassword2" name="newPassword2" value=""></div>
				    </div>
				  </div>
				</div>					
						
			<div class="form-group">
				<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
			</div>

			<div class="form-group">
				<br>
			</div>
			
			<div class="button-group">

				<button type="submit" class="submit-btn" onclick="document.changePassword.button.value='Submit';document.changePassword.submit();"><%=Lang.getText("web_Submit")%></button>
				<button type="button" class="cancel-btn" onclick="document.changePassword.button.value='Cancel';document.changePassword.submit();"><%=Lang.getText("web_Cancel")%></button>

				<input type="hidden" name="selectedAction" value="Submit" />
				<input type="hidden" name="formName" value="changePassword.jsp" /> 
				<input type="hidden" id="button" name="button" value="Submit" />
			</div>

		</form>

	</div>

	<script language="javascript" type="text/javascript">
		function focusIt() {
			document.changePassword.password.focus();
		}
	</script>

</body>

</html>
