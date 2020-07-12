package com.commander4j.autolab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.commander4j.common.Common;
import com.commander4j.config.Config;
import com.commander4j.config.ProdLineDefinition;
import com.commander4j.labelsync.LabelSync;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.sscc.SSCC_Sequence;
import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;

public class AutoLab extends Thread
{

	public static HashMap<String, Thread> threadList_ProdLine = new HashMap<String, Thread>();
	public static HashMap<String, Thread> threadList_SSCC = new HashMap<String, Thread>();
	public static JWait wait = new JWait();
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((AutoLab.class));
	public static Config config;
	public static HashMap<String, ProdLine> list_ProdLines = new HashMap<String, ProdLine>();
	public static HashMap<String, ProdLineDefinition> lines = new HashMap<String, ProdLineDefinition>();
	public static LabelSync sync;
	public static boolean run = true;

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
				break;
			}
		}
		
		StopAutoLab();
	}

	public void requestStop()
	{
		run = false;
	}

	public boolean StartAutoLab()
	{
		boolean result = true;

		Locale.setDefault(new Locale(Common.locale_language, Common.locale_region));



		logger.debug("StartAutoLab() 1");

		// *Get Configuration* //

		config = new Config();

		lines.putAll(config.getConfigProdLines());

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
			threadList_ProdLine.get(prodLine0.getUuid()).start();
		}

		// Start the label sync thread.

		sync = new LabelSync("LabelSync", config.getLabelSyncPath(), config.getLabelSyncFrequency());
		sync.start();

		logger.debug("StartAutoLab() 2");

		return result;
	}

	public boolean StopAutoLab()
	{
		boolean result = true;

		// Stop the Production Line Threads

		Set<Entry<String, Thread>> entrySet2 = threadList_ProdLine.entrySet();
		Iterator<Entry<String, Thread>> it2 = entrySet2.iterator();
		while (it2.hasNext())
		{

			Map.Entry<String, Thread> me2 = (Map.Entry<String, Thread>) it2.next();

			logger.debug("StopAutoLab " + ((ProdLine) me2.getValue()).getName());

			int retries = 5;

			while ((((ProdLine) me2.getValue()).isAlive()) && (retries > 0))
			{
				((ProdLine) me2.getValue()).shutdown();
				wait.milliSec(250);
			}

			if (((ProdLine) me2.getValue()).isAlive())
			{
				((ProdLine) me2.getValue()).interrupt();
			}

		}

		sync.shutdown();

		wait.oneSec();

		return result;
	}



	// These methods permit inter thread communication using the common uuid

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

	public static synchronized boolean isDataReady(String uuid)
	{
		return ((ProdLine) threadList_ProdLine.get(uuid)).print1.isDataReady();
	}

	public static synchronized void start_SSCC_Thread(String filename)
	{
		if (threadList_SSCC.containsKey(filename) == false)
		{
			SSCC_Sequence ssccSeq = new SSCC_Sequence(filename);
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
