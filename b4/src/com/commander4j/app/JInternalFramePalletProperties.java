package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePalletProperties allows you to view/edit all the fields on a pallet record held in the APP_PALLET table. Any changes made will
 * be written to the APP_PALLET_HISTORY_TABLE
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 *
 */
public class JInternalFramePalletProperties extends javax.swing.JInternalFrame {
	private JLabel4j_std jLabelProductionDate_1;
	private JButton4j jButtonUndo;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonEditBatch;
	private JButton4j jButton1;
	private JLabel4j_std jStatusText;
	private JComboBox4j<String> jComboBoxDefaultPalletStatus;
	private JLabel4j_std jLabel15;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel4;
	private JLabel4j_std jLabel8;
	private JLabel4j_std jLabelBatchExpiry;
	private JDateControl expiryDate;
	private JTextField4j jTextFieldBatchStatus;
	private JLabel4j_std jLabel10;
	private JTextField4j jTextFieldMaterialDescription;
	private JLabel4j_std jLabel9;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JDateControl productionDate;
	private JLabel4j_std jLabelProductionDate;
	private JLabel4j_std jLabelDespatchNo;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JLabel4j_std jLabelQuantity;
	private JTextField4j jTextFieldVariant;
	private JLabel4j_std jLabel7;
	private JTextField4j jTextFieldEAN;
	private JLabel4j_std jLabel6;
	private JButton4j jButtonLookupProcessOrder;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JButton4j jButtonLookupLocation;
	private JTextField4j jTextFieldLocation;
	private JButton4j jButtonLookupBatch;
	private JLabel4j_std jLabel3;
	private JButton4j jButtonLookupMaterial;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel1;
	private JComboBox4j<JDBUom> jComboBoxUOM;
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBUom paluom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBDespatch despatch = new JDBDespatch(Common.selectedHostID, Common.sessionID);
	private JDBCustomer customer = new JDBCustomer(Common.selectedHostID, Common.sessionID);
	private JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private JCheckBox4j checkBoxConfirmed = new JCheckBox4j();
	private String lsscc;
	private String luom;
	private JTextField4j jTextFieldLayers;
	private JLabel4j_std lblLayers;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextField4j textFieldDespatchNo;
	private String expiryMode;
	private JCalendarButton calendarButtonproductionDate;
	private JCalendarButton calendarButtonexpiryDate;
	private JTextField4j textFieldCustomer;
	private boolean initialising = true;
	private JButton4j buttonRefreshMaterialData;

	private void enableOrdisableFields(JComponent field) {
		if (field == null) {
			jTextFieldProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));
			jTextFieldMaterial.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_MATERIAL"));
			jTextFieldBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_BATCH"));
			jTextFieldLocation.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_LOCATION"));
			jFormattedTextFieldQuantity.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_QUANTITY"));
			jComboBoxUOM.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_UOM"));
			jComboBoxDefaultPalletStatus.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_STATUS"));
			productionDate.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROD_DATE"));
			checkBoxConfirmed.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_CONFIRMED"));
			textFieldDespatchNo.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DESPATCH"));
			textFieldCustomer.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER"));
			jButtonSave.setEnabled(true);
			jButtonUndo.setEnabled(true);
			enableSave();
		} else {
			if (field != jTextFieldProcessOrder)
				jTextFieldProcessOrder.setEnabled(false);
			if (field != jTextFieldMaterial)
				jTextFieldMaterial.setEnabled(false);
			if (field != jTextFieldBatch)
				jTextFieldBatch.setEnabled(false);
			if (field != jTextFieldLocation)
				jTextFieldLocation.setEnabled(false);
			if (field != jFormattedTextFieldQuantity)
				jFormattedTextFieldQuantity.setEnabled(false);
			if (field != jComboBoxUOM)
				jComboBoxUOM.setEnabled(false);
			if (field != jComboBoxDefaultPalletStatus)
				jComboBoxDefaultPalletStatus.setEnabled(false);
			if (field != productionDate)
				productionDate.setEnabled(false);
			if (field != textFieldDespatchNo)
				textFieldDespatchNo.setEnabled(false);
			if (field != textFieldCustomer)
				textFieldCustomer.setEnabled(false);
			field.setEnabled(true);
			enableSave();
		}
	}

	private void enableSave() {
		boolean result = true;
		String message = "";

		if (jTextFieldLocation.getText().equals("")) {
			result = false;
			message = lang.get("err_Location_Blank");
		}

		if (jTextFieldBatch.getText().equals("")) {
			result = false;
			message = lang.get("err_Batch_Blank");
		}

		if (jTextFieldMaterial.getText().equals("")) {
			result = false;
			message = lang.get("err_Material_Blank");
		}

		if (jTextFieldProcessOrder.getText().equals("")) {
			result = false;
			message = lang.get("err_Process_Order_Blank");
		}

		if (textFieldCustomer.getText().equals("")) {
			result = false;
			message = lang.get("err_Customer_Blank");
		}

		jStatusText.setText(message);
		jButtonSave.setEnabled(result);
		jButtonUndo.setEnabled(result);
	}

	private void locationChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			if (location.getLocationProperties(jTextFieldLocation.getText()) == true) {
				enableOrdisableFields(null);

			} else {
				enableOrdisableFields(jTextFieldLocation);
				jButtonSave.setEnabled(false);
				jStatusText.setText(location.getErrorMessage());
			}
		}
	}

	private void materialBatchChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			if (materialBatch.getMaterialBatchProperties(jTextFieldMaterial.getText(), jTextFieldBatch.getText()) == true) {
				jTextFieldBatchStatus.setText(materialBatch.getStatus());
				enableOrdisableFields(null);

				if (expiryMode.equals("SSCC")) {
					jButtonEditBatch.setVisible(false);
					calendarButtonexpiryDate.setVisible(true);
					expiryDate.setEnabled(true);
					expiryDate.setDate(pallet.getBatchExpiry());
				} else {
					try {
						expiryDate.setDate(materialBatch.getExpiryDate());
						jStatusText.setText("Expiry Date set from Batch " + jTextFieldMaterial.getText() + " / " + jTextFieldBatch.getText());
					} catch (Exception ex) {
						jStatusText.setText(ex.getMessage());
					}
				}

			} else {
				jStatusText.setText(materialBatch.getErrorMessage());

				jButtonSave.setEnabled(false);
				enableOrdisableFields(jTextFieldBatch);
				if (expiryMode.equals("BATCH")) {
					jStatusText.setText("No Batch Record Found " + jTextFieldMaterial.getText() + " / " + jTextFieldBatch.getText() + " (auto create on save).");
				}
			}
		}

	}

	private void materialChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			if (material.getMaterialProperties(jTextFieldMaterial.getText()) == true) {
				enableOrdisableFields(null);
				jTextFieldMaterialDescription.setText(material.getDescription());
				getMaterialUoms(jTextFieldMaterial.getText());
				materialBatchChanged();
				uomChanged();
			} else {
				enableOrdisableFields(jTextFieldMaterial);
				jTextFieldMaterialDescription.setText("");
				jButtonSave.setEnabled(false);
				jStatusText.setText(material.getErrorMessage());
			}
		}
	}

	private void despatchNoChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			String despNo = JUtility.replaceNullStringwithBlank(textFieldDespatchNo.getText());
			if (despNo.equals("") == false) {
				if (despatch.getDespatchProperties(despNo) == true) {
					enableOrdisableFields(null);
				} else {
					enableOrdisableFields(textFieldDespatchNo);
					jButtonSave.setEnabled(false);
					jStatusText.setText(despatch.getErrorMessage());
				}
			}
		}
	}

	private void customerIDChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			String customerID = JUtility.replaceNullStringwithBlank(textFieldCustomer.getText());
			if (customerID.equals("") == false) {
				if (customer.isValidCustomer(customerID) == true) {
					enableOrdisableFields(null);
				} else {
					enableOrdisableFields(textFieldCustomer);
					jButtonSave.setEnabled(false);
					jStatusText.setText(customer.getErrorMessage());
				}
			} else {
				enableOrdisableFields(textFieldCustomer);
				jStatusText.setText("Customer ID cannot be empty");
			}
		}
	}

	private void processOrderChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			if (processOrder.getProcessOrderProperties(jTextFieldProcessOrder.getText()) == true) {
				enableOrdisableFields(null);
				if (jTextFieldMaterial.getText().equals(processOrder.getMaterial()) == false) {
					jTextFieldMaterial.setText(processOrder.getMaterial());
					materialChanged();
					jStatusText.setText("Material updated from process order");
				}
			} else {
				enableOrdisableFields(jTextFieldProcessOrder);
				jButtonSave.setEnabled(false);
				jStatusText.setText(processOrder.getErrorMessage());
			}
			jTextFieldProcessOrderDescription.setText(processOrder.getDescription());
			textFieldCustomer.setText(processOrder.getCustomerID());
		}
	}

	private void uomChanged() {
		if (initialising == false) {
			jStatusText.setText("");
			try {
				luom = ((JDBUom) jComboBoxUOM.getSelectedItem()).getInternalUom();
				pallet.setUom(luom);
				if (materialuom.getMaterialUomProperties(jTextFieldMaterial.getText(), luom)) {
					enableOrdisableFields(null);
				} else {
					enableOrdisableFields(jComboBoxUOM);
					jButtonSave.setEnabled(false);
					jStatusText.setText(materialuom.getErrorMessage());
				}
				jTextFieldEAN.setText(materialuom.getEan());
				jTextFieldVariant.setText(materialuom.getVariant());
			} catch (Exception e) {

			}
		}
	}

	public void getMaterialUoms(String lmaterial) {
		uomList.clear();
		materialuom.setMaterial(lmaterial);
		uomList.addAll(materialuom.getMaterialUoms());
		ComboBoxModel<JDBUom> jComboBoxBaseUOMModel = new DefaultComboBoxModel<JDBUom>(uomList);
		paluom.getInternalUomProperties(pallet.getUom());
		jComboBoxBaseUOMModel.setSelectedItem(paluom);
		jComboBoxUOM.setModel(jComboBoxBaseUOMModel);

	}

	public JInternalFramePalletProperties(String sscc) {
		
		super();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_EDIT"));
		
		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
		
		uomList.add(new JDBUom("", ""));
		uomList.addAll(uom.getInternalUoms());
		lsscc = sscc;
		initGUI();
		
		this.setTitle("Pallet Properties");
		refresh();
		

		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldProcessOrder.requestFocus();
				jTextFieldProcessOrder.setCaretPosition(jTextFieldProcessOrder.getText().length());

				initialising = false;

			}
		});
	}

	public void setPalletSSCC(String sscc)
	{
		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to SSCC [" + lsscc + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				save();
			}
		}
		
		lsscc = sscc;
		refresh();
	}
	
	private void save()
	{
		boolean result = true;

		long txnRef = 0;

		if (pallet.isValidPallet(lsscc) == true) {
			txnRef = pallet.writePalletHistory(txnRef, "EDIT", "FROM");
		}

		pallet.setProcessOrder(jTextFieldProcessOrder.getText());
		pallet.setMaterial(jTextFieldMaterial.getText());
		pallet.setBatchNumber(jTextFieldBatch.getText());
		pallet.setLocationID(jTextFieldLocation.getText());
		Date dom = productionDate.getDate();
		pallet.setDateOfManufacture(JUtility.getTimestampFromDate(dom));

		pallet.setQuantity(jFormattedTextFieldQuantity.getQuantity());
		pallet.setDespatchNo(textFieldDespatchNo.getText());
		pallet.setCustomerID(textFieldCustomer.getText());
		Date exp = expiryDate.getDate();
		pallet.setBatchExpiry(JUtility.getTimestampFromDate(exp));
		if (expiryMode.equals("SSCC")) {
			try {
				Date d = expiryDate.getDate();
				pallet.setBatchExpiry(JUtility.getTimestampFromDate(d));
			} catch (Exception ex) {

			}
		}

		if (pallet.isValidPallet(lsscc) == true) {
			result = pallet.update();
			if (result == true) {
				pallet.writePalletHistory(txnRef, "EDIT", "TO");
				pallet.updateStatus((String) jComboBoxDefaultPalletStatus.getSelectedItem(),true);

				jTextFieldLayers.setText(String.valueOf(pallet.getLayersOnPallet()));
				enableOrdisableFields(null);
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(false);
			} else {
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, pallet.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);

			}
		} else {
			result = pallet.create();
			if (result == true) {
				txnRef = pallet.writePalletHistory(txnRef, "EDIT", "CREATE");
				jTextFieldLayers.setText(String.valueOf(pallet.getLayersOnPallet()));
				enableOrdisableFields(null);
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(false);
			} else {
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, pallet.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);

			}
		}

	}

	private void refresh() {
		pallet.setSSCC(lsscc);
		jTextFieldSSCC.setText(lsscc);
		if (pallet.isValidPallet()) {
			pallet.getPalletProperties(lsscc);

			textFieldCustomer.setText(pallet.getCustomerID());
			jTextFieldProcessOrder.setText(pallet.getProcessOrder());

			if (processOrder.getProcessOrderProperties(jTextFieldProcessOrder.getText()) == true) {
				jTextFieldProcessOrderDescription.setText(processOrder.getDescription());
			} else {
				jTextFieldProcessOrderDescription.setText("Not found !");
			}

			jTextFieldMaterial.setText(pallet.getMaterial());

			if (material.getMaterialProperties(jTextFieldMaterial.getText()) == true) {
				jTextFieldMaterialDescription.setText(material.getDescription());
			} else {
				jTextFieldMaterialDescription.setText("Not found !");
			}

			jTextFieldBatch.setText(pallet.getBatchNumber());

			if (materialBatch.getMaterialBatchProperties(jTextFieldMaterial.getText(), jTextFieldBatch.getText()) == true) {
				jTextFieldBatchStatus.setText(materialBatch.getStatus());

				if (expiryMode.equals("SSCC")) {
					jButtonEditBatch.setVisible(false);
					calendarButtonexpiryDate.setVisible(true);
					expiryDate.setEnabled(true);
					expiryDate.setDate(pallet.getBatchExpiry());
				} else {
					try {
						expiryDate.setDate(materialBatch.getExpiryDate());
						jStatusText.setText("Expiry Date set from Batch " + jTextFieldMaterial.getText() + " / " + jTextFieldBatch.getText());
					} catch (Exception ex) {
						jStatusText.setText(ex.getMessage());
					}
				}

			} else {
				jStatusText.setText("Not found !");
				if (expiryMode.equals("BATCH")) {
					jStatusText.setText("No Batch Record Found " + jTextFieldMaterial.getText() + " / " + jTextFieldBatch.getText());
				}
			}

			jTextFieldLocation.setText(pallet.getLocationID());
			jFormattedTextFieldQuantity.setValue(pallet.getQuantity());

			try {
				productionDate.setDate(pallet.getDateOfManufacture());
			} catch (Exception ex) {
				productionDate.setDate(JUtility.getSQLDateTime());
			}

			jTextFieldEAN.setText(pallet.getEAN());
			jTextFieldVariant.setText(pallet.getVariant());

			checkBoxConfirmed.setSelected(pallet.isConfirmed());

			getMaterialUoms(pallet.getMaterial());
			paluom.getInternalUomProperties(pallet.getUom());
			textFieldDespatchNo.setText(pallet.getDespatchNo());
			jComboBoxUOM.setSelectedItem(paluom);
			jTextFieldLayers.setText(String.valueOf(pallet.getLayersOnPallet()));

			jComboBoxDefaultPalletStatus.setSelectedItem(pallet.getStatus());

			enableOrdisableFields(null);

			jButtonSave.setEnabled(false);
			jButtonUndo.setEnabled(false);
		}
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 476, 598);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
				jDesktopPane1.setLayout(null);
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jDesktopPane1.add(jTextFieldSSCC);
					jTextFieldSSCC.setEditable(false);
					jTextFieldSSCC.setEnabled(false);
					jTextFieldSSCC.setBounds(147, 6, 147, 21);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setBounds(3, 502, 111, 32);
					jButtonSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							save();
						}});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(229, 502, 111, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(342, 502, 111, 32);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					ComboBoxModel<JDBUom> jComboBoxBaseUOMModel = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxUOM);
					jComboBoxUOM.setModel(jComboBoxBaseUOMModel);
					jComboBoxUOM.setMaximumRowCount(12);
					jComboBoxUOM.setBounds(147, 280, 287, 23);
					jComboBoxUOM.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_UOM"));
					jComboBoxUOM.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							uomChanged();
						}
					});

				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Pallet_SSCC"));
					jLabel1.setBounds(7, 6, 133, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setBounds(7, 280, 133, 21);
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setText(lang.get("lbl_Material_UOM"));
				}
				{
					jLabel15 = new JLabel4j_std();
					jDesktopPane1.add(jLabel15);
					jLabel15.setText(lang.get("lbl_Pallet_Status"));
					jLabel15.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel15.setBounds(7, 389, 133, 21);
				}
				{
					textFieldDespatchNo = new JTextField4j(JDBDespatch.field_despatch_no);
					textFieldDespatchNo.setFocusCycleRoot(true);
					textFieldDespatchNo.setEnabled(false);
					textFieldDespatchNo.setBounds(147, 446, 119, 21);
					textFieldDespatchNo.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							despatchNoChanged();
						}
					});
					jDesktopPane1.add(textFieldDespatchNo);
				}
				{
					ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(Common.palletStatus);
					jComboBoxDefaultPalletStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxDefaultPalletStatus);
					jComboBoxDefaultPalletStatus.setModel(jComboBox1Model);
					jComboBoxDefaultPalletStatus.setBounds(147, 389, 168, 23);
					jComboBoxDefaultPalletStatus.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_STATUS"));
					jComboBoxDefaultPalletStatus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(7, 87, 133, 21);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(147, 87, 119, 21);
					jTextFieldMaterial.setEditable(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_MATERIAL"));
					jTextFieldMaterial.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							materialChanged();
						}
					});
				}
				{
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupMaterial);
					jButtonLookupMaterial.setBounds(266, 87, 21, 21);
					jButtonLookupMaterial.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_MATERIAL"));
					jButtonLookupMaterial.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials()) {
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
								materialChanged();

							}
						}
					});
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Material_Batch"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(7, 141, 133, 21);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setBounds(147, 141, 119, 21);
					jTextFieldBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_BATCH"));
					jTextFieldBatch.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							materialBatchChanged();
						}
					});
				}
				{
					jButtonLookupBatch = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupBatch);
					jButtonLookupBatch.setBounds(266, 141, 21, 21);
					jButtonLookupBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_BATCH"));
					jButtonLookupBatch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgCriteriaDefault = jTextFieldMaterial.getText();
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.materialBatches()) {
								jTextFieldBatch.setText(JLaunchLookup.dlgResult);
								materialBatchChanged();
							}
						}
					});
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Location_ID"));
					jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel4.setBounds(7, 226, 133, 21);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setBounds(147, 226, 119, 21);
					jTextFieldLocation.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_LOCATION"));
					jTextFieldLocation.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							locationChanged();
						}
					});
				}
				{
					jButtonLookupLocation = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupLocation);
					jButtonLookupLocation.setBounds(266, 226, 21, 21);
					jButtonLookupLocation.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_LOCATION"));
					jButtonLookupLocation.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations()) {
								jTextFieldLocation.setText(JLaunchLookup.dlgResult);
								locationChanged();
							}
						}
					});
				}
				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(7, 33, 133, 21);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(147, 33, 119, 21);
					jTextFieldProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));
					jTextFieldProcessOrder.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							processOrderChanged();
						}
					});
				}
				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButtonLookupProcessOrder);
					jButtonLookupProcessOrder.setBounds(266, 33, 21, 21);
					jButtonLookupProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));
					jButtonLookupProcessOrder.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders()) {
								jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
								processOrderChanged();
							}
						}
					});
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText(lang.get("lbl_Material_UOM_EAN"));
					jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel6.setBounds(7, 335, 133, 21);
				}
				{
					jTextFieldLayers = new JTextField4j();
					jTextFieldLayers.setFocusCycleRoot(true);
					jTextFieldLayers.setEnabled(false);
					jTextFieldLayers.setBounds(147, 308, 32, 21);
					jDesktopPane1.add(jTextFieldLayers);
				}
				{
					lblLayers = new JLabel4j_std();
					lblLayers.setText(lang.get("lbl_Pallet_Layers"));
					lblLayers.setHorizontalTextPosition(SwingConstants.RIGHT);
					lblLayers.setHorizontalAlignment(SwingConstants.RIGHT);
					lblLayers.setBounds(7, 308, 133, 21);
					jDesktopPane1.add(lblLayers);
				}
				{
					jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
					jDesktopPane1.add(jTextFieldEAN);
					jTextFieldEAN.setBounds(147, 335, 111, 21);
					jTextFieldEAN.setFocusCycleRoot(true);
					jTextFieldEAN.setEnabled(false);
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Material_UOM_Variant"));
					jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel7.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel7.setBounds(266, 335, 74, 21);
				}
				{
					jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);
					jDesktopPane1.add(jTextFieldVariant);
					jTextFieldVariant.setBounds(352, 335, 32, 21);
					jTextFieldVariant.setFocusCycleRoot(true);
					jTextFieldVariant.setEnabled(false);
				}
				{
					jLabelQuantity = new JLabel4j_std();
					jDesktopPane1.add(jLabelQuantity);
					jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelQuantity.setBounds(7, 253, 133, 21);
				}
				{
					jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
					jDesktopPane1.add(jFormattedTextFieldQuantity);
					jFormattedTextFieldQuantity.setFont(Common.font_std);
					jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jFormattedTextFieldQuantity.setBounds(147, 253, 91, 21);
					jFormattedTextFieldQuantity.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_QUANTITY"));
					jFormattedTextFieldQuantity.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});

				}
				{
					jLabelDespatchNo = new JLabel4j_std();
					jDesktopPane1.add(jLabelDespatchNo);
					jLabelDespatchNo.setText(lang.get("lbl_Despatch_No"));
					jLabelDespatchNo.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelDespatchNo.setBounds(7, 444, 133, 25);
				}
				{
					jLabelProductionDate = new JLabel4j_std();
					jDesktopPane1.add(jLabelProductionDate);
					jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
					jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProductionDate.setBounds(7, 416, 133, 25);
				}
				{
					productionDate = new JDateControl();
					jDesktopPane1.add(productionDate);
					productionDate.setBounds(147, 416, 125, 25);
					productionDate.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROD_DATE"));

				}

				productionDate.getEditor().addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						jButtonSave.setEnabled(true);
						jButtonUndo.setEnabled(true);
					}
				});

				productionDate.addChangeListener(new ChangeListener() {
					public void stateChanged(final ChangeEvent e)

					{
						jButtonSave.setEnabled(true);
						jButtonUndo.setEnabled(true);
					}
				});

				{
					jTextFieldProcessOrderDescription = new JTextField4j(JDBProcessOrder.field_description);
					jDesktopPane1.add(jTextFieldProcessOrderDescription);
					jTextFieldProcessOrderDescription.setBounds(147, 60, 287, 21);
					jTextFieldProcessOrderDescription.setEnabled(false);
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Description"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(7, 60, 133, 21);
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText(lang.get("lbl_Description"));
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel9.setBounds(7, 114, 133, 21);
				}
				{
					jTextFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
					jTextFieldMaterialDescription.setEditable(false);
					jDesktopPane1.add(jTextFieldMaterialDescription);
					jTextFieldMaterialDescription.setBounds(147, 114, 287, 21);
					jTextFieldMaterialDescription.setEnabled(false);
					jTextFieldMaterialDescription.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Material_Batch_Status"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(7, 168, 133, 21);
				}
				{
					jTextFieldBatchStatus = new JTextField4j(JDBMaterialBatch.field_batch_status);
					jDesktopPane1.add(jTextFieldBatchStatus);
					jTextFieldBatchStatus.setBounds(147, 168, 119, 21);
					jTextFieldBatchStatus.setEnabled(false);
					jTextFieldBatchStatus.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
				}
				{
					expiryDate = new JDateControl();
					expiryDate.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});

					jDesktopPane1.add(expiryDate);
					expiryDate.setEnabled(false);
					expiryDate.setBounds(147, 195, 125, 25);
					expiryDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
					expiryDate.getEditor().setSize(87, 21);
				}
				{
					jLabelBatchExpiry = new JLabel4j_std();
					jDesktopPane1.add(jLabelBatchExpiry);
					jLabelBatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabelBatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelBatchExpiry.setBounds(7, 199, 133, 21);
				}
				{
					jStatusText = new JLabel4j_std();
					jDesktopPane1.add(jStatusText);
					jStatusText.setForeground(new java.awt.Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jStatusText.setBounds(0, 540, 466, 21);
				}
				{
					jButton1 = new JButton4j(Common.icon_lookup);
					jDesktopPane1.add(jButton1);
					jButton1.setBounds(266, 168, 21, 21);
					jButton1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
					jButton1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", jTextFieldMaterial.getText(), jTextFieldBatch.getText());
						}
					});
				}
				{
					jButtonEditBatch = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEditBatch);
					jButtonEditBatch.setBounds(278, 195, 21, 25);
					jButtonEditBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
					jButtonEditBatch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", jTextFieldMaterial.getText(), jTextFieldBatch.getText());
						}
					});
				}

				{
					jButtonUndo = new JButton4j(Common.icon_undo);
					jButtonUndo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							refresh();
						}
					});
					jButtonUndo.setMnemonic(KeyEvent.VK_U);
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setBounds(116, 502, 111, 32);
					jDesktopPane1.add(jButtonUndo);
				}

				{
					jLabelProductionDate_1 = new JLabel4j_std();
					jLabelProductionDate_1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProductionDate_1.setText(lang.get("lbl_Confirmed"));
					jLabelProductionDate_1.setBounds(7, 470, 133, 24);
					jDesktopPane1.add(jLabelProductionDate_1);
				}

				{

					checkBoxConfirmed.setBackground(Color.WHITE);
					checkBoxConfirmed.addActionListener(new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});
					checkBoxConfirmed.setText("");
					checkBoxConfirmed.setBounds(143, 470, 32, 24);
					jDesktopPane1.add(checkBoxConfirmed);
				}
				{
					calendarButtonproductionDate = new JCalendarButton(productionDate);
					calendarButtonproductionDate.setBounds(275, 418, 21, 21);
					jDesktopPane1.add(calendarButtonproductionDate);
				}
				{
					calendarButtonexpiryDate = new JCalendarButton(expiryDate);
					calendarButtonexpiryDate.setBounds(275, 197, 21, 21);
					calendarButtonexpiryDate.setVisible(false);
					jDesktopPane1.add(calendarButtonexpiryDate);
				}
				{
					JLabel4j_std label = new JLabel4j_std();
					label.setText(lang.get("lbl_Customer_ID"));
					label.setHorizontalAlignment(SwingConstants.TRAILING);
					label.setBounds(12, 362, 128, 21);
					jDesktopPane1.add(label);

					textFieldCustomer = new JTextField4j(JDBCustomer.field_customer_id);
					textFieldCustomer.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent e) {
							customerIDChanged();
						}
					});
					textFieldCustomer.setBounds(147, 362, 126, 21);
					jDesktopPane1.add(textFieldCustomer);

					JButton4j button = new JButton4j(Common.icon_lookup);
					button.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_CUSTOMER"));
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.customers()) {
								textFieldCustomer.setText(JLaunchLookup.dlgResult);
								customerIDChanged();
							}
						}
					});
					button.setBounds(273, 362, 21, 21);
					jDesktopPane1.add(button);
				}
				{
					
					buttonRefreshMaterialData = new JButton4j(Common.icon_refresh);
					buttonRefreshMaterialData.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							materialChanged();
						}
					});
					buttonRefreshMaterialData.setToolTipText("Refresh Material Data");
					buttonRefreshMaterialData.setBounds(290, 87, 21, 21);
					buttonRefreshMaterialData.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_MATERIAL"));
					jDesktopPane1.add(buttonRefreshMaterialData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
