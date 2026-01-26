package com.commander4j.app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMResult;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQMTest;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.table.JDBQMResultTable;
import com.commander4j.tablemodel.JDBQMResultTableModelData;
import com.commander4j.tablemodel.JDBQMResultTableModelIndex;
import com.commander4j.util.JUtility;

public class JInternalFrameQMSampleResults extends JInternalFrame
{


	private Dimension indexSize;
	private JButton4j btnClose;
	private JButton4j btnDefaults;
	private JButton4j btnEdit;
	private JComboBox4j<JDBQMActivity> comboboxActivities;
	private JDBControl controlDB = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBQMActivity activity = new JDBQMActivity(Common.selectedHostID, Common.sessionID);
	private JDBQMDictionary dictionaryDB = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);
	private JDBQMInspection insp = new JDBQMInspection(Common.selectedHostID, Common.sessionID);
	private JDBQMResult resultDB = new JDBQMResult(Common.selectedHostID, Common.sessionID);
	private JDBQMResultTable jTableData;
	private JDBQMResultTable jTableIndex;
	private JDBQMResultTableModelData dataTableModel;
	private JDBQMResultTableModelIndex indexTableModel;
	private JDBQMSample sampleDB = new JDBQMSample(Common.selectedHostID, Common.sessionID);
	private JDBQMTest testDB = new JDBQMTest(Common.selectedHostID, Common.sessionID);
	private JLabel4j_status jStatusBar;
	private JLabel4j_std lbl_inspection;
	private JScrollPane4j jScrollPane1;
	private JTextField4j textField4InspectionID;
	private JTextField4j textField4Material;
	private JTextField4j textField4jInspectionDescription;
	private JTextField4j textFieldDescription;
	private JTextField4j textFieldProcessOrder;
	private JViewport viewport;
	private LinkedList<JDBQMDictionary> dictionaryDBList = new LinkedList<JDBQMDictionary>();
	private LinkedList<JDBQMSample> sampleDBList = new LinkedList<JDBQMSample>();
	private ListSelectionModel model;
	private boolean tableclear = true;
	private static final long serialVersionUID = 1L;


	private void processOrderChanged(String processOrder)
	{
		if (po.isValidProcessOrder(processOrder))
		{
			po.getProcessOrderProperties(processOrder);
			textFieldProcessOrder.setText(po.getProcessOrder());
			textField4Material.setText(po.getMaterial());
			textFieldDescription.setText(po.getDescription());
			textField4InspectionID.setText(po.getInspectionID());
			insp.getProperties(po.getInspectionID());
			textField4jInspectionDescription.setText(insp.getDescription());
			populateActivityList(po.getInspectionID());
			populateTable();
			tableclear = false;
		}
		else
		{
			if (tableclear == false)
			{
				populateActivityList("");
				textField4Material.setText("");
				textFieldDescription.setText("");
				populateTable();
				tableclear = true;
			}
		}
		// populateTable();
		jStatusBar.setText("");
	}

	private void populateActivityList(String inspectionid)
	{
		DefaultComboBoxModel<JDBQMActivity> defComboBoxMod = new DefaultComboBoxModel<JDBQMActivity>();
		defComboBoxMod.addElement(new JDBQMActivity(Common.selectedHostID, Common.sessionID));
		LinkedList<JDBQMActivity> tempActivityList = activity.getActivities(inspectionid);

		for (int j = 0; j < tempActivityList.size(); j++)
		{
			defComboBoxMod.addElement(tempActivityList.get(j));
		}

		comboboxActivities.setModel(defComboBoxMod);

	}

	private void populateTable()
	{

		initializeTable(po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());

		indexTableModel = new JDBQMResultTableModelIndex(Common.selectedHostID, Common.sessionID, po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());
		jTableIndex.setModel(indexTableModel);
		jTableIndex.setCellRenderers(po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID(), "index");
		jTableIndex.setColumnWidths();

		dataTableModel = new JDBQMResultTableModelData(Common.selectedHostID, Common.sessionID, po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());
		jTableData.setModel(dataTableModel);
		jTableData.setCellRenderers(po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID(), "data");
		jTableData.setColumnEditors(po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());
		jTableData.setColumnWidths();

		model = jTableIndex.getSelectionModel();
		jTableData.setSelectionModel(model);

		indexSize = jTableIndex.getPreferredSize();
		viewport = new JViewport();
		viewport.setBackground(Common.color_table_background1);
		viewport.setView(jTableIndex);
		viewport.setPreferredSize(indexSize);
		viewport.setMaximumSize(indexSize);
		jScrollPane1.setCorner(JScrollPane4j.UPPER_LEFT_CORNER, jTableIndex.getTableHeader());
		jScrollPane1.setRowHeaderView(viewport);

		jScrollPane1.setViewportView(jTableData);

		JUtility.scrolltoHomePosition(jScrollPane1);
		jTableIndex.repaint();
		jTableData.repaint();
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusBar, false, 99999, jTableData.getRowCount());
	}

	private void defaults()
	{

		dictionaryDBList = testDB.getTestsPropertiesList(po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());

		sampleDBList = sampleDB.getSamples(po.getProcessOrder(), po.getInspectionID(), ((JDBQMActivity) comboboxActivities.getSelectedItem()).getActivityID());

		for (int x = 0; x < sampleDBList.size(); x++)
		{
			Long sampID = sampleDBList.get(x).getSampleID();

			if (resultDB.getResultsProperties(sampID, controlDB.getKeyValueWithDefault("QM_RESULTS_TEST_DEFAULTS", "PANEL PH", "Test ID to determine if row defaults to be applied.")))
			{
				if (JUtility.replaceNullStringwithBlank(resultDB.getValue()).trim().equals("") == false)
				{
					for (int z = 0; z < dictionaryDBList.size(); z++)
					{
						dictionaryDB = ((JDBQMDictionary) dictionaryDBList.get(z));

						if (dictionaryDB.getDefaultValue().trim().equals("")==false)
						{
							if (resultDB.getResultsProperties(sampID,dictionaryDB.getTestID()))
							{
								if (JUtility.replaceNullStringwithBlank(resultDB.getValue()).equals("") == true)
								{
									if (dictionaryDB.getDefaultValue().equals("")==false)
									{
										resultDB.setUserID(Common.userList.getUser(Common.sessionID).getUserId());
										resultDB.setUpdated(JUtility.getSQLDateTime());
										resultDB.setValue(dictionaryDB.getDefaultValue());
										resultDB.update();
									}
								}
							}
							else
							{
								if (dictionaryDB.getDefaultValue().equals("")==false)
								{
									resultDB.create(sampID, dictionaryDB.getTestID(), dictionaryDB.getDefaultValue(), "Created", Common.userList.getUser(Common.sessionID).getUserId());
								}
							}
						}
					}
				}
			}

		}
	}

	private void editRecord()
	{
		int row = jTableIndex.getSelectedRow();
		if (row >= 0)
		{
			String temp = jTableIndex.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_QM_SAMPLE_EDIT", temp);
		}
	}

	public JInternalFrameQMSampleResults(String processOrder)
	{
		super();

		processOrderChanged(processOrder);

	}

	/**
	 * Create the frame.
	 */
	public JInternalFrameQMSampleResults()
	{

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 1010, 731);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, 1241, 711);

		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setBounds(6, 15, 111, 22);
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		desktopPane.add(lblProcessOrder);

		textFieldProcessOrder = new JTextField4j();
		textFieldProcessOrder.setBounds(125, 15, 138, 22);
		textFieldProcessOrder.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				processOrderChanged(textFieldProcessOrder.getText());
			}
		});
		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		JLabel4j_std lblNewLabel_3 = new JLabel4j_std(lang.get("lbl_Activity_ID"));
		lblNewLabel_3.setBounds(6, 93, 111, 22);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.TRAILING);
		desktopPane.add(lblNewLabel_3);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setBounds(879, 87, 115, 32);
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		desktopPane.add(btnClose);

		btnEdit = new JButton4j(lang.get("btn_Edit"));
		btnEdit.setBounds(648, 87, 115, 32);
		btnEdit.setIcon(Common.icon_edit_16x16);
		btnEdit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editRecord();
			}
		});
		desktopPane.add(btnEdit);

		btnDefaults = new JButton4j(lang.get("lbl_default"));
		btnDefaults.setBounds(763, 87, 115, 32);
		btnDefaults.setIcon(Common.icon_default_16x16);
		btnDefaults.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defaults();
				populateTable();
			}
		});
		desktopPane.add(btnDefaults);

		JButton4j btnRefresh = new JButton4j(lang.get("btn_Refresh"));
		btnRefresh.setBounds(533, 87, 115, 32);
		btnRefresh.setIcon(Common.icon_refresh_16x16);
		btnRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				populateTable();
			}
		});
		desktopPane.add(btnRefresh);

		JButton4j btnProcessOrderLookup = new JButton4j();
		btnProcessOrderLookup.setIcon(Common.icon_lookup_16x16);
		btnProcessOrderLookup.setBounds(261, 15, 21, 22);
		btnProcessOrderLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgCriteriaDefault = "Ready";
				JLaunchLookup.dlgAutoExec = true;
				if (JLaunchLookup.processOrders())
				{
					textFieldProcessOrder.setText(JLaunchLookup.dlgResult);
					processOrderChanged(JLaunchLookup.dlgResult);
				}
			}
		});
		desktopPane.add(btnProcessOrderLookup);

		comboboxActivities = new JComboBox4j<JDBQMActivity>();
		comboboxActivities.setBounds(125, 93, 393, 22);
		comboboxActivities.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				populateTable();
			}
		});
		desktopPane.add(comboboxActivities);

		jStatusBar = new JLabel4j_status();
		jStatusBar.setBounds(0, 680, 993, 21);
		desktopPane.add(jStatusBar);

		jScrollPane1 = new JScrollPane4j(JScrollPane4j.Table);
		jScrollPane1.setBounds(0, 139, 993, 535);

		desktopPane.setLayout(null);
		desktopPane.add(jScrollPane1);

		initializeTable("", "");

		textField4Material = new JTextField4j();
		textField4Material.setEnabled(false);
		textField4Material.setColumns(10);
		textField4Material.setBounds(413, 15, 138, 22);
		desktopPane.add(textField4Material);

		textFieldDescription = new JTextField4j();
		textFieldDescription.setEnabled(false);
		textFieldDescription.setColumns(10);
		textFieldDescription.setBounds(563, 15, 431, 22);
		desktopPane.add(textFieldDescription);

		JLabel4j_std lbl_material = new JLabel4j_std(lang.get("lbl_Material"));
		lbl_material.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_material.setBounds(294, 15, 111, 22);
		desktopPane.add(lbl_material);

		textField4InspectionID = new JTextField4j();
		textField4InspectionID.setEnabled(false);
		textField4InspectionID.setColumns(10);
		textField4InspectionID.setBounds(125, 52, 138, 22);
		desktopPane.add(textField4InspectionID);

		lbl_inspection = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lbl_inspection.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_inspection.setBounds(6, 52, 111, 22);
		desktopPane.add(lbl_inspection);

		textField4jInspectionDescription = new JTextField4j();
		textField4jInspectionDescription.setEnabled(false);
		textField4jInspectionDescription.setColumns(10);
		textField4jInspectionDescription.setBounds(271, 52, 723, 22);
		desktopPane.add(textField4jInspectionDescription);

		processOrderChanged("");

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldProcessOrder.requestFocus();
				textFieldProcessOrder.setCaretPosition(textFieldProcessOrder.getText().length());

			}
		});

	}

	private void initializeTable(String insp, String act)
	{
		jTableIndex = new JDBQMResultTable(Common.selectedHostID, Common.sessionID, insp, act, "index");
		jTableIndex.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableData = new JDBQMResultTable(Common.selectedHostID, Common.sessionID, insp, act, "data");
		jTableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
