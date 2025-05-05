package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameWTSamplePointAdmin.java
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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.Font;

/**
 * The JInternalFrameWTSamplePointAdmin class allows a user to maintain the table APP_WEIGHT_SAMPLE_POINT.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWTSamplePointAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBWTSamplePoint JDBWTSamplePoint
 * @see com.commander4j.app.JInternalFrameWTSamplePointAdmin JInternalFrameWTSamplePointAdmin
 *
 */
public class JInternalFrameWTSamplePointAdmin extends javax.swing.JInternalFrame
{
	private JButton4j jButtonExcel;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBWTSamplePoint> jListSamplePoints;
	private JButton4j jButtonRename;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String lSamplePoint;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std lblSamplePointDescription;

	private void addrecord() {
		JDBWTSamplePoint u = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
		lSamplePoint = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_SamplePoint_Add"));
		if (lSamplePoint != null)
		{
			if (lSamplePoint.equals("") == false)
			{
				lSamplePoint = lSamplePoint.toUpperCase();
				if (u.create(lSamplePoint) == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
				}
				else
				{
					JLaunchMenu.runForm("FRM_WEIGHT_SAMPLEPOINT_EDIT", lSamplePoint);
				}
				populateList(lSamplePoint);
			}
		}
	}

	private void populateList(String defaultitem) {

		DefaultComboBoxModel<JDBWTSamplePoint> DefComboBoxMod = new DefaultComboBoxModel<JDBWTSamplePoint>();

		JDBWTSamplePoint tempSample = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBWTSamplePoint> tempSampleList = tempSample.getSamplePoints(JDBWTSamplePoint.displayType_LIST);
		int sel = -1;
		for (int j = 0; j < tempSampleList.size(); j++)
		{
			JDBWTSamplePoint t = (JDBWTSamplePoint) tempSampleList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getSamplePoint().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBWTSamplePoint> jList1Model = DefComboBoxMod;
		jListSamplePoints.setModel(jList1Model);
		jListSamplePoints.setSelectedIndex(sel);
		jListSamplePoints.setCellRenderer(Common.renderer_list);
		jListSamplePoints.ensureIndexIsVisible(sel);
	}

	private void editRecord() {
		if (jListSamplePoints.isSelectionEmpty() == false)
		{
			lSamplePoint = ((JDBWTSamplePoint) jListSamplePoints.getSelectedValue()).getSamplePoint();
			JLaunchMenu.runForm("FRM_WEIGHT_SAMPLEPOINT_EDIT", lSamplePoint);
		}
	}

	private void delete() {
		if (jListSamplePoints.isSelectionEmpty() == false)
		{
			lSamplePoint = ((JDBWTSamplePoint) jListSamplePoints.getSelectedValue()).getSamplePoint();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_SamplePoint_Delete") + " " + lSamplePoint + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (question == 0)
			{
				JDBWTSamplePoint u = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
				u.setSamplePoint(lSamplePoint);
				u.delete();
				populateList("");
			}
		}
	}

	private void print() {
		JLaunchReport.runReport("RPT_SAMPLEPOINTS",null,"",null,"");
	}

	private void rename() {
		if (jListSamplePoints.isSelectionEmpty() == false)
		{
			String lsample_from = ((JDBWTSamplePoint) jListSamplePoints.getSelectedValue()).getSamplePoint();
			String lsample_to = new String();
			lsample_to = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_SamplePoint_Rename"));
			if (lsample_to != null)
			{
				if (lsample_to.equals("") == false)
				{
					lsample_to = lsample_to.toUpperCase();
					JDBWTSamplePoint u = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
					u.setSamplePoint(lsample_from);
					if (u.renameTo(lsample_to) == false)
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, u.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm_16x16);
					}
					populateList(lsample_to);
				}
			}
		}
	}

	private void excel() {
		JDBWTSamplePoint samp = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		export.saveAs("sample_points.xls", samp.getSamplePointDataResultSet(), Common.mainForm);
		populateList("");
	}

	public JInternalFrameWTSamplePointAdmin()
	{
		super();
		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_WEIGHT_SAMPLEPOINT"));
		{
			lblSamplePointDescription = new JLabel4j_std("Sample Point             Description              Resource            Reporting Group");
			lblSamplePointDescription.setFont(new Font("Monospaced", Font.BOLD, 11));
			lblSamplePointDescription.setBounds(8, 12, 659, 15);
			jDesktopPane1.add(lblSamplePointDescription);
		}
		populateList("");
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 851, 443);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Sample Point Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(6, 27, 686, 374);
					{
						ListModel<JDBWTSamplePoint> jList1Model = new DefaultComboBoxModel<JDBWTSamplePoint>();
						jListSamplePoints = new JList4j<JDBWTSamplePoint>();
						jScrollPane1.setViewportView(jListSamplePoints);
						jListSamplePoints.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_EDIT") == true)
									{
										editRecord();
									}
								}
							}
						});
						jListSamplePoints.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListSamplePoints, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										addrecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_ADD"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										delete();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_DELETE"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_EDIT"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_rename_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										rename();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Rename"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_RENAME"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_print_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										print();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Print"));
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_UNITS"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh_16x16);
								newItemMenuItem.addActionListener(new ActionListener() {
									public void actionPerformed(final ActionEvent e) {
										populateList("");
									}
								});
								newItemMenuItem.setText(lang.get("btn_Refresh"));
								popupMenu.add(newItemMenuItem);
							}
						}
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(704, 27, 125, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							addrecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(704, 58, 125, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_DELETE"));
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							delete();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(704, 89, 125, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();
						}
					});
				}
				{
					jButtonRename = new JButton4j(Common.icon_rename_16x16);
					jDesktopPane1.add(jButtonRename);
					jButtonRename.setText(lang.get("btn_Rename"));
					jButtonRename.setMnemonic(lang.getMnemonicChar());
					jButtonRename.setBounds(704, 120, 125, 32);
					jButtonRename.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_WEIGHT_SAMPLEPOINT_RENAME"));
					jButtonRename.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							rename();

						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_print_16x16);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(704, 151, 125, 32);
					jButtonPrint.setEnabled(true);
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							print();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(704, 244, 125, 32);
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh_16x16);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(704, 213, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							populateList("");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(704, 275, 125, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(704, 182, 125, 32);
					jButtonExcel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							excel();
						}
					});
					jDesktopPane1.add(jButtonExcel);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
