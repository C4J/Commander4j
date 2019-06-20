package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogPalletRePrintLabel.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
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
 * The JDialogPalletRePrintLabel class is a Dialog box loaded from the
 * JInternalFramePalletAdmin class which allows a user to reprint a pallet
 * label.
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogPalletRePrintLabel.jpg" >
 * 
 * @see com.commander4j.app.JInternalFramePalletAdmin JInternalFramePalletAdmin
 *
 */
public class JDialogPalletRePrintLabel extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JTextField4j jTextFieldSSCC;
	private JLabel4j_std jLabel1;
	private String lsscc;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JLabelPrint lab = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBoxPrintQueue = new JComboBox4j<String>();
	private JSpinner jSpinnerQuantity = new JSpinner();
	private JCheckBox4j checkBoxIncHeaderText = new JCheckBox4j();
	private JCheckBox4j jCheckBoxAutoPreview;
	private JSpinner jSpinnerCopies = new JSpinner();
	private JLabel4j_std label_4;
	private PreparedStatement listStatement;
	private String defaultlabel = "";

	public JDialogPalletRePrintLabel(JFrame frame, String sscc)
	{

		super(frame, "Pallet Label Reprint", ModalityType.DOCUMENT_MODAL);
		lsscc = sscc;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Pallet Label Reprint");
		this.setResizable(false);
		this.setSize(669 + Common.LFAdjustWidth, 222 + Common.LFAdjustHeight);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBounds(0, 200, 671, -200);
		jDesktopPane1.setBackground(Common.color_edit_properties);
		this.getContentPane().add(jDesktopPane1);
		jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
		jDesktopPane1.setLayout(null);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_LABEL_COPIES"));

		pal.getPalletProperties(lsscc);
		defaultlabel = lab.getPalletLabelReportName(pal.getProcessOrder());

		initGUI();

		jTextFieldSSCC.setText(lsscc);

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

	private void buildSQL1Record(String lsscc)
	{

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.selectWithExpiry");

		query.addText(temp);

		if (lsscc.equals("") == false)
		{
			query.addParamtoSQL("sscc = ", lsscc);
		}

		query.bindParams();
		query.applyRestriction(false, "none", 0);
		listStatement = query.getPreparedStatement();
	}

	private void initGUI()
	{
		try
		{

			{

				jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
				jTextFieldSSCC.setHorizontalAlignment(SwingConstants.CENTER);
				jDesktopPane1.add(jTextFieldSSCC);
				jTextFieldSSCC.setEditable(false);
				jTextFieldSSCC.setEnabled(false);
				jTextFieldSSCC.setBounds(155, 10, 137, 21);

				jButtonPrint = new JButton4j(Common.icon_print_16x16);
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						String pq = comboBoxPrintQueue.getSelectedItem().toString();
						buildSQL1Record(jTextFieldSSCC.getText());
						JLaunchReport.runReport(defaultlabel, listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());
						dispose();
					}
				});
				jDesktopPane1.add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.setBounds(181, 136, 111, 28);

				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setMnemonic(lang.getMnemonicChar());
				jButtonHelp.setBounds(294, 136, 111, 28);

				jButtonCancel = new JButton4j(Common.icon_close_16x16);
				jDesktopPane1.add(jButtonCancel);
				jButtonCancel.setText(lang.get("btn_Close"));
				jButtonCancel.setMnemonic(lang.getMnemonicChar());
				jButtonCancel.setBounds(407, 136, 111, 28);
				jButtonCancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						dispose();
					}
				});

				jLabel1 = new JLabel4j_std();
				jDesktopPane1.add(jLabel1);
				jLabel1.setText(lang.get("lbl_Pallet_SSCC"));
				jLabel1.setBounds(12, 10, 125, 21);
				jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);

				JLabel4j_std label = new JLabel4j_std();
				label.setHorizontalAlignment(SwingConstants.TRAILING);
				label.setText(lang.get("lbl_Label_Header_Text"));
				label.setBounds(12, 41, 125, 21);
				jDesktopPane1.add(label);

				checkBoxIncHeaderText.setSelected(true);
				checkBoxIncHeaderText.setBackground(Color.WHITE);
				checkBoxIncHeaderText.setBounds(155, 41, 21, 21);
				jDesktopPane1.add(checkBoxIncHeaderText);

				JLabel4j_std label_1 = new JLabel4j_std();
				label_1.setBounds(228, 41, 182, 21);
				label_1.setHorizontalAlignment(SwingConstants.RIGHT);
				label_1.setText(lang.get("lbl_Number_of_SSCCs"));
				jDesktopPane1.add(label_1);

				JLabel4j_std label_2 = new JLabel4j_std();
				label_2.setHorizontalAlignment(SwingConstants.RIGHT);
				label_2.setBounds(228, 70, 182, 21);
				label_2.setText(lang.get("lbl_Labels_Per_SSCC"));
				jDesktopPane1.add(label_2);
				jSpinnerQuantity.setEnabled(false);

				jSpinnerQuantity.setModel(new SpinnerNumberModel(new Integer(1), null, null, new Integer(1)));
				jSpinnerQuantity.setFont(Common.font_std);
				jSpinnerQuantity.setBounds(415, 41, 39, 21);
				jDesktopPane1.add(jSpinnerQuantity);

				jSpinnerCopies.setModel(new SpinnerNumberModel(new Integer(2), null, null, new Integer(1)));
				jSpinnerCopies.setFont(Common.font_std);
				jSpinnerCopies.setBounds(415, 70, 39, 21);
				jDesktopPane1.add(jSpinnerCopies);

				JLabel4j_std label_3 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
				label_3.setHorizontalAlignment(SwingConstants.TRAILING);
				label_3.setBounds(12, 103, 125, 21);
				jDesktopPane1.add(label_3);

				comboBoxPrintQueue.setSelectedIndex(-1);
				comboBoxPrintQueue.setBounds(155, 99, 471, 23);
				jDesktopPane1.add(comboBoxPrintQueue);

				jCheckBoxAutoPreview = new JCheckBox4j();
				jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
				jCheckBoxAutoPreview.setBackground(Color.WHITE);
				jCheckBoxAutoPreview.setBounds(155, 70, 21, 21);
				jDesktopPane1.add(jCheckBoxAutoPreview);

				label_4 = new JLabel4j_std();
				label_4.setBounds(12, 70, 125, 21);
				label_4.setHorizontalTextPosition(SwingConstants.CENTER);
				label_4.setHorizontalAlignment(SwingConstants.TRAILING);
				label_4.setText(lang.get("lbl_Preview"));
				jDesktopPane1.add(label_4);

				mod.setModuleId(defaultlabel);
				mod.getModuleProperties();

				if (mod.getReportType().equals("Label"))
				{
					jCheckBoxAutoPreview.setSelected(false);
					jCheckBoxAutoPreview.setEnabled(false);
					jSpinnerCopies.setEnabled(true);

				} else
				{
					jSpinnerCopies.setEnabled(false);
					jCheckBoxAutoPreview.setSelected(true);
					jCheckBoxAutoPreview.setEnabled(true);
				}
				populatePrinterList(JPrint.getDefaultPrinterQueueName());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
