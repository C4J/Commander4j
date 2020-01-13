package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogWTError.java
 * 
 * Package Name : com.commander4j.app
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;


public class JDialogWTError extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private String warningMessage = "";

	public JDialogWTError(JFrame frame, String warningMessage)
	{
		super(frame);
		setUndecorated(true);

		setModal(true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setResizable(false);

		this.setPreferredSize(new java.awt.Dimension(455, 518));
		this.setSize(new Dimension(570, 243));

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBackground(Color.RED);
		this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
		jDesktopPane1.setLayout(null);
		
		this.warningMessage = warningMessage;

		initGUI();

	}

	private void initGUI()
	{
		try
		{

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Ok"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(222, 205, 125, 32);
			
			JTextPane textArea = new JTextPane();
			textArea.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
			textArea.setEditable(false);
			textArea.setBackground(new Color(255, 255, 255));
			textArea.setContentType("text/html");
			
			textArea.setText(warningMessage);
		
			textArea.setBounds(16, 16, 531, 185);
			
			jDesktopPane1.add(textArea);
			
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			
			JUtility.errorBeep();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
