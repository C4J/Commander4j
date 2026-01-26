package com.commander4j.gui;

import javax.swing.JProgressBar;

import com.commander4j.sys.Common;

public class JProgressBar4j extends JProgressBar {
    private static final long serialVersionUID = 1L;

   // private Color textColor = Common.color_progress_bar_text; // pick whatever you use in your theme

    public JProgressBar4j() {
        super();//        setBackground(Common.color_progress_bar_background);
        setForeground(Common.color_progress_bar);
        setFont(Common.font_std);

        setStringPainted(true);

    }

}
