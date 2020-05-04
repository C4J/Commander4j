package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWasteTransactionType.java
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
 * The JDBWasteTransactionType class updates the table
 * APP_WASTE_TRANSACTIONS. This table contains a single row for each unique
 * Waste Transaction Type as stored in the APP_WASTE_TRANSACTIONS table. 
 * <p>
 * <img alt="" src="./doc-files/APP_WASTE_TRANSACTIONS.jpg" >
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

public class JDBWasteTransactionType
{
	public static int field_WasteTransactionType = 25;
	public static int field_Description = 80;
	public static int field_Include_in_Totals = 1;
	public static int field_Enabled = 1;
	public static int field_store_as_negative = 1;
	
	public static int displayModeFull = 0;
	public static int displayModeShort = 1;
	
	private String dbErrorMessage;
	private String dbWasteTransactionType;  /* PK */
	private String dbDescription;
	private String dbIncludeInTotals;
	private String dbStoreAsNegative;
	private String dbEnabled;
	
	private final Logger logger = Logger.getLogger(JDBWasteTransactionType.class);
	private String hostID;
	private String sessionID;
	private Integer displayMode=displayModeFull;

	public JDBWasteTransactionType(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		setWasteTransactionType("");
		clear();
	}

	public void clear()
	{
		setIncludeInTotals("");
		setDescription("");
		setStoreAsNegative("N");
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
			setWasteTransactionType(res);
			clear();

			if (isValidWasteTransaction() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.create"));
				stmtupdate.setString(1, getWasteTransactionType());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getIncludeInTotals());
				stmtupdate.setString(4, getStoreAsNegative());
				stmtupdate.setString(5, getEnabled());

				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Waste Transaction Type " + getWasteTransactionType() + " already exists");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public Icon getTransactionTypeIcon()
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
	
	
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteTransaction() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.delete"));
				stmtupdate.setString(1, getWasteTransactionType());
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

	public String getIncludeInTotals()
	{
		return JUtility.replaceNullStringwithBlank(dbIncludeInTotals).trim();
	}
	
	
	public String getStoreAsNegative()
	{
		String result="";
		result= JUtility.replaceNullStringwithBlank(dbStoreAsNegative);
		if (result.equals(""))
		{
			result = "N";
		}
		return result;
	}
	
	public void setStoreAsNegative(String var)
	{

		dbStoreAsNegative= JUtility.replaceNullStringwithBlank(var);
		if (dbStoreAsNegative.equals(""))
		{
			dbStoreAsNegative = "N";
		}
	}
	
	public boolean isStoreAsNegative()
	{
		if (getStoreAsNegative().contentEquals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setStoreAsNegative(boolean var)
	{
		if (var)
		{
			dbStoreAsNegative="Y";
		}
		else
		{
			dbStoreAsNegative="N";
		}
	}

	public String getIncludeInTotals(String res)
	{
		String result = "";

		if (getWasteTransactionProperties(res))
		{
			result = getIncludeInTotals();
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

	public ResultSet getWasteTransactionTypesResultSet(PreparedStatement criteria)
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
			setWasteTransactionType(rs.getString("waste_transaction_type"));
			setDescription(rs.getString("description"));
			setIncludeInTotals(rs.getString("include_in_totals"));
			setStoreAsNegative(rs.getString("store_as_negative"));
			setEnabled(rs.getString("enabled"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getWasteTransactionType()
	{
		return JUtility.replaceNullStringwithBlank(dbWasteTransactionType);
	}

	public boolean getTransactionTypeProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.getProperties"));
			stmt.setString(1, getWasteTransactionType());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Invalid Waste Transaction Type " + getWasteTransactionType());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getWasteTransactionProperties(String res)
	{
		setWasteTransactionType(res);
		return getTransactionTypeProperties();
	}
	
	public ResultSet getWasteTransactionTypesDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.getWasteTransactions"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBListData> getWasteTransactionTypes(Boolean enabled) {
	
		LinkedList<JDBListData> sampList = new LinkedList<JDBListData>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.getWasteTransactions"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteTransactionType samp = new JDBWasteTransactionType(getHostID(), getSessionID());
				samp.getPropertiesfromResultSet(rs);
				icon = samp.getTransactionTypeIcon();
				
				if (samp.isEnabled().equals(enabled))
				{
					JDBListData mld = new JDBListData(icon, index, true, samp);
					
					sampList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return sampList;
	}
	
	public String getHTMLPullDownCombo(String itemName, String defaultValue)
	{
		String result = "";
		String selected = "";
		LinkedList<JDBWasteTransactionType> transactionList = new LinkedList<JDBWasteTransactionType>();
				
		transactionList.addAll(getWasteTransactionTypesList(true,displayModeShort));
		result = "<SELECT width=\"100%\" style=\"width: 100%\" ID=\"" + itemName + "\" NAME=\"" + itemName + "\">";
		//result = result + "<OPTION></OPTION>";
		
		if (transactionList.size() > 0)
		{
			for (int x = 0; x < transactionList.size(); x++)
			{
				if (transactionList.get(x).getWasteTransactionType().equals(defaultValue))
				{
					selected = " SELECTED";
				} else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + transactionList.get(x).getWasteTransactionType()+"</OPTION>";
			}
		}
		result = result + "</SELECT>";

		return result;
	}

	public LinkedList<JDBWasteTransactionType> getWasteTransactionTypesList(Boolean enabled,int mode) {
		
		LinkedList<JDBWasteTransactionType> sampList = new LinkedList<JDBWasteTransactionType>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.getWasteTransactions"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWasteTransactionType samp = new JDBWasteTransactionType(getHostID(), getSessionID());
				samp.setDisplayMode(mode);
				samp.getPropertiesfromResultSet(rs);
				
				if (samp.isEnabled().equals(enabled))
				{				
					sampList.addLast(samp);
				}
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return sampList;
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

	public boolean isValidWasteTransaction()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.isValidWasteTransaction"));
			stmt.setFetchSize(1);
			stmt.setString(1, getWasteTransactionType());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Waste Transaction Type " + getWasteTransactionType() + " not found.");
			}
			stmt.close();
			rs.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public Boolean isIncludedInTotals()
	{
		Boolean result = false;
		if (getIncludeInTotals().equals("Y"))
		{
			result = true;
		} else
		{
			result = false;
		}
		return result;
	}

	public boolean isValidWasteTransactionType(String res)
	{
		setWasteTransactionType(res);

		return isValidWasteTransaction();
	}

	public void setIncludeInTotals(String suffix)
	{
		dbIncludeInTotals = suffix;
	}
	
	public void setIncludeInTotals(boolean enabled)
	{
		if (enabled)
		{
			setIncludeInTotals("Y");
		} else
		{
			setIncludeInTotals("N");
		}
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

	public void setWasteTransactionType(String res)
	{
		dbWasteTransactionType = res;
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
			result = JUtility.padString(getWasteTransactionType().toString(), true, field_WasteTransactionType, " ")+getDescription();
		}
		
		if (displayMode==displayModeShort)
		{
			result = getWasteTransactionType().toString();
		}
		
		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidWasteTransaction() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.update"));
				stmtupdate.setString(1, getDescription());
				stmtupdate.setString(2, getIncludeInTotals());
				stmtupdate.setString(3, getStoreAsNegative());
				stmtupdate.setString(4, getEnabled());
				stmtupdate.setString(5, getWasteTransactionType());
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
	
	public boolean rename(String oldTransactionType,String newTransactionType)
	{
		boolean result = false;
		setWasteTransactionType(oldTransactionType);
		setErrorMessage("");

		try
		{
			if (isValidWasteTransactionType(oldTransactionType) == true)
			{
				if (isValidWasteTransactionType(newTransactionType) == false)
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteTransactionType.renameTransactionType"));
					stmtupdate.setString(1, newTransactionType);
					stmtupdate.setString(2, oldTransactionType);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWasteLog.renameTransactionType"));
					stmtupdate.setString(1, newTransactionType);
					stmtupdate.setString(2, oldTransactionType);
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					
					
					setWasteTransactionType(newTransactionType);
					
					result = true;
				}
				else
				{
					setErrorMessage(newTransactionType + " already exists.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

}
