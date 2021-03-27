package com.commander4j.batch;

import java.util.Calendar;

import com.commander4j.autolab.AutoLab;
import com.commander4j.utils.JUtility;


public class Batch
{
	
	private JUtility utils = new JUtility();

	public String getDefaultBatchNumber(String uuid, Calendar caldate)
	{
		String result = "";
		
		result = AutoLab.getDataSet_Field(uuid, "BATCH_FORMAT");
		
		if (result.indexOf("{SHIFT}") >= 0)
		{
			String temp="";
			int hour = caldate.get(Calendar.HOUR_OF_DAY);
			if (hour < 6)
			{
				temp = "1";
			}
			if ((hour >= 6) && (hour <  14))
			{
				temp = "2";
			}
			if ((hour >= 14) && (hour <  22))
			{
				temp = "3";
			}
			if (hour >= 22)
			{
				temp = "1";
			}
			result = result.replaceAll("\\{SHIFT\\}", temp);
		}

		if (result.indexOf("{PLANT}") >= 0)
		{
			String plant = AutoLab.getDataSet_Field(uuid, "PLANT");
			result = result.replaceAll("\\{PLANT\\}", plant);
		}
		
		if (result.indexOf("{PO_PLANT}") >= 0)
		{
			String poplant  = AutoLab.getDataSet_Field(uuid, "PLANT");

			result = result.replaceAll("\\{PO_PLANT\\}", poplant);
		}
	
		
		
		if (result.indexOf("{WEEK}") >= 0)
		{
			result = result.replaceAll("\\{WEEK\\}", utils.getWeekOfYear(caldate));
		}	
		
		if (result.indexOf("{DAY}") >= 0)
		{
			result = result.replaceAll("\\{DAY\\}", utils.getDayOfWeekString1(caldate));
		}		

		if (result.indexOf("{PROCESS_ORDER}") >= 0)
		{
			String po  = AutoLab.getDataSet_Field(uuid, "PROCESS_ORDER");
			
			result = result.replaceAll("\\{PROCESS_ORDER\\}",po);
		}

		if (result.indexOf("{YEAR1}") >= 0)
		{
			String curdate = utils.getSQLDate(caldate).toString().substring(3, 4);
			result = result.replaceAll("\\{YEAR1\\}", curdate);
		}

		if (result.indexOf("{YEAR2}") >= 0)
		{
			String curdate = utils.getSQLDate(caldate).toString().substring(2, 4);
			result = result.replaceAll("\\{YEAR2\\}", curdate);
		}

		if (result.indexOf("{YEAR4}") >= 0)
		{
			String curdate = utils.getSQLDate(caldate).toString().substring(0, 4);
			result = result.replaceAll("\\{YEAR4\\}", curdate);
		}

		if (result.indexOf("{MONTH}") >= 0)
		{
			String curdate = utils.getSQLDate(caldate).toString().substring(5, 7);
			result = result.replaceAll("\\{MONTH\\}", curdate);
		}

		if (result.indexOf("{DAY}") >= 0)
		{
			String curdate = utils.getSQLDate(caldate).toString().substring(8, 10);
			result = result.replaceAll("\\{DAY\\}", curdate);
		}

		if (result.indexOf("{JULIAN_DAY}") >= 0)
		{
			long jd = utils.getJulianDay(caldate);
			String jds = Long.toString(jd).trim();
			jds = utils.padString(jds, false, 3, "0");
			result = result.replaceAll("\\{JULIAN_DAY\\}", jds);
		}

		return result;
	}
}
