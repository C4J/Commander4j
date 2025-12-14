package com.commander4j.print;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.print.PrintService;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBPrinters;
import com.commander4j.sys.Common;
import com.commander4j.util.JPrint;

public class JPrintDevices
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JPrintDevices.class);
	private String dbErrorMessage;
	private String hostID;
	private String sessionID;
//	private JDBWorkstationPreferences workstationPrefs;
//	private String workstation;
//	private JPrintDeviceRenderer renderer = new JPrintDeviceRenderer();
//	private DefaultComboBoxModel<JPrintDevice> defComboBoxDevs = new DefaultComboBoxModel<JPrintDevice>();
//	private LinkedList<JPrintDevice> printDeviceList;
//	private ComboBoxModel<JPrintDevice> jList1Model2 = defComboBoxDevs;

	public JPrintDevices(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

//		workstation = JUtility.getClientName();
//		workstationPrefs = new JDBWorkstationPreferences(host, session);
	}

	/*
	 * public void populatePrinterList(JComboBox4j<JPrintDevice>
	 * comboBoxPrintQueue, String moduleid) { String printerId = ""; String
	 * deviceType = "";
	 * 
	 * int sel = -1;
	 * 
	 * if (workstationPrefs.getProperties(workstation, moduleid)) { deviceType =
	 * workstationPrefs.getDeviceType(); printerId =
	 * workstationPrefs.getPrinterId(); } else { deviceType = "queue"; printerId
	 * = JPrint.getDefaultPrinterQueueName(); }
	 * 
	 * printDeviceList = getDevices(moduleid);
	 * 
	 * for (int j = 0; j < printDeviceList.size(); j++) { if
	 * (deviceType.equals("printer")) { if (printDeviceList.get(j).getPrinter()
	 * != null) { if
	 * (printDeviceList.get(j).getPrinter().getPrinterID().equals(printerId)) {
	 * sel = j; } } } if (deviceType.equals("queue")) { if
	 * (printDeviceList.get(j).getQueue() != null) { if
	 * (printDeviceList.get(j).getQueue().getName().equals(printerId)) { sel =
	 * j; } }
	 * 
	 * }
	 * 
	 * defComboBoxDevs.addElement(printDeviceList.get(j)); }
	 * 
	 * comboBoxPrintQueue.setRenderer(renderer);
	 * 
	 * comboBoxPrintQueue.setModel(jList1Model2);
	 * 
	 * if ((sel == -1) && (defComboBoxDevs.getSize()>0)) { sel = 0; };
	 * 
	 * comboBoxPrintQueue.setSelectedIndex(sel);
	 * 
	 * comboBoxPrintQueue.addActionListener(new ActionListener() { public void
	 * actionPerformed(ActionEvent e) { JPrintDevice selected = (JPrintDevice)
	 * comboBoxPrintQueue.getSelectedItem();
	 * 
	 * if (selected.getType().equals("printer")) {
	 * workstationPrefs.save(workstation, moduleid, selected.getType(),
	 * selected.getPrinter().getPrinterID()); } else {
	 * workstationPrefs.save(workstation, moduleid, selected.getType(),
	 * selected.toString()); } } });
	 * 
	 * }
	 */

	public LinkedList<JPrintDevice> getDevices(String moduleid)
	{
		LinkedList<JPrintDevice> list_queues = new LinkedList<JPrintDevice>();
		LinkedList<JPrintDevice> list_printers = new LinkedList<JPrintDevice>();
		
		LinkedList<JPrintDevice> result = new LinkedList<JPrintDevice>();

		logger.debug("Get print queues");

		LinkedList<PrintService> queues = JPrint.getPrinterServices();

		for (int x = 0; x < queues.size(); x++)
		{
			JPrintDevice dev = new JPrintDevice();
			dev.setType("queue");
			dev.setQueue(queues.get(x));
			list_queues.add(dev);
		}
		
		list_queues.sort(null);

		logger.debug("Get printers");

		LinkedList<JDBPrinters> printers = getLabellers(moduleid);

		for (int x = 0; x < printers.size(); x++)
		{
			JPrintDevice dev = new JPrintDevice();
			dev.setType("printer");
			dev.setPrinter(printers.get(x));
			list_printers.add(dev);
		}
		
		list_printers.sort(null);
		
		result.addAll(list_queues);
		result.addAll(list_printers);

		return result;
	}

	public LinkedList<JDBPrinters> getLabellers(String moduleid)
	{
		LinkedList<JDBPrinters> typeList = new LinkedList<JDBPrinters>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBPrinters.getPrintersForModule"));
			stmt.setString(1, moduleid);
			stmt.setFetchSize(100);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBPrinters mt = new JDBPrinters(getHostID(), getSessionID());
				mt.getPropertiesfromResultSet(rs);
				typeList.add(mt);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return typeList;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	private void setErrorMessage(String errorMsg)
	{
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}
}
