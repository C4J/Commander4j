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
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUserReport;
import com.commander4j.db.JUserReportParameter;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;
import com.commander4j.util.UppercaseDocumentFilter;

/**
 * The JInternalFrameQMSampleLabel is used printing sample labels and populating the APP_QM_SAMPLE table
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
	private JComboBox4j<String> comboBoxPrintQueue = new JComboBox4j<String>();
	private JSpinner spinnerCopies;
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
	private Boolean processOrderValid = false;
	private JDateControl sampleDateTime;
	private JCheckBox4j chckbxAutoTime;
	private JRadioButton4j resolution_200dpi = new JRadioButton4j("200 dpi");
	private JRadioButton4j resolution_300dpi = new JRadioButton4j("300 dpi");
	private JRadioButton4j resolution_default = new JRadioButton4j("Default Resolution");
	private JTextField4j textFieldUserData3;
	private JTextField4j textFieldUserData4;

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
				} else
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
				} else
				{
					ud2 = true;
				}
				if (ud1 & ud2)
				{
					result = true;
				}
			}
		}
		btnPrint.setEnabled(result);
	}

	private void processOrderChanged(String processOrder)
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
			processOrderValid = true;
			
			if (userReport.getUserReportProperties("USER_DATA_1"))
			{
				JUserReportParameter param1 = new JUserReportParameter();
				param1.parameterPosition=1;
				param1.parameterType="String";
				param1.parameterStringValue=po.getProcessOrder();
				LinkedList<JUserReportParameter> paramList = new LinkedList<JUserReportParameter>();
				paramList.add(param1);
				userReport.setSYSTEMparameters(paramList);
				if (userReport.runReport())
				{
					textFieldUserData3.setText(userReport.getSystemResultData());
				}
			}
			
			populateActivityList(po.getInspectionID());
		} else
		{
			textFieldDescription.setText("");
			textFieldInspectionID.setText("");
			textFieldMaterial.setText("");
			textFieldStatus.setText("");
			textFieldResource.setText("");
			textFieldUserData1.setText("");
			textFieldUserData2.setText("");
			dueDate.setDate(JUtility.getSQLDate());
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
			
			setDefaultDPI(defaultitem);
		}
	}
	
	private void setDefaultDPI(String defaultitem)
	{
		boolean Default = true;
		
		if (defaultitem.contains("203"))
		{
			resolution_200dpi.setSelected(true);
			Default = false;
		}
		if (defaultitem.contains("200"))
		{
			resolution_200dpi.setSelected(true);
			Default = false;
		}
		if (defaultitem.contains("300"))
		{
			resolution_300dpi.setSelected(true);	
			Default = false;
		}
		if (Default)
		{
			resolution_default.setSelected(true);
		}
	}
	
	private String getDefaultDPI()
	{
		String result = "";
		
		if (resolution_300dpi.isSelected())
		{
			result = "300";
			
		}
		if (resolution_200dpi.isSelected())
		{
			result = "200";
			
		}
		if (resolution_default.isSelected())
		{
			result = "";
			
		}
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

	public JInternalFrameQMSampleLabel(String processOrder)
	{
		super();

		processOrderChanged(processOrder);

	}

	/**
	 * Create the frame.
	 */
	public JInternalFrameQMSampleLabel()
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

		setVisible(true);
		this.setClosable(true);
		this.setIconifiable(true);
		setBounds(100, 100, 598, 643);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_app_window);
		desktopPane.setBounds(0, 0, 587, 631);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		JLabel4j_std lblProcessOrder = new JLabel4j_std(lang.get("lbl_Process_Order"));
		lblProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
		lblProcessOrder.setBounds(6, 12, 111, 16);
		desktopPane.add(lblProcessOrder);

		JLabel4j_std lblDescription = new JLabel4j_std(lang.get("lbl_Description"));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(6, 43, 111, 16);
		desktopPane.add(lblDescription);

		textFieldProcessOrder = new JTextField4j(JDBProcessOrder.field_process_order);
		textFieldProcessOrder.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				processOrderChanged(textFieldProcessOrder.getText());
			}
		});
		textFieldProcessOrder.setBounds(125, 10, 138, 22);
		desktopPane.add(textFieldProcessOrder);
		textFieldProcessOrder.setColumns(10);

		textFieldDescription = new JTextField4j(JDBProcessOrder.field_description);
		textFieldDescription.setEditable(false);
		textFieldDescription.setBounds(125, 38, 420, 22);
		desktopPane.add(textFieldDescription);
		textFieldDescription.setColumns(10);

		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setEditable(false);
		textFieldInspectionID.setBounds(125, 69, 134, 22);
		desktopPane.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);

		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblInspectionID.setBounds(6, 74, 111, 16);
		desktopPane.add(lblInspectionID);

		JLabel4j_std lblNewLabel_3 = new JLabel4j_std(lang.get("lbl_Activity_ID"));
		lblNewLabel_3.setBounds(30, 230, 111, 16);
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
				sample.create(sampleID, po.getInspectionID(), activityID, po.getProcessOrder(), po.getMaterial(), textFieldUserData1.getText(), textFieldUserData2.getText(), ts);
				String pq = comboBoxPrintQueue.getSelectedItem().toString();
				buildSQL1Record(sampleID);
				String dpi = getDefaultDPI();
				if (dpi.equals("")==false)
				{
					dpi = "_" + dpi;
				}
				JLaunchReport.runReport("RPT_SAMPLE_LABEL"+dpi, listStatement, false, pq, Integer.valueOf(spinnerCopies.getValue().toString()), false);
				lblStatusBar.setText(Integer.valueOf(spinnerCopies.getValue().toString()) + " labels printed. " + sample.getSampleDate().toString());
			}
		});

		btnPrint.setIcon(Common.icon_print);
		btnPrint.setBounds(185, 535, 117, 32);
		desktopPane.add(btnPrint);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.setIcon(Common.icon_close);
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		btnClose.setBounds(314, 535, 117, 32);
		desktopPane.add(btnClose);

		comboBoxPrintQueue.setBounds(145, 468, 388, 27);
		desktopPane.add(comboBoxPrintQueue);

		JLabel4j_std lblNewLabel_4 = new JLabel4j_std(lang.get("lbl_Print_Queue"));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_4.setBounds(6, 472, 138, 16);
		desktopPane.add(lblNewLabel_4);

		JLabel4j_std lblNewLabel_5 = new JLabel4j_std(lang.get("lbl_Number_Of_Labels"));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_5.setBounds(6, 442, 138, 16);
		desktopPane.add(lblNewLabel_5);

		spinnerCopies = new JSpinner();
		spinnerCopies.setBounds(149, 432, 37, 28);
		JSpinner.NumberEditor ne = new JSpinner.NumberEditor(spinnerCopies);
		ne.getTextField().setFont(Common.font_std);
		spinnerCopies.setEditor(ne);
		desktopPane.add(spinnerCopies);

		JButton btnProcessOrderLookup = new JButton();
		btnProcessOrderLookup.setIcon(Common.icon_lookup);
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
		btnProcessOrderLookup.setBounds(261, 10, 21, 22);
		desktopPane.add(btnProcessOrderLookup);

		populatePrinterList(JPrint.getDefaultPrinterQueueName());
		String numberOfLabels = ctrl.getKeyValueWithDefault("QM SAMPLE LABELS", "4", "Number of Labels per Sample");
		spinnerCopies.setValue(Integer.valueOf(numberOfLabels));

		JLabel4j_std lblMaterial = new JLabel4j_std(lang.get("lbl_Material"));
		lblMaterial.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMaterial.setBounds(6, 105, 111, 16);
		desktopPane.add(lblMaterial);

		textFieldMaterial = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterial.setEditable(false);
		textFieldMaterial.setColumns(10);
		textFieldMaterial.setBounds(125, 100, 134, 22);
		desktopPane.add(textFieldMaterial);

		JLabel4j_std lblStatus = new JLabel4j_std(lang.get("lbl_Process_Order_Status"));
		lblStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStatus.setBounds(6, 136, 111, 16);
		desktopPane.add(lblStatus);

		textFieldStatus = new JTextField4j(JDBProcessOrder.field_status);
		textFieldStatus.setEditable(false);
		textFieldStatus.setColumns(10);
		textFieldStatus.setBounds(125, 131, 134, 22);
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
		lblResource.setBounds(261, 74, 138, 16);
		desktopPane.add(lblResource);

		textFieldResource = new JTextField4j(JDBProcessOrder.field_required_resource);
		textFieldResource.setEditable(false);
		textFieldResource.setColumns(10);
		textFieldResource.setBounds(411, 69, 134, 22);
		desktopPane.add(textFieldResource);

		dueDate = new JDateControl();
		dueDate.setEnabled(false);
		dueDate.setBounds(411, 103, 134, 28);
		dueDate.getEditor().setPreferredSize(new java.awt.Dimension(87, 19));
		dueDate.getEditor().setSize(87, 21);
		desktopPane.add(dueDate);

		JLabel4j_std lblDueDate = new JLabel4j_std(lang.get("lbl_Process_Order_Due_Date"));
		lblDueDate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDueDate.setBounds(261, 105, 138, 16);
		desktopPane.add(lblDueDate);

		lblUserData1 = new JLabel4j_std(lang.get("lbl_User_Data1"));
		lblUserData1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData1.setBounds(6, 167, 111, 16);
		desktopPane.add(lblUserData1);

		lblUserData2 = new JLabel4j_std(lang.get("lbl_User_Data2"));
		lblUserData2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData2.setBounds(288, 167, 111, 16);
		desktopPane.add(lblUserData2);

		DocumentFilter filter = new UppercaseDocumentFilter();
		textFieldUserData1 = new JTextField4j(JDBQMSample.field_data_1);
		textFieldUserData1.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});

		((AbstractDocument) textFieldUserData1.getDocument()).setDocumentFilter(filter);
		textFieldUserData1.setColumns(20);
		textFieldUserData1.setBounds(125, 162, 134, 22);
		desktopPane.add(textFieldUserData1);

		textFieldUserData2 = new JTextField4j(JDBQMSample.field_data_2);
		textFieldUserData2.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				printEnable();
			}
		});

		((AbstractDocument) textFieldUserData2.getDocument()).setDocumentFilter(filter);
		textFieldUserData2.setColumns(20);
		textFieldUserData2.setBounds(411, 162, 134, 22);
		desktopPane.add(textFieldUserData2);

		lblStatusBar = new JLabel4j_std();
		lblStatusBar.setForeground(Color.RED);
		lblStatusBar.setBackground(Color.GRAY);
		lblStatusBar.setBounds(0, 568, 575, 21);
		desktopPane.add(lblStatusBar);

		sampleDateTime = new JDateControl();
		sampleDateTime.setEnabled(false);
		sampleDateTime.setBounds(417, 8, 128, 25);
		desktopPane.add(sampleDateTime);

		chckbxAutoTime = new JCheckBox4j("");
		chckbxAutoTime.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (chckbxAutoTime.isSelected())
				{
					sampleDateTime.setEnabled(true);
				} else
				{
					sampleDateTime.setEnabled(false);
				}
			}
		});
		chckbxAutoTime.setBounds(385, 9, 28, 23);
		desktopPane.add(chckbxAutoTime);

		textFieldProcessOrder.requestFocus();
		textFieldProcessOrder.setCaretPosition(textFieldProcessOrder.getText().length());


		resolution_200dpi.setBounds(313, 500, 85, 23);
		desktopPane.add(resolution_200dpi);


		resolution_300dpi.setBounds(411, 500, 85, 23);
		desktopPane.add(resolution_300dpi);

		resolution_default.setSelected(true);
		resolution_default.setBounds(145, 500, 156, 23);
		desktopPane.add(resolution_default);

		ButtonGroup group = new ButtonGroup( );
		group.add(resolution_200dpi);
		group.add(resolution_300dpi);
		group.add(resolution_default);
		
		JLabel4j_std lblUserData3 = new JLabel4j_std(lang.get("lbl_User_Data3"));
		lblUserData3.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData3.setBounds(6, 198, 111, 16);
		desktopPane.add(lblUserData3);
		
		textFieldUserData3 = new JTextField4j(20);
		textFieldUserData3.setColumns(20);
		textFieldUserData3.setBounds(125, 193, 134, 22);
		desktopPane.add(textFieldUserData3);
		
		JLabel4j_std lblUserData4 = new JLabel4j_std(lang.get("lbl_User_Data4"));
		lblUserData4.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUserData4.setBounds(288, 198, 111, 16);
		desktopPane.add(lblUserData4);
		
		textFieldUserData4 = new JTextField4j(20);
		textFieldUserData4.setColumns(20);
		textFieldUserData4.setBounds(411, 193, 134, 22);
		desktopPane.add(textFieldUserData4);
		
		populatePrinterList(JPrint.getDefaultPrinterQueueName());
		
		comboBoxPrintQueue.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String pq = comboBoxPrintQueue.getSelectedItem().toString();
				setDefaultDPI(pq);
			}
		});


		timer.start();

	}
}
