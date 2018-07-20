package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameProcessOrderResourceAdmin.java
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
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialLocation;
import com.commander4j.db.JDBProcessOrderResource;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBProcessOrderResourceTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameProcessOrderResourceAdmin class updates the table
 * APP_PROCESS_ORDER_RESOURCE. This table contains a single row for each unique
 * Process Order Resource. This table holds additional data which is associated
 * with the factory resource. This table is automatically updated with resource
 * names when a new Process is imported via the interfaces. Typical data which
 * might be stored in this table include default batch numbers
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameProcessOrderResourceAdmin.jpg" >
 *
 * @see com.commander4j.db.JDBProcessOrderResource JDBProcessOrderResource
 * @see com.commander4j.db.JDBProcessOrder JDBProcessOrder
 *
 */
public class JInternalFrameProcessOrderResourceAdmin extends JInternalFrame
{
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonAdd;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JTextField4j jTextFieldDescription;
	private JLabel4j_std jLabelBatchSuffix;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabelSortBy;
	private JLabel4j_std jLabelDescription;
	private JTextField4j jTextFieldResource;
	private JLabel4j_std jLabelResource;
	private JTable jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lresource;
	private static boolean dlg_sort_descending = false;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private PreparedStatement listStatement;
	private JTextField4j jTextFieldBatchSuffix = new JTextField4j(20);
	private JCheckBox chckbxEnabled = new JCheckBox("");
	private JButton4j button4jClear;
	private JButton4j button4jDelete;

	public JInternalFrameProcessOrderResourceAdmin()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PROCESS_ORDER_RESOURCE where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_PROCESS_ORDER_RESOURCE"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldResource.requestFocus();
			}
		});
	}

	private void clearFilter()
	{

		jTextFieldResource.setText("");

		jTextFieldDescription.setText("");

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Process_Order_Required_Resource")) == true)
			{
				jTextFieldResource.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals(lang.get("lbl_Description")) == true)
			{
				jTextFieldDescription.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals(lang.get("lbl_Batch")) == true)
			{
				jTextFieldBatchSuffix.setText(jTable1.getValueAt(row, 2).toString());
			}

			search();

		}
	}

	public JInternalFrameProcessOrderResourceAdmin(String material)
	{
		this();
		lresource = material;
		jTextFieldResource.setText(lresource);
		buildSQL();
		populateList();
	}

	private void deleteRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			String lresource = jTable1.getValueAt(row, 0).toString();

			int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Resource_Delete") + " " + lresource, lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
			if (n == 0)
			{
				JDBProcessOrderResource por = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
				por.setResource(lresource);
				por.delete();
				search();
			}
		}
	}

	private void search()
	{
		buildSQL();
		populateList();
	}

	private void excel()
	{
		JDBMaterialLocation materialLocation = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("po_resources.xls", materialLocation.getMaterialLocationDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void addRecord()
	{
		String lresource = "";

		lresource = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Resource_Input"));
		if (lresource != null)
		{
			if (lresource.equals("") == false)
			{
				JDBProcessOrderResource por = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
				lresource = lresource.trim().toUpperCase();
				if (por.isValidResource(lresource) == false)
				{
					JLaunchMenu.runForm("FRM_ADMIN_PO_RESOURCE_EDIT", lresource);
				} else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Resource [" + lresource + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
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
			jToggleButtonSequence.setIcon(Common.icon_descending);
		} else
		{
			jToggleButtonSequence.setToolTipText("Ascending");
			jToggleButtonSequence.setIcon(Common.icon_ascending);
		}
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PROCESS_ORDER_RESOURCE"));
		query.addParamtoSQL("required_resource=", jTextFieldResource.getText());
		query.addParamtoSQL("description=", jTextFieldDescription.getText());
		query.addParamtoSQL("batch_suffix=", jTextFieldBatchSuffix.getText());

		if (chckbxEnabled.isSelected())
		{
			query.addParamtoSQL("enabled=", "Y");
		} else
		{
			query.addParamtoSQL("enabled=", "N");
		}

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateList()
	{
		JDBProcessOrderResource processOrderResource = new JDBProcessOrderResource(Common.selectedHostID, Common.sessionID);
		JDBProcessOrderResourceTableModel resourceTable = new JDBProcessOrderResourceTableModel(processOrderResource.getProcessOrderResourceDataResultSet(listStatement));
		TableRowSorter<JDBProcessOrderResourceTableModel> sorter = new TableRowSorter<JDBProcessOrderResourceTableModel>(resourceTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(resourceTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		// jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(230);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);
		jTable1.getColumnModel().getColumn(3).setPreferredWidth(60);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, resourceTable.getRowCount());
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lresource = jTable1.getValueAt(row, 0).toString();
			JLaunchMenu.runForm("FRM_ADMIN_PO_RESOURCE_EDIT", lresource);
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 520, 598);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Process Order Resources");
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new java.awt.Dimension(483, 266));
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(0, 196, 510, 345);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
								{ "One", "Two" },
								{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PO_RESOURCE_EDIT"))
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
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_search);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										search();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Search"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_add);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PO_RESOURCE_ADD"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										addRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Add"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_edit);
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PO_RESOURCE_EDIT"));
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										editRecord();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Edit"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenuItem newItemMenuItem = new JMenuItem(Common.icon_XLS);
								newItemMenuItem.addActionListener(new ActionListener()
								{
									public void actionPerformed(final ActionEvent e)
									{
										excel();
									}
								});
								newItemMenuItem.setText(lang.get("btn_Excel"));
								popupMenu.add(newItemMenuItem);
							}

							{
								final JMenu sortByMenu = new JMenu();
								sortByMenu.setText(lang.get("lbl_Sort_By"));
								popupMenu.add(sortByMenu);

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("REQUIRED_RESOURCE");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order_Required_Resource"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("BATCH_SUFFIX");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Batch"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("DESCRIPTION");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
									sortByMenu.add(newItemMenuItem);
								}

							}

							{
								final JMenu filterByMenu = new JMenu();
								filterByMenu.setText(lang.get("lbl_Filter_By"));
								popupMenu.add(filterByMenu);

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Process_Order_Required_Resource"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Batch"));
									filterByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											filterBy(newItemMenuItem.getText());
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Description"));
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
									newItemMenuItem.setText(lang.get("btn_Clear_Filter"));
									filterByMenu.add(newItemMenuItem);
								}
							}
						}
					}
				}
				{
					jButtonSearch = new JButton4j(Common.icon_search);
					jDesktopPane1.add(jButtonSearch);
					jButtonSearch.setText(lang.get("btn_Search"));
					jButtonSearch.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonSearch.setBounds(0, 123, 126, 32);
					jButtonSearch.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							search();

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(java.awt.event.KeyEvent.VK_E);
					jButtonEdit.setBounds(384, 123, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PO_RESOURCE_EDIT"));
					jButtonEdit.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
					jButtonHelp.setBounds(256, 153, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(384, 152, 126, 32);
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
					jLabelResource = new JLabel4j_std();
					jDesktopPane1.add(jLabelResource);
					jLabelResource.setText(lang.get("lbl_Process_Order_Required_Resource"));
					jLabelResource.setBounds(0, 10, 150, 21);
					jLabelResource.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldResource = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldResource);
					jTextFieldResource.setBounds(154, 10, 141, 21);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setBounds(0, 37, 150, 21);
					jLabelDescription.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setBounds(154, 37, 341, 21);
				}
				{
					jLabelSortBy = new JLabel4j_std();
					jDesktopPane1.add(jLabelSortBy);
					jLabelSortBy.setText(lang.get("lbl_Sort_By"));
					jLabelSortBy.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelSortBy.setBounds(0, 90, 150, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "REQUIRED_RESOURCE", "BATCH_SUFFIX", "DESCRIPTION", "ENABLED" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(154, 90, 162, 23);
				}
				{
					jLabelBatchSuffix = new JLabel4j_std();
					jDesktopPane1.add(jLabelBatchSuffix);
					jLabelBatchSuffix.setText(lang.get("lbl_Batch_Suffix"));
					jLabelBatchSuffix.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabelBatchSuffix.setBounds(0, 65, 150, 21);
				}

				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(320, 90, 21, 23);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_PO_RESOURCE_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(256, 123, 126, 32);
					jDesktopPane1.add(jButtonAdd);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 544, 510, 21);
					jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					jDesktopPane1.add(jStatusText);
				}

				{
					jButtonExcel = new JButton4j(Common.icon_XLS);
					jButtonExcel.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							excel();
						}
					});

					jButtonExcel.setText(lang.get("btn_Excel"));
					jButtonExcel.setMnemonic(lang.getMnemonicChar());
					jButtonExcel.setBounds(128, 153, 126, 32);
					jDesktopPane1.add(jButtonExcel);
				}

				jTextFieldBatchSuffix.setBounds(154, 64, 80, 21);
				jDesktopPane1.add(jTextFieldBatchSuffix);

				JLabel4j_std label4jEnabled = new JLabel4j_std();
				label4jEnabled.setText(lang.get("lbl_Enabled"));
				label4jEnabled.setHorizontalAlignment(SwingConstants.TRAILING);
				label4jEnabled.setBounds(234, 65, 135, 21);
				jDesktopPane1.add(label4jEnabled);
				chckbxEnabled.setSelected(true);

				chckbxEnabled.setBounds(373, 62, 29, 23);
				jDesktopPane1.add(chckbxEnabled);

				button4jClear = new JButton4j(Common.icon_clear);
				button4jClear.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jTextFieldResource.setText("");
						jTextFieldDescription.setText("");
						jTextFieldBatchSuffix.setText("");
						chckbxEnabled.setSelected(true);
						search();
					}
				});
				button4jClear.setText(lang.get("btn_Clear_Filter"));
				button4jClear.setMnemonic(KeyEvent.VK_C);
				button4jClear.setBounds(128, 123, 126, 32);
				jDesktopPane1.add(button4jClear);

				button4jDelete = new JButton4j(Common.icon_delete);
				button4jDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						deleteRecord();
					}
				});
				button4jDelete.setText(lang.get("btn_Delete"));
				button4jDelete.setMnemonic(KeyEvent.VK_D);
				button4jDelete.setBounds(0, 153, 126, 32);
				jDesktopPane1.add(button4jDelete);

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
