package com.commander4j.labeller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;


public class LabellerUtility {
    
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((LabellerUtility.class));
	
	public Timestamp getTimeStampFromlogopakDIRString(String date,String time)
	{
		Timestamp result;


		try
		{
			Calendar caldate = Calendar.getInstance();

			int year = 0;
			int month = 0;
			int day = 0;
			int hour = 0;
			int min = 0;
			int second = 0;

			year = Integer.valueOf("20"+date.substring(0, 2));
			month = Integer.valueOf(date.substring(2, 4));
			day = Integer.valueOf(date.substring(4, 6));
			hour = Integer.valueOf(time.substring(0,2));
			min = Integer.valueOf(time.substring(2,4));
			second = 0;
			
			if ((month <1) | (month>12))
			{
				throw new Exception("Invalid month "+String.valueOf(month));
			}

			if ((day <1) | (day>31))
			{
				throw new Exception("Invalid day "+String.valueOf(day));
			}
			
			caldate.set(year, month - 1, day, hour, min, second);

			result = new Timestamp(caldate.getTimeInMillis());

			result.setNanos(0); // or other value
		} catch (Exception ex)
		{
			result = null;
		}

		return result;
	}
	
	public void  pause(int interval)
	{
		try
		{
			TimeUnit.MILLISECONDS.sleep(interval);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  String decodeControlChars(String input)
	{
		String result = input;
		
		result = result.replaceAll(Pattern.quote("\u001B"),"<ESC>" );
		result = result.replaceAll(Pattern.quote("\t"),"<TAB>" );
		
		result = result.replaceAll(Pattern.quote("\u0002"),"<STX>" );
		result = result.replaceAll(Pattern.quote("\u0003"),"<ETX>" );

		result = result.replaceAll(Pattern.quote("\n"),"<LF>");
		result = result.replaceAll(Pattern.quote("\r"),"<CR>");

		result = result.replaceAll(Pattern.quote("\u0006"),"<ACK>" );
		result = result.replaceAll(Pattern.quote("\u0015"),"<NAK>" );
		
		return result;
	}
	
	public  String encodeControlChars(String input)
	{
		String result = input;

		result = result.replaceAll(Pattern.quote("<esc>"),"\u001B" );
		result = result.replaceAll(Pattern.quote("<ESC>"),"\u001B" );

		result = result.replaceAll(Pattern.quote("<ack>"),"\u0006" );
		result = result.replaceAll(Pattern.quote("<ACK>"),"\u0006" );

		result = result.replaceAll(Pattern.quote("<nak>"),"\u0015" );
		result = result.replaceAll(Pattern.quote("<NAK>"),"\u0015" );
		
		result = result.replaceAll(Pattern.quote("<tab>"),"\t" );
		result = result.replaceAll(Pattern.quote("<TAB>"),"\t" );
		
		result = result.replaceAll(Pattern.quote("<stx>"),"\u0002" );
		result = result.replaceAll(Pattern.quote("<STX>"),"\u0002" );

		result = result.replaceAll(Pattern.quote("<etx>"),"\u0003" );
		result = result.replaceAll(Pattern.quote("<ETX>"),"\u0003" );

		result = result.replaceAll(Pattern.quote("<lf>"),"\n");
		result = result.replaceAll(Pattern.quote("<LF>"),"\n");
		
		result = result.replaceAll(Pattern.quote("<cr>"),"\r");
		result = result.replaceAll(Pattern.quote("<CR>"),"\r");

		
		return result;
	}
	
	public String replaceNullStringwithBlank(String value)
	{
		if (value == null)
		{
			value = "";
		}

		return value;
	}
	
	public String padString(int size, String character)
	{
		String s = "";

		for (int i = 0; i < size; i++)
		{
			s = s + character;
		}

		return s;
	}
	
	public String padString(String input, boolean right, int size, String character)
	{
		int inputlength = 0;
		String result = replaceNullStringwithBlank(input);

		inputlength = result.length();

		if (inputlength > size)
		{
			// result = result.substring(0,size-1);
			result = result.substring(0, size);
		} else
		{
			if (inputlength < size)
			{
				if (right == true)
				{
					result = result + padString(size - inputlength, character);
				} else
				{
					result = padString(size - inputlength, character) + result;
				}
			}
		}

		return result;
	}
}
