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

public class JDialogProdLineInfo extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField jTextFieldModbusIP;
	private JTextField jTextFieldModbusPort;
	private JTextField jTextFieldModbusCoil;
	private JTextField textField_LabelSource;
	private JTextField textField_DataSetPath;
	private JTextField textField_OutputPath;
	private String uuid = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogProdLineInfo dialog = new JDialogProdLineInfo("");
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
	public JDialogProdLineInfo(String uuid)
	{
		setUuid(uuid);
		
		setTitle("System Information ["+getClientName()+"]"+" Line [" + AutoLab.getProdLine_Name(uuid) + "]");
		setResizable(false);
		setAlwaysOnTop(true);
		setSize(585, 258);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton("Close");
		okClose.setBounds(253, 195, 79, 29);
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
		
		JLabel lblModbusIp = new JLabel("Modbus IP :");
		lblModbusIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusIp.setBounds(21, 25, 144, 16);
		contentPanel.add(lblModbusIp);
		
		JLabel lblModbusPort = new JLabel("Modbus Port :");
		lblModbusPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusPort.setBounds(21, 50, 144, 16);
		contentPanel.add(lblModbusPort);
		
		JLabel lblCoilId = new JLabel("Modbus Coil ID :");
		lblCoilId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCoilId.setBounds(21, 78, 144, 16);
		contentPanel.add(lblCoilId);
		
		JLabel lblLabelSyncPath = new JLabel("Label Sync Path :");
		lblLabelSyncPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLabelSyncPath.setBounds(21, 106, 144, 16);
		contentPanel.add(lblLabelSyncPath);
		
		JLabel lblDatasetPath = new JLabel("DataSet Path :");
		lblDatasetPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatasetPath.setBounds(21, 134, 144, 16);
		contentPanel.add(lblDatasetPath);
		
		JLabel lblOutputPath = new JLabel("Output Path :");
		lblOutputPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutputPath.setBounds(21, 160, 144, 16);
		contentPanel.add(lblOutputPath);
		
		jTextFieldModbusIP = new JTextField();
		jTextFieldModbusIP.setEditable(false);
		jTextFieldModbusIP.setColumns(10);
		jTextFieldModbusIP.setBounds(173, 20, 372, 26);
		contentPanel.add(jTextFieldModbusIP);
		
		jTextFieldModbusPort = new JTextField();
		jTextFieldModbusPort.setEditable(false);
		jTextFieldModbusPort.setColumns(10);
		jTextFieldModbusPort.setBounds(173, 45, 372, 26);
		contentPanel.add(jTextFieldModbusPort);
		
		jTextFieldModbusCoil = new JTextField();
		jTextFieldModbusCoil.setEditable(false);
		jTextFieldModbusCoil.setColumns(10);
		jTextFieldModbusCoil.setBounds(173, 73, 372, 26);
		contentPanel.add(jTextFieldModbusCoil);
		
		textField_LabelSource = new JTextField();
		textField_LabelSource.setEditable(false);
		textField_LabelSource.setColumns(10);
		textField_LabelSource.setBounds(173, 101, 372, 26);
		contentPanel.add(textField_LabelSource);
		
		textField_DataSetPath = new JTextField();
		textField_DataSetPath.setEditable(false);
		textField_DataSetPath.setColumns(10);
		textField_DataSetPath.setBounds(173, 129, 372, 26);
		contentPanel.add(textField_DataSetPath);
		
		textField_OutputPath = new JTextField();
		textField_OutputPath.setEditable(false);
		textField_OutputPath.setColumns(10);
		textField_OutputPath.setBounds(173, 157, 372, 26);
		contentPanel.add(textField_OutputPath);
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
