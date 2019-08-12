package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialProperties.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWTContainerCode;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameMaterialProperties is the screen for editing the basic code
 * Material Data. From this screen you can insert/update/delete records from the
 * APP_MATERIAL and also launch additional screens for many of the related
 * detail tables.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameMaterialAdmin
 *      JInternalFrameMaterialProperties
 * @see com.commander4j.app.JInternalFrameMaterialUomAdmin
 *      JInternalFrameMaterialUomAdmin
 * @see com.commander4j.app.JInternalFrameMaterialBatchAdmin
 *      JInternalFrameMaterialBatchAdmin
 * @see com.commander4j.app.JInternalFrameMaterialCustomerDataAdmin
 *      JInternalFrameMaterialCustomerDataAdmin
 * @see com.commander4j.app.JInternalFrameMaterialTypeAdmin
 *      JInternalFrameMaterialTypeAdmin
 * @see com.commander4j.db.JDBMaterial JDBMaterial
 * @see com.commander4j.db.JDBMaterialUom JDBMaterialUom
 * @see com.commander4j.db.JDBMaterialLocation JDBMaterialLocation
 * @see com.commander4j.db.JDBMaterialType JDBMaterialType
 * @see com.commander4j.db.JDBMaterialCustomerData JDBMaterialCustomerData
 * @see com.commander4j.db.JDBMaterialBatch JDBMaterialBatch
 */
public class JInternalFrameMaterialProperties extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JComboBox4j<JShelfLifeRoundingRule> jComboBoxRoundingRule;
	private JLabel4j_std jLabel1RoundingRule;
	private JComboBox4j<JShelfLifeUom> jComboBoxShelfLifeUOM;
	private JButton4j jButtonCancel;
	private JComboBox4j<String> jComboBoxDefaultBatchStatus;
	private JLabel4j_std jLabelLegacyCode;
	private JButton4j jButtonBatches;
	private JButton4j jButtonUOMs;
	private JButton4j jButtonLocations;
	private JSpinner jSpinnerNetWeight;
	private JSpinner jSpinnerGrossWeight;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1ShelfLifeUOM;
	private JLabel4j_std jLabelBatchStatus;
	private JLabel4j_std jLabelShelfLife;
	private JLabel4j_std jLabelGrossWt;
	private JSpinner jSpinnerShelfLife;
	private JComboBox4j<JDBUom> jComboBoxWeightUOM;
	private JTextField4j jTextFieldLegacyCode;
	private JLabel4j_std jLabelWtUOM;
	private JLabel4j_std jLabelNetWt;
	private JLabel4j_std jLabelMatType;
	private JLabel4j_std jLabelBaseUOM;
	private JLabel4j_std jLabelDescription;
	private JLabel4j_std jLabelMaterialID;
	private JComboBox4j<JDBMaterialType> jComboBoxMaterialType;
	private JComboBox4j<JDBUom> jComboBoxBaseUOM;
	private JDBUom uom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType materialtype = new JDBMaterialType(Common.selectedHostID, Common.sessionID);;
	private JDBUom baseuom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JDBUom weightuom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JShelfLifeUom sluom = new JShelfLifeUom();
	private JShelfLifeRoundingRule slrr = new JShelfLifeRoundingRule();
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private Vector<JShelfLifeUom> shelfLifeUomList = new Vector<JShelfLifeUom>();
	private Vector<JShelfLifeRoundingRule> shelfLifeRule = new Vector<JShelfLifeRoundingRule>();
	private Vector<JDBMaterialType> typeList = new Vector<JDBMaterialType>();
	private String lmaterial;
	private SpinnerNumberModel shelflifenumbermodel = new SpinnerNumberModel();
	private SpinnerNumberModel grossweightnumbermodel = new SpinnerNumberModel((float) 0, null, null, 1);
	private SpinnerNumberModel netweightnumbermodel = new SpinnerNumberModel((double) 0, null, null, 1);
	private JLabel4j_std lblEquipment;
	private JLabel4j_std lblInspectionID;
	private JLabel4j_std lblValidateScanPallet;
	private JLabel4j_std lblValidateScanCase;
	private JLabel4j_std lblValidateScanEach;
	private JTextField4j jTextFieldEquipmentType;
	private JTextField4j jTextFieldInspectionID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private Vector<JDBListData> moduleList = new Vector<JDBListData>();
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
    private JDBMaterialCustomerData matCustData = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);

	private JCheckBox4j checkBoxOverridePackLabel = new JCheckBox4j();
	private JCheckBox4j checkBoxOverridePalletLabel = new JCheckBox4j();
	private JComboBox4j<JDBListData> comboBoxPackModuleID = new JComboBox4j<JDBListData>();
	private JComboBox4j<JDBListData> comboBoxPalletModuleID = new JComboBox4j<JDBListData>();
	private JButton4j jButtonCustomerData = new JButton4j();

	private JCheckBox4j checkBoxValidateScanPallet = new JCheckBox4j();
	private JCheckBox4j checkBoxValidateScanCase = new JCheckBox4j();
	private JCheckBox4j checkBoxValidateScanEach = new JCheckBox4j();
	private JCheckBox4j checkBox4j_Enabled = new JCheckBox4j();
	private JCheckBox4j checkBox4jMoveAfterMake = new JCheckBox4j();
	private JTextField4j textField4jMoveAfterMake = new JTextField4j(20);
	private JButton4j button4jLocationLookup = new JButton4j("");
	private JButton4j jButtonRemoveProductGroup= new JButton4j(Common.icon_despatch_remove_16x16);
	private JButton4j jButtonRemoveContainerCode= new JButton4j(Common.icon_despatch_remove_16x16);
	private JTextField4j textField4j_Product_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField4j textField4j_Container_Code = new JTextField4j(JDBWTContainerCode.field_ContainerCode);

	public JInternalFrameMaterialProperties(String mat)
	{

		super();

		initGUI();
		setMaterialID(mat);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	public void setMaterialID(String mat)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Material [" + lmaterial + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		lmaterial = mat;
		jTextFieldMaterial.setText(lmaterial);
		setTitle("Material [" + lmaterial + "]");

		material.setMaterial(lmaterial);

		if (material.isValidMaterial())
		{
			material.getMaterialProperties(lmaterial);

			materialtype.setType(material.getMaterialType());
			materialtype.getMaterialTypeProperties();

			baseuom.setInternalUom(material.getBaseUom());
			baseuom.getInternalUomProperties();

			weightuom.setInternalUom(material.getWeightUom());
			weightuom.getInternalUomProperties();

			sluom.getShelfLifeUomProperties(material.getShelfLifeUom());
			slrr.getShelfLifeRuleProperties(material.getShelfLifeRule());

			jTextFieldDescription.setText(material.getDescription());

			jTextFieldEquipmentType.setText(material.getEquipmentType());

			jTextFieldInspectionID.setText(material.getInspectionID());

			jSpinnerShelfLife.setValue((Number) material.getShelfLife());

			jSpinnerGrossWeight.setValue((BigDecimal) material.getGrossWeight());
			jSpinnerNetWeight.setValue((BigDecimal) material.getNetWeight());

			jTextFieldLegacyCode.setText(material.getOldMaterial());

			jComboBoxDefaultBatchStatus.setSelectedItem(material.getDefaultBatchStatus());

			checkBoxOverridePackLabel.setSelected(material.isOverridePackLabel());
			checkBoxOverridePalletLabel.setSelected(material.isOverridePalletLabel());

			checkBoxValidateScanPallet.setSelected(material.isValidateScanPallet());
			checkBoxValidateScanCase.setSelected(material.isValidateScanCase());
			checkBoxValidateScanEach.setSelected(material.isValidateScanEach());

			checkBox4j_Enabled.setSelected(material.isEnabled());

			checkBox4jMoveAfterMake.setSelected(material.isMoveAfterMakeEnabled());
			textField4jMoveAfterMake.setText(material.getMoveAfterMakeLocationID());

			comboBoxPackModuleID.setSelectedIndex(-1);
			comboBoxPalletModuleID.setSelectedIndex(-1);
			for (int x = 1; x < moduleList.size(); x++)
			{
				if (moduleList.get(x).getmData().equals(material.getPackLabelModuleID()))
				{
					comboBoxPackModuleID.setSelectedIndex(x);
				}
				if (moduleList.get(x).getmData().equals(material.getPalletLabelModuleID()))
				{
					comboBoxPalletModuleID.setSelectedIndex(x);
				}
			}
			
			matCustData.setMaterial(lmaterial);
			matCustData.setCustomerID("SELF");
			matCustData.setDataID("PRODUCT_GROUP");;

			if (matCustData.getMaterialCustomerDataProperties())
			{
				textField4j_Product_Group.setText(matCustData.getData());
			}
			
			matCustData.setDataID("CONTAINER_CODE");;

			if (matCustData.getMaterialCustomerDataProperties())
			{
				textField4j_Container_Code.setText(matCustData.getData());
			}

			setButtonStates();

		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				jButtonSave.setEnabled(false);
			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(422, 483));
			this.setBounds(0, 0, 668, 623);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			getContentPane().setLayout(null);

			moduleList.add(null);
			moduleList.addAll(mod.getModuleIdsByType("USER"));

			uomList.add(new JDBUom("", ""));
			uomList.addAll(uom.getInternalUoms());
			typeList.add(new JDBMaterialType(Common.selectedHostID, Common.sessionID));
			typeList.addAll(materialtype.getMaterialTypes());
			JShelfLifeUom slu = new JShelfLifeUom();
			slu.setUom("");
			slu.setDescription("");
			shelfLifeUomList.add(slu);
			shelfLifeUomList.addAll(slu.getShelfLifeUOMs());
			JShelfLifeRoundingRule slrr1 = new JShelfLifeRoundingRule();
			slrr1.setRule("");
			slrr1.setDescription("");
			shelfLifeRule.add(slrr1);
			shelfLifeRule.addAll(slrr1.getShelfLifeRoundingRules());

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 660, 591);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(447, 385));
				jDesktopPane1.setLayout(null);
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setEditable(false);
					jTextFieldMaterial.setEnabled(false);
					jTextFieldMaterial.setBounds(172, 9, 146, 21);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBMaterial.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(172, 38, 459, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
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
					jButtonSave.setBounds(453, 220, 178, 32);
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
					jButtonHelp.setBounds(453, 250, 178, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(453, 280, 178, 32);
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					ComboBoxModel<JDBUom> jComboBoxBaseUOMModel = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxBaseUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxBaseUOM);
					jComboBoxBaseUOM.setModel(jComboBoxBaseUOMModel);
					jComboBoxBaseUOM.setMaximumRowCount(12);
					jComboBoxBaseUOM.setBounds(172, 97, 248, 21);
					jComboBoxBaseUOM.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxBaseUOMModel.setSelectedItem(baseuom);

				}
				{
					ComboBoxModel<JDBMaterialType> jComboBoxMaterialTypeModel = new DefaultComboBoxModel<JDBMaterialType>(typeList);
					jComboBoxMaterialType = new JComboBox4j<JDBMaterialType>();
					jDesktopPane1.add(jComboBoxMaterialType);
					jComboBoxMaterialType.setModel(jComboBoxMaterialTypeModel);
					jComboBoxMaterialType.setBounds(172, 68, 248, 21);
					jComboBoxMaterialType.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxMaterialTypeModel.setSelectedItem(materialtype);
				}
				{
					jLabelBatchStatus = new JLabel4j_std();
					jDesktopPane1.add(jLabelBatchStatus);
					jLabelBatchStatus.setText(lang.get("lbl_Material_Default_Batch_Status"));
					jLabelBatchStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelBatchStatus.setBounds(6, 332, 159, 21);
				}
				{
					jLabel1ShelfLifeUOM = new JLabel4j_std();
					jDesktopPane1.add(jLabel1ShelfLifeUOM);
					jLabel1ShelfLifeUOM.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
					jLabel1ShelfLifeUOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1ShelfLifeUOM.setBounds(6, 155, 159, 21);
				}
				{
					ComboBoxModel<JShelfLifeUom> jComboBoxShelfLifeUOMModel = new DefaultComboBoxModel<JShelfLifeUom>(shelfLifeUomList);
					jComboBoxShelfLifeUOM = new JComboBox4j<JShelfLifeUom>();
					jDesktopPane1.add(jComboBoxShelfLifeUOM);
					jComboBoxShelfLifeUOM.setModel(jComboBoxShelfLifeUOMModel);
					jComboBoxShelfLifeUOM.setMaximumRowCount(12);
					jComboBoxShelfLifeUOM.setBounds(172, 156, 165, 21);
					jComboBoxShelfLifeUOM.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxShelfLifeUOMModel.setSelectedItem(sluom);
				}
				{
					jLabel1RoundingRule = new JLabel4j_std();
					jDesktopPane1.add(jLabel1RoundingRule);
					jLabel1RoundingRule.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
					jLabel1RoundingRule.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1RoundingRule.setBounds(6, 184, 159, 21);
				}
				{
					ComboBoxModel<JShelfLifeRoundingRule> jComboBoxRoundingRuleModel = new DefaultComboBoxModel<JShelfLifeRoundingRule>(shelfLifeRule);
					jComboBoxRoundingRule = new JComboBox4j<JShelfLifeRoundingRule>();
					jDesktopPane1.add(jComboBoxRoundingRule);
					jComboBoxRoundingRule.setModel(jComboBoxRoundingRuleModel);
					jComboBoxRoundingRule.setBounds(172, 185, 165, 21);
					jComboBoxRoundingRule.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxRoundingRuleModel.setSelectedItem(slrr);
				}
				{
					jLabelMaterialID = new JLabel4j_std();
					jDesktopPane1.add(jLabelMaterialID);
					jLabelMaterialID.setText(lang.get("lbl_Material"));
					jLabelMaterialID.setBounds(6, 8, 159, 21);
					jLabelMaterialID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setBounds(6, 38, 159, 21);
					jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelBaseUOM = new JLabel4j_std();
					jDesktopPane1.add(jLabelBaseUOM);
					jLabelBaseUOM.setText(lang.get("lbl_Material_Base_UOM"));
					jLabelBaseUOM.setBounds(6, 96, 159, 21);
					jLabelBaseUOM.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelMatType = new JLabel4j_std();
					jDesktopPane1.add(jLabelMatType);
					jLabelMatType.setText(lang.get("lbl_Material_Type"));
					jLabelMatType.setBounds(6, 67, 159, 21);
					jLabelMatType.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabelShelfLife = new JLabel4j_std();
					jDesktopPane1.add(jLabelShelfLife);
					jLabelShelfLife.setText(lang.get("lbl_Material_Shelf_Life"));
					jLabelShelfLife.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelShelfLife.setBounds(6, 126, 159, 21);
				}
				{
					jLabelGrossWt = new JLabel4j_std();
					jDesktopPane1.add(jLabelGrossWt);
					jLabelGrossWt.setText(lang.get("lbl_Material_Gross_Weight"));
					jLabelGrossWt.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelGrossWt.setBounds(6, 214, 159, 21);
					jLabelGrossWt.setFocusTraversalPolicyProvider(true);
				}
				{
					jLabelNetWt = new JLabel4j_std();
					jDesktopPane1.add(jLabelNetWt);
					jLabelNetWt.setText(lang.get("lbl_Material_Net_Weight"));
					jLabelNetWt.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelNetWt.setBounds(6, 244, 159, 21);
				}
				{
					jLabelWtUOM = new JLabel4j_std();
					jDesktopPane1.add(jLabelWtUOM);
					jLabelWtUOM.setText(lang.get("lbl_Material_Weight_UOM"));
					jLabelWtUOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelWtUOM.setBounds(6, 273, 159, 21);
				}
				{
					jTextFieldLegacyCode = new JTextField4j(JDBMaterial.field_old_material);
					jDesktopPane1.add(jTextFieldLegacyCode);
					jTextFieldLegacyCode.setBounds(172, 303, 125, 21);
					jTextFieldLegacyCode.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					ComboBoxModel<JDBUom> jComboBox1Model = new DefaultComboBoxModel<JDBUom>(uomList);
					jComboBoxWeightUOM = new JComboBox4j<JDBUom>();
					jDesktopPane1.add(jComboBoxWeightUOM);
					jComboBoxWeightUOM.setModel(jComboBox1Model);
					jComboBoxWeightUOM.setBounds(172, 274, 248, 21);
					jComboBoxWeightUOM.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jComboBox1Model.setSelectedItem(weightuom);
				}
				{
					jSpinnerShelfLife = new JSpinner();
					jDesktopPane1.add(jSpinnerShelfLife);
					jSpinnerShelfLife.setModel(shelflifenumbermodel);
					jSpinnerShelfLife.setBounds(172, 126, 95, 21);
					JSpinner.NumberEditor nec2 = new JSpinner.NumberEditor(jSpinnerShelfLife);
					nec2.getTextField().setFont(Common.font_std);
					jSpinnerShelfLife.setEditor(nec2);
					jSpinnerShelfLife.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jSpinnerGrossWeight = new JSpinner();

					jDesktopPane1.add(jSpinnerGrossWeight);
					jSpinnerGrossWeight.setModel(grossweightnumbermodel);

					jSpinnerGrossWeight.setBounds(172, 214, 95, 21);
					JSpinner.NumberEditor nec2 = new JSpinner.NumberEditor(jSpinnerGrossWeight);
					nec2.getTextField().setFont(Common.font_std);
					jSpinnerGrossWeight.setEditor(nec2);
					jSpinnerGrossWeight.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jSpinnerNetWeight = new JSpinner();
					jDesktopPane1.add(jSpinnerNetWeight);
					jSpinnerNetWeight.setModel(netweightnumbermodel);
					JSpinner.NumberEditor nec2 = new JSpinner.NumberEditor(jSpinnerNetWeight);
					nec2.getTextField().setFont(Common.font_std);
					jSpinnerNetWeight.setEditor(nec2);
					jSpinnerNetWeight.setBounds(172, 244, 95, 21);
					jSpinnerNetWeight.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonUOMs = new JButton4j();
					jDesktopPane1.add(jButtonUOMs);
					jButtonUOMs.setText(lang.get("btn_Material_UOM_Conversions"));
					jButtonUOMs.setIcon(Common.icon_uom_16x16);
					jButtonUOMs.setBounds(453, 100, 178, 32);
					jButtonUOMs.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_UOM"));
					jButtonUOMs.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							String base = ((JDBUom) jComboBoxBaseUOM.getSelectedItem()).getInternalUom();
							JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_UOM", lmaterial, base);
						}
					});
				}
				{
					JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
					String value = ctrl.getKeyValue("SSCC_LOCATION_VALIDATION");
					Boolean enabled = Boolean.valueOf(value);
					jButtonLocations = new JButton4j(Common.icon_location_16x16);
					jDesktopPane1.add(jButtonLocations);
					jButtonLocations.setText(lang.get("btn_Material_Locations"));
					jButtonLocations.setEnabled(enabled);
					jButtonLocations.setBounds(453, 130, 178, 32);
					if (enabled)
					{
						jButtonLocations.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION"));
						jButtonLocations.setToolTipText("");
					}
					else
					{
						jButtonLocations.setToolTipText("System key SSCC_LOCATION_VALIDATION is set to false");
					}
					jButtonLocations.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_LOCATION", lmaterial);
						}
					});

				}
				{
					jButtonBatches = new JButton4j();
					jDesktopPane1.add(jButtonBatches);
					jButtonBatches.setIcon(Common.icon_batch_16x16);
					jButtonBatches.setText(lang.get("btn_Material_Batches"));
					jButtonBatches.setBounds(453, 70, 178, 32);
					jButtonBatches.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH"));
					jButtonBatches.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH", lmaterial);
						}
					});
				}
				{
					jLabelLegacyCode = new JLabel4j_std();
					jDesktopPane1.add(jLabelLegacyCode);
					jLabelLegacyCode.setText(lang.get("lbl_Material_Legacy_Code"));
					jLabelLegacyCode.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelLegacyCode.setBounds(6, 302, 159, 21);
				}
				{
					ComboBoxModel<String> jComboBoxDefaultBatchStatusModel = new DefaultComboBoxModel<String>(Common.batchStatusIncBlank);
					jComboBoxDefaultBatchStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxDefaultBatchStatus);
					jComboBoxDefaultBatchStatus.setModel(jComboBoxDefaultBatchStatusModel);
					jComboBoxDefaultBatchStatus.setBounds(173, 332, 164, 21);
					jComboBoxDefaultBatchStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}

				checkBoxOverridePackLabel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						setButtonStates();
					}
				});

				checkBoxOverridePackLabel.setBackground(Color.WHITE);
				checkBoxOverridePackLabel.setBounds(172, 448, 21, 24);
				jDesktopPane1.add(checkBoxOverridePackLabel);
				checkBoxOverridePalletLabel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonStates();
					}
				});

				checkBoxValidateScanPallet.setBackground(Color.WHITE);
				checkBoxValidateScanPallet.setBounds(449, 328, 21, 24);
				jDesktopPane1.add(checkBoxValidateScanPallet);
				checkBoxValidateScanPallet.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				checkBoxValidateScanCase.setBackground(Color.WHITE);
				checkBoxValidateScanCase.setBounds(449, 358, 21, 24);
				jDesktopPane1.add(checkBoxValidateScanCase);
				checkBoxValidateScanCase.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				checkBoxValidateScanEach.setBackground(Color.WHITE);
				checkBoxValidateScanEach.setBounds(449, 387, 21, 24);
				jDesktopPane1.add(checkBoxValidateScanEach);
				checkBoxValidateScanEach.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				checkBoxOverridePalletLabel.setBackground(Color.WHITE);
				checkBoxOverridePalletLabel.setBounds(172, 481, 21, 24);
				jDesktopPane1.add(checkBoxOverridePalletLabel);

				JLabel4j_std label4j_stdPackModule = new JLabel4j_std();
				label4j_stdPackModule.setText(lang.get("lbl_Override_Pack_Label"));
				label4j_stdPackModule.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_stdPackModule.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_stdPackModule.setBounds(6, 451, 159, 21);
				jDesktopPane1.add(label4j_stdPackModule);

				JLabel4j_std label4j_stdPalletModule = new JLabel4j_std();
				label4j_stdPalletModule.setText(lang.get("lbl_Override_Pallet_Label"));
				label4j_stdPalletModule.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_stdPalletModule.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_stdPalletModule.setBounds(7, 484, 159, 21);
				jDesktopPane1.add(label4j_stdPalletModule);

				ComboBoxModel<JDBListData> jComboBox1Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBoxPackModuleID.setModel(jComboBox1Model);
				comboBoxPackModuleID.setEnabled(false);
				comboBoxPackModuleID.setBounds(199, 451, 208, 21);
				comboBoxPackModuleID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jDesktopPane1.add(comboBoxPackModuleID);

				ComboBoxModel<JDBListData> jComboBox2Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBoxPalletModuleID.setModel(jComboBox2Model);
				comboBoxPalletModuleID.setEnabled(false);
				comboBoxPalletModuleID.setBounds(199, 484, 208, 21);
				comboBoxPalletModuleID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBoxPalletModuleID);

				jButtonCustomerData.setText(lang.get("btn_Material_Customer_Data"));
				jButtonCustomerData.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_CUST_DATA"));
				jButtonCustomerData.setBounds(453, 160, 178, 32);
				jButtonCustomerData.setIcon(Common.icon_customer_16x16);
				jDesktopPane1.add(jButtonCustomerData);
				jButtonCustomerData.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_CUST_DATA", lmaterial);
					}
				});

				JButton4j button4jDataIDs = new JButton4j(Common.icon_material_16x16);
				button4jDataIDs.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchMenu.runForm("FRM_ADMIN_DATA_IDS");
					}
				});
				button4jDataIDs.setText(lang.get("mod_FRM_ADMIN_DATA_IDS"));
				button4jDataIDs.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_DATA_IDS"));
				button4jDataIDs.setBounds(453, 190, 178, 32);
				jDesktopPane1.add(button4jDataIDs);

				{
					lblEquipment = new JLabel4j_std();
					lblEquipment.setText(lang.get("lbl_Material_Equipment_Type"));
					lblEquipment.setHorizontalAlignment(SwingConstants.TRAILING);
					lblEquipment.setBounds(6, 361, 159, 21);
					jDesktopPane1.add(lblEquipment);
				}
				{
					lblInspectionID = new JLabel4j_std();
					lblInspectionID.setText(lang.get("lbl_Inspection_ID"));
					lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblInspectionID.setBounds(6, 390, 159, 21);
					jDesktopPane1.add(lblInspectionID);
				}
				{
					lblValidateScanPallet = new JLabel4j_std();
					lblValidateScanPallet.setText(lang.get("lbl_Validate_Pallet_GTIN"));
					lblValidateScanPallet.setBounds(472, 331, 159, 21);
					jDesktopPane1.add(lblValidateScanPallet);
				}
				{
					lblValidateScanCase = new JLabel4j_std();
					lblValidateScanCase.setText(lang.get("lbl_Validate_Case_GTIN"));
					lblValidateScanCase.setBounds(472, 361, 159, 21);
					jDesktopPane1.add(lblValidateScanCase);
				}
				{
					lblValidateScanEach = new JLabel4j_std();
					lblValidateScanEach.setText(lang.get("lbl_Validate_Each_GTIN"));
					lblValidateScanEach.setBounds(472, 390, 159, 21);
					jDesktopPane1.add(lblValidateScanEach);
				}
				{
					jTextFieldEquipmentType = new JTextField4j(JDBMaterial.field_equipment_type);
					jTextFieldEquipmentType.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldEquipmentType.setBounds(172, 362, 125, 21);
					jDesktopPane1.add(jTextFieldEquipmentType);
				}
				{
					jTextFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
					jTextFieldInspectionID.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldInspectionID.setBounds(172, 391, 125, 21);
					jDesktopPane1.add(jTextFieldInspectionID);
				}

				JLabel4j_std label4j_Enabled = new JLabel4j_std();
				label4j_Enabled.setText(lang.get("lbl_Enabled"));
				label4j_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Enabled.setBounds(364, 9, 84, 21);
				jDesktopPane1.add(label4j_Enabled);
				checkBox4j_Enabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				checkBox4j_Enabled.setSelected(true);
				checkBox4j_Enabled.setBackground(Color.WHITE);
				checkBox4j_Enabled.setBounds(453, 9, 21, 21);
				jDesktopPane1.add(checkBox4j_Enabled);
				checkBox4jMoveAfterMake.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				checkBox4jMoveAfterMake.setBackground(Color.WHITE);
				checkBox4jMoveAfterMake.setBounds(172, 419, 21, 24);
				jDesktopPane1.add(checkBox4jMoveAfterMake);

				JLabel4j_std label4j_std_MoveAfterMake = new JLabel4j_std();
				label4j_std_MoveAfterMake.setText(lang.get("lbl_Move_after_Make"));
				label4j_std_MoveAfterMake.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_std_MoveAfterMake.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_std_MoveAfterMake.setBounds(6, 423, 159, 21);
				jDesktopPane1.add(label4j_std_MoveAfterMake);
				textField4jMoveAfterMake.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				textField4jMoveAfterMake.setBounds(199, 422, 125, 21);
				jDesktopPane1.add(textField4jMoveAfterMake);

				button4jLocationLookup.setIcon(Common.icon_lookup_16x16);
				button4jLocationLookup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
						JLaunchLookup.dlgAutoExec = true;
						if (JLaunchLookup.locations())
						{
							textField4jMoveAfterMake.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				button4jLocationLookup.setBounds(323, 422, 21, 21);
				jDesktopPane1.add(button4jLocationLookup);

				{
					JButton4j btnLookupInspection = new JButton4j("");
					btnLookupInspection.setIcon(Common.icon_lookup_16x16);
					btnLookupInspection.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.qmInspections())
							{
								jTextFieldInspectionID.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					btnLookupInspection.setBounds(296, 391, 21, 21);
					jDesktopPane1.add(btnLookupInspection);
				}
				

				textField4j_Product_Group.setEnabled(false);
				textField4j_Product_Group.setEditable(false);
				textField4j_Product_Group.setBounds(172, 513, 217, 21);
				jDesktopPane1.add(textField4j_Product_Group);
				
				JLabel4j_std label4j_Product_Group = new JLabel4j_std();
				label4j_Product_Group.setText(lang.get("lbl_Product_Group"));
				label4j_Product_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Product_Group.setBounds(7, 513, 159, 21);
				jDesktopPane1.add(label4j_Product_Group);
				
				textField4j_Container_Code.setEnabled(false);
				textField4j_Container_Code.setEditable(false);
				textField4j_Container_Code.setBounds(172, 542, 109, 21);
				jDesktopPane1.add(textField4j_Container_Code);
				
				JLabel4j_std label4j_Container_Code = new JLabel4j_std();
				label4j_Container_Code.setText(lang.get("lbl_Container_Code"));
				label4j_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Container_Code.setBounds(7, 542, 159, 21);
				jDesktopPane1.add(label4j_Container_Code);
				
				JButton4j button4j_LookupProductGroup = new JButton4j(Common.icon_lookup_16x16);
				button4j_LookupProductGroup.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault="";
						if (JLaunchLookup.wtProductGroups())
						{
							if (textField4j_Product_Group.getText().equals(JLaunchLookup.dlgResult)==false)
							{
								textField4j_Product_Group.setText(JLaunchLookup.dlgResult);
								matCustData.setMaterial(lmaterial);
								matCustData.setCustomerID("SELF");
								matCustData.setDataID("PRODUCT_GROUP");;
								matCustData.setData(JLaunchLookup.dlgResult);
								if (matCustData.isValidMaterialCustomerData()==false)
								{
									matCustData.create();
								}
								matCustData.update();
							}
						}
					}
				});
				button4j_LookupProductGroup.setBounds(389, 513, 21, 21);
				jDesktopPane1.add(button4j_LookupProductGroup);
				
				JButton4j button4j_LookupContainerCode = new JButton4j(Common.icon_lookup_16x16);
				button4j_LookupContainerCode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault="";
						if (JLaunchLookup.wtContainerCode())
						{
							if (textField4j_Container_Code.getText().equals(JLaunchLookup.dlgResult)==false)
							{
								textField4j_Container_Code.setText(JLaunchLookup.dlgResult);
								matCustData.setMaterial(lmaterial);
								matCustData.setCustomerID("SELF");
								matCustData.setDataID("CONTAINER_CODE");;
								matCustData.setData(JLaunchLookup.dlgResult);
								if (matCustData.isValidMaterialCustomerData()==false)
								{
									matCustData.create();
								}
								matCustData.update();
							}
						}
					}
				});
				button4j_LookupContainerCode.setBounds(281, 542, 21, 21);
				jDesktopPane1.add(button4j_LookupContainerCode);
				jButtonRemoveProductGroup.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (textField4j_Product_Group.getText().equals("")==false)
						{
							textField4j_Product_Group.setText("");
							matCustData.setMaterial(lmaterial);
							matCustData.setCustomerID("SELF");
							matCustData.setDataID("PRODUCT_GROUP");;
							matCustData.delete();;
						}
					}
				});
				
				jButtonRemoveProductGroup.setBounds(411, 513, 21, 21);
				jDesktopPane1.add(jButtonRemoveProductGroup);
				jButtonRemoveContainerCode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (textField4j_Container_Code.getText().equals("")==false)
						{
							textField4j_Container_Code.setText("");
							matCustData.setMaterial(lmaterial);
							matCustData.setCustomerID("SELF");
							matCustData.setDataID("CONTAINER_CODE");;
							matCustData.delete();
						}
					}
				});
				
				jButtonRemoveContainerCode.setBounds(302, 542, 21, 21);
				jDesktopPane1.add(jButtonRemoveContainerCode);

				
				jButtonSave.setEnabled(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setButtonStates()
	{
		jButtonSave.setEnabled(true);
		if (checkBoxOverridePackLabel.isSelected())
		{
			comboBoxPackModuleID.setEnabled(true);
		}
		else
		{
			comboBoxPackModuleID.setEnabled(false);
		}

		if (checkBoxOverridePalletLabel.isSelected())
		{
			comboBoxPalletModuleID.setEnabled(true);
		}
		else
		{
			comboBoxPalletModuleID.setEnabled(false);
		}
	}

	private void save()
	{
		boolean result = true;

		material.setDescription(jTextFieldDescription.getText());
		material.setMaterialType(((JDBMaterialType) jComboBoxMaterialType.getSelectedItem()).getType());
		material.setBaseUom(((JDBUom) jComboBoxBaseUOM.getSelectedItem()).getInternalUom());

		material.setShelfLife((Integer) jSpinnerShelfLife.getValue());
		material.setShelfLifeUom(((JShelfLifeUom) jComboBoxShelfLifeUOM.getSelectedItem()).getUom());
		material.setShelfLifeRule(((JShelfLifeRoundingRule) jComboBoxRoundingRule.getSelectedItem()).getRule());

		material.setDefaultBatchStatus((String) jComboBoxDefaultBatchStatus.getSelectedItem());

		BigDecimal bd = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
		bd = BigDecimal.valueOf(grossweightnumbermodel.getNumber().doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP);
		material.setGrossWeight(bd);
		bd = BigDecimal.valueOf(netweightnumbermodel.getNumber().doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP);
		material.setNetWeight(bd);

		try
		{
			material.setWeightUom(((JDBUom) jComboBoxWeightUOM.getSelectedItem()).getInternalUom());
		}
		catch (Exception e)
		{
			material.setWeightUom("");
		}

		material.setOldMaterial(jTextFieldLegacyCode.getText());

		material.setEquipmentType(jTextFieldEquipmentType.getText());

		material.setInspectionID(jTextFieldInspectionID.getText());

		if (checkBoxValidateScanPallet.isSelected())
		{

			material.setValidateScanPallet("Y");
		}
		else
		{
			material.setValidateScanPallet("N");

		}

		if (checkBoxValidateScanCase.isSelected())
		{

			material.setValidateScanCase("Y");
		}
		else
		{
			material.setValidateScanCase("N");

		}

		if (checkBoxValidateScanEach.isSelected())
		{

			material.setValidateScanEach("Y");
		}
		else
		{
			material.setValidateScanEach("N");

		}

		if (checkBox4j_Enabled.isSelected())
		{
			material.setEnabled("Y");
		}
		else
		{
			material.setEnabled("N");
		}

		if (checkBoxOverridePackLabel.isSelected())
		{
			if (comboBoxPackModuleID.getSelectedIndex() >= 0)
			{
				if (comboBoxPackModuleID.getSelectedItem().toString().equals(""))
				{
					material.setOverridePackLabel("N");
					checkBoxOverridePackLabel.setSelected(false);
					material.setPackLabelModuleID("");
				}
				else
				{
					material.setOverridePackLabel("Y");
					checkBoxOverridePackLabel.setSelected(true);
					material.setPackLabelModuleID(comboBoxPackModuleID.getSelectedItem().toString());
				}
			}
			else
			{
				material.setOverridePackLabel("N");
				checkBoxOverridePackLabel.setSelected(false);
				material.setPackLabelModuleID("");
			}

		}
		else
		{
			material.setOverridePackLabel("N");
			material.setPackLabelModuleID("");
		}

		if (checkBox4jMoveAfterMake.isSelected())
		{
			if (textField4jMoveAfterMake.getText().toUpperCase().equals(""))
			{
				material.setMoveAfterMakeEnabled("N");
				checkBox4jMoveAfterMake.setSelected(false);
			}
			else
			{
				material.setMoveAfterMakeEnabled("Y");
				checkBox4jMoveAfterMake.setSelected(true);
			}
			material.setMoveAfterMakeLocationID(textField4jMoveAfterMake.getText().toUpperCase());
		}
		else
		{
			material.setMoveAfterMakeEnabled("Y");
			material.setMoveAfterMakeLocationID("");
			textField4jMoveAfterMake.setText("");
		}

		if (checkBoxOverridePalletLabel.isSelected())
		{
			if (comboBoxPalletModuleID.getSelectedIndex() >= 0)
			{
				if (comboBoxPalletModuleID.getSelectedItem().toString().equals(""))
				{
					material.setOverridePalletLabel("N");
					checkBoxOverridePalletLabel.setSelected(false);
					material.setPalletLabelModuleID("");
				}
				else
				{
					material.setOverridePalletLabel("Y");
					checkBoxOverridePalletLabel.setSelected(true);
					material.setPalletLabelModuleID(comboBoxPalletModuleID.getSelectedItem().toString());
				}
			}
			else
			{
				material.setOverridePalletLabel("N");
				checkBoxOverridePalletLabel.setSelected(false);
				material.setPalletLabelModuleID("");
			}
		}
		else
		{
			material.setOverridePalletLabel("N");
			material.setPalletLabelModuleID("");
		}

		if (material.isValidMaterial() == false)
		{
			result = material.create();
			if (result == true)
			{
				result = material.update();
			}
		}
		else
		{
			result = material.update();
		}
		if (result == false)
		{
			JOptionPane.showMessageDialog(Common.mainForm, material.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
		}
		else
		{
			jButtonSave.setEnabled(false);
		}

	}
}
