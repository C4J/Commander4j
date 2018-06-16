package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameProcessOrderLabel.java
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
import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameProcessOrderLabel is used for printing a label with a barcode representation of the Process Order Number. 
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameProcessOrderLabel.jpg" >
 * 
 * @see com.commander4j.db.JDBProcessOrder JDBProcesOrder 
 * @see com.commander4j.app.JInternalFrameProcessOrderAdmin JInternalFrameProcessOrderAdmin
 * @see com.commander4j.app.JInternalFrameProcessOrderProperties JInternalFrameProcessOrderProperties
 */
public class JInternalFrameProcessOrderLabel extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabel1;
	private String lprocessOrder;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBoxPrintQueue = new JComboBox4j<String>();
	private JSpinner jSpinnerQuantity = new JSpinner();
	private JCheckBox4j jCheckBoxAutoPreview;
	private JLabel4j_std label_4;
	private PreparedStatement listStatement;

	public JInternalFrameProcessOrderLabel()
	{
		super();

		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PROCESS_ORDER_LABEL"));
		
		JLabel4j_std label_1 = new JLabel4j_std();
		label_1.setBounds(228, 41, 182, 21);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setText(lang.get("lbl_Number_Of_Labels"));
     	jDesktopPane1.add(label_1);
	
     	jSpinnerQuantity.setEnabled(true);
		jSpinnerQuantity.setValue(1);
		JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerQuantity);
		ne.getTextField().setFont(Common.font_std); 
		jSpinnerQuantity.setEditor(ne);
		//jSpinnerQuantity.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(1), new Integer(1)));
		jSpinnerQuantity.setFont(Common.font_std);
		jSpinnerQuantity.setBounds(415, 41, 66, 21);
		jSpinnerQuantity.setValue(1);
		jDesktopPane1.add(jSpinnerQuantity);
		
		JLabel4j_std label_3 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
		label_3.setHorizontalAlignment(SwingConstants.TRAILING);
		label_3.setBounds(12, 78, 125, 21);
		jDesktopPane1.add(label_3);
		

		comboBoxPrintQueue.setSelectedIndex(-1);
		comboBoxPrintQueue.setBounds(155, 74, 471, 23);
		jDesktopPane1.add(comboBoxPrintQueue);
		
		jCheckBoxAutoPreview = new JCheckBox4j();
		jCheckBoxAutoPreview.setSelected(true);
		jCheckBoxAutoPreview.setEnabled(true);
		jCheckBoxAutoPreview.setBackground(Color.WHITE);
		jCheckBoxAutoPreview.setBounds(155, 41, 21, 21);
		jDesktopPane1.add(jCheckBoxAutoPreview);
		
		label_4 = new JLabel4j_std();
		label_4.setBounds(12, 41, 125, 21);
		label_4.setHorizontalTextPosition(SwingConstants.CENTER);
		label_4.setHorizontalAlignment(SwingConstants.TRAILING);
		label_4.setText(lang.get("lbl_Preview"));
		jDesktopPane1.add(label_4);		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				mod.setModuleId("RPT_PROCESS_ORDER_LABEL");
				mod.getModuleProperties();

				if (mod.getReportType().equals("Label"))
				{
					jCheckBoxAutoPreview.setSelected(false);
					jCheckBoxAutoPreview.setEnabled(false);
				}
				else
				{
				}
				populatePrinterList(JPrint.getDefaultPrinterQueueName());
			}
		});		

	}

	public JInternalFrameProcessOrderLabel(String processOrder)
	{
		this();
		lprocessOrder = processOrder;
		jTextFieldProcessOrder.setText(lprocessOrder);
		this.setTitle("Process Order Label");
		
	}

	private void populatePrinterList(String defaultitem) {
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempPrinterList = JPrint.getPrinterNames();

		for (int j = 0; j < tempPrinterList.size(); j++)
		{
			defComboBoxMod.addElement(tempPrinterList.get(j));
		}

		int sel = defComboBoxMod.getIndexOf(defaultitem);
		ComboBoxModel<String> jList1Model = defComboBoxMod;
		comboBoxPrintQueue.setModel( jList1Model);
		comboBoxPrintQueue.setSelectedIndex(sel);

		if (JPrint.getNumberofPrinters() == 0)
		{
			comboBoxPrintQueue.setEnabled(false);
		}
		else
		{
			comboBoxPrintQueue.setEnabled(true);
		}
	}	
	
	private void buildSQL1Record(String lprocessOrder) {

		JDBQuery.closeStatement(listStatement);
		
		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBProcessOrder.ViewResource");

		query.addText(temp);

		if (lprocessOrder.equals("") == false)
		{
			query.addParamtoSQL("process_order = ", lprocessOrder);
		}
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement =  query.getPreparedStatement();
	}	

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 663+Common.LFAdjustWidth, 197+Common.LFAdjustHeight);
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
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setEditable(false);
					jTextFieldProcessOrder.setEnabled(false);
					jTextFieldProcessOrder.setBounds(155, 10, 147, 21);
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print);
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String pq = comboBoxPrintQueue.getSelectedItem().toString();
							buildSQL1Record(jTextFieldProcessOrder.getText());
							JLaunchReport.runReport("RPT_PROCESS_ORDER_LABEL",listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerQuantity.getValue().toString()),false);
						}
					});
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(181, 111, 111, 32);

				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(294, 111, 111, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(407, 111, 111, 32);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							dispose();
						}
					});
				}

				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Process_Order"));
					jLabel1.setBounds(12, 10, 125, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
