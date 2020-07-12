package com.commander4j.autolab;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.commander4j.utils.JUtility;
import com.commander4j.utils.JWait;

public class StartStop
{

	public static JWait wait = new JWait();
	public static JUtility utils = new JUtility();
	public static AutoLab autolab;
	public static boolean run = true;
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((AutoLab.class));



	public static synchronized void debugToFile(String info)
	{
		File file = new File( System.getProperty("user.dir") + File.separator +"log.txt");
		FileWriter fr;
		try
		{
			fr = new FileWriter(file, true);
			fr.write(utils.getISOTimeStampStringFormat(utils.getSQLDateTime())+" "+info+"\r\n");
			fr.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		File file = new File( System.getProperty("user.dir") + File.separator +"log.txt");
		file.delete();
		
		debugToFile("Main");


		ShutdownHook sample = new ShutdownHook();

		Runtime.getRuntime().addShutdownHook(sample);
		
		wait = new JWait();
		autolab = new AutoLab();

		debugToFile("Starting");

		autolab.start();

		while (autolab.isAlive())
		{
			wait.milliSec(1000);

		}

		debugToFile("Stopped");

	}


}
