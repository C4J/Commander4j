package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameLanguageProperties extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldResourceKey;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldText;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabel1;
	private String lresourceKey;
	private String llanguageID;
	private JDBLanguage language = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextField4j JTextFieldLanguage;
	private JTextField4j JTextFieldMnemonic;

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

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldText.requestFocus();
				jTextFieldText.setCaretPosition(jTextFieldText.getText().length());

			}
		});
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(366, 145));
			this.setBounds(0, 0, 732+Common.LFAdjustWidth, 232+Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

				jDesktopPane1.setPreferredSize(new java.awt.Dimension(361, 104));
				jDesktopPane1.setLayout(null);
				{
					jTextFieldResourceKey = new JTextField4j(JDBLanguage.field_resource_key);
					jTextFieldResourceKey.setBounds(130, 10, 355, 21);
					jDesktopPane1.add(jTextFieldResourceKey);
					jTextFieldResourceKey.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldResourceKey.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldResourceKey.setEditable(false);
					jTextFieldResourceKey.setEnabled(false);
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(8, 43, 115, 21);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Resource_Key"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(8, 10, 115, 21);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Language_ID"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setBounds(8, 76, 115, 21);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Text"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabel2 = new JLabel4j_std();
					jLabel2.setBounds(8, 109, 115, 21);
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Mnemonic"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update);
					jButtonUpdate.setBounds(153, 142, 132, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							language.setText(jTextFieldText.getText());
							language.setMnemonic(JTextFieldMnemonic.getText());
							language.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{

					jButtonClose = new JButton4j(Common.icon_close);
					jButtonClose.setBounds(424, 142, 132, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jTextFieldText = new JTextField4j(JDBLanguage.field_text);
					jTextFieldText.setBounds(130, 76, 561, 21);
					jDesktopPane1.add(jTextFieldText);
					jTextFieldText.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldText.setFocusCycleRoot(true);
					jTextFieldText.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jButtonHelp.setBounds(288, 142, 132, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jTextFieldText.requestFocus();
					jTextFieldText.setCaretPosition(jTextFieldText.getText().length());

					JTextFieldLanguage = new JTextField4j(JDBLanguage.field_language);
					JTextFieldLanguage.setBounds(130, 43, 91, 21);
					JTextFieldLanguage.setPreferredSize(new Dimension(100, 20));
					JTextFieldLanguage.setHorizontalAlignment(SwingConstants.LEFT);
					JTextFieldLanguage.setEnabled(false);
					JTextFieldLanguage.setEditable(false);
					jDesktopPane1.add(JTextFieldLanguage);

					JTextFieldMnemonic = new JTextField4j(JDBLanguage.field_mnemonic);
					JTextFieldMnemonic.setBounds(130, 109, 28, 21);
					JTextFieldMnemonic.setPreferredSize(new Dimension(40, 20));
					JTextFieldMnemonic.setFocusCycleRoot(true);
					JTextFieldMnemonic.setCaretPosition(0);
					jDesktopPane1.add(JTextFieldMnemonic);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
