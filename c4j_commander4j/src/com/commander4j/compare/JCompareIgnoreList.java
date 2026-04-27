package com.commander4j.compare;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JCompareIgnoreList.java
 *
 * Package Name : com.commander4j.compare
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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.commander4j.sys.Common;

/**
 * Manages the list of field-level ignore exceptions for the database compare
 * tool.  Entries are persisted to xml/compare/ignore.xml.  Each entry
 * identifies a DB type pair (order-independent), a table name and a field
 * name.  When a field matches an entry it is not flagged as different during
 * a comparison, even if the raw schema values differ.
 */
public class JCompareIgnoreList
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JCompareIgnoreList.class);
	private final List<JCompareIgnoreEntry> entries = new ArrayList<>();

	private String getFilePath()
	{
		return Common.base_dir
			+ File.separator + "xml"
			+ File.separator + "compare"
			+ File.separator + "ignore.xml";
	}

	/** Returns a snapshot of the current entries. */
	public List<JCompareIgnoreEntry> getEntries()
	{
		return new ArrayList<>(entries);
	}

	/** Loads entries from ignore.xml.  Silently succeeds if the file does not yet exist. */
	public void load()
	{
		entries.clear();
		File file = new File(getFilePath());
		if (!file.exists())
		{
			logger.debug("JCompareIgnoreList: ignore.xml not found at " + getFilePath() + " — starting empty");
			return;
		}
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			NodeList nodes = doc.getElementsByTagName("Entry");
			for (int i = 0; i < nodes.getLength(); i++)
			{
				Element el = (Element) nodes.item(i);
				String dt1   = el.getAttribute("dbType1");
				String dt2   = el.getAttribute("dbType2");
				String tbl   = el.getAttribute("tableName");
				String fld   = el.getAttribute("fieldName");
				entries.add(new JCompareIgnoreEntry(dt1, dt2, tbl, fld));
			}
			logger.debug("JCompareIgnoreList: loaded " + entries.size() + " entries from " + getFilePath());
		}
		catch (Exception ex)
		{
			logger.error("JCompareIgnoreList.load " + ex.getMessage());
		}
	}

	/** Saves all current entries to ignore.xml, creating the directory if needed. */
	public void save()
	{
		try
		{
			File dir = new File(getFilePath()).getParentFile();
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();

			Element root = doc.createElement("CompareIgnoreList");
			doc.appendChild(root);

			for (JCompareIgnoreEntry e : entries)
			{
				Element el = doc.createElement("Entry");
				el.setAttribute("dbType1",   e.getDbType1());
				el.setAttribute("dbType2",   e.getDbType2());
				el.setAttribute("tableName", e.getTableName());
				el.setAttribute("fieldName", e.getFieldName());
				root.appendChild(el);
			}

			if (doc.getFeature("LS", "3.0") != null)
			{
				DOMImplementationLS domLS = (DOMImplementationLS) doc.getImplementation().getFeature("LS", "3.0");
				LSOutput lso = domLS.createLSOutput();
				FileOutputStream fos = new FileOutputStream(getFilePath());
				lso.setByteStream((OutputStream) fos);
				LSSerializer lss = domLS.createLSSerializer();
				lss.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
				lss.write(doc, lso);
				fos.close();
			}
			logger.debug("JCompareIgnoreList: saved " + entries.size() + " entries to " + getFilePath());
		}
		catch (Exception ex)
		{
			logger.error("JCompareIgnoreList.save " + ex.getMessage());
		}
	}

	/**
	 * Adds an entry if no identical entry already exists.
	 * Saves to disk immediately.
	 * @return true if the entry was added; false if it was already present.
	 */
	public boolean addEntry(String dbTypeA, String dbTypeB, String tableName, String fieldName)
	{
		for (JCompareIgnoreEntry e : entries)
		{
			if (e.matches(dbTypeA, dbTypeB, tableName, fieldName))
			{
				return false;
			}
		}
		entries.add(new JCompareIgnoreEntry(dbTypeA, dbTypeB, tableName, fieldName));
		save();
		return true;
	}

	/**
	 * Removes the given entry from the list and saves to disk immediately.
	 */
	public void removeEntry(JCompareIgnoreEntry entry)
	{
		entries.removeIf(e -> e.matches(
			entry.getDbType1(), entry.getDbType2(),
			entry.getTableName(), entry.getFieldName()));
		save();
	}

	/**
	 * Returns true if a matching entry exists for the given DB type pair,
	 * table and field.  The DB type pair comparison is order-independent.
	 */
	public boolean isIgnored(String dbTypeA, String dbTypeB, String tableName, String fieldName)
	{
		for (JCompareIgnoreEntry e : entries)
		{
			if (e.matches(dbTypeA, dbTypeB, tableName, fieldName))
			{
				return true;
			}
		}
		return false;
	}
}
