package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogQMSelectListProperties.java
 * 
 * Package Name : com.commander4j.app
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * JDialogQMSelectListProperties is used to administer a single record within the APP_QM_SELECTLIST table. 
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogQMSelectListProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBQMSelectList JDBQMSelectList
 * @see com.commander4j.app.JInternalFrameQMSelectListAdmin
 *      JInternalFrameQMSelectListAdmin
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary 
 */
public class JDialogQMSelectListProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldListID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID,Common.sessionID);
	private JDBQMSelectList select = new JDBQMSelectList(Common.selectedHostID,Common.sessionID);
	private JTextField4j textFieldDescription;
	private JTextField4j textFieldValue;
	private JButton4j btnSave;
	private JButton4j btnClose;
	private JSpinner spinnerSequence;
	
	private void enableSave()
	{
		if (textFieldValue.getText().equals("")==false)
		{
			if (textFieldDescription.getText().equals("")==false)
			{
				btnSave.setEnabled(true);
			}
		}
	}
	
	private void save()
	{
		String id = textFieldListID.getText();
		String val = textFieldValue.getText();
		String description = textFieldDescription.getText();
		
		if (select.isValid(id, val)==false)
		{
			select.create(id, val,description,Long.valueOf(spinnerSequence.getValue().toString()));
			textFieldListID.setEnabled(false);
			textFieldValue.setEnabled(false);
		}
		else
		{
			select.setSequence(Long.valueOf(spinnerSequence.getValue().toString()));
			select.setDescription(textFieldDescription.getText());
			select.update();				
		}
	}
	
	public JDialogQMSelectListProperties(JFrame frame,String listid,String val) {
		
		super(frame,"Select List Properties",ModalityType.APPLICATION_MODAL);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Select List Properties");
		this.setResizable(false);		
		this.setSize(415, 208);

		
		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_edit_properties);
		desktopPane.setBounds(0, 0, 414, 185);
		getContentPane().add(desktopPane);
		
		JLabel4j_std lblListID = new JLabel4j_std(lang.get("lbl_List_ID"));
		lblListID.setBounds(8, 27, 87, 16);
		desktopPane.add(lblListID);
		lblListID.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel4j_std lblValue = new JLabel4j_std(lang.get("lbl_Value"));
		lblValue.setBounds(220, 27, 89, 16);
		desktopPane.add(lblValue);
		lblValue.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textFieldListID = new JTextField4j(JDBQMSelectList.field_list_id);
		textFieldListID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				enableSave();
			}
		});
		textFieldListID.setEnabled(false);
		textFieldListID.setBounds(103, 22, 117, 28);
		desktopPane.add(textFieldListID);
		textFieldListID.setColumns(10);
			
		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		btnSave.setIcon(Common.icon_update);
		btnSave.setBounds(118, 143, 117, 29);
		desktopPane.add(btnSave);
		
		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close);
		btnClose.setBounds(240, 143, 117, 29);
		desktopPane.add(btnClose);
		
		textFieldValue = new JTextField4j(JDBQMSelectList.field_value_id);
		textFieldValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableSave();
			}
		});
		textFieldValue.setEnabled(false);
		textFieldValue.setText("<dynamic>");
		textFieldValue.setColumns(10);
		textFieldValue.setBounds(329, 22, 60, 28);
		desktopPane.add(textFieldValue);
		
		JLabel4j_std lblDescription = new JLabel4j_std(lang.get("lbl_Description"));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(6, 64, 89, 16);
		desktopPane.add(lblDescription);
		
		textFieldDescription = new JTextField4j(JDBQMSelectList.field_description);
		textFieldDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				enableSave();
			}
		});
		textFieldDescription.setColumns(10);
		textFieldDescription.setBounds(103, 57, 286, 28);
		desktopPane.add(textFieldDescription);
		
		JLabel4j_std lblSeq = new JLabel4j_std(lang.get("lbl_Sequence_ID"));
		lblSeq.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSeq.setBounds(8, 99, 87, 16);
		desktopPane.add(lblSeq);		

		spinnerSequence = new JSpinner();
		spinnerSequence.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				enableSave();
			}
		});
		spinnerSequence.setBounds(103, 92, 60, 27);
		JSpinner.NumberEditor ne = new JSpinner.NumberEditor(spinnerSequence);
		ne.getTextField().setFont(Common.font_std); 
		spinnerSequence.setEditor(ne);
		spinnerSequence.setValue(100);
		desktopPane.add(spinnerSequence);
		
		listid = JUtility.replaceNullStringwithBlank(listid);
		val = JUtility.replaceNullStringwithBlank(val);
		
		textFieldListID.setText(listid);
		textFieldValue.setText(val);

		select.getProperties(listid,val);

		textFieldDescription.setText(select.getDescription());
		spinnerSequence.setValue(select.getSequence());

		if (listid.equals(""))
		{
			textFieldListID.setEnabled(true);
		}
		else
		{
			textFieldListID.setEnabled(false);
		}
		
		if (val.equals(""))
		{
			
			textFieldValue.setEnabled(true);
		}
		else
		{
			textFieldValue.setEnabled(false);
		}
	}
}
