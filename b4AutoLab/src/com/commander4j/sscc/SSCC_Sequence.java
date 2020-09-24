package com.commander4j.sscc;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.commander4j.autolab.AutoLab;
import com.commander4j.bar.JEANBarcode;
import com.commander4j.utils.JFileIO;
import com.commander4j.utils.JUtility;
import com.commander4j.xml.JXMLDocument;

public class SSCC_Sequence extends Thread
{


	private String filename = "";
	private String path = "";
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger((SSCC_Sequence.class));
	private boolean run = true;

	private JXMLDocument xmlDoc;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;
	private JFileIO fio = new JFileIO();
	private JUtility utils = new JUtility();
	private JEANBarcode barcode = new JEANBarcode();

	
	
	public SSCC_Sequence(String filename)
	{
		setPath(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "sscc"+ File.separator);
		setFilename(filename);
		setName("SSCC Sequence {"+getFilename()+"}");

	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public String getPath()
	{
		return path;
	}

	public synchronized String readSSCC()
	{
		String result = "";
		xmlDoc = new JXMLDocument(getPath() + getFilename());
		
		//Get next SSCC from XML - the actual value in the xml are the next ones to use. Once used the program stores the incremented version back in the file for the next pallet.
		String prefix = xmlDoc.findXPath("/sscc/prefix");
		String sequence = xmlDoc.findXPath("/sscc/sequence");
		String checkdigit = xmlDoc.findXPath("/sscc/checkDigit");
		checkdigit = barcode.calcCheckdigit(prefix+sequence);
		result = prefix+sequence+checkdigit;
		
		//Work out length of Sequence Number excluding check digit.
		int prefixLength = prefix.length();
		int sequenceLength = 18 - prefixLength - 1;

		//Increment Sequence Number for Next SSCC
		int seqNumber = Integer.parseInt(sequence);
		seqNumber++;
		String nextSequence = String.valueOf(seqNumber);
		nextSequence = utils.padString(nextSequence, false, sequenceLength, "0");
		
		//Calculate new checkdigit
		String nextCheckDigit = barcode.calcCheckdigit(prefix+nextSequence);

		//Save next SSCC to xml
		try
		{
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			
			Element element_SSCC = (Element) document.createElement("sscc");

			Element element_prefix = addElement(document, "prefix", prefix);
			element_SSCC.appendChild(element_prefix);

			Element element_sequence = addElement(document, "sequence", nextSequence);
			element_SSCC.appendChild(element_sequence);
			
			Element element_checkdigit = addElement(document, "checkDigit", nextCheckDigit);
			element_SSCC.appendChild(element_checkdigit);
			
			document.appendChild(element_SSCC);
			
			fio.writeToDisk(getPath(), document, getFilename());

		}
		catch (ParserConfigurationException e)
		{
			logger.debug(getName()+" Error reading SSCC sequence number from file :"+e.getLocalizedMessage());
		}
		
		AutoLab.systemNotify.appendToMessage("Generating SSCC ["+result+"] from "+getFilename());
		
		return result;
	}

	public Element addElement(Document doc, String name, String value)
	{
		Element temp = (Element) doc.createElement(name);
		Text temp_value = doc.createTextNode(value);
		temp.appendChild(temp_value);
		return temp;
	}

	
	public void run()
	{


		logger.debug(getName()+" Thread Started...");

		while (run == true)
		{

			try
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				run=false;
			}

		}

		logger.debug(getName()+" Thread Stopped...");
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public void shutdown()
	{
		run = false;
	}

}
