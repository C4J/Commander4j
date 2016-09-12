package com.commander4j.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import com.commander4j.sys.Common;

public class JArchive {

	public static int archiveBackupFiles(String path, int daysToKeep)
	{
		int result = 0;

		// Calculate cut-off date

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1 * daysToKeep);
		Date cutoffDate = cal.getTime();

		// Check and fix silly/missing values

		if (Utility.replaceNullStringwithBlank(path).equals(""))
		{
			path = Common.logDir;
		}

		if (Utility.replaceNullStringwithBlank(path).equals("."))
		{
			path = Common.logDir;
		}
		
		if (daysToKeep<=0)
		{
			daysToKeep=1;
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
				System.out.println("Removing old interface file [" + file.getName() + ", Date: " + lastMod + "] from ["+directory.getName()+"]");
				file.delete();
				count++;
			} catch (Exception ex)
			{

			}

		}

		return count;
	}

}
