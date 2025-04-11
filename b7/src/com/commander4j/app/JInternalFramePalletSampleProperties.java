package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFramePalletSampleProperties.java
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
import java.util.Date;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBPalletExtension;
import com.commander4j.db.JDBPalletSamples;
import com.commander4j.db.JDBSampleDefectIDs;
import com.commander4j.db.JDBSampleDefectTypes;
import com.commander4j.db.JDBSampleReasons;
import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * The JInternalFramePalletSampleProperties is used editing a record in the
 * APP_PALLET_SAMPLE table.
 * 
 */
public class JInternalFramePalletSampleProperties extends JInternalFrame
{
	private JLabel4j_std jLabelSampleQuantity;
	private JQuantityInput jFormattedTextFieldSampleQuantity;
	private JQuantityInput jFormattedTextFieldLane1Quantity;
	private JQuantityInput jFormattedTextFieldLane2Quantity;
	private JQuantityInput jFormattedTextFieldLane3Quantity;
	private JQuantityInput jFormattedTextFieldLane4Quantity;
	private JQuantityInput jFormattedTextFieldLane5Quantity;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabel_SSCC;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JComboBox4j<JDBSampleReasons> jComboBoxReason;
	private JComboBox4j<JDBSampleDefectIDs> jComboBoxDefectID;
	private JComboBox4j<JDBSampleDefectTypes> jComboBoxDefectType;
	private JLabel4j_std jLabelReason;
	private JDateControl jSpinnerSampleDate;
	private JLabel4j_std jLabel_Sample_Date;
	private JLabel4j_std jLabel_Sample_Point;
	private JTextField4j jTextFieldComment;
	private JLabel4j_std jLabelComment;
	private JLabel4j_std jLabelDefectID;
	private JLabel4j_std jLabelDefectType;
	private JTextField4j jTextFieldSSCC;
	private JLabel4j_std lblUserID;
	private JLabel4j_std lblOperative;
	private JTextField4j jTextFieldUserID;
	private String lsscc;
	private Long lsequence;
	private JDBPalletSamples sampleProperties = new JDBPalletSamples(Common.selectedHostID, Common.sessionID);
	private Vector<JDBSampleReasons> reasonList = new Vector<JDBSampleReasons>();
	private Vector<JDBSampleDefectIDs> defectIDList = new Vector<JDBSampleDefectIDs>();
	private Vector<JDBSampleDefectTypes> defectTypeList = new Vector<JDBSampleDefectTypes>();
	private JDBSampleReasons sampleReasons = new JDBSampleReasons(Common.selectedHostID, Common.sessionID);
	private JDBSampleDefectIDs sampleDefectIDs = new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID);
	private JDBSampleDefectTypes sampleDefectTypes = new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID);
	private JDBPalletExtension palext = new JDBPalletExtension(Common.selectedHostID, Common.sessionID);
	private JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JCheckBox4j checkBoxLeaking;
	private Color light_grey = new Color(238, 238, 238);
	private Color error_color = Color.RED;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JPanel panelDefectID = new JPanel();
	private JPanel panelDefectType = new JPanel();
	private JPanel panelReason = new JPanel();
	private JTextField4j jTextFieldFiller_ID;
	private JTextField4j jTextFieldOperative;
	private JButton4j jButtonLookup_Filler_ID;
	private JButton4j jButtonLookup_Operative;
	private String llocation = "";
	private Vector<JDBSampleDefectIDs> tempIDList = new Vector<JDBSampleDefectIDs>();
	private JButton4j jButtonNotifyEmail = new JButton4j(Common.icon_notifyEmail_16x16);
	private JLabel4j_std jLabelSum = new JLabel4j_std();

	private void refreshDefectIDList(String typeFilter, String dflt)
	{

		tempIDList.clear();
		tempIDList.addAll(sampleDefectIDs.getSampleDefects(true));
		defectIDList.clear();
		defectIDList.add(new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID));

		for (int x = 0; x < tempIDList.size(); x++)
		{
			if ((typeFilter.equals("")) || (typeFilter.equals(tempIDList.get(x).getSampleDefectType())))
			{
				defectIDList.add(tempIDList.get(x));
			}
		}

		for (int x = 0; x < defectIDList.size(); x++)
		{
			if (defectIDList.get(x).getSampleDefectID().equals(sampleDefectIDs.getSampleDefectID()))
			{
				jComboBoxDefectID.setSelectedIndex(x);
			}
		}

	}

	private void getPalletExtensionData(String sscc)
	{
		if (palext.getSamplePalletExtensionProperties(sscc))
		{
			llocation = palext.getLocation();
		}
		else
		{
			llocation = "";
		}
	}

	private void getPalletData(String sscc)
	{
		pallet.getPalletProperties(sscc);

	}

	public JInternalFramePalletSampleProperties(String sscc, Long sequence)
	{
		super();

		initGUI();

		setSampleID(sscc, sequence);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_SAMPLE_EDIT"));

		JLabel4j_std jLabelLane1 = new JLabel4j_std();
		jLabelLane1.setForeground(Color.BLUE);
		jLabelLane1.setFont(new Font("Arial", Font.ITALIC, 11));
		jLabelLane1.setText(lang.get("lbl_Lane") + " 1");
		jLabelLane1.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelLane1.setBounds(104, 281, 51, 22);
		jDesktopPane1.add(jLabelLane1);

		JLabel4j_std jLabelLane2 = new JLabel4j_std();
		jLabelLane2.setForeground(Color.BLUE);
		jLabelLane2.setFont(new Font("Arial", Font.ITALIC, 11));
		jLabelLane2.setText(lang.get("lbl_Lane") + " 2");
		jLabelLane2.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelLane2.setBounds(217, 281, 51, 22);
		jDesktopPane1.add(jLabelLane2);

		JLabel4j_std jLabelLane3 = new JLabel4j_std();
		jLabelLane3.setForeground(Color.BLUE);
		jLabelLane3.setFont(new Font("Arial", Font.ITALIC, 11));
		jLabelLane3.setText(lang.get("lbl_Lane") + " 3");
		jLabelLane3.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelLane3.setBounds(330, 281, 51, 22);
		jDesktopPane1.add(jLabelLane3);

		JLabel4j_std jLabelLane4 = new JLabel4j_std();
		jLabelLane4.setForeground(Color.BLUE);
		jLabelLane4.setFont(new Font("Arial", Font.ITALIC, 11));
		jLabelLane4.setText(lang.get("lbl_Lane") + " 4");
		jLabelLane4.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelLane4.setBounds(443, 281, 51, 22);
		jDesktopPane1.add(jLabelLane4);

		JLabel4j_std jLabelLane5 = new JLabel4j_std();
		jLabelLane5.setForeground(Color.BLUE);
		jLabelLane5.setFont(new Font("Arial", Font.ITALIC, 11));
		jLabelLane5.setText(lang.get("lbl_Lane") + " 5");
		jLabelLane5.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabelLane5.setBounds(556, 281, 51, 22);
		jDesktopPane1.add(jLabelLane5);
		
		JLabel4j_std jLabelEquals = new JLabel4j_std();
		jLabelEquals.setText("=");
		jLabelEquals.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelEquals.setForeground(Color.BLUE);
		jLabelEquals.setFont(new Font("Arial", Font.BOLD, 11));
		jLabelEquals.setBounds(673, 281, 27, 22);
		jDesktopPane1.add(jLabelEquals);
		

		jLabelSum.setText("0.000");
		jLabelSum.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelSum.setForeground(Color.BLUE);
		jLabelSum.setFont(new Font("Arial", Font.BOLD, 11));
		jLabelSum.setBounds(699, 281, 80, 22);
		jDesktopPane1.add(jLabelSum);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jSpinnerSampleDate.requestFocus();
				// jTextFieldComment.setCaretPosition(jTextFieldComment.getText().length());

			}
		});

	}

	public void setSampleID(String sscc, Long sequence)
	{

		getPalletExtensionData(sscc);

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to Sample Record [" + lsscc + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		lsscc = sscc;
		lsequence = sequence;

		jTextFieldSSCC.setText(sscc);
		this.setTitle("SSCC [" + sscc + "]");

		sampleProperties.setSSCC(lsscc);
		sampleProperties.setSampleSequence(lsequence);

		if (sampleProperties.getPalletSampleProperties() == true)
		{

			jTextFieldComment.setText(sampleProperties.getSampleComment());
			checkBoxLeaking.setSelected(sampleProperties.isSampleLeaking());
			jTextFieldUserID.setText(sampleProperties.getUserID());
			jTextFieldOperative.setText(sampleProperties.getOperative());

			jFormattedTextFieldSampleQuantity.setValue(sampleProperties.getSampleQuantity());

			jFormattedTextFieldLane1Quantity.setValue(sampleProperties.getLane1Quantity());
			jFormattedTextFieldLane2Quantity.setValue(sampleProperties.getLane2Quantity());
			jFormattedTextFieldLane3Quantity.setValue(sampleProperties.getLane3Quantity());
			jFormattedTextFieldLane4Quantity.setValue(sampleProperties.getLane4Quantity());
			jFormattedTextFieldLane5Quantity.setValue(sampleProperties.getLane5Quantity());

			jTextFieldFiller_ID.setText(sampleProperties.getSamplePoint());
			jTextFieldUserID.setText(sampleProperties.getUserID());

			try
			{
				jSpinnerSampleDate.setDate(sampleProperties.getSampleDateTime());
			}
			catch (Exception e)
			{

			}

			for (int x = 0; x < defectTypeList.size(); x++)
			{
				if (defectTypeList.get(x).getSampleDefectType().equals(sampleProperties.getSampleDefectType()))
				{
					jComboBoxDefectType.setSelectedIndex(x);
				}
			}

			for (int x = 0; x < defectIDList.size(); x++)
			{
				if (defectIDList.get(x).getSampleDefectID().equals(sampleProperties.getSampleDefectID()))
				{
					jComboBoxDefectID.setSelectedIndex(x);
				}
			}

			for (int x = 0; x < reasonList.size(); x++)
			{
				if (reasonList.get(x).getSampleReason().equals(sampleProperties.getSampleReason()))
				{
					jComboBoxReason.setSelectedIndex(x);
				}
			}

		}
		else
		{
			getPalletData(lsscc);

			jButtonSave.setEnabled(true);

			try
			{
				jSpinnerSampleDate.setDate(new Date());
			}
			catch (Exception e)
			{

			}
		}

		jButtonSave.setEnabled(false);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jSpinnerSampleDate.requestFocus();

			}
		});
	}

	private void initGUI()
	{
		try
		{

			{

				this.setPreferredSize(new java.awt.Dimension(448, 289));
				this.setBounds(25, 25, 814, 501);
				setVisible(true);
				this.setClosable(true);
				this.setIconifiable(true);

				reasonList.add(new JDBSampleReasons(Common.selectedHostID, Common.sessionID));
				reasonList.addAll(sampleReasons.getSampleReasons(true));
				ComboBoxModel<JDBSampleReasons> jComboBox2Model = new DefaultComboBoxModel<JDBSampleReasons>(reasonList);
				jComboBoxReason = new JComboBox4j<JDBSampleReasons>();
				jComboBoxReason.setModel(jComboBox2Model);
				jComboBoxReason.setEditable(false);
				jComboBoxReason.setMaximumRowCount(20);

				defectTypeList.add(new JDBSampleDefectTypes(Common.selectedHostID, Common.sessionID));
				defectTypeList.addAll(sampleDefectTypes.getSampleDefects(true));
				ComboBoxModel<JDBSampleDefectTypes> jComboBox6Model = new DefaultComboBoxModel<JDBSampleDefectTypes>(defectTypeList);
				jComboBoxDefectType = new JComboBox4j<JDBSampleDefectTypes>();
				jComboBoxDefectType.setModel(jComboBox6Model);
				jComboBoxDefectType.setEditable(false);
				jComboBoxDefectType.setMaximumRowCount(20);

				defectIDList.add(new JDBSampleDefectIDs(Common.selectedHostID, Common.sessionID));
				defectIDList.addAll(sampleDefectIDs.getSampleDefects(true));
				ComboBoxModel<JDBSampleDefectIDs> jComboBox3Model = new DefaultComboBoxModel<JDBSampleDefectIDs>(defectIDList);
				jComboBoxDefectID = new JComboBox4j<JDBSampleDefectIDs>();
				jComboBoxDefectID.setModel(jComboBox3Model);
				jComboBoxDefectID.setEditable(false);
				jComboBoxDefectID.setMaximumRowCount(20);

				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);

				{
					jButtonSave = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonSave);
					jButtonSave.setText(lang.get("btn_Save"));
					jButtonSave.setBounds(180, 425, 112, 32);
					jButtonSave.setEnabled(false);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							save();
						}
					});
				}

				{
					jLabel_SSCC = new JLabel4j_std();
					jDesktopPane1.add(jLabel_SSCC);
					jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
					jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_SSCC.setBounds(5, 10, 144, 22);
				}
				{
					jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
					jDesktopPane1.add(jTextFieldSSCC);
					jTextFieldSSCC.setEditable(false);
					jTextFieldSSCC.setEnabled(false);
					jTextFieldSSCC.setBounds(163, 10, 126, 22);
				}
				{
					jLabelDefectType = new JLabel4j_std();
					jDesktopPane1.add(jLabelDefectType);
					jLabelDefectType.setText(lang.get("lbl_Defect_Type"));
					jLabelDefectType.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelDefectType.setBounds(5, 153, 144, 22);
				}
				{
					jLabelDefectID = new JLabel4j_std();
					jDesktopPane1.add(jLabelDefectID);
					jLabelDefectID.setText(lang.get("lbl_Defect_ID"));
					jLabelDefectID.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelDefectID.setBounds(5, 187, 144, 22);
				}
				{
					jLabelComment = new JLabel4j_std();
					jDesktopPane1.add(jLabelComment);
					jLabelComment.setText(lang.get("lbl_Comment"));
					jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelComment.setBounds(5, 313, 144, 22);
				}
				{
					jTextFieldComment = new JTextField4j(JDBPalletSamples.field_sample_comment);
					jDesktopPane1.add(jTextFieldComment);
					jTextFieldComment.setBounds(163, 313, 616, 22);
					jTextFieldComment.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					lblUserID = new JLabel4j_std();
					lblUserID.setText(lang.get("lbl_User_ID"));
					lblUserID.setHorizontalAlignment(SwingConstants.TRAILING);
					lblUserID.setBounds(5, 378, 144, 22);
					jDesktopPane1.add(lblUserID);
				}

				{
					lblOperative = new JLabel4j_std();
					lblOperative.setText(lang.get("lbl_Operator_ID"));
					lblOperative.setHorizontalAlignment(SwingConstants.TRAILING);
					lblOperative.setBounds(5, 346, 144, 22);
					jDesktopPane1.add(lblOperative);
				}
				{
					jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
					jTextFieldUserID.setEditable(false);
					jTextFieldUserID.setBounds(163, 378, 126, 22);
					jTextFieldUserID.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jTextFieldUserID);
				}
				{
					jLabel_Sample_Date = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Sample_Date);
					jLabel_Sample_Date.setText(lang.get("lbl_Sample_Date"));
					jLabel_Sample_Date.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Sample_Date.setBounds(5, 45, 144, 22);
				}

				{
					jLabel_Sample_Point = new JLabel4j_std();
					jDesktopPane1.add(jLabel_Sample_Point);
					jLabel_Sample_Point.setText(lang.get("lbl_Sample_SubLocation"));
					jLabel_Sample_Point.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel_Sample_Point.setBounds(5, 80, 144, 22);
				}

				{
					jSpinnerSampleDate = new JDateControl();
					jDesktopPane1.add(jSpinnerSampleDate);
					jSpinnerSampleDate.setBounds(163, 45, 128, 22);
					jSpinnerSampleDate.getEditor().setPreferredSize(new java.awt.Dimension(86, 32));
					jSpinnerSampleDate.getEditor().addKeyListener(new KeyAdapter()
					{
						public void keyPressed(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jSpinnerSampleDate.addChangeListener(new ChangeListener()
					{
						public void stateChanged(final ChangeEvent e)

						{
							jButtonSave.setEnabled(true);
						}
					});
				}
				{
					jLabelReason = new JLabel4j_std();
					jDesktopPane1.add(jLabelReason);
					jLabelReason.setText(lang.get("lbl_Reason"));
					jLabelReason.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelReason.setBounds(5, 117, 144, 22);
				}

				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(406, 425, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(520, 425, 112, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}

				{
					jFormattedTextFieldSampleQuantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldSampleQuantity.setBounds(163, 248, 50, 22);
					jFormattedTextFieldSampleQuantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jDesktopPane1.add(jFormattedTextFieldSampleQuantity);
				}

				{
					jFormattedTextFieldLane1Quantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldLane1Quantity.setBounds(162, 281, 50, 22);
					jFormattedTextFieldLane1Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							calcLaneTotals();
						}
					});
					jDesktopPane1.add(jFormattedTextFieldLane1Quantity);
				}

				{
					jFormattedTextFieldLane2Quantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldLane2Quantity.setBounds(275, 281, 50, 22);
					jFormattedTextFieldLane2Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							calcLaneTotals();
						}
					});
					jDesktopPane1.add(jFormattedTextFieldLane2Quantity);
				}

				{
					jFormattedTextFieldLane3Quantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldLane3Quantity.setBounds(388, 281, 50, 22);
					jFormattedTextFieldLane3Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							calcLaneTotals();
						}
					});
					jDesktopPane1.add(jFormattedTextFieldLane3Quantity);
				}

				{
					jFormattedTextFieldLane4Quantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldLane4Quantity.setBounds(501, 281, 50, 22);
					jFormattedTextFieldLane4Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							calcLaneTotals();
						}
					});
					jDesktopPane1.add(jFormattedTextFieldLane4Quantity);
				}

				{
					jFormattedTextFieldLane5Quantity = new JQuantityInput(new BigDecimal("0"));
					jFormattedTextFieldLane5Quantity.setBounds(615, 281, 50, 22);
					jFormattedTextFieldLane5Quantity.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonSave.setEnabled(true);
							calcLaneTotals();
						}
					});
					jDesktopPane1.add(jFormattedTextFieldLane5Quantity);
				}

				{
					jLabelSampleQuantity = new JLabel4j_std();
					jLabelSampleQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelSampleQuantity.setText(lang.get("lbl_Sample_Quantity"));
					jLabelSampleQuantity.setBounds(5, 248, 144, 22);
					jDesktopPane1.add(jLabelSampleQuantity);
				}

				{
					JLabel4j_std lblLeaking = new JLabel4j_std();
					lblLeaking.setText(lang.get("lbl_Leaking"));
					lblLeaking.setHorizontalAlignment(SwingConstants.TRAILING);
					lblLeaking.setBounds(5, 219, 144, 22);
					jDesktopPane1.add(lblLeaking);
				}

				{
					checkBoxLeaking = new JCheckBox4j();
					checkBoxLeaking.setOpaque(true);
					checkBoxLeaking.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					checkBoxLeaking.setText("");
					checkBoxLeaking.setSelected(false);
					checkBoxLeaking.setBounds(163, 219, 21, 22);
					jDesktopPane1.add(checkBoxLeaking);
				}

				{
					jTextFieldFiller_ID = new JTextField4j(JDBWTSamplePoint.field_SamplePoint);
					jTextFieldFiller_ID.setEditable(false);
					jTextFieldFiller_ID.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldFiller_ID.setBounds(163, 80, 106, 22);
					jDesktopPane1.add(jTextFieldFiller_ID);
				}

				{
					jTextFieldOperative = new JTextField4j();
					jTextFieldOperative.setEditable(false);
					jTextFieldOperative.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							jButtonSave.setEnabled(true);
						}
					});
					jTextFieldOperative.setBounds(163, 346, 106, 22);
					jDesktopPane1.add(jTextFieldOperative);
				}

				{
					jButtonLookup_Filler_ID = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookup_Filler_ID);
					jButtonLookup_Filler_ID.setBounds(270, 80, 21, 22);
					jButtonLookup_Filler_ID.setEnabled(true);
					jButtonLookup_Filler_ID.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = llocation;
							if (JLaunchLookup.weightSamplePoint())
							{
								jTextFieldFiller_ID.setText(JLaunchLookup.dlgResult);
								jButtonSave.setEnabled(true);
							}
						}
					});
				}

				{
					jButtonLookup_Operative = new JButton4j(Common.icon_lookup_16x16);
					jDesktopPane1.add(jButtonLookup_Operative);
					jButtonLookup_Operative.setBounds(270, 346, 21, 22);
					jButtonLookup_Operative.setEnabled(true);
					jButtonLookup_Operative.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JLaunchLookup.dlgAutoExec = true;
							JLaunchLookup.dlgCriteriaDefault = "Y";
							if (JLaunchLookup.operatives())
							{
								jTextFieldOperative.setText(JLaunchLookup.dlgResult);
								jButtonSave.setEnabled(true);
							}
						}
					});
				}

				panelReason.setBounds(163, 115, 502, 27);
				jDesktopPane1.add(panelReason);
				panelReason.setLayout(null);

				jComboBoxReason.setBounds(2, 2, 500, 22);
				panelReason.add(jComboBoxReason);
				jComboBoxReason.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});

				{
					jButtonNotifyEmail.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							notifyEmail();

						}
					});
					jButtonNotifyEmail.setText(lang.get("btn_Notify"));
					jButtonNotifyEmail.setMnemonic('0');
					jButtonNotifyEmail.setEnabled(true);
					jButtonNotifyEmail.setBounds(293, 425, 112, 32);
					jDesktopPane1.add(jButtonNotifyEmail);
				}

				panelDefectID.setBounds(163, 185, 502, 27);
				jDesktopPane1.add(panelDefectID);
				panelDefectID.setLayout(null);

				panelDefectType.setBounds(163, 151, 502, 27);
				jDesktopPane1.add(panelDefectType);
				panelDefectType.setLayout(null);

				jComboBoxDefectType.setBounds(2, 2, 500, 22);
				panelDefectType.add(jComboBoxDefectType);
				jComboBoxDefectType.setMaximumRowCount(20);
				jComboBoxDefectType.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
						refreshDefectIDList(((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType(), ((JDBSampleDefectIDs) jComboBoxDefectID.getSelectedItem()).getSampleDefectID());
					}
				});

				jComboBoxDefectID.setBounds(2, 2, 500, 22);
				panelDefectID.add(jComboBoxDefectID);
				jComboBoxDefectID.setMaximumRowCount(20);
				jComboBoxDefectID.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonSave.setEnabled(true);
					}
				});
				
				jFormattedTextFieldLane1Quantity.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						calcLaneTotals();
					}
				});
				
				jFormattedTextFieldLane2Quantity.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						calcLaneTotals();
					}
				});
				
				jFormattedTextFieldLane3Quantity.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						calcLaneTotals();
					}
				});	
				
				jFormattedTextFieldLane4Quantity.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						calcLaneTotals();
					}
				});
				
				jFormattedTextFieldLane5Quantity.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						calcLaneTotals();
					}
				});

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void calcLaneTotals()
	{
		BigDecimal sum = jFormattedTextFieldLane1Quantity.getQuantity();
		sum = sum.add(jFormattedTextFieldLane2Quantity.getQuantity());
		sum = sum.add(jFormattedTextFieldLane3Quantity.getQuantity());
		sum = sum.add(jFormattedTextFieldLane4Quantity.getQuantity());
		sum = sum.add(jFormattedTextFieldLane5Quantity.getQuantity());
		
		jLabelSum.setText(sum.toString());
	}

	private boolean save()
	{

		boolean result = false;

		if (validateInput())
		{

			sampleProperties.setSampleDateTime(JUtility.getTimestampFromDate(jSpinnerSampleDate.getDate()));

			try
			{
				sampleProperties.setSampleReason(((JDBSampleReasons) jComboBoxReason.getSelectedItem()).getSampleReason());
			}
			catch (Exception e)
			{
				sampleProperties.setSampleReason("");
			}

			try
			{
				sampleProperties.setSampleDefectID(((JDBSampleDefectIDs) jComboBoxDefectID.getSelectedItem()).getSampleDefectID());
			}
			catch (Exception e)
			{
				sampleProperties.setSampleDefectID("");
			}

			try
			{
				sampleProperties.setSampleDefectType(((JDBSampleDefectTypes) jComboBoxDefectType.getSelectedItem()).getSampleDefectType());
			}
			catch (Exception e)
			{
				sampleProperties.setSampleDefectType("");
			}

			sampleProperties.setSampleLeaking(checkBoxLeaking.isSelected());

			sampleProperties.setSampleComment(jTextFieldComment.getText());

			sampleProperties.setSampleQuantity(JUtility.stringToBigDecimal(jFormattedTextFieldSampleQuantity.getText().toString()));

			sampleProperties.setLane1Quantity(JUtility.stringToBigDecimal(jFormattedTextFieldLane1Quantity.getValue().toString()));
			sampleProperties.setLane2Quantity(JUtility.stringToBigDecimal(jFormattedTextFieldLane2Quantity.getValue().toString()));
			sampleProperties.setLane3Quantity(JUtility.stringToBigDecimal(jFormattedTextFieldLane3Quantity.getValue().toString()));
			sampleProperties.setLane4Quantity(JUtility.stringToBigDecimal(jFormattedTextFieldLane4Quantity.getValue().toString()));
			sampleProperties.setLane5Quantity(JUtility.stringToBigDecimal(jFormattedTextFieldLane5Quantity.getValue().toString()));

			sampleProperties.setUserID(Common.userList.getUser(Common.sessionID).getUserId());

			sampleProperties.setOperative(jTextFieldOperative.getText());

			jTextFieldUserID.setText(Common.userList.getUser(Common.sessionID).getUserId());

			sampleProperties.setSamplePoint(jTextFieldFiller_ID.getText());

			if (sampleProperties.isValid() == false)
			{
				if (sampleProperties.getSampleSequence().equals(Long.valueOf(-1)))
				{
					sampleProperties.setSampleSequence(sampleProperties.generateNewSampleSequence());
				}

				sampleProperties.create();
			}

			if (sampleProperties.update())
			{
				jTextFieldComment.setText(sampleProperties.getSampleComment());
				jButtonSave.setEnabled(false);
				result = true;
			}
			else
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(Common.mainForm, sampleProperties.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm_16x16);
			}
		}
		return result;

	}

	private void notifyEmail()
	{
		if (save())
		{
			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Send Email ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);
				pallet.SortNotification(lsscc);
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(this, "Notification Requested.");
			}
		}

	}

	private boolean validateInput()
	{
		boolean valid = true;

		BigDecimal val1 = JUtility.stringToBigDecimal(jFormattedTextFieldSampleQuantity.getText().toString());

		BigDecimal val2 = new BigDecimal("0.00");

		boolean zeroQuantity = false;

		if (val1.compareTo(val2) == 0)
		{
			zeroQuantity = true;
		}

		if (jTextFieldOperative.getText().equals(""))
		{
			valid = false;
			jTextFieldOperative.setBackground(error_color);
		}
		else
		{
			jTextFieldOperative.setBackground(light_grey);

			if (jTextFieldFiller_ID.getText().equals(""))
			{
				valid = false;
				jTextFieldFiller_ID.setBackground(error_color);
			}
			else
			{
				jTextFieldFiller_ID.setBackground(light_grey);

				// Check Reason is present
				if (jComboBoxReason.getSelectedItem().toString().equals(""))
				{
					// Reason is missing
					valid = false;
					panelReason.setBackground(error_color);
				}
				else
				{
					// Reason is present
					panelReason.setBackground(light_grey);

					if (jComboBoxDefectID.getSelectedItem().toString().equals(""))
					{
						// Defect is NOT present
						if (zeroQuantity)
						{
							// Zero is permitted if defect is blank
							jFormattedTextFieldSampleQuantity.setBackground(Color.WHITE);
							panelDefectID.setBackground(light_grey);
						}
						else
						{
							// Quantity Required if Defect present
							valid = false;
							jFormattedTextFieldSampleQuantity.setBackground(error_color);
							panelDefectID.setBackground(error_color);
						}

						if (checkBoxLeaking.isSelected())
						{
							// Leaking should not be selected if no Defect
							// selected
							valid = false;
							checkBoxLeaking.setBackground(error_color);
							panelDefectID.setBackground(error_color);
						}
						else
						{
							checkBoxLeaking.setBackground(light_grey);
							panelDefectID.setBackground(light_grey);
						}
					}
					else
					{
						// Defect is present

						checkBoxLeaking.setBackground(light_grey);

						if (zeroQuantity)
						{
							// Quantity should be zero if Defect blank
							valid = false;
							jFormattedTextFieldSampleQuantity.setBackground(error_color);
							panelDefectID.setBackground(error_color);
						}
						else
						{
							// Zero is permitted if defect is blank
							jFormattedTextFieldSampleQuantity.setBackground(Color.WHITE);
							panelDefectID.setBackground(light_grey);
						}
					}
				}
			}
		}

		if (valid == false)
		{
			JUtility.errorBeep();
		}

		return valid;
	}
}
