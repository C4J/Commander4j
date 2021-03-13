package INTERFACE.com.commander4j.Connector;

import org.w3c.dom.Document;

public interface OutboundConnectorINTERFACE
{

	public final static String Connector_PDF_PRINT = "PDF_PRINT";
	public final static String Connector_ASCII = "ASCII";
	public final static String Connector_EMAIL = "EMAIL";			
	public final static String Connector_CSV = "CSV";
	public final static String Connector_IDOC = "IDOC";
	public final static String Connector_XML = "XML";
	public final static String Connector_MQTT = "MQTT";
	public final static String Connector_SOCKET = "SOCKET";
	public final static String Connector_RAW = "RAW";

	public boolean connectorSave(String path,String prefix,String filename);

	public Document getData();

	public void setData(Document data);

}
