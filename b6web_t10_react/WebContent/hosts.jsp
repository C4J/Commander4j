<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
	
		<title>Host Select</title>
		
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
					
		<%
			session.setAttribute("username","");
		%>
</head>

	<body>
	
		<div class="container">	
		
			<h1>Hosts</h1>
			
			<form id="hosts" name="hosts" action="Process" method="post">
							
	
				<div class="button-group-vertical">
						<%
							String hostList = (String) session.getAttribute("hostList");
							if (hostList == null)
								hostList = "";
							hostList = hostList.trim();
							out.println(hostList);
						%>
				</div>
				
				<br>
				
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
				
				<div class="form-group">
					<br>
				</div>
			
				<div class="button-group">
					<button type="submit" class="submit-btn" onclick="document.hosts.button.value='Submit';document.hosts.submit();">Select</button>
					<button type="button" class="cancel-btn" onclick="document.hosts.button.value='Cancel';document.hosts.submit();">Cancel</button>
					<input type="hidden" name="formName" value="hosts.jsp" /> 
					<input type="hidden" id="button" name="button" value="Submit" /> 					
				</div> 
	
			</form>
			
			<%
				String hostIndexFocus = (String) session.getAttribute("hostIndexFocus");
				out.println(hostIndexFocus);
			%>
			
		</div>
		
	</body>
</html>
