package com.commander4j.bom;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;

public class JDBBom
{
	UUID uuid = UUID.randomUUID();
	JDBBomRecord rec;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBom.class);

	Integer sequence = 0;

	private String hostID;
	private String sessionID;

	public void init()
	{
		sequence = 0;
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

	public JDBBom(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		rec = new JDBBomRecord(getHostID(), getSessionID());
	}

	public String getRootUuid(String bomID, String bomVersion)
	{
		String result = "";

		PreparedStatement stmt;
		ResultSet rs;

		try
		{
			// stmt = Common.host.con.prepareStatement("SELECT * FROM APP_BOM
			// where BOM_ID = ? and BOM_VERSION = ? and DATA_ID = 'root'");
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.getRootUuid"));
			stmt.setFetchSize(1);
			stmt.setString(1, bomID);
			stmt.setString(2, bomVersion);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString("UUID");

			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = "";
		}

		return result;
	}

	public HashMap<String, Integer> getElementCount(JDBBomRecord rec)
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.getElementCount"));

			// stmt = Common.host.con.prepareStatement("SELECT * FROM APP_BOM
			// WHERE PARENT_UUID = ? ORDER BY BOM_SEQUENCE");
			stmt.setString(1, rec.getUuid());

			rs = stmt.executeQuery();

			while (rs.next())
			{
				String key = "";

				key = "[" + rs.getString("data_id") + "]";

				if (result.containsKey(key))
				{
					result.put(key, result.get(key) + 1);
				}
				else
				{
					result.put(key, 1);
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public int getSequence(String data_id, String parent_uuid)
	{
		int result = 0;

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			// stmt = Common.host.con.prepareStatement("SELECT * FROM APP_BOM
			// WHERE PARENT_UUID = ? ORDER BY BOM_SEQUENCE");
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.getSequence"));

			stmt.setString(1, parent_uuid);

			rs = stmt.executeQuery();

			while (rs.next())
			{
				result = rs.getInt("BOM_SEQUENCE");

				if (rs.getString("DATA_ID").equals(data_id))
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public DefaultMutableTreeNode recurseBOM(DefaultMutableTreeNode node, String uuid, int level,String stage)
	{

		try
		{
			PreparedStatement stmt;

			ResultSet rs;

			if (level == 0)
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.recurseBOM1"));
			}
			else
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.recurseBOM2"));
			}

			stmt.setString(1, uuid);

			rs = stmt.executeQuery();

			level++;

			while (rs.next())
			{

				JDBBomRecord rec = new JDBBomRecord(getHostID(), getSessionID());

				rec.getPropertiesFromResultSet(rs);

				boolean stageOK = false;

				if (rec.getDataId().equals("stage"))
				{
					if (rec.getDataString().equals(stage))
					{
						stageOK = true;
					}
				}
				else
				{
					stageOK = true;
				}

				if (stageOK)
				{

					DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(rec, true);

					if (level > 1)
					{
						node.add(leaf);
					}
					else
					{
						node = leaf;
					}

					recurseBOM(leaf, rs.getString("UUID"), level,stage);
				}
			}

			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return node;
	}

	public String cloneTree(JDBBomRecord current, JDBBomRecord newRecord)
	{
		String result = "";

		JDBBomRecord nextRecord = cloneRecord(current, newRecord);

		LinkedList<JDBBomRecord> children = getChildUUIDs(current);

		for (int x = 0; x < children.size(); x++)
		{
			cloneTree(children.get(x), nextRecord);
		}

		return result;
	}

	public String deleteTree(JDBBomRecord current)
	{
		String result = "";

		LinkedList<JDBBomRecord> children = getChildUUIDs(current);

		for (int x = 0; x < children.size(); x++)
		{
			deleteTree(children.get(x));
		}

		deleteUUID(current.getUuid());

		return result;
	}

	public LinkedList<JDBBomRecord> getChildUUIDs(JDBBomRecord current)
	{
		LinkedList<JDBBomRecord> result = new LinkedList<JDBBomRecord>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			// stmt = Common.host.con.prepareStatement("select * from APP_BOM
			// where parent_uuid = ?");
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.getChildUuids"));

			stmt.setString(1, current.getUuid());

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBBomRecord temp = new JDBBomRecord(getHostID(), getSessionID());
				temp.getPropertiesFromResultSet(rs);
				result.add(temp);
			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	public JDBBomRecord cloneRecord(JDBBomRecord current, JDBBomRecord nextRecord)
	{
		String uuidnew = "";

		JDBBomRecord newRecord = cloneBomObject(current);

		uuid = UUID.randomUUID();

		uuidnew = uuid.toString();

		newRecord.setUuid(uuidnew);

		if (nextRecord == null)
		{
			newRecord.setParent_uuid(current.getParent_uuid());
		}
		else
		{
			newRecord.setParent_uuid(nextRecord.getUuid());
		}

		writeRecord(newRecord);

		return newRecord;
	}

	public JDBBomRecord cloneBomObject(JDBBomRecord from)
	{
		JDBBomRecord to = new JDBBomRecord(getHostID(), getSessionID());

		to.setBOMId(from.getBOMId());
		to.setDataId(from.getDataId());
		to.setBOMSequence(from.getBOMSequence());
		to.setBOMVersion(from.getBOMVersion());
		to.setDataDate(from.getDataDate());
		to.setDataString(from.getDataString());
		to.setDataType(from.getDataType());
		to.setDataTimestamp(from.getDataTimestamp());
		to.setDataDecimal(from.getDataDecimal());
		to.setUuid(from.getUuid());
		to.setParent_uuid(from.getParent_uuid());

		return to;
	}

	public JDBBomRecord getProperties(String uuid)
	{
		JDBBomRecord currentRecord = new JDBBomRecord(getHostID(), getSessionID());

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			// stmt = Common.host.con.prepareStatement("select * from APP_BOM
			// where uuid = ?");
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.getProperties"));

			stmt.setString(1, uuid);

			rs = stmt.executeQuery();

			if (rs.next())
			{
				currentRecord.getPropertiesFromResultSet(rs);

			}

			stmt.clearParameters();
			rs.close();
			stmt.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return currentRecord;
	}

	public boolean deleteBOM(String bom_id, String bom_version)
	{
		boolean result = true;

		init();

		try
		{
			PreparedStatement stmtupdate;

			// stmtupdate = Common.host.con.prepareStatement("delete from
			// APP_BOM where BOM_ID = ? and BOM_VERSION = ?");
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.deleteBOM"));

			stmtupdate.setString(1, bom_id);
			stmtupdate.setString(2, bom_version);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean deleteUUID(String uuid)
	{
		boolean result = true;

		init();

		try
		{
			PreparedStatement stmtupdate;

			// stmtupdate = Common.host.con.prepareStatement("delete from
			// APP_BOM where UUID = ?");
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.deleteUuid"));

			stmtupdate.setString(1, uuid);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean write(String bom_id, String bom_version, String data_id, String data_type, Object value, String uuid, String parent_uuid)
	{
		boolean result = true;
		JDBBomRecord rec = new JDBBomRecord(getHostID(), getSessionID());

		rec.clear();

		rec.setBOMId(bom_id);
		rec.setBOMVersion(bom_version);

		rec.setBOMSequence(sequence);
		sequence = sequence + 10;

		rec.setDataId(data_id);
		rec.setDataType(data_type);
		rec.setUuid(uuid);
		rec.setParent_uuid(parent_uuid);

		switch (data_type)
		{
		case "string":
			rec.setDataString((String) value);
			break;
		case "decimal":
			rec.setDataDecimal((BigDecimal) value);
			break;
		case "date":
			rec.setDataDate((Date) value);
			break;
		case "timestamp":
			rec.setDataTimestamp((Timestamp) value);
			break;
		default:
			rec.setDataString("unknown type");
			break;
		}

		result = writeRecord(rec);

		return result;
	}

	public boolean writeRecord(JDBBomRecord rec)
	{
		boolean result = true;
		try
		{
			PreparedStatement stmtupdate;

			// stmtupdate = Common.host.con.prepareStatement("insert into
			// `APP_BOM` (`BOM_ID`,`BOM_VERSION`,`BOM_SEQUENCE`, `DATA_ID`,
			// `DATA_TYPE`, `DATA_STRING`, `DATA_DECIMAL`, `DATA_DATE`,
			// `DATA_TIMESTAMP`, `UUID`, `PARENT_UUID`) VALUES (?,?, ?, ?, ?, ?,
			// ?, ?, ?, ?, ?)");
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.writeRecord"));

			setStatementParams(stmtupdate, rec);

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}
		return result;

	}

	private void setStatementParams(PreparedStatement stmtupdate, JDBBomRecord rec)
	{
		try
		{
			stmtupdate.setString(1, rec.getBOMId());
			stmtupdate.setString(2, rec.getBOMVersion());
			stmtupdate.setInt(3, rec.getBOMSequence());
			stmtupdate.setString(4, rec.getDataId());
			stmtupdate.setString(5, rec.getDataType());
			stmtupdate.setString(6, rec.getDataString());
			stmtupdate.setBigDecimal(7, rec.getDataDecimal());
			stmtupdate.setDate(8, rec.getDataDate());
			stmtupdate.setTimestamp(9, rec.getDataTimestamp());
			stmtupdate.setString(10, rec.getUuid());
			stmtupdate.setString(11, rec.getParent_uuid());
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
	}

	public boolean updateRecord(JDBBomRecord rec)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;

			// stmtupdate = Common.host.con.prepareStatement("UPDATE APP_BOM SET
			// DATA_STRING = ?,DATA_DECIMAL = ?,DATA_DATE = ?,DATA_TIMESTAMP = ?
			// WHERE UUID = ?");
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.updateRecord"));

			stmtupdate.setString(1, rec.getDataString());
			stmtupdate.setBigDecimal(2, rec.getDataDecimal());
			stmtupdate.setDate(3, rec.getDataDate());
			stmtupdate.setTimestamp(4, rec.getDataTimestamp());
			stmtupdate.setString(5, rec.getUuid());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	public boolean updateSequence(JDBBomRecord rec)
	{
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;

			// stmtupdate = Common.host.con.prepareStatement("UPDATE APP_BOM SET
			// BOM_SEQUENCE = ? WHERE UUID = ?");
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBBom.updateSequence"));

			stmtupdate.setInt(1, rec.getBOMSequence());
			stmtupdate.setString(2, rec.getUuid());

			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();

		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}
}
