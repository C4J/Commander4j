package com.commander4j.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.commander4j.sys.Common;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JComboBox4j<E> extends JComboBox<E> {

    private static final long serialVersionUID = 1L;
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);

    private void init()
    {
        setUI(new FlatComboBoxUI());
		setFont(Common.font_combo);
        setFocusable(false);
        setBorder(EMPTY_BORDER);
    }
    
	public JComboBox4j(E[] items) {
        super(items);
        init();
    }
	
	public JComboBox4j() {
		super();
        init();
	}

	public JComboBox4j(String[] fieldAlignment)
	{
		super();
        init();
	}

    private static class FlatComboBoxUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton(new FlatArrowIcon());

            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(EMPTY_BORDER);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(230, 230, 230));
                    button.setOpaque(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setOpaque(false);
                }
            });

            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Optional: remove highlight rectangle if desired
        }
    }

    private static class FlatArrowIcon implements Icon {
        private final int size = 10;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = size;
            int h = size / 2;

            int[] xPoints = { x, x + w / 2, x + w };
            int[] yPoints = { y, y + h, y };

            g2.setColor(Color.DARK_GRAY);
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size / 2;
        }
    }
}
