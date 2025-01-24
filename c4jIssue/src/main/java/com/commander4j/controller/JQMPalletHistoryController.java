package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JQMPalletHistoryDB;
import com.commander4j.entity.JQMPalletHistoryEntity;
import com.commander4j.sys.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMPalletHistoryController extends HttpServlet
{

	private static final long serialVersionUID = 6266031477849351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPalletHistoryController.class);

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.getSession();

		logger.debug("doPut");

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

		JQMPalletHistoryDB palletHistoryDB = new JQMPalletHistoryDB(Common.selectedHostID, request.getSession().getId());
		LinkedList<JQMPalletHistoryEntity> result = new LinkedList<JQMPalletHistoryEntity>();
		BufferedReader bufferedReader = request.getReader();
		JQMPalletHistoryEntity palletHistoryEntity = gson.fromJson(bufferedReader, JQMPalletHistoryEntity.class);

		String reply = "";
		String action = palletHistoryEntity.getAction().toString();
		String sscc = palletHistoryEntity.getSSCC().toString();

		System.out.println(action);

		if (action.equals("query"))
		{
			result = palletHistoryDB.getPalletHistoryBySSCC(sscc);
			reply = gson.toJson(result);
			response.setStatus(Response.SC_ACCEPTED);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();
	}
}
