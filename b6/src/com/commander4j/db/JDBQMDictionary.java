package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMDictionary.java
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
import javax.swing.JLabel;
import org.apache.logging.log4j.Logger;
import com.commander4j.gui.JCheckListItem;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBQMDictionary class is used to insert/update/delete records from the
 * APP_QM_DICTIONARY table. This table is part of the Quality Module and defines
 * the data required to be captured by the user in the Sample Results Screen.
 * <p>
 * <img alt="" src="./doc-files/APP_QM_DICTIONARY.jpg" >
 * 
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 */
public class JDBQMDictionary
{
	private String dbTestID;
	private int dbFieldAlign;
	private int dbFieldWidth;
	private String dbDescription;
	private String dbDataType;
	private String dbUOM;
	private String dbRequired;
	private String dbSelectListID;
	private String dbVisible;
	private Long dbExtensionID;

	private String dbErrorMessage;
	public static int field_test_id = 50;
	public static int field_description = 50;
	public static int field_datatype = 50;
	public static int field_uom = 50;

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBQMDictionary.class);
	private String hostID;
	private String sessionID;

	public JDBQMDictionary(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBQMDictionary(String host, String session, String testid, int align, String datatype, String uom, String required, String description, String visible)
	{
		setHostID(host);
		setSessionID(session);
		setTestID(testid);
		setFieldAlign(align);
		setDataType(datatype);
		setUOM(uom);
		setRequired(required);
		setDescription(description);
		setVisible(visible);
	}

	public void clear()
	{
		setFieldAlign(JLabel.TRAILING);
		setFieldWidth(50);
		setDataType("");
		setUOM("");
		setRequired("");
		setDescription("");
		setVisible("");
		setExtensionID((long) -1);
	}

	public boolean create(String testid, int align, String datatype, String uom, String required, String description, String visible, int wdth)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			setTestID(testid);
			setFieldAlign(align);
			setDataType(datatype);
			setUOM(uom);
			setRequired(required);
			setDescription(description);
			setVisible(visible);
			setFieldWidth(wdth);

			if (isValid() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.create"));
				stmtupdate.setString(1, getTestID());
				stmtupdate.setLong(2, getFieldAlign());
				stmtupdate.setString(3, getDescription());
				stmtupdate.setString(4, getDataType());
				stmtupdate.setString(5, getUOM());
				stmtupdate.setString(6, getRequired());
				stmtupdate.setString(7, getSelectListID());
				stmtupdate.setString(8, getVisible());
				stmtupdate.setLong(9, getExtensionID());
				stmtupdate.setLong(10, getFieldWidth());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("QMDictionary item already exists");
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
			if (isValid() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.delete"));
				stmtupdate.setString(1, getTestID());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public Class getDataClass()
	{
		Class result;
		result = Common.datatypeClass.get(getDataType());
		return result;
	}

	public String getDataType()
	{
		String result = "";
		if (JUtility.replaceNullStringwithBlank(dbSelectListID).equals(""))
		{
			result = dbDataType;
		} else
		{
			result = "list";
		}

		return result;
	}

	public String getDescription()
	{
		String result = "";
		if (dbDescription != null)
			result = dbDescription;
		return result;
	}

	public PreparedStatement getDictionaryDataPreparedStatement()
	{

		PreparedStatement stmt;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.getTests"));
			stmt.setFetchSize(100);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			stmt = null;
		}

		return stmt;
	}

	public ResultSet getDictionaryDataResultSet(PreparedStatement stmt)
	{

		ResultSet rs;

		try
		{
			rs = stmt.executeQuery();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
			rs = null;
		}

		return rs;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public Long getExtensionID()
	{
		Long result = (long) -1;
		if (dbExtensionID != null)
			result = dbExtensionID;
		return result;
	}

	public int getFieldAlign()
	{
		int result = 0;
		result = dbFieldAlign;
		return result;
	}

	public String getFieldAlignment()
	{
		String result = "Left";

		if (getFieldAlign() == JLabel.LEFT)
			result = "Left";

		if (getFieldAlign() == JLabel.CENTER)
			result = "Center";

		if (getFieldAlign() == JLabel.RIGHT)
			result = "Right";

		return result;
	}

	public void setFieldAlignment(String align)
	{
		int result = JLabel.LEFT;

		if (align.equals("Left"))
			result = JLabel.LEFT;

		if (align.equals("Center"))
			result = JLabel.CENTER;

		if (align.equals("Right"))
			result = JLabel.RIGHT;

		setFieldAlign(result);
	}

	public int getFieldWidth()
	{
		return dbFieldWidth;
	}

	private String getHostID()
	{
		return hostID;
	}

	public boolean getProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.getProperties"));
			stmt.setString(1, getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getValuesFromResultSet(rs);
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Dictionary Item [" + getTestID().toString() + "]");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getProperties(String testid)
	{
		setTestID(testid);
		return getProperties();
	}

	public String getRequired()
	{
		String result = "";
		if (dbRequired != null)
			result = dbRequired;
		return result;
	}

	public String getSelectListID()
	{
		String result = "";
		if (dbSelectListID != null)
			result = dbSelectListID;
		return result;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public Vector<JCheckListItem> getTestCheckListList()
	{
		Vector<JCheckListItem> testList = new Vector<JCheckListItem>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.getTests"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMDictionary mt = new JDBQMDictionary(getHostID(), getSessionID());
				mt.getValuesFromResultSet(rs);
				if (mt.getVisible().equals("Y"))
				{
					JCheckListItem ci = new JCheckListItem(mt);
					testList.add(ci);
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return testList;
	}

	public String getTestID()
	{
		String result = "";
		if (dbTestID != null)
			result = dbTestID;
		return result;
	}

	public LinkedList<JDBQMDictionary> getTests()
	{
		LinkedList<JDBQMDictionary> testList = new LinkedList<JDBQMDictionary>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.getTests"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBQMDictionary mt = new JDBQMDictionary(getHostID(), getSessionID());
				mt.getValuesFromResultSet(rs);
				testList.add(mt);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return testList;
	}

	public String getUOM()
	{
		String result = "";
		if (dbUOM != null)
			result = dbUOM;
		return result;
	}

	public void getValuesFromResultSet(ResultSet rs)
	{
		try
		{
			setTestID(rs.getString("test_id"));
			setDataType(rs.getString("datatype"));
			setUOM(rs.getString("uom"));
			setRequired(rs.getString("required"));
			setSelectListID(rs.getString("select_list_id"));
			setDescription(rs.getString("description"));
			setVisible(rs.getString("visible"));
			setExtensionID(rs.getLong("extension_id"));
			setFieldAlign(rs.getInt("field_alignment"));
			setFieldWidth(rs.getInt("field_width"));
		} catch (Exception ex)
		{

		}
	}

	public String getVisible()
	{
		String result = "";
		if (dbVisible != null)
			result = dbVisible;
		return result;
	}

	public boolean isValid()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.isValid"));
			stmt.setString(1, getTestID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Dictionary Item [" + getTestID().toString() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValid(String testid)
	{
		setTestID(testid);
		return isValid();
	}

	public void setDataType(String datatype)
	{
		dbDataType = datatype;
		if (dbDataType.equals("list") == false)
		{
			dbSelectListID = "";
		}
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

	public void setExtensionID(Long extensionid)
	{
		dbExtensionID = extensionid;
	}

	public void setFieldAlign(int align)
	{
		dbFieldAlign = align;
	}

	public void setFieldWidth(int fwidth)
	{
		dbFieldWidth = fwidth;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setRequired(String required)
	{
		required = JUtility.replaceNullStringwithBlank(required);
		if (required.equals("true"))
			required = "Y";
		if (required.equals("X"))
			required = "Y";
		if (required.equals("false"))
			required = "N";
		if (required.equals(""))
			required = "N";
		dbRequired = required;
	}

	public void setSelectListID(String selectlist)
	{
		dbSelectListID = selectlist;
		if (dbSelectListID.equals("") == false)
		{
			dbDataType = "list";
		}
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setTestID(String testid)
	{
		dbTestID = testid;
	}

	public void setUOM(String uom)
	{
		dbUOM = uom;
	}

	public void setVisible(String visible)
	{
		dbVisible = visible;
	}

	public String toString()
	{
		String result = "";
		if (getTestID().equals("") == false)
		{
			// result = JUtility.padString(getTestID(), true, field_test_id, "
			// ") + " - " +
			result = getDescription();
		} else
		{
			result = "";
		}

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");
		// update {schema}app_qm_dictionary set
		// sample_frequency=?,description=?,datatype=?,uom=?,required=?,select_list_id=?,visible=?
		// where test_id = ?
		try
		{
			if (isValid() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBQMDictionary.update"));
				stmtupdate.setLong(1, getFieldAlign());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getDataType());
				stmtupdate.setString(4, getUOM());
				stmtupdate.setString(5, getRequired());
				stmtupdate.setString(6, getSelectListID());
				stmtupdate.setString(7, getVisible());
				stmtupdate.setLong(8, getFieldWidth());
				stmtupdate.setString(9, getTestID());
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
