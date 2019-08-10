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
import java.math.BigDecimal;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

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
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.db.JDBWTScale;
import com.commander4j.db.JDBWTTNE;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
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
	private JDBWTWorkstation workdb = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder orderdb = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBMaterialCustomerData matcustdb = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JDBWTProductGroups matgroupdb = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private JDBMaterial materialdb = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWTTNE tnedb = new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samplePointdb = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);	
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
	private JQuantityInput jTextField_SampleSize = new JQuantityInput(new BigDecimal("0.000"));
	private int lSampleSize = 0;

	private JList4j listResults = new JList4j();

	public JInternalFrameWTWeightCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		String workstation = JUtility.getClientName().toUpperCase();

		textField4j_WorkstationID.setText(workstation);

		String temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE SIZE", "5", "WEIGHT CHECK SAMPLE SIZE");
		lSampleSize = Integer.valueOf(temp);
		jTextField_SampleSize.setText(String.valueOf(lSampleSize));
		
		updateWorkstationInfo(workstation,true);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_WEIGHT_WORKSTATION where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_CAPTURE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
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
		// buildSQL();
		// populateList();
	}
	
	
	//		updateWorkstation 	-- > 	updateSamplePoint
	
	private boolean updateWorkstationInfo(String workstation,boolean lookup)
	{
		boolean result = false;
		String samplePoint = "";
		
		if (lookup == true)
		{
			if (workdb.getProperties(workstation))
			{
				result = true;
				samplePoint = workdb.getSamplePoint();
				textField4j_SamplePoint.setText(samplePoint);
				textField4j_ScaleID.setText(workdb.getScaleID());
				textField4j_ScalePort.setText(workdb.getScalePort());
			}
			else
			{
				result = false;
				textField4j_SamplePoint.setText("");
				textField4j_ScaleID.setText("");
				textField4j_ScalePort.setText("");
			}
		}
		else
		{
			result = false;
		}
		
		if (result == true)
		{
			textField4j_WorkstationID.setBackground(Color.WHITE);
			textField4j_ScaleID.setBackground(Color.WHITE);
			textField4j_ScalePort.setBackground(Color.WHITE);
		}
		else
		{
			textField4j_WorkstationID.setBackground(Color.YELLOW);
			textField4j_ScaleID.setBackground(Color.YELLOW);
			textField4j_ScalePort.setBackground(Color.YELLOW);
		}
		
		updateSamplePoint(samplePoint,result);

		return result;
	}
	
	private boolean updateSamplePoint(String samplePoint,boolean lookup)
	{
		boolean result = false;
		
		if (lookup == true)
		{
			if (samplePointdb.getProperties(samplePoint))
			{
				result = true;

			}
			else
			{
				result = false;

			}
		}
		else
		{
			result = false;
		}
		
		if (result == true)
		{
			textField4j_SamplePoint.setBackground(Color.WHITE);
		}
		else
		{
			textField4j_SamplePoint.setBackground(Color.YELLOW);
		}
		
		return result;
	}

	//	    updateOrderInfo 	-- >  	updateMaterialInfo
	
	
	private boolean updateOrderInfo(String orderNo,boolean lookup)
	{
		boolean result = false;
		
		String material = "";
		String customerID = "";
		String status = "";

		//Lookup is passed to indicate if previous step failed in which case there is no need to lookup date in this step.
		
		if (lookup == true)
		{
			if (orderdb.getProcessOrderProperties(orderNo))
			{
				result = true;
				material = orderdb.getMaterial();
				customerID = orderdb.getCustomerID();
				status = orderdb.getStatus();
				
			}
			else
			{
				result = false;
				material = "";
				customerID = "";
				status = "";

			}
		}
		else
		{
			result = false;
		}
		
		textField4j_OrderStatus.setText(status);
		jTextFieldMaterial.setText(material);
		
		if (result == true)
		{
			jTextFieldProcessOrder.setBackground(Color.WHITE);
			
			if (status.equals("Ready") || (status.equals("Running")))
			{
				textField4j_OrderStatus.setBackground(Color.WHITE);
			}
			else
			{
				textField4j_OrderStatus.setBackground(Color.RED);
			}
			
		}
		else
		{
			jTextFieldProcessOrder.setBackground(Color.YELLOW);
			textField4j_OrderStatus.setBackground(Color.YELLOW);
		}

		updateMaterialInfo(material,customerID,result);

		return result;
	}
	
	private boolean updateMaterialInfo(String material,String customer,boolean lookup)
	{
		boolean result = false;
		
		//Lookup is passed to indicate if previous step failed in which case there is no need to lookup date in this step.
		
		if (lookup == true)
		{
			if (materialdb.getMaterialProperties(material))
			{
				result = true;
				textField4j_Description.setText(materialdb.getDescription());
			}
			else
			{
				result = false;
			}
		}
		else
		{
			result = false;
		}
		
		if (result == true)
		{
			jTextFieldMaterial.setBackground(Color.WHITE);
			textField4j_Description.setBackground(Color.WHITE);
		}
		else
		{
			textField4j_Description.setText("");
			jTextFieldMaterial.setBackground(Color.YELLOW);
			textField4j_Description.setBackground(Color.YELLOW);
		}

		updateCustomerDataInfo(material,customer,"PRODUCT_GROUP",result);
		
		updateCustomerDataInfo(material,customer,"CONTAINER_CODE",result);

		return result;
	}


	private boolean updateCustomerDataInfo(String material,String customer,String key,boolean lookup)
	{
		boolean result = false;
		
		String data = "";
		String group="";
		
		if (lookup == true)
		{
			if (matcustdb.getMaterialCustomerDataProperties(material, customer, key))
			{
				result = true;
				data = matcustdb.getData();
				
				if (key.equals("PRODUCT_GROUP"))
				{
					textField4j_Material_Group.setText(data);
					group=data;
				}
				
				if (key.equals("CONTAINER_CODE"))
				{
					textField4j_Container_Code.setText(data);
				}
			}
			else
			{
				result = false;
				
				if (key.equals("PRODUCT_GROUP"))
				{
					textField4j_Material_Group.setText("");
				}
				
				if (key.equals("CONTAINER_CODE"))
				{
					textField4j_Container_Code.setText("");
				}
			}
		}
		else
		{
			result = false;
		}
		
		if (result == true)
		{
			if (key.equals("PRODUCT_GROUP"))
			{
				textField4j_Material_Group.setBackground(Color.WHITE);
			}
			if (key.equals("CONTAINER_CODE"))
			{
				textField4j_Container_Code.setBackground(Color.WHITE);
			}

		}
		else
		{
			if (key.equals("PRODUCT_GROUP"))
			{
				textField4j_Material_Group.setText("");
				textField4j_Material_Group.setBackground(Color.YELLOW);
			}
			if (key.equals("CONTAINER_CODE"))
			{
				textField4j_Container_Code.setText("");
				textField4j_Container_Code.setBackground(Color.YELLOW);
			}
		}
		
		if (key.equals("PRODUCT_GROUP"))
		{
			updateProductGroup(group,result);
		}

		return result;
	}

	private boolean updateProductGroup(String group,boolean lookup)
	{
		boolean result = false;
		BigDecimal nominal = new BigDecimal("0.000");
		String nominalUom = "";
		BigDecimal tare = new BigDecimal("0.000");
		String tarelUom = "";
		
		//Lookup is passed to indicate if previous step failed in which case there is no need to lookup date in this step.
		
		if (lookup == true)
		{
			if (matgroupdb.getMaterialGroupProperties(group))
			{
				result = true;
				nominal = matgroupdb.getNominalWeight();
				nominalUom = matgroupdb.getNominalUOM();
				tare = matgroupdb.getTareWeight();
				tarelUom = matgroupdb.getTareWeightUOM();
				
				jTextField_NominalWeight.setText(nominal.toString());
				jTextField_NominalWeight_UOM.setText(nominalUom);
				jTextField_TareWeight.setText(tare.toString());
				jTextField_TareWeight_UOM.setText(tarelUom);
			}
			else
			{
				result = false;
				nominal = new BigDecimal("0.000");
				nominalUom = "";
				tare  = new BigDecimal("0.000");
				tarelUom = "";
				

			}
		}
		else
		{
			result = false;
		}
		
		if (result == true)
		{
			jTextField_NominalWeight.setBackground(Color.WHITE);
			jTextField_NominalWeight_UOM.setBackground(Color.WHITE);
			jTextField_TareWeight.setBackground(Color.WHITE);
			jTextField_TareWeight_UOM.setBackground(Color.WHITE);

		}
		else
		{
			jTextField_NominalWeight.setText("0.000");
			jTextField_NominalWeight_UOM.setText("");
			jTextField_TareWeight.setText("0.000");
			jTextField_TareWeight_UOM.setText("");
			
			jTextField_NominalWeight.setBackground(Color.YELLOW);
			jTextField_NominalWeight_UOM.setBackground(Color.YELLOW);
			jTextField_TareWeight.setBackground(Color.YELLOW);
			jTextField_TareWeight_UOM.setBackground(Color.YELLOW);
		}

		updateTNEInfo(nominal,nominalUom,result);
		
		return result;
	}

	private boolean updateTNEInfo(BigDecimal findNominal,String findNominalUOM,boolean lookup)
	{
		boolean result = false;
		

		BigDecimal negt1 = new BigDecimal("0.000");
		BigDecimal negt2 = new BigDecimal("0.000");
		BigDecimal tne = new BigDecimal("0.000");

		
		//Lookup is passed to indicate if previous step failed in which case there is no need to lookup date in this step.
		
		if (lookup == true)
		{
			if (tnedb.getProperties(findNominal, findNominalUOM))
			{
				result = true;
				

				negt1 = tnedb.getNegT1();
				negt2 = tnedb.getNegT2();
				tne = tnedb.getTNE();
				
				
				jTextField_T1.setText(negt1.toString());
				jTextField_T2.setText(negt2.toString());
				jTextField_TNE.setText(tne.toString());
			}
			else
			{
				result = false;
				
				negt1  	= new BigDecimal("0.000");
				negt2  	= new BigDecimal("0.000");
				tne  	= new BigDecimal("0.000");
			}
		}
		else
		{
			result = false;
		}
		
		
		jTextField_T1.setText(negt1.toString());
		jTextField_T2.setText(negt2.toString());
		jTextField_TNE.setText(tne.toString());
		
		if (result == true)
		{
		
			jTextField_T1.setBackground(Color.WHITE);
			jTextField_T2.setBackground(Color.WHITE);
			jTextField_TNE.setBackground(Color.WHITE);

		}
		else
		{
			jTextField_T1.setBackground(Color.WHITE);
			jTextField_T2.setBackground(Color.WHITE);
			jTextField_TNE.setBackground(Color.WHITE);
		}

		return result;
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
					jTextFieldProcessOrder.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyReleased(KeyEvent e)
						{
							updateOrderInfo(jTextFieldProcessOrder.getText(),true);
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
							JLaunchLookup.dlgCriteriaDefault = workdb.getRequiredResource();
							if (JLaunchLookup.processOrdersResources())
							{
								jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
								updateOrderInfo(JLaunchLookup.dlgResult,true);
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
					jStatusText.setBounds(0, 610, 1006, 2122);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}
				textField4j_Description.setEnabled(false);
				textField4j_Description.setEditable(false);

				textField4j_Description.setBounds(643, 55, 331, 25);
				jDesktopPane1.add(textField4j_Description);

				JLabel4j_std label4j_WorkstationID = new JLabel4j_std();
				label4j_WorkstationID.setText(lang.get("lbl_Workstation"));
				label4j_WorkstationID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_WorkstationID.setBounds(0, 18, 96, 25);
				jDesktopPane1.add(label4j_WorkstationID);
				textField4j_WorkstationID.setEditable(false);
				textField4j_WorkstationID.setEnabled(false);
				textField4j_WorkstationID.setDisabledTextColor(Color.BLACK);
				textField4j_WorkstationID.setBounds(104, 18, 142, 25);

				jDesktopPane1.add(textField4j_WorkstationID);

				JLabel4j_std label4j_SamplePoint = new JLabel4j_std();
				label4j_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				label4j_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_SamplePoint.setBounds(224, 18, 108, 25);
				jDesktopPane1.add(label4j_SamplePoint);
				textField4j_SamplePoint.setEnabled(false);
				textField4j_SamplePoint.setEditable(false);
				textField4j_SamplePoint.setBounds(344, 18, 93, 25);
				jDesktopPane1.add(textField4j_SamplePoint);
				textField4j_ScaleID.setEnabled(false);
				textField4j_ScaleID.setEditable(false);
				textField4j_ScaleID.setBounds(538, 18, 93, 25);
				jDesktopPane1.add(textField4j_ScaleID);

				JLabel4j_std label4j_ScaleID = new JLabel4j_std();
				label4j_ScaleID.setText(lang.get("lbl_Scale_ID"));
				label4j_ScaleID.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_ScaleID.setBounds(438, 18, 94, 25);
				jDesktopPane1.add(label4j_ScaleID);
				textField4j_ScalePort.setEnabled(false);
				textField4j_ScalePort.setEditable(false);
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
				label4j_Container_Code.setBounds(224, 92, 108, 25);
				jDesktopPane1.add(label4j_Container_Code);
				textField4j_Container_Code.setEnabled(false);
				textField4j_Container_Code.setEditable(false);

				textField4j_Container_Code.setBounds(346, 92, 93, 25);
				jDesktopPane1.add(textField4j_Container_Code);

				button4j_NewSample.setText("btn_New");
				button4j_NewSample.setMnemonic('0');
				button4j_NewSample.setBounds(351, 566, 126, 32);
				jDesktopPane1.add(button4j_NewSample);

				JLabel4j_std label4j__OrderStatus = new JLabel4j_std();
				label4j__OrderStatus.setText(lang.get("lbl_Process_Order_Status"));
				label4j__OrderStatus.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j__OrderStatus.setBounds(224, 55, 108, 25);
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
				jLabel_TareWeight.setBounds(722, 92, 108, 25);
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

				JLabel4j_std label4j_T2 = new JLabel4j_std();
				label4j_T2.setText(lang.get("lbl_T2_Lower_Limit"));
				label4j_T2.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_T2.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_T2.setBounds(722, 129, 108, 25);
				jDesktopPane1.add(label4j_T2);

				jTextField_T2.setVerifyInputWhenFocusTarget(false);
				jTextField_T2.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_T2.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_T2.setEditable(false);
				jTextField_T2.setBounds(839, 129, 73, 25);
				jDesktopPane1.add(jTextField_T2);

				JLabel4j_std label4j_SampleSize = new JLabel4j_std();
				label4j_SampleSize.setText(lang.get("lbl_SampleSize"));
				label4j_SampleSize.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_SampleSize.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_SampleSize.setBounds(0, 129, 96, 25);
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
				label4j_TNE.setBounds(224, 129, 108, 25);
				jDesktopPane1.add(label4j_TNE);

				jTextField_TNE.setVerifyInputWhenFocusTarget(false);
				jTextField_TNE.setHorizontalAlignment(SwingConstants.TRAILING);
				jTextField_TNE.setFont(new Font("Arial", Font.PLAIN, 11));
				jTextField_TNE.setEditable(false);
				jTextField_TNE.setBounds(364, 129, 73, 25);
				jDesktopPane1.add(jTextField_TNE);

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(617, 213, 365, 313);
				jDesktopPane1.add(scrollPane);
				scrollPane.setViewportView(listResults);

				JLabel lbl_Legend = new JLabel("  Gross Wt   Tare Wt    Net Wt       T1       T2");
				lbl_Legend.setFont(new Font("Monospaced", Font.BOLD, 11));
				lbl_Legend.setBounds(617, 197, 365, 15);
				jDesktopPane1.add(lbl_Legend);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
