package com.commander4j.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBField;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBStructure;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;

public class JInternalFrameUserReportSchema extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList4j<String> jListTables = new JList4j<String>();
	private JList4j<String> jListFields = new JList4j<String>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBStructure structure = new JDBStructure(Common.selectedHostID, Common.sessionID);

	/**
	 * Create the frame.
	 */
	public JInternalFrameUserReportSchema()
	{
		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 560, 583);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 536, 537);
		desktopPane.setBackground(Common.color_edit_properties);
		getContentPane().add(desktopPane);

		JScrollPane scrollPaneTables = new JScrollPane();
		scrollPaneTables.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		scrollPaneTables.setBounds(12, 30, 254, 453);
		desktopPane.add(scrollPaneTables);
		jListTables.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0)
			{
				if (arg0.getValueIsAdjusting() == false)
				{
					populateListFields(jListTables.getSelectedValue().toString());
				}
			}
		});

		jListTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneTables.setViewportView(jListTables);

		JScrollPane scrollPaneFields = new JScrollPane();
		scrollPaneFields.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		scrollPaneFields.setBounds(289, 30, 220, 453);
		desktopPane.add(scrollPaneFields);

		jListFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListFields.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0)
			{
				if (arg0.getValueIsAdjusting() == false)
				{

					if (jListFields.getSelectedIndex() > -1)
					{


					}
				}
			}
		});

		jListFields.setSelectedIndex(-1);
		scrollPaneFields.setViewportView(jListFields);

		JLabel4j_std lblCriteria = new JLabel4j_std(lang.get("lbl_Fields"));
		lblCriteria.setBounds(289, 12, 126, 15);
		desktopPane.add(lblCriteria);

		JLabel4j_std lblReport = new JLabel4j_std(lang.get("lbl_Database_Tables"));
		lblReport.setBounds(12, 12, 126, 15);
		desktopPane.add(lblReport);
		
		JButton4j button4jClose = new JButton4j(Common.icon_close);
		button4jClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button4jClose.setText(lang.get("btn_Close"));
		button4jClose.setMnemonic('0');
		button4jClose.setBounds(215, 495, 126, 32);
		desktopPane.add(button4jClose);

		populateListTables("");

	}


	private void populateListTables(String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempTableList = structure.getTableNames();

		ListModel<String> jList1Model = defComboBoxMod;

		for (int x = 0;x<tempTableList.size();x++)
		{
			defComboBoxMod.addElement(tempTableList.get(x).toString());
		}
		
		jListTables.setModel(jList1Model);
		jListTables.setSelectedIndex(0);

		jListTables.setCellRenderer(Common.renderer_list);

		if (jListTables.isSelectionEmpty())
		{
			if (jListTables.getModel().getSize() > 0)
				jListTables.setSelectedIndex(0);
		}
	}
	
	private void populateListFields(String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<JDBField> tempFieldList = structure.getFieldNames(defaultitem);

		ListModel<String> jList1Model = defComboBoxMod;
		
		for (int x = 0;x<tempFieldList.size();x++)
		{
			defComboBoxMod.addElement(tempFieldList.get(x).toString());
		}
		
		jListFields.setModel(jList1Model);
		jListFields.setSelectedIndex(0);

		jListFields.setCellRenderer(Common.renderer_list);

		if (jListFields.isSelectionEmpty())
		{
			if (jListFields.getModel().getSize() > 0)
				jListFields.setSelectedIndex(0);
		}
	}
}
