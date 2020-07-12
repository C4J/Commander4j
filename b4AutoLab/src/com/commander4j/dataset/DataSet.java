package com.commander4j.dataset;

import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;

import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUtility;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class DataSet extends Thread
{

	private ConcurrentHashMap<String, String> dataMap = new ConcurrentHashMap<String, String>();
	private boolean isDataReady = false;
	private JUtility utility = new JUtility();
	private String productionLine = "'";
	private String printerName = "";
	private String inputPath = "";
	private String inputFilename = "";
	private boolean run = true;
	private String localFilename = "";
	private String remoteFilename = "";
	private String uuid = "";
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((DataSet.class));

	public DataSet(String uuid, String productionLine, String printerName, String inputPath)
	{
		this.uuid = uuid;
		this.productionLine = productionLine;
		this.printerName = printerName;
		this.inputPath = inputPath;
		this.inputFilename = productionLine + "_" + this.printerName + ".CSV";
		this.localFilename = System.getProperty("user.dir") + File.separator + "interface" + File.separator + "input" + File.separator + "dataset" + File.separator + getInputFilename();
		this.remoteFilename = getInputPath() + File.separator + getInputFilename();
		
		setName("DataSet " + productionLine + "_" + printerName);
		
		logger.debug("[" + getUuid() + "] {" + getName() + "} Instance Created.");
	}

	public void shutdown()
	{
		this.run = false;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getRemoteFilename()
	{
		return remoteFilename;
	}

	public void setRemoteFilename(String remoteFilename)
	{
		this.remoteFilename = remoteFilename;
	}

	public String getLocalFilename()
	{
		return localFilename;
	}

	public void setLocalFilename(String localFilename)
	{
		this.localFilename = localFilename;
	}

	public String getProductionLine()
	{
		return productionLine;
	}

	public void setProductionLine(String productionLine)
	{
		this.productionLine = productionLine;
	}

	public String getPrinterName()
	{
		return printerName;
	}

	public void setPrinterName(String printerName)
	{
		this.printerName = printerName;
	}

	public String getInputPath()
	{
		return inputPath;
	}

	public void setInputPath(String inputPath)
	{
		this.inputPath = inputPath;
	}

	public String getInputFilename()
	{
		return inputFilename;
	}

	public void setInputFilename(String inputFilename)
	{
		this.inputFilename = inputFilename;
	}

	public void run()
	{

		boolean defaultLoaded = false;
		run = true;
		setDataReady(false);

		logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Started...");

		File src = new File(getRemoteFilename());
		File dst = new File(getLocalFilename());

		while (run == true)
		{
			if (src.exists())
			{
				logger.debug("[" + getUuid() + "] {" + getName() + "} " + " Found Remote DataSet "+src.getAbsolutePath());
				utility.copyFile(getName(), src, dst);

				loadCSV("Remote", getRemoteFilename());

				defaultLoaded = true;

				FileUtils.deleteQuietly(src);
			}
			else
			{
				if (defaultLoaded == false)
				{
					// Start by loading the
					if (dst.exists())
					{
						logger.debug("[" + getUuid() + "] {" + getName() + "} " + " Found Local DataSet "+dst.getAbsolutePath());
						loadCSV("Local", getLocalFilename());

						defaultLoaded = true;
					}
					else
					{
						logger.debug("[" + getUuid() + "] {" + getName() + "} " + "*ERROR* No DataSet Loaded");
					}
				}
			}

			try
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				run = false;
			}

		}

		src = null;
		dst = null;

		logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Thread Stopped...");
	}

	public boolean loadCSV(String locationType, String inputFilename)
	{
		boolean result = false;
		setDataReady(false);

		CSVParser csvParser;
		CSVReader csvReader;
		char seperator = ',';
		char quote = '"';
		String key = "";
		String value = "";

		try
		{
			dataMap.clear();

			csvParser = new CSVParserBuilder().withSeparator(seperator).withIgnoreQuotations(false).withQuoteChar(quote).build();
			csvReader = new CSVReaderBuilder(new FileReader(inputFilename)).withCSVParser(csvParser).build();

			List<String[]> inputData = csvReader.readAll();

			int size = inputData.get(0).length;

			for (int x = 0; x < size; x++)
			{
				key = inputData.get(0)[x];
				key = AutoLab.config.getDBFieldnameFromCSVFieldname(key);
				
				value = inputData.get(1)[x];

				dataMap.put(key, value);

				if (key.equals(AutoLab.config.getPalletCriteriaField()))
				{
					if (value.contains(AutoLab.config.getPalletCriteria()) || (AutoLab.config.getPalletCriteria().equals("")))
					{
						dataMap.put("LABELS_PER_PRINT", AutoLab.config.getPalletPerPrint());
						dataMap.put("LABELS_PER_PALLET", AutoLab.config.getPalletTotal());
					}
				}
				
				if (key.equals(AutoLab.config.getSemiPalletCriteriaField()))
				{
					if (value.contains(AutoLab.config.getSemiPalletCriteria()) || (AutoLab.config.getSemiPalletCriteria().equals("")))
					{
						dataMap.put("LABELS_PER_PRINT", AutoLab.config.getSemiPalletPerPrint());
						dataMap.put("LABELS_PER_PALLET", AutoLab.config.getSemiPalletTotal());
					}
				}
				
				if (key.equals("CUSTOMER_ID"))
				{
					if (AutoLab.config.getCustomerBatchFormat(value).equals(""))
					{
						//If no customer specific batch format then use system key default.
						dataMap.put("BATCH_FORMAT", AutoLab.config.getSystemKey("BATCH FORMAT"));
					}
					else
					{
						dataMap.put("BATCH_FORMAT", AutoLab.config.getCustomerBatchFormat(value));
					}
				}
				
				if (AutoLab.config.getCloneFieldName(key).equals("")==false)
				{
					dataMap.put(AutoLab.config.getCloneFieldName(key), value);
				}
				
			}

			csvReader = null;
			csvParser = null;
			inputData = null;

			logger.debug("[" + getUuid() + "] {" + getName() + "} " + "Loaded from " + locationType + " \"" + new File(inputFilename).getName() + "\"");

			setDataReady(true);
			result = true;
		}
		catch (Exception ex)
		{
			setDataReady(false);
			result = false;
			logger.debug("[" + getUuid() + "] {" + getName() + "} " + "loadCSV Error :" + ex.getLocalizedMessage());
		}

		return result;
	}
	
	public Enumeration<String> getDataKeys()
	{
		return dataMap.keys();
	}

	
	public void setData(String key,String value)
	{

		dataMap.put(key, value);
	}
	
	public String getData(String key)
	{
		String result = "";

		if (isDataReady())
		{
			if (dataMap.get(key) == null)
			{
				result = "*data key [" + key + "] not found*";
			}
			else
			{
				result = utility.replaceNullStringwithBlank(dataMap.get(key));
			}
		}
		else
		{
			result = "*data not ready*";
		}

		return result;
	}

	public boolean isDataReady()
	{
		return isDataReady;
	}

	public void setDataReady(boolean isDataReady)
	{
		this.isDataReady = isDataReady;
	}

}
