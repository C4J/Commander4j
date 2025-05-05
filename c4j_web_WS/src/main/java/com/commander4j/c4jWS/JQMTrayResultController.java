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

public class JQMTrayResultController extends HttpServlet
{

	private static final long serialVersionUID = -5640407062715924972L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMTrayResultController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JQMTrayResultDB tsdb = new JQMTrayResultDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMTrayResultEntity> sampleList = new LinkedList<JQMTrayResultEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long trayID = url.getParameterVariableLong(request, "trayID");
		Long sampleID = url.getParameterVariableLong(request, "sampleID");
		String userID = url.getParameterVariable(request, "userID");

		String reply = "";

		if (trayID > 0)
		{
			if (sampleID > 0)
			{
				if (userID.equals("") == false)
				{
					// Only PanelId and TrayID Specified
					sampleList.addLast(tsdb.getProperties(Long.valueOf(trayID), Long.valueOf(sampleID), userID));
					reply = gson.toJson(sampleList);
				}
				else
				{
					response.setStatus(Response.SC_NOT_ACCEPTABLE);
					reply = gson.toJson("Invalid URL - userID missing");
				}
			}
			else
			{
				sampleList = tsdb.getResultsByTrayUser(trayID, userID);
				reply = gson.toJson(sampleList);
			}
		}
		else
		{
			// No panelId provided and no status provided so we can't do
			// anything.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - trayId missing");
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
		// the database will be checked for a matching tray and sample id.

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Use GSON to map the fields from the GSON Body to the fields of the
		// Object
		JQMTrayResultEntity trayResultEntity = gson.fromJson(bufferedReader, JQMTrayResultEntity.class);
		// Create an instance of the database handler.
		JQMTrayResultDB tsdb = new JQMTrayResultDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";

		// Invoke the update method and pass an instance of the object
		if (tsdb.update(trayResultEntity))
		{
			// Return the updated record
			reply = gson.toJson(trayResultEntity);
		}
		else
		{
			// Update method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(tsdb.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the JSON Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Decode the body of the request and map the fields to the matching
		// fields of the Object passed to the method.
		JQMTrayResultEntity[] trayResultEntity = gson.fromJson(bufferedReader, JQMTrayResultEntity[].class);

		String reply = "";

		// Create database handler
		JQMTrayResultDB tsdb = new JQMTrayResultDB(Common.selectedHostID, request.getSession().getId());
		logger.debug("Invoke create/update");
		
		// Invoke the create method and pass an instance of the object
		if (tsdb.creates(trayResultEntity))
		{
			// Return the record created which will include the TrayID which was
			// created dynamically.
			reply = gson.toJson("Success");
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(tsdb.getErrorMessage());
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
		JQMTrayResultEntity trayResultEntity = gson.fromJson(bufferedReader, JQMTrayResultEntity.class);

		JQMTrayResultDB trayResultdb = new JQMTrayResultDB(Common.selectedHostID, request.getSession().getId());
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long trayID = url.getParameterVariableLong(request, "trayID");
		Long sampleID = url.getParameterVariableLong(request, "sampleID");
		String userID = url.getParameterVariable(request, "userID");
		
		String reply = "";

		if (trayID > 0)
		{
			if (sampleID > 0)
			{
				if (userID.equals("") == false)
				{
					// Only PanelId and TrayID Specified
					trayResultEntity = new JQMTrayResultEntity();
					trayResultEntity.setTrayID(trayID);
					trayResultEntity.setSampleID(sampleID);
					trayResultEntity.setUserID(userID);
					trayResultdb.delete(trayResultEntity);
				}
			}
		}	
		
		// Invoke the create method and pass an instance of the object
		if (trayResultdb.delete(trayResultEntity))
		{
			// Return the record created which will include the TrayID which was
			// created dynamically.
			reply = gson.toJson(trayResultEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(trayResultdb.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
	}

}
