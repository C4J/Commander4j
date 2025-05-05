package com.commander4j.c4jWS;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;

import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JDBQMSelectListController extends HttpServlet
{

	private static final long serialVersionUID = -4650239229554206236L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession(); // This must always be first line to ensure
								// initialisation for session occurs with
								// AppServlet Session Listener.

		// Create instance of database handler.
		JDBQMSelectList tdb = new JDBQMSelectList(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;

		LinkedList<JDBQMSelectListEntity> selectListItems = new LinkedList<JDBQMSelectListEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		// Create an instance of a utility for parsing the URL
		JURL url = new JURL(request);

		// getParameterVariableLong will return -1 for missing or non numeric
		// parameter

		String selectListID = url.getParameterVariable(request, "selectListID");
		String reply = "";

		if (selectListID.equals("") == false)
		{
			selectListItems = tdb.getSelectListEntity(selectListID);
			reply = gson.toJson(selectListItems);
		}
		else
		{
			// No panelId provided and no status provided so we can't do
			// anything.
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - selectListID invalid");
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
