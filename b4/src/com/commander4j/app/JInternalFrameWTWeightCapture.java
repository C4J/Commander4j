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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import com.commander4j.app.JInternalFrameProductionDeclaration.ClockListener;
import com.commander4j.bar.JEANBarcode;
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
import com.commander4j.db.JDBWTSampleDetail;
import com.commander4j.db.JDBWTSampleHeader;
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
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JQuantityInput;
import com.commander4j.util.JUtility;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

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

	private static final long serialVersionUID = 1;
	private JButton4j btn_Begin = new JButton4j(Common.icon_add_16x16);
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btnj_Cancel = new JButton4j(Common.icon_cancel_16x16);
	private ClockListener clocklistener = new ClockListener();

	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JTextField4j fld_Container_Code = new JTextField4j(JDBQMSample.field_data_2);
	private JDateControl fld_currentDateTime;
	private JTextField4j fld_Description = new JTextField4j(JDBMaterial.field_description);

	private JTextField4j fld_Material;
	private JTextField4j fld_Material_Group = new JTextField4j(JDBWTProductGroups.field_product_group);

	private JQuantityInput fld_Nominal_Weight = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Nominal_Weight_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j fld_Process_Order;
	private JTextField4j fld_Process_Order_Status = new JTextField4j(JDBProcessOrder.field_status);
	private JQuantityInput fld_SampleFrequency = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JQuantityInput fld_SampleSize = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Scale_ID = new JTextField4j(JDBWTScale.field_ScaleID);
	private JTextField4j fld_ScalePort = new JTextField4j(JDBWTWorkstation.field_ScalePort);
	private JQuantityInput fld_T1_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_T2_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Tare_Weight = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Tare_Weight_UOM = new JTextField4j(JDBUom.field_uom);
	private JQuantityInput fld_TNE = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Workstation = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
	private JDesktopPane jDesktopPane1;
	private JScrollPane scrollPane_Weights = new JScrollPane();
	private JLabel4j_std jStatusText;
	private JDBLanguage lang;
	private String lbatch;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private JList4j<JDBWTSampleDetail> list_Weights = new JList4j<JDBWTSampleDetail>();
	private PreparedStatement listStatement;
	private String lmaterial;
	private boolean logEnabled = false;
	private int lSampleFrequency = 15;
	private int lSampleSize = 0;
	private JDBMaterialCustomerData matcustdb = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JDBMaterial materialdb = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWTProductGroups matgroupdb = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder orderdb = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private LinkedList<JDBWTSampleDetail> sampleDetailList = new LinkedList<JDBWTSampleDetail>();
	private JDBWTSampleHeader sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samplePointdb = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private Integer sampleSequence = 0;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private Timer timer = new Timer(1000, clocklistener);
	private JDBWTTNE tnedb = new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDBWTWorkstation workdb = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);

	private BigDecimal zero = new BigDecimal("0.000");

	public JInternalFrameWTWeightCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();
		timer.start();
		String workstation = JUtility.getClientName().toUpperCase();

		fld_Workstation.setText(workstation);

		String temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE SIZE", "5", "WEIGHT CHECK SAMPLE SIZE");
		lSampleSize = Integer.valueOf(temp);
		fld_SampleSize.setText(String.valueOf(lSampleSize));

		temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE FREQUENCY", "15", "WEIGHT CHECK FREQUENCY MINS");
		lSampleFrequency = Integer.valueOf(temp);
		fld_SampleFrequency.setText(String.valueOf(lSampleFrequency));

		updateWorkstationInfo(workstation, true);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_WEIGHT_WORKSTATION where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();

		populateList();
		drawGraph();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn_Help, JUtility.getHelpSetIDforModule("FRM_WEIGHT_CAPTURE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fld_Process_Order.requestFocus();
				fld_Process_Order.setCaretPosition(fld_Process_Order.getText().length());

				JButton btnDebug = new JButton("Debug");
				btnDebug.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						switch (sampleDetailList.size())
						{
						case 0:
							logSampleWeight("100.3", "G");
							break;
						case 1:
							logSampleWeight("101.1", "G");
							break;
						case 2:
							logSampleWeight("101.4", "G");
							break;
						case 3:
							logSampleWeight("96.7", "G");
							break;
						case 4:
							logSampleWeight("90.2", "G");
							break;
						case 5:
							logSampleWeight("102.1", "G");
							break;
						case 6:
							logSampleWeight("95.3", "G");
							break;
						case 7:
							logSampleWeight("98.3", "G");
							break;
						case 8:
							logSampleWeight("100.9", "G");
							break;
						case 9:
							logSampleWeight("96.8", "G");
							break;
						case 10:
							logSampleWeight("91.2", "G");
							break;
						case 11:
							logSampleWeight("102.5", "G");
							break;
						default:
							logSampleWeight("95.9", "G");
							break;
						}

						populateList();
					}
				});
				btnDebug.setBounds(617, 173, 106, 25);
				jDesktopPane1.add(btnDebug);

			}
		});

		addInternalFrameListener(new InternalFrameAdapter()
		{

			public void internalFrameClosing(InternalFrameEvent e)
			{
				shutdown();
			}
		});

	}

	private void shutdown()
	{
		timer.stop();

		while (timer.isRunning())
		{
		}

		timer = null;
	}
	
	public class ClockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Calendar rightNow = Calendar.getInstance();
			Date d = rightNow.getTime();

			try
			{

				fld_currentDateTime.setDate(d);

			}
			catch (Exception e)
			{
			}
		}
	}

	public JInternalFrameWTWeightCapture(String material)
	{
		this();
		lmaterial = material;
		fld_Process_Order.setText(lmaterial);
		fld_Material.setText(lbatch);
		buildSQL();
		populateList();
	}

	public JInternalFrameWTWeightCapture(String material, String batch)
	{
		this();
		lmaterial = material;
		lbatch = batch;
		fld_Process_Order.setText(lmaterial);
		fld_Material.setText(lbatch);
		// buildSQL();
		// populateList();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_WEIGHT_SAMPLE_HEADER ORDER BY WEIGHT_DATE"));
		query.addParamtoSQL("sample_point=", fld_Process_Order.getText());
		query.addParamtoSQL("sample_date=", fld_Material.getText());

		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	// updateWorkstation -- > updateSamplePoint

	private XYDataset createDataset()
	{

		DefaultXYDataset ds = new DefaultXYDataset();

		double[][] gross =
		{
				{ 1, 2, 3 },
				{ 206, 204, 205 } };
		double[][] net =
		{
				{ 1, 2, 3 },
				{ 204, 202, 203 } };

		ds.addSeries("gross", gross);
		ds.addSeries("net", net);

		return ds;
	}

	private void drawGraph()
	{
		XYDataset ds = createDataset();
		JFreeChart chart = ChartFactory.createXYLineChart("Weights Graph", "x", "y", ds, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel cp = new ChartPanel(chart);

		cp.setBounds(18, 232, 587, 320);

		jDesktopPane1.add(cp);

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
					btn_Help = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(btn_Help);
					btn_Help.setText(lang.get("btn_Help"));
					btn_Help.setMnemonic(java.awt.event.KeyEvent.VK_H);
					btn_Help.setBounds(623, 566, 126, 32);
				}
				{
					btn_Close = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(btn_Close);
					btn_Close.setText(lang.get("btn_Close"));
					btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
					btn_Close.setBounds(751, 566, 126, 32);
					btn_Close.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							shutdown();
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					lbl_Process_Order = new JLabel4j_std();
					jDesktopPane1.add(lbl_Process_Order);
					lbl_Process_Order.setText(lang.get("lbl_Process_Order"));
					lbl_Process_Order.setBounds(0, 55, 96, 25);
					lbl_Process_Order.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					fld_Process_Order = new JTextField4j(JDBProcessOrder.field_process_order);
					fld_Process_Order.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyReleased(KeyEvent e)
						{
							updateOrderInfo(fld_Process_Order.getText(), true);
						}
					});
					jDesktopPane1.add(fld_Process_Order);
					fld_Process_Order.setBounds(104, 55, 93, 25);
				}
				{
					lbl_Material = new JLabel4j_std();
					jDesktopPane1.add(lbl_Material);
					lbl_Material.setText(lang.get("lbl_Material"));
					lbl_Material.setBounds(438, 55, 94, 25);
					lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					fld_Material = new JTextField4j(JDBMaterialBatch.field_batch_number);
					fld_Material.setEnabled(false);
					fld_Material.setEditable(false);
					jDesktopPane1.add(fld_Material);
					fld_Material.setBounds(538, 55, 93, 25);
				}

				{
					btn_Process_Order_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_Process_Order_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = workdb.getRequiredResource();
							if (JLaunchLookup.processOrdersResources())
							{
								fld_Process_Order.setText(JLaunchLookup.dlgResult);
								updateOrderInfo(JLaunchLookup.dlgResult, true);
							}
						}
					});
					btn_Process_Order_Lookup.setBounds(196, 54, 21, 25);
					jDesktopPane1.add(btn_Process_Order_Lookup);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 610, 1006, 2122);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}
				fld_Description.setEnabled(false);
				fld_Description.setEditable(false);

				fld_Description.setBounds(643, 55, 331, 25);
				jDesktopPane1.add(fld_Description);

				JLabel4j_std lbl_Workstation = new JLabel4j_std();
				lbl_Workstation.setText(lang.get("lbl_Workstation"));
				lbl_Workstation.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Workstation.setBounds(0, 18, 96, 25);
				jDesktopPane1.add(lbl_Workstation);
				fld_Workstation.setEditable(false);
				fld_Workstation.setEnabled(false);
				fld_Workstation.setDisabledTextColor(Color.BLACK);
				fld_Workstation.setBounds(104, 18, 142, 25);

				jDesktopPane1.add(fld_Workstation);

				JLabel4j_std lbl_SamplePoint = new JLabel4j_std();
				lbl_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				lbl_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_SamplePoint.setBounds(224, 18, 108, 25);
				jDesktopPane1.add(lbl_SamplePoint);
				fld_SamplePoint.setEnabled(false);
				fld_SamplePoint.setEditable(false);
				fld_SamplePoint.setBounds(344, 18, 93, 25);
				jDesktopPane1.add(fld_SamplePoint);
				fld_Scale_ID.setEnabled(false);
				fld_Scale_ID.setEditable(false);
				fld_Scale_ID.setBounds(538, 18, 93, 25);
				jDesktopPane1.add(fld_Scale_ID);

				JLabel4j_std lbl_Scale_ID = new JLabel4j_std();
				lbl_Scale_ID.setText(lang.get("lbl_Scale_ID"));
				lbl_Scale_ID.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Scale_ID.setBounds(438, 18, 94, 25);
				jDesktopPane1.add(lbl_Scale_ID);
				fld_ScalePort.setEnabled(false);
				fld_ScalePort.setEditable(false);
				fld_ScalePort.setBounds(752, 18, 222, 25);
				jDesktopPane1.add(fld_ScalePort);

				JLabel4j_std lbl_Scale_Port = new JLabel4j_std();
				lbl_Scale_Port.setText(lang.get("lbl_Scale_Port"));
				lbl_Scale_Port.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Scale_Port.setBounds(636, 18, 94, 25);
				jDesktopPane1.add(lbl_Scale_Port);

				JLabel4j_std lbl_Material_Group = new JLabel4j_std();
				lbl_Material_Group.setText(lang.get("lbl_Material_Group"));
				lbl_Material_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Material_Group.setBounds(0, 92, 96, 25);
				jDesktopPane1.add(lbl_Material_Group);
				fld_Material_Group.setEnabled(false);
				fld_Material_Group.setEditable(false);

				fld_Material_Group.setBounds(104, 92, 93, 25);
				jDesktopPane1.add(fld_Material_Group);

				JLabel4j_std lbl_Container_Code = new JLabel4j_std();
				lbl_Container_Code.setText(lang.get("lbl_Container_Code"));
				lbl_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Container_Code.setBounds(224, 92, 108, 25);
				jDesktopPane1.add(lbl_Container_Code);
				fld_Container_Code.setEnabled(false);
				fld_Container_Code.setEditable(false);

				fld_Container_Code.setBounds(346, 92, 93, 25);
				jDesktopPane1.add(fld_Container_Code);
				btn_Begin.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fld_currentDateTime.setDate(JUtility.getSQLDateTime());
						if (readyToLog())
						{
							btn_Begin.setEnabled(false);
							btnj_Cancel.setEnabled(true);
							logEnabled = true;
							sampleSequence = 0;
							sampleDetailList.clear();
							populateList();
						}
					}
				});

				btn_Begin.setText(lang.get("lbl_Begin_Weight_Check"));
				btn_Begin.setMnemonic('0');
				btn_Begin.setBounds(177, 566, 220, 32);
				jDesktopPane1.add(btn_Begin);

				JLabel4j_std lbl_Process_Order_Status = new JLabel4j_std();
				lbl_Process_Order_Status.setText(lang.get("lbl_Process_Order_Status"));
				lbl_Process_Order_Status.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Process_Order_Status.setBounds(224, 55, 108, 25);
				jDesktopPane1.add(lbl_Process_Order_Status);
				fld_Process_Order_Status.setEnabled(false);
				fld_Process_Order_Status.setEditable(false);

				fld_Process_Order_Status.setText("");
				fld_Process_Order_Status.setBounds(344, 55, 93, 25);
				jDesktopPane1.add(fld_Process_Order_Status);
				fld_Nominal_Weight.setEditable(false);

				fld_Nominal_Weight.setVerifyInputWhenFocusTarget(false);
				fld_Nominal_Weight.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_Nominal_Weight.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_Nominal_Weight.setBounds(583, 92, 73, 25);
				jDesktopPane1.add(fld_Nominal_Weight);
				fld_Nominal_Weight_UOM.setEnabled(false);
				fld_Nominal_Weight_UOM.setEditable(false);

				fld_Nominal_Weight_UOM.setText("");
				fld_Nominal_Weight_UOM.setPreferredSize(new Dimension(40, 20));
				fld_Nominal_Weight_UOM.setFocusCycleRoot(true);
				fld_Nominal_Weight_UOM.setCaretPosition(0);
				fld_Nominal_Weight_UOM.setBounds(668, 92, 50, 25);
				jDesktopPane1.add(fld_Nominal_Weight_UOM);
				fld_Tare_Weight.setEditable(false);

				fld_Tare_Weight.setVerifyInputWhenFocusTarget(false);
				fld_Tare_Weight.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_Tare_Weight.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_Tare_Weight.setBounds(839, 92, 73, 25);
				jDesktopPane1.add(fld_Tare_Weight);
				fld_Tare_Weight_UOM.setEnabled(false);
				fld_Tare_Weight_UOM.setEditable(false);

				fld_Tare_Weight_UOM.setText("");
				fld_Tare_Weight_UOM.setPreferredSize(new Dimension(40, 20));
				fld_Tare_Weight_UOM.setFocusCycleRoot(true);
				fld_Tare_Weight_UOM.setCaretPosition(0);
				fld_Tare_Weight_UOM.setBounds(924, 92, 50, 25);
				jDesktopPane1.add(fld_Tare_Weight_UOM);

				JLabel4j_std lbl_Nominal_Weight = new JLabel4j_std();
				lbl_Nominal_Weight.setText(lang.get("lbl_Nominal_Weight"));
				lbl_Nominal_Weight.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_Nominal_Weight.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_Nominal_Weight.setBounds(448, 92, 126, 25);
				jDesktopPane1.add(lbl_Nominal_Weight);

				JLabel4j_std lbl_Tare_Weight = new JLabel4j_std();
				lbl_Tare_Weight.setText(lang.get("lbl_Tare_Weight"));
				lbl_Tare_Weight.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_Tare_Weight.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_Tare_Weight.setBounds(722, 92, 108, 25);
				jDesktopPane1.add(lbl_Tare_Weight);

				JLabel4j_std lbl_T1_Lower_Limit = new JLabel4j_std();
				lbl_T1_Lower_Limit.setText(lang.get("lbl_T1_Lower_Limit"));
				lbl_T1_Lower_Limit.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_T1_Lower_Limit.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_T1_Lower_Limit.setBounds(448, 129, 126, 25);
				jDesktopPane1.add(lbl_T1_Lower_Limit);

				fld_T1_Lower_Limit.setVerifyInputWhenFocusTarget(false);
				fld_T1_Lower_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_T1_Lower_Limit.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_T1_Lower_Limit.setEditable(false);
				fld_T1_Lower_Limit.setBounds(583, 129, 73, 25);
				jDesktopPane1.add(fld_T1_Lower_Limit);

				JLabel4j_std lbl_T2_Lower_Limit = new JLabel4j_std();
				lbl_T2_Lower_Limit.setText(lang.get("lbl_T2_Lower_Limit"));
				lbl_T2_Lower_Limit.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_T2_Lower_Limit.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_T2_Lower_Limit.setBounds(722, 129, 108, 25);
				jDesktopPane1.add(lbl_T2_Lower_Limit);

				fld_T2_Lower_Limit.setVerifyInputWhenFocusTarget(false);
				fld_T2_Lower_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_T2_Lower_Limit.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_T2_Lower_Limit.setEditable(false);
				fld_T2_Lower_Limit.setBounds(839, 129, 73, 25);
				jDesktopPane1.add(fld_T2_Lower_Limit);

				JLabel4j_std lbl_SampleSize = new JLabel4j_std();
				lbl_SampleSize.setText(lang.get("lbl_SampleSize"));
				lbl_SampleSize.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_SampleSize.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_SampleSize.setBounds(0, 129, 96, 25);
				jDesktopPane1.add(lbl_SampleSize);

				JLabel4j_std lbl_SampleFrequency = new JLabel4j_std();
				lbl_SampleFrequency.setText(lang.get("lbl_SampleFrequency"));
				lbl_SampleFrequency.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_SampleFrequency.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_SampleFrequency.setBounds(142, 129, 96, 25);
				jDesktopPane1.add(lbl_SampleFrequency);

				fld_SampleSize.setVerifyInputWhenFocusTarget(false);
				fld_SampleSize.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_SampleSize.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_SampleSize.setEditable(false);
				fld_SampleSize.setBounds(104, 129, 38, 25);
				jDesktopPane1.add(fld_SampleSize);

				fld_SampleFrequency.setVerifyInputWhenFocusTarget(false);
				fld_SampleFrequency.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_SampleFrequency.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_SampleFrequency.setEditable(false);
				fld_SampleFrequency.setBounds(246, 129, 38, 25);
				jDesktopPane1.add(fld_SampleFrequency);

				JLabel4j_std lbl_TNE = new JLabel4j_std();
				lbl_TNE.setText(lang.get("lbl_TNE"));
				lbl_TNE.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_TNE.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_TNE.setBounds(224, 129, 108, 25);
				jDesktopPane1.add(lbl_TNE);

				fld_TNE.setVerifyInputWhenFocusTarget(false);
				fld_TNE.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_TNE.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_TNE.setEditable(false);
				fld_TNE.setBounds(364, 129, 73, 25);
				jDesktopPane1.add(fld_TNE);

				scrollPane_Weights.setBounds(617, 232, 365, 320);
				jDesktopPane1.add(scrollPane_Weights);
				list_Weights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane_Weights.setViewportView(list_Weights);

				JLabel lbl_Legend = new JLabel("  Gross Wt   Tare Wt    Net Wt       T1       T2");
				lbl_Legend.setFont(new Font("Monospaced", Font.BOLD, 11));
				lbl_Legend.setBounds(617, 218, 365, 15);
				jDesktopPane1.add(lbl_Legend);

				{
					fld_currentDateTime = new JDateControl();
					jDesktopPane1.add(fld_currentDateTime);
					fld_currentDateTime.setEnabled(false);
					fld_currentDateTime.setBounds(415, 174, 120, 25);
					JTextField tf = ((JSpinner.DefaultEditor) fld_currentDateTime.getEditor()).getTextField();
					tf.setEnabled(false);
					tf.setDisabledTextColor(UIManager.getColor("TextField.foreground"));

				}
				btnj_Cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						logEnabled = false;
						sampleDetailList.clear();
						btnj_Cancel.setEnabled(false);
						btn_Begin.setEnabled(true);

					}
				});
				btnj_Cancel.setEnabled(false);

				btnj_Cancel.setText(lang.get("lbl_Cancel_Weight_Check"));
				btnj_Cancel.setMnemonic('0');
				btnj_Cancel.setBounds(400, 566, 220, 32);
				jDesktopPane1.add(btnj_Cancel);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private boolean logSampleWeight(String weight, String weightUOM)
	{
		boolean result = true;

		if (logEnabled)
		{

			JDBWTSampleDetail sampleDetail = new JDBWTSampleDetail(Common.selectedHostID, Common.sessionID);
			sampleDetail.setSamplePoint(workdb.getSamplePoint());

			sampleDetail.setSampleWeightDate(JUtility.getSQLDateTime());
			sampleSequence++;
			sampleDetail.setSampleSequence(sampleSequence);
			sampleDetail.setSampleGrossWeight(new BigDecimal(weight));
			sampleDetail.setSampleTareWeight(matgroupdb.getTareWeight());
			sampleDetail.setSampleWeightUom(weightUOM);
			BigDecimal netWt = sampleDetail.getSampleGrossWeight();
			netWt = netWt.subtract(matgroupdb.getTareWeight());
			sampleDetail.setSampleNetWeight(netWt);

			if (netWt.compareTo(tnedb.getNegT2()) <= 0)
			{
				sampleDetail.setSampleT1Count(0);
				sampleDetail.setSampleT2Count(1);
			}
			else
			{
				sampleDetail.setSampleT2Count(0);

				if (netWt.compareTo(tnedb.getNegT1()) <= 0)
				{
					sampleDetail.setSampleT1Count(1);
				}
				else
				{
					sampleDetail.setSampleT1Count(0);
				}
			}
			
			sampleDetailList.addLast(sampleDetail);

			if (sampleSequence == lSampleSize)
			{
				saveAll();
				logEnabled = false;
				btn_Begin.setEnabled(true);
				btnj_Cancel.setEnabled(false);
			}
		}
		return result;
	}

	private void populateList()
	{
		DefaultComboBoxModel<JDBWTSampleDetail> DefComboBoxMod = new DefaultComboBoxModel<JDBWTSampleDetail>();


		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			DefComboBoxMod.addElement(t);

		}
		ListModel<JDBWTSampleDetail> jList1Model = DefComboBoxMod;

		list_Weights.setModel(jList1Model);

		list_Weights.setCellRenderer(Common.weight_sample_list);

	}

	private boolean readyToLog()
	{
		boolean result = true;

		return result;
	}
	// updateOrderInfo -- > updateMaterialInfo

	private void saveAll()
	{

	}

	private boolean updateCustomerDataInfo(String material, String customer, String key, boolean lookup)
	{
		boolean result = false;

		String data = "";
		String group = "";

		if (lookup == true)
		{
			if (matcustdb.getMaterialCustomerDataProperties(material, customer, key))
			{
				result = true;
				data = matcustdb.getData();

				if (key.equals("PRODUCT_GROUP"))
				{
					fld_Material_Group.setText(data);
					group = data;
				}

				if (key.equals("CONTAINER_CODE"))
				{
					fld_Container_Code.setText(data);
				}
			}
			else
			{
				result = false;

				if (key.equals("PRODUCT_GROUP"))
				{
					fld_Material_Group.setText("");
				}

				if (key.equals("CONTAINER_CODE"))
				{
					fld_Container_Code.setText("");
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
				fld_Material_Group.setBackground(Color.WHITE);
			}
			if (key.equals("CONTAINER_CODE"))
			{
				fld_Container_Code.setBackground(Color.WHITE);
			}

		}
		else
		{
			if (key.equals("PRODUCT_GROUP"))
			{
				fld_Material_Group.setText("");
				fld_Material_Group.setBackground(Color.YELLOW);
			}
			if (key.equals("CONTAINER_CODE"))
			{
				fld_Container_Code.setText("");
				fld_Container_Code.setBackground(Color.YELLOW);
			}
		}

		if (key.equals("PRODUCT_GROUP"))
		{
			updateProductGroup(group, result);
		}

		return result;
	}

	private boolean updateMaterialInfo(String material, String customer, boolean lookup)
	{
		boolean result = false;

		// Lookup is passed to indicate if previous step failed in which case
		// there is no need to lookup date in this step.

		if (lookup == true)
		{
			if (materialdb.getMaterialProperties(material))
			{
				result = true;
				fld_Description.setText(materialdb.getDescription());
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
			fld_Material.setBackground(Color.WHITE);
			fld_Description.setBackground(Color.WHITE);
		}
		else
		{
			fld_Description.setText("");
			fld_Material.setBackground(Color.YELLOW);
			fld_Description.setBackground(Color.YELLOW);
		}

		updateCustomerDataInfo(material, customer, "PRODUCT_GROUP", result);

		updateCustomerDataInfo(material, customer, "CONTAINER_CODE", result);

		return result;
	}

	private boolean updateOrderInfo(String orderNo, boolean lookup)
	{
		boolean result = false;

		String material = "";
		String customerID = "";
		String status = "";

		// Lookup is passed to indicate if previous step failed in which case
		// there is no need to lookup date in this step.

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

		fld_Process_Order_Status.setText(status);
		fld_Material.setText(material);

		if (result == true)
		{
			fld_Process_Order.setBackground(Color.WHITE);

			if (status.equals("Ready") || (status.equals("Running")))
			{
				fld_Process_Order_Status.setBackground(Color.WHITE);
			}
			else
			{
				fld_Process_Order_Status.setBackground(Color.RED);
			}

		}
		else
		{
			fld_Process_Order.setBackground(Color.YELLOW);
			fld_Process_Order_Status.setBackground(Color.YELLOW);
		}

		updateMaterialInfo(material, customerID, result);

		return result;
	}

	private boolean updateProductGroup(String group, boolean lookup)
	{
		boolean result = false;
		BigDecimal nominal = new BigDecimal("0.000");
		String nominalUom = "";
		BigDecimal tare = new BigDecimal("0.000");
		String tarelUom = "";

		// Lookup is passed to indicate if previous step failed in which case
		// there is no need to lookup date in this step.

		if (lookup == true)
		{
			if (matgroupdb.getMaterialGroupProperties(group))
			{
				result = true;
				nominal = matgroupdb.getNominalWeight();
				nominalUom = matgroupdb.getNominalUOM();
				tare = matgroupdb.getTareWeight();
				tarelUom = matgroupdb.getTareWeightUOM();

				fld_Nominal_Weight.setText(nominal.toString());
				fld_Nominal_Weight_UOM.setText(nominalUom);
				fld_Tare_Weight.setText(tare.toString());
				fld_Tare_Weight_UOM.setText(tarelUom);

			}
			else
			{
				result = false;
				nominal = new BigDecimal("0.000");
				nominalUom = "";
				tare = new BigDecimal("0.000");
				tarelUom = "";

			}
		}
		else
		{
			result = false;
		}

		if (result == true)
		{
			if (nominal.compareTo(zero) == 0)
			{
				fld_Nominal_Weight.setBackground(Color.YELLOW);
			}
			else
			{
				fld_Nominal_Weight.setBackground(Color.WHITE);
			}

			if (nominalUom.equals(""))
			{
				fld_Nominal_Weight_UOM.setBackground(Color.YELLOW);
			}
			else
			{
				fld_Nominal_Weight_UOM.setBackground(Color.WHITE);
			}

			if (tare.compareTo(zero) == 0)
			{
				fld_Tare_Weight.setBackground(Color.YELLOW);
			}
			else
			{
				fld_Tare_Weight.setBackground(Color.WHITE);
			}

			if (tarelUom.equals(""))
			{
				fld_Tare_Weight_UOM.setBackground(Color.YELLOW);
			}
			else
			{
				fld_Tare_Weight_UOM.setBackground(Color.WHITE);
			}

		}
		else
		{
			fld_Nominal_Weight.setText("0.000");
			fld_Nominal_Weight_UOM.setText("");
			fld_Tare_Weight.setText("0.000");
			fld_Tare_Weight_UOM.setText("");

			fld_Nominal_Weight.setBackground(Color.YELLOW);
			fld_Nominal_Weight_UOM.setBackground(Color.YELLOW);
			fld_Tare_Weight.setBackground(Color.YELLOW);
			fld_Tare_Weight_UOM.setBackground(Color.YELLOW);
		}

		updateTNEInfo(nominal, nominalUom, result);

		return result;
	}

	private boolean updateSamplePoint(String samplePoint, boolean lookup)
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
			fld_SamplePoint.setBackground(Color.WHITE);
		}
		else
		{
			fld_SamplePoint.setBackground(Color.YELLOW);
		}

		return result;
	}

	private boolean updateTNEInfo(BigDecimal findNominal, String findNominalUOM, boolean lookup)
	{
		boolean result = false;

		BigDecimal negt1 = new BigDecimal("0.000");
		BigDecimal negt2 = new BigDecimal("0.000");
		BigDecimal tne = new BigDecimal("0.000");

		// Lookup is passed to indicate if previous step failed in which case
		// there is no need to lookup date in this step.

		if (lookup == true)
		{
			if (tnedb.getProperties(findNominal, findNominalUOM))
			{
				result = true;

				negt1 = tnedb.getNegT1();
				negt2 = tnedb.getNegT2();
				tne = tnedb.getTNE();

				fld_T1_Lower_Limit.setText(negt1.toString());
				fld_T2_Lower_Limit.setText(negt2.toString());
				fld_TNE.setText(tne.toString());
			}
			else
			{
				result = false;

				negt1 = new BigDecimal("0.000");
				negt2 = new BigDecimal("0.000");
				tne = new BigDecimal("0.000");
			}
		}
		else
		{
			result = false;
		}

		fld_T1_Lower_Limit.setText(negt1.toString());
		fld_T2_Lower_Limit.setText(negt2.toString());
		fld_TNE.setText(tne.toString());

		if (result == true)
		{

			if (negt1.compareTo(zero) == 0)
			{
				fld_T1_Lower_Limit.setBackground(Color.YELLOW);
			}
			else
			{
				fld_T1_Lower_Limit.setBackground(Color.WHITE);
			}

			if (negt2.compareTo(zero) == 0)
			{
				fld_T2_Lower_Limit.setBackground(Color.YELLOW);
			}
			else
			{
				fld_T2_Lower_Limit.setBackground(Color.WHITE);
			}

			if (tne.compareTo(zero) == 0)
			{
				fld_TNE.setBackground(Color.YELLOW);
			}
			else
			{
				fld_TNE.setBackground(Color.WHITE);
			}

		}
		else
		{
			fld_T1_Lower_Limit.setBackground(Color.YELLOW);
			fld_T2_Lower_Limit.setBackground(Color.YELLOW);
			fld_TNE.setBackground(Color.YELLOW);
		}

		return result;
	}

	private boolean updateWorkstationInfo(String workstation, boolean lookup)
	{
		boolean result = false;
		String samplePoint = "";

		if (lookup == true)
		{
			if (workdb.getProperties(workstation))
			{
				result = true;
				samplePoint = workdb.getSamplePoint();
				fld_SamplePoint.setText(samplePoint);
				fld_Scale_ID.setText(workdb.getScaleID());
				fld_ScalePort.setText(workdb.getScalePort());
			}
			else
			{
				result = false;
				fld_SamplePoint.setText("");
				fld_Scale_ID.setText("");
				fld_ScalePort.setText("");
			}
		}
		else
		{
			result = false;
		}

		if (result == true)
		{
			fld_Workstation.setBackground(Color.WHITE);
			fld_Scale_ID.setBackground(Color.WHITE);
			fld_ScalePort.setBackground(Color.WHITE);
		}
		else
		{
			fld_Workstation.setBackground(Color.YELLOW);
			fld_Scale_ID.setBackground(Color.YELLOW);
			fld_ScalePort.setBackground(Color.YELLOW);
		}

		updateSamplePoint(samplePoint, result);

		return result;
	}
}
