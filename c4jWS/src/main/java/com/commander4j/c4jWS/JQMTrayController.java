package com.commander4j.c4jWS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;

import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMTrayController extends HttpServlet
{

	private static final long serialVersionUID = -9013610816396593339L;
	private Logger logger = Logger.getLogger(JQMTrayController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JQMTrayDB tdb = new JQMTrayDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMTrayEntity> trayList = new LinkedList<JQMTrayEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long panelID = url.getParameterVariableLong(request, "panelID");
		Long trayID = url.getParameterVariableLong(request, "trayID");

		String reply = "";

		// No status specified so check of panelId was provided
		if (panelID > 0)
		{
			if (trayID > 0)
			{
				// Only PanelId and TrayID Specified
				trayList.addLast(tdb.getProperties(Long.valueOf(panelID), Long.valueOf(trayID)));
				reply = gson.toJson(trayList);
			}
			else
			{
				// Only PanelId Specified so return all matching tray
				// records.
				trayList = tdb.getTraysByPanel(Long.valueOf(panelID));
				reply = gson.toJson(trayList);
			}
		}
		else
		{
			// No panelId provided and no status provided so we can't do
			// anything.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - panelID invalid");
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
		// the database will be checked for a matching panel and tray id.

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Use GSON to map the fields from the GSON Body to the fields of the
		// Object
		JQMTrayEntity trayEntity = gson.fromJson(bufferedReader, JQMTrayEntity.class);
		// Create an instance of the database handler.
		JQMTrayDB trayDB = new JQMTrayDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";

		// Invoke the update method and pass an instance of the object
		if (trayDB.update(trayEntity))
		{
			// Return the updated record
			reply = gson.toJson(trayEntity);
		}
		else
		{
			// Update method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(trayDB.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Create - not TrayID is auto generated and does not need to be passed
		// to function within TrayEntity

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the JSON Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Decode the body of the request and map the fields to the matching
		// fields of the Object passed to the method.
		JQMTrayEntity trayEntity = gson.fromJson(bufferedReader, JQMTrayEntity.class);

		String reply = "";

		// Create database handler
		JQMTrayDB trayDB = new JQMTrayDB(Common.selectedHostID, request.getSession().getId());
		logger.debug("Invoke create on Tray ID :" + String.valueOf(trayEntity));

		// Invoke the create method and pass an instance of the object
		if (trayDB.create(trayEntity))
		{
			// Return the record created which will include the TrayID which was
			// created dynamically.
			reply = gson.toJson(trayEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(trayDB.getErrorMessage());
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

		JQMTrayEntity trayEntity = gson.fromJson(bufferedReader, JQMTrayEntity.class);

		JQMTrayDB trayDB = new JQMTrayDB(Common.selectedHostID, request.getSession().getId());
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter

		if (trayEntity == null)
		{
			trayEntity = new JQMTrayEntity();
			trayEntity.setPanelID((long) -1);
			trayEntity.setTrayID((long) -1);
		}

		Long panelID = url.getParameterVariableLong(request, "panelID");

		Long trayID = url.getParameterVariableLong(request, "trayID");

		String reply = "";

		if ((panelID > 0) && (trayID > 0))
		{
			trayEntity = new JQMTrayEntity();

			// If panelID and trayID are provided as URL parameters then use
			// those instead of Body
			trayEntity.setPanelID(panelID);

			trayEntity.setTrayID(trayID);
		}

		// Invoke the create method and pass an instance of the object

		if (trayDB.delete(trayEntity))
		{
			// Return the record created which will include the TrayID which
			// was
			// created dynamically.
			reply = gson.toJson(trayEntity);
		}
		else
		{
			// Create method encountered an error so return fail response
			// with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(trayDB.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}
}
