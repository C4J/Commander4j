package com.commander4j.db;

import java.math.BigDecimal;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBPalletExtension.java
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
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;


public class JDBPalletExtension
{
	public static int field_first_case_input = 1;
	public static int field_last_case_input = 1;
	public static int field_shift = 10;
	public static int field_supplier_id = 15;
	public static int field_incident_ref = 15;	
	
	private String 		dbErrorMessage;
	private String 		dbSSCC;
	private String 		dbFirstCaseInput;
	private Timestamp 	dbFirstCaseTime;
	private String 		dbLastCaseInput;
	private Timestamp 	dbLastCaseTime;
	private String 		dbShiftID = "";
	private String 		dbSupplierID1;
	private String      dbIncident_Ref;
	private String		dbLocation;
	private String      dbProductGroup;
	private String      dbContainerCode;
	private Integer		dbPWeek;
	private String		dbPDay="";
	private String		dbPMonth="";
	private String		dbPYear="";
	private BigDecimal dbPalletQuantity = new BigDecimal("0.00");
	
	private final Logger logger = Logger.getLogger(JDBPalletExtension.class);
	private String hostID;
	private String sessionID;
	
	public JDBPalletExtension(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JDBPalletExtension(String sscc, Timestamp youngest, Timestamp oldest, String shift, String supplier,String incident,String location,String group,String code,Integer wk,String dy, String mn,String yr, BigDecimal palletQuantity)
	{
		setSSCC(sscc);
		setFirstCaseTime(youngest);
		setLastCaseTime(oldest);
		setShiftID(shift);
		setSupplierID1(supplier);
		setIncident_Ref(incident);
		setLocation(location);
		setProductGroup(group);
		setContainerCode(code);
		setPWeek(wk);
		setPDay(dy);
		setPMonth(mn);
		setPYear(yr);
		setPalletQuantity(palletQuantity);
	}
	
	public void clear()
	{
		setFirstCaseTime(null);
		setLastCaseTime(null);
		setShiftID("");
		setSupplierID1("");
		setIncident_Ref("");
		setLocation("");
		setProductGroup("");
		setContainerCode("");
		setPWeek(0);
		setPDay("");
		setPMonth("");
		setPYear("");
		setPalletQuantity(new BigDecimal("0.00"));
	}
	
	public BigDecimal getPalletQuantity()
	{
		return dbPalletQuantity;
	}
	
	public void setPalletQuantity(BigDecimal qty)
	{
		dbPalletQuantity = qty;
	}
	
	public boolean create()
	{

		logger.debug("create [" + getSSCC() + "]");

		boolean result = false;

		if (isValid() == false)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletExtension.create"));
				stmtupdate.setString(1, getSSCC());
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

		return result;
	}

	public boolean create(String sscc)
	{
		setSSCC(sscc);
		return create();
	}
	
	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletExtension.delete"));
			stmtupdate.setString(1, getSSCC());
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
	
	public boolean delete(String sscc)
	{
		boolean result = false;
		setSSCC(sscc);
		result = delete();
		return result;
	}
	
	public String getContainerCode()
	{
		return JUtility.replaceNullStringwithBlank(dbContainerCode);
	}
	
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	public boolean getFirstCaseInput()
	{

		boolean result = false;
		
		if (getFirstCaseInputStr().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public String getFirstCaseInputStr()
	{
		return JUtility.replaceNullObjectwithBlank(dbFirstCaseInput);
	}

	public Timestamp getFirstCaseTime()
	{

		return dbFirstCaseTime;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getIncidentRef()
	{
		return JUtility.replaceNullStringwithBlank(dbIncident_Ref);
	}

	public boolean getLastCaseInput()
	{
		boolean result = false;
		if (getLastCaseInputStr().equals("Y"))
		{
			result = true;
		}
		return result;
	}

	public String getLastCaseInputStr()
	{
		return  JUtility.replaceNullObjectwithBlank(dbLastCaseInput);
	}

	public Timestamp getLastCaseTime()
	{

		return dbLastCaseTime;
	}

	public String getLocation()
	{
		return JUtility.replaceNullStringwithBlank(dbLocation);
	}
	
	public String getPDay()
	{
		return JUtility.replaceNullStringwithBlank(dbPDay);
	}
	
	public String getPMonth()
	{
		return JUtility.replaceNullStringwithBlank(dbPMonth);
	}

	public String getProductGroup()
	{
		return JUtility.replaceNullStringwithBlank(dbProductGroup);
	}
	
	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setSSCC(rs.getString("sscc"));
			setFirstCaseInput(rs.getString("first_case_input"));
			setFirstCaseTime(rs.getTimestamp("first_case_time"));
			setLastCaseInput(rs.getString("last_case_input"));
			setLastCaseTime(rs.getTimestamp("last_case_time"));
			setShiftID(rs.getString("shift_id"));
			setSupplierID1(rs.getString("supplier_id1"));
			setIncident_Ref(rs.getString("incident_reference"));
			setLocation(rs.getString("location"));
			setProductGroup(rs.getString("product_group"));
			setContainerCode(rs.getString("container_code"));
			setPWeek(rs.getInt("pweek"));
			setPDay(rs.getString("pday"));
			setPMonth(rs.getString("pmonth"));
			setPYear(rs.getString("pyear"));
			setPalletQuantity(rs.getBigDecimal("pallet_quantity"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}	

	public String getPYear()
	{
		return JUtility.replaceNullStringwithBlank(dbPYear);
	}

	public boolean getSamplePalletExtensionProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletExtension.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSSCC());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			} else
			{
				setErrorMessage("Unknown SSCC [" + getSSCC() + "]");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getSamplePalletExtensionProperties(String sscc)
	{
		setSSCC(sscc);

		return getSamplePalletExtensionProperties();
	}
	
	private String getSessionID()
	{
		return sessionID;
	}

	public String getShiftID()
	{
		return JUtility.replaceNullStringwithBlank(dbShiftID);
	}

	public String getSSCC()
	{
		return dbSSCC;
	}

	public String getSupplierID1()
	{
		return JUtility.replaceNullStringwithBlank(dbSupplierID1);
	}

	public Integer getPWeek()
	{
		return dbPWeek;
	}

	public boolean isValid()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletExtension.isValid"));
			stmt.setString(1, getSSCC());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid SSCC");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValid(String sscc)
	{
		setSSCC(sscc);

		return isValid();
	}
	
	public void setContainerCode(String cc)
	{
		dbContainerCode = cc;
	}
	
	public void setPDay(String dy)
	{
		dbPDay = JUtility.replaceNullStringwithBlank(dy);
	}
	
	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}
	
	public void setFirstCaseInput(boolean yes)
	{
		if (yes)
		{
			setFirstCaseInput("Y");
		}
		else
		{
			setFirstCaseInput("N");
		}
	}

	public void setFirstCaseInput(String yes)
	{

		dbFirstCaseInput = JUtility.replaceNullStringwithBlank(yes);
		if (dbFirstCaseInput.equals(""))
		{
			dbFirstCaseInput ="N";
		}

	}

	public void setFirstCaseTime(Timestamp youngest)
	{
		try
		{
			dbFirstCaseTime = youngest;
		}
		catch (Exception ex)
		{

		}

	}

	private void setHostID(String host)
	{
		hostID = host;
	}
	
	public void setIncident_Ref(String inc)
	{
		dbIncident_Ref = inc;
	}
	
	public void setLastCaseInput(boolean yes)
	{
		if (yes)
		{
			setLastCaseInput("Y");
		}
		else
		{
			setLastCaseInput("N");
		}
	}

	public void setLastCaseInput(String yes)
	{

		dbLastCaseInput =JUtility.replaceNullStringwithBlank(yes);
		if (dbLastCaseInput.equals(""))
		{
			dbLastCaseInput ="N";
		}
	}
	
	public void setLastCaseTime(Timestamp oldest)
	{

		try
		{
			dbLastCaseTime = oldest;
		}
		catch (Exception ex)
		{

		}

	}
	
	public void setLocation(String filler)
	{
		dbLocation = filler;
	}
	

	public void setPMonth(String mon)
	{
		dbPMonth = JUtility.replaceNullStringwithBlank(mon);
	}
	
	public void setProductGroup(String grp)
	{
		dbProductGroup = grp;
	}

	public void setPYear(String yr)
	{
		dbPYear = JUtility.replaceNullStringwithBlank(yr);
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setShiftID(String shift)
	{
		dbShiftID = shift;
	}

	public void setSSCC(String sscc)
	{
		dbSSCC = sscc;
	}
	
	public void setSupplierID1(String supp)
	{
		dbSupplierID1 = supp;
	}
	
	public void setPWeek(Integer i)
	{
		dbPWeek = 0;
	}
	
	public void setWeekOfYear(Timestamp ts)
	{
		dbPWeek = Integer.valueOf(JUtility.getWeekOfYear(ts));
	}
	

	
	public boolean update()
	{
		boolean result = false;

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletExtension.update"));

				//<text>update {schema}APP_PALLET_EXTENSION first_case_input = ?,first_case_time = ?,last_case_input = ?,last_case_time = ?,supplier_id = ?,shift = ?,incident_refernce = ?,sample_point = ? where sscc = ?</text>
				
				stmtupdate.setString(1, getFirstCaseInputStr());
				stmtupdate.setTimestamp(2,getFirstCaseTime());
				stmtupdate.setString(3, getLastCaseInputStr());
				stmtupdate.setTimestamp(4,getLastCaseTime());
				stmtupdate.setString(5, getSupplierID1());
				stmtupdate.setString(6, getShiftID());
				stmtupdate.setString(7, getIncidentRef());
				stmtupdate.setString(8, getLocation());
				stmtupdate.setString(9, getProductGroup());
				stmtupdate.setString(10, getContainerCode());
				stmtupdate.setInt(11, getPWeek());
				stmtupdate.setString(12, getPDay());
				stmtupdate.setString(13, getPMonth());
				stmtupdate.setString(14, getPYear());
				stmtupdate.setBigDecimal(15, getPalletQuantity());
				stmtupdate.setString(16, getSSCC());
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
