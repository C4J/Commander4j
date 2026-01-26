package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameProcessOrderResourceProperties.java
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
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameProcessOrderResourceProperties class updates the table
 * APP_PROCESS_ORDER_RESOURCE. This table contains a single row for each unique
 * Process Order Resource. This table holds additional data which is associated
 * with the factory resource. This table is automatically updated with resource
 * names when a new Process is imported via the interfaces. Typical data which
 * might be stored in this table include default batch numbers
 * <p>
 * <img alt="" src=
 * "./doc-files/JInternalFrameProcessOrderResourceProperties.jpg" >
 *
 * @see com.commander4j.db.JDBProcessOrderResource JDBProcessOrderResource
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 *
 */
public class JInternalFrameProcessOrderResourceProperties extends JInternalFrame
{
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private JCheckBox4j checkBoxEnabled;
	private JDBLanguage lang;
	private JDBProcessOrderResource processOrderResource = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabelBatchSuffix;
	private JLabel4j_std jLabelDescription;
	private JLabel4j_std jLabelPlantID;
	private JLabel4j_std jLabelResource;
	private JLabel4j_std jLabel_Enabled;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldPlantID;
	private JTextField4j jTextFieldResource;
	private JTextField4j textFieldBatchSuffix;
	private String lresource;
	private static final long serialVersionUID = 1;

	public JInternalFrameProcessOrderResourceProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PO_RESOURCE_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
			}
		});
	}

	public void setResource(String res)
	{
		lresource = res;

		jTextFieldResource.setText(lresource);

		processOrderResource.setResource(lresource);
		processOrderResource.getResourceProperties();

		jTextFieldResource.setText(processOrderResource.getResource());
		jTextFieldDescription.setText(processOrderResource.getDescription());
		jTextFieldPlantID.setText(processOrderResource.getPlant());
		textFieldBatchSuffix.setText(processOrderResource.getBatchSuffix());

		if (processOrderResource.getEnabled().equals("Y"))
		{
			checkBoxEnabled.setSelected(true);
		}
		else
		{
			checkBoxEnabled.setSelected(false);
		}

	}

	public JInternalFrameProcessOrderResourceProperties(String res)
	{
		this();
		setResource(res);
	}

	private void updateRecord()
	{
		jTextFieldPlantID.setText(jTextFieldPlantID.getText().toUpperCase());

		processOrderResource.setResource(jTextFieldResource.getText());

		if (processOrderResource.isValidResource() == false)
		{
			processOrderResource.create(jTextFieldResource.getText());
		}

		processOrderResource.setEnabled(checkBoxEnabled.isSelected());
		processOrderResource.setResource(jTextFieldResource.getText());
		processOrderResource.setDescription(jTextFieldDescription.getText());
		processOrderResource.setPlant(jTextFieldPlantID.getText());
		processOrderResource.setBatchSuffix(textFieldBatchSuffix.getText());

		processOrderResource.update();

		jButtonUpdate.setEnabled(false);
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 377, 245);
			setVisible(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();
			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
			jDesktopPane1.setLayout(null);

			jButtonUpdate = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonUpdate);
			jButtonUpdate.setEnabled(false);
			jButtonUpdate.setText(lang.get("btn_Save"));
			jButtonUpdate.setMnemonic(lang.getMnemonicChar());
			jButtonUpdate.setBounds(6, 167, 112, 32);
			jButtonUpdate.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					updateRecord();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(125, 167, 112, 32);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(244, 167, 112, 32);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabelResource = new JLabel4j_std();
			jDesktopPane1.add(jLabelResource);
			jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
			jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelResource.setBounds(6, 8, 113, 22);

			jTextFieldResource = new JTextField4j(JDBProcessOrderResource.field_Resource_id);
			jDesktopPane1.add(jTextFieldResource);
			jTextFieldResource.setText(lresource);
			jTextFieldResource.setBounds(126, 8, 126, 22);
			jTextFieldResource.setEnabled(false);
			jTextFieldResource.setEditable(false);

			jLabelDescription = new JLabel4j_std();
			jDesktopPane1.add(jLabelDescription);
			jLabelDescription.setText(lang.get("lbl_Description"));
			jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelDescription.setBounds(6, 40, 113, 22);

			jLabelPlantID = new JLabel4j_std();
			jDesktopPane1.add(jLabelPlantID);
			jLabelPlantID.setText(lang.get("lbl_Plant"));
			jLabelPlantID.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelPlantID.setBounds(6, 104, 113, 22);

			jTextFieldDescription = new JTextField4j(JDBProcessOrderResource.field_Description_id);
			jTextFieldDescription.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jDesktopPane1.add(jTextFieldDescription);
			jTextFieldDescription.setText("");
			jTextFieldDescription.setBounds(127, 40, 229, 22);

			jTextFieldPlantID = new JTextField4j(JDBProcessOrderResource.field_Plant_id);
			jTextFieldPlantID.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			jDesktopPane1.add(jTextFieldPlantID);
			jTextFieldPlantID.setText("");
			jTextFieldPlantID.setBounds(125, 104, 104, 22);

			jLabelBatchSuffix = new JLabel4j_std();
			jDesktopPane1.add(jLabelBatchSuffix);
			jLabelBatchSuffix.setText(lang.get("lbl_Batch_Suffix"));
			jLabelBatchSuffix.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelBatchSuffix.setBounds(6, 72, 113, 22);

			textFieldBatchSuffix = new JTextField4j(15);
			textFieldBatchSuffix.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			textFieldBatchSuffix.setText("");
			textFieldBatchSuffix.setBounds(126, 72, 229, 22);
			jDesktopPane1.add(textFieldBatchSuffix);

			checkBoxEnabled = new JCheckBox4j("");
			checkBoxEnabled.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jButtonUpdate.setEnabled(true);
				}
			});
			checkBoxEnabled.setBounds(126, 136, 21, 22);
			jDesktopPane1.add(checkBoxEnabled);

			jLabel_Enabled = new JLabel4j_std();
			jLabel_Enabled.setText(lang.get("lbl_Enabled"));
			jLabel_Enabled.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Enabled.setBounds(6, 136, 113, 22);
			jDesktopPane1.add(jLabel_Enabled);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
