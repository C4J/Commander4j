package com.commander4j.notifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;

public class JDialogSysInfo extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField jTextFieldJavaVersion;
	private JTextField jTextFieldOSName;
	private JTextField jTextFieldOSVersion;
	private JTextField jTextFieldModbusIP;
	private JTextField jTextFieldModbusPort;
	private JTextField jTextFieldModbusCoil;
	private JTextField textField_LabelSource;
	private JTextField textField_DataSetPath;
	private JTextField textField_OutputPath;
	private JTextField jTextFieldUserDir;
	private JTextField jTextFieldUsername;
	private JTextField jTextFieldWorkstationID;
	private String uuid = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogSysInfo dialog = new JDialogSysInfo("");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public JDialogSysInfo(String uuid)
	{
		setUuid(uuid);
		
		setTitle("System Information ["+getClientName()+"]"+" Line [" + AutoLab.getProdLine_Name(uuid) + "]");
		setResizable(false);
		setAlwaysOnTop(true);
		setSize(585, 417);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton("Close");
		okClose.setBounds(263, 358, 79, 29);
		contentPanel.add(okClose);
		okClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		okClose.setActionCommand("OK");
		getRootPane().setDefaultButton(okClose);
		
		JLabel lblComputerName = new JLabel("Computer Name :");
		lblComputerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblComputerName.setBounds(14, 20, 144, 16);
		contentPanel.add(lblComputerName);
		
		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(14, 48, 144, 16);
		contentPanel.add(lblUsername);
		
		JLabel lblHomeFolder = new JLabel("Install Folder :");
		lblHomeFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHomeFolder.setBounds(14, 76, 144, 16);
		contentPanel.add(lblHomeFolder);
		
		JLabel lblJavaVersion = new JLabel("Java Version :");
		lblJavaVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblJavaVersion.setBounds(14, 104, 144, 16);
		contentPanel.add(lblJavaVersion);
		
		JLabel lblOsName = new JLabel("OS Name :");
		lblOsName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOsName.setBounds(14, 132, 144, 16);
		contentPanel.add(lblOsName);
		
		JLabel lblOsVersion = new JLabel("OS Version :");
		lblOsVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOsVersion.setBounds(14, 160, 144, 16);
		contentPanel.add(lblOsVersion);
		
		JLabel lblModbusIp = new JLabel("Modbus IP :");
		lblModbusIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusIp.setBounds(14, 188, 144, 16);
		contentPanel.add(lblModbusIp);
		
		JLabel lblModbusPort = new JLabel("Modbus Port :");
		lblModbusPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusPort.setBounds(14, 213, 144, 16);
		contentPanel.add(lblModbusPort);
		
		JLabel lblCoilId = new JLabel("Modbus Coil ID :");
		lblCoilId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCoilId.setBounds(14, 241, 144, 16);
		contentPanel.add(lblCoilId);
		
		JLabel lblLabelSyncPath = new JLabel("Label Sync Path :");
		lblLabelSyncPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLabelSyncPath.setBounds(14, 269, 144, 16);
		contentPanel.add(lblLabelSyncPath);
		
		JLabel lblDatasetPath = new JLabel("DataSet Path :");
		lblDatasetPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatasetPath.setBounds(14, 297, 144, 16);
		contentPanel.add(lblDatasetPath);
		
		JLabel lblOutputPath = new JLabel("Output Path :");
		lblOutputPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutputPath.setBounds(14, 323, 144, 16);
		contentPanel.add(lblOutputPath);
		
		jTextFieldJavaVersion = new JTextField();
		jTextFieldJavaVersion.setEditable(false);
		jTextFieldJavaVersion.setBounds(166, 99, 372, 26);
		contentPanel.add(jTextFieldJavaVersion);
		jTextFieldJavaVersion.setColumns(10);
		
		jTextFieldOSName = new JTextField();
		jTextFieldOSName.setEditable(false);
		jTextFieldOSName.setColumns(10);
		jTextFieldOSName.setBounds(166, 127, 372, 26);
		contentPanel.add(jTextFieldOSName);
		
		jTextFieldOSVersion = new JTextField();
		jTextFieldOSVersion.setEditable(false);
		jTextFieldOSVersion.setColumns(10);
		jTextFieldOSVersion.setBounds(166, 155, 372, 26);
		contentPanel.add(jTextFieldOSVersion);
		
		jTextFieldModbusIP = new JTextField();
		jTextFieldModbusIP.setEditable(false);
		jTextFieldModbusIP.setColumns(10);
		jTextFieldModbusIP.setBounds(166, 183, 372, 26);
		contentPanel.add(jTextFieldModbusIP);
		
		jTextFieldModbusPort = new JTextField();
		jTextFieldModbusPort.setEditable(false);
		jTextFieldModbusPort.setColumns(10);
		jTextFieldModbusPort.setBounds(166, 208, 372, 26);
		contentPanel.add(jTextFieldModbusPort);
		
		jTextFieldModbusCoil = new JTextField();
		jTextFieldModbusCoil.setEditable(false);
		jTextFieldModbusCoil.setColumns(10);
		jTextFieldModbusCoil.setBounds(166, 236, 372, 26);
		contentPanel.add(jTextFieldModbusCoil);
		
		textField_LabelSource = new JTextField();
		textField_LabelSource.setEditable(false);
		textField_LabelSource.setColumns(10);
		textField_LabelSource.setBounds(166, 264, 372, 26);
		contentPanel.add(textField_LabelSource);
		
		textField_DataSetPath = new JTextField();
		textField_DataSetPath.setEditable(false);
		textField_DataSetPath.setColumns(10);
		textField_DataSetPath.setBounds(166, 292, 372, 26);
		contentPanel.add(textField_DataSetPath);
		
		textField_OutputPath = new JTextField();
		textField_OutputPath.setEditable(false);
		textField_OutputPath.setColumns(10);
		textField_OutputPath.setBounds(166, 320, 372, 26);
		contentPanel.add(textField_OutputPath);
		
		jTextFieldUserDir = new JTextField();
		jTextFieldUserDir.setEditable(false);
		jTextFieldUserDir.setColumns(10);
		jTextFieldUserDir.setBounds(166, 71, 372, 26);
		contentPanel.add(jTextFieldUserDir);
		
		jTextFieldUsername = new JTextField();
		jTextFieldUsername.setEditable(false);
		jTextFieldUsername.setColumns(10);
		jTextFieldUsername.setBounds(166, 43, 372, 26);
		contentPanel.add(jTextFieldUsername);
		
		jTextFieldWorkstationID = new JTextField();
		jTextFieldWorkstationID.setEditable(false);
		jTextFieldWorkstationID.setColumns(10);
		jTextFieldWorkstationID.setBounds(170, 15, 372, 26);
		contentPanel.add(jTextFieldWorkstationID);
		
		jTextFieldUserDir.setText(System.getProperty("user.dir"));
		jTextFieldOSVersion.setText(System.getProperty("os.version"));
		jTextFieldOSName.setText(System.getProperty("os.name"));
		jTextFieldJavaVersion.setText(System.getProperty("java.version"));
		jTextFieldWorkstationID.setText(getClientName());
		jTextFieldUsername.setText(System.getProperty("user.name"));
		jTextFieldModbusIP.setText(AutoLab.getModBus_IP(getUuid()));
		jTextFieldModbusPort.setText(AutoLab.getModBus_Port(getUuid()));
		jTextFieldModbusCoil.setText(AutoLab.getModBus_CoilID(getUuid()));
		textField_LabelSource.setText(AutoLab.getLabelSource());
		textField_DataSetPath.setText(AutoLab.getDataSetPath(getUuid()));
		textField_OutputPath.setText(AutoLab.config.getOutputPath());
	}
	
	private  String getClientName()
	{
		String hostname = "Unknown";

		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		
		return hostname;
	}
	
	private String getUuid()
	{
		return uuid;
	}

	private void setUuid(String uuid)
	{
		this.uuid = uuid;
	}
	
}
