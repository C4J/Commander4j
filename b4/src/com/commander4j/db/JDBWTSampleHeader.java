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

public class JDBWTSampleHeader
{
	public static int field_SamplePoint = 25;
	public static int field_Description = 35;
	public static int field_Location = 35;

	private String dbSamplePoint = "";
	private Timestamp dbSampleDate;
	private Integer dbSampleSequence = 0;

	private String dbUserID = "";
	private String dbWorkstationID = "";
	private String dbScaleID = "";
	private String dbProcessOrder = "";
	private String dbRequiredResource = "";
	private String dbCustomerID = "";
	private String dbMaterial = "";
	private String dbProductGroup = "";
	private String dbContainerCode = "";
	private BigDecimal dbTareWeight = new BigDecimal("0.000");
	private String dbTareWeightUom = "";
	private BigDecimal dbNominalWeight;
	private String dbNominalWeightUom;

	private BigDecimal dbTNE = new BigDecimal("0.000");
	private BigDecimal dbNegT1 = new BigDecimal("0.000");
	private BigDecimal dbNegT2 = new BigDecimal("0.000");
	private BigDecimal dbSampleStdDev = new BigDecimal("0.000");
	private Integer dbSampleT1Count = 0;
	private Integer dbSampleT2Count = 0;

	private Integer dbSampleSize = 0;

	private Integer dbSampleCount = 0;
	private BigDecimal dbSampleMean  = new BigDecimal("0.000");

	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;

	public JDBWTSampleHeader(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);

	}

	private void clear()
	{
		setUserID("");
		setWorkstationID("");
		setScaleID("");
		setProcessOrder("");
		setRequiredResource("");
		setCustomerID("");
		setMaterial("");
		setProductGroup("");
		setContainerCode("");
		setTareWeight(new BigDecimal("0.000"));
		setTareWeightUom("");
		setNominalWeight(new BigDecimal("0.000"));
		setNominalWeightUom("");
		setTNE(new BigDecimal("0.000"));
		setNegT1(new BigDecimal("0.000"));
		setNegT2(new BigDecimal("0.000"));
		setSampleSize(0);
		setSampleCount(0);
		setSampleMean(new BigDecimal("0.000"));
		setSampleStdDev(new BigDecimal("0.000"));
		setSampleT1Count(0);
		setSampleT2Count(0);
	}

	public boolean create()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{

			if (isValidSampleHeader() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.create"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Sample Header already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String samplepoint, Timestamp sampledate)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);

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
			if (isValidSampleHeader() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.delete"));
				stmtupdate.setString(1, getSamplePoint());
				stmtupdate.setTimestamp(2, getSampleDate());

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

	public boolean delete(String samplepoint, Timestamp sampledate)
	{
		setSamplePoint(samplepoint);
		setSampleDate(sampledate);

		return delete();
	}

	public String getContainerCode()
	{
		return dbContainerCode;
	}

	public String getCustomerID()
	{
		return dbCustomerID;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getMaterial()
	{
		return dbMaterial;
	}

	public BigDecimal getNegT1()
	{
		return dbNegT1;
	}

	public BigDecimal getNegT2()
	{
		return dbNegT2;
	}

	public BigDecimal getNominalWeight()
	{
		return dbNominalWeight;
	}

	public String getNominalWeightUom()
	{
		return dbNominalWeightUom;
	}

	public String getProcessOrder()
	{
		return dbProcessOrder;
	}

	public String getProductGroup()
	{
		return dbProductGroup;
	}

	public boolean getProperties(String samplepoint, Timestamp sampledate)
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		setSamplePoint(samplepoint);
		setSampleDate(sampledate);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.getProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Header");
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

			setUserID(rs.getString("user_id"));
			setWorkstationID(rs.getString("workstation_id"));
			setScaleID(rs.getString("scale_id"));
			setProcessOrder(rs.getString("process_order"));
			setRequiredResource(rs.getString("required_resource"));
			setCustomerID(rs.getString("customer_id"));
			setMaterial(rs.getString("material"));
			setProductGroup(rs.getString("product_group"));
			setContainerCode(rs.getString("container_code"));
			setTareWeight(rs.getBigDecimal("tare_weight"));
			setTareWeightUom(rs.getString("tare_weight_uom"));
			setNominalWeight(rs.getBigDecimal("nominal_weight"));
			setNominalWeightUom(rs.getString("nominal_weight_uom"));
			setTNE(rs.getBigDecimal("tne"));
			setNegT1(rs.getBigDecimal("neg_t1"));
			setNegT2(rs.getBigDecimal("neg_t2"));
			setSampleSize(rs.getInt("sample_size"));
			setSampleCount(rs.getInt("sample_count"));
			setSampleMean(rs.getBigDecimal("sample_mean"));
			setSampleStdDev(rs.getBigDecimal("sample_std_dev"));
			setSampleT1Count(rs.getInt("sample_T1_count"));
			setSampleT2Count(rs.getInt("sample_T2_count"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getRequiredResource()
	{
		return dbRequiredResource;
	}

	public Integer getSampleCount()
	{
		return dbSampleCount;
	}

	public Timestamp getSampleDate()
	{
		return dbSampleDate;
	}

	public LinkedList<JDBWTSampleHeader> getSampleHeaders()
	{
		LinkedList<JDBWTSampleHeader> sampList = new LinkedList<JDBWTSampleHeader>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.getSampleHeaders"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBWTSampleHeader samp = new JDBWTSampleHeader(getHostID(), getSessionID());

				samp.setSamplePoint(rs.getString("sample_point"));
				samp.setSampleDate(rs.getTimestamp("sample_date"));
				samp.setSampleSequence(rs.getInt("sample_sequence"));
				samp.setUserID(rs.getString("user_id"));
				samp.setWorkstationID(rs.getString("workstation_id"));
				samp.setScaleID(rs.getString("scale_id"));
				samp.setProcessOrder(rs.getString("process_order"));
				samp.setRequiredResource(rs.getString("required_resource"));
				samp.setCustomerID(rs.getString("customer_id"));
				samp.setMaterial(rs.getString("material"));
				samp.setProductGroup(rs.getString("product_group"));
				samp.setContainerCode(rs.getString("container_code"));
				samp.setTareWeight(rs.getBigDecimal("tare_weight"));
				samp.setTareWeightUom(rs.getString("tare_weight_uom"));
				samp.setNominalWeight(rs.getBigDecimal("nominal_weight"));
				samp.setNominalWeightUom(rs.getString("nominal_weight_uom"));
				samp.setTNE(rs.getBigDecimal("tne"));
				samp.setNegT1(rs.getBigDecimal("neg_t1"));
				samp.setNegT2(rs.getBigDecimal("neg_t2"));
				samp.setSampleSize(rs.getInt("sample_size"));
				samp.setSampleCount(rs.getInt("sample_count"));
				samp.setSampleMean(rs.getBigDecimal("sample_mean"));
				samp.setSampleStdDev(rs.getBigDecimal("sample_std_dev"));
				samp.setSampleT1Count(rs.getInt("sample_T1_count"));
				samp.setSampleT2Count(rs.getInt("sample_T2_count"));

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

	public ResultSet getSampleHeadertDataResultSet()
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.getSampleHeader"));
			stmt.setFetchSize(250);
			rs = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public BigDecimal getSampleMean()
	{
		return dbSampleMean;
	}

	public String getSamplePoint()
	{
		return dbSamplePoint;
	}

	public Integer getSampleSequence()
	{
		return dbSampleSequence;
	}

	public Integer getSampleSize()
	{
		return dbSampleSize;
	}

	public BigDecimal getSampleStdDev()
	{
		return dbSampleStdDev;
	}

	public Integer getSampleT1Count()
	{
		return dbSampleT1Count;
	}

	public Integer getSampleT2Count()
	{
		return dbSampleT2Count;
	}

	public String getScaleID()
	{
		return dbScaleID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public BigDecimal getTareWeight()
	{
		return dbTareWeight;
	}

	public String getTareWeightUom()
	{
		return dbTareWeightUom;
	}

	public BigDecimal getTNE()
	{
		return dbTNE;
	}

	public String getUserID()
	{
		return dbUserID;
	}

	public String getWorkstationID()
	{
		return dbWorkstationID;
	}

	public boolean isValidSampleHeader()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.isValidSampleHeader"));
			stmt.setString(1, getSamplePoint());
			stmt.setTimestamp(2, getSampleDate());

			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Sample Header Record [" + getSamplePoint() + "]");
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

	public boolean isValidSamplePoint(String samplepoint)
	{
		setSamplePoint(samplepoint);
		return isValidSampleHeader();
	}

	public void setContainerCode(String containerCode)
	{
		dbContainerCode = containerCode;
	}

	public void setCustomerID(String customerID)
	{
		dbCustomerID = customerID;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	public void setNegT1(BigDecimal negT1)
	{
		dbNegT1 = negT1;
	}

	public void setNegT2(BigDecimal negT2)
	{
		dbNegT2 = negT2;
	}

	public void setNominalWeight(BigDecimal sampleNominal)
	{
		dbNominalWeight = sampleNominal;
	}

	public void setNominalWeightUom(String sampleWeightUom)
	{
		dbNominalWeightUom = sampleWeightUom;
	}

	public void setProcessOrder(String processOrder)
	{
		dbProcessOrder = processOrder;
	}

	public void setProductGroup(String productGroup)
	{
		dbProductGroup = productGroup;
	}

	public void setRequiredResource(String requiredResource)
	{
		dbRequiredResource = requiredResource;
	}

	public void setSampleCount(Integer sampleCount)
	{
		dbSampleCount = sampleCount;
	}

	public void setSampleDate(Timestamp sampleDate)
	{
		sampleDate.setNanos(0);
		dbSampleDate = sampleDate;
	}

	public void setSampleMean(BigDecimal sampleMean)
	{
		dbSampleMean = sampleMean;
	}

	public void setSamplePoint(String spoint)
	{
		this.dbSamplePoint = JUtility.replaceNullStringwithBlank(spoint);
	}

	public void setSampleSequence(Integer sampleSequence)
	{
		dbSampleSequence = sampleSequence;
	}

	public void setSampleSize(Integer sampleSize)
	{
		dbSampleSize = sampleSize;
	}

	public void setSampleStdDev(BigDecimal sampleStdDev)
	{
		dbSampleStdDev = sampleStdDev;
	}

	public void setSampleT1Count(Integer sampleT1Count)
	{
		dbSampleT1Count = sampleT1Count;
	}

	public void setSampleT2Count(Integer sampleT2Count)
	{
		dbSampleT2Count = sampleT2Count;
	}

	public void setScaleID(String scaleID)
	{
		dbScaleID = scaleID;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setTareWeight(BigDecimal sampleTareWeight)
	{
		dbTareWeight = sampleTareWeight;
	}

	public void setTareWeightUom(String tareWeightUom)
	{
		dbTareWeightUom = tareWeightUom;
	}

	public void setTNE(BigDecimal tne)
	{
		dbTNE = tne;
	}

	public void setUserID(String userID)
	{
		dbUserID = userID;
	}

	public void setWorkstationID(String workstationID)
	{
		dbWorkstationID = workstationID;
	}

	public String toString()
	{

		String result = "";

		result = getTNE().toString();

		return result;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidSampleHeader() == true)
			{
						
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBWTSampleHeader.update"));

				stmtupdate.setString(1, getUserID());
				stmtupdate.setString(2, getWorkstationID());
				stmtupdate.setString(3, getScaleID());
				stmtupdate.setString(4, getProcessOrder());
				stmtupdate.setString(5, getRequiredResource());
				stmtupdate.setString(6, getCustomerID());
				stmtupdate.setString(7, getMaterial());
				stmtupdate.setString(8, getProductGroup());
				stmtupdate.setString(9, getContainerCode());
				stmtupdate.setBigDecimal(10, getTareWeight());
				stmtupdate.setString(11, getTareWeightUom());
				stmtupdate.setBigDecimal(12, getNominalWeight());
				stmtupdate.setString(13, getNominalWeightUom());
				stmtupdate.setBigDecimal(14, getTNE());
				stmtupdate.setBigDecimal(15, getNegT1());
				stmtupdate.setBigDecimal(16, getNegT2());
				stmtupdate.setInt(17, getSampleSize());
				stmtupdate.setInt(18, getSampleCount());
				
				stmtupdate.setBigDecimal(19, getSampleMean());
				stmtupdate.setBigDecimal(20, getSampleStdDev());
				stmtupdate.setInt(21, getSampleT1Count());
				stmtupdate.setInt(22, getSampleT2Count());
				
				stmtupdate.setString(23, getSamplePoint());
				stmtupdate.setTimestamp(24, getSampleDate());

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
