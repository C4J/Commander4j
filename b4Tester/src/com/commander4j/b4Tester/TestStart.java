package com.commander4j.b4Tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.Utility;

public class TestStart
{

	public static TestConfig tcfg = new TestConfig();
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((TestStart.class));

	public static void main(String[] args)

	{
		// Clean down the test environment by removing any files already in the
		// input or output folders.

		printConsole(1,"Ensure that middleware4j is running in ["+Common.rootPath+"]");
		printConsole(1,"");
		printConsole(1,"Press any key to begin test.");
		Utility.pressAnykey();
		
		topLevel(new File(Common.inputPath));
		topLevel(new File(Common.outputPath));

		Iterator<Map.Entry<String, Test>> testSet = Common.testRig.entrySet().iterator();
		printConsole(1, "Test Started");

		while (testSet.hasNext())
		{
			Map.Entry<String, Test> entryTest = testSet.next();

			Test currentTest = Common.testRig.get(entryTest.getKey());
			printConsole(1, "");
			printConsole(5, "------------------------------------------------");
			
			printConsole(1, "Test Id     : " + currentTest.id);
			printConsole(1, "Description : " + currentTest.description);
			printConsole(3, "inputPath   : " + currentTest.input.inputPath);
			printConsole(3, "samplePath  : " + currentTest.input.samplePath);

			printConsole(5, "    ------------------------------------------------");
			printConsole(1, "    Input Id           : " + currentTest.input.id);
			printConsole(1, "    Input Type         : " + currentTest.input.type);
			printConsole(1, "    Input Description  : " + currentTest.input.description);
			printConsole(3, "    Sample Path        : " + currentTest.input.samplePath);
			printConsole(3, "    Input Path         : " + currentTest.input.inputPath);
			printConsole(5, "    ------------------------------------------------");

			Iterator<Map.Entry<String, TestInputFile>> inputFileSet = currentTest.input.inputFiles.entrySet().iterator();

			int copyCount = 0;
			while (inputFileSet.hasNext())
			{
				Map.Entry<String, TestInputFile> inputFile = inputFileSet.next();

				TestInputFile currentInputFile = currentTest.input.inputFiles.get(inputFile.getKey());

				printConsole(3, "        Sample File [" + currentInputFile.id + "] - " + currentInputFile.sampleFilename);
				printConsole(3, "        Input File  [" + currentInputFile.id + "] - " + currentInputFile.inputFilename);

				if (copyFile(currentInputFile.sampleFilename, currentInputFile.inputFilename))
				{
					copyCount++;
				}
			}
			printConsole(1, "        " + String.valueOf(copyCount) + " files copied.");
			printConsole(2, "        Pausing for output files to be created.");
			//Utility.pause(copyCount);

			Iterator<Map.Entry<String, TestOutput>> outputSet = currentTest.output.entrySet().iterator();

			while (outputSet.hasNext())
			{
				Map.Entry<String, TestOutput> entryOutput = outputSet.next();

				TestOutput currentOutput = currentTest.output.get(entryOutput.getKey());

				printConsole(5, "    ------------------------------------------------");
				printConsole(1, "    Output Id          : " + currentOutput.id);
				printConsole(1, "    Description        : " + currentOutput.description);
				printConsole(1, "    Output Type        : " + currentOutput.type);
				printConsole(3, "    Reference Path     : " + currentOutput.referencePath);
				printConsole(3, "    Output Path        : " + currentOutput.outputPath);
				printConsole(4, "    Compare Method     : " + currentOutput.compareMethod);
				printConsole(4, "    Exclude List       : " + currentOutput.excludeList);
				printConsole(5, "    ------------------------------------------------");

				Iterator<Map.Entry<String, TestOutputFile>> outputFileSet = currentTest.output.get(currentOutput.id).outputFiles.entrySet().iterator();

				int compared = 0;

				while (outputFileSet.hasNext())
				{
					Map.Entry<String, TestOutputFile> outputFile = outputFileSet.next();

					TestOutputFile currentOutputFile = currentTest.output.get(currentOutput.id).outputFiles.get(outputFile.getKey());

					printConsole(4, "        Reference File [" + currentOutputFile.id + "] - " + currentOutputFile.referenceFilename);
					printConsole(4, "        Output File    [" + currentOutputFile.id + "] - " + currentOutputFile.outputFilename);
					if (compareFiles(currentOutput.compareMethod, currentOutput.excludeList, currentOutputFile.referenceFilename, currentOutputFile.outputFilename))
					{
						compared++;
					}
				}
				printConsole(1, "        " + String.valueOf(compared) + " files compared.");

			}
			printConsole(5, "    ------------------------------------------------");

			printConsole(5, "------------------------------------------------");
		}
		printConsole(1, "");
		printConsole(1, "Test Completed Successfully");
		
		System.exit(0);
	}

	public static void topLevel(File dir)
	{
		File test2 = new File(dir.getAbsolutePath() + File.separator + "dirClean.ok");

		if (test2.exists())
		{
			printConsole(5, "   Cleaning " + dir.getAbsolutePath());
			dirTree(dir);
		}
		else
		{
			printConsole(0, "\n*ABORT - Top Level directory must contain file called " + test2.getAbsolutePath() + " to indicate its safe to clean.\n");
			abort();
		}
	}

	public static void dirTree(File dir)
	{

		File[] subdirs = dir.listFiles();
		for (File subdir : subdirs)
		{
			if (subdir.isDirectory())
			{
				dirTree(subdir);
			}
			else
			{
				doFile(subdir);
			}
		}
	}

	public static void doFile(File file)
	{
		if (file.getAbsolutePath().endsWith(".DS_Store") == false)
		{
			if (file.getAbsolutePath().endsWith("dirClean.ok") == false)
			{

				if (FileUtils.deleteQuietly(file))
				{
					printConsole(5, "       Deleting " + file.getAbsolutePath() + " ...ok");
				}
				else
				{
					printConsole(5, "       Deleting " + file.getAbsolutePath() + " ...error");
					abort();
				}
			}
		}
	}

	public static void printConsole(int level, String debug)
	{
		if (level <= Common.verboseLevel)
		{
			System.out.println(debug);
		}

	}

	public static boolean copyFile(String from, String to)
	{
		boolean result = false;

		File srcFile = new File(from);
		File destFile = new File(to);

		try
		{
			FileUtils.copyFile(srcFile, destFile);
			printConsole(2, "        copy " + srcFile.getName() + "] ...ok");
			result = true;

		}
		catch (IOException e)
		{
			printConsole(0, "        copy " + srcFile.getName() + "] ...error " + e.getMessage());
			abort();
		}

		return result;
	}

	public static boolean compareFiles(String method, String ignore, String from, String to)
	{
		boolean result = true;

		LinkedList<String> toIgnore = new LinkedList<>();

		if (ignore.contentEquals("") == false)
		{
			toIgnore = Common.excludeList.get(ignore);
		}

		File file1 = new File(from);
		File file2 = new File(to);

		if (file1.exists())
		{
			int maxWait=20;
			while (file2.exists()==false)
			{
				Utility.delay(1000);

				maxWait--;
				
				if (maxWait==0)
				{
					break;
				}
			}
			
			if (file2.exists())
			{
				switch (method)
				{
				case "binary":

					try
					{
						boolean isTwoEqual = FileUtils.contentEquals(file1, file2);
						if (isTwoEqual)
						{
							printConsole(2, "        Comparing by [" + method + "] " + file1.getName() + "] with [" + file2.getName() + " ] ...ok ");
							result = true;
						}
						else
						{
							printConsole(0, "        Comparing by [" + method + "] " + file1.getName() + "] with [" + file2.getName() + " ] ...not the same ");
							result = false;
							abort();
						}

					}
					catch (IOException e)
					{
						printConsole(0, "        Comparing by [" + method + "] " + file1.getName() + "] with [" + file2.getName() + " ] ...error " + e.getMessage());
						result = false;
						abort();
					}
					break;

				case "line":

					try (BufferedReader reader1 = new BufferedReader(new FileReader(from)); BufferedReader reader2 = new BufferedReader(new FileReader(to)))
					{

						HashSet<String> hashset = new HashSet<String>();

						String s = null;
						int seq = 1;
						while ((s = reader1.readLine()) != null)
						{
							hashset.add(String.valueOf(seq) + " - " + s);
							seq++;
						}

						seq = 1;
						while ((s = reader2.readLine()) != null)
						{
							if (hashset.contains(String.valueOf(seq) + " - " + s) == false)
							{
								boolean canBeIgnored = false;
								for (int x = 0; x < toIgnore.size(); x++)
								{
									if (s.contains(toIgnore.get(x)))
									{
										canBeIgnored = true;
									}
								}
								if (canBeIgnored == false)
								{
									printConsole(0, "        Line mismatch  [" + String.valueOf(seq) + " - " + s + "] in " + new File(to).getName());
									result = false;
								}
							}
							seq++;
						}
						if (result == false)
						{
							printConsole(0, "        Compare by [" + method + "] reference file " + new File(from).getName() + " with ouput file " + new File(to).getName() + "] ...not the same");
							abort();
						}

						hashset = null;
					}
					catch (IOException e)
					{
						System.out.println(e);
					}

					break;

				default:
					result = false;
					printConsole(0, "        Invalid compare method  [" + method + "]");
					abort();
				}
			}
			else
			{
				result = false;
				printConsole(0, "        compare output [" + new File(to).getName() + "] ...does not exist");
				abort();
			}
		}
		else
		{
			result = false;
			printConsole(0, "        compare reference [" + new File(from).getName() + "] ...does not exist");
			abort();
		}
		return result;
	}
	
	public static void abort()
	{
		printConsole(0,"***ERROR***");
		System.out.println("\007");
		System.exit(1);
	}

}
