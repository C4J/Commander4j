package com.commander4j.sys;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.Interface.Mapping.Map;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

public class MiddlewareConfig
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((MiddlewareConfig.class));
	LinkedList<Map> maps = new LinkedList<Map>();
	LinkedList<String> directoryErrors = new LinkedList<String>();
	JFileIO fio = new JFileIO();

	public String getInterfaceStatistics()
	{
		String result = "\n\n" + "Interface Statistics" + "\n" + "********************" + "\n\n";

		for (int x = 0; x < getMaps().size(); x++)
		{
			result = result + "Map : [" + getMaps().get(x).getId() + "] Description [" + Utility.padString(getMaps().get(x).getDescription(), true, 60, " ") + "] Inbound Map Count [" + Utility.padString(getMaps().get(x).getInboundMapMessageCount().toString(), false, 5, " ")
					+ "] Outbound Map Count [" + Utility.padString(getMaps().get(x).getOutboundMapMessageCount().toString(), false, 5, " ") + "]" + "\n";
		}

		return result;
	}
	
	public void resetInterfaceStatistics()
	{
		for (int x = 0; x < getMaps().size(); x++)
		{
			getMaps().get(x).resetInboundMapMessageCount();
			getMaps().get(x).resetOutboundMapMessageCount();
		}
	}

	public int getMapDirectoryErrorCount()
	{
		return directoryErrors.size();
	}

	public LinkedList<String> getMapDirectoryErrors()
	{
		return directoryErrors;
	}

	public MiddlewareConfig()
	{
		Common.logDir = System.getProperty("user.dir") + java.io.File.separator + "interface" + java.io.File.separator + "log";

	}

	public LinkedList<Map> getMaps()
	{
		return maps;
	}

	public void startMaps()
	{
		logger.debug("startMaps");
		for (int x = 0; x < maps.size(); x++)
		{
			maps.get(x).setEnabled(true);
		}
	}

	public void stopMaps()
	{
		logger.debug("stopMaps");
		for (int x = 0; x < maps.size(); x++)
		{
			maps.get(x).setEnabled(false);
		}
	}

	
	
	public LinkedList<Map> loadMaps(String filename)
	{
		String configName = "";
		String XSLTPath = "";
		String LogPath = "";
		String ArchiveRetentionDays = "";
		String statusReportTime = "00:00:00";

		int mapSeq = 1;

		maps.clear();

		JXMLDocument doc = new JXMLDocument(filename);

		configName = doc.findXPath("//config/@description");
		XSLTPath = doc.findXPath("//config/XSLTPath");
		LogPath = doc.findXPath("//config/logPath");
		Common.emailEnabled = Boolean.valueOf(doc.findXPath("//config/enableEmailNotifications").toLowerCase());
		
		ArchiveRetentionDays = doc.findXPath("//config/logArchiveRetentionDays");
		statusReportTime = doc.findXPath("//config/statusReportTime");

		logger.debug("Config Name :" + configName);
		
		if (ArchiveRetentionDays.equals(""))
		{
			ArchiveRetentionDays="7";
		}
		
		Common.ArchiveRetentionDays = Integer.valueOf(ArchiveRetentionDays);
		Common.logDir = LogPath;
		Common.configName = configName;
		
		if (statusReportTime.equals(""))
		{
			statusReportTime="09:00:00";
		}
		
		Common.statusReportTime = statusReportTime;

		if (Common.logDir.equals(""))
		{
			Common.logDir = System.getProperty("user.dir") + java.io.File.separator + "interface" + java.io.File.separator + "log";
		}

		logger.debug("Log Path :" + Common.logDir);

		while (doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/@enabed").trim() != "")
		{
			String mapEnabled = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/@enabed").trim();

			if (mapEnabled.equals("Y"))
			{

				Map map = new Map();

				String mapId = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/@id").trim();
				String mapDescription = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/@description").trim();

				map.setId(mapId);
				map.setDescription(mapDescription);

				logger.debug("Loading map              : (" + mapId + ") " + mapDescription);

				int inputSeq = 1;

				String inputId = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@id").trim();
				String inputDescription = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@description").trim();
				String inputType = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/type").trim();
				String inputPath = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/path").trim();
				String inputMask = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/mask").trim();
				String inputPrefix = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/prefix").trim();
				String inputPattern = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputPattern").trim();
				String inputIdocSchemaFilename = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/idocSchemaFilename").trim();
				String pollingInterval = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/pollingInterval").trim();
				String inputXSLT = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/XSLT").trim();
				String csvOptionsIn = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/csvOptions").trim();
				String optionDelimeterIn = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/optionDelimeter").trim();

				InboundInterface inboundInterface = new InboundInterface(map, inputDescription);
				inboundInterface.setId(inputId);
				inboundInterface.setDescription(inputDescription);

				if (fio.isValidDirectory(inputPath) == false)
				{
					directoryErrors.addLast("Map : [" + mapId + "] inputId : [" + inputId + "] Invalid Directory : " + inputPath);
				}

				inboundInterface.setInputPath(inputPath);
				inboundInterface.setXSLTPath(XSLTPath);
				inboundInterface.setXSLTFilename(inputXSLT);
				inboundInterface.setType(inputType);
				inboundInterface.setPrefix(inputPrefix);
				
				if (inputMask.equals("")==false)
				{
					String[] maskArray = inputMask.split(",");
					inboundInterface.setInputFileMask(maskArray);
				}
				
				inboundInterface.setInputPattern(inputPattern);
				inboundInterface.setIdocSchemaFilename(inputIdocSchemaFilename);
				inboundInterface.setPollingInterval(Long.valueOf(pollingInterval));
				inboundInterface.setCSVOptions(csvOptionsIn);
				inboundInterface.setOptionDelimeter(optionDelimeterIn);


				logger.debug("Loading input connector  : (" + inputId + ") " + inputDescription +" Type " +inboundInterface.getType()+" Mask "+Arrays.toString(inboundInterface.getInputFileMask()));

				map.setInboundInterface(inboundInterface);

				int outputSeq = 1;

				while (doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@enabed").trim() != "")
				{
					String outputId = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@id").trim();
					String outputDescription = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@description").trim();

					String outputType = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/type").trim();
					String outputPath = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/path").trim();
					String outputXSLT = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/XSLT").trim();
					String outputPattern = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputPattern").trim();
					String optionDelimeterOut = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/optionDelimeter").trim();
					String csvOptionsOut = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/csvOptions").trim();
					String outputFileExtension = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(inputSeq) + "]/outputFileExtension").trim();
					String outputPrefix = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(inputSeq) + "]/prefix").trim();
					

					String outputParam1_Type = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/condition/param1/@type").trim();
					String outputParam1 = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/condition/param1").trim();
					String outputParam2_Type = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/condition/param2/@type").trim();
					String outputParam2 = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/condition/param2").trim();
					String outputComparitor = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/condition/comparitor").trim();

					String emailSubject = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/subject").trim();
					String emailMessage = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/message").trim();
					String emailListID = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/emailListID").trim();
					String use83GUID = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/use83GUID").trim();

					OutboundInterface outboundInterface = new OutboundInterface(map, outputDescription);
					outboundInterface.setId(outputId);
					outboundInterface.setDescription(outputDescription);
					outboundInterface.setType(outputType);
					outboundInterface.setOutputPath(outputPath);
					outboundInterface.setXSLTPath(XSLTPath);
					outboundInterface.setXSLTFilename(outputXSLT);
					outboundInterface.setOutputPattern(outputPattern);
					outboundInterface.setCSVOptions(csvOptionsOut);
					outboundInterface.setOptionDelimeter(optionDelimeterOut);
					outboundInterface.setOutputFileExtension(outputFileExtension);
					outboundInterface.setEmailSubject(emailSubject);
					outboundInterface.setEmailMessage(emailMessage);
					outboundInterface.setEmailListID(emailListID);
					outboundInterface.set83GUIDFilenameReqd(use83GUID);

					outboundInterface.setPrefix(outputPrefix);
					
					outboundInterface.setCompareParam1(outputParam1);
					outboundInterface.setCompareParam1_Type(outputParam1_Type);
					outboundInterface.setCompareParam2(outputParam2);
					outboundInterface.setCompareParam2_Type(outputParam2_Type);
					outboundInterface.setComparator(outputComparitor);


					if (fio.isValidDirectory(outputPath) == false)
					{
						directoryErrors.addLast("Map : [" + mapId + "] outputId : [" + outputId + "] Invalid Directory : " + outputPath);
					}

					logger.debug("Loading output connector : (" + outputId + ") " + outputDescription);

					
					map.addOutboundInterface(outboundInterface);

					outputSeq++;
				}

				maps.add(map);
			}

			mapSeq++;

		}

		Collections.sort(maps);
		
		return maps;
	}
}
