package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTWorkstationAdmin.java
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBWTSampleDetail;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWTWorkstationAdmin class allows a user to maintain the
 * table APP_WEIGHT_WORKSTATION.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTReportDetails.jpg" >
 * 
 *
 */
public class JDialogWTReportDetails extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBWTSampleDetail> jListSampleDetails;
	private JButton4j jButtonClose;
	private JScrollPane jScrollPane1;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel label;
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JButton4j jButtonHelp = new JButton4j(Common.icon_help_16x16);

	private void populateList(String samplePoint, Timestamp sampleDate)
	{

		DefaultComboBoxModel<JDBWTSampleDetail> DefComboBoxMod = new DefaultComboBoxModel<JDBWTSampleDetail>();

		JDBWTSampleDetail tempWorkstation = new JDBWTSampleDetail(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBWTSampleDetail> tempWorkstationList = tempWorkstation.getSampleWeights(samplePoint, sampleDate);
		int sel = -1;
		for (int j = 0; j < tempWorkstationList.size(); j++)
		{
			JDBWTSampleDetail t = (JDBWTSampleDetail) tempWorkstationList.get(j);
			t.setDisplayType(JDBWTSampleDetail.longString);
			DefComboBoxMod.addElement(t);

		}

		ListModel<JDBWTSampleDetail> jList1Model = DefComboBoxMod;
		jListSampleDetails.setModel(jList1Model);
		jListSampleDetails.setSelectedIndex(sel);
		jListSampleDetails.setCellRenderer(Common.weight_sample_list);
		jListSampleDetails.ensureIndexIsVisible(sel);
	}

	public JDialogWTReportDetails(JFrame frame, String samplePoint, String sampleDate)
	{
		super(frame);

		setModal(true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setResizable(false);

		this.setPreferredSize(new java.awt.Dimension(455, 518));
		this.setSize(new Dimension(731, 360));

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);
		
		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBackground(Common.color_app_window);
		this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
		jDesktopPane1.setLayout(null);
		
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_REPORTS_DETAILS"));


		mod.getModuleProperties("FRM_WEIGHT_REPORTS_DETAILS");

		setTitle(mod.getDescription() + " " + samplePoint + " @ " +sampleDate);
		

		initGUI();

		populateList(samplePoint, JUtility.getTimeStampFromISOString(sampleDate));
	}

	private void initGUI()
	{
		try
		{

			{

				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(6, 27, 718, 257);
					{
						ListModel<JDBWTSampleDetail> jList1Model = new DefaultComboBoxModel<JDBWTSampleDetail>();
						jListSampleDetails = new JList4j<JDBWTSampleDetail>();
						jScrollPane1.setViewportView(jListSampleDetails);

						jListSampleDetails.setModel(jList1Model);

					}
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(366, 296, 125, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				{
					label = new JLabel("Sequence  Date                     Gross        Tare        Net    UOM      T1s      T2s");

					label.setFont(Common.font_list_weights);
					label.setBounds(8, 12, 724, 15);
					jDesktopPane1.add(label);
				}
			}
			

			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic('0');
			jButtonHelp.setBounds(233, 297, 130, 30);
			jDesktopPane1.add(jButtonHelp);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
