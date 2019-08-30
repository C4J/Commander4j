package com.commander4j.db;


/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTSampleDetail.java
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

	public static Integer field_GrossWeight = 10;
	public static Integer field_NetWeight = 10;
	public static Integer field_TareWeight = 10;
	public static Integer field_T1Count = 5;
	public static Integer field_T2Count = 5;

	private String dbSamplePoint = "";
	private Timestamp dbSampleDate;
	private Integer dbSampleSequence = 0;
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

			if (isValidSampleDetail() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.create"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());
				stmtupdate.setInt(3, getSampleSequence());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Sample Detail already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String samplepoint,Timestamp sampledate,Integer sampleSequence)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleSequence(sampleSequence);
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
			if (isValidSampleDetail() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.delete"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());
				stmtupdate.setInt(3, getSampleSequence());
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

	public boolean delete(String samplepoint,Timestamp sampledate,Integer sampleSequence)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleSequence(sampleSequence);

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

	public boolean getProperties(String samplepoint,Timestamp sampledate,Integer sampleSequence)
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setSamplePoint(samplepoint);
		setSampleDate(sampledate);
		setSampleSequence(sampleSequence);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());
			stmt.setInt(3, getSampleSequence());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Detail");
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

			setSampleWeightDate(rs.getTimestamp("sample_weight_date"));
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
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getSampleDetail"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public Integer getSampleSequence()
	{
		return dbSampleSequence;
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
	
	public LinkedList<JDBWTSampleDetail> getSampleWeights(String samplePoint,Timestamp sampleDate) {
		LinkedList<JDBWTSampleDetail> sampList = new LinkedList<JDBWTSampleDetail>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		setSamplePoint(samplePoint);
		setSampleDate(sampleDate);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.getSampleDetails"));
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTSampleDetail samp = new JDBWTSampleDetail(getHostID(), getSessionID());

				samp.setSamplePoint(rs.getString("sample_point"));
				samp.setSampleDate(rs.getTimestamp("sample_date"));
				samp.setSampleSequence(rs.getInt("sample_sequence"));
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
		return isValidSampleDetail();
	}


	public boolean isValidSampleDetail()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.isValidSampleDetail"));
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());
			stmt.setInt(3, getSampleSequence());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Detail Record [" + getSamplePoint() + "]");
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

	public void setSampleSequence(Integer sampleSequence)
	{
		dbSampleSequence = sampleSequence;
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

		result= 
		JUtility.padString(getSampleSequence().toString(), false, 5, " ")+"     "+
		JUtility.getISOTimeStampStringFormat(getSampleWeightDate()).replace("T", " ")+
		JUtility.padString(getSampleGrossWeight().toString(), false, 11, " ")+" "+
		JUtility.padString(getSampleTareWeight().toString(), false, 11, " ")+
		JUtility.padString(getSampleNetWeight().toString(), false, 11, " ")+"    "+
		JUtility.padString(getSampleWeightUom(), true, 3, " ")+
		JUtility.padString(getSampleT1Count().toString(), false, 9, " ")+
		JUtility.padString(getSampleT2Count().toString(), false, 9, " ");


		return result;
	}
	
	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSampleDetail() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleDetail.update"));

				stmtupdate.setTimestamp(1,getSampleWeightDate());
				stmtupdate.setBigDecimal(2,getSampleGrossWeight());
				stmtupdate.setBigDecimal(3,getSampleNetWeight());
				stmtupdate.setBigDecimal(4,getSampleTareWeight());
				stmtupdate.setString(5,getSampleWeightUom());
				stmtupdate.setInt(6,getSampleT1Count());
				stmtupdate.setInt(7,getSampleT2Count());
				stmtupdate.setString(8,getSamplePoint());
				stmtupdate.setTimestamp(9,getSampleDate());
				stmtupdate.setInt(10,getSampleSequence());
				
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
