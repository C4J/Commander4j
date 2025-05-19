package com.commander4j.gui;

import javax.swing.*;

import com.commander4j.sys.Common;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JCheckBox4j extends JCheckBox {

    private static final long serialVersionUID = 1L;
	private boolean hover = false;

	public void init()
	{
		setFont(Common.font_std);
        setOpaque(false);
		setBackground(Common.color_app_window);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));  // Reduce padding
		setFocusable(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
                revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
                revalidate();
            }
        });	
	}
	
	public JCheckBox4j() 
	{
		super();
		init();
	}
	
    public JCheckBox4j(String text) {
        super(text);
    	init();
    }

    @Override
    public Dimension getPreferredSize() {
        // Increase clickable area slightly
        Dimension d = super.getPreferredSize();
        d.width += 6;
        d.height = Math.max(d.height, 22); // Minimum height
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clear background to avoid tooltip ghosting
            if (isOpaque()) {
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            int boxSize = 16;
            int boxX = 2;
            int boxY = (getHeight() - boxSize) / 2;

            // Draw checkbox outline
            g2.setColor(hover ? new Color(150, 150, 150) : new Color(120, 120, 120));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRect(boxX, boxY, boxSize, boxSize);

            // Draw checkmark if selected
            if (isSelected()) {
                g2.setColor(new Color(60, 120, 200));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int x1 = boxX + 3;
                int y1 = boxY + boxSize / 2;
                int x2 = boxX + boxSize / 2 - 1;
                int y2 = boxY + boxSize - 4;
                int x3 = boxX + boxSize - 3;
                int y3 = boxY + 4;

                g2.drawLine(x1, y1, x2, y2);  // left branch
                g2.drawLine(x2, y2, x3, y3);  // right branch
            }

            // Draw label text
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textX = boxX + boxSize + 6;
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 + 1;

            g2.drawString(getText(), textX, textY);

        } finally {
            g2.dispose();
        }
    }




    @Override
    public boolean contains(int x, int y) {
        // Increase clickable area
        return new Rectangle(0, 0, getWidth(), getHeight()).contains(x, y);
    }
}
