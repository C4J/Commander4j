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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JDesktopPane;
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
 * The JInternalFrameModuleProperties class allows a user to update a record in the SYS_MODULES table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameModuleProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBModule JDBModule
 * @see com.commander4j.sys.JInternalFrameModuleAdmin JInternalFrameModuleAdmin
 * @see com.commander4j.sys.JInternalFrameModuleGroups JInternalFrameModuleGroups
 */

public class JInternalFrameModuleProperties extends javax.swing.JInternalFrame
{
	private JSpinner4j jSpinnerPrintCopies;
	private JLabel4j_std jLabel3_3;
	private JLabel4j_std jLabel3_2;
	private JLabel4j_std jLabel3_1;
	private JCheckBox4j jCheckBoxPrintDialog;
	private JCheckBox4j jCheckBoxPrintPreview;
	private JButton4j jButtonExecDirChooser_1;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JLabel4j_std jLabel4;
	private JLabel4j_std jLabel8;
	private JButton4j jButtonExecFileChooser;
	private JButton4j jButtonExecDirChooser;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel9;
	private JTextField4j jTextFieldExecFilename;
	private JTextField4j jTextFieldExecDir;
	private JButton4j jButtonReportFileChooser;
	private JTextField4j jTextFieldReportFilename;
	private JButton4j jButtonHelp;
	private JLabel4j_std jLabel7;
	private JTextField4j jTextFieldHelpsetid;
	private JButton4j jButtonIconPreview;
	private JButton4j jButtonIconFileChooser;
	private JTextField4j jTextFieldIconFilename;
	private JLabel4j_std jLabel2;
	private JTextField4j jTextFieldMnemonic;
	private JLabel4j_std jLabelMnemonic;
	private JLabel4j_std jLabel6;
	private JCheckBox4j jCheckBoxDesktop;
	private JCheckBox4j jCheckBoxScanner;
	private JComboBox4j<String> jComboBoxType;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel3;
	private JButton4j jButtonUpdate;
	private JTextField4j jTextFieldResourceKey;
	private JTextField4j jTextFieldModuleId;
	private JButton4j jButtonAlternative;
	private JLabel4j_std jLabel1;
	private JDBModule module = new JDBModule(Common.selectedHostID, Common.sessionID);
	private Object current_type = new Object();
	private Object new_type = new Object();
	private String lmodule_id;
	private JComboBox4j<String> comboBox;
	private JLabel4j_std lblReportType;
	private JTextField4j textFieldTranslatedDescripton;
	private JLabel4j_std lblResourceKey;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldAutoLabelCommandFilename = new JTextField4j(JDBModule.field_autolabeller_command_file);
	private JTextField4j jTextFieldAutoLabelLabelFilename = new JTextField4j(JDBModule.field_autolabeller_label_file);
	private JButton4j jButtonAutoLabelLabelDirChooser = new JButton4j();
	private JButton4j jButtonAutoLabelCommandFileChooser = new JButton4j();

	public JInternalFrameModuleProperties()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MODULE_EDIT"));

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
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
		comboBox.setSelectedItem(module.getReportType());

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

		reset_changes();
	}

	private void reset_changes() {
		current_type = jComboBoxType.getSelectedItem();
		jButtonUpdate.setEnabled(false);
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(573, 396));
			this.setBounds(0, 0, 698, 505);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Module Properties");
			this.setIconifiable(true);
			getContentPane().setLayout(null);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 690, 475);
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(577, 280));
				jDesktopPane1.setLayout(null);
				{
					textFieldTranslatedDescripton = new JTextField4j();
					textFieldTranslatedDescripton.setEditable(false);
					textFieldTranslatedDescripton.setFocusCycleRoot(true);
					textFieldTranslatedDescripton.setCaretPosition(0);
					textFieldTranslatedDescripton.setBounds(164, 65, 280, 22);
					jDesktopPane1.add(textFieldTranslatedDescripton);
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Module_ID"));
					jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel1.setBounds(0, 10, 152, 22);
				}
				{
					lblReportType = new JLabel4j_std();
					lblReportType.setText(lang.get("lbl_Module_Report_Type"));
					lblReportType.setHorizontalAlignment(SwingConstants.TRAILING);
					lblReportType.setBounds(0, 206, 152, 22);
					jDesktopPane1.add(lblReportType);
				}
				{
					jTextFieldModuleId = new JTextField4j();
					jDesktopPane1.add(jTextFieldModuleId);
					jTextFieldModuleId.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldModuleId.setEditable(false);
					jTextFieldModuleId.setBounds(164, 10, 280, 22);
					jTextFieldModuleId.setEnabled(false);
				}
				{
					jTextFieldResourceKey = new JTextField4j(JDBModule.field_resource_key);
					jDesktopPane1.add(jTextFieldResourceKey);
					jTextFieldResourceKey.setFocusCycleRoot(true);
					jTextFieldResourceKey.setBounds(164, 38, 280, 22);
					jTextFieldResourceKey.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setBounds(125, 425, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							module.setResourceKey(jTextFieldResourceKey.getText());

							module.setIconFilename(jTextFieldIconFilename.getText());
							module.setHelpSetID(jTextFieldHelpsetid.getText());

							module.setType((String) jComboBoxType.getSelectedItem());
							module.setReportType((String) comboBox.getSelectedItem());

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

							if (module.update() == false)
							{
								JUtility.errorBeep();
								JOptionPane.showMessageDialog(null, module.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
							}

							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{

					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(467, 425, 112, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Module_Type"));
					jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3.setBounds(0, 93, 152, 22);
				}
				{
					jLabel4 = new JLabel4j_std();
					jDesktopPane1.add(jLabel4);
					jLabel4.setText(lang.get("lbl_Module_Desktop"));
					jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel4.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel4.setBounds(0, 122, 152, 22);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Description"));
					jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel5.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel5.setBounds(0, 65, 152, 22);
				}
				{
					comboBox = new JComboBox4j<String>();
					comboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							jButtonUpdate.setEnabled(true);
						}
					});
					comboBox.setBounds(164, 206, 180, 22);
					comboBox.addItem("");
					comboBox.addItem("Standard");
					comboBox.addItem("Label");
					jDesktopPane1.add(comboBox);
				}
				{
					jComboBoxType = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxType);
					jComboBoxType.setEnabled(true);
					jComboBoxType.setEditable(false);
					jComboBoxType.setLightWeightPopupEnabled(true);
					jComboBoxType.setIgnoreRepaint(false);
					jComboBoxType.setBounds(164, 93, 180, 22);
					jComboBoxType.addItem("EXEC");
					jComboBoxType.addItem("FORM");
					jComboBoxType.addItem("FUNCTION");
					jComboBoxType.addItem("MENU");
					jComboBoxType.addItem("REPORT");
					jComboBoxType.addItem("USER");
					jComboBoxType.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
								}
								else
								{
									jButtonAlternative.setEnabled(false);
								}

								if (new_type.equals("REPORT") || new_type.equals("USER"))
								{
									jTextFieldReportFilename.setEnabled(true);
									jButtonReportFileChooser.setEnabled(true);
									jCheckBoxPrintPreview.setEnabled(true);
									jCheckBoxPrintDialog.setEnabled(true);
									jSpinnerPrintCopies.setEnabled(true);
									comboBox.setEnabled(true);
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
									comboBox.setEnabled(false);
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
				}
				{
					jCheckBoxScanner = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxScanner);
					jCheckBoxScanner.setSelected(true);
					jCheckBoxScanner.setBounds(164, 146, 21, 22);
					jCheckBoxScanner.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxScanner.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							// module_updated = true;
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jCheckBoxDesktop = new JCheckBox4j();
					jDesktopPane1.add(jCheckBoxDesktop);
					jCheckBoxDesktop.setSelected(true);
					jCheckBoxDesktop.setBounds(164, 122, 21, 22);
					jCheckBoxDesktop.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxDesktop.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							// module_updated = true;
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel6 = new JLabel4j_std();
					jDesktopPane1.add(jLabel6);
					jLabel6.setText(lang.get("lbl_Module_Scanner"));
					jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel6.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel6.setBounds(0, 146, 152, 22);
				}

				{
					jLabelMnemonic = new JLabel4j_std();
					jDesktopPane1.add(jLabelMnemonic);
					jLabelMnemonic.setText(lang.get("lbl_Language_Mnemonic"));
					jLabelMnemonic.setBounds(462, 65, 151, 22);
					jLabelMnemonic.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelMnemonic.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jTextFieldMnemonic = new JTextField4j(JDBModule.field_mneumonic);
					jTextFieldMnemonic.setEditable(false);

					jDesktopPane1.add(jTextFieldMnemonic);
					jTextFieldMnemonic.setText("");

					jTextFieldMnemonic.setBounds(618, 65, 42, 22);
					jTextFieldMnemonic.setToolTipText("Character to underline");
					jTextFieldMnemonic.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Module_Executable_Directory"));
					jLabel2.setBounds(0, 291, 152, 22);
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldIconFilename = new JTextField4j(JDBModule.field_icon_filename);
					jDesktopPane1.add(jTextFieldIconFilename);
					jTextFieldIconFilename.setBounds(164, 177, 220, 22);
					jTextFieldIconFilename.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent evt) {
							jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
						}

						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonIconFileChooser = new JButton4j();
					jDesktopPane1.add(jButtonIconFileChooser);
					jButtonIconFileChooser.setText("..");
					jButtonIconFileChooser.setBounds(383, 177, 22, 22);
					jButtonIconFileChooser.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

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
				}
				{
					jButtonIconPreview = new JButton4j();
					jDesktopPane1.add(jButtonIconPreview);
					jButtonIconPreview.setBounds(292, 177, 23, 22);
					jButtonIconPreview.setBorderPainted(false);
					jButtonIconPreview.setContentAreaFilled(false);
					jButtonIconPreview.setRolloverEnabled(false);
					jButtonIconPreview.setRequestFocusEnabled(false);
				}
				{
					jTextFieldHelpsetid = new JTextField4j(JDBModule.field_helpset_id);
					jDesktopPane1.add(jTextFieldHelpsetid);
					jTextFieldHelpsetid.setBounds(164, 318, 475, 22);
					jTextFieldHelpsetid.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {

							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel7 = new JLabel4j_std();
					jDesktopPane1.add(jLabel7);
					jLabel7.setText(lang.get("lbl_Module_Help"));
					jLabel7.setBounds(0, 318, 152, 22);
					jLabel7.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(353, 425, 112, 32);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jTextFieldReportFilename = new JTextField4j(JDBModule.field_report_filename);
					jDesktopPane1.add(jTextFieldReportFilename);
					jTextFieldReportFilename.setBounds(164, 234, 303, 22);
					jTextFieldReportFilename.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}

						public void keyReleased(KeyEvent evt) {
							jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
						}
					});
				}
				{
					jLabel8 = new JLabel4j_std();
					jDesktopPane1.add(jLabel8);
					jLabel8.setText(lang.get("lbl_Module_Icon_Filename"));
					jLabel8.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel8.setBounds(0, 178, 152, 22);
				}
				{
					jButtonReportFileChooser = new JButton4j();
					jDesktopPane1.add(jButtonReportFileChooser);
					jButtonReportFileChooser.setText("..");
					jButtonReportFileChooser.setBounds(467, 234, 22, 22);
					jButtonReportFileChooser.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							JFileChooser loadRpt = new JFileChooser();
							loadRpt.setAcceptAllFileFilterUsed(false);
							try
							{
								String path = "";
								if (((String) comboBox.getSelectedItem()).equals("Standard"))
								{
									path = "./reports";
								}
								else
								{
									path = "./labels";
								}
								File f = new File(new File(path).getCanonicalPath());
								loadRpt.setCurrentDirectory(f);

								if (((String) comboBox.getSelectedItem()).equals("Standard"))
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
				}
				{
					jTextFieldExecDir = new JTextField4j();
					jDesktopPane1.add(jTextFieldExecDir);
					jTextFieldExecDir.setBounds(164, 290, 475, 22);
					jTextFieldExecDir.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}

						public void keyReleased(KeyEvent evt) {
							jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
						}
					});
				}
				{
					jTextFieldExecFilename = new JTextField4j(JDBModule.field_exec_dir);
					jDesktopPane1.add(jTextFieldExecFilename);
					jTextFieldExecFilename.setBounds(164, 262, 475, 22);
					jTextFieldExecFilename.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}

						public void keyReleased(KeyEvent evt) {
							jButtonIconPreview.setIcon(JDBModule.getModuleIcon16x16(jTextFieldIconFilename.getText(), (String) jComboBoxType.getSelectedItem()));
						}
					});
				}
				{
					jLabel9 = new JLabel4j_std();
					jDesktopPane1.add(jLabel9);
					jLabel9.setText(lang.get("lbl_Module_Report_Filename"));
					jLabel9.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel9.setBounds(0, 234, 152, 22);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Module_Executable_Filename"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(0, 263, 152, 22);
				}
				{
					jButtonExecDirChooser = new JButton4j();
					jDesktopPane1.add(jButtonExecDirChooser);
					jButtonExecDirChooser.setText("..");
					jButtonExecDirChooser.setBounds(638, 290, 22, 22);
					jButtonExecDirChooser.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

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
				}
				{
					jButtonExecFileChooser = new JButton4j();
					jDesktopPane1.add(jButtonExecFileChooser);
					jButtonExecFileChooser.setText("..");
					jButtonExecFileChooser.setBounds(638, 262, 22, 22);
					jButtonExecFileChooser.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

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
				}

				{
					jButtonExecDirChooser_1 = new JButton4j();
					jButtonExecDirChooser_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

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
					jButtonExecDirChooser_1.setBounds(638, 318, 22, 22);
					jDesktopPane1.add(jButtonExecDirChooser_1);
				}

				{
					jCheckBoxPrintPreview = new JCheckBox4j();
					jCheckBoxPrintPreview.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jCheckBoxPrintPreview.setSelected(true);
					jCheckBoxPrintPreview.setBackground(new Color(255, 255, 255));
					jCheckBoxPrintPreview.setBounds(639, 177, 21, 22);
					jDesktopPane1.add(jCheckBoxPrintPreview);
				}

				{
					jCheckBoxPrintDialog = new JCheckBox4j();
					jCheckBoxPrintDialog.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jCheckBoxPrintDialog.setSelected(true);
					jCheckBoxPrintDialog.setBackground(new Color(255, 255, 255));
					jCheckBoxPrintDialog.setBounds(639, 205, 21, 22);
					jDesktopPane1.add(jCheckBoxPrintDialog);
				}

				{
					jLabel3_1 = new JLabel4j_std();
					jLabel3_1.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3_1.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3_1.setText(lang.get("lbl_Module_Print_Preview"));
					jLabel3_1.setBounds(488, 176, 141, 22);
					jDesktopPane1.add(jLabel3_1);
				}

				{
					jLabel3_2 = new JLabel4j_std();
					jLabel3_2.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3_2.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3_2.setText(lang.get("lbl_Module_Print_Dialog"));
					jLabel3_2.setBounds(482, 205, 147, 22);
					jDesktopPane1.add(jLabel3_2);
				}

				{
					jLabel3_3 = new JLabel4j_std();
					jLabel3_3.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabel3_3.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabel3_3.setText(lang.get("lbl_Module_Print_Copies"));
					jLabel3_3.setBounds(467, 233, 141, 22);
					jDesktopPane1.add(jLabel3_3);
				}

				{
					jSpinnerPrintCopies = new JSpinner4j();
					jSpinnerPrintCopies.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jSpinnerPrintCopies.setBounds(618, 234, 42, 22);
					jDesktopPane1.add(jSpinnerPrintCopies);
				}
				
				{
					lblResourceKey = new JLabel4j_std();
					lblResourceKey.setText(lang.get("lbl_Language_Key"));
					lblResourceKey.setHorizontalTextPosition(SwingConstants.RIGHT);
					lblResourceKey.setHorizontalAlignment(SwingConstants.RIGHT);
					lblResourceKey.setBounds(0, 38, 152, 22);
					jDesktopPane1.add(lblResourceKey);
				}
				
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Command_File"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(0, 364, 152, 22);
				jDesktopPane1.add(label4j_std);
				
				JLabel4j_std label4j_std_1 = new JLabel4j_std();
				label4j_std_1.setText(lang.get("lbl_Label_File"));
				label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std_1.setBounds(0, 391, 152, 22);
				jDesktopPane1.add(label4j_std_1);
				jTextFieldAutoLabelCommandFilename.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				
				jTextFieldAutoLabelCommandFilename.setBounds(164, 363, 220, 22);
				jDesktopPane1.add(jTextFieldAutoLabelCommandFilename);
				jTextFieldAutoLabelLabelFilename.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						jButtonUpdate.setEnabled(true);
					}
				});
				
				jTextFieldAutoLabelLabelFilename.setBounds(164, 391, 220, 22);
				jDesktopPane1.add(jTextFieldAutoLabelLabelFilename);
				
				
				jButtonAutoLabelCommandFileChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser loadCMD = new JFileChooser();

						try
						{
							String path = jTextFieldAutoLabelCommandFilename.getText();
							if (path.equals(""))
							{
								path=Common.auto_label_command;
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
				jButtonAutoLabelCommandFileChooser.setBounds(383, 363, 22, 22);
				jDesktopPane1.add(jButtonAutoLabelCommandFileChooser);
				

				jButtonAutoLabelLabelDirChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					
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
				jButtonAutoLabelLabelDirChooser.setBounds(383, 391, 22, 22);
				jDesktopPane1.add(jButtonAutoLabelLabelDirChooser);
					
				JLabel4j_std label4j_std_AutoLabeller = new JLabel4j_std();
				label4j_std_AutoLabeller.setText(lang.get("lbl_Auto_Labeller"));
				label4j_std_AutoLabeller.setHorizontalAlignment(SwingConstants.CENTER);
				label4j_std_AutoLabeller.setBounds(163, 341, 237, 22);
				jDesktopPane1.add(label4j_std_AutoLabeller);
				
				jButtonAlternative = new JButton4j(Common.icon_alternative_16x16);
				jButtonAlternative.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JLaunchMenu.runDialog("FRM_ADMIN_MODULE_ALTERNATE",lmodule_id);
					}
				});
				jButtonAlternative.setText(lang.get("btn_Alternative"));
				jButtonAlternative.setMnemonic('A');
				jButtonAlternative.setEnabled(false);
				jButtonAlternative.setBounds(239, 425, 112, 32);
				jDesktopPane1.add(jButtonAlternative);
				
			}
			postInitGUI();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void postInitGUI() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		reset_changes();

	}
}
