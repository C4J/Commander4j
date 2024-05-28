<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
			<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
			<meta http-equiv="Pragma" content="no-cache" />
			<meta http-equiv="Expires" content="0" />
			
			<title>Despatch Select</title>
			<link href="style/commander.css" rel="stylesheet" type="text/css">
	</head>

	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage"
			scope="page">
			<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
			<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
			<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>

	<body>
	
		<div class="container">	
		
			<h1>
			<%
				String pageTitle = Lang.getText("mod_FRM_ADMIN_DESPATCH");
					 	String currentDespatchListPage = (String) session.getAttribute("currentDespatchListPage");
					 	String maxDespatchPages = (String) session.getAttribute("maxDespatchPages");
					 	if (currentDespatchListPage == null)
					 		currentDespatchListPage = "1";
					 	if (maxDespatchPages == null)
					 		maxDespatchPages = "1";
					 	out.println(pageTitle + " page " + currentDespatchListPage + " of " + maxDespatchPages);
					 %></h1>
			
			<form id="despatchSelect" name="despatchSelect" action="Process" method="post">
							
				
				<div class="button-group-vertical">
					<%
						String despatchList = (String) session.getAttribute("despatchList");
						if (despatchList == null)
							despatchList = "";
						despatchList = despatchList.trim();
						out.println(despatchList);
					 %>
				</div>
						
				<div class="form-group">
					<br>
				</div>	
				
				<div class="button-group">
					<button type="button" class="regular-btn" onclick="document.despatchSelect.button.value='PreviousPage';document.despatchSelect.submit();"><%=Lang.getText("web_Previous_Page")%></button>
					<button type="button" class="regular-btn" onclick="document.despatchSelect.button.value='NextPage';document.despatchSelect.submit();"><%=Lang.getText("web_Next_Page")%></button>				
				</div> 				
											
				<div class="button-group">
					<button type="button" class="submit-btn" onclick="document.despatchSelect.button.value='Create';document.despatchSelect.submit();"><%=Lang.getText("web_Create")%></button>				
					<button type="button" class="submit-btn" onclick="document.despatchSelect.button.value='Amend';document.despatchSelect.submit();"><%=Lang.getText("web_Amend")%></button>
					<button type="button" class="cancel-btn" onclick="document.despatchSelect.button.value='Exit';document.despatchSelect.submit();"><%=Lang.getText("web_Exit")%></button>
				    <input type="hidden" name="formName" value="despatchSelect.jsp" /> 
					<input type="hidden" id="button" name="button" value="Submit" />				
				</div> 
				

				
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
	
			</form>
			
			<%
				String despatchIndexFocus = (String) session.getAttribute("despatchIndexFocus");
				out.println(despatchIndexFocus);
			%>
			
		</div>
		
	</body>

</html>
