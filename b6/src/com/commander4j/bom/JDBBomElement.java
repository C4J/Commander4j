package com.commander4j.bom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBListData;
import com.commander4j.sys.Common;

public class JDBBomElement
{

	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomElement.class);
	
	Integer sequence = 0;
	
	private String data_id = "";
	private JDBBomElementRecord element = new JDBBomElementRecord();
	private String hostID;
	private String sessionID;
	private String dbErrorMessage = "";
	
	public static int field_data_id = 20;
	public static int field_data_type = 20;
	public static int field_data_description = 80;
	public static int field_icon_filename = 80;
	public static int field_lookup_sql = 255;
	public static int field_lookup_field = 32;
	
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
	
	public JDBBomElement(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}
	
	public JDBBomElementRecord getElementRecord()
	{
		return element;
	}
	
	public String getDataId()
	{
		return data_id;
	}

	public void setDataId(String id)
	{
		this.data_id = id;
	}

	public boolean getProperties(String id)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.getProperties"));

			stmt.setString(1, id);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				setDataId(rs.getString("data_id"));

				element.getPropertiesFromResultSet(rs);
				
				result = true;
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean isValid(String id)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.getProperties"));

			stmt.setString(1, id);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean delete(String id)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.delete"));

			stmtupdate.setString(1, id);

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			
			result = true;

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean create(String id,JDBBomElementRecord rec)
	{
		boolean result = false;
		
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.create"));
						
			stmtupdate.setString(1, id);
			stmtupdate.setString(2, rec.getDataType());
			stmtupdate.setString(3, rec.getEnable_edit());
			stmtupdate.setString(4, rec.getEnable_create());
			stmtupdate.setString(5, rec.getEnable_delete());
			stmtupdate.setString(6, rec.getEnable_duplicate());
			stmtupdate.setString(7, rec.getEnable_clipboard());
			stmtupdate.setString(8, rec.getEnable_lookup());
			stmtupdate.setInt(9, rec.getMax_occur_level());
			stmtupdate.setString(10, rec.getLookup_sql());
			stmtupdate.setString(11, rec.getLookup_field());
			stmtupdate.setString(12, rec.getIcon_filename());
			stmtupdate.setString(13, rec.getDescription());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			
			result = true;

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}
	
	public boolean update(String id,JDBBomElementRecord rec)
	{
		boolean result = false;
		
		try
		{
			PreparedStatement stmtupdate;

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.update"));
			
			stmtupdate.setString(1, rec.getDataType());
			stmtupdate.setString(2, rec.getEnable_edit());
			stmtupdate.setString(3, rec.getEnable_create());
			stmtupdate.setString(4, rec.getEnable_delete());
			stmtupdate.setString(5, rec.getEnable_duplicate());
			stmtupdate.setString(6, rec.getEnable_clipboard());
			stmtupdate.setString(7, rec.getEnable_lookup());
			stmtupdate.setInt(8, rec.getMax_occur_level());
			stmtupdate.setString(9, rec.getLookup_sql());
			stmtupdate.setString(10, rec.getLookup_field());
			stmtupdate.setString(11, rec.getIcon_filename());
			stmtupdate.setString(12, rec.getDescription());
			stmtupdate.setString(13, id);

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			
			result = true;

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public String toString() {
		String result = "";

	   result = getDataId();

		return result;
	}
	
	public String getElementDescription(String item)
	{
		String result = "";
		
		if (getProperties(item))
		{
			result = getElementRecord().getDescription();
		}
		else
		{
			result =  item;
		}
		
		return result;
	}
	
	public ResultSet getBomElementsResultSet(PreparedStatement criteria)
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
	
	public LinkedList<JDBListData> getElementIds(boolean blankfirst)
	{
		LinkedList<JDBListData> elementList = new LinkedList<JDBListData>();
		
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		
		if (blankfirst)
		{
			JDBBomElementRecord blank = new JDBBomElementRecord();
			blank.setDataId("");
			blank.setDataType("");
			blank.setIcon_filename("clear.gif");
			JDBListData item = new JDBListData(blank.getIcon(), 0, true,blank);
			elementList.add(item);
		}
		
		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBomElement.getList"));
			rs = stmt.executeQuery();
			
			while (rs.next())
			{
				JDBBomElementRecord currentRecord = new JDBBomElementRecord();
				currentRecord.getPropertiesFromResultSet(rs);
				
				icon = currentRecord.getIcon();
				JDBListData mld = new JDBListData(icon, index, true, currentRecord);
				elementList.addLast(mld);
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}

		return elementList;
	}
}
