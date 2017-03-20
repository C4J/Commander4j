package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterialUom.java
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

import com.commander4j.bar.JEANUtility;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBMaterialUom
{

	private String dbErrorMessage;
	private String dbMaterial;
	private Integer dbMaterialDenominator;
	private String dbMaterialEan;
	private Integer dbMaterialNumerator;
	private String dbMaterialUom;
	private String dbMaterialVariant;
	private String dbMaterialOverride;
	public static int field_ean = 14;
	public static int field_variant = 2;
	private final Logger logger = Logger.getLogger(JDBMaterialUom.class);
	private String hostID;
	private String sessionID;

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


	private JDBUom uom;

	public JDBMaterialUom(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		uom = new JDBUom(getHostID(), getSessionID());
	}

	public JDBMaterialUom(String host, String session, String material, String uom, String ean, String variant, Integer numerator, Integer denominator,String override)
	{
		setHostID(host);
		setSessionID(session);
		setMaterial(material);
		setUom(uom);
		setEan(ean);
		setVariant(variant);
		setNumerator(numerator);
		setDenominator(denominator);
		setOverride(override);
	}

	public JDBMaterialUom(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();

			setMaterial(rs.getString("material"));
			setUom(rs.getString("uom"));
			setEan(rs.getString("ean"));
			setVariant(rs.getString("variant"));
			setNumerator(rs.getInt("numerator"));
			setDenominator(rs.getInt("denominator"));
			setOverride(rs.getString("override"));

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
		// setMaterial("");
		// setUom("");
		setEan("");
		setVariant("");
		setNumerator(0);
		setDenominator(0);
		setOverride("");
	}

	/**
	 * Method create.
	 * 
	 * @return boolean
	 */
	public boolean create()
	{

		logger.debug("create [" + getMaterial() + "] [" + getUom() + "]");

		boolean result = false;

		if (isValid() == true)
		{

			if (isValidMaterialUom() == true)
			{
				setErrorMessage("Key violation - material [" + getMaterial() + " ] [" + getUom() + "] already exists !");
			} else
			{
				try
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.create"));
					stmtupdate.setString(1, getMaterial());
					stmtupdate.setString(2, getUom());
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

	/**
	 * Method create.
	 * 
	 * @param material
	 *            String
	 * @param uom
	 *            String
	 * @return boolean
	 */
	public boolean create(String material, String uom)
	{
		boolean result = false;
		setMaterial(material);
		setUom(uom);
		result = create();
		return result;
	}

	/**
	 * Method delete.
	 * 
	 * @return boolean
	 */
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.delete"));
			stmtupdate.setString(1, getMaterial());
			stmtupdate.setString(2, getUom());
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

	/**
	 * Method delete.
	 * 
	 * @param material
	 *            String
	 * @param uom
	 *            String
	 * @return boolean
	 */
	public boolean delete(String material, String uom)
	{
		boolean result = false;
		setMaterial(material);
		setUom(uom);
		result = delete();
		return result;
	}

	/**
	 * Method getDenominator.
	 * 
	 * @return Integer
	 */
	public Integer getDenominator()
	{
		return dbMaterialDenominator;
	}
	
	public String getOverride()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialOverride);
	}

	/**
	 * Method getEan.
	 * 
	 * @return String
	 */
	public String getEan()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialEan);
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	/**
	 * Method getMaterial.
	 * 
	 * @return String
	 */
	public String getMaterial()
	{
		return dbMaterial;
	}


	public Vector<JDBMaterialUom> getMaterialUomData(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBMaterialUom> result = new Vector<JDBMaterialUom>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBMaterialUom(getHostID(), getSessionID(), "material", "uom", "variant", "ean", 0, 0,""));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBMaterialUom(getHostID(), getSessionID(), rs.getString("material"), rs.getString("uom"), rs.getString("ean"), rs.getString("variant"), rs.getInt("numerator"), rs.getInt("denominator"),rs.getString("override")));
				}
				rs.close();

			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}


	public boolean getMaterialUomProperties()
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
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.getMaterialUomProperties"));
				stmt.setFetchSize(1);
				stmt.setString(1, getMaterial());
				stmt.setString(2, getUom());
				rs = stmt.executeQuery();

				if (rs.next())
				{
					getPropertiesfromResultSet(rs);
					result = true;
				} else
				{
					setErrorMessage("Invalid Material Uom");
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


	public boolean getMaterialUomProperties(String mat, String uom)
	{
		setMaterial(mat);
		setUom(uom);
		return getMaterialUomProperties();
	}


	public Vector<JDBUom> getMaterialUoms()
	{
		Vector<JDBUom> uomList = new Vector<JDBUom>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.getMaterialUoms"));
			stmt.setFetchSize(250);
			stmt.setString(1, getMaterial());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBUom uom = new JDBUom(getHostID(), getSessionID());
				uom.setInternalUom(rs.getString("uom"));
				uom.getInternalUomProperties();
				uomList.add(uom);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return uomList;
	}


	public Vector<JDBUom> getMaterialUoms(String material)
	{
		setMaterial(material);
		return getMaterialUoms();
	}


	public Integer getNumerator()
	{
		return dbMaterialNumerator;
	}


	public String getUom()
	{
		return dbMaterialUom;
	}


	public String getVariant()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterialVariant);
	}


	public boolean isValid()
	{
		boolean result = true;

		/* Check Material */
		if (JUtility.isNullORBlank(dbMaterial) == true)
		{
			setErrorMessage("MATERIAL code cannot be null");
			result = false;
		}

		/* Check Base UOM */
		if (result == true)
		{
			if (JUtility.isNullORBlank(dbMaterialUom) == true)
			{
				setErrorMessage("UOM code cannot be null");
				result = false;
			} else
			{
				uom.setInternalUom(dbMaterialUom);
				result = uom.isValidInternalUom();
				if (result == false)
					setErrorMessage(uom.getErrorMessage());
			}
		}

		if (result == true)
		{
			if (getEan().length() == 14)
			{

				if (JEANUtility.isValidCheckDigit(13, getEan()) == false)
				{
					setErrorMessage("EAN Invalid check digit " + getEan().substring(0, 13) + "[" + getEan().substring(13, 14) + "], expected " + getEan().substring(0, 13) + "[" + JEANUtility.calcCheckDigit(13, getEan()) + "]");
					result = false;
				}
			} else
			{
				if (getEan().equals("") == false)
				{
					setErrorMessage("14 digits required.");
					result = false;
				}
			}
		}

		if (result == false)
		{
			logger.debug("isValid [" + getMaterial() + "] " + getErrorMessage());
		}

		return result;
	}


	public boolean isValidMaterialUom()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.isValidMaterialUom"));
			stmt.setString(1, getMaterial());
			stmt.setString(2, getUom());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Material / Uom");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidMaterialUom :" + result);

		return result;

	}


	public boolean isValidMaterialUom(String material, String uom)
	{
		setMaterial(material);
		setUom(uom);
		return isValidMaterialUom();
	}


	public void setDenominator(Integer denominator)
	{
		dbMaterialDenominator = denominator;
	}

	public void setOverride(String override)
	{
		dbMaterialOverride = JUtility.replaceNullStringwithBlank(override).toUpperCase();
	}
	

	public void setEan(String ean)
	{
		ean = JUtility.replaceNullStringwithBlank(ean);
		if (ean.equals("") == true)
		{
			dbMaterialEan = "";
		} else
		{
			dbMaterialEan = (JUtility.padString(ean, false, 14, "0"));
		}
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


	public void setNumerator(Integer numerator)
	{
		dbMaterialNumerator = numerator;
	}


	public void setUom(String uom)
	{
		dbMaterialUom = uom;
	}


	public void setVariant(String variant)
	{
		variant = JUtility.replaceNullStringwithBlank(variant);
		if (variant.equals("") == true)
		{
			dbMaterialVariant = "";
		} else
		{
			dbMaterialVariant = (JUtility.padString(variant, false, 2, "0"));
		}
	}


	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getMaterial() + "] [" + getUom() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialUom.update"));

				stmtupdate.setString(1, getEan());
				stmtupdate.setString(2, getVariant());
				stmtupdate.setInt(3, getNumerator());
				stmtupdate.setInt(4, getDenominator());
				stmtupdate.setString(5, getOverride());
				stmtupdate.setString(6, getMaterial());
				stmtupdate.setString(7, getUom());
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
