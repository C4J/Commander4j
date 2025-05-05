package com.commander4j.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JQMPalletDB;
import com.commander4j.db.JQMViewBomDB;
import com.commander4j.entity.JQMPalletEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JURL;
import com.commander4j.util.JUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JQMPalletController extends HttpServlet
{

	private static final long serialVersionUID = 6266031476649351904L;
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JQMPalletController.class);


	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		request.getSession();
		
		logger.debug("doPut");

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		BufferedReader bufferedReader = request.getReader();
		JQMPalletEntity palletEntity = gson.fromJson(bufferedReader, JQMPalletEntity.class);
		JQMPalletDB palletDB = new JQMPalletDB(Common.selectedHostID, request.getSession().getId());
		JDBPallet pallet_db = new JDBPallet(Common.selectedHostID, request.getSession().getId());
		JDBProcessOrder order_db = new JDBProcessOrder(Common.selectedHostID, request.getSession().getId());
		JQMViewBomDB view_bom_db = new JQMViewBomDB(Common.selectedHostID, request.getSession().getId());

		String reply = "";
		String action = palletEntity.getAction().toString();
		String sscc = palletEntity.getSSCC().toString();
		String issueToOrder = palletEntity.getProcessOrder().toString();
		String userId = palletEntity.getUserId().toString();
		String location_id = palletEntity.getLocationId();
		BigDecimal quantity = palletEntity.getQuantity();

		JURL url = new JURL(request);

		String stage = JUtility.replaceNullStringwithBlank(url.getParameterVariable(request, "stage"));
		
		System.out.println(action);

		if (action.equals("query"))
		{

			if (palletDB.isValid(palletEntity.getSSCC()))
			{
				palletEntity = palletDB.getProperties(palletEntity.getSSCC());
				reply = gson.toJson(palletEntity);
				response.setStatus(Response.SC_ACCEPTED);
			}
			else
			{
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson("Invalid URL or SSCC invalid");
			}
		}
		
		if (action.equals("info"))
		{

			if (pallet_db.getPalletProperties(palletEntity.getSSCC()))
			{
				palletEntity = palletDB.getProperties(palletEntity.getSSCC());
				palletEntity.setDescription(pallet_db.getMaterialObj().getDescription());
				palletEntity.setLocationId(pallet_db.getLocationID());
				reply = gson.toJson(palletEntity);
				response.setStatus(Response.SC_ACCEPTED);
			}
			else
			{
				response.setStatus(Response.SC_NOT_ACCEPTABLE);
				reply = gson.toJson("Invalid URL or SSCC invalid");
			}
		}
		
		if (action.equals("issue"))
		{

			pallet_db.getPalletProperties(sscc);
			palletEntity.getPropertiesFromPallet(pallet_db);
			palletDB.issueToOrder_rest(sscc,issueToOrder, quantity,location_id,userId);
			reply = gson.toJson(palletEntity);
			response.setStatus(Response.SC_ACCEPTED);
		}
		
		if (action.equals("return"))
		{

			pallet_db.getPalletProperties(sscc);
			palletEntity.getPropertiesFromPallet(pallet_db);
			palletDB.returnFromOrder_rest(sscc,issueToOrder, quantity,location_id,userId);
			reply = gson.toJson(palletEntity);
			response.setStatus(Response.SC_ACCEPTED);
		}
		
		if (action.equals("validateMaterial"))
		{
			
			palletEntity.setCommandStatus("invalid");
			palletEntity.setErrorMessage("");
			
			if (pallet_db.getPalletProperties(sscc))
			{
				palletEntity.setMaterial(pallet_db.getMaterial());

				
				if (order_db.getProcessOrderProperties(issueToOrder))
				{
					palletEntity.setBomId(order_db.getRecipe());
					palletEntity.setBomVersion(order_db.getRecipeVersion());
					
					String material = pallet_db.getMaterial();
					BigDecimal qty = pallet_db.getQuantity();
					
					if (qty.compareTo(new BigDecimal(0)) > 0)
					{
						if (view_bom_db.isMaterialValidForBOM(order_db.getRecipe(), order_db.getRecipeVersion(),stage, "input", material))
						{
							palletEntity.setCommandStatus("valid");
						}
						else
						{
							palletEntity.setErrorMessage("Material ["+material+"] is not valid for order ["+order_db.getProcessOrder()+"]");
						}
					}
					else
					{
						palletEntity.setErrorMessage("Pallet Quantity is ZERO");	
					}
				}
				else
				{
					palletEntity.setErrorMessage(order_db.getErrorMessage());
				}
			}
			else
			{
				palletEntity.setErrorMessage(pallet_db.getErrorMessage());
			}
			
			reply = gson.toJson(palletEntity);
			response.setStatus(Response.SC_ACCEPTED);
		}
		
		if (action.equals("validateLocation"))
		{
			
			palletEntity.setCommandStatus("invalid");
			palletEntity.setErrorMessage("");
			
			if (pallet_db.getPalletProperties(sscc))
			{
				palletEntity.setMaterial(pallet_db.getMaterial());

				
				if (order_db.getProcessOrderProperties(issueToOrder))
				{
					palletEntity.setBomId(order_db.getRecipe());
					palletEntity.setBomVersion(order_db.getRecipeVersion());
					
					String material = pallet_db.getMaterial();
					BigDecimal qty = pallet_db.getQuantity();
					
					if (qty.compareTo(new BigDecimal(0)) > 0)
					{
						if (view_bom_db.isValidMaterialForLocation(order_db.getRecipe(), order_db.getRecipeVersion(),stage, "input", material,location_id))
						{
							palletEntity.setCommandStatus("valid");
						}
						else
						{
							palletEntity.setErrorMessage("Material ["+material+"] is not valid for Location ["+location_id+"]");
						}
					}
					else
					{
						palletEntity.setErrorMessage("Pallet Quantity is ZERO");	
					}
				}
				else
				{
					palletEntity.setErrorMessage(order_db.getErrorMessage());
				}
			}
			else
			{
				palletEntity.setErrorMessage(pallet_db.getErrorMessage());
			}
			
			reply = gson.toJson(palletEntity);
			response.setStatus(Response.SC_ACCEPTED);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(reply);
		out.flush();

	}

}
