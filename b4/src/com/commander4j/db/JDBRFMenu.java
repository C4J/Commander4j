// $codepro.audit.disable numericLiterals
/*
 * Created on 22-May-2005
 *
 */
package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;

/**
 * @author David
 * 
 * @version $Revision: 1.0 $
 */
public class JDBRFMenu
{
	/**
	 * @uml.property name="dbErrorMessage"
	 */
	private String dbErrorMessage;
	/**
	 * @uml.property name="dbModuleId"
	 */
	private String dbModuleId;
	/**
	 * @uml.property name="dbSequenceId"
	 */
	private int dbSequenceId;
	/**
	 * @uml.property name="logger"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final Logger logger = Logger.getLogger(JDBRFMenu.class);
	private String hostID;
	private String sessionID;

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getSessionID() {
		return sessionID;
	}

	private String getHostID() {
		return hostID;
	}

	public JDBRFMenu(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	/**
	 * Method create.
	 * 
	 * @param lModuleId
	 *            String
	 * @param lSequenceId
	 *            int
	 * @return boolean
	 */
	public boolean create(String lModuleId, int lSequenceId) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setModuleId(lModuleId);
			setSequenceId(lSequenceId);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.create"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setInt(2, getSequenceId());
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

	/**
	 * Method delete.
	 * 
	 * @return boolean
	 */
	public boolean delete() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.delete"));
			stmtupdate.setString(1, getModuleId());
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

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return dbErrorMessage;
	}

	/**
	 * Method getModuleId.
	 * 
	 * @return String
	 */
	public String getModuleId() {
		return dbModuleId;
	}

	/**
	 * Method getSequenceId.
	 * 
	 * @return int
	 */
	public int getSequenceId() {
		return dbSequenceId;
	}

	/**
	 * Method renameModuleTo.
	 * 
	 * @param newModuleId
	 *            String
	 * @return boolean
	 */
	public boolean renameModuleTo(String newModuleId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.renameModuleTo"));
			stmtupdate.setString(1, newModuleId);
			stmtupdate.setString(2, getModuleId());
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

	/**
	 * Method rewriteToolbar.
	 * 
	 * @param modules
	 *            LinkedList<JDBListData>
	 * @return boolean
	 */
	public boolean rewriteRFMenu(LinkedList<JDBListData> modules) {
		boolean result = false;
		String lModuleId;
		int lSequenceId = 0;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.rewriteRFMenu"));
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < modules.size(); j++)
			{
				lModuleId = modules.get(j).toString();
				create(lModuleId, lSequenceId++);
			}
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param errorMsg
	 *            String
	 */
	private void setErrorMessage(String errorMsg) {
		logger.error(errorMsg);
		dbErrorMessage = errorMsg;
	}

	/**
	 * Method setModuleId.
	 * 
	 * @param moduleId
	 *            String
	 */
	public void setModuleId(String moduleId) {
		dbModuleId = moduleId;
	}

	/**
	 * Method setSequenceId.
	 * 
	 * @param sequenceId
	 *            int
	 */
	public void setSequenceId(int sequenceId) {
		dbSequenceId = sequenceId;
	}

	/**
	 * Method update.
	 * 
	 * @return boolean
	 */
	public boolean update() {
		boolean result = false;
		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBRFMenu.update"));
			stmtupdate.setString(1, getModuleId());
			stmtupdate.setInt(2, getSequenceId());
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
}
