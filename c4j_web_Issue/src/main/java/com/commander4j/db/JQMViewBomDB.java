package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.commander4j.entity.JQMViewBOMEntity;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JQMViewBomDB
{

	private String sessionID = "";
	private String hostID = "";
	private JQMViewBOMEntity viewBOMEntity;
	private String dbErrorMessage;

	public JQMViewBomDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JQMViewBOMEntity getViewBOMEntity()
	{
		return viewBOMEntity;
	}
	
	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public boolean isValidMaterialForLocation(String bom_id,String bom_version,String stage,String inout,String material,String location)
	{
		boolean result = false;

		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.isValidMaterialForLocation")))
		{
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, inout);
			stmt.setString(4, stage);
			stmt.setString(5, material);
			stmt.setString(6, location);
			stmt.setFetchSize(1);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Material ["+material+"] is not valid for Location ["+location+"]");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	
	public LinkedList<JQMViewBOMEntity> getStagesForBOM(String bom_id,String bom_version)
	{
		setErrorMessage("");
		LinkedList<JQMViewBOMEntity> result = new LinkedList<JQMViewBOMEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.getBomStages")))
		{
			stmt.setFetchSize(10);
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMViewBOMEntity tent = new JQMViewBOMEntity();

					tent.setAction("result");
					tent.setBomID(JUtility.replaceNullStringwithBlank(rs.getString("bom_id")));
					tent.setBomVersion(JUtility.replaceNullStringwithBlank(rs.getString("bom_version")));
					tent.setStage(JUtility.replaceNullStringwithBlank(rs.getString("stage")));
					result.addLast(tent);

				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		
		return result;
	}
	
	public LinkedList<JQMViewBOMEntity> getValidtLocationsforMaterial(String bom_id,String bom_version,String inout,String stage,String material)
	{
		setErrorMessage("");
		LinkedList<JQMViewBOMEntity> result = new LinkedList<JQMViewBOMEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.getValidtLocationsforMaterial")))
		{
			stmt.setFetchSize(10);
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, inout);
			stmt.setString(4, stage);
			stmt.setString(5, material);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMViewBOMEntity tent = new JQMViewBOMEntity();

					tent.setAction("result");
					tent.setBomID(JUtility.replaceNullStringwithBlank(rs.getString("bom_id")));
					tent.setBomVersion(JUtility.replaceNullStringwithBlank(rs.getString("bom_version")));
					tent.setLocation_id(JUtility.replaceNullStringwithBlank(rs.getString("location_Id")));
					tent.setMaterial(JUtility.replaceNullStringwithBlank(rs.getString("material")));
					tent.setInputOutput(JUtility.replaceNullStringwithBlank(rs.getString("input_output")));
					tent.setStage(JUtility.replaceNullStringwithBlank(rs.getString("stage")));
					tent.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
					result.addLast(tent);

				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JQMViewBOMEntity> getValidMaterialsForBOM(String bom_id,String bom_version,String stage,String inout)
	{
		setErrorMessage("");
		LinkedList<JQMViewBOMEntity> result = new LinkedList<JQMViewBOMEntity>();

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.getValidMaterialsForBOM")))
		{
			stmt.setFetchSize(10);
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, inout);
			stmt.setString(4, stage);

			try (ResultSet rs = stmt.executeQuery())
			{
				while (rs.next())
				{
					JQMViewBOMEntity tent = new JQMViewBOMEntity();

					tent.setAction("result");
					tent.setBomID(JUtility.replaceNullStringwithBlank(rs.getString("bom_id")));
					tent.setBomVersion(JUtility.replaceNullStringwithBlank(rs.getString("bom_version")));
					tent.setLocation_id(JUtility.replaceNullStringwithBlank(rs.getString("location_Id")));
					tent.setMaterial(JUtility.replaceNullStringwithBlank(rs.getString("material")));
					tent.setInputOutput(JUtility.replaceNullStringwithBlank(rs.getString("input_output")));
					tent.setStage(JUtility.replaceNullStringwithBlank(rs.getString("stage")));
					tent.setDescription(JUtility.replaceNullStringwithBlank(rs.getString("description")));
					result.addLast(tent);

				}
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
	
	public Boolean isMaterialValidForBOM(String bom_id,String bom_version,String stage,String inout,String material)
	{
		
		Boolean result = false;
		
		setErrorMessage("");

		try (PreparedStatement stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.isMaterialValidForBOM")))
		{
			stmt.setFetchSize(10);
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, inout);
			stmt.setString(4, stage);
			stmt.setString(5, material);

			try (ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					result = true;
				}
				else
				{
					setErrorMessage("Material ["+material+"] is not valid for BOM ["+bom_id+"/"+bom_version+"]");
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
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

}
