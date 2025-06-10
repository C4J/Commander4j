package com.commander4j.cfg;

import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
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


		ButtonHandler buttonhandler = new ButtonHandler();
		getContentPane().setLayout(null);

		btn_close = new JButton4j("Close", Common.icon_cancel_16x16);
		btn_close.setBounds(142, 52, 104, 32);
		btn_close.setText("Cancel");
		getContentPane().add(btn_close);
		
		btn_login = new JButton4j("Login", Common.icon_ok_16x16);
		btn_login.setBounds(19, 52, 104, 32);
		btn_login.setText("OK");
		getContentPane().add(btn_login);
		
		btn_login.addActionListener(buttonhandler);
		btn_close.addActionListener(buttonhandler);

		getRootPane().setDefaultButton(btn_login);

		fld_password = new JPasswordField(10);
		fld_password.setBounds(110, 18, 150, 22);
		getContentPane().add(fld_password);
		
		AbstractDocument doc2 = (AbstractDocument) fld_password.getDocument();
		doc2.setDocumentFilter(new JFixedSizeFilter(JDBUser.field_password));

		JLabel4j_std lblPassword = new JLabel4j_std("Password :");
		lblPassword.setBounds(19, 18, 83, 22);
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		getContentPane().add(lblPassword);

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setSize(284, 128);
		
		GraphicsDevice gd = JUtility.getGraphicsDevice();
		
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Rectangle screenBounds = gc.getBounds();

		setBounds(screenBounds.x + ((screenBounds.width - JDialogSetupPassword.this.getWidth()) / 2), screenBounds.y + ((screenBounds.height - JDialogSetupPassword.this.getHeight()) / 2), JDialogSetupPassword.this.getWidth(), JDialogSetupPassword.this.getHeight());

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
