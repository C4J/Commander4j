package com.commander4j.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMResourcesDB;
import com.commander4j.entity.JQMResourceEntity;
import com.commander4j.sys.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMResourceController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMResourceController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession();
		
		logger.debug("doGet");

		// Create instance of database handler.
		JQMResourcesDB tdb = new JQMResourcesDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMResourceEntity> panelUserList = new LinkedList<JQMResourceEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

		String reply = "";

		panelUserList = tdb.getResources();
		reply = gson.toJson(panelUserList);

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
