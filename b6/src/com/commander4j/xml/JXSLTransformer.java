package com.commander4j.xml;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JXSLTransformer.java
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

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.Logger;

public class JXSLTransformer
{

	private TransformerFactory tf;
	private Source xsl;
	@SuppressWarnings("unused")
	private Templates stylesheet;
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JXSLTransformer.class);

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
