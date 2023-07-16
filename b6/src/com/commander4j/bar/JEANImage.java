package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JEANImage.java
 * 
 * Package Name : com.commander4j.bar
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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapBuilder;

public class JEANImage
{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private static BufferedImage bufImage;

	public static Image getAWTImage(String filename, String msg) {

		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		String data = msg.replace("<GS>", "\u00F1");

		try
		{
			Configuration cfg = builder.buildFromFile(new File(filename));
			BarcodeGenerator gen = BarcodeUtil.getInstance().createBarcodeGenerator(cfg);

			BarcodeDimension barDim = gen.calcDimensions(data);
			bufImage = new BufferedImage((int) barDim.getWidth(), (int) barDim.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

			bufImage = BitmapBuilder.getImage(gen, data, 300);
		}
		catch (Exception e)
		{
			bufImage = new BufferedImage(10, 10, 10);

		}

		return bufImage; // return image you painted on
	}

}
