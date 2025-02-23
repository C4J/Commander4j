package com.commander4j.db;


/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBWTViewWeightSample.java
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

import com.commander4j.util.JUtility;

public class JDBWTViewWeightSample
{

	public static Integer field_GrossWeight = 8;
	public static Integer field_NetWeight = 8;
	public static Integer field_TareWeight = 10;
	public static Integer field_T1Count = 5;
	public static Integer field_T2Count = 5;

	private String dbSamplePoint = "";
	private Timestamp dbSampleDate;
	private BigDecimal dbSampleNominalWeight;
	private String dbNominalWeightUOM;
	private BigDecimal dbSampleMeanWeight;
	private BigDecimal dbStandardDeviation;
	private int dbNoOfSamples;
	private Integer dbTotalT1s;
	private Integer dbTotalT2s;
	private String dbOrder = "";
	private String dbMaterial = "";
	private String dbGroup = "";
	private String dbContainer = "";

	private String dbErrorMessage = "";
	private String hostID;
	private String sessionID;
	public static Integer shortString = 1;
	public static Integer longString = 2;


	public JDBWTViewWeightSample(String host, String session)
	{
		super();
		setHostID(host);
		setSessionID(session);

	}
	
	private void clear()
	{

		setSampleDate(null);
		setSamplePoint("");
		setMaterial("");
		setProcessOrder("");
		setProductGroup("");
		setContainerCode("");

		setNominalWeight(new BigDecimal("0.000"));
		setNominalWeightUOM("");
		setMeanWeight(new BigDecimal("0.000"));
		setStandardDeviation(new BigDecimal("0.000"));
		setTotalT1s(0);
		setTotalT2s(0);
		setNoOfSamples(0);
		
	}


	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public ResultSet getViewWeightSampleDataResultSet(PreparedStatement criteria)
	{
		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();
		}
		catch (Exception e)
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
			
			setSampleDate(rs.getTimestamp("sample_date"));
			setSamplePoint(rs.getString("sample_point"));
			setMaterial(rs.getString("material"));
			setProcessOrder(rs.getString("process_order"));
			setProductGroup(rs.getString("product_group"));
			setContainerCode(rs.getString("container_code"));

			setNominalWeight(rs.getBigDecimal("nominal_weight"));
			setNominalWeightUOM(rs.getString("nominal_weight_uom"));
			setMeanWeight(rs.getBigDecimal("mean_weight"));
			setStandardDeviation(rs.getBigDecimal("standard_deviation"));
			setTotalT1s(rs.getInt("sample_t1_count"));
			setTotalT2s(rs.getInt("sample_t2_count"));
			setNoOfSamples(rs.getInt("no_of_samples"));

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

	public BigDecimal getNominalWeight()
	{
		return dbSampleNominalWeight;
	}
	
	public BigDecimal getMeanWeight()
	{
		return dbSampleMeanWeight;
	}

	
	public BigDecimal getStandardDeviation()
	{
		return dbStandardDeviation;
	}
	
	public void setStandardDeviation(BigDecimal stddev)
	{
		dbStandardDeviation = stddev ;
	}
	
	public String getSamplePoint()
	{
		return dbSamplePoint;
	}

	public Integer getTotalT1s()
	{
		return dbTotalT1s;
	}

	public Integer getTotalT2s()
	{
		return dbTotalT2s;
	}
			
	public int getSampleWeightUom()
	{
		return dbNoOfSamples;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	private void setProcessOrder(String order)
	{
		dbOrder = order;
	}
	
	private void setMaterial(String material)
	{
		dbMaterial = material;
	}
	
	private void setProductGroup(String group)
	{
		dbGroup = group;
	}
	
	private void setContainerCode(String container)
	{
		dbContainer = container;
	}
	
	public String getProcessOrder()
	{
		return JUtility.replaceNullStringwithBlank(dbOrder);
	}
	
	public String getMaterial()
	{
		return JUtility.replaceNullStringwithBlank(dbMaterial);
	}
	
	public String getProductGroup()
	{
		return JUtility.replaceNullStringwithBlank(dbGroup);
	}
	
	public String getContainerCode()
	{
		return JUtility.replaceNullStringwithBlank(dbContainer);
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

	public void setNominalWeight(BigDecimal sampleNomWeight)
	{
		dbSampleNominalWeight = sampleNomWeight;
	}
	
	
	public void setNominalWeightUOM(String uom)
	{
		dbNominalWeightUOM = uom;
	}
	
	public String getNominalWeightUOM()
	{
		return dbNominalWeightUOM;
	}

	public void setSamplePoint(String spoint)
	{
		this.dbSamplePoint = JUtility.replaceNullStringwithBlank(spoint);
	}

	public void setTotalT1s(Integer sampleT1Count)
	{
		dbTotalT1s = sampleT1Count;
	}

	public void setTotalT2s(Integer sampleT2Count)
	{
		dbTotalT2s = sampleT2Count;
	}

	public void setMeanWeight(BigDecimal mean)
	{
		dbSampleMeanWeight = mean;
	}

	public void setNoOfSamples(int noofsamples)
	{
		dbNoOfSamples = noofsamples;
	}
	
	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
}
