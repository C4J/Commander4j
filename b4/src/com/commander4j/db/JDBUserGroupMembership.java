
package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;


public class JDBUserGroupMembership
{

	private String db_error_message;
	private Exception db_exception;
	private String db_group_id;
	private String db_user_id;
	final Logger logger = Logger.getLogger(JDBUserGroupMembership.class);
	private String hostID;
	private String sessionID;
	private JDBAuditPermissions auditPerm;
	private JDBUser user;

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

	public JDBUserGroupMembership(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		auditPerm = new JDBAuditPermissions(getHostID(), getSessionID());
	    user = new JDBUser(getHostID(), getSessionID());
	}

	/**
	 * Method addUsertoGroup.
	 * 
	 * @return boolean
	 */
	public boolean addUsertoGroup(String actionedBy) {
		boolean result = false;

		if (isValidUserGroupMembership() == true)
		{
			result = true;
		}
		else
		{
			try
			{
				logger.debug("addUsertoGroup User [" + getUserId() + "] Group [" + getGroupId() + "]");
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.addUsertoGroup"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER_GROUP", "ADD", getUserId(), getGroupId());
			}
			catch (SQLException e)
			{
				setError(e);
			}
		}
		return result;
	}

	/**
	 * Method create.
	 * 
	 * @param luser_id
	 *            String
	 * @param lgroup_id
	 *            String
	 * @return boolean
	 */
	public boolean create(String luser_id, String lgroup_id) {
		boolean result = false;
		setErrorMessage("");

		try
		{
			setGroupId(lgroup_id);
			setUserId(luser_id);

			if (isValidUserGroupMembership() == false)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.create"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			else
			{
				setErrorMessage("Group Permission already exists");
			}
		}
		catch (SQLException e)
		{
			setError(e);
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
			if (isValidUserGroupMembership() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.delete"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	/**
	 * Method getGroupId.
	 * 
	 * @return String
	 */
	public String getGroupId() {
		return db_group_id;
	}

	/**
	 * Method getGroupsAssignedtoUser.
	 * 
	 * @return LinkedList<String>
	 */
	public LinkedList<String> getGroupsAssignedtoUser() {
		// int tempCount = 0;
		LinkedList<String> groupList = new LinkedList<String>();

		PreparedStatement stmt;
		ResultSet rs;
		// boolean result = false;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.getGroupsAssignedtoUser"));
			stmt.setString(1, getUserId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				groupList.addLast(rs.getString("group_id"));
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return groupList;
	}

	public ResultSet getUserGroupMembershipDataResultSet(String group)
	{
		PreparedStatement stmt;
		ResultSet rs = null;
		setErrorMessage("");
		setGroupId(group);

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.getUsersAssignedtoGroup"));
			stmt.setString(1, getGroupId());
			rs = stmt.executeQuery();

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public LinkedList<JDBListData> getUsersAssignedtoGroup(String group) {
		setGroupId(group);
		return getUsersAssignedtoGroup();
	}
	
	public LinkedList<JDBListData> getUsersAssignedtoGroup() {
		LinkedList<JDBListData> groupList = new LinkedList<JDBListData>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;
		String userID="";
		
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.getUsersAssignedtoGroup"));
			stmt.setString(1, getGroupId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				userID = JUtility.replaceNullStringwithBlank(rs.getString("user_id"));
				if (user.getUserProperties(userID))
				{
					icon = user.getUserIcon();
					JDBListData mld = new JDBListData(icon, index, true, userID);
					groupList.addLast(mld);
				}
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return groupList;
	}
	
	/**
	 * Method getUserId.
	 * 
	 * @return String
	 */
	public String getUserId() {
		return db_user_id;
	}

	/**
	 * Method isValidUserGroupMembership.
	 * 
	 * @return boolean
	 */
	public boolean isValidUserGroupMembership() {
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.isValidUserGroupMembership"));
			stmt.setString(1, getUserId());
			stmt.setString(2, getGroupId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid User Group Membership");
			}
			stmt.close();

		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;

	}

	/**
	 * Method removeAllGroupsfromUser.
	 * 
	 * @return boolean
	 */
	public boolean removeAllGroupsfromUser() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("removeAllGroupsfromUser User [" + getUserId() + "]");
		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.removeAllGroupsfromUser"));
			stmtupdate.setString(1, getUserId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	/**
	 * Method removeAllUsersfromGroup.
	 * 
	 * @return boolean
	 */
	public boolean removeAllUsersfromGroup() {
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.removeAllUsersfromGroup"));
			stmtupdate.setString(1, getGroupId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	/**
	 * Method removeGroupfromUser.
	 * 
	 * @return boolean
	 */
	public boolean removeGroupfromUser() {
		boolean result = false;

		if (isValidUserGroupMembership() == false)
		{
			result = true;
		}
		else
		{
			try
			{
				logger.debug("removeGroupfromUser User [" + getUserId() + "] Group [" + getGroupId() + "]");
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.removeGroupfromUser"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
			}
			catch (SQLException e)
			{
				setError(e);
			}
		}
		return result;
	}

	/**
	 * Method removeUserfromGroup.
	 * 
	 * @return boolean
	 */
	public boolean removeUserfromGroup(String actionedBy) {
		boolean result = false;

		// setUserId(luser_id);

		if (isValidUserGroupMembership() == false)
		{
			result = true;
		}
		else
		{
			try
			{
				logger.debug("removeUserfromGroup User [" + getUserId() + "] Group [" + getGroupId() + "]");
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.removeUserfromGroup"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.setString(2, getGroupId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				
				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER_GROUP", "REMOVE", getUserId(), getGroupId());
			}
			catch (SQLException e)
			{
				setError(e);
			}
		}
		return result;
	}

	/**
	 * Method renameGroupTo.
	 * 
	 * @param lgroup_id
	 *            String
	 * @return boolean
	 */
	public boolean renameGroupTo(String lgroup_id) {
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.renameGroupTo"));
			stmtupdate.setString(1, lgroup_id);
			stmtupdate.setString(2, getGroupId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	/**
	 * Method renameUserTo.
	 * 
	 * @param luser_id
	 *            String
	 * @return boolean
	 */
	public boolean renameUserTo(String luser_id) {
		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUserGroupMembership.renameUserTo"));
			stmtupdate.setString(1, luser_id);
			stmtupdate.setString(2, getUserId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			result = true;
		}
		catch (SQLException e)
		{
			setError(e);
		}

		return result;
	}

	/**
	 * Method setError.
	 * 
	 * @param e
	 *            Exception
	 */
	private void setError(Exception e) {
		logger.error(e);
		db_exception = e;
		setErrorMessage(e.getMessage());
	}

	/**
	 * Method getException.
	 * 
	 * @return Exception
	 */
	public Exception getException() {
		return db_exception;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param ErrorMsg
	 *            String
	 */
	private void setErrorMessage(String ErrorMsg) {
		db_error_message = ErrorMsg;
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return db_error_message;
	}

	/**
	 * Method setGroupId.
	 * 
	 * @param GroupId
	 *            String
	 */
	public void setGroupId(String GroupId) {
		db_group_id = GroupId;
	}

	/**
	 * Method setUserId.
	 * 
	 * @param UserId
	 *            String
	 */
	public void setUserId(String UserId) {
		db_user_id = UserId;
	}
}
