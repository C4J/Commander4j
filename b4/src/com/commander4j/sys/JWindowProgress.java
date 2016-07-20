/*
 * Created on 14-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.commander4j.sys;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JProgressBar;
import javax.swing.JWindow;


public class JWindowProgress extends JWindow
{
	private static final long serialVersionUID = 1;

	private Rectangle rect;
	private int lastprogress = 0;
	private int imageWidth = 0;

	private int progressHeight = 20;
	private static final int WAIT = 100;
	private JProgressBar progress = new JProgressBar();
	

	public JWindowProgress(Dimension imageSize)
	{

		imageWidth = imageSize.width;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		rect = new Rectangle((screenWidth / 2 - (imageWidth/2)),  ((screenHeight /2)+10), imageWidth, progressHeight);
		
		setBounds(rect);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		getContentPane().add(progress,BorderLayout.PAGE_END);
		progress.setBorderPainted(true);
		progress.setPreferredSize(new Dimension(imageWidth, progressHeight));
		progress.setMinimumSize(new Dimension(imageWidth, progressHeight));
		progress.setStringPainted(true);
		progress.setMinimum(0);
		progress.setMaximum(WAIT);
		progress.setBackground(Color.WHITE);
		progress.setForeground(Color.BLUE);
		

		setVisible(true);
	}

	
	public void update(int w, String comment) {
		try
		{
			Thread.sleep(10);
		}
		catch (InterruptedException ie)
		{
		}

		for (int w2 = lastprogress; w2 <= w; w2++)
		{
			setVisible(true);
			progress.setValue(w2);
			progress.paintImmediately( rect );

			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException ie)
			{
			}
		}

		lastprogress = w;	
	}
}
