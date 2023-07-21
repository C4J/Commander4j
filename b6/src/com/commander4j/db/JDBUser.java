package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBUser.java
 * 
 * Package Name : com.commander4j.db
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.app.JVersion;
import com.commander4j.sys.Common;
import com.commander4j.util.JCipher;
import com.commander4j.util.JUtility;

/**
 * The JDBUser is used to insert/update/delete records in the SYS_USERS table. The users database contains one record for each user in the application. How a user is authenticated is also determined by some system controls in the SYS_CONTROL table.
 * <p>
 * <img alt="" src="./doc-files/SYS_USERS.jpg" >
 * 
 * @see com.commander4j.db.JDBGroup JDBGroup
 */
public class JDBUser
{
	private String dbAccountExpires;
	private Timestamp dbAccountExpiryDate;
	private String dbAccountLocked;
	private String dbAccountEnabled;
	private int dbBadPasswordAttempts;
	private String dbComment;
	private String dbPasswordChangeRequired;
	private String dbErrorMessage;
	private String dbLanguage;
	private Timestamp dbLastLogin;
	private boolean dbLoggedIn = false;
	private String loginPassword = "";
	private String dbPasswordChangeAllowed;
	private Timestamp dbPasswordChanged;
	private Timestamp dbLockedDate;
	private String dbPasswordCurrent;
	private String dbPasswordExpires;
	private Calendar dbPasswordExpiryCalendar;
	private java.util.Date dbPasswordExpiryDate;
	private String dbPasswordNew;
	private String dbPasswordVerify;
	private String dbPasswordEncrypted = "N";
	private String dbUserId;
	private boolean initialised = false;
	private LinkedList<String> allowedModules = new LinkedList<String>();
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBUser.class);
	private String hostID;
	private String sessionID;
	private JDBControl ctrl;
	private JDBAuditPermissions auditPerm;
	private JDBLanguage lang;
	private String complexAlphabet = "abcdefghijklmnopqrstuvwxyz";
	private String specialAlphabet = "0123456789!@£$%^&*()_+?:;[]{}'#";
	private boolean complexPassword = false;
	private int complexUppercase = 0;
	private int complexLowercase = 0;
	private int complexSpecial = 0;
	private JDBUserGroupMembership userGroupMembership;
	private String dbEmailAddress = "";
	private int dbPasswordVersion = -1;

	public static int field_user_id = 20;
	public static int field_password = 20;
	public static int field_comment = 40;

	public boolean copyTo(String oldUserID, String newUserID, String actionedBy)
	{
		boolean result = false;
		setErrorMessage("");

		if (isValidUserId(newUserID) == false)
		{
			if (isValidUserId(oldUserID) == true)
			{
				getUserProperties(oldUserID);

				LinkedList<String> assignedGroups = new LinkedList<String>();

				assignedGroups = getUserGroupsAssigned();

				setUserId(newUserID);

				if (create(newUserID, actionedBy))
				{
					for (int x = 0; x < assignedGroups.size(); x++)
					{
						addtoGroup(assignedGroups.get(x), actionedBy);
					}
					result = true;
				}
			}
			else
			{
				setErrorMessage("UserId " + oldUserID + " not found.");
			}
		}
		else
		{
			setErrorMessage("UserId " + newUserID + " already exists.");
		}

		return result;
	}

	public boolean isPasswordComplex(String pass)
	{
		boolean result = true;

		complexPassword = Boolean.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX", "false", "Password needs to be complex"));

		if (complexPassword)
		{
			complexUppercase = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX UPPER REQD", "3", "Number of upper case characters required"));
			complexLowercase = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX LOWER REQD", "3", "Number of lower case characters required"));
			complexSpecial = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX SPECIAL REQD", "3", "Number of special characters required"));

			int upperFound = 0;
			int lowerFound = 0;
			int specialFound = 0;

			pass = JUtility.replaceNullStringwithBlank(pass);

			for (int x = 0; x < pass.length(); x++)
			{
				String c = pass.substring(x, x + 1);

				if (complexAlphabet.toLowerCase().contains(c))
					lowerFound++;
				if (complexAlphabet.toUpperCase().contains(c))
					upperFound++;
				if ((complexAlphabet.toUpperCase() + complexAlphabet.toLowerCase()).contains(c) == false)
					specialFound++;
			}

			if (upperFound < complexUppercase)
				result = false;
			if (lowerFound < complexLowercase)
				result = false;
			if (specialFound < complexSpecial)
				result = false;
		}

		if (result == false)
		{
			String errMsg = lang.get("lbl_Password_Not_Complex");
			errMsg = errMsg.replace("\\n", "<br>");
			errMsg = errMsg.replace("{upper}", String.valueOf(complexUppercase));
			errMsg = errMsg.replace("{lower}", String.valueOf(complexLowercase));
			errMsg = errMsg.replace("{special}", String.valueOf(complexSpecial));

			setErrorMessage(errMsg);
		}

		return result;
	}

	public void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setHostID(String host)
	{
		hostID = host;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private String getHostID()
	{
		return hostID;
	}

	public JDBUser(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		ctrl = new JDBControl(getHostID(), getSessionID());
		auditPerm = new JDBAuditPermissions(getHostID(), getSessionID());
		lang = new JDBLanguage(getHostID(), getSessionID());
		// userReport = new JDBUserReport(getHostID(),getSessionID());
	}

	public void clear()
	{
		setAccountExpires("");
		setAccountExpiryDate(null);
		setAccountLocked("N");
		setAccountEnabled("Y");
		setBadPasswordAttempts(0);
		setComment("");
		setLanguage("");
		setLastLogin(null);
		setPasswordChangeAllowed("");
		setPasswordChangeRequired("N");
		setPasswordExpires("");
		setPasswordNew("");
		setPasswordVerify("");
	}

	private void init()
	{
		userGroupMembership = new JDBUserGroupMembership(getHostID(), getSessionID());

		dbPasswordExpiryCalendar = Calendar.getInstance();
		dbPasswordExpiryCalendar.add(Calendar.DATE, -1 * Common.user_password_expiry_days);
		dbPasswordExpiryDate = dbPasswordExpiryCalendar.getTime();

		initialised = true;
	}

	public Icon getUserIcon()
	{
		Icon icon = new ImageIcon();

		try
		{
			if (isAccountEnabled() == false)
			{
				icon = Common.icon_user_disabled_16x16;
			}
			else
			{
				if (getAccountLocked().equals("Y") == true)
				// if (isAccountLocked() == true)
				{
					icon = Common.icon_user_locked_16x16;
				}
				else
				{
					if (isAccountExpired() == true)
					{
						icon = Common.icon_user_expired_16x16;
					}
					else
					{
						icon = Common.icon_user_16x16;
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		return icon;
	}

	public boolean addtoGroup(String lgroup_id, String actionedBy)
	{
		boolean result = false;

		userGroupMembership.setUserId(getUserId());
		userGroupMembership.setGroupId(lgroup_id);
		result = userGroupMembership.addUsertoGroup(actionedBy);

		return result;
	}

	public boolean changePassword()
	{
		boolean result = false;
		setErrorMessage("");
		logger.debug("changePassword");
		int nonRepeatingPasswords = Integer.valueOf(ctrl.getKeyValue("PASSWORD VERSION HISTORY"));
		int lookUpVersion = getPasswordVersion() - nonRepeatingPasswords;
		String encryptPassword = ctrl.getKeyValue("PASSWORD ENCRYPTED").toUpperCase();
		try
		{
			if (isValidUserId() == true)
			{

				if (isNewPasswordValid())
				{
					String cipherText = "";

					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.changePassword"));
					String newPassword = getPasswordNew();
					if (encryptPassword.equals("Y"))
					{

						JCipher advancedEncryptionStandard = new JCipher(Common.encryptionKey);
						cipherText = advancedEncryptionStandard.encode(newPassword);

					}
					else
					{
						cipherText = newPassword;
					}
					setPasswordEncrypted(encryptPassword);

					stmtupdate.setString(1, cipherText);
					stmtupdate.setTimestamp(2, (Timestamp) JUtility.getSQLDateTime());
					stmtupdate.setString(3, getPasswordEncrypted());
					stmtupdate.setString(4, getUserId());
					stmtupdate.execute();
					stmtupdate.clearParameters();

					if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
					{
						Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					}
					stmtupdate.close();
					result = true;

					setPasswordChangeRequired("N");

					if (nonRepeatingPasswords > 0)
					{
						recordPasswordChange();
						archivePasswordHistory(lookUpVersion + 1);
					}
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	private boolean recordPasswordChange()
	{
		boolean result = false;
		setErrorMessage("");
		logger.debug("recordPasswordChange");
		try
		{

			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.recordPasswordChange"));
			stmtupdate.setString(1, getUserId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean archivePasswordHistory(int lookUpVersion)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("archivePasswordHistory");

		try
		{
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.archivePasswordHistory"));
			stmtupdate.setString(1, getUserId());
			stmtupdate.setInt(2, lookUpVersion);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}

			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean create(String luser_id, String actionedBy)
	{
		boolean result = false;
		setErrorMessage("");
		logger.debug("create");

		try
		{
			setUserId(luser_id);

			if (isValidUserId() == false)
			{
				PreparedStatement stmtupdate;

				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.create"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();

				if (changePassword() == true)
				{
					if (update() == true)
					{
						result = true;
					}
				}
				else
				{
					result = true;
				}

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "CREATE", getUserId(), "");

			}
			else
			{
				setErrorMessage("UserId already exists");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean delete(String luser_id, String actionedBy)
	{
		if (luser_id.toLowerCase().equals("interface"))
		{
			setErrorMessage("System account INTERFACE cannot be deleted.");
			return false;
		}

		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");
		setUserId(luser_id);
		try
		{
			if (isValidUserId() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.delete"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}

				userGroupMembership.setUserId(getUserId());
				result = userGroupMembership.removeAllGroupsfromUser();

				stmtupdate.close();
				result = true;

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "DELETE", getUserId(), "");

				deletePasswordHistory(luser_id);

			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean deletePasswordHistory(String luser_id)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("deletePasswordHistory");
		setUserId(luser_id);
		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.deletePasswordHistory"));
			stmtupdate.setString(1, luser_id);
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean renamePasswordHistory(String newuser_id)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("renamePasswordHistory");

		try
		{

			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.renamePasswordHistory"));
			stmtupdate.setString(1, newuser_id);
			stmtupdate.setString(2, getUserId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}
			stmtupdate.close();
			result = true;

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean lock(String userid, String actionedBy)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidUserId() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.lock"));
				stmtupdate.setTimestamp(1, JUtility.getSQLDateTime());
				stmtupdate.setString(2, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();
				result = true;
				setAccountLocked("Y");

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "LOCK", getUserId(), "");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean unlock(String actionedBy)
	{

		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidUserId() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.unlock"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();
				result = true;
				setAccountLocked("N");

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "UNLOCK", getUserId(), "");

			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean disable(String userid, String actionedBy)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidUserId() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.disable"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();
				result = true;
				setAccountEnabled("N");

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "DISABLE", getUserId(), "");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean enable(String userid, String actionedBy)
	{

		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");

		try
		{
			if (isValidUserId() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.enable"));
				stmtupdate.setString(1, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();
				result = true;
				setAccountEnabled("Y");

				auditPerm.generateNewAuditLogID();
				auditPerm.write(actionedBy, "USER", "ENABLE", getUserId(), "");

			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String getAccountExpires()
	{
		dbAccountExpires = JUtility.replaceNullStringwithBlank(dbAccountExpires);
		if (dbAccountExpires.equals(""))
		{
			dbAccountExpires = "N";
		}
		return dbAccountExpires;
	}

	public Timestamp getAccountExpiryDate()
	{
		return dbAccountExpiryDate;
	}

	public String getAccountLocked()
	{
		dbAccountLocked = JUtility.replaceNullStringwithBlank(dbAccountLocked);
		if (dbAccountLocked.equals(""))
		{
			dbAccountLocked = "N";
		}
		return dbAccountLocked;
	}

	public String getPasswordChangeRequired()
	{
		dbPasswordChangeRequired = JUtility.replaceNullStringwithBlank(dbPasswordChangeRequired);
		if (dbPasswordChangeRequired.equals(""))
		{
			dbPasswordChangeRequired = "N";
		}
		return dbPasswordChangeRequired;
	}

	public String getAccountEnabled()
	{
		dbAccountEnabled = JUtility.replaceNullStringwithBlank(dbAccountEnabled);
		if (dbAccountEnabled.equals(""))
		{
			dbAccountEnabled = "Y";
		}
		return dbAccountEnabled;
	}

	public int getBadPasswordAttempts()
	{
		return dbBadPasswordAttempts;
	}

	public String getComment()
	{
		return JUtility.replaceNullStringwithBlank(dbComment);
	}

	public String getEmailAddress()
	{
		return dbEmailAddress;
	}

	private int getPasswordVersion()
	{
		return dbPasswordVersion;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getLanguage()
	{
		if (JUtility.replaceNullStringwithBlank(dbLanguage).equals(""))
		{
			dbLanguage = Locale.getDefault().getLanguage().toUpperCase();
		}
		return dbLanguage;
	}

	public String getPasswordEncrypted()
	{
		return dbPasswordEncrypted;
	}

	public void setPasswordEncrypted(String yesNo)
	{
		dbPasswordEncrypted = yesNo;
	}

	public void setPasswordEncrypted(boolean yesNo)
	{
		if (yesNo)
		{
			dbPasswordEncrypted = "Y";
		}
		else
		{
			dbPasswordEncrypted = "N";
		}
	}

	public boolean isPasswordEncrypted()
	{
		if (dbPasswordEncrypted.equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Timestamp getLastLoginTimestamp()
	{
		return dbLastLogin;
	}

	public String getPassword()
	{
		return JUtility.replaceNullStringwithBlank(dbPasswordCurrent);
	}

	public String getLoginPassword()
	{
		return JUtility.replaceNullStringwithBlank(loginPassword);
	}

	public void setLoginPassword(String pass)
	{
		loginPassword = JUtility.replaceNullStringwithBlank(pass);
	}

	public String getDecodedPassword()
	{

		String result = "";

		if (isPasswordEncrypted())
		{

			JCipher advancedEncryptionStandard = new JCipher(Common.encryptionKey);

			result = advancedEncryptionStandard.decode(getPassword());
		}
		else
		{

			result = getPassword();
		}

		return result;
	}

	public String getPasswordChangeAllowed()
	{
		return dbPasswordChangeAllowed;
	}

	public Timestamp getPasswordChanged()
	{
		return dbPasswordChanged;
	}

	public Timestamp getAccountLockedDate()
	{
		return dbLockedDate;
	}

	public String getPasswordExpires()
	{
		return dbPasswordExpires;
	}

	public java.util.Date getPasswordExpiryDate()
	{
		return dbPasswordExpiryDate;
	}

	private String getPasswordNew()
	{
		return dbPasswordNew;
	}

	private String getPasswordVerify()
	{
		return dbPasswordVerify;
	}

	public LinkedList<String> getUserGroupsAssigned()
	{
		LinkedList<String> groupList = new LinkedList<String>();

		userGroupMembership.setUserId(getUserId());
		groupList = userGroupMembership.getGroupsAssignedtoUser();

		return groupList;
	}

	public LinkedList<String> getUserGroupsUnAssigned()
	{
		LinkedList<String> groupList = new LinkedList<String>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserGroupsUnAssigned"));
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
			setErrorMessage(e.getMessage());
		}

		return groupList;
	}

	public String getUserId()
	{
		return dbUserId.toUpperCase();
	}

	public ResultSet getUserDataResultSet()
	{
		Statement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserIds"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	public ResultSet getUserPermissionsResultSet()
	{
		Statement stmt;
		ResultSet rs = null;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserPermisions"));

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return rs;
	}
	
	public String toString()
	{
		return JUtility.padString(getUserId(), true, field_user_id, " ") + " " + getComment();
	}

	public LinkedList<JDBListData> getUserIds()
	{
		LinkedList<JDBListData> userList = new LinkedList<JDBListData>();

		Statement stmt;
		ResultSet rs;
		setErrorMessage("");
		Icon icon = new ImageIcon();
		int index = 0;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).createStatement();
			stmt.setFetchSize(250);
			rs = stmt.executeQuery(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserIds"));

			while (rs.next())
			{
				JDBUser u = new JDBUser(getHostID(), getSessionID());
				u.setUserId(rs.getString("user_id"));
				u.setPassword(rs.getString("password"));
				u.setComment(rs.getString("user_comment"));
				u.setEmailAddress(rs.getString("email_address"));
				u.setLanguage(rs.getString("language_id"));
				u.setLastLogin(rs.getTimestamp("last_logon"));
				u.setPasswordExpires(rs.getString("password_expires"));
				u.setPasswordChanged(rs.getTimestamp("last_password_change"));
				u.setBadPasswordAttempts(rs.getInt("bad_password_attempts"));
				u.setAccountLocked(rs.getString("account_locked"));
				u.setAccountLockedDate(rs.getTimestamp("account_locked_date"));
				u.setAccountEnabled(rs.getString("account_enabled"));
				u.setPasswordChangeAllowed(rs.getString("password_change_allowed"));
				u.setAccountExpires(rs.getString("account_expires"));
				u.setAccountExpiryDate(rs.getTimestamp("account_expiry_date"));
				u.setPasswordVersion(rs.getInt("password_version"));
				u.setPasswordChangeRequired(rs.getString("password_change_required"));
				icon = u.getUserIcon();

				JDBListData mld = new JDBListData(icon, index, true, u);
				userList.addLast(mld);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return userList;
	}

	public boolean getUserProperties(String userid)
	{
		setUserId(userid);
		return getUserProperties();
	}

	public boolean getUserProperties()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserProperties"));
			stmt.setString(1, getUserId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				// setUserId(rs.getString("user_id"));
				setPassword(rs.getString("password"));
				setComment(rs.getString("user_comment"));
				setEmailAddress(rs.getString("email_address"));
				setLanguage(rs.getString("language_id"));
				setLastLogin(rs.getTimestamp("last_logon"));
				setPasswordExpires(rs.getString("password_expires"));
				setPasswordChanged(rs.getTimestamp("last_password_change"));
				setBadPasswordAttempts(rs.getInt("bad_password_attempts"));
				setAccountLocked(rs.getString("account_locked"));
				setAccountLockedDate(rs.getTimestamp("account_locked_date"));
				setAccountEnabled(rs.getString("account_enabled"));
				setPasswordChangeAllowed(rs.getString("password_change_allowed"));
				setAccountExpires(rs.getString("account_expires"));
				setAccountExpiryDate(rs.getTimestamp("account_expiry_date"));
				setPasswordVersion(rs.getInt("password_version"));
				setPasswordChangeRequired(rs.getString("password_change_required"));
				setPasswordEncrypted(rs.getString("password_encrypted"));
				result = true;
				rs.close();
				stmt.close();
			}
			else
			{
				setPasswordEncrypted("N");
				setErrorMessage("Invalid UserId [" + getUserId() + "]");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isAccountExpired()
	{
		boolean result = false;

		if (getAccountExpires() == null)
		{
			setAccountExpires("N");
		}
		if (getAccountExpires().equals("Y"))
		{
			try
			{
				Calendar caldate = Calendar.getInstance();
				java.sql.Date curdate = new java.sql.Date(caldate.getTimeInMillis());
				if (getAccountExpiryDate().after(curdate))
				{
					result = false;
				}
				else
				{
					result = true;
				}
			}
			catch (Exception ex)
			{
				result = true;
			}
		}

		return result;
	}

	public boolean isAccountExpiring()
	{
		boolean result = false;

		if (getAccountExpires().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isAccountLocked()
	{
		boolean result = false;

		if (getAccountLocked().equals("Y") == true)
		{
			result = true;
			Timestamp whenLocked = getAccountLockedDate();
			int autoUnlock = Integer.valueOf(ctrl.getKeyValue("PASSWORD AUTO UNLOCK"));

			if (autoUnlock > 0)
			{
				Timestamp now = JUtility.getSQLDateTime();
				long diffMinutes;
				try
				{
					diffMinutes = now.getTime() - whenLocked.getTime();
				}
				catch (Exception ex)
				{
					diffMinutes = 0;
				}

				diffMinutes = diffMinutes / 1000;

				diffMinutes = diffMinutes / 60;

				if (diffMinutes >= autoUnlock)
				{
					// Unlock account as it's been locked more at least then
					// number of minutes specified in "PASSWORD AUTO UNLOCK"
					unlock("SYSTEM");
					result = false;
				}
				else
				{
					result = true;
				}
			}
			else
			{
				result = true;
			}
			;
		}

		return result;
	}

	public boolean isPasswordChangeRequired()
	{
		boolean result = false;

		if (getPasswordChangeRequired().equals("Y") == true)
		{
			result = true;
		}

		return result;
	}

	public boolean isAccountEnabled()
	{
		boolean result = false;

		if (getAccountEnabled().equals("Y") == true)
		{
			result = true;
		}

		return result;
	}

	public boolean isLoggedIn()
	{
		return dbLoggedIn;
	}

	public String generateRandomPassword()
	{
		String result = "";
		Boolean valid = false;

		int minsize = Integer.valueOf(ctrl.getKeyValue("PASSWORD MIN SIZE"));
		int maxsize = Integer.valueOf(ctrl.getKeyValue("PASSWORD MAX SIZE"));
		int complexUppercase = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX UPPER REQD", "3", "Number of upper case characters required"));
		int complexLowercase = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX LOWER REQD", "3", "Number of lower case characters required"));
		int complexSpecial = Integer.valueOf(ctrl.getKeyValueWithDefault("PASSWORD COMPLEX SPECIAL REQD", "3", "Number of special characters required"));
		int lowerFound = 0;
		int upperFound = 0;
		int specialFound = 0;
		int randomNum = 0;
		Boolean lowerOK = false;
		Boolean upperOK = false;
		Boolean specialOK = false;
		
		if (minsize < (complexUppercase + complexLowercase + complexSpecial))
		{
			minsize = (complexUppercase + complexLowercase + complexSpecial);
		}
		
		if (minsize > maxsize)
		{
			maxsize = minsize;
		}

		while (valid == false)
		{

			while ((lowerOK==false) | (upperOK==false) | (specialOK==false))
			{

				for (int x = 0; x < result.length(); x++)
				{
					String c = result.substring(x, x + 1);

					if (complexAlphabet.toLowerCase().contains(c))
						lowerFound++;
					if (complexAlphabet.toUpperCase().contains(c))
						upperFound++;
					if ((complexAlphabet.toUpperCase() + complexAlphabet.toLowerCase()).contains(c) == false)
						specialFound++;
				}

				if (lowerFound < complexLowercase)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, complexAlphabet.length());
					result = result + complexAlphabet.toLowerCase().substring(randomNum, randomNum + 1);
				}
				else
				{
					lowerOK= true;
				}

				if (upperFound < complexUppercase)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, complexAlphabet.length());
					result = result + complexAlphabet.toUpperCase().substring(randomNum, randomNum + 1);
				}
				else
				{
					upperOK= true;
				}

				if (specialFound < complexSpecial)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, specialAlphabet.length());
					result = result + specialAlphabet.toUpperCase().substring(randomNum, randomNum + 1);
				}
				else
				{
					specialOK = true;
				}

			}
			
			
			int actual = ThreadLocalRandom.current().nextInt(minsize, maxsize+1);
			
			while (result.length()  != actual)
			{
				if (result.length() != actual)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, complexAlphabet.length());
					result = result + complexAlphabet.toLowerCase().substring(randomNum, randomNum + 1);
				}

				if (result.length() != actual)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, complexAlphabet.length());
					result = result + complexAlphabet.toUpperCase().substring(randomNum, randomNum + 1);
				}

				if (result.length() != actual)
				{
					randomNum = ThreadLocalRandom.current().nextInt(0, specialAlphabet.length());
					result = result + specialAlphabet.toUpperCase().substring(randomNum, randomNum + 1);
				}
			}
			
			setPasswordNew(result);
			setPasswordVerify(result);

			valid = isNewPasswordValid();
			if (valid)
			{
				valid = isPasswordComplex(result);
			}

		}

		return result;
	}

	public boolean isNewPasswordValid()
	{
		boolean result = false;

		try
		{
			if (getPasswordNew().equals(getPasswordVerify()) == true)
			{

				int nonRepeatingPasswords = Integer.valueOf(ctrl.getKeyValue("PASSWORD VERSION HISTORY"));
				int minsize = Integer.valueOf(ctrl.getKeyValue("PASSWORD MIN SIZE"));
				int maxsize = Integer.valueOf(ctrl.getKeyValue("PASSWORD MAX SIZE"));

				if ((getPasswordNew().length() < minsize) | (getPasswordNew().length() > maxsize))
				{
					setErrorMessage("Password size must be between " + String.valueOf(minsize) + " and " + String.valueOf(maxsize));
				}
				else
				{
					if (isPasswordComplex(getPasswordNew()))
					{
						if (isPasswordUsedBefore(nonRepeatingPasswords) == false)
						{
							if (getPasswordNew().toUpperCase().contains(getUserId().toUpperCase()) == false)
							{
								result = true;
							}
							else
							{
								setErrorMessage("Password cannot contain user id.");
							}
						}
						else
						{
							setErrorMessage("Password used previously");
						}
					}
					else
					{
						// use error message from the isPasswordComplex
						// function.
					}
				}
			}
			else
			{
				setErrorMessage("New password not verified");
			}
		}
		catch (Exception e)
		{
			setErrorMessage("Error during password validation");
		}

		return result;
	}

	private boolean isPasswordUsedBefore(int nonRepeatingPasswords)
	{
		boolean result = false;
		int lookUpVersion = getPasswordVersion() - nonRepeatingPasswords;

		PreparedStatement stmt;
		ResultSet rs;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.findInPasswordHistory"));
			stmt.setString(1, getUserId());
			stmt.setInt(2, lookUpVersion);
			stmt.setString(3, getPasswordNew());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}

			rs.close();
			stmt.close();

			if (result == true)
			{
				setErrorMessage("Password used previously");
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	/**
	 * Method isPasswordChangeAllowed.
	 * 
	 * @return boolean
	 */
	public boolean isPasswordChangeAllowed()
	{
		boolean result = false;

		if (dbPasswordChangeAllowed.equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isPasswordExpired()
	{
		boolean result = false;

		if (getUserId().toLowerCase().equals("interface"))
		{
			return false;
		}

		if (getPasswordExpires().equals("Y"))
		{
			try
			{
				// System.out.println("isPasswordExpired() -
				// getPasswordChanged() = "+getPasswordChanged().toGMTString());
				// System.out.println("isPasswordExpired() -
				// getPasswordExpiryDate() =
				// "+getPasswordExpiryDate().toGMTString());
				if (getPasswordChanged().after(getPasswordExpiryDate()))
				{
					result = false;
				}
				else
				{
					result = true;
				}
			}
			catch (Exception ex)
			{
				result = true;
			}
		}

		return result;
	}

	public boolean isPasswordExpiring()
	{
		boolean result = false;

		if (getPasswordExpires().equals("Y"))
			result = true;
		else
			result = false;

		return result;
	}

	public boolean isValidPassword()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		String temp = ctrl.getKeyValue("PASSWORD COMPLEX");
		boolean passwordComplex = Boolean.valueOf(temp);
		String passwordMode = ctrl.getKeyValue("PASSWORD MODE");

		if (Common.applicationMode.equals("SwingClient"))
		{
			if (isValidUserId())
			{
				if (passwordMode.equals("PASSTHROUGH"))
				{
					result = true;
					return result;
				}

				if ((passwordMode.equals("PASSTHROUGH_FALLBACK")) && getUserId().equals(System.getProperty("user.name").toUpperCase()))
				{
					result = true;
					return result;
				}

			}

		}

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.isValidPassword"));
			stmt.setString(1, getUserId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			if (rs.next())
			{

				String decodedStoredPass = getDecodedPassword();

				if (passwordComplex)
				{
					if (getLoginPassword().equals(getDecodedPassword()))
					{
						result = true;
					}
				}
				else
				{
					if (getLoginPassword().toUpperCase().equals(decodedStoredPass.toUpperCase()))
					{
						result = true;
					}
				}
			}

			rs.close();
			stmt.close();

			if (result == false)
			{
				setErrorMessage("Invalid username or password");
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public boolean isValidUserId(String user)
	{
		setUserId(user);
		return isValidUserId();
	}

	public boolean isValidUserId()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		if (getUserId() == null)
		{
			setUserId("");
		}

		if (getUserId().isEmpty() == false)
		{
			try
			{
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.isValidUserId"));

				stmt.setString(1, getUserId());
				stmt.setFetchSize(1);
				rs = stmt.executeQuery();

				if (rs.next())
				{
					setPasswordVersion(rs.getInt("PASSWORD_VERSION"));
					result = true;
				}
				else
				{
					setErrorMessage("Invalid UserId [" + getUserId() + "]");
				}
				rs.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public void loadModules()
	{
		allowedModules.clear();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.loadModules"));
			stmt.setString(1, getUserId());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				allowedModules.addLast(rs.getString("module_id"));
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public boolean isModuleAllowed(String module_id)
	{
		boolean result = true;

		if (allowedModules.indexOf(module_id) == -1)
		{
			result = false;
		}

		return result;
	}

	public boolean login()
	{
		boolean result = false;
		setLoggedIn(false);

		if (getUserId().toLowerCase().equals("interface"))
		{
			getUserProperties();
			setLoggedIn(true);
			loadModules();
			return true;
		}

		try
		{
			if (isValidUserId() == true)
			{
				getUserProperties();

				if (isAccountEnabled() == true)
				{
					if (isAccountLocked() == false)
					{
						if (isAccountExpired() == false)
						{
							if (isValidPassword() == true)
							{
								PreparedStatement stmtupdate;
								stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.login"));
								stmtupdate.setTimestamp(1, JUtility.getSQLDateTime());
								stmtupdate.setString(2, JVersion.getProgramVersion());
								stmtupdate.setString(3, JUtility.getClientName());
								stmtupdate.setString(4, getUserId());
								stmtupdate.execute();
								stmtupdate.clearParameters();
								if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
								{
									Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
								}
								result = true;
								setLoggedIn(true);
								loadModules();
								stmtupdate.close();
							}
							else
							{
								updateBadPasswordCount();
								getBadPasswordCount();
								setErrorMessage("Invalid username or password");
								if (getBadPasswordAttempts() >= Common.user_max_password_attempts)
								{
									lock(getUserId(), getUserId());
									setErrorMessage("Account Locked");
								}
							}
						}
						else
						{
							setErrorMessage("Account Expired");
						}
					}
					else
					{
						setErrorMessage("Account Locked");
					}
				}
				else
				{
					setErrorMessage("Account Disabled");
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("login :" + e.getMessage());
		}

		logger.debug("login :" + result);
		return result;
	}

	public void logout()
	{
		setLoggedIn(false);
		logger.debug("logout");

	}

	private boolean getBadPasswordCount()
	{
		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;
		setErrorMessage("");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.getUserProperties"));
			stmt.setString(1, getUserId());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				setBadPasswordAttempts(rs.getInt("bad_password_attempts"));
				setAccountLocked(rs.getString("account_locked"));
				result = true;
				rs.close();
				stmt.close();
			}
			else
			{
				setErrorMessage("Invalid UserId [" + getUserId() + "]");
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	private void updateBadPasswordCount()
	{
		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.login(fail)"));
			stmtupdate.setString(1, getUserId());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
			{
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			}
			stmtupdate.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public boolean removefromGroup(String lgroup_id, String actionedBy)
	{
		boolean result = false;

		userGroupMembership.setGroupId(lgroup_id);
		userGroupMembership.setUserId(getUserId());
		result = userGroupMembership.removeUserfromGroup(actionedBy);

		return result;
	}

	public boolean renameTo(String newUserId)
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("renameTo :" + newUserId);

		try
		{
			if (isValidUserId() == true)
			{
				JDBUser newuser = new JDBUser(getHostID(), getSessionID());
				newuser.setUserId(newUserId);
				if (newuser.isValidUserId() == false)
				{

					userGroupMembership.setUserId(getUserId());
					userGroupMembership.renameUserTo(newUserId);

					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.renameTo"));
					stmtupdate.setString(1, newUserId);
					stmtupdate.setString(2, getUserId());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
					{
						Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					}
					stmtupdate.close();

					renamePasswordHistory(newUserId);

					setUserId(newUserId);
					result = true;
				}
				else
				{
					setErrorMessage("New user_id is already in use.");
				}
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public void setAccountExpires(String AccountExpires)
	{
		dbAccountExpires = JUtility.replaceNullStringwithBlank(AccountExpires);
		if (dbAccountExpires.equals(""))
		{
			dbAccountExpires = "N";
		}
	}

	public void setAccountExpiryDate(Timestamp expiryDate)
	{
		dbAccountExpiryDate = expiryDate;
	}

	public void setAccountLocked(String AccountLocked)
	{
		dbAccountLocked = JUtility.replaceNullStringwithBlank(AccountLocked);
		if (dbAccountLocked.equals(""))
		{
			dbAccountLocked = "N";
		}

	}

	public void setAccountLocked(boolean AccountLocked)
	{
		if (AccountLocked)
		{
			setAccountLocked("Y");
		}
		else
		{
			setAccountLocked("N");
		}

	}

	public void setPasswordChangeRequired(String pcr)
	{
		dbPasswordChangeRequired = pcr;
	}

	public void setAccountEnabled(String AccountEnabled)
	{

		dbAccountEnabled = JUtility.replaceNullStringwithBlank(AccountEnabled);

		if (dbAccountEnabled.equals(""))
		{
			dbAccountEnabled = "N";
		}
	}

	private void setBadPasswordAttempts(int BadPasswords)
	{
		dbBadPasswordAttempts = BadPasswords;
	}

	public void setComment(String Comment)
	{
		dbComment = Comment;
	}

	public void setEmailAddress(String email)
	{
		dbEmailAddress = email;
	}

	private void setPasswordVersion(int ver)
	{
		dbPasswordVersion = ver;
	}

	private void setErrorMessage(String ErrorMsg)
	{
		if (ErrorMsg.isEmpty() == false)
		{
			logger.error(ErrorMsg);
		}
		dbErrorMessage = ErrorMsg;
	}

	public void setLanguage(String Language)
	{
		dbLanguage = Language;
	}

	private void setLastLogin(java.sql.Timestamp LastLogin)
	{
		dbLastLogin = LastLogin;
	}

	private void setLoggedIn(boolean flag)
	{
		dbLoggedIn = flag;
	}

	public void setPassword(String Password)
	{
		Password = JUtility.replaceNullStringwithBlank(Password);
		
		Password = Password.replaceAll("\\r", "");
		Password = Password.replaceAll("\\n", "");
		
		if (Password.equals(""))
		{
			Password = "*";
		}
		dbPasswordCurrent = Password;
	}

	public void setPasswordChangeAllowed(String PasswordChangeAllowed)
	{
		dbPasswordChangeAllowed = PasswordChangeAllowed;
	}

	private void setPasswordChanged(Timestamp PasswordChanged)
	{
		try
		{
			dbPasswordChanged = PasswordChanged;
		}
		catch (Exception ex)
		{
			dbPasswordChanged = null;
		}
	}

	private void setAccountLockedDate(Timestamp lockdate)
	{
		try
		{
			dbLockedDate = lockdate;
		}
		catch (Exception ex)
		{
			dbLockedDate = JUtility.getSQLDateTime();
		}
	}

	public void setPasswordExpires(String PasswordExpires)
	{
		dbPasswordExpires = PasswordExpires;
	}

	public void setPasswordNew(String Password)
	{
		if (Password == null)
		{
			Password = "*";
		}
		if (Password.equals(""))
		{
			Password = "*";
		}
		dbPasswordNew = Password;
	}

	public void setPasswordVerify(String Password)
	{
		if (Password == null)
		{
			Password = "*";
		}
		if (Password.equals(""))
		{
			Password = "*";
		}
		dbPasswordVerify = Password;
	}

	public void setUserId(String UserId)
	{
		if (initialised == false)
		{
			init();
		}
		dbUserId = UserId.toUpperCase();
	}

	public boolean update()
	{
		boolean result = false;
		setErrorMessage("");
		logger.debug("update");

		try
		{
			if (isValidUserId() == true)
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBUser.update"));
				stmtupdate.setString(1, getPasswordExpires());
				stmtupdate.setString(2, getPasswordChangeAllowed());
				stmtupdate.setString(3, getLanguage());
				stmtupdate.setString(4, getComment());
				stmtupdate.setString(5, getAccountExpires());
				stmtupdate.setTimestamp(6, getAccountExpiryDate());
				stmtupdate.setString(7, getEmailAddress());
				stmtupdate.setString(8, getPasswordChangeRequired());
				stmtupdate.setString(9, getUserId());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				if (Common.hostList.getHost(getHostID()).getConnection(getSessionID()).getAutoCommit() == false)
				{
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				}
				stmtupdate.close();
				result = true;
			}
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}
}
