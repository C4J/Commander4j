package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogChangePassword.java
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
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPasswordField4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JUtility;

public class JDialogChangePassword extends JDialog
{
	private static final long serialVersionUID = 1;
	public boolean passwordChanged = false;
	public String password;
	public String username;

	private JButton4j btn_change;
	private JButton4j btn_cancel;

	private JTextField4j fld_userName;
	private JPasswordField4j fld_password;
	private JPasswordField4j fld_new_password;
	private JPasswordField4j fld_verify_new_password;

	final int screenWidth = 260;
	final int screenHeight = 190;
	private JLabel4j_std label;
	private JLabel4j_std label_1;
	private JLabel4j_std lblNewPassword;
	private JLabel4j_std lblVerifyPassword;

	public JDialogChangePassword(Frame parent, String DefaultUserName, String DefaultPassword)
	{
		super(parent, "Change Password", true);
		setTitle("Change Password (" + Common.hostList.getHost(Common.selectedHostID).getSiteDescription() + ")");

		fld_userName = new JTextField4j(20);
		fld_userName.setEnabled(false);
		fld_userName.setSize(149, 22);
		fld_userName.setLocation(137, 12);
		fld_userName.setDisabledTextColor(Common.color_text_disabled);
		fld_password = new JPasswordField4j(JDBUser.field_password);
		fld_password.setEnabled(false);
		fld_password.setSize(149, 22);
		fld_password.setLocation(137, 46);
		fld_password.setDisabledTextColor(Common.color_text_disabled);
		fld_new_password =  new JPasswordField4j(JDBUser.field_password);
		fld_new_password.setBounds(137, 80, 149, 22);
		
		fld_verify_new_password = new JPasswordField4j(JDBUser.field_password);
		fld_verify_new_password.setSize(149, 22);
		fld_verify_new_password.setLocation(137, 114);

		if (DefaultUserName.equals("") == false)
		{
			fld_userName.setText(DefaultUserName);
			fld_userName.setEnabled(false);
		}

		if (DefaultPassword.equals("") == false)
		{
			fld_password.setText(DefaultPassword);
			fld_password.setEnabled(false);
		}

		/* Create button objects */
		btn_change = new JButton4j("Change", Common.icon_ok);
		btn_change.setSize(111, 32);
		btn_change.setLocation(27, 148);
		btn_change.setMnemonic('L');
		btn_change.setToolTipText("Confirm password change.");
		btn_cancel = new JButton4j("Cancel", Common.icon_cancel);
		btn_cancel.setLocation(165, 148);
		btn_cancel.setSize(111, 32);
		btn_cancel.setMnemonic('C');
		btn_cancel.setToolTipText("Cancel password change.");

		ButtonHandler buttonhandler = new ButtonHandler();

		btn_change.addActionListener(buttonhandler);
		btn_cancel.addActionListener(buttonhandler);

		KeyboardHandler keyboardhandler = new KeyboardHandler();

		btn_change.addKeyListener(keyboardhandler);
		btn_cancel.addKeyListener(keyboardhandler);
		fld_userName.addKeyListener(keyboardhandler);
		fld_password.addKeyListener(keyboardhandler);
		fld_new_password.addKeyListener(keyboardhandler);
		fld_verify_new_password.addKeyListener(keyboardhandler);

		addKeyListener(keyboardhandler);

		fld_userName.setEditable(false);
		fld_password.setEditable(false);

		getRootPane().setDefaultButton(btn_change);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(304, 213);
		setLocation((screenSize.width - screenWidth) / 2, (screenSize.height - screenHeight) / 2);
		setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		getContentPane().add(fld_userName);
		getContentPane().add(fld_password);
		getContentPane().add(fld_new_password);
		getContentPane().add(fld_verify_new_password);
		getContentPane().add(btn_change);
		getContentPane().add(btn_cancel);
		
		label = new JLabel4j_std("Username :");
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		label.setBounds(12, 12, 111, 22);
		getContentPane().add(label);
		
		label_1 = new JLabel4j_std("Password :");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setBounds(12, 46, 111, 22);
		getContentPane().add(label_1);
		
		lblNewPassword = new JLabel4j_std("New Password :");
		lblNewPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewPassword.setBounds(12, 80, 111, 22);
		getContentPane().add(lblNewPassword);
		
		lblVerifyPassword = new JLabel4j_std("Verify Password :");
		lblVerifyPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblVerifyPassword.setBounds(12, 114, 111, 22);
		getContentPane().add(lblVerifyPassword);
		setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				fld_new_password.requestFocus();
				fld_new_password.setCaretPosition(fld_new_password.getText().length());
			}
		});
	}

	private class KeyboardHandler implements KeyListener
	{
		// handle press of any key
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == 27)
			{
				passwordChanged = false;
				username = "";
				password = "";
				dispose();
			}
		}

		// handle release of any key
		public void keyReleased(KeyEvent event) {
		}

		// handle press of an action key
		public void keyTyped(KeyEvent event) {
		}
	}

	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == btn_change)
			{
				password = new String(fld_password.getPassword());
				username = fld_userName.getText().toUpperCase();
				String pass1 = new String(fld_new_password.getPassword());
				String pass2 = new String(fld_verify_new_password.getPassword());
				JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);

				user.setUserId(username);
				user.setLoginPassword(password);
				user.setPasswordNew(pass1);
				user.setPasswordVerify(pass2);
				
				if (user.changePassword())
				{
					passwordChanged = true;
					dispose();
				}
				else
				{
					passwordChanged = false;
					fld_new_password.setText("");
					fld_verify_new_password.setText("");
					fld_new_password.requestFocus();
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(null,"<html>"+user.getErrorMessage()+"</html>","Password Error",JOptionPane.ERROR_MESSAGE);
				}
			}

			if (event.getSource() == btn_cancel)
			{
				passwordChanged = false;
				username = "";
				password = "";
				dispose();
			}
		}
	}
}
