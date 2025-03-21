package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBAutoLabeller.java
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

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBAutoLabeller class is used to insert/update/delete the APP_AUTO_LABELLER
 * table. When an operator assigns data to an auto labeler (e.g. Case Labeling
 * Screen) the list of destinations is retrieved from this class. You should
 * also refer to the classes JDBAutoLabellerResources, JDBPrinters and
 * JDBPrinterLineMembership
 * <p>
 * <img alt="" src="./doc-files/APP_AUTO_LABELLER.jpg" >
 * 
 * @see com.commander4j.db.JDBAutoLabellerResources JDBAutoLabellerResources
 * @see com.commander4j.db.JDBPrinters JDBPrinters
 * @see com.commander4j.db.JDBPrinterLineMembership JDBPrinterLineMembership
 */

public class JDBAutoLabeller
{
	private String hostID;
	private String sessionID;
	private String db_line;
	private String db_enabled = "Y";
	private String db_group;
	private String db_description;
	private String db_unique_id;
	private String db_sscc_range = "N";
	private String db_prefix_code = "";
	private String db_error_message = "";
	private Long db_sscc_sequence;
	private String db_modified = "N";
	private String db_ValidateResource = "N";
	private String db_ValidateWorkstation = "N";
	private JDBLabelData labdata;
	private JDBProcessOrder po;
	private JDBAutoLabellerResources alr;
	private JDBWorkstationLineMembership wlm;
	public static int field_line = 40;
	public static int field_description = 45;
	public static int field_group_id = 15;

	public JDBAutoLabeller(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		labdata = new JDBLabelData(getHostID(), getSessionID());
		po = new JDBProcessOrder(getHostID(), getSessionID());
		alr = new JDBAutoLabellerResources(getHostID(), getSessionID());
		wlm = new JDBWorkstationLineMembership(getHostID(), getSessionID());
		clear();
	}

	public void clear()
	{
		db_line = "";
		db_enabled = "Y";
		db_group = "";
		db_unique_id = "";
		db_error_message = "";
		db_sscc_sequence = 0L;
	}

	public String getEnabled()
	{
		return db_enabled;
	}
	
	public void setEnabled(String yesno)
	{
		db_enabled = yesno.toUpperCase();
	}
	
	public boolean isEnabled()
	{
		boolean result = false;
		if (db_enabled.equals("Y"))
		{
			result = true;
		}
		return result;
	}
	
	public void setEnabled(boolean yesno)
	{
		if (yesno==true)
		{
			db_enabled = "Y";
		}
		else
		{
			db_enabled = "N";
		}
	}
	
	public String getValidateResource()
	{
		return db_ValidateResource;
	}

	public String getValidateWorkstation()
	{
		return db_ValidateWorkstation;
	}

	public boolean isValidateResource()
	{
		if (db_ValidateResource.equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public boolean isValidateWorkstation()
	{
		if (db_ValidateWorkstation.equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void setValidateResource(boolean yesno)
	{
		if (yesno)
		{
			db_ValidateResource = "Y";
		} else
		{
			db_ValidateResource = "N";
		}
	}

	public void setValidateWorkstation(boolean yesno)
	{
		if (yesno)
		{
			db_ValidateWorkstation = "Y";
		} else
		{
			db_ValidateWorkstation = "N";
		}
	}

	public void setValidateResource(String yesno)
	{
		db_ValidateResource = yesno;
	}

	public void setValidateWorkstation(String yesno)
	{
		db_ValidateWorkstation = JUtility.replaceNullStringwithBlank(yesno);
		if (db_ValidateWorkstation.equals(""))
		{
			db_ValidateWorkstation = "N";
		}
	}

	public String getModified()
	{
		return db_modified;
	}

	public boolean isModified()
	{
		if (db_modified.equals("Y"))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void setModified(boolean yesno)
	{
		if (yesno)
		{
			db_modified = "Y";
		} else
		{
			db_modified = "N";
		}
	}

	public void setModified(String yesno)
	{
		db_modified = yesno;
	}

	public boolean resend(String line, String group, String modified)
	{
		boolean result = false;

		setErrorMessage("");
		setLine(line);
		setGroup(group);
		setModified(modified);

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.resend"));

			stmtupdate.setString(1, getModified());
			stmtupdate.setString(2, getLine());
			stmtupdate.setString(3, getGroup());
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

	public boolean create(String line, String description, String unique)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setLine(line);
			setDescription(description);
			setUniqueID(unique);

			if (isValidLineGroup() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.create"));
				stmtupdate.setString(1, getLine());
				stmtupdate.setString(2, getGroup());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getPrefixCode());
				stmtupdate.setString(5, getUniqueID());
				stmtupdate.setString(6, getModified());
				stmtupdate.setString(7, getValidateResource());
				stmtupdate.setString(8, getValidateWorkstation());
				stmtupdate.setString(9, getEnabled());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Line/Group already exists");
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
			if (isValidLineGroup() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.delete"));
				stmtupdate.setString(1, getLine());
				stmtupdate.setString(2, getGroup());
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

	public ResultSet getAutoLabellerDataResultSet(PreparedStatement criteria)
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

	public String getDescription()
	{
		return db_description;
	}
	
	public String getPrefixCode()
	{
		return db_prefix_code;
	}


	public String getErrorMessage()
	{
		return db_error_message;
	}

	private String getHostID()
	{
		return hostID;
	}

	public JDBLabelData getLabelData()
	{
		return labdata;
	}

	public LinkedList<JDBListData> getLabellerIDsforGroup(String group)
	{
		LinkedList<JDBListData> intList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.getLabellerIDsforGroup"));
			stmt.setString(1, group);
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBAutoLabeller al = new JDBAutoLabeller(getHostID(), getSessionID());
				al.getProperties(rs.getString("LINE"), rs.getString("GROUP_ID"));
				JDBListData mld = new JDBListData(null, index, true, al);
				intList.addLast(mld);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return intList;
	}

	public LinkedList<JDBListData> getLabellerIDs()
	{
		LinkedList<JDBListData> intList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.getLabellerIDs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBAutoLabeller al = new JDBAutoLabeller(getHostID(), getSessionID());
				al.getProperties(rs.getString("LINE"), rs.getString("GROUP_ID"));
				JDBListData mld = new JDBListData(null, index, true, al);
				intList.addLast(mld);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return intList;
	}

	public String getLine()
	{
		return db_line;
	}

	public String getGroup()
	{
		return db_group;
	}

	public boolean getProperties(String line, String group)
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");
		clear();
		setLine(line);
		setGroup(group);
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.getProperties"));
			stmt.setString(1, getLine());
			stmt.setString(2, getGroup());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Line/Group");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setLine(rs.getString("LINE"));
			setGroup(rs.getString("GROUP_ID"));
			setUniqueID(JUtility.replaceNullStringwithBlank(rs.getString("UNIQUE_ID")));
			setDescription(rs.getString("DESCRIPTION"));
			setPrefixCode(rs.getString("prefix_code"));
			setSSCCSequence(rs.getLong("SSCC_SEQUENCE"));
			setSSCCRangeEnable(rs.getString("USE_SSCC_RANGE"));
			setModified(rs.getString("MODIFIED"));
			setValidateResource(rs.getString("VALIDATE_RESOURCE"));
			setValidateWorkstation(rs.getString("VALIDATE_WORKSTATION"));
			setEnabled(rs.getString("ENABLED"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getSSCCRangeEnable()
	{
		return db_sscc_range;
	}

	public Long getSSCCSequence()
	{
		return db_sscc_sequence;
	}

	public String getUniqueID()
	{
		return db_unique_id;
	}

	public boolean isSSCCRangeEnabled()
	{
		boolean result = false;
		if (db_sscc_range.equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public boolean isValidLineGroup()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.isValidLine"));
			stmt.setFetchSize(1);
			stmt.setString(1, getLine());
			stmt.setString(2, getGroup());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Line / Group - (" + getLine() + "/" + getGroup() + ")");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean rename(String oldLine, String newLine, String group)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		setLine(oldLine);
		setGroup(group);
		try
		{
			if (isValidLineGroup() == true)
			{
				JDBAutoLabeller newLineDB = new JDBAutoLabeller(getHostID(), getSessionID());
				newLineDB.setLine(newLine);

				if (newLineDB.isValidLineGroup() == false)
				{

					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.renameTo"));
					stmtupdate.setString(1, newLine);
					stmtupdate.setString(2, oldLine);
					stmtupdate.setString(3, group);
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setLine(newLine);
					result = true;
				} else
				{
					setErrorMessage("New line already exists");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void setDescription(String desc)
	{
		db_description = JUtility.replaceNullStringwithBlank(desc);
	}

	public void setPrefixCode(String code)
	{
		db_prefix_code = JUtility.replaceNullStringwithBlank(code);
	}

	
	private void setErrorMessage(String msg)
	{
		db_error_message = msg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setLine(String line)
	{
		db_line = line;
	}

	public void setGroup(String group)
	{
		db_group = group;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setSSCCRangeEnable(String enable)
	{
		db_sscc_range = enable;
	}

	public void setSSCCRangeEnabled(boolean enabled)
	{
		if (enabled)
		{
			db_sscc_range = "Y";
		} else
		{
			db_sscc_range = "N";
		}
	}

	public void setSSCCSequence(Long sequence)
	{
		db_sscc_sequence = sequence;
	}

	public void setUniqueID(String po)
	{
		db_unique_id = JUtility.replaceNullStringwithBlank(po);
		labdata.getProperties(db_unique_id);

	}

	public String toString()
	{
		return JUtility.padString(getLine(), true, field_line, " ") + " " +getDescription();
	}

	public boolean isValidClientWorkstationID(String client)
	{
		boolean result = false;

		if (isValidateWorkstation())
		{
			wlm.setLineId(getLine());
			wlm.setGroupId(getGroup());
			wlm.setWorkstationId(client.toUpperCase());
			if (wlm.isWorkstationAssignedToLine())
			{
				result = true;
			}
		} else
		{
			result = true;
		}

		return result;
	}

	public boolean isValidProcessOrderResource(String pOrder)
	{
		boolean result = false;

		if (isValidateResource())
		{
			if (po.getProcessOrderProperties(pOrder))
			{

				if (alr.isValidLineResource(getLine(), getGroup(), po.getRequiredResource()))
				{
					result = true;
				}
			}
		} else
		{
			result = true;
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidLineGroup() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.update"));
				stmtupdate.setString(1, getUniqueID());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getPrefixCode());
				stmtupdate.setString(4, getSSCCRangeEnable());
				stmtupdate.setString(5, getModified());
				stmtupdate.setString(6, getValidateResource());
				stmtupdate.setString(7, getValidateWorkstation());
				stmtupdate.setString(8, getEnabled());
				stmtupdate.setString(9, getLine());
				stmtupdate.setString(10, getGroup());
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

	public boolean updateSSCC()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidLineGroup() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBAutoLabeller.updateSSCC"));
				stmtupdate.setLong(1, getSSCCSequence());
				stmtupdate.setString(2, getLine());
				stmtupdate.setString(3, getGroup());
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

}