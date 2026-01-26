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
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

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
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.scales.Scale;
import com.commander4j.scales.ScaleCallbackInteface;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JTextInputDialog;
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
public class JInternalFrameWTWeightCapture extends JInternalFrame
{

	private BigDecimal batch_mean = new BigDecimal("0.000");
	private BigDecimal lowerLimit = new BigDecimal("0.000");
	private BigDecimal mean = new BigDecimal("0.000");
	private BigDecimal std_dev = new BigDecimal("0.000");
	private BigDecimal upperLimit = new BigDecimal("0.000");
	private BigDecimal zero = new BigDecimal("0.000");
	private BlockContainer container;
	private ChartPanel chartPanel;
	private ClockListener clocklistener = new ClockListener();
	private CompositeTitle legends;
	private DateAxis axis;
	private Integer lGraphMaxPlots = 40;
	private Integer t1_count = 0;
	private Integer t2_count = 0;
	private JButton4j btnComment = new JButton4j(Common.icon_edit_16x16);
	private JButton4j btnDebug = new JButton4j();
	private JButton4j btnManualInput = new JButton4j(Common.icon_add_16x16);
	private JButton4j btn_Begin = new JButton4j(Common.icon_weight_capture_16x16);
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btn_SamplePoint_Lookup;
	private JButton4j btnj_Cancel = new JButton4j(Common.icon_cancel_16x16);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang;
	private JDBMaterial materialdb = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialCustomerData matcustdb = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder orderdb = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBWTProductGroups matgroupdb = new JDBWTProductGroups(Common.selectedHostID, Common.sessionID);
	private JDBWTSampleHeader sampleHeader = new JDBWTSampleHeader(Common.selectedHostID, Common.sessionID);
	private JDBWTSamplePoint samplePointdb = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private JDBWTTNE tnedb = new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDBWTWorkstation workdb = new JDBWTWorkstation(Common.selectedHostID, Common.sessionID);
	private JDateControl fld_currentDateTime;
	private JDesktopPane4j jDesktopPane1;
	private JFreeChart chart;
	private JLabel4j_status jStatusText;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private JLabel4j_title qty_Count = new JLabel4j_title();
	private JList4j<JDBWTSampleDetail> list_Weights = new JList4j<JDBWTSampleDetail>();
	private JQuantityInput fld_Batch_Mean = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Mean = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Nominal_Weight = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_SampleFrequency = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_SampleSize = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Standard_Deviation = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_T1_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_T2_Lower_Limit = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_TNE = new JQuantityInput(new BigDecimal("0.000"));
	private JQuantityInput fld_Tare_Weight = new JQuantityInput(new BigDecimal("0.000"));
	private JScrollPane4j scrollPane_Weights = new JScrollPane4j(JScrollPane4j.List);
	private JTextField4j fld_Container_Code = new JTextField4j(JDBQMSample.field_data_2);
	private JTextField4j fld_Description = new JTextField4j(JDBMaterial.field_description);
	private JTextField4j fld_Material;
	private JTextField4j fld_Material_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField4j fld_Nominal_Weight_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j fld_Process_Order = new JTextField4j(JDBProcessOrder.field_process_order);
	private JTextField4j fld_Process_Order_Status = new JTextField4j(JDBProcessOrder.field_status);
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JTextField4j fld_ScalePort = new JTextField4j(JDBWTWorkstation.field_ScalePort);
	private JTextField4j fld_Scale_ID = new JTextField4j(JDBWTScale.field_ScaleID);
	private JTextField4j fld_Tare_Weight_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j fld_Workstation = new JTextField4j(JDBWTWorkstation.field_WorkstationID);
	private LegendTitle legend1;
	private LegendTitle legend2;
	private LinkedList<JDBWTSampleDetail> sampleDetailList = new LinkedList<JDBWTSampleDetail>();
	private NumberAxis axis2 = new NumberAxis("Standard Deviation");
	private Scale scale;
	private String containerCode = "";
	private String materialGroup = "";
	private TimeSeries s1 = new TimeSeries("Mean Weight");
	private TimeSeries s2 = new TimeSeries("Standard Deviation");
	private TimeSeriesCollection dataset1 = new TimeSeriesCollection();
	private TimeSeriesCollection dataset2 = new TimeSeriesCollection();
	private Timer timer = new Timer(1000, clocklistener);
	private XYItemRenderer renderer;
	private XYLineAndShapeRenderer renderer2;
	private XYPlot plot;
	private boolean logEnabled = false;
	private boolean validToScan = false;
	private int lSampleFrequency = 15;
	private int lSampleSize = 5;
	private static PreparedStatement listStatement;
	private static String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private static final long serialVersionUID = 1;

	public JInternalFrameWTWeightCapture()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		String temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE SIZE", "5", "WEIGHT CHECK SAMPLE SIZE");
		lSampleSize = Integer.valueOf(temp);

		temp = ctrl.getKeyValueWithDefault("WEIGHT SAMPLE FREQUENCY", "15", "WEIGHT CHECK FREQUENCY MINS");
		lSampleFrequency = Integer.valueOf(temp);
		fld_SampleFrequency.setEnabled(false);
		fld_SampleFrequency.setText(String.valueOf(lSampleFrequency));

		temp = ctrl.getKeyValueWithDefault("WEIGHT GRAPH MAX PLOTS", "40", "WEIGHT GRAPH MAX PLOTS");
		lGraphMaxPlots = Integer.valueOf(temp);

		initGUI();

		updateProcessOrderInfo("", true);

		timer.start();
		String workstation = JUtility.getClientName().toUpperCase();

		fld_Workstation.setText(workstation);

		updateWorkstationInfo(workstation, true);
		fld_SampleSize.setEnabled(false);
		fld_SampleSize.setText(String.valueOf(lSampleSize));

		updateSamplePoint("", true);

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

		if (scale != null)
		{

			scale.shutdown(false);

			while (scale.isAlive())
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
				}
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

	private void drawGraph()
	{

		createDemoPanel();

	}

	private void createDataset()
	{

		if (validToScan)
		{

			ResultSet rs;
			JDBQuery.closeStatement(listStatement);
			JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
			query.clear();

			query.addText(JUtility.substSchemaName(schemaName, "select sample_date,sample_mean,sample_std_dev from {schema}APP_WEIGHT_SAMPLE_HEADER"));
			query.addParamtoSQL("sample_point =", samplePointdb.getSamplePoint());
			query.addParamtoSQL("process_order =", orderdb.getProcessOrder());

			Calendar calendar2 = Calendar.getInstance();
			calendar2.add(Calendar.DATE, -1);

			query.addParamtoSQL("sample_mean >", 0);

			query.appendSort("sample_date", true);
			query.applyRestriction(false, Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), 9999);

			query.bindParams();
			listStatement = query.getPreparedStatement();

			s1.clear();
			s2.clear();

			try
			{
				rs = listStatement.executeQuery();

				LinkedList<Integer> sequence = new LinkedList<Integer>();
				LinkedList<Double> means = new LinkedList<Double>();

				int count = 0;

				batch_mean = new BigDecimal("0.000");
				Calendar cal = Calendar.getInstance();
				int plotted_count = 0;

				while (rs.next())
				{

					Double d = rs.getDouble("sample_mean");
					Double stddev = rs.getDouble("sample_std_dev");
					BigDecimal t = rs.getBigDecimal("sample_mean");
					Timestamp when = rs.getTimestamp("sample_date");

					count++;

					sequence.addLast(count);
					means.addLast(d.doubleValue());
					batch_mean = batch_mean.add(t);

					plotted_count++;

					if (plotted_count <= lGraphMaxPlots)
					{

						cal.setTime(when);
						int year = cal.get(Calendar.YEAR);
						int month = cal.get(Calendar.MONTH);
						int day = cal.get(Calendar.DAY_OF_MONTH);
						int hour = cal.get(Calendar.HOUR_OF_DAY);
						int mins = cal.get(Calendar.MINUTE);
						int seconds = cal.get(Calendar.SECOND);

						try
						{
							s1.add(new Second(seconds, mins, hour, day, month + 1, year), d.doubleValue());
						}
						catch (Exception ex)
						{

						}

						try
						{
							s2.add(new Second(seconds, mins, hour, day, month + 1, year), stddev.doubleValue());
						}
						catch (Exception ex)
						{

						}
					}

				}

				dataset1.removeAllSeries();
				dataset1.addSeries(s1);

				dataset2.removeAllSeries();
				dataset2.addSeries(s2);

				if (count > 0)
				{
					batch_mean = batch_mean.divide(new BigDecimal(count), 3, RoundingMode.HALF_UP);
					fld_Batch_Mean.setText(batch_mean.toString());
					if (batch_mean.compareTo(matgroupdb.getNominalWeight()) == -1)
					{
						fld_Batch_Mean.setBackground(Color.RED);
						JLaunchMenu.runDialog("FRM_WEIGHT_ERROR", lang.get("err_MeanVNominal_p1"));
					}
					else
					{
						fld_Batch_Mean.setBackground(Common.color_app_window);
					}
				}
				else
				{
					fld_Batch_Mean.setText(zero.toString());
					fld_Batch_Mean.setBackground(Common.color_app_window);
				}

				rs.close();

			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private JFreeChart createChart()
	{

		createDataset();

		chart = ChartFactory.createTimeSeriesChart(null, null, "Mean Weight (grams)", dataset1, false, true, false);

		plot = (XYPlot) chart.getPlot();
		plot.setDomainPannable(true);
		plot.setRangePannable(true);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		plot.setDomainCrosshairLockedOnData(true);
		plot.setRangeCrosshairLockedOnData(true);

		axis2.setAutoRangeIncludesZero(true);
		plot.setRangeAxis(1, axis2);
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);

		renderer = plot.getRenderer();
		renderer.setDefaultToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());

		if (renderer instanceof XYLineAndShapeRenderer)
		{
			XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
			rr.setDefaultShapesVisible(true);
			rr.setDefaultShapesFilled(true);
		}

		renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesPaint(0, Color.black);
		renderer2.setDefaultShapesVisible(true);
		renderer2.setDefaultToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
		plot.setRenderer(1, renderer2);

		axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("MMM-dd HH:mm"));
		axis.setVerticalTickLabels(true);

		NumberAxis axis1 = (NumberAxis) plot.getRangeAxis();
		axis1.setAutoRangeMinimumSize(1.0);

		legend1 = new LegendTitle(renderer);
		legend2 = new LegendTitle(renderer2);

		container = new BlockContainer(new BorderArrangement());
		container.add(legend1, RectangleEdge.LEFT);
		container.add(legend2, RectangleEdge.RIGHT);
		container.add(new EmptyBlock(2000, 0));

		legends = new CompositeTitle(container);
		legends.setPosition(RectangleEdge.BOTTOM);

		chart.addSubtitle(legends);
		chart.setTitle(samplePointdb.getDescription());
		ChartUtils.applyCurrentTheme(chart);

		chart.setTitle(samplePointdb.getDescription());
		plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setTickLabelFont(new Font(Font.DIALOG, Font.BOLD, 10));

		return chart;
	}

	private void updateGraph()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				drawGraph();

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

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));

			btn_Help = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(btn_Help);
			btn_Help.setText(lang.get("btn_Help"));
			btn_Help.setMnemonic(java.awt.event.KeyEvent.VK_H);
			btn_Help.setBounds(402, 585, 193, 32);

			btn_Close = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(btn_Close);
			btn_Close.setText(lang.get("btn_Close"));
			btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
			btn_Close.setBounds(596, 585, 193, 32);
			btn_Close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					shutdown();
					JDBQuery.closeStatement(listStatement);
					dispose();
				}
			});

			lbl_Process_Order = new JLabel4j_std();
			jDesktopPane1.add(lbl_Process_Order);
			lbl_Process_Order.setText(lang.get("lbl_Process_Order"));
			lbl_Process_Order.setBounds(0, 37, 96, 22);
			lbl_Process_Order.setHorizontalAlignment(SwingConstants.TRAILING);

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
			fld_Process_Order.setBounds(104, 37, 93, 22);

			lbl_Material = new JLabel4j_std();
			jDesktopPane1.add(lbl_Material);
			lbl_Material.setText(lang.get("lbl_Material"));
			lbl_Material.setBounds(438, 37, 94, 22);
			lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);

			fld_Material = new JTextField4j(JDBMaterialBatch.field_batch_number);
			fld_Material.setEnabled(false);
			fld_Material.setEditable(false);
			jDesktopPane1.add(fld_Material);
			fld_Material.setBounds(538, 37, 93, 22);

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
			btn_Process_Order_Lookup.setBounds(196, 37, 21, 22);
			jDesktopPane1.add(btn_Process_Order_Lookup);

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
			btn_SamplePoint_Lookup.setBounds(438, 5, 21, 22);
			jDesktopPane1.add(btn_SamplePoint_Lookup);

			jStatusText = new JLabel4j_status();
			jStatusText.setBounds(0, 620, 1006, 41);
			jDesktopPane1.add(jStatusText);

			fld_Description.setEnabled(false);
			fld_Description.setEditable(false);

			fld_Description.setBounds(643, 37, 351, 22);
			jDesktopPane1.add(fld_Description);

			JLabel4j_std lbl_Workstation = new JLabel4j_std();
			lbl_Workstation.setText(lang.get("lbl_Workstation"));
			lbl_Workstation.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Workstation.setBounds(0, 5, 96, 22);
			jDesktopPane1.add(lbl_Workstation);
			fld_Workstation.setEditable(false);
			fld_Workstation.setEnabled(false);
			fld_Workstation.setDisabledTextColor(Color.BLACK);
			fld_Workstation.setBounds(104, 5, 134, 22);

			jDesktopPane1.add(fld_Workstation);

			JLabel4j_std lbl_SamplePoint = new JLabel4j_std();
			lbl_SamplePoint.setText(lang.get("lbl_SamplePoint"));
			lbl_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_SamplePoint.setBounds(224, 5, 108, 22);
			jDesktopPane1.add(lbl_SamplePoint);
			fld_SamplePoint.setEnabled(false);
			fld_SamplePoint.setEditable(false);
			fld_SamplePoint.setBounds(344, 5, 93, 22);
			jDesktopPane1.add(fld_SamplePoint);
			fld_Scale_ID.setEnabled(false);
			fld_Scale_ID.setEditable(false);
			fld_Scale_ID.setBounds(538, 5, 93, 22);
			jDesktopPane1.add(fld_Scale_ID);

			JLabel4j_std lbl_Scale_ID = new JLabel4j_std();
			lbl_Scale_ID.setText(lang.get("lbl_Scale_ID"));
			lbl_Scale_ID.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Scale_ID.setBounds(438, 5, 94, 22);
			jDesktopPane1.add(lbl_Scale_ID);
			fld_ScalePort.setEnabled(false);
			fld_ScalePort.setEditable(false);
			fld_ScalePort.setBounds(752, 5, 242, 22);
			jDesktopPane1.add(fld_ScalePort);

			JLabel4j_std lbl_Scale_Port = new JLabel4j_std();
			lbl_Scale_Port.setText(lang.get("lbl_Scale_Port"));
			lbl_Scale_Port.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Scale_Port.setBounds(636, 5, 94, 22);
			jDesktopPane1.add(lbl_Scale_Port);

			JLabel4j_std lbl_Material_Group = new JLabel4j_std();
			lbl_Material_Group.setText(lang.get("lbl_Material_Group"));
			lbl_Material_Group.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Material_Group.setBounds(0, 69, 96, 22);
			jDesktopPane1.add(lbl_Material_Group);
			fld_Material_Group.setEnabled(false);
			fld_Material_Group.setEditable(false);

			fld_Material_Group.setBounds(104, 69, 113, 22);
			jDesktopPane1.add(fld_Material_Group);

			JLabel4j_std lbl_Container_Code = new JLabel4j_std();
			lbl_Container_Code.setText(lang.get("lbl_Container_Code"));
			lbl_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Container_Code.setBounds(224, 69, 108, 22);
			jDesktopPane1.add(lbl_Container_Code);
			fld_Container_Code.setEnabled(false);
			fld_Container_Code.setEditable(false);

			fld_Container_Code.setBounds(346, 69, 93, 22);
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
						btnComment.setEnabled(false);
						fld_Mean.setText("0.000");
						fld_Batch_Mean.setText("0.000");
						fld_Batch_Mean.setBackground(Common.color_app_window);
						fld_Standard_Deviation.setText("0.000");
						logEnabled = true;
						jStatusText.setText("Start weighing " + lSampleSize + " samples");
						sampleDetailList.clear();
						populateList();
					}
				}
			});

			btn_Begin.setText(lang.get("lbl_Begin_Weight_Check"));
			btn_Begin.setMnemonic('0');
			btn_Begin.setBounds(14, 585, 193, 32);
			jDesktopPane1.add(btn_Begin);

			JLabel4j_std lbl_Process_Order_Status = new JLabel4j_std();
			lbl_Process_Order_Status.setText(lang.get("lbl_Process_Order_Status"));
			lbl_Process_Order_Status.setHorizontalAlignment(SwingConstants.TRAILING);
			lbl_Process_Order_Status.setBounds(224, 37, 108, 22);
			jDesktopPane1.add(lbl_Process_Order_Status);
			fld_Process_Order_Status.setEnabled(false);
			fld_Process_Order_Status.setEditable(false);

			fld_Process_Order_Status.setText("");
			fld_Process_Order_Status.setBounds(344, 37, 93, 22);
			jDesktopPane1.add(fld_Process_Order_Status);
			fld_Nominal_Weight.setEnabled(false);
			fld_Nominal_Weight.setEditable(false);

			fld_Nominal_Weight.setVerifyInputWhenFocusTarget(false);
			fld_Nominal_Weight.setBounds(583, 69, 73, 22);
			jDesktopPane1.add(fld_Nominal_Weight);
			fld_Nominal_Weight_UOM.setEnabled(false);
			fld_Nominal_Weight_UOM.setEditable(false);

			fld_Nominal_Weight_UOM.setText("");
			fld_Nominal_Weight_UOM.setPreferredSize(new Dimension(40, 20));
			fld_Nominal_Weight_UOM.setFocusCycleRoot(true);
			fld_Nominal_Weight_UOM.setCaretPosition(0);
			fld_Nominal_Weight_UOM.setBounds(668, 69, 50, 22);
			jDesktopPane1.add(fld_Nominal_Weight_UOM);
			fld_Tare_Weight.setEnabled(false);
			fld_Tare_Weight.setEditable(false);

			fld_Tare_Weight.setVerifyInputWhenFocusTarget(false);
			fld_Tare_Weight.setBounds(839, 69, 73, 22);
			jDesktopPane1.add(fld_Tare_Weight);
			fld_Tare_Weight_UOM.setEnabled(false);
			fld_Tare_Weight_UOM.setEditable(false);

			fld_Tare_Weight_UOM.setText("");
			fld_Tare_Weight_UOM.setPreferredSize(new Dimension(40, 20));
			fld_Tare_Weight_UOM.setFocusCycleRoot(true);
			fld_Tare_Weight_UOM.setCaretPosition(0);
			fld_Tare_Weight_UOM.setBounds(924, 69, 50, 22);
			jDesktopPane1.add(fld_Tare_Weight_UOM);

			JLabel4j_std lbl_Nominal_Weight = new JLabel4j_std();
			lbl_Nominal_Weight.setText(lang.get("lbl_Nominal_Weight"));
			lbl_Nominal_Weight.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Nominal_Weight.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Nominal_Weight.setBounds(448, 69, 126, 22);
			jDesktopPane1.add(lbl_Nominal_Weight);

			JLabel4j_std lbl_Tare_Weight = new JLabel4j_std();
			lbl_Tare_Weight.setText(lang.get("lbl_Tare_Weight"));
			lbl_Tare_Weight.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Tare_Weight.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Tare_Weight.setBounds(722, 69, 108, 22);
			jDesktopPane1.add(lbl_Tare_Weight);

			JLabel4j_std lbl_T1_Lower_Limit = new JLabel4j_std();
			lbl_T1_Lower_Limit.setText(lang.get("lbl_T1_Lower_Limit"));
			lbl_T1_Lower_Limit.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_T1_Lower_Limit.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_T1_Lower_Limit.setBounds(448, 101, 126, 22);
			jDesktopPane1.add(lbl_T1_Lower_Limit);

			JLabel4j_std lbl_Mean = new JLabel4j_std();
			lbl_Mean.setText(lang.get("lbl_Average_Mean"));
			lbl_Mean.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Mean.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Mean.setBounds(786, 510, 126, 22);
			jDesktopPane1.add(lbl_Mean);

			JLabel4j_title lbl_Count = new JLabel4j_title();
			lbl_Count.setForeground(UIManager.getColor("OptionPane.background"));
			lbl_Count.setOpaque(true);
			lbl_Count.setBackground(UIManager.getColor("OptionPane.questionDialog.border.background"));
			lbl_Count.setFont(new Font("Arial", Font.BOLD, 12));
			lbl_Count.setText(lang.get("web_Count") + "  ");
			lbl_Count.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Count.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Count.setBounds(798, 453, 102, 25);
			jDesktopPane1.add(lbl_Count);
			qty_Count.setForeground(UIManager.getColor("OptionPane.background"));
			qty_Count.setBackground(UIManager.getColor("OptionPane.questionDialog.border.background"));
			qty_Count.setOpaque(true);
			qty_Count.setFont(new Font("Arial", Font.BOLD, 12));

			qty_Count.setText("");
			qty_Count.setHorizontalTextPosition(SwingConstants.RIGHT);
			qty_Count.setHorizontalAlignment(SwingConstants.LEFT);
			qty_Count.setBounds(900, 453, 94, 25);
			jDesktopPane1.add(qty_Count);

			JLabel4j_std lbl_Batch_Mean = new JLabel4j_std();
			lbl_Batch_Mean.setText(lang.get("lbl_Average_Order_Mean"));
			lbl_Batch_Mean.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Batch_Mean.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Batch_Mean.setBounds(786, 564, 126, 22);
			jDesktopPane1.add(lbl_Batch_Mean);

			JLabel4j_std lbl_Standard_Deviation = new JLabel4j_std();
			lbl_Standard_Deviation.setText(lang.get("lbl_Standard_Deviation"));
			lbl_Standard_Deviation.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_Standard_Deviation.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_Standard_Deviation.setBounds(786, 537, 126, 22);
			jDesktopPane1.add(lbl_Standard_Deviation);
			fld_T1_Lower_Limit.setEnabled(false);

			fld_T1_Lower_Limit.setVerifyInputWhenFocusTarget(false);
			fld_T1_Lower_Limit.setEditable(false);
			fld_T1_Lower_Limit.setBounds(583, 101, 73, 22);
			jDesktopPane1.add(fld_T1_Lower_Limit);
			fld_Mean.setEnabled(false);

			fld_Mean.setVerifyInputWhenFocusTarget(false);
			fld_Mean.setFont(new Font("Arial", Font.PLAIN, 14));
			fld_Mean.setEditable(false);
			fld_Mean.setBounds(921, 510, 73, 22);
			jDesktopPane1.add(fld_Mean);
			fld_Batch_Mean.setEnabled(false);

			fld_Batch_Mean.setVerifyInputWhenFocusTarget(false);
			fld_Batch_Mean.setFont(new Font("Arial", Font.PLAIN, 14));
			fld_Batch_Mean.setEditable(false);
			fld_Batch_Mean.setBounds(921, 564, 73, 22);
			jDesktopPane1.add(fld_Batch_Mean);
			fld_Standard_Deviation.setEnabled(false);

			fld_Standard_Deviation.setVerifyInputWhenFocusTarget(false);
			fld_Standard_Deviation.setFont(new Font("Arial", Font.PLAIN, 14));
			fld_Standard_Deviation.setEditable(false);
			fld_Standard_Deviation.setBounds(921, 537, 73, 22);
			jDesktopPane1.add(fld_Standard_Deviation);

			JLabel4j_std lbl_T2_Lower_Limit = new JLabel4j_std();
			lbl_T2_Lower_Limit.setText(lang.get("lbl_T2_Lower_Limit"));
			lbl_T2_Lower_Limit.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_T2_Lower_Limit.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_T2_Lower_Limit.setBounds(722, 101, 108, 22);
			jDesktopPane1.add(lbl_T2_Lower_Limit);
			fld_T2_Lower_Limit.setEnabled(false);

			fld_T2_Lower_Limit.setVerifyInputWhenFocusTarget(false);
			fld_T2_Lower_Limit.setEditable(false);
			fld_T2_Lower_Limit.setBounds(839, 101, 73, 22);
			jDesktopPane1.add(fld_T2_Lower_Limit);

			JLabel4j_std lbl_SampleSize = new JLabel4j_std();
			lbl_SampleSize.setText(lang.get("lbl_SampleSize"));
			lbl_SampleSize.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_SampleSize.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_SampleSize.setBounds(0, 101, 96, 22);
			jDesktopPane1.add(lbl_SampleSize);

			JLabel4j_std lbl_SampleFrequency = new JLabel4j_std();
			lbl_SampleFrequency.setText(lang.get("lbl_SampleFrequency"));
			lbl_SampleFrequency.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_SampleFrequency.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_SampleFrequency.setBounds(142, 101, 96, 22);
			jDesktopPane1.add(lbl_SampleFrequency);

			fld_SampleSize.setVerifyInputWhenFocusTarget(false);
			fld_SampleSize.setEditable(false);
			fld_SampleSize.setBounds(104, 101, 38, 22);
			jDesktopPane1.add(fld_SampleSize);

			fld_SampleFrequency.setVerifyInputWhenFocusTarget(false);
			fld_SampleFrequency.setEditable(false);
			fld_SampleFrequency.setBounds(246, 101, 38, 22);
			jDesktopPane1.add(fld_SampleFrequency);

			JLabel4j_std lbl_TNE = new JLabel4j_std();
			lbl_TNE.setText(lang.get("lbl_TNE"));
			lbl_TNE.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl_TNE.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_TNE.setBounds(224, 101, 108, 22);
			jDesktopPane1.add(lbl_TNE);
			fld_TNE.setEnabled(false);

			fld_TNE.setVerifyInputWhenFocusTarget(false);
			fld_TNE.setHorizontalAlignment(SwingConstants.TRAILING);
			fld_TNE.setEditable(false);
			fld_TNE.setBounds(364, 101, 73, 22);
			jDesktopPane1.add(fld_TNE);

			scrollPane_Weights.setBounds(798, 150, 196, 303);
			jDesktopPane1.add(scrollPane_Weights);
			list_Weights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane_Weights.setViewportView(list_Weights);

			JLabel4j_std lbl_Legend = new JLabel4j_std(" Gross Wt     Net Wt");
			lbl_Legend.setFont(new Font("Monospaced", Font.BOLD, 11));
			lbl_Legend.setBounds(798, 133, 184, 15);
			jDesktopPane1.add(lbl_Legend);

			fld_currentDateTime = new JDateControl();
			fld_currentDateTime.setDisplayMode(JDateControl.mode_disable_visible);
			jDesktopPane1.add(fld_currentDateTime);
			fld_currentDateTime.setEnabled(false);
			fld_currentDateTime.setVisible(false);
			fld_currentDateTime.setBounds(830, 33, 120, 25);
			JTextField tf = ((JSpinner.DefaultEditor) fld_currentDateTime.getEditor()).getTextField();
			tf.setEnabled(false);
			tf.setDisabledTextColor(UIManager.getColor("TextField.foreground"));

			btnj_Cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					btn_Begin.setEnabled(true);
					btnj_Cancel.setEnabled(false);
					btnManualInput.setEnabled(false);
					btnComment.setEnabled(false);
					fld_Mean.setText("0.000");
					fld_Batch_Mean.setText("0.000");
					fld_Batch_Mean.setBackground(Common.color_app_window);
					fld_Standard_Deviation.setText("0.000");
					logEnabled = false;
					sampleDetailList.clear();
					populateList();
					jStatusText.setText("Cancelled");

				}
			});
			btnj_Cancel.setEnabled(false);

			btnj_Cancel.setText(lang.get("lbl_Cancel_Weight_Check"));
			btnj_Cancel.setMnemonic('0');
			btnj_Cancel.setBounds(208, 585, 193, 32);
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
					double target = Double.valueOf(fld_Nominal_Weight.getText());
					double tne = Double.valueOf(fld_TNE.getText());
					int rand1 = ThreadLocalRandom.current().nextInt(0, 2);
					int rand2 = ThreadLocalRandom.current().nextInt(0, 1);
					int rand3 = ThreadLocalRandom.current().nextInt(0, 2);
					double random = ThreadLocalRandom.current().nextDouble(target - (tne * rand1) - rand2, target + (tne * 2) + rand3);
					BigDecimal rnd = BigDecimal.valueOf(random);
					rnd = rnd.divide(new BigDecimal(1), 3, RoundingMode.HALF_UP);
					String result = validateWeight(rnd.toString());
					if ((result.equals("error") == false) && (result.equals("") == false))
					{
						logSampleWeight(rnd.toString(), "G");
					}
				}
			});
			btnDebug.setText("Debug");

			btnDebug.setBounds(643, 32, 196, 25);
			jDesktopPane1.add(btnDebug);

			btnManualInput.setBounds(798, 483, 196, 25);
			jDesktopPane1.add(btnManualInput);
			btnManualInput.setEnabled(false);
			btnComment.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					displayCommentInputPanel();
				}
			});

			btnComment.setText(lang.get("lbl_Add_Comment"));
			btnComment.setEnabled(false);
			btnComment.setBounds(798, 592, 196, 25);
			jDesktopPane1.add(btnComment);
			btnComment.setEnabled(false);

			createDemoPanel();
			chartPanel.setBounds(14, 133, 772, 445);

			jDesktopPane1.add(chartPanel);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void displayCommentInputPanel()
	{
		String comments = sampleHeader.getComments();
		JTextInputDialog textInp = new JTextInputDialog(Common.mainForm, comments, JDBWTSampleHeader.field_Comments);
		textInp.setVisible(true);
		if (textInp.isTextEntered())
		{

			sampleHeader.setComments(textInp.getTextEntered());
			sampleHeader.updateComments();
		}
		textInp = null;
	}

	public void createDemoPanel()
	{

		chart = createChart();
		chartPanel = new ChartPanel(chart, false);
		chartPanel.setMouseZoomable(false);
		chartPanel.setMouseWheelEnabled(false);
		chartPanel.setPopupMenu(null);

	}

	private boolean logSampleWeight(String weight, String weightUOM)
	{
		boolean result = true;

		if (logEnabled)
		{
			BigDecimal current = new BigDecimal(weight);

			if ((current.compareTo(lowerLimit) == 1) && (current.compareTo(upperLimit) == -1))
			{

				jStatusText.setText("Recording " + weight + " " + weightUOM);
				JDBWTSampleDetail sampleDetail = new JDBWTSampleDetail(Common.selectedHostID, Common.sessionID);
				sampleDetail.setSamplePoint(samplePointdb.getSamplePoint());
				sampleDetail.setSampleDate(sampleHeader.getSampleDate());
				sampleDetail.setSampleWeightDate(JUtility.getSQLDateTime());

				sampleDetail.setSampleSequence(sampleDetailList.size() + 1);
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

				if (sampleDetailList.size() == lSampleSize)
				{

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

					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							updateGraph();
						}
					});

					if (t2_count > 0)
					{
						JLaunchMenu.runDialog("FRM_WEIGHT_ERROR", lang.get("err_T2_p1") + lang.get("err_T1_p2"));
						displayCommentInputPanel();

					}
					else
					{
						if (t1_count > 0)
						{
							JLaunchMenu.runDialog("FRM_WEIGHT_ERROR", lang.get("err_T1_p1") + lang.get("err_T1_p2"));
							displayCommentInputPanel();
						}
					}

				}
				else
				{
					jStatusText.setText("Weigh sample " + String.valueOf(sampleDetailList.size() + 1) + " of " + String.valueOf(lSampleSize));
				}
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
			result = result.divide(new BigDecimal(count), 3, RoundingMode.HALF_UP);
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

		qty_Count.setText(String.valueOf(sampleDetailList.size()));

		SwingUtilities.invokeLater(() -> {
			JScrollBar bar = scrollPane_Weights.getVerticalScrollBar();
			bar.setValue(bar.getMaximum());
		});

	}

	private boolean readyToLog()
	{
		boolean result = true;

		return result;
	}
	// updateOrderInfo -- > updateMaterialInfo

	private boolean saveAll(BigDecimal mean, BigDecimal StdDev, Integer t1s, Integer t2s)
	{
		boolean result = true;
		String error = "";

		jStatusText.setText("");

		jStatusText.setText("Saving results to database...");

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
		sampleHeader.setSampleCount(sampleDetailList.size());
		sampleHeader.setSampleMean(mean);
		sampleHeader.setSampleStdDev(StdDev);
		sampleHeader.setSampleT1Count(t1s);
		sampleHeader.setSampleT2Count(t2s);
		sampleHeader.setComments("");

		if (sampleHeader.create() == false)
		{
			result = false;
			error = sampleHeader.getErrorMessage();
		}
		if (result == true)
		{
			if (sampleHeader.update() == false)
			{
				result = false;
				error = sampleHeader.getErrorMessage();
			}

			if (result == true)
			{

				for (int j = 0; j < sampleDetailList.size(); j++)
				{
					JDBWTSampleDetail t = (JDBWTSampleDetail) sampleDetailList.get(j);
					t.setSampleDate(sampleHeader.getSampleDate());

					if (result == true)
					{
						if (t.create() == false)
						{
							result = false;
							error = sampleHeader.getErrorMessage();
						}

						if (result == true)
						{

							if (t.update() == false)
							{
								result = false;
								error = sampleHeader.getErrorMessage();
							}
						}
					}
				}

			}
		}

		if (result == true)
		{
			jStatusText.setText("Results have been saved.");
		}
		else
		{
			jStatusText.setText("Error saving results.");
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, error, lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
		}
		btnComment.setEnabled(true);

		return result;

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
					fld_Material_Group.setBackground(Common.color_textfield_background_disabled);
					materialGroup = data;
				}

				if (key.equals("CONTAINER_CODE"))
				{
					fld_Container_Code.setText(data);
					fld_Container_Code.setBackground(Common.color_textfield_background_disabled);
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
				fld_Material_Group.setBackground(Common.color_textfield_background_disabled);
			}

			if (key.equals("CONTAINER_CODE"))
			{
				fld_Container_Code.setText("");
				fld_Container_Code.setBackground(Common.color_textfield_background_disabled);
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
				fld_Material.setBackground(Common.color_textfield_background_disabled);

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
			fld_Material.setBackground(Common.color_textfield_background_disabled);
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
		fld_Mean.setText("0.000");
		fld_Batch_Mean.setText("0.000");
		fld_Batch_Mean.setBackground(Common.color_app_window);
		fld_Standard_Deviation.setText("0.000");
		btnComment.setEnabled(false);

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

				// fld_Process_Order.setBackground(Color.WHITE);
				fld_Process_Order_Status.setText(status);
				fld_Material.setText(material);

				if (status.equals("Ready") || (status.equals("Running")))
				{
					fld_Process_Order_Status.setBackground(Common.color_textfield_background_disabled);
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
				fld_Process_Order_Status.setBackground(Common.color_textfield_background_disabled);
			}
		}
		else
		{
			fld_Process_Order_Status.setText("");
			fld_Process_Order_Status.setBackground(Common.color_textfield_background_disabled);
			result = false;
		}
		sampleDetailList.clear();
		populateList();
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
			if (matgroupdb.getProductGroupProperties(group))
			{
				result = true;
				nominal = matgroupdb.getNominalWeight();
				nominalUom = matgroupdb.getNominalUOM();
				tare = matgroupdb.getTareWeight();
				tarelUom = matgroupdb.getTareWeightUOM();
				lowerLimit = matgroupdb.getLowerLimit();
				upperLimit = matgroupdb.getUpperLimit();

				if (workdb.isOverrideSampleSize())
				{
					lSampleSize = workdb.getSampleSize();
				}
				else
				{
					lSampleSize = matgroupdb.getSamplesRequired();
				}

				fld_SampleSize.setText(String.valueOf(lSampleSize));

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
					fld_Nominal_Weight.setBackground(Common.color_textfield_background_disabled);
				}

				if (nominalUom.equals(""))
				{
					fld_Nominal_Weight_UOM.setBackground(Color.YELLOW);
				}
				else
				{
					fld_Nominal_Weight_UOM.setBackground(Common.color_textfield_background_disabled);
				}

				if (tare.compareTo(zero) == 0)
				{
					fld_Tare_Weight.setBackground(Color.YELLOW);
				}
				else
				{
					fld_Tare_Weight.setBackground(Common.color_textfield_background_disabled);
				}

				if (tarelUom.equals(""))
				{
					fld_Tare_Weight_UOM.setBackground(Color.YELLOW);
				}
				else
				{
					fld_Tare_Weight_UOM.setBackground(Common.color_textfield_background_disabled);
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
			fld_Nominal_Weight.setBackground(Common.color_textfield_background_disabled);
			fld_Nominal_Weight_UOM.setBackground(Common.color_textfield_background_disabled);
			fld_Tare_Weight.setBackground(Common.color_textfield_background_disabled);
			fld_Tare_Weight_UOM.setBackground(Common.color_textfield_background_disabled);
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
				fld_SamplePoint.setBackground(Common.color_textfield_background_disabled);

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
			fld_SamplePoint.setBackground(Common.color_textfield_background_disabled);
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
					fld_T1_Lower_Limit.setBackground(Common.color_textfield_background_disabled);
				}

				if (negt2.compareTo(zero) == 0)
				{
					fld_T2_Lower_Limit.setBackground(Color.YELLOW);
				}
				else
				{
					fld_T2_Lower_Limit.setBackground(Common.color_textfield_background_disabled);
				}

				if (tne.compareTo(zero) == 0)
				{
					fld_TNE.setBackground(Color.YELLOW);
				}
				else
				{
					fld_TNE.setBackground(Common.color_textfield_background_disabled);
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

			fld_T1_Lower_Limit.setBackground(Common.color_textfield_background_disabled);
			fld_T2_Lower_Limit.setBackground(Common.color_textfield_background_disabled);
			fld_TNE.setBackground(Common.color_textfield_background_disabled);
		}

		if (fld_SamplePoint.getText().equals(""))
		{
			result = false;
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
				if (workdb.isOverrideSampleSize())
				{
					lSampleSize = Integer.valueOf(workdb.getSampleSize());
				}
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
			fld_Workstation.setBackground(Common.color_textfield_background_disabled);

		}
		else
		{
			fld_Workstation.setBackground(Color.YELLOW);

		}

		if (result == true)
		{
			scale = new Scale(Common.selectedHostID, Common.sessionID);

			class callback implements ScaleCallbackInteface
			{
				@Override
				public void setWeight(String weight)
				{
					String result = validateWeight(weight);

					if ((result.equals("error") == false) && (result.equals("") == false))
					{
						logSampleWeight(result, "G");
					}

				}
			}
			callback cb = new callback();

			scale.setCallbackInterface(cb);

			scale.start();

		}

		updateSamplePoint(samplePoint, result);

		return result;
	}

	private boolean addManualWeight()
	{
		boolean result = false;
		String numberOK = "error";
		String grossWt = "";

		while (numberOK.equals("error"))
		{
			grossWt = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Gross_Weight_Grams"), grossWt);

			numberOK = validateWeight(grossWt);

			if ((numberOK.equals("error") == false) && (numberOK.equals("") == false))
			{
				logSampleWeight(numberOK, "G");
				jStatusText.setText("");
				result = true;
			}
			else
			{
				if (numberOK.equals("error"))
				{
					jStatusText.setText("Invalid number format");
					JUtility.errorBeep();
				}
			}

		}
		return result;

	}

	private String validateWeight(String grossWt)
	{
		String result = "error";

		if (grossWt != null)
		{
			if (grossWt.equals("") == false)
			{
				try
				{
					BigDecimal gw1 = new BigDecimal("0.000");
					BigDecimal gw2 = new BigDecimal(grossWt);
					gw1 = gw1.add(gw2);
					result = gw1.toString();
				}
				catch (Exception ex)
				{
					result = "error";
				}
			}
			else
			{
				result = "";
			}
		}
		else
		{
			result = "";
		}

		return result;
	}
}
