package com.commander4j.labelsync;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUnique;
import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;

public class LabelSync extends Thread
{

	private boolean run = true;
	private String name = "";
	private String source = "";
	private String destination = "";
	private int frequency = 60;
	private int countdown = 0;
	private String uuid = "";
	private JWait wait = new JWait();
	private JUtility utility = new JUtility();
	private JUnique unique = new JUnique();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabelSync.class));

	public LabelSync(String name, String source, int frequency)
	{
		this.source = source;
		this.destination  = System.getProperty("user.dir") + File.separator + "labels";
		this.frequency = frequency;
		this.countdown = frequency;
		this.uuid = unique.getUniqueID();

		setName(name);
		logger.debug("["+getUuid()+"] Instance Created.");
	}

	public void shutdown()
	{
		// logger.debug(getName() + " Thread Shutdown Requested.");

		run = false;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getSyncName()
	{
		return name;
	}

	public void setSyncName(String name)
	{
		this.name = name;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getDestination()
	{
		return destination;
	}

	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	public int getFrequency()
	{
		return frequency;
	}

	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}

	public void run()
	{

		run = true;
		countdown = 0;
		logger.debug("["+getUuid()+"] {"+getName()+"} Thread Started...");

		while (run == true)
		{

			if (countdown == 0)
			{

				logger.debug("["+getUuid()+"] {"+getName()+"} Source Path      = " + source);
				logger.debug("["+getUuid()+"] {"+getName()+"} Destination Path = " + destination);
				String filesCopied = "";

				Iterator<File> files = FileUtils.iterateFiles(new File(source), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
				int filesUpdated = 0;

				while (files.hasNext())
				{

					File sourceFile = files.next();

					String foundFile = sourceFile.getName();
					

					File destinationFile = new File(destination + File.separator + foundFile);

					if (utility.copyNewerFile(getName(),sourceFile, destinationFile))
					{
						filesUpdated++;
						filesCopied = filesCopied + foundFile+"\n";
					}

				}
				
				if (filesUpdated>0)
				{
					AutoLab.emailqueue.addToQueue("Info", utility.getClientName() +" LabelSync","LabelSync ["+getUuid()+"] {"+getName()+"} performed.",filesCopied+"\n\n"+filesUpdated+" file(s) copied.");
				}
				
				logger.debug("["+getUuid()+"] {"+getName()+"} performed. "+filesUpdated+" files updated.");
				countdown = frequency;
			}

			countdown--;

			wait.oneSec();
		}

		logger.debug("["+getUuid()+"] {"+getName()+"} Thread Stopped...");
	}

}
