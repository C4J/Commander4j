package com.commander4j.print;


import javax.print.PrintService;
import com.commander4j.db.JDBPrinters;

public class JPrintDevice implements Comparable<JPrintDevice>
{
	private String type = "";
	private PrintService queue;
	private JDBPrinters printer;
	
	public void setType(String val)
	{
		type = val;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setQueue(PrintService val)
	{
		queue = val;
	}
	
	public void setPrinter(JDBPrinters val)
	{
		printer = val;
	}
	
	public PrintService getQueue()
	{
		return queue;
	}
	
	public JDBPrinters getPrinter()
	{
		return printer;
	}
	
    @Override
    public String toString()
    {
        String result = "";

        if ("queue".equalsIgnoreCase(type))
        {
            if (queue != null)
            {
                result = queue.getName();
            }
        }
        else
        {
            if (printer != null)
            {
                result = printer.getPrinterID() + " - " + printer.getDescription();
            }
        }

        return result;
    }

	@Override
    public int compareTo(JPrintDevice other)
	{
	      if (other == null)
	        {
	            // non-null is considered "greater than" null
	            return 1;
	        }

	        String thisText = this.toString();
	        String otherText = other.toString();

	        if (thisText == null && otherText == null)
	        {
	            return 0;
	        }
	        else if (thisText == null)
	        {
	            return -1;
	        }
	        else if (otherText == null)
	        {
	            return 1;
	        }

	        // Case-insensitive comparison of the display text
	        return thisText.compareToIgnoreCase(otherText);
	}
	
}
