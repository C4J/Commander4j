package com.commander4j.config;

public class ProdLineDefinition
{
	String enabled = "";
	String prodLine_Name = "";
	String modbus_Name = "";
	String modbus_IPAddress = "";
	String modbus_Port = "";
	String modbus_Coil_Address = "";
	String modbus_Timeout = "";
	String modbus_Coil_Trigger_Value = "";
	String printer_Name = "";
	String sscc_Filename = "";

	
	public String getEnabled()
	{
		return enabled;
	}
	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}
	public String getProdLine_Name()
	{
		return prodLine_Name;
	}
	public void setProdLine_Name(String prodLine_Name)
	{
		this.prodLine_Name = prodLine_Name;
	}
	public String getModbus_Name()
	{
		return modbus_Name;
	}
	public void setModbus_Name(String modbus_Name)
	{
		this.modbus_Name = modbus_Name;
	}
	public String getModbus_IPAddress()
	{
		return modbus_IPAddress;
	}
	public void setModbus_IPAddress(String modbus_IPAddress)
	{
		this.modbus_IPAddress = modbus_IPAddress;
	}
	public String getModbus_Port()
	{
		return modbus_Port;
	}
	public void setModbus_Port(String modbus_Port)
	{
		this.modbus_Port = modbus_Port;
	}
	public String getModbus_Coil_Address()
	{
		return modbus_Coil_Address;
	}
	public void setModbus_Coil_Address(String modbus_Coil_Address)
	{
		this.modbus_Coil_Address = modbus_Coil_Address;
	}
	public String getModbus_Timeout()
	{
		return modbus_Timeout;
	}
	public void setModbus_Timeout(String modbus_Timeout)
	{
		this.modbus_Timeout = modbus_Timeout;
	}
	public String getModbus_Coil_Trigger_Value()
	{
		return modbus_Coil_Trigger_Value;
	}
	public void setModbus_Coil_Trigger_Value(String modbus_Coil_Trigger_Value)
	{
		this.modbus_Coil_Trigger_Value = modbus_Coil_Trigger_Value;
	}
	public String getPrinter_Name()
	{
		return printer_Name;
	}
	public void setPrinter_Name(String printer_Name)
	{
		this.printer_Name = printer_Name;
	}
	public String getSscc_Filename()
	{
		return sscc_Filename;
	}
	public void setSscc_Filename(String sscc_Filename)
	{
		this.sscc_Filename = sscc_Filename;
	}
}
