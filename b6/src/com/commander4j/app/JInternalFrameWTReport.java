package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTReport.java
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
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableRowSorter;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBQuery2;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.db.JDBWTViewWeightSample;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBViewWeightSamplModel;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The JInternalFrameWTReports is for reporting on weight checks
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTReports.jpg" >
 * 
 */
public class JInternalFrameWTReport extends JInternalFrame
{

	private static boolean dlg_sort_descending = false;
	private static final long serialVersionUID = 1;
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btn_SamplePoint_Lookup;
	private JCalendarButton button_CalendarSampleDateFrom;
	private JCalendarButton button_CalendarSampleDateTo;
	private JCheckBox4j checkBox4jFromEnabled = new JCheckBox4j();
	private JCheckBox4j checkBox4jToEnabled = new JCheckBox4j();
	private JTextField4j fld_Container_Code = new JTextField4j(JDBQMSample.field_data_2);
	private JTextField4j fld_Material = new JTextField4j(JDBMaterial.field_material);
	private JTextField fld_Process_Order = new JTextField(JDBProcessOrder.field_process_order);
	private JTextField4j fld_Product_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JButton4j jButtonClear;
	private JButton4j jButtonDetails;
	private JButton4j jButtonExcel;
	private JButton4j jButtonMaterialLookup;
	private JButton4j jButtonReports;
	private JCheckBox4j jCheckBoxLimit;
	private JComboBox4j<String> jComboBoxSortBy;
	private JDesktopPane jDesktopPane1;
	private JSpinner jSpinnerLimit = new JSpinner();
	private JLabel4j_std jStatusText;
	private JToggleButton jToggleButtonSequence = new JToggleButton();
	private JDBLanguage lang;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private PreparedStatement listStatement;
	private JDateControl sampleDateFrom = new JDateControl();
	private JDateControl sampleDateTo = new JDateControl();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable4j tableResults;
	private JCheckBox4j checkBox4j_T1 = new JCheckBox4j();
	private JCheckBox4j checkBox4j_T2 = new JCheckBox4j();
	private LinkedList<String> selectedSamplePoints = new LinkedList<String>();

	public JInternalFrameWTReport()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(btn_Help, JUtility.getHelpSetIDforModule("FRM_WEIGHT_REPORTS"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				fld_SamplePoint.setToolTipText("None");
				fld_SamplePoint.setEditable(false);
				fld_SamplePoint.setEnabled(false);
				fld_SamplePoint.requestFocus();
				fld_SamplePoint.setCaretPosition(fld_SamplePoint.getText().length());

			}
		});

	}

	private void buildSQL(String type)
	{

		JDBQuery2.closeStatement(listStatement);

		listStatement = buildSQLr(type);
	}
	
	private PreparedStatement buildSQLr(String type)
	{

		PreparedStatement result;
		JDBQuery2 q2 = new JDBQuery2(Common.selectedHostID, Common.sessionID);
		
		q2.applyWhat("*");

		if (type.equals("report"))
		{
			
			q2.applyFrom("{schema}view_weight_samples3");
		}
		else
		{
			q2.applyFrom("{schema}view_weight_samples4");
		}

		
		if (checkBox4jFromEnabled.isSelected())
		{
			q2.applyWhere("sample_date>=", JUtility.getTimestampFromDate(sampleDateFrom.getDate()));
		}

		if (checkBox4jToEnabled.isSelected())
		{
			q2.applyWhere("sample_date<=", JUtility.getTimestampFromDate(sampleDateTo.getDate()));
		}

		if (fld_Material.getText().equals("") == false)
		{
			q2.applyWhere("material = ", fld_Material.getText());
		}

		if (fld_Process_Order.getText().equals("") == false)
		{
			q2.applyWhere("process_order = ", fld_Process_Order.getText());
		}

		if (fld_SamplePoint.getText().equals("") == false)
		{
			q2.applyIn("sample_point in ", selectedSamplePoints);
		}

		if (fld_Product_Group.getText().equals("") == false)
		{
			q2.applyWhere("product_group = ", fld_Product_Group.getText());
		}
		
		if (fld_Container_Code.getText().equals("") == false)
		{
			q2.applyWhere("container_code = ", fld_Container_Code.getText());
		}

		if (checkBox4j_T1.isSelected())
		{
			q2.applyWhere("sample_t1_count > ", 0);
		}

		if (checkBox4j_T2.isSelected())
		{
			q2.applyWhere("sample_t2_count > ", 0);
		}
		
		if (type.equals("report"))
		{
			
			q2.applySort("NOMINAL_WEIGHT,MATERIAL,SAMPLE_DATE,SAMPLE_POINT,SAMPLE_SEQUENCE",false);
		}
		else
		{
			q2.applySort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		}
		
		q2.applyRestriction(jCheckBoxLimit.isSelected(), jSpinnerLimit.getValue());
		q2.applySQL();
		result = q2.getPreparedStatement();

		return result;
	}

	private void clearFilter()
	{

		jComboBoxSortBy.setSelectedIndex(0);
		jToggleButtonSequence.setSelected(false);
		fld_Process_Order.setText("");
		fld_Material.setText("");
		fld_Product_Group.setText("");
		fld_Container_Code.setText("");
		checkBox4jFromEnabled.setSelected(false);
		checkBox4jToEnabled.setSelected(false);
		sampleDateFrom.setEnabled(false);
		button_CalendarSampleDateFrom.setEnabled(false);
		sampleDateTo.setEnabled(false);
		button_CalendarSampleDateTo.setEnabled(false);
		checkBox4j_T1.setSelected(false);
		checkBox4j_T2.setSelected(false);
		selectedSamplePoints.clear();
		fld_SamplePoint.setText("");
		fld_SamplePoint.setToolTipText("None");

		search();
	}

	private void excel()
	{
		JDBWTViewWeightSample sampleHeader = new JDBWTViewWeightSample(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		
		export.setExcelRowLimit(jCheckBoxLimit, jSpinnerLimit);
		
		buildSQL("report");
		
		export.saveAs("weight_checks.xls", sampleHeader.getViewWeightSampleDataResultSet(listStatement), Common.mainForm);
		
	
		buildSQL("search");

		populateList();
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1036, 671);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Weight Check Reports");
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
					btn_Help.setBounds(718, 150, 140, 32);
				}
				{
					btn_Close = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(btn_Close);
					btn_Close.setText(lang.get("btn_Close"));
					btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
					btn_Close.setBounds(861, 150, 140, 32);
					btn_Close.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					lbl_Process_Order = new JLabel4j_std();
					jDesktopPane1.add(lbl_Process_Order);
					lbl_Process_Order.setText(lang.get("lbl_Process_Order"));
					lbl_Process_Order.setBounds(0, 63, 120, 25);
					lbl_Process_Order.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					fld_Process_Order.setFont(Common.font_input);

					jDesktopPane1.add(fld_Process_Order);
					fld_Process_Order.setBounds(128, 63, 107, 25);
				}
				{
					lbl_Material = new JLabel4j_std();
					jDesktopPane1.add(lbl_Material);
					lbl_Material.setText(lang.get("lbl_Material"));
					lbl_Material.setBounds(279, 63, 94, 25);
					lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{

					jDesktopPane1.add(fld_Material);
					fld_Material.setBounds(379, 63, 93, 25);
				}

				{
					btn_Process_Order_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_Process_Order_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{

							JLaunchLookup.dlgCriteriaDefault = "Ready";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.processOrders())
							{
								fld_Process_Order.setText(JLaunchLookup.dlgResult);
							}
						}
					});

					btn_Process_Order_Lookup.setBounds(236, 63, 21, 25);
					jDesktopPane1.add(btn_Process_Order_Lookup);
				}

				{
					btn_SamplePoint_Lookup = new JButton4j(Common.icon_lookup_16x16);
					btn_SamplePoint_Lookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JDialogSamplePointSelect sps = new JDialogSamplePointSelect(selectedSamplePoints);
							sps.setVisible(true);
							selectedSamplePoints = sps.getSelected();
							
							fld_SamplePoint.setText(String.valueOf(selectedSamplePoints.size())+" selected.");
							
							if (selectedSamplePoints.size()>0)
							{

								String tooltiptext = "<html>";
								
								for (int x=0;x<selectedSamplePoints.size();x++)
								{
									tooltiptext = tooltiptext + "<strong>" + selectedSamplePoints.get(x)+"</strong><br>";
								}
								tooltiptext = tooltiptext + "</html>";
								
								fld_SamplePoint.setToolTipText(tooltiptext);
							}
							else
							{
								fld_SamplePoint.setToolTipText("None");
							}

						}
					});
					btn_SamplePoint_Lookup.setBounds(236, 14, 21, 25);
					jDesktopPane1.add(btn_SamplePoint_Lookup);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setFont(new Font("Arial", Font.PLAIN, 11));
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 614, 1006, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				JLabel4j_std lbl_SamplePoint = new JLabel4j_std();
				lbl_SamplePoint.setText(lang.get("lbl_SamplePoint"));
				lbl_SamplePoint.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_SamplePoint.setBounds(0, 14, 122, 25);
				jDesktopPane1.add(lbl_SamplePoint);
				fld_SamplePoint.setBounds(128, 14, 107, 25);
				jDesktopPane1.add(fld_SamplePoint);

				JLabel4j_std lbl_Product_Group = new JLabel4j_std();
				lbl_Product_Group.setText(lang.get("lbl_Product_Group"));
				lbl_Product_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Product_Group.setBounds(748, 14, 107, 25);
				jDesktopPane1.add(lbl_Product_Group);

				fld_Product_Group.setBounds(861, 14, 120, 25);
				jDesktopPane1.add(fld_Product_Group);

				JLabel4j_std lbl_Container_Code = new JLabel4j_std();
				lbl_Container_Code.setText(lang.get("lbl_Container_Code"));
				lbl_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Container_Code.setBounds(748, 63, 107, 25);
				jDesktopPane1.add(lbl_Container_Code);

				fld_Container_Code.setBounds(861, 63, 120, 25);
				jDesktopPane1.add(fld_Container_Code);

				JLabel4j_std lbl_Sample_Date = new JLabel4j_std();
				lbl_Sample_Date.setText(lang.get("lbl_Sample_Date"));
				lbl_Sample_Date.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Sample_Date.setBounds(261, 14, 120, 25);
				jDesktopPane1.add(lbl_Sample_Date);
				checkBox4jFromEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						if (checkBox4jFromEnabled.isSelected())
						{
							sampleDateFrom.setEnabled(true);
							button_CalendarSampleDateFrom.setEnabled(true);
						}
						else
						{
							sampleDateFrom.setEnabled(false);
							button_CalendarSampleDateFrom.setEnabled(false);
						}
					}
				});

				checkBox4jFromEnabled.setBackground(Color.WHITE);
				checkBox4jFromEnabled.setBounds(391, 14, 21, 25);
				jDesktopPane1.add(checkBox4jFromEnabled);

				sampleDateFrom.setEnabled(false);
				sampleDateFrom.setBounds(415, 14, 128, 25);
				jDesktopPane1.add(sampleDateFrom);
				checkBox4jToEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (checkBox4jToEnabled.isSelected())
						{
							sampleDateTo.setEnabled(true);
							button_CalendarSampleDateTo.setEnabled(true);
						}
						else
						{
							sampleDateTo.setEnabled(false);
							button_CalendarSampleDateTo.setEnabled(false);
						}
					}
				});

				checkBox4jToEnabled.setBackground(Color.WHITE);
				checkBox4jToEnabled.setBounds(576, 14, 21, 25);
				jDesktopPane1.add(checkBox4jToEnabled);

				sampleDateTo.setEnabled(false);
				sampleDateTo.setBounds(603, 14, 128, 25);
				jDesktopPane1.add(sampleDateTo);

				scrollPane.setBounds(0, 194, 1014, 408);
				jDesktopPane1.add(scrollPane);

				tableResults = new JTable4j();
				tableResults.setToolTipText("Double click on row to see individual samples");
				tableResults.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{

						if (tableResults.getSelectedRowCount() == 1)
						{
							if (e.getClickCount() == 2)
							{
								if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_REPORTS_DETAILS") == true)
								{
									sampleDetail();
								}
							}
						}
					}
				});
				scrollPane.setViewportView(tableResults);

				JButton4j btn_Search = new JButton4j(Common.icon_search_16x16);
				btn_Search.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						search();
					}
				});

				btn_Search.setText(lang.get("btn_Search"));
				btn_Search.setMnemonic(KeyEvent.VK_S);
				btn_Search.setBounds(3, 150, 140, 32);
				jDesktopPane1.add(btn_Search);

				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Sort_By"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(188, 118, 69, 21);
				jDesktopPane1.add(label4j_std);

				ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
				{"NOMINAL_WEIGHT,SAMPLE_DATE,SAMPLE_POINT",
				 "NOMINAL_WEIGHT,MATERIAL,SAMPLE_DATE,SAMPLE_POINT",	
				 "SAMPLE_DATE,SAMPLE_POINT,NOMINAL_WEIGHT", 
				 "SAMPLE_POINT,PROCESS_ORDER,SAMPLE_DATE", 
				 "SAMPLE_POINT,SAMPLE_DATE",
				 "SAMPLE_POINT,MATERIAL", 
				 "SAMPLE_POINT,PRODUCT_GROUP,SAMPLE_DATE" 
				 });
				
				jComboBoxSortBy = new JComboBox4j<String>();
				jComboBoxSortBy.setMaximumRowCount(15);
				jComboBoxSortBy.setModel(jComboBoxSortByModel);
				jComboBoxSortBy.setBounds(279, 117, 391, 22);
				jDesktopPane1.add(jComboBoxSortBy);
				jToggleButtonSequence.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setSequence(jToggleButtonSequence.isSelected());
					}
				});

				jCheckBoxLimit = new JCheckBox4j();
				jCheckBoxLimit.setSelected(true);
				jCheckBoxLimit.setBackground(Color.WHITE);
				jCheckBoxLimit.setBounds(883, 117, 21, 21);
				jDesktopPane1.add(jCheckBoxLimit);

				jToggleButtonSequence.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setSequence(jToggleButtonSequence.isSelected());
					}
				});

				jToggleButtonSequence.setSelected(false);
				jToggleButtonSequence.setBounds(670, 117, 21, 22);
				jDesktopPane1.add(jToggleButtonSequence);

				JLabel4j_std label4j_std_1 = new JLabel4j_std();
				label4j_std_1.setText(lang.get("lbl_Limit"));
				label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std_1.setBounds(807, 117, 74, 21);
				jDesktopPane1.add(label4j_std_1);


				{
					SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
					jSpinnerIntModel.setMinimum(1);
					jSpinnerIntModel.setMaximum(25000);
					jSpinnerIntModel.setStepSize(1);
					JSpinner.NumberEditor ne = new JSpinner.NumberEditor(jSpinnerLimit);
					ne.getTextField().setFont(Common.font_std);
					jSpinnerLimit.setEditor(ne);
					jSpinnerLimit.setModel(jSpinnerIntModel);
					jSpinnerLimit.setBounds(906, 116, 68, 21);
					jSpinnerLimit.setValue(2000);
					jSpinnerLimit.getEditor().setSize(45, 21);
					jDesktopPane1.add(jSpinnerLimit);
				}

				button_CalendarSampleDateFrom = new JCalendarButton(sampleDateFrom);
				button_CalendarSampleDateFrom.setEnabled(false);
				button_CalendarSampleDateFrom.setBounds(542, 18, 21, 21);
				jDesktopPane1.add(button_CalendarSampleDateFrom);

				button_CalendarSampleDateTo = new JCalendarButton(sampleDateTo);
				button_CalendarSampleDateTo.setEnabled(false);
				button_CalendarSampleDateTo.setBounds(732, 18, 21, 21);
				jDesktopPane1.add(button_CalendarSampleDateTo);

				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();
						}
					});
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jButtonClear.setBounds(146, 150, 140, 32);
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonDetails = new JButton4j(Common.icon_details_16x16);
					jButtonDetails.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_REPORTS_DETAILS"));
					jButtonDetails.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							sampleDetail();
						}
					});
					jButtonDetails.setText(lang.get("btn_Details"));
					jButtonDetails.setBounds(432, 150, 140, 32);
					jDesktopPane1.add(jButtonDetails);
				}

				{
					jButtonReports = new JButton4j(Common.icon_report_16x16);
					jButtonReports.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							print();
						}
					});
					jButtonReports.setText(lang.get("btn_Print"));
					jButtonReports.setBounds(289, 150, 140, 32);
					jDesktopPane1.add(jButtonReports);
				}

				JButton4j btn_LookupProductGroup = new JButton4j(Common.icon_lookup_16x16);
				btn_LookupProductGroup.setBounds(980, 14, 21, 25);
				jDesktopPane1.add(btn_LookupProductGroup);
				btn_LookupProductGroup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.wtProductGroups())
						{

							fld_Product_Group.setText(JLaunchLookup.dlgResult);
						}
					}
				});

				JButton4j btn_LookupContainerCode = new JButton4j(Common.icon_lookup_16x16);
				btn_LookupContainerCode.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "";
						if (JLaunchLookup.wtContainerCode())
						{

							fld_Container_Code.setText(JLaunchLookup.dlgResult);

						}
					}
				});
				btn_LookupContainerCode.setBounds(980, 63, 21, 25);
				jDesktopPane1.add(btn_LookupContainerCode);

				{
					jButtonMaterialLookup = new JButton4j(Common.icon_lookup_16x16);
					jButtonMaterialLookup.setBounds(471, 63, 21, 25);
					jDesktopPane1.add(jButtonMaterialLookup);
					jButtonMaterialLookup.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								fld_Material.setText(JLaunchLookup.dlgResult);
							}
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setBounds(575, 150, 140, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});
					jButtonExcel.setMnemonic(KeyEvent.VK_H);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jDesktopPane1.add(jButtonExcel);
				}
				checkBox4j_T1.setBackground(Color.WHITE);
				checkBox4j_T1.setBounds(610, 63, 21, 25);
				jDesktopPane1.add(checkBox4j_T1);
				checkBox4j_T2.setBackground(Color.WHITE);
				checkBox4j_T2.setBounds(670, 63, 21, 25);
				jDesktopPane1.add(checkBox4j_T2);

				JLabel4j_std label4j_T1 = new JLabel4j_std();
				label4j_T1.setText("T2");
				label4j_T1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_T1.setBounds(645, 63, 21, 25);
				jDesktopPane1.add(label4j_T1);

				JLabel4j_std label4j_T2 = new JLabel4j_std();
				label4j_T2.setText("T1");
				label4j_T2.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_T2.setBounds(553, 63, 52, 25);
				jDesktopPane1.add(label4j_T2);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void print()
	{		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
			
		PreparedStatement temp = buildSQLr("report");
		
		JLaunchReport.runReport("RPT_WT_SD_MEAN", parameters, "", temp, "");
	}

	private void populateList()
	{
		JDBWTViewWeightSample sampleHeader = new JDBWTViewWeightSample(Common.selectedHostID, Common.sessionID);

		JDBViewWeightSamplModel sampleTable = new JDBViewWeightSamplModel(sampleHeader.getViewWeightSampleDataResultSet(listStatement));

		TableRowSorter<JDBViewWeightSamplModel> sorter = new TableRowSorter<JDBViewWeightSamplModel>(sampleTable);

		tableResults.setRowSorter(sorter);

		this.setIconifiable(true);
		tableResults.setModel(sampleTable);

		scrollPane.setViewportView(tableResults);
		JUtility.scrolltoHomePosition(scrollPane);

		tableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SampleDate_Col).setPreferredWidth(145);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SamplePoint_Col).setPreferredWidth(100);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.Material_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.ProcessOrder_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.ProductGroup_Col).setPreferredWidth(140);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.ContainerCode_Col).setPreferredWidth(65);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.ProcessOrder_Col).setPreferredWidth(80);

		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.NominalWeight_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.NominalWeightUom_Col).setPreferredWidth(40);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SampleMean_Col).setPreferredWidth(85);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SampleStdDev_Col).setPreferredWidth(80);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SampleTotalT1s_Col).setPreferredWidth(45);
		tableResults.getColumnModel().getColumn(JDBViewWeightSamplModel.SampleTotalT2s_Col).setPreferredWidth(45);

		scrollPane.repaint();

		JUtility.setResultRecordCountColour(jStatusText, jCheckBoxLimit.isSelected(), Integer.valueOf(jSpinnerLimit.getValue().toString()), sampleTable.getRowCount());

	}

	private void sampleDetail()
	{
		int row = tableResults.getSelectedRow();
		if (row >= 0)
		{
			String samplePoint = tableResults.getValueAt(row, JDBViewWeightSamplModel.SamplePoint_Col).toString();
			String sampleDate = tableResults.getValueAt(row, JDBViewWeightSamplModel.SampleDate_Col).toString();
			JLaunchMenu.runDialog("FRM_WEIGHT_REPORTS_DETAILS", samplePoint, sampleDate);
		}

	}

	private void search()
	{
		buildSQL("search");
		populateList();
	}

	private void setSequence(boolean descending)
	{
		if (jToggleButtonSequence.isSelected() == true)
		{
			// rbdescending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		}
		else
		{
			// rbascending.setSelected(true);
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}
}
