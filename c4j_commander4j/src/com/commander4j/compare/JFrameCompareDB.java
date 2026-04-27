package com.commander4j.compare;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JFrameCompareDB.java
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
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.logging.log4j.Logger;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBField;
import com.commander4j.db.JDBStructure;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * The JFrameCompareDB class allows a user to compare the database schema of two
 * commander4j hosts side by side. Tables present in one database but not the other
 * are highlighted in red. Fields that differ in type or size between the two schemas
 * are also highlighted in red. The selected row is highlighted in yellow.
 */
public class JFrameCompareDB extends JFrame
{
	private static final long serialVersionUID = 1L;
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JFrameCompareDB.class);

	// Connection sessions
	private final String sessionHost1 = JUnique.getUniqueID();
	private final String sessionHost2 = JUnique.getUniqueID();
	private JHost hstHost1 = new JHost();
	private JHost hstHost2 = new JHost();
	private String hostID1 = "";
	private String hostID2 = "";

	// Aligned table data (parallel lists; "" means table absent on that side)
	private final List<String> alignedTablesHost1 = new ArrayList<>();
	private final List<String> alignedTablesHost2 = new ArrayList<>();

	// Aligned field data (parallel lists; ["","",""] means field absent on that side)
	private final List<String[]> leftFields = new ArrayList<>();
	private final List<String[]> rightFields = new ArrayList<>();
	private final List<boolean[]> fieldsDifferent = new ArrayList<>();

	// Per-row issue flags for the table-name lists (true = any field mismatch or table missing on one side)
	private final List<Boolean> tableHasIssues = new ArrayList<>();

	// Field caches populated during computeTableIssues(); keyed by lower-case table name
	private final Map<String, LinkedList<JDBField>> fieldCache1 = new HashMap<>();
	private final Map<String, LinkedList<JDBField>> fieldCache2 = new HashMap<>();

	// GUI Components
	private JComboBox4j<JHost> comboHost1;
	private JComboBox4j<JHost> comboHost2;
	private JButton4j btnCompare;
	private JButton4j btnRefresh;
	private JButton4j btnClose;
	private JList4j<String> listTables1;
	private JList4j<String> listTables2;
	private JTable tableFields1;
	private JTable tableFields2;
	private JScrollPane4j scrollTables1;
	private JScrollPane4j scrollTables2;
	private JScrollPane4j scrollFields1;
	private JScrollPane4j scrollFields2;
	private JLabel4j_status labelStatus;
	private JPanel4j contentPane;
	private JDesktopPane4j desktopPane = new JDesktopPane4j();

	// Flags to prevent recursive selection events
	private boolean syncingTableSelection = false;
	private boolean syncingFieldSelection = false;

	private static int heightAdjustment = 0;
	private static int widthAdjustment = 0;

	private final JCompareIgnoreList ignoreList = new JCompareIgnoreList();
	private String dbTypeHost1 = "";
	private String dbTypeHost2 = "";
	private String currentTable1 = "";
	private String currentTable2 = "";
	private JButton4j btnIgnoreList;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(() ->
		{
			try
			{
				JFrameCompareDB frame = new JFrameCompareDB();
				frame.setVisible(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	public JFrameCompareDB()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1280, 840);

		contentPane = new JPanel4j();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		desktopPane.setBounds(0, 0, 1280, 840);
		desktopPane.setBackground(Common.color_app_window);
		contentPane.add(desktopPane);

		Common.sessionID = JUnique.getUniqueID();
		Common.sd.setData(Common.sessionID, "silentExceptions", "No", true);
		JUtility.initLogging("");
		logger.debug("JFrameCompareDB starting...");

		widthAdjustment = JUtility.getOSWidthAdjustment();
		heightAdjustment = JUtility.getOSHeightAdjustment();

		initGUI();
		ignoreList.load();

		setIconImage(Common.imageIconloader.getImageIcon16x16(Common.image_osx_clone4j).getImage());

		GraphicsDevice gd = JUtility.getGraphicsDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		Rectangle screenBounds = gc.getBounds();

		setBounds(
			screenBounds.x + ((screenBounds.width - getWidth()) / 2),
			screenBounds.y + ((screenBounds.height - getHeight()) / 2),
			getWidth() + widthAdjustment,
			getHeight() + heightAdjustment
		);

		setVisible(true);
		loadHosts();
	}

	// -------------------------------------------------------------------------
	// Compare button state
	// -------------------------------------------------------------------------

	private void updateCompareButtonState()
	{
		JHost h1 = (JHost) comboHost1.getSelectedItem();
		JHost h2 = (JHost) comboHost2.getSelectedItem();
		boolean different = h1 != null && h2 != null && !h1.getSiteNumber().equals(h2.getSiteNumber());
		btnCompare.setEnabled(different);
		btnRefresh.setEnabled(different);
	}

	// -------------------------------------------------------------------------
	// Host loading
	// -------------------------------------------------------------------------

	private void loadHosts()
	{
		Common.hostList.clear();
		Common.hostList.loadHosts();
		LinkedList<JHost> hosts = Common.hostList.getHosts();

		DefaultComboBoxModel<JHost> modelHost1 = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<JHost> modelHost2 = new DefaultComboBoxModel<>();

		for (JHost h : hosts)
		{
			modelHost1.addElement(h);
			modelHost2.addElement(h);
		}

		comboHost1.setModel(modelHost1);
		comboHost2.setModel(modelHost2);

		if (hosts.size() > 1)
		{
			comboHost2.setSelectedIndex(1);
		}

		updateCompareButtonState();
	}

	// -------------------------------------------------------------------------
	// GUI initialisation
	// -------------------------------------------------------------------------

	private void initGUI()
	{
		try
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("Database Compare " + JVersion.getProgramVersion());
			desktopPane.setLayout(null);

			// ---- Host 1 label + combobox ----------------------------------------
			JLabel4j_std lblHost1 = new JLabel4j_std("Host 1:");
			lblHost1.setFont(new Font("Arial", Font.BOLD, 11));
			lblHost1.setBounds(5, 18, 52, 21);
			desktopPane.add(lblHost1);

			comboHost1 = new JComboBox4j<>();
			comboHost1.setBounds(60, 15, 270, 25);
			desktopPane.add(comboHost1);
			comboHost1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					updateCompareButtonState();
				}
			});

			// ---- Buttons --------------------------------------------------------
			btnCompare = new JButton4j("Compare");
			btnCompare.setBounds(389, 14, 100, 25);
			btnCompare.setEnabled(false);
			desktopPane.add(btnCompare);
			btnCompare.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					performComparison();
				}
			});

			btnRefresh = new JButton4j("Refresh");
			btnRefresh.setBounds(497, 14, 100, 25);
			btnRefresh.setEnabled(false);
			desktopPane.add(btnRefresh);
			btnRefresh.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					performRefresh();
				}
			});

			btnClose = new JButton4j("Close");
			btnClose.setBounds(605, 14, 90, 25);
			desktopPane.add(btnClose);
			btnClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					logger.debug("JFrameCompareDB closed");
					System.exit(0);
				}
			});

			btnIgnoreList = new JButton4j("Ignore List");
			btnIgnoreList.setBounds(703, 14, 100, 25);
			desktopPane.add(btnIgnoreList);
			btnIgnoreList.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					String saved1 = currentTable1;
					String saved2 = currentTable2;
					JDialogCompareIgnore dlg = new JDialogCompareIgnore(
						JFrameCompareDB.this, ignoreList, dbTypeHost1, dbTypeHost2);
					dlg.setVisible(true);
					// After the dialog closes, refresh the display if a comparison has been run
					if (!fieldCache1.isEmpty())
					{
						recomputeTableIssuesFromCache();
						currentTable1 = saved1;
						currentTable2 = saved2;
						if (!currentTable1.isEmpty() || !currentTable2.isEmpty())
						{
							loadFieldComparison(currentTable1, currentTable2);
						}
					}
				}
			});

			// ---- Host 2 label + combobox ----------------------------------------
			JLabel4j_std lblHost2 = new JLabel4j_std("Host 2:");
			lblHost2.setFont(new Font("Arial", Font.BOLD, 11));
			lblHost2.setBounds(870, 18, 52, 21);
			desktopPane.add(lblHost2);

			comboHost2 = new JComboBox4j<>();
			comboHost2.setBounds(925, 15, 270, 25);
			desktopPane.add(comboHost2);
			comboHost2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					updateCompareButtonState();
				}
			});

			// ---- Column headers (above each list) --------------------------------
			JLabel4j_std lblTableName1 = new JLabel4j_std("Table Name");
			lblTableName1.setFont(new Font("Monospaced", Font.BOLD, 11));
			lblTableName1.setHorizontalAlignment(SwingConstants.LEFT);
			lblTableName1.setBounds(5, 45, 250, 21);
			desktopPane.add(lblTableName1);

			JLabel4j_std lblFieldProps1 = new JLabel4j_std("Field Properties");
			lblFieldProps1.setFont(new Font("Monospaced", Font.BOLD, 11));
			lblFieldProps1.setHorizontalAlignment(SwingConstants.LEFT);
			lblFieldProps1.setBounds(260, 45, 365, 21);
			desktopPane.add(lblFieldProps1);

			JLabel4j_std lblTableName2 = new JLabel4j_std("Table Name");
			lblTableName2.setFont(new Font("Monospaced", Font.BOLD, 11));
			lblTableName2.setHorizontalAlignment(SwingConstants.LEFT);
			lblTableName2.setBounds(635, 45, 250, 21);
			desktopPane.add(lblTableName2);

			JLabel4j_std lblFieldProps2 = new JLabel4j_std("Field Properties");
			lblFieldProps2.setFont(new Font("Monospaced", Font.BOLD, 11));
			lblFieldProps2.setHorizontalAlignment(SwingConstants.LEFT);
			lblFieldProps2.setBounds(890, 45, 365, 21);
			desktopPane.add(lblFieldProps2);

			// ---- Left table name list -------------------------------------------
			listTables1 = new JList4j<>();
			listTables1.setFont(Common.font_list);
			listTables1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listTables1.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting() && !syncingTableSelection)
					{
						onTable1Selected();
					}
				}
			});

			scrollTables1 = new JScrollPane4j(JScrollPane4j.List);
			scrollTables1.setViewportView(listTables1);
			scrollTables1.setBounds(5, 68, 250, 697);
			desktopPane.add(scrollTables1);

			// ---- Left field table -----------------------------------------------
			tableFields1 = new JTable();
			tableFields1.setFont(Common.font_list);
			tableFields1.setBackground(Common.color_list_background);
			tableFields1.setFillsViewportHeight(true);
			tableFields1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableFields1.setRowHeight(18);
			tableFields1.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting() && !syncingFieldSelection)
					{
						syncingFieldSelection = true;
						int row = tableFields1.getSelectedRow();
						if (row >= 0 && row < tableFields2.getRowCount())
						{
							tableFields2.setRowSelectionInterval(row, row);
						}
						syncingFieldSelection = false;
					}
				}
			});

			tableFields1.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					if (e.isPopupTrigger()) showFieldPopup(tableFields1, e.getX(), e.getY());
				}
				public void mouseReleased(MouseEvent e)
				{
					if (e.isPopupTrigger()) showFieldPopup(tableFields1, e.getX(), e.getY());
				}
			});

			scrollFields1 = new JScrollPane4j(JScrollPane4j.List);
			scrollFields1.setViewportView(tableFields1);
			scrollFields1.setBounds(260, 68, 365, 697);
			desktopPane.add(scrollFields1);

			// ---- Right table name list ------------------------------------------
			listTables2 = new JList4j<>();
			listTables2.setFont(Common.font_list);
			listTables2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listTables2.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting() && !syncingTableSelection)
					{
						onTable2Selected();
					}
				}
			});

			scrollTables2 = new JScrollPane4j(JScrollPane4j.List);
			scrollTables2.setViewportView(listTables2);
			scrollTables2.setBounds(635, 68, 250, 697);
			desktopPane.add(scrollTables2);

			// ---- Right field table ----------------------------------------------
			tableFields2 = new JTable();
			tableFields2.setFont(Common.font_list);
			tableFields2.setBackground(Common.color_list_background);
			tableFields2.setFillsViewportHeight(true);
			tableFields2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableFields2.setRowHeight(18);
			tableFields2.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting() && !syncingFieldSelection)
					{
						syncingFieldSelection = true;
						int row = tableFields2.getSelectedRow();
						if (row >= 0 && row < tableFields1.getRowCount())
						{
							tableFields1.setRowSelectionInterval(row, row);
						}
						syncingFieldSelection = false;
					}
				}
			});

			tableFields2.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					if (e.isPopupTrigger()) showFieldPopup(tableFields2, e.getX(), e.getY());
				}
				public void mouseReleased(MouseEvent e)
				{
					if (e.isPopupTrigger()) showFieldPopup(tableFields2, e.getX(), e.getY());
				}
			});

			scrollFields2 = new JScrollPane4j(JScrollPane4j.List);
			scrollFields2.setViewportView(tableFields2);
			scrollFields2.setBounds(890, 68, 365, 697);
			desktopPane.add(scrollFields2);

			// ---- Status bar -----------------------------------------------------
			labelStatus = new JLabel4j_status("");
			labelStatus.setBounds(5, 768, 1265, 25);
			desktopPane.add(labelStatus);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// Comparison logic
	// -------------------------------------------------------------------------

	/**
	 * Connects to both selected hosts, fetches and aligns the table lists, and
	 * populates the table-name list models.  Returns true on success; false if
	 * either host cannot be reached (status bar is updated on failure).
	 * Does NOT select any table row — callers decide what to select afterwards.
	 */
	private boolean runComparison()
	{
		// Disconnect any existing sessions before starting fresh
		if (!hostID1.isEmpty())
		{
			hstHost1.disconnectAll();
			hostID1 = "";
		}
		if (!hostID2.isEmpty())
		{
			hstHost2.disconnectAll();
			hostID2 = "";
		}

		JHost h1 = (JHost) comboHost1.getSelectedItem();
		JHost h2 = (JHost) comboHost2.getSelectedItem();

		if (h1 == null || h2 == null)
		{
			setStatus("Please select both hosts.");
			return false;
		}

		hstHost1 = h1;
		hstHost2 = h2;
		hostID1 = hstHost1.getSiteNumber();
		hostID2 = hstHost2.getSiteNumber();
		dbTypeHost1 = normalizeDbType(hstHost1.getDatabaseParameters().getjdbcDriver());
		dbTypeHost2 = normalizeDbType(hstHost2.getDatabaseParameters().getjdbcDriver());

		if (hostID1.equals(hostID2))
		{
			setStatus("Please select two different hosts.");
			return false;
		}

		setStatus("Connecting to Host 1 (" + hstHost1.getSiteDescription() + ")...");
		if (!hstHost1.connect(sessionHost1, hostID1))
		{
			setStatus("Cannot connect to Host 1: " + hstHost1.getSiteDescription());
			hostID1 = "";
			return false;
		}

		setStatus("Connecting to Host 2 (" + hstHost2.getSiteDescription() + ")...");
		if (!hstHost2.connect(sessionHost2, hostID2))
		{
			setStatus("Cannot connect to Host 2: " + hstHost2.getSiteDescription());
			hstHost1.disconnectAll();
			hostID1 = "";
			hostID2 = "";
			return false;
		}

		setStatus("Retrieving table names from Host 1...");
		JDBStructure struc1 = new JDBStructure(hostID1, sessionHost1);
		LinkedList<String> tables1 = struc1.getTableNames();

		setStatus("Retrieving table names from Host 2...");
		JDBStructure struc2 = new JDBStructure(hostID2, sessionHost2);
		LinkedList<String> tables2 = struc2.getTableNames();

		setStatus("Aligning table lists...");
		alignedTablesHost1.clear();
		alignedTablesHost2.clear();
		buildAlignedTables(tables1, tables2);

		computeTableIssues();

		// Populate list models (show a space for absent entries so row height is preserved)
		DefaultListModel<String> model1 = new DefaultListModel<>();
		DefaultListModel<String> model2 = new DefaultListModel<>();
		for (String t : alignedTablesHost1)
		{
			model1.addElement(t.isEmpty() ? " " : t);
		}
		for (String t : alignedTablesHost2)
		{
			model2.addElement(t.isEmpty() ? " " : t);
		}

		listTables1.setModel(model1);
		listTables2.setModel(model2);

		// Apply custom cell renderers that colour tables with issues red
		listTables1.setCellRenderer(new TableListRenderer(alignedTablesHost1, tableHasIssues));
		listTables2.setCellRenderer(new TableListRenderer(alignedTablesHost2, tableHasIssues));

		// Clear any previously displayed field data
		clearFieldTables();

		// All field data is now cached — connections are no longer needed
		hstHost1.disconnectAll();
		hstHost2.disconnectAll();

		return true;
	}

	private void performComparison()
	{
		if (!runComparison())
		{
			return;
		}

		// Auto-select the first table so field details are shown immediately
		if (!alignedTablesHost1.isEmpty())
		{
			listTables1.setSelectedIndex(0);
			listTables1.ensureIndexIsVisible(0);
		}
	}

	private void performRefresh()
	{
		// Save the currently selected table name (from the left aligned list)
		String savedTable = "";
		int tableIdx = listTables1.getSelectedIndex();
		if (tableIdx >= 0 && tableIdx < alignedTablesHost1.size())
		{
			savedTable = alignedTablesHost1.get(tableIdx);
		}

		// Save the currently selected field name (prefer left side, fall back to right)
		String savedField = "";
		int fieldRow = tableFields1.getSelectedRow();
		if (fieldRow >= 0 && fieldRow < leftFields.size())
		{
			savedField = leftFields.get(fieldRow)[0];
			if (savedField.isEmpty())
			{
				savedField = rightFields.get(fieldRow)[0];
			}
		}

		if (!runComparison())
		{
			return;
		}

		// Restore table selection — fall back to row 0 if the table no longer exists
		boolean tableFound = false;
		if (!savedTable.isEmpty())
		{
			int newIdx = alignedTablesHost1.indexOf(savedTable);
			if (newIdx >= 0)
			{
				listTables1.setSelectedIndex(newIdx);
				listTables1.ensureIndexIsVisible(newIdx);
				tableFound = true;
			}
		}
		if (!tableFound && !alignedTablesHost1.isEmpty())
		{
			listTables1.setSelectedIndex(0);
			listTables1.ensureIndexIsVisible(0);
		}

		// Restore field selection — leftFields/rightFields are now populated for the
		// restored table; fall back to row 0 if the field no longer exists
		if (!savedField.isEmpty())
		{
			boolean fieldFound = false;
			for (int i = 0; i < leftFields.size(); i++)
			{
				String lName = leftFields.get(i)[0];
				String rName = rightFields.get(i)[0];
				if (lName.equalsIgnoreCase(savedField) || rName.equalsIgnoreCase(savedField))
				{
					tableFields1.setRowSelectionInterval(i, i);
					tableFields2.setRowSelectionInterval(i, i);
					tableFields1.scrollRectToVisible(tableFields1.getCellRect(i, 0, true));
					tableFields2.scrollRectToVisible(tableFields2.getCellRect(i, 0, true));
					fieldFound = true;
					break;
				}
			}
			if (!fieldFound && tableFields1.getRowCount() > 0)
			{
				tableFields1.setRowSelectionInterval(0, 0);
				tableFields2.setRowSelectionInterval(0, 0);
			}
		}
	}

	// -------------------------------------------------------------------------
	// Right-click popup for field tables
	// -------------------------------------------------------------------------

	private void showFieldPopup(JTable sourceTable, int x, int y)
	{
		if (dbTypeHost1.isEmpty() || leftFields.isEmpty())
		{
			return;
		}
		int row = sourceTable.rowAtPoint(new Point(x, y));
		if (row < 0 || row >= leftFields.size())
		{
			return;
		}

		// Select the clicked row and sync the opposite table
		sourceTable.setRowSelectionInterval(row, row);
		if (sourceTable == tableFields1)
		{
			if (row < tableFields2.getRowCount()) tableFields2.setRowSelectionInterval(row, row);
		}
		else
		{
			if (row < tableFields1.getRowCount()) tableFields1.setRowSelectionInterval(row, row);
		}

		String fieldName = leftFields.get(row)[0];
		if (fieldName.isEmpty()) fieldName = rightFields.get(row)[0];
		if (fieldName.isEmpty()) return;

		final String fName = fieldName;
		final String tName = currentTable1.isEmpty() ? currentTable2 : currentTable1;
		boolean alreadyIgnored = ignoreList.isIgnored(dbTypeHost1, dbTypeHost2, tName, fName);

		JPopupMenu popup = new JPopupMenu();

		JMenuItem itemAdd = new JMenuItem("Add to Ignore List");
		itemAdd.setEnabled(!alreadyIgnored);
		itemAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ignoreList.addEntry(dbTypeHost1, dbTypeHost2, tName, fName);
				recomputeTableIssuesFromCache();
				loadFieldComparison(currentTable1, currentTable2);
			}
		});

		JMenuItem itemRemove = new JMenuItem("Remove from Ignore List");
		itemRemove.setEnabled(alreadyIgnored);
		itemRemove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (JCompareIgnoreEntry entry : ignoreList.getEntries())
				{
					if (entry.matches(dbTypeHost1, dbTypeHost2, tName, fName))
					{
						ignoreList.removeEntry(entry);
						break;
					}
				}
				recomputeTableIssuesFromCache();
				loadFieldComparison(currentTable1, currentTable2);
			}
		});

		popup.add(itemAdd);
		popup.add(itemRemove);
		popup.show(sourceTable, x, y);
	}

	// -------------------------------------------------------------------------
	// Table list selection handlers
	// -------------------------------------------------------------------------

	private void onTable1Selected()
	{
		if (alignedTablesHost1.isEmpty())
		{
			return;
		}
		int idx = listTables1.getSelectedIndex();
		if (idx < 0 || idx >= alignedTablesHost1.size())
		{
			return;
		}

		String table1 = alignedTablesHost1.get(idx);
		String table2 = alignedTablesHost2.get(idx);

		// Synchronise selection in the right list and ensure it is visible
		syncingTableSelection = true;
		if (!table2.isEmpty())
		{
			listTables2.setSelectedIndex(idx);
		}
		else
		{
			listTables2.clearSelection();
		}
		listTables2.ensureIndexIsVisible(idx);
		syncingTableSelection = false;

		loadFieldComparison(table1, table2);
	}

	private void onTable2Selected()
	{
		if (alignedTablesHost2.isEmpty())
		{
			return;
		}
		int idx = listTables2.getSelectedIndex();
		if (idx < 0 || idx >= alignedTablesHost2.size())
		{
			return;
		}

		String table1 = alignedTablesHost1.get(idx);
		String table2 = alignedTablesHost2.get(idx);

		// Synchronise selection in the left list and ensure it is visible
		syncingTableSelection = true;
		if (!table1.isEmpty())
		{
			listTables1.setSelectedIndex(idx);
		}
		else
		{
			listTables1.clearSelection();
		}
		listTables1.ensureIndexIsVisible(idx);
		syncingTableSelection = false;

		loadFieldComparison(table1, table2);
	}

	// -------------------------------------------------------------------------
	// Field loading and comparison
	// -------------------------------------------------------------------------

	private void loadFieldComparison(String table1, String table2)
	{
		if (fieldCache1.isEmpty() && fieldCache2.isEmpty())
		{
			return;
		}

		currentTable1 = table1;
		currentTable2 = table2;
		leftFields.clear();
		rightFields.clear();
		fieldsDifferent.clear();

		LinkedList<JDBField> fields1 = new LinkedList<>();
		LinkedList<JDBField> fields2 = new LinkedList<>();

		if (!table1.trim().isEmpty())
		{
			LinkedList<JDBField> cached = fieldCache1.get(table1.toLowerCase());
			if (cached != null)
			{
				fields1 = new LinkedList<>(cached);
			}
		}

		if (!table2.trim().isEmpty())
		{
			LinkedList<JDBField> cached = fieldCache2.get(table2.toLowerCase());
			if (cached != null)
			{
				fields2 = new LinkedList<>(cached);
			}
		}

		buildAlignedFields(fields1, fields2, leftFields, rightFields);

		for (int i = 0; i < leftFields.size(); i++)
		{
			fieldsDifferent.add(getCellDifferences(table1, table2, leftFields.get(i), rightFields.get(i)));
		}

		FieldTableModel leftModel = new FieldTableModel(leftFields);
		FieldTableModel rightModel = new FieldTableModel(rightFields);

		tableFields1.setModel(leftModel);
		tableFields2.setModel(rightModel);

		applyFieldTableStyle(tableFields1);
		applyFieldTableStyle(tableFields2);
	}

	private void applyFieldTableStyle(JTable table)
	{
		FieldCellRenderer renderer = new FieldCellRenderer(fieldsDifferent);
		for (int col = 0; col < table.getColumnCount(); col++)
		{
			TableColumn tc = table.getColumnModel().getColumn(col);
			tc.setCellRenderer(renderer);
		}
		if (table.getColumnCount() == 5)
		{
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(40);
			table.getColumnModel().getColumn(3).setPreferredWidth(25);
			table.getColumnModel().getColumn(4).setPreferredWidth(20);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private void clearFieldTables()
	{
		leftFields.clear();
		rightFields.clear();
		fieldsDifferent.clear();
		tableFields1.setModel(new FieldTableModel(leftFields));
		tableFields2.setModel(new FieldTableModel(rightFields));
	}

	// -------------------------------------------------------------------------
	// Alignment algorithms
	// -------------------------------------------------------------------------

	/**
	 * Pre-computes tableHasIssues for every aligned row.  A row is flagged when
	 * the table is absent on either side, or when any field differs in name,
	 * type or size between the two hosts.
	 */
	private void computeTableIssues()
	{
		tableHasIssues.clear();
		fieldCache1.clear();
		fieldCache2.clear();
		int total = alignedTablesHost1.size();
		for (int i = 0; i < total; i++)
		{
			String t1 = alignedTablesHost1.get(i);
			String t2 = alignedTablesHost2.get(i);

			if (t1.isEmpty() || t2.isEmpty())
			{
				tableHasIssues.add(true);
				continue;
			}

			setStatus("Checking fields for table " + (i + 1) + " of " + total + ": " + t1 + "...");

			JDBStructure struc1 = new JDBStructure(hostID1, sessionHost1);
			LinkedList<JDBField> fields1 = struc1.getFieldNames(t1);
			fieldCache1.put(t1.toLowerCase(), fields1);

			JDBStructure struc2 = new JDBStructure(hostID2, sessionHost2);
			LinkedList<JDBField> fields2 = struc2.getFieldNames(t2);
			fieldCache2.put(t2.toLowerCase(), fields2);

			List<String[]> tempLeft = new ArrayList<>();
			List<String[]> tempRight = new ArrayList<>();
			buildAlignedFields(fields1, fields2, tempLeft, tempRight);

			boolean hasIssue = false;
			for (int j = 0; j < tempLeft.size(); j++)
			{
				if (isDifferent(t1, t2, tempLeft.get(j), tempRight.get(j)))
				{
					hasIssue = true;
					break;
				}
			}
			tableHasIssues.add(hasIssue);
		}
		setStatus("");
	}

	/**
	 * Merges two sorted table-name lists into parallel aligned lists, inserting
	 * empty strings where a table is absent on one side.
	 */
	private void buildAlignedTables(LinkedList<String> tables1, LinkedList<String> tables2)
	{
		int i = 0, j = 0;
		while (i < tables1.size() && j < tables2.size())
		{
			String t1 = tables1.get(i);
			String t2 = tables2.get(j);
			int cmp = t1.compareToIgnoreCase(t2);
			if (cmp == 0)
			{
				alignedTablesHost1.add(t1);
				alignedTablesHost2.add(t2);
				i++;
				j++;
			}
			else if (cmp < 0)
			{
				alignedTablesHost1.add(t1);
				alignedTablesHost2.add("");
				i++;
			}
			else
			{
				alignedTablesHost1.add("");
				alignedTablesHost2.add(t2);
				j++;
			}
		}
		while (i < tables1.size())
		{
			alignedTablesHost1.add(tables1.get(i));
			alignedTablesHost2.add("");
			i++;
		}
		while (j < tables2.size())
		{
			alignedTablesHost1.add("");
			alignedTablesHost2.add(tables2.get(j));
			j++;
		}
	}

	/**
	 * Aligns two field lists by matching field names.  Fields from host 1 are
	 * listed first in their original JDBC order.  Each host-1 field is paired
	 * with the matching host-2 field (by name, case-insensitive).  Fields that
	 * exist only on one side are padded with an empty entry on the other side.
	 */
	private void buildAlignedFields(LinkedList<JDBField> fields1, LinkedList<JDBField> fields2,
			List<String[]> outLeft, List<String[]> outRight)
	{
		LinkedList<JDBField> remaining2 = new LinkedList<>(fields2);

		for (JDBField f1 : fields1)
		{
			JDBField match = null;
			for (JDBField f2 : remaining2)
			{
				if (f2.getfieldName().equalsIgnoreCase(f1.getfieldName()))
				{
					match = f2;
					break;
				}
			}

			if (match != null)
			{
				outLeft.add(new String[]
				{ f1.getfieldName(), f1.getfieldType(), String.valueOf(f1.getfieldSize()),
				  f1.getPrimaryKeySeq() > 0 ? String.valueOf(f1.getPrimaryKeySeq()) : "",
				  f1.isNullable() ? " " : "*" });
				outRight.add(new String[]
				{ match.getfieldName(), match.getfieldType(), String.valueOf(match.getfieldSize()),
				  match.getPrimaryKeySeq() > 0 ? String.valueOf(match.getPrimaryKeySeq()) : "",
				  match.isNullable() ? " " : "*" });
				remaining2.remove(match);
			}
			else
			{
				outLeft.add(new String[]
				{ f1.getfieldName(), f1.getfieldType(), String.valueOf(f1.getfieldSize()),
				  f1.getPrimaryKeySeq() > 0 ? String.valueOf(f1.getPrimaryKeySeq()) : "",
				  f1.isNullable() ? " " : "*" });
				outRight.add(new String[]
				{ "", "", "", "", "" });
			}
		}

		// Append fields that only exist in host 2
		for (JDBField f2 : remaining2)
		{
			outLeft.add(new String[]
			{ "", "", "", "", "" });
			outRight.add(new String[]
			{ f2.getfieldName(), f2.getfieldType(), String.valueOf(f2.getfieldSize()),
			  f2.getPrimaryKeySeq() > 0 ? String.valueOf(f2.getPrimaryKeySeq()) : "",
			  f2.isNullable() ? " " : "*" });
		}
	}

	// -------------------------------------------------------------------------
	// Difference detection
	// -------------------------------------------------------------------------

	/**
	 * Returns a per-column array of difference flags for use by the cell renderer.
	 * When a field is absent on one side all cells are flagged.  For matched fields
	 * only the cells whose values actually differ are flagged.
	 */
	private boolean[] getCellDifferences(String ctxTable1, String ctxTable2, String[] left, String[] right)
	{
		// If this field is in the ignore list, treat it as identical
		String fieldName = left[0].isEmpty() ? right[0] : left[0];
		String tableName = ctxTable1.isEmpty() ? ctxTable2 : ctxTable1;
		if (!dbTypeHost1.isEmpty() && ignoreList.isIgnored(dbTypeHost1, dbTypeHost2, tableName, fieldName))
		{
			return new boolean[5];
		}

		boolean[] diff = new boolean[5];
		boolean leftEmpty = left[0].isEmpty();
		boolean rightEmpty = right[0].isEmpty();

		if (leftEmpty || rightEmpty)
		{
			// Entire field missing on one side — flag every cell
			java.util.Arrays.fill(diff, true);
			return diff;
		}

		// Field name (col 0): matched by definition, never flagged individually
		diff[0] = false;

		String leftTypeNorm = normalizeType(left[1]);
		String rightTypeNorm = normalizeType(right[1]);

		// Type (col 1)
		diff[1] = !leftTypeNorm.equals(rightTypeNorm);

		// Size (col 2) — ignored for types whose size is engine-defined, not user-defined
		diff[2] = isSizeIgnored(leftTypeNorm) ? false : !left[2].equals(right[2]);

		// PK sequence (col 3)
		diff[3] = !left[3].equals(right[3]);

		// Not-null indicator (col 4)
		diff[4] = !left[4].equals(right[4]);

		return diff;
	}

	/**
	 * Returns true when the two field entries represent a meaningful schema
	 * difference that should be flagged in red.
	 */
	private boolean isDifferent(String ctxTable1, String ctxTable2, String[] left, String[] right)
	{
		// If this field is in the ignore list, treat it as identical
		String fieldName = left[0].isEmpty() ? right[0] : left[0];
		String tableName = ctxTable1.isEmpty() ? ctxTable2 : ctxTable1;
		if (!dbTypeHost1.isEmpty() && ignoreList.isIgnored(dbTypeHost1, dbTypeHost2, tableName, fieldName))
		{
			return false;
		}

		boolean leftEmpty = left[0].isEmpty();
		boolean rightEmpty = right[0].isEmpty();

		// One side is missing entirely
		if (leftEmpty || rightEmpty)
		{
			return true;
		}

		String leftTypeNorm = normalizeType(left[1]);
		String rightTypeNorm = normalizeType(right[1]);

		if (!leftTypeNorm.equals(rightTypeNorm))
		{
			return true;
		}

		// Size is ignored for types whose size is engine-defined, not user-defined
		if (!isSizeIgnored(leftTypeNorm) && !left[2].equals(right[2]))
		{
			return true;
		}

		// Primary key sequence and nullable indicator
		if (!left[3].equals(right[3]))
		{
			return true;
		}

		return !left[4].equals(right[4]);
	}

	/**
	 * Maps database-specific type names to a common canonical form so that
	 * functionally equivalent types across MySQL, SQL Server and Oracle are not
	 * incorrectly flagged as different.
	 */
	private static String normalizeType(String type)
	{
		switch (type.toLowerCase().trim())
		{
			case "varchar2":
			case "nvarchar":
				return "varchar";
			case "date":
				return "datetime";
			case "integer":
				return "int";
			case "number":
			case "numeric":
			case "decimal unsigned":
			case "float":
				return "decimal";
			default:
				return type.toLowerCase().trim();
		}
	}

	/**
	 * Returns true for types whose reported size is determined by the database
	 * engine / JDBC driver rather than the schema designer.  Different databases
	 * report different display-width or precision values for these types even
	 * though they are functionally identical, so size differences should not be
	 * flagged as schema mismatches.
	 */
	private static boolean isSizeIgnored(String normalizedType)
	{
		switch (normalizedType)
		{
			case "int":
			case "bigint":
			case "smallint":
			case "tinyint":
			case "bit":
			case "datetime":
			case "timestamp":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Maps a JDBC driver class name to a short, human-readable database type
	 * label used when storing and matching ignore-list entries.
	 */
	private static String normalizeDbType(String jdbcDriver)
	{
		if (jdbcDriver == null || jdbcDriver.isEmpty())
		{
			return "Unknown";
		}
		String d = jdbcDriver.toLowerCase();
		if (d.contains("mysql"))     return "MySQL";
		if (d.contains("sqlserver")) return "SQL Server";
		if (d.contains("oracle"))    return "Oracle";
		return jdbcDriver;
	}

	// -------------------------------------------------------------------------
	// Ignore-list refresh (uses cached field data — no DB connection required)
	// -------------------------------------------------------------------------

	/**
	 * Re-computes tableHasIssues from the cached field data without reconnecting
	 * to the databases.  Called after the ignore list changes so that the table
	 * name lists are re-coloured without requiring a full re-comparison.
	 */
	private void recomputeTableIssuesFromCache()
	{
		if (fieldCache1.isEmpty() && fieldCache2.isEmpty())
		{
			return;
		}
		tableHasIssues.clear();
		int total = alignedTablesHost1.size();
		for (int i = 0; i < total; i++)
		{
			String t1 = alignedTablesHost1.get(i);
			String t2 = alignedTablesHost2.get(i);
			if (t1.isEmpty() || t2.isEmpty())
			{
				tableHasIssues.add(true);
				continue;
			}
			LinkedList<JDBField> fields1 = fieldCache1.getOrDefault(t1.toLowerCase(), new LinkedList<>());
			LinkedList<JDBField> fields2 = fieldCache2.getOrDefault(t2.toLowerCase(), new LinkedList<>());

			List<String[]> tempLeft  = new ArrayList<>();
			List<String[]> tempRight = new ArrayList<>();
			buildAlignedFields(fields1, fields2, tempLeft, tempRight);

			boolean hasIssue = false;
			for (int j = 0; j < tempLeft.size(); j++)
			{
				if (isDifferent(t1, t2, tempLeft.get(j), tempRight.get(j)))
				{
					hasIssue = true;
					break;
				}
			}
			tableHasIssues.add(hasIssue);
		}
		listTables1.setCellRenderer(new TableListRenderer(alignedTablesHost1, tableHasIssues));
		listTables2.setCellRenderer(new TableListRenderer(alignedTablesHost2, tableHasIssues));
		listTables1.repaint();
		listTables2.repaint();
	}

	// -------------------------------------------------------------------------
	// Status bar
	// -------------------------------------------------------------------------

	private void setStatus(String message)
	{
		labelStatus.setText(message);
		labelStatus.paintImmediately(labelStatus.getVisibleRect());
	}

	// =========================================================================
	// Inner classes
	// =========================================================================

	/**
	 * Table model for the field comparison JTables.  Each row holds three
	 * strings: field name, type and size.
	 */
	private static class FieldTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;
		private final List<String[]> data;
		private static final String[] COLUMNS =
		{ "Field Name", "Type", "Size", "PK", "NN" };

		public FieldTableModel(List<String[]> data)
		{
			this.data = data;
		}

		@Override
		public int getRowCount()
		{
			return data.size();
		}

		@Override
		public int getColumnCount()
		{
			return COLUMNS.length;
		}

		@Override
		public String getColumnName(int col)
		{
			return COLUMNS[col];
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			return data.get(row)[col];
		}

		@Override
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}
	}

	/**
	 * Cell renderer for the field comparison JTables.  Rows flagged as different
	 * are drawn in red; the selected row has a yellow background.
	 */
	private class FieldCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		private final List<boolean[]> different;

		public FieldCellRenderer(List<boolean[]> different)
		{
			this.different = different;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int col)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

			boolean[] rowDiff = row < different.size() ? different.get(row) : new boolean[5];
			boolean diff = col < rowDiff.length && rowDiff[col];

			if (isSelected)
			{
				setBackground(Color.YELLOW);
				setForeground(diff ? Color.RED : Color.BLACK);
			}
			else if (diff)
			{
				setBackground(table.getBackground());
				setForeground(Color.RED);
			}
			else
			{
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}

			// PK column (index 3) is right-aligned; all others are left-aligned
			setHorizontalAlignment(col == 3 ? SwingConstants.RIGHT : SwingConstants.LEFT);

			setFont(Common.font_list);
			return this;
		}
	}

	/**
	 * Cell renderer for the table-name JLists.  Rows flagged in tableHasIssues
	 * (missing table on either side, or any field mismatch) are drawn in red.
	 * The selected row has a yellow background.  Empty placeholder entries are
	 * displayed as a blank space.
	 */
	private class TableListRenderer extends DefaultListCellRenderer
	{
		private static final long serialVersionUID = 1L;
		private final List<String> myTables;
		private final List<Boolean> hasIssues;

		public TableListRenderer(List<String> myTables, List<Boolean> hasIssues)
		{
			this.myTables = myTables;
			this.hasIssues = hasIssues;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus)
		{
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			String myTable = index < myTables.size() ? myTables.get(index) : "";
			boolean hasIssue = index < hasIssues.size() && hasIssues.get(index);

			// Display a visible space for placeholder rows so row height is maintained
			setText(myTable.isEmpty() ? " " : myTable);
			setFont(Common.font_list);

			if (isSelected)
			{
				setBackground(Color.YELLOW);
				setForeground(hasIssue ? Color.RED : Color.BLACK);
			}
			else if (hasIssue)
			{
				setBackground(list.getBackground());
				setForeground(Color.RED);
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			return this;
		}
	}
}
