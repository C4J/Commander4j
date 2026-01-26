package com.commander4j.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMTest;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JTitledBorder4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameQMInspectionAdmin is part of the Quality Module within the
 * application. The structure of the screens and tables is loosely based upon
 * the SAP Inspection Lot system. The tables APP_QM_INSPECTION, APP_QM_ACTIVITY
 * and APP_QM_TEST are arranged in a 3 layer hierarchy. For each Inspection
 * there can be one or more Activities. For each Activity there will typically
 * be more than one Test.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMInspectionAdmin.jpg" >
 *
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMTest JDBQMTest
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.app.JDialogQMInspectionProperties
 *      JDialogQMInspectionProperties
 * @see com.commander4j.app.JDialogQMActivityProperties
 *      JDialogQMActivityProperties
 * @see com.commander4j.app.JDialogQMTestProperties JDialogQMTestProperties
 *
 */
public class JInternalFrameQMInspectionAdmin extends JInternalFrame
{

	private JCheckBox4j chckbxLimit;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMActivity activity = new JDBQMActivity(Common.selectedHostID, Common.sessionID);
	private JDBQMInspection inspect = new JDBQMInspection(Common.selectedHostID, Common.sessionID);
	private JDBQMTest test = new JDBQMTest(Common.selectedHostID, Common.sessionID);
	private JList4j<JDBQMActivity> listActivity;
	private JList4j<JDBQMInspection> listInspection;
	private JList4j<JDBQMTest> listTest;
	private JSpinner4j spinnerLimit;
	private JTextField4j textFieldDescription;
	private JTextField4j textFieldInspectionID;
	private PreparedStatement listStatement;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public JInternalFrameQMInspectionAdmin()
	{
		setVisible(true);
		setClosable(true);
		setTitle("Inspection Admin");
		setIconifiable(true);
		setBounds(100, 100, 803, 660);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();

		desktopPane.setBounds(0, 0, 833, 713);
		getContentPane().add(desktopPane);

		JPanel4j panelSelection = new JPanel4j();
		panelSelection.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Inspection")));
		panelSelection.setBounds(6, 12, 786, 121);
		desktopPane.add(panelSelection);
		panelSelection.setLayout(null);

		JButton4j btnSearch1 = new JButton4j(lang.get("btn_Search"));
		btnSearch1.setBounds(663, 13, 117, 32);
		panelSelection.add(btnSearch1);
		btnSearch1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buildSQL();
				populateInspectList("");
			}
		});
		btnSearch1.setIcon(Common.icon_search_16x16);

		JButton4j btnClose1 = new JButton4j(lang.get("btn_Close"));
		btnClose1.setBounds(663, 46, 117, 32);
		panelSelection.add(btnClose1);
		btnClose1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		btnClose1.setIcon(Common.icon_close_16x16);

		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setBounds(12, 13, 146, 22);
		panelSelection.add(lblInspectionID);
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setBounds(166, 13, 117, 22);
		panelSelection.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);

		JPanel4j panelInspection = new JPanel4j();
		panelInspection.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Inspection")));
		panelInspection.setBounds(6, 130, 786, 160);

		desktopPane.add(panelInspection);
		panelInspection.setLayout(null);

		JScrollPane4j scrollPaneInspection = new JScrollPane4j(JScrollPane4j.List);
		scrollPaneInspection.setBounds(8, 18, 650, 133);
		panelInspection.add(scrollPaneInspection);

		listInspection = new JList4j<JDBQMInspection>();
		listInspection.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				if (arg0.getClickCount() == 2)
				{
					editInspectionRecord();
				}
			}
		});
		listInspection.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				String selectedItem = "";
				if (listInspection.getSelectedIndex() >= 0)
				{
					selectedItem = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
				}
				populateActivityList(selectedItem, "");
			}
		});
		// listInspection.setLocation(0, 58);
		scrollPaneInspection.setViewportView(listInspection);

		JButton4j btnAdd1 = new JButton4j(lang.get("btn_Add"));
		btnAdd1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addInspectionRecord();
			}
		});
		btnAdd1.setIcon(Common.icon_add_16x16);
		btnAdd1.setBounds(663, 18, 117, 32);
		btnAdd1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_INSPECTION"));
		panelInspection.add(btnAdd1);

		JButton4j btnDelete1 = new JButton4j(lang.get("btn_Delete"));
		btnDelete1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteInspectionRecord();
			}
		});
		btnDelete1.setBounds(663, 84, 117, 32);
		btnDelete1.setIcon(Common.icon_delete_16x16);
		btnDelete1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_INSPECTION"));
		panelInspection.add(btnDelete1);

		JButton4j btnEdit1 = new JButton4j(lang.get("btn_Edit"));
		btnEdit1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editInspectionRecord();
			}
		});
		btnEdit1.setIcon(Common.icon_edit_16x16);
		btnEdit1.setBounds(663, 51, 117, 32);
		btnEdit1.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_INSPECTION"));
		panelInspection.add(btnEdit1);

		JPanel4j panelActivity = new JPanel4j();
		panelActivity.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Activity")));
		panelActivity.setBounds(6, 295, 786, 160);
		desktopPane.add(panelActivity);
		panelActivity.setLayout(null);

		JScrollPane4j scrollPaneActivity = new JScrollPane4j(JScrollPane4j.List);
		scrollPaneActivity.setBounds(8, 18, 650, 133);
		panelActivity.add(scrollPaneActivity);

		listActivity = new JList4j<JDBQMActivity>();
		listActivity.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					editActivityRecord();
				}
			}
		});
		listActivity.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				String selectedItemInsp = "";
				String selectedItemAct = "";
				if (listActivity.getSelectedIndex() >= 0)
				{
					selectedItemInsp = ((JDBQMActivity) listActivity.getSelectedValue()).getInspectionID();
					selectedItemAct = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
				}
				populateTestList(selectedItemInsp, selectedItemAct, "");
			}
		});
		scrollPaneActivity.setViewportView(listActivity);

		JButton4j btnDelete2 = new JButton4j(lang.get("btn_Delete"));
		btnDelete2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteActivityRecord();
			}
		});
		btnDelete2.setBounds(663, 84, 117, 32);
		btnDelete2.setIcon(Common.icon_delete_16x16);
		btnDelete2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_ACTIVITY"));
		panelActivity.add(btnDelete2);

		JButton4j btnEdit2 = new JButton4j(lang.get("btn_Edit"));
		btnEdit2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				editActivityRecord();
			}
		});
		btnEdit2.setIcon(Common.icon_edit_16x16);
		btnEdit2.setBounds(663, 51, 117, 32);
		btnEdit2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_ACTIVITY"));
		panelActivity.add(btnEdit2);

		JButton4j btnAdd2 = new JButton4j(lang.get("btn_Add"));
		btnAdd2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addActivityRecord();
			}
		});
		btnAdd2.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_ACTIVITY"));
		btnAdd2.setBounds(663, 18, 117, 32);
		btnAdd2.setIcon(Common.icon_add_16x16);
		panelActivity.add(btnAdd2);

		JPanel4j panelTests = new JPanel4j();

		panelTests.setBorder(JTitledBorder4j.getPanelTitledBorder(lang.get("lbl_Test")));
		panelTests.setBounds(6, 460, 786, 160);
		desktopPane.add(panelTests);
		panelTests.setLayout(null);

		JScrollPane4j scrollPaneTests = new JScrollPane4j(JScrollPane4j.List);
		scrollPaneTests.setBounds(8, 18, 650, 133);
		panelTests.add(scrollPaneTests);

		listTest = new JList4j<JDBQMTest>();
		listTest.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					editTestRecord();
				}
			}
		});
		scrollPaneTests.setViewportView(listTest);

		JButton4j btnDelete3 = new JButton4j(lang.get("btn_Delete"));
		btnDelete3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				deleteTestRecord();
			}
		});
		btnDelete3.setIcon(Common.icon_delete_16x16);
		btnDelete3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_TEST"));
		btnDelete3.setBounds(663, 84, 117, 32);
		panelTests.add(btnDelete3);

		JButton4j btnEdit3 = new JButton4j(lang.get("btn_Edit"));
		btnEdit3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				editTestRecord();
			}
		});
		btnEdit3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_TEST"));
		btnEdit3.setIcon(Common.icon_edit_16x16);
		btnEdit3.setBounds(663, 51, 117, 32);
		panelTests.add(btnEdit3);

		JButton4j btnAdd3 = new JButton4j(lang.get("btn_Add"));
		btnAdd3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addTestRecord();
			}
		});
		btnAdd3.setIcon(Common.icon_add_16x16);
		btnAdd3.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_QM_TEST"));
		btnAdd3.setBounds(663, 18, 117, 32);
		panelTests.add(btnAdd3);

		JButton4j btnDictionary = new JButton4j(lang.get("btn_Dictionary"));
		btnDictionary.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				editDictionaryRecord();
			}
		});
		btnDictionary.setIcon(Common.icon_dictionary_16x16);
		btnDictionary.setEnabled(true);
		btnDictionary.setBounds(663, 117, 117, 32);
		panelTests.add(btnDictionary);

		JButton4j btnLookupInspection = new JButton4j("");
		btnLookupInspection.setBounds(284, 13, 21, 22);
		panelSelection.add(btnLookupInspection);
		btnLookupInspection.setIcon(Common.icon_lookup_16x16);

		JLabel4j_std lblDescription = new JLabel4j_std(lang.get("lbl_Description"));
		lblDescription.setBounds(12, 46, 146, 22);
		panelSelection.add(lblDescription);
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldDescription = new JTextField4j(JDBQMInspection.field_description);
		textFieldDescription.setBounds(166, 47, 467, 22);
		panelSelection.add(textFieldDescription);
		textFieldDescription.setColumns(10);

		JLabel4j_std lbl_Limit = new JLabel4j_std(lang.get("lbl_Limit"));
		lbl_Limit.setBounds(12, 81, 146, 22);
		panelSelection.add(lbl_Limit);
		lbl_Limit.setHorizontalAlignment(SwingConstants.TRAILING);

		chckbxLimit = new JCheckBox4j("");
		chckbxLimit.setBounds(166, 81, 28, 22);
		panelSelection.add(chckbxLimit);
		chckbxLimit.setSelected(true);

		spinnerLimit = new JSpinner4j();
		spinnerLimit.setBounds(191, 81, 76, 22);
		panelSelection.add(spinnerLimit);
		spinnerLimit.setValue(100);
		JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(spinnerLimit);
		spinnerLimit.setEditor(ne);
		btnLookupInspection.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";
				if (JLaunchLookup.qmInspections())
				{
					textFieldInspectionID.setText(JLaunchLookup.dlgResult);
					buildSQL();
					populateInspectList("");
				}
			}
		});

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldInspectionID.requestFocus();
				textFieldInspectionID.setCaretPosition(textFieldInspectionID.getText().length());

			}
		});

	}

	private void addTestRecord()
	{
		if (listActivity.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			String act = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
			JLaunchMenu.runDialog("FRM_QM_TEST", insp, act, "");
			populateTestList(insp, act, "");
		}
	}

	private void editTestRecord()
	{
		if (listTest.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			String act = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
			String tst = ((JDBQMTest) listTest.getSelectedValue()).getTestID();
			JLaunchMenu.runDialog("FRM_QM_TEST", insp, act, tst);
			populateTestList(insp, act, tst);
		}
	}

	private void editDictionaryRecord()
	{
		if (listTest.isSelectionEmpty() == false)
		{
			String tst = ((JDBQMTest) listTest.getSelectedValue()).getTestID();
			JLaunchMenu.runDialog("FRM_QM_DICTIONARY", tst);
		}
	}

	private void editActivityRecord()
	{
		if (listActivity.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			String act = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
			JLaunchMenu.runDialog("FRM_QM_ACTIVITY", insp, act);
			populateActivityList(insp, act);
		}
	}

	private void addActivityRecord()
	{
		if (listInspection.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			JLaunchMenu.runDialog("FRM_QM_ACTIVITY", insp, "");
			populateActivityList(insp, "");
		}
	}

	private void addInspectionRecord()
	{
		JLaunchMenu.runDialog("FRM_QM_INSPECTION", "");
		populateInspectList("");
	}

	private void editInspectionRecord()
	{
		if (listInspection.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			JLaunchMenu.runDialog("FRM_QM_INSPECTION", insp);
			populateInspectList(insp);
		}
	}

	private void deleteActivityRecord()
	{
		if (listActivity.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			String act = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
			if (activity.isValid(insp, act))
			{
				int question = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Activity [" + act + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
				if (question == 0)
				{
					activity.delete();
					populateActivityList(insp, "");
				}
			}
		}
	}

	private void deleteInspectionRecord()
	{
		if (listInspection.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			if (inspect.isValid(insp))
			{
				int question = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Inspection [" + insp + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION);
				if (question == 0)
				{
					inspect.delete();
					populateInspectList("");
				}
			}
		}
	}

	private void deleteTestRecord()
	{
		if (listTest.isSelectionEmpty() == false)
		{
			String insp = ((JDBQMInspection) listInspection.getSelectedValue()).getInspectionID();
			String act = ((JDBQMActivity) listActivity.getSelectedValue()).getActivityID();
			String tst = ((JDBQMTest) listTest.getSelectedValue()).getTestID();
			if (test.isValid(insp, act, tst))
			{
				int question = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Test [" + tst + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION);
				if (question == 0)
				{
					test.delete();
					populateTestList(insp, act, "");
				}
			}
		}
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_QM_INSPECTION"));
		query.addParamtoSQL("inspection_id =", textFieldInspectionID.getText());
		query.addParamtoSQL("description like ", "%" + textFieldDescription.getText() + "%");

		query.appendSort("inspection_id", true);

		query.applyRestriction(chckbxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), spinnerLimit.getValue());

		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateInspectList(String defaultitem)
	{
		DefaultComboBoxModel<JDBQMInspection> DefComboBoxMod = new DefaultComboBoxModel<JDBQMInspection>();
		ResultSet rs;
		int sel = -1;
		int x = 0;
		try
		{

			rs = listStatement.executeQuery();

			while (rs.next())
			{

				JDBQMInspection mt = new JDBQMInspection("", "");
				mt.setInspectionID(rs.getString("inspection_id"));
				mt.setDescription(rs.getString("description"));
				DefComboBoxMod.addElement(mt);
				if (mt.getInspectionID().equals(defaultitem))
				{
					sel = x;
				}
				x++;
			}
			rs.close();
			if (sel == -1)
				sel = 0;
		}
		catch (SQLException e)
		{
			System.out.print(e.getMessage());
		}

		ListModel<JDBQMInspection> jList1Model = DefComboBoxMod;
		listInspection.setModel(jList1Model);
		listInspection.setSelectedIndex(sel);
		listInspection.setCellRenderer(Common.renderer_list);
		listInspection.ensureIndexIsVisible(sel);

	}

	private void populateActivityList(String inspectionid, String activityid)
	{
		DefaultComboBoxModel<JDBQMActivity> DefComboBoxMod = new DefaultComboBoxModel<JDBQMActivity>();
		LinkedList<JDBQMActivity> activityList = new LinkedList<JDBQMActivity>();

		activityList = activity.getActivities(inspectionid);

		int sel = -1;
		for (int j = 0; j < activityList.size(); j++)
		{
			JDBQMActivity t = (JDBQMActivity) activityList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getActivityID().equals(activityid))
			{
				sel = j;
			}
		}
		if (sel == -1)
			sel = 0;
		ListModel<JDBQMActivity> jList1Model = DefComboBoxMod;
		listActivity.setModel(jList1Model);
		listActivity.setSelectedIndex(sel);
		listActivity.setCellRenderer(Common.renderer_list);
		listActivity.ensureIndexIsVisible(sel);

	}

	private void populateTestList(String inspectionid, String activityid, String testid)
	{
		DefaultComboBoxModel<JDBQMTest> DefComboBoxMod = new DefaultComboBoxModel<JDBQMTest>();
		LinkedList<JDBQMTest> testList = new LinkedList<JDBQMTest>();

		testList = test.getTests(inspectionid, activityid);

		int sel = -1;
		for (int j = 0; j < testList.size(); j++)
		{
			JDBQMTest t = (JDBQMTest) testList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getTestID().equals(testid))
			{
				sel = j;
			}
		}
		if (sel == -1)
			sel = 0;
		ListModel<JDBQMTest> jList1Model = DefComboBoxMod;
		listTest.setModel(jList1Model);
		listTest.setSelectedIndex(sel);
		listTest.setCellRenderer(Common.renderer_list);
		listTest.ensureIndexIsVisible(sel);

	}
}