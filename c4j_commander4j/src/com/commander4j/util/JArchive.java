package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JArchive.java
 * 
 * Package Name : com.commander4j.util
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
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import com.commander4j.spool.PrintManager;
import com.commander4j.sys.Common;

public class JArchive
{

	public static int archiveBackupFiles(String path, int daysToKeep)
	{
		int result = 0;

		// Calculate cut-off date

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1 * daysToKeep);
		Date cutoffDate = cal.getTime();

		// Check and fix silly/missing values

		if (JUtility.replaceNullStringwithBlank(path).equals(""))
		{
			path = Common.interface_backup_path;
		}

		if (JUtility.replaceNullStringwithBlank(path).equals("."))
		{
			path = Common.interface_backup_path;
		}

		if (daysToKeep <= 0)
		{
			daysToKeep = 1;
		}

		// Get a list of sub-directories within target path

		File directory = new File(path);
		File[] subdirs = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
		for (File dir : subdirs)
		{
			result = result + deleteFiles(dir, new AgeFileFilter(cutoffDate));
		}

		return result;
	}

	public static int archiveSpoolFiles(String path, int daysToKeep)
	{
		int result = 0;

		// Calculate cut-off date

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1 * daysToKeep);
		Date cutoffDate = cal.getTime();

		// Check and fix silly/missing values

		if (JUtility.replaceNullStringwithBlank(path).equals(""))
		{
			path = PrintManager.spoolFolder;
		}

		if (JUtility.replaceNullStringwithBlank(path).equals("."))
		{
			path = PrintManager.spoolFolder;
		}

		if (daysToKeep <= 0)
		{
			daysToKeep = 1;
		}

		// Get a list of sub-directories within target path

		File directory = new File(path);
		File[] subdirs = directory.listFiles((FileFilter) new AgeFileFilter(cutoffDate));
		for (File file : subdirs)
		{
			if (file.isFile())
			{
				result = result + deleteFile(file, ".cmd4j");
			}
		}

		return result;
	}

	public static int deleteFile(File file, String mask)
	{
		int count = 0;

		try
		{
			if (file.getName().endsWith(mask))
			{
				System.out.println("Removing file [" + file.getName());
				file.delete();
				count++;
			}
		}
		catch (Exception ex)
		{

		}

		return count;
	}

	public static int deleteFiles(File directory, FileFilter fileFilter)
	{
		int count = 0;
		File[] files = directory.listFiles(fileFilter);

		for (File file : files)
		{
			// file.delete();
			try
			{
				Date lastMod = new Date(file.lastModified());
				System.out.println("Removing old interface file [" + file.getName() + ", Date: " + lastMod + "] from [" + directory.getName() + "]");
				file.delete();
				count++;
			}
			catch (Exception ex)
			{

			}

		}

		return count;
	}

}
