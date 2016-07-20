package com.commander4j.renderer;

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
