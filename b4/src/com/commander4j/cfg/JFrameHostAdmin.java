package com.commander4j.cfg;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JFrameHostAdmin.java
 * 
 * Package Name : com.commander4j.cfg
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.commander4j.app.JVersion;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBDDL;
import com.commander4j.db.JDBSchema;
import com.commander4j.db.JDBStructure;
import com.commander4j.db.JDBUpdateRequest;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JRadioButton4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JFileFilterXML;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLHost;
import com.commander4j.xml.JXMLSchema;

/**
 * JFrameHostAdmin is a Frame which allows the user to configure any number of
 * database connections which are stored in a file called hosts.xml file.
 * Changes to the host file can be password protected. If the file is password
 * protected you will be prompted to enter the current configuration password
 * using a dialog box called JDialogSetupPassword. When installing or upgrading
 * a database any errors which occur as displayed using the dialog box
 * JDialogDMLErrors
 * 
 * <p>
 * <img alt="" src="./doc-files/JFrameHostAdmin.jpg" >
 * 
 * @see com.commander4j.cfg.JDialogDMLErrors JDialogDMLErrors
 * @see com.commander4j.cfg.JDialogSetupPassword JDialogSetupPassword
 * @see com.commander4j.cfg.Setup Setup
 */
public class JFrameHostAdmin extends JFrame
{
	private JTextField4j jTextFieldURL;
	private static final long serialVersionUID = 1;
	private JDesktopPane desktopPane = new JDesktopPane();
	private JButton4j jButtonApply;
	private JButton4j jButtonDelete;
	private JButton4j jButtonUp;
	private JButton4j jButtonClose;
	private JTextField4j jTextFieldSchema;
	private JLabel4j_std jLabelSchema;
	private JLabel4j_std jLabelSelectTime;
	private JTextField4j jTextFieldSelectLimit;
	private JButton4j jButtonUpdate;
	private JButton4j jButtonTest;
	private JLabel4j_std jLabelServer;
	private JTextField4j jTextFieldDatabase;
	private JTextField4j jTextFieldSID;
	private JCheckBox4j jCheckBoxEnabled;
	private JCheckBox4j jCheckBoxSplash;
	private JTextField4j jTextFieldDriver;
	private JTextField4j jTextFieldSiteNo;
	private JTextField4j jTextFieldPort;
	private JPasswordField jTextFieldPassword;
	private JTextField4j jTextFieldUsername;
	private JTextField4j jTextFieldDateTime;
	private JLabel4j_std jLabelPort;
	private JLabel4j_std jLabelUsername;
	private JLabel4j_std jLabelSiteNo;
	private JLabel4j_std jLabelDescription;
	private JLabel4j_std jLabelPassword;
	private JLabel4j_std jLabelDBDateTime;
	private JLabel4j_std jLabelConnectionString;
	private JTextField4j jTextFieldServer;
	private JButton4j jButtonCancel;
	private JLabel4j_std jLabelSID;
	private JLabel4j_std jLabelDatabase;
	private JLabel4j_std jLabelDriver;
	private JTextField4j jTextFieldConnect;
	private JComboBox4j<String> jComboBoxjdbcDriver;
	private JLabel4j_std jLabelDatabaseType;
	private JTextField4j jTextFieldDescription;
	private JButton4j jButtonSave;
	private JButton4j jButtonUndo;
	private JButton4j jButtonDown;
	private JButton4j jButtonAdd;
	private JButton4j btnSchema;
	private JList4j<JHost> jListHosts;
	private JScrollPane jScrollPane1;
	private LinkedList<JHost> hostList = new LinkedList<JHost>();
	final Logger logger = Logger.getLogger(JFrameHostAdmin.class);
	private JFrameHostAdmin me;
	private JPanel contentPane;
	private JLabel4j_std jLabelUniqueID;
	private JTextField4j jTextFieldUniqueID;
	private JLabel4j_std jLabelMenuURL;
	private JProgressBar progressBar = new JProgressBar();
	private JLabel4j_std labelCommand = new JLabel4j_std("");
	private JTextField4j JTextFieldUpdateURL;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton4j rdbtnManual = new JRadioButton4j("MANUAL");
	private JRadioButton4j rdbtnAutomatic = new JRadioButton4j("AUTOMATIC");
	private JTextField4j jTextField4jInstallDir = new JTextField4j();
	private JPasswordField setupPasswordField;
	private JPasswordField verifyPasswordField;
	private JTextField4j jTextField4jHostVersion;
	private JTextField4j jTextField4jHostUpdatePath;
	private String hostsFilename = "";
	private JButton4j jButtonHelp;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					JFrameHostAdmin frame = new JFrameHostAdmin();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void setHostsFilename(String name)
	{
		hostsFilename = name;
		this.setTitle("Host Administration " + JVersion.getProgramVersion() + "  [" + hostsFilename + "]");
	}

	private void setUpdateMode(String mode)
	{
		if (mode.equals("MANUAL"))
		{
			rdbtnManual.setSelected(true);
		}
		if (mode.equals("AUTOMATIC"))
		{
			rdbtnAutomatic.setSelected(true);
		}
	}

	private String getUpdateMode()
	{
		String result = "";

		if (rdbtnManual.isSelected())
		{
			result = "MANUAL";
		}

		if (rdbtnAutomatic.isSelected())
		{
			result = "AUTOMATIC";
		}
		return result;
	}

	public JFrameHostAdmin()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1068, 659);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		desktopPane.setBounds(0, 0, 1068, 637);
		desktopPane.setBackground(Color.WHITE);
		contentPane.add(desktopPane);

		Common.sessionID = JUnique.getUniqueID();
		Common.sd.setData(Common.sessionID, "silentExceptions", "No", true);
		JUtility.initLogging("");
		logger.debug("JFrameHostAdmin starting...");

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, "http://commander4j.com/mw/index.php?title=Setup4j");
		
		setHostsFilename(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "hosts" + File.separator + "hosts.xml");
		setIconImage(Common.imageIconloader.getImageIcon(Common.image_osx_setup4j).getImage());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);

		getHosts();

		if (Common.setupPassword.equals("") == false)
		{
			JDialogSetupPassword u = new JDialogSetupPassword(null, Common.setupPassword);
			u.setModal(true);
		}

		populateList("");
	}

	private void jTextFieldKeyTyped()
	{
		jButtonApply.setEnabled(true);
		jButtonCancel.setEnabled(true);
		jButtonTest.setEnabled(false);
		jButtonUpdate.setEnabled(false);
	}

	private void setSaveButtonState(boolean enabled)
	{
		jButtonSave.setEnabled(enabled);
		jButtonUndo.setEnabled(enabled);
	}

	private void getHosts()
	{
		setEditable(false);
		hostList.clear();
		Common.hostList.loadHosts(hostsFilename);
		hostList = Common.hostList.getHosts();
		jCheckBoxSplash.setSelected(Common.displaySplashScreen);
		JTextFieldUpdateURL.setText(Common.updateURL);
		jTextField4jInstallDir.setBackground(Common.color_list_assigned);
		jTextField4jInstallDir.setText(Common.updateInstallDir);
		jTextField4jHostUpdatePath.setText(Common.hostUpdatePath);
		jTextField4jHostVersion.setText(Common.hostVersion);
		if (Common.updateInstallDir.equals(""))
		{
			jTextField4jInstallDir.setText(Common.base_dir);
		}
		setupPasswordField.setText(Common.setupPassword);
		verifyPasswordField.setText(Common.setupPassword);
		setUpdateMode(Common.updateMODE);
		setButtonState();
	}

	private void renumberHosts()
	{
		for (int j = 0; j < hostList.size(); j++)
		{
			hostList.get(j).setSiteNumber(String.valueOf(j + 1));
		}
	}

	private void populateList(String defaultitem)
	{
		setButtonState();
		int sel = 0;

		DefaultComboBoxModel<JHost> DefComboBoxMod = new DefaultComboBoxModel<JHost>();

		for (int j = 0; j < hostList.size(); j++)
		{
			DefComboBoxMod.addElement(hostList.get(j));
			if (hostList.get(j).getSiteDescription().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JHost> jList1Model = DefComboBoxMod;
		jListHosts.setModel(jList1Model);
		jListHosts.setCellRenderer(Common.renderer_list);
		jListHosts.setSelectedIndex(sel);
	}

	private void writeBackHosts(LinkedList<JHost> hsts)
	{
		Common.hostList.disconnectAll();
		Common.hostList.clear();
		Common.hostList.addHosts(hsts);

	}

	private void setButtonState()
	{
		if (hostList.size() > 0)
		{
			jButtonUp.setEnabled(true);
			jButtonDown.setEnabled(true);
		}
		else
		{
			jButtonUp.setEnabled(false);
			jButtonDown.setEnabled(false);
		}
	}

	private void getHostData()
	{
		int j = jListHosts.getSelectedIndex();
		if (j >= 0)
		{
			JHost hst = new JHost();
			hst = (JHost) jListHosts.getModel().getElementAt(j);
			jTextFieldDescription.setText(hst.getSiteDescription());
			jTextFieldUniqueID.setText(hst.getUniqueID());
			jTextFieldURL.setText(hst.getSiteURL());
			jTextFieldSiteNo.setText(hst.getSiteNumber());
			jTextFieldDriver.setText(hst.getDatabaseParameters().getjdbcDriver());
			if (hst.getDatabaseParameters().getjdbcDriver().equals(""))
			{
				jComboBoxjdbcDriver.setSelectedIndex(0);
			}

			if (hst.getDatabaseParameters().getjdbcDriver().equals("com.mysql.cj.jdbc.Driver"))
			{
				jComboBoxjdbcDriver.setSelectedIndex(1);
			}
			if (hst.getDatabaseParameters().getjdbcDriver().equals("oracle.jdbc.driver.OracleDriver"))
			{
				jComboBoxjdbcDriver.setSelectedIndex(2);
			}
			if (hst.getDatabaseParameters().getjdbcDriver().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
			{
				jComboBoxjdbcDriver.setSelectedIndex(3);
			}
			if (hst.getDatabaseParameters().getjdbcDriver().equals("http"))
			{
				jComboBoxjdbcDriver.setSelectedIndex(4);
			}

			jTextFieldConnect.setText(hst.getDatabaseParameters().getjdbcConnectString());
			jTextFieldDateTime.setText(hst.getDatabaseParameters().getjdbcDatabaseDateTimeToken());
			jTextFieldSelectLimit.setText(hst.getDatabaseParameters().getjdbcDatabaseSelectLimit());
			jTextFieldSchema.setText(hst.getDatabaseParameters().getjdbcDatabaseSchema());
			jTextFieldUsername.setText(hst.getDatabaseParameters().getjdbcUsername());
			jTextFieldPassword.setText(hst.getDatabaseParameters().getjdbcPassword());
			jTextFieldPort.setText(hst.getDatabaseParameters().getjdbcPort());
			jTextFieldSID.setText(hst.getDatabaseParameters().getjdbcSID());
			jTextFieldServer.setText(hst.getDatabaseParameters().getjdbcServer());
			jTextFieldDatabase.setText(hst.getDatabaseParameters().getjdbcDatabase());
			if (hst.getEnabled().equals("Y"))
				jCheckBoxEnabled.setSelected(true);
			else
				jCheckBoxEnabled.setSelected(false);

			setEditable(true);
			jButtonApply.setEnabled(false);
			jButtonCancel.setEnabled(false);
			jButtonTest.setEnabled(true);
			jButtonUpdate.setEnabled(false);
			btnSchema.setEnabled(false);

		}
		else
		{
			jTextFieldDescription.setText("");
			jTextFieldURL.setText("");
			jTextFieldSiteNo.setText("");
			jTextFieldDriver.setText("");
			jTextFieldConnect.setText("");
			jTextFieldDateTime.setText("");
			jTextFieldSelectLimit.setText("");
			jTextFieldUsername.setText("");
			jTextFieldPassword.setText("");
			jTextFieldPort.setText("");
			jTextFieldSID.setText("");
			jTextFieldServer.setText("");
			jTextFieldDatabase.setText("");
			jCheckBoxEnabled.setSelected(false);
			setEditable(false);
			// jButtonTest.setEnabled(false);
		}
	}

	private JHost setHostData()
	{
		int j = jListHosts.getSelectedIndex();
		JHost hst = new JHost();
		if (j > -1)
		{

			hst.setSiteDescription(jTextFieldDescription.getText());
			hst.setSiteURL(jTextFieldURL.getText());
			hst.setSiteNumber(jTextFieldSiteNo.getText());
			hst.getDatabaseParameters().setjdbcDriver(jTextFieldDriver.getText());
			hst.getDatabaseParameters().setjdbcDatabaseDateTimeToken(jTextFieldDateTime.getText());
			hst.getDatabaseParameters().setjdbcDatabaseSelectLimit(jTextFieldSelectLimit.getText());
			hst.getDatabaseParameters().setjdbcDatabaseSchema(jTextFieldSchema.getText());
			hst.getDatabaseParameters().setjdbcUsername(jTextFieldUsername.getText());
			hst.getDatabaseParameters().setjdbcPassword(String.valueOf(jTextFieldPassword.getPassword()));

			hst.getDatabaseParameters().setjdbcPort(jTextFieldPort.getText());
			hst.getDatabaseParameters().setjdbcSID(jTextFieldSID.getText());
			hst.getDatabaseParameters().setjdbcServer(jTextFieldServer.getText());
			hst.getDatabaseParameters().setjdbcDatabase(jTextFieldDatabase.getText());
			hst.setUniqueID(jTextFieldUniqueID.getText());
			hst.getSqlstatements().setjdbcDriver(hst.getDatabaseParameters().getjdbcDriver());
			hst.getSqlstatements().setjdbcDriver(hst.getDatabaseParameters().getjdbcDriver());

			if (jCheckBoxEnabled.isSelected())
				hst.setEnabled("Y");
			else
				hst.setEnabled("N");
			hostList.set(j, hst);
			setEditable(false);
			populateList("");
			jButtonApply.setEnabled(false);
			jButtonCancel.setEnabled(false);
		}
		return hst;
	}

	public static LinkedList<JHost> moveElementDown(LinkedList<JHost> list, JHost element)
	{
		int position;
		int size;

		size = list.size();

		if (size > 0)
		{
			position = list.indexOf(element);
			if (position < (size - 1))
			{
				list.remove(position);
				list.add(position + 1, element);

			}
		}
		return list;
	}

	public static LinkedList<JHost> moveElementUp(LinkedList<JHost> list, JHost element)
	{
		int position;
		int size;

		size = list.size();

		if (size > 0)
		{
			position = list.indexOf(element);
			if (position > 0)
			{
				list.remove(position);
				list.add(position - 1, element);
			}
		}
		return list;
	}

	private void setEditable(boolean edit)
	{
		jTextFieldDescription.setEnabled(edit);
		jTextFieldURL.setEnabled(edit);
		jTextFieldSiteNo.setEnabled(edit);
		// jTextFieldDriver.setEnabled(edit);
		// jTextFieldConnect.setEnabled(edit);
		jTextFieldDateTime.setEnabled(edit);
		jTextFieldSelectLimit.setEnabled(edit);
		jTextFieldSchema.setEnabled(edit);
		jTextFieldUsername.setEnabled(edit);
		jTextFieldPassword.setEnabled(edit);
		jTextFieldPort.setEnabled(edit);
		jTextFieldSID.setEnabled(edit);
		jTextFieldServer.setEnabled(edit);
		jTextFieldDatabase.setEnabled(edit);
		jCheckBoxEnabled.setEnabled(edit);
		jComboBoxjdbcDriver.setEnabled(edit);

	}

	private void initGUI()
	{
		try
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Host Administration " + JVersion.getProgramVersion());
			desktopPane.setLayout(null);
			// this.setResizable(false);
			{
				{
					jScrollPane1 = new JScrollPane();
					desktopPane.add(jScrollPane1);
					jScrollPane1.setBounds(14, 37, 258, 415);
					{
						ListModel<JHost> jListHostsModel = new DefaultComboBoxModel<JHost>();
						jListHosts = new JList4j<JHost>();
						jScrollPane1.setViewportView(jListHosts);
						jListHosts.setModel(jListHostsModel);
						jListHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jListHosts.addListSelectionListener(new ListSelectionListener()
						{
							public void valueChanged(ListSelectionEvent evt)
							{
								getHostData();
							}
						});
					}
				}
				{
					jButtonAdd = new JButton4j(Common.icon_add);
					desktopPane.add(jButtonAdd);
					jButtonAdd.setText("Add DB Connection");
					jButtonAdd.setBounds(885, 47, 160, 36);
					jButtonAdd.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							// Generate Next Site No //
							String siteNo = "";
							int siteNoVal = 0;
							for (int j = 0; j < hostList.size(); j++)
							{
								siteNo = hostList.get(j).getSiteNumber();
								int x = Integer.valueOf(siteNo);
								if (x > siteNoVal)
								{
									siteNoVal = x;
								}
							}
							siteNoVal++;
							siteNo = String.valueOf(siteNoVal);

							// Generate unique description //
							int i = 0;
							// int j = 0;
							boolean found = false;
							String newdesc = "";
							do
							{
								i++;
								newdesc = "New[" + String.valueOf(i) + "]";
								found = false;
								for (int y = 0; y < hostList.size(); y++)
								{
									if (hostList.get(y).getSiteDescription().equals(newdesc))
									{
										found = true;
									}
								}

							}
							while (found);

							JHost hst = new JHost();
							hst.setSiteNumber(siteNo);
							hst.setSiteDescription(newdesc);

							hostList.add(hst);
							setSaveButtonState(true);

							populateList(newdesc);
							getHostData();
						}
					});
				}
				{
					jButtonApply = new JButton4j(Common.icon_ok);
					desktopPane.add(jButtonApply);
					jButtonApply.setText("Confirm Changes");
					jButtonApply.setBounds(885, 175, 160, 36);
					jButtonApply.setEnabled(false);
					jButtonApply.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							int j = jListHosts.getSelectedIndex();
							JHost hst = setHostData();

							hostList.set(j, hst);
							setEditable(false);
							populateList("");
							jButtonApply.setEnabled(false);
							jButtonCancel.setEnabled(false);

							setSaveButtonState(true);
							jButtonTest.setEnabled(true);
							jListHosts.setSelectedIndex(j);
						}
					});
				}
				{
					jButtonDelete = new JButton4j(Common.icon_delete);
					desktopPane.add(jButtonDelete);
					jButtonDelete.setText("Delete DB Connection");
					jButtonDelete.setBounds(885, 79, 160, 36);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jListHosts.getSelectedIndex() > -1)
							{
								int j = jListHosts.getSelectedIndex();
								hostList.remove(j);
								renumberHosts();
								populateList("");
								if (hostList.size() > 0)
								{
									if (j > (hostList.size() - 1))
									{
										j--;
									}
									if (j >= 0)
									{
										populateList(hostList.get(j).getSiteDescription());
									}
								}

								getHostData();
								setSaveButtonState(true);
							}
						}
					});
				}
				{
					jButtonUp = new JButton4j(Common.icon_arrow_up);
					desktopPane.add(jButtonUp);
					jButtonUp.setEnabled(false);
					jButtonUp.setBounds(284, 221, 28, 28);
					jButtonUp.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jListHosts.getSelectedIndex() > -1)
							{
								int j = jListHosts.getSelectedIndex();
								JHost element = ((JHost) jListHosts.getModel().getElementAt(j));
								hostList = moveElementUp(hostList, element);
								renumberHosts();
								populateList(element.getSiteDescription());
								setSaveButtonState(true);
							}
						}
					});
				}
				{
					jButtonDown = new JButton4j(Common.icon_arrow_down);
					desktopPane.add(jButtonDown);
					jButtonDown.setEnabled(false);
					jButtonDown.setBounds(284, 256, 28, 28);
					jButtonDown.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jListHosts.getSelectedIndex() > -1)
							{
								int j = jListHosts.getSelectedIndex();
								JHost element = ((JHost) jListHosts.getModel().getElementAt(j));
								hostList = moveElementDown(hostList, element);
								renumberHosts();
								populateList(element.getSiteDescription());
								setSaveButtonState(true);
							}
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close);
					desktopPane.add(jButtonClose);
					jButtonClose.setText("Close");
					jButtonClose.setBounds(885, 405, 160, 36);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							logger.debug("JFrameHostAdmin closed");
							dispose();
						}
					});
				}
				{
					jButtonUndo = new JButton4j(Common.icon_undo);
					desktopPane.add(jButtonUndo);
					jButtonUndo.setText("Undo Changes");
					jButtonUndo.setBounds(885, 271, 160, 36);
					jButtonUndo.setEnabled(false);
					jButtonUndo.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							// Common.hosts = JXMLHost.loadHosts(false);
							int j = jListHosts.getSelectedIndex();
							String Current = hostList.get(j).getSiteDescription();
							getHosts();
							populateList(Current);
							getHostData();
							setSaveButtonState(false);
							setSaveButtonState(false);
						}
					});
				}
				{

					jButtonSave = new JButton4j(Common.icon_update);
					desktopPane.add(jButtonSave);
					jButtonSave.setText("Save DB Connections");
					jButtonSave.setBounds(885, 207, 160, 36);
					jButtonSave.setEnabled(false);
					jButtonSave.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							String pass1 = "";
							String pass2 = "";
							pass1 = String.valueOf(setupPasswordField.getPassword());
							pass2 = String.valueOf(verifyPasswordField.getPassword());

							if (pass1.equals(pass2))
							{

								String splash;
								if (jCheckBoxSplash.isSelected())
								{
									splash = "Y";
								}
								else
								{
									splash = "N";
								}

								if (JXMLHost.validateServiceHostPresent(hostList) == false)
								{
									JOptionPane.showMessageDialog(rootPane, "No host has been assigned to the interface service.", "Warning", JOptionPane.WARNING_MESSAGE);
								}

								JXMLHost.writeHosts(hostsFilename, hostList, splash, JTextFieldUpdateURL.getText(), getUpdateMode(), jTextField4jInstallDir.getText(), pass1, jTextField4jHostVersion.getText(), jTextField4jHostUpdatePath.getText());
								jButtonSave.setEnabled(false);
								jButtonUndo.setEnabled(false);
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Setup password has been changed but failed verification - please check.", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					});
				}
				{
					jTextFieldDescription = new JTextField4j();
					jTextFieldDescription.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDescription);
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(425, 40, 435, 21);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldURL = new JTextField4j();
					jTextFieldURL.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldURL);
					jTextFieldURL.setFocusCycleRoot(true);
					jTextFieldURL.setBounds(425, 65, 435, 21);
					jTextFieldURL.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jLabelDatabaseType = new JLabel4j_std();
					desktopPane.add(jLabelDatabaseType);
					jLabelDatabaseType.setText("Database Type");
					jLabelDatabaseType.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDatabaseType.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDatabaseType.setBounds(291, 91, 127, 21);

				}
				{

					ComboBoxModel<String> jComboBoxjdbcDriverModel = new DefaultComboBoxModel<String>(new String[]
					{ "", "mySQL", "Oracle", "SQL Server", "Web URL" });
					jComboBoxjdbcDriver = new JComboBox4j<String>();
					jComboBoxjdbcDriver.setBackground(Color.WHITE);
					desktopPane.add(jComboBoxjdbcDriver);
					jComboBoxjdbcDriver.setModel(jComboBoxjdbcDriverModel);
					jComboBoxjdbcDriver.setBounds(425, 90, 164, 21);
					jComboBoxjdbcDriver.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("Oracle"))
							{
								jTextFieldDriver.setText("oracle.jdbc.driver.OracleDriver");
								jTextFieldConnect.setText("jdbc:oracle:thin:@jdbcServer:jdbcPort:jdbcSID");
								jTextFieldDatabase.setText("database_name");
								jTextFieldUsername.setText("sql_username");
								jTextFieldPassword.setText("sql_password");
								jTextFieldSID.setText("orcl");
								jTextFieldDateTime.setText("sysdate");
								jTextFieldSelectLimit.setText("rownum");
								jTextFieldServer.setText("ip_address");
								jTextFieldPort.setText("1521");
							}
							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("mySQL"))
							{
								jTextFieldDriver.setText("com.mysql.cj.jdbc.Driver");
								jTextFieldConnect.setText("jdbc:mysql://jdbcServer/jdbcDatabase");
								jTextFieldDatabase.setText("database_name?autoReconnect=true");
								jTextFieldUsername.setText("sql_username");
								jTextFieldPassword.setText("sql_password");
								jTextFieldDateTime.setText("sysdate");
								jTextFieldSelectLimit.setText("limit");
								jTextFieldServer.setText("ip_address");
								jTextFieldPort.setText("3306");
							}
							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("SQL Server"))
							{
								jTextFieldDriver.setText("com.microsoft.sqlserver.jdbc.SQLServerDriver");
								jTextFieldConnect.setText("jdbc:sqlserver://jdbcServer\\jdbcSID");
								jTextFieldDatabase.setText("database_name;selectMethod=direct");
								jTextFieldUsername.setText("sql_username");
								jTextFieldPassword.setText("sql_password");
								jTextFieldDateTime.setText("sysdate");
								jTextFieldSelectLimit.setText("top");
								jTextFieldServer.setText("ip_address");
								jTextFieldPort.setText("1433");
							}
							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("Web URL"))
							{
								jTextFieldDriver.setText("http");
								jTextFieldConnect.setText("");
								jTextFieldDateTime.setText("");
								jTextFieldSelectLimit.setText("");
								jTextFieldServer.setText("");
								jTextFieldPort.setText("");
							}
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldConnect = new JTextField4j();
					jTextFieldConnect.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldConnect);
					jTextFieldConnect.setFocusCycleRoot(true);
					jTextFieldConnect.setBounds(425, 145, 435, 21);
					jTextFieldConnect.setEditable(false);
					jTextFieldConnect.setEnabled(false);
					jTextFieldConnect.setDisabledTextColor(Common.color_text_disabled);
					jTextFieldConnect.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jLabelDriver = new JLabel4j_std();
					desktopPane.add(jLabelDriver);
					jLabelDriver.setText("Driver");
					jLabelDriver.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDriver.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDriver.setBounds(291, 116, 127, 21);

				}
				{
					jLabelDatabase = new JLabel4j_std();
					desktopPane.add(jLabelDatabase);
					jLabelDatabase.setText("Database");
					jLabelDatabase.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDatabase.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDatabase.setBounds(320, 196, 98, 21);

				}
				{
					jLabelDBDateTime = new JLabel4j_std();
					desktopPane.add(jLabelDBDateTime);
					jLabelDBDateTime.setText("DB Date Time");
					jLabelDBDateTime.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDBDateTime.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDBDateTime.setBounds(291, 321, 127, 21);

				}
				{
					jLabelPassword = new JLabel4j_std();
					desktopPane.add(jLabelPassword);
					jLabelPassword.setText("Password");
					jLabelPassword.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelPassword.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelPassword.setBounds(291, 296, 127, 21);

				}
				{
					jLabelDescription = new JLabel4j_std();
					desktopPane.add(jLabelDescription);
					jLabelDescription.setText("Description");
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDescription.setBounds(291, 41, 127, 21);
				}
				{
					jLabelSiteNo = new JLabel4j_std();
					desktopPane.add(jLabelSiteNo);
					jLabelSiteNo.setText("Site No");
					jLabelSiteNo.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSiteNo.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSiteNo.setBounds(291, 15, 127, 21);

				}
				{
					jLabelUsername = new JLabel4j_std();
					desktopPane.add(jLabelUsername);
					jLabelUsername.setText("Username");
					jLabelUsername.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelUsername.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelUsername.setBounds(291, 271, 127, 21);

				}
				{
					jLabelPort = new JLabel4j_std();
					desktopPane.add(jLabelPort);
					jLabelPort.setText("Port");
					jLabelPort.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelPort.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelPort.setBounds(320, 221, 98, 21);

				}
				{
					jTextFieldDateTime = new JTextField4j();
					jTextFieldDateTime.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDateTime);
					jTextFieldDateTime.setFocusCycleRoot(true);
					jTextFieldDateTime.setBounds(425, 320, 435, 21);
					jTextFieldDateTime.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldUsername = new JTextField4j();
					jTextFieldUsername.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldUsername);
					jTextFieldUsername.setFocusCycleRoot(true);
					jTextFieldUsername.setBounds(425, 270, 435, 21);
					jTextFieldUsername.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldPassword = new JPasswordField(10);
					jTextFieldPassword.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldPassword);
					jTextFieldPassword.setFocusCycleRoot(true);
					jTextFieldPassword.setBounds(425, 295, 435, 21);
					jTextFieldPassword.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldPort = new JTextField4j();
					jTextFieldPort.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldPort);
					jTextFieldPort.setFocusCycleRoot(true);
					jTextFieldPort.setBounds(425, 220, 435, 21);
					jTextFieldPort.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldSID = new JTextField4j();
					jTextFieldSID.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldSID);
					jTextFieldSID.setFocusCycleRoot(true);
					jTextFieldSID.setBounds(425, 245, 435, 21);
					jTextFieldSID.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldSiteNo = new JTextField4j();
					desktopPane.add(jTextFieldSiteNo);
					jTextFieldSiteNo.setFocusCycleRoot(true);
					jTextFieldSiteNo.setBounds(425, 14, 28, 21);
					jTextFieldSiteNo.setHorizontalAlignment(SwingConstants.CENTER);
					jTextFieldSiteNo.setEnabled(false);
					jTextFieldSiteNo.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldDriver = new JTextField4j();
					jTextFieldDriver.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDriver);
					jTextFieldDriver.setFocusCycleRoot(true);
					jTextFieldDriver.setBounds(425, 117, 435, 21);
					jTextFieldDriver.setEditable(false);
					jTextFieldDriver.setEnabled(false);
					jTextFieldDriver.setDisabledTextColor(Common.color_text_disabled);
					jTextFieldDriver.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jCheckBoxEnabled = new JCheckBox4j();
					jCheckBoxEnabled.setToolTipText("If disabled the site will not appear in the list seen by the user.");
					jCheckBoxEnabled.setFont(Common.font_std);
					desktopPane.add(jCheckBoxEnabled);
					jCheckBoxEnabled.setText("Enabled");
					jCheckBoxEnabled.setBounds(467, 14, 91, 21);
					jCheckBoxEnabled.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxEnabled.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jCheckBoxSplash = new JCheckBox4j();
					jCheckBoxSplash.setToolTipText("<html>Can be used to disable the splash screen which can be useful<br> \nif using Citrix as it reduces the amount of screen drawing and <br>\nhence network bandwidth.</html>");
					jCheckBoxSplash.setSelected(true);
					jCheckBoxSplash.setFont(Common.font_std);
					desktopPane.add(jCheckBoxSplash);
					jCheckBoxSplash.setText("Enable Splash Screen");
					jCheckBoxSplash.setBounds(687, 445, 150, 21);
					jCheckBoxSplash.setBackground(new java.awt.Color(255, 255, 255));
					jCheckBoxSplash.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jLabelSID = new JLabel4j_std();
					desktopPane.add(jLabelSID);
					jLabelSID.setText("SID");
					jLabelSID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSID.setBounds(320, 246, 98, 21);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_cancel);
					desktopPane.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.setBounds(885, 335, 160, 36);
					jButtonCancel.setEnabled(false);
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							getHostData();
						}
					});
				}
				{
					jTextFieldServer = new JTextField4j();
					jTextFieldServer.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldServer);
					jTextFieldServer.setFocusCycleRoot(true);
					jTextFieldServer.setBounds(425, 170, 435, 21);
					jTextFieldServer.setEnabled(false);
					jTextFieldServer.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jLabelConnectionString = new JLabel4j_std();
					desktopPane.add(jLabelConnectionString);
					jLabelConnectionString.setText("Connect String");
					jLabelConnectionString.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelConnectionString.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelConnectionString.setBounds(291, 141, 127, 21);
				}
				{
					jTextFieldDatabase = new JTextField4j();
					jTextFieldDatabase.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDatabase);
					jTextFieldDatabase.setEnabled(false);
					jTextFieldDatabase.setFocusCycleRoot(true);
					jTextFieldDatabase.setBounds(425, 195, 435, 21);
					jTextFieldDatabase.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jLabelServer = new JLabel4j_std();
					desktopPane.add(jLabelServer);
					jLabelServer.setText("Server");
					jLabelServer.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelServer.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelServer.setBounds(320, 171, 98, 21);
				}
				{
					jButtonTest = new JButton4j(Common.icon_connect);
					desktopPane.add(jButtonTest);
					jButtonTest.setText("Connect to DB");
					jButtonTest.setBounds(885, 111, 160, 36);
					jButtonTest.setToolTipText("Connect to selected Host Database");
					jButtonTest.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							writeBackHosts(hostList);

							int j = jListHosts.getSelectedIndex();
							JHost hst = (JHost) jListHosts.getModel().getElementAt(j);
							Common.selectedHostID = hst.getSiteNumber();
							if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID))
							{
								btnSchema.setEnabled(true);
								jButtonUpdate.setEnabled(true);
								Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
							}
							else
							{
								jButtonUpdate.setEnabled(false);
								btnSchema.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update);
					desktopPane.add(jButtonUpdate);
					jButtonUpdate.setText("Create/Update Tables");
					jButtonUpdate.setBounds(885, 143, 160, 36);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setToolTipText("Create or Upgrade Application Database Schema");
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							writeBackHosts(hostList);

							int j = jListHosts.getSelectedIndex();
							JHost hst = (JHost) jListHosts.getModel().getElementAt(j);
							Common.selectedHostID = hst.getSiteNumber();

							if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID))
							{
								JDBSchema schema = new JDBSchema(Common.sessionID, Common.hostList.getHost(Common.selectedHostID));
								JDBUpdateRequest updrst = new JDBUpdateRequest();
								updrst = schema.validate(false);

								if (updrst.schema_updateRequired)
								{
									int continueUpdate = JOptionPane.showConfirmDialog(me, "Current Schema Version is " + String.valueOf(updrst.schema_CURVersion) + ", required version is " + String.valueOf(updrst.schema_NEWVersion) + ". Upgrade ?",
											"Connection to (" + hst.getSiteDescription() + ")", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

									if (continueUpdate == 0)
									{

										labelCommand.setText("Loading SQL commands, please wait....");
										LinkedList<JDBDDL> cmds = new LinkedList<JDBDDL>();
										cmds.clear();
										cmds = JXMLSchema.loadDDLStatements(jTextFieldDriver.getText(), "xml/schema/" + Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver() + "/");
										boolean updateCtrl = false;
										if (cmds.size() > 0)
										{
											if (schema.executeDDL(cmds, progressBar, labelCommand) == true)
											{
												updateCtrl = true;

											}
											else
											{
												JUtility.errorBeep();
												JDialogDMLErrors dmlerrs = new JDialogDMLErrors(me, cmds, updrst);
												dmlerrs.setModal(true);
												int ignoreDDLErrors = JOptionPane.showConfirmDialog(me, "Ignore Errors and set SCHEMA version to " + String.valueOf(updrst.schema_NEWVersion) + " ?", "Connection to (" + hst.getSiteDescription() + ")",
														JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

												if (ignoreDDLErrors == 0)
												{
													updateCtrl = true;
												}
											}
										}
										else
										{
											JOptionPane.showMessageDialog(me, "No DDL Commands found", "Connection to (" + hst.getSiteDescription() + ")", JOptionPane.WARNING_MESSAGE);

										}

										if (updateCtrl)
										{
											JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
											if (ctrl.getProperties("SCHEMA VERSION"))
											{
												ctrl.setKeyValue(String.valueOf(updrst.schema_NEWVersion));
												ctrl.update();
											}
											else
											{
												ctrl.create("SCHEMA VERSION", String.valueOf(updrst.schema_NEWVersion), "Schema Version");
											}
											JOptionPane.showMessageDialog(me, "Schema Version now set to " + String.valueOf(JVersion.getSchemaVersion()), "Control Table", JOptionPane.INFORMATION_MESSAGE);

										}

									}
								}
								else
								{
									JOptionPane.showMessageDialog(me, "No Schema update Required", "Connection to (" + hst.getSiteDescription() + ")", JOptionPane.INFORMATION_MESSAGE);
								}

								if (updrst.program_updateRequired)
								{
									JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);

									if (ctrl.getProperties("PROGRAM VERSION"))
									{
										ctrl.setKeyValue(JVersion.getProgramVersion());
										ctrl.update();
									}
									else
									{
										ctrl.create("PROGRAM VERSION", JVersion.getProgramVersion(), "Program Version");
									}
									JOptionPane.showMessageDialog(me, "Program Version now set to " + JVersion.getProgramVersion(), "Control Table", JOptionPane.INFORMATION_MESSAGE);

								}
								Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
							}
						}
					});
				}
				{
					jTextFieldSelectLimit = new JTextField4j();
					jTextFieldSelectLimit.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldSelectLimit);
					jTextFieldSelectLimit.setBounds(425, 345, 435, 21);
					jTextFieldSelectLimit.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
					jTextFieldSelectLimit.setFocusCycleRoot(true);
					jTextFieldSelectLimit.setEnabled(false);
				}
				{
					jLabelSelectTime = new JLabel4j_std();
					desktopPane.add(jLabelSelectTime);
					jLabelSelectTime.setText("DB Select Limit");
					jLabelSelectTime.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSelectTime.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSelectTime.setBounds(291, 346, 127, 21);

				}
				{
					jLabelSchema = new JLabel4j_std();
					desktopPane.add(jLabelSchema);
					jLabelSchema.setText("DB Schema");
					jLabelSchema.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSchema.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSchema.setBounds(291, 370, 127, 21);
				}
				{
					jTextFieldSchema = new JTextField4j();
					jTextFieldSchema.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldSchema);
					jTextFieldSchema.setEnabled(false);
					jTextFieldSchema.setBounds(425, 370, 435, 21);
					jTextFieldSchema.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
					jTextFieldSchema.setFocusCycleRoot(true);
				}

				{
					jLabelMenuURL = new JLabel4j_std();
					jLabelMenuURL.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelMenuURL.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelMenuURL.setText("URL");
					jLabelMenuURL.setBounds(291, 66, 127, 21);
					desktopPane.add(jLabelMenuURL);
				}

				{
					jTextFieldUniqueID = new JTextField4j();
					jTextFieldUniqueID.setBackground(Common.color_listBackground);
					jTextFieldUniqueID.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{
							jTextFieldKeyTyped();
						}
					});
					jTextFieldUniqueID.setEditable(true);
					jTextFieldUniqueID.setBounds(425, 395, 435, 21);
					desktopPane.add(jTextFieldUniqueID);
				}

				{
					jLabelUniqueID = new JLabel4j_std();
					jLabelUniqueID.setText("Unique ID");
					jLabelUniqueID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelUniqueID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelUniqueID.setBounds(291, 396, 127, 21);
					desktopPane.add(jLabelUniqueID);
				}
				{
					btnSchema = new JButton4j(Common.icon_report);
					btnSchema.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{

							writeBackHosts(hostList);

							int j = jListHosts.getSelectedIndex();
							JHost hst = (JHost) jListHosts.getModel().getElementAt(j);
							Common.selectedHostID = hst.getSiteNumber();
							if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID))
							{
								btnSchema.setEnabled(true);
								jButtonUpdate.setEnabled(true);
								JDBStructure struct = new JDBStructure(Common.selectedHostID, Common.sessionID);
								struct.exportSchema();
								struct.saveAs("SCHEMA " + Common.hostList.getHost(Common.selectedHostID).getSiteDescription() + ".txt", Common.mainForm);
								Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);

							}
							else
							{
								jButtonUpdate.setEnabled(false);
								btnSchema.setEnabled(false);
							}
						}
					});
					btnSchema.setText("DB Schema Report");
					btnSchema.setEnabled(false);
					btnSchema.setBounds(885, 303, 160, 36);
					desktopPane.add(btnSchema);
				}
			}

			progressBar.setBounds(10, 578, 1035, 28);
			progressBar.setBackground(Color.WHITE);
			progressBar.setForeground(Color.BLUE);
			desktopPane.add(progressBar);

			labelCommand.setBounds(10, 608, 1052, 23);
			labelCommand.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			desktopPane.add(labelCommand);

			JButton4j btnService = new JButton4j(Common.icon_interface);
			btnService.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{

					if (jListHosts.getSelectedIndex() > -1)
					{
						int j = jListHosts.getSelectedIndex();
						String desc = hostList.get(j).getSiteDescription();

						for (int x = 0; x < hostList.size(); x++)
						{
							if (x == j)
							{
								hostList.get(x).setUniqueID("service");
							}
							else
							{
								hostList.get(x).setUniqueID("");
							}

						}

						populateList(desc);

						getHostData();
						setSaveButtonState(true);
					}

				}
			});
			btnService.setToolTipText("Connect to selected Host Database");
			btnService.setText("Assign to Service");
			btnService.setBounds(885, 239, 160, 36);
			desktopPane.add(btnService);

			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setFont(new Font("Arial", Font.BOLD, 11));
			label4j_std.setText("Connections");
			label4j_std.setHorizontalTextPosition(SwingConstants.LEFT);
			label4j_std.setHorizontalAlignment(SwingConstants.LEFT);
			label4j_std.setBounds(14, 15, 127, 21);
			desktopPane.add(label4j_std);

			JLabel4j_std JLabelUpdateHostURL = new JLabel4j_std();
			JLabelUpdateHostURL.setText("URL for Application Update");
			JLabelUpdateHostURL.setHorizontalTextPosition(SwingConstants.RIGHT);
			JLabelUpdateHostURL.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabelUpdateHostURL.setBounds(278, 470, 140, 21);
			desktopPane.add(JLabelUpdateHostURL);

			JTextFieldUpdateURL = new JTextField4j();
			JTextFieldUpdateURL.setToolTipText("<html>This should contail the path or network url which points to the<br>\nupdate directory. The update directory should contain the latest version<br>\nof the application plus the file updates.xml<br><br>\nThe URL should also include the name of the file updates.xml file at the end.<br>\nAn example path would look like this :-<br><br>\nfile://servername/sharename/directory/subdirectory/updates.xml\n</html>");
			JTextFieldUpdateURL.setBackground(Common.color_list_assigned);
			JTextFieldUpdateURL.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent arg0)
				{
					jTextFieldKeyTyped();
				}
			});
			JTextFieldUpdateURL.setFocusCycleRoot(true);
			JTextFieldUpdateURL.setBounds(425, 470, 599, 21);
			desktopPane.add(JTextFieldUpdateURL);
			rdbtnManual.setToolTipText("<html>\nYou can determine if a install can be updated automatically via the checkbox’s<br>\nMANUAL v AUTOMATIC. Normally end user workstations would have a hosts<br>\nfile which would be set to Automatic. A server install which includes the interface<br>\nservice would be more appropriatly set to manual updates.<br>\n</html>");

			rdbtnManual.setSelected(true);
			rdbtnManual.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			buttonGroup.add(rdbtnManual);
			rdbtnManual.setBounds(425, 444, 106, 23);
			rdbtnManual.setBackground(Common.color_app_window);
			desktopPane.add(rdbtnManual);
			rdbtnAutomatic.setToolTipText("<html>\nYou can determine if a install can be updated automatically via the checkbox’s<br>\nMANUAL v AUTOMATIC. Normally end user workstations would have a hosts<br>\nfile which would be set to Automatic. A server install which includes the interface<br>\nservice would be more appropriatly set to manual updates.<br>\n</html>");

			rdbtnAutomatic.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			buttonGroup.add(rdbtnAutomatic);
			rdbtnAutomatic.setBounds(543, 444, 109, 23);
			rdbtnAutomatic.setBackground(Common.color_app_window);
			desktopPane.add(rdbtnAutomatic);

			JLabel4j_std label4j_std_1 = new JLabel4j_std();
			label4j_std_1.setText("Application Update Mode");
			label4j_std_1.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_1.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_1.setBounds(278, 448, 140, 21);
			desktopPane.add(label4j_std_1);

			JLabel4j_std label4j_std_2 = new JLabel4j_std();
			label4j_std_2.setText("Install To");
			label4j_std_2.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_2.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_2.setBounds(291, 496, 127, 21);
			desktopPane.add(label4j_std_2);

			jTextField4jInstallDir.setText("");
			jTextField4jInstallDir.setFocusCycleRoot(true);
			jTextField4jInstallDir.setBounds(425, 496, 599, 21);
			desktopPane.add(jTextField4jInstallDir);

			setupPasswordField = new JPasswordField(20);
			setupPasswordField.setToolTipText("Password protect these settings");
			setupPasswordField.setBackground(Common.color_list_unassigned);
			setupPasswordField.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent arg0)
				{
					jTextFieldKeyTyped();
				}
			});
			setupPasswordField.setFocusCycleRoot(true);
			setupPasswordField.setBounds(124, 470, 148, 21);
			desktopPane.add(setupPasswordField);

			JLabel4j_std label4j_std_3 = new JLabel4j_std();
			label4j_std_3.setText("Setup Password");
			label4j_std_3.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_3.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_3.setBounds(14, 470, 98, 21);
			desktopPane.add(label4j_std_3);

			verifyPasswordField = new JPasswordField(20);
			verifyPasswordField.setBackground(Common.color_list_unassigned);
			verifyPasswordField.setFocusCycleRoot(true);
			verifyPasswordField.setBounds(124, 496, 148, 21);
			verifyPasswordField.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent arg0)
				{
					jTextFieldKeyTyped();
				}
			});
			desktopPane.add(verifyPasswordField);

			JLabel4j_std label4j_std_4 = new JLabel4j_std();
			label4j_std_4.setText("Verify Password");
			label4j_std_4.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_4.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_4.setBounds(14, 496, 98, 21);
			desktopPane.add(label4j_std_4);

			JButton4j jButtonOpen = new JButton4j(Common.icon_open);
			jButtonOpen.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{

					JFileChooser chooser = new JFileChooser();
					JFileFilterXML xmlFilter = new JFileFilterXML();
					chooser.setFileFilter(xmlFilter);
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setSelectedFile(new File(hostsFilename));
					chooser.addChoosableFileFilter(xmlFilter);

					int returnVal = chooser.showOpenDialog(jTextFieldUsername);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						setHostsFilename(chooser.getSelectedFile().getAbsolutePath());
						getHosts();
						populateList("");
					}
				}
			});
			jButtonOpen.setText("Open Hosts File");
			jButtonOpen.setBounds(885, 15, 160, 36);
			desktopPane.add(jButtonOpen);

			JLabel4j_std label4j_std_5 = new JLabel4j_std();
			label4j_std_5.setText("URL for Hosts File Update");
			label4j_std_5.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_5.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_5.setBounds(278, 550, 140, 21);
			desktopPane.add(label4j_std_5);

			jTextField4jHostUpdatePath = new JTextField4j();
			jTextField4jHostUpdatePath.setToolTipText("<html>This should contail the path or network url which points to the<br>\ncurrent or updated hosts file. The URL should point to the latest version<br>\nof the hosts.xml settings file. The version number in the current version is<br>\ncompared with the version number in the remote file to determine if the current<br>\nversion needs to be updated.<br><br>\nThe URL should also include the name of the file hosts.xml file at the end.<br>\nAn example path would look like this :-<br><br>\nfile://servername/sharename/directory/subdirectory/hosts.xml\n</html>");
			jTextField4jHostUpdatePath.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			jTextField4jHostUpdatePath.setBackground(Common.color_list_assigned);
			jTextField4jHostUpdatePath.setText("");
			jTextField4jHostUpdatePath.setFocusCycleRoot(true);
			jTextField4jHostUpdatePath.setBounds(425, 550, 599, 21);
			desktopPane.add(jTextField4jHostUpdatePath);

			JLabel4j_std label4j_std_6 = new JLabel4j_std();
			label4j_std_6.setText("Hosts FIle Version");
			label4j_std_6.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_6.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_6.setBounds(291, 524, 127, 21);
			desktopPane.add(label4j_std_6);

			jTextField4jHostVersion = new JTextField4j();
			jTextField4jHostVersion.setToolTipText("<html>If the desktop version determines that the URL for hosts update contains<br>\na file with higher version number than it's own then it will automatically<br>\nupdate itself.</html>");
			jTextField4jHostVersion.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			jTextField4jHostVersion.setBackground(Common.color_list_assigned);
			jTextField4jHostVersion.setHorizontalAlignment(SwingConstants.CENTER);
			jTextField4jHostVersion.setFocusCycleRoot(true);
			jTextField4jHostVersion.setBounds(425, 524, 53, 21);
			desktopPane.add(jTextField4jHostVersion);

			JButton4j button4j = new JButton4j(Common.icon_search);
			button4j.setToolTipText("Validate");
			button4j.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String filePath = JUtility.replaceNullStringwithBlank(JTextFieldUpdateURL.getText());
					if (filePath.equals("") == false)
					{
						if (Files.exists(Paths.get(filePath)))
						{
							JOptionPane.showMessageDialog(jTextFieldPort, "Valid.", "Host Path", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(jTextFieldPort, "Invalid Path [" + filePath + "]", "Host Path", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			button4j.setBounds(1024, 470, 21, 21);
			desktopPane.add(button4j);

			JButton4j button4j_1 = new JButton4j(Common.icon_search);
			button4j_1.setToolTipText("Validate");
			button4j_1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String filePath = JUtility.replaceNullStringwithBlank(jTextField4jHostUpdatePath.getText());
					if (filePath.equals("") == false)
					{
						if (Files.exists(Paths.get(filePath)))
						{
							JOptionPane.showMessageDialog(jTextFieldPort, "Valid.", "Update Path", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(jTextFieldPort, "Invalid Path [" + filePath + "]", "Update Path", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			button4j_1.setBounds(1024, 550, 21, 21);
			desktopPane.add(button4j_1);
			
			jButtonHelp = new JButton4j(Common.icon_help);
			jButtonHelp.setText("Help");
			jButtonHelp.setBounds(885, 370, 160, 36);
			desktopPane.add(jButtonHelp);

			jTextField4jInstallDir.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent arg0)
				{
					jTextFieldKeyTyped();
				}
			});

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
