package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDialogeGroupProperties.java
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
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPasswordField4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JDialogeGroupProperties class is a form which allows a user edit a record
 * within the table SYS_GROUPS.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogeGroupProperties.jpg" >
 *
 * @see com.commander4j.sys.JInternalFrameGroupAdmin JInternalFrameGroupAdmin
 * @see com.commander4j.sys.JInternalFrameGroupPermissions
 *      JInternalFrameGroupPermissions
 * @see com.commander4j.db.JDBGroup JDBGroup
 * @see com.commander4j.db.JDBGroupPermissions JDBGroupPermissions
 */
public class JDialogUserProperties extends JDialog
{

	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JCalendarButton calendarButton;
	private JCheckBox4j jCheckBoxAccountEnabled;
	private JCheckBox4j jCheckBoxAccountExpires;
	private JCheckBox4j jCheckBoxAccountLocked;
	private JCheckBox4j jCheckBoxPasswordChangeAllowed;
	private JCheckBox4j jCheckBoxPasswordChangeRequired;
	private JCheckBox4j jCheckBoxPasswordExpires;
	private JComboBox4j<String> jComboBoxLanguage;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBUser user = new JDBUser(Common.selectedHostID, Common.sessionID);
	private JDateControl lbl_accountExpiryDate = new JDateControl();
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusText = new JLabel4j_status();
	private JLabel4j_std jLabelAccountExpiryDate;
	private JLabel4j_std jLabel_Email;
	private JLabel4j_std lbl_AccountExpires;
	private JLabel4j_std lbl_AccountLocked;
	private JLabel4j_std lbl_BadPasswords;
	private JLabel4j_std lbl_ChangeAllowed;
	private JLabel4j_std lbl_Comment;
	private JLabel4j_std lbl_Language;
	private JLabel4j_std lbl_LastLogon;
	private JLabel4j_std lbl_Password1;
	private JLabel4j_std lbl_Password2;
	private JLabel4j_std lbl_PasswordChanged;
	private JLabel4j_std lbl_PasswordExpires;
	private JLabel4j_std lbl_UserID;
	private JPasswordField4j jPasswordField1;
	private JPasswordField4j jPasswordField2;
	private JTextField4j jTextFieldBadPasswords;
	private JTextField4j jTextFieldComment;
	private JTextField4j jTextFieldLastLogon;
	private JTextField4j jTextFieldLastPasswordChange;
	private JTextField4j jTextFieldUserID;
	private JTextField4j lbl_EmailAddress;
	private Object currentLanguage = new Object();
	private Object newLanguage = new Object();
	private String luserid;
	private boolean modified_Enabled = false;
	private boolean modified_Locked = false;
	private boolean newUser = false;
	private boolean userPasswordUpdated;
	private boolean userUpdated;
	private static final long serialVersionUID = 1;

	public JDialogUserProperties(JFrame parent, String userid)
	{

		super(parent);
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_USER_EDIT"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldComment.requestFocus();
				jTextFieldComment.setCaretPosition(jTextFieldComment.getText().length());

			}
		});

		jTextFieldUserID.setText(userid);

		setTitle(getTitle() + " - " + userid);
		luserid = userid;

		user.setUserId(luserid);
		displayUserProperties();

	}

	private void displayUserProperties()
	{
		if (user.getUserProperties())
		{
			newUser = false;
		}
		else
		{
			newUser = true;
		}

		jTextFieldComment.setText(user.getComment());
		lbl_EmailAddress.setText(user.getEmailAddress());
		if (user.isPasswordEncrypted())
		{
			jPasswordField1.setText(user.getDecodedPassword());
		}
		else
		{
			jPasswordField1.setText(user.getPassword());
		}
		jPasswordField2.setText(user.getDecodedPassword());
		jComboBoxLanguage.setSelectedItem(user.getLanguage());

		try
		{
			jTextFieldLastLogon.setText(DateFormat.getDateTimeInstance().format(user.getLastLoginTimestamp()));
		}
		catch (Exception e)
		{
			jTextFieldLastLogon.setText("Never");
		}

		try
		{
			jTextFieldLastPasswordChange.setText(DateFormat.getDateTimeInstance().format(user.getPasswordChanged()));
		}
		catch (Exception e)
		{
			jTextFieldLastLogon.setText("Never");
		}

		if (user.getAccountLocked().equals("Y"))
			jCheckBoxAccountLocked.setSelected(true);
		else
			jCheckBoxAccountLocked.setSelected(false);

		if (user.isAccountEnabled())
			jCheckBoxAccountEnabled.setSelected(true);
		else
			jCheckBoxAccountEnabled.setSelected(false);

		if (user.isAccountExpiring())
			jCheckBoxAccountExpires.setSelected(true);
		else
			jCheckBoxAccountExpires.setSelected(false);

		setExpiryDateVisibility();

		if (user.isPasswordChangeAllowed())
			jCheckBoxPasswordChangeAllowed.setSelected(true);
		else
			jCheckBoxPasswordChangeAllowed.setSelected(false);

		if (user.isPasswordChangeRequired())
			jCheckBoxPasswordChangeRequired.setSelected(true);
		else
			jCheckBoxPasswordChangeRequired.setSelected(false);

		if (user.isPasswordExpiring())
			jCheckBoxPasswordExpires.setSelected(true);
		else
			jCheckBoxPasswordExpires.setSelected(false);

		jTextFieldBadPasswords.setText("" + user.getBadPasswordAttempts());

		try
		{
			lbl_accountExpiryDate.setBounds(201, 392, 120, 22);
			lbl_accountExpiryDate.setDate(user.getAccountExpiryDate());
		}
		catch (Exception e)
		{

		}

		resetChanges();

	}

	private void initGUI()
	{
		try
		{
			setPreferredSize(new java.awt.Dimension(366, 145));
			setBounds(0, 0, 428, 586);
			setModal(true);
			setTitle("User Properties");
			setResizable(false);

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(361, 104));
			jDesktopPane1.setLayout(null);

			preInitGUI();

			jDesktopPane1 = new JDesktopPane4j();

			lbl_Comment = new JLabel4j_std();
			lbl_Comment.setBounds(0, 40, 163, 22);
			lbl_Password1 = new JLabel4j_std();
			lbl_Password1.setBounds(0, 72, 163, 22);
			lbl_UserID = new JLabel4j_std();
			lbl_UserID.setBounds(0, 7, 163, 22);
			lbl_Password2 = new JLabel4j_std();
			lbl_Password2.setBounds(0, 104, 163, 22);
			jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
			jTextFieldUserID.setBounds(172, 8, 150, 22);
			jTextFieldComment = new JTextField4j(JDBUser.field_comment);
			jTextFieldComment.setBounds(172, 40, 217, 22);
			jPasswordField1 = new JPasswordField4j(JDBUser.field_password);
			jPasswordField1.setBounds(172, 72, 150, 22);
			jPasswordField2 = new JPasswordField4j(JDBUser.field_password);
			jPasswordField2.setBounds(172, 104, 150, 22);
			jComboBoxLanguage = new JComboBox4j<String>();
			jComboBoxLanguage.setBounds(172, 136, 69, 22);
			jComboBoxLanguage.setModel(new DefaultComboBoxModel<String>(Common.languages));
			jTextFieldLastLogon = new JTextField4j();
			jTextFieldLastLogon.setBounds(172, 168, 150, 22);
			jTextFieldLastPasswordChange = new JTextField4j();
			jTextFieldLastPasswordChange.setBounds(172, 200, 150, 22);
			lbl_Language = new JLabel4j_std();
			lbl_Language.setBounds(0, 136, 163, 22);
			lbl_LastLogon = new JLabel4j_std();
			lbl_LastLogon.setBounds(0, 168, 163, 22);
			lbl_PasswordChanged = new JLabel4j_std();
			lbl_PasswordChanged.setBounds(0, 200, 163, 22);
			lbl_AccountLocked = new JLabel4j_std();
			lbl_AccountLocked.setBounds(0, 264, 163, 22);
			lbl_PasswordExpires = new JLabel4j_std();
			lbl_PasswordExpires.setBounds(0, 296, 163, 22);
			lbl_BadPasswords = new JLabel4j_std();
			lbl_BadPasswords.setBounds(0, 360, 163, 22);
			jTextFieldBadPasswords = new JTextField4j();
			jTextFieldBadPasswords.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldBadPasswords.setBounds(172, 360, 30, 22);
			lbl_ChangeAllowed = new JLabel4j_std();
			lbl_ChangeAllowed.setBounds(0, 328, 163, 22);
			jCheckBoxAccountLocked = new JCheckBox4j();
			jCheckBoxAccountLocked.setBounds(170, 264, 22, 22);
			jCheckBoxPasswordExpires = new JCheckBox4j();
			jCheckBoxPasswordExpires.setBounds(169, 296, 22, 22);
			jCheckBoxPasswordChangeAllowed = new JCheckBox4j();
			jCheckBoxPasswordChangeAllowed.setBounds(169, 328, 22, 22);

			jCheckBoxAccountEnabled = new JCheckBox4j();

			jCheckBoxAccountEnabled.setBounds(170, 232, 22, 22);
			jCheckBoxAccountEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jCheckBoxAccountEnabledActionPerformed(evt);
				}
			});
			jDesktopPane1.add(jCheckBoxAccountEnabled);

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jButtonSave.setBounds(20, 488, 112, 32);
			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jButtonCancel.setBounds(284, 488, 112, 32);

			BorderLayout thisLayout = new BorderLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.setHgap(0);
			thisLayout.setVgap(0);

			jDesktopPane1.setPreferredSize(new java.awt.Dimension(364, 329));
			jDesktopPane1.setOpaque(true);
			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			lbl_Comment.setText(lang.get("lbl_Description"));
			jDesktopPane1.add(lbl_Comment);
			lbl_Comment.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_Password1.setText(lang.get("lbl_User_Account_Password"));
			jDesktopPane1.add(lbl_Password1);
			lbl_Password1.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_UserID.setText(lang.get("lbl_User_ID"));

			jDesktopPane1.add(lbl_UserID);
			lbl_UserID.setFocusTraversalKeysEnabled(false);
			lbl_UserID.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_Password2.setText(lang.get("lbl_User_Account_Password_Verify"));
			jDesktopPane1.add(lbl_Password2);
			lbl_Password2.setHorizontalAlignment(SwingConstants.TRAILING);

			jCheckBoxPasswordChangeRequired = new JCheckBox4j();

			jCheckBoxPasswordChangeRequired.setBounds(169, 424, 22, 22);
			jDesktopPane1.add(jCheckBoxPasswordChangeRequired);
			jCheckBoxPasswordChangeRequired.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jCheckBoxPasswordChangeRequiredActionPerformed(evt);
				}
			});

			jTextFieldUserID.setEditable(false);
			jTextFieldUserID.setEnabled(false);
			jTextFieldUserID.setPreferredSize(new java.awt.Dimension(100, 20));
			jTextFieldUserID.setDisabledTextColor(Common.color_text_disabled);
			jDesktopPane1.add(jTextFieldUserID);

			jDesktopPane1.add(jTextFieldComment);
			jTextFieldComment.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jTextFieldCommentKeyTyped(evt);
				}
			});

			jPasswordField1.setPreferredSize(new java.awt.Dimension(100, 20));
			jDesktopPane1.add(jPasswordField1);
			jPasswordField1.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jPasswordField1KeyTyped(evt);
				}
			});

			jPasswordField2.setPreferredSize(new java.awt.Dimension(100, 20));
			jDesktopPane1.add(jPasswordField2);
			jPasswordField2.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jPasswordField2KeyTyped(evt);
				}
			});

			jComboBoxLanguage.setEnabled(true);
			jComboBoxLanguage.setEditable(false);
			jComboBoxLanguage.setLightWeightPopupEnabled(true);
			jComboBoxLanguage.setPreferredSize(new java.awt.Dimension(45, 21));
			jComboBoxLanguage.setIgnoreRepaint(false);
			jDesktopPane1.add(jComboBoxLanguage);
			jComboBoxLanguage.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jComboBoxLanguageActionPerformed(evt);
				}
			});

			jTextFieldLastLogon.setEditable(false);
			jTextFieldLastLogon.setPreferredSize(new java.awt.Dimension(150, 20));
			jDesktopPane1.add(jTextFieldLastLogon);
			jTextFieldLastLogon.setEnabled(false);
			jTextFieldLastLogon.setDisabledTextColor(Common.color_text_disabled);

			jTextFieldLastPasswordChange.setEditable(false);
			jTextFieldLastPasswordChange.setPreferredSize(new java.awt.Dimension(150, 20));
			jDesktopPane1.add(jTextFieldLastPasswordChange);
			jTextFieldLastPasswordChange.setEnabled(false);
			jTextFieldLastPasswordChange.setDisabledTextColor(Common.color_text_disabled);

			lbl_Language.setText(lang.get("lbl_Language"));
			jDesktopPane1.add(lbl_Language);
			lbl_Language.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_LastLogon.setText(lang.get("lbl_User_Account_Last_Logon"));
			jDesktopPane1.add(lbl_LastLogon);
			lbl_LastLogon.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_PasswordChanged.setText(lang.get("lbl_User_Account_Last_Password_Change"));
			jDesktopPane1.add(lbl_PasswordChanged);
			lbl_PasswordChanged.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_AccountLocked.setText(lang.get("lbl_User_Account_Locked"));
			jDesktopPane1.add(lbl_AccountLocked);
			lbl_AccountLocked.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_PasswordExpires.setText(lang.get("lbl_User_Account_Password_Expires"));
			jDesktopPane1.add(lbl_PasswordExpires);
			lbl_PasswordExpires.setHorizontalAlignment(SwingConstants.TRAILING);

			lbl_BadPasswords.setText(lang.get("lbl_User_Account_Bad_Passwords"));
			jDesktopPane1.add(lbl_BadPasswords);
			lbl_BadPasswords.setFocusTraversalKeysEnabled(false);
			lbl_BadPasswords.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldBadPasswords.setEditable(false);
			jTextFieldBadPasswords.setPreferredSize(new java.awt.Dimension(20, 20));
			jDesktopPane1.add(jTextFieldBadPasswords);
			jTextFieldBadPasswords.setEnabled(false);
			jTextFieldBadPasswords.setDisabledTextColor(Common.color_text_disabled);

			lbl_ChangeAllowed.setText(lang.get("lbl_User_Account_Password_Change_Allowed"));
			jDesktopPane1.add(lbl_ChangeAllowed);
			lbl_ChangeAllowed.setHorizontalAlignment(SwingConstants.TRAILING);

			jDesktopPane1.add(jCheckBoxAccountLocked);

			jCheckBoxAccountLocked.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jCheckBoxAccountLockedActionPerformed(evt);
				}
			});

			jDesktopPane1.add(jCheckBoxPasswordExpires);

			jCheckBoxPasswordExpires.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jCheckBoxPasswordExpiresActionPerformed(evt);
				}
			});

			jDesktopPane1.add(jCheckBoxPasswordChangeAllowed);

			jCheckBoxPasswordChangeAllowed.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jCheckBoxPasswordChangeAllowedActionPerformed(evt);
				}
			});

			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jDesktopPane1.add(jButtonSave);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonUpdateActionPerformed(evt);
				}
			});

			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jDesktopPane1.add(jButtonCancel);
			{
				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jButtonHelp.setBounds(152, 488, 112, 32);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setMnemonic(lang.getMnemonicChar());
			}
			{
				jCheckBoxAccountExpires = new JCheckBox4j();
				jCheckBoxAccountExpires.setBounds(169, 392, 22, 22);
				jDesktopPane1.add(jCheckBoxAccountExpires);

				jCheckBoxAccountExpires.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jCheckBoxAccountExpiresActionPerformed(evt);
					}
				});
			}
			{
				lbl_AccountExpires = new JLabel4j_std();
				lbl_AccountExpires.setBounds(0, 392, 163, 22);
				jDesktopPane1.add(lbl_AccountExpires);
				lbl_AccountExpires.setText(lang.get("lbl_User_Account_Expires"));
				lbl_AccountExpires.setHorizontalAlignment(SwingConstants.TRAILING);
			}
			{
				jLabelAccountExpiryDate = new JLabel4j_std();
				jLabelAccountExpiryDate.setBounds(0, 424, 163, 22);
				jDesktopPane1.add(jLabelAccountExpiryDate);
				jLabelAccountExpiryDate.setText(lang.get("lbl_Password_Change_Required"));
				jLabelAccountExpiryDate.setHorizontalAlignment(SwingConstants.TRAILING);
			}
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonCancelActionPerformed(evt);
				}
			});

			lbl_accountExpiryDate.getEditor().addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					userUpdated = true;
					jButtonSave.setEnabled(true);
				}
			});
			lbl_accountExpiryDate.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)

				{
					userUpdated = true;
					jButtonSave.setEnabled(true);
				}
			});
			jDesktopPane1.add(lbl_accountExpiryDate);

			lbl_EmailAddress = new JTextField4j();
			lbl_EmailAddress.setBounds(172, 456, 217, 22);
			lbl_EmailAddress.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(final KeyEvent e)
				{
					userUpdated = true;
					jButtonSave.setEnabled(true);
				}
			});
			jDesktopPane1.add(lbl_EmailAddress);

			jLabel_Email = new JLabel4j_std();
			jLabel_Email.setBounds(0, 456, 163, 22);
			jLabel_Email.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Email.setText(lang.get("lbl_User_Account_Email"));
			jDesktopPane1.add(jLabel_Email);

			calendarButton = new JCalendarButton(lbl_accountExpiryDate);
			calendarButton.setBounds(320, 392, 22, 22);
			calendarButton.setEnabled(false);
			jDesktopPane1.add(calendarButton);

			JLabel4j_std lbl_AccountEnabled = new JLabel4j_std();
			lbl_AccountEnabled.setText(lang.get("lbl_User_Account_Enabled"));
			lbl_AccountEnabled.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_AccountEnabled.setBounds(0, 232, 163, 22);
			jDesktopPane1.add(lbl_AccountEnabled);

			JButton4j jButtonLock = new JButton4j(Common.icon_lock_16x16);
			jButtonLock.setToolTipText("Assign system generated password (also copied to clipboard).");
			jButtonLock.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					randomPassword();
				}
			});
			jButtonLock.setBounds(322, 72, 22, 22);
			jDesktopPane1.add(jButtonLock);

			jStatusText.setBounds(5, 532, 418, 21);

			jDesktopPane1.add(jStatusText);

			postInitGUI();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void preInitGUI()
	{
	}

	public void postInitGUI()
	{
		jComboBoxLanguage.addItem("EN");
		jComboBoxLanguage.addItem("DE");
		jComboBoxLanguage.addItem("FR");
		jComboBoxLanguage.addItem("HU");
		jComboBoxLanguage.addItem("IT");
		jComboBoxLanguage.addItem("NL");

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		resetChanges();

	}

	protected void jButtonUpdateActionPerformed(ActionEvent evt)

	{

		if (userUpdated == true)
		{

			user.setComment(jTextFieldComment.getText());
			user.setEmailAddress(lbl_EmailAddress.getText());

			if (jCheckBoxPasswordChangeAllowed.isSelected())
				user.setPasswordChangeAllowed("Y");
			else
				user.setPasswordChangeAllowed("N");

			if (jCheckBoxPasswordChangeRequired.isSelected())
				user.setPasswordChangeRequired("Y");
			else
				user.setPasswordChangeRequired("N");

			if (jCheckBoxPasswordExpires.isSelected())
				user.setPasswordExpires("Y");
			else
				user.setPasswordExpires("N");

			if (jCheckBoxAccountExpires.isSelected())
				user.setAccountExpires("Y");
			else
				user.setAccountExpires("N");

			user.setLanguage((String) jComboBoxLanguage.getSelectedItem());

			Date d = lbl_accountExpiryDate.getDate();
			user.setAccountExpiryDate(JUtility.getTimestampFromDate(d));

			String pass1 = new String(jPasswordField1.getPassword());
			String pass2 = new String(jPasswordField2.getPassword());
			user.setPasswordNew(pass1);
			user.setPasswordVerify(pass2);

			if (newUser)
			{
				user.create(luserid, Common.userList.getUser(Common.sessionID).getUserId());
				if (userPasswordUpdated == false)
				{
					pass1 = randomPassword();
					pass2 = pass1;
					userPasswordUpdated = true;
				}
				newUser = false;
			}

			if (userPasswordUpdated)
			{
				if (user.isNewPasswordValid())
				{
					if (user.isPasswordComplex(pass1))
					{
						user.changePassword();
						userPasswordUpdated = false;
					}
					else
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(null, "<html>" + user.getErrorMessage() + "</html>", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(null, "<html>" + user.getErrorMessage() + "</html>", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				}
			}

			if (modified_Locked)
			{
				if (jCheckBoxAccountLocked.isSelected())
					user.lock(luserid, Common.userList.getUser(Common.sessionID).getUserId());
				else
					user.unlock(Common.userList.getUser(Common.sessionID).getUserId());

				modified_Locked = false;
			}

			if (modified_Enabled)
			{
				if (jCheckBoxAccountEnabled.isSelected())
					user.enable(luserid, Common.userList.getUser(Common.sessionID).getUserId());
				else
					user.disable(luserid, Common.userList.getUser(Common.sessionID).getUserId());

				modified_Enabled = false;
			}

			if (userUpdated)
			{
				if (jCheckBoxPasswordChangeRequired.isSelected())
					user.setPasswordChangeRequired("Y");
				else
					user.setPasswordChangeRequired("N");

				if (user.update())
				{
					userUpdated = false;
					resetChanges();
				}
				else
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(null, user.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	protected void jButtonCancelActionPerformed(ActionEvent evt)
	{
		dispose();
	}

	protected void jTextFieldCommentKeyTyped(KeyEvent evt)
	{
		userUpdated = true;
		jButtonSave.setEnabled(true);

	}

	protected void jPasswordField1KeyTyped(KeyEvent evt)
	{
		userPasswordUpdated = true;
		userUpdated = true;
		jButtonSave.setEnabled(true);
	}

	protected void jPasswordField2KeyTyped(KeyEvent evt)
	{
		userPasswordUpdated = true;
		userUpdated = true;
		jButtonSave.setEnabled(true);
	}

	protected void jComboBoxLanguageActionPerformed(ActionEvent evt)
	{
		newLanguage = jComboBoxLanguage.getSelectedItem();
		if (newLanguage.equals(currentLanguage) != true)
		{
			userUpdated = true;
			jButtonSave.setEnabled(true);
		}
	}

	protected void jCheckBoxAccountLockedActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		jButtonSave.setEnabled(true);
		modified_Locked = true;
	}

	protected void jCheckBoxAccountEnabledActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		jButtonSave.setEnabled(true);
		modified_Enabled = true;
	}

	protected void jCheckBoxPasswordExpiresActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		jButtonSave.setEnabled(true);
	}

	protected void jCheckBoxPasswordChangeAllowedActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		if (jCheckBoxPasswordChangeAllowed.isSelected() == false)
		{
			jCheckBoxPasswordExpires.setSelected(false);
			jCheckBoxPasswordChangeRequired.setSelected(false);
		}
		jButtonSave.setEnabled(true);
	}

	protected void jCheckBoxPasswordChangeRequiredActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		if (jCheckBoxPasswordChangeRequired.isSelected() == true)
		{
			jCheckBoxPasswordChangeAllowed.setSelected(true);
		}
		jButtonSave.setEnabled(true);
	}

	private void jCheckBoxAccountExpiresActionPerformed(ActionEvent evt)
	{
		userUpdated = true;
		jButtonSave.setEnabled(true);
		setExpiryDateVisibility();
	}

	private void resetChanges()
	{
		userUpdated = false;

		currentLanguage = jComboBoxLanguage.getSelectedItem();
		jButtonSave.setEnabled(false);
	}

	private void setExpiryDateVisibility()
	{
		if (jCheckBoxAccountExpires.isSelected())
		{
			calendarButton.setEnabled(true);
			lbl_accountExpiryDate.setEnabled(true);
			lbl_accountExpiryDate.setDisplayMode(JDateControl.mode_disable_visible);
		}
		else
		{
			lbl_accountExpiryDate.setEnabled(false);
			calendarButton.setEnabled(false);
			lbl_accountExpiryDate.setDisplayMode(JDateControl.mode_disable_not_visible);
		}
	}

	private String randomPassword()
	{
		String result = "";
		String genPass = user.generateRandomPassword();
		user.setPasswordNew(genPass);
		user.setPasswordVerify(genPass);
		jPasswordField1.setText(genPass);
		jPasswordField2.setText(genPass);
		userPasswordUpdated = true;
		userUpdated = true;
		jButtonSave.setEnabled(true);
		StringSelection stringSelection = new StringSelection(genPass);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		jStatusText.setText("New password copied to clipboard.");
		result = genPass;
		return result;
	}

}
