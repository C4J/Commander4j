package com.commander4j.labeller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;
import com.opencsv.CSVReader;

public class Labeller extends Thread
{
	private LabellerProperties prop;
	private Socket socket = new Socket();
	private volatile boolean shutdown = false;
	private LabellerTCPIP_RX rx;
	private LabellerTCPIP_TX tx;
	private boolean connected = false;
	private LabellerUtility utils = new LabellerUtility();
	private LabellerCMDFile labellerCMDFile = new LabellerCMDFile();
	private String commandFile = "";
	private boolean requestPrint = false;

	private File fileWrite;
	private int waitRetries = 20;
	private int waitDelay = 15;
	public volatile ConcurrentLinkedQueue<LabellerFile> directory = new ConcurrentLinkedQueue<LabellerFile>();
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((Labeller.class));
	private HashMap<String, LabellerDBLink> dblinks;

	public void requestPrint()
	{

		requestPrint = true;
	}

	public boolean requestRunning()
	{
		return requestPrint;
	}

	private boolean parseInputData(String fullFilename)
	{
		boolean result = false;
		logger.debug("parseInputData [" + fullFilename + "]");

		try
		{
			CSVReader reader = new CSVReader(new FileReader(fullFilename));
			String[] headerLine;
			String[] dataLine;

			headerLine = reader.readNext();
			dataLine = reader.readNext();

			labellerCMDFile.fileData.clear();

			for (int x = 0; x < headerLine.length; x++)
			{
				labellerCMDFile.fileData.put("$data_" + headerLine[x], dataLine[x]);
			}

			reader.close();

			result = true;

		}
		catch (Exception ex)
		{
			logger.error("parseInputData error :" + ex.getMessage());
		}

		return result;
	}

	@Override
	public void run()
	{
		logger.debug("Labeller run start");

		connected = false;
		shutdown = false;

		while ((shutdown == false))
		{

			/* ADD CODE HERE TO POLL FOLDERS FOR DATA */
			File inputData = new File(prop.getInputPath() + prop.getInputFile());
			requestPrint = inputData.exists();

			/* PARSE DATA */

			if (requestPrint)
			{
				logger.info("Detected input file [" + prop.getInputPath() + prop.getInputFile() + "]");
				parseInputData(prop.getInputPath() + prop.getInputFile());

				if (connected == false)
				{
					logger.info("Connected = false");
					connected = connect();

					if (connected)
					{

						utils.pause(250);

						if (tx.getStatus() != LabellerTCPIP_TX.status_RUNNING)
						{
							logger.info(tx.getStatusName());
							connected = false;
						}

						if (rx.getStatus() != LabellerTCPIP_RX.status_RUNNING)
						{
							logger.info(rx.getStatusName());
							connected = false;
						}
					}

				}
				else
				{
					logger.info("Connected = true");
					if (requestPrint == true)
					{
						if (runScript())
						{
							/* DELETE FILE */
							inputData.delete();
							requestPrint = false;
						}
					}

					if (connected)
					{
						disconnect();
					}
				}
			}
			else
			{
				// System.out.println("Labeller ["+ prop.getId()+"] waiting for
				// request to print");
				utils.pause(250);
			}
		}

		logger.debug("Labeller run end");

	}

	public boolean runScript()
	{

		boolean scriptError = false;

		String LBL = "";
		String CMD = "";
		String VAL = "";
		String DEL = "\"";
		String EOL = "";

		rx.ignoredResponses.clear();
		rx.clearQueue();
		rx.successResponses.clear();
		rx.failedResponses.clear();

		labellerCMDFile.loadFile(prop, commandFile);

		int exePosition = labellerCMDFile.getLinefromLabel("Initialise");

		while ((requestPrint == true) && (connected == true) && (scriptError == false))
		{

			LBL = labellerCMDFile.commandLines.get(exePosition).label;
			CMD = labellerCMDFile.commandLines.get(exePosition).command;
			VAL = labellerCMDFile.getValueAtLine(exePosition).replaceAll("~", "\"");

			logger.debug("runScript :" + JUtility.padString(String.valueOf(exePosition + 1), false, 5, "0") + " - " + JUtility.padString(LBL, true, 18, " ") + JUtility.padString(CMD, true, 30, " ")
					+ JUtility.padString(VAL, true, 50, " "));

			switch (CMD)
			{
			case "DB_QUERY":
				DB_QUERY(VAL);
				break;
			case "MESSAGE_INFO":
				tx.send("*MSG,info," + VAL + "<CR>");
				checkSuccess();
				break;
			case "MESSAGE_ERROR":
				tx.send("*MSG,error," + VAL + "<CR>");
				checkSuccess();
				break;
			case "MESSAGE_WARN":
				tx.send("*MSG,warning," + VAL + "<CR>");
				checkSuccess();
				break;
			case "VARIABLE":
				String variableDefinition = VAL;
				String[] varArray = StringUtils.split(variableDefinition, "=");
				labellerCMDFile.variables.put(varArray[0], labellerCMDFile.removeDelimitors(varArray[1]));
				break;
			case "USER_INPUT":
				String variableDefinition2 = VAL;
				String[] varArray2 = StringUtils.split(labellerCMDFile.removeDelimitors(variableDefinition2), ",");
				Scanner c = new Scanner(System.in);

				String prompt = varArray2[0];
				String defaultValue = varArray2[1];
				String variableName = varArray2[2];
				System.out.print(prompt + " : ");
				String input = c.nextLine();

				if (input.equals(""))
				{
					input = defaultValue;
				}
				labellerCMDFile.variables.put(variableName, input);

				break;
			case "EXIT":
				requestPrint = false;
				utils.pause(100);
				break;
			case "GOTO":
				exePosition = labellerCMDFile.getLinefromLabel(VAL) - 1;
				break;
			case "IF":
				/* "($data_MODULE_ID='RPT_CASE_RU') GOTO label" */
				String[] split1 = StringUtils.splitByWholeSeparator(VAL, "GOTO");
				String criteria = split1[0];
				String[] comparison = StringUtils.split(criteria, "=");
				comparison[0] = StringUtils.replace(comparison[0], "'", "", -1).trim();
				comparison[0] = StringUtils.replace(comparison[0], "(", "", -1).trim();

				comparison[1] = StringUtils.replace(comparison[1], "'", "", -1).trim();
				comparison[1] = StringUtils.replace(comparison[1], ")", "", -1).trim();

				if (comparison[0].equals(comparison[1]))
				{
					String destination = split1[1].trim();
					exePosition = labellerCMDFile.getLinefromLabel(destination) - 1;
				}
				break;
			case "IF REMOTE FILE EXISTS":
				/* 'nestle.pcx' GOTO label" */
				String[] split2 = StringUtils.splitByWholeSeparator(VAL, "GOTO");
				String find = split2[0];
				find = StringUtils.replace(find, "'", "", -1).trim();

				if (IF_EXISTS(find))
				{
					String destination = split2[1].trim();
					exePosition = labellerCMDFile.getLinefromLabel(destination) - 1;
				}
				break;
			case "PAUSE":
				utils.pause(Integer.valueOf(VAL));
				break;
			case "ECHO":
				System.out.println(VAL);
				break;
			case "DELETE_FILE":
				DELETE_FILE(VAL);
				break;
			case "ARCHIVE_FILENAMES1":
				ARCHIVE_FILENAMES1(VAL);
				break;
			case "DIR_REMOTE":
				if (DIR_REMOTE(VAL) == false)
				{
					scriptError = true;
				}
				break;
			case "DIR_REMOTE_FILTER":
				if (DIR_REMOTE_FILTER(VAL) == false)
				{
					scriptError = true;
				}
				break;
			case "STRING_DELIMITER":
				DEL = VAL;
				labellerCMDFile.setDelimiter(DEL);
				break;
			case "SEND_EOL":
				EOL = utils.encodeControlChars(VAL);
				break;
			case "RESPONSE_EOL":
				rx.setResponseEOL(VAL);
				break;
			case "SEND":
				rx.clearQueue();
				tx.send(utils.encodeControlChars(VAL) + EOL);
				utils.pause(100);
				break;
			case "CHECK_SUCCESS":
				// scriptError = checkSuccess();
				break;
			case "IGNORE_RESPONSE":
				rx.ignoredResponses.add(VAL);
				break;
			case "FAIL_RESPONSE":
				rx.failedResponses.add(VAL);
				break;
			case "SUCCESS_RESPONSE":
				rx.successResponses.add(VAL);
				break;
			case "LOCAL_DELETE_FILE":
				String localfilename = System.getProperty("user.dir") + java.io.File.separator + "labeller_files"+java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId() + java.io.File.separator + VAL;
				logger.info("Local Delete  [" + localfilename + "]");
				File fileDelete = new File(localfilename);
				FileUtils.deleteQuietly(fileDelete);
				fileDelete = null;
				break;
			case "FILE_DEFINE":
				String writefilename = System.getProperty("user.dir") + java.io.File.separator + "labeller_files"+java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId() + java.io.File.separator + VAL;
				logger.info("File Defined as  [" + writefilename + "]");
				fileWrite = new File(writefilename);
				if (FileUtils.deleteQuietly(fileWrite))
				{
					logger.info("Existing file deleted  [" + writefilename + "]");
				}
				break;
			case "FILE_WRITE":
				File writePath = new File(System.getProperty("user.dir") + java.io.File.separator + "labeller_files"+java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId());
				try
				{
					FileUtils.forceMkdir(writePath);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					logger.info("Write to file [" + fileWrite.getName() + "] <-- [" + VAL + "]");
					FileUtils.writeStringToFile(fileWrite, utils.encodeControlChars(VAL), "UTF-8", true);
				}
				catch (IOException e)
				{
					logger.error("ERROR [" + e.getMessage() + "]");
					scriptError = true;
				}
				break;
			case "SEND_FILE_INTELHEX":
				if (SEND_FILE_INTELHEX(VAL) == false)
				{
					scriptError = true;
				}
				break;
			case "RECEIVE_FILE_INTELHEX":
				if (RECEIVE_FILE_INTELHEX(VAL, false) == false)
				{
					scriptError = true;
				}
				break;
			case "BACKUP_REMOTE":
				if (BACKUP_REMOTE(VAL) == false)
				{
					scriptError = true;
				}
				break;
			default:
			}

			exePosition++;
		}

		return true;
	}

	public boolean DB_QUERY(String VAL)
	{
		boolean result = false;
		String variableDefinition3 = VAL;
		String[] varArray3 = StringUtils.split(variableDefinition3, ",");
		dblinks.get(varArray3[0]).connect();
		String resultString = dblinks.get(varArray3[0]).queryExecute(varArray3[1], varArray3[2], varArray3[3]);
		labellerCMDFile.variables.put(varArray3[4], resultString);
		logger.info("Query result = [" + varArray3[4] + "]=[" + resultString + "]");
		dblinks.get(varArray3[0]).disconnect();
		return result;
	}

	public boolean IF_EXISTS(String filename)
	{
		boolean result = false;

		if (DIR_REMOTE(filename))
		{

			while ((directory.peek() != null) )
			{
				String foundFilename = directory.poll().getFilename().toLowerCase().trim();
				if (filename.toLowerCase().trim().equals(foundFilename))
				{
					result = true;
					break;
				}
			}

		}

		return result;
	}

	public boolean ARCHIVE_FILENAMES1(String VAL)
	{
		boolean result = false;
		String variableDefinition3 = VAL;
		String[] varArray3 = StringUtils.split(variableDefinition3, ",");

		String filenameMask = varArray3[0];
		String filenameOrderPrefix = varArray3[1];
		String filenameOrderSuffix = varArray3[2];
		if (dblinks.get(varArray3[3]).connect())
		{

			result = DIR_REMOTE(filenameMask);

			while ((directory.peek() != null) && (result == true) && (shutdown == false))
			{
				String deleteFilename = directory.poll().getFilename();

				int startpos;
				if (filenameOrderPrefix.equals(""))
				{
					startpos = 0;
				}
				else
				{
					startpos = deleteFilename.toLowerCase().indexOf(filenameOrderPrefix.toLowerCase());
				}

				if (startpos >= 0)
				{
					int endpos;

					if (filenameOrderSuffix.equals(""))
					{
						endpos = deleteFilename.toLowerCase().indexOf(".", startpos + filenameOrderPrefix.length());
					}
					else
					{
						endpos = deleteFilename.toLowerCase().indexOf(filenameOrderSuffix.toLowerCase(), startpos + filenameOrderPrefix.length());
					}

					if (endpos >= 0)
					{
						String order = deleteFilename.substring(startpos + filenameOrderPrefix.length(), endpos);
						String resultString = dblinks.get(varArray3[3]).queryExecute(varArray3[4], order, varArray3[5]);

						logger.info("[" + prop.getId() + "] - ARCHIVE_FILENAMES1 " + deleteFilename + " Order [" + order + "] Status [" + resultString + "]");

						if (resultString.equals(varArray3[5]))
						{
							logger.info("[" + prop.getId() + "] - DELETE FILE " + deleteFilename);
							DELETE_FILE(deleteFilename);
						}
						else
						{
							logger.info("[" + prop.getId() + "] - IGNORE FILE " + deleteFilename);
						}
					}
				}

			}

			dblinks.get(varArray3[3]).disconnect();
		}
		return result;
	}

	public boolean DELETE_FILE(String VAL)
	{
		boolean result = false;
		logger.info("[" + prop.getId() + "] - DELETE FILE " + VAL);
		tx.send("*DELETE," + VAL + "<CR>");
		checkSuccess();
		result = true;
		return result;
	}

	public boolean BACKUP_REMOTE(String mask)
	{
		boolean result = false;
		result = RECEIVE_FILE_INTELHEX(mask, true);
		return result;
	}

	public boolean SEND_FILE_INTELHEX(String VAL)
	{
		boolean result = false;

		File sendPath = new File(System.getProperty("user.dir") + java.io.File.separator + "labeller_files"+java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId());

		try
		{
			FileUtils.forceMkdir(sendPath);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("");
		logger.info("SEND file [" + VAL + "] Started.");
		rx.clearQueue();
		String sendfilename = System.getProperty("user.dir") + java.io.File.separator + "labeller_files"+java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId() + java.io.File.separator + VAL;
		tx.send("*HEXFILE," + VAL + "<CR>");
		result = checkSuccess();
		if (result == true)
		{
			LabellerIntelHex ihc = new LabellerIntelHex();
			try
			{
				ihc.prepareToConvertToIntelHex(new File(sendfilename), LabellerIntelHex.CRLF_TO_CR);
				String block = ihc.getNextIntelHexBlock();
				while (block != null)
				{
					rx.clearQueue();
					tx.send(block);
					result = checkSuccess();
					block = ihc.getNextIntelHexBlock();
				}
				rx.clearQueue();
				tx.send(ihc.getEOFBlock());
				result = checkSuccess();
				rx.clearQueue();
				logger.info("SEND file [" + VAL + "] Complete.");
				result = true;
			}
			catch (Exception e)
			{
				logger.error("SEND file [" + VAL + "] Error." + e.getMessage());

			}
			System.out.println("");
		}
		return result;
	}

	public boolean RECEIVE_FILE_INTELHEX(String VAL, boolean isBackup)
	{
		boolean result = false;

		String downloadFileMask = VAL;
		result = DIR_REMOTE(downloadFileMask);

		File downloadPath;
		if (isBackup)
		{
			downloadPath = new File(System.getProperty("user.dir") + java.io.File.separator + "labeller_backups"  + java.io.File.separator + prop.getSite()+ java.io.File.separator + prop.getId() + java.io.File.separator
					+ JUtility.getISOTimeStampStringFormat(JUtility.getSQLDateTime()).substring(0, 16).replaceAll(":", "-").replaceAll("T", "-"));
		}
		else
		{
			downloadPath = new File(System.getProperty("user.dir") + java.io.File.separator + "labeller_files" + java.io.File.separator + prop.getSite() + java.io.File.separator + prop.getId());
		}

		try
		{
			FileUtils.forceMkdir(downloadPath);
		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
		}

		System.out.println("");
		while ((directory.peek() != null) && (result == true) && (shutdown == false))
		{
			
			LabellerFile labfile = directory.poll();
			String downloadFilename = labfile.getFilename();	
			Timestamp labfiledatetime = labfile.getDatetime();
					
			System.out.println("");
			logger.info("DOWNLOAD Start " + downloadFilename);

			LinkedList<String> fileData = new LinkedList<String>();
			String temp = "";

			String previousResponseEOL = rx.getResponseEOL();
			rx.setResponseEOL("<CR><LF>");
			rx.clearQueue();

			String outfilename2 = downloadPath + java.io.File.separator + downloadFilename;
			String outfilename1 = outfilename2 + ".hex";

			File outFile1 = new File(outfilename1);
			File outFile2 = new File(outfilename2);

			FileUtils.deleteQuietly(outFile2);

			result = tx.send("*TYPEHEX," + downloadFilename + "<CR>");

			if (result == true)
			{

				while ((waitForReply() == true) && (result == true))
				{
					temp = rx.responseQueue.poll();
					temp = temp.replace("CMD->", "");
					fileData.add(temp);
					result = tx.send("000<ACK>");
					if (temp.equals(":0000000000"))
					{
						break;
					}
				}

				if (result == true)
				{
					try
					{
						FileUtils.writeLines(outFile1, "UTF-8", fileData, utils.encodeControlChars("<CR><LF>"));
						LabellerIntelHex ohc = new LabellerIntelHex();
						ohc.convertFromIntelHex(outFile1, outFile2);
						outFile2.setLastModified(labfiledatetime.getTime());
						Path outPath2 = outFile2.toPath();
						FileTime time = FileTime.fromMillis((labfiledatetime.getTime()));
						Files.setAttribute(outPath2, "creationTime", time);		
						FileUtils.deleteQuietly(outFile1);
					}
					catch (Exception ex)
					{
						result = false;
						logger.error("DOWNLOAD Error " + ex.getMessage());
					}

					logger.info("DOWNLOAD Complete " + downloadFilename);
					System.out.println("");

					// result = checkSuccess();
				}
			}
			rx.setResponseEOL(previousResponseEOL);
		}

		if (result == false)
		{
			logger.error("DOWNLOAD `Failed");

		}

		System.out.println("");
		return result;
	}

	public boolean DIR_REMOTE(String VAL)
	{
		boolean result = false;
		result = DIR_REMOTE_FILTER(VAL);
		return result;
	}

	public boolean DELETE_DIR_FILTER(String VAL)
	{
		boolean result = false;

		result = DIR_REMOTE_FILTER(VAL);

		if (result)
		{

			while ((directory.peek() != null) && (result == true) && (shutdown == false))
			{
				String deleteFilename = directory.poll().getFilename();
				DELETE_FILE(deleteFilename);
			}
		}

		result = true;

		return result;
	}

	public boolean DIR_REMOTE_FILTER(String VAL)
	{
		boolean result = false;

		String dirParams = VAL;
		String[] dirCMDFilter = StringUtils.split(labellerCMDFile.removeDelimitors(dirParams), ",");

		directory.clear();
		rx.clearQueue();
		tx.send("*DIR," + dirCMDFilter[0] + ",SDT<CR>");
		waitForReply();

		utils.pause(250);
		String response = "";
		System.out.println("");
		logger.info("Directory Listing");
		logger.info("[" + prop.getId() + "] -----------------------------------------------------------------------");
		while (rx.responseQueue.size() > 0)
		{
			response = JUtility.replaceNullStringwithBlank(rx.responseQueue.poll());
			if (response.equals("SUCCESS"))
			{
				break;
			}
			if (response.equals("FAILED"))
			{
				break;
			}

			response = response.replace("CMD->", "");
			String[] DTS = response.split(",");
			LabellerFile file = new LabellerFile();
			if (DTS.length == 4)
			{
				file.setFilename(DTS[0]);
				file.setDateTime(DTS[2], DTS[3]);
				file.setSize(Long.valueOf(DTS[1]));

				if (dirCMDFilter.length == 2)
				{
					if (file.getFilename().contains(dirCMDFilter[1]))
					{
						logger.info("[" + prop.getId() + "] - " + file.toString());
						directory.add(file);
					}
					else
					{
						logger.debug("[" + prop.getId() + "] - " + "Filter ignoring :" + file.getFilename());
					}
				}
				else
				{
					directory.add(file);
					logger.info("[" + prop.getId() + "] - " + file.toString());
				}
			}
		}
		logger.info("[" + prop.getId() + "] -----------------------------------------------------------------------");
		logger.info("[" + prop.getId() + "]   " + directory.size() + " file(s).");
		System.out.println("");
		result = true;

		return result;
	}

	public boolean waitForReply()
	{
		boolean responseReceived = false;

		int timeout = waitRetries;

		int qSize = rx.responseQueue.size();

		boolean cont = true;

		while (cont)
		{
			if (qSize == rx.responseQueue.size())
			{
				// No Changes to queue - wait for timeout before assuming it's
				// stabalised.
				utils.pause(waitDelay);
				timeout--;
			}
			else
			{
				// Changes so reset timeout.
				timeout = waitRetries;
				qSize = rx.responseQueue.size();
			}

			if (timeout == 0)
			{
				if (rx.responseQueue.size() > 0)
				{
					logger.debug("**** STABALISED ****");
					cont = false;
				}
				else
				{
					logger.debug("**** TIMEOUT NO RESPONSE ****");
					cont = true;
				}
			}

		}

		if (rx.responseQueue.size() > 0)
		{
			timeout = waitRetries;
			responseReceived = true;
		}

		return responseReceived;
	}

	public boolean checkSuccess()
	{
		boolean success = true;
		int timeout = waitRetries;
		String response = "";
		waitForReply();
		while ((timeout > 0) && (rx.responseQueue.size() > 0))
		{
			response = JUtility.replaceNullStringwithBlank(rx.responseQueue.poll());
			if (response.equals("") == false)
			{
				if (response.equals("SUCCESS"))
				{
					System.out.println("");
					logger.debug("Result [" + response + "]");
					System.out.println("");
					success = true;
					timeout = waitRetries;
					// rx.clearQueue();
					break;
				}
				if (response.equals("FAILED"))
				{
					System.out.println("");
					logger.debug("Result [" + response + "]");
					System.out.println("");
					success = false;
					timeout = waitRetries;
					// rx.clearQueue();
					break;
				}

				timeout--;

				if (timeout == 0)
				{
					System.out.println("");
					System.out.println("");
					success = true;
					// rx.clearQueue();
					break;
				}
			}
		}

		return success;
	}

	public void disconnect()
	{

		while (tx.isAlive())
		{
			tx.shutdown();
		}
		System.out.println("");
		logger.debug("TX Shutdown Reason [" + tx.getStatusName() + "]");
		System.out.println("");

		while (rx.isAlive())
		{
			rx.shutdown();
		}
		System.out.println("");
		logger.debug("RX Shutdown Reason [" + tx.getStatusName() + "]");
		System.out.println("");

		logger.debug("Disconnected.");

		connected = false;
	}

	public boolean connect()
	{
		boolean result = false;

		while ((connected == false) && (shutdown == false))
		{
			try
			{
				socket = new Socket();
				socket.connect(new InetSocketAddress(prop.getIpAddress(), prop.getPortNumber()), prop.getConnectTimeout());
				socket.setKeepAlive(true);

				System.out.println("");
				logger.debug("Connected.");
				System.out.println("");

				rx = new LabellerTCPIP_RX();
				rx.setName(prop.getId());
				rx.setSocketConnection(socket);
				rx.setLabellerProperties(prop);
				rx.start();

				tx = new LabellerTCPIP_TX();
				tx.setName(prop.getId());
				tx.setSocketConnection(socket);
				tx.setLabellerProperties(prop);
				tx.start();

				connected = true;
				result = true;

			}
			catch (IOException e)
			{
				utils.pause(500);
				logger.error("Connect Error : " + e.getMessage());
				result = false;
			}
		}

		return result;
	}

	public void shutdown()
	{
		System.out.println("");
		logger.info("Shutdown requested.");
		System.out.println("");
		shutdown = true;
	}

	public Labeller(LabellerProperties prop, String commandFile, HashMap<String, LabellerDBLink> dblinks)
	{
		this.commandFile = commandFile;
		this.prop = prop;
		this.dblinks = dblinks;
		rx = new LabellerTCPIP_RX();
		rx.setLabellerProperties(prop);
		tx = new LabellerTCPIP_TX();
		tx.setLabellerProperties(prop);

	}

}
