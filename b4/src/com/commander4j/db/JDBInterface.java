package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBInterface
{

	private String dbErrorMessage;
	private String dbInterfaceType;
	private String dbInterfaceDirection;
	private String dbPath;
	private String dbEnabled;
	private String dbDevice;
	private String dbFormat;
	private String dbEmailFlags;
	private String dbEmailAddresses;
	private String dbUomConversion;
	public static int field_interface_type = 50;
	public static int field_interface_direction = 50;
	public static int field_path = 255;
	public static int field_device = 15;
	public static int field_subject = 100;
	public static int field_topic = 100;
	public static int field_server = 45;
	public static int field_username = 45;
	public static int field_password = 45;
	public static int field_format = 15;
	public static int field_uom_conversion = 30;
	public static int field_email_flags = 10;
	public static int field_email_addresses = 500;

	private final Logger logger = Logger.getLogger(JDBInterface.class);

	private String hostID;

	private String sessionID;

	public JDBInterface(ResultSet rs)
	{
		getPropertiesfromResultSet(rs);
	}

	public JDBInterface(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public JDBInterface(String interfaceType, String interfaceDirection, String Path, String Enabled, String Device)
	{
		setInterfaceType(interfaceType);
		setInterfaceDirection(interfaceDirection);
		setPath(Path);
		setEnabled(Enabled);
		setDevice(Device);
	}

	public void clear() {
		setPath("");
		setEnabled("");
		setUOMConversion("None");
	}

	public boolean create() {

		logger.debug("create [" + getInterfaceType() + "][" + getInterfaceDirection() + "]");

		boolean result = false;

		if (isValid() == true)
		{

			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.create"));
				stmtupdate.setString(1, getInterfaceType());
				stmtupdate.setString(2, getInterfaceDirection());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				update();
				result = true;
			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.delete"));
			stmtupdate.setString(1, getInterfaceType());
			stmtupdate.setString(2, getInterfaceDirection());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String type, String direction) {
		boolean result = false;
		setInterfaceType(type);
		setInterfaceDirection(direction);
		result = delete();
		return result;
	}

	public String getDevice() {
		return dbDevice;
	}

	public String getEmailAddresses() {
		return JUtility.replaceNullObjectwithBlank(dbEmailAddresses);
	}

	public Boolean getEmailError() {
		Boolean result = false;
		if (getEmailFlags().contains("E"))
		{
			result = true;
		}

		return result;
	}

	public String getEmailFlags() {
		return JUtility.replaceNullObjectwithBlank(dbEmailFlags);
	}

	public Boolean getEmailSuccess() {
		Boolean result = false;
		if (getEmailFlags().contains("S"))
		{
			result = true;
		}

		return result;
	}

	public Boolean getEmailWarning() {
		Boolean result = false;
		if (getEmailFlags().contains("W"))
		{
			result = true;
		}

		return result;
	}

	public String getEnabled() {
		return dbEnabled;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	public String getFormat() {
		dbFormat = JUtility.replaceNullStringwithBlank(dbFormat);
		if (dbFormat.equals(""))
		{
			dbFormat = "XML";
		}
		return dbFormat;
	}

	public String getHostID() {
		return hostID;
	}

	public LinkedList<String> getInputPaths() {
		LinkedList<String> result = new LinkedList<String>();
		result.clear();
		LinkedList<JDBInterface> intList = new LinkedList<JDBInterface>();
		intList = getInterfaceList("Disk", "Input");
		String path = "";
		if (intList.size() > 0)
		{
			for (int x = 0; x < intList.size(); x++)
			{
				path = intList.get(x).getRealPath();
				if (result.indexOf(path) == -1)
				{
					result.add(JUtility.formatPath(path));
				}
			}
		}

		return result;
	}

	public Vector<JDBInterface> getInterfaceData(PreparedStatement criteria) {

		ResultSet rs;
		Vector<JDBInterface> result = new Vector<JDBInterface>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBInterface("interfaceType", "interfaceDirection", "Path", "Enabled", "Device"));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBInterface(rs.getString("interface_Type"), rs.getString("interface_Direction"), rs.getString("Path"), rs.getString("Enabled"), rs.getString("Device")));
				}
				rs.close();

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getInterfaceDataResultSet(PreparedStatement criteria) {

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

	public String getInterfaceDirection() {
		return dbInterfaceDirection;
	}

	public LinkedList<JDBInterface> getInterfaceList(String device, String interfaceDirection) {
		LinkedList<JDBInterface> result = new LinkedList<JDBInterface>();
		result.clear();

		PreparedStatement stmt;
		ResultSet rs;

		String schemaName = Common.hostList.getHost(getHostID()).getDatabaseParameters().getjdbcDatabaseSchema();
		String ad = "asc";

		JDBQuery query = new JDBQuery(getHostID(), getSessionID());
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_INTERFACE"));
		query.addParamtoSQL("device=", device);
		query.addParamtoSQL("enabled=", "Y");
		query.addParamtoSQL("interface_direction=", interfaceDirection);

		query.addText(" order by " + "interface_type" + " " + ad);

		query.bindParams();
		stmt = query.getPreparedStatement();
		try
		{
			rs = stmt.executeQuery();
			while (rs.next())
			{
				JDBInterface temp = new JDBInterface(rs.getString("interface_Type"), interfaceDirection, JUtility.formatPath(rs.getString("Path")), rs.getString("Enabled"), rs.getString("Device"));
				result.add(temp);
			}
			rs.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean getInterfaceProperties() {
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.getInterfaceProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getInterfaceType());
			stmt.setString(2, getInterfaceDirection());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Interface Definition");
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

	public boolean getInterfaceProperties(String type, String direction) {
		setInterfaceType(type);
		setInterfaceDirection(direction);
		return getInterfaceProperties();
	}

	public String getInterfaceType() {
		return dbInterfaceType;
	}

	public String getPath() {
		return JUtility.formatPath(dbPath);
	}

	public void getPropertiesfromResultSet(ResultSet rs) {
		try
		{
			clear();
			setInterfaceType(rs.getString("interface_Type"));
			setInterfaceDirection(rs.getString("interface_Direction"));
			setPath(rs.getString("Path"));
			setEnabled(rs.getString("Enabled"));
			setDevice(rs.getString("Device"));
			setFormat(rs.getString("Format"));
			setUOMConversion(rs.getString("uom_conversion"));
			setEmailFlags(rs.getString("email_flags"));
			setEmailAddresses(rs.getString("email_addresses"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getRealPath() {
		String result = getPath();
		result = result.replace("{base_dir}", Common.base_dir);
		logger.debug(result);
		return result;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getUOMConversion() {
		dbUomConversion = JUtility.replaceNullObjectwithBlank(dbUomConversion);
		if (dbUomConversion.equals(""))
		{
			dbUomConversion = "None";
		}
		return dbUomConversion;
	}

	public boolean isEnabled() {
		boolean result = false;
		if (dbEnabled != null)
		{
			if (dbEnabled.equals("Y") == true)
			{
				result = true;
			}
		}
		return result;
	}

	public boolean isValid() {
		boolean result = true;

		if (JUtility.isNullORBlank(getInterfaceType()) == true)
		{
			setErrorMessage("InterfaceType cannot be null");
			result = false;
		}

		if (result == true)
		{
			if (JUtility.isNullORBlank(getInterfaceDirection()) == true)
			{
				setErrorMessage("Interface Direction cannot be null");
				result = false;
			}
		}

		if (result == false)
		{
			logger.debug("isValid [" + getInterfaceType() + "] " + getErrorMessage());
		}

		return result;
	}

	public boolean isValidInterface() {

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.isValidInterface"));
			stmt.setString(1, getInterfaceType());
			stmt.setString(2, getInterfaceDirection());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid Interface");
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidInterface :" + result);

		return result;
	}

	public boolean isValidInterface(String type, String direction) {
		setInterfaceType(type);
		setInterfaceDirection(direction);
		return isValidInterface();
	}

	public void setDevice(String device) {
		dbDevice = device;
	}

	public void setEmailAddresses(String addresses) {
		dbEmailAddresses = addresses;
	}

	public void setEmailError(Boolean yesNo) {
		if (yesNo == true)
		{
			if (getEmailError() == false)
			{
				dbEmailFlags = getEmailFlags() + "E";
			}
		}
		else
		{
			if (getEmailError() == true)
			{
				dbEmailFlags = getEmailFlags().replace("E", "");
			}
		}
	}

	public void setEmailFlags(String flags) {
		dbEmailFlags = flags;
	}

	public void setEmailSuccess(Boolean yesNo) {
		if (yesNo == true)
		{
			if (getEmailSuccess() == false)
			{
				dbEmailFlags = getEmailFlags() + "S";
			}
		}
		else
		{
			if (getEmailSuccess() == true)
			{
				dbEmailFlags = getEmailFlags().replace("S", "");
			}
		}
	}

	public void setEmailWarning(Boolean yesNo) {
		if (yesNo == true)
		{
			if (getEmailWarning() == false)
			{
				dbEmailFlags = getEmailFlags() + "W";
			}
		}
		else
		{
			if (getEmailWarning() == true)
			{
				dbEmailFlags = getEmailFlags().replace("W", "");
			}
		}
	}

	public void setEnabled(Boolean enabled) {
		if (enabled == true)
		{
			dbEnabled = "Y";
		}
		else
		{
			dbEnabled = "N";
		}
	}

	public void setEnabled(String enabled) {
		if (enabled == null)
		{
			dbEnabled = "N";
		}
		else
		{
			dbEnabled = enabled.toUpperCase();
		}
	}

	private void setErrorMessage(String errorMsg) {
		dbErrorMessage = errorMsg;
	}

	public void setFormat(String format) {
		dbFormat = format;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	public void setInterfaceDirection(String direction) {
		dbInterfaceDirection = direction;
	}

	public void setInterfaceType(String type) {
		dbInterfaceType = type;
	}

	public void setPath(String path) {
		dbPath = path.replace(Common.base_dir, "{base_dir}");
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	public void setUOMConversion(String conv) {
		dbUomConversion = conv;
	}

	public boolean update() {
		boolean result = false;

		logger.debug("update [" + getInterfaceType() + "] [" + getInterfaceDirection() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBInterface.update"));

				stmtupdate.setString(1, getPath());
				stmtupdate.setString(2, getEnabled());
				stmtupdate.setString(3, getDevice());
				stmtupdate.setString(4, getFormat());
				stmtupdate.setString(5, getEmailFlags());
				stmtupdate.setString(6, getEmailAddresses());
				stmtupdate.setString(7, getUOMConversion());
				stmtupdate.setString(8, getInterfaceType());
				stmtupdate.setString(9, getInterfaceDirection());

				stmtupdate.execute();
				stmtupdate.clearParameters();

				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;

			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}
}
