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

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.logging.log4j.Logger;

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
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JTitledBorder4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePackLabelPrint is used to insert records into the
 * APP_LABEL_DATA table and print a case/tray label either directly or via an
 * interface to an external system. When printing case labels you have the
 * option of sending a report directly to a printer or using the "Assign to
 * Labeller" option. If the assign option is selected all of the data required
 * to print the label is written to a record in the table. A background thread
 * is then used to write the required data to a file for each labeller which
 * needs to print the data. It should be noted that the operator selects a
 * production line when printing and each production line can have one or more
 * physical labellers.
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePackLabelPrint.jpg" >
 *
 * @see com.commander4j.db.JDBLabelData JDBLabelData
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 * @see com.commander4j.db.JDBAutoLabellerResources JDBAutoLabellerResources
 * @see com.commander4j.db.JDBPrinters JDBPrinters
 */
public class JInternalFramePackLabelPrint extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JInternalFramePackLabelPrint.class);
	private BigDecimal caseDefaultQuantity;
	private ClockListener clocklistener = new ClockListener();
	private JButton4j jButtonAssign;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPOLookup;
	private JButton4j jButtonPrint;
	private JCalendarButton calendarButtonjSpinnerExpiryDate;
	private JCalendarButton calendarButtonjSpinnerProductionDate;
	private JCheckBox4j checkBoxIncHeaderText;
	private JCheckBox4j jCheckBoxAutoPreview;
	private JCheckBox4j jCheckBoxBatchPrefixOverride;
	private JCheckBox4j jCheckBoxDOMOverride;
	private JCheckBox4j jCheckBoxExpiryOverride;
	private JComboBoxPODevices4j comboBoxPrintQueue;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialBatch materialbatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processorder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource processorderResource = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private JDateControl jSpinnerDueDate;
	private JDateControl jSpinnerExpiryDate;
	private JDateControl jSpinnerProductionDate;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabel1ShelfLifeUOM;
	private JLabel4j_std jLabelBatch;
	private JLabel4j_std jLabelBatchExpiry;
	private JLabel4j_std jLabelDescription;
	private JLabel4j_std jLabelDueDate;
	private JLabel4j_std jLabelEAN;
	private JLabel4j_std jLabelLegacyCode;
	private JLabel4j_std jLabelLocation;
	private JLabel4j_std jLabelMaterial;
	private JLabel4j_std jLabelOrderDescription;
	private JLabel4j_std jLabelOrderStatus;
	private JLabel4j_std jLabelPrintLabel_1;
	private JLabel4j_std jLabelPrintLabel_2;
	private JLabel4j_std jLabelProcessOrder;
	private JLabel4j_std jLabelProductionDate;
	private JLabel4j_std jLabelQuantity_1;
	private JLabel4j_std jLabelRecipe;
	private JLabel4j_std jLabelRounding;
	private JLabel4j_std jLabelShelfLife;
	private JLabel4j_std lblPrintQueueFor;
	private JLabelPrint labelPrint = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JPanel4j jPanelLabel;
	private JPanel4j jPanelMaterial;
	private JPanel4j jPanelProcessOrder;
	private JQuantityInput jFormattedTextFieldBaseUOMQuantity;
	private JQuantityInput jFormattedTextFieldRequiredUOMQuantity;
	private JShelfLifeRoundingRule shelfliferoundingrule = new JShelfLifeRoundingRule();
	private JShelfLifeUom shelflifeuom = new JShelfLifeUom();
	private JSpinner4j jSpinnerQuantity;
	private JSpinner4j jSpinnerShelfLife;
	private JTextField4j jTextFieldBaseEAN;
	private JTextField4j jTextFieldBaseUom;
	private JTextField4j jTextFieldBaseVariant;
	private JTextField4j jTextFieldBatchPrefix;
	private JTextField4j jTextFieldBatchSuffix;
	private JTextField4j jTextFieldLegacyCode;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldMaterial;
	private JTextField4j jTextFieldMaterialDescription;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JTextField4j jTextFieldProcessOrderStatus;
	private JTextField4j jTextFieldRecipe;
	private JTextField4j jTextFieldRecipeVersion;
	private JTextField4j jTextFieldRequiredEAN;
	private JTextField4j jTextFieldRequiredUom;
	private JTextField4j jTextFieldRequiredVariant;
	private JTextField4j jTextFieldShelfLifeRoundingRule;
	private JTextField4j jTextFieldShelfLifeUOM;
	private JTextField4j textField4jCustomer = new JTextField4j();
	private JTextField4j textField4jResource = new JTextField4j();
	private PreparedStatement listStatement;
	private SpinnerNumberModel quantitynumbermodel = new SpinnerNumberModel();
	private SpinnerNumberModel shelflifenumbermodel = new SpinnerNumberModel();
	private String batchFormat = "";
	private String batchValidate = "";
	private String expiryMode = "";
	private Timer timer = new Timer(1000, clocklistener);
	private boolean DOMEditable = true;
	private boolean ExpiryEditable = true;

	public class ClockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Calendar rightNow = Calendar.getInstance();
			Date d = rightNow.getTime();

			try
			{
				if (jCheckBoxDOMOverride.isSelected() == false)
				{
					jSpinnerProductionDate.setDate(d);
				}
				else
				{
					calcBBEBatch();
				}

			}
			catch (Exception e)
			{
			}
		}
	}

	public JInternalFramePackLabelPrint(String procOrder)
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

		DOMEditable = Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_MANUAL_EDIT_DOM");
		ExpiryEditable = Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_MANUAL_EDIT_EXPIRY");

		initGUI();
		clearFields();

		timer.start();

		jTextFieldBatchPrefix.setText("");
		jTextFieldBatchSuffix.setText("");

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

		procOrder = JUtility.replaceNullStringwithBlank(procOrder);

		processOrderChanged(procOrder);
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
				jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));

				if (material.getMaterial().length() > 0)
				{
					if (expiryMode.equals("BATCH"))
					{
						if (materialbatch.getMaterialBatchProperties(material.getMaterial(), jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText()) == true)
						{
							try
							{
								jSpinnerExpiryDate.setDate(materialbatch.getExpiryDate());
							}
							catch (Exception e)
							{
							}
						}
						else
						{
							Date de = jSpinnerProductionDate.getDate();

							try
							{
								jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
							}
							catch (Exception e)
							{
							}
						}
					}
					else
					{
						Date de = jSpinnerProductionDate.getDate();

						try
						{
							jSpinnerExpiryDate.setDate(material.calcBBE(de, material.getShelfLife(), material.getShelfLifeUom(), material.getShelfLifeRule()));
						}
						catch (Exception e)
						{
						}
					}
				}
			}

			jSpinnerExpiryDate.setDate(material.getRoundedExpiryDate(jSpinnerExpiryDate.getDate()));
		}
		catch (Exception e)
		{

		}
	}

	private void checkFieldColours()
	{
		Color background = Common.color_app_window;

		try
		{
			if (jTextFieldProcessOrderStatus.getText().equals("") == false)
			{
				if (jTextFieldProcessOrderStatus.getText().equals("Ready") || (jTextFieldProcessOrderStatus.getText().equals("Running")))
				{
					jTextFieldProcessOrderStatus.setBackground(Common.color_app_window);
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
		}
		catch (Exception e)
		{
			background = Color.YELLOW;
		}

		try
		{
			jTextFieldBaseEAN.setBackground(background);
		}
		catch (Exception e)
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
		}
		catch (Exception e)
		{
			background = Color.YELLOW;
		}

		try
		{
			jTextFieldRequiredEAN.setBackground(background);
		}
		catch (Exception e)
		{

		}

	}

	private void clearFields()
	{
		// previousDateString = "";
		jTextFieldProcessOrderDescription.setText("");
		jTextFieldProcessOrderStatus.setText("");
		jTextFieldRecipe.setText("");
		jTextFieldRecipeVersion.setText("");
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
		textField4jResource.setText("");
		textField4jCustomer.setText("");

		jCheckBoxDOMOverride.setSelected(false);
		enableField(jSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());

		jCheckBoxExpiryOverride.setSelected(false);
		enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiryOverride.isSelected()));

		jCheckBoxBatchPrefixOverride.setSelected(false);
		enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());

		jFormattedTextFieldRequiredUOMQuantity.setValue(0);
		jFormattedTextFieldBaseUOMQuantity.setValue(0);
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
		// labelData.setProdQuantity(jFormattedTextFieldRequiredUOMQuantity.getQuantity());
		labelData.setProdQuantity(jFormattedTextFieldBaseUOMQuantity.getQuantity());
		labelData.setProdUom(jTextFieldRequiredUom.getText());
		// labelData.setBaseQuantity(jFormattedTextFieldBaseUOMQuantity.getQuantity());
		labelData.setBaseQuantity(jFormattedTextFieldRequiredUOMQuantity.getQuantity());
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

	private void enableField(JComponent field, Boolean allowed)
	{
		if (allowed == true)
		{
			field.setEnabled(true);
		}
		else
		{
			field.setEnabled(false);
		}
	}

	private boolean getExpiryDateManualEditStatus(boolean requestedStatus)
	{
		boolean result = ExpiryEditable;

		if (ExpiryEditable)
		{
			result = requestedStatus;
		}

		return result;
	}

	private boolean getProductionDateManualEditStatus(boolean requestedStatus)
	{
		boolean result = DOMEditable;

		if (DOMEditable)
		{
			result = requestedStatus;
		}

		return result;
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(674, 474));
			this.setBounds(0, 0, 774, 567);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			jDesktopPane1 = new JDesktopPane4j();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(665, 490));

			jDesktopPane1.setLayout(null);
			jButtonPrint = new JButton4j(Common.icon_label_16x16);
			jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_PRINTER"));

			jButtonAssign = new JButton4j(Common.icon_production_line_16x16);
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
			jButtonPrint.setText(lang.get("btn_Label"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(6, 471, 182, 32);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					printOrAssign("Print");
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
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

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(382, 471, 182, 32);
			jPanelProcessOrder = new JPanel4j();
			jPanelProcessOrder.setFont(Common.font_title);
			jDesktopPane1.add(jPanelProcessOrder);
			jPanelProcessOrder.setBounds(7, 7, 748, 112);
			jPanelProcessOrder.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Process_Order")));
			jPanelProcessOrder.setLayout(null);

			jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
			jPanelProcessOrder.add(jTextFieldProcessOrder);
			;
			jTextFieldProcessOrder.setBounds(161, 21, 119, 22);
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
			jLabelProcessOrder.setBounds(10, 21, 142, 22);
			jTextFieldProcessOrderDescription = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderDescription);
			jTextFieldProcessOrderDescription.setBounds(449, 21, 287, 22);
			jTextFieldProcessOrderDescription.setEditable(false);
			jTextFieldProcessOrderDescription.setEnabled(false);
			jLabelOrderDescription = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelOrderDescription);
			jLabelOrderDescription.setText(lang.get("lbl_Description"));
			jLabelOrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelOrderDescription.setBounds(306, 21, 136, 22);
			jLabelOrderStatus = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelOrderStatus);
			jLabelOrderStatus.setText(lang.get("lbl_Process_Order_Status"));
			jLabelOrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelOrderStatus.setBounds(290, 49, 152, 22);
			jSpinnerDueDate = new JDateControl();
			jPanelProcessOrder.add(jSpinnerDueDate);
			jSpinnerDueDate.setEnabled(false);
			jSpinnerDueDate.setBounds(161, 49, 120, 22);
			jSpinnerDueDate.getEditor().setOpaque(true);
			jLabelDueDate = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelDueDate);
			jLabelDueDate.setText(lang.get("lbl_Process_Order_Due_Date"));
			jLabelDueDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDueDate.setBounds(10, 49, 142, 22);
			jTextFieldProcessOrderStatus = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldProcessOrderStatus);
			jTextFieldProcessOrderStatus.setBounds(449, 49, 126, 22);
			jTextFieldProcessOrderStatus.setEditable(false);
			jTextFieldProcessOrderStatus.setEnabled(false);
			jTextFieldRecipe = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldRecipe);
			jTextFieldRecipe.setBounds(161, 77, 119, 22);
			jTextFieldRecipe.setEditable(false);
			jTextFieldRecipe.setEnabled(false);
			jTextFieldRecipeVersion = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldRecipeVersion);
			jTextFieldRecipeVersion.setBounds(300, 77, 58, 22);
			jTextFieldRecipeVersion.setEditable(false);
			jTextFieldRecipeVersion.setEnabled(false);
			jLabelRecipe = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelRecipe);
			jLabelRecipe.setText(lang.get("lbl_Process_Order_Recipe"));
			jLabelRecipe.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelRecipe.setBounds(10, 77, 142, 22);
			jTextFieldLocation = new JTextField4j();
			jPanelProcessOrder.add(jTextFieldLocation);
			jTextFieldLocation.setBounds(449, 77, 126, 22);
			jTextFieldLocation.setEditable(false);
			jTextFieldLocation.setEnabled(false);
			jLabelLocation = new JLabel4j_std();
			jPanelProcessOrder.add(jLabelLocation);
			jLabelLocation.setText(lang.get("lbl_Location_ID"));
			jLabelLocation.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelLocation.setBounds(290, 77, 152, 22);
			jButtonPOLookup = new JButton4j(Common.icon_lookup_16x16);
			jPanelProcessOrder.add(jButtonPOLookup);
			jButtonPOLookup.setBounds(280, 21, 20, 22);
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

			jPanelMaterial = new JPanel4j();

			jDesktopPane1.add(jPanelMaterial);
			jPanelMaterial.setBounds(7, 119, 748, 112);
			jPanelMaterial.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Material")));
			jPanelMaterial.setLayout(null);
			jPanelMaterial.setFont(Common.font_title);
			jTextFieldMaterial = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterial);
			jTextFieldMaterial.setBounds(161, 21, 128, 22);
			jTextFieldMaterial.setEditable(false);
			jTextFieldMaterial.setEnabled(false);
			jLabelMaterial = new JLabel4j_std();
			jPanelMaterial.add(jLabelMaterial);
			jLabelMaterial.setText(lang.get("lbl_Material"));
			jLabelMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelMaterial.setBounds(10, 21, 142, 22);
			jTextFieldMaterialDescription = new JTextField4j();
			jPanelMaterial.add(jTextFieldMaterialDescription);
			jTextFieldMaterialDescription.setBounds(449, 21, 287, 22);
			jTextFieldMaterialDescription.setEditable(false);
			jTextFieldMaterialDescription.setEnabled(false);
			jLabelDescription = new JLabel4j_std();
			jPanelMaterial.add(jLabelDescription);
			jLabelDescription.setText(lang.get("lbl_Description"));
			jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDescription.setBounds(291, 21, 151, 22);
			jSpinnerShelfLife = new JSpinner4j();
			jPanelMaterial.add(jSpinnerShelfLife);
			jSpinnerShelfLife.setModel(shelflifenumbermodel);
			JSpinner4j.NumberEditor nec = new JSpinner4j.NumberEditor(jSpinnerShelfLife);
			jSpinnerShelfLife.setEditor(nec);
			jSpinnerShelfLife.setBounds(161, 49, 63, 22);
			jSpinnerShelfLife.setEnabled(false);
			jTextFieldShelfLifeRoundingRule = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeRoundingRule);
			jTextFieldShelfLifeRoundingRule.setBounds(638, 49, 98, 22);
			jTextFieldShelfLifeRoundingRule.setEditable(false);
			jTextFieldShelfLifeRoundingRule.setEnabled(false);
			jLabelRounding = new JLabel4j_std();
			jPanelMaterial.add(jLabelRounding);
			jLabelRounding.setText(lang.get("lbl_Material_Shelf_Life_Rounding_Rule"));
			jLabelRounding.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelRounding.setBounds(546, 49, 85, 22);
			jTextFieldShelfLifeUOM = new JTextField4j();
			jPanelMaterial.add(jTextFieldShelfLifeUOM);
			jTextFieldShelfLifeUOM.setBounds(449, 49, 91, 22);
			jTextFieldShelfLifeUOM.setEditable(false);
			jTextFieldShelfLifeUOM.setEnabled(false);
			jLabel1ShelfLifeUOM = new JLabel4j_std();
			jPanelMaterial.add(jLabel1ShelfLifeUOM);
			jLabel1ShelfLifeUOM.setText(lang.get("lbl_Material_Shelf_Life_UOM"));
			jLabel1ShelfLifeUOM.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel1ShelfLifeUOM.setBounds(281, 49, 161, 22);
			jLabelShelfLife = new JLabel4j_std();
			jPanelMaterial.add(jLabelShelfLife);
			jLabelShelfLife.setText(lang.get("lbl_Material_Shelf_Life"));
			jLabelShelfLife.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelShelfLife.setBounds(10, 49, 142, 22);
			jTextFieldLegacyCode = new JTextField4j();
			jPanelMaterial.add(jTextFieldLegacyCode);
			jTextFieldLegacyCode.setBounds(161, 77, 128, 22);
			jTextFieldLegacyCode.setEditable(false);
			jTextFieldLegacyCode.setEnabled(false);
			jLabelLegacyCode = new JLabel4j_std();
			jPanelMaterial.add(jLabelLegacyCode);
			jLabelLegacyCode.setText(lang.get("lbl_Material_Legacy_Code"));
			jLabelLegacyCode.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelLegacyCode.setBounds(10, 77, 142, 22);
			jPanelLabel = new JPanel4j();
			jDesktopPane1.add(jPanelLabel);
			jPanelLabel.setBounds(7, 231, 748, 136);
			jPanelLabel.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("btn_Label")));
			jPanelLabel.setLayout(null);
			jPanelLabel.setFont(Common.font_std);

			comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, "RPT_PACK_LABEL", "");
			comboBoxPrintQueue.setBounds(115, 436, 621, 22);
			jDesktopPane1.add(comboBoxPrintQueue);

			jSpinnerProductionDate = new JDateControl();
			jPanelLabel.add(jSpinnerProductionDate);
			jSpinnerProductionDate.setFont(new java.awt.Font("Dialog", 0, 12));
			jSpinnerProductionDate.setBounds(176, 21, 120, 22);
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
				jSpinnerProductionDate.setEnabled(getProductionDateManualEditStatus(false));
			}
			catch (Exception e)
			{
			}

			jLabelProductionDate = new JLabel4j_std();
			jPanelLabel.add(jLabelProductionDate);
			jLabelProductionDate.setText(lang.get("lbl_Pallet_DOM"));
			jLabelProductionDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelProductionDate.setBounds(10, 21, 142, 22);

			jFormattedTextFieldBaseUOMQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldBaseUOMQuantity.setEditable(false);
			jPanelLabel.add(jFormattedTextFieldBaseUOMQuantity);
			jFormattedTextFieldBaseUOMQuantity.setBounds(349, 85, 94, 22);
			jFormattedTextFieldBaseUOMQuantity.setValue(0);
			jFormattedTextFieldBaseUOMQuantity.setEnabled(false);

			jFormattedTextFieldRequiredUOMQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldRequiredUOMQuantity.setEditable(false);

			jPanelLabel.add(jFormattedTextFieldRequiredUOMQuantity);
			jFormattedTextFieldRequiredUOMQuantity.setBounds(349, 52, 94, 22);
			jFormattedTextFieldRequiredUOMQuantity.setValue(0);
			jFormattedTextFieldRequiredUOMQuantity.setEnabled(false);
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
			jTextFieldRequiredUom.setBounds(462, 85, 56, 22);
			jTextFieldRequiredUom.setEditable(false);
			jTextFieldRequiredUom.setEnabled(false);

			jTextFieldBaseUom = new JTextField4j();
			jTextFieldBaseUom.setHorizontalAlignment(SwingConstants.CENTER);
			jPanelLabel.add(jTextFieldBaseUom);
			jTextFieldBaseUom.setBounds(462, 52, 56, 22);
			jTextFieldBaseUom.setEditable(false);
			jTextFieldBaseUom.setEnabled(false);

			jCheckBoxDOMOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxDOMOverride);

			jCheckBoxDOMOverride.setBounds(153, 21, 21, 22);
			jCheckBoxDOMOverride.setEnabled(false);
			jCheckBoxDOMOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerProductionDate, getProductionDateManualEditStatus(jCheckBoxDOMOverride.isSelected()));
					enableField(calendarButtonjSpinnerProductionDate, jCheckBoxDOMOverride.isSelected());
				}
			});

			calendarButtonjSpinnerProductionDate = new JCalendarButton(jSpinnerProductionDate);
			calendarButtonjSpinnerProductionDate.setEnabled(getProductionDateManualEditStatus(false));
			calendarButtonjSpinnerProductionDate.setBounds(300, 21, 21, 21);
			jPanelLabel.add(calendarButtonjSpinnerProductionDate);
			jLabelBatch = new JLabel4j_std();
			jPanelLabel.add(jLabelBatch);
			jLabelBatch.setText(lang.get("lbl_Material_Batch"));
			jLabelBatch.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelBatch.setBounds(10, 85, 142, 22);
			jTextFieldBatchPrefix = new JTextField4j(JDBMaterialBatch.field_batch_number);
			jPanelLabel.add(jTextFieldBatchPrefix);
			jTextFieldBatchPrefix.setBounds(176, 85, 108, 22);
			jTextFieldBatchPrefix.setEnabled(false);
			jSpinnerExpiryDate = new JDateControl();
			jSpinnerExpiryDate.setDisplayMode(JDateControl.mode_disable_visible);
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
			jSpinnerExpiryDate.setBounds(176, 52, 120, 22);
			jSpinnerExpiryDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
			jSpinnerExpiryDate.getEditor().setSize(87, 21);
			jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));
			jLabelBatchExpiry = new JLabel4j_std();
			jPanelLabel.add(jLabelBatchExpiry);
			jLabelBatchExpiry.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			jLabelBatchExpiry.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelBatchExpiry.setBounds(10, 52, 142, 22);
			jCheckBoxExpiryOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxExpiryOverride);
			jCheckBoxExpiryOverride.setBounds(153, 52, 21, 22);

			jCheckBoxExpiryOverride.setEnabled(false);
			jCheckBoxExpiryOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiryOverride.isSelected()));
					enableField(calendarButtonjSpinnerExpiryDate, jCheckBoxExpiryOverride.isSelected());
					calcBBEBatch();
				}
			});

			jCheckBoxBatchPrefixOverride = new JCheckBox4j();
			jPanelLabel.add(jCheckBoxBatchPrefixOverride);
			jCheckBoxBatchPrefixOverride.setEnabled(false);

			jCheckBoxBatchPrefixOverride.setBounds(153, 85, 21, 22);
			jCheckBoxBatchPrefixOverride.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());
					calcBBEBatch();
				}
			});

			jStatusText = new JLabel4j_status();
			jDesktopPane1.add(jStatusText);
			jStatusText.setBounds(6, 511, 748, 21);

			final JPanel4j panel = new JPanel4j();
			panel.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Options")));

			panel.setFont(Common.font_title);
			panel.setLayout(null);
			panel.setBounds(7, 379, 748, 45);
			jDesktopPane1.add(panel);
			jLabelPrintLabel_1 = new JLabel4j_std();
			jLabelPrintLabel_1.setBounds(66, 15, 138, 22);
			panel.add(jLabelPrintLabel_1);
			jLabelPrintLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabelPrintLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelPrintLabel_1.setText(lang.get("lbl_Preview"));
			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setBounds(208, 15, 21, 22);
			panel.add(jCheckBoxAutoPreview);
			jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
			jCheckBoxAutoPreview.setSelected(true);

			jCheckBoxAutoPreview.setEnabled(true);
			jLabelPrintLabel_2 = new JLabel4j_std();
			jLabelPrintLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelPrintLabel_2.setText(lang.get("lbl_Label_Header_Text"));
			jLabelPrintLabel_2.setBounds(315, 15, 138, 22);
			panel.add(jLabelPrintLabel_2);
			jLabelQuantity_1 = new JLabel4j_std();
			jLabelQuantity_1.setBounds(507, 15, 154, 22);
			panel.add(jLabelQuantity_1);
			jLabelQuantity_1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelQuantity_1.setText(lang.get("lbl_No_Of_Labels"));
			jSpinnerQuantity = new JSpinner4j();
			jSpinnerQuantity.addChangeListener(new ChangeListener()
			{
				public void stateChanged(final ChangeEvent e)
				{
					int t = Integer.valueOf(jSpinnerQuantity.getValue().toString());
					int maxt = Integer.valueOf(ctrl.getKeyValueWithDefault("MAX CASE LABEL PRINT", "1500", "Max number of labels that can be printed"));

					if (t <= 0)
					{
						jSpinnerQuantity.setValue(1);
					}

					if (t > maxt)
					{
						jSpinnerQuantity.setValue(maxt);
					}
				}
			});

			jSpinnerQuantity.setBounds(665, 15, 71, 22);
			jSpinnerQuantity.setInputVerifier(null);
			jSpinnerQuantity.setModel(quantitynumbermodel);
			jSpinnerQuantity.setValue(1);
			JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerQuantity);
			ne.getTextField().setFont(Common.font_std);
			jSpinnerQuantity.setEditor(ne);
			panel.add(jSpinnerQuantity);

			checkBoxIncHeaderText = new JCheckBox4j();
			checkBoxIncHeaderText.setSelected(true);

			checkBoxIncHeaderText.setBounds(453, 15, 21, 22);
			panel.add(checkBoxIncHeaderText);

			jTextFieldBatchSuffix = new JTextField4j();
			jTextFieldBatchSuffix.setText("");
			jTextFieldBatchSuffix.setBounds(285, 85, 39, 22);
			jPanelLabel.add(jTextFieldBatchSuffix);

			calendarButtonjSpinnerExpiryDate = new JCalendarButton(jSpinnerExpiryDate);
			calendarButtonjSpinnerExpiryDate.setEnabled(false);
			calendarButtonjSpinnerExpiryDate.setBounds(300, 52, 21, 21);
			jPanelLabel.add(calendarButtonjSpinnerExpiryDate);

			lblPrintQueueFor = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			lblPrintQueueFor.setHorizontalAlignment(SwingConstants.TRAILING);
			lblPrintQueueFor.setBounds(6, 436, 102, 22);
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
			jTextFieldRequiredEAN.setBounds(535, 85, 126, 22);
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
			jTextFieldBaseEAN.setBounds(535, 52, 126, 22);
			jPanelLabel.add(jTextFieldBaseEAN);
			jTextFieldBaseEAN.setFocusCycleRoot(true);
			jTextFieldBaseEAN.setEditable(false);
			jTextFieldBaseEAN.setEnabled(false);

			jTextFieldRequiredVariant = new JTextField4j();
			jTextFieldRequiredVariant.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldRequiredVariant.setBounds(681, 85, 39, 22);
			jPanelLabel.add(jTextFieldRequiredVariant);
			jTextFieldRequiredVariant.setFocusCycleRoot(true);
			jTextFieldRequiredVariant.setEditable(false);
			jTextFieldRequiredVariant.setEnabled(false);

			jTextFieldBaseVariant = new JTextField4j();
			jTextFieldBaseVariant.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldBaseVariant.setBounds(681, 52, 39, 22);
			jPanelLabel.add(jTextFieldBaseVariant);
			jTextFieldBaseVariant.setFocusCycleRoot(true);
			jTextFieldBaseVariant.setEditable(false);
			jTextFieldBaseVariant.setEnabled(false);

			jLabelEAN = new JLabel4j_std();
			jLabelEAN.setBounds(535, 27, 120, 22);
			jPanelLabel.add(jLabelEAN);
			jLabelEAN.setText(lang.get("lbl_Material_UOM_EAN"));
			jLabelEAN.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelEAN.setHorizontalTextPosition(SwingConstants.CENTER);

			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setText(lang.get("lbl_Material_UOM_Variant"));
			label4j_std.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std.setBounds(662, 27, 80, 22);
			jPanelLabel.add(label4j_std);

			JLabel4j_std label4j_std_1 = new JLabel4j_std();
			label4j_std_1.setText(lang.get("lbl_Pallet_Quantity"));
			label4j_std_1.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std_1.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_1.setBounds(349, 27, 94, 22);
			jPanelLabel.add(label4j_std_1);

			JLabel4j_std label4j_std_2 = new JLabel4j_std();
			label4j_std_2.setText(lang.get("lbl_Pallet_UOM"));
			label4j_std_2.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std_2.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_2.setBounds(455, 27, 63, 22);
			jPanelLabel.add(label4j_std_2);

			textField4jResource.setText("");
			textField4jResource.setEnabled(false);
			textField4jResource.setEditable(false);
			textField4jResource.setBounds(588, 49, 148, 22);
			jPanelProcessOrder.add(textField4jResource);

			textField4jCustomer.setText("");
			textField4jCustomer.setEnabled(false);
			textField4jCustomer.setEditable(false);
			textField4jCustomer.setBounds(587, 77, 148, 22);
			jPanelProcessOrder.add(textField4jCustomer);

			JLabel4j_std jLabelRecipeVersion = new JLabel4j_std();
			jLabelRecipeVersion.setText("/");
			jLabelRecipeVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jLabelRecipeVersion.setBounds(280, 77, 21, 21);
			jPanelProcessOrder.add(jLabelRecipeVersion);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
				if (JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Quantity_Confirm"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16) == JOptionPane.YES_OPTION)
				{
					confirmQuantity = true;
				}
				else
				{
					confirmQuantity = false;
				}
			}
		}

		if (confirmQuantity == true)
		{

			String processOrder = processorder.getProcessOrder();
			String material = processorder.getMaterial();
			String batchNumber = jTextFieldBatchPrefix.getText() + jTextFieldBatchSuffix.getText();
			Timestamp expiryDate = JUtility.getTimestampFromDate(jSpinnerExpiryDate.getDate());

			if (materialbatch.autoCreateMaterialBatch(material, batchNumber, batchValidate, expiryDate, ""))
			{

				String key = createLabelData(noOfLabels);

				if (mode.equals("Print"))
				{

					JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
					buildSQL(key);

					JLaunchReport.runReport(labelPrint.getPackLabelReportName(processOrder), listStatement, jCheckBoxAutoPreview.isSelected(), pq, noOfLabels, checkBoxIncHeaderText.isSelected());

				}

				if (mode.equals("Assign"))
				{
					JLaunchMenu.runDialog("FRM_LABEL_DATA_ASSIGN", key);
				}

			}
			else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, materialbatch.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
			}
		}
	}

	public void processOrderChanged(String po)
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

		}
		else
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
			jTextFieldRecipeVersion.setText(processorder.getRecipeVersion());
			jTextFieldLocation.setText(processorder.getLocation());

			try
			{
				jSpinnerDueDate.setDate(processorder.getDueDate());
			}
			catch (Exception e)
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
			textField4jResource.setText(processorder.getRequiredResource());
			textField4jCustomer.setText(processorder.getCustomerID());

			valid = true;

			jTextFieldBatchSuffix.setText(processorderResource.getBatchSuffixForResource(processorder.getRequiredResource()));

			batchFormat = materialbatch.getBatchFormatString(processorder);
			batchValidate = materialbatch.getBatchValidationString(processorder);

			calcBBEBatch();

			comboBoxPrintQueue.refreshData("RPT_PACK_LABEL", po);

		}
		else
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
			enableField(jSpinnerExpiryDate, getExpiryDateManualEditStatus(jCheckBoxExpiryOverride.isSelected()));
			enableField(jTextFieldBatchPrefix, jCheckBoxBatchPrefixOverride.isSelected());
			jTextFieldBatchSuffix.setEnabled(true);

			jSpinnerProductionDate.setDisplayMode(JDateControl.mode_disable_visible);

			jSpinnerExpiryDate.setDisplayMode(JDateControl.mode_disable_visible);
			jSpinnerDueDate.setDisplayMode(JDateControl.mode_disable_visible);

		}
		else
		{

			jButtonPrint.setEnabled(false);
			jButtonAssign.setEnabled(false);
			jFormattedTextFieldRequiredUOMQuantity.setEnabled(false);

			jSpinnerProductionDate.setEnabled(false);
			jCheckBoxDOMOverride.setSelected(false);
			jCheckBoxDOMOverride.setEnabled(false);

			jSpinnerExpiryDate.setEnabled(getExpiryDateManualEditStatus(false));
			jCheckBoxExpiryOverride.setSelected(false);
			jCheckBoxExpiryOverride.setEnabled(false);

			jTextFieldBatchPrefix.setEnabled(false);
			jTextFieldBatchSuffix.setEnabled(false);
			jCheckBoxBatchPrefixOverride.setSelected(false);
			jCheckBoxBatchPrefixOverride.setEnabled(false);

			jSpinnerProductionDate.setDisplayMode(JDateControl.mode_disable_not_visible);

			jSpinnerExpiryDate.setDisplayMode(JDateControl.mode_disable_not_visible);
			jSpinnerDueDate.setDisplayMode(JDateControl.mode_disable_not_visible);
		}
		checkFieldColours();
	}
}
