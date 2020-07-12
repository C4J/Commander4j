/*
 * Created on 01-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.xml;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JXMLDocument.java
 * 
 * Package Name : com.commander4j.xml
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
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 */
public class JXMLDocument
{
	private DocumentBuilder builder;
	/**
	 * Field document. Value: {@value document}
	 */
	private Document document;
	/**
	 * Field xpath. Value: {@value xpath}
	 */
	private XPath xpath = XPathFactory.newInstance().newXPath();

	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((JXMLDocument.class));

	public Document getDocument() {
		return document;
	}

	public JXMLDocument()
	{
		try
		{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (Exception ex)
		{
			logger.error("JXMLDocument constructor " + ex.getMessage());
		}
	}

	/**
	 * Constructor for JXMLDocument.
	 * 
	 * @param filename
	 *            String
	 */
	public JXMLDocument(String filename)
	{
		this();
		setDocument(filename);
	}

	/**
	 * Method findXPath.
	 * 
	 * @param path
	 *            String
	 * @return String
	 */
	public String findXPath(String path) {
		String result = "";

		try
		{
			Node widgetNode = (Node) xpath.evaluate(path, document, XPathConstants.NODE);
			result = widgetNode.getFirstChild().getNodeValue().toString();
		}
		catch (Exception ex)
		{
			result = "";
		}

		return result;
	}


	public Boolean setDocument(String filename) {
		Boolean result = false;
		try
		{
			document = builder.parse(new File(filename));
			result = true;
		}
		catch (Exception ex)
		{
			logger.error("JXMLDocument.setDocument " + ex.getMessage());
		}
		return result;
	}

	public void setDocument(Document doc) {
		document = doc;
	}

	public void setDocumentText(String text) {
		logger.debug("setDocumentText :" + text);
		try
		{
			document = builder.parse(new InputSource(new StringReader(text)));

		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			logger.error("JXMLDocument.setDocumentText " + ex.getMessage());
		}
	}

}
