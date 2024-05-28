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
		
		<title>Menu</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css" />
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage"
		scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

	<body>
	
		<div class="container">	
		
			<h1><%=Lang.getText("mod_root")%></h1>
			
			<form id="menus" name="menus" action="Process" method="post">
							
	
				<div class="button-group-vertical">
					<%
						String menuList = (String) session.getAttribute("menuList");
						if (menuList == null)
							menuList = "";
						menuList = menuList.trim();
						out.println(menuList);
					%>
				</div>
								
				<div class="button-group">
					<button type="submit" class="submit-btn" onclick="document.menus.button.value='Submit';document.menus.submit();">Select</button>
					<button type="button" class="cancel-btn" onclick="document.menus.button.value='Cancel';document.menus.submit();">Exit</button>
				    <input type="hidden" name="formName" value="menu.jsp" /> 
					<input type="hidden" id="button" name="button" value="Submit" />				
				</div> 
	
			</form>
			
			<%
				String menuIndexFocus = (String) session.getAttribute("menuIndexFocus");
				out.println(menuIndexFocus);
			%>
			
		</div>
		
	</body>


</html>
