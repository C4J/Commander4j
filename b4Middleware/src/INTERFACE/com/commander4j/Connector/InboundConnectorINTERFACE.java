package INTERFACE.com.commander4j.Connector;

import org.w3c.dom.Document;

public interface InboundConnectorINTERFACE
{

	public final static String Connector_ASCII = "ASCII";
	public final static String Connector_CSV = "CSV";
	public final static String Connector_IDOC = "IDOC";
	public final static String Connector_XML = "XML";
	public final static String Connector_DB = "DB";

	public boolean connectorLoad(String filename);

	public Document getData();

	public void setData(Document data);

}
