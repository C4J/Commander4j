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

public class JQMUserController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = Logger.getLogger(JQMUserController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JQMUserDB tdb = new JQMUserDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMUserEntity> panelUserList = new LinkedList<JQMUserEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter
		String enabled = url.getParameterVariable(request, "enabled");
		String userID = url.getParameterVariable(request, "userID");

		String reply = "";

		if ((enabled.equals("")==false) || (userID.equals("")==false))
		{
			// If User specified - use that.

			if (userID.equals(""))
			{
				panelUserList = tdb.getUsersByEnabled(enabled);
				reply = gson.toJson(panelUserList);
			}
			else
			{

				panelUserList = tdb.getUserByUserID(userID);
				reply = gson.toJson(panelUserList);
			}
		}
		else
		{

			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - enabled or userID invalid");
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

		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Read the JSON Body of the request
		BufferedReader bufferedReader = request.getReader();
		// Decode the body of the request and map the fields to the matching
		// fields of the Object passed to the method.
		JQMUserEntity userEntity = gson.fromJson(bufferedReader, JQMUserEntity.class);

		String reply = "";

		// Create database handler
		JQMUserDB userdb = new JQMUserDB(Common.selectedHostID, request.getSession().getId());
		logger.debug("Invoke create on User ID :" + String.valueOf(userEntity));

		// Invoke the create method and pass an instance of the object
		if (userdb.create(userEntity))
		{
			// Return the record created which will include the PanelID which
			// was
			// created dynamically.
			reply = gson.toJson(userEntity);
		}
		else
		{
			// Create method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(userdb.getErrorMessage());
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
		JQMUserEntity userEntity = gson.fromJson(bufferedReader, JQMUserEntity.class);
		// Create an instance of the database handler.
		JQMUserDB userDB = new JQMUserDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";

		// Invoke the update method and pass an instance of the object
		if (userDB.update(userEntity))
		{
			// Return the updated record
			reply = gson.toJson(userEntity);
		}
		else
		{
			// Update method encountered an error so return fail response with
			// error message.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson(userDB.getErrorMessage());
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
