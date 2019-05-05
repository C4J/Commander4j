/*
 * Created on 01-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((JXMLDocument.class));

	public Document getDocument()
	{
		return document;
	}

	public JXMLDocument()
	{
		try
		{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception ex)
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
	public String findXPath(String path)
	{
		String result = "";

		try
		{
			Node widgetNode = (Node) xpath.evaluate(path, document, XPathConstants.NODE);
			result = widgetNode.getFirstChild().getNodeValue().toString();
		} catch (Exception ex)
		{
			result = "";
		}

		return result;
	}

	/**
	 * Method setDocument.
	 * 
	 * @param filename
	 *            String
	 */
	public Boolean setDocument(String filename)
	{
		Boolean result = false;
		logger.debug("setDocument :" + filename);
		try
		{
			document = builder.parse(new File(filename));
			result = true;
		} catch (Exception ex)
		{
			logger.error("JXMLDocument.setDocument " + ex.getMessage());
		}
		return result;
	}

	public void setDocument(Document doc)
	{
		document = doc;
	}

	public void setDocumentText(String text)
	{
		logger.debug("setDocumentText :" + text);
		try
		{
			document = builder.parse(new InputSource(new StringReader(text)));

		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			logger.error("JXMLDocument.setDocumentText " + ex.getMessage());
		}
	}

	public String documentToString(org.w3c.dom.Document doc) throws TransformerException
	{

		// Create dom source for the document
		DOMSource domSource = new DOMSource(doc);

		// Create a string writer
		StringWriter stringWriter = new StringWriter();

		// Create the result stream for the transform
		StreamResult result = new StreamResult(stringWriter);

		// Create a Transformer to serialize the document
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");

		// Transform the document to the result stream
		transformer.transform(domSource, result);
		return stringWriter.toString();
	}

}
