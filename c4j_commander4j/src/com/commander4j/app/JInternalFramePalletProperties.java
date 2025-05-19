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

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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
import com.commander4j.db.JDBEquipmentType;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePalletProperties allows you to view/edit all the fields on a
 * pallet record held in the APP_PALLET table. Any changes made will be written
 * to the APP_PALLET_HISTORY_TABLE
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 *
 */
public class JInternalFramePalletProperties extends javax.swing.JInternalFrame
{
	private final static int newSSCC = 1;
	private static final long serialVersionUID = 1;
	private final static int updateSSCC = 2;
	private JButton4j btn_BatchExpiry;
	private JButton4j btn_Location;
	private JButton4j btn_MaterialBatch;
	private JButton4j btn_ProcessOrder;
	private JButton4j btn_RefreshProcessOrder;
	private JCalendarButton calendarButtonexpiryDate;
	private JCalendarButton calendarButtonproductionDate;
	private String expiryMode;
	private String defaultBatchStatus;
	private String defaultPalletStatus;

	private JDateControl fld_BatchExpiry;
	private JTextField4j fld_BatchStatus;
	private JCheckBox4j fld_Confirmed = new JCheckBox4j();
	private JTextField4j fld_Customer;
	private JTextField4j fld_DespatchNo;
	private JDateControl fld_DOM;
	private JTextField4j fld_EAN;
	private JTextField4j fld_Equipment = new JTextField4j(JDBEquipmentType.field_EquipmentType);
	private JTextField4j fld_Layers;
	private JTextField4j fld_Location;
	private JTextField4j fld_Material;
	private JTextField4j fld_MaterialBatch;
	private JTextField4j fld_MaterialDescription;
	private JComboBox4j<String> fld_PalletStatus;
	private JTextField4j fld_ProcessOrder;
	private JTextField4j fld_ProcessOrderDescription;
	private JQuantityInput fld_Quantity;
	private JTextField4j fld_SSCC;
	private JTextField4j fld_UOM;
	private JTextField4j fld_Variant;
	private JDBPallet initial_pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JButton4j jButtonUndo;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jStatusText;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lbl_BatchExpiry;
	private JLabel4j_std lbl_BatchStatus;
	private JLabel4j_std lbl_Confirmed;
	private JLabel4j_std lbl_DespatchNo;
	private JLabel4j_std lbl_DOM;
	private JLabel4j_std lbl_EAN;
	private JLabel4j_std lbl_Layers;
	private JLabel4j_std lbl_Location;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_MaterialBatch;
	private JLabel4j_std lbl_MaterialDescription;
	private JLabel4j_std lbl_PalletStatus;
	private JLabel4j_std lbl_ProcessOrder;
	private JLabel4j_std lbl_ProcessOrderDescription;
	private JLabel4j_std lbl_Quantity;
	private JLabel4j_std lbl_SSCC;
	private JLabel4j_std lbl_UOM;
	private JLabel4j_std lbl_Variant;
	private String lsscc;
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialUom materialUom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);

	private JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private int palletMode = 0;
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);

	public JInternalFramePalletProperties(String sscc)
	{

		super();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_EDIT"));

		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
		defaultBatchStatus = ctrl.getKeyValue("DEFAULT BATCH STATUS");
		defaultPalletStatus = ctrl.getKeyValue("DEFAULT PALLET STATUS");

		initGUI();

		setPalletSSCC(sscc);

	}

	private void customerIDChanged(String cust)
	{

		pallet.setCustomerID(cust);

		refresh();

	}

	private void equipmentChanged()
	{

		pallet.setEquipmentType(fld_Equipment.getText());

		refresh();

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 471, 630);
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
					fld_SSCC = new JTextField4j(JDBPallet.field_sscc);
					fld_SSCC.setEnabled(false);
					jDesktopPane1.add(fld_SSCC);
					fld_SSCC.setEditable(false);
					fld_SSCC.setBounds(147, 6, 147, 22);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setBounds(3, 533, 111, 32);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(229, 533, 111, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(342, 533, 111, 32);
					jButtonCancel.addActionListener(new ActionListener()
					{

						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					fld_UOM = new JTextField4j();
					fld_UOM.setEnabled(false);
					fld_UOM.setEditable(false);
					jDesktopPane1.add(fld_UOM);
					fld_UOM.setBounds(147, 276, 32, 22);

				}
				{
					lbl_SSCC = new JLabel4j_std();
					jDesktopPane1.add(lbl_SSCC);
					lbl_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
					lbl_SSCC.setBounds(7, 6, 133, 22);
					lbl_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					lbl_UOM = new JLabel4j_std();
					jDesktopPane1.add(lbl_UOM);
					lbl_UOM.setBounds(7, 276, 133, 22);
					lbl_UOM.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_UOM.setText(lang.get("lbl_Material_UOM"));
				}
				{
					lbl_PalletStatus = new JLabel4j_std();
					jDesktopPane1.add(lbl_PalletStatus);
					lbl_PalletStatus.setText(lang.get("lbl_Pallet_Status"));
					lbl_PalletStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_PalletStatus.setBounds(7, 384, 133, 22);
				}
				{
					fld_DespatchNo = new JTextField4j(JDBDespatch.field_despatch_no);
					fld_DespatchNo.setEnabled(false);
					fld_DespatchNo.setEditable(false);
					fld_DespatchNo.setBounds(147, 438, 119, 22);
					jDesktopPane1.add(fld_DespatchNo);
				}
				{
					ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(Common.palletStatus);
					fld_PalletStatus = new JComboBox4j<String>();
					jDesktopPane1.add(fld_PalletStatus);
					fld_PalletStatus.setModel(jComboBox1Model);
					fld_PalletStatus.setBounds(147, 384, 168, 22);
					fld_PalletStatus.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_STATUS"));
					fld_PalletStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							palletStatusChanged(fld_PalletStatus.getSelectedItem().toString());
						}
					});
				}

				{
					fld_BatchStatus = new JTextField4j();
					fld_BatchStatus.setEnabled(false);
					fld_BatchStatus.setEditable(false);
					jDesktopPane1.add(fld_BatchStatus);
					fld_BatchStatus.setBounds(147, 168, 168, 22);
				}

				{
					lbl_Material = new JLabel4j_std();
					jDesktopPane1.add(lbl_Material);
					lbl_Material.setText(lang.get("lbl_Material"));
					lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Material.setBounds(7, 87, 133, 22);
				}
				{
					fld_Material = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(fld_Material);
					fld_Material.setBounds(147, 87, 119, 22);
					fld_Material.setEditable(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_MATERIAL"));
					fld_Material.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							materialChanged(fld_Material.getText());
						}
					});
				}
				{
					lbl_MaterialBatch = new JLabel4j_std();
					jDesktopPane1.add(lbl_MaterialBatch);
					lbl_MaterialBatch.setText(lang.get("lbl_Material_Batch"));
					lbl_MaterialBatch.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_MaterialBatch.setBounds(7, 141, 133, 22);
				}
				{
					fld_MaterialBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(fld_MaterialBatch);
					fld_MaterialBatch.setBounds(147, 141, 119, 22);
					fld_MaterialBatch.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							materialBatchChanged(fld_MaterialBatch.getText());
						}
					});
				}
				{
					btn_MaterialBatch = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(btn_MaterialBatch);
					btn_MaterialBatch.setBounds(266, 141, 21, 22);
					btn_MaterialBatch.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_BATCH"));
					btn_MaterialBatch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = fld_Material.getText();
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.materialBatches())
							{
								fld_MaterialBatch.setText(JLaunchLookup.dlgResult);
								materialBatchChanged(fld_MaterialBatch.getText());
							}
						}
					});
				}
				{
					lbl_Location = new JLabel4j_std();
					jDesktopPane1.add(lbl_Location);
					lbl_Location.setText(lang.get("lbl_Location_ID"));
					lbl_Location.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Location.setBounds(7, 222, 133, 22);
				}
				{
					fld_Location = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(fld_Location);
					fld_Location.setBounds(147, 222, 119, 22);
					fld_Location.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_LOCATION"));
					fld_Location.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							//fld_Location.setText(JLaunchLookup.dlgResult);
							locationChanged(fld_Location.getText());

						}
					});
				}
				{
					btn_Location = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(btn_Location);
					btn_Location.setBounds(266, 222, 21, 22);
					btn_Location.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_LOCATION"));
					btn_Location.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								fld_Location.setText(JLaunchLookup.dlgResult);
								locationChanged(fld_Location.getText());
							}
						}
					});
				}
				{
					lbl_ProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(lbl_ProcessOrder);
					lbl_ProcessOrder.setText(lang.get("lbl_Process_Order"));
					lbl_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_ProcessOrder.setBounds(7, 33, 133, 22);
				}
				{
					fld_ProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(fld_ProcessOrder);
					fld_ProcessOrder.setBounds(147, 33, 119, 22);
					fld_ProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));
					fld_ProcessOrder.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							processOrderChanged(fld_ProcessOrder.getText());
						}
					});
				}
				{
					btn_ProcessOrder = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(btn_ProcessOrder);
					btn_ProcessOrder.setBounds(266, 33, 21, 22);
					btn_ProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));
					btn_ProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders())
							{
								pallet.setProcessOrder(JLaunchLookup.dlgResult);

								processOrderChanged(JLaunchLookup.dlgResult);
							}
						}
					});
				}
				{
					btn_RefreshProcessOrder = new JButton4j(Common.icon_refresh_16x16);
					btn_RefreshProcessOrder.setToolTipText(lang.get("btn_Refresh"));
					jDesktopPane1.add(btn_RefreshProcessOrder);
					btn_RefreshProcessOrder.setBounds(287, 33, 21, 22);
					btn_RefreshProcessOrder.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_PROCESS_ORDER"));

					btn_RefreshProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (processOrder.getProcessOrderProperties(fld_ProcessOrder.getText()) == true)
							{
								if (material.getMaterialProperties(processOrder.getMaterial()) == true)
								{
									if (materialUom.getMaterialUomProperties(processOrder.getMaterial(), processOrder.getRequiredUom())==true)
									{
										
										fld_ProcessOrderDescription.setText(processOrder.getDescription());
										
										pallet.setMaterial(processOrder.getMaterial());
										fld_Material.setText(processOrder.getMaterial());
										fld_MaterialDescription.setText(material.getDescription());
										
										pallet.setCustomerID(processOrder.getCustomerID());
										fld_Customer.setText(processOrder.getCustomerID());
										
										pallet.setUom(processOrder.getRequiredUom());
										fld_UOM.setText(pallet.getUom());
										
										pallet.setEAN(materialUom.getEan());
										fld_EAN.setText(pallet.getEAN());
										
										pallet.setVariant(materialUom.getVariant());
										fld_Variant.setText(pallet.getVariant());	
										
										pallet.setCustomerID(processOrder.getCustomerID());
										fld_Customer.setText(pallet.getCustomerID());
										
										pallet.calcBaseUOMQuantity(pallet.getUom(), pallet.getQuantity());
										
										fld_Layers.setText(String.valueOf(pallet.calcLayersOnPallet(pallet.getBaseQuantity())));
										pallet.setLayersOnPallet(pallet.calcLayersOnPallet(pallet.getBaseQuantity()));
									}
									
								}
							}
						}
					});
				}
				{
					lbl_EAN = new JLabel4j_std();
					jDesktopPane1.add(lbl_EAN);
					lbl_EAN.setText(lang.get("lbl_Material_UOM_EAN"));
					lbl_EAN.setHorizontalAlignment(SwingConstants.RIGHT);
					lbl_EAN.setHorizontalTextPosition(SwingConstants.RIGHT);
					lbl_EAN.setBounds(7, 330, 133, 22);
				}
				{
					fld_Layers = new JTextField4j();
					fld_Layers.setEnabled(false);
					fld_Layers.setEditable(false);
					fld_Layers.setBounds(147, 303, 32, 22);
					jDesktopPane1.add(fld_Layers);
				}
				{
					lbl_Layers = new JLabel4j_std();
					lbl_Layers.setText(lang.get("lbl_Pallet_Layers"));
					lbl_Layers.setHorizontalTextPosition(SwingConstants.RIGHT);
					lbl_Layers.setHorizontalAlignment(SwingConstants.RIGHT);
					lbl_Layers.setBounds(7, 303, 133, 22);
					jDesktopPane1.add(lbl_Layers);
				}
				{
					fld_EAN = new JTextField4j();
					fld_EAN.setEnabled(false);
					fld_EAN.setEditable(false);
					jDesktopPane1.add(fld_EAN);
					fld_EAN.setBounds(147, 330, 111, 22);
				}
				{
					lbl_Variant = new JLabel4j_std();
					jDesktopPane1.add(lbl_Variant);
					lbl_Variant.setText(lang.get("lbl_Material_UOM_Variant"));
					lbl_Variant.setHorizontalAlignment(SwingConstants.RIGHT);
					lbl_Variant.setHorizontalTextPosition(SwingConstants.RIGHT);
					lbl_Variant.setBounds(266, 330, 74, 22);
				}
				{
					fld_Variant = new JTextField4j();
					fld_Variant.setEnabled(false);
					fld_Variant.setEditable(false);
					jDesktopPane1.add(fld_Variant);
					fld_Variant.setBounds(352, 330, 32, 22);
				}
				{
					lbl_Quantity = new JLabel4j_std();
					jDesktopPane1.add(lbl_Quantity);
					lbl_Quantity.setText(lang.get("lbl_Pallet_Quantity"));
					lbl_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Quantity.setBounds(7, 249, 133, 22);
				}
				{
					fld_Quantity = new JQuantityInput(new BigDecimal("0"));
					fld_Quantity.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);

							pallet.setQuantity(fld_Quantity.getQuantity());
							fld_Layers.setText(String.valueOf(pallet.calcLayersOnPallet(pallet.getBaseQuantity())));

						}
					});
					jDesktopPane1.add(fld_Quantity);
					fld_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
					fld_Quantity.setBounds(147, 249, 91, 22);
					fld_Quantity.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_QUANTITY"));
					fld_Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
							pallet.setQuantity(fld_Quantity.getQuantity());
							fld_Layers.setText(String.valueOf(pallet.calcLayersOnPallet(pallet.getBaseQuantity())));

						}
					});

				}
				{
					lbl_DespatchNo = new JLabel4j_std();
					jDesktopPane1.add(lbl_DespatchNo);
					lbl_DespatchNo.setText(lang.get("lbl_Despatch_No"));
					lbl_DespatchNo.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_DespatchNo.setBounds(7, 438, 133, 22);
				}
				{
					lbl_DOM = new JLabel4j_std();
					jDesktopPane1.add(lbl_DOM);
					lbl_DOM.setText(lang.get("lbl_Pallet_DOM"));
					lbl_DOM.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_DOM.setBounds(7, 411, 133, 22);
				}
				{
					fld_DOM = new JDateControl();
					fld_DOM.setDisplayMode(JDateControl.mode_disable_visible);
					jDesktopPane1.add(fld_DOM);
					fld_DOM.setBounds(147, 411, 128, 22);

				}

				fld_DOM.getEditor().addKeyListener(new KeyAdapter()
				{

					public void keyPressed(KeyEvent e)
					{
						jButtonSave.setEnabled(true);
						jButtonUndo.setEnabled(true);
					}
				});

				fld_DOM.addChangeListener(new ChangeListener()
				{
					public void stateChanged(final ChangeEvent e)

					{
						jButtonSave.setEnabled(true);
						jButtonUndo.setEnabled(true);

						pallet.setDateOfManufacture(JUtility.getTimestampFromDate(fld_DOM.getDate()));

					}
				});

				{
					fld_ProcessOrderDescription = new JTextField4j(JDBProcessOrder.field_description);
					fld_ProcessOrderDescription.setEnabled(false);
					fld_ProcessOrderDescription.setEditable(false);
					jDesktopPane1.add(fld_ProcessOrderDescription);
					fld_ProcessOrderDescription.setBounds(147, 60, 287, 22);
				}
				{
					lbl_ProcessOrderDescription = new JLabel4j_std();
					jDesktopPane1.add(lbl_ProcessOrderDescription);
					lbl_ProcessOrderDescription.setText(lang.get("lbl_Description"));
					lbl_ProcessOrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_ProcessOrderDescription.setBounds(7, 60, 133, 22);
				}
				{
					lbl_MaterialDescription = new JLabel4j_std();
					jDesktopPane1.add(lbl_MaterialDescription);
					lbl_MaterialDescription.setText(lang.get("lbl_Description"));
					lbl_MaterialDescription.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_MaterialDescription.setBounds(7, 114, 133, 22);
				}
				{
					fld_MaterialDescription = new JTextField4j(JDBMaterial.field_description);
					fld_MaterialDescription.setEditable(false);
					jDesktopPane1.add(fld_MaterialDescription);
					fld_MaterialDescription.setBounds(147, 114, 287, 22);
					fld_MaterialDescription.setEnabled(false);

				}
				{
					lbl_BatchStatus = new JLabel4j_std();
					jDesktopPane1.add(lbl_BatchStatus);
					lbl_BatchStatus.setText(lang.get("lbl_Material_Batch_Status"));
					lbl_BatchStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_BatchStatus.setBounds(7, 168, 133, 22);
				}

				{
					btn_BatchExpiry = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(btn_BatchExpiry);
					btn_BatchExpiry.setBounds(278, 195, 21, 22);
					btn_BatchExpiry.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
					btn_BatchExpiry.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchMenu.runDialog("FRM_ADMIN_MATERIAL_BATCH_EDIT", fld_Material.getText(), fld_MaterialBatch.getText());
							materialBatchChanged(fld_MaterialBatch.getText());
						}
					});
				}

				{
					fld_BatchExpiry = new JDateControl();
					fld_BatchExpiry.setDisplayMode(JDateControl.mode_disable_visible);
					fld_BatchExpiry.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent e)
						{
							Date exp = pallet.getMaterialObj().getRoundedExpiryDate(fld_BatchExpiry.getDate());
							pallet.setBatchExpiry(JUtility.getTimestampFromDate(exp));
							// fld_BatchExpiry.setDate(exp);
							jButtonSave.setEnabled(true);
							jButtonUndo.setEnabled(true);
						}
					});

					jDesktopPane1.add(fld_BatchExpiry);

					if (expiryMode.equals("SSCC"))
					{
						fld_BatchExpiry.setEnabled(true);
						// btn_BatchExpiry.setEnabled(true);
					}
					else
					{
						fld_BatchExpiry.setEnabled(false);
						// btn_BatchExpiry.setEnabled(false);
					}

					fld_BatchExpiry.setBounds(147, 195, 128, 22);
					fld_BatchExpiry.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
					fld_BatchExpiry.getEditor().setSize(87, 21);
				}
				{
					lbl_BatchExpiry = new JLabel4j_std();
					jDesktopPane1.add(lbl_BatchExpiry);
					lbl_BatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					lbl_BatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_BatchExpiry.setBounds(7, 195, 133, 22);
				}
				{
					jStatusText = new JLabel4j_std();
					jDesktopPane1.add(jStatusText);
					jStatusText.setForeground(new java.awt.Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jStatusText.setBounds(0, 571, 466, 21);
				}

				{
					jButtonUndo = new JButton4j(Common.icon_undo_16x16);
					jButtonUndo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							pallet.getPalletProperties(lsscc);
							refresh();
							jButtonSave.setEnabled(false);
						}
					});
					jButtonUndo.setMnemonic(KeyEvent.VK_U);
					jButtonUndo.setText(lang.get("btn_Undo"));
					jButtonUndo.setBounds(116, 533, 111, 32);
					jDesktopPane1.add(jButtonUndo);
				}

				{
					lbl_Confirmed = new JLabel4j_std();
					lbl_Confirmed.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Confirmed.setText(lang.get("lbl_Confirmed"));
					lbl_Confirmed.setBounds(7, 492, 133, 22);
					jDesktopPane1.add(lbl_Confirmed);
				}

				{

					fld_Confirmed.setBackground(Color.WHITE);
					fld_Confirmed.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							pallet.setConfirmed(fld_Confirmed.isSelected());
							if (pallet.isConfirmed())
							{
								fld_Confirmed.setEnabled(false);
							}
							else
							{
								fld_Confirmed.setEnabled(true);
							}
						}
					});
					fld_Confirmed.setText("");
					fld_Confirmed.setBounds(147, 492, 21, 22);
					jDesktopPane1.add(fld_Confirmed);
				}
				{
					calendarButtonproductionDate = new JCalendarButton(fld_DOM);
					calendarButtonproductionDate.setBounds(275, 412, 21, 21);
					jDesktopPane1.add(calendarButtonproductionDate);
				}
				{
					calendarButtonexpiryDate = new JCalendarButton(fld_BatchExpiry);
					calendarButtonexpiryDate.setBounds(275, 197, 21, 21);
					calendarButtonexpiryDate.setVisible(false);
					jDesktopPane1.add(calendarButtonexpiryDate);
				}
				{
					JLabel4j_std lbl_Customer = new JLabel4j_std();
					lbl_Customer.setText(lang.get("lbl_Customer_ID"));
					lbl_Customer.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_Customer.setBounds(7, 357, 133, 22);
					jDesktopPane1.add(lbl_Customer);

					fld_Customer = new JTextField4j(JDBCustomer.field_customer_id);
					fld_Customer.setEnabled(false);
					fld_Customer.setEditable(false);
					fld_Customer.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent e)
						{
							customerIDChanged(fld_Customer.getText());
						}
					});
					fld_Customer.setBounds(147, 357, 126, 22);
					jDesktopPane1.add(fld_Customer);
				}

				JLabel4j_std lbl_Equipment = new JLabel4j_std();
				lbl_Equipment.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Equipment.setBounds(7, 465, 133, 22);
				lbl_Equipment.setText(lang.get("lbl_Material_Equipment_Type"));
				jDesktopPane1.add(lbl_Equipment);
				fld_Equipment.setEnabled(false);
				fld_Equipment.setEditable(false);

				fld_Equipment.setBounds(147, 465, 126, 22);
				jDesktopPane1.add(fld_Equipment);

				JButton4j jButtonLookupEquipment = new JButton4j(Common.icon_lookup_16x16);
				jButtonLookupEquipment.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PALLET_EDIT_EQUIPMENT"));
				jButtonLookupEquipment.setBounds(273, 465, 21, 22);
				jButtonLookupEquipment.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "Y";
						if (JLaunchLookup.equipmentType())
						{
							fld_Equipment.setText(JLaunchLookup.dlgResult);
							equipmentChanged();
						}
					}
				});
				jDesktopPane1.add(jButtonLookupEquipment);
			}
		}
		catch (

		Exception e)
		{
			e.printStackTrace();
		}
	}

	private void locationChanged(String locn)
	{
		pallet.setLocationID(locn);

		refresh();
	}

	private void materialBatchChanged(String batch)
	{
		pallet.setBatchNumber(batch);

		if (expiryMode.equals("BATCH"))
		{
			if (materialBatch.getMaterialBatchProperties(pallet.getMaterial(), pallet.getBatchNumber()))
			{
				fld_BatchExpiry.setDate(materialBatch.getExpiryDate());
			}
		}

		refresh();
	}

	private void palletStatusChanged(String status)
	{
		pallet.setStatus(fld_PalletStatus.getSelectedItem().toString());
		refresh();
	}

	private void materialChanged(String mater)
	{
		pallet.setMaterial(mater);
		refresh();
	}

	private void processOrderChanged(String ponum)
	{

		pallet.setProcessOrder(ponum);

		if (ponum.equals(""))
		{
			jButtonSave.setEnabled(false);
		}
		else
		{
			if (processOrder.getProcessOrderProperties(ponum))
			{
				pallet.populateFromProcessOrder();

				if (palletMode == newSSCC)
				{
					pallet.setQuantity(new BigDecimal(processOrder.getFullPalletQuantity()));

					material.getMaterialProperties(pallet.getMaterial());

					if (expiryMode.equals("SSCC"))
					{
						fld_BatchExpiry.setDate(material.calcBBE(fld_DOM.getDate(), material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
					}

				}
				refresh();
			}
			else
			{
				jButtonSave.setEnabled(false);
				pallet.setQuantity(new BigDecimal("0.000"));
				materialChanged("");
			}
		}

	}

	private void refresh()
	{
		boolean valid = true;
		String msg = "";

		fld_Customer.setText(pallet.getCustomerID());

		if (processOrder.getProcessOrderProperties(pallet.getProcessOrder()) == false)
		{
			if (valid)
			{
				msg = processOrder.getErrorMessage();
				valid = false;
			}
		}
		fld_ProcessOrder.setText(pallet.getProcessOrder());
		fld_ProcessOrderDescription.setText(processOrder.getDescription());
		fld_Material.setText(pallet.getMaterial());

		material.getMaterialProperties(pallet.getMaterial());

		fld_MaterialDescription.setText(material.getDescription());
		fld_MaterialBatch.setText(pallet.getBatchNumber());

		if (materialBatch.getMaterialBatchProperties(pallet.getMaterial(), pallet.getBatchNumber()) == false)
		{
			materialBatch.setStatus(defaultBatchStatus);
		}

		fld_BatchStatus.setText(materialBatch.getStatus());

		if (expiryMode.equals("SSCC"))
		{
			if ((pallet.getBatchExpiry() == null) == false)
			{
				fld_BatchExpiry.setDate(pallet.getBatchExpiry());
			}
		}
		else
		{
			try
			{
				if ((materialBatch.getExpiryDate() == null) == false)
				{
					fld_BatchExpiry.setDate(materialBatch.getExpiryDate());
				}
			}
			catch (Exception ex)
			{
			}
		}

		if (location.getLocationProperties(pallet.getLocationID()) == false)
		{
			if (valid)
			{
				msg = location.getErrorMessage();
				valid = false;
			}
		}

		fld_Location.setText(pallet.getLocationID());
		fld_Quantity.setValue(pallet.getQuantity());
		fld_Layers.setText(String.valueOf(pallet.calcLayersOnPallet(pallet.getBaseQuantity())));

		fld_UOM.setText(pallet.getUom());

		try
		{
			fld_DOM.setDate(pallet.getDateOfManufacture());
		}
		catch (Exception ex)
		{
			fld_DOM.setDate(JUtility.getSQLDateTime());
		}

		fld_EAN.setText(pallet.getEAN());
		fld_Variant.setText(pallet.getVariant());

		if (pallet.isConfirmed())
		{
			fld_Confirmed.setEnabled(false);
		}
		else
		{
			fld_Confirmed.setEnabled(true);
		}

		fld_Customer.setText(pallet.getCustomerID());

		fld_Confirmed.setSelected(pallet.isConfirmed());

		fld_DespatchNo.setText(pallet.getDespatchNo());

		fld_Equipment.setText(pallet.getEquipmentType());

		fld_Layers.setText(String.valueOf(pallet.getLayersOnPallet()));

		fld_PalletStatus.setSelectedItem(pallet.getStatus());

		jButtonSave.setEnabled(valid);
		jButtonUndo.setEnabled(true);
		jStatusText.setText(msg);

	}

	private void save()
	{

		boolean result = false;

		long txnRef = 0;

		Date exp = pallet.getMaterialObj().getRoundedExpiryDate(fld_BatchExpiry.getDate());
		pallet.setBatchExpiry(JUtility.getTimestampFromDate(exp));

		switch (palletMode)
		{

		case updateSSCC:
		{
			if (pallet.update())
			{
				if (pallet.getStatus().equals(initial_pallet.getStatus()) == false)
				{
					String newStatus = pallet.getStatus();
					pallet.setStatus(initial_pallet.getStatus());
					pallet.updateStatus(newStatus, true);
				}

				txnRef = pallet.writePalletHistory(txnRef, "EDIT", "TO");

				initial_pallet.writePalletHistory(txnRef, "EDIT", "FROM");

				result = true;

				initial_pallet.getPalletProperties(lsscc);
			}
		}
		case newSSCC:
		{
			if (pallet.create())
			{
				txnRef = pallet.writePalletHistory(txnRef, "EDIT", "CREATE");

				result = true;

				initial_pallet.getPalletProperties(lsscc);

				palletMode = updateSSCC;
			}

		}

		}

		if (result == false)
		{

			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, pallet.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);

		}
		else
		{
			jButtonSave.setEnabled(false);
		}

	}

	public void setPalletSSCC(String sscc)
	{
		lsscc = sscc;

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to SSCC [" + lsscc + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		this.setTitle("Pallet Properties [" + sscc + "]");

		pallet.setSSCC(lsscc);
		fld_SSCC.setText(lsscc);

		if (pallet.isValidPallet())
		{
			palletMode = updateSSCC;
			pallet.getPalletProperties(lsscc);
			initial_pallet.getPalletProperties(lsscc);
			refresh();
		}
		else
		{
			palletMode = newSSCC;

			refresh();

			fld_PalletStatus.setSelectedItem(defaultPalletStatus);
			fld_Confirmed.setSelected(true);
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fld_ProcessOrder.requestFocus();
				fld_ProcessOrder.setCaretPosition(fld_ProcessOrder.getText().length());

				setButtonUnmodified();

			}
		});
	}

	private void setButtonUnmodified()
	{
		jButtonSave.setEnabled(false);
		jButtonUndo.setEnabled(false);
	}

}
