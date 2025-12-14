package com.commander4j.spool;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class Test
{

	public static void main(String[] args)
	{
		
		try
		{
			FileUtils.copyDirectory(new File("./spool/archive"), new File("./spool/"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initLogging("./xml/log/log4j2.xml");
		
		PrintManager printManager = new PrintManager();
		
		for (int x=1;x<=5;x++)
		{
			PrintJob a = new PrintJob("127.0.0.1",9100,String.valueOf(x)+".zpl");
			printManager.submitJob(a);
		}
		
		for (int x=6;x<10;x++)
		{
			PrintJob a = new PrintJob("10.0.0.160",9100,String.valueOf(x)+".zpl");
			printManager.submitJob(a);
		}
		
		try
		{
			Thread.sleep(30000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printManager.abort();
		
	}
	
	public static void initLogging(String filename)
	{
		if (filename.isEmpty())
		{
			filename = "xml" + File.separator + "log" + File.separator + "log4j2.xml";
		}

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);

		// this will force a reconfiguration
		context.setConfigLocation(file.toURI());

	}

}
