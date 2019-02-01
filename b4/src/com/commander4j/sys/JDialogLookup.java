package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogLookup.java
 * 
 * Package Name : com.commander4j.sys
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBTable;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.Font;

public class JDialogLookup extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonSelect;
	private JButton4j jButtonCancel;
	private JLabel4j_std jLabel2;
	private JList4j<String> jListData;
	private JLabel4j_std jTextFieldHeading;
	private JScrollPane jScrollPane1;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTextField4j jTextFieldCriteria;
	private JComboBox4j<String> jComboBoxOrderBy;
	private JComboBox4j<String> jComboBoxCriteria;
	public static String dlg_title;
	public static boolean dlg_selected = true;
	public static String dlg_selected_var;
	private JButton4j jButtonHelp;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private static String dlg_criteria_field_name;
	private static int dlg_criteria_field_size;
	private static String dlg_criteria_field_type;
	public static String dlg_criteria_field_name_default;
	private static int dlg_criteria_field_name_default_pos;
	private static String dlg_orderBy_field_name;
	private static int dlg_orderBy_field_size;
	private static String dlg_orderBy_field_type;
	public static String dlg_orderBy_name_default;
	public static int dlg_orderBy_name_default_pos;
	public static boolean dlg_sort_descending = false;
	public static JDBTable dlg_table;
	public static String dlg_key_field_name;
	public static String dlg_key_field_type;
	public static int dlg_key_field_size;
	public static boolean hideInactive = false;
	private String errorMessage;
	private String dataResult;
	private JButton4j jButtonSearch;
	private JToggleButton jToggleButtonSequence;
	private JDialogLookup me;
	private Dimension screen;
	private Rectangle window;
	private Dimension startupSize;
	private PreparedStatement listStatement;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID,Common.sessionID);

	private void buildSQL() {
		
		try
		{
			listStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);

		query.clear();

		if (dlg_table.getTableName().toUpperCase().endsWith("SYS_USERS"))
		{
			query.addText("select user_id,user_comment from " + dlg_table.getTableName());
		}
		else
		{
		query.addText("select * from " + dlg_table.getTableName());
		}

		if (((String) jComboBoxCriteria.getSelectedItem()).equals("") == false)
		{
			if (jTextFieldCriteria.getText().equals("") == false)
			{
				String type = "";
				type = dlg_table.getColumnTypeForField((String) jComboBoxCriteria.getSelectedItem());

				if (type.equals("java.math.BigDecimal"))
				{
					query.addParamtoSQL((String) jComboBoxCriteria.getSelectedItem() + " = ", JUtility.stringToBigDecimal(jTextFieldCriteria.getText().toString()));
				}
				if (type.equals("java.lang.String"))
				{
					query.addParamtoSQL((String) jComboBoxCriteria.getSelectedItem() + " LIKE ", "%" + jTextFieldCriteria.getText() + "%");
				}
				if (type.equals("java.sql.Timestamp"))
				{
					query.addParamtoSQL((String) jComboBoxCriteria.getSelectedItem() + " LIKE ", "%" + jTextFieldCriteria.getText() + "%");
				}

			}
		}
		
		if ( dlg_table.getTableName().equals("APP_JOURNEY") )
		{
			query.addParamtoSQL("JOURNEY_REF <> ", "NO_JOURNEY");

			query.addParamtoSQL("STATUS = ", "Unassigned");
		}
		
		if (hideInactive)
		{
			query.addParamtoSQL("ACTIVE = ", "Y");
		}


		query.appendSort(jComboBoxOrderBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false,"none", 0);

		query.bindParams();

		listStatement = query.getPreparedStatement();
	}

	public Vector<JLaunchLookup> getData(PreparedStatement criteria) {

		ResultSet rs;
		Vector<JLaunchLookup> result = new Vector<JLaunchLookup>();

		if (Common.hostList.getHost(Common.selectedHostID).toString().equals(null))
		{
			result.addElement(new JLaunchLookup(dlg_key_field_name, dlg_criteria_field_name, dlg_orderBy_field_name));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				dlg_key_field_type = dlg_table.getColumnTypeForField(dlg_key_field_name);
				dlg_criteria_field_type = dlg_table.getColumnTypeForField(dlg_criteria_field_name);
				dlg_orderBy_field_type = dlg_table.getColumnTypeForField(dlg_orderBy_field_name);

				String temp = "";
				while (rs.next())
				{
					JLaunchLookup jl = new JLaunchLookup();
					jl.dlgKeyField = getFieldValueAsString(rs, dlg_key_field_name, dlg_key_field_type);
					jl.dlgCriteriaField = getFieldValueAsString(rs, dlg_criteria_field_name, dlg_criteria_field_type);
					temp = getFieldValueAsString(rs, dlg_orderBy_field_name, dlg_orderBy_field_type);
					jl.dlgOrderField = temp;
					result.addElement(jl);
				}
				rs.close();

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	private String getFieldValueAsString(ResultSet rs, String fieldname, String fieldtype) {
		String result = "";

		try
		{
			if (fieldtype.equals("java.lang.String"))
			{
				result = rs.getString(fieldname);
			}
			if (fieldtype.equals("java.sql.Timestamp"))
			{
				result = rs.getTimestamp(fieldname).toString().substring(0,16);
			}
			if (fieldtype.equals("java.math.BigDecimal"))
			{
				result = JUtility.bigDecimaltoString(rs.getBigDecimal(fieldname));
			}
		}
		catch (Exception ex)
		{
			result = fieldname;
		}

		return result;
	}

	private void setErrorMessage(String errormessage) {
		errorMessage = errormessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	private void populateList() {
		String heading = "";
		heading = JUtility.padString(dlg_key_field_name, true, dlg_key_field_size, " ") + " ";
		heading = heading + JUtility.padString(dlg_criteria_field_name, true, dlg_criteria_field_size, " ") + " ";
		heading = heading + dlg_orderBy_field_name;
		heading = heading.replace("_", " ");
		heading = JUtility.capitaliseAll(heading);
		jTextFieldHeading.setText(heading);

		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		Vector<JLaunchLookup> tempDataList = getData(listStatement);
		for (int j = 0; j < tempDataList.size(); j++)
		{
			dataResult = "";
			dataResult = JUtility.padString(tempDataList.get(j).dlgKeyField, true, dlg_key_field_size, " ") + " ";
			dataResult = dataResult + JUtility.padString(tempDataList.get(j).dlgCriteriaField, true, dlg_criteria_field_size, " ") + " ";
			if (j == 0)
			{
				int adjust = dlg_orderBy_field_size;
				if (adjust < dlg_orderBy_field_name.length())
				{
					adjust = dlg_orderBy_field_name.length();
				}
				dataResult = dataResult + JUtility.padString(JUtility.replaceNullObjectwithBlank(tempDataList.get(j).dlgOrderField), true, adjust, " ");
			}
			else
			{
				dataResult = dataResult + JUtility.replaceNullObjectwithBlank(tempDataList.get(j).dlgOrderField) + "  ";
			}

			defComboBoxMod.addElement(dataResult);
		}
		me.setSize(startupSize);
		ListModel<String> jList1Model = defComboBoxMod;
		jListData.setModel(jList1Model);
		jListData.setSelectedIndex(0);
		jListData.setCellRenderer(Common.renderer_list);

		me.setSize(startupSize);
	}

	private void search() {
		dlg_key_field_size = dlg_table.getColumnSizeForField(dlg_key_field_name);
		dlg_criteria_field_name = (String) jComboBoxCriteria.getSelectedItem();
		dlg_criteria_field_size = dlg_table.getColumnSizeForField(dlg_criteria_field_name);
		dlg_orderBy_field_name = (String) jComboBoxOrderBy.getSelectedItem();
		dlg_orderBy_field_size = dlg_table.getColumnSizeForField(dlg_orderBy_field_name);
		buildSQL();
		populateList();
		// growToAccomodate();
	}

	private void setSequence(boolean descending) {
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending);
		}
		else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
		}
	}

	public JDialogLookup(JFrame frame)
	{
		super(frame);
		getContentPane().setBackground(Common.color_edit_properties);
		
		dlg_selected = false;
		setTitle(dlg_title);
		// self.setFrameIcon();

		dlg_criteria_field_name_default_pos = 0;
		for (int x = 0; x < dlg_table.getFieldNames().size(); x++)
		{
			if (dlg_table.getColumnNameForField(x).toLowerCase().equals(dlg_criteria_field_name_default.toLowerCase()))
			{
				dlg_criteria_field_name_default_pos = x;
			}
		}

		dlg_orderBy_name_default_pos = 0;
		for (int x = 0; x < dlg_table.getFieldNames().size(); x++)
		{
			if (dlg_table.getColumnNameForField(x).toLowerCase().equals(dlg_orderBy_name_default.toLowerCase()))
			{
				dlg_orderBy_name_default_pos = x;
			}
		}

		initGUI();

		me = this;

		jTextFieldCriteria.setText(JLaunchLookup.dlgCriteriaDefault);

		screen = Toolkit.getDefaultToolkit().getScreenSize();
		window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		startupSize = me.getSize();

		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(471, 544));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				dlg_selected_var = "";
				dlg_selected = false;
				try
				{
					listStatement.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				dispose();
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				jScrollPane1.setSize(jDesktopPane1.getSize().width - 13, jDesktopPane1.getSize().height - 159);
				jTextFieldHeading.setSize(jDesktopPane1.getSize().width - 13, jTextFieldHeading.getSize().height);
				jScrollPane1.validate();

			}
		});

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_LOOKUP"));

		setSequence(dlg_sort_descending);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}"+dlg_table.getTableName()+" where 1=2"));
		query.bindParams();
		listStatement = query.getPreparedStatement();
		buildSQL();
		populateList();
		
		if (JLaunchLookup.dlgAutoExec)
		{
			search();
		}
	}

	private void initGUI() {
		try
		{
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.SOUTH);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(357, 518));
				jDesktopPane1.setBorder(BorderFactory.createTitledBorder(""));
				jDesktopPane1.setLayout(null);
				jDesktopPane1.setLocation(0, 0);
				{
					jButtonSelect = new JButton4j(Common.icon_ok);
					jDesktopPane1.add(jButtonSelect);
					jButtonSelect.setText(lang.get("btn_Select"));
					jButtonSelect.setBounds(119, 98, 113, 32);
					jButtonSelect.setMnemonic(java.awt.event.KeyEvent.VK_L);
					jButtonSelect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if (jListData.isSelectionEmpty() == false)
							{
								dlg_selected_var = ((String) jListData.getSelectedValue()).substring(0, dlg_table.getColumnSizeForField(dlg_key_field_name));
								dlg_selected = true;
								try
								{
									listStatement.close();
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
								dispose();
							}
						}
					});
				}
				{
					jButtonCancel = new JButton4j(Common.icon_cancel);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("web_Cancel"));
					jButtonCancel.setBounds(235, 98, 113, 32);
					jButtonCancel.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dlg_selected = false;
							dlg_selected_var = "";
							try
							{
								listStatement.close();
							}
							catch (SQLException e)
							{
								e.printStackTrace();
							}
							dispose();
						}
					});
				}
				{
					jTextFieldCriteria = new JTextField4j();
					jDesktopPane1.add(jTextFieldCriteria);
					jTextFieldCriteria.setBounds(70, 35, 385, 21);
				}
				{
					ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(dlg_table.getFieldNames());
					jComboBoxCriteria = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxCriteria);
					jComboBoxCriteria.setModel(jComboBox1Model);
					jComboBoxCriteria.setBounds(70, 7, 182, 23);
					jComboBoxCriteria.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jTextFieldCriteria.setText("");
						}
					});
					jComboBoxCriteria.setSelectedIndex(dlg_criteria_field_name_default_pos);
				}
				{
					ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(dlg_table.getFieldNames());
					jComboBoxOrderBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxOrderBy);
					jComboBoxOrderBy.setModel(jComboBox2Model);
					jComboBoxOrderBy.setBounds(70, 63, 182, 23);
					jComboBoxOrderBy.setSelectedIndex(dlg_orderBy_name_default_pos);
				}

				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText("Value :");
					jLabel1.setBounds(0, 35, 63, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText("Criteria :");
					jLabel2.setBounds(0, 7, 63, 21);
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Sort_By")+" :");
					jLabel3.setBounds(0, 63, 63, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(7, 154, 448, 350);
					jScrollPane1.getHorizontalScrollBar().addComponentListener(new ComponentAdapter() {
						public void componentResized(ComponentEvent evt) {
							if (jScrollPane1.getHorizontalScrollBar().isVisible() == true)
							{
								me.setSize(me.getSize().width + 50, (me.getSize().height));
								screen = Toolkit.getDefaultToolkit().getScreenSize();
								window = getBounds();
								setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
								me.validate();
							}
						}
					});
					{
						ListModel<String> jList1Model = new DefaultComboBoxModel<String>();

						jListData = new JList4j<String>();
						jScrollPane1.setViewportView(jListData);
						jListData.setModel(jList1Model);
						jListData.setCellRenderer(Common.renderer_list);
						jListData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListData.setFont(Common.font_list);
						jListData.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									jButtonSelect.doClick();
								}
							}
						});
					}
				}
				{
					jToggleButtonSequence = new JToggleButton(Common.icon_ascending);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(259, 63, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}
				{

					jButtonSearch = new JButton4j(Common.icon_search);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setBounds(3, 98, 113, 32);
					jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							search();
						}
					});
				}
				{
					jTextFieldHeading = new JLabel4j_std();
					jTextFieldHeading.setFont(new Font("Monospaced", Font.PLAIN, 11));
					jDesktopPane1.add(jTextFieldHeading);
					jTextFieldHeading.setBounds(7, 133, 448, 21);
					jTextFieldHeading.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldHeading.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(351, 98, 113, 32);
				}
			}
			this.setSize(471, 534);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
