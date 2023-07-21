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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

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
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(InboundMessageCollectionThread.class);

	private JFileIO mover = new JFileIO();
	public static boolean recoveringFiles = false;

	public InboundMessageCollectionThread(LinkedList<String> fromPathList, String toPath)
	{
		super();
		inputPathList.clear();
		inputPathList = fromPathList;
		inputPathCount = inputPathList.size();
	}

	public void run()
	{
		File dir;
		String[] extensions =
		{ "xml", "XML" };
		List<File> filenames;
		File sizeFile;
		long size;

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
				logger.debug("InboundMessageCollectionThread closed.");
				return;
			}

			for (int x = 0; x < inputPathCount; x++)
			{

				inputPath = inputPathList.get(x) + java.io.File.separator;
				dir = new File(inputPathList.get(x));
				filenames = (List<File>) FileUtils.listFiles(dir, extensions, false);

				if (filenames.size() > 0)
				{

					recoveringFiles = true;
					for (int i = filenames.size() - 1; i >= 0; i--)
					{
						inputFile = inputPath + filenames.get(i).getName();

						sizeFile = new File(inputFile);
						size = FileUtils.sizeOf(sizeFile);
						
						if (size > 0)
						{

							renamedFile = inputPath + filenames.get(i).getName().replaceAll(".xml", ".lmx");

							renamedOutputFile = Common.base_dir + java.io.File.separator + "xml" + java.io.File.separator + "interface" + java.io.File.separator + "recovery" + java.io.File.separator + filenames.get(i).getName().replaceAll(".xml", ".lmx");
							outputFile = Common.base_dir + java.io.File.separator + "xml" + java.io.File.separator + "interface" + java.io.File.separator + "recovery" + java.io.File.separator + filenames.get(i).getName();

							// If file already exists delete it.

							logger.debug("Rename " + inputFile + " to " + renamedFile);
							mover.move_File(inputFile, renamedFile);

							logger.debug("Move " + renamedFile + " to " + renamedOutputFile);
							mover.move_File(renamedFile, renamedOutputFile);

							logger.debug("Rename " + renamedOutputFile + " to " + outputFile);
							mover.move_File(renamedOutputFile, outputFile);
						}

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
