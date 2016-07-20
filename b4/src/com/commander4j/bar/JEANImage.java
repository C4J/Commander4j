package com.commander4j.bar;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapBuilder;

/**
 */
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
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return bufImage; // return image you painted on
	}

}
