
package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JWindowProgress.java
 *
 * Package Name : com.commander4j.sys
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


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JWindow;

import com.commander4j.gui.JProgressBar4j;

public class JWindowProgress extends JWindow
{
	private static final long serialVersionUID = 1;

	private Rectangle rect;
	private int lastprogress = 0;
	private int imageWidth = 0;

	private int progressHeight = 20;
	private static final int WAIT = 100;
	private JProgressBar4j progress = new JProgressBar4j();


	public JWindowProgress(Rectangle splashBounds)
	{


		rect = new Rectangle(splashBounds.x,  splashBounds.y+splashBounds.height+10, splashBounds.width, progressHeight);


		setBounds(rect);

		getContentPane().setLayout(new BorderLayout(0, 0));

		getContentPane().add(progress,BorderLayout.PAGE_END);

		progress.setBorderPainted(true);
		progress.setPreferredSize(new Dimension(imageWidth, progressHeight));
		progress.setMinimumSize(new Dimension(imageWidth, progressHeight));
		progress.setStringPainted(true);
		progress.setMinimum(0);
		progress.setMaximum(WAIT);

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
