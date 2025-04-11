package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogDataIDProperties.java
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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBDataIDs;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JDialogDataIDProperties class is used to amend the APP_MATERIAL_DATA_IDS
 * table. The default APP_MATERIAL table holds the basic material master data.
 * However in order to extend the type of data which can be stored and also link
 * it so a customer there is another table called APP_MATERIAL_CUSTOMER_DATA.
 * This second table is keyed on MATERIAL, CUSTOMER_ID and DATA_ID. The DATA_ID
 * can be anything meaningful name to describe the new data item being stored.
 * <p>
 * <img alt="" src="./doc-files/JDialogDataIDProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameDataIDAdmin JInternalFrameDataIDAdmin
 * @see com.commander4j.db.JDBDataIDs JDBDataIDs
 */
public class JDialogDataIDProperties extends javax.swing.JDialog {
	private static final long serialVersionUID = 1;

	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldType;
	private JLabel4j_std jLabel1;
	private JDBDataIDs mt = new JDBDataIDs(Common.selectedHostID, Common.sessionID);
	private String ltype;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDesktopPane desktopPane = new JDesktopPane();

	public JDialogDataIDProperties(JFrame frame, String typ) {

		super(frame, "Data ID Properties", ModalityType.APPLICATION_MODAL);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Data ID Properties");
		this.setResizable(false);
		this.setSize( 469, 171);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);

		desktopPane.setBackground(Common.color_edit_properties);
		desktopPane.setBounds(0, 0, 469, 156);
		getContentPane().add(desktopPane);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_DATA_IDS_EDIT"));

		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
			}
		});

		jTextFieldType.setText(typ);
		setTitle(getTitle() + " - " + typ);
		ltype = typ;

		mt.setID(ltype);
		mt.getProperties();

		jTextFieldType.setText(mt.getID());
		jTextFieldDescription.setText(mt.getDescription());

		jButtonUpdate.setEnabled(false);
	}

	private void initGUI()
	{
		try
		{

			{
				getContentPane().add(desktopPane, BorderLayout.CENTER);
				{
					jLabel1 = new JLabel4j_std();
					desktopPane.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel1.setText(lang.get("lbl_Data_ID"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(6, 17, 175, 22);
				}
				{
					jTextFieldType = new JTextField4j();
					desktopPane.add(jTextFieldType, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldType.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldType.setEditable(false);
					jTextFieldType.setEnabled(false);
					jTextFieldType.setBounds(185, 17, 272, 22);
				}
				{
					jLabel3 = new JLabel4j_std();
					desktopPane.add(jLabel3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel3.setText(lang.get("lbl_Description"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(6, 49, 175, 22);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBDataIDs.field_description);
					desktopPane.add(jTextFieldDescription, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldDescription.setBounds(185, 49, 272, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					desktopPane.add(jButtonUpdate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setBounds(46, 91, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							mt.setDescription(jTextFieldDescription.getText());

							mt.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					desktopPane.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(165, 91, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					desktopPane.add(jButtonClose, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(284, 91, 112, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
