package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.commander4j.gui.JList4j;

import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class JDialogLicences extends JDialog
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JList4j<JLicenceInfo> list = new JList4j<JLicenceInfo>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogLicences dialog = new JDialogLicences();
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
	public JDialogLicences()
	{

		setTitle("Licences");
		setBounds(100, 100, 528, 587);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 516, 502);
		contentPanel.add(scrollPane);
		

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(6, 520, 516, 39);
		contentPanel.add(buttonPane);
		buttonPane.setLayout(null);
		{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			okButton.setBounds(210, 6, 75, 29);
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		
		populateList();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		
		setResizable(false);
		setModal(true);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	}
	
	private void populateList()
	{

		DefaultComboBoxModel<JLicenceInfo> DefComboBoxMod = new DefaultComboBoxModel<JLicenceInfo>();

		JLicenceInfo tempCust = new JLicenceInfo();

		LinkedList<JLicenceInfo> licenceList = tempCust.getLicences();

		
		for (int j = 0; j < licenceList.size(); j++)
		{
			JLicenceInfo t = (JLicenceInfo) licenceList.get(j);
			DefComboBoxMod.addElement(t);
			
		}

		ListModel<JLicenceInfo> jList1Model = DefComboBoxMod;
		list.setModel(jList1Model);
		list.setSelectedIndex(0);
		list.setCellRenderer(Common.renderer_list);
		list.ensureIndexIsVisible(0);
	}
	
}
