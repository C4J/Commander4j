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
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBTable;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JDialogLookup extends javax.swing.JDialog
{
	public static int numberOfCriteria = 6;

	/**
	 * The dialog uses absolute positioning. The criteria rows start at
	 * criteriaRowTop and are criteriaRowPitch apart - everything below them is
	 * positioned relative to criteriaBottom() so that changing
	 * numberOfCriteria moves the rest of the dialog with it.
	 */
	private static final int criteriaRowTop = 22;
	private static final int criteriaRowPitch = 23;

	private static int criteriaBottom()
	{
		return criteriaRowTop + (numberOfCriteria * criteriaRowPitch);
	}

	private static int dialogHeight()
	{
		return criteriaBottom() + 545;
	}

	private static class CriteriaRow
	{
		private String fieldName = "";
		private String fieldType = "";
		private int fieldSize = 0;
		private int defaultPos = 0;
		private JComboBox4j<String> combo;
		private JComboBox4j<String> operator;
		private JTextField4j value;
	}

	/**
	 * Operators offered for a given column, most appropriate first. The first
	 * entry is the default and is chosen so that the generated SQL matches the
	 * behaviour of the dialog before operators were selectable - String columns
	 * default to LIKE, everything else to equals.
	 */
	private static String[] operatorsFor(String fieldname, String type)
	{
		if (fieldname.equalsIgnoreCase("ENABLED") || fieldname.equalsIgnoreCase("ACTIVE"))
		{
			return new String[] { "=", "<>" };
		}

		if (type.equals("java.lang.String"))
		{
			return new String[] { "LIKE", "=", "<>" };
		}

		if (type.equals("java.sql.Timestamp") || type.equals("java.time.LocalDateTime"))
		{
			return new String[] { "=", ">=", "<=", ">", "<" };
		}

		if (type.equals("java.math.BigDecimal") || type.equals("java.lang.Integer") || type.equals("java.lang.Long") || type.equals("java.lang.Double"))
		{
			return new String[] { "=", "<>", ">", ">=", "<", "<=" };
		}

		return new String[] { "=" };
	}

	private static String defaultOperatorFor(String fieldname, String type)
	{
		return operatorsFor(fieldname, type)[0];
	}

	private CriteriaRow[] criteriaRows = new CriteriaRow[numberOfCriteria];

	private Dimension startupSize;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JButton4j jButtonSelect;
	private JComboBox4j<String> jComboBoxOrderBy;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JDialogLookup me;
	private JLabel4j_title jLabel1;
	private JLabel4j_title jLabel2;
	private JLabel4j_title jLabelOperator;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jTextFieldHeading;
	private JList4j<String> jListData;
	private JScrollPane4j jScrollPane1;
	private Vector<String> criteriaFieldNames = new Vector<String>();
	private Vector<String> orderByFieldNames = new Vector<String>();

	private JToggleButton4j jToggleButtonSequence;
	private PreparedStatement listStatement;
	private String dataResult;
	private String errorMessage;
	private static String dlg_orderBy_field_name;
	private static String dlg_orderBy_field_type;
	private static final long serialVersionUID = 1;
	private static int dlg_orderBy_field_size;
	public static JDBTable dlg_table;
	public static String[] dlg_criteria_field_name_default = new String[numberOfCriteria];
	public static String dlg_key_field_name;
	public static String dlg_key_field_type;
	public static String dlg_orderBy_name_default;
	public static String dlg_selected_var;
	public static String dlg_title;
	public static boolean hideDisabled = false;
	public static boolean dlg_selected = true;
	public static boolean dlg_sort_descending = false;
	public static boolean hideInactive = false;
	public static int dlg_key_field_size;
	public static int dlg_orderBy_name_default_pos;
	private JLabel4j_std jLabel_Limit;
	private JSpinner4j jSpinnerLimit;
	private JCheckBox4j jCheckBoxLimit;
	private JLabel4j_status jStatusText;

	private void buildSQL()
	{

		JDBQuery2.closeStatement(listStatement);

		JDBQuery2 query = new JDBQuery2(Common.selectedHostID, Common.sessionID);

		if (dlg_table.getTableName().toUpperCase().endsWith("SYS_USERS"))
		{
			query.applyWhat("user_id,user_comment");
			query.applyFrom("{schema}"+dlg_table.getTableName());
		}
		else
		{
			query.applyWhat("*");
			query.applyFrom("{schema}"+dlg_table.getTableName());

		}

		boolean userFilteredEnabled = false;
		boolean userFilteredActive = false;

		for (int x = 0; x < numberOfCriteria; x++)
		{
			if (((String) criteriaRows[x].combo.getSelectedItem()).equals("") == false)
			{
				if (criteriaRows[x].value.getText().equals("") == false)
				{
					String type = "";
					String fieldname = (String) criteriaRows[x].combo.getSelectedItem();
					String stringvalue = "";
					int intvalue = 0;

					type = dlg_table.getColumnTypeForField((String) criteriaRows[x].combo.getSelectedItem());

					String operator = (String) criteriaRows[x].operator.getSelectedItem();

					if (operator == null || operator.equals(""))
					{
						operator = defaultOperatorFor(fieldname, type);
					}

					if (type.equals("java.math.BigDecimal"))
					{
						query.applyWhere(fieldname + " " + operator + " ", JUtility.stringToBigDecimal(criteriaRows[x].value.getText().toString()));
					}
					if (type.equals("java.lang.String"))
					{
						stringvalue = criteriaRows[x].value.getText();

						if (fieldname.equalsIgnoreCase("ENABLED") || fieldname.equalsIgnoreCase("ACTIVE"))
						{
							query.applyWhere(fieldname + " " + operator + " ", stringvalue.toUpperCase());

							if (fieldname.equalsIgnoreCase("ENABLED"))
							{
								userFilteredEnabled = true;
							}
							else
							{
								userFilteredActive = true;
							}
						}
						else
						{
							if (operator.equals("LIKE"))
							{
								query.applyWhere(fieldname + " LIKE ", "%" + stringvalue + "%");
							}
							else
							{
								query.applyWhere(fieldname + " " + operator + " ", stringvalue);
							}
						}
					}
					if (type.equals("java.sql.Timestamp") || type.equals("java.time.LocalDateTime"))
					{
						String datevalue = criteriaRows[x].value.getText().trim();
						Timestamp fromTimestamp = JUtility.getTimeStampFromISOString(datevalue);

						if (fromTimestamp != null)
						{
							// A date with no time and an equals operator means
							// anything on that day - any other operator uses the
							// timestamp as a boundary.
							if (operator.equals("=") && (datevalue.length() <= 10))
							{
								Calendar cal = Calendar.getInstance();
								cal.setTimeInMillis(fromTimestamp.getTime());
								cal.add(Calendar.DAY_OF_MONTH, 1);
								Timestamp toTimestamp = new Timestamp(cal.getTimeInMillis());

								query.applyWhere(fieldname + " >= ", fromTimestamp);
								query.applyWhere(fieldname + " < ", toTimestamp);
							}
							else
							{
								query.applyWhere(fieldname + " " + operator + " ", fromTimestamp);
							}
						}
					}

					if (type.equals("java.lang.Integer"))
					{
						try
						{
							intvalue = Integer.parseInt(criteriaRows[x].value.getText().trim());
							query.applyWhere(fieldname + " " + operator + " ", intvalue);
						}
						catch (NumberFormatException e)
						{
						}
					}

					if (type.equals("java.lang.Long"))
					{
						try
						{
							query.applyWhere(fieldname + " " + operator + " ", Long.parseLong(criteriaRows[x].value.getText().trim()));
						}
						catch (NumberFormatException e)
						{
						}
					}

					if (type.equals("java.lang.Double"))
					{
						try
						{
							query.applyWhere(fieldname + " " + operator + " ", Double.parseDouble(criteriaRows[x].value.getText().trim()));
						}
						catch (NumberFormatException e)
						{
						}
					}

				}
			}
		}

		if (dlg_table.getTableName().equals("APP_JOURNEY"))
		{
			query.applyWhere("JOURNEY_REF <> ", "NO_JOURNEY");

			query.applyWhere("STATUS = ", "Unassigned");
		}

		if (hideInactive && (userFilteredActive == false))
		{
			query.applyWhere("ACTIVE = ", "Y");
		}

		if (hideDisabled && (userFilteredEnabled == false))
		{
			query.applyWhere("ENABLED = ", "Y");
		}

		query.applySort(jComboBoxOrderBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());

		query.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());

		query.applySQL();

		listStatement = query.getPreparedStatement();
	}

	public Vector<JLaunchLookup> getData(PreparedStatement criteria)
	{

		ResultSet rs = null;
		Vector<JLaunchLookup> result = new Vector<JLaunchLookup>();

		if (Common.hostList.getHost(Common.selectedHostID).toString().equals(null))
		{
			String[] criteriaNames = new String[numberOfCriteria];
			for (int x = 0; x < numberOfCriteria; x++)
			{
				criteriaNames[x] = criteriaRows[x].fieldName;
			}
			result.addElement(new JLaunchLookup(dlg_key_field_name, criteriaNames, dlg_orderBy_field_name));
		}
		else
		{
			try
			{
				rs = criteria.executeQuery();

				dlg_key_field_type = dlg_table.getColumnTypeForField(dlg_key_field_name);

				dlg_orderBy_field_type = dlg_table.getColumnTypeForField(dlg_orderBy_field_name);

				for (int x = 0; x < numberOfCriteria; x++)
				{
					criteriaRows[x].fieldType = dlg_table.getColumnTypeForField(criteriaRows[x].fieldName);
				}

				while (rs.next())
				{
					JLaunchLookup jl = new JLaunchLookup();

					jl.dlgKeyField = getFieldValueAsString(rs, dlg_key_field_name, dlg_key_field_type);

					for (int x = 0; x < numberOfCriteria; x++)
					{
						if (criteriaRows[x].fieldName.equals("") == false)
						{
							jl.dlgSearchFieldname[x] = getFieldValueAsString(rs, criteriaRows[x].fieldName, criteriaRows[x].fieldType);
						}
					}

					jl.dlgOrderField = getFieldValueAsString(rs, dlg_orderBy_field_name, dlg_orderBy_field_type);

					result.addElement(jl);
				}

			}
			catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
			finally
			{
				if (rs != null)
				{
					try
					{
						rs.close();
					}
					catch (Exception e)
					{

					}
				}
			}
		}

		return result;
	}

	private String getFieldValueAsString(ResultSet rs, String fieldname, String fieldtype)
	{
		String result = "";

		try
		{
			if (fieldtype.equals("java.lang.String"))
			{
				result = rs.getString(fieldname);
			}
			else if (fieldtype.equals("java.sql.Timestamp") || fieldtype.equals("java.time.LocalDateTime"))
			{
				result = rs.getTimestamp(fieldname).toString().substring(0, 19);
			}
			else if (fieldtype.equals("java.sql.Date") || fieldtype.equals("java.time.LocalDate"))
			{
				result = rs.getDate(fieldname).toString();
			}
			else if (fieldtype.equals("java.math.BigDecimal"))
			{
				result = JUtility.bigDecimaltoString(rs.getBigDecimal(fieldname));
			}
			else if (fieldtype.equals("java.lang.Integer"))
			{
				result = ((Integer) rs.getInt(fieldname)).toString();
			}
			else
			{
				result = JUtility.replaceNullObjectwithBlank(rs.getString(fieldname));
			}
		}
		catch (Exception ex)
		{
			result = fieldname;
		}

		return result;
	}

	private void setErrorMessage(String errormessage)
	{
		errorMessage = errormessage;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void populateList()
	{
		boolean showOrderBy = dlg_orderBy_field_name.equalsIgnoreCase(dlg_key_field_name) == false;
		boolean[] showCriteria = new boolean[numberOfCriteria];

		for (int x = 0; x < numberOfCriteria; x++)
		{
			if (dlg_orderBy_field_name.equalsIgnoreCase(criteriaRows[x].fieldName))
			{
				showOrderBy = false;
			}

			showCriteria[x] = criteriaRows[x].fieldName.equals("") == false;

			if (criteriaRows[x].fieldName.equalsIgnoreCase(dlg_key_field_name))
			{
				showCriteria[x] = false;
			}

			for (int y = 0; y < x; y++)
			{
				if (criteriaRows[x].fieldName.equalsIgnoreCase(criteriaRows[y].fieldName))
				{
					showCriteria[x] = false;
				}
			}
		}

		String heading = "";
		heading = JUtility.padString(dlg_key_field_name, true, dlg_key_field_size, " ") + " ";
		for (int x = 0; x < numberOfCriteria; x++)
		{
			if (showCriteria[x])
			{
				heading = heading + JUtility.padString(criteriaRows[x].fieldName, true, criteriaRows[x].fieldSize, " ") + " ";
			}
		}
		if (showOrderBy)
		{
			heading = heading + dlg_orderBy_field_name;
		}
		heading = heading.replace("_", " ");
		heading = JUtility.capitaliseAll(heading);
		jTextFieldHeading.setText(heading);

		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		Vector<JLaunchLookup> tempDataList = getData(listStatement);
		for (int j = 0; j < tempDataList.size(); j++)
		{
			dataResult = JUtility.padString(tempDataList.get(j).dlgKeyField, true, dlg_key_field_size, " ") + " ";

			for (int x = 0; x < numberOfCriteria; x++)
			{
				if (showCriteria[x])
				{
					dataResult = dataResult + JUtility.padString(tempDataList.get(j).dlgSearchFieldname[x], true, criteriaRows[x].fieldSize, " ") + " ";
				}
			}

			if (showOrderBy)
			{
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
			}

			defComboBoxMod.addElement(dataResult);
		}
		int chrome = me.getSize().width - jScrollPane1.getSize().width;

		ListModel<String> jList1Model = defComboBoxMod;
		jListData.setModel(jList1Model);
		jListData.setSelectedIndex(0);
		jListData.setCellRenderer(Common.renderer_list);

		int requiredWidth = jListData.getPreferredSize().width + 30 + chrome;

		int newWidth = startupSize.width;

		if (requiredWidth > newWidth)
		{
			newWidth = requiredWidth;
		}

		GraphicsConfiguration gc = Common.mainForm.getGraphicsConfiguration();
		Rectangle screenBounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		int maxWidth = screenBounds.width - screenInsets.left - screenInsets.right;
		int maxHeight = screenBounds.height - screenInsets.top - screenInsets.bottom;

		if (newWidth > maxWidth)
		{
			newWidth = maxWidth;
		}

		int newHeight = startupSize.height;

		if (newHeight > maxHeight)
		{
			newHeight = maxHeight;
		}

		me.setSize(newWidth, newHeight);

		Dimension parentSize = Common.mainForm.getSize();
		Point parentPos = Common.mainForm.getLocation();
		Dimension formsize = getSize();
		int leftmargin = ((parentSize.width - formsize.width) / 2);
		int topmargin = ((parentSize.height - formsize.height) / 2);

		int newX = parentPos.x + leftmargin;
		int newY = parentPos.y + topmargin;

		if (newX + formsize.width > screenBounds.x + screenBounds.width - screenInsets.right)
		{
			newX = screenBounds.x + screenBounds.width - screenInsets.right - formsize.width;
		}
		if (newY + formsize.height > screenBounds.y + screenBounds.height - screenInsets.bottom)
		{
			newY = screenBounds.y + screenBounds.height - screenInsets.bottom - formsize.height;
		}
		if (newX < screenBounds.x + screenInsets.left)
		{
			newX = screenBounds.x + screenInsets.left;
		}
		if (newY < screenBounds.y + screenInsets.top)
		{
			newY = screenBounds.y + screenInsets.top;
		}

		setLocation(newX, newY);

		me.validate();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), tempDataList.size());
	}

	private void search()
	{
		dlg_key_field_size = dlg_table.getColumnSizeForField(dlg_key_field_name);

		if (dlg_key_field_name.length() > dlg_key_field_size)
		{
			dlg_key_field_size = dlg_key_field_name.length();
		}

		for (int x = 0; x < numberOfCriteria; x++)
		{
			criteriaRows[x].fieldName = (String) criteriaRows[x].combo.getSelectedItem();
			criteriaRows[x].fieldSize = dlg_table.getColumnSizeForField(criteriaRows[x].fieldName);

			if (criteriaRows[x].fieldName.length() > criteriaRows[x].fieldSize)
			{
				criteriaRows[x].fieldSize = criteriaRows[x].fieldName.length();
			}
		}
		dlg_orderBy_field_name = (String) jComboBoxOrderBy.getSelectedItem();
		dlg_orderBy_field_size = dlg_table.getColumnSizeForField(dlg_orderBy_field_name);
		buildSQL();
		populateList();
		// growToAccomodate();
	}

	private void setSequence(boolean descending)
	{
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}

	public JDialogLookup(JFrame frame)
	{
		super(frame);

		for (int x = 0; x < numberOfCriteria; x++)
		{
			criteriaRows[x] = new CriteriaRow();
		}

		dlg_selected = false;
		setTitle(dlg_title);
		// self.setFrameIcon();

		orderByFieldNames.addAll(dlg_table.getFieldNames());
		Collections.sort(orderByFieldNames, String.CASE_INSENSITIVE_ORDER);

		criteriaFieldNames.add("");
		criteriaFieldNames.addAll(orderByFieldNames);

		for (int y = 0; y < numberOfCriteria; y++)
		{
			if (dlg_criteria_field_name_default[y] == null)
			{
				dlg_criteria_field_name_default[y] = "";
			}

			criteriaRows[y].defaultPos = 0;
			for (int x = 0; x < criteriaFieldNames.size(); x++)
			{
				if (criteriaFieldNames.get(x).toLowerCase().equals(dlg_criteria_field_name_default[y].toLowerCase()))
				{
					criteriaRows[y].defaultPos = x;
					break;
				}
			}
		}

		dlg_orderBy_name_default_pos = 0;
		for (int x = 0; x < orderByFieldNames.size(); x++)
		{
			if (orderByFieldNames.get(x).toLowerCase().equals(dlg_orderBy_name_default.toLowerCase()))
			{
				dlg_orderBy_name_default_pos = x;
				break;
			}
		}

		initGUI();

		me = this;

		for (int x = 0; x < numberOfCriteria; x++)
		{
			criteriaRows[x].value.setText(JLaunchLookup.getSearchValue(dlg_criteria_field_name_default[x]));
		}

		Dimension screensize = Common.mainForm.getSize();
		Point parentPos = Common.mainForm.getLocation();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(parentPos.x + leftmargin, parentPos.y + topmargin);

		startupSize = me.getSize();

		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(546, dialogHeight()));
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				dlg_selected_var = "";
				dlg_selected = false;
				JDBQuery2.closeStatement(listStatement);
				dispose();
			}
		});
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt)
			{
				jScrollPane1.setSize(jDesktopPane1.getSize().width - 13, jDesktopPane1.getSize().height - jScrollPane1.getLocation().y - 33);
				jTextFieldHeading.setSize(jDesktopPane1.getSize().width - 13, jTextFieldHeading.getSize().height);
				jStatusText.setBounds(7, jDesktopPane1.getSize().height - 28, jDesktopPane1.getSize().width - 13, 21);
				jScrollPane1.validate();

			}
		});

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_LOOKUP"));

		setSequence(dlg_sort_descending);

		JDBQuery2 query = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		query.applyWhat("*");
		query.applyFrom("{schema}" + dlg_table.getTableName() + " WHERE 1=2");
		query.applySQL();
		listStatement = query.getPreparedStatement();
		search();
	}

	@Override
	public void dispose()
	{
		for (int x = 0; x < numberOfCriteria; x++)
		{
			dlg_criteria_field_name_default[x] = "";
		}

		JLaunchLookup.clearSearchDefaults();

		super.dispose();
	}

	/**
	 * Reload the operator combo for a criteria row from the column type of the
	 * currently selected field, honouring any default set by the calling form
	 * with JLaunchLookup.setSearchOperator.
	 */
	private void refreshOperators(int row)
	{
		String fieldname = (String) criteriaRows[row].combo.getSelectedItem();

		if (fieldname == null || fieldname.equals(""))
		{
			criteriaRows[row].operator.setModel(new DefaultComboBoxModel<String>(new String[] {}));
			criteriaRows[row].operator.setEnabled(false);
			return;
		}

		String type = dlg_table.getColumnTypeForField(fieldname);

		criteriaRows[row].operator.setModel(new DefaultComboBoxModel<String>(operatorsFor(fieldname, type)));
		criteriaRows[row].operator.setEnabled(true);

		// An operator the column does not support is ignored by setSelectedItem
		// leaving the type default in place.
		criteriaRows[row].operator.setSelectedItem(JLaunchLookup.getSearchOperator(fieldname));
	}

	private void initGUI()
	{
		try
		{

			jDesktopPane1 = new JDesktopPane4j();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			int rowsBottom = criteriaBottom();

			jDesktopPane1.setPreferredSize(new Dimension(357, dialogHeight()));
			jDesktopPane1.setBorder(null);
			jDesktopPane1.setLayout(null);
			jDesktopPane1.setLocation(0, 0);

			jButtonSelect = new JButton4j(Common.icon_ok_16x16);
			jDesktopPane1.add(jButtonSelect);
			jButtonSelect.setText(lang.get("btn_Select"));
			jButtonSelect.setBounds(123, rowsBottom + 40, 113, 32);
			jButtonSelect.setMnemonic(java.awt.event.KeyEvent.VK_L);
			jButtonSelect.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jListData.isSelectionEmpty() == false)
					{
						dlg_selected_var = ((String) jListData.getSelectedValue()).substring(0, dlg_table.getColumnSizeForField(dlg_key_field_name));
						dlg_selected = true;
						JDBQuery2.closeStatement(listStatement);
						dispose();
					}
				}
			});

			jButtonCancel = new JButton4j(Common.icon_cancel_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("web_Cancel"));
			jButtonCancel.setBounds(355, rowsBottom + 40, 113, 32);
			jButtonCancel.setMnemonic(java.awt.event.KeyEvent.VK_C);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dlg_selected = false;
					dlg_selected_var = "";
					JDBQuery2.closeStatement(listStatement);
					dispose();
				}
			});

			for (int x = 0; x < numberOfCriteria; x++)
			{
				final int xx = x;
				criteriaRows[xx].value = new JTextField4j();
				jDesktopPane1.add(criteriaRows[x].value);
				criteriaRows[xx].value.setBounds(267, criteriaRowTop + (x * criteriaRowPitch), 258, 22);

				// Built before the field combo - selecting a field fires the
				// listener below which populates this one.
				criteriaRows[xx].operator = new JComboBox4j<String>();
				jDesktopPane1.add(criteriaRows[x].operator);
				criteriaRows[xx].operator.setBounds(192, criteriaRowTop + (x * criteriaRowPitch), 70, 22);
				criteriaRows[xx].operator.setMaximumRowCount(10);

				ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(criteriaFieldNames);

				criteriaRows[xx].combo = new JComboBox4j<String>();
				jDesktopPane1.add(criteriaRows[x].combo);

				criteriaRows[xx].combo.setModel(jComboBox1Model);
				criteriaRows[xx].combo.setMaximumRowCount(20);
				criteriaRows[xx].combo.setBounds(5, criteriaRowTop + (x * criteriaRowPitch), 182, 22);
				criteriaRows[xx].combo.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						criteriaRows[xx].value.setText("");
						refreshOperators(xx);
					}
				});
				criteriaRows[xx].combo.setSelectedIndex(criteriaRows[xx].defaultPos);

				// setSelectedIndex only fires the listener when the selection
				// actually changes - a row defaulting to the blank field needs
				// the operator combo initialising explicitly.
				refreshOperators(xx);
			}

			ComboBoxModel<String> jComboBox2Model = new DefaultComboBoxModel<String>(orderByFieldNames);
			jComboBoxOrderBy = new JComboBox4j<String>();
			jComboBoxOrderBy.setMaximumRowCount(20);
			jDesktopPane1.add(jComboBoxOrderBy);
			jComboBoxOrderBy.setModel(jComboBox2Model);
			jComboBoxOrderBy.setBounds(75, rowsBottom + 12, 182, 22);
			jComboBoxOrderBy.setSelectedIndex(dlg_orderBy_name_default_pos);

			jLabel1 = new JLabel4j_title();
			jDesktopPane1.add(jLabel1);
			jLabel1.setText("Required Value");
			jLabel1.setBounds(267, 1, 113, 22);
			jLabel1.setHorizontalAlignment(SwingConstants.LEADING);

			jLabel2 = new JLabel4j_title();
			jDesktopPane1.add(jLabel2);
			jLabel2.setText("Field");
			jLabel2.setBounds(5, 1, 63, 22);
			jLabel2.setHorizontalAlignment(SwingConstants.LEADING);

			jLabelOperator = new JLabel4j_title();
			jDesktopPane1.add(jLabelOperator);
			jLabelOperator.setText("Operator");
			jLabelOperator.setBounds(192, 1, 70, 22);
			jLabelOperator.setHorizontalAlignment(SwingConstants.LEADING);

			jLabel3 = new JLabel4j_std();
			jDesktopPane1.add(jLabel3);
			jLabel3.setText(lang.get("lbl_Sort_By") + " :");
			jLabel3.setBounds(5, rowsBottom + 12, 63, 22);
			jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);

			jScrollPane1 = new JScrollPane4j(JScrollPane4j.List);
			jDesktopPane1.add(jScrollPane1);
			jScrollPane1.setBounds(7, rowsBottom + 98, 523, 372);

			jStatusText = new JLabel4j_status();
			jStatusText.setBounds(7, rowsBottom + 475, 523, 21);
			jDesktopPane1.add(jStatusText);

			ListModel<String> jList1Model = new DefaultComboBoxModel<String>();

			jListData = new JList4j<String>();
			jScrollPane1.setViewportView(jListData);
			jListData.setModel(jList1Model);
			jListData.setCellRenderer(Common.renderer_list);
			jListData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListData.setFont(Common.font_list);
			jListData.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2)
					{
						jButtonSelect.doClick();
					}
				}
			});

			jToggleButtonSequence = new JToggleButton4j(Common.icon_ascending_16x16);
			jDesktopPane1.add(jToggleButtonSequence);
			jToggleButtonSequence.setBounds(260, 172, 21, 22);
			jToggleButtonSequence.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					setSequence(jToggleButtonSequence.isSelected());
				}
			});

			
			jLabel_Limit = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Limit);
			jLabel_Limit.setText(lang.get("lbl_Limit"));
			jLabel_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Limit.setBounds(318, 172, 52, 22);
			
			SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
			jSpinnerIntModel.setMinimum(1);
			jSpinnerIntModel.setMaximum(1000);
			jSpinnerIntModel.setStepSize(1);
			jSpinnerLimit = new JSpinner4j();
			JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerLimit);
			jSpinnerLimit.setEditor(ne);
			jSpinnerLimit.setModel(jSpinnerIntModel);
			jSpinnerLimit.setBounds(400, 172, 68, 22);
			jSpinnerLimit.setValue(1000);
			jSpinnerLimit.getEditor().setSize(45, 21);
			jDesktopPane1.add(jSpinnerLimit);

			jCheckBoxLimit = new JCheckBox4j();
			jDesktopPane1.add(jCheckBoxLimit);

			jCheckBoxLimit.setBounds(375, 172, 22, 22);
			jCheckBoxLimit.setSelected(true);
			jCheckBoxLimit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					if (jCheckBoxLimit.isSelected())
					{
						jSpinnerLimit.setEnabled(true);
					}
					else
					{
						jSpinnerLimit.setEnabled(false);
					}
				}
			});
			
			jButtonSearch = new JButton4j(Common.icon_search_16x16);
			jDesktopPane1.add(jButtonSearch);
			jButtonSearch.setText(lang.get("btn_Search"));
			jButtonSearch.setBounds(7, rowsBottom + 40, 113, 32);
			jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
			jButtonSearch.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					search();
				}
			});

			jTextFieldHeading = new JLabel4j_std();
			jTextFieldHeading.setFont(new Font("Monospaced", Font.PLAIN, 11));
			jDesktopPane1.add(jTextFieldHeading);
			jTextFieldHeading.setBounds(7, rowsBottom + 76, 523, 22);
			jTextFieldHeading.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldHeading.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
			jButtonHelp.setBounds(239, rowsBottom + 40, 113, 32);

			this.setSize(545, dialogHeight());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
