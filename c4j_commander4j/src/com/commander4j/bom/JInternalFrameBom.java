package com.commander4j.bom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang3.math.NumberUtils;

import com.commander4j.calendar.JCalendarDialog2;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileFilterXML;
import com.commander4j.util.JUtility;

public class JInternalFrameBom extends JInternalFrame
{

	private static final long serialVersionUID = 1L;
	private static JTree tree;
	private static BomTreeRenderer bomTreeRenderer = new BomTreeRenderer();
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private Transferable transferable;
	private JPanel contentPane;
	private JToolBar jtreeToolbar;
	private JButton4j btnExpandAll;
	private JButton4j btnExpandNode;
	private JButton4j btnCollapseAll;
	private JButton4j btnCollapseNode;

	private JButton4j btnImport;
	private JButton4j btnSearch;
	private JButton4j btnProcess;
	private JButton4j btnClose;
	private JScrollPane scrollPane = new JScrollPane();
	private JTextField4j textFieldVersion;
	private JTextField4j textFieldBOM;
	private JLabel4j_std lblBOM;
	private JLabel4j_std lblVersion;
	private JLabel4j_std lblStage;
	private String bom_id = "";
	private String bom_version = "";
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBBom bomDB = new JDBBom(Common.selectedHostID, Common.sessionID);
	private JDBBomList bomListsDB = new JDBBomList(Common.selectedHostID, Common.sessionID);
	private JComboBox4j<String> comboBoxMode;
	private JComboBox4j<String> comboBoxStage;

	public JInternalFrameBom(String bom, String version)
	{
		super();

		setVisible(true);

		setTitle("Bill of Materials");
		setBounds(100, 100, 820, 730);
		contentPane = new JPanel();

		UIManager.put("ToolTip.font", Common.font_tree_tooltip);

		ButtonHandler buttonhandler = new ButtonHandler();

		jtreeToolbar = new JToolBar();
		jtreeToolbar.setOrientation(JToolBar.HORIZONTAL);
		jtreeToolbar.setFloatable(false);
		jtreeToolbar.setBorder(new EmptyBorder(2, 2, 2, 2));

		btnExpandAll = new JButton4j(Common.icon_expand_all_16x16);
		btnExpandAll.addActionListener(buttonhandler);
		btnExpandAll.setToolTipText("Expand all menu items");
		btnExpandAll.setSize(30, 30);
		btnExpandAll.setMaximumSize(new Dimension(30, 30));
		btnExpandAll.setMinimumSize(new Dimension(30, 30));
		btnExpandAll.setPreferredSize(new Dimension(30, 30));

		btnExpandNode = new JButton4j(Common.icon_expand_node_16x16);
		btnExpandNode.addActionListener(buttonhandler);
		btnExpandNode.setToolTipText("Expand selected menu branch");
		btnExpandNode.setSize(30, 30);
		btnExpandNode.setMaximumSize(new Dimension(30, 30));
		btnExpandNode.setMinimumSize(new Dimension(30, 30));
		btnExpandNode.setPreferredSize(new Dimension(30, 30));

		btnCollapseAll = new JButton4j(Common.icon_collapse_all_16x16);
		btnCollapseAll.addActionListener(buttonhandler);
		btnCollapseAll.setToolTipText("Collapse menu tree");
		btnCollapseAll.setSize(30, 30);
		btnCollapseAll.setMaximumSize(new Dimension(30, 30));
		btnCollapseAll.setMinimumSize(new Dimension(30, 30));
		btnCollapseAll.setPreferredSize(new Dimension(30, 30));

		btnCollapseNode = new JButton4j(Common.icon_collapse_node_16x16);
		btnCollapseNode.addActionListener(buttonhandler);
		btnCollapseNode.setToolTipText("Collapse selected menu branch");
		btnCollapseNode.setSize(30, 30);
		btnCollapseNode.setMaximumSize(new Dimension(30, 30));
		btnCollapseNode.setMinimumSize(new Dimension(30, 30));
		btnCollapseNode.setPreferredSize(new Dimension(30, 30));

		btnImport = new JButton4j(Common.icon_import_16x16);
		btnImport.addActionListener(buttonhandler);
		btnImport.setToolTipText(lang.get("btn_Import"));
		btnImport.setSize(30, 30);
		btnImport.setMaximumSize(new Dimension(30, 30));
		btnImport.setMinimumSize(new Dimension(30, 30));
		btnImport.setPreferredSize(new Dimension(30, 30));

		btnSearch = new JButton4j(Common.icon_search_16x16);
		btnSearch.addActionListener(buttonhandler);
		btnSearch.setToolTipText(lang.get("btn_Search"));
		btnSearch.setSize(30, 30);
		btnSearch.setMaximumSize(new Dimension(30, 30));
		btnSearch.setMinimumSize(new Dimension(30, 30));
		btnSearch.setPreferredSize(new Dimension(30, 30));

		btnProcess = new JButton4j(Common.icon_execute_16x16);
		btnProcess.addActionListener(buttonhandler);
		btnProcess.setToolTipText(lang.get("btn_Run"));
		btnProcess.setSize(30, 30);
		btnProcess.setMaximumSize(new Dimension(30, 30));
		btnProcess.setMinimumSize(new Dimension(30, 30));
		btnProcess.setPreferredSize(new Dimension(30, 30));

		btnClose = new JButton4j(Common.icon_close_16x16);
		btnClose.addActionListener(buttonhandler);
		btnClose.setToolTipText(lang.get("btn_Close"));
		btnClose.setSize(30, 30);
		btnClose.setMaximumSize(new Dimension(30, 30));
		btnClose.setMinimumSize(new Dimension(30, 30));
		btnClose.setPreferredSize(new Dimension(30, 30));
		
		comboBoxMode = new JComboBox4j<String>();
		comboBoxMode.setSize(120,28);
		comboBoxMode.setMinimumSize(new Dimension(110,28));
		comboBoxMode.setMaximumSize(new Dimension(110,28));
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(new String[] { "adjacent", "first available" });
		comboBoxMode.setModel(model);
		comboBoxMode.setFont(Common.font_input_large);
		comboBoxMode.setToolTipText("Update Lanes based selected mode");
		
		lblBOM = new JLabel4j_std("BOM Id");
		lblBOM.setSize(new Dimension( 50, 30));
		lblBOM.setMinimumSize(new Dimension( 50, 30));
		lblBOM.setMaximumSize(new Dimension( 50, 30));
		lblBOM.setFont(Common.font_input_large);
		
		textFieldBOM = new JTextField4j();
		textFieldBOM.setSize(110, 30);
		textFieldBOM.setMaximumSize(new Dimension(110, 30));
		textFieldBOM.setFont(Common.font_input_large);
		textFieldBOM.setText(bom_id);
		textFieldBOM.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				bom_id = textFieldBOM.getText();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				bom_id = textFieldBOM.getText();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				bom_id = textFieldBOM.getText();
			}
		});
		
		lblVersion = new JLabel4j_std("Version");
		lblVersion.setSize(50, 30);
		lblVersion.setMinimumSize(new Dimension( 50, 30));
		lblVersion.setMaximumSize(new Dimension( 50, 30));
		lblVersion.setFont(Common.font_input_large);
		
		textFieldVersion = new JTextField4j();
		textFieldVersion.setSize( 40, 27);
		textFieldVersion.setFont(Common.font_input_large);
		textFieldVersion.setMinimumSize(new Dimension(40, 27));
		textFieldVersion.setMaximumSize(new Dimension(40, 27));
		textFieldVersion.setText(bom_version);
		textFieldVersion.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				bom_version = textFieldVersion.getText();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				bom_version = textFieldVersion.getText();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				bom_version = textFieldVersion.getText();
			}
		});
		
		LinkedList<String> listValues = bomListsDB.getListItems("stage");
		String[] valarray = listValues.toArray(new String[listValues.size()]);		
		DefaultComboBoxModel<String> stages = new DefaultComboBoxModel<String>(valarray);
		
		lblStage = new JLabel4j_std("Stage");
		lblStage.setSize(40, 28);
		lblStage.setFont(Common.font_input_large);
		lblStage.setMinimumSize(new Dimension(40, 28));
		lblStage.setMaximumSize(new Dimension(40, 28));
		
		comboBoxStage = new JComboBox4j<String>();
		comboBoxStage.setSize(70, 30);
		comboBoxStage.setMaximumSize(new Dimension(70,30));
		comboBoxStage.setModel(stages);
		comboBoxStage.setFont(Common.font_input_large);
		

		jtreeToolbar.addSeparator();
		jtreeToolbar.add(btnExpandAll);
		jtreeToolbar.add(btnExpandNode);
		jtreeToolbar.add(btnCollapseAll);
		jtreeToolbar.add(btnCollapseNode);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(btnImport);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(comboBoxMode);
		jtreeToolbar.add(btnProcess);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(lblBOM);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(textFieldBOM);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(lblVersion);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(textFieldVersion);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(lblStage);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(comboBoxStage);
		jtreeToolbar.add(btnSearch);
		jtreeToolbar.addSeparator();
		jtreeToolbar.add(btnClose);

		textFieldBOM.requestFocus();
		textFieldBOM.setCaretPosition(textFieldBOM.getText().length());

		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));

		setContentPane(contentPane);

		contentPane.add(jtreeToolbar, BorderLayout.NORTH);

		contentPane.add(scrollPane, BorderLayout.CENTER);

		loadBOM(bom,version);
	}

	public void refresh(String bom, String version)
	{	
		setBOMId(bom,version);
		loadBOM(bom,version);
		moveToFront();
		setVisible(true);
	}
	
	//Set BOM ID and Version Variables
	public void setBOMId(String bom, String version)
	{
		bom_id = bom;
		bom_version = version;

		textFieldBOM.setText(bom_id);
		textFieldVersion.setText(bom_version);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldBOM.requestFocus();
				textFieldBOM.setCaretPosition(textFieldBOM.getText().length());
			}
		});
	}
	
	//Read XML and insert into database
	private void importBOM()
	{
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "import" + File.separator + "bom");
		JFileFilterXML xmlFilter = new JFileFilterXML();
		chooser.setFileFilter(xmlFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.addChoosableFileFilter(xmlFilter);

		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			String importFile = chooser.getSelectedFile().getAbsolutePath();

			BomImport bomImport = new BomImport(Common.selectedHostID,Common.sessionID);

			JDBBomRecord result = bomImport.importBom(importFile);

			bom_id = result.getBOMId();
			bom_version = result.getBOMVersion();
			
			setBOMId(bom_id,bom_version);
		}
	}
	
	private void validateBOM(String bomID, String bomVersion,String stage)
	{
		BomValidate val = new BomValidate();

		if (comboBoxMode.getSelectedItem().toString().equals("first available"))
		{
			val.allocateLocations(bom_id, bom_version, "input", stage,BomValidate.mode_firstAvailable);
		}

		if (comboBoxMode.getSelectedItem().toString().equals("adjacent"))
		{
			val.allocateLocations(bom_id, bom_version, "input",stage, BomValidate.mode_adjacent);
		}	
	}

	//Load BOM from Database into Tree
	private void loadBOM(String bomID, String bomVersion)
	{

		setBOMId(bomID, bomVersion);
		
		root = new DefaultMutableTreeNode("root");
		String uuid = bomDB.getRootUuid(bomID, bomVersion);

		String stage = comboBoxStage.getSelectedItem().toString();
		root = bomDB.recurseBOM(root, uuid, 0,stage);

		tree = new JTree(root);
		tree.setToggleClickCount(0);

		tree.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();
					JDBBomRecord rec = (JDBBomRecord) nodeObj;

					JDBBomElement element = new JDBBomElement(Common.selectedHostID, Common.sessionID);
					element.getProperties(rec.getDataId());

					boolean edit = element.getElementRecord().isEnable_edit();

					if (edit)
					{
						rec = editValue(rec);

						if (rec.isModified())
						{
							((DefaultMutableTreeNode) val).setUserObject(rec);

							bomDB.updateRecord(rec);

							DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
							model.reload(val);
						}
					}
				}
			}
		});

		tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();
				try
				{
					JDBBomRecord rec = (JDBBomRecord) nodeObj;
					tree.setComponentPopupMenu(getPopupMenu(rec));
				}
				catch (Exception ex)
				{
					tree.setComponentPopupMenu(null);
				}
			}
		});

		tree.setBorder(new EmptyBorder(8, 8, 5, 5));
		tree.setFont(Common.font_tree);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(bomTreeRenderer);
		tree.setRowHeight(17);

		ToolTipManager.sharedInstance().registerComponent(tree);

		scrollPane.setViewportView(tree);

		expandTree();
	}

	private void expandTree()
	{
		int j = tree.getRowCount();
		int i = 0;
		while (i < j)
		{
			tree.expandRow(i);
			i += 1;
			j = tree.getRowCount();
		}
	}

	private JPopupMenu getPopupMenu(JDBBomRecord rec)
	{
		JPopupMenu popupMenu = new JPopupMenu();

		JDBBomElement element = new JDBBomElement(Common.selectedHostID, Common.sessionID);
		element.getProperties(rec.getDataId());

		boolean copytoclipboard = element.getElementRecord().isEnable_clipboard();
		boolean edit = element.getElementRecord().isEnable_edit();
		boolean duplicate = element.getElementRecord().isEnable_duplicate();
		boolean delete = element.getElementRecord().isEnable_delete();
		boolean create = element.getElementRecord().isEnable_create();

		if (copytoclipboard)
		{
			JMenuItem menu3 = new JMenuItem(lang.get("lbl_Clipboard_Copy"));
			menu3.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();
					JDBBomRecord rec = (JDBBomRecord) nodeObj;
					transferable = new StringSelection(rec.getString());
					clipboard.setContents(transferable, null);
				}
			});
			popupMenu.add(menu3);
		}

		if (duplicate)
		{
			JMenuItem menu4 = new JMenuItem(lang.get("lbl_Duplicate"));
			menu4.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();
					JDBBomRecord rec = (JDBBomRecord) nodeObj;

					int response = JOptionPane.showConfirmDialog(JInternalFrameBom.this, lang.get("lbl_Duplicate")+" '" + rec.getElement().getElementRecord().getDescription() + "'?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
							Common.icon_confirm_16x16);

					if (response == JOptionPane.YES_OPTION)
					{
						bomDB.cloneTree(rec, null);
						loadBOM(bom_id, bom_version);
					}
				}
			});
			popupMenu.add(menu4);
		}

		if (delete)
		{
			JMenuItem menu5 = new JMenuItem(lang.get("lbl_Delete"));
			menu5.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();
					JDBBomRecord rec = (JDBBomRecord) nodeObj;

					int response = JOptionPane.showConfirmDialog(JInternalFrameBom.this, lang.get("lbl_Delete")+" '" + rec.getElement().getElementRecord().getDescription() + "'?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
							Common.icon_confirm_16x16);

					if (response == JOptionPane.YES_OPTION)
					{
						bomDB.deleteTree(rec);
						loadBOM(bom_id, bom_version);
					}
				}
			});
			popupMenu.add(menu5);
		}

		if (edit)
		{
			JMenuItem menu6 = new JMenuItem(lang.get("lbl_Edit"));
			menu6.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ev)
				{
					DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();

					JDBBomRecord rec = (JDBBomRecord) nodeObj;

					rec = editValue(rec);

					if (rec.isModified())
					{

						((DefaultMutableTreeNode) val).setUserObject(rec);

						bomDB.updateRecord(rec);

						DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
						model.reload(val);
					}
				}
			});
			popupMenu.add(menu6);
		}

		if (create)
		{
			JMenu menu7 = new JMenu(lang.get("lbl_Create"));

			JDBBomStructure struc = new JDBBomStructure(Common.selectedHostID, Common.sessionID);

			LinkedList<JDBBomStructureRecord> newlist = struc.getAllowedChildren(rec);

			HashMap<String, Integer> occur = bomDB.getElementCount(rec);

			if (newlist.size() > 0)
			{

				String key = "";
				int actual = 0;
				int max = 0;
				int items = 0;

				for (int x = 0; x < newlist.size(); x++)
				{

					key = "[" + newlist.get(x).getDataId() + "]";

					actual = 0;
					try
					{
						actual = occur.get(key);
					}
					catch (Exception ex)
					{
						actual = 0;
					}

					max = 0;
					try
					{
						max = newlist.get(x).getElementRecord().getMax_occur_level();
					}
					catch (Exception ex)
					{
						max = 0;
					}

					if (actual < max)
					{

						JMenuItem menu7item = new JMenuItem(newlist.get(x).getElementRecord().getDescription());

						final String newType = newlist.get(x).getElementRecord().getDataType();
						final String newDescription = newlist.get(x).getElementRecord().getDescription();
						final boolean editEnabled = newlist.get(x).getElementRecord().isEnable_edit();
						final String newId = newlist.get(x).getDataId();

						menu7item.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent ev)
							{
								DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
								Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();

								JDBBomRecord currentrec = (JDBBomRecord) nodeObj;

								JDBBomRecord newrec = new JDBBomRecord(Common.selectedHostID, Common.sessionID);

								int response = JOptionPane.showConfirmDialog(JInternalFrameBom.this, lang.get("lbl_Create")+" '" + newDescription + "'?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Common.icon_confirm_16x16);

								if (response == JOptionPane.YES_OPTION)
								{
									newrec.setBOMId(currentrec.getBOMId());
									newrec.setBOMVersion(currentrec.getBOMVersion());
									newrec.setDataType(newType);
									newrec.setDataId(newId);
									newrec.setParent_uuid(currentrec.getUuid());
									newrec.setUuid(currentrec.newUUID());
									newrec.setBOMSequence(bomDB.getSequence(newId, currentrec.getUuid()));

									if (editEnabled)
									{
										newrec = editValue(newrec);
									}

									bomDB.writeRecord(newrec);

									loadBOM(bom_id, bom_version);
								}
							}
						});
						menu7.add(menu7item);
						items++;
					}
				}

				if (items > 0)
				{
					popupMenu.add(menu7);
				}
			}
		}

		JMenuItem menu8 = new JMenuItem("Display Sequence");
		menu8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				DefaultMutableTreeNode val = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				Object nodeObj = ((DefaultMutableTreeNode) val).getUserObject();

				JDBBomRecord rec = (JDBBomRecord) nodeObj;

				String stringValue = String.valueOf(rec.getBOMSequence());

				boolean valid = false;
				while (valid == false)
				{
					String temp = (String) JOptionPane.showInputDialog(JInternalFrameBom.this, "Display Sequence", element.getElementRecord().getDescription(), JOptionPane.OK_CANCEL_OPTION, Common.icon_confirm_16x16, null, stringValue);

					if (temp != null)
					{
						valid = NumberUtils.isCreatable(temp);

						if (valid)
						{
							rec.setBOMSequence(Integer.valueOf(temp));
						}
					}
					else
					{
						valid = true;
					}
				}

				bomDB.updateSequence(rec);

				loadBOM(bom_id, bom_version);
			}
		});
		popupMenu.add(menu8);

		return popupMenu;
	}

	public class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == btnExpandAll)
			{
				expandAll(tree, true);
			}

			if (event.getSource() == btnCollapseAll)
			{
				expandAll(tree, false);
			}

			if (event.getSource() == btnExpandNode)
			{
				TreePath path = tree.getSelectionPath();
				expandAll(tree, path, true);
			}

			if (event.getSource() == btnCollapseNode)
			{
				TreePath path = tree.getSelectionPath();
				expandAll(tree, path, false);
			}

			if (event.getSource() == btnImport)
			{	
				importBOM();
				loadBOM(bom_id, bom_version);
			}

			if (event.getSource() == btnProcess)
			{
				bom_id = textFieldBOM.getText();
				bom_version = textFieldVersion.getText();

				String stage = comboBoxStage.getSelectedItem().toString();
				validateBOM(bom_id, bom_version,stage);
				loadBOM(bom_id, bom_version);

			}

			if (event.getSource() == btnSearch)
			{
				bom_id = textFieldBOM.getText();
				bom_version = textFieldVersion.getText();

				//JTreePrintPreview.showPrintPreview(tree);
				
				loadBOM(bom_id, bom_version);
			}

			if (event.getSource() == btnClose)
			{
				dispose();
			}
		}
	}

	public void expandAll(JTree tree, boolean expand)
	{
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand)
	{
		try
		{
			TreeNode node = (TreeNode) parent.getLastPathComponent();
			if (node.getChildCount() >= 0)
			{
				for (Enumeration<?> e = node.children(); e.hasMoreElements();)
				{
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = parent.pathByAddingChild(n);
					expandAll(tree, path, expand);
				}
			}

			if (expand)
			{
				tree.expandPath(parent);
			}
			else
			{
				tree.collapsePath(parent);
			}
		}
		catch (Exception e)
		{

		}
	}

	private JDBBomRecord editValue(JDBBomRecord rec)
	{

		rec.setModified(false);
		String stringValue = "";
		Calendar timestampValue = Calendar.getInstance();
		JDBBomElement element = new JDBBomElement(Common.selectedHostID, Common.sessionID);
		String result = "";

		element.getProperties(rec.getDataId());

		if (rec.getDataType().equals("string"))
		{
			stringValue = rec.getDataString();

			// Plain string input
			if (element.getElementRecord().isEnable_lookup() == false)
			{
				result = (String) JOptionPane.showInputDialog(JInternalFrameBom.this, lang.get("lbl_Edit")+" "+lang.get("lbl_Value"), element.getElementRecord().getDescription(), JOptionPane.OK_CANCEL_OPTION, Common.icon_confirm_16x16, null, stringValue);

				if (result != null)
				{
					rec.setDataString(result);
					rec.setModified(true);
				}
			}
			else
			{
				// Lookup list
				JDBBomList ll = new JDBBomList(Common.selectedHostID, Common.sessionID);

				LinkedList<String> allitems = ll.getValues(element.getElementRecord().getLookup_sql(), element.getElementRecord().getLookup_field());

				int found = 0;

				for (int x = 0; x < allitems.size(); x++)
				{
					if (allitems.get(x).equals(stringValue))
					{
						found = x;
						break;
					}
				}

				Object[] array = allitems.toArray();

				String temp = (String) JOptionPane.showInputDialog(JInternalFrameBom.this, lang.get("btn_Select")+" "+lang.get("lbl_Value"), element.getElementRecord().getDescription(), JOptionPane.OK_CANCEL_OPTION, Common.icon_confirm_16x16, array, array[found]);

				if (temp != null)
				{
					result = temp;
					rec.setDataString(result);
					rec.setModified(true);
				}
				else
				{
					result = null;
				}
			}
		}

		if (rec.getDataType().equals("decimal"))
		{
			stringValue = rec.getDataDecimal().toString();

			boolean valid = false;
			while (valid == false)
			{

				String temp = (String) JOptionPane.showInputDialog(JInternalFrameBom.this, lang.get("lbl_Edit")+" "+lang.get("lbl_Value"), element.getElementRecord().getDescription(), JOptionPane.OK_CANCEL_OPTION, Common.icon_confirm_16x16, null, stringValue);

				if (temp != null)
				{
					result = temp;
					valid = NumberUtils.isCreatable(result);
					if (valid)
					{
						rec.setDataDecimal(new BigDecimal(result));
						rec.setModified(true);
					}
				}
				else
				{
					result = null;
					valid = true;
				}
			}
		}

		if (rec.getDataType().equals("timestamp"))
		{
			timestampValue.setTimeInMillis(rec.getDataTimestamp().getTime());
			Calendar backup = (Calendar) timestampValue.clone();

			backup.setTimeInMillis(rec.getDataTimestamp().getTime());

			Calendar cldr = Calendar.getInstance();
			cldr = timestampValue;

			JCalendarDialog2 cal = new JCalendarDialog2(JInternalFrameBom.this, cldr);
			cal.setVisible(true);

			cldr = cal.getDate();

			if (cldr.compareTo(backup) != 0)
			{
				rec.setModified(true);
			}

			Timestamp timestamp = new Timestamp(cldr.getTimeInMillis());
			result = JUtility.getISOTimeStampStringFormat(timestamp);

			rec.setDataTimestamp(JUtility.getTimeStampFromISOString(result));

		}

		return rec;
	}
}
