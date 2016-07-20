package com.commander4j.bar;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class JBarcodePanel extends JPanel
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
