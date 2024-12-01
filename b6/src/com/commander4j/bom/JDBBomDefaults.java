package com.commander4j.bom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;


public class JDBBomDefaults
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomDefaults.class);
	
	private String source_field = "";
	private String source_value = "";
	private String destination_field = "";
	private String destination_value = "";
	private String hostID;
	private String sessionID;
	private String dbErrorMessage = "";
	
	public static int field_source_field = 32;
	public static int field_source_value = 50;
	public static int field_destination_field = 32;
	public static int field_destination_value = 50;
	
	private void setHostID(String host)
	{
		hostID = host;
	}
	
	private String getHostID()
	{
		return hostID;
	}
	
	private String getSessionID()
	{
		return sessionID;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}
	
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	public JDBBomDefaults(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public void clear()
	{
		setSourceField("");
		setSourceValue("");
		setDestinationField("");
		setDestinationValue("");
		setErrorMessage("");
	}

	public String getSouceField()
	{
		return source_field;
	}
	
	public void setSourceField(String sourceField)
	{
		this.source_field = sourceField;
	}

	public String getSourceValue()
	{
		return source_value;
	}

	public void setSourceValue(String sourceValue)
	{
		this.source_value = sourceValue;
	}

	public String getDestinationField()
	{
		return destination_field;
	}

	public void setDestinationField(String destinationField)
	{
		this.destination_field = destinationField;
	}

	public String getDestinationValue()
	{
		return destination_value;
	}

	public void setDestinationValue(String destinationValue)
	{
		this.destination_value = destinationValue;
	}
	
	public String defaultValue(String sourceField,String sourceValue,String destinationField)
	{
		String result = "";
		
		if (getProperties(sourceField,sourceValue,destinationField))
		{
			result = getDestinationValue();
		}
		
		return result;
	}

	public boolean getProperties(String sourceField,String sourceValue,String destinationField)
	{
		boolean result = false;
		
		clear();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomDefaults.getProperties"));
			
			stmt.setString(1, sourceField);
			stmt.setString(2, sourceValue);
			stmt.setString(3, destinationField);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				setSourceField(rs.getString("source_field"));
				setSourceValue(rs.getString("source_value"));
				setDestinationField(rs.getString("destination_field"));
				setDestinationValue(rs.getString("destination_value"));
				result = true;
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean delete(String sourceField,String sourceValue,String destinationField)
	{
		boolean result = true;
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomDefaults.delete"));
			stmtupdate.setString(1, sourceField);
			stmtupdate.setString(2, sourceValue);
			stmtupdate.setString(3, destinationField);

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			
			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean create(String sourceField,String sourceValue,String destinationField,String destinationValue)
	{
		boolean result = true;
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomDefaults.create"));

			stmtupdate.setString(1, sourceField);
			stmtupdate.setString(2, sourceValue);
			stmtupdate.setString(3, destinationField);
			stmtupdate.setString(4, destinationValue);

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			
			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean update(String sourceField,String sourceValue,String destinationField,String destinationValue)
	{
		boolean result = false;
		setErrorMessage("");
		
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomDefaults.update"));
			
			stmtupdate.setString(1, destinationValue);
			stmtupdate.setString(2, sourceField);
			stmtupdate.setString(3, sourceValue);
			stmtupdate.setString(4, destinationField);


			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			
			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public String toString() {
		String result = "";

	   result = getDestinationValue();

		return result;
	}
	
	public ResultSet getMaterialBatchDataResultSet(PreparedStatement criteria)
	{
		ResultSet rs;
		setErrorMessage("");

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
			setSourceField(rs.getString("source_field"));
			setSourceValue(rs.getString("source_value"));
			setDestinationField(rs.getString("destination_field"));
			setDestinationValue(rs.getString("destination_value"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}
	
	public ResultSet getBomDefaultsResultSet(PreparedStatement criteria)
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
}
