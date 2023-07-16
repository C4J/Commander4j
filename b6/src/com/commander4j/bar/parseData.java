package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : parseData.java
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

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.commander4j.util.JUtility;

public class parseData {

	ResultSetMetaData rsMetaData;
	ResultSet rs;
	HashMap<String, String> variables;

	
	private String hostID;
	private String sessionID;
	
	public parseData(String host,String session)
	{
		setHostID(host);
		setSessionID(session);
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

	public void init(ResultSet rs, HashMap<String, String> variables)
	{
		try
		{
			this.rs = rs;
			if (variables==null)
			{
				this.variables = new HashMap<String, String>();
				this.variables.put("empty", "empty");
			}
			else
			{
				this.variables = variables;
			}
			
			rsMetaData = rs.getMetaData();
		} catch (SQLException e)
		{

		}
	}

	public String doParsing(String inputLine)
	{
		String parseResult = inputLine;
		String fieldDef = "";
		String fieldName = "";
		String fieldFormat = "";
		int fieldType = 0;
		String fieldData = "";
		int fieldColumn = 0;
		String fullDataDeclaration = "";
		String[] attributes;
		int dataStartPos;
		int dataEndPos;

		while (parseResult.indexOf("<*") >= 0)
		{
			dataStartPos = parseResult.indexOf("<*") + 2;
			dataEndPos = parseResult.indexOf("*>") - 2;
			fullDataDeclaration = parseResult.substring(dataStartPos - 2, dataEndPos + 4);
			fieldDef = parseResult.substring(dataStartPos, dataEndPos + 2);
			fieldData = "";
			attributes = fieldDef.split("\\^");
			fieldName = attributes[0];
			BigDecimal bd;
			DecimalFormat df;
			double dbl;
			float flt;
			int integer;

			if (attributes.length >= 2)
			{
				fieldFormat = attributes[1];
			} else
			{
				fieldFormat = "";
			}

			try
			{

				if (variables.containsKey(fieldName))
				{
					parseResult = parseResult.replace(fullDataDeclaration, variables.get(fieldName).toString());
				} else
				{

					fieldColumn = rs.findColumn(fieldName);
					if (fieldColumn >= 0)
					{

						fieldType = rsMetaData.getColumnType(fieldColumn);

						switch (fieldType)
						{
						case java.sql.Types.VARCHAR:
							fieldData = JUtility.replaceNullStringwithBlank(rs.getString(fieldColumn));
							fieldData = fieldData.replace("(", "{");
							fieldData = fieldData.replace(")", "}");
							break;
						case java.sql.Types.NVARCHAR:
							fieldData = JUtility.replaceNullStringwithBlank(rs.getString(fieldColumn));
							fieldData = fieldData.replace("(", "{");
							fieldData = fieldData.replace(")", "}");
							break;
						case java.sql.Types.CHAR:
							fieldData = JUtility.replaceNullStringwithBlank(rs.getString(fieldColumn));
							fieldData = fieldData.replace("(", "{");
							fieldData = fieldData.replace(")", "}");
							break;
						case java.sql.Types.DATE:
							try
							{
								if (fieldFormat.equals(""))
									fieldFormat = "yyyy-MM-dd";
								Timestamp ts = rs.getTimestamp(fieldColumn);
								SimpleDateFormat formatter = new SimpleDateFormat(fieldFormat);
								fieldData = formatter.format(ts);

							} catch (Exception ex)
							{
								fieldData = fieldName + " " + ex.getMessage();
							}
							break;
						case java.sql.Types.TIMESTAMP:
							try
							{
								if (fieldFormat.equals(""))
									fieldFormat = "yyyy-MM-dd";
								Timestamp ts = rs.getTimestamp(fieldColumn);
								SimpleDateFormat formatter = new SimpleDateFormat(fieldFormat);
								fieldData = formatter.format(ts);

							} catch (Exception ex)
							{
								fieldData = fieldName + " " + ex.getMessage();
							}
							break;
						case java.sql.Types.DECIMAL:
							bd = rs.getBigDecimal(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(bd);
							break;
						case java.sql.Types.NUMERIC:
							bd = rs.getBigDecimal(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(bd);
							break;
						case java.sql.Types.BIGINT:
							integer = rs.getInt(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(integer);
							break;
						case java.sql.Types.INTEGER:
							integer = rs.getInt(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(integer);
							break;
						case java.sql.Types.FLOAT:
							flt = rs.getFloat(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(flt);
							break;
						case java.sql.Types.DOUBLE:
							dbl = rs.getDouble(fieldColumn);
							if (fieldFormat.equals(""))
								fieldFormat = "0000";
							df = new DecimalFormat(fieldFormat);
							fieldData = df.format(dbl);
							break;
						default:
							String typeName = JDBCType.valueOf(fieldType).getName();
							fieldData = "Unhandled data type ["+typeName+"]" + String.valueOf(fieldType);
							break;
						}
						parseResult = parseResult.replace(fullDataDeclaration, fieldData);
					}
				}

			} catch (SQLException e)
			{
				parseResult = parseResult.replace(fullDataDeclaration, fieldName + " not found!");
			}
		}

		return parseResult;
	}

}
