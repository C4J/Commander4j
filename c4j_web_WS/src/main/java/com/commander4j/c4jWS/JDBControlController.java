package com.commander4j.c4jWS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JDBControlController extends HttpServlet
{

	private static final long serialVersionUID = -3225137788488489978L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPanelController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JDBControl tdb = new JDBControl(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		JDBControlEntity controlEntity = new JDBControlEntity();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter

		String systemKey = url.getParameterVariable(request, "systemKey");

		String reply = "";

		// If Status specified - use that.
		if (tdb.getProperties(systemKey))
		{
			controlEntity.setSystemKey(tdb.getSystemKey());
			controlEntity.setKeyValue(tdb.getKeyValue());
			controlEntity.setDescription(tdb.getDescription());

			reply = gson.toJson(controlEntity);
		}
		else
		{
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - systemKey invalid");
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Create - note PanelID is auto generated and does not need to be
		// passed
		// to function within PanelEntity

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create database handler
		JDBControl tdb = new JDBControl(Common.selectedHostID, request.getSession().getId());
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the JSON Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Decode the body of the request and map the fields to the matching
		// fields of the Object passed to the method.
		JDBControlEntity controlEntity = gson.fromJson(bufferedReader, JDBControlEntity.class);

		String reply = "";

		logger.debug("Invoke get control key or create if it does not exist (create) :" + String.valueOf(controlEntity));

		if ((controlEntity.getSystemKey().equals("") == true) || (controlEntity.getKeyValue().equals("") == true) || (controlEntity.getDescription().equals("") == true))
		{
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = "Missing values, body must contain, systemKey,keyValue and description.\n"+gson.toJson(controlEntity);
		}
		else
		{
			// Invoke the create method and pass an instance of the object
			tdb.getKeyValueWithDefault(controlEntity.getSystemKey(), controlEntity.getKeyValue(), controlEntity.getDescription());
			controlEntity.setSystemKey(tdb.getSystemKey());
			controlEntity.setKeyValue(tdb.getKeyValue());
			controlEntity.setDescription(tdb.getDescription());
			reply = gson.toJson(controlEntity);
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
