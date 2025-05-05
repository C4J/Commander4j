package com.commander4j.gui;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDesktopPane4j.java
 * 
 * Package Name : com.commander4j.gui
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
