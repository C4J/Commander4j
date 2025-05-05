package com.commander4j.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMProcessOrderDB;
import com.commander4j.entity.JQMProcessOrderEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JURL;
import com.commander4j.util.JUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMProcessOrderController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMProcessOrderController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.getSession();
		
		logger.debug("doGet");

		JQMProcessOrderDB tdb = new JQMProcessOrderDB(Common.selectedHostID, request.getSession().getId());
		LinkedList<JQMProcessOrderEntity> processOrderList = new LinkedList<JQMProcessOrderEntity>();
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JURL url = new JURL(request);

		String action = JUtility.replaceNullStringwithBlank(url.getParameterVariable(request, "action"));
		String status = JUtility.replaceNullStringwithBlank(url.getParameterVariable(request, "status"));
		String processOrder = JUtility.replaceNullStringwithBlank(url.getParameterVariable(request, "processOrder"));
		String resource = JUtility.replaceNullStringwithBlank(url.getParameterVariable(request, "resource"));

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
					reply = gson.toJson(processOrderList);
				}
				else
				{
					response.setStatus(Response.SC_NOT_ACCEPTABLE);
					reply = gson.toJson("Invalid URL - missing status and or resource");
				}
				break;
				
			case "getOrder":
				if (processOrder.equals("") == false)
				{
					processOrderList = tdb.getProcessOrderByID(processOrder);
					reply = gson.toJson(processOrderList);
				}
				else
				{
					response.setStatus(Response.SC_NOT_ACCEPTABLE);
					reply = gson.toJson("Invalid URL - missing processOrder");
				}
				break;
				
			default:
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson("Invalid URL - missing action");
		}

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
