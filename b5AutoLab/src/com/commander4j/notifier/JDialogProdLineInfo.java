package com.commander4j.notifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.resources.JRes;

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
	private ImageIcon img = new ImageIcon("./images/windows/image_ok.gif");
	private JTextField textFieldSSCCSequencePath;
	private JTextField textFieldPrinterName;
	private JTextField textFieldProductionLineName;
	private JTextField textFieldDataSetFilename;

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
		setIconImage(img.getImage());
		setTitle(JRes.getText("production_line")+" ["+getClientName()+"]"+" Line [" + AutoLab.getProdLine_Name(uuid) + "]");
		setResizable(false);
		setAlwaysOnTop(true);
		setSize(834, 381);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton(JRes.getText("close"));
		okClose.setBounds(364, 312, 106, 29);
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
		
		JLabel lblModbusIp = new JLabel(JRes.getText("modbus_ip")+ " :");
		lblModbusIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusIp.setBounds(6, 13, 247, 16);
		contentPanel.add(lblModbusIp);
		
		JLabel lblModbusPort = new JLabel(JRes.getText("modbus_port")+" :");
		lblModbusPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusPort.setBounds(6, 42, 247, 16);
		contentPanel.add(lblModbusPort);
		
		JLabel lblCoilId = new JLabel(JRes.getText("modbus_coil_id")+" :");
		lblCoilId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCoilId.setBounds(6, 71, 247, 16);
		contentPanel.add(lblCoilId);
		
		JLabel lblLabelSyncPath = new JLabel(JRes.getText("label_sync_path")+" :");
		lblLabelSyncPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLabelSyncPath.setBounds(6, 100, 247, 16);
		contentPanel.add(lblLabelSyncPath);
		
		JLabel lblDatasetPath = new JLabel(JRes.getText("dataset_path")+" :");
		lblDatasetPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatasetPath.setBounds(6, 216, 247, 16);
		contentPanel.add(lblDatasetPath);
		
		JLabel lblOutputPath = new JLabel(JRes.getText("output_declaration_path")+" :");
		lblOutputPath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutputPath.setBounds(6, 249, 247, 16);
		contentPanel.add(lblOutputPath);
		
		jTextFieldModbusIP = new JTextField();
		jTextFieldModbusIP.setEditable(false);
		jTextFieldModbusIP.setColumns(10);
		jTextFieldModbusIP.setBounds(262, 8, 566, 26);
		contentPanel.add(jTextFieldModbusIP);
		
		jTextFieldModbusPort = new JTextField();
		jTextFieldModbusPort.setEditable(false);
		jTextFieldModbusPort.setColumns(10);
		jTextFieldModbusPort.setBounds(262, 34, 566, 26);
		contentPanel.add(jTextFieldModbusPort);
		
		jTextFieldModbusCoil = new JTextField();
		jTextFieldModbusCoil.setEditable(false);
		jTextFieldModbusCoil.setColumns(10);
		jTextFieldModbusCoil.setBounds(262, 64, 566, 26);
		contentPanel.add(jTextFieldModbusCoil);
		
		textField_LabelSource = new JTextField();
		textField_LabelSource.setEditable(false);
		textField_LabelSource.setColumns(10);
		textField_LabelSource.setBounds(262, 94, 566, 26);
		contentPanel.add(textField_LabelSource);
		
		textField_DataSetPath = new JTextField();
		textField_DataSetPath.setEditable(false);
		textField_DataSetPath.setColumns(10);
		textField_DataSetPath.setBounds(262, 214, 566, 26);
		contentPanel.add(textField_DataSetPath);
		
		textField_OutputPath = new JTextField();
		textField_OutputPath.setEditable(false);
		textField_OutputPath.setColumns(10);
		textField_OutputPath.setBounds(262, 244, 566, 26);
		contentPanel.add(textField_OutputPath);
		
		JLabel lblSsccSequencePath = new JLabel(JRes.getText("sscc_sequence_path")+ " :");
		lblSsccSequencePath.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSsccSequencePath.setBounds(6, 279, 247, 16);
		
		textFieldSSCCSequencePath = new JTextField();
		textFieldSSCCSequencePath.setText((String) null);
		textFieldSSCCSequencePath.setEditable(false);
		textFieldSSCCSequencePath.setColumns(10);
		textFieldSSCCSequencePath.setBounds(262, 274, 566, 26);
		
		contentPanel.add(lblSsccSequencePath);
		contentPanel.add(textFieldSSCCSequencePath);
		
		textFieldPrinterName = new JTextField();
		textFieldPrinterName.setText((String) null);
		textFieldPrinterName.setEditable(false);
		textFieldPrinterName.setColumns(10);
		textFieldPrinterName.setBounds(262, 154, 566, 26);
		contentPanel.add(textFieldPrinterName);
		
		JLabel lblPrinterName = new JLabel(JRes.getText("printer_name")+" :");
		lblPrinterName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrinterName.setBounds(6, 159, 247, 16);
		contentPanel.add(lblPrinterName);
		
		JLabel lblProductionLineName = new JLabel(JRes.getText("production_line_name")+" :");
		lblProductionLineName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductionLineName.setBounds(6, 132, 247, 16);
		contentPanel.add(lblProductionLineName);
		
		textFieldProductionLineName = new JTextField();
		textFieldProductionLineName.setText((String) null);
		textFieldProductionLineName.setEditable(false);
		textFieldProductionLineName.setColumns(10);
		textFieldProductionLineName.setBounds(262, 124, 566, 26);
		contentPanel.add(textFieldProductionLineName);
		
		JLabel lblDatasetFilename = new JLabel(JRes.getText("dataset_filename")+" :");
		lblDatasetFilename.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatasetFilename.setBounds(6, 187, 247, 16);
		contentPanel.add(lblDatasetFilename);
		
		textFieldDataSetFilename = new JTextField();
		textFieldDataSetFilename.setText((String) null);
		textFieldDataSetFilename.setEditable(false);
		textFieldDataSetFilename.setColumns(10);
		textFieldDataSetFilename.setBounds(262, 184, 566, 26);
		contentPanel.add(textFieldDataSetFilename);
		

		

		refresh();


	}
	
	private void refresh()
	{
		jTextFieldModbusIP.setText(AutoLab.getModBus_IP(getUuid()));
		jTextFieldModbusPort.setText(AutoLab.getModBus_Port(getUuid()));
		jTextFieldModbusCoil.setText(AutoLab.getModBus_CoilID(getUuid()));
		textField_LabelSource.setText(AutoLab.getLabelSource());
		textField_DataSetPath.setText(AutoLab.getDataSetPath(getUuid()));
		textField_OutputPath.setText(AutoLab.config.getOutputPath());
		textFieldSSCCSequencePath.setText(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "sscc"+ File.separator+((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).getSsccSequenceFilename());
		textFieldPrinterName.setText(((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).getPrinterName());
		textFieldProductionLineName.setText(((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).getProdLineName());
		textFieldDataSetFilename.setText(((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).getProdLineName()+"_"+((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).getPrinterName()+".CSV");
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
