package com.commander4j.label;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import com.commander4j.autolab.AutoLab;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.utils.JUtility;


public class Label
{

	private HashMap<String, String> varLabelDEFINEs = new HashMap<String, String>();
	private HashMap<String, String> expanded_variables = new HashMap<String, String>();
	private String uuid = "";
	private static String incorrectNoParams = " [Incorrect number of parameters]";
	private static String incorrectDateTimeFormat = " [Incorrect date/time format]";
	private JUtility utils = new JUtility();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((Label.class));
	
	public String process(String uuid)
	{
		this.uuid=uuid;
		String labelData = "";
		String fname = AutoLab.getDataSet_Field(uuid, "REPORT_FILENAME");
		String template = System.getProperty("user.dir") + File.separator + "labels" + File.separator + AutoLab.getDataSet_Field(uuid, "REPORT_FILENAME");
		File templateFile = new File(template);
		appendNotification("Reading Label Layout ["+fname+"].");
		labelData = getTemplate(templateFile);
		expanded_variables = expandVariables(varLabelDEFINEs);
		optimiseEAN128();

		labelData = doParseFields(labelData);
		labelData = doParseFunctions(labelData);
		return labelData;
	}
	
	public void appendNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).appendNotification(message);
	}
	
	public void setNotification(String message)
	{
		((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).setNotification(message);
	}

	public String getTemplate(File aFile)
	{
		
		StringBuilder contents = new StringBuilder();
		Boolean suppressEOL = false;

		Boolean commandMode = false;

		try
		{
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try
			{
				String line = null;
				while ((line = input.readLine()) != null)
				{
					line = line.trim();
					if ((line.startsWith("/*") == false) & (line.length() > 0))
					{
						commandMode = false;
						
						if (line.startsWith("DEFINE BARCODE "))
						{
							commandMode = true;
							String parse = line.substring(15);
							String delims = "[ ]+";
							String[] tokens = parse.split(delims);

							varLabelDEFINEs.put(tokens[0], tokens[1]);
						}
						
						if (line.startsWith("SUPPRESS EOL"))
						{
							commandMode = true;
							suppressEOL = true;
						}

						
						if (commandMode == false)
						{

							line = doParseFields(line);
							line = doParseFunctions(line);	
							
							contents.append(line);

							if (suppressEOL == false)
							{
								contents.append(System.getProperty("line.separator"));
							}
						}
					}
				}
			} finally
			{
				input.close();
			}
		} catch (IOException ex)
		{

		}

		logger.debug("\r\n\r\n"+contents.toString());
		return contents.toString();
	}
	
	public String doParseFields(String inputLine)
	{
		String parseResult = inputLine;
		String fieldDef = "";
		String fieldName = "";

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
			attributes = fieldDef.split("\\^");
			fieldName = attributes[0];

			parseResult = parseResult.replace(fullDataDeclaration, AutoLab.getDataSet_Field(uuid, fieldName));
		}

		return parseResult;
	}
	
	public String doParseFunctions(String inputLine)
	{
		String parseResult = inputLine;

		// Supported Expressions using format

		String[] Functions = new String[]
		{ "<SUBTR_LPAD(", "<DATETIME(", "<SUBSTRING(", "<LEFT(", "<RIGHT(", "<PADLEFT(", "<PADRIGHT(", "<UPPERCASE(", "<LOWERCASE(", "<TRIM(", "<LTRIM(", "<RTRIM(", "<TIMESTAMP(", "<USERNAME(", "<VERSION(", "<IIF(", "<EXPIRYDATE(", "<PRODDATE(","<PALLET_WEIGHT_TEXT(", "<PALLET_WEIGHT_BARCODE(" };

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
					result = target.substring(start - 1, start + end - 1);
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
					result = target.substring(0, end);
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
					result = target.substring(target.length() - start, target.length());
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
					result = System.getProperty("user.name");
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
					result = "1.00";
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

						
						expirydate = utils.convertStringToTimestamp("dd-MMM-yyyy HH:mm:ss", AutoLab.getDataSet_Field(uuid, "EXPIRY_DATE"));
						


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

						dateOfManufacture = utils.convertStringToTimestamp("dd-MMM-yyyy HH:mm:ss", AutoLab.getDataSet_Field(uuid, "DATE_OF_MANUFACTURE"));

						if (dateOfManufacture == null)
						{
							// If the date is null then return a string of spaces the same size as the format spec.
							result = utils.padSpace(params[0].length());
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

						fielddatetime = utils.convertStringToTimestamp("dd MMM yyyy HH:mm:ss", AutoLab.getDataSet_Field(uuid,fieldname));

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
	
	public HashMap<String, String> expandVariables(HashMap<String, String> hm)
	{
		HashMap<String, String> result = new HashMap<String, String>();

		Set<Entry<String, String>> set = hm.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();

		String temp = "";

		while (i.hasNext())
		{
			Map.Entry<String, String> me = i.next();

			// ++++++++++++
			temp = doParseFields((String) me.getValue());
			temp = doParseFunctions(temp);

			result.put((String) me.getKey(), temp);

		}

		return result;
	}

	
	public void optimiseEAN128()
	{

		Set<Entry<String, String>> set = expanded_variables.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();
		String key = "";
		String originalBarcode = "";
		String optimisedBarcode = "";
		String currentMode = "";
		String newMode = "";
		String initialFNCReqd = "^";
		String pair = "";
		int start = 0;
		int end = 0;
		int size = 0;

		while (i.hasNext())
		{
			Map.Entry<String, String> me = i.next();

			key = (String) me.getKey();
			if (key.startsWith("BARCODE") == true)
			{
				optimisedBarcode = "";
				currentMode = "";
				originalBarcode = (String) me.getValue();
				size = originalBarcode.length();

				for (int x = 0; x < size; x = x + 2)
				{
					start = x;
					end = start + 2;
					if (end > size)
					{
						end = size;
					}
					pair = originalBarcode.substring(start, end);

					if (pair.startsWith("^") == false)
					{

						if ((pair.length() < 2) | (pair.endsWith("^")))
						{
							// Odd (single char at end of string //
							newMode = "alphanumeric";
						} else
						{
							try
							{
								Integer.valueOf(pair);
								newMode = "numeric";
							} catch (Exception ex)
							{
								// Not numeric //
								newMode = "alphanumeric";
							}

						}
						if (newMode.equals(currentMode) == false)
						{
							if (optimisedBarcode.equals(""))
							{
								initialFNCReqd = "^";
							} else
							{
								initialFNCReqd = "";
							}
							if (newMode.equals("numeric"))
							{
								optimisedBarcode = optimisedBarcode + expanded_variables.get("CODEC") + initialFNCReqd;
							}
							if (newMode.equals("alphanumeric"))
							{
								optimisedBarcode = optimisedBarcode + expanded_variables.get("CODEB") + initialFNCReqd;
							}

							currentMode = newMode;
						}
						optimisedBarcode = optimisedBarcode + pair;
					} else
					{
						optimisedBarcode = optimisedBarcode + "^";
						x--;
					}
				}

				optimisedBarcode = optimisedBarcode.replace("^", expanded_variables.get("FNC1"));

				expanded_variables.put(key, optimisedBarcode);
				AutoLab.setDataSet_FieldValue(uuid, key, optimisedBarcode);
			}
		}
	}

	
}
