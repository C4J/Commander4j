package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBCustomer.java
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

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * JDBCustomer class is used to insert/update/delete the APP_CUSTOMER table.
 * This table is used to hold customer names and label options. When printing
 * labels the the system looks at the customer ref in the Process Order and then
 * retrieves the customer details from the APP_CUSTOMER table. This permits
 * customer specific titles to appear on labels. The default customer ref of
 * SELF can be used for all Process Orders if the company name on the label is
 * constant.
 * <p>
 * <img alt="" src="./doc-files/APP_CUSTOMER.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameCustomerAdmin JInternalFrameCustomerAdmin
 * @see com.commander4j.app.JInternalFrameCustomerProperties JInternalFrameCustomerProperties
 */
public class JDBCustomer
{
	private String dbErrorMessage;
	private String dbPrintOnLabel;
	private String dbCustomerName;
	private String dbCustomerID;
	public static int field_customer_id = 20;
	public static int field_customer_name = 50;
	public static int field_cust_data_1 = 40;
	public static int field_cust_data_2 = 40;
	public static int field_print_on_label = 1;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBCustomer.class);
	private String hostID;
	private String sessionID;

	private String dbOverride_Pack_Label;
	private String dbOverride_Pallet_Label;
	private String dbPack_Label_ModuleID;
	private String dbPallet_Label_ModuleID;
	private String dbCustomerData01;
	private String dbCustomerData02;
	private String dbCustomerData03;
	private String dbCustomerData04;
	private String dbCustomerOverrideBatchFormat;
	private String dbCustomerBatchFormat;
	private String dbCustomerBatchValidation;
	
	public void setCustomerBatchFormat(String format)
	{
		dbCustomerBatchFormat = format;
	}
	
	public void setCustomerBatchValidation(String validation)
	{
		dbCustomerBatchValidation = validation;
	}
	
	public void setOverrideBatchFormat(String override)
	{
		dbCustomerOverrideBatchFormat = override;
	}
	
	public void setOverrideBatchFormat(boolean override)
	{
		if (override)
		{
			dbCustomerOverrideBatchFormat = "Y";
		}
		else
		{
			dbCustomerOverrideBatchFormat = "N";
		}
	}
	
	public String getCustomerBatchFormat()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerBatchFormat);
	}
	
	public String getCustomerBatchValidation()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerBatchValidation);
	}
	
	public String getCustomerBatchOverride()
	{
		String result = JUtility.replaceNullStringwithBlank(dbCustomerOverrideBatchFormat);
		
		if (result.equals(""))
		{
			result = "N";
		}
		
		return result;
	}
	
	public boolean isBatchOverride()
	{
		boolean result = false;
		if (getCustomerBatchOverride().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public String getCustomerData01()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerData01);
	}

	public String getCustomerData02()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerData02);
	}
	
	public String getCustomerData03()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerData03);
	}
	
	public String getCustomerData04()
	{
		return JUtility.replaceNullStringwithBlank(dbCustomerData04);
	}

	public void setCustomerData01(String data01)
	{
		dbCustomerData01 = data01;
	}

	public void setCustomerData02(String data02)
	{
		dbCustomerData02 = data02;
	}
	
	public void setCustomerData03(String data03)
	{
		dbCustomerData03 = data03;
	}
	
	public void setCustomerData04(String data04)
	{
		dbCustomerData04 = data04;
	}

	public JDBCustomer(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBCustomer(String host, String session, String id, String name, String label)
	{
		setHostID(host);
		setSessionID(session);
		setID(id);
		setName(name);
		setPrintOnLabel(label);
	}

	public void clear()
	{
		setName("");
		setPrintOnLabel("Y");
		setOverridePackLabel(false);
		setOverridePalletLabel(false);
		setPackLabelModuleID("");
		setPalletLabelModuleID("");
		setErrorMessage("");
		setCustomerData01("");
		setCustomerData02("");
		setCustomerData03("");
		setCustomerData04("");
		setOverrideBatchFormat(false);
		setCustomerBatchFormat("");
		setCustomerBatchValidation("");
		
	}

	public boolean create(String lid, String lname, String printonLabel)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setID(lid);
			setName(lname);
			setPrintOnLabel(printonLabel);

			if (isValidCustomer() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.create"));
				stmtupdate.setString(1, getID());
				stmtupdate.setString(2, getName());
				stmtupdate.setString(3, getPrintOnLabel());
				stmtupdate.setString(4, getOverridePackLabel());
				stmtupdate.setString(5, getPackLabelModuleID());
				stmtupdate.setString(6, getOverridePalletLabel());
				stmtupdate.setString(7, getPalletLabelModuleID());
				stmtupdate.setString(8, getCustomerData01());
				stmtupdate.setString(9, getCustomerData02());
				stmtupdate.setString(10, getCustomerData03());
				stmtupdate.setString(11, getCustomerData04());
				stmtupdate.setTimestamp(12, JUtility.getSQLDateTime());
				stmtupdate.setString(13, getCustomerBatchOverride());
				stmtupdate.setString(14, getCustomerBatchFormat());
				stmtupdate.setString(15, getCustomerBatchValidation());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			} else
			{
				setErrorMessage("Customer already exists");
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
			if (getID().equals("SELF") == false)
			{
				if (isValidCustomer() == true)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.delete"));
					stmtupdate.setString(1, getID());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();
					result = true;
				}
			} else
			{
				setErrorMessage("You cannot delete SELF");
			}
		} catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public ResultSet getCustomerDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.getCustomers"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public boolean getCustomerProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getCustomerProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.getCustomerProperties"));
			stmt.setString(1, getID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setName(rs.getString("customer_name"));
				setPrintOnLabel(rs.getString("print_on_label"));
				setOverridePackLabel(rs.getString("override_pack_label"));
				setPackLabelModuleID(rs.getString("pack_label_module_id"));
				setOverridePalletLabel(rs.getString("override_pallet_label"));
				setPalletLabelModuleID(rs.getString("pallet_label_module_id"));
				setCustomerData01(rs.getString("customer_data_01"));
				setCustomerData02(rs.getString("customer_data_02"));
				setCustomerData03(rs.getString("customer_data_03"));
				setCustomerData04(rs.getString("customer_data_04"));				
				setOverrideBatchFormat(rs.getString("OVERRIDE_BATCH_FORMAT"));
				setCustomerBatchFormat(rs.getString("BATCH_FORMAT"));
				setCustomerBatchValidation(rs.getString("BATCH_VALIDATION"));
				result = true;
				rs.close();
				stmt.close();
			} else
			{
				setErrorMessage("Invalid Customer ID");
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean getCustomerProperties(String cust)
	{
		setID(cust);
		return getCustomerProperties();
	}

	public LinkedList<JDBCustomer> getCustomers()
	{
		LinkedList<JDBCustomer> typeList = new LinkedList<JDBCustomer>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.getCustomers"));
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBCustomer mt = new JDBCustomer(getHostID(), getSessionID());
				mt.setID(rs.getString("customer_id"));
				mt.setName(rs.getString("customer_name"));
				mt.setPrintOnLabel(rs.getString("print_on_label"));
				mt.setOverridePackLabel(rs.getString("override_pack_label"));
				mt.setPackLabelModuleID(rs.getString("pack_label_module_id"));
				mt.setOverridePalletLabel(rs.getString("override_pallet_label"));
				mt.setPalletLabelModuleID(rs.getString("pallet_label_module_id"));
				mt.setCustomerData01(rs.getString("customer_data_01"));
				mt.setCustomerData02(rs.getString("customer_data_02"));
				mt.setCustomerData03(rs.getString("customer_data_03"));
				mt.setCustomerData04(rs.getString("customer_data_04"));
				mt.setOverrideBatchFormat(rs.getString("OVERRIDE_BATCH_FORMAT"));
				mt.setCustomerBatchFormat(rs.getString("BATCH_FORMAT"));
				mt.setCustomerBatchValidation(rs.getString("BATCH_VALIDATION"));
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

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getID()
	{
		String result = "";
		if (dbCustomerID != null)
			result = dbCustomerID;
		return result;
	}

	public String getName()
	{
		String result = "";
		if (dbCustomerName != null)
			result = dbCustomerName;
		return result;
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

	public String getOverridePalletLabel()
	{
		dbOverride_Pallet_Label = JUtility.replaceNullStringwithBlank(dbOverride_Pallet_Label);
		if (dbOverride_Pallet_Label.equals(""))
		{
			dbOverride_Pallet_Label = "N";
		}
		return dbOverride_Pallet_Label;

	}

	public String getPackLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPack_Label_ModuleID);
	}

	public String getPalletLabelModuleID()
	{
		return JUtility.replaceNullStringwithBlank(dbPallet_Label_ModuleID);
	}

	public String getPrintOnLabel()
	{
		return dbPrintOnLabel;
	}

	private String getSessionID()
	{
		return sessionID;
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

	public boolean isValidCustomer()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.isValidCustomer"));
			stmt.setString(1, getID());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Customer [" + getID() + "]");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	public boolean isValidCustomer(String cust)
	{
		setID(cust);
		return isValidCustomer();
	}

	public boolean renameTo(String newType)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		try
		{
			if (isValidCustomer() == true)
			{
				JDBCustomer mattype = new JDBCustomer(getHostID(), getSessionID());
				mattype.setID(newType);
				if (mattype.isValidCustomer() == false)
				{
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.renameTo"));
					stmtupdate.setString(1, newType);
					stmtupdate.setString(2, getID());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					stmtupdate.close();

					setID(newType);
					result = true;
				} else
				{
					setErrorMessage("New Customer ID is already in use.");
				}
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
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

	public void setID(String type)
	{
		dbCustomerID = type;
	}

	public void setName(String description)
	{
		dbCustomerName = description;
	}

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

	public void setPackLabelModuleID(String id)
	{
		dbPack_Label_ModuleID = id;
	}

	public void setPalletLabelModuleID(String id)
	{
		dbPallet_Label_ModuleID = id;
	}

	public void setPrintOnLabel(String lprint)
	{
		dbPrintOnLabel = lprint;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String toString()
	{
		String result = "";
		if (getID().equals("") == false)
		{
			result = JUtility.padString(getID(), true, field_customer_id, " ") + " - " + getName();
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

		try
		{
			if (isValidCustomer() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBCustomer.update"));
				stmtupdate.setString(1, getName());
				stmtupdate.setString(2, getPrintOnLabel());
				stmtupdate.setString(3, getOverridePackLabel());
				stmtupdate.setString(4, getPackLabelModuleID());
				stmtupdate.setString(5, getOverridePalletLabel());
				stmtupdate.setString(6, getPalletLabelModuleID());
				stmtupdate.setString(7, getCustomerData01());
				stmtupdate.setString(8, getCustomerData02());
				stmtupdate.setString(9, getCustomerData03());
				stmtupdate.setString(10, getCustomerData04());
				stmtupdate.setTimestamp(11, JUtility.getSQLDateTime());
				
				stmtupdate.setString(12, getCustomerBatchOverride());
				stmtupdate.setString(13, getCustomerBatchFormat());
				stmtupdate.setString(14, getCustomerBatchValidation());				
				
				stmtupdate.setString(15, getID());
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
