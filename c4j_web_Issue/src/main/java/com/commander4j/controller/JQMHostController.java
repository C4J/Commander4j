package com.commander4j.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.entity.JQMHostEntity;
import com.commander4j.sys.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMHostController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMHostController.class);

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();
		
		logger.debug("doPut");
		
		String uniquieHost = Common.hostList.getHostIDforUniqueId("service");
		
		String hostName = Common.hostList.getHost(uniquieHost).getSiteDescription();
				
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

		String reply = "";

		JQMHostEntity host = new JQMHostEntity();
		
		host.setHostID(uniquieHost);
		host.setDescription(hostName);
		
		reply = gson.toJson(host);
		response.setStatus(Response.SC_ACCEPTED);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.print(reply);
		out.flush();

	}
}
