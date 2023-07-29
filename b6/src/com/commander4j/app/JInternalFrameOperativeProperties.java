package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMHNDecisionProperties.java
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

import org.apache.commons.lang.WordUtils;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBOperatives;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameOperativeProperties class allows the user to edit a single
 * record the APP_OPERATIVES table.
 */
public class JInternalFrameOperativeProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldSurname;
	private JLabel4j_std jLabelSurname;
	private JTextField4j jTextFieldForename;
	private JTextField4j jTextFieldID;
	private JLabel4j_std jLabel_ID;
	private JDBOperatives operativedb = new JDBOperatives(Common.selectedHostID, Common.sessionID);
	private String lid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");

	public JInternalFrameOperativeProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_OPERATIVES"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldForename.requestFocus();
				jTextFieldForename.setCaretPosition(jTextFieldForename.getText().length());

			}
		});
	}

	public JInternalFrameOperativeProperties(String id)
	{

		this();

		jTextFieldID.setText(id);
		setTitle(getTitle() + " - " + id);
		lid = id;

		operativedb.setID(lid);
		operativedb.getProperties();

		jTextFieldID.setText(operativedb.getID());
		jTextFieldSurname.setText(operativedb.getSurname());
		jTextFieldForename.setText(operativedb.getForename());
		chckbxEnabled.setSelected(operativedb.isEnabled());

		jButtonUpdate.setEnabled(false);

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 391, 201);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_ID = new JLabel4j_std();
					jLabel_ID.setBounds(0, 7, 98, 21);
					jDesktopPane1.add(jLabel_ID);
					jLabel_ID.setText(lang.get("lbl_Operator_ID"));
					jLabel_ID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_ID.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldID = new JTextField4j(JDBOperatives.field_id);
					jTextFieldID.setBounds(105, 7, 100, 21);
					jDesktopPane1.add(jTextFieldID);
					jTextFieldID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldID.setEditable(false);
					jTextFieldID.setEnabled(false);
				}
				{
					jLabelSurname = new JLabel4j_std();
					jLabelSurname.setBounds(0, 73, 98, 21);
					jDesktopPane1.add(jLabelSurname);
					jLabelSurname.setText(lang.get("lbl_surname"));
					jLabelSurname.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSurname.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldSurname = new JTextField4j(JDBOperatives.field_surname);
					jTextFieldSurname.setBounds(104, 73, 238, 21);
					jDesktopPane1.add(jTextFieldSurname);
					jTextFieldSurname.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldSurname.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(3, 136, 123, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jTextFieldSurname.setText(jTextFieldSurname.getText().toUpperCase());
							operativedb.setSurname(jTextFieldSurname.getText().toUpperCase());
							jTextFieldForename.setText(WordUtils.capitalize(jTextFieldForename.getText()));
							operativedb.setForename(WordUtils.capitalize(jTextFieldForename.getText()));
							operativedb.setEnabled(chckbxEnabled.isSelected());
							operativedb.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(129, 136, 123, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(255, 136, 123, 32);
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

				JLabel4j_std jLabelForename = new JLabel4j_std();
				jLabelForename.setText(lang.get("lbl_forename"));
				jLabelForename.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabelForename.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabelForename.setBounds(0, 40, 98, 21);
				jDesktopPane1.add(jLabelForename);

				jTextFieldForename = new JTextField4j(JDBOperatives.field_forename);
				jTextFieldForename.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jTextFieldForename.setFocusCycleRoot(true);
				jTextFieldForename.setCaretPosition(0);
				jTextFieldForename.setBounds(105, 40, 238, 21);
				jDesktopPane1.add(jTextFieldForename);

				JLabel4j_std label_Enabled = new JLabel4j_std();
				label_Enabled.setBounds(0, 106, 97, 21);
				label_Enabled.setText(lang.get("lbl_Enabled"));
				label_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
				jDesktopPane1.add(label_Enabled);
				chckbxEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				chckbxEnabled.setBounds(105, 102, 21, 23);
				jDesktopPane1.add(chckbxEnabled);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
