package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBViewBarcodeValidate.java
 * 
 * Package Name : com.commander4j.db
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBViewBarcodeValidate {

	private String db_process_order;
	private String db_material;	
	private String db_description;	
	private String db_status;
	private String db_required_resource;
	private String db_prod_uom;
	private String db_prod_ean;
	private String db_prod_variant;
	private String db_base_uom;
	private String db_base_ean;
	private String db_base_variant;	

	private String db_error_message;

	private String hostID;
	private String sessionID;
	final Logger logger = Logger.getLogger(JDBViewBarcodeValidate.class);

	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(db_process_order);
	}

	public void setProcessOrder(String db_process_order)
	{
		this.db_process_order = db_process_order;
	}

	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(db_material);
	}

	public void setMaterial(String db_material)
	{
		this.db_material = db_material;
	}

	public String getStatus()
	{
		return JUtility.replaceNullStringwithBlank(db_status);
	}

	public void setStatus(String db_status)
	{
		this.db_status = db_status;
	}

	public String getRequiredResource()
	{
		return JUtility.replaceNullStringwithBlank(db_required_resource);
	}

	public void setRequiredResource(String db_required_resource)
	{
		this.db_required_resource = db_required_resource;
	}

	public String getProdUom()
	{
		return JUtility.replaceNullStringwithBlank(db_prod_uom);
	}

	public void setProdUom(String db_prod_uom)
	{
		this.db_prod_uom = db_prod_uom;
	}

	public String getProdEan()
	{
		return JUtility.replaceNullStringwithBlank(db_prod_ean);
	}

	public void setProdEan(String db_prod_ean)
	{
		this.db_prod_ean = db_prod_ean;
	}

	public String getProdVariant()
	{
		return JUtility.replaceNullStringwithBlank(db_prod_variant);
	}

	public void setProdVariant(String db_prod_variant)
	{
		this.db_prod_variant = db_prod_variant;
	}

	public String getBaseUom()
	{
		return JUtility.replaceNullStringwithBlank(db_base_uom);
	}

	public void setBaseUom(String db_base_uom)
	{
		this.db_base_uom = db_base_uom;
	}

	public String getBaseEan()
	{
		return JUtility.replaceNullStringwithBlank(db_base_ean);
	}

	public void setBaseEan(String db_base_ean)
	{
		this.db_base_ean = db_base_ean;
	}

	public String getBaseVariant()
	{
		return JUtility.replaceNullStringwithBlank(db_base_variant);
	}

	public void setBaseVariant(String db_base_variant)
	{
		this.db_base_variant = db_base_variant;
	}

	public String getErrorMessage()
	{
		return db_error_message;
	}

	public void setErrorMessage(String db_error_message)
	{
		this.db_error_message = db_error_message;
	}
	
	public JDBViewBarcodeValidate(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}

	public void clear() {
		setMaterial("");
		setDescription("");
		setRequiredResource("");
		setStatus("");
		setProdUom("");
		setProdEan("");
		setProdVariant("");
		setBaseUom("");
		setBaseEan("");
		setBaseVariant("");
		setErrorMessage("");
	}


	public String getDescription() {
		return JUtility.replaceNullStringwithBlank(db_description);
	}

	private String getHostID() {
		return hostID;
	}

	public boolean getProperties(String si)
	{
		setProcessOrder(si);
		return getProperties();
	}
		
	public boolean getProperties() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		clear();

		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBarcodeValidate.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getProcessOrder());
			rs = stmt.executeQuery();

			if (rs.next()) {
				setMaterial(rs.getString("material"));
				setDescription(rs.getString("description"));
				setRequiredResource(rs.getString("required_resource"));
				setStatus(rs.getString("status"));
				setProdUom(rs.getString("prod_uom"));
				setProdEan(rs.getString("prod_ean"));
				setProdVariant(rs.getString("prod_variant"));
				setBaseUom(rs.getString("base_uom"));
				setBaseEan(rs.getString("base_ean"));
				setBaseVariant(rs.getString("base_variant"));
				result = true;
			} else {
				setErrorMessage("Invalid Process Order ["+getProcessOrder()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	private String getSessionID() {
		return sessionID;
	}


	public void setDescription(String Description) {
		db_description = Description;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	
}
