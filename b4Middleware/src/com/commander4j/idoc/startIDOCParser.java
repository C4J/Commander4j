package com.commander4j.idoc;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class startIDOCParser
{

	public static void main(String[] args)
	{
		IdocParser idp = new IdocParser(System.getProperty("user.dir") + File.separator + "idoc" + File.separator + "schema" + File.separator + "ZMATMAS03.txt", System.getProperty("user.dir") + File.separator + "samples" + File.separator + "input"  + File.separator + "18 (SAP IDOC Material Master)"+ File.separator + "20170421_153238_0590.txt");
		try
		{

			idp.ReadConfigFile();
			idp.GetConfigData();
			idp.GetData();
			
			OutputData od = idp.GetOutputData();

			/* DOCUMENT */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			String msgId= od.getMsgId();
			
			System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			System.out.println("<MESSAGE number=\""+idp.getFileToProcess()+"\" type=\"mm\" id=\""+msgId+"\">");
			
			Element message = (Element) document.createElement("MESSAGE");
			message.setAttribute("number", idp.getFileToProcess());
			message.setAttribute("type", "idoc");
			message.setAttribute("id", msgId);
			
			int dscount = od.GetDataSegments().size();
			
			for (int x=1;x<=dscount;x++)
			{
				DataSegment ds = od.GetDataSegments().get(x-1);
				System.out.println("   <DATA type=\""+ds.SegmentName+"\">");
				
				/* DATA */
				Element data = (Element) document.createElement("DATA");
				data.setAttribute("type", ds.SegmentName);
				
				Set<String> keyset = ds.Properties.keySet();

					Iterator<String> it = keyset.iterator();
					while (it.hasNext()) {
						Object obj = it.next();
						
						Object val = ds.Properties.get(obj.toString());
						System.out.println("      <FIELD name=\""+ obj.toString()+"\" value=\""+val+"\"/>");
						
						/* FIELD */
						Element field= (Element) document.createElement("FIELD");
						field.setAttribute("name", val.toString());
						data.appendChild(field);
						
					} 
					System.out.println("   </DATA>");
					message.appendChild(data);
			}
			System.out.println("</MESSAGE>");
			
			document.appendChild(message);
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
