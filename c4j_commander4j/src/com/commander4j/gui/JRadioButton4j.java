package com.commander4j.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.commander4j.sys.Common;

public class JRadioButton4j extends JRadioButton {

    private static final long serialVersionUID = 1L;
    private boolean hover = false;

    public JRadioButton4j() {
        super();
        init();
    }

    public JRadioButton4j(String text) {
        super(text);
        init();
    }

    private void init() {
        setFont(Common.font_std);
        setBackground(Common.color_app_window);
        setOpaque(true);  // Important to allow proper background clearing
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
		setFocusable(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width += 6;
        d.height = Math.max(d.height, 18);
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clear background to avoid ghosting from tooltips
            if (isOpaque()) {
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            int boxSize = 16;
            int boxX = 2;
            int boxY = (getHeight() - boxSize) / 2;

            // Draw outer circle
            g2.setColor(hover ? new Color(150, 150, 150) : new Color(120, 120, 120));
           // g2.setStroke(new BasicStroke(1.2f));
            g2.drawOval(boxX, boxY, boxSize, boxSize);

            if (isSelected()) {
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(60, 120, 200));

                int x1 = boxX + 3;
                int y1 = boxY + boxSize / 2;
                int x2 = boxX + boxSize / 2 - 1;
                int y2 = boxY + boxSize - 4;
                int x3 = boxX + boxSize - 3;
                int y3 = boxY + 4;

                g2.drawLine(x1, y1, x2, y2);  // left part of tick
                g2.drawLine(x2, y2, x3, y3);  // right part of tick
            }


            // Draw label
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
        return new Rectangle(0, 0, getWidth(), getHeight()).contains(x, y);
    }

}
