package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramMaterialBatchAdmin.java
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBWTScale;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBMaterialBatchTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTDataCapture is for capturing/recording weight checks
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTDataCapture.jpg" >
 * 
 * @see com.commander4j.db.JDBWTSampleHeader JDBWTSampleHeader
 * @see com.commander4j.db.JDBWTSampleDetail JDBWTSampleDetail
 */
public class JInternalFrameWTDataCapture extends JInternalFrame
{
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonLookupProcessOrder;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel_Material;
	private JTextField4j jTextFieldProcessOrder;
	private JLabel4j_std jLabel_ProcessOrder;
	private JTable4j jTable_Weights;
	private JButton4j jButtonHelp;
	private JScrollPane jScrollPane_Weights;
	private String lmaterial;
	private String lbatch;

	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private JCalendarButton calendarButtonexpiryFrom;
	private JCalendarButton calendarButtonexpiryTo;
	private PreparedStatement listStatement;
	private JTextField4j textField4j_Description = new JTextField4j(JDBMaterial.field_description);
	private JTextField4j textField4j_WorkstationID = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
	private JTextField4j textField4j_ScaleID = new JTextField4j(JDBWTScale.field_ScaleID);
	private JTextField4j textField4j_ScalePort = new JTextField4j(JDBWTWorkstation.field_ScalePort);
	private JTextField4j textField4j_User_Data_1 = new JTextField4j(JDBQMSample.field_data_1);
	private JTextField4j textField4j_User_Data_2 = new JTextField4j(JDBQMSample.field_data_2);
	private JTextField4j textField4j_User_Data_3 = new JTextField4j(JDBQMSample.field_data_3);
	private JTextField4j textField4j_User_Data_4 = new JTextField4j(JDBQMSample.field_data_4);
	private JButton4j button4j_NewSample = new JButton4j(Common.icon_add_16x16);

	public JInternalFrameWTDataCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_BATCH"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	public JInternalFrameWTDataCapture(String material)
	{
		this();
		lmaterial = material;
		jTextFieldProcessOrder.setText(lmaterial);
		jTextFieldMaterial.setText(lbatch);
		buildSQL();
		populateList();
	}

	private void excel()
	{
		JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("material_batch.xls", materialBatch.getMaterialBatchDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	public JInternalFrameWTDataCapture(String material, String batch)
	{
		this();
		lmaterial = material;
		lbatch = batch;
		jTextFieldProcessOrder.setText(lmaterial);
		jTextFieldMaterial.setText(lbatch);
		buildSQL();
		populateList();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_BATCH"));
		query.addParamtoSQL("material=", jTextFieldProcessOrder.getText());
		query.addParamtoSQL("batch_number=", jTextFieldMaterial.getText());

		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateList()
	{
		JDBMaterialBatch materialBatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
		JDBMaterialBatchTableModel materialBatchTable = new JDBMaterialBatchTableModel(materialBatch.getMaterialBatchDataResultSet(listStatement));
		TableRowSorter<JDBMaterialBatchTableModel> sorter = new TableRowSorter<JDBMaterialBatchTableModel>(materialBatchTable);

		jTable_Weights.setRowSorter(sorter);
		jTable_Weights.setModel(materialBatchTable);

		jScrollPane_Weights.setViewportView(jTable_Weights);
		JUtility.scrolltoHomePosition(jScrollPane_Weights);
		jTable_Weights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable_Weights.getColumnModel().getColumn(0).setPreferredWidth(95);
		jTable_Weights.getColumnModel().getColumn(1).setPreferredWidth(105);
		jTable_Weights.getColumnModel().getColumn(2).setPreferredWidth(105);
		jTable_Weights.getColumnModel().getColumn(3).setPreferredWidth(120);

		jScrollPane_Weights.repaint();
	}

	private void editRecord()
	{
		int row = jTable_Weights.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable_Weights.getValueAt(row, 0).toString();
			lbatch = jTable_Weights.getValueAt(row, 1).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_BATCH_EDIT", lmaterial, lbatch);
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1016, 666);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Weight Checks");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				{
					jScrollPane_Weights = new JScrollPane();
					jScrollPane_Weights.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane_Weights);
					jScrollPane_Weights.setBounds(503, 200, 440, 277);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable_Weights = new JTable4j();

						jScrollPane_Weights.setViewportView(jTable_Weights);
						jTable_Weights.setModel(jTable1Model);

						jTable_Weights.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_BATCH_EDIT"))
									{
										editRecord();
									}
								}
							}
						});
					}
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(430, 566, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(558, 566, 126, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jLabel_ProcessOrder = new JLabel4j_std();
					jDesktopPane1.add(jLabel_ProcessOrder);
					jLabel_ProcessOrder.setText(lang.get("lbl_ProcessOrder"));
					jLabel_ProcessOrder.setBounds(0, 59, 120, 21);
					jLabel_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(126, 55, 96, 25);
				}
				{
					jLabel_Material = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Material);
					jLabel_Material.setText(lang.get("lbl_Material"));
					jLabel_Material.setBounds(245, 59, 73, 21);
					jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(324, 55, 126, 25);
				}

				{
					jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
					jButtonLookupProcessOrder.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.processOrders())
							{
								jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupProcessOrder.setBounds(223, 55, 21, 25);
					jDesktopPane1.add(jButtonLookupProcessOrder);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 615, 1000, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(299, 566, 126, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				{
					calendarButtonexpiryFrom.setEnabled(false);
					calendarButtonexpiryFrom.setBounds(265, 126, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryFrom);
				}
				{
					calendarButtonexpiryTo.setEnabled(false);
					calendarButtonexpiryTo.setBounds(265, 166, 21, 21);
					jDesktopPane1.add(calendarButtonexpiryTo);
				}

				textField4j_Description.setBounds(543, 57, 431, 25);
				jDesktopPane1.add(textField4j_Description);

				JLabel4j_std label4j_Description = new JLabel4j_std();
				label4j_Description.setText(lang.get("lbl_Description"));
				label4j_Description.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Description.setBounds(455, 59, 82, 21);
				jDesktopPane1.add(label4j_Description);

				JLabel4j_std label4j_WorkstationID = new JLabel4j_std();
				label4j_WorkstationID.setText(lang.get("lbl_Workstation"));
				label4j_WorkstationID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_WorkstationID.setBounds(0, 22, 120, 21);
				jDesktopPane1.add(label4j_WorkstationID);

				textField4j_WorkstationID.setBounds(126, 18, 118, 25);
				jDesktopPane1.add(textField4j_WorkstationID);

				JLabel4j_std label4j_SamplePoint = new JLabel4j_std();
				label4j_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				label4j_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_SamplePoint.setBounds(245, 22, 73, 21);
				jDesktopPane1.add(label4j_SamplePoint);

				JTextField4j textField4j_SamplePoint = new JTextField4j(25);
				textField4j_SamplePoint.setBounds(324, 18, 118, 25);
				jDesktopPane1.add(textField4j_SamplePoint);

				textField4j_ScaleID.setBounds(543, 18, 118, 25);
				jDesktopPane1.add(textField4j_ScaleID);

				JLabel4j_std label4j_ScaleID = new JLabel4j_std();
				label4j_ScaleID.setText(lang.get("lbl_ScaleID"));
				label4j_ScaleID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_ScaleID.setBounds(464, 22, 73, 21);
				jDesktopPane1.add(label4j_ScaleID);

				textField4j_ScalePort.setBounds(752, 18, 222, 25);
				jDesktopPane1.add(textField4j_ScalePort);

				JLabel4j_std label4j_ScalePort = new JLabel4j_std();
				label4j_ScalePort.setText(lang.get("lbl_ScalePort"));
				label4j_ScalePort.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_ScalePort.setBounds(664, 22, 82, 21);
				jDesktopPane1.add(label4j_ScalePort);

				JLabel4j_std label4j_User_Data_1 = new JLabel4j_std();
				label4j_User_Data_1.setText(lang.get("lbl_User_Data_1"));
				label4j_User_Data_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_User_Data_1.setBounds(0, 96, 120, 21);
				jDesktopPane1.add(label4j_User_Data_1);

				textField4j_User_Data_1.setBounds(126, 92, 118, 25);
				jDesktopPane1.add(textField4j_User_Data_1);

				JLabel4j_std label4j_User_Data_2 = new JLabel4j_std();
				label4j_User_Data_2.setText(lang.get("lbl_User_Data_2"));
				label4j_User_Data_2.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_User_Data_2.setBounds(245, 96, 73, 21);
				jDesktopPane1.add(label4j_User_Data_2);

				textField4j_User_Data_2.setBounds(324, 92, 126, 25);
				jDesktopPane1.add(textField4j_User_Data_2);

				JLabel4j_std label4j_User_Data_3 = new JLabel4j_std();
				label4j_User_Data_3.setText(lang.get("lbl_User_Data_3"));
				label4j_User_Data_3.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_User_Data_3.setBounds(464, 96, 73, 21);
				jDesktopPane1.add(label4j_User_Data_3);

				textField4j_User_Data_3.setBounds(543, 92, 126, 25);
				jDesktopPane1.add(textField4j_User_Data_3);

				JLabel4j_std label4j_User_Data_4 = new JLabel4j_std();
				label4j_User_Data_4.setText(lang.get("lbl_User_Data_4"));
				label4j_User_Data_4.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_User_Data_4.setBounds(673, 96, 73, 21);
				jDesktopPane1.add(label4j_User_Data_4);

				textField4j_User_Data_4.setBounds(750, 92, 126, 25);
				jDesktopPane1.add(textField4j_User_Data_4);
				

				button4j_NewSample.setText("btn_Add");
				button4j_NewSample.setMnemonic('0');
				button4j_NewSample.setBounds(118, 129, 126, 32);
				jDesktopPane1.add(button4j_NewSample);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
