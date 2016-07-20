package com.commander4j.xml;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

public class JXSLTransformer
{

	private TransformerFactory tf;
	private Source xsl;
	@SuppressWarnings("unused")
	private Templates stylesheet;
	private final Logger logger = Logger.getLogger(JXSLTransformer.class);

	public JXSLTransformer(String xslFilename)
	{

		try
		{
			tf = TransformerFactory.newInstance();
			xsl = new StreamSource(xslFilename);
			stylesheet = tf.newTemplates(xsl);
		}
		catch (Exception ex)
		{
			logger.error("Cannot load " + xslFilename);
		}
	}

}
