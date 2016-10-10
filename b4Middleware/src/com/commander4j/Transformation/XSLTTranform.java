package com.commander4j.Transformation;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XSLTTranform
{

	Document inputDocument;
	Document outputDocument;

	public static void main(String[] args)
	{
		XSLTTranform test = new XSLTTranform();
		test.process("./xml/xslt/MATERIAL_IDOC_XML_to_C4J_XML.xsl", "./xml/samples/input/Material Master/Wroclaw2.xml", "./xml/interface/output/Wroclaw2_result.xml");
	}

	public boolean process(File stylesheet, Document document, String xmlOut)
	{
		boolean result = false;

		try
		{

			TransformerFactory tFactory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(stylesheet);
			Transformer transformer = tFactory.newTransformer(stylesource);

			DOMSource source = new DOMSource(document);
			// StreamResult sResult = new StreamResult(new
			// XmlStreamWriter((OutputStream) outputDocument));
			StreamResult sResult = new StreamResult(new File(xmlOut));
			transformer.transform(source, sResult);
			result = true;
		} catch (Exception ex)
		{
			System.out.println("\n** Transformer Factory error" + ex.getMessage());

		}

		return result;
	}

	public boolean process(String xsltFilename, String xmlInputFilename, String xmlOutputFilename)
	{
		boolean result = false;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try
		{
			File datafile = new File(xmlInputFilename);
			File stylesheet = new File(xsltFilename);

			DocumentBuilder builder = factory.newDocumentBuilder();
			inputDocument = builder.parse(datafile);

			process(stylesheet, inputDocument, xmlOutputFilename);

		} catch (Exception ex)
		{
			System.out.println("\n** Transformer Factory error" + ex.getMessage());

		}

		return result;
	}

}
