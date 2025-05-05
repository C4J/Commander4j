package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogQMTestProperties.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMTest;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * The JDialogQMTestProperties is part of the Quality Module within the
 * application. The structure of the screens and tables is loosely based upon
 * the SAP Inspection Lot system. The tables APP_QM_INSPECTION, APP_QM_ACTIVITY
 * and APP_QM_TEST are arranged in a 3 layer hierarchy. For each Inspection
 * there can be one or more Activities. For each Activity there will typically
 * be more than one Test.
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogQMTestProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMTest JDBQMTest
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.app.JInternalFrameQMInspectionAdmin JInternalFrameQMInspectionAdmin
 * @see com.commander4j.app.JDialogQMInspectionProperties JDialogQMInspectionProperties
 * @see com.commander4j.app.JDialogQMActivityProperties JDialogQMActivityProperties
 * @see com.commander4j.app.JDialogQMTestProperties JDialogQMTestProperties
 *
 */
public class JDialogQMTestProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldInspectionID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID,Common.sessionID);
	private JDBQMInspection inspect = new JDBQMInspection(Common.selectedHostID,Common.sessionID);
	private JDBQMActivity active = new JDBQMActivity(Common.selectedHostID,Common.sessionID);
	private JDBQMTest test = new JDBQMTest(Common.selectedHostID,Common.sessionID);
	private JDBQMDictionary dictionary = new JDBQMDictionary(Common.selectedHostID,Common.sessionID);
	private JSpinner4j spinnerSequence;
	private JComboBox4j<JDBQMDictionary> comboBoxTestID;
	private DefaultComboBoxModel<JDBQMDictionary> comboBoxTestModel;
	private JTextField4j textFieldInspectionDescription;
	private Vector<JDBQMDictionary> testList = new Vector<JDBQMDictionary>();
	private JTextField4j textFieldActivityDescription;
	private JTextField4j textFieldActivityID;
	private JButton4j btnSave;
	private JButton4j btnClose;
	private JSpinner4j.NumberEditor ne;
	
	private void populateTestList(String inspectionid,String activityid,String testid) 
	{
		testList.clear();
		testList.add(new JDBQMDictionary(Common.selectedHostID, Common.sessionID));
		testList.addAll(dictionary.getTests());
		
		comboBoxTestModel = new DefaultComboBoxModel<JDBQMDictionary>(testList);

		comboBoxTestID.setModel(comboBoxTestModel);
		comboBoxTestID.setMaximumRowCount(12);	
		
		int sel=-1;
		for (int x=0;x<testList.size();x++)
		{
			if (testList.get(x).getTestID().equals(testid))
			{
				sel = x;
				break;
			}
		}
		comboBoxTestID.setSelectedIndex(sel);
	}
	
	private void enableSave()
	{
		if ((((JDBQMDictionary) comboBoxTestID.getSelectedItem()).getTestID()).equals(""))
		{
			btnSave.setEnabled(false);
		}
		else
		{
			btnSave.setEnabled(true);				
		}	
	}

	private void save()
	{
		String insp = textFieldInspectionID.getText();
		String act = textFieldActivityID.getText();
		String tst = ((JDBQMDictionary) comboBoxTestID.getSelectedItem()).getTestID();
		Long seq = (Long.valueOf(spinnerSequence.getValue().toString()));
		
		if (test.isValid(insp, act, tst)==false)
		{
			test.create(insp, act,tst, seq);
		}
		else
		{
			test.setSequence((Long.valueOf(spinnerSequence.getValue().toString())));
			test.update();				
		}
	}
	
	public JDialogQMTestProperties(JFrame frame,String inspectionid,String activityid,String testid) {
		
		super(frame,"Test Properties",ModalityType.APPLICATION_MODAL);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Test Properties");
		this.setResizable(false);		
		this.setSize(855, 224);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_edit_properties);
		desktopPane.setBounds(0, 0, 861, 202);
		getContentPane().add(desktopPane);
		
		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setBounds(8, 22, 87, 22);
		desktopPane.add(lblInspectionID);
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel4j_std lblActivityID = new JLabel4j_std(lang.get("lbl_Activity_ID"));
		lblActivityID.setBounds(6, 69, 89, 16);
		desktopPane.add(lblActivityID);
		lblActivityID.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel4j_std lblTestID = new JLabel4j_std(lang.get("lbl_Test_ID"));
		lblTestID.setBounds(188, 102, 98, 22);
		desktopPane.add(lblTestID);
		lblTestID.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setEnabled(false);
		textFieldInspectionID.setBounds(108, 22, 153, 22);
		desktopPane.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);
		
		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Sequence_ID"));
		label4j_std.setBounds(8, 102, 87, 22);
		desktopPane.add(label4j_std);
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		
		comboBoxTestID = new JComboBox4j<JDBQMDictionary>();
		comboBoxTestID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enableSave();
			}
		});
		comboBoxTestID.setBounds(298, 102, 542, 22);
		desktopPane.add(comboBoxTestID);
		
		spinnerSequence = new JSpinner4j();
		spinnerSequence.setBounds(108, 102, 68, 22);
		ne = new JSpinner4j.NumberEditor(spinnerSequence);
		spinnerSequence.setEditor(ne);
		spinnerSequence.setValue(100);
		spinnerSequence.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				enableSave();
			}
		});
		desktopPane.add(spinnerSequence);
		
		textFieldInspectionDescription = new JTextField4j(JDBQMInspection.field_description);
		textFieldInspectionDescription.setEnabled(false);
		textFieldInspectionDescription.setBounds(377, 22, 463, 22);
		desktopPane.add(textFieldInspectionDescription);
		textFieldInspectionDescription.setColumns(10);
		

		
		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		btnSave.setIcon(Common.icon_update_16x16);
		btnSave.setBounds(308, 139, 117, 32);
		desktopPane.add(btnSave);
		
		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.setBounds(429, 139, 117, 32);
		desktopPane.add(btnClose);
		
		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(273, 22, 92, 22);
		desktopPane.add(label4j_std_1);
		
		textFieldActivityID = new JTextField4j(JDBQMActivity.field_activity_id);
		textFieldActivityID.setEnabled(false);
		textFieldActivityID.setText("<dynamic>");
		textFieldActivityID.setColumns(10);
		textFieldActivityID.setBounds(108, 63, 153, 22);
		desktopPane.add(textFieldActivityID);
		
		JLabel4j_std label4j_std_2 = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_2.setBounds(267, 63, 98, 22);
		desktopPane.add(label4j_std_2);
		
		textFieldActivityDescription = new JTextField4j(JDBQMActivity.field_description);
		textFieldActivityDescription.setEnabled(false);
		textFieldActivityDescription.setColumns(10);
		textFieldActivityDescription.setBounds(377, 63, 463, 22);
		desktopPane.add(textFieldActivityDescription);
		
		inspectionid = JUtility.replaceNullStringwithBlank(inspectionid);
		activityid = JUtility.replaceNullStringwithBlank(activityid);
		testid = JUtility.replaceNullStringwithBlank(testid);
		
		textFieldInspectionID.setText(inspectionid);
		inspect.getProperties(inspectionid);
		textFieldInspectionDescription.setText(inspect.getDescription());
		
		textFieldActivityID.setText(activityid);
		active.getProperties(inspectionid,activityid);
		textFieldActivityDescription.setText(active.getDescription());
		
		populateTestList(inspectionid,activityid,testid); 

		if (testid.equals("")==false)
		{
			if (test.getProperties(inspectionid, activityid,testid))
			{
				spinnerSequence.setValue(test.getSequence());
			}

		}	
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ne.getTextField().requestFocus();
				ne.getTextField().setCaretPosition(ne.getTextField().getText().length());
			}
		});

	}
}
