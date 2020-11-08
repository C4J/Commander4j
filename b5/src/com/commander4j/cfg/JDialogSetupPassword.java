package com.commander4j.cfg;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogSetupPassword.java
 * 
 * Package Name : com.commander4j.cfg
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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JFixedSizeFilter;
import com.commander4j.util.JUtility;

public class JDialogSetupPassword extends JDialog
{


	private static final long serialVersionUID = 1;

	private String password;
	private String setupPassword;
	private JButton4j btn_login;
	private JButton4j btn_close;
	private JPasswordField fld_password;
	final int screenWidth = 250;
	final int screenHeight = 150;

	public JDialogSetupPassword(Frame parent,String password) {

		super(parent, "Login", true);
		setupPassword = password;

		setTitle("Setup Password");

		JLabel lname = new JLabel("User name");
		lname.setFont(Common.font_std);
		JLabel lpassword = new JLabel("Password");
		lpassword.setFont(Common.font_std);

		ButtonHandler buttonhandler = new ButtonHandler();
		getContentPane().setLayout(null);

		btn_close = new JButton4j("Close", Common.icon_cancel_16x16);
		btn_close.setText("Cancel");
		btn_close.setBounds(142, 52, 104, 25);
		getContentPane().add(btn_close);
		btn_login = new JButton4j("Login", Common.icon_ok_16x16);
		btn_login.setText("OK");
		btn_login.setBounds(19, 52, 104, 25);
		getContentPane().add(btn_login);
		btn_login.addActionListener(buttonhandler);
		btn_close.addActionListener(buttonhandler);

		getRootPane().setDefaultButton(btn_login);

		fld_password = new JPasswordField(10);
		fld_password.setBounds(110, 18, 150, 22);
		getContentPane().add(fld_password);
		AbstractDocument doc2 = (AbstractDocument) fld_password.getDocument();
		doc2.setDocumentFilter(new JFixedSizeFilter(JDBUser.field_password));
		fld_password.setFont(Common.font_std);

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setBounds(19, 24, 83, 16);
		getContentPane().add(lblPassword);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(271, 119);
		setLocation((screenSize.width - screenWidth) / 2, (screenSize.height - screenHeight) / 2);
		setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);

	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{

			if (event.getSource() == btn_login)
			{
				password = new String(fld_password.getPassword());

				if (password.equals(setupPassword))
				{
					dispose();
				}
				else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(null, "Incorrect config password", "Password", JOptionPane.ERROR_MESSAGE);
				}

			}

			if (event.getSource() == btn_close)
			{
					System.exit(0);
			}
		}
	}
}
