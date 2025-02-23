package com.commander4j.bom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBBomList
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBViewBomRecord.class);
	private String hostID;
	private String sessionID;
	private String dbErrorMessage;
	private JDBBomListRecord listRecord = new JDBBomListRecord();

	public JDBBomListRecord getListRecord()
	{
		return listRecord;
	}
	
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}
	
	private void setErrorMessage(String ErrorMsg)
	{
		dbErrorMessage = ErrorMsg;
		logger.debug(ErrorMsg);
	}
	
	public JDBBomList(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

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
	
	public boolean delete(JDBBomListRecord rec)
	{
		boolean result = true;

		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.delete"));

			stmtupdate.setString(1, rec.getList_id());
			stmtupdate.setString(2, rec.getItem());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean create(JDBBomListRecord rec)
	{
		boolean result = false;
		
		listRecord = rec;
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.create"));

			stmtupdate.setString(1, rec.getList_id());
			stmtupdate.setString(2, rec.getItem());
			stmtupdate.setInt(3, rec.getSequence());
			stmtupdate.setString(4, rec.getEnabled());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
			result = false;
		}
		return result;

	}
	
	public boolean update(JDBBomListRecord to)
	{
		boolean result = false;
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.update"));

			stmtupdate.setInt(1, to.getSequence());
			stmtupdate.setString(2, to.getEnabled());
			
			stmtupdate.setString(3, to.getList_id());
			stmtupdate.setString(4, to.getItem());


			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			
			listRecord = to;
			
			result = true;

		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
			result = false;
		}
		return result;

	}
	
	public LinkedList<String> getListItems(String list_id)
	{
		LinkedList<String> result = new LinkedList<String>();
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.getValues"));

			
			stmt.setString(1, list_id);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				String listitem = JUtility.replaceNullStringwithBlank(rs.getString("item"));
				
				result.addLast(listitem);
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		
		return result;
	}
	
	
	public boolean getProperties(JDBBomListRecord rec)
	{
		boolean result = false;
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.getProperties"));

			stmt.setString(1, rec.getList_id());
			stmt.setString(2, rec.getItem());

			rs = stmt.executeQuery();

			if (rs.next())
			{
				listRecord.getPropertiesFromResultSet(rs);	
				result = true;
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		
		return result;
	}
	
	public LinkedList<JDBBomListRecord> getListValues(String list_id)
	{
		LinkedList<JDBBomListRecord> result = new LinkedList<JDBBomListRecord>();
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.getValues"));

			
			stmt.setString(1, list_id);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBBomListRecord listitem = new JDBBomListRecord();
				
				listitem.getPropertiesFromResultSet(rs);				
				result.addLast(listitem);
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		
		return result;
	}
	
	public LinkedList<String> getValues(String sql,String fieldname)
	{
		LinkedList<String> result = new LinkedList<String>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(sql);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				String item = JUtility.replaceNullStringwithBlank(rs.getString(fieldname));
				result.add(item);
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		
		Collections.sort(result);

		return result;
	}
	
	public LinkedList<JDBBomListRecord> getListIds(Boolean enabled)
	{
		LinkedList<JDBBomListRecord> result = new LinkedList<JDBBomListRecord>();
		String criteria = "";
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomList.getListIds"));

			if (enabled)
			{
				criteria = "Y";
			}
			else
			{
				criteria = "N";
			}
			
			stmt.setString(1, criteria);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBBomListRecord listitem = new JDBBomListRecord();
				
				listitem.getPropertiesFromResultSet(rs);				
				result.addLast(listitem);
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			setErrorMessage(e.getMessage());
		}
		
		return result;
	}
	
}
