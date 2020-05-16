package com.commander4j.manifest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Create
{

	public static String[] excludes;
	public static String source = "";
	public static String startClass = "";
	public static int outputLine = 0;
	public static File fout;
	public static FileOutputStream fos;
	public static BufferedWriter bw;

	public static void main(String[] args)
	{

		if (args.length == 0)
		{
			System.out.println("Syntax: b4Manifest manifest.name\n");
			System.out.println("\n");
			System.out.println("Parameter file contains a list parameters :-");
			System.out.println("   Project base folder");
			System.out.println("   Start Class");
			System.out.println("   jars to exlude");
			System.out.println("\n");
		}
		else
		{
			File test = new File(args[0]);
			if (test.exists())
			{
				try
				{
					List<String> contents = FileUtils.readLines(test, "utf-8");
					int lineNumber = 1;

					// Iterate the result to print each line of the file.
					for (String line : contents)
					{

						switch (lineNumber)
						{
						case 1:
							source = line;
							break;
						case 2:
							startClass = line;
							break;
						case 3:
							excludes = line.split(" ");
							break;
						default:
							break;
						}

						if (lineNumber == 3)
						{
							System.out.println("Invoking b4Manifest on " + line);
							topLevel(new File(source));
							System.out.println("\nComplete.");
							bw.close();
							break;
						}
						lineNumber++;
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

			}
			else
			{
				System.out.println(test.getAbsolutePath() + " not found.\n");
				System.out.println("\n");
			}
		}
	}

	public static void topLevel(File sourceFile)
	{

		dirTree(sourceFile);

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

			if (file.getAbsolutePath().endsWith(".jar") == true)
			{

				boolean include = true;
				if (excludes.length == 0)
				{
					include = true;
				}
				else
				{
					for (int x = 0; x < excludes.length; x++)
					{
						if (file.getAbsolutePath().endsWith(excludes[x]))
						{
							include = false;
						}
					}
				}

				if (include)
				{
					outputLine++;

					try
					{
						String prefix = "";

						if (outputLine == 1)
						{
							fout = new File("MANIFEST.MF");
							fos = new FileOutputStream(fout);
							bw = new BufferedWriter(new OutputStreamWriter(fos));

							prefix = "Manifest-Version: 1.0\n" + "Main-Class: "+startClass+"\n" + "Class-Path:";
						}

						bw.write(prefix + " " + file.getAbsolutePath().substring(source.length() + 1) + " ");
						bw.newLine();
					}
					catch (Exception e)
					{

						e.printStackTrace();
					}

				}

			}

		}
	}
}
