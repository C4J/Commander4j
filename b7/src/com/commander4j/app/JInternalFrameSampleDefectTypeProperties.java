package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameSampleDefectProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSampleDefectTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameSampleDefectTypeProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextArea jTextFieldLongDescription;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldDefectID;
	private JLabel4j_std jLabelDefectID;
	private JLabel4j_std jLabelEnabled;
	private JLabel4j_std jLabelSequence;
	private JDBSampleDefectTypes reas = new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID);
	private String ltype;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JSpinner jSpinnerLimit;
	private JCheckBox tglbtnLeakingData;
	private JCheckBox tglbtnNonLeakingData;

	public JInternalFrameSampleDefectTypeProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SAMPLE_REASON"));

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				
			}
		});
	}

	public JInternalFrameSampleDefectTypeProperties(String reason)
	{

		this();

		jTextFieldDefectID.setText(reason);
		setTitle(getTitle() + " - " + reason);
		ltype = reason;

		reas.setSampleDefectType(ltype);
		reas.getSampleDefectProperties();

		jTextFieldDefectID.setText(reas.getSampleDefectType());
		jTextFieldDescription.setText(reas.getDescription());
		jTextFieldLongDescription.setText(reas.getLongDescription());
		jSpinnerLimit.setValue(reas.getOutputColumn());
		chckbxEnabled.setSelected(reas.isEnabled());
		
		tglbtnLeakingData.setSelected(reas.isLeakingData());
		tglbtnNonLeakingData.setSelected(reas.isNonLeakingData());

		
		jButtonUpdate.setEnabled(false);
	}
	

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 751, 299);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabelDefectID = new JLabel4j_std();
					jLabelDefectID.setBounds(0, 7, 128, 21);
					jDesktopPane1.add(jLabelDefectID);
					jLabelDefectID.setText(lang.get("lbl_Defect_ID"));
					jLabelDefectID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDefectID.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jLabelEnabled = new JLabel4j_std();
					jLabelEnabled.setBounds(0, 167, 128, 21);
					jDesktopPane1.add(jLabelEnabled);
					jLabelEnabled.setText(lang.get("lbl_Enabled"));
					jLabelEnabled.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelEnabled.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jLabelSequence = new JLabel4j_std();
					jLabelSequence.setBounds(0, 138, 128, 21);
					jDesktopPane1.add(jLabelSequence);
					jLabelSequence.setText(lang.get("lbl_Sequence_ID"));
					jLabelSequence.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSequence.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jTextFieldDefectID = new JTextField4j(JDBSampleDefectTypes.field_sample_defect_type);
					jTextFieldDefectID.setBounds(139, 7, 170, 21);
					jDesktopPane1.add(jTextFieldDefectID);
					jTextFieldDefectID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldDefectID.setEditable(false);
					jTextFieldDefectID.setEnabled(false);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jLabelDescription.setBounds(0, 40, 128, 21);
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				
				{
					jTextFieldDescription = new JTextField4j(JDBSampleDefectTypes.field_description);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setBounds(138, 40, 585, 21);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setFocusCycleRoot(true);
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					jTextFieldDescription.setBorder(border);
				}
				
				{
					jTextFieldLongDescription = new JTextArea();
					jTextFieldLongDescription.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldLongDescription.setWrapStyleWord(true);
					jTextFieldLongDescription.setLineWrap(true);
					jTextFieldLongDescription.setBounds(138, 73, 585, 57);
					jDesktopPane1.add(jTextFieldLongDescription);
					jTextFieldLongDescription.setFocusCycleRoot(true);
					Border border = BorderFactory.createLineBorder(Color.BLACK);
					jTextFieldLongDescription.setBorder(border);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(176, 223, 112, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							reas.setDescription(jTextFieldDescription.getText());
							reas.setLongDescription(jTextFieldLongDescription.getText());
							reas.setEnabled(chckbxEnabled.isSelected());
							reas.setOutputColumn(Integer.valueOf(jSpinnerLimit.getValue().toString()));
							reas.setLeakingData(tglbtnLeakingData.isSelected());
							reas.setNonLeakingData(tglbtnNonLeakingData.isSelected());
							reas.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(288, 223, 112, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(401, 223, 112, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				
				{
					chckbxEnabled.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					chckbxEnabled.setBounds(136, 167, 22, 23);
					jDesktopPane1.add(chckbxEnabled);
				}
				
				JLabel4j_std jLabelLongDescription = new JLabel4j_std();
				jLabelLongDescription.setText(lang.get("lbl_Long_Description"));
				jLabelLongDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabelLongDescription.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabelLongDescription.setBounds(0, 73, 128, 21);
				jDesktopPane1.add(jLabelLongDescription);
				
				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(10);
					jSpinnerIntModel.setMaximum(990);
					jSpinnerIntModel.setStepSize(10);
					jSpinnerLimit = new JSpinner();
					jSpinnerLimit.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setBounds(139, 138, 68, 21);
					jSpinnerLimit.setValue(10);
					jSpinnerLimit.getEditor().setSize(45, 21);
					jDesktopPane1.add(jSpinnerLimit);
				}
				
				tglbtnLeakingData = new JCheckBox(lang.get("lbl_Leaking_Code"));
				tglbtnLeakingData.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				tglbtnLeakingData.setBounds(477, 135, 246, 25);
				jDesktopPane1.add(tglbtnLeakingData);
				
				tglbtnNonLeakingData = new JCheckBox(lang.get("lbl_Non_Leaking_Code"));
				tglbtnNonLeakingData.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				tglbtnNonLeakingData.setBounds(219, 135, 246, 25);
				jDesktopPane1.add(tglbtnNonLeakingData);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
