package com.commander4j.thread;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JWait;
import com.commander4j.util.Utility;

public class StatusThread extends Thread
{
	public boolean allDone = false;
	String currentDateTime = "";
	String currentTime = "";
	String currentDate = "";
	String lastRunDate = "";
	int mb = 1024 * 1024;
	Runtime runtime = Runtime.getRuntime();
	
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((StatusThread.class));

	public StatusThread()
	{
		super();

	}

	public void run()
	{
		logger.debug("StatusThread started.");
		while (true)
		{

			JWait.oneSec();
			currentDateTime = Utility.getDateTimeString("yyyy-MM-dd HH:mm:ss");
			currentDate = currentDateTime.substring(0, 10);
			currentTime = currentDateTime.substring(11, 19);

			if (currentTime.equals(Common.statusReportTime))
			{
				if (currentDate.equals(lastRunDate) == false)
				{
					lastRunDate = currentDate;
					
					String report="Garbage Collector Started.\n\n";

					report = report+"Before GC\n\n";
					report = report+"  Used Memory  :" + (runtime.totalMemory() - runtime.freeMemory()) / mb + "mb\n";
					report = report+"  Free Memory  :" + runtime.freeMemory() / mb + "mb\n";
					report = report+"  Total Memory :" + runtime.totalMemory() / mb + "mb\n";
					report = report+"  Max Memory   :" + runtime.maxMemory() / mb + "mb\n\n";
					System.gc();
					report = report+"After GC\n\n";
					report = report+"  Used Memory  :" + (runtime.totalMemory() - runtime.freeMemory()) / mb + "mb\n";
					report = report+"  Free Memory  :" + runtime.freeMemory() / mb + "mb\n";
					report = report+"  Total Memory :" + runtime.totalMemory() / mb + "mb\n";
					report = report+"  Max Memory   :" + runtime.maxMemory() / mb + "mb\n\n";
					
					report = report+"Garbage Collector Finished.\n\n";
					
					report = report+Common.smw.cfg.getInterfaceStatistics();
					Common.smw.cfg.resetInterfaceStatistics();
					Common.emailqueue.addToQueue("System", "Statistics", report, "");
					logger.debug(report);
				}
			}

			if (allDone)
			{
				logger.debug("StatusThread closed.");
				return;
			}

		}
	}
}
