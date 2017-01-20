package com.commander4j.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDBMHN
{

	private String dbErrorMessage;
	private String dbMHN_Number;
	private String dbInitiator;
	private String dbAuthorisor;
	private String dbRecorder;
	private String dbReason1;
	private String dbReason2;
	private String dbReason3;
	private Timestamp dbDateCreated;
	private Timestamp dbDateExpected;
	private Timestamp dbDateResolved;
	private String dbComments;
	private String dbStatus;
	private String dbResource;
	
	public static int field_mhn_number = 10;
	public static int field_initiator = 20;
	public static int field_recorder = 20;
	public static int field_authorisor = 20;
	public static int field_reason1 = 10;	
	public static int field_reason2 = 10;
	public static int field_reason3 = 10;
	public static int field_comments = 250;	

	private final Logger logger = Logger.getLogger(JDBMHN.class);

	private String hostID;

	private String sessionID;

	public JDBMHN(ResultSet rs) {
		getPropertiesfromResultSet(rs);
	}
	
	
	public JDBMHN(String host, String session) {
		setHostID(host);
		setSessionID(session);
	}
	
	public JDBMHN(String MHNNumber, String initiator, String recorder, String authorisor,String reason1,String reason2,String reason3, Timestamp created,Timestamp expected,Timestamp resolved,String status,String comment,String resource,Integer count) {
		setMHNNumber(MHNNumber);
		setInitiator(initiator);
		setRecorder(recorder);
		setAuthorisor(authorisor);
		setReason1(reason1);
		setReason2(reason2);
		setReason3(reason3);
		setDateCreated(created);
		setDateExpected(expected);
		setDateResolved(resolved);
		setStatus(status);
		setComment(comment);
		setResource(resource);
	}

	public void clear()
	{
		setInitiator("");
		setRecorder("");
		setAuthorisor("");
		setReason1("");
		setReason2("");
		setReason3("");
		setDateCreated(null);
		setDateExpected(null);
		setDateResolved(null);
		setStatus("Pending");
		setComment("");
		setResource("");
	}

	public boolean create()
	{

		logger.debug("create MHN [" + getMHNNumber() + "][" + getRecorder() + "]");

		boolean result = false;

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.create"));
			stmtupdate.setString(1, getMHNNumber());
			stmtupdate.setTimestamp(2, JUtility.getSQLDateTime());
			stmtupdate.setString(3,"Active");
			setRecorder(Common.userList.getUser(getSessionID()).getUserId());
			setDateExpected(null);
			setDateResolved(null);
			stmtupdate.setString(4, getRecorder());
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
		return result;
	}

	public boolean delete()
	{
		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			
			LinkedList<String> ssccs = new LinkedList<String>();
			ssccs.addAll(getPalletsAssigned());
			
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.delete"));
			stmtupdate.setString(1, getMHNNumber());
			stmtupdate.execute();
			stmtupdate.clearParameters();
			Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
			stmtupdate.close();
			
			if (ssccs.size()>0)
			{
				JDBPallet pal = new JDBPallet(getHostID(),getSessionID());
				for (int idx=0;idx<ssccs.size();idx++)
				{
					pal.getPalletProperties(ssccs.get(idx));
					pal.updateMHNDecision("");
					pal.updateMHNNumber("");
				}
				pal=null;
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

	public boolean delete(String number)
	{
		boolean result = false;
		setMHNNumber(number);
		result = delete();
		return result;
	}
	

	public String formatProcessOrderNo(String MHNNo) {
		String result = "error";
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String MHNNoFormat = "{NNNNNNNN}";
		if (ctrl.getProperties("MHN NUMBER FORMAT") == true)
		{
			MHNNoFormat = ctrl.getKeyValue();
		}
		result = JUtility.formatNumber(MHNNo, MHNNoFormat);
		return result;
	}
	
	public String generateNewMHNNumber() {
		String result = "error";
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String MHNNo = "1";
		int SeqNumber = 0;
		boolean retry = true;

		do
		{
			if (ctrl.lockRecord("MHN NUMBER") == true)
			{

				if (ctrl.getProperties("MHN NUMBER") == true)
				{
					MHNNo = ctrl.getKeyValue();
					SeqNumber = Integer.parseInt(MHNNo);

					result = formatProcessOrderNo(MHNNo);
					setMHNNumber(result);
					retry = false;

					SeqNumber++;
					MHNNo = String.valueOf(SeqNumber);
					ctrl.setKeyValue(MHNNo);
					ctrl.update();
				}
			}

		}
		while (retry);

		return result;
	}

	public String getAuthorisor()
	{
		return dbAuthorisor;
	}

	public String getComments()
	{
		return JUtility.replaceNullObjectwithBlank(dbComments);
	}

	public Timestamp getDateCreated()
	{
		return dbDateCreated;
	}

	public Timestamp getDateExpected()
	{
		
		return dbDateExpected;
	}

	public Timestamp getDateResolved()
	{
		return dbDateResolved;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getInitiator()
	{
		return dbInitiator;
	}

	public ResultSet getMHNDataResultSet(PreparedStatement criteria)
	{

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

	public String getMHNNumber()
	{
		return dbMHN_Number;
	}
	
	public boolean getMHNProperties()
	{
		boolean result = false;

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");
		logger.debug("getMHNProperties");

		clear();

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.getMHNProperties"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMHNNumber());
			rs = stmt.executeQuery();

			if (rs.next())
			{
				getPropertiesfromResultSet(rs);
				result = true;
			}
			else
			{
				setErrorMessage("Invalid MHN");
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

	public boolean getMHNProperties(String mhn)
	{
		setMHNNumber(mhn);
		return getMHNProperties();
	}
	
	public LinkedList<String> getPalletsAssigned() {
		LinkedList<String> decisionList = new LinkedList<String>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPallet.assignedToMHN"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMHNNumber());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				String temp = rs.getString("sscc");
				decisionList.addLast(temp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return decisionList;
	}
	
	public LinkedList<String> getPalletDecisionSummmary() {
		LinkedList<String> decisionList = new LinkedList<String>();

		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.palletDecisionSummary"));
			stmt.setFetchSize(1);
			stmt.setString(1, getMHNNumber());
			stmt.setString(2, getMHNNumber());
			rs = stmt.executeQuery();

			while (rs.next())
			{
				String temp = JUtility.padString(rs.getString("Decision"), true, 10, " ")+
				              JUtility.padString(String.valueOf(rs.getBigDecimal("sum_quantity")), false, 12, " ")+
				              "   ( "+JUtility.padString(String.valueOf(rs.getInt("count_sscc")), false, 3, " ")+" )";
				decisionList.addLast(temp);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return decisionList;
	}

	public LinkedList<String> getPalletDecisionSummmary(String number) {
		setMHNNumber(number);
		return getPalletDecisionSummmary();
	}


	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setMHNNumber(rs.getString("MHN_Number"));
			setRecorder(rs.getString("recorder"));
			setInitiator(rs.getString("initiator"));
			setAuthorisor(rs.getString("authorisor"));
			setReason1(rs.getString("reason1"));
			setReason2(rs.getString("reason2"));
			setReason3(rs.getString("reason3"));
			setDateCreated(rs.getTimestamp("date_created"));
			setDateExpected(rs.getTimestamp("date_expected"));
			setStatus(rs.getString("status"));

			setDateResolved(rs.getTimestamp("date_resolved"));


			setComment(rs.getString("comments"));
			setResource(rs.getString("required_resource"));
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public String getReason1()
	{
		return JUtility.replaceNullStringwithBlank(dbReason1);
	}

	public String getReason2()
	{
		return JUtility.replaceNullStringwithBlank(dbReason2);
	}

	public String getReason3()
	{
		return JUtility.replaceNullStringwithBlank(dbReason3);
	}


	public String getRecorder()
	{
		return dbRecorder;
	}

	public String getResource()
	{
		return dbResource;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public String getStatus()
	{
		return dbStatus;
	}

	public boolean isValidMHN() {

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.isValidMHN"));
			stmt.setString(1, getMHNNumber());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			}
			else
			{
				setErrorMessage("Invalid MHN [" + getMHNNumber() + "]");
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

	public void setAuthorisor(String authorisor)
	{
		dbAuthorisor = authorisor;
	}

	public void setComment(String comment)
	{
		dbComments = comment;
	}

	public void setDateCreated(Timestamp created)
	{
		dbDateCreated = created;
	}
	
	public void setDateExpected(Timestamp expected)
	{
		dbDateExpected = expected;
	}

	public void setDateResolved(Timestamp resolved)
	{
		dbDateResolved = resolved;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}
	
	public void setInitiator(String initiator)
	{
		dbInitiator = initiator;
	}

	public void setMHNNumber(String number)
	{
		dbMHN_Number = number;
	}

	public void setReason1(String reason)
	{
		dbReason1 = reason;
	}

	public void setReason2(String reason)
	{
		dbReason2 = reason;
	}

	public void setReason3(String reason)
	{
		dbReason3 = reason;
	}

	
	public void setRecorder(String recorder)
	{
		dbRecorder = recorder;
	}

	public void setResource(String resource)
	{
		dbResource = resource;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public void setStatus(String status)
	{
		dbStatus = status;
	}

	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getMHNNumber() + "] [" + getInitiator() + "]");

		try
		{
			PreparedStatement stmtupdate;
			stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBMHN.update"));

			stmtupdate.setString(1, getRecorder());
			stmtupdate.setString(2, getInitiator());
			stmtupdate.setString(3, getReason1());
			stmtupdate.setString(4, getReason2());
			stmtupdate.setString(5, getReason3());
			stmtupdate.setTimestamp(6, getDateExpected());
			if (getStatus().equals("Active"))
			{
				setDateResolved(null);
			}
			if (getStatus().equals("Closed"))
			{
				if (getDateResolved()==null)
				{
					setDateResolved(JUtility.getSQLDateTime());
				}
			}		
			stmtupdate.setTimestamp(7, getDateResolved());
			stmtupdate.setString(8, getStatus());
			stmtupdate.setString(9, getComments());
			stmtupdate.setString(10, getResource());
			stmtupdate.setString(11, getAuthorisor());
			stmtupdate.setString(12, getMHNNumber());

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
