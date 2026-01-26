package com.commander4j.sys;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameModuleProperties.java
 *
 * Package Name : com.commander4j.sys
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JFileFilterExecs;
import com.commander4j.util.JFileFilterImages;
import com.commander4j.util.JFileFilterLabels;
import com.commander4j.util.JFileFilterReports;
import com.commander4j.util.JFileFilterTXT;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameModuleProperties class allows a user to update a record in
 * the SYS_MODULES table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameModuleProperties.jpg" >
 *
 * @see com.commander4j.db.JDBModule JDBModule
 * @see com.commander4j.sys.JInternalFrameModuleAdmin JInternalFrameModuleAdmin
 * @see com.commander4j.sys.JInternalFrameModuleGroups
 *      JInternalFrameModuleGroups
 */

public class JInternalFrameModuleProperties extends javax.swing.JInternalFrame
{
	private JButton4j jButtonAlternative;
	private JButton4j jButtonAutoLabelCommandFileChooser = new JButton4j();
	private JButton4j jButtonAutoLabelLabelDirChooser = new JButton4j();
	private JButton4j jButtonClose;
	private JButton4j jButtonExecDirChooser;
	private JButton4j jButtonExecDirChooser_1;
	private JButton4j jButtonExecFileChooser;
	private JButton4j jButtonHelp;
	private JButton4j jButtonIconFileChooser;
	private JButton4j jButtonIconPreview;
	private JButton4j jButtonReportFileChooser;
	private JButton4j jButtonUpdate;
	private JCheckBox4j chckbxEnableDirectPrint = new JCheckBox4j("");
	private JCheckBox4j jCheckBoxDesktop;
	private JCheckBox4j jCheckBoxPrintDialog;
	private JCheckBox4j jCheckBoxPrintPreview;
	private JCheckBox4j jCheckBoxScanner;
	private JComboBox4j<String> comboBox4jGroup = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxDPI = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxLanguage = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxPrinterType = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxReportType;
	private JComboBox4j<String> jComboBoxType;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule module = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Description;
	private JLabel4j_std jLabel_Desktop;
	private JLabel4j_std jLabel_ExecDirectory;
	private JLabel4j_std jLabel_ExecFilename;
	private JLabel4j_std jLabel_IconFilename;
	private JLabel4j_std jLabel_Mnemonic;
	private JLabel4j_std jLabel_ModuleHelp;
	private JLabel4j_std jLabel_ModuleID;
	private JLabel4j_std jLabel_ModuleType;
	private JLabel4j_std jLabel_PrintCopies;
	private JLabel4j_std jLabel_PrintDialog;
	private JLabel4j_std jLabel_PrintPreview;
	private JLabel4j_std jLabel_ReportFilename;
	private JLabel4j_std jLabel_ReportType;
	private JLabel4j_std jLabel_ResourceKey;
	private JLabel4j_std jLabel_Scanner;
	private JSpinner4j jSpinnerPrintCopies;
	private JTextField4j jTextFieldAutoLabelCommandFilename = new JTextField4j(JDBModule.field_autolabeller_command_file);
	private JTextField4j jTextFieldAutoLabelLabelFilename = new JTextField4j(JDBModule.field_autolabeller_label_file);
	private JTextField4j jTextFieldExecDir;
	private JTextField4j jTextFieldExecFilename;
	private JTextField4j jTextFieldHelpsetid;
	private JTextField4j jTextFieldIconFilename;
	private JTextField4j jTextFieldMnemonic;
	private JTextField4j jTextFieldModuleId;
	private JTextField4j jTextFieldReportFilename;
	private JTextField4j jTextFieldResourceKey;
	private JTextField4j textFieldTranslatedDescripton;
	private Object current_type = new Object();
	private Object new_type = new Object();
	private String lmodule_id;
	private static final long serialVersionUID = 1;

	public JInternalFrameModuleProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MODULE_EDIT"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldResourceKey.requestFocus();
				jTextFieldResourceKey.setCaretPosition(jTextFieldResourceKey.getText().length());

			}
		});
	}

	public JInternalFrameModuleProperties(String moduleid)
	{

		this();
		lmodule_id = moduleid;
		jTextFieldModuleId.setText(lmodule_id);
		setTitle(getTitle() + " - " + lmodule_id);

		module.setModuleId(lmodule_id);
		module.getModuleProperties();

		jTextFieldResourceKey.setText(module.getResourceKey());
		textFieldTranslatedDescripton.setText(module.getDescription());

		jTextFieldIconFilename.setText(module.getIconFilename());
		jTextFieldReportFilename.setText(module.getReportFilename());
		jTextFieldExecFilename.setText(module.getExecFilename());
		jTextFieldExecDir.setText(module.getExecDir());
		jTextFieldHelpsetid.setText(module.getHelpSetID());

		jComboBoxType.setSelectedItem(module.getType());
		comboBoxReportType.setSelectedItem(module.getReportType());

		if (module.isDKModule())
			jCheckBoxDesktop.setSelected(true);
		else
			jCheckBoxDesktop.setSelected(false);

		if (module.isRFModule())
			jCheckBoxScanner.setSelected(true);
		else
			jCheckBoxScanner.setSelected(false);

		if (module.isPrintPreview())
			jCheckBoxPrintPreview.setSelected(true);
		else
			jCheckBoxPrintPreview.setSelected(false);

		if (module.isPrintDialog())
			jCheckBoxPrintDialog.setSelected(true);
		else
			jCheckBoxPrintDialog.setSelected(false);

		jSpinnerPrintCopies.setValue(module.getPrintCopies());

		jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));

		jTextFieldAutoLabelCommandFilename.setText(module.getLabelCommandFilename());
		jTextFieldAutoLabelLabelFilename.setText(module.getAutoLabelLabelFilename());

		comboBoxPrinterType.setSelectedItem(module.getPrinterType());
		comboBox4jGroup.setSelectedItem(module.getGroupID());
		comboBoxLanguage.setSelectedItem(module.getLanguage());
		comboBoxDPI.setSelectedItem(module.getDPI());
		chckbxEnableDirectPrint.setSelected(module.isDirectPrintEnabled());

		reset_changes();
	}

	private void reset_changes()
	{
		current_type = jComboBoxType.getSelectedItem();
		jButtonUpdate.setEnabled(false);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(573, 396));
			this.setBounds(0, 0, 698, 646);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Module Properties");
			this.setIconifiable(true);
			getContentPane().setLayout(null);

			jDesktopPane1 = new JDesktopPane4j();
			jDesktopPane1.setBounds(0, 0, 690, 616);

			this.getContentPane().add(jDesktopPane1);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(577, 280));
			jDesktopPane1.setLayout(null);

			textFieldTranslatedDescripton = new JTextField4j();
			textFieldTranslatedDescripton.setEnabled(false);
			textFieldTranslatedDescripton.setEditable(false);
			textFieldTranslatedDescripton.setFocusCycleRoot(true);
			textFieldTranslatedDescripton.setCaretPosition(0);
			textFieldTranslatedDescripton.setBounds(164, 65, 280, 22);
			jDesktopPane1.add(textFieldTranslatedDescripton);

			jLabel_ModuleID = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ModuleID);
			jLabel_ModuleID.setText(lang.get("lbl_Module_ID"));
			jLabel_ModuleID.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_ModuleID.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_ModuleID.setBounds(0, 7, 152, 22);

			jLabel_ReportType = new JLabel4j_std();
			jLabel_ReportType.setText(lang.get("lbl_Module_Report_Type"));
			jLabel_ReportType.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ReportType.setBounds(0, 210, 152, 22);
			jDesktopPane1.add(jLabel_ReportType);

			jTextFieldModuleId = new JTextField4j();
			jDesktopPane1.add(jTextFieldModuleId);
			jTextFieldModuleId.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldModuleId.setEditable(false);
			jTextFieldModuleId.setBounds(164, 7, 280, 22);
			jTextFieldModuleId.setEnabled(false);

			jTextFieldResourceKey = new JTextField4j(JDBModule.field_resource_key);
			jDesktopPane1.add(jTextFieldResourceKey);
			jTextFieldResourceKey.setFocusCycleRoot(true);
			jTextFieldResourceKey.setBounds(164, 36, 280, 22);
			jTextFieldResourceKey.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jButtonUpdate = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonUpdate);
			jButtonUpdate.setEnabled(false);
			jButtonUpdate.setText(lang.get("btn_Save"));
			jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonUpdate.setMnemonic(lang.getMnemonicChar());
			jButtonUpdate.setBounds(125, 564, 112, 32);
			jButtonUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					module.setResourceKey(jTextFieldResourceKey.getText());

					module.setIconFilename(jTextFieldIconFilename.getText());
					module.setHelpSetID(jTextFieldHelpsetid.getText());

					module.setType((String) jComboBoxType.getSelectedItem());
					module.setReportType((String) comboBoxReportType.getSelectedItem());

					if (jCheckBoxDesktop.isSelected())
						module.setDKActive("Y");
					else
						module.setDKActive("N");

					if (jCheckBoxScanner.isSelected())
						module.setRFActive("Y");
					else
						module.setRFActive("N");

					if (jCheckBoxPrintPreview.isSelected())
						module.setPrintPreview("Y");
					else
						module.setPrintPreview("N");

					if (jCheckBoxPrintDialog.isSelected())
						module.setPrintDialog("Y");
					else
						module.setPrintDialog("N");

					module.setPrintCopies(Integer.valueOf(jSpinnerPrintCopies.getValue().toString()));

					module.setReportFilename(jTextFieldReportFilename.getText());
					module.setExecFilename(jTextFieldExecFilename.getText());
					module.setExecDir(jTextFieldExecDir.getText());

					module.setAutoLabelCommandFilename(jTextFieldAutoLabelCommandFilename.getText());
					module.setAutoLabelLabelFilename(jTextFieldAutoLabelLabelFilename.getText());

					module.setPrinterType(comboBoxPrinterType.getSelectedItem().toString());
					module.setLanguage(JUtility.replaceNullStringwithBlank(comboBoxLanguage.getSelectedItem().toString()));
					module.setGroupID(comboBox4jGroup.getSelectedItem().toString());
					module.setDPI(comboBoxDPI.getSelectedItem().toString());
					module.setEnableDirectPrint(chckbxEnableDirectPrint.isSelected());

					if (module.update() == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(null, module.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
					}

					jButtonUpdate.setEnabled(false);
				}
			});

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(467, 564, 112, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_ModuleType = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ModuleType);
			jLabel_ModuleType.setText(lang.get("lbl_Module_Type"));
			jLabel_ModuleType.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_ModuleType.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_ModuleType.setBounds(0, 94, 152, 22);

			jLabel_Desktop = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Desktop);
			jLabel_Desktop.setText(lang.get("lbl_Module_Desktop"));
			jLabel_Desktop.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Desktop.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Desktop.setBounds(0, 123, 152, 22);

			jLabel_Description = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Description);
			jLabel_Description.setText(lang.get("lbl_Description"));
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Description.setBounds(0, 65, 152, 22);

			jCheckBoxPrintPreview = new JCheckBox4j();
			jCheckBoxPrintPreview.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jCheckBoxPrintPreview.setSelected(true);
			jCheckBoxPrintPreview.setBounds(639, 181, 21, 22);
			jDesktopPane1.add(jCheckBoxPrintPreview);

			comboBoxReportType = new JComboBox4j<String>();
			comboBoxReportType.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					jButtonUpdate.setEnabled(true);
					if (comboBoxReportType.getSelectedItem().equals("Label"))
					{
						jCheckBoxPrintPreview.setSelected(true);
						comboBox4jGroup.setEnabled(true);
						comboBoxPrinterType.setEnabled(true);
						comboBoxLanguage.setEnabled(true);
						comboBoxDPI.setEnabled(true);
					}
					else
					{
						comboBox4jGroup.setEnabled(false);
						comboBoxPrinterType.setEnabled(false);
						comboBoxLanguage.setEnabled(false);
						comboBoxDPI.setEnabled(false);
					}
				}
			});
			comboBoxReportType.setBounds(164, 210, 180, 22);
			comboBoxReportType.addItem("");
			comboBoxReportType.addItem("Standard");
			comboBoxReportType.addItem("Label");
			jDesktopPane1.add(comboBoxReportType);

			jComboBoxType = new JComboBox4j<String>();
			jDesktopPane1.add(jComboBoxType);
			jComboBoxType.setEnabled(true);
			jComboBoxType.setEditable(false);
			jComboBoxType.setLightWeightPopupEnabled(true);
			jComboBoxType.setIgnoreRepaint(false);
			jComboBoxType.setBounds(164, 94, 180, 22);
			jComboBoxType.addItem("EXEC");
			jComboBoxType.addItem("FORM");
			jComboBoxType.addItem("FUNCTION");
			jComboBoxType.addItem("MENU");
			jComboBoxType.addItem("REPORT");
			jComboBoxType.addItem("USER");
			jComboBoxType.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					new_type = jComboBoxType.getSelectedItem();
					if (new_type != null)
					{
						if (new_type.equals(current_type) != true)
						{
							// module_updated = true;
							jButtonUpdate.setEnabled(true);
							jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
						}

						if (new_type.equals("REPORT"))
						{
							jButtonAlternative.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MODULE_ALTERNATE"));
							comboBoxReportType.setEnabled(true);
						}
						else
						{
							jButtonAlternative.setEnabled(false);
							comboBoxReportType.setEnabled(false);
						}

						if (new_type.equals("REPORT") || new_type.equals("USER"))
						{
							jTextFieldReportFilename.setEnabled(true);
							jButtonReportFileChooser.setEnabled(true);
							jCheckBoxPrintPreview.setEnabled(true);
							jCheckBoxPrintDialog.setEnabled(true);
							jSpinnerPrintCopies.setEnabled(true);
							comboBoxReportType.setEnabled(true);
							jTextFieldAutoLabelCommandFilename.setEnabled(true);
							jTextFieldAutoLabelLabelFilename.setEnabled(true);
							jButtonAutoLabelCommandFileChooser.setEnabled(true);
							jButtonAutoLabelLabelDirChooser.setEnabled(true);
						}
						else
						{
							jTextFieldReportFilename.setEnabled(false);
							jButtonReportFileChooser.setEnabled(false);
							jCheckBoxPrintPreview.setEnabled(false);
							jCheckBoxPrintDialog.setEnabled(false);
							jSpinnerPrintCopies.setEnabled(false);
							comboBoxReportType.setEnabled(false);
							jTextFieldAutoLabelCommandFilename.setEnabled(false);
							jTextFieldAutoLabelLabelFilename.setEnabled(false);
							jButtonAutoLabelCommandFileChooser.setEnabled(false);
							jButtonAutoLabelLabelDirChooser.setEnabled(false);
						}

						if (new_type.equals("EXEC"))
						{
							jTextFieldExecFilename.setEnabled(true);
							jButtonExecFileChooser.setEnabled(true);
							jTextFieldExecDir.setEnabled(true);
							jButtonExecDirChooser.setEnabled(true);
						}
						else
						{
							jTextFieldExecFilename.setEnabled(false);
							jButtonExecFileChooser.setEnabled(false);
							jTextFieldExecDir.setEnabled(false);
							jButtonExecDirChooser.setEnabled(false);
						}
					}
				}
			});

			jCheckBoxScanner = new JCheckBox4j();
			jDesktopPane1.add(jCheckBoxScanner);
			jCheckBoxScanner.setSelected(true);
			jCheckBoxScanner.setBounds(164, 152, 21, 22);
			jCheckBoxScanner.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					// module_updated = true;
					jButtonUpdate.setEnabled(true);
				}
			});

			jCheckBoxDesktop = new JCheckBox4j();
			jDesktopPane1.add(jCheckBoxDesktop);
			jCheckBoxDesktop.setSelected(true);
			jCheckBoxDesktop.setBounds(164, 123, 21, 22);
			jCheckBoxDesktop.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					// module_updated = true;
					jButtonUpdate.setEnabled(true);
				}
			});

			jLabel_Scanner = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Scanner);
			jLabel_Scanner.setText(lang.get("lbl_Module_Scanner"));
			jLabel_Scanner.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Scanner.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Scanner.setBounds(0, 152, 152, 22);

			jLabel_Mnemonic = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Mnemonic);
			jLabel_Mnemonic.setText(lang.get("lbl_Language_Mnemonic"));
			jLabel_Mnemonic.setBounds(462, 65, 151, 22);
			jLabel_Mnemonic.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Mnemonic.setHorizontalTextPosition(SwingConstants.RIGHT);

			jTextFieldMnemonic = new JTextField4j(JDBModule.field_mneumonic);
			jTextFieldMnemonic.setEnabled(false);
			jTextFieldMnemonic.setEditable(false);

			jDesktopPane1.add(jTextFieldMnemonic);
			jTextFieldMnemonic.setText("");

			jTextFieldMnemonic.setBounds(618, 65, 42, 22);
			jTextFieldMnemonic.setToolTipText("Character to underline");
			jTextFieldMnemonic.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jLabel_ExecDirectory = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ExecDirectory);
			jLabel_ExecDirectory.setText(lang.get("lbl_Module_Executable_Directory"));
			jLabel_ExecDirectory.setBounds(0, 413, 152, 22);
			jLabel_ExecDirectory.setHorizontalAlignment(SwingConstants.TRAILING);

			jTextFieldIconFilename = new JTextField4j(JDBModule.field_icon_filename);
			jDesktopPane1.add(jTextFieldIconFilename);
			jTextFieldIconFilename.setBounds(164, 181, 220, 22);
			jTextFieldIconFilename.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
				}

				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jButtonIconFileChooser = new JButton4j();
			jDesktopPane1.add(jButtonIconFileChooser);
			jButtonIconFileChooser.setText("..");
			jButtonIconFileChooser.setBounds(383, 181, 22, 22);
			jButtonIconFileChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					JFileChooser loadIco = new JFileChooser();

					try
					{
						File f = new File(new File("./images").getCanonicalPath());
						loadIco.setCurrentDirectory(f);
						loadIco.addChoosableFileFilter(new JFileFilterImages());
						loadIco.setAcceptAllFileFilterUsed(false);
						loadIco.setSelectedFile(new File(jTextFieldIconFilename.getText()));
					}
					catch (Exception e)
					{
					}

					if (loadIco.showOpenDialog(jButtonIconFileChooser) == JFileChooser.APPROVE_OPTION)
					{
						File selectedFile;
						selectedFile = loadIco.getSelectedFile();

						if (selectedFile != null)
						{
							if (jTextFieldIconFilename.getText().compareTo(selectedFile.getName()) != 0)
							{
								jTextFieldIconFilename.setText(selectedFile.getName());
								jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
								jButtonUpdate.setEnabled(true);
							}
						}
					}
				}
			});

			jButtonIconPreview = new JButton4j();
			jDesktopPane1.add(jButtonIconPreview);
			jButtonIconPreview.setBounds(292, 177, 23, 22);
			jButtonIconPreview.setBorderPainted(false);
			jButtonIconPreview.setContentAreaFilled(false);
			jButtonIconPreview.setRolloverEnabled(false);
			jButtonIconPreview.setRequestFocusEnabled(false);

			jTextFieldHelpsetid = new JTextField4j(JDBModule.field_helpset_id);
			jDesktopPane1.add(jTextFieldHelpsetid);
			jTextFieldHelpsetid.setBounds(164, 442, 475, 22);
			jTextFieldHelpsetid.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{

					jButtonUpdate.setEnabled(true);
				}
			});

			jLabel_ModuleHelp = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ModuleHelp);
			jLabel_ModuleHelp.setText(lang.get("lbl_Module_Help"));
			jLabel_ModuleHelp.setBounds(0, 442, 152, 22);
			jLabel_ModuleHelp.setHorizontalAlignment(SwingConstants.TRAILING);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setBounds(353, 564, 112, 32);
			jButtonHelp.setMnemonic(lang.getMnemonicChar());

			jTextFieldReportFilename = new JTextField4j(JDBModule.field_report_filename);
			jDesktopPane1.add(jTextFieldReportFilename);
			jTextFieldReportFilename.setBounds(164, 355, 303, 22);
			jTextFieldReportFilename.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}

				public void keyReleased(KeyEvent evt)
				{
					jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
				}
			});

			jLabel_IconFilename = new JLabel4j_std();
			jDesktopPane1.add(jLabel_IconFilename);
			jLabel_IconFilename.setText(lang.get("lbl_Module_Icon_Filename"));
			jLabel_IconFilename.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_IconFilename.setBounds(0, 181, 152, 22);

			jButtonReportFileChooser = new JButton4j();
			jDesktopPane1.add(jButtonReportFileChooser);
			jButtonReportFileChooser.setText("..");
			jButtonReportFileChooser.setBounds(467, 355, 22, 22);
			jButtonReportFileChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					JFileChooser loadRpt = new JFileChooser();
					loadRpt.setAcceptAllFileFilterUsed(false);
					try
					{
						String path = "";
						if (((String) comboBoxReportType.getSelectedItem()).equals("Standard"))
						{
							path = "./reports";
						}
						else
						{
							path = "./labels";
						}
						File f = new File(new File(path).getCanonicalPath());
						loadRpt.setCurrentDirectory(f);

						if (((String) comboBoxReportType.getSelectedItem()).equals("Standard"))
						{
							loadRpt.addChoosableFileFilter(new JFileFilterReports());
						}
						else
						{
							loadRpt.addChoosableFileFilter(new JFileFilterLabels());
						}

						loadRpt.setSelectedFile(new File(jTextFieldReportFilename.getText()));
					}
					catch (Exception e)
					{
					}

					if (loadRpt.showOpenDialog(jButtonReportFileChooser) == JFileChooser.APPROVE_OPTION)
					{
						File selectedFile;
						selectedFile = loadRpt.getSelectedFile();

						if (selectedFile != null)
						{
							if (jTextFieldReportFilename.getText().compareTo(selectedFile.getName()) != 0)
							{
								jTextFieldReportFilename.setText(selectedFile.getName());
								jButtonUpdate.setEnabled(true);
							}
						}
					}
				}
			});

			jTextFieldExecDir = new JTextField4j();
			jDesktopPane1.add(jTextFieldExecDir);
			jTextFieldExecDir.setBounds(164, 413, 475, 22);
			jTextFieldExecDir.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}

				public void keyReleased(KeyEvent evt)
				{
					jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
				}
			});

			jTextFieldExecFilename = new JTextField4j(JDBModule.field_exec_dir);
			jDesktopPane1.add(jTextFieldExecFilename);
			jTextFieldExecFilename.setBounds(164, 384, 475, 22);
			jTextFieldExecFilename.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonUpdate.setEnabled(true);
				}

				public void keyReleased(KeyEvent evt)
				{
					jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
				}
			});

			jLabel_ReportFilename = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ReportFilename);
			jLabel_ReportFilename.setText(lang.get("lbl_Module_Report_Filename"));
			jLabel_ReportFilename.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ReportFilename.setBounds(0, 355, 152, 22);

			jLabel_ExecFilename = new JLabel4j_std();
			jDesktopPane1.add(jLabel_ExecFilename);
			jLabel_ExecFilename.setText(lang.get("lbl_Module_Executable_Filename"));
			jLabel_ExecFilename.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_ExecFilename.setBounds(0, 384, 152, 22);

			jButtonExecDirChooser = new JButton4j();
			jDesktopPane1.add(jButtonExecDirChooser);
			jButtonExecDirChooser.setText("..");
			jButtonExecDirChooser.setBounds(638, 413, 22, 22);
			jButtonExecDirChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					JFileChooser loadDir = new JFileChooser();

					try
					{
						File f = new File(new File("").getCanonicalPath());
						loadDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						loadDir.setCurrentDirectory(f);
						loadDir.setSelectedFile(new File(jTextFieldExecDir.getText()));

						if (loadDir.showOpenDialog(jButtonExecDirChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = loadDir.getSelectedFile();

							if (selectedFile != null)
							{
								if (jTextFieldExecDir.getText().compareTo(selectedFile.getCanonicalPath()) != 0)
								{
									jTextFieldExecDir.setText(selectedFile.getCanonicalPath());
									jButtonUpdate.setEnabled(true);
								}
							}
						}

					}
					catch (Exception e)
					{
					}

				}
			});

			jButtonExecFileChooser = new JButton4j();
			jDesktopPane1.add(jButtonExecFileChooser);
			jButtonExecFileChooser.setText("..");
			jButtonExecFileChooser.setBounds(638, 384, 22, 22);
			jButtonExecFileChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					JFileChooser loadExec = new JFileChooser();

					try
					{
						File f = new File(new File("").getCanonicalPath());
						loadExec.setCurrentDirectory(f);
						loadExec.addChoosableFileFilter(new JFileFilterExecs());
						loadExec.setSelectedFile(new File(jTextFieldExecFilename.getText()));

						if (loadExec.showOpenDialog(jButtonExecFileChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = loadExec.getSelectedFile();

							if (selectedFile != null)
							{
								if (jTextFieldExecFilename.getText().compareTo(selectedFile.getName()) != 0)
								{
									jTextFieldExecFilename.setText(selectedFile.getCanonicalPath());
									jButtonUpdate.setEnabled(true);
								}
							}
						}

					}
					catch (Exception e)
					{
					}

				}
			});

			jButtonExecDirChooser_1 = new JButton4j();
			jButtonExecDirChooser_1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{

					JFileChooser loadWeb = new JFileChooser();

					try
					{
						File f = new File(new File("./help/html/commander4j").getCanonicalPath());
						loadWeb.setCurrentDirectory(f);

						loadWeb.setSelectedFile(new File(jTextFieldHelpsetid.getText()));

						if (loadWeb.showOpenDialog(jButtonExecFileChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = loadWeb.getSelectedFile();

							String fullpath = selectedFile.getCanonicalPath();
							fullpath = fullpath.replace(Common.base_dir, "{base_dir}");

							if (selectedFile != null)
							{
								if (jTextFieldHelpsetid.getText().compareTo(selectedFile.getName()) != 0)
								{
									jTextFieldHelpsetid.setText(fullpath);
									jButtonUpdate.setEnabled(true);
								}
							}
						}

					}
					catch (Exception ex)
					{
					}

				}
			});
			jButtonExecDirChooser_1.setText("..");
			jButtonExecDirChooser_1.setBounds(638, 442, 22, 22);
			jDesktopPane1.add(jButtonExecDirChooser_1);

			jCheckBoxPrintDialog = new JCheckBox4j();
			jCheckBoxPrintDialog.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jCheckBoxPrintDialog.setSelected(true);
			jCheckBoxPrintDialog.setBounds(639, 210, 21, 22);
			jDesktopPane1.add(jCheckBoxPrintDialog);

			jLabel_PrintPreview = new JLabel4j_std();
			jLabel_PrintPreview.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_PrintPreview.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_PrintPreview.setText(lang.get("lbl_Module_Print_Preview"));
			jLabel_PrintPreview.setBounds(488, 181, 141, 22);
			jDesktopPane1.add(jLabel_PrintPreview);

			jLabel_PrintDialog = new JLabel4j_std();
			jLabel_PrintDialog.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_PrintDialog.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_PrintDialog.setText(lang.get("lbl_Module_Print_Dialog"));
			jLabel_PrintDialog.setBounds(482, 210, 147, 22);
			jDesktopPane1.add(jLabel_PrintDialog);

			jLabel_PrintCopies = new JLabel4j_std();
			jLabel_PrintCopies.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_PrintCopies.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_PrintCopies.setText(lang.get("lbl_Module_Print_Copies"));
			jLabel_PrintCopies.setBounds(467, 355, 141, 22);
			jDesktopPane1.add(jLabel_PrintCopies);

			jSpinnerPrintCopies = new JSpinner4j();
			jSpinnerPrintCopies.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jSpinnerPrintCopies.setBounds(618, 355, 42, 22);
			jDesktopPane1.add(jSpinnerPrintCopies);

			jLabel_ResourceKey = new JLabel4j_std();
			jLabel_ResourceKey.setText(lang.get("lbl_Language_Key"));
			jLabel_ResourceKey.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_ResourceKey.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_ResourceKey.setBounds(0, 36, 152, 22);
			jDesktopPane1.add(jLabel_ResourceKey);

			JLabel4j_std jLabel_CommandFile = new JLabel4j_std();
			jLabel_CommandFile.setText(lang.get("lbl_Command_File"));
			jLabel_CommandFile.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_CommandFile.setBounds(0, 500, 152, 22);
			jDesktopPane1.add(jLabel_CommandFile);

			JLabel4j_std jLabel_LabelFile = new JLabel4j_std();
			jLabel_LabelFile.setText(lang.get("lbl_Label_File"));
			jLabel_LabelFile.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_LabelFile.setBounds(0, 529, 152, 22);
			jDesktopPane1.add(jLabel_LabelFile);
			jTextFieldAutoLabelCommandFilename.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jTextFieldAutoLabelCommandFilename.setBounds(164, 500, 220, 22);
			jDesktopPane1.add(jTextFieldAutoLabelCommandFilename);
			jTextFieldAutoLabelLabelFilename.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			jTextFieldAutoLabelLabelFilename.setBounds(164, 529, 220, 22);
			jDesktopPane1.add(jTextFieldAutoLabelLabelFilename);

			jButtonAutoLabelCommandFileChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JFileChooser loadCMD = new JFileChooser();

					try
					{
						String path = jTextFieldAutoLabelCommandFilename.getText();
						if (path.equals(""))
						{
							path = Common.auto_label_command;
						}
						File f = new File(new File(path).getCanonicalPath());
						loadCMD.setCurrentDirectory(f);
						loadCMD.setAcceptAllFileFilterUsed(false);
						loadCMD.addChoosableFileFilter(new JFileFilterTXT());
						loadCMD.setSelectedFile(new File(jTextFieldReportFilename.getText()));
					}
					catch (Exception e3)
					{
					}

					if (loadCMD.showOpenDialog(jButtonAutoLabelCommandFileChooser) == JFileChooser.APPROVE_OPTION)
					{
						File selectedFile;
						selectedFile = loadCMD.getSelectedFile();

						if (selectedFile != null)
						{
							if (jTextFieldAutoLabelCommandFilename.getText().compareTo(selectedFile.getName()) != 0)
							{
								jTextFieldAutoLabelCommandFilename.setText(selectedFile.getName());
								jButtonUpdate.setEnabled(true);
							}
						}
					}
				}
			});
			jButtonAutoLabelCommandFileChooser.setText("..");
			jButtonAutoLabelCommandFileChooser.setBounds(383, 500, 22, 22);
			jDesktopPane1.add(jButtonAutoLabelCommandFileChooser);

			jButtonAutoLabelLabelDirChooser.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{

					JFileChooser loadLab = new JFileChooser();

					try
					{
						String path = Common.auto_label_labels;
						File f = new File(new File(path).getCanonicalPath());
						loadLab.setCurrentDirectory(f);
						loadLab.setAcceptAllFileFilterUsed(false);
						loadLab.addChoosableFileFilter(new JFileFilterLabels());
						loadLab.setSelectedFile(new File(jTextFieldAutoLabelLabelFilename.getText()));

						if (loadLab.showOpenDialog(jButtonReportFileChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = loadLab.getSelectedFile();

							if (selectedFile != null)
							{
								if (jTextFieldAutoLabelLabelFilename.getText().compareTo(selectedFile.getName()) != 0)
								{
									jTextFieldAutoLabelLabelFilename.setText(selectedFile.getName());
									jButtonUpdate.setEnabled(true);
								}
							}
						}
					}
					catch (Exception e2)
					{
					}

				}
			});
			jButtonAutoLabelLabelDirChooser.setText("..");
			jButtonAutoLabelLabelDirChooser.setBounds(383, 529, 22, 22);
			jDesktopPane1.add(jButtonAutoLabelLabelDirChooser);

			JLabel4j_std jLabel_AutoLabeller = new JLabel4j_std();
			jLabel_AutoLabeller.setText(lang.get("lbl_Auto_Labeller"));
			jLabel_AutoLabeller.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel_AutoLabeller.setBounds(163, 471, 237, 22);
			jDesktopPane1.add(jLabel_AutoLabeller);

			jButtonAlternative = new JButton4j(Common.icon_alternative_16x16);
			jButtonAlternative.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchMenu.runDialog("FRM_ADMIN_MODULE_ALTERNATE", lmodule_id);
				}
			});
			jButtonAlternative.setText(lang.get("btn_Alternative"));
			jButtonAlternative.setMnemonic('A');
			jButtonAlternative.setEnabled(false);
			jButtonAlternative.setBounds(239, 564, 112, 32);
			jDesktopPane1.add(jButtonAlternative);

			JLabel4j_std jLabel_GroupID = new JLabel4j_std();
			jLabel_GroupID.setText(lang.get("lbl_Group_ID"));
			jLabel_GroupID.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_GroupID.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_GroupID.setBounds(31, 239, 125, 22);
			jDesktopPane1.add(jLabel_GroupID);
			comboBox4jGroup.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBox4jGroup.setModel(new DefaultComboBoxModel<String>(Common.printerGroup));
			comboBox4jGroup.setPreferredSize(new Dimension(40, 20));
			comboBox4jGroup.setFocusCycleRoot(true);
			comboBox4jGroup.setBounds(164, 239, 180, 22);
			jDesktopPane1.add(comboBox4jGroup);

			JLabel4j_std jLabel_PrinterType = new JLabel4j_std();
			jLabel_PrinterType.setText(lang.get("lbl_Printer_Type"));
			jLabel_PrinterType.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_PrinterType.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_PrinterType.setBounds(31, 268, 125, 22);
			jDesktopPane1.add(jLabel_PrinterType);
			comboBoxPrinterType.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBoxPrinterType.setModel(new DefaultComboBoxModel<String>(Common.printerTypes));
			comboBoxPrinterType.setPreferredSize(new Dimension(40, 20));
			comboBoxPrinterType.setFocusCycleRoot(true);
			comboBoxPrinterType.setBounds(164, 268, 180, 22);
			jDesktopPane1.add(comboBoxPrinterType);

			JLabel4j_std jLabel_LanguageID = new JLabel4j_std();
			jLabel_LanguageID.setText(lang.get("lbl_Language"));
			jLabel_LanguageID.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_LanguageID.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_LanguageID.setBounds(31, 297, 125, 22);
			jDesktopPane1.add(jLabel_LanguageID);
			comboBoxLanguage.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBoxLanguage.setModel(new DefaultComboBoxModel<String>(Common.printerLanguage));
			comboBoxLanguage.setPreferredSize(new Dimension(40, 20));
			comboBoxLanguage.setFocusCycleRoot(true);
			comboBoxLanguage.setBounds(164, 297, 180, 22);
			jDesktopPane1.add(comboBoxLanguage);

			JLabel4j_std jLabel_DPI = new JLabel4j_std();
			jLabel_DPI.setText("DPI");
			jLabel_DPI.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_DPI.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_DPI.setBounds(31, 326, 125, 22);
			jDesktopPane1.add(jLabel_DPI);
			comboBoxDPI.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			comboBoxDPI.setBounds(141, 145, 89, 22);
			comboBoxDPI.setModel(new DefaultComboBoxModel<String>(Common.printerDPI));
			comboBoxDPI.setPreferredSize(new Dimension(40, 20));
			comboBoxDPI.setFocusCycleRoot(true);
			comboBoxDPI.setBounds(163, 326, 89, 22);
			jDesktopPane1.add(comboBoxDPI);

			JLabel4j_std jLabel_DirectPrint = new JLabel4j_std();
			jLabel_DirectPrint.setText(lang.get("lbl_Direct_Print"));
			jLabel_DirectPrint.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_DirectPrint.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_DirectPrint.setBounds(505, 239, 125, 22);
			jDesktopPane1.add(jLabel_DirectPrint);
			chckbxEnableDirectPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});

			chckbxEnableDirectPrint.setBounds(638, 239, 22, 22);
			jDesktopPane1.add(chckbxEnableDirectPrint);

			postInitGUI();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void postInitGUI()
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		reset_changes();

	}
}
