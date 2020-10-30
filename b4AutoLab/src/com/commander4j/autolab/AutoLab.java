package com.commander4j.autolab;

import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.logging.log4j.Logger;

import com.commander4j.common.Common;
import com.commander4j.config.Config;
import com.commander4j.config.ProdLineDefinition;
import com.commander4j.email.EmailQueue;
import com.commander4j.email.EmailThread;
import com.commander4j.labelsync.LabelSync;
import com.commander4j.notifier.JFrameNotifier;
import com.commander4j.notifier.TrayIconProdLineStatus;
import com.commander4j.notifier.TrayIconSystemInfo;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.resources.JRes;
import com.commander4j.sscc.SSCC_Sequence;
import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;

public class AutoLab extends Thread
{

	public static HashMap<String, Thread> threadList_ProdLine = new HashMap<String, Thread>();
	public static HashMap<String, Thread> threadList_SSCC = new HashMap<String, Thread>();
	public static HashMap<String, TrayIconProdLineStatus> iconList_SystemTray = new HashMap<String, TrayIconProdLineStatus>();
	public static JWait wait = new JWait();
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((AutoLab.class));
	public static Config config;
	public static HashMap<String, ProdLine> list_ProdLines = new HashMap<String, ProdLine>();
	public static HashMap<String, ProdLineDefinition> lines = new HashMap<String, ProdLineDefinition>();
	public static LabelSync sync;
	public static boolean run = true;
	public static EmailQueue emailqueue = new EmailQueue();
	public static String version = "1.46";
	private JUtility utils = new JUtility();
	public static EmailThread emailthread;
	private TrayIconSystemInfo trayIconSystem = new TrayIconSystemInfo();
	public static JFrameNotifier systemNotify;
	public static JRes rb = new JRes();

	@Override
	public void run()
	{

		StartAutoLab();

		while (run)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				run = false;
				// break;
			}
		}

		StopAutoLab();

		logger.debug("Application Terminated.");
	}

	public void requestStop()
	{
		run = false;
	}

	public boolean StartAutoLab()
	{
		boolean result = true;

		systemNotify = new JFrameNotifier("", JRes.getText("system_log"), JRes.getText("starting"));


		systemNotify.appendToMessage("AutoLab4j "+version);
		systemNotify.appendToMessage(JRes.getText("starting_background_process")+ " ["+JRes.getText("email")+"]");
		emailthread = new EmailThread();
		emailthread.start();

		emailqueue.addToQueue("StartStop", "AutoLab " + version + " started on " + utils.getClientName(), "AutoLab service version " + version + " started.", "");
		logger.debug("Version " + version);

		Locale.setDefault(new Locale(Common.locale_language, Common.locale_region));

		systemNotify.appendToMessage(JRes.getText("loading_config"));
		// *Get Configuration* //

		config = new Config();

		lines.putAll(config.getConfigProdLines());

		trayIconSystem.init();

		// Start one thread per production line.

		Set<Entry<String, ProdLineDefinition>> entrySet0 = lines.entrySet();
		Iterator<Entry<String, ProdLineDefinition>> it0 = entrySet0.iterator();
		while (it0.hasNext())
		{
			Map.Entry<String, ProdLineDefinition> me0 = (Map.Entry<String, ProdLineDefinition>) it0.next();

			ProdLineDefinition prodlinedef0 = me0.getValue();
			ProdLine prodLine0 = new ProdLine(prodlinedef0.getProdLine_Name(), prodlinedef0.getModbus_Name(), prodlinedef0.getModbus_IPAddress(), Integer.valueOf(prodlinedef0.getModbus_Port()), Integer.valueOf(prodlinedef0.getModbus_Coil_Address()), Integer.valueOf(prodlinedef0.getModbus_Timeout()),
					Boolean.valueOf(prodlinedef0.getModbus_Coil_Trigger_Value()), prodlinedef0.getPrinter_Name(), config.getDataSetPath(), prodlinedef0.getSscc_Filename());
			threadList_ProdLine.put(prodLine0.getUuid(), prodLine0);

			logger.debug("[" + prodLine0.getUuid() + "] Adding Icon to System Tray");
			TrayIconProdLineStatus trayIconStatus = new TrayIconProdLineStatus();
			trayIconStatus.init(prodLine0.getUuid());
			iconList_SystemTray.put(prodLine0.getUuid(), trayIconStatus);
			systemNotify.appendToMessage(JRes.getText("starting_background_process")+ " ["+JRes.getText("production_line")+" "+prodlinedef0.getProdLine_Name()+"]");
			threadList_ProdLine.get(prodLine0.getUuid()).start();
		}

		// Start the label sync thread.

		systemNotify.appendToMessage(JRes.getText("starting_background_process")+ " ["+JRes.getText("labelsync")+"]");
		sync = new LabelSync("LabelSync", config.getLabelSyncPath(), config.getLabelSyncFrequency());
		sync.start();

		return result;
	}

	public void StopAutoLab()
	{

		// Stop the Production Line Threads

		Set<Entry<String, Thread>> entrySet2 = threadList_ProdLine.entrySet();
		Iterator<Entry<String, Thread>> it2 = entrySet2.iterator();
		while (it2.hasNext())
		{

			Map.Entry<String, Thread> me2 = (Map.Entry<String, Thread>) it2.next();

			logger.debug("StopAutoLab " + ((ProdLine) me2.getValue()).getName());

			systemNotify.appendToMessage(JRes.getText("stopping_background_process") + " [" + ((ProdLine) me2.getValue()).getProdLineName() + "]");

			String uuid = ((ProdLine) me2.getValue()).getUuid();

			int retries = 5;

			while ((((ProdLine) me2.getValue()).isAlive()) && (retries > 0))
			{

				((ProdLine) me2.getValue()).shutdown();

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					retries = 0;
				}
				retries--;

			}

			if (((ProdLine) me2.getValue()).isAlive())
			{
				((ProdLine) me2.getValue()).interrupt();
			}

			logger.debug("[" + uuid + "] Removing Icon from System Tray");
			SystemTray.getSystemTray().remove(iconList_SystemTray.get(uuid).getTrayIcon());
			iconList_SystemTray.remove(uuid);

		}

		systemNotify.appendToMessage(JRes.getText("stopping_background_process")+" [LabelSync]");
		sync.shutdown();

		emailqueue.addToQueue("StartStop", "AutoLab " + version + " stopped on " + utils.getClientName(), "AutoLab service version " + version + " stopped.", "");

		while (emailqueue.getQueueSize() > 0)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}

		systemNotify.appendToMessage(JRes.getText("stopping_background_process")+" ["+JRes.getText("email")+"]");

		emailthread.shutdown();

		while (emailthread.isAlive())
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{

			}
		}

		SystemTray.getSystemTray().remove(trayIconSystem.getTrayIcon());

		systemNotify.setVisible(false);
		systemNotify.dispose();
		systemNotify = null;

	}

	public static synchronized void minimiseAll()
	{
		Set<Entry<String, Thread>> entrySet2 = threadList_ProdLine.entrySet();
		Iterator<Entry<String, Thread>> it2 = entrySet2.iterator();
		while (it2.hasNext())
		{

			Map.Entry<String, Thread> me2 = (Map.Entry<String, Thread>) it2.next();

			((ProdLine) me2.getValue()).prodLineNotify.setExtendedState(JFrame.ICONIFIED);
			
			((ProdLine) me2.getValue()).prodLinePreview.setExtendedState(JFrame.ICONIFIED);

		}
		
		systemNotify.setExtendedState(JFrame.ICONIFIED);
	}
	
	public static synchronized void saveWindowLayouts()
	{
		Set<Entry<String, Thread>> entrySet2 = threadList_ProdLine.entrySet();
		Iterator<Entry<String, Thread>> it2 = entrySet2.iterator();
		while (it2.hasNext())
		{

			Map.Entry<String, Thread> me2 = (Map.Entry<String, Thread>) it2.next();

			JUtility.saveWindowLayout(((ProdLine) me2.getValue()).prodLineNotify);
			
			JUtility.saveWindowLayout(((ProdLine) me2.getValue()).prodLinePreview);

		}
		
		JUtility.saveWindowLayout(systemNotify);

	}
	
	public static synchronized void restoreAll()
	{
		Set<Entry<String, Thread>> entrySet2 = threadList_ProdLine.entrySet();
		Iterator<Entry<String, Thread>> it2 = entrySet2.iterator();
		
		systemNotify.setExtendedState(JFrame.NORMAL);
		
		while (it2.hasNext())
		{

			Map.Entry<String, Thread> me2 = (Map.Entry<String, Thread>) it2.next();

			((ProdLine) me2.getValue()).prodLineNotify.setExtendedState(JFrame.NORMAL);
			
			((ProdLine) me2.getValue()).prodLinePreview.setExtendedState(JFrame.NORMAL);

		}

	}

	// These methods permit inter thread communication using the common uuid

	public static synchronized TrayIconProdLineStatus updateTrayIconStatus(String uuid)
	{
		TrayIconProdLineStatus result = ((TrayIconProdLineStatus) iconList_SystemTray.get(uuid));
		return result;

	}

	public static synchronized void removeTrayIconStatus(String uuid)
	{
		iconList_SystemTray.remove(uuid);

	}

	public static synchronized boolean isDataSet_DataReady(String uuid)
	{
		boolean result = false;
		result = ((ProdLine) threadList_ProdLine.get(uuid)).dataset1.isDataReady();
		return result;

	}

	public static synchronized String getDataSet_Field(String uuid, String fieldname)
	{
		String result = "";
		JUtility utils = new JUtility();
		result = ((ProdLine) threadList_ProdLine.get(uuid)).dataset1.getData(fieldname);

		if (config.getFormat(fieldname).equals("") == false)
		{
			result = utils.formatNumber(result, (config.getFormat(fieldname)));
		}

		return result;

	}

	public static synchronized String getDataSetPath(String uuid)
	{
		String result = ((ProdLine) threadList_ProdLine.get(uuid)).getRemoteDataSetPath();
		return result;
	}

	public static synchronized String getLabelSource()
	{
		String result = AutoLab.sync.getSource();
		return result;
	}

	public static synchronized String getModBus_IP(String uuid)
	{
		String result = ((ProdLine) threadList_ProdLine.get(uuid)).getModbusIPAddress();
		return result;
	}

	public static synchronized String getModBus_Port(String uuid)
	{
		int result = ((ProdLine) threadList_ProdLine.get(uuid)).getModbusPortNumber();
		return String.valueOf(result);
	}

	public static synchronized String getModBus_CoilID(String uuid)
	{
		int result = ((ProdLine) threadList_ProdLine.get(uuid)).getModbusCoil();
		return String.valueOf(result);
	}

	public static synchronized String getProdLine_Name(String uuid)
	{

		String result = ((ProdLine) threadList_ProdLine.get(uuid)).getProdLineName();
		return result;
	}

	public static synchronized void setDataSet_FieldValue(String uuid, String fieldname, String value)
	{

		((ProdLine) threadList_ProdLine.get(uuid)).dataset1.setData(fieldname, value);

	}

	public static synchronized ArrayList<String> getDataSet_FieldNames(String uuid)
	{
		Enumeration<String> result = ((ProdLine) threadList_ProdLine.get(uuid)).dataset1.getDataKeys();
		ArrayList<String> keys = Collections.list(result);

		return keys;

	}

	public static synchronized void setPrintProperties(String uuid, String ipaddress, int port)
	{
		((ProdLine) threadList_ProdLine.get(uuid)).print1.setIpAddress(ipaddress);
		((ProdLine) threadList_ProdLine.get(uuid)).print1.setPortNumber(port);
	}

	public static synchronized void request_Print(String uuid)
	{
		((ProdLine) threadList_ProdLine.get(uuid)).print1.setDataReady(true);
	}

	public static synchronized void set_PrintData(String uuid, String data)
	{
		((ProdLine) threadList_ProdLine.get(uuid)).print1.setData(data);
	}
	
	public static synchronized void set_PreviewData(String uuid, String data)
	{
		if (AutoLab.config.isLabelaryEnabled() == true)
		{
			((ProdLine) threadList_ProdLine.get(uuid)).prodLinePreview.setData(data);
		}
	}

	public static synchronized boolean isDataReady(String uuid)
	{
		return ((ProdLine) threadList_ProdLine.get(uuid)).print1.isDataReady();
	}

	public static synchronized void start_SSCC_Thread(String uuid,String filename)
	{
		if (threadList_SSCC.containsKey(filename) == false)
		{
			SSCC_Sequence ssccSeq = new SSCC_Sequence(uuid,filename);
			threadList_SSCC.put(filename, ssccSeq);
			threadList_SSCC.get(filename).start();
		}
	}

	public static synchronized void stop_SSCC_Thread(String filename)
	{
		if (threadList_SSCC.containsKey(filename) == true)
		{
			try
			{
				int retries = 0;
				while (((SSCC_Sequence) threadList_SSCC.get(filename)).isAlive() && (retries < 10))
				{
					((SSCC_Sequence) threadList_SSCC.get(filename)).shutdown();
					wait.milliSec(250);
				}
				if (((SSCC_Sequence) threadList_SSCC.get(filename)).isAlive())
				{
					((SSCC_Sequence) threadList_SSCC.get(filename)).interrupt();
				}
			}
			catch (Exception ex1)
			{
				logger.debug("[SSCC " + filename + "] Exception : " + ex1.getLocalizedMessage());
			}

			((SSCC_Sequence) threadList_SSCC.get(filename)).shutdown();
		}
	}

	public static synchronized String get_SSCC_Sequence(String filename)
	{
		String result = "";
		if (threadList_SSCC.containsKey(filename))
		{

			result = ((SSCC_Sequence) threadList_SSCC.get(filename)).readSSCC();
		}
		return result;
	}

}
