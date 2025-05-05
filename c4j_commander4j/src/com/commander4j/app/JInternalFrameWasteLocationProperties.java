package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTSamplePointProperties.java
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWasteLocation;
import com.commander4j.db.JDBWasteLocationTypes;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JCheckListItem;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.renderer.MultiItemCheckListRenderer;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLocationProperties class allows the user to edit a
 * record in the APP_WASTE_LOCATION table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteLocationProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWasteLocationProperties
 *
 */
public class JInternalFrameWasteLocationProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldLocationID;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel_WasteLocation;
	private JDBWasteLocation wasteLocation = new JDBWasteLocation(Common.selectedHostID, Common.sessionID);
	private JDBWasteTypes wasteTypes = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private JDBWasteLocationTypes wasteLocationTypes = new JDBWasteLocationTypes(Common.selectedHostID, Common.sessionID);
	private String llocationid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbx_PO_Reqd = new JCheckBox4j("");
	private JCheckBox4j chckbx_Reason_Reqd = new JCheckBox4j("");
	private JCheckBox4j chckbx_Enabled = new JCheckBox4j("");
	private JList4j<JCheckListItem> listTypes;
	ComboBoxModel<JCheckListItem> model;

	public void setLocationID(String location)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Location [" + jTextFieldLocationID.getText() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		llocationid = location;
		jTextFieldLocationID.setText(llocationid);
		setTitle("Location [" + llocationid + "]");

		wasteLocation.setWasteLocationID(llocationid);
		wasteLocation.getWasteLocationProperties(llocationid);

		jTextFieldLocationID.setText(wasteLocation.getWasteLocationID());
		jTextFieldDescription.setText(wasteLocation.getDescription());
		
		chckbx_PO_Reqd.setSelected(wasteLocation.isProcessOrderRequired());

		chckbx_Reason_Reqd.setSelected(wasteLocation.isReasonIDRequired());

		chckbx_Enabled.setSelected(wasteLocation.isEnabled());
		
		model = new DefaultComboBoxModel<JCheckListItem>(wasteTypes.getTypesCheckListForLocation(llocationid));
		
		listTypes.setModel(model);
		listTypes.setCellRenderer(new MultiItemCheckListRenderer());

	}

	public JInternalFrameWasteLocationProperties(String location)
	{

		super();

		llocationid = location;
		
		initGUI();
		
		setLocationID(location);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WASTE_LOCATION"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 624, 432);
			setVisible(true);
			this.setTitle("Waste Location Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_WasteLocation = new JLabel4j_std();
					jDesktopPane1.add(jLabel_WasteLocation);
					jLabel_WasteLocation.setText(lang.get("lbl_Location_ID"));
					jLabel_WasteLocation.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_WasteLocation.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_WasteLocation.setBounds(0, 10, 149, 22);
				}
				{
					jTextFieldLocationID = new JTextField4j(JDBWasteLocation.field_WasteLocationID);
					jDesktopPane1.add(jTextFieldLocationID);
					jTextFieldLocationID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldLocationID.setEditable(false);
					jTextFieldLocationID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldLocationID.setBounds(155, 10, 237, 22);
					jTextFieldLocationID.setEnabled(false);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(139, 356, 110, 32);
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
					jButtonHelp.setBounds(251, 356, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(362, 356, 110, 32);
					jButtonClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel_Description = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Description);
					jLabel_Description.setText(lang.get("lbl_Description"));
					jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Description.setBounds(0, 43, 149, 22);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWasteLocation.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 433, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
					
					chckbx_PO_Reqd.setBounds(155, 101, 29, 22);
					jDesktopPane1.add(chckbx_PO_Reqd);
					
					chckbx_Reason_Reqd.setBounds(155, 131, 29, 22);
					jDesktopPane1.add(chckbx_Reason_Reqd);
					
					chckbx_Enabled.setBounds(155, 161, 29, 22);
					jDesktopPane1.add(chckbx_Enabled);
					
					JLabel4j_std jLabel_Description = new JLabel4j_std();
					jLabel_Description.setText(lang.get("lbl_PO_Required"));
					jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Description.setBounds(0, 101, 149, 22);
					jDesktopPane1.add(jLabel_Description);
					
					JLabel4j_std jLabel_Reason_Reqd = new JLabel4j_std();
					jLabel_Reason_Reqd.setText(lang.get("lbl_Reason_Reqd"));
					jLabel_Reason_Reqd.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Reason_Reqd.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Reason_Reqd.setBounds(0, 131, 149, 22);
					jDesktopPane1.add(jLabel_Reason_Reqd);
					
					JLabel4j_std jLabel_Enabled = new JLabel4j_std();
					jLabel_Enabled.setText(lang.get("lbl_Enabled"));
					jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Enabled.setBounds(0, 161, 149, 22);
					jDesktopPane1.add(jLabel_Enabled);
					
					chckbx_PO_Reqd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					chckbx_Reason_Reqd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					chckbx_Enabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					
					
					JLabel4j_std jLabel_Permitted_Types = new JLabel4j_std();
					jLabel_Permitted_Types.setHorizontalAlignment(SwingConstants.LEFT);
					jLabel_Permitted_Types.setText(lang.get("lbl_Permitted_Types"));
					jLabel_Permitted_Types.setBounds(191, 76, 397, 22);
					jLabel_Permitted_Types.setFont(Common.font_bold);
					jDesktopPane1.add(jLabel_Permitted_Types);
					
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(192, 101, 393, 239);

					
					listTypes = new JList4j<JCheckListItem>();

					listTypes.addMouseListener(new MouseAdapter()
					{
						public void mouseClicked(MouseEvent event)
						{
							JList4j<?> list = (JList4j<?>) event.getSource();

							// Get index of item clicked

							int index = list.locationToIndex(event.getPoint());
							JCheckListItem item = (JCheckListItem) list.getModel().getElementAt(index);

							// Toggle selected state

							item.setSelected(!item.isSelected());

							// Repaint cell

							list.repaint(list.getCellBounds(index, index));
							
							jButtonSave.setEnabled(true);
						}
					});
					
					scrollPane.setViewportView(listTypes);

					jDesktopPane1.add(scrollPane);
				}

	}}catch(

	Exception e)
	{
			e.printStackTrace();
		}
	}

	private void save()
	{
		wasteLocation.setWasteLocationID(jTextFieldLocationID.getText().toUpperCase());
		wasteLocation.setDescription(jTextFieldDescription.getText());
		wasteLocation.setProcessOrderRequired(chckbx_PO_Reqd.isSelected());
		wasteLocation.setReasonIDRequired(chckbx_Reason_Reqd.isSelected()); 
		wasteLocation.setEnabled(chckbx_Enabled.isSelected());
		wasteLocation.update();
		

		LinkedList<String> selectedTypes = new LinkedList<String>();

		int x = listTypes.getModel().getSize();
		if (x > 0)
		{
			JCheckListItem tempItem;
			for (int sel = 0; sel < x; sel++)
			{
				tempItem = (JCheckListItem) listTypes.getModel().getElementAt(sel);
				JDBWasteTypes t = (JDBWasteTypes) tempItem.getValue();
				if (tempItem.isSelected())
				{
					String i = t.getWasteTypeID();
					
					selectedTypes.add(i);

				}
			}
		}
		
		wasteLocationTypes.rewriteTypesAssignedToLocation(llocationid, selectedTypes);
		
		jButtonSave.setEnabled(false);
	}
}
