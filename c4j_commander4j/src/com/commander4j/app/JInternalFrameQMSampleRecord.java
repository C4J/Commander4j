package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMSampleRecord.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameQMSampleRecord is amend a sample record in the
 * APP_QM_SAMPLE table. table. The records were originally added to the table
 * via the form JInternalFrameQMSampleLabel.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMSampleRecord.jpg" >
 * 
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 */
public class JInternalFrameQMSampleRecord extends javax.swing.JInternalFrame
{
	private JTextField4j jTextFieldInspectionID;
	private JLabel4j_std lblInspectionID;
	private JTextField4j jTextFieldUserData1;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldSampleID;
	private JTextField4j jTextFieldActivityID;
	private JLabel4j_std lblProcessOrderDescription;
	private JTextField4j jTextFieldMaterialDescription;
	private JLabel4j_std lblMaterialDescription;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JLabel4j_std lblUserData1;
	private JTextField4j jTextFieldUserData2;
	private JTextField4j jTextFieldUserData3;
	private JTextField4j jTextFieldUserData4;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std lblProcessOrder;
	private JLabel4j_std lblActivityID;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std lblMaterial;
	private JLabel4j_std lblUserData2;
	private JLabel4j_std lblSampleID;
	private JDBQMSample sample = new JDBQMSample(Common.selectedHostID, Common.sessionID);
	private Long lsampleid;
	private JDBMaterial material = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDateControl sampleDate;
	private JComboBox4j<String> comboBoxPrintQueue = new JComboBox4j<String>();
	private PreparedStatement listStatement;
	private JSpinner4j spinnerCopies;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JButton4j jButtonLookup_Shift_Names;
	private JButton4j jButtonLookup_Packing_Line;

	public JInternalFrameQMSampleRecord(String samp)
	{
		super();
		initGUI();
		blankfields();

		jTextFieldSampleID.setText(samp);
		refresh();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldUserData1.requestFocus();
				jTextFieldUserData1.setCaretPosition(jTextFieldUserData1.getText().length());
				jTextFieldSampleID.setEnabled(false);

			}
		});

	}

	public JInternalFrameQMSampleRecord()
	{
		super();

		initGUI();
		blankfields();

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldSampleID.requestFocus();
				jTextFieldSampleID.setCaretPosition(jTextFieldSampleID.getText().length());
				
			}
		});

	}

	private void updateRecord()
	{
		if (sample.isValidSample(lsampleid))
		{
			sample.getProperties();
			sample.setSampleDate(JUtility.getTimestampFromDate(sampleDate.getDate()));
			sample.setUserData1(jTextFieldUserData1.getText());
			sample.setUserData2(jTextFieldUserData2.getText());
			sample.setUserData3(jTextFieldUserData3.getText());
			sample.setUserData4(jTextFieldUserData4.getText());
			sample.update();
		}
	}

	private void populatePrinterList(String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		LinkedList<String> tempPrinterList = JPrint.getPrinterNames();

		for (int j = 0; j < tempPrinterList.size(); j++)
		{
			defComboBoxMod.addElement(tempPrinterList.get(j));
		}

		int sel = defComboBoxMod.getIndexOf(defaultitem);
		ComboBoxModel<String> jList1Model = defComboBoxMod;
		comboBoxPrintQueue.setModel(jList1Model);
		comboBoxPrintQueue.setSelectedIndex(sel);

		if (JPrint.getNumberofPrinters() == 0)
		{
			comboBoxPrintQueue.setEnabled(false);
		} else
		{
			comboBoxPrintQueue.setEnabled(true);
		}
	}

	private void blankfields()
	{
		jTextFieldProcessOrder.setText("");
		jTextFieldMaterial.setText("");
		jTextFieldMaterialDescription.setText("");
		jTextFieldProcessOrderDescription.setText("");
		jTextFieldInspectionID.setText("");
		jTextFieldActivityID.setText("");
		jTextFieldUserData1.setText("");
		jTextFieldUserData2.setText("");
		jTextFieldUserData3.setText("");
		jTextFieldUserData4.setText("");
		sampleDate.setDate(JUtility.getSQLDate());
		sampleDate.setDisplayMode(JDateControl.mode_disable_not_visible);
		jButtonSave.setEnabled(false);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(471, 531));
			this.setBounds(0, 0, 540, 525);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
				{
					jTextFieldSampleID = new JTextField4j();
					jTextFieldSampleID.addKeyListener(new KeyAdapter()
					{
						public void keyReleased(final KeyEvent e)
						{
							refresh();
						}
					});
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jTextFieldSampleID);
					jTextFieldSampleID.setEditable(true);
					jTextFieldSampleID.setEnabled(true);
					jTextFieldSampleID.setBounds(147, 10, 119, 22);
				}
				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setEnabled(true);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setMnemonic(lang.getMnemonicChar());
					jButtonSave.setBounds(109, 431, 100, 32);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							updateRecord();
						}
					});
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(315, 431, 100, 32);
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				{
					lblSampleID = new JLabel4j_std();
					jDesktopPane1.add(lblSampleID);
					lblSampleID.setText(lang.get("lbl_SampleID"));
					lblSampleID.setBounds(7, 10, 133, 22);
					lblSampleID.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					lblUserData2 = new JLabel4j_std();
					jDesktopPane1.add(lblUserData2);
					lblUserData2.setBounds(7, 271, 133, 22);
					lblUserData2.setHorizontalAlignment(SwingConstants.TRAILING);
					lblUserData2.setText(lang.get("lbl_User_Data2"));
				}

				{
					lblMaterial = new JLabel4j_std();
					jDesktopPane1.add(lblMaterial);
					lblMaterial.setText(lang.get("lbl_Material"));
					lblMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMaterial.setBounds(7, 126, 133, 22);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(147, 126, 119, 22);
					jTextFieldMaterial.setEnabled(false);
				}
				{
					lblActivityID = new JLabel4j_std();
					jDesktopPane1.add(lblActivityID);
					lblActivityID.setText(lang.get("lbl_Activity_ID"));
					lblActivityID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblActivityID.setBounds(7, 213, 133, 22);
				}
				{
					jTextFieldActivityID = new JTextField4j(JDBQMActivity.field_activity_id);
					jDesktopPane1.add(jTextFieldActivityID);
					jTextFieldActivityID.setBounds(147, 213, 119, 22);
					jTextFieldActivityID.setEnabled(false);
				}

				{
					lblProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(lblProcessOrder);
					lblProcessOrder.setText(lang.get("lbl_Process_Order"));
					lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
					lblProcessOrder.setBounds(7, 68, 133, 22);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(147, 68, 119, 22);
					jTextFieldProcessOrder.setEnabled(false);

				}
				{
					jTextFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
					jTextFieldUserData2.setToolTipText("Custom Field USER_DATA_2");
					jTextFieldUserData2.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_2_EDITABLE", "true", "QM USER DATA 2 EDITABLE")));
					jTextFieldUserData2.setBounds(147, 271, 119, 22);
					jTextFieldUserData2.setFocusCycleRoot(true);
					jDesktopPane1.add(jTextFieldUserData2);
				}
				{
					lblUserData1 = new JLabel4j_std();
					jDesktopPane1.add(lblUserData1);
					lblUserData1.setText(lang.get("lbl_User_Data1"));
					lblUserData1.setHorizontalAlignment(SwingConstants.TRAILING);
					lblUserData1.setBounds(7, 242, 133, 22);
				}

				{
					jTextFieldProcessOrderDescription = new JTextField4j(JDBProcessOrder.field_description);
					jDesktopPane1.add(jTextFieldProcessOrderDescription);
					jTextFieldProcessOrderDescription.setBounds(147, 97, 287, 22);
					jTextFieldProcessOrderDescription.setEnabled(false);
				}
				{
					lblProcessOrderDescription = new JLabel4j_std();
					jDesktopPane1.add(lblProcessOrderDescription);
					lblProcessOrderDescription.setText(lang.get("lbl_Description"));
					lblProcessOrderDescription.setHorizontalAlignment(SwingConstants.TRAILING);
					lblProcessOrderDescription.setBounds(7, 97, 133, 22);
				}
				{
					lblMaterialDescription = new JLabel4j_std();
					jDesktopPane1.add(lblMaterialDescription);
					lblMaterialDescription.setText(lang.get("lbl_Description"));
					lblMaterialDescription.setHorizontalAlignment(SwingConstants.TRAILING);
					lblMaterialDescription.setBounds(7, 155, 133, 22);
				}
				{
					jTextFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
					jDesktopPane1.add(jTextFieldMaterialDescription);
					jTextFieldMaterialDescription.setBounds(147, 155, 287, 22);
					jTextFieldMaterialDescription.setEnabled(false);

				}
				{
					jStatusText = new JLabel4j_std();
					jDesktopPane1.add(jStatusText);
					jStatusText.setForeground(new java.awt.Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jStatusText.setBounds(0, 475, 530, 21);
				}

				{
					jTextFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
					jTextFieldUserData1.setToolTipText("Custom Field USER_DATA_1");
					jTextFieldUserData1.setFocusCycleRoot(true);
					jTextFieldUserData1.setBounds(147, 242, 119, 22);
					jTextFieldUserData1.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_1_EDITABLE", "true", "QM USER DATA 1 EDITABLE")));
					jDesktopPane1.add(jTextFieldUserData1);
				}

				{
					lblInspectionID = new JLabel4j_std();
					lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblInspectionID.setText(lang.get("lbl_Location_ID"));
					lblInspectionID.setBounds(7, 184, 133, 22);
					jDesktopPane1.add(lblInspectionID);
				}

				{
					jTextFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
					jTextFieldInspectionID.setBounds(147, 184, 119, 22);
					jTextFieldInspectionID.setEnabled(false);
					jDesktopPane1.add(jTextFieldInspectionID);
				}

				{
					JLabel4j_std lblSampleDate = new JLabel4j_std();
					lblSampleDate.setText(lang.get("lbl_Sample_Date"));
					lblSampleDate.setHorizontalAlignment(SwingConstants.TRAILING);
					lblSampleDate.setBounds(7, 39, 133, 22);
					jDesktopPane1.add(lblSampleDate);
				}

				{
					sampleDate = new JDateControl();
					sampleDate.setDisplayMode(JDateControl.mode_disable_not_visible);
					jDesktopPane1.add(sampleDate);
					sampleDate.setBounds(147, 39, 128, 22);
					sampleDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
					sampleDate.getEditor().setSize(87, 21);
				}
				{
					JButton4j btnPrint = new JButton4j(Common.icon_print_16x16);
					btnPrint.setText(lang.get("btn_Print"));
					btnPrint.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							updateRecord();
							Long sampleID = sample.generateSampleID();
							String pq = comboBoxPrintQueue.getSelectedItem().toString();
							buildSQL1Record(sampleID);
							JLaunchReport.runReport("RPT_SAMPLE_LABEL", listStatement, false, pq, Integer.valueOf(spinnerCopies.getValue().toString()), false);
							jStatusText.setText(Integer.valueOf(spinnerCopies.getValue().toString()) + " labels printed. " + sample.getSampleDate().toString());
						}
					});
					btnPrint.setMnemonic('0');
					btnPrint.setEnabled(true);
					btnPrint.setBounds(212, 431, 100, 32);
					jDesktopPane1.add(btnPrint);
				}

				comboBoxPrintQueue.setBounds(147, 358, 365, 22);
				jDesktopPane1.add(comboBoxPrintQueue);

				{
					spinnerCopies = new JSpinner4j();
					spinnerCopies.setBounds(147, 387, 37, 22);
					JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(spinnerCopies);
					spinnerCopies.setEditor(ne);
					jDesktopPane1.add(spinnerCopies);
				}

				JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Print_Queue"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(7, 358, 133, 22);
				jDesktopPane1.add(label4j_std);

				JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Number_Of_Labels"));
				label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std_1.setBounds(7, 387, 133, 22);
				jDesktopPane1.add(label4j_std_1);
				
				JLabel4j_std lblUserData3 = new JLabel4j_std();
				lblUserData3.setText(lang.get("lbl_User_Data3"));
				lblUserData3.setHorizontalAlignment(SwingConstants.TRAILING);
				lblUserData3.setBounds(7, 300, 133, 22);
				jDesktopPane1.add(lblUserData3);
				
				jTextFieldUserData3 = new JTextField4j(20);
				jTextFieldUserData3.setEditable(false);
				jTextFieldUserData3.setToolTipText("Custom Field USER_DATA_3");
				jTextFieldUserData3.setText("");
				jTextFieldUserData3.setFocusCycleRoot(true);
				jTextFieldUserData3.setBounds(147, 300, 119, 22);
				jTextFieldUserData3.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_3_EDITABLE", "true", "QM USER DATA 3 EDITABLE")));
				jDesktopPane1.add(jTextFieldUserData3);
				
				{
					jButtonLookup_Shift_Names = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookup_Shift_Names);
					jButtonLookup_Shift_Names.setBounds(265, 329, 22, 22);
					jButtonLookup_Shift_Names.setEnabled(true);
					jButtonLookup_Shift_Names.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.shiftNames())
							{
								jTextFieldUserData3.setText(JLaunchLookup.dlgResult);
								jTextFieldUserData3.setCaretPosition(jTextFieldUserData3.getText().length());

							}
						}
					});
				}
				
				{
					jButtonLookup_Packing_Line = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookup_Packing_Line);
					jButtonLookup_Packing_Line.setBounds(265, 300, 22, 22);
					jButtonLookup_Packing_Line.setEnabled(true);
					jButtonLookup_Packing_Line.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.packingLine())
							{
								jTextFieldUserData2.setText(JLaunchLookup.dlgResult);
								jTextFieldUserData2.setCaretPosition(jTextFieldUserData2.getText().length());

							}
						}
					});
				}
				
				JLabel4j_std lblUserData4 = new JLabel4j_std();
				lblUserData4.setText(lang.get("lbl_User_Data4"));
				lblUserData4.setHorizontalAlignment(SwingConstants.TRAILING);
				lblUserData4.setBounds(7, 329, 133, 22);
				jDesktopPane1.add(lblUserData4);
				
				jTextFieldUserData4 = new JTextField4j(20);
				jTextFieldUserData4.setToolTipText("Custom Field USER_DATA_4");
				jTextFieldUserData4.setText("");
				jTextFieldUserData4.setFocusCycleRoot(true);
				jTextFieldUserData4.setBounds(147, 329, 119, 22);
				jTextFieldUserData4.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_4_EDITABLE", "true", "QM USER DATA 4 EDITABLE")));
				jDesktopPane1.add(jTextFieldUserData4);

				populatePrinterList(JPrint.getDefaultPrinterQueueName());
				String numberOfLabels = ctrl.getKeyValueWithDefault("QM SAMPLE LABELS", "4", "Number of Labels per Sample");
				spinnerCopies.setValue(Integer.valueOf(numberOfLabels));

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void buildSQL1Record(Long sampleID)
	{

		JDBQuery.closeStatement(listStatement);

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBQMSample.selectWithLimit");

		query.addText(temp);

		query.addParamtoSQL("sample_id = ", sampleID);

		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void refresh()
	{
		try
		{
			lsampleid = Long.valueOf(jTextFieldSampleID.getText());
			sample.setSampleID(lsampleid);

			if (sample.isValidSample())
			{
				sample.getProperties();

				jTextFieldProcessOrder.setText(sample.getProcessOrder());
				jTextFieldMaterial.setText(sample.getMaterial());
				jTextFieldActivityID.setText(sample.getActivityID());
				jTextFieldInspectionID.setText(sample.getInspectionID());

				jTextFieldUserData1.setText(sample.getUserData1());
				jTextFieldUserData2.setText(sample.getUserData2());

				jTextFieldUserData3.setText(sample.getUserData3());
				jTextFieldUserData4.setText(sample.getUserData4());
				
				processOrder.getProcessOrderProperties(sample.getProcessOrder());
				jTextFieldProcessOrderDescription.setText(processOrder.getDescription());

				material.getMaterialProperties(sample.getMaterial());
				jTextFieldMaterialDescription.setText(material.getDescription());

				sampleDate.setDate(sample.getSampleDate());
				sampleDate.setDisplayMode(JDateControl.mode_disable_visible);

				jButtonSave.setEnabled(true);
				jStatusText.setText("Sample " + String.valueOf(sample.getSampleID()) + " retrieved.");
			} else
			{
				jStatusText.setText("Sample " + String.valueOf(sample.getSampleID()) + " does not exist.");
				blankfields();
			}
		} catch (Exception ex)
		{
			lsampleid = 0l;
			blankfields();
		}

	}
}
