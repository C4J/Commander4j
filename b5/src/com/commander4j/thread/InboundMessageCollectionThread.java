package com.commander4j.thread;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : InboundMessageCollectionThread.java
 * 
 * Package Name : com.commander4j.thread
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

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
public class InboundMessageCollectionThread extends Thread
{
	public boolean allDone = false;
	private String outputFile;
	private String renamedOutputFile;
	private String inputFile;
	private String renamedFile;
	private LinkedList<String> inputPathList = new LinkedList<String>();
	private String inputPath;
	private int inputPathCount = 0;
	private final Logger logger = Logger.getLogger(InboundMessageCollectionThread.class);
	private LinkedList<String> filenames = new LinkedList<String>();
	private File[] chld;
	private String fileName;
	private JFileIO mover = new JFileIO();
	public static boolean recoveringFiles = false;

	public InboundMessageCollectionThread(LinkedList<String> fromPathList, String toPath)
	{
		super();
		inputPathList.clear();
		inputPathList = fromPathList;
		inputPathCount = inputPathList.size();
	}

	private LinkedList<String> getInputFilename(String inputPath)
	{
		LinkedList<String> Result = new LinkedList<String>();
		File dir;

		dir = new File(inputPath);

		chld = dir.listFiles((FileFilter) FileFileFilter.INSTANCE);
		
		if (chld == null)
		{
			logger.debug("Specified directory does not exist or is not a directory. [" + inputPath + "]");
		}
		else
		{
			Arrays.sort(chld, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for (int i = 0; i < chld.length; i++)
			{
				fileName = chld[i].getName();
				if (fileName.toLowerCase().endsWith(".xml"))
				{
					Result.addLast(fileName);
				}
			}
		}

		return Result;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

			if (allDone)
			{
				logger.debug("fileCollectorThread closed.");
				return;
			}

			for (int x = 0; x < inputPathCount; x++)
			{

				inputPath = inputPathList.get(x) + java.io.File.separator;
				filenames = getInputFilename(inputPathList.get(x));

				if (filenames.size() > 0)
				{

					recoveringFiles = true;
					for (int i = filenames.size() - 1; i >= 0; i--)
					{
						inputFile = inputPath + filenames.get(i);
						renamedFile = inputPath + filenames.get(i).replaceAll(".xml", ".lmx");

						renamedOutputFile = Common.base_dir + java.io.File.separator + "xml" + java.io.File.separator + "interface" + java.io.File.separator + "recovery" + java.io.File.separator + filenames.get(i).replaceAll(".xml", ".lmx");
						outputFile = Common.base_dir + java.io.File.separator + "xml" + java.io.File.separator + "interface" + java.io.File.separator + "recovery" + java.io.File.separator + filenames.get(i);

						//If file already exists delete it.
						
						mover.move_File(inputFile, renamedFile);
						mover.move_File(renamedFile, renamedOutputFile);
						mover.move_File(renamedOutputFile, outputFile);

						if (allDone)
						{
							logger.debug("fileCollectorThread closed.");
							return;
						}
					}
					recoveringFiles = false;
				}
			}
		}
	}
}
