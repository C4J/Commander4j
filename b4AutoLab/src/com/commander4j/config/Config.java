package com.commander4j.config;

import java.io.File;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.commander4j.utils.JUtility;
import com.commander4j.xml.JXMLDocument;

public class Config
{
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((Config.class));
	private JXMLDocument xmlDoc;
	private String filename = "";
	private String path = "";
	private String enabled = "";
	private HashMap<String, ProdLineDefinition> config_ProdLines = new HashMap<String, ProdLineDefinition>();
	private HashMap<String,String> batch_Formats = new HashMap<String,String>();
	private HashMap<String,String> fieldNamesLookup = new HashMap<String,String>();
	private HashMap<String,String> systemKeys = new HashMap<String,String>();
	private HashMap<String,String> formats = new HashMap<String,String>();
	private HashMap<String,String> clone = new HashMap<String,String>();
	private String labelSyncPath = "";
	private int labelSyncFrequency=10;
	private String dataSetPath = "";
	private String outputPath = "";
	private boolean suppress_Label_Print = false;
	private boolean suppress_Output_Message = false;
	private boolean emailEnabled = false;
	private String PalletPerPrint = "2";
	private String PalletTotal = "2";
	private String PalletCriteria = "";
	private String PalletCriteriaField = "";
	private String SemiPalletPerPrint = "2";
	private String SemiPalletTotal = "4";
	private String SemiPalletCriteria = "";
	private String SemiPalletCriteriaField = "";
	private JUtility util = new JUtility();
	
	public boolean isEmailEnabled()
	{
		return emailEnabled;
	}

	public void setEmailEnabled(boolean emailEnable)
	{
		this.emailEnabled = emailEnable;
	}

	public HashMap<String, ProdLineDefinition> getConfigProdLines()
	{
		return config_ProdLines;
	}

	public void setConfigProdLines(HashMap<String, ProdLineDefinition> config_ProdLines)
	{
		this.config_ProdLines = config_ProdLines;
	}

	public Config()
	
	{
		logger.debug("");
		setPath(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator);
		setFilename("config.xml");

		xmlDoc = new JXMLDocument(getPath() + getFilename());
		
		setEmailEnabled(Boolean.valueOf(xmlDoc.findXPath("/config/email/@enabled")));
		logger.debug("Config - Email Enabled               [" + isEmailEnabled() + "]");
		
		setSuppressLabelPrint(Boolean.valueOf(xmlDoc.findXPath("/config/debug/@suppressLabelPrint")));
		logger.debug("Config - Suppress Label Print        [" + isSuppressLabelPrint() + "]");
		
		setSuppressOutputMessage(Boolean.valueOf(xmlDoc.findXPath("/config/debug/@suppressOutputMessage")));
		logger.debug("       - Suppress Output XML         [" + isSuppressOutputMessage() + "]");
		
		setLabelSyncPath(util.formatPath(xmlDoc.findXPath("/config/paths/labelSync/@path")));
		logger.debug("       - Label Sync Path             [" + getLabelSyncPath() + "]");	
		
		setLabelSyncFrequency(Integer.valueOf(xmlDoc.findXPath("/config/paths/labelSync/@frequency")));
		logger.debug("       - Label Sync Frequency        [" + getLabelSyncFrequency() + "]");
		
		setDataSetPath(util.formatPath(xmlDoc.findXPath("/config/paths/dataSet/@path")));
		logger.debug("       - DataSet Path                [" + getDataSetPath() + "]");
		
		setOutputPath(util.formatPath(xmlDoc.findXPath("/config/paths/output/@path")));
		logger.debug("       - Output Path                 [" + getOutputPath() + "]");
		
		setPalletPerPrint(xmlDoc.findXPath("/config/labels/pallet/@perPrint"));
		logger.debug("       - PALLET Per Print            [" + getPalletPerPrint() + "]");
		
		setPalletTotal(xmlDoc.findXPath("/config/labels/pallet/@total"));
		logger.debug("       - PALLET Print Total          [" + getPalletTotal() + "]");
		
		setPalletCriteriaField(xmlDoc.findXPath("/config/labels/pallet/@field"));
		logger.debug("       - PALLET Print Criteria Field [" + getPalletCriteriaField() + "]");
		
		setPalletCriteria(xmlDoc.findXPath("/config/labels/pallet/@criteria"));
		logger.debug("       - PALLET Print Criteria       [" + getPalletCriteria() + "]");
		
		setSemiPalletPerPrint(xmlDoc.findXPath("/config/labels/semiPallet/@perPrint"));
		logger.debug("       - SEMI PALLET Per Print       [" + getSemiPalletPerPrint() + "]");
		
		setSemiPalletTotal(xmlDoc.findXPath("/config/labels/semiPallet/@total"));
		logger.debug("       - SEMI PALLET Print Total     [" + getSemiPalletTotal() + "]");
		
		setSemiPalletCriteriaField(xmlDoc.findXPath("/config/labels/semiPallet/@field"));
		logger.debug("       - SEMI PALLET Print Criteria  [" + getSemiPalletCriteriaField() + "]");

		setSemiPalletCriteria(xmlDoc.findXPath("/config/labels/semiPallet/@criteria"));
		logger.debug("       - PALLET Print Criteria Field [" + getSemiPalletCriteria() + "]");
		logger.debug("");

		int seq = 1;

		while (xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/@enabled").equals("") == false)
		{
			enabled = xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/@enabled");

			if (enabled.toUpperCase().equals("Y"))
			{
				ProdLineDefinition prodLine = new ProdLineDefinition();

				prodLine.setProdLine_Name(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/@name"));
				logger.debug("Line   - Production Line      [" + prodLine.getProdLine_Name() + "]");

				prodLine.setModbus_Name(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/@name"));
				logger.debug("         Modbus Name          [" + prodLine.getModbus_Name() + "]");

				prodLine.setModbus_IPAddress(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/ipAddress"));
				logger.debug("         Modbus IP Address    [" + prodLine.getModbus_IPAddress() + "]");

				prodLine.setModbus_Port(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/portNumber"));
				logger.debug("         Modbus Port          [" + prodLine.getModbus_Port() + "]");

				prodLine.setModbus_Coil_Address(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/coilAddress"));
				logger.debug("         Modbus Coil          [" + prodLine.getModbus_Coil_Address() + "]");

				prodLine.setModbus_Timeout(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/timeout"));
				logger.debug("         Modbus Timeout       [" + prodLine.getModbus_Timeout() + "]");

				prodLine.setModbus_Coil_Trigger_Value(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/modbus/coilTriggerValue"));
				logger.debug("         Coil Trigger Value   [" + prodLine.getModbus_Coil_Trigger_Value() + "]");

				prodLine.setPrinter_Name(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/printer//@name"));
				logger.debug("         Printer Name         [" + prodLine.getPrinter_Name() + "]");

				prodLine.setSscc_Filename(xmlDoc.findXPath("/config/productionLines/productionLine[" + String.valueOf(seq) + "]/SSCC//@filename"));
				logger.debug("         SSCC Seq Filename    [" + prodLine.getSscc_Filename() + "]");
				
				config_ProdLines.put(prodLine.getProdLine_Name(), prodLine);
				logger.debug("");
			}

			seq++;
		}
		logger.debug("Config - Loaded " + config_ProdLines.size() + " Production Lines.");
		
		seq = 1;
		String customerID="";
		String batchFormat="";

		logger.debug("Batch Formats");
		while (xmlDoc.findXPath("/config/batchFormats/customer[" + String.valueOf(seq) + "]/@id").equals("") == false)
		{
			customerID = xmlDoc.findXPath("/config/batchFormats/customer[" + String.valueOf(seq) + "]/@id");
			batchFormat = xmlDoc.findXPath("/config/batchFormats/customer[" + String.valueOf(seq) + "]/@pattern");
			
			batch_Formats.put(customerID, batchFormat);
			logger.debug("Customer ID=["+customerID+"]  Batch Format = ["+batchFormat+"]");
			
			seq++;
		}
		logger.debug("");	
		
		seq = 1;
		String csvFieldname="";
		String dbFieldnsame="";

		logger.debug("Fieldname Translation Matrix");

		while (xmlDoc.findXPath("/config/fieldNamesLookup/fieldName[" + String.valueOf(seq) + "]/@csv").equals("") == false)
		{
			csvFieldname = xmlDoc.findXPath("/config/fieldNamesLookup/fieldName[" + String.valueOf(seq) + "]/@csv");
			dbFieldnsame = xmlDoc.findXPath("/config/fieldNamesLookup/fieldName[" + String.valueOf(seq) + "]/@db");
			
			fieldNamesLookup.put(csvFieldname, dbFieldnsame);
			logger.debug("CSV Field=["+csvFieldname+"]  DB Field = ["+dbFieldnsame+"]");
			
			seq++;
		}
		logger.debug("");	

		
		seq = 1;
		String key="";
		String value="";

		logger.debug("System Keys");

		while (xmlDoc.findXPath("/config/controls/system[" + String.valueOf(seq) + "]/@key").equals("") == false)
		{
			key = xmlDoc.findXPath("/config/controls/system[" + String.valueOf(seq) + "]/@key");
			value = xmlDoc.findXPath("/config/controls/system[" + String.valueOf(seq) + "]/@value");
			
			systemKeys.put(key, value);
			logger.debug("System Key=["+key+"]  Value = ["+value+"]");
			
			seq++;
		}
		logger.debug("");
		
		seq = 1;
		String field="";
		String format="";

		logger.debug("Formats");

		while (xmlDoc.findXPath("/config/formats/field[" + String.valueOf(seq) + "]/@name").equals("") == false)
		{
			field = xmlDoc.findXPath("/config/formats/field[" + String.valueOf(seq) + "]/@name");
			format = xmlDoc.findXPath("/config/formats/field[" + String.valueOf(seq) + "]/@format");
			
			formats.put(field, format);
			logger.debug("Field=["+field+"]  Format = ["+format+"]");
			
			seq++;
		}
		logger.debug("");
		
		seq = 1;
		String from="";
		String to="";

		logger.debug("Clone");

		while (xmlDoc.findXPath("/config/clone/field[" + String.valueOf(seq) + "]/@from").equals("") == false)
		{
			from = xmlDoc.findXPath("/config/clone/field[" + String.valueOf(seq) + "]/@from");
			to = xmlDoc.findXPath("/config/clone/field[" + String.valueOf(seq) + "]/@to");
			
			clone.put(from, to);
			logger.debug("From=["+field+"]  To = ["+to+"]");
			
			seq++;
		}
		logger.debug("");

	}
	
	public String getDBFieldnameFromCSVFieldname(String field)
	{
		String result=field;
		
		if (fieldNamesLookup.containsKey(field))
		{
			result = fieldNamesLookup.get(field);		
		}
		
		return result;
	}
	
	public String getCustomerBatchFormat(String customerID)
	{
		String result="";
		
		if (batch_Formats.containsKey(customerID))
		{
			result = batch_Formats.get(customerID);
		}
		
		return result;
	}
	
	public String getSystemKey(String id)
	{
		String result="";
		
		if (systemKeys.containsKey(id))
		{
			result = systemKeys.get(id);
		}

		return result;
	}
	
	public String getCloneFieldName(String field)
	{
		String result = "";
		
		if (clone.containsKey(field))
		{
			result = clone.get(field);
		}
		
		return result;
	}
	
	public String getFormat(String field)
	{
		String result = "";
		
		if (formats.containsKey(field))
		{
			result = formats.get(field);
		}
		
		return result;
	}

	public String getSemiPalletCriteriaField()
	{
		return SemiPalletCriteriaField;
	}

	public void setSemiPalletCriteriaField(String semiPalletCriteriaField)
	{
		SemiPalletCriteriaField = semiPalletCriteriaField;
	}

	public String getPalletCriteriaField()
	{
		return PalletCriteriaField;
	}

	public void setPalletCriteriaField(String palletCriteriaField)
	{
		PalletCriteriaField = palletCriteriaField;
	}

	public String getPalletCriteria()
	{
		return PalletCriteria;
	}

	public void setPalletCriteria(String palletCriteria)
	{
		PalletCriteria = palletCriteria;
	}

	public String getSemiPalletCriteria()
	{
		return SemiPalletCriteria;
	}

	public void setSemiPalletCriteria(String semiPalletCriteria)
	{
		SemiPalletCriteria = semiPalletCriteria;
	}

	public boolean isSuppressLabelPrint()
	{
		return suppress_Label_Print;
	}

	public void setSuppressLabelPrint(boolean suppress_Label_Print)
	{
		this.suppress_Label_Print = suppress_Label_Print;
	}

	public boolean isSuppressOutputMessage()
	{
		return suppress_Output_Message;
	}

	public void setSuppressOutputMessage(boolean suppress_Output_Message)
	{
		this.suppress_Output_Message = suppress_Output_Message;
	}

	public String getDataSetPath()
	{
		return dataSetPath;
	}

	public void setDataSetPath(String dataSetPath)
	{
		this.dataSetPath = dataSetPath;
	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public void setOutputPath(String outPath)
	{
		this.outputPath = outPath;
	}

	public String getLabelSyncPath()
	{
		return labelSyncPath;
	}

	public void setLabelSyncPath(String labelSyncPath)
	{
		this.labelSyncPath = labelSyncPath;
	}

	public int getLabelSyncFrequency()
	{
		return labelSyncFrequency;
	}

	public void setLabelSyncFrequency(int labelSyncFrequency)
	{
		this.labelSyncFrequency = labelSyncFrequency;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPalletPerPrint()
	{
		return PalletPerPrint;
	}

	public void setPalletPerPrint(String palletPerPrint)
	{
		PalletPerPrint = palletPerPrint;
	}

	public String getPalletTotal()
	{
		return PalletTotal;
	}

	public void setPalletTotal(String palletTotal)
	{
		PalletTotal = palletTotal;
	}

	public String getSemiPalletPerPrint()
	{
		return SemiPalletPerPrint;
	}

	public void setSemiPalletPerPrint(String semiPalletPerPrint)
	{
		SemiPalletPerPrint = semiPalletPerPrint;
	}

	public String getSemiPalletTotal()
	{
		return SemiPalletTotal;
	}

	public void setSemiPalletTotal(String semiPalletTotal)
	{
		SemiPalletTotal = semiPalletTotal;
	}



}
