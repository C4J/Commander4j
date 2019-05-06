package com.commander4j.messageSplitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import com.commander4j.util.JXMLDocument;

public class ExtractXml2 extends Thread
{

	private String inputPath = "";
	private String outputPath = "";

	public ExtractXml2()
	{

		initLogging();

		JXMLDocument doc = new JXMLDocument("./xml/config/config.xml");

		inputPath = doc.findXPath("//config/inputPath");
		outputPath = doc.findXPath("//config/outputPath");

		if (inputPath.equals(""))
		{
			inputPath = "./interface/input/";
		}

		if (outputPath.equals(""))
		{
			outputPath = "./interface/output/";
		}

		inputPath = inputPath.replace("/", File.separator);
		inputPath = inputPath.replace("\\", File.separator);

		outputPath = outputPath.replace("/", File.separator);
		outputPath = outputPath.replace("\\", File.separator);

		if (inputPath.endsWith(File.separator) == false)
		{
			inputPath = inputPath + File.separator;
		}

		if (outputPath.endsWith(File.separator) == false)
		{
			outputPath = outputPath + File.separator;
		}

	}

	public void run()
	{
		startupInterface();
	}

	public void requestStop()
	{
		this.shutdown = true;
	}

	public void startupInterface()
	{
		this.shutdown = false;

		splitXML(inputPath, outputPath);
	}

	boolean shutdown = false;

	public boolean splitXML(String inputPath, String outputPath)
	{

		boolean writing = false;
		boolean create = false;
		boolean result = false;

		Path path;
		String outputFilename = "";
		String tempFilename = "";
		StandardOpenOption fileMode;

		while (shutdown == false)
		{
			com.commander4j.util.JWait.milliSec(100);

			File fl = new File(inputPath);

			File[] fileList = fl.listFiles();

			for (File file : fileList)
			{
				com.commander4j.util.JWait.milliSec(100);

				if (file.isDirectory() == false)
				{

					File xmlFile = file.getAbsoluteFile();

					if ((xmlFile.exists()) && (xmlFile.getName().toLowerCase().endsWith(".xml")))
					{
						System.out.println("Starting " + xmlFile.getName());

						com.commander4j.util.JWait.milliSec(100);

						try (BufferedReader br = new BufferedReader(new FileReader(xmlFile)))
						{
							String line = "";
							while ((line = br.readLine()) != null)
							{
								// System.out.println(line);

								if (line.trim().startsWith("<!--*START* filename "))
								{
									outputFilename = outputPath + line.trim().substring(21, line.length() - 3);
									outputFilename = outputFilename.replace("-->", "");
									tempFilename = outputPath + line.trim().substring(21, line.length() - 3) + ".tmp";
									tempFilename = tempFilename.replace("-->", "");

									path = Paths.get(outputFilename);

									line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

									create = true;
									writing = true;
								}

								if (line.trim().startsWith("<!--*END* filename"))
								{
									writing = false;
									FileUtils.moveFile(FileUtils.getFile(tempFilename), FileUtils.getFile(outputFilename));
								}

								if (writing)
								{

									path = Paths.get(tempFilename);
									System.out.println(tempFilename);

									if (create)
									{
										fileMode = StandardOpenOption.CREATE;
										FileUtils.deleteQuietly(new File(tempFilename));
										FileUtils.deleteQuietly(new File(outputFilename));
									}
									else
									{
										fileMode = StandardOpenOption.APPEND;
									}

									if (line.trim().equals("") == false)
									{
										try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, fileMode))
										{
											writer.write(line + "\n");
											writer.flush();
											writer.close();
											create = false;
										}
										catch (Exception e)
										{
											e.printStackTrace();
										}
									}

								}
							}
							result = true;
						}
						catch (Exception ex)
						{
							result = false;
						}
						System.out.println("Finished " + xmlFile.getName());

						com.commander4j.util.JWait.milliSec(100);

						FileUtils.deleteQuietly(new File(xmlFile.getAbsoluteFile() + ".complete"));
						try
						{
							System.out.println("Renaming " + xmlFile.getName() + " to " + xmlFile.getName() + ".complete");
							FileUtils.moveFile(FileUtils.getFile(xmlFile.getAbsoluteFile()), FileUtils.getFile(xmlFile.getAbsoluteFile() + ".complete"));
						}
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return result;
	}

	public void initLogging()
	{

		String filename = "." + File.separator + "xml" + File.separator + "config" + File.separator + "log4j2.xml";

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);
		context.setConfigLocation(file.toURI());
	}

	public static void main(String[] args)
	{
		ExtractXml2 ex = new ExtractXml2();
		ex.splitXML("./interface/input", "./interface/output/");
		System.exit(0);
	}

}