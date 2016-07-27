package com.commander4j.sys;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.Interface.Mapping.Map;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JXMLDocument;

public class Config {

	Logger logger = Logger.getLogger(Config.class);
	LinkedList<Map> maps = new LinkedList<Map>();
	LinkedList<String> directoryErrors = new LinkedList<String>();
	JFileIO fio = new JFileIO();
	
	public int getMapDirectoryErrorCount()
	{
		return directoryErrors.size();
	}

	public LinkedList<String> getMapDirectoryErrors()
	{
		return directoryErrors;
	}
	
	public Config()
	{
		Common.logDir = System.getProperty("user.dir")+java.io.File.separator+"xml"+java.io.File.separator+"interface"+java.io.File.separator+"log";
		
	}
	
	public LinkedList<Map> getMaps()
	{
		return maps;
	}
	
	public void startMaps()
	{
		logger.debug("startMaps");
		for (int x=0;x<maps.size();x++)
		{
			maps.get(x).setEnabled(true);
		}
	}
	
	public void stopMaps()
	{
		logger.debug("stopMaps");
		for (int x=0;x<maps.size();x++)
		{
			maps.get(x).setEnabled(false);
		}
	}

	public LinkedList<Map> loadMaps(String filename)
	{

		String configName = "";
		//String backupPath = "";
		String XSLTPath = "";
		Logger logger = Logger.getLogger(Config.class);

		int mapSeq = 1;

		maps.clear();

		JXMLDocument doc = new JXMLDocument(filename);

		configName = doc.findXPath("//config/@description");
		//backupPath = doc.findXPath("//config/backupPath");
		XSLTPath = doc.findXPath("//config/XSLTPath");

		logger.debug("Loading config :" + configName);

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
				String pollingInterval = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/pollingInterval").trim();
				String inputXSLT = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/XSLT").trim();
				String backupPath = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/input[" + String.valueOf(inputSeq) + "]/backupPath").trim();

				InboundInterface inboundInterface = new InboundInterface(map, inputDescription);
				inboundInterface.setId(inputId);
				inboundInterface.setDescription(inputDescription);
				
				if (fio.isValidDirectory(inputPath)==false)
				{
					directoryErrors.addLast("Map : ["+mapId+ "] inputId : ["+inputId+ "] Invalid Directory : "+inputPath);
				}
				
				inboundInterface.setInputPath(inputPath);
				inboundInterface.setBackupPath(backupPath);
				inboundInterface.setXSLTPath(XSLTPath);
				inboundInterface.setXSLTFilename(inputXSLT);
				inboundInterface.setInputFileMask(inputMask);
				inboundInterface.setType(inputType);
				inboundInterface.setPollingInterval(Long.valueOf(pollingInterval));

				logger.debug("Loading input connector  : (" + inputId + ") " + inputDescription);

				map.setInboundInterface(inboundInterface);

				int outputSeq = 1;

				while (doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@enabed").trim() != "")
				{
					String outputId = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@id").trim();
					String outputDescription = doc.findXPath("//config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@description").trim();

					String outputType = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/type").trim();
					String outputPath = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/path").trim();
					String outputXSLT = doc.findXPath("/config/map[" + String.valueOf(mapSeq) + "]/output[" + String.valueOf(outputSeq) + "]/XSLT").trim();

					OutboundInterface outboundInterface = new OutboundInterface(map, outputDescription);
					outboundInterface.setId(outputId);
					outboundInterface.setDescription(outputDescription);
					outboundInterface.setType(outputType);
					outboundInterface.setOutputPath(outputPath);
					outboundInterface.setXSLTPath(XSLTPath);
					outboundInterface.setXSLTFilename(outputXSLT);
					
					if (fio.isValidDirectory(outputPath)==false)
					{
						directoryErrors.addLast("Map : ["+mapId+ "] outputId : ["+outputId+ "] Invalid Directory : "+outputPath);
					}

					logger.debug("Loading output connector : (" + outputId + ") " + outputDescription);

					map.addOutboundInterface(outboundInterface);

					outputSeq++;
				}

				maps.add(map);
			}

			mapSeq++;

		}

		return maps;
	}
}
