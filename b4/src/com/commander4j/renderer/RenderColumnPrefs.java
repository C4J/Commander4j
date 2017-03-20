package com.commander4j.renderer;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : RenderColumnPrefs.java
 * 
 * Package Name : com.commander4j.renderer
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

import java.awt.Color;

public class RenderColumnPrefs
{
	private int Alignment;
	private Color ForegroundColour;
	private Color BackgroundColour1;
	private Color BackgroundColour2;
	private int width;
	
	public RenderColumnPrefs(int align,Color foreground,Color background1,Color background2,int w)
	{
		Alignment = align;
		ForegroundColour = foreground;
		BackgroundColour1 = background1;
		BackgroundColour2 = background2;
		width = w;
	}
	
	public int getAlignment()
	{
		return Alignment;
	}

	public Color getForegroundColour()
	{
		return ForegroundColour;
	}
	
	public Color getBackgroundColour1()
	{
		return BackgroundColour1;
	}
	public Color getBackgroundColour2()
	{
		return BackgroundColour2;
	}
	public int getWidth()
	{
		return width;
	}
}
