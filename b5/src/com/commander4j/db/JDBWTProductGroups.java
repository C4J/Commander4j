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
	public static int field_NominalWeight = 10;
	public static int field_TareWeight = 10;
	public static int field_nominal_uom = 3;
	public static int field_tare_uom = 3;
	public static int field_LowerLimit = 10;
	public static int field_UpperLimit = 10;
	public static int field_Samples_Required = 10;
	private String dbErrorMessage;
	private String dbMaterialGroup;
	private String dbDescription;

	private BigDecimal dbNominalWeight = new BigDecimal("0.000");
	private String dbNominalWeightUOM = "";
	private BigDecimal dbTareWeight = new BigDecimal("0.000");
	private BigDecimal dbLowerLimit = new BigDecimal("0.000");
	private BigDecimal dbUpperLimit = new BigDecimal("0.000");
	private Integer dbSamplesRequired = 0;
	private String dbTareWeightUOM = "";
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

	public JDBWTProductGroups(String host, String session, String materialGroup, String description, BigDecimal nominalWeight, String nominalUOM, BigDecimal tareWeight, String tareUOM,Integer samples,BigDecimal lowerLimit,BigDecimal upperLimit)
	{
		setHostID(host);
		setSessionID(session);
		setProductGroup(materialGroup);
		setDescription(description);
		setNominalWeight(nominalWeight);
		setNominalUOM(nominalUOM);
		setTareWeight(tareWeight);
		setTareUOM(tareUOM);
		setSamplesRequired(samples);
		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);

	}
	
	public void setSamplesRequired(Integer samples)
	{
		dbSamplesRequired = samples;
	}
	
	public void setLowerLimit(BigDecimal lowerLimit)
	{
		dbLowerLimit = lowerLimit;
	}
	
	public void setUpperLimit(BigDecimal upperLimit)
	{
		dbUpperLimit = upperLimit;
	}
	
	public Integer getSamplesRequired()
	{
		return dbSamplesRequired;
	}
	
	public BigDecimal getLowerLimit()
	{
		if (dbLowerLimit == null)
		{
			dbLowerLimit = new BigDecimal("0.000");
		}
		return dbLowerLimit;
	}
	
	public BigDecimal getUpperLimit()
	{
		if (dbUpperLimit == null)
		{
			dbUpperLimit = new BigDecimal("0.000");
		}
		return dbUpperLimit;
	}

	public void clear()
	{
		setDescription("");
		setNominalWeight(new BigDecimal("0"));
		setTareWeight(new BigDecimal("0"));
		setTareUOM("");
		setNominalUOM("");
		setLowerLimit(new BigDecimal("0"));
		setUpperLimit(new BigDecimal("0"));
		setSamplesRequired(0);
		
	}

	public boolean create()
	{

		logger.debug("create [" + getProductGroup() + "]");

		boolean result = false;

		if (isValidProductGroup() == true)
		{
			setErrorMessage("Material Group [" + getProductGroup() + "] already exists !");
		}
		else
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.create"));
				stmtupdate.setString(1, getProductGroup());
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
		setProductGroup(materialGroup);
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
			stmtupdate.setString(1, getProductGroup());
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
		setProductGroup(materialGroup);

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

	public ResultSet getProductGroupDataResultSet()
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

	public ResultSet getProductGroupDataResultSet(PreparedStatement criteria)
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

	public String getProductGroup()
	{
		return dbMaterialGroup;
	}

	public boolean getProductGroupProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			if (getProductGroup().equals("") == false)
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.getProperties"));
				stmt.setFetchSize(1);
				stmt.setString(1, getProductGroup());
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

	public boolean getProductGroupProperties(String matgroup)
	{
		setProductGroup(matgroup);

		return getProductGroupProperties();
	}

	public LinkedList<JDBWTProductGroups> getProductGroups()
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
				tne.setProductGroup(rs.getString("product_group"));
				tne.setDescription(rs.getString("description"));
				tne.setNominalWeight(rs.getBigDecimal("nominal_weight"));
				tne.setNominalUOM(rs.getString("nominal_weight_uom"));
				tne.setTareWeight(rs.getBigDecimal("tare_weight"));
				tne.setTareUOM(rs.getString("tare_weight_uom"));
				tne.setLowerLimit(rs.getBigDecimal("lower_limit"));
				tne.setUpperLimit(rs.getBigDecimal("upper_limit"));
				tne.setSamplesRequired(rs.getInt("samples_required"));
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

	public Vector<JDBWTProductGroups> getProductGroups(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBWTProductGroups> result = new Vector<JDBWTProductGroups>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBWTProductGroups(getHostID(), getSessionID(), "product_group", "description", new BigDecimal("0"), "nominal_weight_uom", new BigDecimal("0"), "tare_weight_uom",0,new BigDecimal("0"),new BigDecimal("0")));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBWTProductGroups(getHostID(), getSessionID(), rs.getString("product_group"), rs.getString("description"), rs.getBigDecimal("nominal_weight"), rs.getString("nominal_weight_uom"), rs.getBigDecimal("tare_weight"),
							rs.getString("tare_weight_uom"),rs.getInt("samples_required"),rs.getBigDecimal("lower_limit"),rs.getBigDecimal("upper_limit")));
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

			setProductGroup(rs.getString("product_group"));
			setDescription(rs.getString("description"));
			setNominalWeight(rs.getBigDecimal("nominal_weight"));
			setNominalUOM(rs.getString("nominal_weight_uom"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
			setTareUOM(rs.getString("tare_weight_uom"));
			setLowerLimit(rs.getBigDecimal("lower_limit"));
			setUpperLimit(rs.getBigDecimal("upper_limit"));
			setSamplesRequired(rs.getInt("samples_required"));
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

	public boolean isValidProductGroup()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProductGroup.isValidProductGroup"));
			stmt.setString(1, getProductGroup());

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

	public boolean isValidProductGroup(String materialGroup)
	{
		setProductGroup(materialGroup);

		return isValidProductGroup();
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

	public void setProductGroup(String materialGroup)
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

		result = JUtility.padString(getProductGroup().toString(), true, field_product_group, " ")+	
        JUtility.padString(getNominalWeight().toString(), false, field_NominalWeight, " ")+" "+ 
		JUtility.padString(getNominalUOM(), true, field_nominal_uom, " ")+" "+
        JUtility.padString(getTareWeight().toString(), false, field_TareWeight, " ")+" "+ 
		JUtility.padString(getTareWeightUOM(), true, field_tare_uom, " ")+"  "+ 
		JUtility.padString(getLowerLimit().toString(), false, field_LowerLimit, " ")+"    "+ 
		JUtility.padString(getUpperLimit().toString(), false, field_UpperLimit, " ")+ 
		JUtility.padString(getSamplesRequired().toString(), false, field_Samples_Required, " ");

		return result;
	}

	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getProductGroup() + "]");

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
				stmtupdate.setInt(6, getSamplesRequired());
				stmtupdate.setBigDecimal(7, getLowerLimit());
				stmtupdate.setBigDecimal(8, getUpperLimit());
				stmtupdate.setString(9, getProductGroup());
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
