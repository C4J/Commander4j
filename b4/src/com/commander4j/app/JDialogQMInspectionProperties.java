package com.commander4j.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDialogQMInspectionProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldInspectionID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID,Common.sessionID);
	private JDBQMInspection inspect = new JDBQMInspection(Common.selectedHostID,Common.sessionID);
	private JTextField4j textFieldInspectionDescription;
	private JButton4j btnSave;
	private JButton4j btnClose;
	
	private void enableSave()
	{
		if (textFieldInspectionID.getText().equals("")==false)
		{
			if (textFieldInspectionDescription.getText().equals("")==false)
			{
				btnSave.setEnabled(true);
			}
		}
	}
	
	private void save()
	{
		String insp = textFieldInspectionID.getText();
		String description = textFieldInspectionDescription.getText();
		
		if (inspect.isValid(insp)==false)
		{
			inspect.create(insp,description);
		}
		else
		{
			inspect.setDescription(textFieldInspectionDescription.getText());
			inspect.update();				
		}
	}

	
	public JDialogQMInspectionProperties(JFrame frame,String inspectionid) {
		
		super(frame,"Inspection Properties",ModalityType.APPLICATION_MODAL);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Inspection Properties");
		this.setResizable(false);		
		this.setSize(855, 141);

		
		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_edit_properties);
		desktopPane.setBounds(0, 0, 855, 118);
		getContentPane().add(desktopPane);
		
		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setBounds(8, 27, 87, 16);
		desktopPane.add(lblInspectionID);
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSave();
			}
		});
		textFieldInspectionID.setEnabled(false);
		textFieldInspectionID.setBounds(108, 22, 153, 28);
		desktopPane.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);
		
		
		textFieldInspectionDescription = new JTextField4j(JDBQMInspection.field_description);
		textFieldInspectionDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSave();
			}
		});
		textFieldInspectionDescription.setBounds(377, 22, 463, 28);
		desktopPane.add(textFieldInspectionDescription);
		textFieldInspectionDescription.setColumns(10);
		

		
		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		btnSave.setIcon(Common.icon_update);
		btnSave.setBounds(293, 75, 117, 29);
		desktopPane.add(btnSave);
		
		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close);
		btnClose.setBounds(414, 75, 117, 29);
		desktopPane.add(btnClose);
		
		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(273, 27, 92, 16);
		desktopPane.add(label4j_std_1);
		
		inspectionid = JUtility.replaceNullStringwithBlank(inspectionid);
		
		textFieldInspectionID.setText(inspectionid);
		inspect.getProperties(inspectionid);
		textFieldInspectionDescription.setText(inspect.getDescription());
	

		if (inspectionid.equals(""))
		{
			textFieldInspectionID.setEnabled(true);
		}
		else
		{
			textFieldInspectionID.setEnabled(false);
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textFieldInspectionDescription.requestFocus();
				textFieldInspectionDescription.setCaretPosition(textFieldInspectionDescription.getText().length());
			}
		});

	}
}
