package com.commander4j.thread;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBReportRequest;
import com.commander4j.db.JDBUser;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JUnique;
import com.commander4j.util.JWait;

public class ReportingThread extends Thread
{

	public boolean allDone = false;
	private String sessionID;
	private String hostID;
	private final Logger logger = Logger.getLogger(ReportingThread.class);

	public String getHostID() {
		return hostID;
	}

	public void setHostID(String host) {
		hostID = host;
	}

	public ReportingThread(String host)
	{
		super();

		setHostID(host);
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public void run() {
		setSessionID(JUnique.getUniqueID());
		JDBUser user = new JDBUser(getHostID(), getSessionID());
		user.setUserId("interface");
		user.setPassword("interface");
		user.setLoginPassword("interface");
		Common.userList.addUser(getSessionID(), user);
		Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);

		Boolean dbconnected = false;

		if (Common.hostList.getHost(hostID).isConnected(sessionID) == false)
		{

			dbconnected = Common.hostList.getHost(hostID).connect(sessionID, hostID);

		}
		else
		{
			dbconnected = true;
		}

		if (dbconnected)
		{
			JDBReportRequest rr = new JDBReportRequest(getHostID(), getSessionID());
			LinkedList<Long> repList = new LinkedList<Long>();

			int noOfReports = 0;

			while (true)
			{

				JWait.milliSec(500);

				if (allDone)
				{
					if (dbconnected)
					{
						Common.hostList.getHost(hostID).disconnect(getSessionID());
					}
					return;
				}

				repList.clear();
				repList = rr.getReportRequestIDs();
				noOfReports = repList.size();

				if (noOfReports > 0)
				{
					for (int x = 0; x < noOfReports; x++)
					{
						JWait.milliSec(100);
						rr.setReportRequestID(repList.get(x));
						rr.getReportRequestProperties();
						String module = rr.getModuleID();

						if (rr.getInvocationMode().equals("ParameterOnly"))
						{
							for (int y = 0; y < rr.getCopies(); y++)
							{
								rr.getParameters();
								HashMap<String, Object> parameters = rr.getParameters();
								parameters.put("p_copy", String.valueOf(y));
								JLaunchReport.silentExceptions = true;
								logger.debug("Starting report : "+module);
								JLaunchReport.runReport(module, parameters, "",null, rr.getPrintQueue());
								logger.debug("Finished report : "+module);
								JLaunchReport.silentExceptions = false;
							}
						}

						rr.delete();
					}
				}
			}
		}
	}
}
