package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

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
	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
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

		String action = JURL.getParameter(request, "action");
		String reply = "";

		if (action.equals("getStages"))
		{

			String bom_id = JURL.getParameter(request, "bom_id");
			String bom_version = JURL.getParameter(request, "bom_version");

			stageList = tdb.getStagesForBOM(bom_id, bom_version);
			reply = GSON.toJson(stageList);

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

		BufferedReader bufferedReader = request.getReader();
		JQMViewBOMEntity viewBOMEntity = GSON.fromJson(bufferedReader, JQMViewBOMEntity.class);
		JQMViewBomDB viewBOMDB = new JQMViewBomDB(Common.selectedHostID, request.getSession().getId());

		Boolean result = false;
		String reply = "";

		if (viewBOMEntity.getAction().equals("validate"))
		{

			result = viewBOMDB.isValidMaterialForLocation(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput(), viewBOMEntity.getMaterial(), viewBOMEntity.getLocation_id());

			if (result == true)
			{
				reply = GSON.toJson("valid");
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				reply = GSON.toJson("invalid");
			}
		}

		if (viewBOMEntity.getAction().equals("getValidLocations"))
		{

			LinkedList<JQMViewBOMEntity> validLocations = viewBOMDB.getValidtLocationsforMaterial(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput(), viewBOMEntity.getMaterial());

			reply = GSON.toJson(validLocations);
			response.setStatus(HttpServletResponse.SC_OK);

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			reply = GSON.toJson("none");

		}

		if (viewBOMEntity.getAction().equals("getMaterialsForBOM"))
		{

			LinkedList<JQMViewBOMEntity> validLocations = viewBOMDB.getValidMaterialsForBOM(viewBOMEntity.getBomID(), viewBOMEntity.getBomVersion(), viewBOMEntity.getStage(), viewBOMEntity.getInputOutput());

			reply = GSON.toJson(validLocations);
			response.setStatus(HttpServletResponse.SC_OK);

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

		String reply = "";

		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		reply = GSON.toJson("Update not supported");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
