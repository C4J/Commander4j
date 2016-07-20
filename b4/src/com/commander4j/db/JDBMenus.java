// $codepro.audit.disable numericLiterals
/*
 * 
 * Created on 19-May-2005
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
public class JDBMenus
{
	/**
	 * @uml.property name="dbErrorMessage"
	 */
	private String dbErrorMessage;
	/**
	 * @uml.property name="dbMenuId"
	 */
	private String dbMenuId;
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
	private final Logger logger = Logger.getLogger(JDBMenus.class);
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

	public JDBMenus(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	/**
	 * Method create.
	 * 
	 * @param lMenuId
	 *            String
	 * @param lModuleId
	 *            String
	 * @param lSequenceId
	 *            int
	 * @return boolean
	 */
	public boolean create(String lMenuId, String lModuleId, int lSequenceId) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setMenuId(lMenuId);
			setModuleId(lModuleId);
			setSequenceId(lSequenceId);

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.create"));
			stmtupdate.setString(1, getMenuId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setInt(3, getSequenceId());
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
	 * Method deleteMenusForMenuId.
	 * 
	 * @param menuid
	 *            String
	 * @return boolean
	 */
	public boolean deleteMenusForMenuId(String menuid) {
		setMenuId(menuid);
		return deleteMenusForMenuId();
	}

	/**
	 * Method deleteMenusForMenuId.
	 * 
	 * @return boolean
	 */
	public boolean deleteMenusForMenuId() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.deleteMenusForMenuId"));
			stmtupdate.setString(1, getMenuId());
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
	 * Method deleteMenusForModuleId.
	 * 
	 * @param moduleid
	 *            String
	 * @return boolean
	 */
	public boolean deleteMenusForModuleId(String moduleid) {
		setModuleId(moduleid);
		return deleteMenusForModuleId();
	}

	/**
	 * Method deleteMenusForModuleId.
	 * 
	 * @return boolean
	 */
	public boolean deleteMenusForModuleId() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.deleteMenusForModuleId"));
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.delete"));
			stmtupdate.setString(1, getMenuId());
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
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return dbErrorMessage;
	}

	/**
	 * Method getMenuId.
	 * 
	 * @return String
	 */
	public String getMenuId() {
		return dbMenuId;
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.renameModuleTo"));
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
	 * Method renameMenuTo.
	 * 
	 * @param newMenuId
	 *            String
	 * @return boolean
	 */
	public boolean renameMenuTo(String newMenuId) {
		boolean result = false;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.renameMenuTo"));
			stmtupdate.setString(1, newMenuId);
			stmtupdate.setString(2, getMenuId());
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
	 * Method rewriteMenu.
	 * 
	 * @param lMenuId
	 *            String
	 * @param modules
	 *            LinkedList<JDBListData>
	 * @return boolean
	 */
	public boolean rewriteMenu(String lMenuId, LinkedList<JDBListData> modules) {
		boolean result = false;
		String lModuleId;
		int lSequenceId = 0;

		setErrorMessage("");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.rewriteMenu"));
			stmtupdate.setString(1, lMenuId);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();

			for (int j = 0; j < modules.size(); j++)
			{
				lModuleId = modules.get(j).toString();
				create(lMenuId, lModuleId, lSequenceId++);
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
	 * Method setMenuId.
	 * 
	 * @param menuId
	 *            String
	 */
	public void setMenuId(String menuId) {
		dbMenuId = menuId;
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
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMenus.update"));
			stmtupdate.setString(1, getMenuId());
			stmtupdate.setString(2, getModuleId());
			stmtupdate.setInt(3, getSequenceId());
			stmtupdate.setString(4, getMenuId());
			stmtupdate.setString(5, getModuleId());
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
