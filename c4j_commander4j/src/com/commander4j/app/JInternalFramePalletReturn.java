package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFramePalletReturn.java
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.TableRowSorter;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBQuery2;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBPalletReturnableTableModel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFramePalletReturn reverses a pallet issue. The operator enters or
 * looks up an SSCC, the table is then filled with the quantities which are
 * still available to be returned - one row per process order / location. The
 * quantity from the selected row is offered as the default and can be reduced
 * before the Return button is pressed.
 *
 * <p>
 * The pallet label can be reprinted once the return has been written, using the
 * same options row as JInternalFramePalletIssue.
 *
 * <p>
 * This is the single screen equivalent of the three page Return transaction in
 * the c4j_web_Issue RF terminal application.
 *
 * @see com.commander4j.db.JDBPallet JDBPallet
 * @see com.commander4j.app.JInternalFramePalletIssue JInternalFramePalletIssue
 * @see com.commander4j.tablemodel.JDBPalletReturnableTableModel
 *      JDBPalletReturnableTableModel
 */
public class JInternalFramePalletReturn extends javax.swing.JInternalFrame
{

	private static final long serialVersionUID = 1;

	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonLookupSSCC;
	private JButton4j jButtonReturn;

	private JPanel4j panelOrders = new JPanel4j();

	private JCheckBox4j checkBoxIncHeaderText = new JCheckBox4j();
	private JCheckBox4j checkBoxRePrint = new JCheckBox4j("");
	private JCheckBox4j jCheckBoxAutoPreview;

	private JComboBoxPODevices4j comboBoxPrintQueue;

	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);

	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusBar = new JLabel4j_status();
	private JLabel4j_std labelCopies = new JLabel4j_std();
	private JLabel4j_std labelPreview;
	private JLabel4j_std labelPrintQueue;
	private JLabel4j_std labelReturnQuantity;
	private JLabel4j_std labelReturnUOM;
	private JLabel4j_std labelSSCC;

	private JLabelPrint lab = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JQuantityInput jFormattedTextFieldReturnQuantity;
	private JSpinner4j jSpinnerCopies = new JSpinner4j();
	private SpinnerNumberModel copiesnumbermodel;

	private JTextField4j jTextFieldReturnUOM;
	private JTextField4j jTextFieldSSCC;

	private JTable4j jTable1;
	private JScrollPane4j jScrollPane1;

	private String defaultlabel = "";

	private BigDecimal zero = new BigDecimal(0);

	/**
	 * The statement behind the reprint - built per SSCC and released when the
	 * frame closes.
	 */
	private PreparedStatement labelStatement;

	/**
	 * The row the operator picked in the table. Everything
	 * JDBPallet.returnPallet needs is either here or in the input fields above
	 * - both are visible to the Return button.
	 */
	private JDBPallet.Returnable selectedReturnable = null;

	public JInternalFramePalletReturn()
	{
		super();

		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				closeLabelStatement();
			}
		});

		this.setTitle("Return Pallet");

		int copies = Integer.valueOf(ctrl.getKeyValueWithDefault("DEFAULT_LABELS_TO_PRINT", "2", "Default No of Labels to print"));
		copiesnumbermodel = new SpinnerNumberModel(copies, 1, 100, 1);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_RETURN"));

		jTextFieldSSCC.setText("");

		getPallet();

		setFocusPosition(jTextFieldSSCC);
	}

	private void initGUI()
	{
		try
		{
			// ===== Frame =====

			this.setPreferredSize(new java.awt.Dimension(519, 520));
			this.setBounds(0, 0, 519, 520);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(519, 520));
			jDesktopPane1.setLayout(null);
			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);

			// ===== Pallet row =====

			labelSSCC = new JLabel4j_std();
			labelSSCC.setText(lang.get("lbl_Pallet_SSCC"));
			labelSSCC.setBounds(82, 12, 88, 22);
			jDesktopPane1.add(labelSSCC);

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jTextFieldSSCC.setBounds(82, 33, 125, 22);
			jDesktopPane1.add(jTextFieldSSCC);
			jTextFieldSSCC.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					getPallet();
					validateReturn();
				}
			});

			jButtonLookupSSCC = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupSSCC.setBounds(207, 33, 22, 22);
			jDesktopPane1.add(jButtonLookupSSCC);
			jButtonLookupSSCC.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String p = LocalDateTime.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

					JLaunchLookup.setSearchValue("transaction_date", p);
					JLaunchLookup.setSearchOperator("transaction_date", ">");
					JLaunchLookup.setSearchValue("transaction_type", "ISSUE");
					JLaunchLookup.setSearchValue("transaction_subtype", "TO");

					if (JLaunchLookup.pallet_history())
					{
						jTextFieldSSCC.setText(JLaunchLookup.dlgResult);
						getPallet();
						validateReturn();
					}
				}
			});

			labelReturnQuantity = new JLabel4j_std();
			labelReturnQuantity.setText(lang.get("lbl_Return_Quantity"));
			labelReturnQuantity.setBounds(247, 11, 117, 22);
			jDesktopPane1.add(labelReturnQuantity);

			jFormattedTextFieldReturnQuantity = new JQuantityInput(new BigDecimal("0"));
			jFormattedTextFieldReturnQuantity.setVerifyInputWhenFocusTarget(false);
			jFormattedTextFieldReturnQuantity.setBounds(247, 33, 91, 22);
			jDesktopPane1.add(jFormattedTextFieldReturnQuantity);
			jFormattedTextFieldReturnQuantity.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					validateReturn();
				}
			});

			labelReturnUOM = new JLabel4j_std();
			labelReturnUOM.setText(lang.get("lbl_Pallet_UOM"));
			labelReturnUOM.setBounds(356, 11, 74, 22);
			jDesktopPane1.add(labelReturnUOM);

			jTextFieldReturnUOM = new JTextField4j();
			jTextFieldReturnUOM.setHorizontalAlignment(SwingConstants.CENTER);
			jTextFieldReturnUOM.setEnabled(false);
			jTextFieldReturnUOM.setBounds(356, 33, 42, 22);
			jDesktopPane1.add(jTextFieldReturnUOM);

			// ===== Panel : Returnable Orders =====

			panelOrders.setLayout(new BorderLayout(0, 0));
			panelOrders.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Returnable Orders", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			panelOrders.setBounds(12, 67, 495, 261);
			jDesktopPane1.add(panelOrders);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
			jScrollPane1.setBounds(0, 0, 100, 100);
			panelOrders.add(jScrollPane1);

			jTable1 = new JTable4j();
			jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
			jTable1.setToolTipText(lang.get("lbl_Table_Hint"));
			jScrollPane1.setViewportView(jTable1);

			// The selection model survives setModel() so this only needs adding
			// once.

			jTable1.getSelectionModel().addListSelectionListener(e -> {
				if (e.getValueIsAdjusting())
					return; // ignore the intermediate events

				defaultValuesFromTable();
				validateReturn();
			});

			// ===== Options row =====

			JLabel4j_std labelReprint = new JLabel4j_std();
			labelReprint.setText(lang.get("btn_Re_Print"));
			labelReprint.setHorizontalAlignment(SwingConstants.TRAILING);
			labelReprint.setBounds(1, 349, 74, 22);
			jDesktopPane1.add(labelReprint);
			checkBoxRePrint.setBounds(83, 349, 28, 22);
			jDesktopPane1.add(checkBoxRePrint);

			JLabel4j_std labelHeader = new JLabel4j_std();
			labelHeader.setText(lang.get("lbl_Label_Header_Text"));
			labelHeader.setHorizontalAlignment(SwingConstants.TRAILING);
			labelHeader.setBounds(106, 349, 107, 22);
			jDesktopPane1.add(labelHeader);

			checkBoxIncHeaderText.setSelected(true);
			checkBoxIncHeaderText.setBackground(Color.WHITE);
			checkBoxIncHeaderText.setBounds(216, 349, 21, 22);
			jDesktopPane1.add(checkBoxIncHeaderText);

			labelPreview = new JLabel4j_std();
			labelPreview.setText(lang.get("lbl_Preview"));
			labelPreview.setHorizontalTextPosition(SwingConstants.CENTER);
			labelPreview.setHorizontalAlignment(SwingConstants.TRAILING);
			labelPreview.setBounds(245, 349, 74, 22);
			jDesktopPane1.add(labelPreview);

			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setSelected(true);
			jCheckBoxAutoPreview.setBackground(Color.WHITE);
			jCheckBoxAutoPreview.setBounds(324, 349, 21, 22);
			jDesktopPane1.add(jCheckBoxAutoPreview);

			labelCopies.setText(lang.get("lbl_Labels_Per_SSCC"));
			labelCopies.setHorizontalAlignment(SwingConstants.RIGHT);
			labelCopies.setBounds(354, 349, 97, 22);
			jDesktopPane1.add(labelCopies);

			jSpinnerCopies.setInputVerifier(null);
			jSpinnerCopies.setModel(copiesnumbermodel);
			JSpinner4j.NumberEditor nec2 = new JSpinner4j.NumberEditor(jSpinnerCopies);
			jSpinnerCopies.setEditor(nec2);
			jSpinnerCopies.setBounds(460, 349, 39, 22);
			jDesktopPane1.add(jSpinnerCopies);

			// ===== Print Queue row =====

			labelPrintQueue = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			labelPrintQueue.setHorizontalAlignment(SwingConstants.TRAILING);
			labelPrintQueue.setBounds(1, 386, 100, 22);
			jDesktopPane1.add(labelPrintQueue);

			comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, "RPT_PALLET_LABEL", "");
			comboBoxPrintQueue.setBounds(108, 386, 391, 22);
			jDesktopPane1.add(comboBoxPrintQueue);

			// ===== Buttons =====

			jButtonReturn = new JButton4j(Common.icon_undo_16x16);
			jButtonReturn.setEnabled(false);
			jButtonReturn.setText(lang.get("btn_Return"));
			jButtonReturn.setMnemonic(lang.getMnemonicChar());
			jButtonReturn.setBounds(82, 420, 111, 32);
			jDesktopPane1.add(jButtonReturn);
			jButtonReturn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String sscc = jTextFieldSSCC.getText();

					if (validateReturn())
					{
						BigDecimal quantity = jFormattedTextFieldReturnQuantity.getQuantity();
						String order = selectedReturnable.processOrderID();
						String location = selectedReturnable.locationID();

						// Captured before the refresh below - getPallet()
						// rebuilds
						// the table, which clears the selection and with it
						// selectedReturnable.

						String labelModule = defaultlabel;
						JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
						boolean reprint = checkBoxRePrint.isSelected();
						boolean preview = jCheckBoxAutoPreview.isSelected();
						boolean incHeader = checkBoxIncHeaderText.isSelected();
						int copies = Integer.valueOf(jSpinnerCopies.getValue().toString());

						boolean returned = pal.returnPallet(sscc, order, quantity, location);

						if (returned)
						{
							String message = "Returned " + quantity.toPlainString() + " of " + sscc;

							jStatusBar.setText(message);
							jButtonReturn.setEnabled(false);

							getPallet();

							// The label carries the pallet quantity, so this
							// has
							// to follow the return being written.

							if (reprint)
							{
								if (pal.getQuantity().compareTo(BigDecimal.ZERO) == 1)
								{
									buildSQL1Record(sscc);
									JLaunchReport.runReport(labelModule, labelStatement, preview, pq, copies, incHeader);
								}
							}
						}
						else
						{
							String reason = JUtility.replaceNullStringwithBlank(pal.getErrorMessage());

							if (reason.equals(""))
							{
								reason = "Return of " + sscc + " failed.";
							}

							jStatusBar.setText(reason);
						}
					}
					else
					{
						// Only reachable if the pallet changed between the
						// button being enabled and the press.
						jStatusBar.setText(sscc + " is no longer valid for return.");
					}
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(196, 420, 111, 32);
			jDesktopPane1.add(jButtonHelp);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(309, 420, 111, 32);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					closeLabelStatement();
					dispose();
				}
			});

			// ===== Status Bar =====

			jStatusBar.setBounds(0, 464, 507, 21);
			jDesktopPane1.add(jStatusBar);

			setReturnableModel(new LinkedList<JDBPallet.Returnable>());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setFocusPosition(Component field)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				field.requestFocus();

				if (field.getClass().getName().equals("com.commander4j.gui.JQuantityInput"))
					((JQuantityInput) field).setCaretPosition(((JQuantityInput) field).getText().length());

				if (field.getClass().getName().equals("com.commander4j.gui.JTextField4j"))
					((JTextField4j) field).setCaretPosition(((JTextField4j) field).getText().length());
			}
		});
	}

	/**
	 * Read the pallet and, if it exists, list the quantities still available to
	 * be returned. Anything which is not 18 characters long is treated as a
	 * part typed SSCC and simply blanks the screen without an error.
	 */
	private void getPallet()
	{
		String sscc = JUtility.replaceNullStringwithBlank(jTextFieldSSCC.getText());
		String order = "";

		boolean valid = false;

		jStatusBar.setText("");

		if (sscc.length() == 18)
		{
			if (pal.getPalletProperties(sscc))
			{
				// Captured up front - pal is stateful and is reused below.

				order = pal.getProcessOrder();

				populateReturnableTable(sscc);

				comboBoxPrintQueue.refreshData("RPT_PALLET_LABEL", order);

				valid = true;
			}
			else
			{
				jStatusBar.setText(pal.getErrorMessage());

				emptyReturnableTable();
				clearReturnFields();
			}
		}
		else
		{
			emptyReturnableTable();
			clearReturnFields();
		}

		if (valid)
		{
			// The label belongs to the order which produced the pallet, not the
			// order it was issued to, so this is resolved from the pallet in
			// exactly the same way as JInternalFramePalletIssue.

			defaultlabel = lab.getPalletLabelReportName(order);

			if (mod.getModuleProperties(defaultlabel))
			{
				if (mod.getReportType().equals("Label"))
				{
					jCheckBoxAutoPreview.setSelected(false);
					jCheckBoxAutoPreview.setEnabled(false);

					jSpinnerCopies.setVisible(true);
					labelCopies.setVisible(true);
				}
				else
				{
					jCheckBoxAutoPreview.setSelected(true);
					jCheckBoxAutoPreview.setEnabled(true);

					jSpinnerCopies.setVisible(false);
					labelCopies.setVisible(false);
				}
			}
		}
		else
		{
			defaultlabel = "";
		}
	}

	/**
	 * getPallet() runs on every keystroke, so only rebuild the table when there
	 * is actually something in it - a scanner firing 18 characters would
	 * otherwise tear the model down 18 times over.
	 */
	private void emptyReturnableTable()
	{
		if (jTable1.getRowCount() > 0)
		{
			setReturnableModel(new LinkedList<JDBPallet.Returnable>());
		}
	}

	/**
	 * Fill the table from APP_PALLET_HISTORY - the ISSUE/TO rows netted against
	 * the RETURN/FROM rows, grouped by process order and location. Groups which
	 * have already been returned in full are dropped by JDBPallet, so an empty
	 * list means there is nothing to return.
	 */
	private void populateReturnableTable(String sscc)
	{
		LinkedList<JDBPallet.Returnable> list = pal.getReturnableBySSCC(sscc);

		setReturnableModel(list);

		if (list.isEmpty())
		{
			jStatusBar.setText("No orders with returnable quantity.");
		}
		else
		{
			jStatusBar.setText(list.size() + " order(s) available for return.");
		}
	}

	private void setReturnableModel(LinkedList<JDBPallet.Returnable> list)
	{
		JDBPalletReturnableTableModel model = new JDBPalletReturnableTableModel(Common.selectedHostID, Common.sessionID, list);

		TableRowSorter<JDBPalletReturnableTableModel> sorter = new TableRowSorter<JDBPalletReturnableTableModel>(model);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(model);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.getTableHeader().setPreferredSize(new Dimension(jScrollPane1.getWidth(), 25));

		jTable1.getColumnModel().getColumn(JDBPalletReturnableTableModel.returnable_order_col).setPreferredWidth(140);
		jTable1.getColumnModel().getColumn(JDBPalletReturnableTableModel.returnable_location_col).setPreferredWidth(160);
		jTable1.getColumnModel().getColumn(JDBPalletReturnableTableModel.returnable_quantity_col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBPalletReturnableTableModel.returnable_uom_col).setPreferredWidth(70);

		jScrollPane1.repaint();

		// setModel() clears the selection which fires the listener and empties
		// the Return panel for us.
	}

	/**
	 * Copy the selected table row into the Return panel. The returnable
	 * quantity is offered as the default and the operator can reduce it.
	 */
	private void defaultValuesFromTable()
	{
		int row = jTable1.getSelectedRow();

		selectedReturnable = null;

		if ((row != -1) && (jTable1.getModel() instanceof JDBPalletReturnableTableModel))
		{
			// convert if the table is sorted/filtered
			int modelRow = jTable1.convertRowIndexToModel(row);

			selectedReturnable = ((JDBPalletReturnableTableModel) jTable1.getModel()).getReturnable(modelRow);
		}

		if (selectedReturnable == null)
		{
			clearReturnFields();
		}
		else
		{
			jTextFieldReturnUOM.setText(selectedReturnable.uom());
			jFormattedTextFieldReturnQuantity.setQuantity(selectedReturnable.quantity());
		}
	}

	/**
	 * The Return button is only live once every check below has passed - the
	 * failing fields are flagged with inError() so the operator can see why.
	 *
	 * Note that the pallet and batch status are not checked here. The return
	 * destination is derived from the pallet history inside returnPallet, so
	 * that check can only be made by the database layer - its failure arrives
	 * on the status bar via getErrorMessage().
	 */
	private boolean validateReturn()
	{
		boolean result = false;

		boolean ssccValid = true;
		boolean orderValid = true;
		boolean quantityValid = true;

		int ssccLen = jTextFieldSSCC.getText().length();

		if (ssccLen == 18)
		{
			ssccValid = pal.getPalletProperties(jTextFieldSSCC.getText());

			if (ssccValid)
			{
				orderValid = (selectedReturnable != null);

				if (orderValid)
				{
					BigDecimal quantity = jFormattedTextFieldReturnQuantity.getQuantity();

					quantityValid = !(quantity.compareTo(zero) <= 0);

					if (quantityValid)
					{
						// Capped at the quantity shown on the selected row -
						// the
						// operator can only return what that order was issued.
						quantityValid = !(quantity.compareTo(selectedReturnable.quantity()) > 0);
					}
				}
			}
		}
		else
		{
			orderValid = false;
		}

		jTextFieldSSCC.inError(!ssccValid);
		jFormattedTextFieldReturnQuantity.inError(!quantityValid);

		result = (ssccLen == 18) && ssccValid && orderValid && quantityValid;

		jButtonReturn.setEnabled(result);

		return result;
	}

	private void clearReturnFields()
	{
		selectedReturnable = null;
		jTextFieldReturnUOM.setText("");
		jFormattedTextFieldReturnQuantity.setValue(0);
	}

	/**
	 * Build the single pallet resultset behind the label reprint. The label
	 * carries the quantity, so this must only be run once the return has been
	 * written.
	 */
	private void buildSQL1Record(String lsscc)
	{
		closeLabelStatement();

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

		labelStatement = query.getPreparedStatement();
	}

	/**
	 * Release the label statement. Safe to call more than once - the frame can
	 * be closed either with the Close button or the window close icon.
	 */
	private void closeLabelStatement()
	{
		if (labelStatement != null)
		{
			JDBQuery2.closeStatement(labelStatement);
			labelStatement = null;
		}
	}
}
