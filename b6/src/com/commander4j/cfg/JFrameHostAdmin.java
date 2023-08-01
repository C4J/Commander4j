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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.Logger;

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
import javax.swing.JButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextArea;


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
	private JLabel4j_std jLabelTimeZone;
	private JLabel4j_std jLabelDBDateTime;
	private JLabel4j_std jLabelConnectionString;
	private JTextField4j jTextFieldServer;
	private JButton4j jButtonCancel;
	private JLabel4j_std jLabelSID;
	private JLabel4j_std jLabelDatabase;
	private JLabel4j_std jLabelDriver;
	private JTextArea jTextFieldConnect;
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
	final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JFrameHostAdmin.class);
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
	private JTextField4j textField4jActualSchema = new JTextField4j();
	private JTextField4j textField4jActualProgram = new JTextField4j();
	private JTextField4j textField4jReqdSchema = new JTextField4j();
	private JTextField4j textField4jReqdProgram = new JTextField4j();
	private JLabel labelActualProgramVersion = new JLabel("");
	private JLabel labelActualSchemaVersion = new JLabel("");
	private JButton4j btn4jAmendSchemaVersion = new JButton4j(Common.icon_edit_16x16);
	private JButton4j btn4jAmendProgramVersion = new JButton4j(Common.icon_edit_16x16);
	private JComboBox4j<String> jComboBoxjdbcTimeZone = new JComboBox4j<String>();
	private JCheckBox chckbxUseTimeZoneInConnect = new JCheckBox("Use Timezone in Connection");
	private JButton btnDefaultTimeZone = new JButton("");
	private JButton btnDefaultCollation = new JButton("");
	private JButton btnDefaultCharSet = new JButton("");
	private JComboBox4j<String> jComboBoxCollation = new JComboBox4j<String>();
	private JComboBox4j<String> jComboBoxCharSet = new JComboBox4j<String>();
	private DefaultComboBoxModel<String> collationModel;
	private DefaultComboBoxModel<String> CharSetModel;

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

	private void buildCollationList()
	{
		collationModel = new DefaultComboBoxModel<String>();
		collationModel.addElement("");
		collationModel.addElement("armscii8_general_ci");
		collationModel.addElement("ascii_general_ci");
		collationModel.addElement("big5_chinese_ci");
		collationModel.addElement("binary");
		collationModel.addElement("cp1250_general_ci");
		collationModel.addElement("cp1251_general_ci");
		collationModel.addElement("cp1256_general_ci");
		collationModel.addElement("cp1257_general_ci");
		collationModel.addElement("cp850_general_ci");
		collationModel.addElement("cp852_general_ci");
		collationModel.addElement("cp866_general_ci");
		collationModel.addElement("cp932_japanese_ci");
		collationModel.addElement("dec8_swedish_ci");
		collationModel.addElement("eucjpms_japanese_ci");
		collationModel.addElement("euckr_korean_ci");
		collationModel.addElement("gb2312_chinese_ci");
		collationModel.addElement("gbk_chinese_ci");
		collationModel.addElement("geostd8_general_ci");
		collationModel.addElement("greek_general_ci");
		collationModel.addElement("hebrew_general_ci");
		collationModel.addElement("hp8_english_ci");
		collationModel.addElement("keybcs2_general_ci");
		collationModel.addElement("koi8r_general_ci");
		collationModel.addElement("koi8u_general_ci");
		collationModel.addElement("latin1_swedish_ci");
		collationModel.addElement("latin2_general_ci");
		collationModel.addElement("latin5_turkish_ci");
		collationModel.addElement("latin7_general_ci");
		collationModel.addElement("macce_general_ci");
		collationModel.addElement("macroman_general_ci");
		collationModel.addElement("sjis_japanese_ci");
		collationModel.addElement("swe7_swedish_ci");
		collationModel.addElement("tis620_thai_ci");
		collationModel.addElement("ucs2_general_ci");
		collationModel.addElement("ujis_japanese_ci");
		collationModel.addElement("utf16_general_ci");
		collationModel.addElement("utf16le_general_ci");
		collationModel.addElement("utf32_general_ci");
		collationModel.addElement("utf8_general_ci");
		collationModel.addElement("utf8mb4_0900_ai_ci");
		collationModel.addElement("utf8mb4_general_ci");

		jComboBoxCollation.setModel(collationModel);
		jComboBoxCollation.setMaximumRowCount(collationModel.getSize());

	}

	private void buildCharSetList()
	{
		CharSetModel = new DefaultComboBoxModel<String>();
		CharSetModel.addElement("");
		CharSetModel.addElement("armscii8");
		CharSetModel.addElement("ascii");
		CharSetModel.addElement("big5");
		CharSetModel.addElement("binary");
		CharSetModel.addElement("cp1250");
		CharSetModel.addElement("cp1251");
		CharSetModel.addElement("cp1256");
		CharSetModel.addElement("cp1257");
		CharSetModel.addElement("cp850");
		CharSetModel.addElement("cp852");
		CharSetModel.addElement("cp866");
		CharSetModel.addElement("cp932");
		CharSetModel.addElement("dec8");
		CharSetModel.addElement("eucjpms");
		CharSetModel.addElement("euckr");
		CharSetModel.addElement("gb18030");
		CharSetModel.addElement("gb2312");
		CharSetModel.addElement("gbk");
		CharSetModel.addElement("geostd8");
		CharSetModel.addElement("greek");
		CharSetModel.addElement("hebrew");
		CharSetModel.addElement("hp8");
		CharSetModel.addElement("keybcs2");
		CharSetModel.addElement("koi8r");
		CharSetModel.addElement("koi8u");
		CharSetModel.addElement("latin1");
		CharSetModel.addElement("latin2");
		CharSetModel.addElement("latin5");
		CharSetModel.addElement("latin7");
		CharSetModel.addElement("macce");
		CharSetModel.addElement("macroman");
		CharSetModel.addElement("sjis");
		CharSetModel.addElement("swe7");
		CharSetModel.addElement("tis620");
		CharSetModel.addElement("ucs2");
		CharSetModel.addElement("ujis");
		CharSetModel.addElement("utf16");
		CharSetModel.addElement("utf16le");
		CharSetModel.addElement("utf32");
		CharSetModel.addElement("utf8");
		CharSetModel.addElement("utf8mb4");

		jComboBoxCharSet.setModel(CharSetModel);
		jComboBoxCharSet.setMaximumRowCount(CharSetModel.getSize());

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
		setBounds(100, 100, 1187, 752);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		desktopPane.setBounds(0, 0, 1190, 762);
		desktopPane.setBackground(Color.WHITE);
		contentPane.add(desktopPane);

		Common.sessionID = JUnique.getUniqueID();
		Common.sd.setData(Common.sessionID, "silentExceptions", "No", true);
		JUtility.initLogging("");
		logger.debug("JFrameHostAdmin starting...");

		initGUI();

		buildCollationList();
		buildCharSetList();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, "http://commander4j.com/mw/index.php?title=Setup4j");

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
		textField4jActualProgram.setText("");
		textField4jActualSchema.setText("");
		labelActualProgramVersion.setIcon(null);
		labelActualSchemaVersion.setIcon(null);
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

			jComboBoxjdbcTimeZone.setSelectedItem(hst.getDatabaseParameters().getjdbcDatabaseTimeZone());
			chckbxUseTimeZoneInConnect.setSelected(hst.getDatabaseParameters().isjdbcDatabaseTimeZoneEnable());
			jComboBoxCollation.setSelectedItem(hst.getDatabaseParameters().getjdbcCollation());
			jComboBoxCharSet.setSelectedItem(hst.getDatabaseParameters().getjdbcCharacterSet());

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
			jComboBoxjdbcTimeZone.setSelectedItem("");
			chckbxUseTimeZoneInConnect.setSelected(false);
			jComboBoxCollation.setSelectedItem("");
			jComboBoxCharSet.setSelectedItem("");
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

			if (jComboBoxjdbcTimeZone.getSelectedIndex() > 0)
			{
				hst.getDatabaseParameters().setjdbcDatabaseTimeZone(jComboBoxjdbcTimeZone.getSelectedItem().toString());
			}
			else
			{
				hst.getDatabaseParameters().setjdbcDatabaseTimeZone("");
			}

			if (jComboBoxCollation.getSelectedIndex() > 0)
			{
				hst.getDatabaseParameters().setjdbcCollation(jComboBoxCollation.getSelectedItem().toString());
			}
			else
			{
				hst.getDatabaseParameters().setjdbcCollation("");
			}

			if (jComboBoxCharSet.getSelectedIndex() > 0)
			{
				hst.getDatabaseParameters().setjdbcCharacterSet(jComboBoxCharSet.getSelectedItem().toString());
			}
			else
			{
				hst.getDatabaseParameters().setjdbcCharacterSet("");
			}

			hst.getDatabaseParameters().setjdbcDatabaseTimeZoneEnable(chckbxUseTimeZoneInConnect.isSelected());
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

	private void checkVersions()
	{
		String result = "";

		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		String currentProgramVersion = ctrl.getKeyValue("PROGRAM VERSION");
		String requiredProgramVersion = JVersion.getProgramVersion();
		textField4jActualProgram.setText(currentProgramVersion);
		String currentSchemaVersion = ctrl.getKeyValue("SCHEMA VERSION");
		String requiredSchemaVersion = String.valueOf(JVersion.getSchemaVersion());
		textField4jActualSchema.setText(currentSchemaVersion);

		if (currentProgramVersion.equals(requiredProgramVersion))
		{
			labelActualProgramVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "success-tick.png"));
		}
		else
		{
			if (currentProgramVersion.equals(""))
			{
				labelActualProgramVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "empty-db.gif"));
				result = "Commander4j version not found in database, use the AUTO Update option to install them";
			}
			else
			{
				labelActualProgramVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "fail-cross.png"));
				result = "Commander4j version needs updating, use the AUTO Update option to update it";
			}
		}

		if (currentSchemaVersion.equals(requiredSchemaVersion))
		{
			labelActualSchemaVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "success-tick.png"));
		}
		else
		{
			if (currentSchemaVersion.equals(""))
			{
				labelActualSchemaVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "empty-db.gif"));
				result = "No Commander4j tables found, use the AUTO Update option to install them";
			}
			else
			{
				labelActualSchemaVersion.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator + "fail-cross.png"));
				result = "Commander4j tables found but need to be updated, use the AUTO Update option to update them";
			}
		}
		labelActualSchemaVersion.repaint();
		labelActualProgramVersion.repaint();
		labelCommand.setText(result);
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
					jScrollPane1.setBounds(14, 37, 250, 415);
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
					jButtonAdd = new JButton4j(Common.icon_add_16x16);
					desktopPane.add(jButtonAdd);
					jButtonAdd.setText("Add DB Connection");
					jButtonAdd.setBounds(1011, 49, 160, 35);
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
					jButtonApply = new JButton4j(Common.icon_ok_16x16);
					desktopPane.add(jButtonApply);
					jButtonApply.setText("Confirm Changes");
					jButtonApply.setBounds(1011, 185, 160, 35);
					jButtonApply.setEnabled(false);
					jButtonApply.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jComboBoxjdbcTimeZone.getSelectedIndex() > 0)
							{
								chckbxUseTimeZoneInConnect.setSelected(true);
							}

							if (chckbxUseTimeZoneInConnect.isSelected() == true && jComboBoxjdbcTimeZone.getSelectedIndex() == -1)
							{
								chckbxUseTimeZoneInConnect.setSelected(false);
							}

							if (chckbxUseTimeZoneInConnect.isSelected() == false && jComboBoxjdbcTimeZone.getSelectedIndex() > 0)
							{
								jComboBoxjdbcTimeZone.setSelectedIndex(0);
							}

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
					jButtonDelete = new JButton4j(Common.icon_delete_16x16);
					desktopPane.add(jButtonDelete);
					jButtonDelete.setText("Delete DB Connection");
					jButtonDelete.setBounds(1011, 83, 160, 35);
					jButtonDelete.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							if (jListHosts.getSelectedIndex() > -1)
							{
								int j = jListHosts.getSelectedIndex();
								String d = hostList.get(j).getSiteDescription();
								JUtility.errorBeep();
								int n = JOptionPane.showConfirmDialog(null, "Delete [" + d + "] ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);

								if (n == 0)
								{
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
						}
					});
				}
				{
					jButtonUp = new JButton4j(Common.icon_arrow_up_16x16);
					desktopPane.add(jButtonUp);
					jButtonUp.setEnabled(false);
					jButtonUp.setBounds(270, 205, 28, 28);
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
					jButtonDown = new JButton4j(Common.icon_arrow_down_16x16);
					desktopPane.add(jButtonDown);
					jButtonDown.setEnabled(false);
					jButtonDown.setBounds(270, 240, 28, 28);
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
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					desktopPane.add(jButtonClose);
					jButtonClose.setText("Close");
					jButtonClose.setBounds(1011, 423, 160, 35);
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
					jButtonUndo = new JButton4j(Common.icon_undo_16x16);
					desktopPane.add(jButtonUndo);
					jButtonUndo.setText("Undo Changes");
					jButtonUndo.setBounds(1011, 287, 160, 35);
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

					jButtonSave = new JButton4j(Common.icon_update_16x16);
					desktopPane.add(jButtonSave);
					jButtonSave.setText("Save Configuration");
					jButtonSave.setBounds(1011, 219, 160, 35);
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
					jTextFieldDescription.setBounds(410, 40, 590, 21);
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
					jTextFieldURL.setBounds(410, 65, 590, 21);
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
					jLabelDatabaseType.setBounds(275, 92, 127, 21);

				}

				String[] allZones = TimeZone.getAvailableIDs();

				int sizeOfArray = allZones.length;

				String[] list = new String[sizeOfArray + 1];

				list[0] = "";

				for (int x = 0; x < allZones.length; x++)
				{
					list[x + 1] = allZones[x];
				}

				ComboBoxModel<String> jComboBoxjdbcTimeZoneModel = new DefaultComboBoxModel<String>(list);
				jComboBoxjdbcTimeZone.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent e)
					{
						if (jComboBoxjdbcTimeZone.getSelectedIndex() >= 0)
						{
							chckbxUseTimeZoneInConnect.setSelected(true);
						}
						else
						{
							chckbxUseTimeZoneInConnect.setSelected(false);
						}
					}
				});
				jComboBoxjdbcTimeZone.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jTextFieldKeyTyped();
					}
				});

				jComboBoxjdbcTimeZone.setBounds(410, 370, 258, 21);
				jComboBoxjdbcTimeZone.setModel(jComboBoxjdbcTimeZoneModel);
				jComboBoxjdbcTimeZone.setMaximumRowCount(25);
				desktopPane.add(jComboBoxjdbcTimeZone);

				chckbxUseTimeZoneInConnect.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						if (chckbxUseTimeZoneInConnect.isSelected() == false)
						{
							jComboBoxjdbcTimeZone.setSelectedIndex(-1);
						}
						else
						{
							if (jComboBoxjdbcTimeZone.getSelectedIndex() <= 0)
							{
								jComboBoxjdbcTimeZone.setSelectedItem(TimeZone.getDefault().getID());
							}
						}
						jTextFieldKeyTyped();
					}
				});
				chckbxUseTimeZoneInConnect.setFont(Common.font_std);
				chckbxUseTimeZoneInConnect.setBackground(Color.WHITE);
				chckbxUseTimeZoneInConnect.setBounds(410, 344, 217, 23);
				desktopPane.add(chckbxUseTimeZoneInConnect);
				{

					ComboBoxModel<String> jComboBoxjdbcDriverModel = new DefaultComboBoxModel<String>(new String[]
					{ "", "mySQL", "Oracle", "SQL Server", "Web URL" });
					jComboBoxjdbcDriver = new JComboBox4j<String>();
					jComboBoxjdbcDriver.setToolTipText("Select the required database type.");
					jComboBoxjdbcDriver.setBackground(Color.WHITE);
					desktopPane.add(jComboBoxjdbcDriver);
					jComboBoxjdbcDriver.setModel(jComboBoxjdbcDriverModel);
					jComboBoxjdbcDriver.setBounds(410, 92, 193, 21);
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
								jComboBoxjdbcTimeZone.setSelectedItem("");
								jComboBoxjdbcTimeZone.setEnabled(false);
								chckbxUseTimeZoneInConnect.setEnabled(false);
								btnDefaultTimeZone.setEnabled(false);
								jComboBoxCollation.setEnabled(false);
								jComboBoxCollation.setSelectedItem("");
								jComboBoxCharSet.setEnabled(false);
								jComboBoxCharSet.setSelectedItem("");
								btnDefaultCollation.setEnabled(false);
								btnDefaultCharSet.setEnabled(false);
							}

							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("mySQL"))
							{
								jTextFieldDriver.setText("com.mysql.cj.jdbc.Driver");
								jTextFieldConnect.setText("jdbc:mysql://jdbcServer/jdbcDatabase");
								jTextFieldDatabase.setText("database_name");
								jTextFieldUsername.setText("sql_username");
								jTextFieldPassword.setText("sql_password");
								jTextFieldDateTime.setText("sysdate");
								jTextFieldSelectLimit.setText("limit");
								jTextFieldServer.setText("ip_address");
								jTextFieldPort.setText("3306");
								jComboBoxjdbcTimeZone.setSelectedItem("");
								jComboBoxjdbcTimeZone.setEnabled(true);
								chckbxUseTimeZoneInConnect.setEnabled(true);
								btnDefaultTimeZone.setEnabled(true);
								jComboBoxCollation.setEnabled(true);
								jComboBoxCollation.setSelectedItem("utf8mb4_0900_ai_ci");
								jComboBoxCharSet.setEnabled(true);
								jComboBoxCharSet.setSelectedItem("utf8mb4");
								btnDefaultCollation.setEnabled(true);
								btnDefaultCharSet.setEnabled(true);
							}

							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("SQL Server"))
							{
								jTextFieldDriver.setText("com.microsoft.sqlserver.jdbc.SQLServerDriver");
								jTextFieldConnect.setText("jdbc:sqlserver://jdbcServer\\jdbcSID");
								jTextFieldDatabase.setText("database_name");
								jTextFieldUsername.setText("sql_username");
								jTextFieldPassword.setText("sql_password");
								jTextFieldDateTime.setText("sysdate");
								jTextFieldSelectLimit.setText("top");
								jTextFieldServer.setText("ip_address");
								jTextFieldPort.setText("1433");
								jComboBoxjdbcTimeZone.setSelectedItem("");
								jComboBoxjdbcTimeZone.setEnabled(false);
								chckbxUseTimeZoneInConnect.setEnabled(false);
								btnDefaultTimeZone.setEnabled(false);
								jComboBoxCollation.setEnabled(false);
								jComboBoxCollation.setSelectedItem("");
								jComboBoxCharSet.setEnabled(false);
								jComboBoxCharSet.setSelectedItem("");
								btnDefaultCollation.setEnabled(false);
								btnDefaultCharSet.setEnabled(false);
							}

							if (jComboBoxjdbcDriver.getSelectedItem().toString().equals("Web URL"))
							{
								jTextFieldDriver.setText("http");
								jTextFieldConnect.setText("");
								jTextFieldDateTime.setText("");
								jTextFieldSelectLimit.setText("");
								jTextFieldServer.setText("");
								jTextFieldPort.setText("");
								jComboBoxjdbcTimeZone.setSelectedItem("");
								jComboBoxjdbcTimeZone.setEnabled(false);
								chckbxUseTimeZoneInConnect.setEnabled(false);
								btnDefaultTimeZone.setEnabled(false);
								chckbxUseTimeZoneInConnect.setEnabled(false);
								btnDefaultTimeZone.setEnabled(false);
								jComboBoxCollation.setEnabled(false);
								jComboBoxCollation.setSelectedItem("");
								jComboBoxCharSet.setEnabled(false);
								jComboBoxCharSet.setSelectedItem("");
								btnDefaultCollation.setEnabled(false);
								btnDefaultCharSet.setEnabled(false);
							}
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldConnect = new JTextArea();
					jTextFieldConnect.setWrapStyleWord(true);
					jTextFieldConnect.setLineWrap(true);
					jTextFieldConnect.setToolTipText("jdbc Connection URL");
					jTextFieldConnect.setEditable(false);
					jTextFieldConnect.setFont(Common.font_std);
					desktopPane.add(jTextFieldConnect);
					jTextFieldConnect.setFocusCycleRoot(true);
					jTextFieldConnect.setBounds(410, 120, 590, 37);
				}
				{
					jLabelDriver = new JLabel4j_std();
					desktopPane.add(jLabelDriver);
					jLabelDriver.setText("Driver");
					jLabelDriver.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDriver.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDriver.setBounds(276, 164, 127, 21);

				}
				{
					jLabelDatabase = new JLabel4j_std();
					desktopPane.add(jLabelDatabase);
					jLabelDatabase.setText("Database");
					jLabelDatabase.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDatabase.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDatabase.setBounds(305, 215, 98, 21);

				}
				{
					jLabelDBDateTime = new JLabel4j_std();
					desktopPane.add(jLabelDBDateTime);
					jLabelDBDateTime.setText("DB Date Time");
					jLabelDBDateTime.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDBDateTime.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDBDateTime.setBounds(275, 445, 127, 21);

				}
				{
					jLabelPassword = new JLabel4j_std();
					desktopPane.add(jLabelPassword);
					jLabelPassword.setText("Password");
					jLabelPassword.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelPassword.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelPassword.setBounds(276, 315, 127, 21);

				}
				{
					jLabelTimeZone = new JLabel4j_std();
					desktopPane.add(jLabelTimeZone);
					jLabelTimeZone.setText("DB Timezone");
					jLabelTimeZone.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelTimeZone.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelTimeZone.setBounds(275, 370, 127, 21);

				}
				{
					jLabelDescription = new JLabel4j_std();
					desktopPane.add(jLabelDescription);
					jLabelDescription.setText("Description");
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDescription.setBounds(275, 40, 127, 21);
				}
				{
					jLabelSiteNo = new JLabel4j_std();
					desktopPane.add(jLabelSiteNo);
					jLabelSiteNo.setText("Connection No");
					jLabelSiteNo.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSiteNo.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSiteNo.setBounds(275, 14, 127, 21);

				}
				{
					jLabelUsername = new JLabel4j_std();
					desktopPane.add(jLabelUsername);
					jLabelUsername.setText("Username");
					jLabelUsername.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelUsername.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelUsername.setBounds(276, 290, 127, 21);

				}
				{
					jLabelPort = new JLabel4j_std();
					desktopPane.add(jLabelPort);
					jLabelPort.setText("Port");
					jLabelPort.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelPort.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelPort.setBounds(305, 240, 98, 21);

				}
				{
					jTextFieldDateTime = new JTextField4j();
					jTextFieldDateTime.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDateTime);
					jTextFieldDateTime.setFocusCycleRoot(true);
					jTextFieldDateTime.setBounds(410, 445, 282, 21);
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
					jTextFieldUsername.setBounds(411, 290, 282, 21);
					jTextFieldUsername.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jTextFieldKeyTyped();
						}
					});
				}
				{
					jTextFieldPassword = new JPasswordField(20);
					jTextFieldPassword.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldPassword);
					jTextFieldPassword.setFocusCycleRoot(true);
					jTextFieldPassword.setBounds(411, 315, 282, 21);
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
					jTextFieldPort.setBounds(411, 240, 282, 21);
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
					jTextFieldSID.setBounds(411, 265, 282, 21);
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
					jTextFieldSiteNo.setBounds(410, 14, 28, 21);
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
					jTextFieldDriver.setFont(Common.font_std);
					jTextFieldDriver.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDriver);
					jTextFieldDriver.setFocusCycleRoot(true);
					jTextFieldDriver.setBounds(411, 164, 282, 21);
					jTextFieldDriver.setEditable(false);
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
					jCheckBoxSplash.setBounds(772, 470, 150, 21);
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
					jLabelSID.setBounds(305, 265, 98, 21);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_cancel_16x16);
					desktopPane.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.setBounds(1011, 355, 160, 35);
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
					jTextFieldServer.setBounds(411, 190, 282, 21);
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
					jLabelConnectionString.setText("JDBC Connection URL");
					jLabelConnectionString.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelConnectionString.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelConnectionString.setBounds(275, 120, 127, 21);
				}
				{
					jTextFieldDatabase = new JTextField4j();
					jTextFieldDatabase.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldDatabase);
					jTextFieldDatabase.setEnabled(false);
					jTextFieldDatabase.setFocusCycleRoot(true);
					jTextFieldDatabase.setBounds(411, 215, 282, 21);
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
					jLabelServer.setBounds(305, 190, 98, 21);
				}
				{
					jButtonTest = new JButton4j(Common.icon_connect_16x16);
					desktopPane.add(jButtonTest);
					jButtonTest.setText("Connect / Check DB");
					jButtonTest.setBounds(1011, 117, 160, 35);
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
								checkVersions();
								btn4jAmendSchemaVersion.setEnabled(true);
								btn4jAmendProgramVersion.setEnabled(true);
								btnSchema.setEnabled(true);
								jButtonUpdate.setEnabled(true);

								Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
							}
							else
							{
								btn4jAmendSchemaVersion.setEnabled(false);
								btn4jAmendProgramVersion.setEnabled(false);
								jButtonUpdate.setEnabled(false);
								btnSchema.setEnabled(false);
							}
						}
					});
				}
				{
					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					desktopPane.add(jButtonUpdate);
					jButtonUpdate.setText("AUTO Update DB");
					jButtonUpdate.setBounds(1011, 151, 160, 35);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setToolTipText("Automatically Create or Upgrade Application Database Schema");
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
									String updateMesage = "";
									if (updrst.schema_CURVersion == -1)
									{
										updateMesage = "No existing database tables for Commander4j found, Create New ?";
									}
									else
									{
										updateMesage = "Current Schema Version is " + String.valueOf(updrst.schema_CURVersion) + ", required version is " + String.valueOf(updrst.schema_NEWVersion) + ". Upgrade ?";
									}

									int continueUpdate = JOptionPane.showConfirmDialog(me, updateMesage, "Connection to (" + hst.getSiteDescription() + ")", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);

									if (continueUpdate == 0)
									{

										labelCommand.setText("Loading SQL commands, please wait....");
										LinkedList<JDBDDL> cmds = new LinkedList<JDBDDL>();
										cmds.clear();
										cmds = JXMLSchema.loadDDLStatements(jTextFieldDriver.getText(), "xml/schema/" + Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver() + "/", labelCommand);
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
														JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm_16x16);

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

									if (ctrl.getProperties("PROGRAM VERSION"))
									{
										JOptionPane.showMessageDialog(me, "Program Version now set to " + JVersion.getProgramVersion(), "Control Table", JOptionPane.INFORMATION_MESSAGE);
									}

								}

								checkVersions();
								Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
							}
						}
					});
				}
				{
					jTextFieldSelectLimit = new JTextField4j();
					jTextFieldSelectLimit.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldSelectLimit);
					jTextFieldSelectLimit.setBounds(410, 470, 282, 21);
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
					jLabelSelectTime.setBounds(275, 470, 127, 21);

				}
				{
					jLabelSchema = new JLabel4j_std();
					desktopPane.add(jLabelSchema);
					jLabelSchema.setText("DB Schema");
					jLabelSchema.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSchema.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelSchema.setBounds(275, 495, 127, 21);
				}
				{
					jTextFieldSchema = new JTextField4j();
					jTextFieldSchema.setBackground(Common.color_listBackground);
					desktopPane.add(jTextFieldSchema);
					jTextFieldSchema.setEnabled(false);
					jTextFieldSchema.setBounds(410, 495, 282, 21);
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
					jLabelMenuURL.setBounds(275, 65, 127, 21);
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
					jTextFieldUniqueID.setEditable(false);
					jTextFieldUniqueID.setBounds(410, 520, 282, 21);
					desktopPane.add(jTextFieldUniqueID);
				}

				{
					jLabelUniqueID = new JLabel4j_std();
					jLabelUniqueID.setText("Unique ID");
					jLabelUniqueID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelUniqueID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelUniqueID.setBounds(275, 520, 127, 21);
					desktopPane.add(jLabelUniqueID);
				}
				{
					btnSchema = new JButton4j(Common.icon_report_16x16);
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
					btnSchema.setBounds(1011, 321, 160, 35);
					desktopPane.add(btnSchema);
				}
			}

			jComboBoxCollation.setMaximumRowCount(25);
			jComboBoxCollation.setBounds(410, 395, 258, 21);
			jComboBoxCollation.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			desktopPane.add(jComboBoxCollation);

			jComboBoxCharSet.setMaximumRowCount(25);
			jComboBoxCharSet.setBounds(410, 420, 258, 21);
			jComboBoxCharSet.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			desktopPane.add(jComboBoxCharSet);

			progressBar.setBounds(0, 661, 1185, 28);
			progressBar.setBackground(Color.WHITE);
			progressBar.setForeground(Color.BLUE);
			desktopPane.add(progressBar);
			labelCommand.setForeground(Color.RED);
			labelCommand.setFont(Common.font_std);

			labelCommand.setBounds(0, 691, 1183, 23);
			labelCommand.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			desktopPane.add(labelCommand);

			JButton4j btnService = new JButton4j(Common.icon_interface_16x16);
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
			btnService.setBounds(1011, 253, 160, 35);
			desktopPane.add(btnService);

			JLabel4j_std label4j_std2 = new JLabel4j_std();
			label4j_std2.setFont(Common.font_std);
			label4j_std2.setText("Connections");
			label4j_std2.setHorizontalTextPosition(SwingConstants.LEFT);
			label4j_std2.setHorizontalAlignment(SwingConstants.LEFT);
			label4j_std2.setBounds(14, 15, 127, 21);
			desktopPane.add(label4j_std2);

			JLabel4j_std JLabelUpdateHostURL = new JLabel4j_std();
			JLabelUpdateHostURL.setText("URL for Application Update");
			JLabelUpdateHostURL.setHorizontalTextPosition(SwingConstants.RIGHT);
			JLabelUpdateHostURL.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabelUpdateHostURL.setBounds(242, 549, 160, 21);
			desktopPane.add(JLabelUpdateHostURL);

			JTextFieldUpdateURL = new JTextField4j();
			JTextFieldUpdateURL.setToolTipText(
					"<html>This should contail the path or network url which points to the<br>\nupdate directory. The update directory should contain the latest version<br>\nof the application plus the file updates.xml<br><br>\nThe URL should also include the name of the file updates.xml file at the end.<br>\nAn example path would look like this :-<br><br>\nfile://servername/sharename/directory/subdirectory/updates.xml\n</html>");
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
			JTextFieldUpdateURL.setBounds(410, 549, 745, 21);
			desktopPane.add(JTextFieldUpdateURL);
			rdbtnManual.setToolTipText(
					"<html>\nYou can determine if a install can be updated automatically via the checkbox’s<br>\nMANUAL v AUTOMATIC. Normally end user workstations would have a hosts<br>\nfile which would be set to Automatic. A server install which includes the interface<br>\nservice would be more appropriatly set to manual updates.<br>\n</html>");
			rdbtnManual.setBackground(Color.WHITE);
			rdbtnManual.setSelected(true);
			rdbtnManual.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			buttonGroup.add(rdbtnManual);
			rdbtnManual.setBounds(736, 517, 106, 23);
			rdbtnManual.setBackground(Color.WHITE);
			desktopPane.add(rdbtnManual);
			rdbtnAutomatic.setToolTipText(
					"<html>\nYou can determine if a install can be updated automatically via the checkbox’s<br>\nMANUAL v AUTOMATIC. Normally end user workstations would have a hosts<br>\nfile which would be set to Automatic. A server install which includes the interface<br>\nservice would be more appropriatly set to manual updates.<br>\n</html>");

			rdbtnAutomatic.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jTextFieldKeyTyped();
				}
			});
			buttonGroup.add(rdbtnAutomatic);
			rdbtnAutomatic.setBounds(853, 518, 109, 23);
			rdbtnAutomatic.setBackground(Color.WHITE);
			desktopPane.add(rdbtnAutomatic);

			JLabel4j_std label4j_std_1 = new JLabel4j_std();
			label4j_std_1.setText("Application Update Mode");
			label4j_std_1.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_1.setHorizontalAlignment(SwingConstants.LEFT);
			label4j_std_1.setBounds(772, 495, 140, 21);
			desktopPane.add(label4j_std_1);

			JLabel4j_std label4j_std_2 = new JLabel4j_std();
			label4j_std_2.setText("Install To");
			label4j_std_2.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_2.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_2.setBounds(242, 575, 160, 21);
			desktopPane.add(label4j_std_2);

			jTextField4jInstallDir.setText("");
			jTextField4jInstallDir.setFocusCycleRoot(true);
			jTextField4jInstallDir.setBounds(410, 575, 745, 21);
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
			setupPasswordField.setBounds(116, 493, 148, 21);
			desktopPane.add(setupPasswordField);

			JLabel4j_std label4j_std_3 = new JLabel4j_std();
			label4j_std_3.setText("Setup Password");
			label4j_std_3.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_3.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_3.setBounds(17, 495, 91, 21);
			desktopPane.add(label4j_std_3);

			verifyPasswordField = new JPasswordField(20);
			verifyPasswordField.setBackground(Common.color_list_unassigned);
			verifyPasswordField.setFocusCycleRoot(true);
			verifyPasswordField.setBounds(116, 519, 148, 21);
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
			label4j_std_4.setBounds(17, 520, 91, 21);
			desktopPane.add(label4j_std_4);

			JButton4j jButtonOpen = new JButton4j(Common.icon_open_16x16);
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
			jButtonOpen.setBounds(1011, 15, 160, 35);
			desktopPane.add(jButtonOpen);

			JLabel4j_std label4j_std_5 = new JLabel4j_std();
			label4j_std_5.setText("URL for Hosts File Update");
			label4j_std_5.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_5.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_5.setBounds(242, 627, 160, 21);
			desktopPane.add(label4j_std_5);

			jTextField4jHostUpdatePath = new JTextField4j();
			jTextField4jHostUpdatePath.setToolTipText(
					"<html>This should contail the path or network url which points to the<br>\ncurrent or updated hosts file. The URL should point to the latest version<br>\nof the hosts.xml settings file. The version number in the current version is<br>\ncompared with the version number in the remote file to determine if the current<br>\nversion needs to be updated.<br><br>\nThe URL should also include the name of the file hosts.xml file at the end.<br>\nAn example path would look like this :-<br><br>\nfile://servername/sharename/directory/subdirectory/hosts.xml\n</html>");
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
			jTextField4jHostUpdatePath.setBounds(410, 627, 745, 21);
			desktopPane.add(jTextField4jHostUpdatePath);

			JLabel4j_std label4j_std_6 = new JLabel4j_std();
			label4j_std_6.setText("Hosts FIle Version");
			label4j_std_6.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_6.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_6.setBounds(242, 601, 160, 21);
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
			jTextField4jHostVersion.setBounds(410, 601, 53, 21);
			desktopPane.add(jTextField4jHostVersion);

			JButton4j button4j = new JButton4j(Common.icon_search_16x16);
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
			button4j.setBounds(1157, 549, 21, 21);
			desktopPane.add(button4j);

			JButton4j button4j_1 = new JButton4j(Common.icon_search_16x16);
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
			button4j_1.setBounds(1157, 627, 21, 21);
			desktopPane.add(button4j_1);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jButtonHelp.setText("Help");
			jButtonHelp.setBounds(1011, 389, 160, 35);
			desktopPane.add(jButtonHelp);

			jTextField4jInstallDir.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent arg0)
				{
					jTextFieldKeyTyped();
				}
			});

			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setBorder(new TitledBorder(null, "Version Information", TitledBorder.CENTER, TitledBorder.TOP, null, null));
			panel.setBounds(714, 194, 272, 256);
			desktopPane.add(panel);
			panel.setLayout(null);

			JLabel4j_std label4j_stdASV = new JLabel4j_std();
			label4j_stdASV.setText("Actual Schema Version");
			label4j_stdASV.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_stdASV.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_stdASV.setBounds(23, 62, 145, 21);
			panel.add(label4j_stdASV);

			JLabel4j_std label4j_stdRSV = new JLabel4j_std();
			label4j_stdRSV.setText("Required Schema Version");
			label4j_stdRSV.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_stdRSV.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_stdRSV.setBounds(23, 32, 145, 21);
			panel.add(label4j_stdRSV);

			JLabel4j_std label4j_std_RPV = new JLabel4j_std();
			label4j_std_RPV.setText("Required Progam Version");
			label4j_std_RPV.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_std_RPV.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_std_RPV.setBounds(23, 92, 145, 21);
			panel.add(label4j_std_RPV);

			JLabel4j_std label4j_stdAPV = new JLabel4j_std();
			label4j_stdAPV.setText("Actual Program Version");
			label4j_stdAPV.setHorizontalTextPosition(SwingConstants.RIGHT);
			label4j_stdAPV.setHorizontalAlignment(SwingConstants.RIGHT);
			label4j_stdAPV.setBounds(23, 122, 145, 21);
			panel.add(label4j_stdAPV);
			textField4jReqdSchema.setFont(Common.font_std);

			JLabel4j_std lbl4j_stdCollation = new JLabel4j_std();
			lbl4j_stdCollation.setText("Collation");
			lbl4j_stdCollation.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl4j_stdCollation.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4j_stdCollation.setBounds(275, 395, 127, 21);
			desktopPane.add(lbl4j_stdCollation);

			JLabel4j_std lbl4j_stdCharset = new JLabel4j_std();
			lbl4j_stdCharset.setText("CharSet");
			lbl4j_stdCharset.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl4j_stdCharset.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl4j_stdCharset.setBounds(275, 420, 127, 21);
			desktopPane.add(lbl4j_stdCharset);

			textField4jReqdSchema.setEditable(false);
			textField4jReqdSchema.setToolTipText("<html>The database schema version needed to run this<br>release of the application.</html>");
			textField4jReqdSchema.setText(String.valueOf(JVersion.getSchemaVersion()));
			textField4jReqdSchema.setHorizontalAlignment(SwingConstants.CENTER);
			textField4jReqdSchema.setFocusCycleRoot(true);
			textField4jReqdSchema.setBackground(new Color(255, 255, 204));
			textField4jReqdSchema.setBounds(181, 32, 53, 21);
			panel.add(textField4jReqdSchema);
			textField4jReqdProgram.setFont(Common.font_std);

			textField4jReqdProgram.setToolTipText(
					"<html>This shows the version of the program which should be used to <br>connect to this database. A warning is shown on the client if a different <br>version is used in which case the client may need updating or the<br>AUTO Update option needs to be run.</html>");
			textField4jReqdProgram.setText(JVersion.getProgramVersion());
			textField4jReqdProgram.setHorizontalAlignment(SwingConstants.CENTER);
			textField4jReqdProgram.setFocusCycleRoot(true);
			textField4jReqdProgram.setEditable(false);
			textField4jReqdProgram.setBackground(new Color(255, 255, 204));
			textField4jReqdProgram.setBounds(181, 92, 53, 21);
			panel.add(textField4jReqdProgram);
			textField4jActualSchema.setFont(Common.font_std);

			textField4jActualSchema.setToolTipText("<html>The current database schema version of the connected database.<br>If this is less than the required number you need to use the<br>AUTO Update option.</html>");
			textField4jActualSchema.setText("");
			textField4jActualSchema.setHorizontalAlignment(SwingConstants.CENTER);
			textField4jActualSchema.setFocusCycleRoot(true);
			textField4jActualSchema.setEditable(false);
			textField4jActualSchema.setBackground(new Color(255, 255, 204));
			textField4jActualSchema.setBounds(181, 62, 53, 21);
			panel.add(textField4jActualSchema);
			textField4jActualProgram.setFont(Common.font_std);

			textField4jActualProgram.setToolTipText("<html>The program version of the target database. If this is less than the required <br>program version you need to use the AUTO Update option</html>");
			textField4jActualProgram.setText("");
			textField4jActualProgram.setHorizontalAlignment(SwingConstants.CENTER);
			textField4jActualProgram.setFocusCycleRoot(true);
			textField4jActualProgram.setEditable(false);
			textField4jActualProgram.setBackground(new Color(255, 255, 204));
			textField4jActualProgram.setBounds(181, 122, 53, 21);
			panel.add(textField4jActualProgram);
			labelActualProgramVersion.setToolTipText("Shows the status of the target database. ");

			labelActualProgramVersion.setBounds(235, 115, 30, 30);
			panel.add(labelActualProgramVersion);
			labelActualSchemaVersion.setToolTipText("Shows the status of the target database. ");

			labelActualSchemaVersion.setBounds(235, 55, 30, 30);
			panel.add(labelActualSchemaVersion);
			btn4jAmendSchemaVersion.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String schemaVersion = JOptionPane.showInputDialog(null, "Amend Schema Version", textField4jReqdSchema.getText());
					if (schemaVersion != null)
					{
						if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID))
						{
							JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
							ctrl.setSystemKey("SCHEMA VERSION");
							ctrl.setKeyValue(schemaVersion);
							ctrl.update();
							checkVersions();
							Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
						}
					}
				}
			});

			btn4jAmendSchemaVersion.setEnabled(false);
			btn4jAmendSchemaVersion.setText("Correct Schema Version !");
			btn4jAmendSchemaVersion.setBounds(47, 155, 187, 39);
			panel.add(btn4jAmendSchemaVersion);
			btn4jAmendProgramVersion.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String programVersion = JOptionPane.showInputDialog(null, "Amend Program Version", textField4jReqdProgram.getText());
					if (programVersion != null)
					{
						if (Common.hostList.getHost(Common.selectedHostID).connect(Common.sessionID, Common.selectedHostID))
						{
							JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
							ctrl.setSystemKey("PROGRAM VERSION");
							ctrl.setKeyValue(programVersion);
							ctrl.update();
							checkVersions();
							Common.hostList.getHost(Common.selectedHostID).disconnect(Common.sessionID);
						}
					}
				}
			});

			btn4jAmendProgramVersion.setEnabled(false);
			btn4jAmendProgramVersion.setText("Correct Program Version !");
			btn4jAmendProgramVersion.setBounds(47, 192, 187, 39);
			panel.add(btn4jAmendProgramVersion);

			JLabel4j_std label4j_std21 = new JLabel4j_std();
			label4j_std21.setFont(Common.font_std);
			label4j_std21.setText("Protect Configuration with Password");
			label4j_std21.setHorizontalTextPosition(SwingConstants.CENTER);
			label4j_std21.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std21.setBounds(24, 467, 248, 21);
			desktopPane.add(label4j_std21);
			btnDefaultTimeZone.setToolTipText("Set default Timezone value");
			btnDefaultCollation.setToolTipText("Set default Collation");
			btnDefaultCharSet.setToolTipText("Set default CharSet");

			btnDefaultTimeZone.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (jComboBoxjdbcTimeZone.getSelectedItem().toString().equals(TimeZone.getDefault().getID()) == false)
					{
						jComboBoxjdbcTimeZone.setSelectedItem(TimeZone.getDefault().getID());
						jTextFieldKeyTyped();
					}
				}
			});
			btnDefaultTimeZone.setBounds(671, 370, 21, 21);
			desktopPane.add(btnDefaultTimeZone);

			btnDefaultCollation.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (jComboBoxCollation.getSelectedItem().toString().equals("utf8mb4_0900_ai_ci") == false)
					{
						jComboBoxCollation.setSelectedItem("utf8mb4_0900_ai_ci");
						jTextFieldKeyTyped();
					}
				}
			});
			btnDefaultCollation.setBounds(671, 395, 21, 21);
			desktopPane.add(btnDefaultCollation);

			btnDefaultCharSet.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (jComboBoxCharSet.getSelectedItem().toString().equals("utf8mb4") == false)
					{
						jComboBoxCharSet.setSelectedItem("utf8mb4");
						jTextFieldKeyTyped();
					}
				}
			});
			btnDefaultCharSet.setBounds(671, 420, 21, 21);
			desktopPane.add(btnDefaultCharSet);

			setHostsFilename(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "hosts" + File.separator + "hosts.xml");
			setIconImage(Common.imageIconloader.getImageIcon16x16(Common.image_osx_setup4j).getImage());
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
