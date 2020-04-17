package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameLocationProperties.java
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
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;


/**
* The JInternalFrameMaterialLocationProperties class allows the user to
* view/edit a single record in the APP_LOCATION table. In addition to fields
* containing meaningful identification information which can be used by
* external ERP systems the Location record also has a number of flags which
* determine when messages are generated. Within the record it is possible to
* identify which pallet and batch status codes are allowed in a location. This
* is typically used to block / allow pallets (SSCC's) to be moved into a
* location by the despatch transaction.
* 
* <p>
* <img alt="" src="./doc-files/JInternalFrameLocationProperties.jpg" >
* 
* @see com.commander4j.app.JInternalFrameLocationAdmin JInternalFrameLocationAdmin
* @see com.commander4j.db.JDBLocation JDBLocation
* @see com.commander4j.db.JDBDespatch JDBDespatch
* @see com.commander4j.app.JInternalFrameDespatch JInternalFrameDespatch
*/
public class JInternalFrameLocationProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldLocationID;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel4;
	private JLabel4j_std jLabel8;
	private JTextField4j jTextFieldStorageBin;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JLabel4j_std jLabel9;
	private JTextField4j jTextFieldStorageSection;
	private JTextField4j jTextFieldStorageType;
	private JLabel4j_std jLabel7;
	private JTextField4j jTextFieldStorageLocation;
	private JLabel4j_std jLabel6;
	private JLabel4j_std jLabel5;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldPlant;
	private JTextField4j jTextFieldGLN;
	private JTextField4j jTextFieldWarehouse;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private String llocation;
	private JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldEquipmentTrackingID;
	private JLabel4j_std lblMsgDespatchConfirm;
	private JLabel4j_std lblMsgPreAdvice;
	private JLabel4j_std lblMsgPalletSplit;
	private JLabel4j_std lblMsgEquipmentTracking;
	private JLabel4j_std lblMsgProductionConfirmation;
	private JCheckBox4j checkBox_DespatchConfirm;
	private JCheckBox4j checkBox_PreAdvice;
	private JCheckBox4j checkBox_StatusChange;
	private JCheckBox4j checkBox_PalletSplit;
	private JCheckBox4j checkBox_JourneyRef;
	private JCheckBox4j checkBox_Equipment_Tracking;
	private JCheckBox4j checkBox_Production_Confirmation;
	private JCheckBox4j checkBox_PalletDelete;
	private JScrollPane scrollPane;
	private JList4j<String> palletStatusList;
	private JList4j<String> batchStatusList;
	private JLabel4j_std lblPermitPalletStatus;
	private JLabel4j_std lblPermitBatchStatus;
	private JDBLanguage lang;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("Enabled");
	private JCheckBox4j checkBox_EmailPreAdvice = new JCheckBox4j("");
	private String defaultTitle="";

	public JInternalFrameLocationProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_LOCATION_EDIT"));
		defaultTitle = getTitle();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldPlant.requestFocus();
				jTextFieldPlant.setCaretPosition(jTextFieldPlant.getText().length());

			}
		});
	}

	public void setLocationID(String loc)
	{
		
		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Location [" + llocation + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			} 
		}
		
		llocation = loc;
		jTextFieldLocationID.setText(llocation);
		setTitle(defaultTitle + " - " + llocation);

		location.setLocationID(llocation);

		if (location.isValidLocation())
		{
			location.getLocationProperties(llocation);
			jTextFieldPlant.setText(location.getPlant());
			jTextFieldWarehouse.setText(location.getWarehouse());
			jTextFieldGLN.setText(location.getGLN());
			jTextFieldDescription.setText(location.getDescription());
			jTextFieldStorageLocation.setText(location.getStorageLocation());
			jTextFieldStorageType.setText(location.getStorageType());
			jTextFieldStorageSection.setText(location.getStorageSection());
			jTextFieldStorageBin.setText(location.getStorageBin());
			jTextFieldEquipmentTrackingID.setText(location.getEquipmentTrackingID());
			chckbxEnabled.setSelected(location.isEnabled());
			checkBox_DespatchConfirm.setSelected(location.isDespatchConfirmationMessageRequired());
			checkBox_PreAdvice.setSelected(location.isDespatchPreAdviceMessageRequired());
			checkBox_EmailPreAdvice.setSelected(location.isDespatchEmailRequired());
			checkBox_StatusChange.setSelected(location.isStatusChangeMessageRequired());
			checkBox_PalletSplit.setSelected(location.isPalletSplitMessageRequired());
			checkBox_JourneyRef.setSelected(location.isJourneyRefRequired());
			checkBox_PalletDelete.setSelected(location.isPalletDeleteMessageRequired());
			checkBox_Equipment_Tracking.setSelected(location.isDespatchEquipmentTrackingMessageRequired());
			checkBox_Production_Confirmation.setSelected(location.isProductionConfirmationMessageRequired());

			int count = JUtility.countOccurrences(location.getPermittedPalletStatus(), "^") - 1;

			if (count > 0)
			{
				int[] indices = new int[count];
				int index = 0;
				String temp = "";

				for (int x = 0; x < palletStatusList.getModel().getSize(); x++)
				{
					temp = palletStatusList.getModel().getElementAt(x).toString();
					if (location.getPermittedPalletStatus().contains(temp) == true)
					{
						indices[index] = x;
						index++;
					}
				}
				palletStatusList.setSelectedIndices(indices);
			}

			count = JUtility.countOccurrences(location.getPermittedBatchStatus(), "^") - 1;

			if (count > 0)
			{
				int[] indices = new int[count];
				int index = 0;
				String temp = "";

				for (int x = 0; x < batchStatusList.getModel().getSize(); x++)
				{
					temp = batchStatusList.getModel().getElementAt(x).toString();
					if (location.getPermittedBatchStatus().contains(temp) == true)
					{
						indices[index] = x;
						index++;
					}
				}
				batchStatusList.setSelectedIndices(indices);
			}

			jButtonSave.setEnabled(false);
		}
		else
		{
			chckbxEnabled.setSelected(true);
		}
	}
	
	public JInternalFrameLocationProperties(String loc)
	{
		this();
		setLocationID(loc);

	}
	
	private boolean save()
	{
		boolean result = true;

		location.setLocationID(jTextFieldLocationID.getText());
		location.setPlant(jTextFieldPlant.getText());
		location.setWarehouse(jTextFieldWarehouse.getText());
		location.setGLN(jTextFieldGLN.getText());
		location.setDescription(jTextFieldDescription.getText());
		location.setStorageLocation(jTextFieldStorageLocation.getText());
		location.setStorageType(jTextFieldStorageType.getText());
		location.setStorageSection(jTextFieldStorageSection.getText());
		location.setStorageBin(jTextFieldStorageBin.getText());
		location.setEquipmentTrackingID(jTextFieldEquipmentTrackingID.getText());
		location.setMsgDespatchConfirm(checkBox_DespatchConfirm.isSelected());
		location.setMsgDespatchEquipTrack(checkBox_Equipment_Tracking.isSelected());
		location.setMsgDespatchPreadvice(checkBox_PreAdvice.isSelected());
		location.setMsgStatusChange(checkBox_StatusChange.isSelected());
		location.setMsgProdConfirm(checkBox_Production_Confirmation.isSelected());
		location.setMsgPalletSplit(checkBox_PalletSplit.isSelected());
		location.setMsgDelete(checkBox_PalletDelete.isSelected());
		location.setEnabled(chckbxEnabled.isSelected());
		location.setMsgJourneyRef(checkBox_JourneyRef.isSelected());
		location.setEmailDespatch(checkBox_EmailPreAdvice.isSelected());

		String palletStatusSelected = "^";
		if (palletStatusList.isSelectionEmpty() == false)
		{
			List<String> temp = palletStatusList.getSelectedValuesList();

			for (int x = 0; x < temp.size(); x++)
			{
				palletStatusSelected = palletStatusSelected + temp.get(x) + "^";
			}
		}
		location.setPermittedPalletStatus(palletStatusSelected);

		String batchStatusSelected = "^";
		if (batchStatusList.isSelectionEmpty() == false)
		{
			List<String> temp = batchStatusList.getSelectedValuesList();


			for (int x = 0; x < temp.size(); x++)
			{
				batchStatusSelected = batchStatusSelected + temp.get(x) + "^";
			}
		}
		location.setPermittedBatchStatus(batchStatusSelected);

		if (location.isValidLocation() == false)
		{
			result = location.create();
		}
		else
		{
			result = location.update();
		}
		if (result == false)
		{
			JOptionPane.showMessageDialog(Common.mainForm, location.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
		}
		else
		{
			jButtonSave.setEnabled(false);
		}
		
		return result;
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(450, 340));
			this.setBounds(0, 0, 489, 666);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Location Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					scrollPane = new JScrollPane();
					scrollPane.setBounds(52, 480, 163, 84);
					palletStatusList = new JList4j<String>();
					DefaultListModel<String> lmod1 = new DefaultListModel<String>();
					for (int temp=0;temp<Common.palletStatus.length;temp++)
					{
						lmod1.add(temp,Common.palletStatus[temp]);
					}
					
					palletStatusList.setModel(lmod1);
					palletStatusList.setToolTipText("Highlight multiple records by holding down the CTRL key at the same time as clicking.");
					palletStatusList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					scrollPane.setViewportView(palletStatusList);
					jDesktopPane1.add(scrollPane);
					ListSelectionListener listSelectionListener = new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent listSelectionEvent) {
							jButtonSave.setEnabled(true);
						}
					};
					palletStatusList.addListSelectionListener(listSelectionListener);
				}
				{
					scrollPane = new JScrollPane();
					scrollPane.setBounds(242, 480, 163, 84);
					batchStatusList = new JList4j<String>();
					DefaultListModel<String> lmod2 = new DefaultListModel<String>();
					for (int temp=0;temp<Common.batchStatus.length;temp++)
					{
						lmod2.add(temp,Common.batchStatus[temp]);
					}
					batchStatusList.setModel(lmod2);
					batchStatusList.setToolTipText("Highlight multiple records by holding down the CTRL key at the same time as clicking.");
					batchStatusList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					scrollPane.setViewportView(batchStatusList);
					jDesktopPane1.add(scrollPane);
					ListSelectionListener listSelectionListener = new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent listSelectionEvent) {
							jButtonSave.setEnabled(true);
						}
					};
					batchStatusList.addListSelectionListener(listSelectionListener);
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Storage_Location"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1.setBounds(0, 7, 151, 21);
				}
				{
					lblPermitPalletStatus = new JLabel4j_std();
					lblPermitPalletStatus.setFont(Common.font_bold);
					lblPermitPalletStatus.setText(lang.get("lbl_Storage_Location_Permit_Pallet_Status"));
					lblPermitPalletStatus.setBounds(52, 458, 163, 21);
					jDesktopPane1.add(lblPermitPalletStatus);
				}
				{
					lblPermitBatchStatus = new JLabel4j_std();
					lblPermitBatchStatus.setFont(Common.font_bold);
					lblPermitBatchStatus.setText(lang.get("lbl_Storage_Location_Permit_Batch_Status"));
					lblPermitBatchStatus.setBounds(242, 457, 163, 21);
					jDesktopPane1.add(lblPermitBatchStatus);
				}
				{
					jTextFieldLocationID = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocationID);
					jTextFieldLocationID.setText(llocation);
					jTextFieldLocationID.setEditable(false);
					jTextFieldLocationID.setEnabled(false);
					jTextFieldLocationID.setBounds(158, 7, 140, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Storage_Warehouse"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(0, 63, 151, 21);
				}
				{
					jTextFieldWarehouse = new JTextField4j(JDBLocation.field_warehouse);
					jDesktopPane1.add(jTextFieldWarehouse);
					jTextFieldWarehouse.setBounds(158, 63, 105, 21);
					jTextFieldWarehouse.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jTextFieldGLN = new JTextField4j(JDBLocation.field_gln);
					jDesktopPane1.add(jTextFieldGLN);
					jTextFieldGLN.setBounds(158, 91, 140, 21);
					jTextFieldGLN.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Description"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(0, 119, 151, 21);
				}
				{
					jTextFieldPlant = new JTextField4j(JDBLocation.field_plant);
					jDesktopPane1.add(jTextFieldPlant);
					jTextFieldPlant.setBounds(158, 35, 105, 21);
					jTextFieldPlant.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Storage_Plant"));
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setBounds(0, 35, 151, 21);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBLocation.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(158, 119, 308, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Storage_GLN"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(0, 91, 151, 21);
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText(lang.get("lbl_Storage_Location"));
					jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel6.setBounds(0, 147, 151, 21);
				}
				{
					jTextFieldStorageLocation = new JTextField4j(JDBLocation.field_storage_location);
					jDesktopPane1.add(jTextFieldStorageLocation);
					jTextFieldStorageLocation.setBounds(158, 147, 105, 21);
					jTextFieldStorageLocation.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Storage_Type"));
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel7.setBounds(0, 175, 151, 21);
				}
				{
					jTextFieldStorageType = new JTextField4j(JDBLocation.field_storage_type);
					jDesktopPane1.add(jTextFieldStorageType);
					jTextFieldStorageType.setBounds(158, 175, 105, 21);
					jTextFieldStorageType.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Storage_Section"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(0, 203, 151, 21);
				}
				{
					jTextFieldStorageSection = new JTextField4j(JDBLocation.field_storage_section);
					jDesktopPane1.add(jTextFieldStorageSection);
					jTextFieldStorageSection.setBounds(158, 203, 105, 21);
					jTextFieldStorageSection.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText(lang.get("lbl_Storage_Bin"));
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel9.setBounds(0, 231, 151, 21);
				}
				{
					jTextFieldStorageBin = new JTextField4j(JDBLocation.field_storage_bin);
					jDesktopPane1.add(jTextFieldStorageBin);
					jTextFieldStorageBin.setBounds(158, 231, 105, 21);
					jTextFieldStorageBin.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					lblMsgDespatchConfirm = new JLabel4j_std();
					lblMsgDespatchConfirm.setText("Msg Despatch Confirm");
					lblMsgDespatchConfirm.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgDespatchConfirm.setBounds(0, 299, 186, 21);
					jDesktopPane1.add(lblMsgDespatchConfirm);
				}
				{
					lblMsgPreAdvice = new JLabel4j_std();
					lblMsgPreAdvice.setText("Msg Despatch Pre Advice");
					lblMsgPreAdvice.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgPreAdvice.setBounds(0, 332, 186, 21);
					jDesktopPane1.add(lblMsgPreAdvice);
				}
				{
					lblMsgPreAdvice = new JLabel4j_std();
					lblMsgPreAdvice.setText("Msg SSCC Status Change");
					lblMsgPreAdvice.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgPreAdvice.setBounds(0, 396, 186, 21);
					jDesktopPane1.add(lblMsgPreAdvice);
				}
				{
					lblMsgPalletSplit = new JLabel4j_std();
					lblMsgPalletSplit.setText("Msg SSCC Split");
					lblMsgPalletSplit.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgPalletSplit.setBounds(225, 364, 181, 21);
					jDesktopPane1.add(lblMsgPalletSplit);
				}
				{
					lblMsgEquipmentTracking = new JLabel4j_std();
					lblMsgEquipmentTracking.setText("Msg Equipment Tracking");
					lblMsgEquipmentTracking.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgEquipmentTracking.setBounds(225, 298, 181, 21);
					jDesktopPane1.add(lblMsgEquipmentTracking);
				}
				{
					lblMsgProductionConfirmation = new JLabel4j_std();
					lblMsgProductionConfirmation.setText("Msg Production Confirm");
					lblMsgProductionConfirmation.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMsgProductionConfirmation.setBounds(225, 331, 181, 21);
					jDesktopPane1.add(lblMsgProductionConfirmation);
				}
				{
					checkBox_DespatchConfirm = new JCheckBox4j("");
					checkBox_DespatchConfirm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_DespatchConfirm.setBounds(193, 295, 31, 24);
					jDesktopPane1.add(checkBox_DespatchConfirm);
				}
				{
					JLabel4j_std label4j_std2 = new JLabel4j_std();
					label4j_std2.setText("Email Despatch Pre Advice");
					label4j_std2.setHorizontalAlignment(SwingConstants.TRAILING);
					label4j_std2.setBounds(0, 364, 186, 21);
					jDesktopPane1.add(label4j_std2);
					

					checkBox_EmailPreAdvice.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_EmailPreAdvice.setBounds(193, 361, 31, 24);
					jDesktopPane1.add(checkBox_EmailPreAdvice);
				}
				{
					checkBox_PreAdvice = new JCheckBox4j("");
					checkBox_PreAdvice.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_PreAdvice.setBounds(193, 329, 31, 24);
					jDesktopPane1.add(checkBox_PreAdvice);
				}
				{
					checkBox_StatusChange = new JCheckBox4j("");
					checkBox_StatusChange.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_StatusChange.setBounds(193, 393, 31, 24);
					jDesktopPane1.add(checkBox_StatusChange);
				}	
				{
					checkBox_PalletSplit = new JCheckBox4j("");
					checkBox_PalletSplit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_PalletSplit.setBounds(413, 361, 31, 24);
					jDesktopPane1.add(checkBox_PalletSplit);
				}	
				{
					checkBox_JourneyRef = new JCheckBox4j("");
					checkBox_JourneyRef.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_JourneyRef.setBounds(413, 393, 31, 24);
					jDesktopPane1.add(checkBox_JourneyRef);
				}	
				
				{
					checkBox_Equipment_Tracking = new JCheckBox4j("");
					checkBox_Equipment_Tracking.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_Equipment_Tracking.setBounds(413, 295, 31, 24);
					jDesktopPane1.add(checkBox_Equipment_Tracking);
				}
				{
					checkBox_Production_Confirmation = new JCheckBox4j("");
					checkBox_Production_Confirmation.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonSave.setEnabled(true);
						}
					});
					checkBox_Production_Confirmation.setBounds(413, 328, 31, 24);
					jDesktopPane1.add(checkBox_Production_Confirmation);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText("Save");
					jButtonSave.setMnemonic(83);
					jButtonSave.setBounds(62, 576, 112, 28);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							save();

						}
					});
				}
			}
			{
				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jButtonHelp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText("Help");
				jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				jButtonHelp.setBounds(181, 576, 112, 28);
			}
			{
				jButtonCancel = new JButton4j(Common.icon_close_16x16);
				jDesktopPane1.add(jButtonCancel);
				jButtonCancel.setText("Close");
				jButtonCancel.setMnemonic(67);
				jButtonCancel.setBounds(300, 576, 112, 28);
				jButtonCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dispose();
					}
				});
			}
			{
				jTextFieldEquipmentTrackingID = new JTextField4j(JDBLocation.field_equipment_tracking_id);
				jTextFieldEquipmentTrackingID.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				jTextFieldEquipmentTrackingID.setBounds(158, 262, 105, 21);
				jDesktopPane1.add(jTextFieldEquipmentTrackingID);
			}
			{
				JLabel4j_std label = new JLabel4j_std();
				label.setText(lang.get("lbl_Storage_Equipment_Tracking_ID"));
				label.setHorizontalAlignment(SwingConstants.TRAILING);
				label.setBounds(0, 262, 151, 21);
				jDesktopPane1.add(label);
			}
			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setText("Msg SSCC Delete");
			label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std.setBounds(0, 427, 186, 21);
			jDesktopPane1.add(label4j_std);
			
			JLabel4j_std label5j_std = new JLabel4j_std();
			label5j_std.setText("Msg Journey Ref");
			label5j_std.setHorizontalAlignment(SwingConstants.TRAILING);
			label5j_std.setBounds(225, 396, 181, 21);
			jDesktopPane1.add(label5j_std);			
			
			checkBox_PalletDelete = new JCheckBox4j("");
			checkBox_PalletDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					jButtonSave.setEnabled(true);
				}
			});
			checkBox_PalletDelete.setBounds(193, 425, 31, 24);
			jDesktopPane1.add(checkBox_PalletDelete);
			chckbxEnabled.setFont(Common.font_std);
			chckbxEnabled.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					jButtonSave.setEnabled(true);
				}
			});
			

			chckbxEnabled.setBounds(310, 4, 156, 23);
			chckbxEnabled.setText(lang.get("lbl_Interface_Enabled"));
			jDesktopPane1.add(chckbxEnabled);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
