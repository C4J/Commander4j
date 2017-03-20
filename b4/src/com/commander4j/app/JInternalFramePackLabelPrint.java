package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePackLabelPrint.java
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JPrint;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

public class JInternalFramePackLabelPrint extends JInternalFrame {
	private static final long serialVersionUID = 1;
	private JLabel4j_std jLabelPrintLabel_2;
	private JSpinner jSpinnerQuantity;
	private JLabel4j_std jLabelQuantity_1;
	private JCheckBox4j jCheckBoxAutoPreview;
	private JLabel4j_std jLabelPrintLabel_1;
	private JButton4j jButtonHelp;
	private JDateControl jSpinnerDueDate;
	private JLabel4j_std jLabelDueDate;
	private JTextField4j jTextFieldRecipe;
	private JLabel4j_std jLabelRecipe;
	private JTextField4j jTextFieldShelfLifeRoundingRule;
	private JLabel4j_std jLabelRounding;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JLabel4j_std jLabelOrderDescription;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabelMaterial;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabelProcessOrder;
	private JTextField4j jTextFieldMaterialDescription;
	private JLabel4j_std jLabelDescription;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldRequiredUom;
	private JTextField4j jTextFieldBaseUom;
	private JCheckBox4j jCheckBoxBatchPrefixOverride;
	private JCheckBox4j jCheckBoxExpiryOverride;
	private JButton4j jButtonPOLookup;
	private JQuantityInput jFormattedTextFieldRequiredUOMQuantity;
	private JQuantityInput jFormattedTextFieldBaseUOMQuantity;
	private JLabel4j_std jLabelProductionDate;
	private JDateControl jSpinnerProductionDate;
	private JPanel jPanelLabel;
	private JTextField4j jTextFieldBaseVariant;
	private JTextField4j jTextFieldRequiredVariant;
	private JTextField4j jTextFieldRequiredEAN;
	private JTextField4j jTextFieldBaseEAN;
	private JLabel4j_std jLabelEAN;
	private JTextField4j jTextFieldLegacyCode;
	private JLabel4j_std jLabelLegacyCode;
	private JTextField4j jTextFieldShelfLifeUOM;
	private JLabel4j_std jLabel1ShelfLifeUOM;
	private JCheckBox4j jCheckBoxDOMOverride;
	private JLabel4j_std jStatusText;
	private JLabel4j_std jLabelShelfLife;
	private JTextField4j jTextFieldProcessOrderStatus;
	private JPanel jPanelMaterial;
	private JPanel jPanelProcessOrder;
	private JLabel4j_std jLabelOrderStatus;
	private JDateControl jSpinnerExpiryDate;
	private JLabel4j_std jLabelBatchExpiry;
	private JTextField4j jTextFieldBatchPrefix;
	private JLabel4j_std jLabelBatch;
	private JSpinner jSpinnerShelfLife;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabelLocation;
	private JButton4j jButtonPrint;
	private JDesktopPane jDesktopPane1;
	private SpinnerNumberModel shelflifenumbermodel = new SpinnerNumberModel();
	private SpinnerNumberModel quantitynumbermodel = new SpinnerNumberModel();
	private JDBProcessOrder processorder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource processorderResource = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);	
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialbatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JShelfLifeUom shelflifeuom = new JShelfLifeUom();
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JShelfLifeRoundingRule shelfliferoundingrule = new JShelfLifeRoundingRule();
	final Logger logger = Logger.getLogger(JInternalFramePackLabelPrint.class);
	private ClockListener clocklistener = new ClockListener();
	private Timer timer = new Timer(1000, clocklistener);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBoxPrintQueue;
	private JLabel4j_std lblPrintQueueFor;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private String batchFormat = "";
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private String expiryMode = "";
	private JCheckBox4j checkBoxIncHeaderText;
	private JTextField4j jTextFieldBatchSuffix;
	private JCalendarButton calendarButtonjSpinnerProductionDate;
	private JCalendarButton calendarButtonjSpinnerExpiryDate;
	private BigDecimal caseDefaultQuantity;
	private JLabelPrint labelPrint = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;
	private JButton4j jButtonAssign;

	public JInternalFramePackLabelPrint()
	{
		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				timer.stop();

				while (timer.isRunning())
				{
				}

				timer = null;
			}
		});

		initGUI();
		clearFields();

		timer.start();

		ctrl.getProperties("BATCH FORMAT");
		batchFormat = ctrl.getKeyValue();
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");

		calcBBEBatch();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_LABEL_PRINT"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldProcessOrder.requestFocus();
				jTextFieldProcessOrder.setCaretPosition(jTextFieldProcessOrder.getText().length());

			}
		});

		populatePrinterList(JPrint.getDefaultPrinterQueueName());
		processOrderChanged("");
	}

	private void populatePrinterList(String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempPrinterList = JPrint.getPrinterNames();

		for (int j = 0; j < tempPrinterList.size(); j++)
		{
			defComboBoxMod.addElement(tempPrinterList.get(j));
		}

		int sel = defComboBoxMod.getIndexOf(defaultitem);
		ComboBoxModel<String> jList1Model = defComboBoxMod;
		comboBoxPrintQueue.setModel(jList1Model);
		comboBoxPrintQueue.setSelectedIndex(sel);

		if (JPrint.getNumberofPrinters() == 0)
		{
			comboBoxPrintQueue.setEnabled(false);
		} else
		{
			comboBoxPrintQueue.setEnabled(true);
		}
	}

	private void processOrderChanged(String po)
	{
		boolean valid = false;
		clearFields();

		jTextFieldProcessOrder.setText(po);
		processorder.setProcessOrder(po);

		mod.setModuleId(labelPrint.getPackLabelReportName(po));
		mod.getModuleProperties();

		if (mod.getReportType().equals("Label"))
		{
			jCheckBoxAutoPreview.setSelected(false);
			jCheckBoxAutoPreview.setEnabled(false);

		} else
		{
			jCheckBoxAutoPreview.setSelected(true);
			jCheckBoxAutoPreview.setEnabled(true);
		}

		if (processorder.getProcessOrderProperties() == true)
		{
			pallet.setProcessOrder(po);
			pallet.populateFromProcessOrder();

			jTextFieldProcessOrderDescription.setText(processorder.getDescription());
			jTextFieldMaterial.setText(processorder.getMaterial());

			jTextFieldProcessOrderStatus.setText(processorder.getStatus());
			jTextFieldRecipe.setText(processorder.getRecipe());
			jTextFieldLocation.setText(processorder.getLocation());

			try
			{
				jSpinnerDueDate.setDate(processorder.getDueDate());
			} catch (Exception e)
			{
			}

			material.getMaterialProperties(processorder.getMaterial());
			jTextFieldMaterialDescription.setText(material.getDescription());
			jSpinnerShelfLife.setValue((Number) material.getShelfLife());
			shelflifeuom.getShelfLifeUomProperties(material.getShelfLifeUom());
			jTextFieldShelfLifeUOM.setText(shelflifeuom.toString());
			shelfliferoundingrule.getShelfLifeRuleProperties(material.getShelfLifeRule());
			jTextFieldShelfLifeRoundingRule.setText(shelfliferoundingrule.toString());
			jFormattedTextFieldRequiredUOMQuantity.setValue(JUtility.stringToBigDecimal(processorder.getRequiredUOMQuantity()));
			caseDefaultQuantity = JUtility.stringToBigDecimal(processorder.getRequiredUOMQuantity());
			jTextFieldLegacyCode.setText(material.getOldMaterial());
			jTextFieldRequiredUom.setText(processorder.getRequiredUom());
			materialuom.getMaterialUomProperties(material.getMaterial(), processorder.getRequiredUom());
			jTextFieldRequiredEAN.setText(materialuom.getEan());
			jTextFieldRequiredVariant.setText(materialuom.getVariant());

			pallet.setBatchNumber(jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText());
			pallet.setQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldRequiredUOMQuantity.getText().toString()));

			materialuom.getMaterialUomProperties(material.getMaterial(), material.getBaseUom());
			jFormattedTextFieldBaseUOMQuantity.setValue(materialuom.getNumerator());
			jTextFieldBaseUom.setText(material.getBaseUom());
			jTextFieldBaseEAN.setText(materialuom.getEan());
			jTextFieldBaseVariant.setText(materialuom.getVariant());

			valid = true;

			jTextFieldBatchSuffix.setText(processorderResource.getBatchSuffixForResource(processorder.getRequiredResource()));
			calcBBEBatch();

		} else
		{
			valid = false;
		}

		if (valid)
		{

			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_PRINTER"));
			jButtonAssign.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_AUTOLAB"));
			jFormattedTextFieldRequiredUOMQuantity.setEnabled(true);

			jCheckBoxDOMOverride.setEnabled(true);
			jCheckBoxExpiryOverride.setEnabled(true);
			jCheckBoxBatchPrefixOverride.setEnabled(true);
			enableField(jSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());
			enableField(jSpinnerExpiryDate, jCheckBoxExpiryOverride.isSelected());
			enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());
			jTextFieldBatchSuffix.setEnabled(true);
		} else
		{

			jButtonPrint.setEnabled(false);
			jButtonAssign.setEnabled(false);
			jFormattedTextFieldRequiredUOMQuantity.setEnabled(false);

			jSpinnerProductionDate.setEnabled(false);
			jCheckBoxDOMOverride.setSelected(false);
			jCheckBoxDOMOverride.setEnabled(false);

			jSpinnerExpiryDate.setEnabled(false);
			jCheckBoxExpiryOverride.setSelected(false);
			jCheckBoxExpiryOverride.setEnabled(false);

			jTextFieldBatchPrefix.setEnabled(false);
			jTextFieldBatchSuffix.setEnabled(false);
			jCheckBoxBatchPrefixOverride.setSelected(false);
			jCheckBoxBatchPrefixOverride.setEnabled(false);
		}
		checkFieldColours();
	}

	private void enableField(JComponent field, Boolean allowed)
	{
		if (allowed == true)
		{
			field.setEnabled(true);
		} else
		{
			field.setEnabled(false);
		}
	}

	private void clearFields()
	{
		// previousDateString = "";
		jTextFieldProcessOrderDescription.setText("");
		jTextFieldProcessOrderStatus.setText("");
		jTextFieldRecipe.setText("");
		jTextFieldLocation.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldMaterialDescription.setText("");
		jTextFieldShelfLifeUOM.setText("");
		jTextFieldShelfLifeRoundingRule.setText("");
		jTextFieldLegacyCode.setText("");
		jTextFieldBatchPrefix.setText("");
		jTextFieldBatchSuffix.setText("");
		jTextFieldRequiredUom.setText("");
		jTextFieldRequiredEAN.setText("");
		jTextFieldRequiredVariant.setText("");
		jTextFieldBaseEAN.setText("");
		jTextFieldBaseVariant.setText("");

		jCheckBoxDOMOverride.setSelected(false);
		enableField(jSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());

		jCheckBoxExpiryOverride.setSelected(false);
		enableField(jSpinnerExpiryDate, jCheckBoxExpiryOverride.isSelected());

		jCheckBoxBatchPrefixOverride.setSelected(false);
		enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());

		jFormattedTextFieldRequiredUOMQuantity.setValue(0);
		jFormattedTextFieldBaseUOMQuantity.setValue(0);
	}

	private void calcBBEBatch()
	{
		try
		{
			String temp = "";
			Date d = jSpinnerProductionDate.getDate();
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(d);

			JDBMaterialBatch mb = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
			temp = mb.getDefaultBatchNumber(batchFormat, caldate, processorder);

			if (temp.equalsIgnoreCase(jTextFieldBatchPrefix.getText()) == false)
			{
				if (jCheckBoxBatchPrefixOverride.isSelected() == false)
				{
					jTextFieldBatchPrefix.setText(mb.getDefaultBatchNumber(batchFormat, caldate, processorder));
				}
			}

			if (jCheckBoxExpiryOverride.isSelected() == false)
			{
				jSpinnerExpiryDate.setEnabled(false);

				if (material.getMaterial().length() > 0)
				{
					if (expiryMode.equals("BATCH"))
					{
						if (materialbatch.getMaterialBatchProperties(material.getMaterial(), jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText()) == true)
						{
							try
							{
								jSpinnerExpiryDate.setDate(materialbatch.getExpiryDate());
							} catch (Exception e)
							{
							}
						} else
						{
							Date de = jSpinnerProductionDate.getDate();

							try
							{
								jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
							} catch (Exception e)
							{
							}
						}
					} else
					{
						Date de = jSpinnerProductionDate.getDate();

						try
						{
							jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
						} catch (Exception e)
						{
						}
					}
				}
			}

			jSpinnerExpiryDate.setDate(material.getRoundedExpiryDate(jSpinnerExpiryDate.getDate()));
		} catch (Exception e)
		{

		}
	}

	private String createLabelData(int labels)
	{
		String result = "";

		JDBLabelData labelData = new JDBLabelData(Common.selectedHostID, Common.sessionID);

		labelData.generateUniqueID();
		labelData.setPrintDate(JUtility.getSQLDateTime());
		labelData.setUserID(Common.userList.getUser(Common.sessionID).getUserId());
		labelData.setWorkstationID(JUtility.getClientName());
		labelData.setMaterial(processorder.getMaterial());
		labelData.setMaterialType(material.getMaterialType());
		labelData.setBatchNumber(jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText());
		labelData.setProcessOrder(processorder.getProcessOrder());
		labelData.setRequiredResource(processorder.getRequiredResource());
		labelData.setLocationID(processorder.getLocation());
		labelData.setProdQuantity(jFormattedTextFieldRequiredUOMQuantity.getQuantity());
		labelData.setProdUom(jTextFieldRequiredUom.getText());
		labelData.setBaseQuantity(jFormattedTextFieldBaseUOMQuantity.getQuantity());
		labelData.setBaseUom(jTextFieldBaseUom.getText());
		labelData.setDateofManufacture(JUtility.getTimestampFromDate(jSpinnerProductionDate.getDate()));
		labelData.setExpiryDate(JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate()));
		labelData.setExpiryMode(expiryMode);
		labelData.setProdEAN(jTextFieldRequiredEAN.getText());
		labelData.setProdVariant(jTextFieldRequiredVariant.getText());
		labelData.setBaseEAN(jTextFieldBaseEAN.getText());
		labelData.setBaseVariant(jTextFieldBaseVariant.getText());
		labelData.setCustomer(processorder.getCustomerID());
		labelData.setPrintCopies((long) labels);
		labelData.setPrintQueue(comboBoxPrintQueue.getSelectedItem().toString());
		labelData.setModuleID(labelPrint.getPackLabelReportName(labelData.getProcessOrder()));
		labelData.setBatchPrefix(jTextFieldBatchPrefix.getText());
		labelData.setBatchSuffix(jTextFieldBatchSuffix.getText());
		labelData.setOverrideBatchPrefix(jCheckBoxBatchPrefixOverride.isSelected());
		labelData.setOverrideDateofManufacture(jCheckBoxDOMOverride.isSelected());
		labelData.setOverrideExpiryDate(jCheckBoxExpiryOverride.isSelected());
		labelData.setLabelType("Pack");

		labelData.create();
		result = labelData.getUniqueID();

		return result;
	}

	private void printOrAssign(String mode)
	{
		int noOfLabels = Integer.valueOf(jSpinnerQuantity.getValue().toString());

		Boolean confirmQuantity = true;
		BigDecimal a = jFormattedTextFieldRequiredUOMQuantity.getQuantity();

		if (caseDefaultQuantity.compareTo(new BigDecimal("0")) > 0)
		{
			if (a.compareTo(caseDefaultQuantity) > 0)
			{
				if (JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Quantity_Confirm"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm) == JOptionPane.YES_OPTION)
				{
					confirmQuantity = true;
				} else
				{
					confirmQuantity = false;
				}
			}
		}

		if (confirmQuantity == true)
		{

			String processOrder = processorder.getMaterial();
			String batchNumber = jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText();
			Timestamp expiryDate = JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate());

			if (materialbatch.autoCreateMaterialBatch(processOrder, batchNumber, expiryDate, ""))
			{

				String key = createLabelData(noOfLabels);

				if (mode.equals("Print"))
				{

					String pq = comboBoxPrintQueue.getSelectedItem().toString();
					buildSQL(key);

					JLaunchReport.runReport(labelPrint.getPackLabelReportName(processOrder), listStatement, jCheckBoxAutoPreview.isSelected(), pq, noOfLabels, checkBoxIncHeaderText.isSelected());

				}

				if (mode.equals("Assign"))
				{
					JLaunchMenu.runDialog("FRM_LABEL_DATA_ASSIGN", key);
				}

			} else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, materialbatch.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
			}
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(674, 474));
			this.setBounds(0, 0, 785, 586);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			jDesktopPane1 = new JDesktopPane();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(665, 490));
			jDesktopPane1.setBackground(Common.color_app_window);
			jDesktopPane1.setLayout(null);
			jButtonPrint = new JButton4j(Common.icon_print);
			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_PRINTER"));

			jButtonAssign = new JButton4j(Common.icon_auto_labeller);
			jButtonAssign.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					printOrAssign("Assign");
				}
			});
			jButtonAssign.setText(lang.get("btn_Assign_to_Labeller"));
			jButtonAssign.setMnemonic('0');
			jButtonAssign.setEnabled(false);
			jButtonAssign.setBounds(194, 471, 182, 32);
			jButtonAssign.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_AUTOLAB"));
			jDesktopPane1.add(jButtonAssign);

			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setEnabled(false);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(6, 471, 182, 32);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					printOrAssign("Print");
				}
			});

			jButtonClose = new JButton4j(Common.icon_close);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(570, 471, 182, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					timer.stop();

					while (timer.isRunning())
					{
					}

					timer = null;
					dispose();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(382, 471, 182, 32);
			jPanelProcessOrder = new JPanel();
			jPanelProcessOrder.setFont(Common.font_title);
			jDesktopPane1.add(jPanelProcessOrder);
			jPanelProcessOrder.setBounds(7, 7, 748, 112);
			jPanelProcessOrder.setBorder(BorderFactory.createTitledBorder(lang.get("lbl_Process_Order")));
			jPanelProcessOrder.setLayout(null);
			jPanelProcessOrder.setBackground(Common.color_app_window);
			jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
			jPanelProcessOrder.add(jTextFieldProcessOrder);;
			jTextFieldProcessOrder.setBounds(161, 21, 119, 21);
			jTextFieldProcessOrder.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					processOrderChanged(jTextFieldProcessOrder.getText());
				}
			});

			jLabelProcessOrder = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelProcessOrder);
			jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
			jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelProcessOrder.setBounds(12, 21, 142, 21);
			jTextFieldProcessOrderDescription = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderDescription);
			jTextFieldProcessOrderDescription.setBounds(449, 21, 287, 21);
			jTextFieldProcessOrderDescription.setEditable(false);
			jTextFieldProcessOrderDescription.setEnabled(false);
			jLabelOrderDescription = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelOrderDescription);
			jLabelOrderDescription.setText(lang.get("lbl_Description"));
			jLabelOrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelOrderDescription.setBounds(306, 21, 136, 21);
			jLabelOrderStatus = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelOrderStatus);
			jLabelOrderStatus.setText(lang.get("lbl_Process_Order_Status"));
			jLabelOrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelOrderStatus.setBounds(290, 49, 152, 21);
			jSpinnerDueDate = new JDateControl();
			jPanelProcessOrder.add(jSpinnerDueDate);
			jSpinnerDueDate.setEnabled(false);
			jSpinnerDueDate.setBounds(161, 49, 137, 21);
			jSpinnerDueDate.getEditor().setOpaque(true);
			jSpinnerDueDate.setForeground(new java.awt.Color(238, 238, 238));
			jLabelDueDate = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelDueDate);
			jLabelDueDate.setText(lang.get("lbl_Process_Order_Due_Date"));
			jLabelDueDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDueDate.setBounds(12, 49, 142, 21);
			jTextFieldProcessOrderStatus = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderStatus);
			jTextFieldProcessOrderStatus.setBounds(449, 49, 126, 21);
			jTextFieldProcessOrderStatus.setEditable(false);
			jTextFieldProcessOrderStatus.setEnabled(false);
			jTextFieldRecipe = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldRecipe);
			jTextFieldRecipe.setBounds(161, 77, 139, 21);
			jTextFieldRecipe.setEditable(false);
			jTextFieldRecipe.setEnabled(false);
			jLabelRecipe = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelRecipe);
			jLabelRecipe.setText(lang.get("lbl_Process_Order_Recipe"));
			jLabelRecipe.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelRecipe.setBounds(12, 77, 142, 21);
			jTextFieldLocation = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldLocation);
			jTextFieldLocation.setBounds(449, 77, 126, 21);
			jTextFieldLocation.setEditable(false);
			jTextFieldLocation.setEnabled(false);
			jLabelLocation = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelLocation);
			jLabelLocation.setText(lang.get("lbl_Location_ID"));
			jLabelLocation.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelLocation.setBounds(290, 77, 152, 21);
			jButtonPOLookup = new JButton4j(Common.icon_lookup);
			jPanelProcessOrder.add(jButtonPOLookup);
			jButtonPOLookup.setBounds(280, 21, 20, 20);
			jButtonPOLookup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgCriteriaDefault = "Ready";
					JLaunchLookup.dlgAutoExec = true;

					if (JLaunchLookup.processOrders())
					{
						String po = JLaunchLookup.dlgResult;
						processOrderChanged(po);
					}
				}
			});

			jPanelMaterial = new JPanel();
			jPanelMaterial.setBackground(Common.color_app_window);
			jDesktopPane1.add(jPanelMaterial);
			jPanelMaterial.setBounds(7, 119, 748, 112);
			jPanelMaterial.setBorder(BorderFactory.createTitledBorder(lang.get("lbl_Material")));
			jPanelMaterial.setLayout(null);
			jPanelMaterial.setFont(Common.font_title);
			jTextFieldMaterial = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterial);
			jTextFieldMaterial.setBounds(161, 21, 128, 21);
			jTextFieldMaterial.setEditable(false);
			jTextFieldMaterial.setEnabled(false);
			jLabelMaterial = new JLabel4j_std();
			jPanelMaterial.add(jLabelMaterial);
			jLabelMaterial.setText(lang.get("lbl_Material"));
			jLabelMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelMaterial.setBounds(12, 21, 142, 21);
			jTextFieldMaterialDescription = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterialDescription);
			jTextFieldMaterialDescription.setBounds(449, 21, 287, 21);
			jTextFieldMaterialDescription.setEditable(false);
			jTextFieldMaterialDescription.setEnabled(false);
			jLabelDescription = new JLabel4j_std();
			jPanelMaterial.add(jLabelDescription);
			jLabelDescription.setText(lang.get("lbl_Description"));
			jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDescription.setBounds(291, 21, 151, 21);
			jSpinnerShelfLife = new JSpinner();
			jPanelMaterial.add(jSpinnerShelfLife);
			jSpinnerShelfLife.setModel(shelflifenumbermodel);
			JSpinner.NumberEditor nec = new JSpinner.NumberEditor(jSpinnerShelfLife);
			nec.getTextField().setFont(Common.font_std);
			jSpinnerShelfLife.setEditor(nec);
			jSpinnerShelfLife.setBounds(161, 49, 63, 21);
			jSpinnerShelfLife.setEnabled(false);
			jTextFieldShelfLifeRoundingRule = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeRoundingRule);
			jTextFieldShelfLifeRoundingRule.setBounds(638, 49, 98, 21);
			jTextFieldShelfLifeRoundingRule.setEditable(false);
			jTextFieldShelfLifeRoundingRule.setEnabled(false);
			jLabelRounding = new JLabel4j_std();
			jPanelMaterial.add(jLabelRounding);
			jLabelRounding.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
			jLabelRounding.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelRounding.setBounds(546, 49, 85, 21);
			jTextFieldShelfLifeUOM = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeUOM);
			jTextFieldShelfLifeUOM.setBounds(449, 49, 91, 21);
			jTextFieldShelfLifeUOM.setEditable(false);
			jTextFieldShelfLifeUOM.setEnabled(false);
			jLabel1ShelfLifeUOM = new JLabel4j_std();
			jPanelMaterial.add(jLabel1ShelfLifeUOM);
			jLabel1ShelfLifeUOM.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
			jLabel1ShelfLifeUOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel1ShelfLifeUOM.setBounds(281, 49, 161, 21);
			jLabelShelfLife = new JLabel4j_std();
			jPanelMaterial.add(jLabelShelfLife);
			jLabelShelfLife.setText(lang.get("lbl_Material_Shelf_Life"));
			jLabelShelfLife.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelShelfLife.setBounds(12, 49, 142, 21);
			jTextFieldLegacyCode = new JTextField4j();
			jPanelMaterial.add(jTextFieldLegacyCode);
			jTextFieldLegacyCode.setBounds(161, 77, 128, 21);
			jTextFieldLegacyCode.setEditable(false);
			jTextFieldLegacyCode.setEnabled(false);
			jLabelLegacyCode = new JLabel4j_std();
			jPanelMaterial.add(jLabelLegacyCode);
			jLabelLegacyCode.setText(lang.get("lbl_Material_Legacy_Code"));
			jLabelLegacyCode.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelLegacyCode.setBounds(12, 77, 142, 21);
			jPanelLabel = new JPanel();
			jDesktopPane1.add(jPanelLabel);
			jPanelLabel.setBounds(7, 231, 748, 136);
			jPanelLabel.setBorder(BorderFactory.createTitledBorder(null, lang.get("btn_Label"), TitledBorder.LEADING, TitledBorder.TOP));
			jPanelLabel.setLayout(null);
			jPanelLabel.setFont(Common.font_std);
			jPanelLabel.setBackground(Common.color_app_window);
			comboBoxPrintQueue = new JComboBox4j<String>();
			comboBoxPrintQueue.setBounds(115, 436, 621, 23);
			jDesktopPane1.add(comboBoxPrintQueue);
			jSpinnerProductionDate = new JDateControl();
			jPanelLabel.add(jSpinnerProductionDate);
			jSpinnerProductionDate.setFont(new java.awt.Font("Dialog", 0, 12));
			jSpinnerProductionDate.setBounds(176, 21, 136, 21);
			jSpinnerProductionDate.getEditor().addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					calcBBEBatch();
				}
			});

			jSpinnerProductionDate.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)
				{
					calcBBEBatch();
				}
			});

			try
			{
				jSpinnerProductionDate.setDate(JUtility.getSQLDate());
				jSpinnerProductionDate.setEnabled(false);
			} catch (Exception e)
			{
			}

			jLabelProductionDate = new JLabel4j_std();
			jPanelLabel.add(jLabelProductionDate);
			jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
			jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelProductionDate.setBounds(46, 21, 108, 21);

			jFormattedTextFieldBaseUOMQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldBaseUOMQuantity.setEditable(false);
			jPanelLabel.add(jFormattedTextFieldBaseUOMQuantity);
			jFormattedTextFieldBaseUOMQuantity.setFont(Common.font_std);
			jFormattedTextFieldBaseUOMQuantity.setBounds(349, 85, 94, 21);
			jFormattedTextFieldBaseUOMQuantity.setValue(0);
			jFormattedTextFieldBaseUOMQuantity.setEnabled(false);
			jFormattedTextFieldBaseUOMQuantity.setHorizontalAlignment(SwingConstants.TRAILING);

			jFormattedTextFieldRequiredUOMQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldRequiredUOMQuantity.setEditable(false);
			jFormattedTextFieldRequiredUOMQuantity.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent arg0)
				{
					Color background = Color.WHITE;

					try
					{
						if (jTextFieldProcessOrder.getText().equals("") == false)
						{
							BigDecimal newval = new BigDecimal(jFormattedTextFieldRequiredUOMQuantity.getValue().toString());
							int res = newval.compareTo(new BigDecimal(0));
							if (res == 0)
							{
								background = Color.YELLOW;
							}
						}
					} catch (Exception e)
					{
						background = Color.YELLOW;
					}
					jFormattedTextFieldRequiredUOMQuantity.setBackground(background);
				}
			});

			jPanelLabel.add(jFormattedTextFieldRequiredUOMQuantity);
			jFormattedTextFieldRequiredUOMQuantity.setFont(Common.font_std);
			jFormattedTextFieldRequiredUOMQuantity.setBounds(349, 52, 94, 21);
			jFormattedTextFieldRequiredUOMQuantity.setValue(0);
			jFormattedTextFieldRequiredUOMQuantity.setEnabled(false);
			jFormattedTextFieldRequiredUOMQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jFormattedTextFieldRequiredUOMQuantity.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					pallet.setUom(jTextFieldRequiredUom.getText());
					pallet.setQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldRequiredUOMQuantity.getText().toString()));
				}
			});

			jTextFieldRequiredUom = new JTextField4j();
			jTextFieldRequiredUom.setHorizontalAlignment(SwingConstants.CENTER);
			jPanelLabel.add(jTextFieldRequiredUom);
			jTextFieldRequiredUom.setBounds(462, 85, 56, 21);
			jTextFieldRequiredUom.setEditable(false);
			jTextFieldRequiredUom.setEnabled(false);

			jTextFieldBaseUom = new JTextField4j();
			jTextFieldBaseUom.setHorizontalAlignment(SwingConstants.CENTER);
			jPanelLabel.add(jTextFieldBaseUom);
			jTextFieldBaseUom.setBounds(462, 52, 56, 21);
			jTextFieldBaseUom.setEditable(false);
			jTextFieldBaseUom.setEnabled(false);

			jCheckBoxDOMOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxDOMOverride);
			jCheckBoxDOMOverride.setBackground(new java.awt.Color(255, 255, 255));
			jCheckBoxDOMOverride.setBounds(153, 21, 21, 21);
			jCheckBoxDOMOverride.setEnabled(false);
			jCheckBoxDOMOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());
					enableField(calendarButtonjSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());
				}
			});

			calendarButtonjSpinnerProductionDate = new JCalendarButton(jSpinnerProductionDate);
			calendarButtonjSpinnerProductionDate.setEnabled(false);
			calendarButtonjSpinnerProductionDate.setBounds(308, 21, 21, 21);
			jPanelLabel.add(calendarButtonjSpinnerProductionDate);
			jLabelBatch = new JLabel4j_std();
			jPanelLabel.add(jLabelBatch);
			jLabelBatch.setText(lang.get("lbl_Material_Batch"));
			jLabelBatch.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelBatch.setBounds(46, 85, 108, 21);
			jTextFieldBatchPrefix = new JTextField4j(JDBMaterialBatch.field_batch_number);
			jPanelLabel.add(jTextFieldBatchPrefix);
			jTextFieldBatchPrefix.setBounds(176, 85, 108, 21);
			jTextFieldBatchPrefix.setEnabled(false);
			jSpinnerExpiryDate = new JDateControl();
			jSpinnerExpiryDate.addFocusListener(new FocusAdapter()
			{
				@Override
				public void focusLost(FocusEvent arg0)
				{
					jSpinnerExpiryDate.setDate(material.getRoundedExpiryDate(jSpinnerExpiryDate.getDate()));
				}
			});

			jPanelLabel.add(jSpinnerExpiryDate);
			jSpinnerExpiryDate.setFont(Common.font_std);
			jSpinnerExpiryDate.setBounds(176, 52, 135, 25);
			jSpinnerExpiryDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
			jSpinnerExpiryDate.getEditor().setSize(87, 21);
			jSpinnerExpiryDate.setEnabled(false);
			jLabelBatchExpiry = new JLabel4j_std();
			jPanelLabel.add(jLabelBatchExpiry);
			jLabelBatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			jLabelBatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelBatchExpiry.setBounds(46, 56, 108, 21);
			jCheckBoxExpiryOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxExpiryOverride);
			jCheckBoxExpiryOverride.setBounds(153, 53, 21, 21);
			jCheckBoxExpiryOverride.setBackground(new java.awt.Color(255, 255, 255));
			jCheckBoxExpiryOverride.setEnabled(false);
			jCheckBoxExpiryOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerExpiryDate, jCheckBoxExpiryOverride.isSelected());
					enableField(calendarButtonjSpinnerExpiryDate, jCheckBoxExpiryOverride.isSelected());
					calcBBEBatch();
				}
			});

			jCheckBoxBatchPrefixOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxBatchPrefixOverride);
			jCheckBoxBatchPrefixOverride.setEnabled(false);
			jCheckBoxBatchPrefixOverride.setBackground(new java.awt.Color(255, 255, 255));
			jCheckBoxBatchPrefixOverride.setBounds(153, 85, 21, 21);
			jCheckBoxBatchPrefixOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());
					calcBBEBatch();
				}
			});

			jStatusText = new JLabel4j_std();
			jDesktopPane1.add(jStatusText);
			jStatusText.setForeground(new java.awt.Color(255, 0, 0));
			jStatusText.setBounds(0, 513, 761, 21);
			jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

			final JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, lang.get("lbl_Options"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			panel.setBackground(Common.color_app_window);
			panel.setFont(Common.font_title);
			panel.setLayout(null);
			panel.setBounds(7, 379, 748, 45);
			jDesktopPane1.add(panel);
			jLabelPrintLabel_1 = new JLabel4j_std();
			jLabelPrintLabel_1.setBounds(66, 15, 138, 21);
			panel.add(jLabelPrintLabel_1);
			jLabelPrintLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabelPrintLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelPrintLabel_1.setText(lang.get("lbl_Preview"));
			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setBounds(208, 15, 21, 21);
			panel.add(jCheckBoxAutoPreview);
			jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
			jCheckBoxAutoPreview.setSelected(true);
			jCheckBoxAutoPreview.setBackground(Common.color_app_window);
			jCheckBoxAutoPreview.setEnabled(true);
			jLabelPrintLabel_2 = new JLabel4j_std();
			jLabelPrintLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelPrintLabel_2.setText(lang.get("lbl_Label_Header_Text"));
			jLabelPrintLabel_2.setBounds(315, 15, 138, 21);
			panel.add(jLabelPrintLabel_2);
			jLabelQuantity_1 = new JLabel4j_std();
			jLabelQuantity_1.setBounds(528, 15, 154, 21);
			panel.add(jLabelQuantity_1);
			jLabelQuantity_1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelQuantity_1.setText(lang.get("lbl_No_Of_Labels"));
			jSpinnerQuantity = new JSpinner();
			jSpinnerQuantity.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)
				{
					int t = Integer.valueOf(jSpinnerQuantity.getValue().toString());

					if (t <= 0)
					{
						jSpinnerQuantity.setValue(1);
					}

					if (t > 50)
					{
						jSpinnerQuantity.setValue(50);
					}
				}
			});

			jSpinnerQuantity.setBounds(687, 15, 49, 21);
			jSpinnerQuantity.setInputVerifier(null);
			jSpinnerQuantity.setModel(quantitynumbermodel);
			jSpinnerQuantity.setValue(1);
			JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerQuantity);
			ne.getTextField().setFont(Common.font_std);
			jSpinnerQuantity.setEditor(ne);
			panel.add(jSpinnerQuantity);

			checkBoxIncHeaderText = new JCheckBox4j();
			checkBoxIncHeaderText.setSelected(true);
			checkBoxIncHeaderText.setBackground(Color.WHITE);
			checkBoxIncHeaderText.setBounds(453, 15, 21, 21);
			panel.add(checkBoxIncHeaderText);

			jTextFieldBatchSuffix = new JTextField4j();
			jTextFieldBatchSuffix.setText("");
			jTextFieldBatchSuffix.setBounds(285, 85, 39, 21);
			jPanelLabel.add(jTextFieldBatchSuffix);

			calendarButtonjSpinnerExpiryDate = new JCalendarButton(jSpinnerExpiryDate);
			calendarButtonjSpinnerExpiryDate.setEnabled(false);
			calendarButtonjSpinnerExpiryDate.setBounds(308, 52, 21, 21);
			jPanelLabel.add(calendarButtonjSpinnerExpiryDate);

			lblPrintQueueFor = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			lblPrintQueueFor.setHorizontalAlignment(SwingConstants.TRAILING);
			lblPrintQueueFor.setBounds(6, 442, 102, 16);
			jDesktopPane1.add(lblPrintQueueFor);

			jTextFieldBatchPrefix.setText("");
			jTextFieldBatchSuffix.setText("");
			jTextFieldRequiredEAN = new JTextField4j();
			jTextFieldRequiredEAN.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					checkFieldColours();
				}
			});
			jTextFieldRequiredEAN.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldRequiredEAN.setBounds(535, 85, 126, 21);
			jPanelLabel.add(jTextFieldRequiredEAN);
			jTextFieldRequiredEAN.setFocusCycleRoot(true);
			jTextFieldRequiredEAN.setEditable(false);
			jTextFieldRequiredEAN.setEnabled(false);

			jTextFieldBaseEAN = new JTextField4j();
			jTextFieldBaseEAN.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent arg0)
				{
					checkFieldColours();

				}
			});
			jTextFieldBaseEAN.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldBaseEAN.setBounds(535, 52, 126, 21);
			jPanelLabel.add(jTextFieldBaseEAN);
			jTextFieldBaseEAN.setFocusCycleRoot(true);
			jTextFieldBaseEAN.setEditable(false);
			jTextFieldBaseEAN.setEnabled(false);

			jTextFieldRequiredVariant = new JTextField4j();
			jTextFieldRequiredVariant.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldRequiredVariant.setBounds(681, 85, 39, 21);
			jPanelLabel.add(jTextFieldRequiredVariant);
			jTextFieldRequiredVariant.setFocusCycleRoot(true);
			jTextFieldRequiredVariant.setEditable(false);
			jTextFieldRequiredVariant.setEnabled(false);

			jTextFieldBaseVariant = new JTextField4j();
			jTextFieldBaseVariant.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldBaseVariant.setBounds(681, 52, 39, 21);
			jPanelLabel.add(jTextFieldBaseVariant);
			jTextFieldBaseVariant.setFocusCycleRoot(true);
			jTextFieldBaseVariant.setEditable(false);
			jTextFieldBaseVariant.setEnabled(false);

			jLabelEAN = new JLabel4j_std();
			jLabelEAN.setBounds(535, 27, 120, 21);
			jPanelLabel.add(jLabelEAN);
			jLabelEAN.setText(lang.get("lbl_Material_UOM_EAN"));
			jLabelEAN.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelEAN.setHorizontalTextPosition(SwingConstants.CENTER);

			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setText(lang.get("lbl_Material_UOM_Variant"));
			label4j_std.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std.setBounds(662, 27, 80, 21);
			jPanelLabel.add(label4j_std);

			JLabel4j_std label4j_std_1 = new JLabel4j_std();
			label4j_std_1.setText(lang.get("lbl_Pallet_Quantity"));
			label4j_std_1.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std_1.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_1.setBounds(349, 27, 94, 21);
			jPanelLabel.add(label4j_std_1);

			JLabel4j_std label4j_std_2 = new JLabel4j_std();
			label4j_std_2.setText(lang.get("lbl_Pallet_UOM"));
			label4j_std_2.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std_2.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_2.setBounds(455, 27, 63, 21);
			jPanelLabel.add(label4j_std_2);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void checkFieldColours()
	{
		Color background = Color.WHITE;

		try
		{
			if (jTextFieldProcessOrderStatus.getText().equals("") == false)
			{
				if (jTextFieldProcessOrderStatus.getText().equals("Ready") || (jTextFieldProcessOrderStatus.getText().equals("Running")))
				{
					jTextFieldProcessOrderStatus.setBackground(Color.WHITE);
				}
				else
				{
					jTextFieldProcessOrderStatus.setBackground(Color.RED);
				}
				if (jTextFieldBaseEAN.getText().equals("") == true)
				{
					background = Color.YELLOW;
				}
			}
		} catch (Exception e)
		{
			background = Color.YELLOW;
		}

		try
		{
			jTextFieldBaseEAN.setBackground(background);
		} catch (Exception e)
		{

		}

		try
		{
			if (jTextFieldProcessOrderStatus.getText().equals("") == false)
			{
				if (jTextFieldRequiredEAN.getText().equals("") == true)
				{
					background = Color.YELLOW;
				}
			}
		} catch (Exception e)
		{
			background = Color.YELLOW;
		}

		try
		{
			jTextFieldRequiredEAN.setBackground(background);
		} catch (Exception e)
		{

		}

	}

	public class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent event)
		{
			Calendar rightNow = Calendar.getInstance();
			Date d = rightNow.getTime();

			try
			{
				if (jCheckBoxDOMOverride.isSelected() == false)
				{
					jSpinnerProductionDate.setDate(d);
				} else
				{
					calcBBEBatch();
				}

			} catch (Exception e)
			{
			}
		}
	}

	private void buildSQL(String key)
	{
		JDBQuery.closeStatement(listStatement);

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBLabelData.select");

		query.addText(temp);

		query.addParameter(key);

		query.applyRestriction(false, "none", 0);
		query.bindParams();

		listStatement = query.getPreparedStatement();
	}
}
