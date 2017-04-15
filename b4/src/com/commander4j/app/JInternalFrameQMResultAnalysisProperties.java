package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameProcessOrderProperties.java
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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMAnalysis;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;

/**
 * The JInternalFrameQMResultAnalysisProperties is used editing a record in the
 * APP_QM_ANALYSIS table.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMResultAnalysisProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBQMAnalysis JDBQMAnalysis
 */
public class JInternalFrameQMResultAnalysisProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelAnalysisID;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldBatchSuffixParam;
	private JTextField4j jTextFieldProcessOrderParam;
	private JTextField4j jTextFieldMaterialParam;
	private JTextField4j jTextFieldResourceParam;
	private JTextField4j jTextFieldUserData1Param;
	private JTextField4j jTextFieldUserData2Param;
	private JTextField4j jTextFieldUserData3Param;
	private JTextField4j jTextFieldUserData4Param;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldAnalysisID;
	private String lanalysisID;
	private JDBQMAnalysis analysis = new JDBQMAnalysis(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j chckbxBatchSuffixReqd;
	private JCheckBox4j chckbxProcessOrderReqd;
	private JCheckBox4j chckbxMaterialReqd;
	private JCheckBox4j chckbxResourceReqd;
	private JCheckBox4j chckbxUserData1Reqd;
	private JCheckBox4j chckbxUserData2Reqd;
	private JCheckBox4j chckbxUserData3Reqd;
	private JCheckBox4j chckbxUserData4Reqd;
	private JComboBox4j<JDBListData> comboBox4jModuleID = new JComboBox4j<JDBListData>();
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private Vector<JDBListData> moduleList = new Vector<JDBListData>();
	private JSpinner spinnerSequence = new JSpinner();
	private JCheckBox4j chckbxDateFromReqd;
	private JCheckBox4j chckbxDateToReqd;
	private JTextField4j jTextFieldDateFromParam;
	private JTextField4j jTextFieldDateToParam ;

	public JInternalFrameQMResultAnalysisProperties(String id)
	{
		super();

		moduleList.add(null);
		moduleList.addAll(mod.getModuleIdsByType("USER"));

		initGUI();
		load(id);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_QM_RESULT_ANALYSIS_EDIT"));

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
			{
				this.setPreferredSize(new java.awt.Dimension(448, 289));
				this.setBounds(25, 25, 501, 547);
				setVisible(true);
				this.setClosable(true);
				this.setIconifiable(true);

				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);

				jButtonSave = new JButton4j(Common.icon_update);
				jDesktopPane1.add(jButtonSave);
				jButtonSave.setText(lang.get("btn_Save"));
				jButtonSave.setBounds(59, 465, 112, 32);
				jButtonSave.setEnabled(false);
				jButtonSave.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{

						save();
					}
				});

				jLabelAnalysisID = new JLabel4j_std();
				jDesktopPane1.add(jLabelAnalysisID);
				jLabelAnalysisID.setText(lang.get("lbl_Analysis_ID"));
				jLabelAnalysisID.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelAnalysisID.setBounds(12, 12, 144, 21);

				jTextFieldAnalysisID = new JTextField4j(JDBProcessOrder.field_process_order);
				jDesktopPane1.add(jTextFieldAnalysisID);
				jTextFieldAnalysisID.setEditable(false);
				jTextFieldAnalysisID.setEnabled(false);
				jTextFieldAnalysisID.setBounds(178, 12, 126, 21);

				jLabelDescription = new JLabel4j_std();
				jDesktopPane1.add(jLabelDescription);
				jLabelDescription.setText(lang.get("lbl_Description"));
				jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelDescription.setBounds(12, 45, 144, 21);

				jTextFieldDescription = new JTextField4j(JDBProcessOrder.field_description);
				jDesktopPane1.add(jTextFieldDescription);
				jTextFieldDescription.setBounds(178, 45, 301, 21);
				jTextFieldDescription.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldBatchSuffixParam = new JTextField4j();
				jDesktopPane1.add(jTextFieldBatchSuffixParam);
				jTextFieldBatchSuffixParam.setBounds(171, 228, 301, 21);
				jTextFieldBatchSuffixParam.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldProcessOrderParam = new JTextField4j();
				jDesktopPane1.add(jTextFieldProcessOrderParam);
				jTextFieldProcessOrderParam.setBounds(171, 257, 301, 21);
				jTextFieldProcessOrderParam.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldMaterialParam = new JTextField4j();
				jDesktopPane1.add(jTextFieldMaterialParam);
				jTextFieldMaterialParam.setBounds(171, 286, 301, 21);
				jTextFieldMaterialParam.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldResourceParam = new JTextField4j();
				jDesktopPane1.add(jTextFieldResourceParam);
				jTextFieldResourceParam.setBounds(171, 313, 301, 21);
				jTextFieldResourceParam.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldUserData1Param = new JTextField4j();
				jDesktopPane1.add(jTextFieldUserData1Param);
				jTextFieldUserData1Param.setBounds(171, 340, 301, 21);
				jTextFieldUserData1Param.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldUserData2Param = new JTextField4j();
				jDesktopPane1.add(jTextFieldUserData2Param);
				jTextFieldUserData2Param.setBounds(171, 367, 301, 21);
				jTextFieldUserData2Param.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldUserData3Param = new JTextField4j();
				jDesktopPane1.add(jTextFieldUserData3Param);
				jTextFieldUserData3Param.setBounds(171, 394, 301, 21);
				jTextFieldUserData3Param.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jTextFieldUserData4Param = new JTextField4j();
				jDesktopPane1.add(jTextFieldUserData4Param);
				jTextFieldUserData4Param.setBounds(171, 423, 301, 21);
				jTextFieldUserData4Param.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonSave.setEnabled(true);
					}
				});

				jButtonHelp = new JButton4j(Common.icon_help);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setBounds(178, 465, 112, 32);

				jButtonClose = new JButton4j(Common.icon_close);
				jDesktopPane1.add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setBounds(297, 465, 112, 32);
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						dispose();
					}
				});

				chckbxBatchSuffixReqd = new JCheckBox4j(lang.get("lbl_Batch_Suffix"));
				chckbxBatchSuffixReqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxBatchSuffixReqd.setBounds(12, 228, 155, 21);
				jDesktopPane1.add(chckbxBatchSuffixReqd);

				chckbxProcessOrderReqd = new JCheckBox4j(lang.get("lbl_Process_Order"));
				chckbxProcessOrderReqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxProcessOrderReqd.setBounds(12, 257, 155, 21);
				jDesktopPane1.add(chckbxProcessOrderReqd);

				chckbxMaterialReqd = new JCheckBox4j(lang.get("lbl_Material"));
				chckbxMaterialReqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxMaterialReqd.setBounds(12, 286, 155, 21);
				jDesktopPane1.add(chckbxMaterialReqd);

				chckbxResourceReqd = new JCheckBox4j(lang.get("lbl_Process_Order_Required_Resource"));
				chckbxResourceReqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxResourceReqd.setBounds(12, 313, 155, 21);
				jDesktopPane1.add(chckbxResourceReqd);

				chckbxUserData1Reqd = new JCheckBox4j(lang.get("lbl_User_Data1"));
				chckbxUserData1Reqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxUserData1Reqd.setBounds(12, 340, 155, 21);
				jDesktopPane1.add(chckbxUserData1Reqd);

				chckbxUserData2Reqd = new JCheckBox4j(lang.get("lbl_User_Data2"));
				chckbxUserData2Reqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxUserData2Reqd.setBounds(12, 367, 155, 21);
				jDesktopPane1.add(chckbxUserData2Reqd);

				chckbxUserData3Reqd = new JCheckBox4j(lang.get("lbl_User_Data3"));
				chckbxUserData3Reqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxUserData3Reqd.setBounds(12, 394, 155, 21);
				jDesktopPane1.add(chckbxUserData3Reqd);

				chckbxUserData4Reqd = new JCheckBox4j(lang.get("lbl_User_Data4"));
				chckbxUserData4Reqd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				chckbxUserData4Reqd.setBounds(12, 423, 155, 21);
				jDesktopPane1.add(chckbxUserData4Reqd);

				ComboBoxModel<JDBListData> jComboBox1Model = new DefaultComboBoxModel<JDBListData>(moduleList);
				comboBox4jModuleID.setModel(jComboBox1Model);
				comboBox4jModuleID.setEnabled(false);
				comboBox4jModuleID.setBounds(178, 110, 231, 21);
				jDesktopPane1.add(comboBox4jModuleID);
				spinnerSequence.setFont(new Font("Arial", Font.PLAIN, 11));
				spinnerSequence.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				spinnerSequence.setBounds(178, 78, 49, 20);
				jDesktopPane1.add(spinnerSequence);

				JLabel4j_std jLabelSequence = new JLabel4j_std();
				jLabelSequence.setText("lbl_Sequence");
				jLabelSequence.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelSequence.setBounds(12, 78, 144, 21);
				jDesktopPane1.add(jLabelSequence);

				JLabel4j_std jLabelModule = new JLabel4j_std();
				jLabelModule.setText(lang.get("lbl_Module_ID"));
				jLabelModule.setHorizontalAlignment(SwingConstants.TRAILING);
				jLabelModule.setBounds(12, 110, 144, 21);
				jDesktopPane1.add(jLabelModule);

				JLabel4j_std jLabelRequired = new JLabel4j_std();
				jLabelRequired.setFont(new Font("Arial", Font.BOLD, 11));
				jLabelRequired.setText("lbl_Required");
				jLabelRequired.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelRequired.setBounds(27, 143, 144, 21);
				jDesktopPane1.add(jLabelRequired);

				JLabel4j_std jLabelParameter = new JLabel4j_std();
				jLabelParameter.setFont(new Font("Arial", Font.BOLD, 11));
				jLabelParameter.setText("lbl_Parameter");
				jLabelParameter.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelParameter.setBounds(178, 143, 144, 21);
				jDesktopPane1.add(jLabelParameter);
				
				chckbxDateFromReqd = new JCheckBox4j(lang.get("lbl_Sample_Date"+ " >="));
				chckbxDateFromReqd.setBounds(12, 172, 155, 21);
				jDesktopPane1.add(chckbxDateFromReqd);
				
				chckbxDateToReqd = new JCheckBox4j(lang.get("lbl_Sample_Date"+ " <="));
				chckbxDateToReqd.setBounds(12, 201, 155, 21);
				jDesktopPane1.add(chckbxDateToReqd);
				
				jTextFieldDateFromParam = new JTextField4j();
				jTextFieldDateFromParam.setBounds(171, 172, 301, 21);
				jDesktopPane1.add(jTextFieldDateFromParam);
				
				jTextFieldDateToParam = new JTextField4j();
				jTextFieldDateToParam.setBounds(171, 201, 301, 21);
				jDesktopPane1.add(jTextFieldDateToParam);				

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void load(String lanalysisID)
	{
		if (analysis.getAnalysisProperties(lanalysisID))
		{
			jTextFieldDescription.setText(analysis.getDescription());
			spinnerSequence.setValue(analysis.getDisplaySequence());
			chckbxBatchSuffixReqd.setSelected(analysis.isBatchSuffixReqd());
			chckbxProcessOrderReqd.setSelected(analysis.isProcessOrderReqd());
			chckbxMaterialReqd.setSelected(analysis.isMaterialReqd());
			chckbxResourceReqd.setSelected(analysis.isResourceReqd());
			chckbxUserData1Reqd.setSelected(analysis.isUserData1Reqd());
			chckbxUserData2Reqd.setSelected(analysis.isUserData2Reqd());
			chckbxUserData3Reqd.setSelected(analysis.isUserData3Reqd());
			chckbxUserData4Reqd.setSelected(analysis.isUserData4Reqd());
			chckbxDateFromReqd.setSelected(analysis.isSampleDateStartReqd());
			chckbxDateToReqd.setSelected(analysis.isSampleDateEndReqd());
			
			jTextFieldDescription.setText(analysis.getDescription());
			jTextFieldProcessOrderParam.setText(analysis.getProcessOrderParam());
			jTextFieldMaterialParam.setText(analysis.getMaterialParam());
			jTextFieldResourceParam.setText(analysis.getResourceParam());
			jTextFieldUserData1Param.setText(analysis.getUserData1Param());
			jTextFieldUserData2Param.setText(analysis.getUserData2Param());
			jTextFieldUserData3Param.setText(analysis.getUserData3Param());
			jTextFieldUserData4Param.setText(analysis.getUserData4Param());
			jTextFieldDateFromParam.setText(analysis.getSampleDateStartParam());
			jTextFieldDateToParam.setText(analysis.getSampleDateEndParam());
			
			for (int x = 1; x < moduleList.size(); x++)
			{
				if (moduleList.get(x).getmData().equals(analysis.getModuleID()))
				{
					comboBox4jModuleID.setSelectedIndex(x);
				}
			}

			jButtonSave.setEnabled(false);
		}
	}

	private void save()
	{
		analysis.setAnalysisID(lanalysisID);
		analysis.setDescription(jTextFieldDescription.getText());

		if (analysis.isValidAnalysis() == false)
		{
			analysis.create(lanalysisID);
		}
		if (analysis.update())
		{
			jButtonSave.setEnabled(false);
		} else
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, analysis.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
		}

	}
}
