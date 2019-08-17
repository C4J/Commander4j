package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTScaleProperties.java
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWTScale;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTScaleProperties class allows the user to edit a
 * record in the APP_WEIGHT_SCALE table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTScaleProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameWTScaleProperties
 *
 */
public class JInternalFrameWTScaleProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JTextField4j jTextFieldScale;
	private JLabel4j_std jLabel_Connection;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabel_Description;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JComboBox4j<String> comboBox_Connection;
	private JLabel4j_std jLabel_Scale;
	private JDBWTScale scale = new JDBWTScale(Common.selectedHostID, Common.sessionID);
	private String lscale;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBox_Model = new JComboBox4j<String>();
	private JComboBox4j<String> comboBox_Make = new JComboBox4j<String>();
	private JComboBox4j<String> comboBox_Baud;
	private JComboBox4j<String> comboBox_DataBits;
	private JComboBox4j<String> comboBox_StopBits;
	private JComboBox4j<String> comboBox_FlowControl;
	private JComboBox4j<String> comboBox_Parity;
	private JComboBox4j<String> comboBox_EndOfLine;

	public void setScaleID(String scaleid)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Scale [" + scaleid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		lscale = scaleid;
		jTextFieldScale.setText(lscale);
		setTitle("Scale [" + lscale + "]");

		scale.setScaleID(lscale);
		scale.getProperties(lscale);

		jTextFieldScale.setText(scale.getScaleID());
		jTextFieldDescription.setText(scale.getDescription());
		comboBox_Make.setSelectedItem(scale.getMake());
		if (comboBox_Make.getSelectedIndex()<0)
		{
			comboBox_Make.setSelectedIndex(0);
		}
		comboBox_Model.setSelectedItem(scale.getModel());
		if (comboBox_Model.getSelectedIndex()<0)
		{
			comboBox_Model.setSelectedIndex(0);
		}
		comboBox_Connection.setSelectedItem(scale.getConnection());
		if (comboBox_Connection.getSelectedIndex()<0)
		{
			comboBox_Connection.setSelectedIndex(0);
		}
		comboBox_Baud.setSelectedItem(String.valueOf(scale.getBaudRate()).trim());
		if (comboBox_Baud.getSelectedIndex()<0)
		{
			comboBox_Baud.setSelectedIndex(0);
		}
		comboBox_DataBits.setSelectedItem(String.valueOf(scale.getDataBits()).trim());
		if (comboBox_DataBits.getSelectedIndex()<0)
		{
			comboBox_DataBits.setSelectedIndex(0);
		}
		
	
		if (scale.getStopBits()==1)
		{
			comboBox_StopBits.setSelectedIndex(0);
		}
		else
		{
			if (scale.getStopBits()==3)
			{
				comboBox_StopBits.setSelectedIndex(1);
			}
			else
			{
				if (scale.getStopBits()==2)
				{
					comboBox_StopBits.setSelectedIndex(2);
				}
				else
				{
					comboBox_StopBits.setSelectedIndex(-1);
				}
			}
		}
		
		comboBox_FlowControl.setSelectedItem(scale.getFlowControl());
		if (comboBox_FlowControl.getSelectedIndex()<0)
		{
			comboBox_FlowControl.setSelectedIndex(0);
		}
		comboBox_Parity.setSelectedItem(scale.getParity());
		if (comboBox_Parity.getSelectedIndex()<0)
		{
			comboBox_Parity.setSelectedIndex(0);
		}
		comboBox_EndOfLine.setSelectedItem(scale.getEndOfLine());
		if (comboBox_EndOfLine.getSelectedIndex()<0)
		{
			comboBox_EndOfLine.setSelectedIndex(0);
		}
		jButtonSave.setEnabled(false);
	}

	public JInternalFrameWTScaleProperties(String uomid)
	{

		super();
		initGUI();
		setScaleID(uomid);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_SCALE_ADD"));

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
			this.setBounds(25, 25, 424, 493);
			setVisible(true);
			this.setTitle("Scale Properties");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jLabel_Scale = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Scale);
					jLabel_Scale.setText(lang.get("lbl_Scale_ID"));
					jLabel_Scale.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Scale.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Scale.setBounds(0, 10, 149, 21);
				}
				{
					jTextFieldScale = new JTextField4j(JDBWTScale.field_ScaleID);
					jDesktopPane1.add(jTextFieldScale);
					jTextFieldScale.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldScale.setEditable(false);
					jTextFieldScale.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldScale.setBounds(155, 10, 237, 24);
					jTextFieldScale.setEnabled(false);
				}
				{
					jLabel_Connection = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Connection);
					jLabel_Connection.setText(lang.get("lbl_Connection"));
					jLabel_Connection.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel_Connection.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel_Connection.setBounds(0, 152, 149, 21);
				}
				{
					comboBox_Connection = new JComboBox4j<String>();
					comboBox_Connection.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					comboBox_Connection.setModel(new DefaultComboBoxModel<String>(new String[] {"Serial", "Ethernet", "USB"}));
					jDesktopPane1.add(comboBox_Connection);
					comboBox_Connection.setPreferredSize(new java.awt.Dimension(40, 20));
					comboBox_Connection.setFocusCycleRoot(true);
					comboBox_Connection.setBounds(155, 152, 109, 24);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(false);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonSave.setBounds(42, 408, 110, 32);
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
					jButtonHelp.setBounds(154, 408, 110, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(266, 408, 110, 32);
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
					jLabel_Description.setBounds(0, 46, 149, 21);
				}

				{
					jTextFieldDescription = new JTextField4j(JDBWTScale.field_Description);
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

				JLabel4j_std label4j_Make = new JLabel4j_std();
				label4j_Make.setText(lang.get("lbl_Make"));
				label4j_Make.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Make.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Make.setBounds(0, 77, 149, 24);
				jDesktopPane1.add(label4j_Make);

				JLabel4j_std label4j_Model = new JLabel4j_std();
				label4j_Model.setText(lang.get("lbl_Model"));
				label4j_Model.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Model.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Model.setBounds(0, 116, 149, 24);
				jDesktopPane1.add(label4j_Model);
				comboBox_Model.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				comboBox_Model.setModel(new DefaultComboBoxModel<String>(new String[] {"N/A", "ICS465", "MONOBLOC", "VIPER"}));

				comboBox_Model.setBounds(155, 116, 237, 24);
				jDesktopPane1.add(comboBox_Model);
				comboBox_Make.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				comboBox_Make.setModel(new DefaultComboBoxModel<String>(new String[]
				{ "NONE", "METTLER TOLEDO" }));

				comboBox_Make.setBounds(155, 77, 237, 24);
				jDesktopPane1.add(comboBox_Make);

				{
					comboBox_Baud = new JComboBox4j<String>();
					comboBox_Baud.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					comboBox_Baud.setModel(new DefaultComboBoxModel<String>(new String[]
					{ "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200" }));
					comboBox_Baud.setPreferredSize(new Dimension(40, 20));
					comboBox_Baud.setFocusCycleRoot(true);
					comboBox_Baud.setBounds(155, 185, 81, 24);
					jDesktopPane1.add(comboBox_Baud);
				}
				{
					comboBox_DataBits = new JComboBox4j<String>();
					comboBox_DataBits.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					comboBox_DataBits.setModel(new DefaultComboBoxModel<String>(new String[]
					{ "8", "7" }));
					comboBox_DataBits.setPreferredSize(new Dimension(40, 20));
					comboBox_DataBits.setFocusCycleRoot(true);
					comboBox_DataBits.setBounds(155, 221, 49, 24);
					jDesktopPane1.add(comboBox_DataBits);
				}
				{
					comboBox_StopBits = new JComboBox4j<String>();
					comboBox_StopBits.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					comboBox_StopBits.setModel(new DefaultComboBoxModel<String>(new String[]
					{ "1", "1.5","2" }));
					comboBox_StopBits.setPreferredSize(new Dimension(40, 20));
					comboBox_StopBits.setFocusCycleRoot(true);
					comboBox_StopBits.setBounds(155, 257, 49, 24);
					jDesktopPane1.add(comboBox_StopBits);
				}
				{
					comboBox_FlowControl = new JComboBox4j<String>();
					comboBox_FlowControl.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					comboBox_FlowControl.setModel(new DefaultComboBoxModel<String>(new String[]
					{ "NONE", "XON/XOFF" }));
					comboBox_FlowControl.setPreferredSize(new Dimension(40, 20));
					comboBox_FlowControl.setFocusCycleRoot(true);
					comboBox_FlowControl.setBounds(155, 328, 109, 24);
					jDesktopPane1.add(comboBox_FlowControl);
				}

				JLabel4j_std label4j_Baud = new JLabel4j_std();
				label4j_Baud.setText(lang.get("lbl_BaudRate"));
				label4j_Baud.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Baud.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Baud.setBounds(0, 185, 149, 21);
				jDesktopPane1.add(label4j_Baud);

				JLabel4j_std label4j_DataBits = new JLabel4j_std();
				label4j_DataBits.setText(lang.get("lbl_DataBits"));
				label4j_DataBits.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_DataBits.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_DataBits.setBounds(0, 224, 149, 21);
				jDesktopPane1.add(label4j_DataBits);

				JLabel4j_std label4j_StopBits = new JLabel4j_std();
				label4j_StopBits.setText(lang.get("lbl_StopBits"));
				label4j_StopBits.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_StopBits.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_StopBits.setBounds(0, 260, 149, 21);
				jDesktopPane1.add(label4j_StopBits);

				JLabel4j_std label4j_FlowControl = new JLabel4j_std();
				label4j_FlowControl.setText(lang.get("lbl_FlowControl"));
				label4j_FlowControl.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_FlowControl.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_FlowControl.setBounds(0, 331, 149, 21);
				jDesktopPane1.add(label4j_FlowControl);
				
				JLabel4j_std label4j_Parity = new JLabel4j_std();
				label4j_Parity.setText(lang.get("lbl_Parity"));
				label4j_Parity.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Parity.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Parity.setBounds(0, 296, 149, 21);
				jDesktopPane1.add(label4j_Parity);
				
				comboBox_Parity = new JComboBox4j<String>();
				comboBox_Parity.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				comboBox_Parity.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Odd", "Even", "Mark", "Space"}));
				comboBox_Parity.setPreferredSize(new Dimension(40, 20));
				comboBox_Parity.setFocusCycleRoot(true);
				comboBox_Parity.setBounds(155, 293, 81, 24);
				jDesktopPane1.add(comboBox_Parity);
				
				JLabel4j_std label4j_EndOfLine = new JLabel4j_std();
				label4j_EndOfLine.setText(lang.get("lbl_EndOfLine"));
				label4j_EndOfLine.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_EndOfLine.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_EndOfLine.setBounds(0, 364, 149, 21);
				jDesktopPane1.add(label4j_EndOfLine);
				
				comboBox_EndOfLine = new JComboBox4j<String>();
				comboBox_EndOfLine.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonSave.setEnabled(true);
					}
				});
				comboBox_EndOfLine.setModel(new DefaultComboBoxModel<String>(new String[] {"CR/LF", "CR", "LF", "CR/LF/EOT"}));
				comboBox_EndOfLine.setPreferredSize(new Dimension(40, 20));
				comboBox_EndOfLine.setFocusCycleRoot(true);
				comboBox_EndOfLine.setBounds(155, 361, 81, 24);
				jDesktopPane1.add(comboBox_EndOfLine);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		scale.setScaleID(jTextFieldScale.getText());
		scale.setDescription(jTextFieldDescription.getText());
		scale.setMake(comboBox_Make.getSelectedItem().toString());
		scale.setModel(comboBox_Model.getSelectedItem().toString());
		int baud = Integer.valueOf(comboBox_Baud.getSelectedItem().toString());
		scale.setBaudRate(baud);
		int databits = Integer.valueOf(comboBox_DataBits.getSelectedItem().toString());
		scale.setDataBits(databits);
		
		switch (comboBox_StopBits.getSelectedItem().toString())
		{

		case "1":
			scale.setStopBits(1);
			break;
		case "2":
			scale.setStopBits(2);
			break;
		case "1.5":
			scale.setStopBits(3);
			break;
		default:
		}
			
		scale.setFlowControl(comboBox_FlowControl.getSelectedItem().toString());
		scale.setConnection(comboBox_Connection.getSelectedItem().toString());
		scale.setParity(comboBox_Parity.getSelectedItem().toString());
		scale.setEndOfLine(comboBox_EndOfLine.getSelectedItem().toString());

		scale.update();
		jButtonSave.setEnabled(false);
	}
}
