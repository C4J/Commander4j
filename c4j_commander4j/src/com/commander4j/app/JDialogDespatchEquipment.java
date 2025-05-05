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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBDespatchEquipmentTypes;
import com.commander4j.db.JDBEquipmentType;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This Dialog box allow you to assign an alternative report module based on
 * workstation name.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogModuleAlternative.jpg" >
 * 
 */

public class JDialogDespatchEquipment extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonEdit;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBDespatch despatch = new JDBDespatch(Common.selectedHostID, Common.sessionID);
	private JList4j<JDBDespatchEquipmentTypes> listDespatchEquipment = new JList4j<JDBDespatchEquipmentTypes>();
	private String selectedDespatch = "";
	private int initialQuantity = -1;
	private int currentQuantity = -1;

	private JLabel4j_std jStatusText = new JLabel4j_std("");
	private JTextField4j textFieldNoOfPallets = new JTextField4j();

	public JDialogDespatchEquipment(JFrame frame, String despatch)
	{
		super(frame, "Equipment assigned to " + despatch, ModalityType.DOCUMENT_MODAL);

		selectedDespatch = despatch;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setResizable(false);
		this.setSize(323, 371);

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
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_DESPATCH_EQUIPMENT"));

		initGUI();
		
		

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

				JScrollPane scrollPanelDespatchEquipment = new JScrollPane();
				scrollPanelDespatchEquipment.setBounds(23, 32, 275, 190);
				jDesktopPane1.add(scrollPanelDespatchEquipment);
				listDespatchEquipment.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent evt)
					{
						if (evt.getClickCount() == 2)
						{
							editRecord();
						}
					}
				});

				listDespatchEquipment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPanelDespatchEquipment.setViewportView(listDespatchEquipment);

				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Equipment", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
				panel_1.setBounds(12, 12, 299, 291);
				jDesktopPane1.add(panel_1);
				panel_1.setLayout(null);

				JButton4j jButtonAdd = new JButton4j(Common.icon_add_16x16);
				jButtonAdd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						addRecord();
					}
				});
				jButtonAdd.setBounds(12, 248, 32, 32);
				panel_1.add(jButtonAdd);

				JButton4j jButtonDelete = new JButton4j(Common.icon_delete_16x16);
				jButtonDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteRecord();
					}
				});
				jButtonDelete.setBounds(44, 248, 32, 32);
				panel_1.add(jButtonDelete);

				JButton4j jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
				jButtonRefresh.setBounds(108, 248, 32, 32);
				panel_1.add(jButtonRefresh);
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(140, 248, 32, 32);
					panel_1.add(jButtonHelp);
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jButtonEdit.setBounds(76, 248, 32, 32);
					panel_1.add(jButtonEdit);
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							editRecord();
						}
					});
					jButtonEdit.setEnabled(true);
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setHorizontalTextPosition(SwingConstants.RIGHT);

				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(172, 248, 112, 32);
					panel_1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());

					JLabel4j_std lbl_NoOfPallets = new JLabel4j_std();
					lbl_NoOfPallets.setText(lang.get("lbl_No_Of_Pallets"));
					lbl_NoOfPallets.setHorizontalAlignment(SwingConstants.TRAILING);
					lbl_NoOfPallets.setBounds(48, 218, 120, 22);
					panel_1.add(lbl_NoOfPallets);

					textFieldNoOfPallets.setHorizontalAlignment(SwingConstants.CENTER);
					textFieldNoOfPallets.setBounds(179, 219, 41, 22);
					textFieldNoOfPallets.setEditable(false);
					panel_1.add(textFieldNoOfPallets);

					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (initialQuantity != currentQuantity)
							{
								despatch.updateTotalEquipment(selectedDespatch, currentQuantity);
							}
							
							dispose();
						}
					});

					jStatusText.setBounds(0, 315, 323, 21);
					jStatusText.setForeground(new java.awt.Color(255, 0, 0));
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);

				}

				jButtonRefresh.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						refreshDespatchEquipmentList();
					}
				});

			}

			populateDespatchEquipmentType(selectedDespatch, "");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void refreshDespatchEquipmentList()
	{
		String defaultItem = "";
		jStatusText.setText("");

		if (listDespatchEquipment.isSelectionEmpty() == false)
		{
			defaultItem = ((JDBDespatchEquipmentTypes) listDespatchEquipment.getSelectedValue()).getEquipmentType();
		}
		populateDespatchEquipmentType(selectedDespatch, defaultItem);
	}

	private void editRecord()
	{
		jStatusText.setText("");
		if (listDespatchEquipment.isSelectionEmpty() == false)
		{
			String item = ((JDBDespatchEquipmentTypes) listDespatchEquipment.getSelectedValue()).getEquipmentType();

			JDBDespatchEquipmentTypes po = new JDBDespatchEquipmentTypes(Common.selectedHostID, Common.sessionID);

			if (po.getDespatchEquipmentTypeProperties(selectedDespatch, item))
			{
				String strQuantity = (String) JOptionPane.showInputDialog(lang.get("lbl_Quantity_of") + " " + item, po.getQuantity());

				if (strQuantity != null)
				{
					if (strQuantity.equals("") == false)
					{
						if (StringUtils.isNumeric(strQuantity))
						{
							po.setQuantity(Integer.valueOf(strQuantity));
							po.update();
							populateDespatchEquipmentType(selectedDespatch, item);
						}
					}
				}
			}
		}
	}

	private void deleteRecord()
	{
		jStatusText.setText("");
		if (listDespatchEquipment.isSelectionEmpty() == false)
		{
			String item = ((JDBDespatchEquipmentTypes) listDespatchEquipment.getSelectedValue()).getEquipmentType();

			int n = JOptionPane.showConfirmDialog(this, lang.get("btn_Delete") + " " + lang.get("lbl_Material_Equipment_Type") + " [" + item + "]", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBDespatchEquipmentTypes po = new JDBDespatchEquipmentTypes(Common.selectedHostID, Common.sessionID);

				po.setDespatchNo(selectedDespatch);
				po.setEquipmentType(item);
				po.delete();

				populateDespatchEquipmentType(selectedDespatch, item);
				;
			}
		}
	}

	private void addRecord()
	{
		jStatusText.setText("");
		JDBEquipmentType u = new JDBEquipmentType(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBEquipmentType> custList = u.getEquipmentTypeList(true, JDBEquipmentType.displayModeFull);

		String[] customerList = new String[custList.size()];

		for (int x = 0; x < custList.size(); x++)
		{
			customerList[x] = custList.get(x).getEquipmentType();
		}

		if (custList.size() > 0)
		{

			String lequiptype = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Select_Equipment_Type"), lang.get("btn_Select"), JOptionPane.PLAIN_MESSAGE, Common.icon_confirm_16x16, customerList, customerList[0]);

			if (lequiptype != null)
			{
				if (lequiptype.equals("") == false)
				{
					lequiptype = lequiptype.toUpperCase();

					if (u.isValidEquipmentType(lequiptype))
					{
						JDBDespatchEquipmentTypes po = new JDBDespatchEquipmentTypes(Common.selectedHostID, Common.sessionID);

						if (po.getDespatchEquipmentTypeProperties(selectedDespatch, lequiptype) == false)
						{
							String strQuantity = (String) JOptionPane.showInputDialog(lang.get("lbl_Quantity_of") + " " + lequiptype, 1);

							if (strQuantity != null)
							{
								if (strQuantity.equals("") == false)
								{
									po.setDespatchNo(selectedDespatch);
									po.setEquipmentType(lequiptype);
									po.setQuantity(Integer.valueOf(strQuantity));
									po.create();
									populateDespatchEquipmentType(selectedDespatch, lequiptype);
									;
								}
							}
						}
						else
						{
							jStatusText.setText(lequiptype + " already in list.");
						}

					}
				}
			}

		}
		else
		{
			JOptionPane.showMessageDialog(Common.mainForm, "No Equipment Types defined.", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void populateDespatchEquipmentType(String defaultDespatch, String defaultEquip)
	{
		jStatusText.setText("");
		currentQuantity = 0;
		DefaultComboBoxModel<JDBDespatchEquipmentTypes> defComboBoxMod = new DefaultComboBoxModel<JDBDespatchEquipmentTypes>();

		JDBDespatchEquipmentTypes altMod = new JDBDespatchEquipmentTypes(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBDespatchEquipmentTypes> tempEquipList = altMod.getDespatchEquipmentTypeList(defaultDespatch, JDBDespatchEquipmentTypes.displayModeFull);

		int sel = -1;
		for (int j = 0; j < tempEquipList.size(); j++)
		{
			defComboBoxMod.addElement(tempEquipList.get(j));
			if (tempEquipList.get(j).getEquipmentType().equals(defaultEquip))
			{
				sel = j;
			}
			currentQuantity = currentQuantity + tempEquipList.get(j).getQuantity();

		}

		ListModel<JDBDespatchEquipmentTypes> jList1Model = defComboBoxMod;

		listDespatchEquipment.setModel(jList1Model);
		listDespatchEquipment.setSelectedIndex(sel);
		
		if (initialQuantity == -1)
		{
			initialQuantity = currentQuantity;
		}

		textFieldNoOfPallets.setText(String.valueOf(currentQuantity));
	}
}
