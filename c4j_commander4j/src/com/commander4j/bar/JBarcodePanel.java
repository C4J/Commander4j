package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JBarcodePanel.java
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

import java.awt.Graphics;
import java.awt.Image;

import com.commander4j.gui.JPanel4j;

public class JBarcodePanel extends JPanel4j
{

	private static final long serialVersionUID = 1L;

	Image image;

	public void setImage(Image image) {
		this.image = image;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
