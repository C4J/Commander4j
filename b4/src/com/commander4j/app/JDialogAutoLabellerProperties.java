package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogAutoLabellerProperties.java
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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBAutoLabeller;
import com.commander4j.db.JDBAutoLabellerResources;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBPrinterLineMembership;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBWorkstationLineMembership;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * This Dialog box allow you to edit the properties of the Auto Labeler. The
 * data is stored in the table APP_AUTO_LABELLER. This form allows you to
 * determine which Process Order Resources are associated with the Line, which
 * Workstations are allowed to assign data to the Line and which physical
 * printers are assigned to it.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogAutoLabellerProperties.jpg" >
 * 
 * @see com.commander4j.app.JInternalFrameAutoLabellerLines
 *      JInternalFrameAutoLabellerLines
 * @see com.commander4j.db.JDBAutoLabeller JDBAutoLabeller
 */

public class JDialogAutoLabellerProperties extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldType;
	private JLabel4j_std jLabelLineID;
	private JDBAutoLabeller autolab = new JDBAutoLabeller(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JButton4j jButtonAssignPrinter = new JButton4j(Common.icon_arrow_left);
	private JButton4j jButtonUnAssignPrinter = new JButton4j(Common.icon_arrow_right);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JSpinner JSpinnerSSCCSequence = new JSpinner();
	private JTextField4j jTextFieldSSCCPrefix = new JTextField4j();
	private JTextField4j textField4SSCCCheckDigit = new JTextField4j();
	private String SSCCPrefix;
	private int PrefixLen;
	private String pattern;
	private JEANBarcode barcode = new JEANBarcode();
	private JCheckBox4j chckbxSSCCRange = new JCheckBox4j("");
	private JList4j<String> listAssignedResources = new JList4j<String>();
	private JList4j<String> listAllResources = new JList4j<String>();
	private JCheckBox4j checkBox4jValidateResource = new JCheckBox4j("");
	private JCheckBox4j checkBox4jValidateWorkstation = new JCheckBox4j("");
	private JComboBox4j<String> comboBox4jGroup = new JComboBox4j<String>();
	private JList4j<JDBListData> jListAssignedPrinters;
	private JList4j<JDBListData> jListUnAssignedPrinters;
	private JList4j<String> listWorkstations = new JList4j<String>();

	private DefaultComboBoxModel<JDBListData> unassignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> unassignedList = new LinkedList<JDBListData>();
	private DefaultComboBoxModel<JDBListData> assignedModel = new DefaultComboBoxModel<JDBListData>();
	private LinkedList<JDBListData> assignedList = new LinkedList<JDBListData>();
	private String selectedLine = "";
	private String selectedGroup = "";
	private JTextField4j jTextFieldPrefixCode;

	public JDialogAutoLabellerProperties(JFrame frame, String line, String group)
	{
		super(frame, "Line Properties", ModalityType.DOCUMENT_MODAL);

		selectedLine = line;
		selectedGroup = group;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Line Properties");
		this.setResizable(false);
		this.setSize(1022, 574);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		jDesktopPane1 = new JDesktopPane();
		jDesktopPane1.setBounds(0, 200, 671, -200);
		jDesktopPane1.setBackground(Common.color_edit_properties);
		this.getContentPane().add(jDesktopPane1);
		jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
		jDesktopPane1.setLayout(null);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_AUTO_LAB_EDIT"));

		initGUI();

		if (group.equals("Pack"))
		{
			chckbxSSCCRange.setEnabled(false);
		}

		autolab.getProperties(line, group);

		jTextFieldType.setText(line);
		comboBox4jGroup.setSelectedItem(group);

		setTitle(getTitle() + " - " + line + " " + group);
		jTextFieldType.setText(autolab.getLine());
		jTextFieldDescription.setText(autolab.getDescription());
		jTextFieldPrefixCode.setText(autolab.getPrefixCode());
		JSpinnerSSCCSequence.setValue(autolab.getSSCCSequence());
		chckbxSSCCRange.setSelected(autolab.isSSCCRangeEnabled());
		JSpinnerSSCCSequence.setEnabled(autolab.isSSCCRangeEnabled());
		checkBox4jValidateResource.setSelected(autolab.isValidateResource());

		checkBox4jValidateWorkstation.setSelected(autolab.isValidateWorkstation());

		populateListAssignedResources(line, group, "");
		populateListAllResources();
		populateUnAssignedPrinterList();
		populateAssignedPrinterList();
		populateListWorkstations();
		setButtonState();
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				

			}
		});

	}

	/**
	 * Calculate the SSCC check digit.
	 */
	private void calcCheckDigit()
	{
		String sscc = SSCCPrefix;
		String suffix = JUtility.padString(JSpinnerSSCCSequence.getValue().toString().trim(), false, pattern.length(), "0");
		sscc = sscc + suffix;
		textField4SSCCCheckDigit.setText(barcode.calcCheckdigit(sscc));
	}

	/**
	 * Method used to build GUI
	 */
	private void initGUI()
	{
		try
		{

			{

				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jLabelLineID = new JLabel4j_std();
					jDesktopPane1.add(jLabelLineID, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelLineID.setText(lang.get("lbl_Line_ID"));
					jLabelLineID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelLineID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelLineID.setBounds(0, 17, 179, 21);
				}
				comboBox4jGroup.setEnabled(false);
				comboBox4jGroup.setPreferredSize(new Dimension(40, 20));
				comboBox4jGroup.setFocusCycleRoot(true);
				comboBox4jGroup.setBounds(192, 50, 167, 21);

				comboBox4jGroup.setModel(new DefaultComboBoxModel<String>(Common.printerGroup));
				comboBox4jGroup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(comboBox4jGroup);
				{
					jTextFieldType = new JTextField4j();
					jDesktopPane1.add(jTextFieldType, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldType.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldType.setEditable(false);
					jTextFieldType.setEnabled(false);
					jTextFieldType.setBounds(191, 17, 136, 21);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDescription.setBounds(339, 17, 107, 21);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBAutoLabeller.field_description);
					jDesktopPane1.add(jTextFieldDescription, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldDescription.setBounds(456, 17, 327, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
					jTextFieldDescription.setFocusCycleRoot(true);
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setBounds(277, 501, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							autolab.setDescription(jTextFieldDescription.getText());
							autolab.setPrefixCode(jTextFieldPrefixCode.getText());
							autolab.setSSCCRangeEnabled(chckbxSSCCRange.isSelected());
							autolab.setSSCCSequence(Long.valueOf(JSpinnerSSCCSequence.getValue().toString()));
							if (autolab.isValidLineGroup() == false)
							{
								autolab.create(autolab.getLine(), jTextFieldDescription.getText(), "");
							}
							autolab.setValidateResource(checkBox4jValidateResource.isSelected());
							autolab.setValidateWorkstation(checkBox4jValidateWorkstation.isSelected());
							autolab.update();
							autolab.updateSSCC();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(521, 501, 112, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(643, 501, 112, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				JSpinnerSSCCSequence.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent arg0)
					{
						jButtonUpdate.setEnabled(true);
						calcCheckDigit();
					}
				});

				{
					JLabel4j_std label4j_std = new JLabel4j_std();
					label4j_std.setText(lang.get("lbl_Group_ID"));
					label4j_std.setHorizontalTextPosition(SwingConstants.RIGHT);
					label4j_std.setHorizontalAlignment(SwingConstants.RIGHT);
					label4j_std.setBounds(23, 52, 157, 19);
					jDesktopPane1.add(label4j_std);
				}

				textField4SSCCCheckDigit.setText("");
				textField4SSCCCheckDigit.setHorizontalAlignment(SwingConstants.RIGHT);
				textField4SSCCCheckDigit.setEnabled(false);
				textField4SSCCCheckDigit.setEditable(false);
				textField4SSCCCheckDigit.setBounds(732, 50, 22, 28);
				jDesktopPane1.add(textField4SSCCCheckDigit);

				jTextFieldSSCCPrefix.setHorizontalAlignment(SwingConstants.CENTER);
				jTextFieldSSCCPrefix.setEnabled(false);
				jTextFieldSSCCPrefix.setEditable(false);
				jTextFieldSSCCPrefix.setBounds(549, 50, 96, 28);
				jDesktopPane1.add(jTextFieldSSCCPrefix);

				SSCCPrefix = ctrl.getKeyValue("SSCC PREFIX");
				PrefixLen = SSCCPrefix.length();
				jTextFieldSSCCPrefix.setText(SSCCPrefix);
				pattern = JUtility.padString(18 - PrefixLen - 1, "0");

				JSpinnerSSCCSequence.setBounds(646, 50, 83, 28);
				JSpinner.NumberEditor ne = new JSpinner.NumberEditor(JSpinnerSSCCSequence, pattern);
				ne.getTextField().setFont(Common.font_std);
				JSpinnerSSCCSequence.setEditor(ne);
				jDesktopPane1.add(JSpinnerSSCCSequence);

				JLabel4j_std jLabelSSCCSequence = new JLabel4j_std();
				jLabelSSCCSequence.setText(lang.get("lbl_SSCC_Sequence"));
				jLabelSSCCSequence.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabelSSCCSequence.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabelSSCCSequence.setBounds(376, 53, 134, 21);
				jDesktopPane1.add(jLabelSSCCSequence);
				chckbxSSCCRange.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						autolab.setSSCCRangeEnabled(chckbxSSCCRange.isSelected());
						JSpinnerSSCCSequence.setEnabled(chckbxSSCCRange.isSelected());
						jButtonUpdate.setEnabled(true);
					}
				});

				chckbxSSCCRange.setBounds(517, 50, 28, 23);
				jDesktopPane1.add(chckbxSSCCRange);

				JScrollPane scrollPaneAssignedResources = new JScrollPane();
				scrollPaneAssignedResources.setBounds(30, 154, 170, 318);
				jDesktopPane1.add(scrollPaneAssignedResources);

				scrollPaneAssignedResources.setViewportView(listAssignedResources);
				listAssignedResources.setForeground(Common.color_listFontStandard);
				listAssignedResources.setCellRenderer(Common.renderer_list_assigned);
				listAssignedResources.setBackground(Common.color_list_assigned);

				JScrollPane scrollPaneAllResources = new JScrollPane();
				scrollPaneAllResources.setBounds(235, 154, 170, 318);
				jDesktopPane1.add(scrollPaneAllResources);

				scrollPaneAllResources.setViewportView(listAllResources);
				listAllResources.setCellRenderer(Common.renderer_list_unassigned);
				listAllResources.setBackground(Common.color_list_unassigned);

				JButton4j jButtonAssignResource = new JButton4j(Common.icon_arrow_left);
				jButtonAssignResource.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (listAllResources.getSelectedIndex() > -1)
						{
							for (int j = listAllResources.getMaxSelectionIndex(); j >= listAllResources.getMinSelectionIndex(); j--)
							{
								if (listAllResources.isSelectedIndex(j))
								{
									String item = (String) listAllResources.getModel().getElementAt(j);
									JDBAutoLabellerResources po = new JDBAutoLabellerResources(Common.selectedHostID, Common.sessionID);
									po.create(jTextFieldType.getText(), comboBox4jGroup.getSelectedItem().toString(), item);
								}
							}
							populateListAssignedResources(jTextFieldType.getText(), comboBox4jGroup.getSelectedItem().toString(), "");
							populateListAllResources();
							jButtonUpdate.setEnabled(true);
						}
					}
				});
				jButtonAssignResource.setBounds(202, 284, 26, 24);
				jDesktopPane1.add(jButtonAssignResource);

				JButton4j jButtonUnAssignResource = new JButton4j(Common.icon_arrow_right);
				jButtonUnAssignResource.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (listAssignedResources.getSelectedIndex() > -1)
						{
							for (int j = listAssignedResources.getMaxSelectionIndex(); j >= listAssignedResources.getMinSelectionIndex(); j--)
							{
								if (listAssignedResources.isSelectedIndex(j))
								{
									String item = (String) listAssignedResources.getModel().getElementAt(j);
									JDBAutoLabellerResources po = new JDBAutoLabellerResources(Common.selectedHostID, Common.sessionID);
									po.delete(jTextFieldType.getText(), comboBox4jGroup.getSelectedItem().toString(), item);
								}
							}
							populateListAssignedResources(jTextFieldType.getText(), comboBox4jGroup.getSelectedItem().toString(), "");
							populateListAllResources();
							jButtonUpdate.setEnabled(true);
						}
					}
				});
				jButtonUnAssignResource.setBounds(202, 316, 26, 23);
				jDesktopPane1.add(jButtonUnAssignResource);

				JLabel4j_std label4jValidateResources = new JLabel4j_std();
				label4jValidateResources.setText(lang.get("lbl_Validate_Process_Order_Resource"));
				label4jValidateResources.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4jValidateResources.setHorizontalAlignment(SwingConstants.RIGHT);
				label4jValidateResources.setBounds(1, 86, 179, 21);
				jDesktopPane1.add(label4jValidateResources);

				checkBox4jValidateResource.setSelected(false);
				checkBox4jValidateResource.setBounds(192, 83, 28, 23);
				checkBox4jValidateResource.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						autolab.setValidateResource(checkBox4jValidateResource.isSelected());
						jButtonUpdate.setEnabled(true);
					}
				});
				checkBox4jValidateResource.setSelected(autolab.isValidateResource());
				jDesktopPane1.add(checkBox4jValidateResource);

				checkBox4jValidateWorkstation.setSelected(autolab.isValidateWorkstation());
				checkBox4jValidateWorkstation.setBounds(517, 84, 28, 23);
				jDesktopPane1.add(checkBox4jValidateWorkstation);
				checkBox4jValidateWorkstation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						autolab.setValidateWorkstation(checkBox4jValidateWorkstation.isSelected());
						jButtonUpdate.setEnabled(true);
					}
				});

				jButtonUpdate.setEnabled(false);

				JLabel4j_title label4j_title = new JLabel4j_title();
				label4j_title.setText(lang.get("lbl_Assigned"));
				label4j_title.setBounds(30, 136, 197, 16);
				jDesktopPane1.add(label4j_title);

				JLabel4j_title label4j_title_1 = new JLabel4j_title();
				label4j_title_1.setText(lang.get("lbl_Unassigned"));
				label4j_title_1.setBounds(235, 136, 191, 16);
				jDesktopPane1.add(label4j_title_1);

				JScrollPane jScrollPaneUnAssignedPrinters = new JScrollPane();
				jScrollPaneUnAssignedPrinters.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				jScrollPaneUnAssignedPrinters.setBounds(830, 154, 170, 318);
				jDesktopPane1.add(jScrollPaneUnAssignedPrinters);

				jListUnAssignedPrinters = new JList4j<JDBListData>();
				jListUnAssignedPrinters.setFont(new Font("Monospaced", Font.PLAIN, 11));
				jListUnAssignedPrinters.setBackground(new Color(255, 240, 255));
				jListUnAssignedPrinters.setCellRenderer(Common.renderer_list_unassigned);
				jScrollPaneUnAssignedPrinters.setViewportView(jListUnAssignedPrinters);

				JScrollPane jScrollPaneAssignedPrinters = new JScrollPane();
				jScrollPaneAssignedPrinters.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				jScrollPaneAssignedPrinters.setBounds(630, 154, 170, 318);
				jDesktopPane1.add(jScrollPaneAssignedPrinters);

				jListAssignedPrinters = new JList4j<JDBListData>();
				jListAssignedPrinters.setFont(new Font("Monospaced", Font.PLAIN, 11));
				jListAssignedPrinters.setBackground(new Color(233, 255, 233));
				jListAssignedPrinters.setCellRenderer(Common.renderer_list_assigned);
				jScrollPaneAssignedPrinters.setViewportView(jListAssignedPrinters);

				JLabel4j_title label4j_title2 = new JLabel4j_title();
				label4j_title2.setText(lang.get("lbl_Assigned"));
				label4j_title2.setBounds(630, 134, 180, 18);
				jDesktopPane1.add(label4j_title2);

				JLabel4j_title label4j_title_21 = new JLabel4j_title();
				label4j_title_21.setText(lang.get("lbl_Unassigned"));
				label4j_title_21.setBounds(830, 131, 180, 21);
				jDesktopPane1.add(label4j_title_21);
				jButtonAssignPrinter.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (jListUnAssignedPrinters.getSelectedIndex() > -1)
						{
							JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);
							for (int j = jListUnAssignedPrinters.getMaxSelectionIndex(); j >= jListUnAssignedPrinters.getMinSelectionIndex(); j--)
							{
								if (jListUnAssignedPrinters.isSelectedIndex(j))
								{
									JDBListData item = (JDBListData) jListUnAssignedPrinters.getModel().getElementAt(j);

									if (plm.addPrintertoLine(selectedLine, selectedGroup, item.toString()))
									{
										addToPrinterList(assignedList, item, false);
									}
								}
							}

							for (int j = jListUnAssignedPrinters.getMaxSelectionIndex(); j >= jListUnAssignedPrinters.getMinSelectionIndex(); j--)
							{
								if (jListUnAssignedPrinters.isSelectedIndex(j))
								{
									Object item = jListUnAssignedPrinters.getModel().getElementAt(j);

									removeFromPrinterList(unassignedList, item);

								}
							}

							refreshJList(jListAssignedPrinters, assignedModel, assignedList);
							refreshJList(jListUnAssignedPrinters, unassignedModel, unassignedList);

							setButtonState();
						}

					}
				});

				jButtonAssignPrinter.setBounds(802, 284, 25, 25);
				jDesktopPane1.add(jButtonAssignPrinter);
				jButtonUnAssignPrinter.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (jListAssignedPrinters.getSelectedIndex() > -1)
						{
							JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);
							for (int j = jListAssignedPrinters.getMaxSelectionIndex(); j >= jListAssignedPrinters.getMinSelectionIndex(); j--)
							{
								if (jListAssignedPrinters.isSelectedIndex(j))
								{
									JDBListData item = (JDBListData) jListAssignedPrinters.getModel().getElementAt(j);

									addToPrinterList(unassignedList, item, true);
								}
							}

							for (int j = jListAssignedPrinters.getMaxSelectionIndex(); j >= jListAssignedPrinters.getMinSelectionIndex(); j--)
							{
								if (jListAssignedPrinters.isSelectedIndex(j))
								{
									Object item = jListAssignedPrinters.getModel().getElementAt(j);

									if (plm.removePrinterfromLine(selectedLine, selectedGroup, item.toString()))
									{
										removeFromPrinterList(assignedList, item);
									}
								}
							}
							refreshJList(jListUnAssignedPrinters, unassignedModel, unassignedList);
							refreshJList(jListAssignedPrinters, assignedModel, assignedList);

							setButtonState();
						}
					}
				});

				jButtonUnAssignPrinter.setBounds(802, 313, 25, 25);
				jDesktopPane1.add(jButtonUnAssignPrinter);

				JButton4j jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
				jButtonRefresh.setText(lang.get("btn_Refresh"));
				jButtonRefresh.setBounds(401, 501, 112, 32);
				jDesktopPane1.add(jButtonRefresh);

				JScrollPane scrollPaneWorkstations = new JScrollPane();
				scrollPaneWorkstations.setBounds(430, 154, 167, 288);
				jDesktopPane1.add(scrollPaneWorkstations);

				listWorkstations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPaneWorkstations.setViewportView(listWorkstations);

				JPanel panelResources = new JPanel();
				panelResources.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Resources", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
				panelResources.setBounds(16, 119, 400, 370);
				jDesktopPane1.add(panelResources);
				panelResources.setLayout(null);

				JPanel panel = new JPanel();
				panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Printers", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
				panel.setBounds(614, 119, 396, 370);
				jDesktopPane1.add(panel);
				panel.setLayout(null);

				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Workstations", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
				panel_1.setBounds(417, 119, 195, 370);
				jDesktopPane1.add(panel_1);
				panel_1.setLayout(null);

				JButton4j jButtonAddWorkstation = new JButton4j(Common.icon_add);
				jButtonAddWorkstation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						addWorkstationRecord();
					}
				});
				jButtonAddWorkstation.setBounds(13, 326, 25, 25);
				panel_1.add(jButtonAddWorkstation);

				JButton4j jButtonDeleteWorkstation = new JButton4j(Common.icon_delete);
				jButtonDeleteWorkstation.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteWorkstationRecord();
					}
				});
				jButtonDeleteWorkstation.setBounds(42, 326, 25, 25);
				panel_1.add(jButtonDeleteWorkstation);

				JLabel4j_std label4jValidateWorkstations = new JLabel4j_std();
				label4jValidateWorkstations.setText(lang.get("lbl_Validate_Workstation_ID"));
				label4jValidateWorkstations.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4jValidateWorkstations.setHorizontalAlignment(SwingConstants.RIGHT);
				label4jValidateWorkstations.setBounds(240, 86, 270, 21);
				jDesktopPane1.add(label4jValidateWorkstations);
				
				jTextFieldPrefixCode = new JTextField4j(4);
				jTextFieldPrefixCode.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});


				jTextFieldPrefixCode.setFocusCycleRoot(true);
				jTextFieldPrefixCode.setCaretPosition(0);
				jTextFieldPrefixCode.setBounds(928, 17, 72, 21);
				jDesktopPane1.add(jTextFieldPrefixCode);
				
				JLabel4j_std jLabelPrexfixCode = new JLabel4j_std();
				jLabelPrexfixCode.setText(lang.get("lbl_Prefix_Code"));
				jLabelPrexfixCode.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabelPrexfixCode.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabelPrexfixCode.setBounds(793, 17, 120, 21);
				jDesktopPane1.add(jLabelPrexfixCode);


			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void deleteWorkstationRecord()
	{
		if (listWorkstations.isSelectionEmpty() == false)
		{
			String item = ((String) listWorkstations.getSelectedValue()).toString();
			int n = JOptionPane.showConfirmDialog(this, lang.get("btn_Delete") + " " + lang.get("lbl_Workstation") + " [" + item + "]", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{

				JDBWorkstationLineMembership po = new JDBWorkstationLineMembership(Common.selectedHostID, Common.sessionID);
				po.delete(selectedLine, selectedGroup, item);
				populateListWorkstations();

			}

		}
	}

	private void refreshJList(JList4j<JDBListData> jlist, DefaultComboBoxModel<JDBListData> comboboxmodel, LinkedList<JDBListData> linkedlist)
	{
		comboboxmodel.removeAllElements();

		for (int j = 0; j < linkedlist.size(); j++)
		{
			comboboxmodel.addElement(linkedlist.get(j));
		}
		ListModel<JDBListData> jList1Model = comboboxmodel;

		jlist.setModel(jList1Model);
	}

	public void setButtonState()
	{

		jButtonUnAssignPrinter.setEnabled(false);
		jButtonAssignPrinter.setEnabled(false);

		if (unassignedList.size() > 0)
		{
			jButtonAssignPrinter.setEnabled(true);
		}

		if (assignedList.size() > 0)
		{
			jButtonUnAssignPrinter.setEnabled(true);
		}

	}

	private void addWorkstationRecord()
	{
		JDBWorkstationLineMembership u = new JDBWorkstationLineMembership(Common.selectedHostID, Common.sessionID);
		String lworkstation_id = "";
		lworkstation_id = JOptionPane.showInputDialog(this, lang.get("btn_Add") + " " + lang.get("lbl_Workstation"));
		if (lworkstation_id != null)
		{
			if (lworkstation_id.equals("") == false)
			{
				lworkstation_id = lworkstation_id.toUpperCase();

				u.setWorkstationId(lworkstation_id);

				if (u.isWorkstationAssignedToLine())
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(this, "Workstation ID already Assigned", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
				} else
				{
					u.create(selectedLine, selectedGroup, lworkstation_id);
					populateListWorkstations();
				}
			}
		}
	}

	private void populateListAllResources()
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempGroupList = po.getResourceList(selectedLine, selectedGroup);
		int sel = -1;
		for (int j = 0; j < tempGroupList.size(); j++)
		{
			defComboBoxMod.addElement(tempGroupList.get(j));

		}

		ListModel<String> jList1Model = defComboBoxMod;
		listAllResources.setModel(jList1Model);
		listAllResources.setSelectedIndex(sel);
		listAllResources.setCellRenderer(Common.renderer_list);
		listAllResources.ensureIndexIsVisible(sel);
	}

	private void populateListWorkstations()
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBWorkstationLineMembership po = new JDBWorkstationLineMembership(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempGroupList = po.getWorkstationsAssignedtoLine(selectedLine, selectedGroup);
		int sel = -1;
		for (int j = 0; j < tempGroupList.size(); j++)
		{
			defComboBoxMod.addElement(tempGroupList.get(j));

		}

		ListModel<String> jList1Model = defComboBoxMod;
		listWorkstations.setModel(jList1Model);
		listAllResources.setSelectedIndex(sel);
		listAllResources.setCellRenderer(Common.renderer_list);
		listAllResources.ensureIndexIsVisible(sel);
	}

	private void populateListAssignedResources(String line, String group, String defaultitem)
	{
		DefaultComboBoxModel<String> defComboBoxMod = new DefaultComboBoxModel<String>();

		JDBAutoLabellerResources po = new JDBAutoLabellerResources(Common.selectedHostID, Common.sessionID);
		LinkedList<String> tempGroupList = po.getRequiredResourceList(line, comboBox4jGroup.getSelectedItem().toString());
		int sel = -1;
		for (int j = 0; j < tempGroupList.size(); j++)
		{
			defComboBoxMod.addElement(tempGroupList.get(j));
			if (tempGroupList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<String> jList1Model = defComboBoxMod;
		listAssignedResources.setModel(jList1Model);
		listAssignedResources.setSelectedIndex(sel);
		listAssignedResources.setCellRenderer(Common.renderer_list);
		listAssignedResources.ensureIndexIsVisible(sel);
	}

	private void populateUnAssignedPrinterList()
	{
		unassignedModel.removeAllElements();

		JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);

		unassignedList = plm.getPrintersNotAssignedtoLine(selectedLine, selectedGroup);

		if (unassignedList.size() > 0)
		{
			for (int j = 0; j < unassignedList.size(); j++)
			{
				unassignedModel.addElement(unassignedList.get(j));
			}
			jButtonAssignPrinter.setEnabled(true);
			jButtonUnAssignPrinter.setEnabled(true);
		} else
		{
			jButtonAssignPrinter.setEnabled(false);
			jButtonUnAssignPrinter.setEnabled(false);
		}

		ListModel<JDBListData> jList1Model = unassignedModel;

		jListUnAssignedPrinters.setModel(jList1Model);
	}

	private void populateAssignedPrinterList()
	{
		assignedModel.removeAllElements();

		JDBPrinterLineMembership plm = new JDBPrinterLineMembership(Common.selectedHostID, Common.sessionID);

		assignedList = plm.getPrintersAssignedtoLine(selectedLine, selectedGroup);

		if (assignedList.size() > 0)
		{
			for (int j = 0; j < assignedList.size(); j++)
			{
				assignedModel.addElement(assignedList.get(j));
			}
		}

		ListModel<JDBListData> jList1Model = assignedModel;

		jListAssignedPrinters.setModel(jList1Model);
	}

	public void addToPrinterList(LinkedList<JDBListData> list, JDBListData newValue, boolean sort)
	{
		list.add(newValue);
		if (sort == true)
			Collections.sort(list);
	}

	public void removeFromPrinterList(LinkedList<JDBListData> list, Object oldValue)
	{
		list.remove(list.indexOf(oldValue));
	}
}
