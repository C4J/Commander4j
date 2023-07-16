package com.commander4j.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JImagePanel4j extends JPanel {


	private static final long serialVersionUID = 1L;
	private BufferedImage background;

    public JImagePanel4j(String imageFilename) {
        setLayout(new BorderLayout());
        try {
            background = ImageIO.read(new File(imageFilename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(background, 0, 0, this);
            g2d.dispose();
        }
    }
}
