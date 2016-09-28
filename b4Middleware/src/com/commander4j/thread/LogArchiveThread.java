package com.commander4j.thread;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JArchive;

public class LogArchiveThread extends Thread
{
	public boolean allDone = false;
	Integer counter = 0;

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LogArchiveThread.class));

	public LogArchiveThread()
	{
		super();

	}

	public void run()
	{
		while (true)
		{
			try
			{
				sleep(1000);
			} catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			if (counter >= 60)
			{
				logger.debug("Checking for log files to archive.");
				JArchive.archiveBackupFiles(Common.logDir, Common.ArchiveRetentionDays);
				counter = 0;
			} else
			{
				counter++;
			}

			if (allDone)
			{
				logger.debug("LogArchiveThread closed.");
				return;
			}

		}
	}
}
