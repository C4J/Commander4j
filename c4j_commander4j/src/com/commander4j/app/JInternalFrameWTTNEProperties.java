package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameWTTNEProperties.java
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
import java.math.BigDecimal;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUom;
import com.commander4j.db.JDBWTTNE;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameSamplePointProperties class allows the user to edit a
 * record in the APP_WEIGHT_TNE table.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTTNEProperties.jpg" >
 *
 * @see com.commander4j.app.JInternalFrameWTTNEProperties
 *
 */
public class JInternalFrameWTTNEProperties extends JInternalFrame
{
	private BigDecimal ltneValue;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSave;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBWTTNE tne = new JDBWTTNE(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Nominal;
	private JQuantityInput jTextField_NegT1 = new JQuantityInput(new BigDecimal("0"));
	private JQuantityInput jTextField_NegT2 = new JQuantityInput(new BigDecimal("0"));
	private JQuantityInput jTextField_Nominal = new JQuantityInput(new BigDecimal("0"));
	private JQuantityInput jTextField_TNE;
	private JTextField4j jTextField_NegT1_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_NegT2_UOM = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_Nominal_Uom = new JTextField4j(JDBUom.field_uom);
	private JTextField4j jTextField_TNE_Uom = new JTextField4j(JDBUom.field_uom);
	private String ltneUom;
	private static final long serialVersionUID = 1;

	public void setTNE(String tneValue, String tneUom)
	{

		if (jButtonSave.isEnabled())
		{

			int question = JOptionPane.showConfirmDialog(Common.mainForm, "Save changes to TNE [" + tneValue.toString() + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				save();
			}
		}

		jButtonSave.setEnabled(false);

		ltneValue = new BigDecimal(tneValue);
		ltneUom = tneUom;

		setTitle("TNE [" + ltneValue + "]");

		tne.setNominalWT(ltneValue);
		tne.setNominalWTUOM(tneUom);
		tne.getProperties();

		jTextField_Nominal.setEditable(false);

		jTextField_Nominal.setText(tne.getNominalWT().toString());
		jTextField_Nominal_Uom.setText(tne.getNominalWTUOM());
		jTextField_TNE_Uom.setEditable(false);
		jTextField_TNE_Uom.setText(tneUom);
		jTextField_NegT1.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				jButtonSave.setEnabled(true);
			}
		});
		jTextField_NegT1.setText(tne.getNegT1().toString());
		jTextField_TNE.setText(tne.getTNE().toString());
		jTextField_NegT1_UOM.setEditable(false);
		jTextField_NegT1_UOM.setText(tneUom);
		jTextField_NegT2.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				jButtonSave.setEnabled(true);
			}
		});
		jTextField_NegT2.setText(tne.getNegT2().toString());
		jTextField_NegT2_UOM.setEditable(false);
		jTextField_NegT2_UOM.setText(tneUom);
		jButtonSave.setEnabled(false);

	}

	public JInternalFrameWTTNEProperties(String value, String uom)
	{

		super();
		initGUI();
		setTNE(value, uom);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_TNE_ADD"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextField_TNE.requestFocus();
				jTextField_TNE.setCaretPosition(0);
				jButtonSave.setEnabled(false);
			}
		});

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(387, 165));
			this.setBounds(25, 25, 424, 213);
			setVisible(true);
			this.setTitle("Tolerable Negative Error");

			jDesktopPane1 = new JDesktopPane4j();

			this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setLayout(null);

			jLabel_Nominal = new JLabel4j_std();
			jDesktopPane1.add(jLabel_Nominal);
			jLabel_Nominal.setText(lang.get("lbl_Nominal_Weight"));
			jLabel_Nominal.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Nominal.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Nominal.setBounds(29, 8, 137, 22);

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setHorizontalTextPosition(SwingConstants.RIGHT);
			jButtonSave.setBounds(39, 138, 110, 32);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					save();
				}
			});

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(151, 138, 110, 32);

			jButtonClose = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonClose);
			jButtonClose.setText(lang.get("btn_Close"));
			jButtonClose.setMnemonic(lang.getMnemonicChar());
			jButtonClose.setBounds(263, 138, 110, 32);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jTextField_Nominal_Uom.setEditable(false);
			jDesktopPane1.add(jTextField_Nominal_Uom);
			jTextField_Nominal_Uom.setPreferredSize(new java.awt.Dimension(40, 20));
			jTextField_Nominal_Uom.setFocusCycleRoot(true);
			jTextField_Nominal_Uom.setBounds(296, 8, 50, 22);
			jTextField_Nominal_Uom.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
				}
			});

			jTextField_Nominal.setVerifyInputWhenFocusTarget(false);
			jTextField_Nominal.setHorizontalAlignment(SwingConstants.TRAILING);
			jTextField_Nominal.setBounds(176, 8, 108, 22);
			jDesktopPane1.add(jTextField_Nominal);

			jTextField_TNE = new JQuantityInput(new BigDecimal("0"));
			jTextField_TNE.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jButtonSave.setEnabled(true);
				}
			});
			jTextField_TNE.setVerifyInputWhenFocusTarget(false);
			jTextField_TNE.setHorizontalAlignment(SwingConstants.TRAILING);
			jTextField_TNE.setBounds(176, 40, 108, 22);
			jDesktopPane1.add(jTextField_TNE);

			JLabel4j_std jLabel_TNE = new JLabel4j_std();
			jLabel_TNE.setText(lang.get("lbl_TNE"));
			jLabel_TNE.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_TNE.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_TNE.setBounds(29, 40, 137, 22);
			jDesktopPane1.add(jLabel_TNE);
			jTextField_TNE_Uom.setText("");
			jTextField_TNE_Uom.setPreferredSize(new Dimension(40, 20));
			jTextField_TNE_Uom.setFocusCycleRoot(true);
			jTextField_TNE_Uom.setCaretPosition(0);
			jTextField_TNE_Uom.setBounds(296, 40, 50, 22);
			jDesktopPane1.add(jTextField_TNE_Uom);

			JLabel4j_std jLabel_NegT1 = new JLabel4j_std();
			jLabel_NegT1.setText(lang.get("lbl_NegT1"));
			jLabel_NegT1.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_NegT1.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_NegT1.setBounds(29, 72, 137, 22);
			jDesktopPane1.add(jLabel_NegT1);
			jTextField_NegT1.setVerifyInputWhenFocusTarget(false);
			jTextField_NegT1.setHorizontalAlignment(SwingConstants.TRAILING);
			jTextField_NegT1.setBounds(176, 72, 108, 22);
			jDesktopPane1.add(jTextField_NegT1);

			jTextField_NegT1_UOM.setEnabled(false);
			jTextField_NegT1_UOM.setText("");
			jTextField_NegT1_UOM.setPreferredSize(new Dimension(40, 20));
			jTextField_NegT1_UOM.setFocusCycleRoot(true);
			jTextField_NegT1_UOM.setCaretPosition(0);
			jTextField_NegT1_UOM.setBounds(296, 72, 50, 22);
			jDesktopPane1.add(jTextField_NegT1_UOM);

			JLabel4j_std jLabel_NegT2 = new JLabel4j_std();
			jLabel_NegT2.setText(lang.get("lbl_NegT2"));
			jLabel_NegT2.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_NegT2.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_NegT2.setBounds(29, 104, 137, 22);
			jDesktopPane1.add(jLabel_NegT2);
			jTextField_NegT2.setVerifyInputWhenFocusTarget(false);
			jTextField_NegT2.setHorizontalAlignment(SwingConstants.TRAILING);
			jTextField_NegT2.setBounds(176, 104, 108, 22);
			jDesktopPane1.add(jTextField_NegT2);
			jTextField_NegT2_UOM.setText("");
			jTextField_NegT2_UOM.setPreferredSize(new Dimension(40, 20));
			jTextField_NegT2_UOM.setFocusCycleRoot(true);
			jTextField_NegT2_UOM.setCaretPosition(0);
			jTextField_NegT2_UOM.setBounds(296, 104, 50, 22);
			jDesktopPane1.add(jTextField_NegT2_UOM);

			jButtonSave.setEnabled(false);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void save()
	{
		tne.setNominalWT(ltneValue);
		tne.setNominalWTUOM(ltneUom);
		tne.setNegT1(jTextField_NegT1.getQuantity());
		tne.setNegT2(jTextField_NegT2.getQuantity());
		tne.setTNE(jTextField_TNE.getQuantity());

		tne.update();
		jButtonSave.setEnabled(false);
	}
}
