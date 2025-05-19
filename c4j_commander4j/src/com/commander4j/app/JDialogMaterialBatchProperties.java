package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialBatchProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameMaterialBatchProperties class allows you to update a record
 * in the APP_MATERIAL_BATCH table and is called from the parent form
 * JInternalFrameMaterialBatchAdmin
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialBatchProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterialBatch JDBMaterialBatch
 */
public class JDialogMaterialBatchProperties extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private String lmaterial;
	private String lbatch;
	private JDBMaterialBatch materialbatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JDBMaterial materialdb = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1;
	private JDateControl dateTimePicker = new JDateControl();
	private JDBLanguage lang;
	private JCalendarButton calendarButton;
	private String expiryMode;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);


	public JDialogMaterialBatchProperties(JFrame frame,String material, String batch)
	{
		super();		
		
		lmaterial = material;
		lbatch = batch;
		
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
		expiryMode = ctrl.getKeyValue("EXPIRY DATE MODE");
		
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_BATCH_EDIT"));

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Batch Properties");
		this.setResizable(false);
		this.setSize(358, 207);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);
		
		materialdb.getMaterialProperties(material);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jComboBoxStatus.requestFocus();
			}
		});

	}

	private void initGUI()
	{
		try
		{

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setBounds(7, 130, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							try
							{
								materialbatch.setStatus((String) jComboBoxStatus.getSelectedItem());
							} catch (Exception e)
							{
								materialbatch.setStatus("");
							}
							Date d = dateTimePicker.getDate();

							materialbatch.setExpiryDate(JUtility.getTimestampFromDate(d));
							if (materialbatch.isValidMaterialBatch())
							{
								materialbatch.update();
							} else
							{
								materialbatch.create();
							}
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(119, 130, 112, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(231, 130, 112, 32);
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1.setBounds(7, 13, 112, 22);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setText(lmaterial);
					jTextFieldMaterial.setBounds(126, 13, 150, 22);
					jTextFieldMaterial.setEnabled(false);
					jTextFieldMaterial.setEditable(false);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Material_Batch"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(7, 41, 112, 22);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setText(lbatch);
					jTextFieldBatch.setBounds(126, 41, 150, 22);
					jTextFieldBatch.setEnabled(false);
					jTextFieldBatch.setEditable(false);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(7, 97, 112, 22);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.batchStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(126, 69, 150, 22);
					jComboBoxStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Batch_Status"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(7, 69, 112, 22);
				}

				{
					dateTimePicker.getEditor().addKeyListener(new KeyAdapter()
					{
						public void keyPressed(KeyEvent e)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					dateTimePicker.addChangeListener(new ChangeListener()
					{
						public void stateChanged(final ChangeEvent e)
						{
							Date exp = materialdb.getRoundedExpiryDate(dateTimePicker.getDate());
							dateTimePicker.setDate(exp);
							jButtonUpdate.setEnabled(true);
						}
					});

					// dateTimePicker.setEditable(true);
					dateTimePicker.setBounds(126, 97, 150, 25);
					if (expiryMode.equals("SSCC"))
					{
						dateTimePicker.setEnabled(false);
					}
					else
					{
						dateTimePicker.setEnabled(true);
					}
					
					dateTimePicker.setDisplayMode(JDateControl.mode_disable_visible);
					jDesktopPane1.add(dateTimePicker);
				}
				{
					calendarButton = new JCalendarButton(dateTimePicker);
					calendarButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					calendarButton.setBounds(255, 97, 22, 22);
					jDesktopPane1.add(calendarButton);
				}
				

				
				jTextFieldMaterial.setText(lmaterial);
				jTextFieldBatch.setText(lbatch);

				materialbatch.setMaterial(lmaterial);
				materialbatch.setBatch(lbatch);
				jTextFieldMaterial.setText(materialbatch.getMaterial());
				jTextFieldBatch.setText(materialbatch.getBatch());

				if (materialbatch.getMaterialBatchProperties())
				{
					jComboBoxStatus.setSelectedItem(materialbatch.getStatus());
					try
					{
						dateTimePicker.setDate(materialbatch.getExpiryDate());
					} catch (Exception e)
					{

					}
					jButtonUpdate.setEnabled(false);
				} else
				{
					JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
					mat.getMaterialProperties(lmaterial);
					jComboBoxStatus.setSelectedItem(mat.getDefaultBatchStatus());

					Date de = JUtility.getSQLDateTime();
					try
					{
						dateTimePicker.setDate(mat.calcBBE(de, mat.getShelfLife(), mat.getShelfLifeUom(), mat.getShelfLifeRule()));
					} catch (Exception e)
					{

					}
					jButtonUpdate.setEnabled(true);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
