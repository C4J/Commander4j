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
	private JTextField jTextFieldUserDir;
	private JTextField jTextFieldUsername;
	private JTextField jTextFieldWorkstationID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogSysInfo dialog = new JDialogSysInfo();
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
	public JDialogSysInfo()
	{
		
		setTitle("System Information ["+getClientName()+"]");
		setResizable(false);
		setAlwaysOnTop(true);
		setSize(585, 263);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton("Close");
		okClose.setBounds(253, 193, 79, 29);
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
		
}
