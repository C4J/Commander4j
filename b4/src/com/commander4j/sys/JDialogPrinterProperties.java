package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogPrinterProperties.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBPrinters;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JDialogPrinterProperties extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelType;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldDescription;
	private JComboBox4j<String> comboBoxPrinterType;
	private JTextField4j jTextFieldPrinterID;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabelID;
	private JLabel4j_std jLabelDescription;
	private String lprnID;
	private String lgrpID;
	private JDBPrinters printers = new JDBPrinters(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldIPAddress = new JTextField4j(JDBPrinters.field_ip_address);
	private JTextField4j jTextFieldPort = new JTextField4j(JDBPrinters.field_printer_port);
	private JComboBox4j<String> comboBoxLanguage = new JComboBox4j<String>();
	private JCheckBox4j chckbxEnabled = new JCheckBox4j("");
	private JComboBox4j<String> comboBox4jGroup = new JComboBox4j<String>();
	private JComboBox4j<String> comboBoxDPI = new JComboBox4j<String>();
	private JTextField4j jTextFieldExportPath = new JTextField4j(JDBPrinters.field_export_path);
	private JButton4j jButtonExportPathChooser = new JButton4j();
	private JCheckBox4j chckbxEnableExport = new JCheckBox4j("");
	private JCheckBox4j chckbxEnableDirectPrint = new JCheckBox4j("");
	private JComboBox4j<String> comboBox4ExportFormat = new JComboBox4j<String>();
	private JTextField4j jTextFieldPaperSize = new JTextField4j(10);

	public JDialogPrinterProperties(JFrame parent, String prn_id,String grp_id)
	{

		super(parent);
		setResizable(false);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PRINTERS"));

		jTextFieldPrinterID.setText(prn_id);
		this.setTitle(lang.get("lbl_Printer_ID") + " - " + prn_id);
		lprnID = prn_id;
		lgrpID = grp_id;

		printers.setPrinterID(lprnID);
		printers.setGroupID(lgrpID);
		
		printers.getPrinterProperties();
		
		jTextFieldDescription.setText(printers.getDescription());
		jTextFieldIPAddress.setText(printers.getIPAddress());
		jTextFieldPort.setText(printers.getPort());
		if (printers.getEnabled().equals("Y"))
		{
			chckbxEnabled.setSelected(true);
		}
		else
		{
			chckbxEnabled.setSelected(false);
		}
		
		comboBoxLanguage.setSelectedItem(printers.getLanguage());

		comboBoxPrinterType.setSelectedItem(printers.getPrinterType());
		comboBoxLanguage.setSelectedItem(printers.getLanguage());
		comboBox4jGroup.setEnabled(false);
		comboBox4jGroup.setSelectedItem(printers.getGroupID());
		comboBoxDPI.setSelectedItem(printers.getDPI());
		jButtonUpdate.setEnabled(false);
		chckbxEnableExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEnabledObjects(chckbxEnableExport.isSelected());
				jButtonUpdate.setEnabled(true);
			}
		});
		chckbxEnableExport.setSelected(printers.isExportEnabled());
		chckbxEnableDirectPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonUpdate.setEnabled(true);
			}
		});
		chckbxEnableDirectPrint.setSelected(printers.isDirectPrintEnabled());
		jTextFieldExportPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				jButtonUpdate.setEnabled(true);
			}
		});
		jTextFieldExportPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonUpdate.setEnabled(true);
			}
		});
		jTextFieldExportPath.setText(printers.getExportPath());
		comboBox4ExportFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonUpdate.setEnabled(true);
			}
		});
		
		comboBox4ExportFormat.setSelectedItem(printers.getExportFormat());
		
		jButtonUpdate.setEnabled(false);
		jTextFieldPaperSize.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				jButtonUpdate.setEnabled(true);
			}
		});
		

		jTextFieldPaperSize.setText(printers.getPaperSize());
		jTextFieldPaperSize.setPreferredSize(new Dimension(40, 20));
		jTextFieldPaperSize.setFocusCycleRoot(true);
		jTextFieldPaperSize.setBounds(141, 178, 89, 21);
		jDesktopPane1.add(jTextFieldPaperSize);
		
		JLabel4j_std jLabel_PaperSize = new JLabel4j_std();
		jLabel_PaperSize.setText("Paper Size");
		jLabel_PaperSize.setHorizontalTextPosition(SwingConstants.RIGHT);
		jLabel_PaperSize.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel_PaperSize.setBounds(9, 180, 125, 19);
		jDesktopPane1.add(jLabel_PaperSize);
		
		JLabel4j_std jLabelSizeHint = new JLabel4j_std();
		jLabelSizeHint.setEnabled(false);
		jLabelSizeHint.setText("(widthxheight)");
		jLabelSizeHint.setHorizontalTextPosition(SwingConstants.LEFT);
		jLabelSizeHint.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelSizeHint.setBounds(234, 178, 157, 19);
		jDesktopPane1.add(jLabelSizeHint);
		
	}

	private void setEnabledObjects(boolean enabled)
	{
	}
	
	private void initGUI() {
		try
		{
			setPreferredSize(new java.awt.Dimension(460, 163));
			this.setBounds(25, 25, 581, 448);
			setModal(true);
			getContentPane().setLayout(null);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 581, 437);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new Dimension(452, 140));
				jDesktopPane1.setLayout(null);
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setBounds(9, 208, 125, 19);
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelID = new JLabel4j_std();
					jDesktopPane1.add(jLabelID);
					jLabelID.setText(lang.get("lbl_Printer_ID"));
					jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelID.setBounds(9, 15, 125, 19);
				}
				{
					jLabelType = new JLabel4j_std();
					jDesktopPane1.add(jLabelType);
					jLabelType.setText(lang.get("lbl_Printer_Type"));
					jLabelType.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelType.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelType.setBounds(9, 81, 125, 19);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setBounds(92, 373, 130, 32);
					jButtonUpdate.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							printers.setDescription(jTextFieldDescription.getText());
							printers.setPrinterType(comboBoxPrinterType.getSelectedItem().toString());
							printers.setLanguage(JUtility.replaceNullStringwithBlank(comboBoxLanguage.getSelectedItem().toString()));
							printers.setIPAddress(jTextFieldIPAddress.getText());
							printers.setPort(jTextFieldPort.getText());
							printers.setGroupID(comboBox4jGroup.getSelectedItem().toString());
							printers.setDPI(comboBoxDPI.getSelectedItem().toString());
							printers.setPaperSize(jTextFieldPaperSize.getText());
							printers.setPrinterEnabled(chckbxEnabled.isSelected());
							String path = jTextFieldExportPath.getText();
							path = path.replace(Common.base_dir, "{base_dir}");
							printers.setExportPath(path);
							printers.setEnableExport(chckbxEnableExport.isSelected());
							printers.setEnableDirectPrint(chckbxEnableDirectPrint.isSelected());
							printers.setExportFormat(JUtility.replaceNullStringwithBlank(comboBox4ExportFormat.getSelectedItem().toString()));

							printers.update();
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(356, 373, 130, 32);
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jTextFieldPrinterID = new JTextField4j(JDBPrinters.field_printer_id);
					jTextFieldPrinterID.setFocusable(false);
					jDesktopPane1.add(jTextFieldPrinterID);
					jTextFieldPrinterID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldPrinterID.setEditable(false);
					jTextFieldPrinterID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldPrinterID.setBounds(142, 13, 252, 21);
					jTextFieldPrinterID.setEnabled(false);
				}
				{
					comboBoxPrinterType = new JComboBox4j<String>();
					comboBoxPrinterType.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					comboBoxPrinterType.setModel(new DefaultComboBoxModel<String>(Common.printerTypes));
					jDesktopPane1.add(comboBoxPrinterType);
					comboBoxPrinterType.setPreferredSize(new java.awt.Dimension(40, 20));
					comboBoxPrinterType.setFocusCycleRoot(true);
					comboBoxPrinterType.setBounds(142, 79, 167, 21);

				}
				{
					jTextFieldDescription = new JTextField4j(JDBPrinters.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(141, 206, 325, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(224, 373, 130, 32);
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				}
				{
					comboBoxDPI.setPreferredSize(new Dimension(40, 20));
					comboBoxDPI.setFocusCycleRoot(true);
					comboBoxDPI.setBounds(141, 145, 89, 21);
					comboBoxDPI.setModel(new DefaultComboBoxModel<String>(Common.printerDPI));
					comboBoxDPI.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					jDesktopPane1.add(comboBoxDPI);
				}
			}
			

			chckbxEnabled.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					jButtonUpdate.setEnabled(true);
				}
			});
			chckbxEnabled.setBounds(511, 44, 28, 23);
			jDesktopPane1.add(chckbxEnabled);
			
			JLabel4j_std jLabelIPAddress = new JLabel4j_std();
			jLabelIPAddress.setText(lang.get("lbl_IP_Address"));
			jLabelIPAddress.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabelIPAddress.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelIPAddress.setBounds(9, 241, 125, 19);
			jDesktopPane1.add(jLabelIPAddress);
			
			JLabel4j_std jLabelPort = new JLabel4j_std();
			jLabelPort.setText(lang.get("lbl_Port"));
			jLabelPort.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabelPort.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelPort.setBounds(9, 274, 125, 19);
			jDesktopPane1.add(jLabelPort);
			
			JLabel4j_std label4j_line = new JLabel4j_std();
			label4j_line.setText(lang.get("lbl_Language"));
			label4j_line.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_line.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_line.setBounds(9, 114, 125, 19);
			jDesktopPane1.add(label4j_line);
			

			jTextFieldIPAddress.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent arg0) {
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldIPAddress.setText((String) null);
			jTextFieldIPAddress.setPreferredSize(new Dimension(40, 20));
			jTextFieldIPAddress.setFocusCycleRoot(true);
			jTextFieldIPAddress.setBounds(141, 239, 168, 21);
			jDesktopPane1.add(jTextFieldIPAddress);
			

			jTextFieldPort.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});
			jTextFieldPort.setText((String) null);
			jTextFieldPort.setPreferredSize(new Dimension(40, 20));
			jTextFieldPort.setFocusCycleRoot(true);
			jTextFieldPort.setBounds(141, 272, 75, 21);
			jDesktopPane1.add(jTextFieldPort);
			
			comboBoxLanguage.setModel(new DefaultComboBoxModel<String>(Common.printerLanguage));

			comboBoxLanguage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});
			comboBoxLanguage.setPreferredSize(new Dimension(40, 20));
			comboBoxLanguage.setFocusCycleRoot(true);
			comboBoxLanguage.setBounds(142, 112, 168, 21);
			jDesktopPane1.add(comboBoxLanguage);
			
			JLabel4j_std jTextFieldEnabled = new JLabel4j_std();
			jTextFieldEnabled.setText(lang.get("lbl_Enabled"));
			jTextFieldEnabled.setHorizontalTextPosition(SwingConstants.RIGHT);
			jTextFieldEnabled.setHorizontalAlignment(SwingConstants.RIGHT);
			jTextFieldEnabled.setBounds(395, 46, 110, 19);
			jDesktopPane1.add(jTextFieldEnabled);
			
			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setText(lang.get("lbl_Group_ID"));
			label4j_std.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std.setBounds(9, 48, 125, 19);
			jDesktopPane1.add(label4j_std);
						
			comboBox4jGroup.setPreferredSize(new Dimension(40, 20));
			comboBox4jGroup.setFocusCycleRoot(true);
			comboBox4jGroup.setBounds(142, 46, 167, 21);
			comboBox4jGroup.setModel(new DefaultComboBoxModel<String>(Common.printerGroup));
			comboBox4jGroup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonUpdate.setEnabled(true);
				}
			});
			jDesktopPane1.add(comboBox4jGroup);
			
			JLabel4j_std label4j_std1 = new JLabel4j_std();
			label4j_std1.setText("DPI");
			label4j_std1.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std1.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std1.setBounds(9, 145, 125, 19);
			jDesktopPane1.add(label4j_std1);
			
			JLabel4j_std jLabelExportPath = new JLabel4j_std();
			jLabelExportPath.setText(lang.get("lbl_Export_Path"));
			jLabelExportPath.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabelExportPath.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelExportPath.setBounds(9, 309, 125, 19);
			jDesktopPane1.add(jLabelExportPath);
			

			chckbxEnableExport.setBounds(142, 305, 28, 23);
			jDesktopPane1.add(chckbxEnableExport);
			
			jTextFieldExportPath.setText("");
			jTextFieldExportPath.setPreferredSize(new Dimension(40, 20));
			jTextFieldExportPath.setFocusCycleRoot(true);
			jTextFieldExportPath.setBounds(169, 307, 358, 21);
			jDesktopPane1.add(jTextFieldExportPath);
			
			
			jButtonExportPathChooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser choosePath = new JFileChooser();

					try
					{
						File f = new File(new File("").getCanonicalPath());
						choosePath.setCurrentDirectory(f);
						choosePath.setSelectedFile(new File(jTextFieldExportPath.getText()));
						choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						if (choosePath.showOpenDialog(jButtonExportPathChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = choosePath.getSelectedFile();

							if (selectedFile != null)
							{
								if (jTextFieldExportPath.getText().compareTo(selectedFile.getName()) != 0)
								{
									jTextFieldExportPath.setText(selectedFile.getCanonicalPath());
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
			jButtonExportPathChooser.setText("..");
			jButtonExportPathChooser.setBounds(526, 306, 17, 21);
			jDesktopPane1.add(jButtonExportPathChooser);
			
			JLabel4j_std label4j_Direct = new JLabel4j_std();
			label4j_Direct.setText(lang.get("lbl_Direct_Print"));
			label4j_Direct.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_Direct.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_Direct.setBounds(378, 81, 125, 19);
			jDesktopPane1.add(label4j_Direct);
			
			chckbxEnableDirectPrint.setBounds(511, 77, 28, 23);
			jDesktopPane1.add(chckbxEnableDirectPrint);
			

			comboBox4ExportFormat.setPreferredSize(new Dimension(40, 20));
			comboBox4ExportFormat.setFocusCycleRoot(true);
			comboBox4ExportFormat.setBounds(141, 340, 89, 21);
			comboBox4ExportFormat.setModel(new DefaultComboBoxModel<String>(Common.printerExportFormat));
			jDesktopPane1.add(comboBox4ExportFormat);
			
			JLabel4j_std jLabelInterfaceFormat = new JLabel4j_std();
			jLabelInterfaceFormat.setText(lang.get("lbl_Interface_Format"));
			jLabelInterfaceFormat.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabelInterfaceFormat.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelInterfaceFormat.setBounds(9, 340, 125, 19);
			jDesktopPane1.add(jLabelInterfaceFormat);
			setEnabledObjects(printers.isExportEnabled());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
