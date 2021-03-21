package ABSTRACT.com.commander4j.Interface;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import INTERFACE.com.commander4j.Connector.OutboundConnectorINTERFACE;
import INTERFACE.com.commander4j.Interface.OutboundInterfaceINTERFACE;

import com.commander4j.Connector.Outbound.OutboundConnectorASCII;
import com.commander4j.Connector.Outbound.OutboundConnectorCSV;
import com.commander4j.Connector.Outbound.OutboundConnectorEmail;
import com.commander4j.Connector.Outbound.OutboundConnectorIDOC;
import com.commander4j.Connector.Outbound.OutboundConnectorMQTT;
import com.commander4j.Connector.Outbound.OutboundConnectorPDF_PRINT;
import com.commander4j.Connector.Outbound.OutboundConnectorRAW;
import com.commander4j.Connector.Outbound.OutboundConnectorSOCKET;
import com.commander4j.Connector.Outbound.OutboundConnectorXML;
import com.commander4j.Interface.Mapping.Map;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public abstract class OutboundInterfaceABSTRACT extends TimerTask implements OutboundInterfaceINTERFACE
{
	boolean enabled = false;
	private String type = "";
	public OutboundConnectorABSTRACT connector;
	private Long timerFrequency = (long) 2000;
	private boolean running = false;
	Timer timer;
	protected Map map;
	protected Document data;
	private String outputPath = "";
	private String xsltFilename = "";
	private String xsltPath = "";
	private String description;
	private String id;
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((OutboundInterfaceABSTRACT.class));
	private String outputPattern = "";
	private String csvOptions = "";
	private String delimeter = "";
	private String outputFilePrefix = "";
	private String outputFileExtension = "";
	private String emailSubject = "";
	private String emailMessage = "";
	private String emailListID = "";
	private String compareParam1 = "";
	private String compareParam1_Type = "";
	private String compareParam2 = "";
	private String compareParam2_Type = "";
	private String comparitor = "";
	private String Use8_3_GUID_Filename = "N";
	private String queueName = "";
	private String mqttTopic="";
	private String mqttContentXPath="";
	private String mqttClient="";
	private String mqttBroker="";
	private int mqttQos=2;
	protected Utility util = new Utility();
	private String hostIP = "127.0.0.1";
	private int hostPort = 9100;
	private int hostRepeat = 1;
	
	public String getHostIP()
	{
		return hostIP;
	}

	public void setHostIP(String hostIP)
	{
		this.hostIP = hostIP;
	}

	public int getHostPort()
	{
		return hostPort;
	}

	public void setHostPort(String port)
	{
		port = util.replaceNullStringwithBlank(port);
		if (port.equals("")) port = "9100";	
		this.hostPort = Integer.valueOf(port);
	}

	public int getHostRepeat()
	{
		return hostRepeat;
	}

	public void setHostRepeat(String repeat)
	{
		repeat = util.replaceNullStringwithBlank(repeat);
		if (repeat.equals("")) repeat = "1";	
		this.hostRepeat = Integer.valueOf(repeat);
	}
	
	public String getMapId()
	{
		return this.map.getId();
	}
	
	public void setMQTTTopic(String q)
	{
		this.mqttTopic = util.replaceNullStringwithBlank(q);
	}
	
	public void setMQTTContentXML(String q)
	{
		this.mqttContentXPath = util.replaceNullStringwithBlank(q);
	}
	
	public void setMQTTClient(String q)
	{
		this.mqttClient = util.replaceNullStringwithBlank(q);
	}

	public void setMQTTBroker(String q)
	{
		this.mqttBroker = util.replaceNullStringwithBlank(q);
	}
	
	public void setMQTTQos(String q)
	{
		try {
			this.mqttQos = Integer.valueOf(util.replaceNullStringwithBlank(q));
		} catch (Exception ex){
			this.mqttQos = 0;
		}

	}
	
	public String getMQTTTopic()
	{
		return mqttTopic;
	}
	
	public String getMQTTContentXPath()
	{
		if (mqttContentXPath.equals(""))
		{
			this.mqttContentXPath = "/mqtt/content";
		}
		return mqttContentXPath;
	}
	
	public String getMQTBroker()
	{
		return mqttBroker;
	}
	
	public int getMQTTQos()
	{
		return this.mqttQos;
	}
	
	public String getMQTTClient()
	{
		return mqttClient;
	}
	
	public void setQueueName(String q)
	{
		this.queueName = util.replaceNullStringwithBlank(q);
	}

	public String getQueueName()
	{
		return util.replaceNullStringwithBlank(this.queueName);
	}
	
	public String get83GUIDFilename(String prefix,String originalFilename)
	{
		String result = originalFilename;
		
		prefix = util.replaceNullStringwithBlank(prefix);
		
		int prefixLength = prefix.length();
		
		if (is83GUIDFilenameReqd())
		{
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			uuid = uuid.substring(0, 8-prefixLength);
			
			result = prefix+uuid;
			
			logger.debug("Filename changed from ["+originalFilename+"] to ["+result+"]");
		}
		else
		{
			result = prefix+originalFilename;
		}
		
		return result;
	}

	public void set83GUIDFilenameReqd(boolean yesNo)
	{
		if (yesNo)
		{
			this.Use8_3_GUID_Filename = "Y";
		}
		else
		{
			this.Use8_3_GUID_Filename = "N";
		}
	}

	public void set83GUIDFilenameReqd(String yesNo)
	{
		this.Use8_3_GUID_Filename = util.replaceNullStringwithBlank(yesNo).toUpperCase();

		if (this.Use8_3_GUID_Filename.equals(""))
		{
			this.Use8_3_GUID_Filename = "N";
		}
	}

	public String get83GUIDFilenameReqd()
	{
		return this.Use8_3_GUID_Filename;
	}

	public boolean is83GUIDFilenameReqd()
	{
		if (Use8_3_GUID_Filename.equals("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setCompareParam1(String p1)
	{
		this.compareParam1 = p1;
	}

	public void setCompareParam1_Type(String typ1)
	{
		this.compareParam1_Type = typ1;
	}

	public void setCompareParam2(String p2)
	{
		this.compareParam2 = p2;
	}

	public void setCompareParam2_Type(String typ2)
	{
		this.compareParam2_Type = typ2;
	}

	public void setComparator(String cmp)
	{
		this.comparitor = cmp;
	}

	public String getCompareParam1()
	{
		return util.replaceNullStringwithBlank(this.compareParam1);
	}

	public String getCompareParam1_Type()
	{
		return util.replaceNullStringwithBlank(this.compareParam1_Type);
	}

	public String getCompareParam2()
	{
		return util.replaceNullStringwithBlank(this.compareParam2);
	}

	public String getCompareParam2_Type()
	{
		return util.replaceNullStringwithBlank(this.compareParam2_Type);
	}

	public String getComparator()
	{
		return util.replaceNullStringwithBlank(this.comparitor);
	}

	public void setPrefix(String prefix)
	{
		this.outputFilePrefix = prefix;
	}

	public String getPrefix()
	{
		return util.replaceNullStringwithBlank(this.outputFilePrefix);
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return this.description;
	}

	public String getXSLTFilename()
	{
		return xsltFilename;
	}

	public void setXSLTFilename(String xsltFilename)
	{
		this.xsltFilename = xsltFilename;
	}

	public OutboundInterfaceABSTRACT(Map map)
	{
		this.map = map;
	}

	public Document getData()
	{
		return this.data;
	}

	public boolean getEnabled()
	{
		return this.enabled;
	}

	public void setOutputPath(String path)

	{
		this.outputPath = path;
	}

	public void setOutputFileExtension(String extension)
	{
		if (util.replaceNullStringwithBlank(extension).equals("") == false)
		{
			this.outputFileExtension = extension;
		}
	}

	public String getOutputFileExtension()
	{
		return this.outputFileExtension;
	}

	public String getOutputPath()
	{
		if (outputPath.equals(""))
			outputPath = System.getProperty("user.dir") + File.separator + "interface" + File.separator + "output";

		return this.outputPath;
	}

	public void setEnabled(boolean enable)
	{
		logger.debug("setEnabled " + String.valueOf(enable));
		if ((enable == true) && (enabled == false) && (running == false))
		{
			// start
			timer = new Timer("Output_"+getMapId()+"_"+getId());
			connector.setEnabled(enabled);
			this.enabled = enable;
			setRunning(true);
			logger.debug("Start Requested : [" + getDescription() + "]");
			timer.schedule(this, 0, timerFrequency);
		}
		else
		{
			// stop
			timer.cancel();
			logger.debug("Stop Requested : [" + getDescription() + "]");
			setRunning(false);
		}
	}

	private void setRunning(boolean yes)
	{
		this.running = yes;
	}

	public boolean isRunning()
	{
		return this.running;
	}

	public String getType()
	{
		return type;
	}

	public String getOutputPattern()
	{
		return outputPattern;
	}

	public void setOutputPattern(String pattern)
	{
		outputPattern = util.replaceNullStringwithBlank(pattern);

	}

	public String getCSVOptions()
	{
		return csvOptions;
	}

	public void setCSVOptions(String options)
	{
		csvOptions = util.replaceNullStringwithBlank(options);

	}

	public String getOptionDelimeter()
	{
		return delimeter;
	}

	public void setOptionDelimeter(String value)
	{
		delimeter = util.replaceNullStringwithBlank(value);

	}

	public void setType(String type)
	{
		this.type = type;

		switch (type)
		{
		case OutboundConnectorINTERFACE.Connector_PDF_PRINT:
			connector = new OutboundConnectorPDF_PRINT((OutboundInterface) this);
			setOutputFileExtension("pdf");
			break;
		case OutboundConnectorINTERFACE.Connector_ASCII:
			connector = new OutboundConnectorASCII((OutboundInterface) this);
			setOutputFileExtension("txt");
			break;
		case OutboundConnectorINTERFACE.Connector_CSV:
			connector = new OutboundConnectorCSV((OutboundInterface) this);
			setOutputFileExtension("csv");
			break;
		case OutboundConnectorINTERFACE.Connector_EMAIL:
			connector = new OutboundConnectorEmail((OutboundInterface) this);
			setOutputFileExtension("");
			break;

		case OutboundConnectorINTERFACE.Connector_IDOC:
			connector = new OutboundConnectorIDOC((OutboundInterface) this);
			setOutputFileExtension("idoc");
			break;
		case OutboundConnectorINTERFACE.Connector_XML:
			connector = new OutboundConnectorXML((OutboundInterface) this);
			setOutputFileExtension("xml");
			break;
		case OutboundConnectorINTERFACE.Connector_MQTT:
			connector = new OutboundConnectorMQTT((OutboundInterface) this);
			setOutputFileExtension("xml");
			break;
		case OutboundConnectorINTERFACE.Connector_SOCKET:
			connector = new OutboundConnectorSOCKET((OutboundInterface) this);
			setOutputFileExtension("xml");
			break;
		case OutboundConnectorINTERFACE.Connector_RAW:
			connector = new OutboundConnectorRAW((OutboundInterface) this);
			setOutputFileExtension("");
			break;
		default:
			throw new IllegalArgumentException();
		}

	}

	public void setEmailSubject(String subject)
	{
		this.emailSubject = subject;
	}

	public void setEmailMessage(String message)
	{
		this.emailMessage = message;
	}

	public void setEmailListID(String address)
	{
		this.emailListID = address;
	}

	public String getEmailSubject()
	{
		return this.emailSubject;
	}

	public String getEmailMessage()
	{
		return this.emailMessage;
	}

	public String getEmailListID()
	{
		return this.emailListID;
	}

	public String getXSLTPath()
	{
		if (xsltPath.equals(""))
			xsltPath = System.getProperty("user.dir") + File.separator + "xslt" + File.separator;
		return xsltPath;
	}

	public void setXSLTPath(String xsltPath)
	{
		this.xsltPath = xsltPath;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

}
