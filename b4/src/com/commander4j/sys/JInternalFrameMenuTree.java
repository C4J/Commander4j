
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JInternalFrameMenuTree.java
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBModule;


/**
 * The JInternalFrameMenuTree class is responsible for displaying the menu tree as shown below.
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameMenuTree.jpg" >
 * 
 * @see com.commander4j.db.JDBMenus JDBMenus
 * @see com.commander4j.sys.JInternalFrameMenuStructure JInternalFrameMenuStructure
 */
public class JInternalFrameMenuTree extends JInternalFrame

{
	private static final long serialVersionUID = 1;
	protected JToolBar jtreeToolbar;
	protected JButton btnExpandAll;
	protected JButton btnExpandNode;
	protected JButton btnCollapseAll;
	protected JButton btnCollapseNode;
	protected JTree tree;
	final int screenWidth = 300;
	final int screenHeight = 600;
	final Logger logger = Logger.getLogger(JInternalFrameMenuTree.class);

	public class MenuTreeRenderer extends DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = 1;

		public MenuTreeRenderer()
		{
			setFrameIcon(Common.icon_home_16x16);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			setIcon(JDBModule.getModuleIcon24x24(getMenuIconFilename(value), getMenuItemType(value)));
			setToolTipText(getMenuHint(value));

			return this;
		}

		protected String getMenuItemType(Object value) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			JMenuOption nodeInfo = (JMenuOption) (node.getUserObject());
			String type = nodeInfo.moduleType;

			return type;
		}

		protected String getMenuHint(Object value) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			JMenuOption nodeInfo = (JMenuOption) (node.getUserObject());
			String type = nodeInfo.hint;
			return type;
		}

		protected String getMenuIconFilename(Object value) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			JMenuOption nodeInfo = (JMenuOption) (node.getUserObject());
			String type = nodeInfo.iconFilename;
			return type;
		}
	}

	public JInternalFrameMenuTree(String menuID, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, JMenuBar menubar, JMenu menu)
	{

		setTitle(title);
		setResizable(resizable);
		setClosable(closable);
		setMaximizable(maximizable);
		setIconifiable(iconifiable);
		setFont(Common.font_std);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameIconified(InternalFrameEvent evt) {
				JLaunchMenu.cascadeFrames();
			}

			public void internalFrameDeiconified(InternalFrameEvent evt) {
				JLaunchMenu.cascadeFrames();
			}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setSize(screenWidth+Common.LFTreeMenuAdjustWidth, screenHeight+Common.LFTreeMenuAdjustHeight);
		setLocation((screenSize.width - (screenWidth+Common.LFTreeMenuAdjustWidth)) / 2, (screenSize.height - (screenHeight+Common.LFTreeMenuAdjustHeight)) / 2);
		setResizable(true);

		Container container = getContentPane();

		JMenuOption menuOption = new JMenuOption(Common.selectedHostID, Common.sessionID);
		menuOption.menuID = "MAIN";
		menuOption.moduleID = "";
		menuOption.description = "Main Menu";
		menuOption.moduleType = "MENU";
		menuOption.sequenceID = 0;

		DefaultMutableTreeNode top = new DefaultMutableTreeNode(menuOption);

		buildMenuTree(menuID, top, menubar, 0, menu);

		tree = new JTree(top);
		tree.setCellRenderer(new MenuTreeRenderer());
		tree.setRowHeight(27);
		ToolTipManager.sharedInstance().registerComponent(tree);

		tree.setFont(Common.font_tree);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2)
				{
					int x = me.getX();
					int y = me.getY();

					TreePath path = tree.getPathForLocation(x, y);

					if (path == null)
						return;

					DefaultMutableTreeNode option = (DefaultMutableTreeNode)

					tree.getLastSelectedPathComponent();
					if (option.isLeaf())
					{
						JMenuOption selectedOption = new JMenuOption(Common.selectedHostID, Common.sessionID);
						selectedOption = (JMenuOption) option.getUserObject();

						if (selectedOption.moduleType.equals("FORM"))
						{
							JLaunchMenu.runForm(selectedOption.moduleID);
						}
						if (selectedOption.moduleType.equals("REPORT"))
						{
							JLaunchReport.runReport(selectedOption.moduleID,null,"",null,"");
						}
						if (selectedOption.moduleType.equals("EXEC"))
						{
							JLaunchExec.runExec(Common.selectedHostID, Common.sessionID, selectedOption.moduleID);
						}
					}
				}
			}
		});

		JScrollPane treeview = new JScrollPane(tree);
		treeview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		treeview.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		container.add(treeview);

		ButtonHandler buttonhandler = new ButtonHandler();

		jtreeToolbar = new JToolBar();
		jtreeToolbar.setOrientation(0);
		jtreeToolbar.setFloatable(false);

		btnExpandAll = new JButton(Common.icon_expand_all);
		btnExpandAll.addActionListener(buttonhandler);
		btnExpandAll.setToolTipText("Expand all menu items");
		btnExpandAll.setSize(30,30);
		btnExpandAll.setMaximumSize(new Dimension(30,30));
		btnExpandAll.setMinimumSize(new Dimension(30,30));
		btnExpandAll.setPreferredSize(new Dimension(30,30));

		btnExpandNode = new JButton(Common.icon_expand_node);
		btnExpandNode.addActionListener(buttonhandler);
		btnExpandNode.setToolTipText("Expand selected menu branch");
		btnExpandNode.setSize(30,30);
		btnExpandNode.setMaximumSize(new Dimension(30,30));
		btnExpandNode.setMinimumSize(new Dimension(30,30));
		btnExpandNode.setPreferredSize(new Dimension(30,30));

		btnCollapseAll = new JButton(Common.icon_collapse_all);
		btnCollapseAll.addActionListener(buttonhandler);
		btnCollapseAll.setToolTipText("Collapse menu tree");
		btnCollapseAll.setSize(30,30);
		btnCollapseAll.setMaximumSize(new Dimension(30,30));
		btnCollapseAll.setMinimumSize(new Dimension(30,30));
		btnCollapseAll.setPreferredSize(new Dimension(30,30));

		btnCollapseNode = new JButton(Common.icon_collapse_node);
		btnCollapseNode.addActionListener(buttonhandler);
		btnCollapseNode.setToolTipText("Collapse selected menu branch");
		btnCollapseNode.setSize(30,30);
		btnCollapseNode.setMaximumSize(new Dimension(30,30));
		btnCollapseNode.setMinimumSize(new Dimension(30,30));
		btnCollapseNode.setPreferredSize(new Dimension(30,30));

		jtreeToolbar.add(btnExpandAll);
		jtreeToolbar.add(btnExpandNode);
		jtreeToolbar.add(btnCollapseAll);
		jtreeToolbar.add(btnCollapseNode);

		container.add(jtreeToolbar, BorderLayout.NORTH);

		setVisible(true);

	}

	public void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
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
			// Expansion or collapse must be done bottom-up
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

	public class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
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
		}
	}

	private void buildMenuTree(String menuID, DefaultMutableTreeNode node, JMenuBar menubar, int level, JMenu menu) {

		JDBModule m = new JDBModule(Common.selectedHostID, Common.sessionID);

		try
		{
			// logger.debug("buildMenuTree :"+node);

			PreparedStatement stmt;
			ResultSet rs;
			stmt = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).prepareStatement(Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JInternalFrameMenuTree.buildMenuTree"));
			stmt.setString(1, menuID);
			stmt.setString(2, Common.userList.getUser(Common.sessionID).getUserId());
			stmt.setFetchSize(25);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				JMenuOption menuOption = new JMenuOption(Common.selectedHostID, Common.sessionID);
				menuOption.load(rs);

				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(menuOption);
				node.add(newnode);
				if (menuOption.moduleType.equals("MENU"))
				{
					JMenu newmenu = new JMenu(menuOption.description);
					newmenu.setFont(Common.font_menu);
					newmenu.setMnemonic(menuOption.mnemonic);

					if (level == 0)
					{
						menubar.add(newmenu);
					}
					else
					{
						newmenu.setIcon(Common.icon_menu_16x16);
						menu.add(newmenu);
					}
					buildMenuTree(menuOption.moduleID, newnode, menubar, level + 1, newmenu);
				}
				else
				{
					JMenuItem newmenuitem = new JMenuPulldownMenuItem(menuOption);

					m.setModuleId(menuOption.moduleID);
					m.getModuleProperties();

					String type = m.getType();

					newmenuitem.setIcon(JDBModule.getModuleIcon16x16(m.getIconFilename(), type));

					newmenuitem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JMenuPulldownMenuItem o = (JMenuPulldownMenuItem) evt.getSource();
							if (o.getModuleType().equals("FORM"))
							{
								JLaunchMenu.runForm(o.toString());
							}
							if (o.getModuleType().equals("REPORT"))
							{
								JLaunchReport.runReport(o.toString(),null,"",null,"");
							}
							if (o.getModuleType().equals("EXEC"))
							{
								JLaunchExec.runExec(Common.selectedHostID, Common.sessionID, o.toString());
							}
						}
					});

					menu.add(newmenuitem);
				}
			}
			rs.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error in JInternalFrameMenuTree");
		}

	}

}
