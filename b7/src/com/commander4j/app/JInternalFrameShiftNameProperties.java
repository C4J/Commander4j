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

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBShiftNames;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameMHNDecisionProperties class allows the user to edit a single
 * record the APP_MHN_DECISIONS table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMHNDecisionProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMHNDecisionAdmin
 *      JInternalFrameMHNDecisionAdmin
 * @see com.commander4j.db.JDBMHNDecisions JDBMHNDecisions
 */
public class JInternalFrameShiftNameProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldShiftID;
	private JLabel4j_std jLabel_ShiftID;
	private JDBShiftNames shiftdb = new JDBShiftNames(Common.selectedHostID, Common.sessionID);
	private String lshiftid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");

	public JInternalFrameShiftNameProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SHIFT_NAMES"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});
	}

	public JInternalFrameShiftNameProperties(String shift)
	{

		this();

		jTextFieldShiftID.setText(shift);
		setTitle(getTitle() + " - " + shift);
		lshiftid = shift;

		shiftdb.setShiftID(lshiftid);
		shiftdb.getProperties();

		jTextFieldShiftID.setText(shiftdb.getShiftID());
		jTextFieldDescription.setText(shiftdb.getDescription());
		chckbxEnabled.setSelected(shiftdb.isEnabled());
		
		jButtonUpdate.setEnabled(false);

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 391, 183);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_ShiftID = new JLabel4j_std();
					jLabel_ShiftID.setBounds(0, 7, 98, 22);
					jDesktopPane1.add(jLabel_ShiftID);
					jLabel_ShiftID.setText(lang.get("lbl_Shift_ID"));
					jLabel_ShiftID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_ShiftID.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldShiftID = new JTextField4j(JDBShiftNames.field_shift_id);
					jTextFieldShiftID.setBounds(105, 7, 100, 22);
					jDesktopPane1.add(jTextFieldShiftID);
					jTextFieldShiftID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldShiftID.setEditable(false);
					jTextFieldShiftID.setEnabled(false);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jLabelDescription.setBounds(0, 40, 98, 22);
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBShiftNames.field_description);
					jTextFieldDescription.setBounds(105, 40, 238, 22);
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
					jButtonUpdate.setBounds(3, 103, 123, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							shiftdb.setDescription(jTextFieldDescription.getText());
							shiftdb.setEnabled(chckbxEnabled.isSelected());
							shiftdb.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(129, 103, 123, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(255, 103, 123, 32);
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
				label_Enabled.setBounds(0, 69, 97, 22);
				label_Enabled.setText(lang.get("lbl_Enabled"));
				label_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
				jDesktopPane1.add(label_Enabled);
				chckbxEnabled.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				

				chckbxEnabled.setBounds(105, 69, 21, 22);
				jDesktopPane1.add(chckbxEnabled);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
