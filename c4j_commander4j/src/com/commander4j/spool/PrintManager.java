package com.commander4j.spool;

import java.io.File;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JArchive;

public class PrintManager
{
	public static String spoolFolder = "."+File.separator+"spool";
	public static String archiveFolder= "."+File.separator+"spool"+File.separator+"archive";
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PrintManager.class);
	
	HashMap<String, Printer> printManager = new HashMap<String, Printer>();

	public boolean submitJob(PrintJob job)
	{
		boolean result = false;

		String id = "Device " + job.ip + ":" + String.valueOf(job.port);

		if (printManager.containsKey(id) == false)
		{
			logger.debug("Starting Printer Thread for ip "+job.ip+" port "+job.port);
			Printer printer = new Printer(job.ip, job.port);
			printer.start();

			printManager.put(printer.getName(), printer);
		}

		if (printManager.containsKey(id) == true)
		{

			if (((Thread) printManager.get(id)).isAlive())
			{
				printManager.get(id).submitJob(job);
				result = true;
			}
			else
			{
				logger.debug("Thread "+id+" not running.");
			}

		}
		else
		{
			logger.debug("printManager does not contain "+id);
		}
		
		archive();
		
		return result;
	}
	
	public int activePrintJobs()
	{
		int result = 0;
		
		for (Printer printThread : printManager.values())
		{
			if (printThread.isAlive())
			{
				result = result + printThread.getJobCount();
				
			}
		}
		
		return result;
	}

	public void abort()
	{
		for (Printer printThread : printManager.values())
		{
			if (printThread.isAlive())
			{
				printThread.abort();
				
			}
		}
	}
	
	public void archive()
	{
		JArchive.archiveSpoolFiles(PrintManager.spoolFolder, 1);
		JArchive.archiveSpoolFiles(PrintManager.archiveFolder, 1);
	}
}
