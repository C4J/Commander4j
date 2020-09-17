package com.commander4j.notifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;

public class JDialogOrderInfo extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField jTextFieldLocationID;
	private JTextField jTextFieldShelfLife;
	private JTextField jTextFieldQuantity;
	private JTextField jTextFieldDescription;
	private JTextField jTextFieldMaterial;
	private JTextField jTextFieldProductionUOM;
	private JTextField jTextFieldOrder;
	private JTextField jTextFieldCustomerID;
	private String uuid = "";
	private JTextField jTextFieldShelfLifeUOM;
	private JTextField jTextFieldShelfLifeRule;
	private JTextField jTextFieldEAN;
	private JTextField textFieldVARIANT;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogOrderInfo dialog = new JDialogOrderInfo("");
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
	public JDialogOrderInfo(String uuid)
	{

		setUuid(uuid);

		setTitle("Line [" + AutoLab.getProdLine_Name(uuid) + "] - Order [" + AutoLab.getDataSet_Field(getUuid(), "PROCESS_ORDER") + "]");

		setResizable(false);
		setAlwaysOnTop(true);
		setSize(585, 412);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton("Close");
		okClose.setBounds(253, 355, 79, 29);
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

		JLabel lblOrder = new JLabel("Order :");
		lblOrder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrder.setBounds(14, 3, 144, 26);
		contentPanel.add(lblOrder);

		JLabel lblSSCC = new JLabel("Unit of Measure :");
		lblSSCC.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSSCC.setBounds(14, 119, 144, 26);
		contentPanel.add(lblSSCC);

		JLabel lblMaterial = new JLabel("Material :");
		lblMaterial.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaterial.setBounds(14, 32, 144, 26);
		contentPanel.add(lblMaterial);

		JLabel lblLocationID = new JLabel("Location ID :");
		lblLocationID.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLocationID.setBounds(14, 235, 144, 26);
		contentPanel.add(lblLocationID);

		JLabel lblExpiryDate = new JLabel("Shelf Life :");
		lblExpiryDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblExpiryDate.setBounds(14, 148, 144, 26);
		contentPanel.add(lblExpiryDate);

		JLabel lblQuantity = new JLabel("Quantity :");
		lblQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuantity.setBounds(14, 90, 144, 26);
		contentPanel.add(lblQuantity);

		JLabel lblModbusIp = new JLabel("Description :");
		lblModbusIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModbusIp.setBounds(14, 61, 144, 26);
		contentPanel.add(lblModbusIp);

		jTextFieldLocationID = new JTextField();
		jTextFieldLocationID.setEditable(false);
		jTextFieldLocationID.setBounds(166, 235, 372, 26);
		contentPanel.add(jTextFieldLocationID);
		jTextFieldLocationID.setColumns(10);

		jTextFieldShelfLife = new JTextField();
		jTextFieldShelfLife.setEditable(false);
		jTextFieldShelfLife.setColumns(10);
		jTextFieldShelfLife.setBounds(166, 148, 372, 26);
		contentPanel.add(jTextFieldShelfLife);

		jTextFieldQuantity = new JTextField();
		jTextFieldQuantity.setEditable(false);
		jTextFieldQuantity.setColumns(10);
		jTextFieldQuantity.setBounds(166, 90, 372, 26);
		contentPanel.add(jTextFieldQuantity);

		jTextFieldDescription = new JTextField();
		jTextFieldDescription.setEditable(false);
		jTextFieldDescription.setColumns(10);
		jTextFieldDescription.setBounds(166, 61, 372, 26);
		contentPanel.add(jTextFieldDescription);

		jTextFieldMaterial = new JTextField();
		jTextFieldMaterial.setEditable(false);
		jTextFieldMaterial.setColumns(10);
		jTextFieldMaterial.setBounds(166, 32, 372, 26);
		contentPanel.add(jTextFieldMaterial);

		jTextFieldProductionUOM = new JTextField();
		jTextFieldProductionUOM.setEditable(false);
		jTextFieldProductionUOM.setColumns(10);
		jTextFieldProductionUOM.setBounds(166, 119, 372, 26);
		contentPanel.add(jTextFieldProductionUOM);

		jTextFieldOrder = new JTextField();
		jTextFieldOrder.setEditable(false);
		jTextFieldOrder.setColumns(10);
		jTextFieldOrder.setBounds(166, 3, 372, 26);
		contentPanel.add(jTextFieldOrder);

		jTextFieldCustomerID = new JTextField();
		jTextFieldCustomerID.setText((String) null);
		jTextFieldCustomerID.setEditable(false);
		jTextFieldCustomerID.setColumns(10);
		jTextFieldCustomerID.setBounds(166, 264, 372, 26);
		contentPanel.add(jTextFieldCustomerID);

		JLabel lblCustomerID = new JLabel("Customer ID :");
		lblCustomerID.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerID.setBounds(14, 264, 144, 26);
		contentPanel.add(lblCustomerID);

		JLabel lblShelfLifeUom = new JLabel("Shelf Life UOM :");
		lblShelfLifeUom.setHorizontalAlignment(SwingConstants.RIGHT);
		lblShelfLifeUom.setBounds(14, 177, 144, 26);
		contentPanel.add(lblShelfLifeUom);

		jTextFieldShelfLifeUOM = new JTextField();
		jTextFieldShelfLifeUOM.setText((String) null);
		jTextFieldShelfLifeUOM.setEditable(false);
		jTextFieldShelfLifeUOM.setColumns(10);
		jTextFieldShelfLifeUOM.setBounds(166, 177, 372, 26);
		contentPanel.add(jTextFieldShelfLifeUOM);

		JLabel lblShelfLifeRule = new JLabel("Shelf Life Rule :");
		lblShelfLifeRule.setHorizontalAlignment(SwingConstants.RIGHT);
		lblShelfLifeRule.setBounds(14, 206, 144, 26);
		contentPanel.add(lblShelfLifeRule);

		jTextFieldShelfLifeRule = new JTextField();
		jTextFieldShelfLifeRule.setText((String) null);
		jTextFieldShelfLifeRule.setEditable(false);
		jTextFieldShelfLifeRule.setColumns(10);
		jTextFieldShelfLifeRule.setBounds(166, 206, 372, 26);
		contentPanel.add(jTextFieldShelfLifeRule);

		jTextFieldEAN = new JTextField();
		jTextFieldEAN.setText((String) null);
		jTextFieldEAN.setEditable(false);
		jTextFieldEAN.setColumns(10);
		jTextFieldEAN.setBounds(166, 293, 372, 26);
		contentPanel.add(jTextFieldEAN);

		JLabel lblEAN = new JLabel("GTIN :");
		lblEAN.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEAN.setBounds(14, 293, 144, 26);
		contentPanel.add(lblEAN);
		
		textFieldVARIANT = new JTextField();
		textFieldVARIANT.setText((String) null);
		textFieldVARIANT.setEditable(false);
		textFieldVARIANT.setColumns(10);
		textFieldVARIANT.setBounds(166, 322, 372, 26);
		contentPanel.add(textFieldVARIANT);
		
		JLabel lblVariant = new JLabel("Variant :");
		lblVariant.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVariant.setBounds(14, 322, 144, 26);
		contentPanel.add(lblVariant);

		refresh();

	}

	private void refresh()
	{
		jTextFieldShelfLife.setText(AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE"));
		jTextFieldShelfLifeUOM.setText(AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_UOM"));
		jTextFieldShelfLifeRule.setText(AutoLab.getDataSet_Field(getUuid(), "SHELF_LIFE_RULE"));
		jTextFieldMaterial.setText(AutoLab.getDataSet_Field(getUuid(), "MATERIAL"));
		jTextFieldOrder.setText(AutoLab.getDataSet_Field(getUuid(), "PROCESS_ORDER"));
		jTextFieldLocationID.setText(AutoLab.getDataSet_Field(getUuid(), "LOCATION_ID"));
		jTextFieldDescription.setText(AutoLab.getDataSet_Field(getUuid(), "DESCRIPTION"));
		jTextFieldProductionUOM.setText(AutoLab.getDataSet_Field(getUuid(), "PROD_UOM"));
		jTextFieldQuantity.setText(AutoLab.getDataSet_Field(getUuid(), "QUANTITY"));
		jTextFieldCustomerID.setText(AutoLab.getDataSet_Field(getUuid(), "CUSTOMER_ID"));
		jTextFieldEAN.setText(AutoLab.getDataSet_Field(getUuid(), "EAN"));
		textFieldVARIANT.setText(AutoLab.getDataSet_Field(getUuid(), "PROD_VARIANT"));
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
