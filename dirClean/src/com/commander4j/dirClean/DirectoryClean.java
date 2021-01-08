package com.commander4j.dirClean;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DirectoryClean
{
	
	private static int deleted = 0;
	
	public static void main(String[] args)
	{

		if (args.length == 0)
		{
			System.out.println("Syntax: dirClean filename.clean\n");
			System.out.println("\n");
			System.out.println("Parameter file contains a list of folders to clean (1 folder per row)");
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

					// Iterate the result to print each line of the file.
					for (String line : contents)
					{

						if (line.equals("") == false)
						{
							//System.out.println("Invoking dirClean on " + line);
							topLevel(new File(line));

						}
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
		System.out.println(" "+String.valueOf(deleted)+ " file(s) deleted.");
	}

	public static void topLevel(File dir)
	{
		File test2 = new File(dir.getAbsolutePath() + File.separator + "dirClean.ok");

		if (test2.exists())
		{

			//System.out.println("\n   Cleaning " + dir.getAbsolutePath() + "\n");
			dirTree(dir);

		}
		else
		{
			System.out.println("\n*ABORT - Top Level directory must contain file called " + test2.getAbsolutePath() + " to indicate its safe to clean.\n");
			System.exit(1);
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

				//System.out.print("       Deleting " + file.getAbsolutePath());
				if (FileUtils.deleteQuietly(file))
				{
					System.out.print(".");
					deleted++;
				}
				else
				{
					System.out.print("\nError deleting "+file.getName()+"\n");
				}
			}
		}
	}
}
