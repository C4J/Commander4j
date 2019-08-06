package com.commander4j.db;


/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTSamplePoint.java
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
import java.sql.Timestamp;
import java.util.LinkedList;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBWTSampleDetail
{
	public static int field_SamplePoint = 25;
	public static int field_Description = 35;
	public static int field_Location = 35;

	private String dbSamplePoint = "";
	private Timestamp dbSampleDate;
	private Timestamp dbSampleWeightDate;
	private BigDecimal dbSampleGrossWeight;
	private BigDecimal dbSampleTareWeight;
	private BigDecimal dbSampleNetWeight;
	private String dbSampleWeightUom;
	private Integer dbSampleT1Count;
	private Integer dbSampleT2Count;

	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;

	public JDBWTSampleDetail(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);

	}

	private void clear()
	{
		setSampleWeightUom("");
		setSampleGrossWeight(new BigDecimal("0.000"));
		setSampleTareWeight(new BigDecimal("0.000"));
		setSampleNetWeight(new BigDecimal("0.000"));
		setSampleT1Count(0);
		setSampleT2Count(0);
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidSampleWeight() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.create"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());
				stmtupdate.setTimestamp(3, getSampleWeightDate());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Sample Weight already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String samplepoint,Timestamp sampledate,Timestamp weightdate)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleWeightDate(weightdate);
		return create();
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		clear();

		try
		{
			if (isValidSampleWeight() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.delete"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());
				stmtupdate.setTimestamp(3, getSampleWeightDate());
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

	public boolean delete(String samplepoint,Timestamp sampledate,Timestamp weightdate)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleWeightDate(weightdate);

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

	public boolean getProperties(String samplepoint,Timestamp sampledate,Timestamp weightdate)
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleWeightDate(weightdate);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSamplePoint());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Weight");
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

			setSampleGrossWeight(rs.getBigDecimal("sample_gross_weight"));
			setSampleTareWeight(rs.getBigDecimal("sample_tare_weight"));
			setSampleNetWeight(rs.getBigDecimal("sample_net_weight"));
			setSampleT1Count(rs.getInt("sample_t1_count"));
			setSampleT2Count(rs.getInt("sample_t2_count"));
			setSampleWeightUom(rs.getString("sample_weight_uom"));


		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public Timestamp getSampleDate()
	{
		return dbSampleDate;
	}

	public BigDecimal getSampleGrossWeight()
	{
		return dbSampleGrossWeight;
	}

	public BigDecimal getSampleNetWeight()
	{
		return dbSampleNetWeight;
	}

	public String getSamplePoint()
	{
		return dbSamplePoint;
	}

	public ResultSet getSamplePointDataResultSet() {
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getSampleWeight"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public Integer getSampleT1Count()
	{
		return dbSampleT1Count;
	}
	
	public Integer getSampleT2Count()
	{
		return dbSampleT2Count;
	}

	public BigDecimal getSampleTareWeight()
	{
		return dbSampleTareWeight;
	}
	
	public Timestamp getSampleWeightDate()
	{
		return dbSampleWeightDate;
	}

	public LinkedList<JDBWTSampleDetail> getSampleWeightss(int displayType) {
		LinkedList<JDBWTSampleDetail> sampList = new LinkedList<JDBWTSampleDetail>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getSampleWeights"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTSampleDetail samp = new JDBWTSampleDetail(getHostID(), getSessionID());

				samp.setSamplePoint(rs.getString("sample_point"));
				samp.setSampleDate(rs.getTimestamp("sample_date"));
				samp.setSampleWeightDate(rs.getTimestamp("sample_weight_date"));
				samp.setSampleGrossWeight(rs.getBigDecimal("sample_gross_weight"));
				samp.setSampleTareWeight(rs.getBigDecimal("sample_tare_weight"));
				samp.setSampleNetWeight(rs.getBigDecimal("sample_net_weight"));
				samp.setSampleT1Count(rs.getInt("sample_t1_count"));
				samp.setSampleT2Count(rs.getInt("sample_t2_count"));
				samp.setSampleWeightUom(rs.getString("sample_weight_uom"));
				sampList.add(samp);
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

	public String getSampleWeightUom()
	{
		return dbSampleWeightUom;
	}

	private String getSessionID()
	{
		return sessionID;
	}


	public boolean isValidSamplePoint(String samplepoint)
	{
		setSamplePoint(samplepoint);
		return isValidSampleWeight();
	}

	public boolean isValidSampleWeight()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.isValidSampleWeight"));
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());
			stmt.setTimestamp(3, getSampleWeightDate());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Point [" + getSamplePoint() + "]");
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

	public void setSampleDate(Timestamp sampleDate)
	{
		dbSampleDate = sampleDate;
	}

	public void setSampleGrossWeight(BigDecimal sampleGrossWeight)
	{
		dbSampleGrossWeight = sampleGrossWeight;
	}

	public void setSampleNetWeight(BigDecimal sampleNetWeight)
	{
		dbSampleNetWeight = sampleNetWeight;
	}

	public void setSamplePoint(String spoint)
	{
		this.dbSamplePoint = JUtility.replaceNullStringwithBlank(spoint);
	}

	public void setSampleT1Count(Integer sampleT1Count)
	{
		dbSampleT1Count = sampleT1Count;
	}

	public void setSampleT2Count(Integer sampleT2Count)
	{
		dbSampleT2Count = sampleT2Count;
	}


	public void setSampleTareWeight(BigDecimal sampleTareWeight)
	{
		dbSampleTareWeight = sampleTareWeight;
	}

	public void setSampleWeightDate(Timestamp sampleWeightDate)
	{
		dbSampleWeightDate = sampleWeightDate;
	}

	public void setSampleWeightUom(String sampleWeightUom)
	{
		dbSampleWeightUom = sampleWeightUom;
	}
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	public String toString() {
		
		
		String result = "";
		

		result= getSampleGrossWeight().toString();



		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSampleWeight() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleWeight.update"));

				stmtupdate.setBigDecimal(1,getSampleGrossWeight());
				stmtupdate.setBigDecimal(2,getSampleTareWeight());
				stmtupdate.setBigDecimal(3,getSampleNetWeight());
				stmtupdate.setInt(4,getSampleT1Count());
				stmtupdate.setInt(5,getSampleT2Count());
				stmtupdate.setString(6,getSampleWeightUom());
				stmtupdate.setString(7,getSamplePoint());
				stmtupdate.setTimestamp(8,getSampleDate());
				stmtupdate.setTimestamp(9,getSampleWeightDate());
				
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

}
