package com.commander4j.compare;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JDialogCompareIgnore.java
 *
 * Package Name : com.commander4j.compare
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
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;

/**
 * Modal dialog that displays the full compare ignore list and allows the user
 * to remove entries.  All entries are always shown; rows that match the
 * current active comparison's DB type pair are highlighted in bold so they
 * are easy to identify.
 */
public class JDialogCompareIgnore extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1L;

	private final JCompareIgnoreList ignoreList;
	private final String activeDbType1;
	private final String activeDbType2;

	private JTable table;
	private IgnoreTableModel tableModel;
	private JButton4j btnRemove;
	private JButton4j btnClose;
	private JLabel4j_std labelContext;
	private JDesktopPane4j desktopPane;

	// Dialog dimensions
	private static final int W = 820;
	private static final int H = 420;

	public JDialogCompareIgnore(JFrame owner, JCompareIgnoreList ignoreList,
			String activeDbType1, String activeDbType2)
	{
		super(owner, "Compare Ignore List", true);
		this.ignoreList   = ignoreList;
		this.activeDbType1 = activeDbType1;
		this.activeDbType2 = activeDbType2;

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 820, 432);

		JPanel4j contentPane = new JPanel4j();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, W, H);
		desktopPane.setBackground(Common.color_app_window);
		desktopPane.setLayout(null);
		contentPane.add(desktopPane);

		initGUI();

		setLocationRelativeTo(owner);
	}

	private void initGUI()
	{
		// Context label
		String contextText;
		if (!activeDbType1.isEmpty() && !activeDbType2.isEmpty())
		{
			contextText = "Active comparison: " + activeDbType1 + " / " + activeDbType2
				+ "   (matching rows shown in bold)";
		}
		else
		{
			contextText = "No active comparison";
		}

		labelContext = new JLabel4j_std(contextText);
		labelContext.setFont(new Font("Arial", Font.PLAIN, 11));
		labelContext.setBounds(5, 8, W - 20, 20);
		desktopPane.add(labelContext);

		// Column header labels
		String[] headers = { "DB Type 1", "DB Type 2", "Table Name", "Field Name" };
		int[] colX        = {  5, 205, 405, 610 };
		int[] colW        = { 195, 195, 200, 195 };
		for (int i = 0; i < headers.length; i++)
		{
			JLabel4j_std lbl = new JLabel4j_std(headers[i]);
			lbl.setFont(new Font("Monospaced", Font.BOLD, 11));
			lbl.setHorizontalAlignment(SwingConstants.LEFT);
			lbl.setBounds(colX[i], 33, colW[i], 20);
			desktopPane.add(lbl);
		}

		// Table
		tableModel = new IgnoreTableModel(ignoreList.getEntries());
		table = new JTable(tableModel);
		table.setFont(Common.font_list);
		table.setBackground(Common.color_list_background);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(18);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Column widths
		int[] widths = { 190, 190, 195, 190 };
		for (int i = 0; i < widths.length; i++)
		{
			TableColumn tc = table.getColumnModel().getColumn(i);
			tc.setPreferredWidth(widths[i]);
			tc.setCellRenderer(new IgnoreCellRenderer(activeDbType1, activeDbType2));
		}

		JScrollPane4j scroll = new JScrollPane4j(JScrollPane4j.List);
		scroll.setViewportView(table);
		scroll.setBounds(5, 55, W - 20, H - 105);
		desktopPane.add(scroll);

		// Buttons
		btnRemove = new JButton4j("Remove Selected");
		btnRemove.setBounds(270, 375, 140, 25);
		desktopPane.add(btnRemove);
		btnRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				removeSelected();
			}
		});

		btnClose = new JButton4j("Close");
		btnClose.setBounds(420, 375, 90, 25);
		desktopPane.add(btnClose);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				dispose();
			}
		});
	}

	private void removeSelected()
	{
		int row = table.getSelectedRow();
		if (row < 0)
		{
			return;
		}
		JCompareIgnoreEntry entry = tableModel.getEntry(row);
		ignoreList.removeEntry(entry);
		tableModel.setEntries(ignoreList.getEntries());
		tableModel.fireTableDataChanged();
	}

	// -------------------------------------------------------------------------
	// Inner classes
	// -------------------------------------------------------------------------

	private static class IgnoreTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;
		private static final String[] COLUMNS = { "DB Type 1", "DB Type 2", "Table Name", "Field Name" };
		private List<JCompareIgnoreEntry> data;

		public IgnoreTableModel(List<JCompareIgnoreEntry> data)
		{
			this.data = data;
		}

		public void setEntries(List<JCompareIgnoreEntry> entries)
		{
			this.data = entries;
		}

		public JCompareIgnoreEntry getEntry(int row)
		{
			return data.get(row);
		}

		@Override public int getRowCount()    { return data.size(); }
		@Override public int getColumnCount() { return COLUMNS.length; }
		@Override public String getColumnName(int col) { return COLUMNS[col]; }
		@Override public boolean isCellEditable(int row, int col) { return false; }

		@Override
		public Object getValueAt(int row, int col)
		{
			JCompareIgnoreEntry e = data.get(row);
			switch (col)
			{
				case 0: return e.getDbType1();
				case 1: return e.getDbType2();
				case 2: return e.getTableName();
				case 3: return e.getFieldName();
				default: return "";
			}
		}
	}

	/**
	 * Renders rows that match the active DB type pair in bold so they stand out.
	 * The selected row is highlighted in yellow.
	 */
	private static class IgnoreCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		private final String activeDbType1;
		private final String activeDbType2;

		public IgnoreCellRenderer(String activeDbType1, String activeDbType2)
		{
			this.activeDbType1 = activeDbType1;
			this.activeDbType2 = activeDbType2;
		}

		@Override
		public Component getTableCellRendererComponent(JTable tbl, Object value,
				boolean isSelected, boolean hasFocus, int row, int col)
		{
			super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

			IgnoreTableModel model = (IgnoreTableModel) tbl.getModel();
			JCompareIgnoreEntry entry = model.getEntry(row);

			boolean isActiveRow = entry.matches(activeDbType1, activeDbType2,
				entry.getTableName(), entry.getFieldName());

			if (isSelected)
			{
				setBackground(Color.YELLOW);
				setForeground(Color.BLACK);
			}
			else
			{
				setBackground(tbl.getBackground());
				setForeground(tbl.getForeground());
			}

			setFont(isActiveRow
				? Common.font_list.deriveFont(Font.BOLD)
				: Common.font_list);

			setHorizontalAlignment(SwingConstants.LEFT);
			return this;
		}
	}
}
