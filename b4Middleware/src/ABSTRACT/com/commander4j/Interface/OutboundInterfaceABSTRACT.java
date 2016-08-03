package ABSTRACT.com.commander4j.Interface;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import INTERFACE.com.commander4j.Connector.OutboundConnectorINTERFACE;
import INTERFACE.com.commander4j.Interface.OutboundInterfaceINTERFACE;

import com.commander4j.Connector.Outbound.OutboundConnectorASCII;
import com.commander4j.Connector.Outbound.OutboundConnectorCSV;
import com.commander4j.Connector.Outbound.OutboundConnectorDB;
import com.commander4j.Connector.Outbound.OutboundConnectorIDOC;
import com.commander4j.Connector.Outbound.OutboundConnectorXML;
import com.commander4j.Interface.Mapping.Map;
import com.commander4j.Interface.Outbound.OutboundInterface;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.OutboundConnectorABSTRACT;

public abstract class OutboundInterfaceABSTRACT extends TimerTask implements OutboundInterfaceINTERFACE{
	boolean enabled = false;
	private String type = "";
	public OutboundConnectorABSTRACT connector;
	private Long timerFrequency = (long) 2000;
	private boolean running=false;
	Timer timer = new Timer();
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
    private String delimeter="";
    private String outputFileExtension = "";

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
		if (Utility.replaceNullStringwithBlank(extension).equals("")==false)
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
			outputPath = System.getProperty("user.dir") + File.separator+"interface" + File.separator + "output";
		
		return this.outputPath;
	}

	public void setEnabled(boolean enable)
	{
		logger.debug("setEnabled "+String.valueOf(enable));
		if ((enable==true) && (enabled==false) && (running==false))
		{
			// start
			connector.setEnabled(enabled);
			this.enabled = enable;
			setRunning(true);
			logger.debug("Start Requested : ["+getDescription()+"]");
			timer.scheduleAtFixedRate(this, 0, timerFrequency);
		}
		else
		{
			// stop
			timer.cancel();
			logger.debug("Stop Requested : ["+getDescription()+"]");
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
		outputPattern = Utility.replaceNullStringwithBlank(pattern);

	}
	
	public String getCSVOptions()
	{
		return csvOptions;
	}
	
	public void setCSVOptions(String options)
	{
		csvOptions = Utility.replaceNullStringwithBlank(options);

	}
	
	public String getOptionDelimeter()
	{
		return delimeter;
	}
	
	public void setOptionDelimeter(String value)
	{
		delimeter = Utility.replaceNullStringwithBlank(value);

	}

	public void setType(String type)
	{
		this.type = type;

		switch (type)
		{
		case OutboundConnectorINTERFACE.Connector_ASCII:
			connector = new OutboundConnectorASCII((OutboundInterface) this);
			setOutputFileExtension("txt");
			break;
		case OutboundConnectorINTERFACE.Connector_CSV:
			connector = new OutboundConnectorCSV((OutboundInterface) this);
			setOutputFileExtension("csv");
			break;
		case OutboundConnectorINTERFACE.Connector_DB:
			connector = new OutboundConnectorDB((OutboundInterface) this);
			setOutputFileExtension("db");
			break;
		case OutboundConnectorINTERFACE.Connector_IDOC:
			connector = new OutboundConnectorIDOC((OutboundInterface) this);
			setOutputFileExtension("idoc");
			break;
		case OutboundConnectorINTERFACE.Connector_XML:
			connector = new OutboundConnectorXML((OutboundInterface) this);
			setOutputFileExtension("xml");
			break;			
		default:
			throw new IllegalArgumentException();
		}

	}
	
	public String getXSLTPath()
	{
		if (xsltPath.equals(""))
			xsltPath = System.getProperty("user.dir")  + File.separator + "xslt";		
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
