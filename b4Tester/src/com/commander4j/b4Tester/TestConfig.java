package com.commander4j.b4Tester;

import java.io.File;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JXMLDocument;
import com.commander4j.util.Utility;

public class TestConfig
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((TestConfig.class));

	public TestConfig()
	{
		String configFile = System.getProperty("user.dir") + java.io.File.separator + "xml" + java.io.File.separator + "config" + java.io.File.separator + "config.xml";
		loadTests(configFile);
	}

	public void loadTests(String filename)
	{
		int testSeq = 1;

		JXMLDocument doc = new JXMLDocument(filename);

		Common.testName = doc.findXPath("//testConfig/description");
		Common.verboseLevel = Integer.valueOf(doc.findXPath("//testConfig/verboseLevel"));
		Common.rootPath = fixPath(doc.findXPath("/testConfig/tests/rootPath"));
		Common.outputPath = fixPath(Common.rootPath + doc.findXPath("/testConfig/tests/outputPath"));
		Common.inputPath = fixPath(Common.rootPath + doc.findXPath("/testConfig/tests/inputPath"));
		Common.samplePath = fixPath(Common.rootPath + doc.findXPath("/testConfig/tests/samplePath"));
		Common.referencePath = fixPath(Common.rootPath + doc.findXPath("/testConfig/tests/referencePath"));
		

		
		int ignoreSeq = 1;
		while (doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/@id").trim()!= "")
		{
			if (doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/@id").trim()!= "")
			{
				String ignoreListId = doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/@id").trim();
				

				int ignoreTextSeq = 1;
				LinkedList<String> ignoreText = new LinkedList<String>();
				
				while (doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/ignore["+String.valueOf(ignoreTextSeq) +"]").trim()!= "")
				{
					
					if (doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/ignore["+String.valueOf(ignoreTextSeq) +"]").trim()!= "")
					{

						String ingnoreText = doc.findXPath("//testConfig/tests/excludes/list[" + String.valueOf(ignoreSeq) + "]/ignore["+String.valueOf(ignoreTextSeq) +"]").trim();
						ignoreText.addLast(ingnoreText);
						
					}
					ignoreTextSeq++;
				}
				
				if (ignoreText.size()>0)
				{
					Common.excludeList.put(ignoreListId, ignoreText);
				}
			}
			ignoreSeq++;
		}
		///

		if (Common.logDir.equals(""))
		{
			Common.logDir = System.getProperty("user.dir") + java.io.File.separator + "xml" + java.io.File.separator + "config" + java.io.File.separator + "log4j2.xml";
			Utility.initLogging(Common.logDir);
		}

		Common.testRig.clear();

		while (doc.findXPath("//testConfig/tests/test[" + String.valueOf(testSeq) + "]/@enabled").trim() != "")
		{
			String testEnabled = doc.findXPath("//testConfig/tests/test[" + String.valueOf(testSeq) + "]/@enabled").trim();

			if (testEnabled.equals("Y"))
			{

				Test test = new Test();

				test.enabled = testEnabled;
				test.id = doc.findXPath("//testConfig/tests/test[" + String.valueOf(testSeq) + "]/@id").trim();
				test.description = doc.findXPath("//testConfig/tests/test[" + String.valueOf(testSeq) + "]/@description").trim();

				int inputSeq = 1;

				if (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@id").trim() != "")
				{

					test.input.id = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@id").trim();
					test.input.description = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@description").trim();
					test.input.type = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/@type").trim();
					test.input.samplePath = fixPath(Common.samplePath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/samplePath").trim());
					test.input.inputPath = fixPath(Common.inputPath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputPath").trim());

					int inputFileSeq = 1;

					while (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputs/file[" + String.valueOf(inputFileSeq) + "]/@id").trim() != "")
					{
						if (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputs/file[" + String.valueOf(inputFileSeq) + "]/@id").trim() != "")
						{
							TestInputFile tif = new TestInputFile();
							tif.id = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputs/file[" + String.valueOf(inputFileSeq) + "]/@id").trim();
							tif.inputFilename = test.input.inputPath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputs/file[" + String.valueOf(inputFileSeq) + "]").trim();
							tif.sampleFilename = test.input.samplePath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/input[" + String.valueOf(inputSeq) + "]/inputs/file[" + String.valueOf(inputFileSeq) + "]").trim();

							test.input.inputFiles.put(tif.id, tif);
							inputFileSeq++;
						}
					}

					int outputSeq = 1;

					while (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@id").trim() != "")
					{
						if (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@id").trim() != "")
						{
							TestOutput output = new TestOutput();

							output.id = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@id").trim();
							output.description = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@description").trim();
							output.type = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/@type").trim();
							output.referencePath = fixPath(Common.referencePath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/referencePath").trim());
							output.outputPath = fixPath(Common.outputPath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputPath").trim());
							output.compareMethod = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/@compareMethod").trim();
							output.excludeList = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/@excludeList").trim();

							test.output.put(output.id, output);

							int outputFileSeq = 1;

							while (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/file[" + String.valueOf(outputFileSeq) + "]/@id").trim() != "")
							{
								if (doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/file[" + String.valueOf(outputFileSeq) + "]/@id").trim() != "")
								{
									TestOutputFile tof = new TestOutputFile();
									tof.id = doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/file[" + String.valueOf(outputFileSeq) + "]/@id").trim();
									tof.outputFilename = output.outputPath + doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/file[" + String.valueOf(outputFileSeq) + "]").trim();
									tof.referenceFilename = output.referencePath
											+ doc.findXPath("/testConfig/tests/test[" + String.valueOf(testSeq) + "]/output[" + String.valueOf(outputSeq) + "]/outputs/file[" + String.valueOf(outputFileSeq) + "]").trim();

									test.output.get(output.id).outputFiles.put(tof.id, tof);
									outputFileSeq++;
								}
							}

							outputSeq++;
						}
					}
				}

				Common.testRig.put(test.id, test);
			}

			testSeq++;

		}

	}

	private String fixPath(String path)
	{
		String result = Utility.replaceNullStringwithBlank(path);

		if (result.length() > 0)
		{
			if (result.endsWith(File.separator) == false)
			{
				result = result + File.separator;
			}
		}

		result = result.replace(File.separator + File.separator, File.separator);

		return result;
	}

}
