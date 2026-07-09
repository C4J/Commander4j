package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMPalletHistoryDB;
import com.commander4j.entity.JQMPalletHistoryEntity;
import com.commander4j.sys.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMPalletHistoryController extends HttpServlet
{

	private static final long serialVersionUID = 6266031477849351904L;
	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPalletHistoryController.class);

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.getSession();

		logger.debug("doPut");

		JQMPalletHistoryDB palletHistoryDB = new JQMPalletHistoryDB(Common.selectedHostID, request.getSession().getId());
		LinkedList<JQMPalletHistoryEntity> result = new LinkedList<JQMPalletHistoryEntity>();
		BufferedReader bufferedReader = request.getReader();

		JQMPalletHistoryEntity palletHistoryEntity;
		try
		{
			palletHistoryEntity = GSON.fromJson(bufferedReader, JQMPalletHistoryEntity.class);
		}
		catch (JsonSyntaxException e)
		{
			palletHistoryEntity = null;
		}

		if (palletHistoryEntity == null)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(GSON.toJson("Request body missing or invalid"));
			out.flush();
			return;
		}

		String reply = "";
		String action = palletHistoryEntity.getAction();
		String sscc = palletHistoryEntity.getSSCC();

		logger.debug("action [" + action + "]");

		if (action.equals("query"))
		{
			result = palletHistoryDB.getPalletHistoryBySSCC(sscc);
			reply = GSON.toJson(result);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			reply = GSON.toJson("Unknown action [" + action + "]");
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
	}
}
