package com.commander4j.app;

import java.awt.BasicStroke;

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
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;

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
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

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
	private JButton4j btn_Begin = new JButton4j(Common.icon_weight_capture_16x16);
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btn_SamplePoint_Lookup;
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
	private JTextField fld_Process_Order = new JTextField(JDBProcessOrder.field_process_order);
	private JTextField4j fld_Process_Order_Status = new JTextField4j(JDBProcessOrder.field_status);
	private JQuantityInput fld_SampleFrequency = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JQuantityInput fld_SampleSize = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Scale_ID = new JTextField4j(JDBWTScale.field_ScaleID);
	private JTextField4j fld_ScalePort = new JTextField4j(JDBWTWorkstation.field_ScalePort);
	private JQuantityInput fld_T1_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_T2_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Mean = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Batch_Mean = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Standard_Deviation = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Tare_Weight = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Tare_Weight_UOM = new JTextField4j(JDBUom.field_uom);
	private JQuantityInput fld_TNE = new JQuantityInput(new BigDecimal("0.000"));
	private JTextField4j fld_Workstation = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
	private JDesktopPane jDesktopPane1;
	private JScrollPane scrollPane_Weights = new JScrollPane();
	private JLabel4j_std jStatusText;
	private JDBLanguage lang;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private JList4j<JDBWTSampleDetail> list_Weights = new JList4j<JDBWTSampleDetail>();
	private PreparedStatement listStatement;
	private boolean logEnabled = false;
	private int lSampleFrequency = 15;
	private int lSampleSize = 5;

	private JDBMaterialCustomerData matcustdb = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JDBMaterial materialdb = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWTProductGroups matgroupdb = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder orderdb = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private LinkedList<JDBWTSampleDetail> sampleDetailList = new LinkedList<JDBWTSampleDetail>();
	private JDBWTSampleHeader sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samplePointdb = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private JDBWTTNE tnedb = new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDBWTWorkstation workdb = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
	private JDBWTScale scaledb = new JDBWTScale(Common.selectedHostID, Common.sessionID);

	private Integer sampleSequence = 0;
	private Integer lGraphWindowHours = 6;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private Timer timer = new Timer(1000, clocklistener);
	private BigDecimal mean = new BigDecimal("0.000");
	private BigDecimal batch_mean = new BigDecimal("0.000");
	private BigDecimal std_dev = new BigDecimal("0.000");
	private BigDecimal zero = new BigDecimal("0.000");
	private static Double graphMinY = new Double(-1);
	private static Double graphMaxY = new Double(-1);

	private Integer t1_count = 0;
	private Integer t2_count = 0;
	private String materialGroup = "";
	private String containerCode = "";
	private boolean validToScan = false;
	private JButton btnManualInput = new JButton(Common.icon_add_16x16);
	private JButton btnDebug = new JButton();
	private static CategoryPlot plot = new CategoryPlot();
	private static DefaultCategoryDataset ds = new DefaultCategoryDataset();
	private static JFreeChart chart = ChartFactory.createLineChart("Mean Weight", // chart
																					// title
			"Time", // domain axis label
			"Grams", // range axis label
			ds, // data
			PlotOrientation.VERTICAL, // orientation
			false, // include legend
			true, // tooltips
			false // urls
	);
	private static ChartPanel panel = new ChartPanel(chart);

	public JInternalFrameWTWeightCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		updateProcessOrderInfo("", true);

		timer.start();
		String workstation = JUtility.getClientName().toUpperCase();

		fld_Workstation.setText(workstation);

		String temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE SIZE", "5", "WEIGHT CHECK SAMPLE SIZE");
		lSampleSize = Integer.valueOf(temp);
		fld_SampleSize.setText(String.valueOf(lSampleSize));

		temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE FREQUENCY", "15", "WEIGHT CHECK FREQUENCY MINS");
		lSampleFrequency = Integer.valueOf(temp);
		fld_SampleFrequency.setText(String.valueOf(lSampleFrequency));
		
		temp = ctrl.getKeyValueWithDefault("WEIGHT GRAPH HOURS", "6", "WEIGHT GRAPH TIME WINDOW");
		lGraphWindowHours = Integer.valueOf(temp);

		updateWorkstationInfo(workstation, true);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_WEIGHT_WORKSTATION where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();

		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn_Help, JUtility.getHelpSetIDforModule("FRM_WEIGHT_CAPTURE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		addInternalFrameListener(new InternalFrameAdapter()
		{

			public void internalFrameClosing(InternalFrameEvent e)
			{
				shutdown();
			}
		});

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fld_Process_Order.requestFocus();
				fld_Process_Order.setCaretPosition(fld_Process_Order.getText().length());
			}
		});

	}

	private void shutdown()
	{

		if (SerialPort.getCommPorts().length > 0)
		{
			if (scaledb.isConnected())
			{
				scaledb.comPort.removeDataListener();
				scaledb.comPort.closePort();
				scaledb.setConnected(false);
			}
		}

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

	private void buildSQL()
	{

		// DefaultCategoryDataset result = new DefaultCategoryDataset();

		if (validToScan)
		{
			ResultSet rs;
			JDBQuery.closeStatement(listStatement);
			JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
			query.clear();

			query.addText(JUtility.substSchemaName(schemaName, "select sample_date,sample_mean from {schema}APP_WEIGHT_SAMPLE_HEADER"));
			query.addParamtoSQL("sample_point =", workdb.getSamplePoint());
			query.addParamtoSQL("process_order =", orderdb.getProcessOrder());

			Calendar calendar = Calendar.getInstance();
			
			calendar.add(Calendar.HOUR, (-1 * lGraphWindowHours));
			calendar.add(Calendar.MINUTE, -1);
			
			Timestamp startdate = new Timestamp(calendar.getTimeInMillis());
			startdate.setNanos(0);
			
			query.addParamtoSQL("sample_date >=", startdate);

			query.addParamtoSQL("sample_mean >", 0);

			query.bindParams();
			listStatement = query.getPreparedStatement();
			try
			{
				rs = listStatement.executeQuery();

				LinkedList<Integer> sequence = new LinkedList<Integer>();
				LinkedList<Double> means = new LinkedList<Double>();

				int count = 0;

				batch_mean = new BigDecimal("0.000");

				while (rs.next())
				{
					count++;
					sequence.addLast(count);

					Double d = rs.getDouble("sample_mean");
					BigDecimal t = rs.getBigDecimal("sample_mean");
					Timestamp when = rs.getTimestamp("sample_date");
					String whenstr = JUtility.getISOTimeStampStringFormat(when);
					whenstr = whenstr.substring(11, 16);

					d.doubleValue();
					means.addLast(d.doubleValue());
					batch_mean = batch_mean.add(t);

					if (ds.getColumnIndex(whenstr) > 0)
					{
						ds.setValue(d, "Weight", whenstr);
					}
					else
					{
						ds.addValue(d, "Weight", whenstr);
					}

					if ((d < graphMinY) || (graphMinY == -1))
					{
						graphMinY = d;
					}

					if ((d > graphMaxY) || (graphMaxY == -1))
					{
						graphMaxY = d;
					}

				}

				if (count > 0)
				{
					batch_mean = batch_mean.divide(new BigDecimal(count), 3, BigDecimal.ROUND_HALF_UP);
					fld_Batch_Mean.setText(batch_mean.toString());
				}

				rs.close();

			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// return result;
	}

	private void drawGraph()
	{

		// ds = buildSQL();

		buildSQL();
		createChart(ds);

		// .fireChartChanged();

		// panel = new ChartPanel(chart);

		// panel.setMouseWheelEnabled(false);
		// panel.setAutoscrolls(false);

		// panel.setBounds(14, 178, 746, 374);

		// jDesktopPane1.add(panel);

	}

	private void createChart(CategoryDataset dataset)
	{

		// chart.addSubtitle(new TextTitle("Mean Weight"));
		TextTitle source = new TextTitle();
		source.setFont(new Font("SansSerif", Font.PLAIN, 10));
		source.setPosition(RectangleEdge.BOTTOM);
		source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		chart.setTitle("Mean Weight ("+String.valueOf(lGraphWindowHours)+" Hours)");
		chart.addSubtitle(source);

		plot = (CategoryPlot) chart.getPlot();
        plot.setRangePannable(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeZeroBaselineVisible(true);

		// customise the range axis...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		if (graphMinY == -1.0)
		{
			graphMinY = (tnedb.getNominalWT().subtract(tnedb.getTNE()).doubleValue());
		}

		if (graphMaxY == -1.0)
		{
			graphMaxY = (tnedb.getNominalWT().add(tnedb.getTNE()).doubleValue());
		}

		if (graphMinY == graphMaxY)
		{
			graphMinY--;
			graphMaxY++;
		}

		rangeAxis.setRange(graphMinY, graphMaxY);
		rangeAxis.setLabelAngle(0);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

		ChartUtils.applyCurrentTheme(chart);

		// customise the renderer...
        LineAndShapeRenderer renderer 
        = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setDefaultShapesVisible(true);
		renderer.setDrawOutlines(true);
		renderer.setUseFillPaint(true);
		renderer.setDefaultFillPaint(Color.white);
		renderer.setSeriesStroke(0, new BasicStroke(3.0f));
		renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));

	}

	private void updateGraph()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				// updateProcessOrderInfo(JLaunchLookup.dlgResult, true);
				drawGraph();
				;

			}
		});
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1016, 693);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Weight Checks");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));

				{
					btn_Help = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(btn_Help);
					btn_Help.setText(lang.get("btn_Help"));
					btn_Help.setMnemonic(java.awt.event.KeyEvent.VK_H);
					btn_Help.setBounds(498, 566, 220, 40);
				}
				{
					btn_Close = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(btn_Close);
					btn_Close.setText(lang.get("btn_Close"));
					btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
					btn_Close.setBounds(719, 566, 220, 40);
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
					fld_Process_Order.setFont(Common.font_input);
					fld_Process_Order.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyReleased(KeyEvent e)
						{
							updateProcessOrderInfo(fld_Process_Order.getText(), true);
							updateGraph();
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
							JLaunchLookup.dlgCriteriaDefault = samplePointdb.getRequiredResource();
							if (JLaunchLookup.processOrdersResources())
							{
								fld_Process_Order.setText(JLaunchLookup.dlgResult);
								updateProcessOrderInfo(JLaunchLookup.dlgResult, true);
								updateGraph();
							}
						}
					});
					btn_Process_Order_Lookup.setBounds(196, 54, 21, 25);
					jDesktopPane1.add(btn_Process_Order_Lookup);
				}

				{
					btn_SamplePoint_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_SamplePoint_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.weightSamplePoint())
							{
								fld_SamplePoint.setText(JLaunchLookup.dlgResult);
								updateSamplePoint(JLaunchLookup.dlgResult, true);
								fld_Process_Order.setText("");
								updateProcessOrderInfo("", true);
							}
						}
					});
					btn_SamplePoint_Lookup.setBounds(438, 18, 21, 25);
					jDesktopPane1.add(btn_SamplePoint_Lookup);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setFont(new Font("Arial", Font.PLAIN, 16));
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 620, 1006, 41);
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
				fld_Workstation.setBounds(104, 18, 134, 25);

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
							btnManualInput.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_CAPTURE_MANUAL_ADD"));
							fld_Mean.setText("0.000");
							fld_Batch_Mean.setText("0.000");
							fld_Standard_Deviation.setText("0.000");
							logEnabled = true;
							jStatusText.setText("Start weighing " + lSampleSize + " samples");
							sampleSequence = 0;
							sampleDetailList.clear();
							populateList();
						}
					}
				});

				btn_Begin.setText(lang.get("lbl_Begin_Weight_Check"));
				btn_Begin.setMnemonic('0');
				btn_Begin.setBounds(53, 566, 220, 40);
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

				JLabel4j_std lbl_Mean = new JLabel4j_std();
				lbl_Mean.setText(lang.get("lbl_Average_Mean"));
				lbl_Mean.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_Mean.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_Mean.setBounds(767, 458, 126, 25);
				jDesktopPane1.add(lbl_Mean);

				JLabel4j_std lbl_Batch_Mean = new JLabel4j_std();
				lbl_Batch_Mean.setText(lang.get("lbl_Average_Batch_Mean"));
				lbl_Batch_Mean.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_Batch_Mean.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_Batch_Mean.setBounds(767, 516, 126, 25);
				jDesktopPane1.add(lbl_Batch_Mean);

				JLabel4j_std lbl_Standard_Deviation = new JLabel4j_std();
				lbl_Standard_Deviation.setText(lang.get("lbl_Standard_Deviation"));
				lbl_Standard_Deviation.setHorizontalTextPosition(SwingConstants.RIGHT);
				lbl_Standard_Deviation.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl_Standard_Deviation.setBounds(767, 487, 126, 25);
				jDesktopPane1.add(lbl_Standard_Deviation);

				fld_T1_Lower_Limit.setVerifyInputWhenFocusTarget(false);
				fld_T1_Lower_Limit.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_T1_Lower_Limit.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_T1_Lower_Limit.setEditable(false);
				fld_T1_Lower_Limit.setBounds(583, 129, 73, 25);
				jDesktopPane1.add(fld_T1_Lower_Limit);

				fld_Mean.setVerifyInputWhenFocusTarget(false);
				fld_Mean.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_Mean.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_Mean.setEditable(false);
				fld_Mean.setBounds(901, 458, 73, 25);
				jDesktopPane1.add(fld_Mean);

				fld_Batch_Mean.setVerifyInputWhenFocusTarget(false);
				fld_Batch_Mean.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_Batch_Mean.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_Batch_Mean.setEditable(false);
				fld_Batch_Mean.setBounds(901, 516, 73, 25);
				jDesktopPane1.add(fld_Batch_Mean);

				fld_Standard_Deviation.setVerifyInputWhenFocusTarget(false);
				fld_Standard_Deviation.setHorizontalAlignment(SwingConstants.TRAILING);
				fld_Standard_Deviation.setFont(new Font("Arial", Font.PLAIN, 11));
				fld_Standard_Deviation.setEditable(false);
				fld_Standard_Deviation.setBounds(901, 487, 73, 25);
				jDesktopPane1.add(fld_Standard_Deviation);

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

				scrollPane_Weights.setBounds(798, 178, 184, 190);
				jDesktopPane1.add(scrollPane_Weights);
				list_Weights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane_Weights.setViewportView(list_Weights);

				JLabel lbl_Legend = new JLabel("   Gross Wt       Net Wt");
				lbl_Legend.setFont(new Font("Monospaced", Font.BOLD, 11));
				lbl_Legend.setBounds(798, 162, 184, 15);
				jDesktopPane1.add(lbl_Legend);

				{
					fld_currentDateTime = new JDateControl();
					jDesktopPane1.add(fld_currentDateTime);
					fld_currentDateTime.setEnabled(false);
					fld_currentDateTime.setVisible(false);
					fld_currentDateTime.setBounds(331, 154, 120, 25);
					JTextField tf = ((JSpinner.DefaultEditor) fld_currentDateTime.getEditor()).getTextField();
					tf.setEnabled(false);
					tf.setDisabledTextColor(UIManager.getColor("TextField.foreground"));

				}

				btnj_Cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						btn_Begin.setEnabled(true);
						btnj_Cancel.setEnabled(false);
						btnManualInput.setEnabled(false);
						fld_Mean.setText("0.000");
						fld_Batch_Mean.setText("0.000");
						fld_Standard_Deviation.setText("0.000");
						logEnabled = false;
						sampleSequence = 0;
						sampleDetailList.clear();
						populateList();

					}
				});
				btnj_Cancel.setEnabled(false);

				btnj_Cancel.setText(lang.get("lbl_Cancel_Weight_Check"));
				btnj_Cancel.setMnemonic('0');
				btnj_Cancel.setBounds(275, 566, 220, 40);
				jDesktopPane1.add(btnj_Cancel);

				btnManualInput.setText(lang.get("btn_Add"));

				btnManualInput.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						addManualWeight();

					}
				});
				btnDebug.setVisible(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_CAPTURE_DEBUG"));
				btnDebug.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						double random = ThreadLocalRandom.current().nextDouble(91, 107);
						BigDecimal rnd = BigDecimal.valueOf(random);
						rnd = rnd.divide(new BigDecimal(1), 3, BigDecimal.ROUND_HALF_UP);
						logSampleWeight(rnd.toString(), "G");
					}
				});
				btnDebug.setText("Debug");

				btnDebug.setBounds(798, 398, 184, 25);
				jDesktopPane1.add(btnDebug);

				btnManualInput.setBounds(798, 369, 184, 25);
				jDesktopPane1.add(btnManualInput);
				btnManualInput.setEnabled(false);

				panel = new ChartPanel(chart);

				panel.setMouseWheelEnabled(false);
				panel.setAutoscrolls(false);

				panel.setBounds(14, 178, 772, 374);

				jDesktopPane1.add(panel);

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
			jStatusText.setText("Recording " + weight + " " + weightUOM);
			JDBWTSampleDetail sampleDetail = new JDBWTSampleDetail(Common.selectedHostID, Common.sessionID);
			sampleDetail.setSamplePoint(workdb.getSamplePoint());
			sampleDetail.setSampleDate(sampleHeader.getSampleDate());
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

			populateList();

			if (sampleSequence == lSampleSize)
			{
				jStatusText.setText("Saving results to database...");

				std_dev = calculateStandardDeviation();
				mean = calculateMean();

				t1_count = getT1Count();
				t2_count = getT2Count();
				fld_Standard_Deviation.setText(std_dev.toString());

				saveAll(mean, std_dev, t1_count, t2_count);

				fld_Mean.setText(mean.toString());

				logEnabled = false;
				btn_Begin.setEnabled(true);
				btnj_Cancel.setEnabled(false);
				btnManualInput.setEnabled(false);

				jStatusText.setText("Results have been saved.");

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						updateGraph();
					}
				});

			}
			else
			{
				jStatusText.setText("Weigh sample " + String.valueOf(sampleSequence + 1) + " of " + String.valueOf(lSampleSize));
			}
		}
		else
		{
			jStatusText.setText("Click Begin before starting sampling");
		}
		return result;
	}

	private Integer getT1Count()
	{

		int count = 0;

		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			count = count + t.getSampleT1Count();

		}

		return count;
	}

	private Integer getT2Count()
	{

		int count = 0;

		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			count = count + t.getSampleT2Count();

		}

		return count;
	}

	private BigDecimal calculateMean()
	{
		BigDecimal result = new BigDecimal("0.000");
		int count = 0;

		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			result = result.add(t.getSampleNetWeight());
			count++;

		}

		if (count > 0)
		{
			result = result.divide(new BigDecimal(count), 3, BigDecimal.ROUND_HALF_UP);
		}

		return result;
	}

	private BigDecimal calculateStandardDeviation()
	{
		BigDecimal result = new BigDecimal("0.000");

		Double stddevDouble = 0.0;

		if (sampleDetailList.size() > 0)
		{

			double[] doubeArray = new double[sampleDetailList.size()];

			for (int j = 0; j < sampleDetailList.size(); j++)
			{
				JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
				Double doubleNet = t.getSampleNetWeight().doubleValue();
				doubeArray[j] = doubleNet;
			}

			StandardDeviation stddev = new StandardDeviation();
			stddevDouble = stddev.evaluate(doubeArray);
		}

		result = (BigDecimal.valueOf(stddevDouble));
		result = result.setScale(3, RoundingMode.CEILING);
		return result;
	}

	private void populateList()
	{
		DefaultComboBoxModel<JDBWTSampleDetail> DefComboBoxMod = new DefaultComboBoxModel<JDBWTSampleDetail>();
		list_Weights.setCellRenderer(Common.weight_sample_list);

		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			t.setDisplayType(JDBWTSampleDetail.shortString);
			DefComboBoxMod.addElement(t);

		}
		ListModel<JDBWTSampleDetail> jList1Model = DefComboBoxMod;

		list_Weights.setModel(jList1Model);

		scrollPane_Weights.repaint();

	}

	private boolean readyToLog()
	{
		boolean result = true;

		return result;
	}
	// updateOrderInfo -- > updateMaterialInfo

	private void saveAll(BigDecimal mean, BigDecimal StdDev, Integer t1s, Integer t2s)
	{
		sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);
		sampleHeader.setSamplePoint(samplePointdb.getSamplePoint());
		sampleHeader.setSampleDate(JUtility.getSQLDateTime());
		sampleHeader.setUserID(Common.userList.getUser(Common.sessionID).getUserId());
		sampleHeader.setWorkstationID(workdb.getWorkstationID());
		sampleHeader.setScaleID(workdb.getScaleID());
		sampleHeader.setProcessOrder(orderdb.getProcessOrder());
		sampleHeader.setRequiredResource(orderdb.getRequiredResource());
		sampleHeader.setCustomerID(orderdb.getCustomerID());
		sampleHeader.setMaterial(orderdb.getMaterial());
		sampleHeader.setProductGroup(materialGroup);
		sampleHeader.setContainerCode(containerCode);
		sampleHeader.setNominalWeight(matgroupdb.getNominalWeight());
		sampleHeader.setNominalWeightUom(matgroupdb.getNominalUOM());
		sampleHeader.setTareWeight(matgroupdb.getTareWeight());
		sampleHeader.setTareWeightUom(matgroupdb.getTareWeightUOM());
		sampleHeader.setTNE(tnedb.getTNE());
		sampleHeader.setNegT1(tnedb.getNegT1());
		sampleHeader.setNegT2(tnedb.getNegT2());
		sampleHeader.setSampleSize(lSampleSize);
		sampleHeader.setSampleCount(lSampleSize);
		sampleHeader.setSampleMean(mean);
		sampleHeader.setSampleStdDev(StdDev);
		sampleHeader.setSampleT1Count(t1s);
		sampleHeader.setSampleT2Count(t2s);
		sampleHeader.create();
		sampleHeader.update();

		for (int j = 0; j < sampleDetailList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
			t.setSampleDate(sampleHeader.getSampleDate());
			t.create();
			t.update();

		}

	}

	private boolean updateCustomerDataInfo(String material, String customer, String key, boolean lookup)
	{
		boolean result = false;

		String data = "";

		if (lookup == true)
		{
			if (matcustdb.getMaterialCustomerDataProperties(material, customer, key))
			{
				result = true;
				data = matcustdb.getData();

				if (key.equals("PRODUCT_GROUP"))
				{
					fld_Material_Group.setText(data);
					fld_Material_Group.setBackground(Color.WHITE);
					materialGroup = data;
				}

				if (key.equals("CONTAINER_CODE"))
				{
					fld_Container_Code.setText(data);
					fld_Container_Code.setBackground(Color.WHITE);
					containerCode = data;
				}
			}
			else
			{
				result = false;

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
		}
		else
		{
			result = false;

			if (key.equals("PRODUCT_GROUP"))
			{
				fld_Material_Group.setText("");
				fld_Material_Group.setBackground(Color.WHITE);
			}

			if (key.equals("CONTAINER_CODE"))
			{
				fld_Container_Code.setText("");
				fld_Container_Code.setBackground(Color.WHITE);
			}
		}

		if (key.equals("PRODUCT_GROUP"))
		{
			updateProductGroup(materialGroup, result);
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
				fld_Material.setBackground(Color.WHITE);

			}
			else
			{
				result = false;
				fld_Description.setText("");
				fld_Material.setBackground(Color.YELLOW);
			}

		}
		else
		{
			result = false;
			fld_Material.setBackground(Color.WHITE);
			fld_Material.setText("");
			fld_Description.setText("");
		}

		updateCustomerDataInfo(material, customer, "PRODUCT_GROUP", result);

		updateCustomerDataInfo(material, customer, "CONTAINER_CODE", result);

		return result;
	}

	private boolean updateProcessOrderInfo(String orderNo, boolean lookup)
	{
		boolean result = false;

		String material = "";
		String customerID = "";
		String status = "";
		ds.clear();

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

				fld_Process_Order.setBackground(Color.WHITE);
				fld_Process_Order_Status.setText(status);
				fld_Material.setText(material);

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
				result = false;
				material = "";
				customerID = "";
				status = "";
				fld_Process_Order_Status.setText("");
				fld_Process_Order.setBackground(Color.YELLOW);
				fld_Process_Order_Status.setBackground(Color.WHITE);
			}
		}
		else
		{
			fld_Process_Order_Status.setText("");
			fld_Process_Order_Status.setBackground(Color.WHITE);
			result = false;
		}
		sampleDetailList.clear();
		populateList();
		updateMaterialInfo(material, customerID, result);
		
//		graphMinY = new Double(-1);
//	    graphMaxY = new Double(-1);

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
				result = false;
				nominal = new BigDecimal("0.000");
				nominalUom = "";
				tare = new BigDecimal("0.000");
				tarelUom = "";

				fld_Nominal_Weight.setBackground(Color.YELLOW);
				fld_Nominal_Weight_UOM.setBackground(Color.YELLOW);
				fld_Tare_Weight.setBackground(Color.YELLOW);
				fld_Tare_Weight_UOM.setBackground(Color.YELLOW);
			}
		}
		else
		{
			result = false;
			result = false;
			nominal = new BigDecimal("0.000");
			nominalUom = "";
			tare = new BigDecimal("0.000");
			tarelUom = "";

			fld_Nominal_Weight.setText("0.000");
			fld_Nominal_Weight_UOM.setText("");
			fld_Tare_Weight.setText("0.000");
			fld_Tare_Weight_UOM.setText("");
			fld_Nominal_Weight.setBackground(Color.WHITE);
			fld_Nominal_Weight_UOM.setBackground(Color.WHITE);
			fld_Tare_Weight.setBackground(Color.WHITE);
			fld_Tare_Weight_UOM.setBackground(Color.WHITE);
		}

		updateTNEInfo(nominal, nominalUom, result);

		return result;
	}

	private boolean updateSamplePoint(String samplePoint, boolean lookup)
	{
		boolean result = false;

		if (lookup == true)
		{
			fld_SamplePoint.setText(samplePoint);
			if (samplePointdb.getProperties(samplePoint))
			{
				result = true;
				fld_SamplePoint.setBackground(Color.WHITE);

			}
			else
			{
				result = false;
				fld_SamplePoint.setBackground(Color.YELLOW);
			}
		}
		else
		{
			result = false;
			fld_SamplePoint.setText("");
			fld_SamplePoint.setBackground(Color.WHITE);
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
				result = false;

				negt1 = new BigDecimal("0.000");
				negt2 = new BigDecimal("0.000");
				tne = new BigDecimal("0.000");

				fld_T1_Lower_Limit.setText("0.000");
				fld_T2_Lower_Limit.setText("0.000");
				fld_TNE.setText("0.000");

				fld_T1_Lower_Limit.setBackground(Color.YELLOW);
				fld_T2_Lower_Limit.setBackground(Color.YELLOW);
				fld_TNE.setBackground(Color.YELLOW);

			}
		}
		else
		{
			result = false;
			negt1 = new BigDecimal("0.000");
			negt2 = new BigDecimal("0.000");
			tne = new BigDecimal("0.000");

			fld_T1_Lower_Limit.setText("0.000");
			fld_T2_Lower_Limit.setText("0.000");
			fld_TNE.setText("0.000");

			fld_T1_Lower_Limit.setBackground(Color.WHITE);
			fld_T2_Lower_Limit.setBackground(Color.WHITE);
			fld_TNE.setBackground(Color.WHITE);
		}

		validToScan = result;
		btn_Begin.setEnabled(validToScan);

		if (validToScan)
		{
			jStatusText.setText("Click Begin to start weighing samples");
		}
		else
		{
			jStatusText.setText("Unable to record weights until highlighted errors have been corrected.");
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

		}
		else
		{
			fld_Workstation.setBackground(Color.YELLOW);

		}

		if (result == true)
		{
			if (scaledb.connect(workdb.getScaleID(), workdb.getScalePort()))
			{
				scaledb.scaleReset();
				scaledb.scaleRequestWeightonChange();
				scaledb.comPort.addDataListener(new SerialPortMessageListener()
				{
					@Override
					public int getListeningEvents()
					{
						return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
					}

					@Override
					public void serialEvent(SerialPortEvent event)
					{
						byte[] newData = event.getReceivedData();
						String data = new String(newData);

						if (data.startsWith("S S "))
						{
							String temp = data.replaceAll("\\s{2,}", " ").trim();

							String[] parts = temp.split(" ");
							if (Double.valueOf(parts[2]) > 0)
							{
								logSampleWeight(parts[2], parts[3].toUpperCase());
							}

						}
						data = data.replace("\r\n", "<CR><LF>");
						System.out.println("Debug RX >" + data + "<");

					}

					@Override
					public boolean delimiterIndicatesEndOfMessage()
					{
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public byte[] getMessageDelimiter()
					{
						byte[] eol = "\r\n".getBytes();
						return eol;
					}
				});

			}
		}

		updateSamplePoint(samplePoint, result);

		return result;
	}

	private boolean addManualWeight()
	{
		boolean numberOK = false;
		String grossWt = "";

		while (numberOK == false)
		{
			grossWt = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Gross_Weight_Grams"),grossWt);
			//grossWt = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Gross_Weight_Grams"), null, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm_16x16, null, null);
			if (grossWt != null)
			{
				if (grossWt.equals("") == false)
				{
					try
					{
						BigDecimal gw1 = new BigDecimal("0.000");
						BigDecimal gw2 = new BigDecimal(grossWt);
						gw1 = gw1.add(gw2);
						grossWt = gw1.toString();
						jStatusText.setText("");
						logSampleWeight(grossWt, "G");
						numberOK = true;
					}
					catch (Exception ex)
					{
						jStatusText.setText("Invalid number format");
						JUtility.errorBeep();
						numberOK = false;
					}
				}
				else
				{
					numberOK = true;
				}
			}
			else
			{
				numberOK = true;
			}
		}
		return numberOK;

	}

}
