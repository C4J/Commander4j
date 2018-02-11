package com.commander4j.labeller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import com.commander4j.xml.JXMLDocument;

public class Server extends Thread
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((Server.class));
	LabellerUtility utils = new LabellerUtility();
	public boolean started = false;
	public boolean shutdown = false;
	HashMap<String, Labeller> labellers = new HashMap<String, Labeller>();

	private void requestStop()
	{
		logger.info("Server - requestStop");
		stopAllLabellers();
	}

	public void run()
	{
		logger.info("Server - Run");
		startupInterface();
		started = true;
		while ((shutdown==false) && (started==true))
		{
			utils.pause(100);
		}

	}

	public int stop(int exitCode)
	{
		logger.info("Server - stop");
		try
		{
			requestStop();
			utils.pause(100);
			started = false;

		} catch (Exception ex)
		{
		}

		return exitCode;
	}

	public void addLabeller(LabellerProperties prop, String script)
	{
		logger.info("Server - addLabeller [" + prop.getId() + "]");
		Labeller labeller = new Labeller(prop, script);
		labeller.setName(prop.getId());
		labellers.put(prop.getId(), labeller);
	}

	public void startLabeller(String id)
	{
		logger.info("Server - startLabeller [" + id + "]");
		labellers.get(id).start();
		while (labellers.get(id).isAlive() == false)
		{
			logger.info("Server - waiting for Labeller  [" + id + "] thread to start.");
			utils.pause(10);
		}
		logger.info("Server - Labeller  [" + id + "] thread started.");
	}

	public void startAllLabellers()
	{
		logger.info("Server - startAllLabellers");
		Iterator<Entry<String, Labeller>> it = labellers.entrySet().iterator();
		while (it.hasNext())
		{
			HashMap.Entry<String, Labeller> pair = (HashMap.Entry<String, Labeller>) it.next();
			startLabeller(pair.getKey().toString());
		}
	}

	public void stopLabeller(String id)
	{
		logger.info("Server - stopLabeller - [" + id + "]");
		if (labellers.get(id).isAlive())
		{
			labellers.get(id).shutdown();

			while (labellers.get(id).isAlive())
			{
				utils.pause(10);
			}
		}
	}

	public void stopAllLabellers()
	{
		logger.info("Server - stopAllLabellers");
		Iterator<Entry<String, Labeller>> it = labellers.entrySet().iterator();
		while (it.hasNext())
		{
			HashMap.Entry<String, Labeller> pair = (HashMap.Entry<String, Labeller>) it.next();
			stopLabeller(pair.getKey().toString());

			it.remove();
		}
	}

	public void deleteLabeller(String id)
	{
		if (labellers.get(id).isAlive() == false)
		{
			labellers.remove(id);
		}
	}

	public void startupInterface()
	{
		logger.info("Server - startupInterface");
		loadLabellers();
		startAllLabellers();
	}

	public void requestPrint(String id)
	{
		if (labellers.containsKey(id))
		{
			if (labellers.get(id).isAlive())
			{
				logger.info("Server - requestPrint - ["+id+"]");
				labellers.get(id).requestPrint();
			}
			else
			{
				logger.error("Server - requestPrint - Labeller Thread not running for ["+id+"]");
			}
		}
		else
		{
			logger.error("Server - requestPrint - Labeller Thread not loaded for ["+id+"]");
		}
	}

	public void loadLabellers()
	{
		logger.info("Server - loadLabellers");

		JXMLDocument xmltest = new JXMLDocument("./xml/config/labellers.xml");

		int labeller = 1;

		while (xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/@id").trim().equals("") == false)
		{
			String id = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/@id").trim();
			String enabled = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/enabled").trim();
			String ipAddress = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/ipAddres").trim();
			String portNumber = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/portNumber").trim();
			String commandFile = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/commandFile").trim();
			String inputPath = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/inputPath").trim();
			String inputFile = xmltest.findXPath("//labellers/labeller[" + String.valueOf(labeller) + "]/inputFile").trim();
			System.out.println(id);

			if (enabled.toUpperCase().equals("Y"))
			{

				LabellerProperties prop1 = new LabellerProperties();

				prop1.setId(id);
				prop1.setIpAddress(ipAddress);
				prop1.setPortNumber(Integer.valueOf(portNumber));
				prop1.setInputPath(inputPath);
				prop1.setInputFile(inputFile);
				addLabeller(prop1, commandFile);
			}

			labeller++;
		}

	}

	public static void main(String[] args)
	{
		Logger logger = org.apache.logging.log4j.LogManager.getLogger((Server.class));
		logger.info("Application starting");

		logger.info("Server starting");
		System.out.println("");
		LabellerUtility utils = new LabellerUtility();
		Server server = new Server();

		// server.startupInterface();

		server.start();

		while (server.isAlive() == false)
		{
			utils.pause(10);
		}
		utils.pause(250);

		// consider adding loop here waiting for shutdown ****

		logger.info(server);
		logger.info(server.labellers);
		server.labellers.get("Labeller 1").requestPrint();

		while (server.labellers.get("Labeller 1").requestRunning() == true)
		{
			utils.pause(10);
		}

		server.requestStop();

		System.out.println("");
		logger.info("Server finished.");
		System.exit(0);
	}

}
