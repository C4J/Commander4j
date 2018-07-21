package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameQMDictionaryAdmin.java
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JMenuItem4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JExcel;

/**
 * The JInternalFrameQMDictionaryAdmin class allows a user manage a table called
 * APP_QM_DICTIONARY which field definitions which are used for data entry within the JDBQMResult input table/grid
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameQMDictionaryAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.app.JDialogQMDictionaryProperties JDialogQMDictionaryProperties
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 *
 */
public class JInternalFrameQMDictionaryAdmin extends javax.swing.JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JList4j<JDBQMDictionary> jListDictionary;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonEdit;
	private JButton4j jButtonDelete;
	private JButton4j jButtonAdd;
	private JScrollPane jScrollPane1;
	private String ltestid;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JInternalFrameQMDictionaryAdmin()
	{
		super();
		initGUI();
		populateList("", "");
	}

	private void editRecord()
	{
		if (jListDictionary.isSelectionEmpty() == false)
		{
			String testid = ((JDBQMDictionary) jListDictionary.getSelectedValue()).getTestID();
			JLaunchMenu.runDialog("FRM_QM_DICTIONARY", testid);
		}
	}

	private void addRecord()
	{
		JLaunchMenu.runDialog("FRM_QM_DICTIONARY", "");
	}

	private void populateList(String defaultid, String defaultval)
	{

		DefaultComboBoxModel<JDBQMDictionary> DefComboBoxMod = new DefaultComboBoxModel<JDBQMDictionary>();

		JDBQMDictionary tempSelectList = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);

		LinkedList<JDBQMDictionary> tempList = tempSelectList.getTests();
		int sel = -1;
		for (int j = 0; j < tempList.size(); j++)
		{
			JDBQMDictionary t = (JDBQMDictionary) tempList.get(j);
			DefComboBoxMod.addElement(t);
			if (t.getTestID().equals(defaultid))
			{
				sel = j;
			}
		}

		ListModel<JDBQMDictionary> jList1Model = DefComboBoxMod;
		jListDictionary.setModel(jList1Model);
		jListDictionary.setSelectedIndex(sel);
		jListDictionary.setCellRenderer(Common.renderer_list);
		jListDictionary.ensureIndexIsVisible(sel);
	}

	private void deleteRecord()
	{
		if (jListDictionary.isSelectionEmpty() == false)
		{
			ltestid = ((JDBQMDictionary) jListDictionary.getSelectedValue()).getTestID();
			int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Dictionary_Delete") + " " + ltestid + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (question == 0)
			{
				JDBQMDictionary u = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);
				u.setTestID(ltestid);
				u.delete();
				populateList("", "");
			}
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(455, 518));
			this.setBounds(0, 0, 522, 555);
			setVisible(true);
			this.setClosable(true);
			this.setIconifiable(true);
			this.setTitle("Dictionary Admin");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setLayout(null);
				{
					jScrollPane1 = new JScrollPane();
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 0, 363, 523);
					{
						ListModel<JDBQMDictionary> jList1Model = new DefaultComboBoxModel<JDBQMDictionary>();
						jListDictionary = new JList4j<JDBQMDictionary>();
						jScrollPane1.setViewportView(jListDictionary);
						jListDictionary.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									editRecord();
								}
							}
						});
						jListDictionary.setModel(jList1Model);

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jListDictionary, popupMenu);

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_add);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								newItemMenuItem.setEnabled(true);
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_delete);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										deleteRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Delete"));
								newItemMenuItem.setEnabled(true);
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_edit);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								newItemMenuItem.setEnabled(true);
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem4j newItemMenuItem = new JMenuItem4j(Common.icon_refresh);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										populateList("", "");
									}
								});
								newItemMenuItem.setText(lang.get("btn_Refresh"));
								popupMenu.add(newItemMenuItem);
							}
						}
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(375, 10, 125, 32);
					jButtonAdd.setEnabled(true);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							addRecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(375, 41, 125, 32);
					jButtonDelete.setEnabled(true);
					jButtonDelete.setFocusTraversalKeysEnabled(false);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deleteRecord();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(375, 72, 125, 32);
					jButtonEdit.setEnabled(true);
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}

				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.setBounds(375, 102, 125, 32);
					jButtonRefresh.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							populateList("", "");
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(375, 163, 125, 32);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});

					JButton4j btnExcel = new JButton4j(Common.icon_XLS);
					btnExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							excel();
						}
					});
					btnExcel.setText(lang.get("btn_Excel"));
					btnExcel.setMnemonic('0');
					btnExcel.setBounds(375, 132, 125, 32);
					jDesktopPane1.add(btnExcel);

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void excel()
	{
		JDBQMDictionary dict = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		PreparedStatement ps = dict.getDictionaryDataPreparedStatement();
		export.saveAs("qm_dictionary.xls", dict.getDictionaryDataResultSet(ps), Common.mainForm);
		try
		{
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
		populateList("","");
	}
	
	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */
	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
