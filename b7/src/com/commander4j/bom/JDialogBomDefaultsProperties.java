package com.commander4j.bom;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogBomDefaultsProperties.java
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

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JDialogBomDefaultsProperties extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelSourceValue;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldDestinationValue;
	private JTextField4j jTextFieldSourceValue;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabelDestinationValue;
	private String lsourcefield;
	private String lsourcevalue;
	private String ldestinationfield;
	private JDBBomDefaults bomdefaults = new JDBBomDefaults(Common.selectedHostID, Common.sessionID);
	private JDBBomElement elem = new JDBBomElement(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JDialogBomDefaultsProperties(JFrame parent, String sourceField,String sourceValue,String destinationField)
	{

		super(parent,"BOM Default Values",ModalityType.APPLICATION_MODAL);
		
		lsourcefield = sourceField;
		lsourcevalue = sourceValue;
		ldestinationfield = destinationField;

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_DEFAULTS_EDIT"));

		jLabelSourceValue.setText(elem.getElementDescription(sourceField));
		
		jTextFieldSourceValue.setText(sourceValue);
		
		jLabelDestinationValue.setText(elem.getElementDescription(destinationField));
						
		bomdefaults.getProperties(lsourcefield,lsourcevalue,destinationField);

		jTextFieldDestinationValue.setText(bomdefaults.getDestinationValue());

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDestinationValue.requestFocus();
				jTextFieldDestinationValue.setCaretPosition(jTextFieldDestinationValue.getText().length());
			}
		});
	}

	private void initGUI()
	{
		try
		{
			// setDefaultLookAndFeelDecorated(true);
			setPreferredSize(new java.awt.Dimension(460, 163));
			this.setBounds(25, 25, 437, 167);
			setModal(true);
			this.setTitle("Bill of Material Defaults");
			getContentPane().setLayout(null);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 433, 172);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new Dimension(452, 140));
				jDesktopPane1.setLayout(null);
				{
					jLabelDestinationValue = new JLabel4j_std();
					jLabelDestinationValue.setBounds(9, 48, 138, 22);
					jDesktopPane1.add(jLabelDestinationValue);
					jLabelDestinationValue.setText(lang.get("lbl_Destination_Value"));
					jLabelDestinationValue.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDestinationValue.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelSourceValue = new JLabel4j_std();
					jLabelSourceValue.setBounds(10, 19, 137, 22);
					jDesktopPane1.add(jLabelSourceValue);
					jLabelSourceValue.setText(lang.get("lbl_Source_Value"));
					jLabelSourceValue.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSourceValue.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jButtonUpdate.setBounds(48, 85, 110, 32);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (bomdefaults.getProperties(lsourcefield,lsourcevalue,ldestinationfield))
							{
								bomdefaults.update(lsourcefield,lsourcevalue,ldestinationfield,jTextFieldDestinationValue.getText());
							}
							else
							{
								bomdefaults.create(lsourcefield,lsourcevalue,ldestinationfield,jTextFieldDestinationValue.getText());
							}
							jButtonUpdate.setEnabled(false);
							dispose();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(269, 85, 110, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jTextFieldSourceValue = new JTextField4j(JDBControl.field_key_value);
					jTextFieldSourceValue.setBounds(159, 19, 252, 22);
					jDesktopPane1.add(jTextFieldSourceValue);
					jTextFieldSourceValue.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldSourceValue.setFocusCycleRoot(false);
					jTextFieldSourceValue.setEditable(false);

				}
				
				{
					jTextFieldDestinationValue = new JTextField4j(JDBControl.field_description);
					jTextFieldDestinationValue.setBounds(159, 48, 252, 22);
					jDesktopPane1.add(jTextFieldDestinationValue);
					jTextFieldDestinationValue.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDestinationValue.setFocusCycleRoot(true);
					jTextFieldDestinationValue.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(163, 85, 100, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
