package com.commander4j.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMProcessOrderDB;
import com.commander4j.entity.JQMProcessOrderEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMProcessOrderController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMProcessOrderController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.getSession();

		logger.debug("doGet");

		JQMProcessOrderDB tdb = new JQMProcessOrderDB(Common.selectedHostID, request.getSession().getId());
		LinkedList<JQMProcessOrderEntity> processOrderList = new LinkedList<JQMProcessOrderEntity>();

		String action = JURL.getParameter(request, "action");
		String status = JURL.getParameter(request, "status");
		String processOrder = JURL.getParameter(request, "processOrder");
		String resource = JURL.getParameter(request, "resource");

		String reply = "";

		switch (action)
		{
			case "getOrders":
				if ((status.equals("") == false) || (resource.equals("") == false))
				{
					if (resource.equals("") == true)
					{
						processOrderList = tdb.getProcessOrdersByStatus(status);
					}
					else
					{
						processOrderList = tdb.getProcessOrdersByStatusByResource(status, resource);
					}
					reply = GSON.toJson(processOrderList);
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					reply = GSON.toJson("Invalid URL - missing status and or resource");
				}
				break;
				
			case "getOrder":
				if (processOrder.equals("") == false)
				{
					processOrderList = tdb.getProcessOrderByID(processOrder);
					reply = GSON.toJson(processOrderList);
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					reply = GSON.toJson("Invalid URL - missing processOrder");
				}
				break;
				
			default:
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				reply = GSON.toJson("Invalid URL - missing action");
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
