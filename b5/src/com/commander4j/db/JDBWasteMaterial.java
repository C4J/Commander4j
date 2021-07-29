package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteLocations.java
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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBWasteMaterial class updates the table
 * APP_WASTE_MATERIAL. This table contains a single row for each unique
 * Waste Location as stored in the APP_WASTE_MATERIAL table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_MATERIAL.jpg" >
 * 
 * @see com.commander4j.db.JDBWasteReasons JDBWasteReasons
 * @see com.commander4j.db.JDBWasteReportingIDS JDBWasteReportingIDS
 * @see com.commander4j.db.JDBWasteTransactionType JDBWasteTransactionType
 * @see com.commander4j.db.JDBWasteLocationReporting JDBWasteLocationReporting
 * @see com.commander4j.db.JDBWasteReasons JDBWasteTypes
 * @see com.commander4j.db.JDBWasteReasons JDBWasteMaterial
 * @see com.commander4j.db.JDBWasteReasons JDBWasteLog
 * 
 */

public class JDBWasteMaterial
{
	public static int field_WasteMaterialID = 25;
	public static int field_Description = 80;
	public static int field_WasteTypeID_id = 25;
	public static int field_UOM = 3;
	public static int field_Enabled = 1;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;
	
	private String dbErrorMessage;
	private String dbWasteMaterialID;  /* PK */
	private String dbWasteTypeID;
	private BigDecimal dbCostPerKG;
	private String dbDescription;
	private String dbEnabled;
	
	private final Logger logger = Logger.getLogger(JDBWasteMaterial.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteMaterial(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public void clear()
	{

		setWasteTypeID("");
		setCostPerKG(new BigDecimal("0"));
		setDescription("");
		setEnabled(true);
	}
	
	public void setDisplayMode(int mode)
	{
		displayMode = mode;
	}
	
	public boolean create(String res)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setWasteMaterialID(res);
			clear();

			if (isValidWasteMaterial() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.create"));
				stmtupdate.setString(1, getWasteMaterialID());
				stmtupdate.setString(2, getWasteTypeID());
				stmtupdate.setBigDecimal(3, getCostPerKG());
				stmtupdate.setString(4, getDescription());
				stmtupdate.setString(5, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Material " + getWasteMaterialID() + " already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteMaterial() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.delete"));
				stmtupdate.setString(1, getWasteMaterialID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public BigDecimal getCostPerKG()
	{
		return dbCostPerKG;
	}
	
	public void setCostPerKG(BigDecimal val)
	{
		dbCostPerKG = val;
	}
	
	public String getWasteTypeID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteTypeID).trim();
	}

	public String getWasteTypeID(String res)
	{
		String result = "";

		if (getWasteMaterialProperties(res))
		{
			result = getWasteTypeID();
		}

		return result;
	}
	
	public String getDescription()
	{
		return JUtility.replaceNullStringwithBlank(dbDescription);
	}

	public String getEnabled()
	{
		String result = JUtility.replaceNullStringwithBlank(dbEnabled);
		if (result.equals(""))
		{
			dbEnabled = "N";
			result = "N";
		}
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public ResultSet getWasteMaterialResultSet(PreparedStatement criteria)
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

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setWasteMaterialID(rs.getString("waste_material_id"));
			setWasteTypeID(rs.getString("waste_type_id"));
			setCostPerKG(rs.getBigDecimal("cost_per_kg"));
			setDescription(rs.getString("description"));
			setEnabled(rs.getString("enabled"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteMaterialID()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteMaterialID);
	}

	public boolean getWasteMaterialProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.getProperties"));
			stmt.setString(1, getWasteMaterialID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Material [" + getWasteMaterialID()+"]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public ResultSet getWasteMaterialsDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.getWasteMaterials"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public boolean getWasteMaterialProperties(String res)
	{
		setWasteMaterialID(res);
		return getWasteMaterialProperties();
	}
	
	public Icon getMaterialIcon()
	{
		Icon icon = new ImageIcon();

		try
		{
			if (isEnabled() == false)
			{
				icon = Common.icon_cancel_16x16;
			}
			else
			{
				icon = Common.icon_ok_16x16;

			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		return icon;
	}
	
	public String getHTMLPullDownCombo(String itemName, String defaultValue, String onchange)
	{
		String result = "";
		String selected = "";
		LinkedList<JDBWasteMaterial> materialList = new LinkedList<JDBWasteMaterial>();
		
		materialList.addAll(getWasteMaterialsList(true,displayModeShort));
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\" " +onchange + "\">";
		result = result + "<OPTION></OPTION>";
		
		if (materialList.size() > 0)
		{
			for (int x = 0; x < materialList.size(); x++)
			{
				if (materialList.get(x).getWasteMaterialID().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + materialList.get(x).getWasteMaterialID()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}	
	
	public LinkedList<JDBWasteMaterial> getWasteMaterialsList(Boolean enabled,int mode) {

		LinkedList<JDBWasteMaterial> wasteMaterialList = new LinkedList<JDBWasteMaterial>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.getWasteMaterials"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteMaterial samp = new JDBWasteMaterial(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{	
					wasteMaterialList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteMaterialList;
	}

	
	public String getHTMLPullDownCombofoLocation(String itemName, String defaultValue,String location,String onchange)
	{
		String result = "";
		String selected = "";
		LinkedList<JDBWasteMaterial> materialList = new LinkedList<JDBWasteMaterial>();
		
		materialList.addAll(getWasteMaterialIDsforlLocationList(location,true,displayModeShort));
		
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\" " +onchange + "\">";
		result = result + "<OPTION></OPTION>";
		
		if (materialList.size() > 0)
		{
			for (int x = 0; x < materialList.size(); x++)
			{
				if (materialList.get(x).getWasteMaterialID().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + materialList.get(x).getWasteMaterialID()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}
	
	public LinkedList<JDBWasteMaterial> getWasteMaterialIDsforlLocationList(String location,Boolean enabled,int mode) {

		LinkedList<JDBWasteMaterial> wasteMaterialList = new LinkedList<JDBWasteMaterial>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLocationMaterial.getMaterialsforLocation"));
			stmt.setFetchSize(250);
			stmt.setString(1, location);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteMaterial samp = new JDBWasteMaterial(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{	
					wasteMaterialList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteMaterialList;
	}
	
	public LinkedList<JDBListData> getWasteMaterials(Boolean enabled,int mode) {
		
		LinkedList<JDBListData> wasteMaterialList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.getWasteMaterials"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteMaterial samp = new JDBWasteMaterial(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getMaterialIcon();
				
				if (samp.isEnabled().equals(enabled))
				{
					JDBListData mld = new JDBListData(icon, index, true, samp);
					
					wasteMaterialList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return wasteMaterialList;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public Boolean isEnabled()
	{
		Boolean result = false;
		if (getEnabled().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}

	public boolean isValidWasteMaterial()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.isValidWasteMaterial"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteMaterialID());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Material ID [" + getWasteMaterialID() + "] not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean isValidWasteMaterialLocation(String material,String location)
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterialLocation.isValid"));
			stmt.setFetchSize(1);
			stmt.setString(1,material);		
			stmt.setString(2,location);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Material ["+material+"] invalid for Location ["+location+"]");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidWasteMaterial(String res)
	{
		setWasteMaterialID(res);

		return isValidWasteMaterial();
	}

	public void setWasteTypeID(String str)
	{
		dbWasteTypeID = str;
	}
	
	public void setDescription(String desc)
	{
		dbDescription = desc;
	}

	public void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			setEnabled("Y");
		} else
		{
			setEnabled("N");
		}
	}

	public void setEnabled(String enabled)
	{
		dbEnabled = enabled;
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

	public void setWasteMaterialID(String res)
	{
		dbWasteMaterialID = res;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";

		if (displayMode==displayModeFull)
		{
			result = JUtility.padString(getWasteMaterialID().toString(), true, field_WasteMaterialID, " ")+	getDescription();
		}
		
		if (displayMode==displayModeShort)
		{
			result = getWasteMaterialID();
		}
		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteMaterial() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.update"));
				stmtupdate.setString(1, getWasteTypeID());
				stmtupdate.setBigDecimal(2, getCostPerKG());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.setString(5, getWasteMaterialID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean rename(String oldMaterialID,String newMaterialID)
	{
		boolean result = false;
		setWasteMaterialID(oldMaterialID);
		setErrorMessage("");

		try
		{
			if (isValidWasteMaterial(oldMaterialID) == true)
			{
				if (isValidWasteMaterial(newMaterialID) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteMaterial.renameMaterialID"));
					stmtupdate.setString(1, newMaterialID);
					stmtupdate.setString(2, oldMaterialID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.renameMaterialID"));
					stmtupdate.setString(1, newMaterialID);
					stmtupdate.setString(2, oldMaterialID);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteMaterialID(newMaterialID);
					
					result = true;
				}
				else
				{
					setErrorMessage(newMaterialID + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
