package com.commander4j.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDesktopPane4j extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private boolean enableWallpaper = false;

	public JDesktopPane4j(String imageName)
	{

		super();

		if (JUtility.replaceNullStringwithBlank(imageName).equals(""))
		{
			enableWallpaper = false;
		} else
		{
			String filename = Common.base_dir + java.io.File.separator + "images" + java.io.File.separator + imageName;

			try
			{
				File x = new File(filename);

				img = ImageIO.read(x.toURI().toURL());
				enableWallpaper = true;

			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected void paintComponent(Graphics grphcs)
	{
		super.paintComponent(grphcs);

		if (enableWallpaper == true)
		{
			int width = getWidth();
			int height = getHeight();
			int imageW = img.getWidth(this);
			int imageH = img.getHeight(this);

			for (int x = 0; x < width; x += imageW)
			{
				for (int y = 0; y < height; y += imageH)
				{
					grphcs.drawImage(img, x, y, this);
				}
			}
		}

	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(img.getWidth(), img.getHeight());
	}
}
