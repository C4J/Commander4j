package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogAssignLabelDataToLine.java
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBAutoLabeller;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * Called from the Case Labelling screen once the user has selected the required
 * process order this dialog box will allow the user to send the data to a
 * nominated Production Line.
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogAssignLabelDataToLine.jpg" >
 * 
 * @see com.commander4j.app.JInternalFramePackLabelPrint JInternalFramePackLabelPrint
 *
 */
public class JDialogAssignLabelDataToLine extends javax.swing.JDialog {
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonAssign;
	private JLabel4j_std lbl_ProcessOrder;
	private String unique_id;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBLabelData labdat = new JDBLabelData(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lbl_BatchNumber;
	private JDBAutoLabeller autolab = new JDBAutoLabeller(Common.selectedHostID, Common.sessionID);
	private JList4j<JDBListData> list;
	private String selectedGroup;

	/**
	 * @param frame Parent Frame
	 * @param unique Unique GUID passed from calling routine. This will be written to the database.
	 */
	public JDialogAssignLabelDataToLine(JFrame frame, String unique)
	{

		super(frame);

		setModal(true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setResizable(false);
		this.setSize(677, 461);

		Dimension screensize = Common.mainForm.getSize();
		Point parentPos = Common.mainForm.getLocation();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(parentPos.x + leftmargin , parentPos.y+ topmargin);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBounds(0, 200, 671, -200);
		jDesktopPane1.setBackground(Common.color_edit_properties);
		this.getContentPane().add(jDesktopPane1);
		jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
		jDesktopPane1.setLayout(null);

		unique_id = unique;
		labdat.getProperties(unique_id);

		mod.getModuleProperties("FRM_LABEL_DATA_ASSIGN");

		setTitle(mod.getDescription() + " (" + labdat.getLabelType() + ")");
		
		selectedGroup = labdat.getLabelType();

		initGUI();
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_LABEL_DATA_ASSIGN"));

		populateList(selectedGroup);

	}

	/**
	 *  Used to enable all buttons
	 */
	private void enableButtons()
	{
		if (list.getValueIsAdjusting() == false)
		{
			if (list.getSelectedIndex() > -1)
			{
				jButtonAssign.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_LABEL_DATA_ASSIGN_TO_AUTOLAB"));
			}
		}
	}

	/**
	 *  Method validates and then assigns the Process Order to the Production Line.
	 */
	private void assign()
	{
		if (list.getValueIsAdjusting() == false)
		{
			if (list.getSelectedIndex() > -1)
			{
				String lineid = ((JDBAutoLabeller) ((JDBListData) list.getSelectedValue()).getmData()).getLine();
				if (autolab.getProperties(lineid, labdat.getLabelType()))
				{
					int n = JOptionPane.showConfirmDialog(this, "Assign Process Order " + labdat.getProcessOrder() + " to " + lineid + " ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
					if (n == 0)
					{
						if (autolab.isValidClientWorkstationID(JUtility.getClientName()))
						{
							if (autolab.isValidProcessOrderResource(labdat.getProcessOrder()))
							{
								labdat.updateLine(unique_id, lineid);
								autolab.setUniqueID(unique_id);
								autolab.setModified(true);
								autolab.update();
								//labdat.sendMessage();
							} else
							{
								JUtility.errorBeep();
								JOptionPane.showMessageDialog(this, "Process Order " + labdat.getProcessOrder() + " cannot be assigned to " + lineid + "\nResource is incorrect " + labdat.getRequiredResource(), lang.get("err_Error"),
										JOptionPane.ERROR_MESSAGE);
							}
						} else
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(this, "Workstation ["+JUtility.getClientName()+"] cannot assign data to [" + lineid + "].", lang.get("err_Error"),
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}

	/**
	 * @param group populates the list of labelers on screen for the specified group
	 */
	private void populateList(String group)
	{
		jButtonAssign.setEnabled(false);

		DefaultComboBoxModel<JDBListData> DefComboBoxMod = new DefaultComboBoxModel<JDBListData>();

		LinkedList<JDBListData> tempLabList = autolab.getLabellerIDsforGroup(group);

		int sel = -1;
		for (int j = 0; j < tempLabList.size(); j++)
		{
			if (((JDBAutoLabeller) tempLabList.get(j).getmData()).isEnabled())
			{
				DefComboBoxMod.addElement(tempLabList.get(j));
			}

		}

		ListModel<JDBListData> jList1Model = DefComboBoxMod;

		list.setModel(jList1Model);
		list.setSelectedIndex(sel);
		list.setCellRenderer(Common.renderer_list);
		list.ensureIndexIsVisible(sel);

		enableButtons();
	}

	/**
	 *  Method used to build GUI
	 */
	private void initGUI()
	{
		try
		{

			jButtonAssign = new JButton4j(Common.icon_production_line_16x16);
			jButtonAssign.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					assign();
				}
			});
			jButtonAssign.setEnabled(false);
			jDesktopPane1.add(jButtonAssign);
			jButtonAssign.setText(lang.get("btn_Assign"));
			jButtonAssign.setMnemonic(lang.getMnemonicChar());
			jButtonAssign.setBounds(80, 387, 130, 32);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(364, 387, 130, 32);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(504, 387, 130, 32);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			lbl_ProcessOrder = new JLabel4j_std();
			jDesktopPane1.add(lbl_ProcessOrder);
			lbl_ProcessOrder.setText(lang.get("lbl_Process_Order"));
			lbl_ProcessOrder.setBounds(12, 10, 125, 22);
			lbl_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);

			JLabel4j_std lbl_Material = new JLabel4j_std();
			lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Material.setText(lang.get("lbl_Material"));
			lbl_Material.setBounds(12, 40, 125, 22);
			jDesktopPane1.add(lbl_Material);

			JLabel4j_std lbl_ExpiryDate = new JLabel4j_std();
			lbl_ExpiryDate.setBounds(306, 100, 125, 22);
			lbl_ExpiryDate.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_ExpiryDate.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
			jDesktopPane1.add(lbl_ExpiryDate);

			JLabel4j_std lbl_User_ID = new JLabel4j_std();
			lbl_User_ID.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_User_ID.setBounds(12, 227, 125, 22);
			lbl_User_ID.setText(lang.get("lbl_User_ID"));
			jDesktopPane1.add(lbl_User_ID);

			JLabel4j_std lbl_Production_Date = new JLabel4j_std(lang.get("lbl_Pallet_DOM"));
			lbl_Production_Date.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Production_Date.setBounds(12, 100, 125, 22);
			jDesktopPane1.add(lbl_Production_Date);

			lbl_BatchNumber = new JLabel4j_std();
			lbl_BatchNumber.setBounds(12, 70, 125, 22);
			lbl_BatchNumber.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl_BatchNumber.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_BatchNumber.setText(lang.get("lbl_Batch"));
			jDesktopPane1.add(lbl_BatchNumber);

			JLabel4j_std lbl_WorkstationID = new JLabel4j_std();
			lbl_WorkstationID.setText(lang.get("lbl_Workstation"));
			lbl_WorkstationID.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_WorkstationID.setBounds(306, 229, 125, 22);
			jDesktopPane1.add(lbl_WorkstationID);

			JTextField4j textField4j_ProcessOrder = new JTextField4j();
			textField4j_ProcessOrder.setEnabled(false);
			textField4j_ProcessOrder.setBounds(154, 10, 139, 22);
			textField4j_ProcessOrder.setText(labdat.getProcessOrder());
			jDesktopPane1.add(textField4j_ProcessOrder);

			JTextField4j textField4j_Material = new JTextField4j();
			textField4j_Material.setEnabled(false);
			textField4j_Material.setBounds(154, 38, 139, 22);
			textField4j_Material.setText(labdat.getMaterial());
			jDesktopPane1.add(textField4j_Material);

			JTextField4j textField4j_BatchNumber = new JTextField4j();
			textField4j_BatchNumber.setEnabled(false);
			textField4j_BatchNumber.setBounds(154, 68, 139, 22);
			textField4j_BatchNumber.setText(labdat.getBatchNumber());
			jDesktopPane1.add(textField4j_BatchNumber);

			JTextField4j textField4j_User_ID = new JTextField4j();
			textField4j_User_ID.setEnabled(false);
			textField4j_User_ID.setBounds(154, 227, 152, 22);
			textField4j_User_ID.setText(labdat.getUserID());
			jDesktopPane1.add(textField4j_User_ID);

			JTextField4j textField4j_WorkstationID = new JTextField4j();
			textField4j_WorkstationID.setEditable(false);
			textField4j_WorkstationID.setEnabled(false);
			textField4j_WorkstationID.setBounds(448, 227, 208, 22);
			textField4j_WorkstationID.setText(labdat.getWorkstationID());
			jDesktopPane1.add(textField4j_WorkstationID);

			JDateControl dateControl_DateofManufacture = new JDateControl();
			dateControl_DateofManufacture.setFont(new Font("Arial", Font.PLAIN, 11));
			dateControl_DateofManufacture.setEnabled(false);
			dateControl_DateofManufacture.setBounds(154, 98, 128, 25);
			dateControl_DateofManufacture.setDate(labdat.getDateofManufacture());
			jDesktopPane1.add(dateControl_DateofManufacture);

			JDateControl dateControl_ExpiryDate = new JDateControl();
			dateControl_ExpiryDate.setFont(new Font("Arial", Font.PLAIN, 11));
			dateControl_ExpiryDate.setEnabled(false);
			dateControl_ExpiryDate.setBounds(448, 98, 128, 25);
			dateControl_ExpiryDate.setDate(labdat.getExpirtDate());
			jDesktopPane1.add(dateControl_ExpiryDate);

			JTextField4j textField4j_Description = new JTextField4j();
			textField4j_Description.setEnabled(false);
			textField4j_Description.setBounds(305, 38, 351, 22);
			if (mat.getMaterialProperties(labdat.getMaterial()))
			{
				textField4j_Description.setText(mat.getDescription());
			}
			jDesktopPane1.add(textField4j_Description);

			JLabel4j_std lbl_ResourceID = new JLabel4j_std();
			lbl_ResourceID.setText(lang.get("lbl_Resource_Key"));
			lbl_ResourceID.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_ResourceID.setBounds(306, 70, 125, 22);
			jDesktopPane1.add(lbl_ResourceID);

			JTextField4j textField4j_Resource = new JTextField4j();
			textField4j_Resource.setEnabled(false);
			textField4j_Resource.setBounds(448, 68, 208, 22);
			textField4j_Resource.setText(labdat.getRequiredResource());
			jDesktopPane1.add(textField4j_Resource);

			JLabel4j_std lbl_Status = new JLabel4j_std();
			lbl_Status.setText(lang.get("lbl_Process_Order_Status"));
			lbl_Status.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Status.setBounds(353, 10, 152, 22);
			jDesktopPane1.add(lbl_Status);

			JQuantityInput jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
			jDesktopPane1.add(jFormattedTextFieldQuantity);
			jFormattedTextFieldQuantity.setBounds(154, 198, 74, 22);
			jFormattedTextFieldQuantity.setEnabled(false);

			JTextField4j textField4j_Status = new JTextField4j();
			textField4j_Status.setEnabled(false);
			textField4j_Status.setBounds(517, 10, 139, 22);
			if (po.getProcessOrderProperties(labdat.getProcessOrder()))
			{
				textField4j_Status.setText(po.getStatus());
				jFormattedTextFieldQuantity.setText(po.getFullPalletQuantity());
			}
			jDesktopPane1.add(textField4j_Status);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(22, 262, 634, 113);
			jDesktopPane1.add(scrollPane);

			list = new JList4j<JDBListData>();
			list.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent arg0)
				{
					enableButtons();
				}
			});
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane.setViewportView(list);

			JButton4j JButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
			JButtonRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					populateList(selectedGroup);
				}
			});
			JButtonRefresh.setText(lang.get("btn_Refresh"));
			JButtonRefresh.setMnemonic('0');
			JButtonRefresh.setBounds(222, 387, 130, 32);
			jDesktopPane1.add(JButtonRefresh);

			JLabel4j_std label4j_Production_UOM = new JLabel4j_std((String) null);
			label4j_Production_UOM.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_Production_UOM.setBounds(12, 165, 125, 22);
			label4j_Production_UOM.setText(lang.get("lbl_Production_UOM"));
			jDesktopPane1.add(label4j_Production_UOM);

			JLabel4j_std label4j_std_Base_UOM = new JLabel4j_std((String) null);
			label4j_std_Base_UOM.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std_Base_UOM.setBounds(12, 132, 125, 22);
			label4j_std_Base_UOM.setText(lang.get("lbl_Base_UOM"));
			jDesktopPane1.add(label4j_std_Base_UOM);

			JTextField4j textField4j_Prod_EAN = new JTextField4j();
			textField4j_Prod_EAN.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Prod_EAN.setEnabled(false);
			textField4j_Prod_EAN.setText(labdat.getProdEAN());
			textField4j_Prod_EAN.setBounds(315, 165, 139, 22);
			jDesktopPane1.add(textField4j_Prod_EAN);

			JTextField4j textField4j_Base_EAN = new JTextField4j();
			textField4j_Base_EAN.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Base_EAN.setEnabled(false);
			textField4j_Base_EAN.setText(labdat.getBaseEAN());
			textField4j_Base_EAN.setBounds(315, 132, 139, 22);
			jDesktopPane1.add(textField4j_Base_EAN);

			JTextField4j textField4j_Prod_UOM = new JTextField4j();
			textField4j_Prod_UOM.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Prod_UOM.setEnabled(false);
			textField4j_Prod_UOM.setText(labdat.getProdUom());
			textField4j_Prod_UOM.setBounds(258, 165, 51, 22);
			jDesktopPane1.add(textField4j_Prod_UOM);

			JTextField4j textField4j_Prod_Variant = new JTextField4j();
			textField4j_Prod_Variant.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Prod_Variant.setEnabled(false);
			textField4j_Prod_Variant.setText(labdat.getProdVariant());
			textField4j_Prod_Variant.setBounds(472, 165, 51, 22);
			jDesktopPane1.add(textField4j_Prod_Variant);

			JTextField4j textField4j_Base_Variant = new JTextField4j();
			textField4j_Base_Variant.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Base_Variant.setEnabled(false);
			textField4j_Base_Variant.setText(labdat.getBaseVariant());
			textField4j_Base_Variant.setBounds(472, 132, 51, 22);
			jDesktopPane1.add(textField4j_Base_Variant);

			JTextField4j textField4j_Base_UOM = new JTextField4j();
			textField4j_Base_UOM.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Base_UOM.setEnabled(false);
			textField4j_Base_UOM.setText(labdat.getBaseUom());
			textField4j_Base_UOM.setBounds(258, 132, 51, 22);
			jDesktopPane1.add(textField4j_Base_UOM);

			JLabel4j_std label4j_std_Pallet_Quantity = new JLabel4j_std((String) null);
			label4j_std_Pallet_Quantity.setText(lang.get("lbl_Pallet_Quantity"));
			label4j_std_Pallet_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
			label4j_std_Pallet_Quantity.setBounds(12, 198, 125, 22);
			jDesktopPane1.add(label4j_std_Pallet_Quantity);

			JLabel4j_std label4j_std_1 = new JLabel4j_std((String) null);
			label4j_std_1.setText("x");
			label4j_std_1.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_1.setBounds(232, 165, 22, 22);
			jDesktopPane1.add(label4j_std_1);

			JLabel4j_std label4j_std_2 = new JLabel4j_std((String) null);
			label4j_std_2.setText("x");
			label4j_std_2.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_2.setBounds(232, 132, 22, 22);
			jDesktopPane1.add(label4j_std_2);

			JLabel4j_std label4j_std_3 = new JLabel4j_std((String) null);
			label4j_std_3.setText("/");
			label4j_std_3.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_3.setBounds(453, 165, 22, 22);
			jDesktopPane1.add(label4j_std_3);

			JLabel4j_std label4j_std_4 = new JLabel4j_std((String) null);
			label4j_std_4.setText("/");
			label4j_std_4.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_4.setBounds(453, 132, 22, 22);
			jDesktopPane1.add(label4j_std_4);

			JTextField4j textField4j_Prod_Quantity = new JTextField4j();
			textField4j_Prod_Quantity.setEnabled(false);
			textField4j_Prod_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
			textField4j_Prod_Quantity.setBounds(154, 165, 74, 22);
			textField4j_Prod_Quantity.setText(labdat.getProdQuantity().toString());
			jDesktopPane1.add(textField4j_Prod_Quantity);

			JTextField4j textField4j_Base_Quantity = new JTextField4j();
			textField4j_Base_Quantity.setEnabled(false);
			textField4j_Base_Quantity.setHorizontalAlignment(SwingConstants.TRAILING);
			textField4j_Base_Quantity.setBounds(154, 132, 74, 22);
			textField4j_Base_Quantity.setText(labdat.getBaseQuantity().toString());
			jDesktopPane1.add(textField4j_Base_Quantity);

			JTextField4j textField4j_Pallet_UOM = new JTextField4j();
			textField4j_Pallet_UOM.setEnabled(false);
			textField4j_Pallet_UOM.setHorizontalAlignment(SwingConstants.CENTER);
			textField4j_Pallet_UOM.setBounds(258, 198, 51, 22);
			textField4j_Pallet_UOM.setText(labdat.getProdUom());
			jDesktopPane1.add(textField4j_Pallet_UOM);

			JLabel4j_std label4j_std = new JLabel4j_std((String) null);
			label4j_std.setText("x");
			label4j_std.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std.setBounds(232, 198, 22, 22);
			jDesktopPane1.add(label4j_std);

			JLabel4j_std lbl_ReportID = new JLabel4j_std((String) null);
			lbl_ReportID.setText(lang.get("lbl_Report_ID"));
			lbl_ReportID.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_ReportID.setBounds(306, 198, 125, 22);
			jDesktopPane1.add(lbl_ReportID);

			JTextField4j textField4j_Report_ID = new JTextField4j();
			textField4j_Report_ID.setEditable(false);
			textField4j_Report_ID.setText(labdat.getModuleID());
			textField4j_Report_ID.setBounds(448, 196, 208, 22);
			jDesktopPane1.add(textField4j_Report_ID);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
