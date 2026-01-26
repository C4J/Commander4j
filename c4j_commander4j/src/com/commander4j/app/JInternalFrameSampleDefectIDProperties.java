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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBSampleDefectIDs;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JTextArea4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.db.JDBSampleDefectTypes;

public class JInternalFrameSampleDefectIDProperties extends JInternalFrame
{
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JComboBox4j<JDBSampleDefectTypes> jComboBoxDefectType = new JComboBox4j<JDBSampleDefectTypes>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBSampleDefectIDs reas = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Defect;
	private JLabel4j_std jLabel_Enabled;
	private JLabel4j_std jLabel_Description;
	private JPanel4j panelDefectType = new JPanel4j();
	private JTextArea4j jTextFieldLongDescription;
	private JTextField4j jTextFieldDefectID;
	private JTextField4j jTextFieldDescription;
	private String ltype;
	private Vector<JDBSampleDefectTypes> defectTypeList = new Vector<JDBSampleDefectTypes>();
	private static final long serialVersionUID = 1;
	private JDBSampleDefectTypes sampleDefectTypes = new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID);

	public JInternalFrameSampleDefectIDProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_SAMPLE_DEFECT_ID"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});
	}

	public JInternalFrameSampleDefectIDProperties(String reason)
	{

		this();

		jTextFieldDefectID.setText(reason);
		setTitle(getTitle() + " - " + reason);
		ltype = reason;

		reas.setSampleDefectID(ltype);
		reas.getSampleDefectProperties();

		jTextFieldDefectID.setText(reas.getSampleDefectID());
		jTextFieldDescription.setText(reas.getDescription());
		jTextFieldLongDescription.setText(reas.getLongDescription());

		for (int x = 0; x < defectTypeList.size(); x++)
		{
			if (defectTypeList.get(x).getSampleDefectType().equals(reas.getSampleDefectType()))
			{
				jComboBoxDefectType.setSelectedIndex(x);
			}
		}

		chckbxEnabled.setSelected(reas.isEnabled());
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 751, 286);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabel_Defect = new JLabel4j_std();
			jLabel_Defect.setBounds(0, 7, 128, 22);
			jDesktopPane1.add(jLabel_Defect);
			jLabel_Defect.setText(lang.get("lbl_Defect_ID"));
			jLabel_Defect.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Defect.setHorizontalTextPosition(SwingConstants.RIGHT);

			jLabel_Enabled = new JLabel4j_std();
			jLabel_Enabled.setBounds(0, 179, 128, 22);
			jDesktopPane1.add(jLabel_Enabled);
			jLabel_Enabled.setText(lang.get("lbl_Enabled"));
			jLabel_Enabled.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Enabled.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldDefectID = new JTextField4j(JDBSampleDefectIDs.field_sample_defect_type);
			jTextFieldDefectID.setBounds(139, 7, 170, 22);
			jDesktopPane1.add(jTextFieldDefectID);
			jTextFieldDefectID.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldDefectID.setEditable(false);
			jTextFieldDefectID.setEnabled(false);

			jLabel_Description = new JLabel4j_std();
			jLabel_Description.setBounds(0, 81, 128, 22);
			jDesktopPane1.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldDescription = new JTextField4j(JDBSampleDefectIDs.field_description);
			jTextFieldDescription.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldDescription.setBounds(138, 81, 585, 22);
			jDesktopPane1.add(jTextFieldDescription);
			jTextFieldDescription.setFocusCycleRoot(true);
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			jTextFieldDescription.setBorder(border);

			jTextFieldLongDescription = new JTextArea4j();
			jTextFieldLongDescription.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldLongDescription.setWrapStyleWord(true);
			jTextFieldLongDescription.setLineWrap(true);
			jTextFieldLongDescription.setBounds(138, 114, 585, 57);
			jDesktopPane1.add(jTextFieldLongDescription);
			jTextFieldLongDescription.setFocusCycleRoot(true);
			Border border2 = BorderFactory.createLineBorder(Color.BLACK);
			jTextFieldLongDescription.setBorder(border2);

			jButtonUpdate = new JButton4j(Common.icon_update_16x16);
			jButtonUpdate.setBounds(189, 209, 112, 32);
			jDesktopPane1.add(jButtonUpdate);
			jButtonUpdate.setEnabled(false);
			jButtonUpdate.setText(lang.get("btn_Save"));
			jButtonUpdate.setMnemonic(lang.getMnemonicChar());
			jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					reas.setDescription(jTextFieldDescription.getText());
					reas.setLongDescription(jTextFieldLongDescription.getText());
					reas.setEnabled(chckbxEnabled.isSelected());
					reas.setSampleDefectType(((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType());
					reas.update();
					jButtonUpdate.setEnabled(false);
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setBounds(301, 209, 112, 32);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jButtonClose.setBounds(414, 209, 112, 32);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			chckbxEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			chckbxEnabled.setBounds(136, 179, 22, 22);
			jDesktopPane1.add(chckbxEnabled);

			JLabel4j_std jLabel_LongDescription = new JLabel4j_std();
			jLabel_LongDescription.setText(lang.get("lbl_Long_Description"));
			jLabel_LongDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_LongDescription.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_LongDescription.setBounds(0, 114, 128, 22);
			jDesktopPane1.add(jLabel_LongDescription);

			panelDefectType.setLayout(null);
			panelDefectType.setBounds(138, 40, 585, 27);
			jDesktopPane1.add(panelDefectType);

			defectTypeList.add(new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID));
			defectTypeList.addAll(sampleDefectTypes.getSampleDefects(true));
			ComboBoxModel<JDBSampleDefectTypes> jComboBox6Model = new DefaultComboBoxModel<JDBSampleDefectTypes>(defectTypeList);
			jComboBoxDefectType = new JComboBox4j<JDBSampleDefectTypes>();
			jComboBoxDefectType.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jComboBoxDefectType.setModel(jComboBox6Model);
			jComboBoxDefectType.setEditable(false);
			jComboBoxDefectType.setBounds(2, 2, 583, 22);
			panelDefectType.add(jComboBoxDefectType);
			jComboBoxDefectType.setMaximumRowCount(20);
			jComboBoxDefectType.setEditable(false);

			JLabel4j_std jLabel_DefectType = new JLabel4j_std();
			jLabel_DefectType.setText(lang.get("lbl_Defect_Type"));
			jLabel_DefectType.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_DefectType.setBounds(-16, 45, 144, 22);
			jDesktopPane1.add(jLabel_DefectType);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
