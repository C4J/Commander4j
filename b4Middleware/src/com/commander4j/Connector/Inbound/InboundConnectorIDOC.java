package com.commander4j.Connector.Inbound;

import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.commander4j.Interface.Inbound.InboundInterface;
import com.commander4j.idoc.DataSegment;
import com.commander4j.idoc.IdocParser;
import com.commander4j.idoc.OutputData;
import com.commander4j.sys.Common;

import ABSTRACT.com.commander4j.Connector.InboundConnectorABSTRACT;

public class InboundConnectorIDOC extends InboundConnectorABSTRACT
{

	Logger logger = org.apache.logging.log4j.LogManager.getLogger((InboundConnectorIDOC.class));
	
	public InboundConnectorIDOC(InboundInterface inter)
	{
		super(Connector_IDOC, inter);
	}

	@Override
	public boolean connectorLoad(String fullFilename)
	{
		
		logger.debug("connectorLoad [" + fullFilename + "]");		
		boolean result = false;

		if (backupInboundFile(fullFilename))
		{

			String idocSchemaFilename = getInboundInterface().getIdocSchemaFilename();

			// TODO Auto-generated method stub
			IdocParser idp = new IdocParser(idocSchemaFilename, fullFilename);
			try
			{
				
				idp.ReadConfigFile();
				idp.GetConfigData();
				idp.GetData();

				OutputData od = idp.GetOutputData();
				
				/* DOCUMENT */
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				data = builder.newDocument();
				result = true;
				
				String msgId= od.getMsgId();

				System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				System.out.println("<MESSAGE number=\""+idp.getFileToProcess()+"\" type=\"mm\" id=\""+msgId+"\">");

				Element message = (Element) data.createElement("MESSAGE");
				message.setAttribute("number", idp.getFileToProcess());
				message.setAttribute("type", "idoc");
				message.setAttribute("id", msgId);

				int dscount = od.GetDataSegments().size();
				
				for (int x = 1; x <= dscount; x++)
				{
					DataSegment ds = od.GetDataSegments().get(x - 1);
					System.out.println("   <DATA type=\"" + ds.SegmentName + "\">");

					/* DATA */
					Element outputdata = (Element) data.createElement("DATA");
					outputdata.setAttribute("type", ds.SegmentName);

					Set<String> keyset = ds.Properties.keySet();

					Iterator<String> it = keyset.iterator();
					while (it.hasNext())
					{
						Object obj = it.next();

						Object val = ds.Properties.get(obj.toString());
						System.out.println("      <FIELD name=\"" + obj.toString() + "\" value=\"" + val + "\"/>");

						/* FIELD */
						Element outputfield = (Element) data.createElement("FIELD");
						outputfield.setAttribute("name", obj.toString());
						
						
						if (val.toString().length()>0)
						{
						    StringBuilder filtered = new StringBuilder(val.toString().length());
						    for (int i = 0; i < val.toString().length(); i++) {
						        char current = val.toString().charAt(i);
						        
						        if ((current >= 0x20) || (current == 0x13) || (current == 0x10)) {
						            filtered.append(current);
						        }
						        else
						        {
						        		filtered.append("_");
						        }

						    }
						    outputfield.setAttribute("value",filtered.toString());
						}
						else
						{
							outputfield.setAttribute("value",val.toString());
						}
						
						outputdata.appendChild(outputfield);

					}
					System.out.println("   </DATA>");
					message.appendChild(outputdata);
				}
				System.out.println("</MESSAGE>");

				data.appendChild(message);

				result = true;

			} catch (Exception ex)
			{
				result = false;
				logger.error("connectorLoad " + getType() + " " + ex.getMessage());
				Common.emailqueue.addToQueue("Error", "Error reading "+getType(), "connectorLoad " + getType() + " " + ex.getMessage()+"\n\n"+fullFilename, "");

			}
			idp = null;

		}

		return result;
	}

}
