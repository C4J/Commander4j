/*
 * Created on 14-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.sys;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

public class JWindowSplash extends JWindow
{
	private static final long serialVersionUID = 1;
	private BufferedImage bufImage;
	private Rectangle rect;
	private int imageWidth = 0;
	private int imageHeight = 0;
	
	public Dimension getImageDimensions()
	{
		return new Dimension(imageWidth, imageHeight);
	}

	public JWindowSplash()
	{

		Image image = new ImageIcon(Common.image_path + Common.image_splash).getImage();

		imageWidth = image.getWidth(this);
		imageHeight = image.getHeight(this);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		rect = new Rectangle((screenWidth / 2 - (imageWidth/2)) , ((screenHeight /2)- imageHeight), imageWidth, imageHeight);
		setBounds(rect);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		try
		{
			bufImage = new Robot().createScreenCapture(rect);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}

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
