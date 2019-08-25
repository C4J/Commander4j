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
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBWTProductGroups;
import com.commander4j.db.JDBWTWorkstation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.util.JDateControl;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.KeyEvent;

/**
 * The JInternalFrameWTReports is for reporting on weight checks
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTReports.jpg" >
 * 
 */
public class JInternalFrameWTReports extends JInternalFrame
{

	private static final long serialVersionUID = 1;
	private JButton4j btn_Close;
	private JButton4j btn_Help;
	private JButton4j btn_Process_Order_Lookup;
	private JButton4j btn_Material_Lookup;
	private JButton4j btn_SamplePoint_Lookup;

	private JTextField4j fld_Container_Code = new JTextField4j(JDBQMSample.field_data_2);

	private JTextField4j fld_Material;
	private JTextField4j fld_Material_Group = new JTextField4j(JDBWTProductGroups.field_product_group);
	private JTextField fld_Process_Order = new JTextField(JDBProcessOrder.field_process_order);
	private JTextField4j fld_SamplePoint = new JTextField4j(JDBWTWorkstation.field_SamplePoint);
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jStatusText;
	private JDBLanguage lang;
	private JLabel4j_std lbl_Material;
	private JLabel4j_std lbl_Process_Order;
	private PreparedStatement listStatement;
	private JTable tableResults;


	public JInternalFrameWTReports()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

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
				

				

			}
		});

	}



	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 1016, 671);
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
					btn_Help.setBounds(362, 570, 146, 32);
				}
				{
					btn_Close = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(btn_Close);
					btn_Close.setText(lang.get("btn_Close"));
					btn_Close.setMnemonic(java.awt.event.KeyEvent.VK_C);
					btn_Close.setBounds(520, 570, 146, 32);
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
					fld_Process_Order.setBounds(154, 63, 107, 25);
				}
				{
					lbl_Material = new JLabel4j_std();
					jDesktopPane1.add(lbl_Material);
					lbl_Material.setText(lang.get("lbl_Material"));
					lbl_Material.setBounds(279, 63, 94, 25);
					lbl_Material.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					fld_Material = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(fld_Material);
					fld_Material.setBounds(379, 63, 93, 25);
				}

				{
					btn_Process_Order_Lookup = new JButton4j(Common.icon_lookup_16x16);

					btn_Process_Order_Lookup.setBounds(261, 63, 21, 25);
					jDesktopPane1.add(btn_Process_Order_Lookup);
				}
				
				{
					btn_Material_Lookup = new JButton4j(Common.icon_lookup_16x16);

					btn_Material_Lookup.setBounds(261, 63, 21, 25);
					jDesktopPane1.add(btn_Material_Lookup);
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

								fld_Process_Order.setText("");

							}
						}
					});
					btn_SamplePoint_Lookup.setBounds(737, 23, 21, 25);
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
				lbl_SamplePoint.setBounds(524, 23, 108, 25);
				jDesktopPane1.add(lbl_SamplePoint);
				fld_SamplePoint.setBounds(644, 23, 93, 25);
				jDesktopPane1.add(fld_SamplePoint);

				JLabel4j_std lbl_Material_Group = new JLabel4j_std();
				lbl_Material_Group.setText(lang.get("lbl_Material_Group"));
				lbl_Material_Group.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Material_Group.setBounds(540, 63, 96, 25);
				jDesktopPane1.add(lbl_Material_Group);
				fld_Material_Group.setEditable(false);

				fld_Material_Group.setBounds(644, 63, 120, 25);
				jDesktopPane1.add(fld_Material_Group);

				JLabel4j_std lbl_Container_Code = new JLabel4j_std();
				lbl_Container_Code.setText(lang.get("lbl_Container_Code"));
				lbl_Container_Code.setHorizontalAlignment(SwingConstants.TRAILING);
				lbl_Container_Code.setBounds(770, 63, 108, 25);
				jDesktopPane1.add(lbl_Container_Code);
				fld_Container_Code.setEditable(false);

				fld_Container_Code.setBounds(892, 63, 93, 25);
				jDesktopPane1.add(fld_Container_Code);
				
				JLabel4j_std label4j_SampleDate = new JLabel4j_std();
				label4j_SampleDate.setText("lbl_Pallet_DOM");
				label4j_SampleDate.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_SampleDate.setBounds(0, 25, 120, 25);
				jDesktopPane1.add(label4j_SampleDate);
				
				JCheckBox4j checkBox4jFromEnabled = new JCheckBox4j();
				checkBox4jFromEnabled.setBackground(Color.WHITE);
				checkBox4jFromEnabled.setBounds(130, 23, 21, 25);
				jDesktopPane1.add(checkBox4jFromEnabled);
				
				JDateControl sampleDateFrom = new JDateControl();
				sampleDateFrom.setEnabled(false);
				sampleDateFrom.setBounds(154, 23, 128, 25);
				jDesktopPane1.add(sampleDateFrom);
				
				JCheckBox4j checkBox4jToEnabled = new JCheckBox4j();
				checkBox4jToEnabled.setBackground(Color.WHITE);
				checkBox4jToEnabled.setBounds(353, 23, 21, 25);
				jDesktopPane1.add(checkBox4jToEnabled);
				
				JDateControl sampleDateTo = new JDateControl();
				sampleDateTo.setEnabled(false);
				sampleDateTo.setBounds(380, 23, 128, 25);
				jDesktopPane1.add(sampleDateTo);
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(12, 131, 973, 401);
				jDesktopPane1.add(scrollPane);
				
				tableResults = new JTable();
				scrollPane.setViewportView(tableResults);
				
				JButton4j btn_Search = new JButton4j(Common.icon_search_16x16);
				btn_Search.setText(lang.get("btn_Search"));
				btn_Search.setMnemonic(KeyEvent.VK_S);
				btn_Search.setBounds(204, 570, 146, 32);
				jDesktopPane1.add(btn_Search);


			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
