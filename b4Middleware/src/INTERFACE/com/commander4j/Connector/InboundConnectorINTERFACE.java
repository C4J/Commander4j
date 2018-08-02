package INTERFACE.com.commander4j.Connector;

import org.w3c.dom.Document;

public interface InboundConnectorINTERFACE
{

	public final static String Connector_ASCII = "ASCII";
	public final static String Connector_CSV = "CSV";
	public final static String Connector_Excel = "XLS";
	public final static String Connector_IDOC = "IDOC";
	public final static String Connector_EMAIL = "EMAIL";
	public final static String Connector_RAW = "RAW";
	public final static String Connector_XML = "XML";
	public final static String Connector_DB = "DB";
	public final static String Connector_JDBC = "JDBC";
	
	public final static String[] Mask_ASCII = {"txt", "TXT"};
	public final static String[] Mask_CSV = {"csv", "CSV"};
	public final static String[] Mask_Excel = {"xls", "XLS", "xlsx", "XLSX"};	
	public final static String[] Mask_IDOC = {"txt", "TXT","idoc","IDOC"};
	public final static String[] Mask_EMAIL = {};	
	public final static String[] Mask_RAW = {};
	public final static String[] Mask_XML = {"xml", "XML"};
	public final static String[] Mask_DB = {"db", "DB","dbf","DBF","mdb","MDB"};
	public final static String[] Mask_JDBC = {};

	public boolean connectorLoad(String filename);

	public Document getData();

	public void setData(Document data);

}
