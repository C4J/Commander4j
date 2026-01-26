package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameProductionConfirmation.java
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

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameProductionConfirmation class is used to confirm an already
 * created SSCC.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameProductionConfirmation.jpg" >
 * JInternalFrameProductionDeclaration
 *
 * @see com.commander4j.app.JInternalFrameProductionDeclaration
 *      JInternalFrameProductionConfirmation
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 * @see com.commander4j.servlet.Process
 */
public class JInternalFrameProductionConfirmation extends javax.swing.JInternalFrame
{
	private JButton4j jButtonCancel;
	private JButton4j jButtonConfirm;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUnConfirm;
	private JCheckBox4j checkBoxConfirmed = new JCheckBox4j();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBLocation location = new JDBLocation(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JFormattedTextField jFormattedTextFieldQuantity;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel_BaseQuantity;
	private JLabel4j_std jLabel_BaseUOM;
	private JLabel4j_std jLabel_Batch;
	private JLabel4j_std jLabel_Batch_Status;
	private JLabel4j_std jLabel_Confirmed;
	private JLabel4j_std jLabel_EAN;
	private JLabel4j_std jLabel_Location;
	private JLabel4j_std jLabel_Material;
	private JLabel4j_std jLabel_Material_Description;
	private JLabel4j_std jLabel_Order_Description;
	private JLabel4j_std jLabel_ProcessOrder;
	private JLabel4j_std jLabel_Quantity;
	private JLabel4j_std jLabel_SSCC;
	private JLabel4j_std jLabel_UOM;
	private JLabel4j_std jLabel_Variant;
	private JQuantityInput jFormattedTextFieldBaseQuantity;
	private JTextField4j jTextFieldBaseUom;
	private JTextField4j jTextFieldBatch;
	private JTextField4j jTextFieldBatchStatus;
	private JTextField4j jTextFieldEAN;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldMaterialDescription;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JTextField4j jTextFieldProcessOrderStatus = new JTextField4j();
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldUom;
	private JTextField4j jTextFieldVariant;
	private String lsscc;
	private static final long serialVersionUID = 1;

	public JInternalFrameProductionConfirmation()
	{
		super();

		initGUI();
		blankfields();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_PROD_CONFIRM"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldSSCC.requestFocus();
				jTextFieldSSCC.setCaretPosition(jTextFieldSSCC.getText().length());

			}
		});
	}

	public JInternalFrameProductionConfirmation(String sscc)
	{
		this();
		setSSCC(sscc);

	}

	public void setSSCC(String sscc)
	{
		lsscc = sscc;
		jTextFieldSSCC.setText(lsscc);

		refresh();
	}

	private void refresh()
	{
		pallet.setSSCC(lsscc);

		if (pallet.isValidPallet())
		{
			pallet.getPalletProperties(lsscc);

			jTextFieldProcessOrder.setText(pallet.getProcessOrder());
			jTextFieldMaterial.setText(pallet.getMaterial());
			jTextFieldBatch.setText(pallet.getBatchNumber());
			jFormattedTextFieldQuantity.setValue(pallet.getQuantity());
			jTextFieldLocation.setText(pallet.getLocationID());

			jTextFieldEAN.setText(pallet.getEAN());
			jTextFieldVariant.setText(pallet.getVariant());

			checkBoxConfirmed.setSelected(pallet.isConfirmed());

			jTextFieldUom.setText(pallet.getUom());
			jTextFieldBaseUom.setText(pallet.getBaseUom());
			jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantity().toString());

			processOrderChanged();
			materialChanged();
			materialBatchChanged();
			locationChanged();

			if (pallet.isConfirmed())
			{
				jButtonConfirm.setEnabled(false);
				jButtonUnConfirm.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PAL_PROD_UNCONFIRM"));
			}
			else
			{
				jButtonConfirm.setEnabled(true);
				jButtonUnConfirm.setEnabled(false);
			}
			jStatusText.setText("SSCC " + pallet.getSSCC() + " retrieved.");
		}
		else
		{
			jStatusText.setText("SSCC " + pallet.getSSCC() + " does not exist.");
			blankfields();
		}
	}

	private void locationChanged()
	{
		if (location.getLocationProperties(jTextFieldLocation.getText()) == true)
		{
			jStatusText.setText(location.getErrorMessage());
		}
	}

	private void materialBatchChanged()
	{
		if (materialBatch.getMaterialBatchProperties(jTextFieldMaterial.getText(), jTextFieldBatch.getText()) == true)
		{
			jTextFieldBatchStatus.setText(materialBatch.getStatus());
		}

	}

	private void materialChanged()
	{
		if (material.getMaterialProperties(jTextFieldMaterial.getText()) == true)
		{
			jTextFieldMaterialDescription.setText(material.getDescription());
		}

	}

	private void processOrderChanged()
	{
		if (processOrder.getProcessOrderProperties(jTextFieldProcessOrder.getText()) == true)
		{
			jTextFieldProcessOrderDescription.setText(processOrder.getDescription());

			jTextFieldProcessOrderStatus.setText(processOrder.getStatus());

			if (jTextFieldProcessOrderStatus.getText().equals("Ready") || (jTextFieldProcessOrderStatus.getText().equals("Running")))
			{
				jTextFieldProcessOrderStatus.setBackground(Common.color_textfield_background_disabled);
			}
			else
			{
				jTextFieldProcessOrderStatus.setBackground(Color.RED);
			}
		}

	}

	private void blankfields()
	{

		jTextFieldProcessOrder.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldBatch.setText("");
		jFormattedTextFieldQuantity.setValue(0);

		jTextFieldEAN.setText("");
		jTextFieldVariant.setText("");

		checkBoxConfirmed.setSelected(false);

		jTextFieldBaseUom.setText("");
		jFormattedTextFieldBaseQuantity.setText("");

		jButtonConfirm.setEnabled(false);
		jButtonUnConfirm.setEnabled(false);

		jTextFieldProcessOrderStatus.setText("");
		jTextFieldProcessOrderStatus.setBackground(Common.color_textfield_background_disabled);
		jTextFieldProcessOrderDescription.setText("");
		jTextFieldMaterialDescription.setText("");
		jTextFieldLocation.setText("");
		jTextFieldLocation.setText("");
		jTextFieldBatchStatus.setText("");
		jTextFieldUom.setText("");

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 457, 547);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			this.getContentPane().setBackground(Common.color_app_window);

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jTextFieldSSCC.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(final KeyEvent e)
				{
					lsscc = jTextFieldSSCC.getText();
					lsscc = JUtility.replaceNullStringwithBlank(lsscc);
					if (lsscc.length() == 18)
					{
						refresh();
					}
					else
					{
						blankfields();
					}
				}
			});

			jDesktopPane1.setLayout(null);
			jDesktopPane1.add(jTextFieldSSCC);
			jTextFieldSSCC.setEditable(true);
			jTextFieldSSCC.setEnabled(true);
			jTextFieldSSCC.setBounds(147, 14, 133, 22);

			jButtonConfirm = new JButton4j(Common.icon_scanner_16x16);
			jDesktopPane1.add(jButtonConfirm);
			jButtonConfirm.setEnabled(true);
			jButtonConfirm.setText(lang.get("btn_Confirm"));
			jButtonConfirm.setMnemonic(lang.getMnemonicChar());
			jButtonConfirm.setBounds(8, 450, 110, 32);
			jButtonConfirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					boolean result = true;
					String errMsg = "";

					refresh();
					if (pallet.isConfirmed() == false)
					{
						pallet.setDateOfManufacture(JUtility.getSQLDateTime());
						pallet.setTransactionRef(0);
						result = pallet.confirm();
						if (result == true)
						{
							checkBoxConfirmed.setSelected(true);
							jStatusText.setText("SSCC " + pallet.getSSCC() + " confirmed.");
						}
						else
						{
							errMsg = pallet.getErrorMessage();
							jStatusText.setText(errMsg);
						}
						refresh();
					}
					else
					{
						jStatusText.setText("SSCC " + pallet.getSSCC() + " already confirmed.");
						// already confirmed
					}

					if (result == false)
					{
						JOptionPane.showMessageDialog(Common.mainForm, errMsg, lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					else
					{
						jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantity().toString());
					}
				}
			});

			jButtonUnConfirm = new JButton4j(Common.icon_unconfirm_16x16);
			jButtonUnConfirm.setText(lang.get("btn_Unconfirm"));
			jButtonUnConfirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					boolean result = true;
					String errMsg = "";

					refresh();
					if (pallet.isConfirmed() == true)
					{
						pallet.setTransactionRef(0);
						result = pallet.Unconfirm();
						if (result == true)
						{
							checkBoxConfirmed.setSelected(false);
							jStatusText.setText("SSCC " + pallet.getSSCC() + " Unconfirmed.");
						}
						else
						{
							errMsg = pallet.getErrorMessage();
							jStatusText.setText(errMsg);
						}
						refresh();
					}
					else
					{
						jStatusText.setText("SSCC " + pallet.getSSCC() + " already Unconfirmed.");
						// already confirmed
					}

					if (result == false)
					{
						JOptionPane.showMessageDialog(Common.mainForm, errMsg, lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
					}
					else
					{
						jFormattedTextFieldBaseQuantity.setText(pallet.getBaseQuantity().toString());
					}
				}
			});
			jButtonUnConfirm.setBounds(116, 450, 110, 32);
			jDesktopPane1.add(jButtonUnConfirm);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(225, 450, 110, 32);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(332, 450, 110, 32);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_SSCC = new JLabel4j_std();
			jDesktopPane1.add(jLabel_SSCC);
			jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
			jLabel_SSCC.setBounds(7, 14, 133, 22);
			jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabel_UOM = new JLabel4j_std();
			jDesktopPane1.add(jLabel_UOM);
			jLabel_UOM.setBounds(7, 275, 133, 22);
			jLabel_UOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_UOM.setText(lang.get("lbl_Material_UOM"));

			jLabel_Material = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Material);
			jLabel_Material.setText(lang.get("lbl_Material"));
			jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Material.setBounds(7, 98, 133, 22);

			jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
			jDesktopPane1.add(jTextFieldMaterial);
			jTextFieldMaterial.setBounds(147, 98, 119, 22);
			jTextFieldMaterial.setEnabled(false);

			jLabel_Batch = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Batch);
			jLabel_Batch.setText(lang.get("lbl_Material_Batch"));
			jLabel_Batch.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Batch.setBounds(7, 185, 133, 22);

			jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
			jDesktopPane1.add(jTextFieldBatch);
			jTextFieldBatch.setBounds(147, 185, 119, 22);
			jTextFieldBatch.setEnabled(false);

			jLabel_ProcessOrder = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ProcessOrder);
			jLabel_ProcessOrder.setText(lang.get("lbl_Process_Order"));
			jLabel_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ProcessOrder.setBounds(7, 42, 133, 22);

			jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
			jDesktopPane1.add(jTextFieldProcessOrder);
			jTextFieldProcessOrder.setBounds(147, 42, 119, 22);
			jTextFieldProcessOrder.setEnabled(false);

			jLabel_EAN = new JLabel4j_std();
			jDesktopPane1.add(jLabel_EAN);
			jLabel_EAN.setText(lang.get("lbl_Material_UOM_EAN"));
			jLabel_EAN.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_EAN.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_EAN.setBounds(7, 356, 133, 22);

			jTextFieldEAN = new JTextField4j(JDBMaterialUom.field_ean);
			jDesktopPane1.add(jTextFieldEAN);
			jTextFieldEAN.setBounds(147, 356, 119, 22);
			jTextFieldEAN.setFocusCycleRoot(true);
			jTextFieldEAN.setEnabled(false);

			jLabel_Variant = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Variant);
			jLabel_Variant.setText(lang.get("lbl_Material_UOM_Variant"));
			jLabel_Variant.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Variant.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Variant.setBounds(7, 384, 133, 22);

			jTextFieldVariant = new JTextField4j(JDBMaterialUom.field_variant);
			jDesktopPane1.add(jTextFieldVariant);
			jTextFieldVariant.setBounds(147, 384, 29, 22);
			jTextFieldVariant.setFocusCycleRoot(true);
			jTextFieldVariant.setEnabled(false);

			jLabel_Quantity = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Quantity);
			jLabel_Quantity.setText(lang.get("lbl_Pallet_Quantity"));
			jLabel_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Quantity.setBounds(7, 245, 133, 22);

			jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
			jDesktopPane1.add(jFormattedTextFieldQuantity);
			jFormattedTextFieldQuantity.setBounds(147, 245, 91, 22);
			jFormattedTextFieldQuantity.setEnabled(false);

			jTextFieldProcessOrderDescription = new JTextField4j(JDBProcessOrder.field_description);
			jDesktopPane1.add(jTextFieldProcessOrderDescription);
			jTextFieldProcessOrderDescription.setBounds(147, 70, 287, 22);
			jTextFieldProcessOrderDescription.setEnabled(false);

			jLabel_Order_Description = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Order_Description);
			jLabel_Order_Description.setText(lang.get("lbl_Description"));
			jLabel_Order_Description.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Order_Description.setBounds(7, 70, 133, 22);

			jLabel_Material_Description = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Material_Description);
			jLabel_Material_Description.setText(lang.get("lbl_Description"));
			jLabel_Material_Description.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Material_Description.setBounds(7, 126, 133, 22);

			jTextFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
			jDesktopPane1.add(jTextFieldMaterialDescription);
			jTextFieldMaterialDescription.setBounds(147, 126, 287, 22);
			jTextFieldMaterialDescription.setEnabled(false);

			jStatusText = new JLabel4j_status();
			jDesktopPane1.add(jStatusText);
			jStatusText.setBounds(8, 490, 434, 21);

			jLabel_BaseQuantity = new JLabel4j_std();
			jLabel_BaseQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BaseQuantity.setText(lang.get("lbl_Pallet_Base_Quantity"));
			jLabel_BaseQuantity.setBounds(7, 302, 133, 22);
			jDesktopPane1.add(jLabel_BaseQuantity);

			jFormattedTextFieldBaseQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldBaseQuantity.setFocusable(false);
			jFormattedTextFieldBaseQuantity.setEnabled(false);
			jFormattedTextFieldBaseQuantity.setBounds(147, 302, 91, 22);
			jDesktopPane1.add(jFormattedTextFieldBaseQuantity);

			jTextFieldBaseUom = new JTextField4j(JDBUom.field_uom);
			jTextFieldBaseUom.setEnabled(false);
			jTextFieldBaseUom.setFocusCycleRoot(true);
			jTextFieldBaseUom.setBounds(147, 329, 119, 22);
			jDesktopPane1.add(jTextFieldBaseUom);

			jLabel_BaseUOM = new JLabel4j_std();
			jLabel_BaseUOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_BaseUOM.setText(lang.get("lbl_Pallet_Base_UOM"));
			jLabel_BaseUOM.setBounds(7, 329, 133, 22);
			jDesktopPane1.add(jLabel_BaseUOM);

			jLabel_Confirmed = new JLabel4j_std();
			jLabel_Confirmed.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Confirmed.setText(lang.get("lbl_Confirmed"));
			jLabel_Confirmed.setBounds(7, 410, 133, 22);
			jDesktopPane1.add(jLabel_Confirmed);

			checkBoxConfirmed.setBackground(Color.WHITE);
			checkBoxConfirmed.setText("");
			checkBoxConfirmed.setBounds(147, 410, 32, 22);
			checkBoxConfirmed.setEnabled(false);
			jDesktopPane1.add(checkBoxConfirmed);

			jLabel_Location = new JLabel4j_std();
			jLabel_Location.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Location.setText(lang.get("lbl_Location_ID"));
			jLabel_Location.setBounds(7, 153, 133, 22);
			jDesktopPane1.add(jLabel_Location);

			jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
			jTextFieldLocation.setBounds(147, 153, 119, 22);
			jTextFieldLocation.setEnabled(false);
			jDesktopPane1.add(jTextFieldLocation);

			jLabel_Batch_Status = new JLabel4j_std();
			jLabel_Batch_Status.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Batch_Status.setText(lang.get("lbl_Material_Batch_Status"));
			jLabel_Batch_Status.setBounds(7, 215, 133, 22);
			jDesktopPane1.add(jLabel_Batch_Status);

			jTextFieldBatchStatus = new JTextField4j(JDBMaterialBatch.field_batch_status);
			jTextFieldBatchStatus.setEnabled(false);
			jTextFieldBatchStatus.setBounds(147, 215, 119, 22);
			jDesktopPane1.add(jTextFieldBatchStatus);

			jTextFieldUom = new JTextField4j(JDBUom.field_uom);
			jTextFieldUom.setBounds(147, 275, 119, 22);
			jTextFieldUom.setEnabled(false);
			jDesktopPane1.add(jTextFieldUom);

			jTextFieldProcessOrderStatus.setText("");
			jTextFieldProcessOrderStatus.setEnabled(false);
			jTextFieldProcessOrderStatus.setEditable(false);
			jTextFieldProcessOrderStatus.setBounds(308, 42, 126, 22);
			jDesktopPane1.add(jTextFieldProcessOrderStatus);

		}
		catch (Exception e)

		{
			e.printStackTrace();
		}
	}
}
