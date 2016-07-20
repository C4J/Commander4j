package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialLocation;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMaterialLocationProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private String lmaterial;
	private String llocation;
	private JDBMaterialLocation materiallocation = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldLocation;
	private JLabel4j_std jLabel2;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1;
	private JDBLanguage lang;

	public JInternalFrameMaterialLocationProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_LOCATION_EDIT"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jComboBoxStatus.requestFocus();
			}
		});
	}

	public JInternalFrameMaterialLocationProperties(String material, String location)
	{
		this();
		lmaterial = material;
		llocation = location;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldLocation.setText(llocation);

		materiallocation.setMaterial(lmaterial);
		materiallocation.setLocation(llocation);
		jTextFieldMaterial.setText(materiallocation.getMaterial());
		jTextFieldLocation.setText(materiallocation.getLocation());
		

		if (materiallocation.getMaterialLocationProperties())
		{
			jComboBoxStatus.setSelectedItem(materiallocation.getStatus());

			jButtonUpdate.setEnabled(false);
		}
		else
		{
			jComboBoxStatus.setSelectedItem("Valid");
			jButtonUpdate.setEnabled(true);
		}

	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 374+Common.LFAdjustWidth, 192+Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Color.WHITE);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setBounds(6, 100, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							try
							{
								materiallocation.setStatus((String) jComboBoxStatus.getSelectedItem());
							}
							catch (Exception e)
							{
								materiallocation.setStatus("");
							}

							if (materiallocation.isValidMaterialLocation())
							{
								materiallocation.update();
							}
							else
							{
								materiallocation.create(jTextFieldMaterial.getText(),jTextFieldLocation.getText());
								materiallocation.update();
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
					jButtonHelp.setBounds(118, 100, 112, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(230, 100, 112, 32);
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
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(49, 41, 70, 21);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setText(llocation);
					jTextFieldLocation.setBounds(126, 41, 126, 21);
					jTextFieldLocation.setEnabled(false);
					jTextFieldLocation.setEditable(false);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.locationStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(126, 69, 190, 23);
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
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
