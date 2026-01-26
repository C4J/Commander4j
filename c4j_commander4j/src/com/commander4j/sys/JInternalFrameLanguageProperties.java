package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameLanguageProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameLanguageProperties class is a form which displays allows
 * you to edit a record in the SYS_LANGUAGE table
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameLanguageProperties.jpg" >
 *
 * @see com.commander4j.db.JDBLanguage JDBLanguage
 * @see com.commander4j.sys.JInternalFrameLanguageAdmin
 *      JInternalFrameLanguageAdmin
 * @see com.commander4j.bean.JLanguage JLanguage
 */
public class JInternalFrameLanguageProperties extends javax.swing.JInternalFrame
{
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBLanguage language = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Resource;
	private JLabel4j_std jLabel_LanguageID;
	private JLabel4j_std jLabel_Mnemonic;
	private JLabel4j_std jLabel_Text;
	private JTextField4j JTextFieldLanguage;
	private JTextField4j JTextFieldMnemonic;
	private JTextField4j jTextFieldResourceKey;
	private JTextField4j jTextFieldText;
	private String llanguageID;
	private String lresourceKey;
	private static final long serialVersionUID = 1;

	public JInternalFrameLanguageProperties()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_LANGUAGE_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
	}

	public JInternalFrameLanguageProperties(String resourceKey, String languageID)
	{

		this();

		setTitle(getTitle() + " - " + resourceKey + " [" + languageID + "]");
		lresourceKey = resourceKey;
		llanguageID = languageID;

		language.getProperties(lresourceKey, llanguageID);

		jTextFieldResourceKey.setText(resourceKey);
		JTextFieldLanguage.setText(languageID);
		jTextFieldText.setText(language.getText());
		JTextFieldMnemonic.setText(String.valueOf(language.getMnemonicChar()));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldText.requestFocus();
				jTextFieldText.setCaretPosition(jTextFieldText.getText().length());

			}
		});
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(366, 145));
			this.setBounds(0, 0, 720, 212);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

			jDesktopPane1.setPreferredSize(new java.awt.Dimension(361, 104));
			jDesktopPane1.setLayout(null);

			jTextFieldResourceKey = new JTextField4j(JDBLanguage.field_resource_key);
			jTextFieldResourceKey.setBounds(130, 8, 355, 22);
			jDesktopPane1.add(jTextFieldResourceKey);
			jTextFieldResourceKey.setPreferredSize(new java.awt.Dimension(100, 20));
			jTextFieldResourceKey.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldResourceKey.setEditable(false);
			jTextFieldResourceKey.setEnabled(false);

			jLabel_Resource = new JLabel4j_std();
			jLabel_Resource.setBounds(8, 40, 115, 22);
			jDesktopPane1.add(jLabel_Resource);
			jLabel_Resource.setText(lang.get("lbl_Resource_Key"));
			jLabel_Resource.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Resource.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_LanguageID = new JLabel4j_std();
			jLabel_LanguageID.setBounds(8, 8, 115, 22);
			jDesktopPane1.add(jLabel_LanguageID);
			jLabel_LanguageID.setText(lang.get("lbl_Language_ID"));
			jLabel_LanguageID.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_LanguageID.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_Text = new JLabel4j_std();
			jLabel_Text.setBounds(8, 72, 115, 22);
			jDesktopPane1.add(jLabel_Text);
			jLabel_Text.setText(lang.get("lbl_Text"));
			jLabel_Text.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Text.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_Mnemonic = new JLabel4j_std();
			jLabel_Mnemonic.setBounds(8, 104, 115, 22);
			jDesktopPane1.add(jLabel_Mnemonic);
			jLabel_Mnemonic.setText(lang.get("lbl_Mnemonic"));
			jLabel_Mnemonic.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Mnemonic.setHorizontalTextPosition(SwingConstants.RIGHT);

			jButtonUpdate = new JButton4j(Common.icon_update_16x16);
			jButtonUpdate.setBounds(153, 137, 132, 32);
			jDesktopPane1.add(jButtonUpdate);
			jButtonUpdate.setText(lang.get("btn_Save"));
			jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonUpdate.setMnemonic(lang.getMnemonicChar());
			jButtonUpdate.setEnabled(false);
			jButtonUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					language.setText(jTextFieldText.getText());
					language.setMnemonic(JTextFieldMnemonic.getText());
					language.update();
					jButtonUpdate.setEnabled(false);
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jButtonClose.setBounds(424, 137, 132, 32);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jTextFieldText = new JTextField4j(JDBLanguage.field_text);
			jTextFieldText.setBounds(130, 72, 561, 22);
			jDesktopPane1.add(jTextFieldText);
			jTextFieldText.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextFieldText.setFocusCycleRoot(true);
			jTextFieldText.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setBounds(288, 137, 132, 32);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jTextFieldText.requestFocus();
			jTextFieldText.setCaretPosition(jTextFieldText.getText().length());

			JTextFieldLanguage = new JTextField4j(JDBLanguage.field_language);
			JTextFieldLanguage.setBounds(130, 40, 91, 22);
			JTextFieldLanguage.setPreferredSize(new Dimension(100, 20));
			JTextFieldLanguage.setHorizontalAlignment(SwingConstants.LEFT);
			JTextFieldLanguage.setEnabled(false);
			JTextFieldLanguage.setEditable(false);
			jDesktopPane1.add(JTextFieldLanguage);

			JTextFieldMnemonic = new JTextField4j(JDBLanguage.field_mnemonic);
			JTextFieldMnemonic.setBounds(130, 104, 28, 22);
			JTextFieldMnemonic.setPreferredSize(new Dimension(40, 20));
			JTextFieldMnemonic.setFocusCycleRoot(true);
			JTextFieldMnemonic.setCaretPosition(0);
			jDesktopPane1.add(JTextFieldMnemonic);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
