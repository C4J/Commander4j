
package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import com.commander4j.util.JUtility;


public class JWindowSplash extends JWindow
{
	private static final long serialVersionUID = 1;
	private BufferedImage bufImage;

	private int imageWidth = 0;
	private int imageHeight = 0;
	
	public Dimension getImageDimensions()
	{
		return new Dimension(imageWidth, imageHeight);
	}

	public JWindowSplash()
	{

		GraphicsDevice gd = JUtility.getGraphicsDevice();
		
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Rectangle screenBounds = gc.getBounds();
		
		Image image = new ImageIcon(Common.image_path_16x16 + Common.image_splash).getImage();

		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);

		setBounds(screenBounds.x + ((screenBounds.width - imageWidth) / 2), screenBounds.y + ((screenBounds.height - imageHeight) / 2), imageWidth, imageHeight);

		getContentPane().setLayout(new BorderLayout(0, 0));
		
		bufImage =  new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2D = bufImage.createGraphics();
		g2D.drawImage(image, 0, 0, this);

		setVisible(true);
	}

	public void paint(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(bufImage, 0, 0, this);
	}
	
}
