package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

public class JDBMaterialLocation
{
	private String dbErrorMessage;
	private String dbMaterial;
	private String dbLocation;
	private String dbStatus;
	public static int field_Material_id = 20;
	public static int field_Location_id = 15;
	public static int field_Status = 20;
	private final Logger logger = Logger.getLogger(JDBMaterialLocation.class);
	private String hostID;
	private String sessionID;
	private JDBControl ctrl;
	private Boolean materialLocationLookupEnabled = false;

	public JDBMaterialLocation(String host, String session) {
		setHostID(host);
		setSessionID(session);
		ctrl = new JDBControl(getHostID(), getSessionID());
		materialLocationLookupEnabled = Boolean.valueOf(ctrl.getKeyValue("SSCC_LOCATION_VALIDATION"));
	}

	public void clear()
	{
		setMaterial("");
		setLocation("");
	}

	public boolean getMaterialLocationProperties(String mat, String locn)
	{
		setMaterial(mat);
		setLocation(locn);
		return getMaterialLocationProperties();
	}

	public boolean getMaterialLocationProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
					.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.getProperties"));
			stmt.setString(1, getMaterial());
			stmt.setString(2, getLocation());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setStatus(rs.getString("status"));
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Material/Location");
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

	public boolean create(String material, String location)
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			setMaterial(material);
			setLocation(location);

			if (isValidMaterialLocation() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
						.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.create"));
				stmtupdate.setString(1, getMaterial());
				stmtupdate.setString(2, getLocation());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Material " + getMaterial() + " Location " + getLocation() + " already exists");
			}
		}
		catch (SQLException e)
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
			if (isValidMaterialLocation() == true)
			{

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
						.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.delete"));
				stmtupdate.setString(1, getMaterial());
				stmtupdate.setString(2, getLocation());
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

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getLocation()
	{
		return dbLocation;
	}

	public String getMaterial()
	{
		return dbMaterial;
	}

	public ResultSet getMaterialLocationDataResultSet(PreparedStatement criteria)
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
			setMaterial(rs.getString("material"));
			setLocation(rs.getString("location_id"));
			setStatus(rs.getString("status"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public String getStatus()
	{
		return dbStatus;
	}

	public boolean isValidMaterialLocation()
	{

		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
					.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.isValidMaterialLocation"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMaterial());
			stmt.setString(2, getLocation());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Material " + getMaterial() + " Location " + getLocation() + " not found.");
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

	public boolean isValidSSCCMaterialLocation()
	{

		boolean result = false;
		if (materialLocationLookupEnabled)
		{
			PreparedStatement stmt;
			ResultSet rs;
			try
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
						.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.isValidMaterialLocation"));
				stmt.setFetchSize(1);
				stmt.setString(1, getMaterial());
				stmt.setString(2, getLocation());
				rs = stmt.executeQuery();

				if (rs.next())
				{
					getPropertiesfromResultSet(rs);
					if (getStatus().equals("Valid")==true)
					{
						result = true;
					}
				}
				stmt.close();
				rs.close();

			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}
		else
		{
			result = true;
		}

		if (result==false)
		{
			setErrorMessage("Material " + getMaterial() + " Location " + getLocation() + " not valid.");
		}
		else
		{
			setErrorMessage("");
		}
		
		return result;

	}

	public boolean isValidMaterialLocation(String mat, String locn)
	{
		setMaterial(mat);
		setLocation(locn);
		return isValidMaterialLocation();
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

	public void setLocation(String location)
	{
		dbLocation = location;
	}

	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setStatus(String status)
	{
		dbStatus = status;
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidMaterialLocation() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID())
						.prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMaterialLocation.update"));
				stmtupdate.setString(1, getStatus());
				stmtupdate.setString(2, getMaterial());
				stmtupdate.setString(3, getLocation());
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
