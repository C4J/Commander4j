package com.commander4j.db;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBSchema.java
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

import java.awt.Color;
import java.awt.Rectangle;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.commander4j.app.JVersion;
import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JSplashScreenUtils;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLSchema;


public class JDBSchema {

	private JHost hst = new JHost();
	private String dbErrorMessage;
	private String sessionID;

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	public JDBSchema(String session, JHost host)
	{
		setSessionID(session);
		setHost(host);
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
		}
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	public String getSchemaName()
	{
		String result = "";
		result = com.commander4j.util.JUtility.replaceNullStringwithBlank(hst.getDatabaseParameters().getjdbcDatabaseSchema());
		if (result.equals("") == false)
		{
			result = result + ".";
		}
		return result;
	}

	public JDBUpdateRequest validate(boolean showWarnings)
	{
		JDBUpdateRequest result = new JDBUpdateRequest();

		JDBTable table = new JDBTable(hst.getSiteNumber(), getSessionID(), hst.getDatabaseParameters().getjdbcDatabaseSchema() + "SYS_CONTROL");
		if (table.isValidTable() == false)
		{
			hst.setSchemaVersion(-1);
			result.schema_CURVersion = -1;
			result.schema_NEWVersion = JVersion.getSchemaVersion();
			result.schema_updateRequired = true;

			result.program_NEWVersion = new Double("-1");
			result.program_CURVersion = JVersion.getProgramVersionValue();
			result.program_updateRequired = true;

			if (showWarnings)
			{
				JUtility.errorBeep();
				JSplashScreenUtils.hide();
				JOptionPane.showMessageDialog(null, "One or more missing application tables, please run Commander4j Setup", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		} else
		{
			JDBControl ctrl = new JDBControl(hst.getSiteNumber(), getSessionID());
			if (ctrl.getProperties("SCHEMA VERSION") == false)
			{
				ctrl.create("SCHEMA VERSION", "0", "Database Schema Version");
			}

			hst.setSchemaVersion(Integer.valueOf(ctrl.getKeyValue()));

			int newVersion = JXMLSchema.getSchemaVersion();

			if (hst.getSchemaVersion() < newVersion)
			{
				result.schema_CURVersion = Integer.valueOf(ctrl.getKeyValue());
				result.schema_NEWVersion = JVersion.getSchemaVersion();
				result.schema_updateRequired = true;
				if (showWarnings)
				{
					JUtility.errorBeep();

					JSplashScreenUtils.hide();

					int answer = JOptionPane.showConfirmDialog(
							null,
							"Database Schema is out of date.\nPlease use Commander4j Setup program to upgrade.\nDetected version [" + String.valueOf(Common.hostList.getHost(hst.getSiteNumber()).getSchemaVersion()) + "], Expected version ["
									+ String.valueOf(newVersion) + "]\nContinue with application logon ?", "Database Schema", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

					if (answer == JOptionPane.NO_OPTION)
					{
						System.exit(0);
					} else
					{
						JSplashScreenUtils.show();
					}
				}
			}

			if (ctrl.getProperties("PROGRAM VERSION") == false)
			{
				ctrl.create("PROGRAM VERSION", JVersion.getProgramVersion(), "Application Version");
			}

			result.program_CURVersion = Double.valueOf(ctrl.getKeyValue("PROGRAM VERSION"));
			result.program_NEWVersion = Double.valueOf(JVersion.getProgramVersion());

			if (result.program_CURVersion < result.program_NEWVersion)
			{
				result.program_updateRequired = true;
				if (result.program_NEWVersion > result.program_CURVersion)
				{

					JSplashScreenUtils.hide();

					if (showWarnings)
					{
						int answer = JOptionPane.showConfirmDialog(null,
								"Your database is configured to expect program version " + String.valueOf(result.program_CURVersion) + 
								"\nYou appear to be running version " + String.valueOf(result.program_NEWVersion)
								+ " of the application.\n\nPlease run the Setup4j Configuration to update your database.\nContinue with application logon ?", 
								"Program Version", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
						if (answer == JOptionPane.NO_OPTION)
						{
							System.exit(0);
						} else
						{
							JSplashScreenUtils.show();
						}
					}
				}

			}

			else
			{
				result.program_updateRequired = false;
				if (result.program_NEWVersion < result.program_CURVersion)
				{
					JSplashScreenUtils.hide();

					if (showWarnings)
					{
						int answer = JOptionPane.showConfirmDialog(null, "You appear to be running version "+JVersion.getProgramVersion()+"\nPlease install Commander4j " + String.valueOf(result.program_CURVersion) 
								 + "\n\nContinue with application logon ?", "Program Version", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
						if (answer == JOptionPane.NO_OPTION)
						{
							System.exit(0);
						} else
						{
							JSplashScreenUtils.show();
						}
					}

				}
				
			}
		}

		return result;
	}

	public boolean executeDDL(LinkedList<JDBDDL> ddl, JProgressBar progress, JLabel commandLabel)
	{
		Statement stmtupdate;
		boolean result = true;
		int sze = ddl.size();
		String command = "";

		if (sze > 0)
		{

			progress.setMinimum(0);
			progress.setMaximum(sze - 1);
			progress.setBackground(Color.WHITE);
			progress.setForeground(Color.BLUE);
			progress.setStringPainted(true);
			try
			{
				stmtupdate = hst.getConnection(getSessionID()).createStatement();

				for (int l = 0; l < sze; l++)
				{
					try
					{
						command = (ddl.get(l).getText().trim());
						progress.setValue(l);
						commandLabel.setText(command.toLowerCase());
						Rectangle progressRect = progress.getBounds();
						progressRect.x = 0;
						progressRect.y = 0;
						progress.paintImmediately(progressRect);
						progressRect = commandLabel.getBounds();
						progressRect.x = 0;
						progressRect.y = 0;
						commandLabel.paintImmediately(progressRect);
						try
						{
							Thread.sleep(10);
						} catch (InterruptedException ie)
						{
						}

						if (command.equalsIgnoreCase("null") == false)
						{
							stmtupdate.executeUpdate(ddl.get(l).getText());
							hst.getConnection(getSessionID()).commit();
						}
						ddl.get(l).setError("Success");
					} catch (Exception ex)
					{
						result = false;
						String err = ex.getMessage();
						ddl.get(l).setError(err);
						ddl.get(l).setErrorCount(ddl.get(l).getErrorCount() + 1);
					}
				}

			} catch (Exception ex)
			{
				setErrorMessage(ex.getMessage());
			}
		}
		commandLabel.setText("");
		return result;
	}

	public void setHost(JHost host)
	{
		hst = host;
	}

}
