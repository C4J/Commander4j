package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogLogin.java
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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPasswordField4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JUtility;

public class JDialogLogin extends JDialog {

	private static final long serialVersionUID = 1;

	public String password;
	public String username;

	private JButton4j btn_login;
	private JButton4j btn_close;
	private JTextField4j fld_userName;

	private JPasswordField4j fld_password;

	private JDBUser user;
	private JCheckBox4j jCheckbox_chgPassword;

	final int screenWidth = 250;

	final int screenHeight = 150;

	public JDialogLogin(Frame parent)
	{

		super(parent, "Login", true);
		setModal(true);
		user = new JDBUser(Common.selectedHostID, Common.sessionID);

		setTitle("Login (" + Common.hostList.getHost(Common.selectedHostID).getSiteDescription() + ")");

		JLabel4j_std lname = new JLabel4j_std("User name");
		lname.setFont(Common.font_std);
		JLabel4j_std lpassword = new JLabel4j_std("Password");
		lpassword.setFont(Common.font_std);

		ButtonHandler buttonhandler = new ButtonHandler();

		KeyboardHandler keyboardhandler = new KeyboardHandler();
		getContentPane().setLayout(null);
		fld_userName = new JTextField4j(JDBUser.field_user_id);
		fld_userName.setText(System.getProperty("user.name"));
		fld_userName.setBounds(103, 12, 146, 22);
		getContentPane().add(fld_userName);

		fld_userName.setFont(Common.font_std);
		fld_userName.setCaretPosition(fld_userName.getText().length());
		btn_close = new JButton4j("Close", Common.icon_cancel);
		btn_close.setBounds(150, 114, 104, 32);
		getContentPane().add(btn_close);
		btn_close.setMnemonic('C');
		btn_close.setToolTipText("Click to cancel logon.");
		btn_login = new JButton4j("Login", Common.icon_ok);
		btn_login.setBounds(23, 114, 104, 32);
		getContentPane().add(btn_login);
		btn_login.setMnemonic('L');
		btn_login.setToolTipText("Click to validate logon.");
		btn_login.addActionListener(buttonhandler);

		btn_login.addKeyListener(keyboardhandler);

		getRootPane().setDefaultButton(btn_login);
		jCheckbox_chgPassword = new JCheckBox4j("Change Password");
		jCheckbox_chgPassword.setBounds(103, 82, 133, 20);
		getContentPane().add(jCheckbox_chgPassword);
		jCheckbox_chgPassword.setFont(Common.font_std);
		jCheckbox_chgPassword.setMnemonic('P');
		jCheckbox_chgPassword.setToolTipText("Check to change password.");
		fld_password = new JPasswordField4j(JDBUser.field_password);
		fld_password.setBounds(103, 48, 146, 22);
		getContentPane().add(fld_password);
		fld_password.setFont(Common.font_std);

		JLabel4j_std lblUsername = new JLabel4j_std("Username :");
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setBounds(12, 14, 83, 16);
		getContentPane().add(lblUsername);

		JLabel4j_std lblPassword = new JLabel4j_std("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setBounds(12, 54, 83, 16);
		getContentPane().add(lblPassword);
		fld_password.addKeyListener(keyboardhandler);
		jCheckbox_chgPassword.addKeyListener(keyboardhandler);
		btn_close.addActionListener(buttonhandler);
		btn_close.addKeyListener(keyboardhandler);
		fld_userName.addKeyListener(keyboardhandler);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fld_password.requestFocus();
				fld_password.setCaretPosition(fld_password.getPassword().length);
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(277, 183);
		setLocation((screenSize.width - screenWidth) / 2, (screenSize.height - screenHeight) / 2);
		setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);

		try
		{
			this.setCursor(Cursor.getDefaultCursor());
			setAlwaysOnTop(true);

		} finally
		{

		}
	}
	
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event)
		{

			if (event.getSource() == btn_login)
			{
				password = new String(fld_password.getPassword());
				username = fld_userName.getText().toUpperCase();
				Common.logonValidated = false;

				user.setUserId(username);
				user.setLoginPassword(password);
				if (user.login())
				{
					Common.userList.addUser(Common.sessionID, user);
					Common.logonValidated = true;
					Common.validatedUsername = username;
					Common.validatedPassword = password; 
					Common.passwordChangeRequired = user.isPasswordChangeRequired();
					Common.passwordChangeRequested = jCheckbox_chgPassword.isSelected();
					Common.passwordExpired = user.isPasswordExpired();
					dispose();
				} else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(fld_password, user.getErrorMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
					fld_password.setText("");
			           SwingUtilities.invokeLater( new Runnable() 
		                 { 
		                 public void run() 
		                     { 
		                	 fld_password.requestFocus(); 
		                 } 
		             });
			           fld_password.setCaretPosition(0);
				}

			}

			if (event.getSource() == btn_close)
			{
				Common.logonValidated = false;
				Common.passwordChangeRequired = false;
				Common.passwordChangeRequested = false;
				username = "";
				password = "";
				dispose();
			}
		}
	}	
	
	private class KeyboardHandler implements KeyListener {
		public void keyPressed(KeyEvent event)
		{
			if (event.getKeyCode() == 27)
			{
				Common.logonValidated = false;
				username = "";
				password = "";
				dispose();
			}
		}

		public void keyReleased(KeyEvent event)
		{
		}

		public void keyTyped(KeyEvent event)
		{
		}
	}
}
