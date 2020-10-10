package com.commander4j.app;

import java.awt.Color;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameLabelHistory.java
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
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLabelData;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBQuery2;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBLabelDataTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameLabelHistory is used view the records (print history) in the
 * APP_LABEL_DATA table.
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameLabelHistory.jpg" >
 * 
 * @see com.commander4j.db.JDBLabelData JDBLabelData
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 */
public class JInternalFrameLabelHistory extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private JButton4j jButtonRefresh;
	private static final long serialVersionUID = 1;
	private JScrollPane jScrollPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JTable4j jTable1;
	private String selectedLine;
	private String selectedGroup;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;
	private JCheckBox4j jCheckBoxLimit = new JCheckBox4j();
	private JSpinner jSpinnerLimit = new JSpinner();
	private JLabel4j_std jStatusText;

	public JInternalFrameLabelHistory(String line, String group)
	{
		super();
		selectedLine = line;
		selectedGroup = group;
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_LABELLER_HISTORY"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		populateList();

	}

	private PreparedStatement buildSQLr()
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		q2.applyWhat("*");
		q2.applyFrom("{schema}APP_LABEL_DATA");
		q2.applyWhere("LINE=", selectedLine);
		q2.applyWhere("LABEL_TYPE=", selectedGroup);
		q2.applySort("PRINT_DATE", true);
		if (jCheckBoxLimit.isSelected())
		{
			q2.applyRestriction(true, jSpinnerLimit.getValue());
		}
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private void buildSQL()
	{

		JDBQuery2.closeStatement(listStatement);

		listStatement = buildSQLr();
	}

	private void populateList()
	{

		JDBLabelData label = new JDBLabelData(Common.selectedHostID, Common.sessionID);
		buildSQL();
		JDBLabelDataTableModel labeltable = new JDBLabelDataTableModel(label.getLabelDataResultSet(listStatement));
		TableRowSorter<JDBLabelDataTableModel> sorter = new TableRowSorter<JDBLabelDataTableModel>(labeltable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(labeltable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);

		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.Print_Date_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.UserID_Col).setPreferredWidth(75);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.WorkstationID_Col).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.ProcessOrder_Col).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.Material_Col).setPreferredWidth(85);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.BatchNumber_Col).setPreferredWidth(90);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.Expiry_Col).setPreferredWidth(125);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.Line_Col).setPreferredWidth(110);
		jTable1.getColumnModel().getColumn(JDBLabelDataTableModel.LabelType_Col).setPreferredWidth(60);
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), jTable1.getRowCount());

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(669, 341));
			this.setBounds(0, 0, 995, 585);
			setVisible(true);
			this.setClosable(true);
			this.getContentPane().setLayout(null);
			this.setIconifiable(true);

			{
				jScrollPane1 = new JScrollPane();
				jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
				this.getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(0, 3, 978, 469);
				jScrollPane1.setFont(Common.font_std);
				{
					jTable1 = new JTable4j();

				}

			}
			{
				jButtonClose = new JButton4j(Common.icon_close_16x16);
				this.getContentPane().add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setBounds(527, 484, 115, 35);
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonCloseActionPerformed(evt);
					}
				});
			}
			{
				jButtonPrint = new JButton4j(Common.icon_report_16x16);
				this.getContentPane().add(jButtonPrint);
				jButtonPrint.setText(lang.get("btn_Print"));
				jButtonPrint.setBounds(294, 484, 115, 35);
				jButtonPrint.setMnemonic(lang.getMnemonicChar());
				jButtonPrint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						HashMap<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("p_line_no", selectedLine);
						buildSQL();
						JLaunchReport.runReport("RPT_LABEL_HISTORY", parameters, "", listStatement, "");
					}
				});
			}
			{
				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				this.getContentPane().add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setBounds(410, 484, 115, 35);
				jButtonHelp.setMnemonic(lang.getMnemonicChar());

			}
			{
				jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
				jButtonExcel.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						JDBControl control = new JDBControl(Common.selectedHostID, Common.sessionID);

						JExcel export = new JExcel();
						buildSQL();
						export.saveAs("label_history.xls", control.getControlDataResultSet(listStatement), Common.mainForm);
						populateList();
					}
				});
				jButtonExcel.setText(lang.get("btn_Excel"));
				jButtonExcel.setMnemonic(lang.getMnemonicChar());
				jButtonExcel.setBounds(178, 484, 115, 35);
				getContentPane().add(jButtonExcel);
			}
			{
				jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
				jButtonRefresh.addActionListener(new ActionListener()
				{
					public void actionPerformed(final ActionEvent e)
					{
						populateList();
					}
				});
				jButtonRefresh.setText(lang.get("btn_Refresh"));
				jButtonRefresh.setMnemonic(lang.getMnemonicChar());
				jButtonRefresh.setBounds(62, 484, 115, 35);
				getContentPane().add(jButtonRefresh);
			}

			{
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Limit"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(736, 494, 84, 21);
				getContentPane().add(label4j_std);
			}

			{
				jCheckBoxLimit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						populateList();
					}
				});
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(834, 494, 21, 21);
				getContentPane().add(jCheckBoxLimit);
			}

			{
				SpinnerModel model = new SpinnerNumberModel(100, 50, 10000, 50);
				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
				ne.getTextField().setFont(Common.font_std);

				jSpinnerLimit.setEditor(ne);
				jSpinnerLimit.setValue(100);
				jSpinnerLimit.setModel(model);
				jSpinnerLimit.setBounds(863, 494, 68, 21);
				getContentPane().add(jSpinnerLimit);

				ChangeListener listener = new ChangeListener()
				{
					@Override
					public void stateChanged(ChangeEvent e)
					{
						populateList();

					}
				};

				jSpinnerLimit.addChangeListener(listener);

			}
			{
				jStatusText = new JLabel4j_std();
				jStatusText.setForeground(new Color(255, 0, 0));
				jStatusText.setBackground(Color.GRAY);
				jStatusText.setBounds(0, 530, 985, 21);
				jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				getContentPane().add(jStatusText);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jButtonCloseActionPerformed(ActionEvent evt)
	{
		JDBQuery.closeStatement(listStatement);
		dispose();
	}

}
