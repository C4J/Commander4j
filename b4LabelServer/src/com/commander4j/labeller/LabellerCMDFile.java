package com.commander4j.labeller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.commander4j.util.JUtility;

public class LabellerCMDFile
{

	public HashMap<String, String> variables = new HashMap<String, String>();
	public HashMap<String, String> fileData = new HashMap<String, String>();
	public ArrayList<String> labelIndex = new ArrayList<String>();
	public LinkedList<LabellerCMDLine> commandLines = new LinkedList<LabellerCMDLine>();
	private String Delimiter = "";

	public HashMap<String, String> getVariables()
	{
		return variables;
	}

	public void addLine(LabellerCMDLine line)
	{
		commandLines.addLast(line);

		labelIndex.add(line.label);
	}

	public int getLinefromLabel(String label)
	{
		int result = -1;
		result = labelIndex.indexOf(label);

		return result;
	}

	public void setDelimiter(String del)
	{
		Delimiter = del;
	}

	public String removeDelimitors(String var)
	{
		String result = var;

		if (Delimiter.equals("") == false)
		{
			try
			{
				String[] valuesInQuotes = StringUtils.substringsBetween(var, Delimiter, Delimiter);
				result = "";
				for (int x = 0; x < valuesInQuotes.length; x++)
				{
					result = result + valuesInQuotes[x];

					if (x < valuesInQuotes.length - 1)
					{
						result = result + ",";
					}
				}
			} catch (Exception ex)
			{
				result = var;
			}
		}

		return result;
	}

	public String getValueAtLine(int line)
	{
		String result = commandLines.get(line).val;

		if (commandLines.get(line).command.equals("VARIABLE") == false)
		{
			if (Delimiter.equals("") == false)
			{
				result = removeDelimitors(result);
			}
		}

		result = replaceVariables(result);

		result = replaceFileData(result);

		result = execFunctions(result);
		
		result = result.replace("±", ",");

		return result;
	}

	public static String execFunctions(String VAL)
	{
		String result = VAL;

		String func = "EXTRACT_DATE";

		while (result.contains(func))
		{
			int functionNameStartPos = VAL.indexOf(func); // Start of function name

			String beforeFunction = VAL.substring(0, functionNameStartPos); // String before function name

			int functionNameEndPos = functionNameStartPos + func.length(); // End of Function name

			String stringAfterFunctionName = VAL.substring(functionNameEndPos); // String after Function name

			int closingBracketPosition = stringAfterFunctionName.indexOf(")"); // Position of end of function

			String allParameters = stringAfterFunctionName.substring(1, closingBracketPosition);

			String afterFunction = stringAfterFunctionName.substring(closingBracketPosition + 1);

			String[] parameterArray = allParameters.split(",");

			String inputDateString = parameterArray[0];

			DateFormat inputDateFormat = new SimpleDateFormat(parameterArray[1], Locale.ENGLISH);

			try
			{
				Date inputDate;

				inputDate = inputDateFormat.parse(inputDateString);

				DateFormat df = new SimpleDateFormat(parameterArray[2]);

				String outputDate = df.format(inputDate);

				result = beforeFunction + outputDate + afterFunction;

				System.out.println("EXTRACT_DATE: " + result);

			} catch (Exception ex)
			{
				result = "error " + ex.getMessage();
			}

		}

		func = "REPLACE";

		while (result.contains(func))
		{
			int functionNameStartPos = VAL.indexOf(func); // Start of function name

			String beforeFunction = VAL.substring(0, functionNameStartPos); // String before function name

			int functionNameEndPos = functionNameStartPos + func.length(); // End of Function name

			String stringAfterFunctionName = VAL.substring(functionNameEndPos); // String after Function name

			int closingBracketPosition = stringAfterFunctionName.indexOf(")"); // Position of end of function

			String allParameters = stringAfterFunctionName.substring(1, closingBracketPosition);

			String afterFunction = stringAfterFunctionName.substring(closingBracketPosition + 1);

			String[] parameterArray = allParameters.split(",");

			String inputString = "";
			String findString = "";
			String replaceString = "";

			try
			{
				inputString = parameterArray[0];
			} catch (Exception ex)
			{
				inputString = "";
			}

			try
			{
				findString = parameterArray[1];
			} catch (Exception ex)
			{
				findString = "";
			}

			try
			{
				replaceString = parameterArray[2];
			} catch (Exception ex)
			{
				replaceString = "";
			}

			try
			{
				String outputString = inputString.replaceAll(findString, replaceString);

				result = beforeFunction + outputString + afterFunction;

				System.out.println("REPLACE: " + result);

			} catch (Exception ex)
			{
				result = "error " + ex.getMessage();
			}

		}

		func = "PADLEFT";

		while (result.contains(func))
		{
			int functionNameStartPos = VAL.indexOf(func); // Start of function name

			String beforeFunction = VAL.substring(0, functionNameStartPos); // String before function name

			int functionNameEndPos = functionNameStartPos + func.length(); // End of Function name

			String stringAfterFunctionName = VAL.substring(functionNameEndPos); // String after Function name

			int closingBracketPosition = stringAfterFunctionName.indexOf(")"); // Position of end of function

			String allParameters = stringAfterFunctionName.substring(1, closingBracketPosition);

			String afterFunction = stringAfterFunctionName.substring(closingBracketPosition + 1);

			String[] parameterArray = allParameters.split(",");

			String inputString = "";
			String padSize = "";
			String padChar = "";

			try
			{
				inputString = parameterArray[0];
			} catch (Exception ex)
			{
				inputString = "";
			}

			try
			{
				padSize = parameterArray[1];
			} catch (Exception ex)
			{
				padSize = String.valueOf(inputString.length()).trim();
			}

			try
			{
				padChar = parameterArray[2];
			} catch (Exception ex)
			{
				padChar = "";
			}

			try
			{
				int padS = Integer.valueOf(padSize);

				while (inputString.length() < padS)
				{
					inputString = padChar + inputString;
				}

				result = beforeFunction + inputString + afterFunction;

				System.out.println("PADLEFT : " + result);

			} catch (Exception ex)
			{
				result = "error " + ex.getMessage();
			}

		}

		return result;
	}

	public String replaceVariables(String var)
	{
		String result = var;

		Set<Entry<String, String>> set = variables.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, String> mentry = (Map.Entry<String, String>) iterator.next();
			result = StringUtils.replaceOnce(result, mentry.getKey().toString(), mentry.getValue().toString().replace(",", "±"));
		}

		return result;
	}

	public String replaceFileData(String var)
	{
		String result = var;

		Set<Entry<String, String>> set = fileData.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, String> mentry = (Map.Entry<String, String>) iterator.next();
			result = StringUtils.replaceOnce(result, mentry.getKey().toString(), mentry.getValue().toString().toString().replace(",", "±"));
		}

		return result;
	}

	public Boolean loadFile(LabellerProperties prop, String filename)
	{
		Boolean result = true;

		filename = System.getProperty("user.dir") + java.io.File.separator + "labeller_cmd" + java.io.File.separator + filename;
		System.out.println("");
		System.out.println("[" + prop.getId() + "]" + " loadFile [" + filename + "]");
		System.out.println("");
		try
		{
			List<String> strings = Files.readAllLines(FileSystems.getDefault().getPath(filename), Charset.forName("UTF-8"));
			for (int x = 0; x < strings.size(); x++)
			{
				String sourceLine = strings.get(x);
				if (sourceLine.trim().equals("") == false)
				{
					if (sourceLine.startsWith("/*") == false)
					{
						sourceLine = sourceLine + JUtility.padSpace(50);
						LabellerCMDLine cmdLine = new LabellerCMDLine();
						cmdLine.label = sourceLine.substring(0, 18).trim();
						cmdLine.command = sourceLine.substring(18, 48).trim();
						cmdLine.val = sourceLine.substring(48).trim();

						addLine(cmdLine);

						System.out.println("[" + prop.getId() + "]" + " Line > " + cmdLine);
					}
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			result = false;
		}
		System.out.println("");
		getLinefromLabel("Start");

		return result;
	}

}
