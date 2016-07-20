package com.commander4j.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

/**
 */
public class JDBQuery
{
	private String sqltext;
	private Integer criteriaCount;
	private Vector<Object> params = new Vector<Object>();
	private Integer numberofparams;
	private PreparedStatement prepStatement;
	private String dbErrorMessage;
	private String hostID;
	private String sessionID;
	private String sortFields = "";
	private String sortDirection = "";
	private String orderBy = "";
	private final Logger logger = Logger.getLogger(JDBQuery.class);

	public static void closeStatement(PreparedStatement stmt)
	{
		try
		{
			stmt.close();
		}
		catch (Exception e)
		{

		}
	}
	
	public JDBQuery(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		clear();
	}

	public void addParameter(Object param)
	{
		params.add(param);
	}

	public void applyRestriction(boolean active, String restrictionType, Object limit)
	{

		if (restrictionType.toLowerCase().equals("top") == false)
		{
			applySort();
		}

		if (active)
		{
			if (restrictionType.toLowerCase().equals("top"))
			{
				sqltext = "select top " + String.valueOf(limit) + " subquery.* from (" + sqltext + ") as subquery";
			}

			if (restrictionType.toLowerCase().equals("limit"))
			{
				sqltext = "select * from (" + sqltext + ") as subquery limit " + String.valueOf(limit);
			}

			if (restrictionType.toLowerCase().equals("rownum"))
			{
				sqltext = "select * from (" + sqltext + ") where rownum <= " + String.valueOf(limit);
			}
		}

		if (restrictionType.toLowerCase().equals("top") == true)
		{
			applySort();
		}
	}

	public void addParamtoSQL(String field, Object param)
	{
		if (field != null)
		{
			if (field.equals("") == false)
			{
				if (param != null)
				{
					if (param.toString().equals("") == false)
					{
						criteriaCount++;

						if (criteriaCount == 1)
						{
							sqltext = sqltext + " where ";
						}
						else
						{
							sqltext = sqltext + " and ";
						}
						sqltext = sqltext + field + "?";

						addParameter(param);
					}
				}
			}
		}

	}

	public Integer getCriteriaCount()
	{
		return criteriaCount;
	}

	public void setCriterialCount(Integer cnt)
	{
		criteriaCount = cnt;
	}

	public void addText(String text)
	{
		text = text.replace("[top]", "");
		sqltext = sqltext + text;
	}
	
	public void appendSort(String fields,boolean direction)
	{
		if (direction)
		{
			appendSort(fields,"desc");
		}
		else
		{
			appendSort(fields,"asc");
		}
	}

	public void appendSort(String fields, String direction)
	{
		setSortFields(fields);
		setSortDirection(direction);
		orderBy = " order by " + getSortFields();
		orderBy = orderBy.replace(",", " " + getSortDirection() + "*");
		orderBy = orderBy.replace("*", ",");
		orderBy = orderBy + " " + getSortDirection();

	}

	private void applySort()

	{
		addText(orderBy);
	}

	private void bindParam(Integer pos, Object obj)
	{
		if (obj != null)
		{
			try
			{
				if (obj.getClass().equals(java.lang.Character.class))
				{
					prepStatement.setString(pos, obj.toString());
				}
				if (obj.getClass().equals(String.class))
				{
					prepStatement.setString(pos, (String) obj);
				}
				if (obj.getClass().equals(Integer.class))
				{
					prepStatement.setInt(pos, (Integer) obj);
				}
				if (obj.getClass().equals(Double.class))
				{
					prepStatement.setDouble(pos, (Double) obj);
				}
				if (obj.getClass().equals(Float.class))
				{
					prepStatement.setFloat(pos, (Float) obj);
				}
				if (obj.getClass().equals(Timestamp.class))
				{
					prepStatement.setTimestamp(pos, (Timestamp) obj);
				}
				if (obj.getClass().equals(BigDecimal.class))
				{
					prepStatement.setBigDecimal(pos, (BigDecimal) obj);
				}
				if (obj.getClass().equals(java.sql.Date.class))
				{
					prepStatement.setDate(pos, (java.sql.Date) obj);
				}
				if (obj.getClass().equals(Long.class))
				{
					prepStatement.setLong(pos, (Long) obj);
				}
			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}
		
	}

	public void bindParams()
	{
		numberofparams = params.size();
		try
		{
			prepStatement = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sqltext, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			prepStatement.setFetchSize(25);

			for (int i = 0; i < numberofparams; i++)
			{
				bindParam(i + 1, params.elementAt(i));
			}
		}
		catch (Exception ex)
		{
			setErrorMessage(ex.getMessage());
		}
		logger.debug(sqltext);

	}

	public void clear()
	{
		sqltext = "";
		criteriaCount = 0;
		params.clear();
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public PreparedStatement getPreparedStatement()
	{
		return prepStatement;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getSortDirection()
	{
		return sortDirection;
	}

	private String getSortFields()
	{
		return sortFields;
	}

	public String getSqlText()
	{
		return sqltext;
	}
	
	public void setSqlText(String txt)
	{
		sqltext = txt;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private void setSortDirection(String direction)
	{
		sortDirection = direction;
	}

	private void setSortFields(String fields)
	{
		sortFields = fields;
	}

}
