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

public class JQMTraySampleController extends HttpServlet
{

	private static final long serialVersionUID = -5640407062715724972L;
	private Logger logger = Logger.getLogger(JQMTraySampleController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JQMTraySampleDB tsdb = new JQMTraySampleDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMTraySampleEntity> traySampleList = new LinkedList<JQMTraySampleEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long trayID = url.getParameterVariableLong(request, "trayID");
		Long sampleID = url.getParameterVariableLong(request, "sampleID");

		String reply = "";

		if (trayID > 0)
		{
			if (sampleID > 0)
			{
				// Only PanelId and TrayID Specified
				traySampleList.addLast(tsdb.getProperties(Long.valueOf(trayID), Long.valueOf(sampleID)));
				reply = gson.toJson(traySampleList);
			}
			else
			{
				traySampleList = tsdb.getSamplesByTray(trayID);
				reply = gson.toJson(traySampleList);
			}
		}
		else
		{
			// No panelId provided and no status provided so we can't do
			// anything.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - trayID invalid");
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
		JQMTraySampleEntity traySampleEntity = gson.fromJson(bufferedReader, JQMTraySampleEntity.class);
		// Create an instance of the database handler.
		JQMTraySampleDB traySampleDB = new JQMTraySampleDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";

		// Invoke the update method and pass an instance of the object
		if (traySampleDB.update(traySampleEntity))
		{
			// Return the updated record
			reply = gson.toJson(traySampleEntity);
		}
		else
		{
			// Update method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(traySampleDB.getErrorMessage());
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
		JQMTraySampleEntity traySampleEntity = gson.fromJson(bufferedReader, JQMTraySampleEntity.class);
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		String reply = "";

		// Create database handler
		JQMTraySampleDB traySampleDB = new JQMTraySampleDB(Common.selectedHostID, request.getSession().getId());
		logger.debug("Invoke create on Tray ID Sample ID :" + String.valueOf(traySampleEntity));

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long trayID = url.getParameterVariableLong(request, "trayID");
		Long sampleID = url.getParameterVariableLong(request, "sampleID");
		Long sequenceID = url.getParameterVariableLong(request, "sequenceID");
		
		if (traySampleEntity == null)
		{
			traySampleEntity = new JQMTraySampleEntity();
		}

		if (trayID > 0)
		{
			traySampleEntity.setTrayID(trayID);
		}

		if (sampleID > 0)
		{
			traySampleEntity.setSampleID(sampleID);
		}

		if (sequenceID > 0)
		{
			traySampleEntity.setSequenceID(sequenceID);
		}

		// If the provided sequence_numnber is zero then calculate the next one
		// to use.
		if (traySampleEntity.getSequenceID() == 0)
		{
			traySampleEntity.setSequenceID(traySampleDB.getNewSequenceID(traySampleEntity.getTrayID()));
		}

		if ((traySampleEntity.getSequenceID() == 0) || (traySampleEntity.getTrayID() == 0) || ((traySampleEntity.getSampleID() == 0)))
		{
			// No panelId provided and no status provided so we can't do
			// anything.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - trayId and sampleID must be provided");
		}
		else
		{

			// Invoke the create method and pass an instance of the object
			if (traySampleDB.create(traySampleEntity))
			{
				traySampleEntity = traySampleDB.getTraySampleEntity();
				// Return the record created which will include the TrayID which
				// was
				// created dynamically.
				reply = gson.toJson(traySampleEntity);
			}
			else
			{
				// Create method encountered an error so return fail response
				// with
				// error message.
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson(traySampleDB.getErrorMessage());
			}
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
		JQMTraySampleEntity traySampleEntity = gson.fromJson(bufferedReader, JQMTraySampleEntity.class);

		JQMTraySampleDB traySampleDB = new JQMTraySampleDB(Common.selectedHostID, request.getSession().getId());
		JURL url = new JURL(request);
		
		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		Long trayID = url.getParameterVariableLong(request, "trayID");
		Long sampleID = url.getParameterVariableLong(request, "sampleID");
		
		if (traySampleEntity == null)
		{
			traySampleEntity = new JQMTraySampleEntity();
			traySampleEntity.setTrayID((long) -1);
			traySampleEntity.setSampleID((long) -1);
		}
		
		String reply = "";

		if ((trayID > 0) && (sampleID > 0))
		{
			traySampleEntity = new JQMTraySampleEntity();
			traySampleEntity.setTrayID(trayID);
			traySampleEntity.setSampleID(sampleID);
		}
		
		// Invoke the create method and pass an instance of the object
		if (traySampleDB.delete(traySampleEntity))
		{
			// Return the record created which will include the TrayID which was
			// created dynamically.
			reply = gson.toJson(traySampleEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(traySampleDB.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
	}

}
