package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameBomStructureProperties.java
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
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomStructureProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;

	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;

	private JDBBomStructure structure = new JDBBomStructure(Common.selectedHostID, Common.sessionID);
	private JDBBomStructureRecord originalRecord = new JDBBomStructureRecord();
	private JDBBomStructureRecord newRecord = new JDBBomStructureRecord();

	private JDBBomElement element = new JDBBomElement(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private JComboBox4j<JDBListData> comboBoxDataID = new JComboBox4j<JDBListData>();
	private JComboBox4j<JDBListData> comboBoxParentDataID = new JComboBox4j<JDBListData>();


	private Vector<JDBListData> elementList = new Vector<JDBListData>();


	private JLabel4j_std jLabel_Data_ID = new JLabel4j_std();
	private JLabel4j_std jLabel_Parent_Data_ID = new JLabel4j_std();


	public void updateSearch(String data_id, String parent_data_id)
	{
		originalRecord.setDataId(data_id);
		originalRecord.setDataIDParent(parent_data_id);

		newRecord.setDataId(data_id);
		newRecord.setDataIDParent(parent_data_id);

		//structure.getProperties(originalRecord);

		for (int x = 1; x < elementList.size(); x++)
		{
			if (((JDBBomElementRecord) elementList.get(x).getmData()).getDataId().equals(originalRecord.getDataId()))
			{
				comboBoxDataID.setSelectedIndex(x);
			}

			if (((JDBBomElementRecord) elementList.get(x).getmData()).getDataId().equals(originalRecord.getDataIDParent()))
			{
				comboBoxParentDataID.setSelectedIndex(x);
			}
		}

		setButtonState(false);

	}

	public JInternalFrameBomStructureProperties()
	{
		super();

		elementList.clear();
		elementList.add(null);
		elementList.addAll(element.getElementIds(true));

		comboBoxDataID.setRenderer(Common.renderer_list);
		comboBoxParentDataID.setRenderer(Common.renderer_list);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_STRUCTURE"));

	}

	public JInternalFrameBomStructureProperties(String data_id, String parent_data_id)
	{

		this();

		updateSearch(data_id, parent_data_id);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 464, 175);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(49, 96, 112, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (update())
							{
								setButtonState(true);
							}
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(182, 96, 112, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(313, 96, 112, 32);
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

				ComboBoxModel<JDBListData> jComboBox1Model = new DefaultComboBoxModel<JDBListData>(elementList);
				comboBoxDataID.setBounds(143, 57, 282, 22);
				comboBoxDataID.setModel(jComboBox1Model);
				comboBoxDataID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						setButtonState(true);
					}
				});

				jDesktopPane1.add(comboBoxDataID);

				ComboBoxModel<JDBListData> jComboBox2Model = new DefaultComboBoxModel<JDBListData>(elementList);
				comboBoxParentDataID.setBounds(143, 18, 282, 22);
				comboBoxParentDataID.setModel(jComboBox2Model);
				comboBoxParentDataID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						setButtonState(true);
					}
				});
				jDesktopPane1.add(comboBoxParentDataID);


				jLabel_Data_ID.setBounds(22, 57, 112, 22);
				jDesktopPane1.add(jLabel_Data_ID);
				jLabel_Data_ID.setText(lang.get("lbl_Data_ID"));
				jLabel_Data_ID.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_Data_ID.setHorizontalTextPosition(SwingConstants.RIGHT);

				jLabel_Parent_Data_ID.setBounds(22, 18, 112, 22);
				jDesktopPane1.add(jLabel_Parent_Data_ID);
				jLabel_Parent_Data_ID.setText(lang.get("lbl_Parent_Data_ID"));
				jLabel_Parent_Data_ID.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_Data_ID.setHorizontalTextPosition(SwingConstants.RIGHT);


			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setButtonState(boolean truefalse)
	{
		jButtonUpdate.setEnabled(truefalse);

	}

	private boolean update()
	{
		boolean result = false;

		String new_id = (((JDBBomElementRecord) ((JDBListData) comboBoxDataID.getSelectedItem()).getmData()).getDataId());
		String new_pid = (((JDBBomElementRecord) ((JDBListData) comboBoxParentDataID.getSelectedItem()).getmData()).getDataId());


		newRecord.setDataId(new_id);
		newRecord.setDataIDParent(new_pid);

		result = structure.update(originalRecord, newRecord);

		JLaunchMenu.runForm("FRM_BOM_STRUCTURE", newRecord.getDataId(), newRecord.getDataIDParent());

		if (result)
		{
			originalRecord.setDataId(newRecord.getDataId());
			originalRecord.setDataIDParent(newRecord.getDataIDParent());

		}

		return result;
	}
}
