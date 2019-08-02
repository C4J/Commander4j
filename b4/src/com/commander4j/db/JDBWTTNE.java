package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTTNE.java
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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWTTNE
{
	public static int field_Nominal = 10;
	public static int field_Nominal_UOM= 3;
	public static int field_TNE = 10;
	public static int field_NegT1 = 10;
	public static int field_NegT2 = 10;
	private BigDecimal dbNominalWT = new BigDecimal("0");
	private String dbNominalWTUOM = "";
	private BigDecimal dbTNE = new BigDecimal("0");
	private BigDecimal dbNegT1 = new BigDecimal("0");
	private BigDecimal dbNegT2 = new BigDecimal("0");
	private BigDecimal zero =  new BigDecimal("0");
	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;

	public JDBWTTNE(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);
	}

	private void clear()
	{

		setTNE(zero);
		setNegT1(zero);
		setNegT2(zero);

	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidTNEID() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.create"));
				stmtupdate.setBigDecimal(1, getNominalWT());
				stmtupdate.setString(2, getNominalWTUOM());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("TNE Nominal Weight already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(BigDecimal nominalwt,String nominalUOM)
	{
		setNominalWT(nominalwt);
		setNominalWTUOM(nominalUOM);
		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidTNEID() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.delete"));
				stmtupdate.setBigDecimal(1, getNominalWT());
				stmtupdate.setString(2, getNominalWTUOM());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(BigDecimal nominalwt,String nominalUOM)
	{

		setNominalWT(nominalwt);
		setNominalWTUOM(nominalUOM);
		return delete();
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public BigDecimal getNegT1()
	{
		return dbNegT1;
	}

	public BigDecimal getNegT2()
	{
		return dbNegT2;
	}

	public BigDecimal getNominalWT()
	{
		return dbNominalWT;
	}

	public String getNominalWTUOM()
	{
		return dbNominalWTUOM;
	}
	
	public boolean getProperties(BigDecimal nominal,String uom)
	{
		setNominalWT(nominal);
		setNominalWTUOM(uom);
		return getProperties();
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.getProperties"));
			stmt.setFetchSize(1);
			stmt.setBigDecimal(1, getNominalWT());
			stmt.setString(2, getNominalWTUOM());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Nominal Weight");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
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
			setNominalWT(rs.getBigDecimal("nominal_weight"));
			setNominalWTUOM(rs.getString("nominal_weight_uom"));
			setTNE(rs.getBigDecimal("tne"));
			setNegT1(rs.getBigDecimal("neg_t1"));
			setNegT2(rs.getBigDecimal("neg_t2"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}
	
	public ResultSet getTNEDataResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.getTNEs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public LinkedList<JDBWTTNE> getTNEs() {
		LinkedList<JDBWTTNE> tneList = new LinkedList<JDBWTTNE>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.getTNEs"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTTNE tne = new JDBWTTNE(getHostID(), getSessionID());
				tne.setNominalWT(rs.getBigDecimal("nominal_weight"));
				tne.setNominalWTUOM(rs.getString("nominal_weight_uom"));
				tne.setTNE(rs.getBigDecimal("tne"));
				tne.setNegT1(rs.getBigDecimal("neg_t1"));
				tne.setNegT2(rs.getBigDecimal("neg_t2"));
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
	
	private String getSessionID()
	{
		return sessionID;
	}

	public BigDecimal getTNE()
	{
		return dbTNE;
	}

	public boolean isValidNominalWt(BigDecimal nominalWT,String nominalUOM)
	{
		setNominalWT(nominalWT);
		setNominalWTUOM(nominalUOM);
		return isValidTNEID();
	}

	public boolean isValidTNEID()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.isValidTNE"));
			stmt.setBigDecimal(1, getNominalWT());
			stmt.setString(2, getNominalWTUOM());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid TNE [" + getNominalWT().toString() + " "+getNominalWTUOM()+"] ");
			}
			stmt.close();
			rs.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;

	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setNegT1(BigDecimal t1)
	{
		if (t1 == null)
		{
			this.dbNegT1 = new BigDecimal("0.000");
		}
		else
		{
			this.dbNegT1 = t1;
		}
		
	}

	public void setNegT2(BigDecimal t2)
	{
		if (t2 == null)
		{
			this.dbNegT2 = new BigDecimal("0.000");
		}
		else
		{
			this.dbNegT2 = t2;
		}
	}

	public void setNominalWT(BigDecimal dbNominalWT)
	{
		if (dbNominalWT == null)
		{
			this.dbNominalWT = new BigDecimal("0.000");
		}
		else
		{
			this.dbNominalWT = dbNominalWT;
		}
	}

	public void setNominalWTUOM(String nominalUOM)
	{
		this.dbNominalWTUOM = JUtility.replaceNullStringwithBlank(nominalUOM);
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setTNE(BigDecimal tne)
	{
		this.dbTNE = tne;
	}
	
	public String toString() {
		
		
		String result = "";

		result= JUtility.padString(getNominalWT().toString(), false, field_Nominal, " ")+" "+ 
		        JUtility.padString(getNominalWTUOM(), true, field_Nominal_UOM, " ")+" "+ 
				JUtility.padString(getTNE().toString(), false, field_TNE, " ")+" "+
		        JUtility.padString(getNominalWTUOM(), true, field_Nominal_UOM, " ")+" "+ 

		        JUtility.padString(getNegT1().toString(), false, field_NegT1, " ")+" "+ 
				JUtility.padString(getNominalWTUOM(), true, field_Nominal_UOM, " ")+" "+
		        JUtility.padString(getNegT2().toString(), false, field_NegT2, " ")+" "+ 
				JUtility.padString(getNominalWTUOM(), true, field_Nominal_UOM, " ");

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidTNEID() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTTNE.update"));
				stmtupdate.setBigDecimal(1, getTNE());
				stmtupdate.setBigDecimal(2, getNegT1());
				stmtupdate.setBigDecimal(3, getNegT2());
				stmtupdate.setBigDecimal(4, getNominalWT());
				stmtupdate.setString(5, getNominalWTUOM());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public boolean update(BigDecimal nominalwt,String nominalUOM)
	{

		setNominalWT(nominalwt);
		setNominalWTUOM(nominalUOM);
		return update();
	}

}
