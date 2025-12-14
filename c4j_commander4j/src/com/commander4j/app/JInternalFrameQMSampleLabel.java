package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMSampleLabel.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUserReport;
import com.commander4j.db.JUserReportParameter;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameQMSampleLabel is used printing sample labels and populating
 * the APP_QM_SAMPLE table
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMSampleLabel.jpg" >
 * 
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 */
public class JInternalFrameQMSampleLabel extends JInternalFrame
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldProcessOrder;
	private JTextField4j textFieldDescription;
	private JTextField4j textFieldInspectionID;
	private JTextField4j textFieldMaterial;
	private JTextField4j textFieldStatus;
	private JButton4j btnPrint;
	private JButton4j btnClose;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBQMActivity activity = new JDBQMActivity(Common.selectedHostID, Common.sessionID);
	private JDBQMSample sample = new JDBQMSample(Common.selectedHostID, Common.sessionID);
	private JDBUserReport userReport = new JDBUserReport(Common.selectedHostID, Common.sessionID);
	private JDBMaterialCustomerData matCustData = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
	private JComboBoxPODevices4j comboBoxPrintQueue;
	private JSpinner4j spinnerCopies;
	private JList4j<JDBQMActivity> listActivities;
	private JTextField4j textFieldResource;
	private JDateControl dueDate;
	private PreparedStatement listStatement;
	private JLabel4j_std lblUserData1;
	private JLabel4j_std lblUserData2;
	private JTextField4j textFieldUserData1;
	private JTextField4j textFieldUserData2;
	private ClockListener clocklistener = new ClockListener();
	private Timer timer = new Timer(1000, clocklistener);
	private JLabel4j_std lblStatusBar;
	private Boolean qmud1 = false;
	private Boolean qmud2 = false;
	private Boolean qmud3 = false;
	private Boolean qmud4 = false;
	private Boolean processOrderValid = false;
	private JDateControl sampleDateTime;
	private JCheckBox4j chckbxAutoTime;
	private JRadioButton4j resolution_200dpi = new JRadioButton4j("200 dpi");
	private JRadioButton4j resolution_300dpi = new JRadioButton4j("300 dpi");
	private JRadioButton4j resolution_default = new JRadioButton4j("Default Resolution");
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;
	private JButton4j jButtonLookup_Shift_Names;
	private JButton4j jButtonLookup_Packing_Line;
	private String selectedModuleId = "RPT_SAMPLE_LABEL";

	public class ClockListener implements ActionListener
	{
		int hour = 0;
		int min = 0;
		int sec = 0;

		String hours = "";
		String mins = "";
		String secs = "";

		public void actionPerformed(ActionEvent event)
		{
			if (chckbxAutoTime.isSelected() == false)
			{
				Calendar rightNow = Calendar.getInstance();
				sampleDateTime.setDate(JUtility.getSQLDate(rightNow));
			}
		}
	}

	private void printEnable()
	{
		Boolean result = false;
		Boolean ud1 = false;
		Boolean ud2 = false;
		Boolean ud3 = false;
		Boolean ud4 = false;

		if (processOrderValid == true)
		{
			if (listActivities.getSelectedIndex() != -1)
			{
				ud1 = false;
				if (qmud1 == true)
				{
					if (textFieldUserData1.getText().equals("") == false)
					{
						ud1 = true;
					}
				}
				else
				{
					ud1 = true;
				}

				ud2 = false;
				if (qmud2 == true)
				{
					if (textFieldUserData2.getText().equals("") == false)
					{
						ud2 = true;
					}
				}
				else
				{
					ud2 = true;
				}

				ud3 = false;
				if (qmud3 == true)
				{
					if (textFieldUserData3.getText().equals("") == false)
					{
						ud3 = true;
					}
				}
				else
				{
					ud3 = true;
				}

				ud4 = false;
				if (qmud4 == true)
				{
					if (textFieldUserData4.getText().equals("") == false)
					{
						ud4 = true;
					}
				}
				else
				{
					ud4 = true;
				}

				if (ud1 & ud2 & ud3 & ud4)
				{
					result = true;
				}

			}
		}
		btnPrint.setEnabled(result);
	}

	public void processOrderChanged(String processOrder)
	{
		if (po.isValidProcessOrder(processOrder))
		{
			po.getProcessOrderProperties(processOrder);
			textFieldProcessOrder.setText(po.getProcessOrder());
			textFieldDescription.setText(po.getDescription());
			textFieldInspectionID.setText(po.getInspectionID());
			textFieldMaterial.setText(po.getMaterial());
			textFieldStatus.setText(po.getStatus());
			textFieldResource.setText(po.getRequiredResource());
			dueDate.setDate(po.getDueDate());
			dueDate.setDisplayMode(JDateControl.mode_disable_visible);
			processOrderValid = true;

			if (textFieldStatus.getText().equals("Ready") || (textFieldStatus.getText().equals("Running")))
			{
				textFieldStatus.setBackground(Color.WHITE);
			}
			else
			{
				textFieldStatus.setBackground(Color.RED);
			}

			if (userReport.getUserReportProperties("USER_DATA_1"))
			{
				JUserReportParameter param1 = new JUserReportParameter();
				param1.parameterPosition = 1;
				param1.parameterType = "String";
				param1.parameterStringValue = po.getProcessOrder();
				LinkedList<JUserReportParameter> paramList = new LinkedList<JUserReportParameter>();
				paramList.add(param1);
				userReport.setSYSTEMparameters(paramList);
				if (userReport.runReport())
				{
					textFieldUserData1.setText(userReport.getSystemResultData());
				}
			}
			else
			{
				textFieldUserData1.setText("");
			}

			if (userReport.getUserReportProperties("USER_DATA_2"))
			{
				JUserReportParameter param1 = new JUserReportParameter();
				param1.parameterPosition = 1;
				param1.parameterType = "String";
				param1.parameterStringValue = po.getProcessOrder();
				LinkedList<JUserReportParameter> paramList = new LinkedList<JUserReportParameter>();
				paramList.add(param1);
				userReport.setSYSTEMparameters(paramList);
				if (userReport.runReport())
				{
					textFieldUserData2.setText(userReport.getSystemResultData());
				}
			}
			else
			{
				textFieldUserData2.setText("");
			}

			if (userReport.getUserReportProperties("USER_DATA_3"))
			{
				JUserReportParameter param1 = new JUserReportParameter();
				param1.parameterPosition = 1;
				param1.parameterType = "String";
				param1.parameterStringValue = po.getProcessOrder();
				LinkedList<JUserReportParameter> paramList = new LinkedList<JUserReportParameter>();
				paramList.add(param1);
				userReport.setSYSTEMparameters(paramList);
				if (userReport.runReport())
				{
					textFieldUserData3.setText(userReport.getSystemResultData());
				}
			}
			else
			{
				textFieldUserData3.setText("");
			}

			if (userReport.getUserReportProperties("USER_DATA_4"))
			{
				JUserReportParameter param1 = new JUserReportParameter();
				param1.parameterPosition = 1;
				param1.parameterType = "String";
				param1.parameterStringValue = po.getProcessOrder();
				LinkedList<JUserReportParameter> paramList = new LinkedList<JUserReportParameter>();
				paramList.add(param1);
				userReport.setSYSTEMparameters(paramList);
				if (userReport.runReport())
				{
					textFieldUserData4.setText(userReport.getSystemResultData());
				}
			}
			else
			{
				textFieldUserData4.setText("");
			}

			matCustData.setMaterial(po.getMaterial());
			matCustData.setCustomerID(po.getCustomerID());
			matCustData.setDataID("PRODUCT_GROUP");

			if (matCustData.getMaterialCustomerDataProperties())
			{
				textFieldUserData4.setText(matCustData.getData());
			}

			matCustData.setDataID("CONTAINER_CODE");

			if (matCustData.getMaterialCustomerDataProperties())
			{
				textFieldUserData1.setText(matCustData.getData());
			}

			populateActivityList(po.getInspectionID());
		}
		else
		{
			textFieldDescription.setText("");
			textFieldInspectionID.setText("");
			textFieldMaterial.setText("");
			textFieldStatus.setText("");
			textFieldResource.setText("");
			textFieldUserData1.setText("");
			textFieldUserData2.setText("");
			textFieldUserData3.setText("");
			textFieldUserData4.setText("");
			dueDate.setDate(JUtility.getSQLDate());
			dueDate.setDisplayMode(JDateControl.mode_disable_not_visible);
			populateActivityList("");
			processOrderValid = false;
		}
		lblStatusBar.setText("");
		printEnable();
	}

	private void populateActivityList(String inspectionid)
	{
		DefaultComboBoxModel<JDBQMActivity> defComboBoxMod = new DefaultComboBoxModel<JDBQMActivity>();

		LinkedList<JDBQMActivity> tempActivityList = activity.getActivities(inspectionid);

		int sel = -1;
		for (int j = 0; j < tempActivityList.size(); j++)
		{
			defComboBoxMod.addElement(tempActivityList.get(j));
		}

		ListModel<JDBQMActivity> jList1Model = defComboBoxMod;
		listActivities.setModel(jList1Model);

		listActivities.setCellRenderer(Common.renderer_list);
		listActivities.ensureIndexIsVisible(sel);
	}

	private String getDefaultDPI()
	{
		String result = "";
		String separator = "";

		if (resolution_300dpi.isSelected())
		{
			result = "300";
			separator = "_";

		}
		if (resolution_200dpi.isSelected())
		{
			result = "200";
			separator = "_";

		}
		if (resolution_default.isSelected())
		{
			result = "";
			separator = "";

		}

		result = separator + result;

		return result;
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

	public void refreshQueue()
	{
		comboBoxPrintQueue.refreshData(selectedModuleId + getDefaultDPI(), "");
		getDefaultDPI();
		setSelectedDPIRadioButton();
	}

	private void setSelectedDPIRadioButton()
	{
		JPrintDevice sel = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
		String dpi = "";

		if (sel.getType().equals("queue"))
		{
			String queueName = sel.getQueue().getName();

			if (queueName.contains("200"))
			{
				dpi = "200";
			}
			if (queueName.contains("203"))
			{
				dpi = "200";
			}
			if (queueName.contains("300"))
			{
				dpi = "300";
			}
			if (queueName.contains("305"))
			{
				dpi = "300";
			}
		}

		if (sel.getType().equals("printer"))
		{
			dpi = sel.getPrinter().getDPI();
		}

		switch (dpi)
		{
		case "200":
			resolution_200dpi.setSelected(true);
			break;
		case "300":
			resolution_300dpi.setSelected(true);
			break;
		default:
			break;
		}
	}

	public JInternalFrameQMSampleLabel(String processOrder)
	{

		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				timer.stop();

				while (timer.isRunning())
				{
				}

				timer = null;
			}
		});

		qmud1 = Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_1_REQD", "true", "QM USER DATA 1 REQD"));
		qmud2 = Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_2_REQD", "true", "QM USER DATA 2 REQD"));
		qmud3 = Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_3_REQD", "true", "QM USER DATA 3 REQD"));
		qmud4 = Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_4_REQD", "true", "QM USER DATA 4 REQD"));

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 581, 618);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_app_window);
		desktopPane.setBounds(0, 0, 587, 631);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		lblProcessOrder.setBounds(6, 10, 111, 22);
		desktopPane.add(lblProcessOrder);

		JLabel4j_std lblDescription = new JLabel4j_std(lang.get("lbl_Description"));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(6, 40, 111, 22);
		desktopPane.add(lblDescription);

		textFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
		textFieldProcessOrder.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				processOrderChanged(textFieldProcessOrder.getText());
			}
		});
		textFieldProcessOrder.setBounds(125, 10, 115, 22);
		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		textFieldDescription = new JTextField4j(JDBProcessOrder.field_description);
		textFieldDescription.setEditable(false);
		textFieldDescription.setBounds(125, 40, 420, 22);
		desktopPane.add(textFieldDescription);
		textFieldDescription.setColumns(10);

		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setEditable(false);
		textFieldInspectionID.setBounds(125, 70, 134, 22);
		desktopPane.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);

		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblInspectionID.setBounds(6, 70, 111, 22);
		desktopPane.add(lblInspectionID);

		JLabel4j_std lblNewLabel_3 = new JLabel4j_std(lang.get("lbl_Activity_ID"));
		lblNewLabel_3.setBounds(30, 226, 111, 22);
		desktopPane.add(lblNewLabel_3);

		btnPrint = new JButton4j(lang.get("btn_Print"));
		btnPrint.setEnabled(false);
		btnPrint.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Long sampleID = sample.generateSampleID();
				String activityID = ((JDBQMActivity) listActivities.getSelectedValue()).getActivityID();
				Timestamp ts = JUtility.getTimestampFromDate(sampleDateTime.getDate());
				sample.create(sampleID, po.getInspectionID(), activityID, po.getProcessOrder(), po.getMaterial(), textFieldUserData1.getText(), textFieldUserData2.getText(), textFieldUserData3.getText(), textFieldUserData4.getText(), ts);
				JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
				buildSQL1Record(sampleID);

				JLaunchReport.runReport(selectedModuleId + getDefaultDPI(), listStatement, false, pq, Integer.valueOf(spinnerCopies.getValue().toString()), false);
				lblStatusBar.setText(Integer.valueOf(spinnerCopies.getValue().toString()) + " labels printed. " + sample.getSampleDate().toString());
			}
		});

		btnPrint.setIcon(Common.icon_print_16x16);
		btnPrint.setBounds(165, 534, 117, 32);
		desktopPane.add(btnPrint);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		btnClose.setBounds(294, 534, 117, 32);
		desktopPane.add(btnClose);

		comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, selectedModuleId + getDefaultDPI(), "");
		comboBoxPrintQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedDPIRadioButton();
			}
		});
		comboBoxPrintQueue.setBounds(149, 468, 388, 22);
		desktopPane.add(comboBoxPrintQueue);
		setSelectedDPIRadioButton();

		JLabel4j_std lblNewLabel_4 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_4.setBounds(6, 468, 138, 22);
		desktopPane.add(lblNewLabel_4);

		JLabel4j_std lblNewLabel_5 = new JLabel4j_std(lang.get("lbl_Number_Of_Labels"));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_5.setBounds(6, 432, 138, 22);
		desktopPane.add(lblNewLabel_5);

		spinnerCopies = new JSpinner4j();
		spinnerCopies.setBounds(149, 432, 37, 22);
		JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(spinnerCopies);
		spinnerCopies.setEditor(ne);
		desktopPane.add(spinnerCopies);

		JButton4j btnProcessOrderLookup = new JButton4j();
		btnProcessOrderLookup.setIcon(Common.icon_lookup_16x16);
		btnProcessOrderLookup.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgCriteriaDefault = "Ready";
				JLaunchLookup.dlgAutoExec = true;
				if (JLaunchLookup.processOrders())
				{
					textFieldProcessOrder.setText(JLaunchLookup.dlgResult);
					processOrderChanged(JLaunchLookup.dlgResult);
				}
			}
		});
		btnProcessOrderLookup.setBounds(238, 10, 21, 22);
		desktopPane.add(btnProcessOrderLookup);

		String numberOfLabels = ctrl.getKeyValueWithDefault("QM SAMPLE LABELS", "4", "Number of Labels per Sample");
		spinnerCopies.setValue(Integer.valueOf(numberOfLabels));

		JLabel4j_std lblMaterial = new JLabel4j_std(lang.get("lbl_Material"));
		lblMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMaterial.setBounds(6, 100, 111, 22);
		desktopPane.add(lblMaterial);

		textFieldMaterial = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterial.setEditable(false);
		textFieldMaterial.setColumns(10);
		textFieldMaterial.setBounds(125, 100, 134, 22);
		desktopPane.add(textFieldMaterial);

		JLabel4j_std lblStatus = new JLabel4j_std(lang.get("lbl_Process_Order_Status"));
		lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStatus.setBounds(6, 130, 111, 22);
		desktopPane.add(lblStatus);

		textFieldStatus = new JTextField4j(JDBProcessOrder.field_status);
		textFieldStatus.setEditable(false);
		textFieldStatus.setColumns(10);
		textFieldStatus.setBounds(125, 130, 134, 22);
		desktopPane.add(textFieldStatus);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 248, 515, 173);
		desktopPane.add(scrollPane);

		listActivities = new JList4j<JDBQMActivity>();
		listActivities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listActivities.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent arg0)
			{
				printEnable();
			}
		});
		scrollPane.setViewportView(listActivities);

		JLabel4j_std lblResource = new JLabel4j_std(lang.get("lbl_Process_Order_Required_Resource"));
		lblResource.setHorizontalAlignment(SwingConstants.TRAILING);
		lblResource.setBounds(261, 70, 138, 22);
		desktopPane.add(lblResource);

		textFieldResource = new JTextField4j(JDBProcessOrder.field_required_resource);
		textFieldResource.setEditable(false);
		textFieldResource.setColumns(10);
		textFieldResource.setBounds(411, 70, 134, 22);
		desktopPane.add(textFieldResource);

		dueDate = new JDateControl();
		dueDate.setDisplayMode(JDateControl.mode_disable_visible);
		dueDate.setEnabled(false);
		dueDate.setBounds(411, 100, 134, 22);
		dueDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
		dueDate.getEditor().setSize(87, 21);
		desktopPane.add(dueDate);

		JLabel4j_std lblDueDate = new JLabel4j_std(lang.get("lbl_Process_Order_Due_Date"));
		lblDueDate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDueDate.setBounds(261, 100, 138, 22);
		desktopPane.add(lblDueDate);

		lblUserData1 = new JLabel4j_std(lang.get("lbl_User_Data1"));
		lblUserData1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData1.setBounds(6, 160, 111, 22);
		desktopPane.add(lblUserData1);

		lblUserData2 = new JLabel4j_std(lang.get("lbl_User_Data2"));
		lblUserData2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData2.setBounds(288, 160, 111, 22);
		desktopPane.add(lblUserData2);

		textFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
		textFieldUserData1.setToolTipText("Custom Field USER_DATA_1");
		textFieldUserData1.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});

		textFieldUserData1.setColumns(20);
		textFieldUserData1.setBounds(125, 160, 134, 22);
		textFieldUserData1.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_1_EDITABLE", "true", "QM USER DATA 1 EDITABLE")));
		desktopPane.add(textFieldUserData1);

		textFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
		textFieldUserData2.setToolTipText("Custom Field USER_DATA_2");
		textFieldUserData2.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});

		textFieldUserData2.setColumns(20);
		textFieldUserData2.setBounds(411, 160, 115, 22);
		textFieldUserData2.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_2_EDITABLE", "true", "QM USER DATA 2 EDITABLE")));
		desktopPane.add(textFieldUserData2);

		lblStatusBar = new JLabel4j_std();
		lblStatusBar.setForeground(Color.RED);
		lblStatusBar.setBackground(Color.GRAY);
		lblStatusBar.setBounds(0, 568, 575, 21);
		desktopPane.add(lblStatusBar);

		sampleDateTime = new JDateControl();
		sampleDateTime.setDisplayMode(JDateControl.mode_disable_visible);
		sampleDateTime.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{

				if (chckbxAutoTime.isSelected() == false)
				{
					Date selected = sampleDateTime.getDate();
					Date current = JUtility.getSQLDateTime();
					if (selected.after(current))
					{
						sampleDateTime.setDate(current);
					}
				}

			}
		});

		sampleDateTime.setEnabled(false);
		sampleDateTime.setBounds(417, 10, 128, 22);
		desktopPane.add(sampleDateTime);

		chckbxAutoTime = new JCheckBox4j("");
		chckbxAutoTime.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (chckbxAutoTime.isSelected())
				{
					sampleDateTime.setEnabled(true);
				}
				else
				{
					sampleDateTime.setEnabled(false);
				}
			}
		});
		chckbxAutoTime.setBounds(390, 10, 22, 22);
		desktopPane.add(chckbxAutoTime);

		textFieldProcessOrder.requestFocus();
		textFieldProcessOrder.setCaretPosition(textFieldProcessOrder.getText().length());
		resolution_200dpi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				refreshQueue();
			}
		});

		resolution_200dpi.setBounds(294, 502, 85, 22);
		desktopPane.add(resolution_200dpi);
		resolution_300dpi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				refreshQueue();
			}
		});

		resolution_300dpi.setBounds(411, 502, 85, 22);
		desktopPane.add(resolution_300dpi);
		resolution_default.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				refreshQueue();
			}
		});

		resolution_default.setSelected(true);
		resolution_default.setBounds(145, 502, 156, 22);
		desktopPane.add(resolution_default);

		ButtonGroup group = new ButtonGroup();
		group.add(resolution_200dpi);
		group.add(resolution_300dpi);
		group.add(resolution_default);

		JLabel4j_std lblUserData3 = new JLabel4j_std(lang.get("lbl_User_Data3"));
		lblUserData3.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData3.setBounds(6, 190, 111, 22);
		desktopPane.add(lblUserData3);

		textFieldUserData3 = new JTextField4j(20);
		textFieldUserData3.setEditable(false);
		textFieldUserData3.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});
		textFieldUserData3.setToolTipText("Custom Field USER_DATA_3");
		textFieldUserData3.setColumns(20);
		textFieldUserData3.setBounds(125, 190, 115, 22);
		textFieldUserData3.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_3_EDITABLE", "true", "QM USER DATA 3 EDITABLE")));
		desktopPane.add(textFieldUserData3);

		JLabel4j_std lblUserData4 = new JLabel4j_std(lang.get("lbl_User_Data4"));
		lblUserData4.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData4.setBounds(288, 190, 111, 22);
		desktopPane.add(lblUserData4);

		textFieldUserData4 = new JTextField4j(20);
		textFieldUserData4.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});
		textFieldUserData4.setToolTipText("Custom Field USER_DATA_4");
		textFieldUserData4.setColumns(20);
		textFieldUserData4.setBounds(411, 190, 134, 22);
		textFieldUserData4.setEditable(Boolean.valueOf(ctrl.getKeyValueWithDefault("QM_USER_DATA_4_EDITABLE", "true", "QM USER DATA 4 EDITABLE")));
		desktopPane.add(textFieldUserData4);

		{
			jButtonLookup_Shift_Names = new JButton4j(Common.icon_lookup_16x16);
			desktopPane.add(jButtonLookup_Shift_Names);
			jButtonLookup_Shift_Names.setBounds(238, 190, 21, 22);
			jButtonLookup_Shift_Names.setEnabled(true);
			jButtonLookup_Shift_Names.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";
					if (JLaunchLookup.shiftNames())
					{
						textFieldUserData3.setText(JLaunchLookup.dlgResult);
						textFieldUserData3.setCaretPosition(textFieldUserData3.getText().length());
						printEnable();

					}
				}
			});
		}

		{
			jButtonLookup_Packing_Line = new JButton4j(Common.icon_lookup_16x16);
			desktopPane.add(jButtonLookup_Packing_Line);
			jButtonLookup_Packing_Line.setBounds(524, 160, 21, 22);
			jButtonLookup_Packing_Line.setEnabled(true);
			jButtonLookup_Packing_Line.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgAutoExec = true;
					JLaunchLookup.dlgCriteriaDefault = "";
					if (JLaunchLookup.packingLine())
					{
						textFieldUserData2.setText(JLaunchLookup.dlgResult);
						textFieldUserData2.setCaretPosition(textFieldUserData2.getText().length());
						printEnable();

					}
				}
			});
		}

		processOrderChanged(processOrder);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldProcessOrder.requestFocus();
				textFieldProcessOrder.setCaretPosition(textFieldProcessOrder.getText().length());

			}
		});

		timer.start();

	}

}
