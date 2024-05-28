<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		
		<title>Despatch Header</title>
		<link href="style/commander.css" rel="stylesheet" type="text/css">
	</head>

	<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">
		<jsp:setProperty name="Lang" property="hostID" value="<%=session.getAttribute(\"selectedHost\")%>" />
		<jsp:setProperty name="Lang" property="sessionID" value="<%=session.getId()%>" />
		<jsp:setProperty name="Lang" property="languageID" value="<%=session.getAttribute(\"language\")%>" />
	</jsp:useBean>

	<body onLoad="focusIt()">
		<form id="despatchHeader" name="despatchHeader" action="Process" method="post">
	
			<div class="container">
				<h1><%=Lang.getText("lbl_Despatch_No")%>&nbsp;<%out.println(session.getAttribute("despatchNo"));%></h1>
				
				<div class="divTable">
				  <div class="divTableHeading">
				    <div class="divTableRow">
				      <div class="divTableHead"></div>
				      <div class="divTableHead"></div>
				    </div>
				  </div>
				  
				  <div class="divTableBody">
				  	<div class="divTableRow">
				      <div class="divTableCell" align="right"><label><%=Lang.getText("web_From")%></label></div>
				      <div class="divTableCell"><%out.println(session.getAttribute("despatchFromLocationCombo"));%></div></div>
				    </div>
				  	<div class="divTableRow">
				      <div class="divTableCell" align="right" ><label for="despatchToLocation"><%=Lang.getText("web_To")%></label></div>
				      <div class="divTableCell"><%out.println(session.getAttribute("despatchToLocationCombo"));%></div>
				    </div>	    
				    <div class="divTableRow">
				      <div class="divTableCell" align="right"><label for="despatchTrailer"><%=Lang.getText("lbl_Trailer")%></label></div>
				      <div class="divTableCell"><input type="text" id="despatchTrailer" name="despatchTrailer" value="<%out.println(session.getAttribute("despatchTrailer"));%>"></div>
				    </div>
				    <div class="divTableRow">
				      <div class="divTableCell" align="right"><label for="despatchHaulier"><%=Lang.getText("lbl_Haulier")%></label></div>
				      <div class="divTableCell"><input type="text" id="despatchHaulier" name="despatchHaulier" value="<%out.println(session.getAttribute("despatchHaulier"));%>"></div>
				    </div>
				    <div class="divTableRow">
				      <div class="divTableCell" align="right"><label for="despatchLoadNo"><%=Lang.getText("lbl_Load_No")%></label></div>
				      <div class="divTableCell"><input type="text" id="despatchLoadNo" name="despatchLoadNo" value="<%out.println(session.getAttribute("despatchLoadNo"));%>"></div>
				    </div>
				    <div class="divTableRow">
				      <div class="divTableCell" align="right"><label for="despatchJourneyRef"><%=Lang.getText("lbl_Journey_Ref")%></label></div>
				      <div class="divTableCell"><input type="text" id="despatchJourneyRef" name="despatchJourneyRef" value="<%out.println(session.getAttribute("despatchJourneyRef"));%>"></div>
				    </div>
				    <div class="divTableRow">
				      <div class="divTableCell" align="right"></div>
				      		<%String counter = Lang.getText("lbl_Assigned") + " " + (String) session.getAttribute("despatchPalletCount"); %>
						<div class="divTableCell" align="center"><label><%out.println(counter);%></label></div>
				    </div>
				  </div>
			
				<div class="form-group">
					<label style="color:black;  background-color:yellow"><%String errormessage = (String) session.getAttribute("_ErrorMessage");if (errormessage == null) errormessage = "";errormessage = errormessage.trim();out.println(errormessage);%></label>
				</div>
				
				<div class="button-group">
					<button type="button" class="regular-btn" onclick="document.despatchHeader.button.value='Add Pallets';document.despatchHeader.submit();"><%=Lang.getText("web_Add_SSCC")%></button>
					<button type="button" class="regular-btn" onclick="document.despatchHeader.button.value='Print STN';document.despatchHeader.submit();"><%=Lang.getText("web_Print_STN")%></button>
					<input type="hidden" name="formName" value="despatchHeader.jsp" /> 
					<input type="hidden" id="button" name="button" value="Add Pallets" />				
				</div> 
				<div class="button-group">		
					<button type="button" class="submit-btn" onclick="document.despatchHeader.button.value='Confirm Despatch';document.despatchHeader.submit();"><%=Lang.getText("web_Confirm_STN")%></button>
					<button type="button" class="cancel-btn" onclick="document.despatchHeader.button.value='Exit';document.despatchHeader.submit();"><%=Lang.getText("web_Exit")%></button>			
				</div> 
				  
			</div>
				

		</form>
		
		<script language="javascript" type="text/javascript">
			function focusIt() 
			{
				document.despatchHeader.despatchFromLocation.focus();
			}
		</script>

	</body>
</html>
