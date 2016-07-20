package com.commander4j.html;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JPrint;

/**
 * @author David
 * 
 */
public class JMenuRFPrinterList
{

	final Logger logger = Logger.getLogger(JMenuRFPrinterList.class);

	public String buildPrinterList(String selectedPrinter) {

		String result = "";
		String selected = "";
		LinkedList<String> list = new LinkedList<String>();
		list = JPrint.getPrinterNames();
		result = "<SELECT width=\"100%\" style=\"width: 100%\" NAME=\"" + "selectedPrintQueue" + "\">";
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
