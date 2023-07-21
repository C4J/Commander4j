package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBMaterialType.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBMaterialType class is used to insert/update/delete records from the
 * APP_MATERIAL_TYPE table. This table contains a list of valid types for a
 * Material. A type is used to filter materials for reporting purposes.
 * <p>
 * <img alt="" src="./doc-files/APP_MATERIAL_TYPE.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterial JDBMaterial
 */
public class JDBMaterialType
{

	private String dbErrorMessage;
	private String dbMaterialDescription;
	private String dbMaterialType;
	public static int field_material_type = 5;
	public static int field_description = 80;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBMaterialType.class);
	private String hostID;
	private String sessionID;
	private String dbOverride_Pack_Label;
	private String dbOverride_Pallet_Label;
	private String dbPack_Label_ModuleID;
	private String dbPallet_Label_ModuleID;

	public void setOverridePackLabel(boolean yesno)
	{
		if (yesno)
		{
			setOverridePackLabel("Y");
		} else
		{
			setOverridePackLabel("N");
		}
	}

	public void setOverridePackLabel(String yesno)
	{
		dbOverride_Pack_Label = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isOverridePackLabel()
	{
		if (getOverridePackLabel().equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public String getOverridePackLabel()
	{
		dbOverride_Pack_Label = JUtility.replaceNullStringwithBlank(dbOverride_Pack_Label);
		if (dbOverride_Pack_Label.equals(""))
		{
			dbOverride_Pack_Label = "N";
		}
		return dbOverride_Pack_Label;

	}

	public void setPackLabelModuleID(String id)
	{
		dbPack_Label_ModuleID = id;
	}

	public String getPackLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPack_Label_ModuleID);
	}

	public void setOverridePalletLabel(boolean yesno)
	{
		if (yesno)
		{
			setOverridePalletLabel("Y");
		} else
		{
			setOverridePalletLabel("N");
		}
	}

	public void setOverridePalletLabel(String yesno)
	{
		dbOverride_Pallet_Label = JUtility.replaceNullStringwithBlank(yesno);
	}

	public boolean isOverridePalletLabel()
	{
		if (getOverridePalletLabel().equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public String getOverridePalletLabel()
	{
		dbOverride_Pallet_Label = JUtility.replaceNullStringwithBlank(dbOverride_Pallet_Label);
		if (dbOverride_Pallet_Label.equals(""))
		{
			dbOverride_Pallet_Label = "N";
		}
		return dbOverride_Pallet_Label;

	}

	public void setPalletLabelModuleID(String id)
	{
		dbPallet_Label_ModuleID = id;
	}

	public String getPalletLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPallet_Label_ModuleID);
	}

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

	public JDBMaterialType(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public boolean create(String ltype, String ldescription)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setType(ltype);

			setDescription(ldescription);

			if (isValidMaterialType() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.create"));
				stmtupdate.setString(1, getType());
				stmtupdate.setString(2, getDescription());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Material Type already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidMaterialType() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getOverridePackLabel());
				stmtupdate.setString(3, getPackLabelModuleID());
				stmtupdate.setString(4, getOverridePalletLabel());
				stmtupdate.setString(5, getPalletLabelModuleID());
				stmtupdate.setString(6, getType());
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

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidMaterialType() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.delete"));
				stmtupdate.setString(1, getType());
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

	public boolean renameTo(String newType)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidMaterialType() == true)
			{
				JDBMaterialType mattype = new JDBMaterialType(getHostID(), getSessionID());
				mattype.setType(newType);
				if (mattype.isValidMaterialType() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.renameTo"));
					stmtupdate.setString(1, newType);
					stmtupdate.setString(2, getType());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setType(newType);
					result = true;
				} else
				{
					setErrorMessage("Material Type is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidMaterialType()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.isValidMaterialType"));
			stmt.setString(1, getType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Material Type [" + getType() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValidMaterialType(String type)
	{
		setType(type);
		return isValidMaterialType();
	}

	public JDBMaterialType(String host, String session, String type, String description)
	{
		setHostID(host);
		setSessionID(session);
		setType(type);
		setDescription(description);
	}

	public String getDescription()
	{
		String result = "";
		if (dbMaterialDescription != null)
			result = dbMaterialDescription;
		return result;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public void clear()
	{
		// setType("");
		setDescription("");
		setErrorMessage("");
	}

	public boolean getMaterialTypeProperties(String materialType)
	{
		setType(materialType);
		return getMaterialTypeProperties();
	}

	public boolean getMaterialTypeProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMaterialTypeProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.getMaterialTypeProperties"));
			stmt.setString(1, getType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDescription(rs.getString("description"));
				setOverridePackLabel(rs.getString("override_pack_label"));
				setPackLabelModuleID(rs.getString("pack_label_module_id"));
				setOverridePalletLabel(rs.getString("override_pallet_label"));
				setPalletLabelModuleID(rs.getString("pallet_label_module_id"));
				result = true;
			} else
			{
				setErrorMessage("Invalid Material Type");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public Vector<JDBMaterialType> getMaterialTypes()
	{
		Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialType.getMaterialTypes"));
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBMaterialType mt = new JDBMaterialType(getHostID(), getSessionID());
				mt.setType(rs.getString("material_type"));
				mt.setDescription(rs.getString("description"));
				mt.setOverridePackLabel(rs.getString("override_pack_label"));
				mt.setOverridePalletLabel(rs.getString("override_pallet_label"));
				mt.setPackLabelModuleID(rs.getString("pack_label_module_id"));
				mt.setPalletLabelModuleID(rs.getString("pallet_label_module_id"));
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	public String getType()
	{
		String result = "";
		if (dbMaterialType != null)
			result = dbMaterialType;
		return result;
	}

	public void setDescription(String description)
	{
		dbMaterialDescription = description;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public void setType(String type)
	{
		dbMaterialType = type;
	}

	public String toString()
	{
		String result = "";
		if (getType().equals("") == false)
		{
			result = JUtility.padString(getType(), true, field_material_type, " ") + " - " + getDescription();
		} else
		{
			result = "";
		}

		return result;
	}
}
