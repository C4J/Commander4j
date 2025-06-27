package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameProcessOrderProperties.java
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
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
 * The JInternalFrameProcessOrderProperties is used editing a record in the APP_PROCESS_ORDER
 * table. 
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameProcessOrderProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBProcessOrder JDBProcesOrder 
 * @see com.commander4j.app.JInternalFrameProcessOrderAdmin JInternalFrameProcessOrderAdmin
 * @see com.commander4j.app.JInternalFrameProcessOrderLabel JInternalFrameProcessOrderLabel
 */
public class JInternalFrameProcessOrderProperties extends JInternalFrame {
	private JCalendarButton calendarButtonDueDate;
	private JTextField4j jTextFieldMaterialDescription;
	private JLabel4j_std jLabelMaterialDesc;
	private JLabel4j_std jLabelQuantity;
	private JQuantityInput jFormattedTextFieldRequiredQuantity;
	private JLabel4j_std jLabelUOM;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelProcessOrder;
	private JButton4j jButtonCustomerLookup;
	private JButton4j jButtonLocationLookup;
	private JButton4j jButtonResourceLookup;
	private JButton4j jButtonMaterialLookup;
	private JButton4j jButtonBOMLookup;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JComboBox4j<String> jComboBoxStatus;
	private JComboBox4j<String> jComboBoxPalletStatus = new JComboBox4j<String>();
	private JLabel4j_std jLabelBatchStatus;
	private JLabel4j_std jLabelRecipeVersion;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldCustomer;
	private JLabel4j_std jLabelLocation;
	private JDateControl jSpinnerDueDate;
	private JLabel4j_std jLabelDueDate;
	private JTextField4j jTextFieldRecipeID;
	private JTextField4j jTextFieldRecipeVersion;
	private JTextField4j jTextFieldRequiredResource;
	private JLabel4j_std jLabel1Recipe;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabelMaterial;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std lblInspectionID;
	private JTextField4j jTextFieldInspectionID;
	private String lprocessorder;
	private JDBProcessOrder processorder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private Vector<JDBUom> uomList = new Vector<JDBUom>();
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBUom paluom = new JDBUom(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<JDBUom> jComboBoxRequiredUOM = new JComboBox4j<JDBUom>();
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameProcessOrderProperties(String po)
	{
		super();

		initGUI();
		setProcessOrderNo(po);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PROCESS_ORDER_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	public void setProcessOrderNo(String po)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Process Order [" + lprocessorder + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		lprocessorder = po;
		jTextFieldProcessOrder.setText(po);
		this.setTitle("Process Order [" + po + "]");
		processorder.setProcessOrder(lprocessorder);

		if (processorder.getProcessOrderProperties() == true)
		{
			jTextFieldMaterial.setText(processorder.getMaterial());
			jTextFieldDescription.setText(processorder.getDescription());
			jTextFieldRecipeID.setText(processorder.getRecipe());
			jTextFieldRecipeVersion.setText(processorder.getRecipeVersion());
			jTextFieldLocation.setText(processorder.getLocation());
			jTextFieldCustomer.setText(processorder.getCustomerID());
			jTextFieldInspectionID.setText(processorder.getInspectionID());
			jTextFieldRequiredResource.setText(processorder.getRequiredResource());
			jFormattedTextFieldRequiredQuantity.setValue(processorder.getRequiredQuantity());
			try
			{
				jSpinnerDueDate.setDate(processorder.getDueDate());
			} catch (Exception e)
			{

			}
			jComboBoxStatus.setSelectedItem(processorder.getStatus());
			jComboBoxPalletStatus.setSelectedItem(processorder.getDefaultPalletStatus());
		} else
		{

			jComboBoxPalletStatus.setSelectedItem("Ready");
			jTextFieldLocation.setText(ctrl.getKeyValue("DEFAULT_LOCATION"));
			try
			{
				jSpinnerDueDate.setDate(new Date());
			} catch (Exception e)
			{

			}
		}

		materialChanged();

		jButtonSave.setEnabled(false);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});
	}

	private void materialChanged()
	{

		if (material.getMaterialProperties(jTextFieldMaterial.getText()) == true)
		{
			jTextFieldMaterialDescription.setText(material.getDescription());
			getMaterialUoms(jTextFieldMaterial.getText());

		} else
		{
			jTextFieldMaterialDescription.setText("");
			processorder.setRequiredUom("");
			processorder.setMaterial("");
			getMaterialUoms("");
		}
	}

	public void getMaterialUoms(String lmaterial)
	{
		uomList.clear();
		materialuom.setMaterial(lmaterial);
		uomList.addAll(materialuom.getMaterialUoms());
		ComboBoxModel<JDBUom> jComboBoxBaseUOMModel = new DefaultComboBoxModel<JDBUom>(uomList);
		paluom.getInternalUomProperties(processorder.getRequiredUom());
		jComboBoxBaseUOMModel.setSelectedItem(paluom);
		jComboBoxRequiredUOM.setModel(jComboBoxBaseUOMModel);

	}

	private void initGUI()
	{
		try
		{

			{

				this.setPreferredSize(new java.awt.Dimension(448, 289));
				this.setBounds(25, 25, 491, 531);
				setVisible(true);
				this.setClosable(true);
				this.setIconifiable(true);

				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);

				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setBounds(62, 450, 112, 32);
					jButtonSave.setEnabled(false);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							save();
						}
					});
				}

				{
					jLabelProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabelProcessOrder);
					jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelProcessOrder.setBounds(12, 12, 144, 22);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setEditable(false);
					jTextFieldProcessOrder.setEnabled(false);
					jTextFieldProcessOrder.setBounds(163, 12, 130, 22);
				}
				{
					jLabelMaterial = new JLabel4j_std();
					jDesktopPane1.add(jLabelMaterial);
					jLabelMaterial.setText(lang.get("lbl_Material"));
					jLabelMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelMaterial.setBounds(12, 75, 144, 22);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setEnabled(true);
					jTextFieldMaterial.setBounds(163, 75, 130, 22);
					jTextFieldMaterial.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldMaterial.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(KeyEvent evt)
						{
							materialChanged();
						}
					});
				}
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelDescription.setBounds(12, 45, 144, 22);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBProcessOrder.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(163, 45, 301, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabel1Recipe = new JLabel4j_std();
					jDesktopPane1.add(jLabel1Recipe);
					jLabel1Recipe.setText(lang.get("lbl_Process_Order_Recipe"));
					jLabel1Recipe.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1Recipe.setBounds(12, 136, 144, 22);
				}
				{
					jLabelRecipeVersion  = new JLabel4j_std();
					jDesktopPane1.add(jLabelRecipeVersion);
					jLabelRecipeVersion.setText("/");
					jLabelRecipeVersion.setHorizontalAlignment(SwingConstants.CENTER);
					jLabelRecipeVersion.setBounds(295, 136, 10, 21);
				}
				{
					lblInspectionID = new JLabel4j_std();
					lblInspectionID.setText(lang.get("lbl_Inspection_ID"));
					lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblInspectionID.setBounds(12, 417, 144, 22);
					jDesktopPane1.add(lblInspectionID);
				}
				{
					jTextFieldRecipeID = new JTextField4j(JDBProcessOrder.field_recipe_id);
					jDesktopPane1.add(jTextFieldRecipeID);
					jTextFieldRecipeID.setBounds(163, 136, 130, 22);
					jTextFieldRecipeID.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jTextFieldRecipeVersion = new JTextField4j(JDBProcessOrder.field_recipe_version);
					jDesktopPane1.add(jTextFieldRecipeVersion);
					jTextFieldRecipeVersion.setBounds(308, 136, 66, 22);
					jTextFieldRecipeVersion.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jTextFieldRequiredResource = new JTextField4j(JDBProcessOrder.field_required_resource);
					jDesktopPane1.add(jTextFieldRequiredResource);
					jTextFieldRequiredResource.setBounds(163, 353, 130, 22);
					jTextFieldRequiredResource.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jTextFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
					jTextFieldInspectionID.setBounds(163, 417, 130, 22);
					jTextFieldInspectionID.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jTextFieldInspectionID);
				}
				JButton4j btnInspectionIDLookup = new JButton4j();
				btnInspectionIDLookup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.qmInspections())
						{
							JLaunchLookup.dlgCriteriaDefault = "";
							jTextFieldInspectionID.setText(JLaunchLookup.dlgResult);
						}
					}
				});
				btnInspectionIDLookup.setIcon(Common.icon_lookup_16x16);
				btnInspectionIDLookup.setBounds(292, 417, 21, 22);
				jDesktopPane1.add(btnInspectionIDLookup);
				{
					jLabelDueDate = new JLabel4j_std();
					jDesktopPane1.add(jLabelDueDate);
					jLabelDueDate.setText(lang.get("lbl_Process_Order_Due_Date"));
					jLabelDueDate.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelDueDate.setBounds(12, 225, 144, 22);
				}
				{
					jSpinnerDueDate = new JDateControl();
					jSpinnerDueDate.setDisplayMode(JDateControl.mode_disable_visible);
					jDesktopPane1.add(jSpinnerDueDate);
					jSpinnerDueDate.setBounds(163, 225, 128, 22);
					jSpinnerDueDate.getEditor().setPreferredSize(new java.awt.Dimension(86, 32));
					jSpinnerDueDate.getEditor().addKeyListener(new KeyAdapter()
					{
						public void keyPressed(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jSpinnerDueDate.addChangeListener(new ChangeListener()
					{
						public void stateChanged(final ChangeEvent e)

						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					calendarButtonDueDate = new JCalendarButton(jSpinnerDueDate);
					calendarButtonDueDate.setBounds(292, 225, 21, 21);
					jDesktopPane1.add(calendarButtonDueDate);
				}
				{
					jLabelLocation = new JLabel4j_std();
					jDesktopPane1.add(jLabelLocation);
					jLabelLocation.setText(lang.get("lbl_Location_ID"));
					jLabelLocation.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelLocation.setBounds(12, 258, 144, 22);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setBounds(163, 258, 130, 22);
					jTextFieldLocation.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jTextFieldCustomer = new JTextField4j(JDBCustomer.field_customer_id);
					jDesktopPane1.add(jTextFieldCustomer);
					jTextFieldCustomer.setBounds(163, 386, 130, 22);
					jTextFieldCustomer.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonCustomerLookup = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonCustomerLookup);
					jButtonCustomerLookup.setBounds(292, 386, 21, 22);
					jButtonCustomerLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.customers())
							{
								jTextFieldCustomer.setText(JLaunchLookup.dlgResult);
								jButtonSave.setEnabled(true);
							}
						}
					});
				}
				{
					jComboBoxRequiredUOM.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxRequiredUOM.setBounds(163, 195, 301, 22);
					jDesktopPane1.add(jComboBoxRequiredUOM);
				}
				{
					jLabelBatchStatus = new JLabel4j_std();
					jDesktopPane1.add(jLabelBatchStatus);
					jLabelBatchStatus.setText(lang.get("lbl_Process_Order_Status"));
					jLabelBatchStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelBatchStatus.setBounds(12, 289, 144, 22);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.processOrderStatus);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(163, 289, 150, 22);
					jComboBoxStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}

				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(181, 450, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(300, 450, 112, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jButtonMaterialLookup = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonMaterialLookup);
					jButtonMaterialLookup.setBounds(292, 75, 21, 22);
					jButtonMaterialLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
								materialChanged();
								jButtonSave.setEnabled(true);
							}
						}
					});
				}
				{
					jButtonBOMLookup = new JButton4j(Common.icon_bom_16x16);
					jButtonBOMLookup.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_BOM_ADMIN"));
					jDesktopPane1.add(jButtonBOMLookup);
					jButtonBOMLookup.setBounds(374, 136, 21, 22);
					jButtonBOMLookup.setToolTipText(lang.get("mod_MENU_BOM"));
					jButtonBOMLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchMenu.runForm("FRM_BOM_ADMIN", jTextFieldRecipeID.getText(), jTextFieldRecipeVersion.getText());
						}
					});
				}
				
				{
					jButtonLocationLookup = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLocationLookup);
					jButtonLocationLookup.setBounds(292, 258, 21, 22);
					jButtonLocationLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.locations())
							{
								jTextFieldLocation.setText(JLaunchLookup.dlgResult);
								jButtonSave.setEnabled(true);
							}
						}
					});
				}
				
				{
					jButtonResourceLookup = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonResourceLookup);
					jButtonResourceLookup.setBounds(292, 353, 21, 22);
					jButtonResourceLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.resources())
							{
								jTextFieldRequiredResource.setText(JLaunchLookup.dlgResult);
								jButtonSave.setEnabled(true);
							}
						}
					});
				}

				{
					jLabelUOM = new JLabel4j_std();
					jLabelUOM.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelUOM.setText(lang.get("lbl_Process_Order_Required_UOM"));
					jLabelUOM.setBounds(12, 195, 144, 22);
					jDesktopPane1.add(jLabelUOM);
				}

				{
					jFormattedTextFieldRequiredQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldRequiredQuantity.setBounds(163, 165, 91, 22);
					jFormattedTextFieldRequiredQuantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jFormattedTextFieldRequiredQuantity);
				}

				{
					jLabelQuantity = new JLabel4j_std();
					jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelQuantity.setText(lang.get("lbl_Process_Order_Required_Quantity"));
					jLabelQuantity.setBounds(12, 165, 144, 22);
					jDesktopPane1.add(jLabelQuantity);
				}

				{
					jLabelMaterialDesc = new JLabel4j_std();
					jLabelMaterialDesc.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelMaterialDesc.setText(lang.get("lbl_Description"));
					jLabelMaterialDesc.setBounds(12, 105, 144, 22);
					jDesktopPane1.add(jLabelMaterialDesc);
				}

				{
					jTextFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
					jTextFieldMaterialDescription.setBounds(163, 105, 301, 22);
					jTextFieldMaterialDescription.setEnabled(false);
					jDesktopPane1.add(jTextFieldMaterialDescription);
				}

				{
					ComboBoxModel<String> jComboBoxBatchStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxPalletStatus.setModel(jComboBoxBatchStatusModel);
					jComboBoxPalletStatus.setBounds(163, 322, 150, 22);
					jDesktopPane1.add(jComboBoxPalletStatus);
				}

				{
					ComboBoxModel<String> jComboBoxBatchStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
					jComboBoxPalletStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});

					jComboBoxPalletStatus.setModel(jComboBoxBatchStatusModel);
					// jComboBoxPalletStatus.setBounds(163, 320, 150, 21);
					// jDesktopPane1.add(jComboBoxPalletStatus);
				}
				{
					JLabel4j_std lblBatchStatus = new JLabel4j_std();
					lblBatchStatus.setText(lang.get("lbl_Process_Order_Default_Pallet_Status"));
					lblBatchStatus.setHorizontalAlignment(SwingConstants.TRAILING);
					lblBatchStatus.setBounds(12, 320, 144, 21);
					jDesktopPane1.add(lblBatchStatus);
				}

				{
					JLabel4j_std lblReqdResource = new JLabel4j_std();
					lblReqdResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
					lblReqdResource.setHorizontalAlignment(SwingConstants.TRAILING);
					lblReqdResource.setBounds(12, 353, 144, 21);
					jDesktopPane1.add(lblReqdResource);
				}

				{
					JLabel4j_std lblCustomerID = new JLabel4j_std();
					lblCustomerID.setText(lang.get("lbl_Customer_ID"));
					lblCustomerID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblCustomerID.setBounds(12, 386, 144, 21);
					jDesktopPane1.add(lblCustomerID);
				}

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						jTextFieldDescription.requestFocus();
						jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

					}
				});

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		processorder.setDescription(jTextFieldDescription.getText());
		processorder.setRecipe(jTextFieldRecipeID.getText());
		processorder.setRecipeVersion(jTextFieldRecipeVersion.getText());
		processorder.setLocation(jTextFieldLocation.getText());
		processorder.setCustomerID(jTextFieldCustomer.getText());
		processorder.setInspectionID(jTextFieldInspectionID.getText());
		processorder.setMaterial(jTextFieldMaterial.getText());
		processorder.setRequiredQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldRequiredQuantity.getText().toString()));
		processorder.setRequiredUom(((JDBUom) jComboBoxRequiredUOM.getSelectedItem()).getInternalUom());
		processorder.setRequiredResource(jTextFieldRequiredResource.getText());
		try
		{
			processorder.setStatus((String) jComboBoxStatus.getSelectedItem());
		} catch (Exception e)
		{
			processorder.setStatus("");
		}

		try
		{
			processorder.setDefaultPalletStatus((String) jComboBoxPalletStatus.getSelectedItem());
		} catch (Exception e)
		{
			processorder.setDefaultPalletStatus("");
		}

		Date d = jSpinnerDueDate.getDate();
		processorder.setDueDate(JUtility.getTimestampFromDate(d));
		if (processorder.isValidProcessOrder() == false)
		{
			processorder.create();
		}
		if (processorder.update())
		{
			jTextFieldDescription.setText(processorder.getDescription());
			jButtonSave.setEnabled(false);
		} else
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, processorder.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
		}

		jComboBoxPalletStatus.setSelectedItem(processorder.getDefaultPalletStatus());

	}

}
