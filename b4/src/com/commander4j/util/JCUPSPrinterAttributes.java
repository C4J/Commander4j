package com.commander4j.util;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import java.util.List;

public class JCUPSPrinterAttributes
{

	public static String getName(String printerDescription)
	{
		String result = "error";
		try
		{
			List<CupsPrinter> printers = new CupsClient().getPrinters();

			for (CupsPrinter p : printers)
			{
				String temp1 = p.getDescription().toString();
		 	 	String temp2 = p.getName();
				String temp3 = printerDescription;
				//Modified to look for match in name or description (feature of OSX Java 7)
				if ((temp1.equals(temp3)) || (temp2.equals(temp3)))
				{
					result = p.getName();
					break;
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
}
