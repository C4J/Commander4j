package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameWTSamplePointProperties.java
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.db.JDBWTScale;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTSamplePointProperties class allows the user to edit a
 * record in the APP_WEIGHT_SAMPLE_POINT table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTSamplePointProperties.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameWTSamplePointProperties
 *
 */
public class JInternalFrameWTSamplePointProperties extends JInternalFrame
{
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JComboBox4j<JDBProcessOrderResource> comboBox4j_Resources = new JComboBox4j<JDBProcessOrderResource>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource poResources = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samppoint = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabelEnabled;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel_Location;
	private JLabel4j_std jLabel_SamplePoint;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldLocation;
	private JTextField4j jTextFieldReportingGroup = new JTextField4j(35);
	private JTextField4j jTextFieldSamplePoint;
	private String lsamplepoint;
	private Vector<JDBProcessOrderResource> resourceList = new Vector<JDBProcessOrderResource>();
	private static final long serialVersionUID = 1;

	public void setSamplePointID(String sampid)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Sample Point [" + sampid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		lsamplepoint = sampid;
		jTextFieldSamplePoint.setText(lsamplepoint);
		setTitle("Sample Point [" + lsamplepoint + "]");

		samppoint.setSamplePoint(lsamplepoint);
		samppoint.getProperties(lsamplepoint);

		jTextFieldSamplePoint.setText(samppoint.getSamplePoint());
		jTextFieldLocation.setText(samppoint.getLocation());
		jTextFieldDescription.setText(samppoint.getDescription());
		chckbxEnabled.setSelected(samppoint.isEnabled());
		jTextFieldReportingGroup.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				jButtonSave.setEnabled(true);
			}
		});
		jTextFieldReportingGroup.setText(samppoint.getReportingGroup());
		jButtonSave.setEnabled(false);
	}

	public JInternalFrameWTSamplePointProperties(String uomid)
	{

		super();
		initGUI();
		setSamplePointID(uomid);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_SAMPLEPOINT_ADD"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		for (int x = 0; x < resourceList.size(); x++)
		{
			if (((JDBProcessOrderResource) resourceList.get(x)).toString().equals(samppoint.getRequiredResource()) == true)
			{
				comboBox4j_Resources.setSelectedIndex(x);
				break;
			}
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				jButtonSave.setEnabled(false);
			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 566, 245);
			setVisible(true);
			this.setTitle("Sample Point Properties");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabel_SamplePoint = new JLabel4j_std();
			jDesktopPane1.add(jLabel_SamplePoint);
			jLabel_SamplePoint.setText(lang.get("lbl_SamplePoint"));
			jLabel_SamplePoint.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_SamplePoint.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_SamplePoint.setBounds(0, 8, 149, 22);

			jLabelEnabled = new JLabel4j_std();
			jDesktopPane1.add(jLabelEnabled);
			jLabelEnabled.setText(lang.get("lbl_Enabled"));
			jLabelEnabled.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelEnabled.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabelEnabled.setBounds(400, 8, 106, 22);

			jTextFieldSamplePoint = new JTextField4j(JDBWTSamplePoint.field_SamplePoint);
			jDesktopPane1.add(jTextFieldSamplePoint);
			jTextFieldSamplePoint.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldSamplePoint.setEditable(false);
			jTextFieldSamplePoint.setPreferredSize(new java.awt.Dimension(100, 20));
			jTextFieldSamplePoint.setBounds(155, 8, 237, 22);
			jTextFieldSamplePoint.setEnabled(false);

			jLabel_Location = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Location);
			jLabel_Location.setText(lang.get("lbl_Storage_Location"));
			jLabel_Location.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Location.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Location.setBounds(0, 72, 149, 22);

			jTextFieldLocation = new JTextField4j(JDBWTSamplePoint.field_Location);
			jDesktopPane1.add(jTextFieldLocation);
			jTextFieldLocation.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextFieldLocation.setFocusCycleRoot(true);
			jTextFieldLocation.setBounds(155, 72, 237, 22);
			jTextFieldLocation.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
				}
			});

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonSave.setBounds(118, 169, 110, 32);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					save();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(230, 169, 110, 32);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(342, 169, 110, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_Description = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Description.setBounds(0, 40, 149, 22);

			jTextFieldDescription = new JTextField4j(JDBWTScale.field_Description);
			jDesktopPane1.add(jTextFieldDescription);
			jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextFieldDescription.setFocusCycleRoot(true);
			jTextFieldDescription.setBounds(155, 40, 237, 22);
			jTextFieldDescription.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
				}
			});

			resourceList.add(new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID));
			resourceList.addAll(poResources.getResources());
			ComboBoxModel<JDBProcessOrderResource> comboBox4j_ResourceModel = new DefaultComboBoxModel<JDBProcessOrderResource>(resourceList);
			comboBox4j_Resources.setModel(comboBox4j_ResourceModel);
			comboBox4j_Resources.setBounds(155, 104, 387, 22);
			comboBox4j_Resources.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonSave.setEnabled(true);
				}
			});
			jDesktopPane1.add(comboBox4j_Resources);

			JLabel4j_std label4j_Required_Resource = new JLabel4j_std();
			label4j_Required_Resource.setText(lang.get("lbl_Process_Order_Required_Resource"));
			label4j_Required_Resource.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_Required_Resource.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_Required_Resource.setBounds(0, 104, 149, 22);
			jDesktopPane1.add(label4j_Required_Resource);

			jTextFieldReportingGroup.setText("");
			jTextFieldReportingGroup.setPreferredSize(new Dimension(40, 20));
			jTextFieldReportingGroup.setFocusCycleRoot(true);
			jTextFieldReportingGroup.setCaretPosition(0);
			jTextFieldReportingGroup.setBounds(155, 135, 237, 22);
			jDesktopPane1.add(jTextFieldReportingGroup);

			JLabel4j_std jLabel_ReportingGroup = new JLabel4j_std();
			jLabel_ReportingGroup.setText(lang.get("lbl_Reporting_Group"));
			jLabel_ReportingGroup.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_ReportingGroup.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_ReportingGroup.setBounds(0, 135, 149, 22);
			jDesktopPane1.add(jLabel_ReportingGroup);

			chckbxEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonSave.setEnabled(true);
				}
			});
			chckbxEnabled.setBounds(509, 8, 22, 22);
			jDesktopPane1.add(chckbxEnabled);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		samppoint.setLocation(jTextFieldLocation.getText().toUpperCase());
		samppoint.setDescription(jTextFieldDescription.getText());
		samppoint.setSamplePoint(jTextFieldSamplePoint.getText().toUpperCase());
		samppoint.setRequiredResource(((JDBProcessOrderResource) comboBox4j_Resources.getSelectedItem()).getResource());
		samppoint.setReportingGroup(jTextFieldReportingGroup.getText().toUpperCase());
		samppoint.setEnabled(chckbxEnabled.isSelected());
		samppoint.update();
		jButtonSave.setEnabled(false);
	}
}
