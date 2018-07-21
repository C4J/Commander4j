package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletSplit.java
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
import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBControl;
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
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePalletSplit allows a user to split a pallet into 2 pallets. You
 * are prompted to enter a quantity of cases to remove from the current SSCC and
 * this is automatically added to a new SSCC. The program also allows you to
 * reprint labels for the old and new SSCC's
  * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFramePalletSplit.jpg" >
 * 
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.db.JDBPalletHistory JDBPalletHistory
 */
public class JInternalFramePalletSplit extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSplit;
	private JTextField4j jTextFieldSSCC;
	private JTextField4j jTextFieldNewSSCC;
	private JLabel4j_std labelSSCC;
	private JLabel4j_std labelNewSSCC;
	private JLabel4j_std labelSSCCNewQuantity;
	private String lsscc;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JLabelPrint lab = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBoxPrintQueue = new JComboBox4j<String>();
	private JSpinner jSpinnerQuantity = new JSpinner();
	private JCheckBox4j checkBoxIncHeaderText = new JCheckBox4j();
	private JCheckBox4j jCheckBoxAutoPreview;
	private JSpinner jSpinnerCopies = new JSpinner();
	private JLabel4j_std labelPreview;
	private PreparedStatement listStatement;
	private JQuantityInput jFormattedTextFieldQuantity;
	private JQuantityInput jFormattedTextFieldSplitQuantity;
	private JQuantityInput jFormattedTextFieldNewQuantity;
	private JLabel4j_std labelSSCCQuantity;
	private JLabel4j_std labelNewSSCCQuantity;
	private SpinnerNumberModel copiesnumbermodel;
	private JLabel4j_std jStatusBar = new JLabel4j_std();
	private JCheckBox4j checkBoxPrintOldSSCC = new JCheckBox4j("");
	private JCheckBox4j checkBoxPrintNewSSCC = new JCheckBox4j("");
	private JLabel4j_std labelCopies = new JLabel4j_std();
	private String defaultlabel = "";

	public JInternalFramePalletSplit()
	{
		super();

		this.setTitle("Split Pallet");

		int copies = Integer.valueOf(ctrl.getKeyValueWithDefault("DEFAULT_LABELS_TO_PRINT", "2", "Default No of Labels to print"));
		copiesnumbermodel = new SpinnerNumberModel(copies, 1, 100, 1);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_SPLIT"));

		lsscc = "";
		jTextFieldSSCC.setText(lsscc);

		refresh();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldSSCC.requestFocus();
				jTextFieldSSCC.setCaretPosition(jTextFieldSSCC.getText().length());

			}
		});
	}

	public JInternalFramePalletSplit(String sscc)
	{
		super();

		this.setTitle("Split Pallet");

		int copies = Integer.valueOf(ctrl.getKeyValueWithDefault("DEFAULT_LABELS_TO_PRINT", "2", "Default No of Labels to print"));
		copiesnumbermodel = new SpinnerNumberModel(copies, 1, 100, 1);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_SPLIT"));

		lsscc = sscc;
		jTextFieldSSCC.setText(lsscc);
		jTextFieldSSCC.setEnabled(false);

		refresh();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jFormattedTextFieldSplitQuantity.requestFocus();
				jFormattedTextFieldSplitQuantity.setCaretPosition(jFormattedTextFieldSplitQuantity.getText().length());

			}
		});

	}

	private void refresh()
	{
		String sscc = JUtility.replaceNullStringwithBlank(jTextFieldSSCC.getText());
		String order = "";

		if (sscc.length() == 18)
		{
			if (pal.getPalletProperties(jTextFieldSSCC.getText()))
			{
				jFormattedTextFieldQuantity.setValue(pal.getQuantity());
				jButtonSplit.setEnabled(true);
				jStatusBar.setText(jTextFieldSSCC.getText() + " retrieved.");
				order = pal.getProcessOrder();
			} else
			{
				jStatusBar.setText(pal.getErrorMessage());
				jFormattedTextFieldQuantity.setValue(0);
				jButtonSplit.setEnabled(false);
			}
		} else
		{
			jStatusBar.setText("");
			jButtonSplit.setEnabled(false);
		}

		defaultlabel = lab.getPalletLabelReportName(order);

		if (mod.getModuleProperties(defaultlabel))
		{
			if (mod.getReportType().equals("Label"))
			{
				jCheckBoxAutoPreview.setSelected(false);
				jCheckBoxAutoPreview.setEnabled(false);

				jSpinnerCopies.setVisible(true);
				labelCopies.setVisible(true);
			} else
			{
				jCheckBoxAutoPreview.setSelected(true);
				jCheckBoxAutoPreview.setEnabled(true);
				jSpinnerCopies.setVisible(false);
				labelCopies.setVisible(false);
			}
		}
		;

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
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 642, 304);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
				jDesktopPane1.setLayout(null);

				jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);

				jTextFieldSSCC.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyReleased(KeyEvent arg0)
					{
						refresh();
					}
				});
				jTextFieldSSCC.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						refresh();
					}
				});
				jDesktopPane1.add(jTextFieldSSCC);
				jTextFieldSSCC.setBounds(121, 24, 134, 21);

				jTextFieldNewSSCC = new JTextField4j();
				jDesktopPane1.add(jTextFieldNewSSCC);
				jTextFieldNewSSCC.setEditable(false);
				jTextFieldNewSSCC.setEnabled(false);
				jTextFieldNewSSCC.setBounds(121, 53, 134, 21);

				jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
				jDesktopPane1.add(jFormattedTextFieldQuantity);
				jFormattedTextFieldQuantity.setFont(Common.font_std);
				jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				jFormattedTextFieldQuantity.setBounds(464, 24, 91, 21);
				jFormattedTextFieldQuantity.setVerifyInputWhenFocusTarget(false);
				jFormattedTextFieldQuantity.setEnabled(false);

				jFormattedTextFieldNewQuantity = new JQuantityInput(new BigDecimal("0"));
				jDesktopPane1.add(jFormattedTextFieldNewQuantity);
				jFormattedTextFieldNewQuantity.setFont(Common.font_std);
				jFormattedTextFieldNewQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				jFormattedTextFieldNewQuantity.setBounds(464, 53, 91, 21);
				jFormattedTextFieldNewQuantity.setVerifyInputWhenFocusTarget(false);
				jFormattedTextFieldNewQuantity.setEnabled(false);

				jFormattedTextFieldSplitQuantity = new JQuantityInput(new BigDecimal("0"));
				jDesktopPane1.add(jFormattedTextFieldSplitQuantity);
				jFormattedTextFieldSplitQuantity.setFont(Common.font_std);
				jFormattedTextFieldSplitQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				jFormattedTextFieldSplitQuantity.setBounds(269, 87, 91, 21);
				jFormattedTextFieldSplitQuantity.setVerifyInputWhenFocusTarget(false);

				labelSSCCQuantity = new JLabel4j_std();
				jDesktopPane1.add(labelSSCCQuantity);
				labelSSCCQuantity.setText(lang.get("lbl_Pallet_Quantity"));
				labelSSCCQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				labelSSCCQuantity.setBounds(369, 24, 88, 21);

				labelSSCCNewQuantity = new JLabel4j_std();
				jDesktopPane1.add(labelSSCCNewQuantity);
				labelSSCCNewQuantity.setText(lang.get("lbl_Pallet_Quantity"));
				labelSSCCNewQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				labelSSCCNewQuantity.setBounds(369, 53, 88, 21);

				labelNewSSCCQuantity = new JLabel4j_std();
				jDesktopPane1.add(labelNewSSCCQuantity);
				labelNewSSCCQuantity.setText(lang.get("lbl_Required_Quantity"));
				labelNewSSCCQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
				labelNewSSCCQuantity.setBounds(137, 86, 125, 21);

				jButtonSplit = new JButton4j(Common.icon_split);
				jButtonSplit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String splitSSCC = pal.splitPallet(jTextFieldSSCC.getText(), new BigDecimal(jFormattedTextFieldSplitQuantity.getValue().toString()));
						if (splitSSCC.equals("") == false)
						{
							jStatusBar.setText("SSCC " + jTextFieldSSCC.getText() + " updated, SSCC " + splitSSCC + " created.");
							jTextFieldNewSSCC.setText(splitSSCC);
							jFormattedTextFieldNewQuantity.setValue(jFormattedTextFieldSplitQuantity.getValue());
							String pq = comboBoxPrintQueue.getSelectedItem().toString();
							if (checkBoxPrintOldSSCC.isSelected())
							{
								buildSQL1Record(jTextFieldSSCC.getText());
								JLaunchReport.runReport(defaultlabel, listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());
							}
							if (checkBoxPrintNewSSCC.isSelected())
							{
								buildSQL1Record(jTextFieldNewSSCC.getText());
								JLaunchReport.runReport(defaultlabel, listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());
							}
						} else
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(Common.mainForm, pal.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.WARNING_MESSAGE);
						}
					}
				});

				jDesktopPane1.add(jButtonSplit);
				jButtonSplit.setText(lang.get("btn_Split"));
				jButtonSplit.setMnemonic(lang.getMnemonicChar());
				jButtonSplit.setBounds(151, 205, 111, 32);

				jButtonHelp = new JButton4j(Common.icon_help);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setMnemonic(lang.getMnemonicChar());
				jButtonHelp.setBounds(265, 205, 111, 32);

				jButtonCancel = new JButton4j(Common.icon_close);
				jDesktopPane1.add(jButtonCancel);
				jButtonCancel.setText(lang.get("btn_Close"));
				jButtonCancel.setMnemonic(lang.getMnemonicChar());
				jButtonCancel.setBounds(378, 205, 111, 32);
				jButtonCancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						dispose();
					}
				});

				labelSSCC = new JLabel4j_std();
				jDesktopPane1.add(labelSSCC);
				labelSSCC.setText(lang.get("lbl_Source_SSCC"));
				labelSSCC.setBounds(6, 24, 103, 21);
				labelSSCC.setHorizontalAlignment(SwingConstants.TRAILING);

				labelNewSSCC = new JLabel4j_std();
				jDesktopPane1.add(labelNewSSCC);
				labelNewSSCC.setText(lang.get("lbl_Destination_SSCC"));
				labelNewSSCC.setBounds(18, 53, 91, 21);
				labelNewSSCC.setHorizontalAlignment(SwingConstants.TRAILING);

				JLabel4j_std labelHeader = new JLabel4j_std();
				labelHeader.setHorizontalAlignment(SwingConstants.TRAILING);
				labelHeader.setText(lang.get("lbl_Label_Header_Text"));
				labelHeader.setBounds(18, 112, 91, 21);
				jDesktopPane1.add(labelHeader);

				checkBoxIncHeaderText.setSelected(true);
				checkBoxIncHeaderText.setBackground(Color.WHITE);
				checkBoxIncHeaderText.setBounds(121, 112, 21, 21);
				jDesktopPane1.add(checkBoxIncHeaderText);

				JLabel4j_std labelQuantity = new JLabel4j_std();
				labelQuantity.setBounds(248, 112, 182, 21);
				labelQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
				labelQuantity.setText(lang.get("lbl_Number_of_SSCCs"));
				jDesktopPane1.add(labelQuantity);

				labelCopies.setHorizontalAlignment(SwingConstants.RIGHT);
				labelCopies.setBounds(248, 141, 182, 21);
				labelCopies.setText(lang.get("lbl_Labels_Per_SSCC"));
				jDesktopPane1.add(labelCopies);

				jSpinnerQuantity.setEnabled(false);
				jSpinnerQuantity.setModel(new SpinnerNumberModel(new Integer(1), null, null, new Integer(1)));
				jSpinnerQuantity.setFont(Common.font_std);
				jSpinnerQuantity.setBounds(437, 112, 39, 21);
				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerQuantity);
				ne.getTextField().setFont(Common.font_std);
				jSpinnerQuantity.setEditor(ne);
				jDesktopPane1.add(jSpinnerQuantity);

				jSpinnerCopies.setFont(Common.font_std);
				jSpinnerCopies.setBounds(437, 141, 39, 21);
				jSpinnerCopies.setInputVerifier(null);
				jSpinnerCopies.setModel(copiesnumbermodel);
				JSpinner.NumberEditor nec2 = new JSpinner.NumberEditor(jSpinnerCopies);
				nec2.getTextField().setFont(Common.font_std);
				jSpinnerCopies.setEditor(nec2);
				jDesktopPane1.add(jSpinnerCopies);

				JLabel4j_std label_3 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
				label_3.setHorizontalAlignment(SwingConstants.TRAILING);
				label_3.setBounds(18, 174, 91, 21);
				jDesktopPane1.add(label_3);

				comboBoxPrintQueue.setSelectedIndex(-1);
				comboBoxPrintQueue.setBounds(121, 170, 499, 23);
				jDesktopPane1.add(comboBoxPrintQueue);

				jCheckBoxAutoPreview = new JCheckBox4j();
				jCheckBoxAutoPreview.setToolTipText("Auto SSCC");
				jCheckBoxAutoPreview.setSelected(true);
				jCheckBoxAutoPreview.setEnabled(false);
				jCheckBoxAutoPreview.setBackground(Color.WHITE);
				jCheckBoxAutoPreview.setBounds(121, 141, 21, 21);
				jCheckBoxAutoPreview.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_PRODDEC_PREVIEW"));
				jDesktopPane1.add(jCheckBoxAutoPreview);

				labelPreview = new JLabel4j_std();
				labelPreview.setBounds(18, 141, 91, 21);
				labelPreview.setHorizontalTextPosition(SwingConstants.CENTER);
				labelPreview.setHorizontalAlignment(SwingConstants.TRAILING);
				labelPreview.setText(lang.get("lbl_Preview"));
				jDesktopPane1.add(labelPreview);

				jStatusBar.setForeground(Color.RED);
				jStatusBar.setBackground(Color.GRAY);
				jStatusBar.setBounds(0, 249, 632, 21);
				jStatusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				jDesktopPane1.add(jStatusBar);

				JPanel panel = new JPanel();
				panel.setBackground(Common.color_edit_properties);
				panel.setBorder(new TitledBorder(null, lang.get("btn_Print"), TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panel.setBounds(270, 6, 90, 75);
				jDesktopPane1.add(panel);
				panel.setLayout(null);

				checkBoxPrintOldSSCC.setSelected(true);
				checkBoxPrintOldSSCC.setBounds(32, 17, 28, 23);
				panel.add(checkBoxPrintOldSSCC);

				checkBoxPrintNewSSCC.setSelected(true);
				checkBoxPrintNewSSCC.setBounds(32, 46, 28, 23);
				panel.add(checkBoxPrintNewSSCC);

				populatePrinterList(JPrint.getDefaultPrinterQueueName());

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
