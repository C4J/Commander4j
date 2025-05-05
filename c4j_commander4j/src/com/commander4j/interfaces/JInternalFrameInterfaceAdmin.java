package com.commander4j.interfaces;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameInterfaceAdmin.java
 * 
 * Package Name : com.commander4j.interfaces
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTable4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.gui.JToggleButton4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBInterfaceTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * JInternalFrameInterfaceAdmin is used to administer the XML interfaces used by
 * the application, both input and output. Any modifications are performed by
 * the form JInternalFrameInterfaceProperties. Interface configuration is stored
 * in a table SYS_INTERFACE.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameInterfaceAdmin.jpg" >
 * 
 * @see com.commander4j.interfaces.JInternalFrameInterfaceProperties JInternalFrameInterfaceProperties
 * @see com.commander4j.db.JDBInterface JDBInterface
 * @see com.commander4j.db.JDBInterfaceLog JDBInterfaceLog
 */
public class JInternalFrameInterfaceAdmin extends JInternalFrame
{
	private JButton4j jButtonDelete;
	private JButton4j jButtonExcel;
	private JButton4j jButtonClear;
	private JLabel4j_std jStatusBar;
	private JButton4j jButtonAdd;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton4j jToggleButtonSequence;
	private JTextField4j jTextFieldPath;
	private JComboBox4j<String> jComboBoxInterfaceDirection;
	private JLabel4j_std jLabel5;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JLabel4j_std jLabel1;
	private JTable4j jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String ltype;
	private String ldirection;
	private static boolean dlg_sort_descending = false;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private PreparedStatement listStatement;
	private JComboBox4j<String> comboBoxInterfaceType = new JComboBox4j<String>();

	public JInternalFrameInterfaceAdmin()
	{
		super();
		setIconifiable(true);
		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_INTERFACE where 1=2"));
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_INTERFACE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter()
	{

		comboBoxInterfaceType.setSelectedItem("");

		jTextFieldPath.setText("");

		jComboBoxInterfaceDirection.setSelectedItem("");

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals("INTERFACE_TYPE") == true)
			{
				comboBoxInterfaceType.setSelectedItem(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals("PATH") == true)

			{
				jTextFieldPath.setText(jTable1.getValueAt(row, 5).toString());
			}

			if (fieldname.equals("INTERFACE_DIRECTION") == true)
			{
				jComboBoxInterfaceDirection.setSelectedItem(jTable1.getValueAt(row, 1).toString());
			}

			search();

		}
	}

	public JInternalFrameInterfaceAdmin(String material)
	{
		this();
		ltype = material;
		comboBoxInterfaceType.setSelectedItem("");
		jTextFieldPath.setText(ldirection);
		buildSQL();
		populateList();
	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void excel()
	{
		JDBInterface interfaceConfig = new JDBInterface(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("interface.xls", interfaceConfig.getInterfaceDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void addRecord()
	{
		String ltype = "";
		String ldirection = "";
		ltype = (String) JOptionPane.showInputDialog(Common.mainForm, "Interface", "Type", JOptionPane.PLAIN_MESSAGE, Common.icon_interface_16x16, Common.messageTypesexclBlank, "Interface Definition");

		if (ltype != null)
		{
			if (ltype.equals("") == false)
			{
				Object[] directions =
				{ "Input", "Output" };
				ldirection = (String) JOptionPane.showInputDialog(Common.mainForm, "Interface", "Direction", JOptionPane.PLAIN_MESSAGE, Common.icon_interface_16x16, directions, "Output");

				if (ldirection != null)
				{
					if (ldirection.equals("") == false)
					{
						JDBInterface matbat = new JDBInterface(Common.selectedHostID, Common.sessionID);
						if (matbat.isValidInterface(ltype, ldirection) == false)
						{
							JLaunchMenu.runForm("FRM_ADMIN_INTERFACE_EDIT", ltype, ldirection);
						} else
						{
							JOptionPane.showMessageDialog(Common.mainForm, "Interface [" + ltype + " / " + ldirection + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}

	}

	private void deleteRecord()
	{
		String ltype = "";
		String ldirection = "";
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			ltype = jTable1.getValueAt(row, 0).toString();
			ldirection = jTable1.getValueAt(row, 1).toString();
			int n = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Interface Config [" + ltype + "] [" + ldirection + "] ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
			if (n == 0)
			{
				JDBInterface l = new JDBInterface(Common.selectedHostID, Common.sessionID);
				l.setInterfaceType(ltype);
				l.setInterfaceDirection(ldirection);
				boolean result = l.delete();
				if (result == false)
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(Common.mainForm, l.getErrorMessage(), "Delete error [" + ltype + "] [" + ldirection + "]", JOptionPane.WARNING_MESSAGE, Common.icon_confirm_16x16);
				} else
				{
					buildSQL();
					populateList();
				}
			}
		}
	}

	private void sortBy(String fieldname)
	{
		jComboBoxSortBy.setSelectedItem(fieldname);
		search();
	}

	private void setSequence(boolean descending)
	{
		jToggleButtonSequence.setSelected(descending);
		if (jToggleButtonSequence.isSelected() == true)
		{
			jToggleButtonSequence.setToolTipText("Descending");
			jToggleButtonSequence.setIcon(Common.icon_descending_16x16);
		} else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending_16x16);
		}
	}

	public JInternalFrameInterfaceAdmin(String type, String direction)
	{
		this();
		ltype = type;
		ldirection = direction;
		comboBoxInterfaceType.setSelectedItem(ltype);
		jTextFieldPath.setText(ldirection);
		buildSQL();
		populateList();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}SYS_INTERFACE"));
		
		if (comboBoxInterfaceType.getSelectedItem().toString().equals("") == false)
		{
			query.addParamtoSQL("INTERFACE_TYPE=", comboBoxInterfaceType.getSelectedItem().toString());
		}
		
		query.addParamtoSQL("PATH=", jTextFieldPath.getText());
		
		query.addParamtoSQL("INTERFACE_DIRECTION=", jComboBoxInterfaceDirection.getSelectedItem().toString());

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false, Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), 1000);

		query.bindParams();
		
		listStatement = query.getPreparedStatement();
	}

	private void populateList()
	{
		JDBInterface interfaceConfig = new JDBInterface(Common.selectedHostID, Common.sessionID);
		JDBInterfaceTableModel interfaceConfigTable = new JDBInterfaceTableModel(interfaceConfig.getInterfaceDataResultSet(listStatement));
		TableRowSorter<JDBInterfaceTableModel> sorter = new TableRowSorter<JDBInterfaceTableModel>(interfaceConfigTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(interfaceConfigTable);

		jScrollPane1.setViewportView(jTable1);


		jTable1.getColumnModel().getColumn(0).setPreferredWidth(200);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(3).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(4).setPreferredWidth(60);
		jTable1.getColumnModel().getColumn(5).setPreferredWidth(440);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusBar, false, 0, interfaceConfigTable.getRowCount());
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			ltype = jTable1.getValueAt(row, 0).toString();
			ldirection = jTable1.getValueAt(row, 1).toString();
			JLaunchMenu.runForm("FRM_ADMIN_INTERFACE_EDIT", ltype, ldirection);
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 976, 576);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Interface Configuration");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.setBounds(0, 187, 955, 327);
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable1 = new JTable4j();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);

						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_EDIT"))
									{
										editRecord();
									}
								}
							}
						});

						{
							final JPopupMenu popupMenu = new JPopupMenu();
							addPopup(jTable1, popupMenu);

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_search_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										search();
									}
								});
								newItemMenuItem.setText("Search");
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_add_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_ADD"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText("Add");
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_delete_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_DELETE"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										deleteRecord();
									}
								});
								newItemMenuItem.setText("Delete");
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_edit_16x16);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText("Edit");
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_XLS_16x16);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										excel();
									}
								});
								newItemMenuItem.setText("Excel");
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenu sortByMenu = new JMenu();
								sortByMenu.setText("Sort by");
								popupMenu.add(sortByMenu);

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("INTERFACE_TYPE");
										}
									});
									newItemMenuItem.setText("Interface Type");
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("INTERFACE_DIRECTION");
										}
									});
									newItemMenuItem.setText("Interface Direction");
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("PATH");
										}
									});
									newItemMenuItem.setText("Path");
									sortByMenu.add(newItemMenuItem);
								}

							}

							{
								final JMenu filterByMenu = new JMenu();
								filterByMenu.setText("Filter by");
								popupMenu.add(filterByMenu);

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("INTERFACE_TYPE");
										}
									});
									newItemMenuItem.setText("Interface Type");
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("INTERFACE_DIRECTION");
										}
									});
									newItemMenuItem.setText("Interface Direction");
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy("PATH");
										}
									});
									newItemMenuItem.setText("Path");
									filterByMenu.add(newItemMenuItem);
								}

								{
									filterByMenu.addSeparator();
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											clearFilter();
										}
									});
									newItemMenuItem.setText("Clear Filter");
									filterByMenu.add(newItemMenuItem);
								}
							}
						}
					}
				}
				{
					comboBoxInterfaceType.setMaximumRowCount(20);

					comboBoxInterfaceType.setModel(new DefaultComboBoxModel<String>(Common.messageTypesincBlank));
					comboBoxInterfaceType.setBounds(152, 11, 210, 22);
					jDesktopPane1.add(comboBoxInterfaceType);
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search_16x16);
					jButtonSearch.setBounds(2, 143, 118, 32);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit_16x16);
					jButtonEdit.setBounds(482, 143, 118, 32);

					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jButtonHelp.setBounds(722, 143, 118, 32);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jButtonClose.setBounds(842, 143, 118, 32);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							JDBQuery.closeStatement(listStatement);
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jLabel1.setBounds(12, 11, 133, 22);
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Interface_Type"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jLabel3 = new JLabel4j_std();
					jLabel3.setBounds(12, 77, 133, 22);
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Interface_Path"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldPath = new JTextField4j();
					jTextFieldPath.setBounds(152, 77, 694, 22);
					jDesktopPane1.add(jTextFieldPath);
				}
				{
					jLabel10 = new JLabel4j_std();
					jLabel10.setBounds(12, 110, 133, 22);
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "INTERFACE_TYPE", "INTERFACE_DIRECTION", "PATH" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jComboBoxSortBy.setBounds(152, 110, 209, 22);
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
				}
				{
					jLabel5 = new JLabel4j_std();
					jLabel5.setBounds(12, 44, 133, 22);
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Interface_Direction"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(new String[]
					{ "", "Input", "Output" });
					jComboBoxInterfaceDirection = new JComboBox4j<String>();
					jComboBoxInterfaceDirection.setBounds(152, 44, 134, 22);
					jDesktopPane1.add(jComboBoxInterfaceDirection);
					jComboBoxInterfaceDirection.setModel(jComboBoxStatusModel);
				}
				{
					jToggleButtonSequence = new JToggleButton4j();
					jToggleButtonSequence.setBounds(361, 110, 22, 22);
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					jButtonAdd.setBounds(242, 143, 118, 32);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					jButtonAdd.setMnemonic(KeyEvent.VK_A);
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_ADD"));
					jDesktopPane1.add(jButtonAdd);
				}

				{
					jStatusBar = new JLabel4j_std();
					jStatusBar.setBounds(0, 519, 966, 22);
					jStatusBar.setForeground(new Color(255, 0, 0));
					jStatusBar.setBackground(Color.GRAY);
					jStatusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusBar);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS_16x16);
					jButtonExcel.setBounds(602, 143, 118, 32);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});
					jButtonExcel.setMnemonic(KeyEvent.VK_Z);
					jButtonExcel.setText(lang.get("btn_Excel"));
					jDesktopPane1.add(jButtonExcel);
				}
				
				{
					jButtonClear = new JButton4j(Common.icon_clear_16x16);
					jButtonClear.setBounds(122, 143, 118, 32);
					jButtonClear.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							clearFilter();
						}
					});
					jButtonClear.setMnemonic(KeyEvent.VK_Z);
					jButtonClear.setText(lang.get("btn_Clear_Filter"));
					jDesktopPane1.add(jButtonClear);
				}

				{
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					jButtonDelete.setBounds(362, 143, 118, 32);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							deleteRecord();
						}
					});
					jButtonDelete.setMnemonic(KeyEvent.VK_D);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_INTERFACE_DELETE"));
					jDesktopPane1.add(jButtonDelete);
				}
			}
		} catch (Exception e)
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
