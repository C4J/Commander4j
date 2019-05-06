package com.commander4j.dirFlatten;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DirectoryFlatten
{
	public static void main(String[] args)
	{
		String source = "";
		String destination = "";
		String mask = "";

		if (args.length == 0)
		{
			System.out.println("Syntax: dirFlatten filename.flatten\n");
			System.out.println("\n");
			System.out.println("Parameter file contains a list parameters :-");
			System.out.println("   Source folder which contains subfolders");
			System.out.println("   Destination folder");
			System.out.println("   Filename mask (*.jar)");
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
							destination = line;
							break;
						case 3:
							mask = line;
							break;
						}

						if (lineNumber == 3)
						{
							System.out.println("Invoking dirFlatten on " + line);
							topLevel(new File(source),destination,mask);
							System.out.println("\nComplete.");
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

	public static void topLevel(File sourceFile,String destination,String mask)
	{
		File test2 = new File(sourceFile.getAbsolutePath() + File.separator + "dirFlatten.ok");

		if (test2.exists())
		{

			System.out.println("\n   Flattening " + sourceFile.getAbsolutePath() + "\n");
			dirTree(sourceFile,destination,mask);

		}
		else
		{
			System.out.println("\n*ABORT - Top level source directory must contain file called " + test2.getAbsolutePath() + " to indicate its safe to flatten.\n");
			System.exit(1);
		}
	}

	public static void dirTree(File dir,String destination,String mask)
	{

		File[] subdirs = dir.listFiles();
		for (File subdir : subdirs)
		{
			if (subdir.isDirectory())
			{
				dirTree(subdir,destination,mask);
			}
			else
			{
				doFile(subdir,destination,mask);
			}
		}
	}

	public static void doFile(File file,String destination,String mask)
	{
		if (file.getAbsolutePath().endsWith(".DS_Store") == false)
		{

			if (file.getAbsolutePath().endsWith(mask) == true)
			{
				File destFile = new File(destination+File.separator+file.getName());
				try
				{
					FileUtils.deleteQuietly(destFile);
					System.out.print("Copying "+file.getName()+"...");
					FileUtils.copyFile(file, destFile, true);
					System.out.println("ok");
				}
				catch (IOException e)
				{
					System.out.println("error "+e.getMessage());
				}
				destFile=null;
			}
		}
	}
}
