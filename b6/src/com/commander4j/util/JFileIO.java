package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JFileIO.java
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class JFileIO
{
	private String errorMessage = "";
	private String filename = "";
	private String shortfilename = "";
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JFileIO.class);

	public String getShortFilename()
	{
		return shortfilename;
	}

	public void setShortFilename(String filename)
	{
		this.shortfilename = filename;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	private void setErrorMessage(String errmsg)
	{
		errorMessage = errmsg;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public boolean move_File(String srcFile, String destFile)
	{
		File from = new File(srcFile);
		File to = new File(destFile);
		boolean result = true;

		try
		{
			deleteFile(destFile);
			org.apache.commons.io.FileUtils.moveFile(from, to);
		} catch (IOException e)
		{
			logger.debug("move_File error :" + e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}
		return result;
	}

	public boolean move_FileToDirectory(String srcFile, String destDir, boolean createDestDir)
	{
		File from = new File(srcFile);
		File dir = new File(destDir);
		boolean result = true;

		try
		{
			org.apache.commons.io.FileUtils.moveFileToDirectory(from, dir, createDestDir);
		} catch (IOException e)
		{
			logger.debug("move_FileToDirectory error :" + e.getMessage());
			setErrorMessage(e.getMessage());
			result = false;
		}
		return result;
	}

	public String readFiletoString(String filename)
	{
		String result = "";

		File file = new File(filename);

		try
		{
			result = FileUtils.readFileToString(file,"UTF-8");

		} catch (IOException e)
		{
			logger.debug("readFiletoString error :" + e.getMessage());
			setErrorMessage(e.getMessage());
		}
		return result;
	}

	public boolean deleteFile(String filename)
	{
		boolean result = false;

		File file = new File(filename);

		result = file.delete();

		return result;
	}

	public Boolean writeToDisk(String path, Document document, long dbTransactionRef, String filesuffix)
	{
		Boolean result = true;
		try
		{
			NumberFormat formatter = new DecimalFormat("0000000000");
			path = path.replace("\\", java.io.File.separator);
			path = path.replace("/", java.io.File.separator);

			if (path.length() > 0)
			{
				if (path.substring(path.length() - 1).equals(java.io.File.separator) == false)
				{
					path = path + java.io.File.separator;
				}
			}
			String filename = path;
			
			if (dbTransactionRef >= 0)
			{
				filename = filename +  formatter.format(dbTransactionRef);
			}
			
			filename = filename + filesuffix;
			
			setFilename(filename);
			setShortFilename(formatter.format(dbTransactionRef) + filesuffix);

			// ===============================

			DOMImplementationLS DOMiLS = null;
			FileOutputStream FOS = null;

			// testing the support for DOM Load and Save
			if ((document.getFeature("Core", "3.0") != null) && (document.getFeature("LS", "3.0") != null))
			{
				DOMiLS = (DOMImplementationLS) (document.getImplementation()).getFeature("LS", "3.0");

				// get a LSOutput object
				LSOutput LSO = DOMiLS.createLSOutput();

				FOS = new FileOutputStream(filename);
				LSO.setByteStream((OutputStream) FOS);

				// get a LSSerializer object
				LSSerializer LSS = DOMiLS.createLSSerializer();

				// do the serialization
				LSS.write(document, LSO);

				FOS.close();
			} 

			// ===============================

		} catch (Exception ex)
		{
			logger.debug("writeToDisk error :" + ex.getMessage());
			setErrorMessage("Error writing message to disk. " + ex.getMessage());
			result = false;
		}
		return result;
	}

	public Boolean writeToDisk(String path, String document, long dbTransactionRef, String filesuffix)
	{
		Boolean result = true;
		try
		{
			NumberFormat formatter = new DecimalFormat("0000000000");
			path = path.replace("\\", java.io.File.separator);
			path = path.replace("/", java.io.File.separator);

			if (path.length() > 0)
			{
				if (path.substring(path.length() - 1).equals(java.io.File.separator) == false)
				{
					path = path + java.io.File.separator;
				}
			}
			String filename = path + formatter.format(dbTransactionRef) + filesuffix;
			setFilename(filename);
			setShortFilename(formatter.format(dbTransactionRef) + filesuffix);
			java.io.FileWriter fw = new java.io.FileWriter(filename);
			fw.write(document);
			fw.close();

		} catch (Exception ex)
		{
			logger.debug("writeToDisk error :" + ex.getMessage());
			setErrorMessage("Error writing message to disk. " + ex.getMessage());
			result = false;
		}
		return result;
	}
	
	public Boolean writeToDisk(String filename, Document document)
	{
		Boolean result = true;
		try
		{
			setFilename(filename);
			setShortFilename(filename);
			// ===============================

			DOMImplementationLS DOMiLS = null;
			FileOutputStream FOS = null;

			// testing the support for DOM Load and Save
			if ((document.getFeature("Core", "3.0") != null) && (document.getFeature("LS", "3.0") != null))
			{
				DOMiLS = (DOMImplementationLS) (document.getImplementation()).getFeature("LS", "3.0");

				// get a LSOutput object
				LSOutput LSO = DOMiLS.createLSOutput();

				FOS = new FileOutputStream(filename);
				LSO.setByteStream((OutputStream) FOS);

				// get a LSSerializer object
				LSSerializer LSS = DOMiLS.createLSSerializer();

				// do the serialization
				LSS.write(document, LSO);

				FOS.close();
			} 

			// ===============================

		} catch (Exception ex)
		{
			logger.debug("writeToDisk error :" + ex.getMessage());
			setErrorMessage("Error writing message to disk. " + ex.getMessage());
			result = false;
		}
		return result;
	}

	public List<String> readFileLines(String filename)
	{
		List<String> result = null;
		File from = new File(filename);
		try
		{
			result = FileUtils.readLines(from,"UTF-8");
		} catch (IOException e)
		{
			logger.debug("readFileLines error :" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public Boolean writeFileLines(String filename, List<String> data)
	{
		Boolean result = true;
		File from = new File(filename);
		try
		{
			FileUtils.writeLines(from, data);
		} catch (IOException e)
		{
			result = false;
			logger.debug("writeFileLines error :" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

}