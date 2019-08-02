package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterialCustomerData.java
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
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBMaterialGroups class is used to insert/update/delete the
 * APP_product_group table. This table is keyed on , CUSTOMER_ID and DATA_ID.
 * <p>
 * <img alt="" src="./doc-files/APP_product_groupS.jpg" >
 * 
 * @see com.commander4j.db.JDBWTProductGroups JDBMaterialGroups
 */

public class JDBWTProductGroups
{

	public static int field_product_group = 25;
	public static int field_description = 50;
	public static int field_nominal_uom = 3;
	public static int field_tare_uom = 3;
	private String dbErrorMessage;
	private String dbMaterialGroup;
	private String dbDescription;

	private BigDecimal dbNominalWeight;
	private String dbNominalWeightUOM;
	private BigDecimal dbTareWeight;
	private String dbTareWeightUOM;
	private final Logger logger = Logger.getLogger(JDBWTProductGroups.class);
	private String hostID;
	private String sessionID;
	private JDBUom uom;

	public JDBWTProductGroups(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public JDBWTProductGroups(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		uom = new JDBUom(getHostID(), getSessionID());
	}

	public JDBWTProductGroups(String host, String session, String materialGroup, String description, BigDecimal nominalWeight, String nominalUOM, BigDecimal tareWeight, String tareUOM)
	{
		setHostID(host);
		setSessionID(session);
		setMaterialGroup(materialGroup);
		setDescription(description);
		setNominalWeight(nominalWeight);
		setNominalUOM(nominalUOM);
		setTareWeight(tareWeight);
		setTareUOM(tareUOM);

	}

	public void clear()
	{
		setDescription("");
		setNominalWeight(new BigDecimal("0"));
		setTareWeight(new BigDecimal("0"));
		setTareUOM("");
		setNominalUOM("");
	}

	public boolean create()
	{

		logger.debug("create [" + getMaterialGroup() + "]");

		boolean result = false;

		if (isValidMaterialGroup() == true)
		{
			setErrorMessage("Material Group [" + getMaterialGroup() + "] already exists !");
		}
		else
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.create"));
				stmtupdate.setString(1, getMaterialGroup());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				update();
				result = true;
			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public boolean create(String materialGroup)
	{
		boolean result = false;
		setMaterialGroup(materialGroup);
		result = create();
		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.delete"));
			stmtupdate.setString(1, getMaterialGroup());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String materialGroup)
	{
		boolean result = false;
		setMaterialGroup(materialGroup);

		result = delete();
		return result;
	}

	public String getDescription()
	{
		return dbDescription;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getMaterialDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.getProductGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public ResultSet getMaterialDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		}
		catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public String getMaterialGroup()
	{
		return dbMaterialGroup;
	}

	public boolean getMaterialGroupProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			if (getMaterialGroup().equals("") == false)
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.getProperties"));
				stmt.setFetchSize(1);
				stmt.setString(1, getMaterialGroup());
				rs = stmt.executeQuery();

				if (rs.next())
				{
					getPropertiesfromResultSet(rs);
					result = true;
				}
				else
				{
					setErrorMessage("Invalid Material Group");
				}
				rs.close();
				stmt.close();
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getMaterialGroupProperties(String matgroup)
	{
		setMaterialGroup(matgroup);

		return getMaterialGroupProperties();
	}

	public LinkedList<JDBWTProductGroups> getMaterialGroups()
	{
		LinkedList<JDBWTProductGroups> tneList = new LinkedList<JDBWTProductGroups>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.getProductGroups"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTProductGroups tne = new JDBWTProductGroups(getHostID(), getSessionID());
				tne.setMaterialGroup(rs.getString("product_group"));
				tne.setDescription(rs.getString("description"));
				tne.setNominalWeight(rs.getBigDecimal("nominal_weight"));
				tne.setNominalUOM(rs.getString("nominal_weight_uom"));
				tne.setTareWeight(rs.getBigDecimal("tare_weight"));
				tne.setTareUOM(rs.getString("tare_weight_uom"));
				tneList.add(tne);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return tneList;
	}

	public Vector<JDBWTProductGroups> getMaterialGroups(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBWTProductGroups> result = new Vector<JDBWTProductGroups>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBWTProductGroups(getHostID(), getSessionID(), "product_group", "description", new BigDecimal("0"), "nominal_weight_uom", new BigDecimal("0"), "tare_weight_uom"));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBWTProductGroups(getHostID(), getSessionID(), rs.getString("product_group"), rs.getString("description"), rs.getBigDecimal("nominal_weight"), rs.getString("nominal_weight_uom"), rs.getBigDecimal("tare_weight"),
							rs.getString("tare_weight_uom")));
				}
				rs.close();

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public String getNominalUOM()
	{
		return JUtility.replaceNullStringwithBlank(dbNominalWeightUOM);
	}

	public BigDecimal getNominalWeight()
	{
		return dbNominalWeight;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setMaterialGroup(rs.getString("product_group"));
			setDescription(rs.getString("description"));
			setNominalWeight(rs.getBigDecimal("nominal_weight"));
			setNominalUOM(rs.getString("nominal_weight_uom"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
			setTareUOM(rs.getString("tare_weight_uom"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public BigDecimal getTareWeight()
	{
		return dbTareWeight;
	}

	public String getTareWeightUOM()
	{
		return JUtility.replaceNullStringwithBlank(dbTareWeightUOM);
	}

	public boolean isValid()
	{
		boolean result = true;

		if (JUtility.isNullORBlank(dbMaterialGroup) == true)
		{
			setErrorMessage("Material Group code cannot be blank");
			result = false;
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getNominalUOM()) == true)
			{
				setErrorMessage("Nominal Weight UOM cannot be blank");
				result = false;
			}
			else
			{
				if (uom.isValidInternalUom(getNominalUOM()) == false)
				{
					setErrorMessage(uom.getErrorMessage());
					result = false;
				}
			}
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getTareWeightUOM()) == true)
			{
				setErrorMessage("Tare Weight UOM cannot be blank");
				result = false;
			}
			else
			{
				if (uom.isValidInternalUom(getTareWeightUOM()) == false)
				{
					setErrorMessage(uom.getErrorMessage());
					result = false;
				}
			}
		}

		return result;
	}

	public boolean isValidMaterialGroup()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.isValidProductGroup"));
			stmt.setString(1, getMaterialGroup());

			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Material Group");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidMaterialGroup :" + result);

		return result;

	}

	public boolean isValidMaterialGroup(String materialGroup)
	{
		setMaterialGroup(materialGroup);

		return isValidMaterialGroup();
	}

	public void setDescription(String description)
	{
		dbDescription = description;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setMaterialGroup(String materialGroup)
	{
		dbMaterialGroup = materialGroup;
	}

	public void setNominalUOM(String nomUOM)
	{
		dbNominalWeightUOM = JUtility.replaceNullStringwithBlank(nomUOM).toUpperCase();

	}

	public void setNominalWeight(BigDecimal dbNominalWeight)
	{
		if (dbNominalWeight == null)
		{
			this.dbNominalWeight = new BigDecimal("0.000");
		}
		else
		{
			this.dbNominalWeight = dbNominalWeight;
		}
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setTareUOM(String tareUOM)
	{
		dbTareWeightUOM = JUtility.replaceNullStringwithBlank(tareUOM).toUpperCase();
	}

	public void setTareWeight(BigDecimal dbTareWeight)
	{

		if (dbTareWeight == null)
		{
			this.dbTareWeight = new BigDecimal("0.000");
		}
		else
		{
			this.dbTareWeight = dbTareWeight;
		}
	}

	public String toString()
	{

		String result = "";

		result = JUtility.padString(getMaterialGroup().toString(), true, field_product_group, " ");

		return result;
	}

	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getMaterialGroup() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setBigDecimal(2, getNominalWeight());
				stmtupdate.setString(3, getNominalUOM());
				stmtupdate.setBigDecimal(4, getTareWeight());
				stmtupdate.setString(5, getTareWeightUOM());
				stmtupdate.setString(6, getMaterialGroup());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;

			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

}
