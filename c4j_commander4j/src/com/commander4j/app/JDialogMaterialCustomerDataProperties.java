package com.commander4j.app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialCustomerData;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDialogMaterialCustomerDataProperties edits a customer specific element of data. It inserts/updates a record in the table APP_MATERIAL_CUSTOMER_DATA
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogMaterialCustomerDataProperties.jpg" >
 *
 * @see com.commander4j.db.JDBMaterialCustomerData JDBMaterialCustomerData
 * @see com.commander4j.app.JInternalFrameMaterialCustomerDataAdmin JInternalFrameMaterialCustomerDataAdmin
 */
public class JDialogMaterialCustomerDataProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JButton4j btnClose;
	private JButton4j btnSave;
	private JDBCustomer cust = new JDBCustomer(Common.selectedHostID,Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID,Common.sessionID);
	private JDBMaterial mat = new JDBMaterial(Common.selectedHostID,Common.sessionID);
	private JDBMaterialCustomerData matcustdata = new JDBMaterialCustomerData(Common.selectedHostID,Common.sessionID);
	private JTextField4j textFieldCustomerDescription = new JTextField4j(JDBCustomer.field_customer_name);
	private JTextField4j textFieldCustomerID = new JTextField4j(JDBCustomer.field_customer_id);
	private JTextField4j textFieldData;
	private JTextField4j textFieldMaterialDescription = new JTextField4j(JDBMaterial.field_description);
	private JTextField4j textFieldMaterialID;
	private String lcustomer="";
	private String ldataid="";
	private String lmaterial="";

	public JDialogMaterialCustomerDataProperties(JFrame frame,String material,String customer,String dataid) {

		super(frame,"Customer Data Properties",ModalityType.APPLICATION_MODAL);

		lmaterial = material;
		lcustomer = customer;
		ldataid = dataid;

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Customer Data Properties");
		this.setResizable(false);
		this.setSize(855, 222);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Common.color_app_window);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, 855, 200);
		getContentPane().add(desktopPane);

		JLabel4j_std lblIMaterial = new JLabel4j_std(lang.get("lbl_Material"));
		lblIMaterial.setBounds(8, 27, 114, 22);
		desktopPane.add(lblIMaterial);
		lblIMaterial.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldMaterialID = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterialID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSave();
			}
		});
		textFieldMaterialID.setEnabled(false);
		textFieldMaterialID.setBounds(134, 27, 153, 22);
		desktopPane.add(textFieldMaterialID);
		textFieldMaterialID.setText(material);


		textFieldData = new JTextField4j(JDBMaterialCustomerData.field_data);
		textFieldData.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSave();
			}
		});
		textFieldData.setBounds(299, 106, 537, 22);
		desktopPane.add(textFieldData);

		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		btnSave.setIcon(Common.icon_update_16x16);
		btnSave.setBounds(296, 150, 117, 32);
		desktopPane.add(btnSave);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.setBounds(417, 150, 117, 32);
		desktopPane.add(btnClose);

		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Data_ID"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(8, 106, 114, 22);
		desktopPane.add(label4j_std_1);

		material = JUtility.replaceNullStringwithBlank(material);

		textFieldMaterialID.setText(material);

		textFieldCustomerID.setEnabled(false);
		textFieldCustomerID.setBounds(134, 67, 153, 22);
		textFieldCustomerID.setText(customer);
		desktopPane.add(textFieldCustomerID);

		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Customer_ID"));
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(8, 67, 114, 22);
		desktopPane.add(label4j_std);


		textFieldMaterialDescription.setEnabled(false);
		textFieldMaterialDescription.setBounds(299, 27, 537, 22);
		desktopPane.add(textFieldMaterialDescription);

		textFieldCustomerDescription.setEnabled(false);
		textFieldCustomerDescription.setBounds(299, 67, 537, 22);
		desktopPane.add(textFieldCustomerDescription);

		JTextField4j textFieldDataID = new JTextField4j(JDBMaterialCustomerData.field_data_id);
		textFieldDataID.setEnabled(false);
		textFieldDataID.setBounds(134, 106, 153, 22);
		textFieldDataID.setText(dataid);
		desktopPane.add(textFieldDataID);

		matcustdata.getMaterialCustomerDataProperties(lmaterial,lcustomer,ldataid);
		textFieldData.setText(matcustdata.getData());
		mat.getMaterialProperties(material);
		textFieldMaterialDescription.setText(mat.getDescription());
		cust.getCustomerProperties(customer);
		textFieldCustomerDescription.setText(cust.getName());

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldData.requestFocus();
				textFieldData.setCaretPosition(textFieldData.getText().length());

			}
		});

	}

	private void enableSave()
	{
		if (textFieldMaterialID.getText().equals("")==false)
		{
			if (textFieldData.getText().equals("")==false)
			{
				btnSave.setEnabled(true);
			}
		}
	}


	private void save()
	{
		matcustdata.setData(textFieldData.getText());

		if (matcustdata.isValidMaterialCustomerData(lmaterial,lcustomer,ldataid)==false)
		{
			matcustdata.create(lmaterial,lcustomer,ldataid);
		}
		matcustdata.update();
		btnSave.setEnabled(false);
	}
}
