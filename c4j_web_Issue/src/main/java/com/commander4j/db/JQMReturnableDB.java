package com.commander4j.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.commander4j.entity.JQMReturnableEntity;
import com.commander4j.sys.Common;

public class JQMReturnableDB
{
	private String sessionID = "";
	private String hostID = "";
	private JQMReturnableEntity returnableEntity;
	private String dbErrorMessage;

	public JQMReturnableDB(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JQMReturnableEntity getReturnableEntity()
	{
		return returnableEntity;
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

	public LinkedList<JQMReturnableEntity> getReturnableBySSCC(String sscc)
	{
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		LinkedList<JQMReturnableEntity> result = new LinkedList<JQMReturnableEntity>();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPalletHistory.getReturnableBySSCC"));
			stmt.setFetchSize(1);
			stmt.setString(1, sscc);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				if (rs.getBigDecimal("quantity").compareTo(new BigDecimal(0)) > 0)
				{
					JQMReturnableEntity tent = new JQMReturnableEntity();

					tent.setProcessOrderID(rs.getString("process_order"));
					tent.setLocationID(rs.getString("location_id"));
					tent.setQuantity(rs.getBigDecimal("quantity"));
					tent.setUom(rs.getString("uom"));

					result.addLast(tent);
				}

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

	private void setErrorMessage(String errorMsg)
	{
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

}
