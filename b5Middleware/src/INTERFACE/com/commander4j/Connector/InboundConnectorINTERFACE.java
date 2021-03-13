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
	public final static String Connector_PDF_PRINT = "PDF_PRINT";
	
	public final static String[] Mask_ASCII = {"txt", "TXT"};
	public final static String[] Mask_CSV = {"csv", "CSV"};
	public final static String[] Mask_Excel = {"xls", "XLS", "xlsx", "XLSX"};	
	public final static String[] Mask_IDOC = {"txt", "TXT","idoc","IDOC"};
	public final static String[] Mask_EMAIL = {"txt", "TXT","xml", "XML","csv", "CSV","idoc","IDOC","pdf", "PDF"};	
	public final static String[] Mask_RAW = {"txt", "TXT","xml", "XML","csv", "CSV","idoc","IDOC","pdf", "PDF"};
	public final static String[] Mask_XML = {"xml", "XML"};
	public final static String[] Mask_PDF_PRINT = {"pdf", "PDF"};


	public boolean connectorLoad(String filename);

	public Document getData();

	public void setData(Document data);

}
