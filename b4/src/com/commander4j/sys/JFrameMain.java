package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JFrameMain.java
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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBListData;
import com.commander4j.db.JDBModule;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.util.JHelp;

/**
 * JFrameMain is the main desktop for the Swing application as shown below.
 *
 * <p>
 * <img alt="" src="./doc-files/JFrameMain.jpg" >
 */
public class JFrameMain extends JFrame implements ComponentListener
{
	private static final long serialVersionUID = 1;
	protected JDesktopPane4j desktopPane = new JDesktopPane4j("");
	private Container contentPane = getContentPane();
	private JMenuToolbarMenuItem btnHome;
	private JMenuToolbarMenuItem btnExit;
	private JMenuToolbarMenuItem btnHelp;
	private JMenuToolbarMenuItem btnCascade;
	private JMenuToolbarMenuItem btnMinimize;
	private JMenuToolbarMenuItem btnRestore;
	private JMenuToolbarMenuItem btnExecute;
	private JMenuBar menuBar = new JMenuBar();
	private JToolBar jtb = new JToolBar();
	private JMenuToolbarMenu tbm = new JMenuToolbarMenu(Common.selectedHostID, Common.sessionID);
	private JStatusBar jsb = new JStatusBar();
	private JComboBox<Object> jcb = new JComboBox<Object>();
	private JMenu mFile = new JMenu("File");
	private JMenu mWindow = new JMenu("Window");
	private JMenu mView = new JMenu("View");
	private JMenu mHelp = new JMenu("Help");
	private JMenuItem mExit = new JMenuItem("Exit");
	private JMenuItem mCascade = new JMenuItem("Cascade");
	private JMenuItem mMinimize = new JMenuItem("Minimize");
	private JMenuItem mRestore = new JMenuItem("Restore");
	private JMenuItem mMenu = new JMenuItem("Menu");
	private JMenuItem mHelpContents = new JMenuItem("Contents");
	private JMenuItem mHelpAbout = new JMenuItem("About");
	private JMenuItem mHelpSystemProperties = new JMenuItem("System Info");
	private JMenuItem mHelpLicences = new JMenuItem("Licences");
	protected JInternalFrameMenuTree treeMenu;
	private DefaultComboBoxModel<Object> defComboBoxMod = new DefaultComboBoxModel<Object>();
	private JDBModule tempModule = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JMenuOption mo = new JMenuOption(Common.selectedHostID, Common.sessionID);
	private ComboBoxModel<Object> comboModel = defComboBoxMod;
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);

	class AppDesktopManager extends DefaultDesktopManager
	{
		private static final long serialVersionUID = 1;

		public void reIconifyFrame(JInternalFrame jif) {
			super.deiconifyFrame(jif);
			Rectangle rect = getBoundsForIconOf(jif);
			super.iconifyFrame(jif);
			jif.getDesktopIcon().setBounds(rect);
		}
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		setTreeSize();
/*		JInternalFrame[] jifs = desktopPane.getAllFrames();
		for (int i = 0; i < jifs.length; i++)
		{
			if (jifs[i].isIcon())
			{
				try
				{
					jifs[i].setIcon(true);
				} catch (PropertyVetoException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
*/	}

	public void componentShown(ComponentEvent e) {
	}

	public void setTreeSize() {
		int Height;
		Height = this.getHeight() - 150;
		if (Height < 100)
			Height = 100;
		treeMenu.setBounds(0, 0, Common.menuTreeWidth+Common.LFTreeMenuAdjustWidth, Height);
	}

	private static void ConfirmExit() {
		int question = JOptionPane.showConfirmDialog(Common.mainForm, "Exit application ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);
		if (question == 0)
		{

			Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
			System.exit(0);
		}
	}

	static class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e) {
			ConfirmExit();
		}
	}

	public JFrameMain()
	{

		super(Common.appDisplayName+" " + JVersion.getProgramVersion() + " (" + Common.hostList.getHost(Common.selectedHostID).getSiteDescription() + ")");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 50);
		setLocation(0, 0);
		setResizable(true);
		
		setExtendedState(Frame.MAXIMIZED_HORIZ);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener());

		//desktopPane.setDesktopManager(new AppDesktopManager());
		desktopPane.setBackground(Color.WHITE);
		contentPane.add(desktopPane, BorderLayout.CENTER);
		ButtonHandler buttonhandler = new ButtonHandler();

		menuBar.setFont(Common.font_std);
		setJMenuBar(menuBar);

		mFile.setFont(Common.font_menu);
		mFile.setMnemonic(java.awt.event.KeyEvent.VK_F);

		mExit.setFont(Common.font_menu);
		mExit.setIcon(Common.imageIconloader.getImageIcon16x16(Common.image_close));
		mExit.addActionListener(buttonhandler);

		mWindow.setFont(Common.font_menu);
		mWindow.setMnemonic(java.awt.event.KeyEvent.VK_W);

		mCascade.setFont(Common.font_menu);
		mCascade.addActionListener(buttonhandler);

		mMinimize.setFont(Common.font_menu);
		mMinimize.addActionListener(buttonhandler);

		mRestore.setFont(Common.font_menu);
		mRestore.addActionListener(buttonhandler);

		mView.setFont(Common.font_menu);
		mView.setMnemonic(java.awt.event.KeyEvent.VK_V);

		mMenu.setFont(Common.font_menu);
		mMenu.setIcon(Common.imageIconloader.getImageIcon16x16(Common.image_home));
		mMenu.addActionListener(buttonhandler);

		mHelp.setFont(Common.font_menu);
		mHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);

		mHelpContents.setFont(Common.font_menu);
		mHelpContents.setMnemonic(java.awt.event.KeyEvent.VK_C);
		mHelpContents.addActionListener(buttonhandler);

		mHelpAbout.setFont(Common.font_menu);
		mHelpAbout.setMnemonic(java.awt.event.KeyEvent.VK_A);
		mHelpAbout.addActionListener(buttonhandler);

		mHelpSystemProperties.setFont(Common.font_menu);
		mHelpSystemProperties.setMnemonic(java.awt.event.KeyEvent.VK_S);
		mHelpSystemProperties.addActionListener(buttonhandler);
		
		mHelpLicences.setFont(Common.font_menu);
		mHelpLicences.setMnemonic(java.awt.event.KeyEvent.VK_L);
		mHelpLicences.addActionListener(buttonhandler);

		mFile.add(mExit);
		mView.add(mMenu);

		mWindow.add(mCascade);
		mWindow.add(mMinimize);
		mWindow.add(mRestore);

		mHelp.add(mHelpContents);
		mHelp.add(mHelpSystemProperties);
		mHelp.add(mHelpLicences);
		mHelp.add(mHelpAbout);

		jtb.setOrientation(0);
		jtb.setPreferredSize(new Dimension(jtb.getSize().width, Common.buttonToolbarSize+5));
		jtb.setSize(jtb.getSize().width, Common.buttonToolbarSize+5);
		jtb.setFloatable(false);

		jcb.setPreferredSize(new Dimension(275, Common.buttonToolbarSize));
		jcb.setMaximumSize(new Dimension(275, Common.buttonToolbarSize));
		jcb.setMaximumRowCount(30);
		jcb.setSize(new Dimension(275, Common.buttonToolbarSize));
		jcb.setToolTipText("Quick Launch Menu");

		contentPane.add(jtb, BorderLayout.NORTH);
		contentPane.add(jsb, BorderLayout.SOUTH);

		btnHome = new JMenuToolbarMenuItem(lang.get("btn_Menu"),"Display Menu Tree",Common.icon_home_32x32);
		btnHome.addActionListener(buttonhandler);

		btnMinimize  = new JMenuToolbarMenuItem(lang.get("btn_Minimise"),"Minimize open windows.",Common.icon_minimize_32x32);
		btnMinimize.addActionListener(buttonhandler);

		btnCascade = new JMenuToolbarMenuItem(lang.get("btn_Tile"),"Cascade open windows.",Common.icon_cascade_32x32);
		btnCascade.addActionListener(buttonhandler);

		btnRestore = new JMenuToolbarMenuItem(lang.get("btn_Restore"),"Restore iconified windows.",Common.icon_restore_32x32);
		btnRestore.addActionListener(buttonhandler);

		btnHelp = new JMenuToolbarMenuItem(lang.get("btn_Help"),"Help",Common.icon_help_32x32);
		btnHelp.addActionListener(buttonhandler);

		btnExit = new JMenuToolbarMenuItem(lang.get("btn_Close"),"Exit application",Common.icon_close_32x32);
		btnExit.addActionListener(buttonhandler);

		// **************** SECURITY **********************************

		menuBar.add(mFile);
		menuBar.add(mView);

		treeMenu = new JInternalFrameMenuTree("root", "Menu", true, false, false, true, menuBar, mView);

		menuBar.add(mWindow);
		menuBar.add(mHelp);

		boolean quickMenu = Boolean.valueOf(ctrl.getKeyValue("QUICK_MENU_ENABLE"));
		
		jtb.add(btnHome);
		jtb.add(new JToolBar.Separator());
		tbm.buildMenu(this.jtb,quickMenu);
		jtb.add(new JToolBar.Separator());
		jtb.add(btnMinimize);
		jtb.add(btnCascade);
		jtb.add(btnRestore);
		jtb.add(btnHelp);
		jtb.add(new JToolBar.Separator());

		LinkedList<JDBListData> tempModuleList = tempModule.getModulesofTypeforUser(Common.selectedHostID, Common.sessionID, "FORM");

		mo.clear();
		defComboBoxMod.removeAllElements();
		defComboBoxMod.addElement(mo);
		for (int j = 0; j < tempModuleList.size(); j++)
		{
			defComboBoxMod.addElement(tempModuleList.get(j));
		}

		tempModuleList = tempModule.getModulesofTypeforUser(Common.selectedHostID, Common.sessionID, "EXEC");

		if (tempModuleList.isEmpty() == false)
		{
			defComboBoxMod.addElement("SEPARATOR");
			for (int j = 0; j < tempModuleList.size(); j++)
			{
				defComboBoxMod.addElement(tempModuleList.get(j));
			}
		}

		jcb.setModel(comboModel);
		jcb.setRenderer(Common.renderer_list);


		if (quickMenu)
		{
			jtb.add(jcb);
			btnExecute =  new JMenuToolbarMenuItem("","Execute Quick Menu Option.",Common.icon_execute_16x16);
			btnExecute.addActionListener(buttonhandler);
			jtb.add(btnExecute);
			jtb.add(new JToolBar.Separator());
		}
		jtb.add(btnExit);

		desktopPane.add(treeMenu);
		
		treeMenu.setVisible(false);
		try
		{
			treeMenu.setIcon(true);
		} catch (Exception e)
		{
		}

		desktopPane.addComponentListener(this);

		final JHelp help1 = new JHelp();
		help1.enableHelpOnButton(btnHelp, Common.helpURL);

		final JHelp help2 = new JHelp();
		help2.enableHelpOnMenuItem(mHelpContents, Common.helpURL);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				minmaxTree();
				setTreeSize();
			}
		});

	}
	
	private void minmaxTree()
	{
		if (treeMenu.isIcon())
		{
			treeMenu.setVisible(true);

			try
			{
				treeMenu.setIcon(false);
			}
			catch (Exception ex)
			{
				System.out.println("Cannot setIcon on treeMenu");
			}

			try
			{
				treeMenu.setSelected(true);
			}
			catch (Exception ex)
			{
				System.out.println("Cannot setSelected on treeMenu");
			}
		}
		else
		{
			try
			{
				treeMenu.setIcon(true);
			}
			catch (Exception ex)
			{
				System.out.println("Cannot setIcon on treeMenu");
			}
		}	
	}

	public class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == btnHome | event.getSource() == mMenu)
			{
				minmaxTree();
			}

			if (event.getSource() == mHelpAbout)
			{
				JLaunchMenu.runDialog("FRM_ABOUT");
			}

			if (event.getSource() == mHelpSystemProperties)
			{
				JLaunchMenu.runDialog("FRM_SYSTEM_PROPERTIES");
			}
			
			if (event.getSource() == mHelpLicences)
			{
				JLaunchMenu.runDialog("FRM_LICENCES");
			}

			if (event.getSource() == mCascade)
			{
				JLaunchMenu.cascadeFrames();
			}
			if (event.getSource() == mMinimize)
			{
				JLaunchMenu.minimizeAll();
			}
			if (event.getSource() == mRestore)
			{
				JLaunchMenu.restoreAll();
			}
			if (event.getSource() == btnCascade)
			{
				JLaunchMenu.cascadeFrames();
			}
			if (event.getSource() == btnRestore)
			{
				JLaunchMenu.restoreAll();
			}
			if (event.getSource() == btnMinimize)
			{
				JLaunchMenu.minimizeAll();
			}
			if (event.getSource() == btnExecute)
			{
				try
				{
					JDBListData ld = (JDBListData) jcb.getSelectedItem();
					JMenuOption mo = ((JMenuOption) ld.getObject());
					String x = mo.moduleID;
					if (mo.moduleType.equals("FORM"))
					{
						JLaunchMenu.runForm(x);
					}
					if (mo.moduleType.equals("REPORT"))
					{
						JLaunchReport.runReport(x,null,"",null,"");
					}
					if (mo.moduleType.equals("EXEC"))
					{
						JLaunchExec.runExec(Common.selectedHostID, Common.sessionID, x);
					}
				}
				catch (Exception ex)
				{

				}
			}
			if (event.getSource() == btnExit | event.getSource() == mExit)
			{
				ConfirmExit();
			}
		}
	}
}
