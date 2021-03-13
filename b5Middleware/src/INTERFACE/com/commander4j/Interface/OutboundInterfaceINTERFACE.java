package INTERFACE.com.commander4j.Interface;

public interface OutboundInterfaceINTERFACE
{

	public String getDescription();

	public boolean getEnabled();

	public String getOutputPath();

	public String getType();

	public String getXSLTFilename();

	public void setDescription(String description);

	public void setEnabled(boolean yesno);

	public void setOutputPath(String path);

	public void setType(String type);

	public void setXSLTFilename(String path);

	public void setId(String id);

	public String getId();
	
	public void setCompareParam1(String xpath);
	
	public void setCompareParam1_Type(String type);
	
	public void setCompareParam2(String xpath);
	
	public void setCompareParam2_Type(String type);
	
	public void setComparator(String equalNotEqual);

}
