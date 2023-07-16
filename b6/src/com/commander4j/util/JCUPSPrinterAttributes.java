package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JCUPSPrinterAttributes.java
 * 
 * Package Name : com.commander4j.util
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

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
