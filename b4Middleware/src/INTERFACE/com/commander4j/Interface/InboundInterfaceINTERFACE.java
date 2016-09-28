package INTERFACE.com.commander4j.Interface;

public interface InboundInterfaceINTERFACE
{

	public String getDescription();

	public boolean getEnabled();

	public String getInputFileMask();

	public String getInputPath();

	public String getType();

	public String getXSLTFilename();

	public String getXSLTPath();

	public void setDescription(String description);

	public void setEnabled(boolean yesno);

	public void setInputFileMask(String mask);

	public void setInputFilename(String filename);

	public void setInputPath(String path);

	public void setPollingInterval(Long millisec);

	public void setType(String type);

	public void setXSLTFilename(String path);

	public void setXSLTPath(String path);

	public void setId(String id);

	public String getId();
}
