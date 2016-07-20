package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.util.LinkedList; 

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBDataIDs;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMaterialCustomerDataTableModel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMaterialCustomerDataAdmin extends javax.swing.JInternalFrame {
	private JLabel4j_std jStatusText;
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonAdd;
	private JButton4j jButtonClose;
	private JButton4j jButtonRefresh;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JButton4j jButtonDelete;
	private JButton4j jButtonEdit;
	private JTable jTable1;
	private JScrollPane jScrollPane1;
	private String lmaterial;
	private String lcustomer;
	private String ldataid;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;

	public JInternalFrameMaterialCustomerDataAdmin(String material) {

		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_CUST_DATA"));
		lmaterial = material;
		populateList(lmaterial);

	}

	private void populateList(String criteria)
	{

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_CUSTOMER_DATA"));
		query.addParamtoSQL("material=", criteria);
		query.appendSort("material,customer_id,data_id", "asc");
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();

		JDBMaterialCustomerData materialcustdata = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
		JDBMaterialCustomerDataTableModel materialcustdatatable = new JDBMaterialCustomerDataTableModel(materialcustdata.getMaterialDataResultSet(listStatement));
		TableRowSorter<JDBMaterialCustomerDataTableModel> sorter = new TableRowSorter<JDBMaterialCustomerDataTableModel>(materialcustdatatable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(materialcustdatatable);
		jScrollPane1.setViewportView(jTable1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setFont(Common.font_list);

		jTable1.getColumnModel().getColumn(JDBMaterialCustomerDataTableModel.Material_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBMaterialCustomerDataTableModel.Customer_Col).setPreferredWidth(100);
		jTable1.getColumnModel().getColumn(JDBMaterialCustomerDataTableModel.Data_ID_Col).setPreferredWidth(120);
		jTable1.getColumnModel().getColumn(JDBMaterialCustomerDataTableModel.Data_Col).setPreferredWidth(340);

		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, materialcustdatatable.getRowCount());

	}

	private void editRecord()
	{
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			lmaterial = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Material_Col).toString();
			lcustomer = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Customer_Col).toString();
			ldataid = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Data_ID_Col).toString();
			JLaunchMenu.runDialog("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT", lmaterial, lcustomer, ldataid);
			populateList(lmaterial);
		}
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(625, 245));
			this.setBounds(0, 0, 852, 279);
			setVisible(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.setLayout(null);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(7, 7, 665, 200);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][]
						{
						{ "One", "Two" },
						{ "Three", "Four" } }, new String[]
						{ "Column 1", "Column 2" });
						this.setClosable(true);
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
						jTable1.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt)
							{
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT"))
									{
										editRecord();
									}
								}
							}
						});
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					jButtonAdd.setText(lang.get("btn_Add"));
					jDesktopPane1.add(jButtonAdd);
					jButtonAdd.setMnemonic(lang.getMnemonicChar());
					jButtonAdd.setBounds(685, 7, 126, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_CUST_DATA_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							JDBMaterialCustomerData m = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
							JDBCustomer u = new JDBCustomer(Common.selectedHostID, Common.sessionID);

							LinkedList<JDBCustomer> custList = u.getCustomers();
							String[] customerList = new String[custList.size()];
							for (int x = 0; x < custList.size(); x++)
							{
								customerList[x] = custList.get(x).getID();
							}

							lcustomer = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Customer_Input"), lang.get("btn_Select"), JOptionPane.PLAIN_MESSAGE, Common.icon_confirm, customerList, customerList[0]);

							if (lcustomer != null)
							{
								if (lcustomer.equals("") == false)
								{
									lcustomer = lcustomer.toUpperCase();
									if (u.isValidCustomer(lcustomer))
									{
										
										JDBDataIDs di = new JDBDataIDs(Common.selectedHostID, Common.sessionID);
										LinkedList<JDBDataIDs> idList = di.getDataIDs();

										String[] dataIDList = new String[idList.size()];
										for (int x = 0; x < idList.size(); x++)
										{
											dataIDList[x] = idList.get(x).getID();
										}

										ldataid = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Data_ID_Input"), lang.get("btn_Select"), JOptionPane.QUESTION_MESSAGE, Common.icon_confirm, dataIDList, dataIDList[0]);
										if (ldataid != null)
										{
											if (ldataid.equals("") == false)
											{
												ldataid = ldataid.toUpperCase();

												m.setMaterial(lmaterial);
												m.setCustomerID(lcustomer);
												m.setDataID(ldataid);

												if (m.isValid() == true)
												{
													if (m.isValidMaterialCustomerData() == false)
													{
														m.create();
													}
													JLaunchMenu.runDialog("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT", lmaterial, lcustomer, ldataid);
													populateList(lmaterial);
												}

											}
										}
									} else
									{
										JOptionPane.showMessageDialog(Common.mainForm, "Invalid Customer [" + lcustomer + "] does not exist", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE);
									}
								}
							}

						}
					});
				}
				{
					jButtonEdit = new JButton4j(Common.icon_edit);
					jDesktopPane1.add(jButtonEdit);
					jButtonEdit.setText(lang.get("btn_Edit"));
					jButtonEdit.setMnemonic(lang.getMnemonicChar());
					jButtonEdit.setBounds(685, 35, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							editRecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(685, 63, 126, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_CUST_DATA_DELETE"));
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							int row = jTable1.getSelectedRow();
							if (row >= 0)
							{
								lcustomer = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Material_Col).toString();
								lcustomer = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Customer_Col).toString();
								ldataid = jTable1.getValueAt(row, JDBMaterialCustomerDataTableModel.Data_ID_Col).toString();
								int question = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Material [" + lmaterial + "] Customer [" + lcustomer + "] Data ID [" + ldataid + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0,
										Common.icon_confirm);
								if (question == 0)
								{
									JDBMaterialCustomerData m = new JDBMaterialCustomerData(Common.selectedHostID, Common.sessionID);
									m.setMaterial(lmaterial);
									m.setCustomerID(lcustomer);
									m.setDataID(ldataid);
									boolean result = m.delete();
									if (result == false)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), "Delete error Material Customer Data  [" + lmaterial + "] Customer [" + lcustomer + "]  Data ID [" + ldataid + "]",
												JOptionPane.WARNING_MESSAGE);
									} else
									{
										populateList(lmaterial);
									}
								}
							}
						}
					});
				}
				{
					jButtonPrint = new JButton4j(Common.icon_report);
					jDesktopPane1.add(jButtonPrint);
					jButtonPrint.setText(lang.get("btn_Print"));
					jButtonPrint.setMnemonic(lang.getMnemonicChar());
					jButtonPrint.setBounds(685, 91, 126, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_MATERIAL_CUST_DATA"));
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{

							JLaunchReport.runReport("RPT_MATERIAL_CUST_DATA", null, JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_CUSTOMER_DATA where material = \'" + lmaterial + "\' order by material,customer,data_id"),
									null, "");
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(685, 147, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(685, 175, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(685, 119, 126, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							populateList(lmaterial);
						}
					});
				}

				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(10, 185, 466, 21);
					jDesktopPane1.add(jStatusText);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
