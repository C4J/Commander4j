package com.commander4j.db;

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
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;
/**
 * JDBMaterialCustomerData class is used  to insert/update/delete the APP_MATERIAL_CUSTOMER_DATA table. 
 * This table is keyed on MATERIAL, CUSTOMER_ID and DATA_ID.
 * <p>
 * <img alt="" src="./doc-files/APP_MATERIAL_CUSTOMER_DATA.jpg" >
 */

public class JDBMaterialCustomerData
{

	private String dbErrorMessage;
	private String dbMaterial;
	private String dbCustomerID;
	private String dbDataID;
	private String dbData;
	
	private final Logger logger = Logger.getLogger(JDBMaterialCustomerData.class);
	private String hostID;
	private String sessionID;
	public static int field_data_id = 20;
	public static int field_data = 80;
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}



	public JDBMaterialCustomerData(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JDBMaterialCustomerData(String host, String session, String material, String customer, String dataid, String data)
	{
		setHostID(host);
		setSessionID(session);
		setMaterial(material);
		setCustomerID(customer);
		setDataID(dataid);
		setData(data);

	}

	public JDBMaterialCustomerData(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setMaterial(rs.getString("material"));
			setCustomerID(rs.getString("customer_id"));
			setDataID(rs.getString("data_id"));
			setData(rs.getString("data"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public ResultSet getMaterialDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public void clear()
	{
		setData("");
	}


	public boolean create()
	{

		logger.debug("create [" + getMaterial() + "] [" + getCustomerID() + "] [" + getDataID() + "]");

		boolean result = false;

		if (isValid() == true)
		{

			if (isValidMaterialCustomerData() == true)
			{
				setErrorMessage("Key violation - material [" + getMaterial() + " ] [" + getCustomerID() + "] [" + getDataID() + "] already exists !");
			} else
			{
				try
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialCustomerData.create"));
					stmtupdate.setString(1, getMaterial());
					stmtupdate.setString(2, getCustomerID());
					stmtupdate.setString(3, getDataID());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					update();
					result = true;
				} catch (SQLException e)
				{
					setErrorMessage(e.getMessage());
				}
			}
		}

		return result;
	}


	public boolean create(String material, String cust,String dataid)
	{
		boolean result = false;
		setMaterial(material);
		setCustomerID(cust);
		setDataID(dataid);
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialCustomerData.delete"));
			stmtupdate.setString(1, getMaterial());
			stmtupdate.setString(2, getCustomerID());
			stmtupdate.setString(3, getDataID());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}


	public boolean delete(String material, String cust,String dataid)
	{
		boolean result = false;
		setMaterial(material);
		setCustomerID(cust);
		setDataID(dataid);
		result = delete();
		return result;
	}


	public String getDataID()
	{
		return JUtility.replaceNullStringwithBlank(dbDataID);
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}


	public String getMaterial()
	{
		return dbMaterial;
	}


	public Vector<JDBMaterialCustomerData> getMaterialCustomerData(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBMaterialCustomerData> result = new Vector<JDBMaterialCustomerData>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBMaterialCustomerData(getHostID(), getSessionID(), "material", "customer_id", "data_id", "data"));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBMaterialCustomerData(getHostID(), getSessionID(), rs.getString("material"), rs.getString("customer_id"), rs.getString("data_id"), rs.getString("data")));
				}
				rs.close();

			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}
	
	public boolean getMaterialCustomerDataProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			if (getMaterial().equals("") == false)
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialCustomerData.getProperties"));
				stmt.setFetchSize(1);
				stmt.setString(1, getMaterial());
				stmt.setString(2, getCustomerID());
				stmt.setString(3, getDataID());
				rs = stmt.executeQuery();

				if (rs.next())
				{
					getPropertiesfromResultSet(rs);
					result = true;
				} else
				{
					setErrorMessage("Invalid Material Customer Data ID");
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}


	public boolean getMaterialCustomerDataProperties(String mat, String cust,String dataid)
	{
		setMaterial(mat);
		setCustomerID(cust);
		setDataID(dataid);
		return getMaterialCustomerDataProperties();
	}


	public String getCustomerID()
	{
		return dbCustomerID;
	}


	public String getData()
	{
		return JUtility.replaceNullStringwithBlank(dbData);
	}


	public boolean isValid()
	{
		boolean result = true;


		if (JUtility.isNullORBlank(dbMaterial) == true)
		{
			setErrorMessage("MATERIAL code cannot be null");
			result = false;
		}


		if (result == true)
		{
			if (JUtility.isNullORBlank(dbCustomerID) == true)
			{
				setErrorMessage("Customer code cannot be null");
				result = false;
			}
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(dbDataID) == true)
			{
				setErrorMessage("DataID code cannot be null");
				result = false;
			}
		}

		return result;
	}

	public boolean isValidMaterialCustomerData()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialCustomerData.isValid"));
			stmt.setString(1, getMaterial());
			stmt.setString(2, getCustomerID());
			stmt.setString(3, getDataID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Material / Customer / Data ID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidMaterialCustomerData :" + result);

		return result;

	}


	public boolean isValidMaterialCustomerData(String material, String cust,String dataid)
	{
		setMaterial(material);
		setCustomerID(cust);
		setDataID(dataid);
		return isValidMaterialCustomerData();
	}

	public void setDataID(String dataid)
	{
		dbDataID = JUtility.replaceNullStringwithBlank(dataid);

	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}


	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	public void setCustomerID(String cust)
	{
		dbCustomerID = cust;
	}


	public void setData(String data)
	{
		dbData = JUtility.replaceNullStringwithBlank(data);
	}


	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getMaterial() + "] [" + getCustomerID() + "] ["+ getDataID() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialCustomerData.update"));
				stmtupdate.setString(1, getData());
				stmtupdate.setString(2, getMaterial());
				stmtupdate.setString(3, getCustomerID());
				stmtupdate.setString(4, getDataID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;

			} catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

}
