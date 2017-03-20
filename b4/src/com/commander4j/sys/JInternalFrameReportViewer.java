package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameReportViewer.java
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

import javax.swing.JDesktopPane;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class JInternalFrameReportViewer extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JPanel pnlMain;

	/**
	 * Auto-generated main method to display this JInternalFrame inside a new
	 * JFrame.
	 */

	public JInternalFrameReportViewer()
	{
		super();
		initGUI();
	}

	public JInternalFrameReportViewer(JasperPrint jp, boolean isXML)
	{
		super();
		initGUI();
		try
		{
			{
				JRViewer viewer = new JRViewer(jp);
				viewer.setZoomRatio((float) 0.75);
				pnlMain.add(viewer, BorderLayout.CENTER);
				viewer.setPreferredSize(new java.awt.Dimension(647, 451));
				viewer.setAutoscrolls(true);
			}
		}
		catch (Exception e)
		{

		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(917, 573));
			this.setBounds(25, 25, 917, 573);

			this.setClosable(true);
			this.setTitle("Report Viewer");
			this.setIconifiable(true);
			this.setMaximizable(true);
			this.setResizable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				BorderLayout jDesktopPane1Layout = new BorderLayout();
				jDesktopPane1.setLayout(jDesktopPane1Layout);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					pnlMain = new JPanel();
					BorderLayout pnlMainLayout = new BorderLayout();
					pnlMain.setLayout(pnlMainLayout);
					jDesktopPane1.add(pnlMain, BorderLayout.CENTER);
					pnlMain.setBounds(1, 2, 917, 573);

				}
			}
			// setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
