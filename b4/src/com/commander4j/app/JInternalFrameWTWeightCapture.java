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
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.db.JDBWTScale;
import com.commander4j.db.JDBWTTNE;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBMaterialBatchTableModel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
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
public class JInternalFrameWTWeightCapture extends JInternalFrame
{
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

	private PreparedStatement listStatement;
	private JTextField4j textField4j_Description = new JTextField4j(JDBMaterial.field_description);
	private JTextField4j textField4j_WorkstationID = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
	private JTextField4j textField4j_ScaleID = new JTextField4j(JDBWTScale.field_ScaleID);
	private JTextField4j textField4j_ScalePort = new JTextField4j(JDBWTWorkstation.field_ScalePort);
	private JTextField4j textField4j_Material_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField4j textField4j_Container_Code = new JTextField4j(JDBQMSample.field_data_2);
	private JButton4j button4j_NewSample = new JButton4j(Common.icon_add_16x16);
	private JDBWTWorkstation  workdb= new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder  orderdb= new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBMaterialCustomerData matcustdb = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JDBWTProductGroups matgroupdb = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private JDBMaterial  materialdb= new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWTTNE  tnedb= new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JTextField4j textField4j_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JTextField4j textField4j_OrderStatus = new JTextField4j(JDBProcessOrder.field_status);
	private JQuantityInput jTextField_NominalWeight = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput jTextField_TareWeight = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j jTextField_NominalWeight_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_TareWeight_UOM = new JTextField4j(JDBUom.field_uom);
	private JQuantityInput jTextField_T1 = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput jTextField_T2 = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput jTextField_TNE = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j jTextField_T1_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_T2_UOM = new JTextField4j(JDBUom.field_uom);
	private JQuantityInput jTextField_SampleSize = new JQuantityInput(new BigDecimal("0.000"));
	private int lSampleSize = 0;
	
	public JInternalFrameWTWeightCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		String workstation = JUtility.getClientName().toUpperCase();
		textField4j_WorkstationID.setText(workstation);
		
		String temp = ctrl.getKeyValueWithDefault("WEIGHT SAMLPLE SIZE", "5", "WEIGHT CHECK SAMPLE SIZE");
		lSampleSize = Integer.valueOf(temp);
		jTextField_SampleSize.setText(String.valueOf(lSampleSize));
		
		
		workdb.getProperties(workstation);
		textField4j_SamplePoint.setEnabled(false);
		textField4j_SamplePoint.setEditable(false);
		textField4j_SamplePoint.setText(workdb.getSamplePoint());
		textField4j_ScaleID.setEnabled(false);
		textField4j_ScaleID.setEditable(false);
		textField4j_ScaleID.setText(workdb.getScaleID());
		textField4j_ScalePort.setEnabled(false);
		textField4j_ScalePort.setEditable(false);
		textField4j_ScalePort.setText(workdb.getScalePort());
		
		
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_CAPTURE"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jTextFieldProcessOrder.requestFocus();
				jTextFieldProcessOrder.setCaretPosition(jTextFieldProcessOrder.getText().length());
				
			}
		});

	}

	public JInternalFrameWTWeightCapture(String material)
	{
		this();
		lmaterial = material;
		jTextFieldProcessOrder.setText(lmaterial);
		jTextFieldMaterial.setText(lbatch);
		buildSQL();
		populateList();
	}


	public JInternalFrameWTWeightCapture(String material, String batch)
	{
		this();
		lmaterial = material;
		lbatch = batch;
		jTextFieldProcessOrder.setText(lmaterial);
		jTextFieldMaterial.setText(lbatch);
//		buildSQL();
//		populateList();
	}

	private void updateOrderInfo()
	{
		String order = jTextFieldProcessOrder.getText();
		if (orderdb.getProcessOrderProperties(order))
		{
			jTextFieldMaterial.setText(orderdb.getMaterial());
			textField4j_OrderStatus.setText(orderdb.getStatus());
			
			if (textField4j_OrderStatus.getText().equals("Ready") || (textField4j_OrderStatus.getText().equals("Running")))
			{
				textField4j_OrderStatus.setBackground(Color.WHITE);
			}
			else
			{
				textField4j_OrderStatus.setBackground(Color.RED);
			}
			
			if (materialdb.getMaterialProperties(orderdb.getMaterial()))
			{
				textField4j_Description.setText(materialdb.getDescription());
			}
			if (matcustdb.getMaterialCustomerDataProperties(orderdb.getMaterial(),orderdb.getCustomerID(),"PRODUCT_GROUP"))
			{
				textField4j_Material_Group.setText(matcustdb.getData());
				if (matgroupdb.getMaterialGroupProperties(matcustdb.getData()))
				{
						jTextField_NominalWeight.setText(matgroupdb.getNominalWeight().toString());
						jTextField_NominalWeight_UOM.setText(matgroupdb.getNominalUOM());
						jTextField_TareWeight.setText(matgroupdb.getTareWeight().toString());
						jTextField_TareWeight_UOM.setText(matgroupdb.getTareWeightUOM());
						if (tnedb.getProperties(matgroupdb.getNominalWeight(), matgroupdb.getNominalUOM()))
						{
							jTextField_T1.setText(tnedb.getNegT1().toString());
							jTextField_T2.setText(tnedb.getNegT2().toString());
							jTextField_T1_UOM.setText(tnedb.getNominalWTUOM());
							jTextField_T2_UOM.setText(tnedb.getNominalWTUOM());
							jTextField_TNE.setText(tnedb.getTNE().toString());
						}
						else
						{
							jTextField_T1.setText("0.000");
							jTextField_T2.setText("0.000");
							jTextField_TNE.setText("0.000");
							jTextField_T1_UOM.setText("");
							jTextField_T2_UOM.setText("");
						}
				}
				else
				{
					jTextField_NominalWeight.setText("0.000");
					jTextField_TareWeight.setText("0.000");
					jTextField_TareWeight.setText("");
					jTextField_TareWeight_UOM.setText("");
				}
			}
			else
			{
				textField4j_Material_Group.setText("");
			}
			if (matcustdb.getMaterialCustomerDataProperties(orderdb.getMaterial(),orderdb.getCustomerID(),"CONTAINER_CODE"))
			{
				textField4j_Container_Code.setText(matcustdb.getData());
			}
			else
			{
				textField4j_Container_Code.setText("");
			}
						
		}
		
	}
	
	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_WEIGHT_SAMPLE_HEADER ORDER BY WEIGHT_DATE"));
		query.addParamtoSQL("sample_point=", jTextFieldProcessOrder.getText());
		query.addParamtoSQL("sample_date=", jTextFieldMaterial.getText());

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
					jScrollPane_Weights.setBounds(511, 209, 440, 277);
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
					jButtonHelp.setBounds(489, 566, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(617, 566, 126, 32);
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
					jLabel_ProcessOrder.setText(lang.get("lbl_Process_Order"));
					jLabel_ProcessOrder.setBounds(0, 55, 96, 25);
					jLabel_ProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
					jTextFieldProcessOrder.addKeyListener(new KeyAdapter() {
						@Override
						public void keyReleased(KeyEvent e) {
							updateOrderInfo();
						}
					});
					jDesktopPane1.add(jTextFieldProcessOrder);
					jTextFieldProcessOrder.setBounds(104, 55, 93, 25);
				}
				{
					jLabel_Material = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Material);
					jLabel_Material.setText(lang.get("lbl_Material"));
					jLabel_Material.setBounds(438, 55, 94, 25);
					jLabel_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jTextFieldMaterial.setEnabled(false);
					jTextFieldMaterial.setEditable(false);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(538, 55, 93, 25);
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
								updateOrderInfo();
							}
						}
					});
					jButtonLookupProcessOrder.setBounds(196, 54, 21, 25);
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
				textField4j_Description.setEnabled(false);
				textField4j_Description.setEditable(false);


				textField4j_Description.setBounds(752, 55, 222, 25);
				jDesktopPane1.add(textField4j_Description);

				JLabel4j_std label4j_Description = new JLabel4j_std();
				label4j_Description.setText(lang.get("lbl_Description"));
				label4j_Description.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Description.setBounds(636, 55, 107, 25);
				jDesktopPane1.add(label4j_Description);

				JLabel4j_std label4j_WorkstationID = new JLabel4j_std();
				label4j_WorkstationID.setText(lang.get("lbl_Workstation"));
				label4j_WorkstationID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_WorkstationID.setBounds(0, 18, 96, 25);
				jDesktopPane1.add(label4j_WorkstationID);
				textField4j_WorkstationID.setEditable(false);

				textField4j_WorkstationID.setBounds(104, 18, 93, 25);
				jDesktopPane1.add(textField4j_WorkstationID);

				JLabel4j_std label4j_SamplePoint = new JLabel4j_std();
				label4j_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				label4j_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_SamplePoint.setBounds(206, 18, 126, 25);
				jDesktopPane1.add(label4j_SamplePoint);
				textField4j_SamplePoint.setBounds(344, 18, 93, 25);
				jDesktopPane1.add(textField4j_SamplePoint);

				textField4j_ScaleID.setBounds(538, 18, 93, 25);
				jDesktopPane1.add(textField4j_ScaleID);

				JLabel4j_std label4j_ScaleID = new JLabel4j_std();
				label4j_ScaleID.setText(lang.get("lbl_Scale_ID"));
				label4j_ScaleID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_ScaleID.setBounds(438, 18, 94, 25);
				jDesktopPane1.add(label4j_ScaleID);

				textField4j_ScalePort.setBounds(752, 18, 222, 25);
				jDesktopPane1.add(textField4j_ScalePort);

				JLabel4j_std label4j_ScalePort = new JLabel4j_std();
				label4j_ScalePort.setText(lang.get("lbl_Scale_Port"));
				label4j_ScalePort.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_ScalePort.setBounds(636, 18, 94, 25);
				jDesktopPane1.add(label4j_ScalePort);

				JLabel4j_std label4j_Material_Group = new JLabel4j_std();
				label4j_Material_Group.setText(lang.get("lbl_Material_Group"));
				label4j_Material_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Material_Group.setBounds(0, 92, 96, 25);
				jDesktopPane1.add(label4j_Material_Group);
				textField4j_Material_Group.setEnabled(false);
				textField4j_Material_Group.setEditable(false);

				textField4j_Material_Group.setBounds(104, 92, 93, 25);
				jDesktopPane1.add(textField4j_Material_Group);

				JLabel4j_std label4j_Container_Code = new JLabel4j_std();
				label4j_Container_Code.setText(lang.get("lbl_Container_Code"));
				label4j_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_Container_Code.setBounds(206, 92, 126, 25);
				jDesktopPane1.add(label4j_Container_Code);
				textField4j_Container_Code.setEnabled(false);
				textField4j_Container_Code.setEditable(false);

				textField4j_Container_Code.setBounds(346, 92, 93, 25);
				jDesktopPane1.add(textField4j_Container_Code);
				
				button4j_NewSample.setText("btn_Add");
				button4j_NewSample.setMnemonic('0');
				button4j_NewSample.setBounds(351, 566, 126, 32);
				jDesktopPane1.add(button4j_NewSample);
				
				JLabel4j_std label4j__OrderStatus = new JLabel4j_std();
				label4j__OrderStatus.setText(lang.get("lbl_Process_Order_Status"));
				label4j__OrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j__OrderStatus.setBounds(215, 55, 117, 25);
				jDesktopPane1.add(label4j__OrderStatus);
				textField4j_OrderStatus.setEnabled(false);
				textField4j_OrderStatus.setEditable(false);
				
				textField4j_OrderStatus.setText("");
				textField4j_OrderStatus.setBounds(344, 55, 93, 25);
				jDesktopPane1.add(textField4j_OrderStatus);
				jTextField_NominalWeight.setEditable(false);
				
				jTextField_NominalWeight.setVerifyInputWhenFocusTarget(false);
				jTextField_NominalWeight.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_NominalWeight.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_NominalWeight.setBounds(583, 92, 73, 25);
				jDesktopPane1.add(jTextField_NominalWeight);
				jTextField_NominalWeight_UOM.setEnabled(false);
				jTextField_NominalWeight_UOM.setEditable(false);

				
	
				jTextField_NominalWeight_UOM.setText("");
				jTextField_NominalWeight_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_NominalWeight_UOM.setFocusCycleRoot(true);
				jTextField_NominalWeight_UOM.setCaretPosition(0);
				jTextField_NominalWeight_UOM.setBounds(668, 92, 50, 25);
				jDesktopPane1.add(jTextField_NominalWeight_UOM);
				jTextField_TareWeight.setEditable(false);
				
				jTextField_TareWeight.setVerifyInputWhenFocusTarget(false);
				jTextField_TareWeight.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_TareWeight.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_TareWeight.setBounds(839, 92, 73, 25);
				jDesktopPane1.add(jTextField_TareWeight);
				jTextField_TareWeight_UOM.setEnabled(false);
				jTextField_TareWeight_UOM.setEditable(false);


				
				jTextField_TareWeight_UOM.setText("");
				jTextField_TareWeight_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_TareWeight_UOM.setFocusCycleRoot(true);
				jTextField_TareWeight_UOM.setCaretPosition(0);
				jTextField_TareWeight_UOM.setBounds(924, 92, 50, 25);
				jDesktopPane1.add(jTextField_TareWeight_UOM);

				JLabel4j_std jLabel_NominalWeight = new JLabel4j_std();
				jLabel_NominalWeight.setText(lang.get("lbl_Nominal_Weight"));
				jLabel_NominalWeight.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_NominalWeight.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_NominalWeight.setBounds(448, 92, 126, 25);
				jDesktopPane1.add(jLabel_NominalWeight);
				
				JLabel4j_std jLabel_TareWeight = new JLabel4j_std();
				jLabel_TareWeight.setText(lang.get("lbl_Tare_Weight"));
				jLabel_TareWeight.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_TareWeight.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_TareWeight.setBounds(712, 92, 118, 25);
				jDesktopPane1.add(jLabel_TareWeight);
				
				JLabel4j_std label4j_T1 = new JLabel4j_std();
				label4j_T1.setText(lang.get("lbl_T1_Lower_Limit"));
				label4j_T1.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_T1.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_T1.setBounds(448, 129, 126, 25);
				jDesktopPane1.add(label4j_T1);
				
				jTextField_T1.setVerifyInputWhenFocusTarget(false);
				jTextField_T1.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_T1.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_T1.setEditable(false);
				jTextField_T1.setBounds(583, 129, 73, 25);
				jDesktopPane1.add(jTextField_T1);
				
				jTextField_T1_UOM.setText("");
				jTextField_T1_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_T1_UOM.setFocusCycleRoot(true);
				jTextField_T1_UOM.setEnabled(false);
				jTextField_T1_UOM.setEditable(false);
				jTextField_T1_UOM.setCaretPosition(0);
				jTextField_T1_UOM.setBounds(668, 129, 50, 25);
				jDesktopPane1.add(jTextField_T1_UOM);
				
				JLabel4j_std label4j_T2 = new JLabel4j_std();
				label4j_T2.setText(lang.get("lbl_T2_Lower_Limit"));
				label4j_T2.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_T2.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_T2.setBounds(712, 129, 118, 25);
				jDesktopPane1.add(label4j_T2);
				
				jTextField_T2.setVerifyInputWhenFocusTarget(false);
				jTextField_T2.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_T2.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_T2.setEditable(false);
				jTextField_T2.setBounds(839, 129, 73, 25);
				jDesktopPane1.add(jTextField_T2);
				
				jTextField_T2_UOM.setText("");
				jTextField_T2_UOM.setPreferredSize(new Dimension(40, 20));
				jTextField_T2_UOM.setFocusCycleRoot(true);
				jTextField_T2_UOM.setEnabled(false);
				jTextField_T2_UOM.setEditable(false);
				jTextField_T2_UOM.setCaretPosition(0);
				jTextField_T2_UOM.setBounds(924, 129, 50, 25);
				jDesktopPane1.add(jTextField_T2_UOM);
				
				JLabel4j_std label4j_SampleSize = new JLabel4j_std();
				label4j_SampleSize.setText(lang.get("lbl_SampleSize"));
				label4j_SampleSize.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_SampleSize.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_SampleSize.setBounds(23, 129, 73, 25);
				jDesktopPane1.add(label4j_SampleSize);
				

				jTextField_SampleSize.setVerifyInputWhenFocusTarget(false);
				jTextField_SampleSize.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_SampleSize.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_SampleSize.setEditable(false);
				jTextField_SampleSize.setBounds(104, 129, 38, 25);
				jDesktopPane1.add(jTextField_SampleSize);

				JLabel4j_std label4j_TNE = new JLabel4j_std();
				label4j_TNE.setText(lang.get("lbl_TNE"));
				label4j_TNE.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_TNE.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_TNE.setBounds(206, 129, 126, 25);
				jDesktopPane1.add(label4j_TNE);
				

				jTextField_TNE.setVerifyInputWhenFocusTarget(false);
				jTextField_TNE.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_TNE.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_TNE.setEditable(false);
				jTextField_TNE.setBounds(364, 129, 73, 25);
				jDesktopPane1.add(jTextField_TNE);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
