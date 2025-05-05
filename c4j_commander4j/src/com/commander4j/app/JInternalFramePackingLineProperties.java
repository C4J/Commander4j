package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePackingLineProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBPackingLines;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;


public class JInternalFramePackingLineProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldPackingLineID;
	private JLabel4j_std jLabel_PackingLineID;
	private JDBPackingLines packingdb = new JDBPackingLines(Common.selectedHostID, Common.sessionID);
	private String lpackingline;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");

	public JInternalFramePackingLineProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PACKING_LINES"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
			}
		});
	}

	public JInternalFramePackingLineProperties(String packingline)
	{

		this();

		jTextFieldPackingLineID.setText(packingline);
		setTitle(getTitle() + " - " + packingline);
		lpackingline = packingline;

		packingdb.setPackingLineID(lpackingline);
		packingdb.getProperties();

		jTextFieldPackingLineID.setText(packingdb.getPackingLineID());
		jTextFieldDescription.setText(packingdb.getDescription());
		chckbxEnabled.setSelected(packingdb.isEnabled());
		
		jButtonUpdate.setEnabled(false);

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 466, 180);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_PackingLineID = new JLabel4j_std();
					jLabel_PackingLineID.setBounds(12, 12, 129, 22);
					jDesktopPane1.add(jLabel_PackingLineID);
					jLabel_PackingLineID.setText(lang.get("lbl_Packing_Line_ID"));
					jLabel_PackingLineID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_PackingLineID.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldPackingLineID = new JTextField4j(JDBPackingLines.field_packing_line_id);
					jTextFieldPackingLineID.setBounds(148, 12, 170, 22);
					jDesktopPane1.add(jTextFieldPackingLineID);
					jTextFieldPackingLineID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldPackingLineID.setEditable(false);
					jTextFieldPackingLineID.setEnabled(false);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jLabelDescription.setBounds(12, 45, 129, 22);
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBPackingLines.field_description);
					jTextFieldDescription.setBounds(148, 45, 265, 22);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(38, 106, 123, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							packingdb.setDescription(jTextFieldDescription.getText());
							packingdb.setEnabled(chckbxEnabled.isSelected());
							packingdb.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(164, 106, 123, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(290, 106, 123, 32);
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
				}

				JLabel4j_std label_Enabled = new JLabel4j_std();
				label_Enabled.setBounds(12, 74, 129, 22);
				label_Enabled.setText(lang.get("lbl_Enabled"));
				label_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
				jDesktopPane1.add(label_Enabled);
				chckbxEnabled.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				

				chckbxEnabled.setBounds(148, 74, 21, 22);
				jDesktopPane1.add(chckbxEnabled);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
