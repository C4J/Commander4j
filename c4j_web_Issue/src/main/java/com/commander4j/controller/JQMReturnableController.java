package com.commander4j.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMReturnableDB;
import com.commander4j.entity.JQMReturnableEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMReturnableController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMReturnableController.class);

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();
		
		logger.debug("doGet");
		
		JQMReturnableDB tdb = new JQMReturnableDB(Common.selectedHostID, request.getSession().getId());
		LinkedList<JQMReturnableEntity> returnable = new LinkedList<JQMReturnableEntity>();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JURL url = new JURL(request);

		String sscc = url.getParameterVariable(request, "sscc");

		String reply = "";

		if ((sscc.equals("") == false))
		{
			returnable = tdb.getReturnableBySSCC(sscc);
			reply = gson.toJson(returnable);
			response.setStatus(Response.SC_ACCEPTED);
		}
		else
		{
			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("Invalid URL - enabled or SSCC invalid");
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
