package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterialUom;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUom;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.tablemodel.JDBMaterialUomTableModel;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameMaterialUomAdmin extends javax.swing.JInternalFrame
{
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
	private String luom;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private PreparedStatement listStatement;

	public JInternalFrameMaterialUomAdmin()
	{
		super();
		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_MATERIAL_UOM"));

	}

	public JInternalFrameMaterialUomAdmin(String material, String base)
	{

		this();
		lmaterial = material;
		populateList(lmaterial);

	}

	private void populateList(String criteria) {

		JDBQuery.closeStatement(listStatement);
		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_UOM"));
		query.addParamtoSQL("material=", criteria);
		query.appendSort("material,uom", "asc");
		query.applyRestriction(false, "none", 0);
		query.bindParams();
		listStatement = query.getPreparedStatement();
		
		JDBMaterialUom materialuom = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
		JDBMaterialUomTableModel materialuomtable = new JDBMaterialUomTableModel(materialuom.getMaterialDataResultSet(listStatement));
		TableRowSorter<JDBMaterialUomTableModel> sorter = new TableRowSorter<JDBMaterialUomTableModel>(materialuomtable);

		jTable1.setRowSorter(sorter);
		jTable1.setModel(materialuomtable);
		jScrollPane1.setViewportView(jTable1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setFont(Common.font_list);

		TableColumn col = jTable1.getColumnModel().getColumn(0);
		col.setPreferredWidth(100); // Material
		col = jTable1.getColumnModel().getColumn(1);
		col.setPreferredWidth(50); // Uom
		col = jTable1.getColumnModel().getColumn(2);
		col.setPreferredWidth(110); // EAN
		col = jTable1.getColumnModel().getColumn(3);
		col.setPreferredWidth(50); // Variant
		col = jTable1.getColumnModel().getColumn(4);
		col.setPreferredWidth(80); // Nominator
		col = jTable1.getColumnModel().getColumn(5);
		col.setPreferredWidth(80); // Denominator
		jScrollPane1.repaint();

		JUtility.setResultRecordCountColour(jStatusText, false, 0, materialuomtable.getRowCount());

	}

	private void editRecord() {
		int row = jTable1.getSelectedRow();
		if (row >= 0)
		{
			luom = jTable1.getValueAt(row, 1).toString();
			JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_UOM_EDIT", lmaterial, luom);
		}
	}

	private void initGUI() {
		try
		{
			this.setPreferredSize(new java.awt.Dimension(625, 245));
			this.setBounds(0, 0, 637, 279);
			setVisible(true);
			this.setIconifiable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_app_window);
				this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				{
					jScrollPane1 = new JScrollPane();
					jScrollPane1.getViewport().setBackground(Common.color_tablebackground);
					jDesktopPane1.add(jScrollPane1);
					jScrollPane1.setBounds(7, 7, 469, 200);
					{
						TableModel jTable1Model = new DefaultTableModel(new String[][] { { "One", "Two" }, { "Three", "Four" } }, new String[] { "Column 1", "Column 2" });
						this.setClosable(true);
						jTable1 = new JTable();
						jTable1.setDefaultRenderer(Object.class, Common.renderer_table);
						jScrollPane1.setViewportView(jTable1);
						jTable1.setModel(jTable1Model);
						jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						jTable1.getTableHeader().setFont(Common.font_table_header);
						jTable1.getTableHeader().setForeground(Common.color_tableHeaderFont);
						jTable1.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (evt.getClickCount() == 2)
								{
									if (Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_UOM_EDIT"))
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
					jButtonAdd.setBounds(483, 7, 126, 32);
					jButtonAdd.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_UOM_ADD"));
					jButtonAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JDBMaterialUom m = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
							JDBUom u = new JDBUom(Common.selectedHostID, Common.sessionID);
							luom = (String) JOptionPane.showInputDialog(Common.mainForm, lang.get("dlg_Material_UOM_Input"), null, JOptionPane.QUESTION_MESSAGE,Common.icon_confirm, null, null);
							if (luom != null)
							{
								if (luom.equals("") == false)
								{
									luom = luom.toUpperCase();
									u.setInternalUom(luom);
									if (u.isValidInternalUom())
									{
										m.setMaterial(lmaterial);
										m.setUom(luom);
										if (m.isValidMaterialUom() == false)
										{
											JLaunchMenu.runForm("FRM_ADMIN_MATERIAL_UOM_EDIT", lmaterial, luom);
										}
										else
										{
											JOptionPane.showMessageDialog(Common.mainForm, "Material [" + lmaterial + "] Uom [" + luom + "] already exists", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
										}
										populateList(lmaterial);
									}
									else
									{
										JOptionPane.showMessageDialog(Common.mainForm, "Invalid Uom [" + luom + "] does not exist", lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE);
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
					jButtonEdit.setBounds(483, 35, 126, 32);
					jButtonEdit.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_UOM_EDIT"));
					jButtonEdit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							editRecord();

						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					jDesktopPane1.add(jButtonDelete);
					jButtonDelete.setText(lang.get("btn_Delete"));
					jButtonDelete.setMnemonic(lang.getMnemonicChar());
					jButtonDelete.setBounds(483, 63, 126, 32);
					jButtonDelete.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("FRM_ADMIN_MATERIAL_UOM_EDIT"));
					jButtonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							int row = jTable1.getSelectedRow();
							if (row >= 0)
							{
								luom = jTable1.getValueAt(row, 1).toString();
								int question = JOptionPane.showConfirmDialog(Common.mainForm, "Delete Material [" + lmaterial + "] Uom [" + luom + "] ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);
								if (question == 0)
								{
									JDBMaterialUom m = new JDBMaterialUom(Common.selectedHostID, Common.sessionID);
									m.setMaterial(lmaterial);
									m.setUom(luom);
									boolean result = m.delete();
									if (result == false)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, m.getErrorMessage(), "Delete error Material [" + lmaterial + "] Uom [" + luom + "]", JOptionPane.WARNING_MESSAGE);
									}
									else
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
					jButtonPrint.setBounds(483, 91, 126, 32);
					jButtonPrint.setEnabled(Common.userList.getUser(Common.sessionID).isModuleAllowed("RPT_MATERIAL_UOM"));
					jButtonPrint.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {

							JLaunchReport.runReport("RPT_MATERIAL_UOM", null, JUtility.substSchemaName(schemaName, "select * from {schema}APP_MATERIAL_UOM where material = \'" + lmaterial + "\' order by uom"),null, "");
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(483, 147, 126, 32);
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setMnemonic(lang.getMnemonicChar());
					jButtonClose.setBounds(483, 175, 126, 32);
					jButtonClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButtonRefresh = new JButton4j(Common.icon_refresh);
					jDesktopPane1.add(jButtonRefresh);
					jButtonRefresh.setText(lang.get("btn_Refresh"));
					jButtonRefresh.setBounds(483, 119, 126, 32);
					jButtonRefresh.setMnemonic(lang.getMnemonicChar());
					jButtonRefresh.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
