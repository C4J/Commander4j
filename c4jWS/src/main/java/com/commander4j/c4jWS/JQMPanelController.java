package com.commander4j.c4jWS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMPanelController extends HttpServlet
{

	private static final long serialVersionUID = -5780058810224157434L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPanelController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JQMPanelDB tdb = new JQMPanelDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMPanelEntity> panelList = new LinkedList<JQMPanelEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long panelID = url.getParameterVariableLong(request, "panelID");
		String status = url.getParameterVariable(request, "status");
		Long maxRows = url.getParameterVariableLong(request, "maxRows");

		String reply = "";

		if (status.equals("") == false)
		{
			// If Status specified - use that.

			panelList = tdb.getPanelsByStatus(status);
			reply = gson.toJson(panelList);
		}
		else
		{
		if (maxRows > 0)
		{
			// If Status specified - use that.

			panelList = tdb.getPanelsListLimit(maxRows);
			reply = gson.toJson(panelList);
		}
		else
		{
			// No status specified so check of panelId was provided
			if (panelID > 0)
			{
				// Only PanelId Specified
				panelList.addLast(tdb.getProperties(Long.valueOf(panelID)));
				reply = gson.toJson(panelList);
			}
			else
			{
				// No panelId provided and no status provided so we can't do
				// anything.
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson("Invalid URL - panelId or status invalid");
			}

		}
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		// Update expects the update to be contained within the JSON Body - so
		// the database will be checked for a matching panel id.

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Use GSON to map the fields from the GSON Body to the fields of the
		// Object
		JQMPanelEntity panelEntity = gson.fromJson(bufferedReader, JQMPanelEntity.class);
		// Create an instance of the database handler.
		JQMPanelDB paneldb = new JQMPanelDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";

		// Invoke the update method and pass an instance of the object
		if (paneldb.update(panelEntity))
		{
			// Return the updated record
			reply = gson.toJson(panelEntity);
		}
		else
		{
			// Update method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(paneldb.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Create - note PanelID is auto generated and does not need to be passed
		// to function within PanelEntity

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the JSON Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Decode the body of the request and map the fields to the matching
		// fields of the Object passed to the method.
		JQMPanelEntity panelEntity = gson.fromJson(bufferedReader, JQMPanelEntity.class);

		String reply = "";

		// Create database handler
		JQMPanelDB paneldb = new JQMPanelDB(Common.selectedHostID, request.getSession().getId());
		logger.debug("Invoke create on Panel ID :" + String.valueOf(panelEntity));

		// Invoke the create method and pass an instance of the object
		if (paneldb.create(panelEntity))
		{
			// Return the record created which will include the PanelID which was
			// created dynamically.
			reply = gson.toJson(panelEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(paneldb.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Delete

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		BufferedReader bufferedReader = request.getReader();
		JQMPanelEntity panelEntity = gson.fromJson(bufferedReader, JQMPanelEntity.class);

		JQMPanelDB paneldb = new JQMPanelDB(Common.selectedHostID, request.getSession().getId());
		JURL url = new JURL(request);

		String reply = "";
		
		if (panelEntity == null)
		{
			panelEntity = new JQMPanelEntity();
			panelEntity.setPanelID((long) -1);		
		}
		
		Long panelID = url.getParameterVariableLong(request, "panelID");
		
		if ((panelID > 0))
		{
			panelEntity = new JQMPanelEntity();

			// If panelID and trayID are provided as URL parameters then use
			// those instead of Body
			panelEntity.setPanelID(panelID);
	
		}

		// Invoke the create method and pass an instance of the object
		if (paneldb.delete(panelEntity))
		{
			// Return the record created which will include the PanelID which was
			// created dynamically.
			reply = gson.toJson(panelEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(paneldb.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}
}
