package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JDateControl;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMaterialBatchProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private String lmaterial;
	private String lbatch;
	private JDBMaterialBatch materialbatch = new JDBMaterialBatch(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldBatch;
	private JLabel4j_std jLabel2;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1;
	private JDateControl dateTimePicker = new JDateControl();
	private JDBLanguage lang;
	private JCalendarButton calendarButton;

	public JInternalFrameMaterialBatchProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_BATCH_EDIT"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jComboBoxStatus.requestFocus();
			}
		});
	}

	public JInternalFrameMaterialBatchProperties(String material, String batch)
	{
		this();
		lmaterial = material;
		lbatch = batch;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldBatch.setText(lbatch);

		materialbatch.setMaterial(lmaterial);
		materialbatch.setBatch(lbatch);
		jTextFieldMaterial.setText(materialbatch.getMaterial());
		jTextFieldBatch.setText(materialbatch.getBatch());

		if (materialbatch.getMaterialBatchProperties())
		{
			jComboBoxStatus.setSelectedItem(materialbatch.getStatus());
			try
			{
				dateTimePicker.setDate(materialbatch.getExpiryDate());
			}
			catch (Exception e)
			{

			}
			jButtonUpdate.setEnabled(false);
		}
		else
		{
			JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
			mat.getMaterialProperties(material);
			jComboBoxStatus.setSelectedItem(mat.getDefaultBatchStatus());

			Date de = JUtility.getSQLDateTime();
			try
			{
				dateTimePicker.setDate(mat.calcBBE(de, mat.getShelfLife(), mat.getShelfLifeUom(), mat.getShelfLifeRule()));
			}
			catch (Exception e)
			{

			}
			jButtonUpdate.setEnabled(true);
		}

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 378, 213);
			setVisible(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setBounds(7, 130, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							try
							{
								materialbatch.setStatus((String) jComboBoxStatus.getSelectedItem());
							}
							catch (Exception e)
							{
								materialbatch.setStatus("");
							}
							Date d = dateTimePicker.getDate();
							materialbatch.setExpiryDate(JUtility.getTimestampFromDate(d));
							if (materialbatch.isValidMaterialBatch())
							{
								materialbatch.update();
							}
							else
							{
								materialbatch.create();
							}
							jButtonUpdate.setEnabled(false);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(119, 130, 112, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(231, 130, 112, 32);
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1.setBounds(49, 13, 70, 21);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setText(lmaterial);
					jTextFieldMaterial.setBounds(126, 13, 126, 21);
					jTextFieldMaterial.setEnabled(false);
					jTextFieldMaterial.setEditable(false);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Material_Batch"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(49, 41, 70, 21);
				}
				{
					jTextFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jDesktopPane1.add(jTextFieldBatch);
					jTextFieldBatch.setText(lbatch);
					jTextFieldBatch.setBounds(126, 41, 126, 21);
					jTextFieldBatch.setEnabled(false);
					jTextFieldBatch.setEditable(false);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_Batch_Expiry_Date"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(35, 97, 84, 21);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.batchStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(126, 69, 150, 21);
					jComboBoxStatus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Material_Batch_Status"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(42, 69, 77, 21);
				}

				{
					dateTimePicker.getEditor().addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});
					dateTimePicker.addChangeListener(new ChangeListener() {
						public void stateChanged(final ChangeEvent e) {
							jButtonUpdate.setEnabled(true);
						}
					});

					// dateTimePicker.setEditable(true);
					dateTimePicker.setBounds(126, 97, 150, 25);
					jDesktopPane1.add(dateTimePicker);
				}
				{
					calendarButton = new JCalendarButton(dateTimePicker);
					calendarButton.setBounds(255, 101, 21, 21);
					jDesktopPane1.add(calendarButton);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
