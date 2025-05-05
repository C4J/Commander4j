package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameUomProperties.java
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomListProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldListID;
	private JLabel4j_std jLabel2;
	private JButton4j jButtonClose;
	private JCheckBox4j jCheckBoxEnabled;
	private JLabel4j_std jLabel3;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldItem;
	private JLabel4j_std jLabel1;
	private JDBBomList bomList = new JDBBomList(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lblLocalUom;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JSpinner4j jSpinnerLimit_Sequence;
	private JDBBomListRecord listRecord = new JDBBomListRecord();


	public JInternalFrameBomListProperties()
	{
		super();
		initGUI();
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_LIST"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
	}
	
	public void setID(String list_id,String item)
	{

		if (jButtonSave.isEnabled())
		{
			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to L [" + list_id + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}

		jButtonSave.setEnabled(false);

		listRecord.setList_id(list_id);
		listRecord.setItem(item);
		
		bomList.getProperties(listRecord);

		jTextFieldListID.setText(bomList.getListRecord().getList_id());
		jTextFieldItem.setText(bomList.getListRecord().getItem());
		
		jSpinnerLimit_Sequence.setValue(bomList.getListRecord().getSequence());
		
		jCheckBoxEnabled.setSelected(bomList.getListRecord().isEnabled());
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldItem.requestFocus();
				jTextFieldItem.setCaretPosition(jTextFieldItem.getText().length());
				
				jButtonSave.setEnabled(false);
			}
		});
	}
	
	public JInternalFrameBomListProperties(String list_id,String item)
	{
		
		this();
		
		setID(list_id,item);
		
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 424, 224);
			setVisible(true);
			this.setTitle("List Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_List_ID"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(0, 10, 110, 22);
				}
				{
					jTextFieldListID = new JTextField4j();
					jTextFieldListID.setEnabled(false);
					jTextFieldListID.setEditable(false);
					jDesktopPane1.add(jTextFieldListID);
					jTextFieldListID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldListID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldListID.setBounds(120, 10, 264, 22);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Item"));
					jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel2.setBounds(0, 43, 110, 22);
				}
				{
					jTextFieldItem = new JTextField4j();
					jTextFieldItem.setEnabled(false);
					jTextFieldItem.setEditable(false);
					jDesktopPane1.add(jTextFieldItem);
					jTextFieldItem.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldItem.setFocusCycleRoot(true);
					jTextFieldItem.setBounds(120, 43, 264, 22);
					jTextFieldItem.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(45, 142, 110, 32);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(157, 142, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(269, 142, 110, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Enabled"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(0, 109, 110, 22);
				}
				
				SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
				jSpinnerIntModel.setMinimum(0);
				jSpinnerIntModel.setMaximum(99);
				jSpinnerIntModel.setStepSize(1);

				jSpinnerLimit_Sequence = new JSpinner4j();
				jSpinnerLimit_Sequence.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(jSpinnerLimit_Sequence);

				jSpinnerLimit_Sequence.setModel(jSpinnerIntModel);
				JSpinner4j.NumberEditor ne_jSpinnerLimit_Sequence = new JSpinner4j.NumberEditor(jSpinnerLimit_Sequence);
				jSpinnerLimit_Sequence.setEditor(ne_jSpinnerLimit_Sequence);
				jSpinnerLimit_Sequence.setBounds(120, 76, 46, 22);
				jSpinnerLimit_Sequence.getEditor().setSize(45, 21);
				
				{
					lblLocalUom = new JLabel4j_std();
					lblLocalUom.setText(lang.get("lbl_Sequence_ID"));
					lblLocalUom.setHorizontalTextPosition(SwingConstants.RIGHT);
					lblLocalUom.setHorizontalAlignment(SwingConstants.RIGHT);
					lblLocalUom.setBounds(0, 76, 110, 22);
					jDesktopPane1.add(lblLocalUom);
				}

				{
					jCheckBoxEnabled = new JCheckBox4j();
					jCheckBoxEnabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jCheckBoxEnabled);
					jCheckBoxEnabled.setPreferredSize(new java.awt.Dimension(40, 20));
					jCheckBoxEnabled.setFocusCycleRoot(true);
					jCheckBoxEnabled.setBounds(120, 109, 24, 22);
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						jSpinnerLimit_Sequence.requestFocus();
					}
				});
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void save()
	{
		listRecord.setSequence(Integer.valueOf(jSpinnerLimit_Sequence.getValue().toString()));
		
		listRecord.setEnabled(jCheckBoxEnabled.isSelected());
		
		bomList.update(listRecord);
		
		JLaunchMenu.runForm("FRM_BOM_LIST", listRecord.getList_id(), listRecord.getItem(), listRecord.isEnabled());
		
		jButtonSave.setEnabled(false);
	}
}
