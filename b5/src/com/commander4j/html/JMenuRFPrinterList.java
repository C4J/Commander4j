package com.commander4j.html;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuRFPrinterList.java
 * 
 * Package Name : com.commander4j.html
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

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JPrint;

public class JMenuRFPrinterList
{

	final Logger logger = Logger.getLogger(JMenuRFPrinterList.class);

	public String buildPrinterList(String selectedPrinter) {

		String result = "";
		String selected = "";
		LinkedList<String> list = new LinkedList<String>();
		list = JPrint.getPrinterNames();
		result = "<SELECT width=\"100%\" style=\"font-size: 24px; width: 100%\" NAME=\"" + "selectedPrintQueue" + "\">";
		result = result + "<OPTION>";

		if (list.size() > 0)
		{
			for (int x = 0; x < list.size(); x++)
		    //for (int x = list.size()-1; x >=0; x--)
			{
				if (list.get(x).toString().equals(selectedPrinter))
				{
					selected = " SELECTED";
				}
				else
				{
					selected = "";
				}
				result = result + "<OPTION" + selected + ">" + list.get(x).toString();
			}
		}
		result = result + "</SELECT>";

		return result;
	}
}
