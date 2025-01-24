package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBUser;
import com.commander4j.entity.JQMUserEntity;
import com.commander4j.sys.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMUserController extends HttpServlet
{


	private static final long serialVersionUID = 1L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMUserController.class);
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();
		logger.debug("doPut");
		

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		BufferedReader bufferedReader = request.getReader();
		JQMUserEntity userEntity = gson.fromJson(bufferedReader, JQMUserEntity.class);
		JDBUser userDB = new JDBUser(Common.selectedHostID, request.getSession().getId());

		String reply = "";
		
		userDB.setUserId(userEntity.getUserID());
		userDB.setLoginPassword(userEntity.getUserPassword());
		
		if (userDB.login())
		{

			Common.userList.addUser(request.getSession().getId(),userDB);
			response.setStatus(Response.SC_ACCEPTED);
			userEntity.setCommandStatus("Success");
			reply = gson.toJson(userEntity);
		}
		else
		{
			response.setStatus(Response.SC_ACCEPTED);
			userEntity.setCommandStatus(userDB.getErrorMessage());
			reply = gson.toJson(userEntity);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
