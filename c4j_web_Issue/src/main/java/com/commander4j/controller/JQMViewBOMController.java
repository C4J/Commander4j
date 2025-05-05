package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMViewBomDB;
import com.commander4j.entity.JQMViewBOMEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMViewBOMController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMViewBOMController.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// Retrieve

		request.getSession();
		
		logger.debug("doGet");

		// Create instance of database handler.
		JQMViewBomDB tdb = new JQMViewBomDB(Common.selectedHostID, request.getSession().getId());
		// Create an object to hold the return result;
		LinkedList<JQMViewBOMEntity> stageList = new LinkedList<JQMViewBOMEntity>();
		// Create and instance of the Google JSON utility.
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JURL url = new JURL(request);

		String action = url.getParameterVariable(request, "action");
		String reply = "";
		
		if (action.equals("getStages"))
		{
		
			String bom_id = url.getParameterVariable(request, "bom_id");
			String bom_version = url.getParameterVariable(request, "bom_version");
			
			stageList = tdb.getStagesForBOM(bom_id, bom_version);
			reply = gson.toJson(stageList);
		
		}
		

		// Output the response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.print(reply);
		out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();

		logger.debug("doPost");

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		BufferedReader bufferedReader = request.getReader();
		JQMViewBOMEntity viewBOMEntity = gson.fromJson(bufferedReader, JQMViewBOMEntity.class);
		JQMViewBomDB viewBOMDB = new JQMViewBomDB(Common.selectedHostID, request.getSession().getId());

		Boolean result = false;
		String reply = "";

		if (viewBOMEntity.getAction().equals("validate"))
		{

			result = viewBOMDB.isValidMaterialForLocation(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput(), viewBOMEntity.getMaterial(), viewBOMEntity.getLocation_id());

			if (result == true)
			{
				reply = gson.toJson("valid");
				response.setStatus(Response.SC_ACCEPTED);
			}
			else
			{
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson("invalid");
			}
		}

		if (viewBOMEntity.getAction().equals("getValidLocations"))
		{

			LinkedList<JQMViewBOMEntity> validLocations = viewBOMDB.getValidtLocationsforMaterial(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput(), viewBOMEntity.getMaterial());

			reply = gson.toJson(validLocations);
			response.setStatus(Response.SC_ACCEPTED);

			response.setStatus(Response.SC_NOT_ACCEPTABLE);
			reply = gson.toJson("none");

		}

		if (viewBOMEntity.getAction().equals("getMaterialsForBOM"))
		{

			LinkedList<JQMViewBOMEntity> validLocations = viewBOMDB.getValidMaterialsForBOM(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput());

			reply = gson.toJson(validLocations);
			response.setStatus(Response.SC_ACCEPTED);

		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();

		logger.debug("doPut");

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

		String reply = "";

		response.setStatus(Response.SC_NOT_ACCEPTABLE);
		reply = gson.toJson("Update not supported");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
