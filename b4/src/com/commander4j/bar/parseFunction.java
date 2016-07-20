package com.commander4j.bar;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBControl;

public class parseFunction {
	private String hostID;
	private String sessionID;
	private JDBControl ctrl;
	private String expiryDateMode = "";
	private static String incorrectNoParams = " [Incorrect number of parameters]";
	private static String incorrectDateTimeFormat = " [Incorrect date/time format]";
	ResultSetMetaData rsMetaData;
	ResultSet rs;

	public parseFunction(String host, String session) {
		setHostID(host);
		setSessionID(session);

		ctrl = new JDBControl(getHostID(), getSessionID());

		expiryDateMode = ctrl.getKeyValue("EXPIRY DATE MODE");
	}

	public void init(ResultSet rs)
	{
		try
		{
			this.rs = rs;
			rsMetaData = rs.getMetaData();
		} catch (SQLException e)
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
		{"<DATETIME(", "<SUBSTRING(", "<LEFT(", "<RIGHT(", "<PADLEFT(", "<PADRIGHT(", "<UPPERCASE(", "<LOWERCASE(", "<TRIM(", "<LTRIM(", "<RTRIM(", "<TIMESTAMP(", "<USERNAME(", "<VERSION(", "<IIF(", "<EXPIRYDATE(" };

		// For each expression above
		for (int x = 0; x < Functions.length; x++)
		{

			while (parseResult.indexOf(Functions[x].toUpperCase()) >= 0)
			{
				// Can we find the expression in the data passed to the function
				// ?
				int functionStartPos = inputLine.toUpperCase().indexOf(Functions[x].toUpperCase());
				int bracketStartPos = -1;
				int bracketEndPos = -1;
				String functionName = "";
				String paramString = "";
				String params[];
				String fullFunctionDeclaration = "";

				// Was the function name found ?
				if (functionStartPos >= 0)
				{

					bracketStartPos = functionStartPos + Functions[x].length();

					// If yes scan for parameters
					for (int y = bracketStartPos; y < inputLine.length(); y++)
					{
						// Check for trailing ) which indicates end of
						// parameters
						if (inputLine.substring(y, y + 1).equals(")"))
						{
							bracketEndPos = y;
							functionName = inputLine.substring(functionStartPos + 1, functionStartPos + Functions[x].length() - 1).toUpperCase();
							fullFunctionDeclaration = inputLine.substring(functionStartPos, bracketEndPos + 2);
							paramString = inputLine.substring(bracketStartPos, bracketEndPos);
							params = paramString.split(",");
							inputLine = inputLine.replace(fullFunctionDeclaration, executeFunction(functionName, params));
							parseResult = inputLine;
							break;
						}
					}
				}
			}
		}
		return parseResult;
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
					result = target.substring(start - 1, start + end - 1);
				} else
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
					result = target.substring(0, end);
				} else
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
					result = target.substring(target.length() - start, target.length());
				} else
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
				} else
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
				} else
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
				} else
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
				} else
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
				} else
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
					} else
					{
						result = "";
					}
				} else
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
					} else
					{
						result = "";
					}
				} else
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
					} catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				} else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("USERNAME"))
			{
				if (params.length == 1)
				{
					result = System.getProperty("user.name");
				} else
				{
					result = functionName + incorrectNoParams;
				}
			}

			if (functionName.equals("VERSION"))
			{
				if (params.length == 1)
				{
					result = JVersion.getProgramVersionValue().toString();
				} else
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
					} else
					{
						result = params[3];
					}

				} else
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

						if (expiryDateMode.endsWith("BATCH"))
						{
							expirydate = rs.getTimestamp("expiry_date");
						} else
						{
							expirydate = rs.getTimestamp("sscc_expiry_date");
						}

						expirydate.setNanos(0);
						DateFormat dateFormat = new SimpleDateFormat(params[0]);

						result = dateFormat.format(expirydate);

					} catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				} else
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
						fielddatetime=null;
						format=null;
						format=null;

					} catch (Exception ex)
					{
						result = functionName + incorrectDateTimeFormat;
					}
				} else
				{
					result = functionName + incorrectNoParams;
				}

			}

		} catch (Exception ex)
		{
			result = functionName + " [" + ex.getMessage() + "]";
		}

		return result;
	}

}