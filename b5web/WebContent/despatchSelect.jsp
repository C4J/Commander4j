<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=240"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="expires" content="0">
	<META HTTP-Equiv="scanner" Content="disabled">
	<META HTTP-Equiv="scanner" Content="autoenter">
	<META HTTP-Equiv="acceleratekey" content="all">
	<title>Despatch Select</title>
	<link href="style/commander.css" rel="stylesheet" type="text/css">
</head>

<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage"
	scope="page">
	<jsp:setProperty name="Lang" property="hostID"
		value="<%=session.getAttribute(\"selectedHost\")%>" />
	<jsp:setProperty name="Lang" property="sessionID"
		value="<%=session.getId()%>" />
	<jsp:setProperty name="Lang" property="languageID"
		value="<%=session.getAttribute(\"language\")%>" />
</jsp:useBean>

<body onLoad="focusIt()">
	<form id="despatchSelect" name="despatchSelect" action="Process"
		method="post">


		<table align="center" border="0" cellpadding="0" cellspacing="0" width="238">
			<tr>
				<th bgcolor="#FFFFFF">
					<font color="#FF0000"> 
					<%
					 	String pageTitle = Lang.getText("mod_FRM_ADMIN_DESPATCH");
					 	String currentDespatchListPage = (String) session.getAttribute("currentDespatchListPage");
					 	String maxDespatchPages = (String) session.getAttribute("maxDespatchPages");
					 	if (currentDespatchListPage == null)
					 		currentDespatchListPage = "1";
					 	if (maxDespatchPages == null)
					 		maxDespatchPages = "1";
					 	out.println(pageTitle + " page " + currentDespatchListPage + " of " + maxDespatchPages);
					 %></font>
			    </th>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td width="100%" height="20" align="left">
					<%
						String despatchList = (String) session.getAttribute("despatchList");
						if (despatchList == null)
							despatchList = "";
						despatchList = despatchList.trim();
						out.println(despatchList);
					%>
				</td>
			</tr>

			<tr>

				<td width="238"><div align="center" style="color: green; background-color: #ffff42">
					<%
						String errormessage = (String) session.getAttribute("_ErrorMessage");
						if (errormessage == null)
							errormessage = "";
						errormessage = errormessage.trim();
						out.println(errormessage);
					%>
				</div></td>
			</tr>
		    <tr>
				<td>&nbsp;</td>
			</tr>
			
		</table>

		<table width="238" border="0" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="238" height="20" align="center">
					<input tabindex='1'	type="submit" name="buttonAmend" value="<%=Lang.getText("web_Amend")%>" id="buttonAmend" onclick="document.despatchSelect.button.value='Amend';" />&nbsp; 
					<input tabindex='2' type="submit" name="buttonCreate" value="<%=Lang.getText("web_Create")%>" id="buttonCreate" onclick="document.despatchSelect.button.value='Create';" />&nbsp;
					<input tabindex='3' type="submit" name="buttonExit"	value="<%=Lang.getText("web_Exit")%>" id="buttonExit" onclick="document.despatchSelect.button.value='Exit';" /> 
					<input type="hidden" name="formName" value="despatchSelect.jsp" /> 
					<input type="hidden" id="button" name="button" value="Amend" />
				</td>
			</tr>
			<tr>
				<td width="238" height="20" align="center">
					<input tabindex='4'	type="submit" name="buttonPreviousPage" value="<%=Lang.getText("web_Previous_Page")%>" id="buttonPreviousPage" onclick="document.despatchSelect.button.value='PreviousPage';" />&nbsp;
					<input tabindex='5' type="submit" name="buttonNextPage"	value="<%=Lang.getText("web_Next_Page")%>" id="buttonNextPage" onclick="document.despatchSelect.button.value='NextPage';" />
				</td>
			</tr>
		</table>
	</form>
	<%
		String despatchIndexFocus = (String) session.getAttribute("despatchIndexFocus");
		out.println(despatchIndexFocus);
	%>
</body>
</html>
