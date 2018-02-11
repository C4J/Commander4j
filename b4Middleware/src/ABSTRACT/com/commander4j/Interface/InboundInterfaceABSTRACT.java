package ABSTRACT.com.commander4j.Interface;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.commander4j.Connector.Inbound.InboundConnectorASCII;
import com.commander4j.Connector.Inbound.InboundConnectorCSV;
import com.commander4j.Connector.Inbound.InboundConnectorDB;
import com.commander4j.Connector.Inbound.InboundConnectorExcel;
import com.commander4j.Connector.Inbound.InboundConnectorIDOC;
import com.commander4j.Connector.Inbound.InboundConnectorEmail;
import com.commander4j.Connector.Inbound.InboundConnectorXML;
import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.Interface.Mapping.Map;
import com.commander4j.util.Utility;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;
import INTERFACE.com.commander4j.Connector.InboundConnectorINTERFACE;
import INTERFACE.com.commander4j.Interface.InboundInterfaceINTERFACE;

public abstract class InboundInterfaceABSTRACT extends TimerTask implements InboundInterfaceINTERFACE
{
	boolean enabled = false;
	private String type = "";
	public InboundConnectorABSTRACT connector;
	private Long timerFrequency = (long) 2000;
	private boolean running = false;
	private Timer timer = new Timer();
	protected Map map;
	protected Document data;
	private String inputPath = "";
	private String[] inputFileMask = {"*"};
	private String inputFilename = "";;
	private String xsltFilename = "";
	private String xsltPath = "";
	private String description;
	private String id = "";
	private String idocSchemaFilename="";

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundInterfaceABSTRACT.class));

	private String inputPattern = "";

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

	public String getXSLTPath()
	{
		if (xsltPath.equals(""))
			xsltPath = System.getProperty("user.dir") + File.separator + "xslt" + File.separator;
		return xsltPath;
	}

	public void setXSLTFilename(String xsltFilename)
	{
		this.xsltFilename = xsltFilename;
	}

	public void setXSLTPath(String xsltPath)
	{
		this.xsltPath = xsltPath;
	}

	public InboundInterfaceABSTRACT(Map map)
	{
		this.map = map;
	}

	public void processInboundData()
	{
		map.processInboundInterfaceToMap(getFilename(), getData());
	}

	public String getFilename()
	{
		return this.inputFilename;
	}

	public String getIdocSchemaFilename()
	{
		return this.idocSchemaFilename;
	}
	
	public Document getData()
	{
		return this.data;
	}

	public boolean getEnabled()
	{
		return this.enabled;
	}

	public void setInputFileMask(String[] mask)
	{
		this.inputFileMask = mask;
	}

	public String[] getInputFileMask()
	{
		if (this.inputFileMask.length==0)
		{
			return null;
		}
		else
		{
		return this.inputFileMask;
		}
	}

	public void setInputFilename(String filename)
	{
		this.inputFilename = filename;
	}
	
	public void setIdocSchemaFilename(String filename)
	{
		this.idocSchemaFilename = filename;
	}

	public void setInputPath(String path)
	{
		this.inputPath = path;
	}

	public String getInputPath()
	{
		if (inputPath.equals(""))
			inputPath = System.getProperty("user.dir") + File.separator + "interface" + File.separator + "input";

		return this.inputPath;
	}

	public void setEnabled(boolean enable)
	{
		logger.debug("setEnabled " + String.valueOf(enable));
		if ((enable == true) && (enabled == false) && (running == false))
		{
			// start
			connector.setEnabled(enabled);
			this.enabled = enable;
			setRunning(true);
			logger.debug("Start Requested : [" + getDescription() + "]");
			timer.schedule(this, 0, timerFrequency);
		} else
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

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setPollingInterval(Long millisec)
	{
		timerFrequency = millisec;
	}

	public String getInputPattern()
	{
		return inputPattern;
	}

	public void setInputPattern(String pattern)
	{
		inputPattern = Utility.replaceNullStringwithBlank(pattern);

	}

	public void setType(String type)
	{
		this.type = type;

		switch (type)
		{
		case InboundConnectorINTERFACE.Connector_ASCII:
			connector = new InboundConnectorASCII((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_ASCII);
			break;
		case InboundConnectorINTERFACE.Connector_CSV:
			connector = new InboundConnectorCSV((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_CSV);
			break;
		case InboundConnectorINTERFACE.Connector_EMAIL:
			connector = new InboundConnectorEmail((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_EMAIL);
			break;
		case InboundConnectorINTERFACE.Connector_Excel:
			connector = new InboundConnectorExcel((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_Excel);
			break;			
		case InboundConnectorINTERFACE.Connector_DB:
			connector = new InboundConnectorDB((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_DB);
			break;
		case InboundConnectorINTERFACE.Connector_IDOC:
			connector = new InboundConnectorIDOC((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_IDOC);
			break;
		case InboundConnectorINTERFACE.Connector_XML:
			connector = new InboundConnectorXML((InboundInterface) this);
			setInputFileMask(InboundConnectorINTERFACE.Mask_XML);
			break;
		default:
			throw new IllegalArgumentException();
		}

	}

}
