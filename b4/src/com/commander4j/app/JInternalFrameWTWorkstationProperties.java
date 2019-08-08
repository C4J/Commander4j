package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTWorkstationProperties.java
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
import java.util.Collections;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.db.JDBWTScale;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTWorkstationProperties class allows the user to edit a
 * record in the APP_WEIGHT_WORKSTATION table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTWorkstationProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWTWorkstationProperties
 *
 */
public class JInternalFrameWTWorkstationProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldWorkstation;
	private JLabel4j_std jLabel_Location;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel1;
	private JDBWTWorkstation workstation = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
	private JDBWTScale scale = new JDBWTScale(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samplePoint = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrderResource poResources = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private String lworkstation;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBox_Ports = new JComboBox4j<String>();
	private JComboBox4j<JDBWTScale> comboBox_Scales = new JComboBox4j<JDBWTScale>();
	private JComboBox4j<JDBWTSamplePoint> comboBox4j_SamplePoint = new JComboBox4j<JDBWTSamplePoint>();
	private JComboBox4j<JDBProcessOrderResource> comboBox4j_Resources = new JComboBox4j<JDBProcessOrderResource>();
	private Vector<JDBWTSamplePoint> samplePointList = new Vector<JDBWTSamplePoint>();
	private Vector<JDBProcessOrderResource> resourceList = new Vector<JDBProcessOrderResource>();
	private Vector<JDBWTScale> scaleList = new Vector<JDBWTScale>();
	private Vector<String> portList = new Vector<String>();
	private String[] standardPorts = new String[]
	{ "COM1", "COM2", "COM3", "COM4" };

	public void setWorkstationID(String wstation)
	{
		lworkstation = wstation;

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Workstation [" + wstation + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		jTextFieldWorkstation.setText(lworkstation);
		setTitle("Workstation [" + lworkstation + "]");

		workstation.setWorkstationID(lworkstation);
		workstation.getProperties(lworkstation);

		jTextFieldWorkstation.setText(workstation.getWorkstationID());
		jTextFieldLocation.setText(workstation.getLocation());
		jTextFieldDescription.setText(workstation.getDescription());

		for (int x = 0; x < samplePointList.size(); x++)
		{
			if (((JDBWTSamplePoint) samplePointList.get(x)).getSamplePoint().equals(workstation.getSamplePoint()) == true)
			{
				comboBox4j_SamplePoint.setSelectedIndex(x);
				break;
			}
		}

		for (int x = 0; x < scaleList.size(); x++)
		{
			if (((JDBWTScale) scaleList.get(x)).getScaleID().equals(workstation.getScaleID()) == true)
			{
				comboBox_Scales.setSelectedIndex(x);
				break;
			}
		}

		Collections.addAll(portList, standardPorts);

		for (int x = 0; x < portList.size(); x++)
		{

			if (((String) portList.get(x)).toString().equals(workstation.getScalePort()) == true)
			{
				comboBox_Ports.setSelectedIndex(x);
				break;
			}
		}
		
		for (int x = 0; x < resourceList.size(); x++)
		{
			if (((JDBProcessOrderResource) resourceList.get(x)).toString().equals(workstation.getRequiredResource()) == true)
			{
				comboBox4j_Resources.setSelectedIndex(x);
				break;
			}
		}
		
		jButtonSave.setEnabled(false);

	}

	public JInternalFrameWTWorkstationProperties(String workstation)
	{

		super();
		
		initGUI();
		setWorkstationID(workstation);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_WORKSTATION_ADD"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());

			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 564, 358);
			setVisible(true);
			this.setTitle("Workstation Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Workstation"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldWorkstation = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
					jDesktopPane1.add(jTextFieldWorkstation);
					jTextFieldWorkstation.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldWorkstation.setEditable(false);
					jTextFieldWorkstation.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldWorkstation.setBounds(155, 10, 237, 24);
					jTextFieldWorkstation.setEnabled(false);
				}
				{
					jLabel_Location = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Location);
					jLabel_Location.setText(lang.get("lbl_Storage_Location"));
					jLabel_Location.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Location.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Location.setBounds(0, 76, 149, 21);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBWTWorkstation.field_Location);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldLocation.setFocusCycleRoot(true);
					jTextFieldLocation.setBounds(155, 76, 237, 24);
					jTextFieldLocation.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(115, 270, 110, 32);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							save();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(227, 270, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(339, 270, 110, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jLabel_Description = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Description);
					jLabel_Description.setText(lang.get("lbl_Description"));
					jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Description.setBounds(0, 43, 149, 21);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWTWorkstation.field_Description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(155, 43, 237, 24);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}

				JLabel4j_std label4j_Scale_ID = new JLabel4j_std();
				label4j_Scale_ID.setText(lang.get("lbl_Scale_ID"));
				label4j_Scale_ID.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Scale_ID.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Scale_ID.setBounds(0, 151, 149, 21);
				jDesktopPane1.add(label4j_Scale_ID);

				JLabel4j_std label4j_Scale_Port = new JLabel4j_std();
				label4j_Scale_Port.setText(lang.get("lbl_Scale_Port"));
				label4j_Scale_Port.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Scale_Port.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Scale_Port.setBounds(0, 190, 149, 21);
				jDesktopPane1.add(label4j_Scale_Port);
				
				JLabel4j_std label4j_Required_Resource = new JLabel4j_std();
				label4j_Required_Resource.setText(lang.get("lbl_Process_Order_Required_Resource"));
				label4j_Required_Resource.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Required_Resource.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Required_Resource.setBounds(0, 226, 149, 21);
				jDesktopPane1.add(label4j_Required_Resource);

				ComboBoxModel<String> jComboBoxModelPort = new DefaultComboBoxModel<String>(portList);
				comboBox_Ports.setModel(jComboBoxModelPort);
				comboBox_Ports.setBounds(155, 187, 387, 24);
				comboBox_Ports.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBox_Ports);

				scaleList.add(new JDBWTScale(Common.selectedHostID, Common.sessionID));
				scaleList.addAll(scale.getScales());
				ComboBoxModel<JDBWTScale> jComboBoxModelScale = new DefaultComboBoxModel<JDBWTScale>(scaleList);
				comboBox_Scales.setModel(jComboBoxModelScale);
				comboBox_Scales.setBounds(155, 148, 387, 24);
				comboBox_Scales.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBox_Scales);

				samplePointList.add(new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID));
				samplePointList.addAll(samplePoint.getSamplePoints(JDBWTSamplePoint.displayType_COMBO));
				ComboBoxModel<JDBWTSamplePoint> jComboBoxModelSamplePoint = new DefaultComboBoxModel<JDBWTSamplePoint>(samplePointList);
				comboBox4j_SamplePoint.setModel(jComboBoxModelSamplePoint);
				comboBox4j_SamplePoint.setBounds(155, 112, 387, 24);
				comboBox4j_SamplePoint.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBox4j_SamplePoint);

				JLabel4j_std label4j_SamplePoint = new JLabel4j_std();
				label4j_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				label4j_SamplePoint.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_SamplePoint.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_SamplePoint.setBounds(0, 115, 149, 21);
				jDesktopPane1.add(label4j_SamplePoint);
				
				resourceList.add(new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID));
				resourceList.addAll(poResources.getResources());
				ComboBoxModel<JDBProcessOrderResource> comboBox4j_ResourceModel = new DefaultComboBoxModel<JDBProcessOrderResource>(resourceList);
				comboBox4j_Resources.setModel(comboBox4j_ResourceModel);
				comboBox4j_Resources.setBounds(155, 223, 387, 24);
				comboBox4j_Resources.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBox4j_Resources);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		workstation.setLocation(jTextFieldLocation.getText());
		workstation.setDescription(jTextFieldDescription.getText());
		workstation.setWorkstationID(jTextFieldWorkstation.getText());
		workstation.setSamplePoint(((JDBWTSamplePoint) comboBox4j_SamplePoint.getSelectedItem()).getSamplePoint());
		workstation.setScaleID(((JDBWTScale) comboBox_Scales.getSelectedItem()).getScaleID());
		workstation.setRequiredResource(((JDBProcessOrderResource) comboBox4j_Resources.getSelectedItem()).getResource());
		
		if (comboBox_Ports.getSelectedIndex() >= 0)
		{
			workstation.setScalePort((String) comboBox_Ports.getSelectedItem().toString());
		}
		else
		{
			workstation.setScalePort("");
		}
		workstation.update();
		jButtonSave.setEnabled(false);
	}
}
