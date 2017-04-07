package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMaterialLocationAdmin.java
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialLocation;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.tablemodel.JDBMaterialLocationTableModel;
import com.commander4j.util.JExcel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameMaterialLocationAdmin class allows you to maintain the
 * table APP_MATERIAL_LOCATION. This table is optional validation used when
 * moving pallets into a new location. The system key SSCC_MATERIAL_LOCATION is
 * used to enable/disables its use by setting it's value to True or False.
 * 
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMaterialLocationAdmin.jpg" >
 * 
 * @see com.commander4j.db.JDBMaterialLocation JDBMaterialLocation
 * @see com.commander4j.app.JInternalFrameMaterialLocationProperties JInternalFrameMaterialLocationProperties
 * 
 */
public class JInternalFrameMaterialLocationAdmin extends JInternalFrame
{
	private JButton4j jButtonExcel;
	private JLabel4j_std jStatusText;
	private JButton4j jButtonAdd;
	private JButton4j jButtonLookupLocation;
	private JButton4j jButtonLookupMaterial;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonEdit;
	private JButton4j jButtonClose;
	private JToggleButton jToggleButtonSequence;
	private JTextField4j jTextFieldLocation;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel5;
	private JComboBox4j<String> jComboBoxSortBy;
	private JLabel4j_std jLabel10;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldMaterial;
	private JLabel4j_std jLabel1;
	private JTable jTable1;
	private JButton4j jButtonHelp;
	private JButton4j jButtonSearch;
	private JScrollPane jScrollPane1;
	private String lmaterial;
	private String lLocation;
	private static boolean dlg_sort_descending = false;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang;
	private PreparedStatement listStatement;

	public JInternalFrameMaterialLocationAdmin()
	{
		super();
		setIconifiable(true);
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL where 1=2"));
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		populateList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_LOCATION"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setSequence(dlg_sort_descending);
	}

	private void clearFilter()
	{

		jTextFieldMaterial.setText("");

		jTextFieldLocation.setText("");

		jComboBoxStatus.setSelectedItem("");

		search();

	}

	private void filterBy(String fieldname)
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{

			if (fieldname.equals(lang.get("lbl_Material")) == true)
			{
				jTextFieldMaterial.setText(jTable1.getValueAt(row, 0).toString());
			}

			if (fieldname.equals(lang.get("lbl_Material_Location")) == true)
			{
				jTextFieldLocation.setText(jTable1.getValueAt(row, 1).toString());
			}

			if (fieldname.equals("lbl_Material_Location_Status") == true)
			{
				jComboBoxStatus.setSelectedItem(jTable1.getValueAt(row, 2).toString());
			}

			search();

		}
	}

	public JInternalFrameMaterialLocationAdmin(String material)
	{
		this();
		lmaterial = material;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldLocation.setText(lLocation);
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
		JDBMaterialLocation materialLocation = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
		JExcel export = new JExcel();
		buildSQL();
		export.saveAs("material_location.xls", materialLocation.getMaterialLocationDataResultSet(listStatement), Common.mainForm);
		populateList();
	}

	private void addRecord()
	{
		String lmaterial = "";
		String llocation = "";
		lmaterial = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_Input"));
		if (lmaterial != null)
		{
			if (lmaterial.equals("") == false)
			{
				JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
				if (mat.isValidMaterial(lmaterial))
				{
					llocation = JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_Location_Input"));
					if (llocation != null)
					{
						if (llocation.equals("") == false)
						{
							JDBLocation locn = new JDBLocation(Common.selectedHostID, Common.sessionID);
							if (locn.isValidLocation(llocation))
							{
								JDBMaterialLocation matloc = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
								if (matloc.isValidMaterialLocation(lmaterial, llocation) == false)
								{
									JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_LOCATION_EDIT", lmaterial, llocation);
								} else
								{
									JOptionPane.showMessageDialog(Common.mainForm, "Material/Location [" + lmaterial + " / " + llocation + "] already exists", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
								}
							} else
							{
								JOptionPane.showMessageDialog(Common.mainForm, "Location [" + llocation + "] does not exist", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
							}
						}
					}
				} else
				{
					JOptionPane.showMessageDialog(Common.mainForm, "Material [" + lmaterial + "] does not exist", lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
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

	public JInternalFrameMaterialLocationAdmin(String material, String location)
	{
		this();
		lmaterial = material;
		lLocation = location;
		jTextFieldMaterial.setText(lmaterial);
		jTextFieldLocation.setText(lLocation);
		buildSQL();
		populateList();
	}

	private void buildSQL()
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_LOCATION"));
		query.addParamtoSQL("material=", jTextFieldMaterial.getText());
		query.addParamtoSQL("location_id=", jTextFieldLocation.getText());
		query.addParamtoSQL("status=", jComboBoxStatus.getSelectedItem().toString());

		query.appendSort(jComboBoxSortBy.getSelectedItem().toString(), jToggleButtonSequence.isSelected());
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
	}

	private void populateList()
	{
		JDBMaterialLocation materialLocation = new JDBMaterialLocation(Common.selectedHostID, Common.sessionID);
		JDBMaterialLocationTableModel materialLocationTable = new JDBMaterialLocationTableModel(materialLocation.getMaterialLocationDataResultSet(listStatement));
		TableRowSorter<JDBMaterialLocationTableModel> sorter = new TableRowSorter<JDBMaterialLocationTableModel>(materialLocationTable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(materialLocationTable);

		jScrollPane1.setViewportView(jTable1);
		JUtility.scrolltoHomePosition(jScrollPane1);
		// jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(118);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(118);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(145);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, materialLocationTable.getRowCount());
	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable1.getValueAt(row, 0).toString();
			lLocation = jTable1.getValueAt(row, 1).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_LOCATION_EDIT", lmaterial, lLocation);
		}

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(497, 522));
			this.setBounds(0, 0, 429 + Common.LFAdjustWidth, 598 + Common.LFAdjustHeight);
			setVisible(true);
			this.setClosable(true);
			this.setTitle("Material Locations");
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
					jScrollPane1.setBounds(0, 196, 402, 333);
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
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION_EDIT"))
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION_ADD"));
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
								newItemMenuItem.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION_EDIT"));
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
											sortBy("MATERIAL");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("LOCATION_ID");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Location"));
									sortByMenu.add(newItemMenuItem);
								}

								{
									final JMenuItem newItemMenuItem = new JMenuItem();
									newItemMenuItem.addActionListener(new ActionListener()
									{
										public void actionPerformed(final ActionEvent e)
										{
											sortBy("STATUS");
										}
									});
									newItemMenuItem.setText(lang.get("lbl_Material_Location_Status"));
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
									newItemMenuItem.setText(lang.get("lbl_Material"));
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
									newItemMenuItem.setText(lang.get("lbl_Location"));
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
									newItemMenuItem.setText(lang.get("lbl_Status"));
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
					jButtonSearch.setBounds(10, 125, 126, 32);
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
					jButtonEdit.setBounds(266, 125, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION_EDIT"));
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
					jButtonHelp.setBounds(138, 155, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.setBounds(266, 154, 126, 32);
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
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Material"));
					jLabel1.setBounds(0, 10, 91, 21);
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldMaterial = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldMaterial);
					jTextFieldMaterial.setBounds(99, 10, 141, 21);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Location_ID"));
					jLabel3.setBounds(0, 37, 91, 21);
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
				}
				{
					jTextFieldLocation = new JTextField4j(JDBLocation.field_location_id);
					jDesktopPane1.add(jTextFieldLocation);
					jTextFieldLocation.setBounds(99, 37, 141, 21);
				}
				{
					jLabel10 = new JLabel4j_std();
					jDesktopPane1.add(jLabel10);
					jLabel10.setText(lang.get("lbl_Sort_By"));
					jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel10.setBounds(0, 92, 91, 21);
				}
				{
					ComboBoxModel<String> jComboBoxSortByModel = new DefaultComboBoxModel<String>(new String[]
					{ "MATERIAL", "LOCATION_ID", "STATUS" });
					jComboBoxSortBy = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxSortBy);
					jComboBoxSortBy.setModel(jComboBoxSortByModel);
					jComboBoxSortBy.setBounds(99, 92, 141, 23);
				}
				{
					jLabel5 = new JLabel4j_std();
					jDesktopPane1.add(jLabel5);
					jLabel5.setText(lang.get("lbl_Material_Location_Status"));
					jLabel5.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel5.setBounds(0, 65, 91, 21);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(Common.locationStatusIncBlank);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(99, 65, 141, 23);
				}
				{
					jToggleButtonSequence = new JToggleButton();
					jDesktopPane1.add(jToggleButtonSequence);
					jToggleButtonSequence.setBounds(246, 92, 21, 21);
					jToggleButtonSequence.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							setSequence(jToggleButtonSequence.isSelected());
						}
					});
				}

				{
					jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
					jButtonLookupMaterial.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgAutoExec = false;
							JLaunchLookup.dlgCriteriaDefault = "";
							if (JLaunchLookup.materials())
							{
								jTextFieldMaterial.setText(JLaunchLookup.dlgResult);
							}
						}
					});
					jButtonLookupMaterial.setBounds(246, 10, 21, 21);
					jDesktopPane1.add(jButtonLookupMaterial);
				}

				{
					jButtonLookupLocation = new JButton4j(Common.icon_lookup);
					jButtonLookupLocation.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							JLaunchLookup.dlgCriteriaDefault = "Y";
							JLaunchLookup.dlgAutoExec = true;
							if (JLaunchLookup.locations())
							{
								jTextFieldLocation.setText(JLaunchLookup.dlgResult);
							}

						}
					});
					jButtonLookupLocation.setBounds(246, 37, 21, 21);
					jDesktopPane1.add(jButtonLookupLocation);
				}

				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_LOCATION_ADD"));
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(final ActionEvent e)
						{
							addRecord();
						}
					});
					jButtonAdd.setText(lang.get("btn_Add"));
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(138, 125, 126, 32);
					jDesktopPane1.add(jButtonAdd);
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 530, 402, 21);
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
					jButtonExcel.setBounds(10, 155, 126, 32);
					jDesktopPane1.add(jButtonExcel);
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
