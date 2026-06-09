package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : parseFunction.java
 * 
 * Package Name : com.commander4j.bar
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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;
import com.commander4j.util.RequestCurrentUser;

public class parseFunction
{
	private String hostID;
	private String sessionID;
	private JDBControl ctrl;
	private JDBPallet pal;
	private String expiryDateMode = "";
	private static String incorrectNoParams = " [Incorrect number of parameters]";
	private static String incorrectDateTimeFormat = " [Incorrect date/time format]";
	private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(parseFunction.class);

	ResultSetMetaData rsMetaData;
	ResultSet rs;

	public parseFunction(String host, String session)
	{
		setHostID(host);
		setSessionID(session);

		ctrl = new JDBControl(getHostID(), getSessionID());
		pal = new JDBPallet(getHostID(), getSessionID());

		expiryDateMode = ctrl.getKeyValue("EXPIRY DATE MODE");
	}

	public void init(ResultSet rs)
	{
		try
		{
			this.rs = rs;
			rsMetaData = rs.getMetaData();
		}
		catch (SQLException e)
		{

		}
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String doParsing(String inputLine)
	{
		String parseResult = inputLine;

		// Supported Expressions using format

		String[] Functions = new String[]
		{ "<SUBTR_LPAD(", "<DATETIME(", "<SUBSTRING(", "<LEFT(", "<RIGHT(", "<PADLEFT(", "<PADRIGHT(",  "<JULIAN_YJJJ(","<UPPERCASE(", "<LOWERCASE(", "<TRIM(", "<LTRIM(", "<RTRIM(", "<TIMESTAMP(", "<USERNAME(", "<VERSION(", "<IIF(", "<EXPIRYDATE(", "<PRODDATE(", "<DATE_CREATED(",
				"<PALLET_WEIGHT_TEXT(", "<PALLET_WEIGHT_BARCODE(" };

		// Resolve calls innermost-first. On each pass we locate the function call
		// with the highest start index - it cannot contain another call, so it is
		// safe to evaluate and its result feeds any enclosing call. Repeating until
		// none remain lets functions be nested, e.g. <PADLEFT(<SUBSTRING(x,1,3)>,5,0)>.
		int safety = 5000; // guard so a malformed line can never loop forever

		while (safety-- > 0)
		{
			// Find the rightmost (= innermost) "<NAME(" token of any function.
			int callStart = -1;
			String token = "";
			for (int x = 0; x < Functions.length; x++)
			{
				int pos = parseResult.lastIndexOf(Functions[x]);
				if (pos > callStart)
				{
					callStart = pos;
					token = Functions[x];
				}
			}

			if (callStart < 0)
			{
				break; // no function calls remain
			}

			int bracketStartPos = callStart + token.length();
			// The call closes with ")>". Because this call is innermost, its parameters
			// contain no nested call and (data parentheses were masked to {}/} earlier)
			// no stray parenthesis, so the first ")>" is its closing marker.
			int bracketEndPos = parseResult.indexOf(")>", bracketStartPos);

			if (bracketEndPos < 0)
			{
				logger.warn("Unterminated function call [" + token + " ... )>] in [" + parseResult + "] - leaving literal");
				break;
			}

			String functionName = token.substring(1, token.length() - 1).toUpperCase();
			String paramString = parseResult.substring(bracketStartPos, bracketEndPos);
			String[] params = paramString.split(",");

			String value = executeFunction(functionName, params);

			// Protect commas in the result so an enclosing function's split(",") keeps
			// it as a single argument. Restored alongside the {}/() restore below.
			value = value.replace(",", "±");

			parseResult = parseResult.substring(0, callStart) + value + parseResult.substring(bracketEndPos + 2);
		}

		parseResult = parseResult.replace("±", ",");
		parseResult = parseResult.replace("{", "(");
		parseResult = parseResult.replace("}", ")");
		return parseResult;
	}

	// Bounds-safe substring: clamps from/to into [0, length] so short data returns
	// what is available instead of throwing an error string onto the label. Matches
	// the autolab4j engine so shared label templates render identically.
	private String safeSubstring(String s, int from, int to)
	{
		if (from < 0)
		{
			from = 0;
		}
		if (from > s.length())
		{
			from = s.length();
		}
		if (to > s.length())
		{
			to = s.length();
		}
		if (to < from)
		{
			to = from;
		}
		return s.substring(from, to);
	}

	private String executeFunction(String functionName, String[] params)
	{
		String result = "";
		String target = "";
		String pad = "";
		int start;
		int end;
		int size;

		try
		{

			// Execute SUBSTRING
			functionName = functionName.trim().toUpperCase();

			if (functionName.equals("SUBSTRING"))
			{
				if (params.length == 3)
				{
					target = params[0];
					start = Integer.valueOf(params[1].toString());
					end = Integer.valueOf(params[2].toString());
					result = safeSubstring(target, start - 1, start + end - 1);
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("SUBTR_LPAD"))
			{
				if (params.length == 5)
				{
					target = params[0];
					start = Integer.valueOf(params[1].toString());
					end = Integer.valueOf(params[2].toString());
					size = Integer.valueOf(params[3].toString());
					pad = params[4];
					while (target.length() < size)
					{
						target = pad + target;
					}
					result = safeSubstring(target, start - 1, start + end - 1);
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			// Execute SUBSTRING

			if (functionName.equals("LEFT"))
			{
				if (params.length == 2)
				{
					target = params[0];
					end = Integer.valueOf(params[1].toString());
					result = safeSubstring(target, 0, end);
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("RIGHT"))
			{
				if (params.length == 2)
				{
					target = params[0];
					start = Integer.valueOf(params[1].toString());
					end = target.length();
					result = safeSubstring(target, target.length() - start, target.length());
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("PADLEFT"))
			{
				// PADLEFT(input,3,0)
				if (params.length == 3)
				{
					target = params[0];
					size = Integer.valueOf(params[1].toString());
					pad = params[2];
					while (target.length() < size)
					{
						target = pad + target;
					}
					result = target;
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("PADRIGHT"))
			{
				if (params.length == 3)
				{
					target = params[0];
					size = Integer.valueOf(params[1].toString());
					pad = params[2];
					while (target.length() < size)
					{
						target = target + pad;
					}
					result = target;
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("UPPERCASE"))
			{
				if (params.length == 1)
				{
					target = params[0];
					result = target.toUpperCase();
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("LOWERCASE"))
			{
				if (params.length == 1)
				{
					target = params[0];
					result = target.toLowerCase();
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("TRIM"))
			{
				if (params.length == 1)
				{
					target = params[0];
					result = target.trim();
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("RTRIM"))
			{
				if (params.length == 1)
				{

					target = params[0];
					target = ("x" + target).trim();
					if (target.length() > 1)
					{
						result = target.substring(1);
					}
					else
					{
						result = "";
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("LTRIM"))
			{
				if (params.length == 1)
				{
					target = params[0];
					target = (target + "x").trim();
					if (target.length() > 1)
					{
						result = target.substring(0, target.length() - 1);
					}
					else
					{
						result = "";
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("TIMESTAMP"))
			{
				if (params.length == 1)
				{
					try
					{
						if (params[0].equals(""))
						{
							params[0] = "dd/MM/yyyy HH:mm:ss";
						}
						DateFormat dateFormat = new SimpleDateFormat(params[0]);
						Date date = new Date();
						result = dateFormat.format(date);
					}
					catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("USERNAME"))
			{
				if (params.length == 1)
				{
					result = RequestCurrentUser.getAuthoritativeUsername().toUpperCase();
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("VERSION"))
			{
				if (params.length == 1)
				{
					result = JVersion.getProgramVersionValue().toString();
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("IIF"))
			{
				if (params.length == 4)
				{
					if (params[0].equals(params[1]))
					{
						result = params[2];
					}
					else
					{
						result = params[3];
					}

				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("PALLET_WEIGHT_TEXT"))
			{
				if (params.length == 2)
				{

					String weightUom = params[0];

					if (weightUom.equals(""))
					{
						weightUom = "KG";
					}

					int decimalPlaces = Integer.valueOf(params[1]);

					String sscc = rs.getString("SSCC");

					result = pal.getPalletWeight(sscc, weightUom, decimalPlaces);

				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("PALLET_WEIGHT_BARCODE"))
			{
				if (params.length == 2)
				{

					String weightUom = params[0];

					if (weightUom.equals(""))
					{
						weightUom = "KG";
					}

					int decimalPlaces = Integer.valueOf(params[1]);

					String sscc = rs.getString("SSCC");

					result = pal.getPalletWeight(sscc, weightUom, decimalPlaces);

					result = result.replace(".", "");

				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("EXPIRYDATE"))
			{
				if (params.length == 1)
				{
					try
					{
						if (params[0].equals(""))
						{
							params[0] = "dd/MM/yyyy";
						}

						Timestamp expirydate;

						if (rs.getMetaData().getTableName(1).toLowerCase().equals("view_label_data"))
						{
							expirydate = rs.getTimestamp("expiry_date");
						}
						else
						{
							if (expiryDateMode.endsWith("BATCH"))
							{
								expirydate = rs.getTimestamp("expiry_date");
							}
							else
							{
								expirydate = rs.getTimestamp("sscc_expiry_date");
							}
						}

						expirydate.setNanos(0);
						DateFormat dateFormat = new SimpleDateFormat(params[0]);

						result = dateFormat.format(expirydate);

					}
					catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("JULIAN_YJJJ"))
			{

				Timestamp dateOfManufacture;

				dateOfManufacture = rs.getTimestamp("date_of_manufacture");

				if (dateOfManufacture == null)
				{
					// If the date is null then return a string of
					// spaces the same size as the format spec.
					result = JUtility.padSpace(3);
				}
				else
				{
					dateOfManufacture.setNanos(0);
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

					String temp = dateFormat.format(dateOfManufacture);
					
					Calendar caldate = Calendar.getInstance();
					caldate.setTimeInMillis(dateOfManufacture.getTime());
					
					long jd = JUtility.getJulianDay(caldate);
					String jds = Long.toString(jd).trim();
					jds = JUtility.padString(jds, false, 3, "0");
					result = temp.substring(0, 1) + jds;
				}

			}

			if (functionName.equals("PRODDATE"))
			{
				if (params.length == 1)
				{
					try
					{
						if (params[0].equals(""))
						{
							params[0] = "dd/MM/yyyy";
						}

						Timestamp dateOfManufacture;

						dateOfManufacture = rs.getTimestamp("date_of_manufacture");

						if (dateOfManufacture == null)
						{
							// If the date is null then return a string of
							// spaces the same size as the format spec.
							result = JUtility.padSpace(params[0].length());
						}
						else
						{
							dateOfManufacture.setNanos(0);
							DateFormat dateFormat = new SimpleDateFormat(params[0]);

							result = dateFormat.format(dateOfManufacture);
						}

					}
					catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}
			
			if (functionName.equals("DATE_CREATED"))
			{
				if (params.length == 1)
				{
					try
					{
						if (params[0].equals(""))
						{
							params[0] = "dd/MM/yyyy";
						}

						Timestamp dateOfManufacture;

						dateOfManufacture = rs.getTimestamp("date_created");

						if (dateOfManufacture == null)
						{
							// If the date is null then return a string of
							// spaces the same size as the format spec.
							result = JUtility.padSpace(params[0].length());
						}
						else
						{
							dateOfManufacture.setNanos(0);
							DateFormat dateFormat = new SimpleDateFormat(params[0]);

							result = dateFormat.format(dateOfManufacture);
						}

					}
					catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("DATETIME"))
			{
				String fieldname = "";
				String format = "";
				Timestamp fielddatetime;

				if (params.length == 2)
				{
					fieldname = params[0];
					format = params[1];
					try
					{
						if (format.equals(""))
						{
							format = "dd/MM/yyyy HH:mm:ss";
						}

						if (fieldname.equals(""))
						{
							fieldname = "dd/MM/yyyy hh:mm:ss";
						}

						fielddatetime = rs.getTimestamp(fieldname);

						fielddatetime.setNanos(0);
						DateFormat dateFormat = new SimpleDateFormat(format);

						result = dateFormat.format(fielddatetime);
						fielddatetime = null;
						format = null;
						format = null;

					}
					catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				}
				else
				{
					result = functionName + incorrectNoParams;
				}

			}

		}
		catch (Exception ex)
		{
			result = functionName + " [" + ex.getMessage() + "]";
		}

		return result;
	}

}