package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.commander4j.db.JDBLanguage;

import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JList4j;
import com.commander4j.util.JUtility;

import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDialogLicenses extends JDialog
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	JList4j<JLicenseInfo> list = new JList4j<JLicenseInfo>();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	public JDialogLicenses(JFrame frame)
	{

		super(frame);
		JDialogLicenses me = this;
		setTitle("Libraries");
		setBounds(100, 100, 767, 587);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 24, 755, 492);
		contentPanel.add(scrollPane);
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{

					if (list.isSelectionEmpty() == false)
					{
						JLicenseInfo iii = ((JLicenseInfo) list.getSelectedValue());
						JDialogDisplayLicense dialog = new JDialogDisplayLicense(me, iii);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					
					}

				}
			}
		});

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(6, 520, 755, 39);
		contentPanel.add(buttonPane);
		buttonPane.setLayout(null);
		{
			JButton4j okButton = new JButton4j(Common.icon_ok_16x16);
			okButton.setText(lang.get("btn_Ok"));
			okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
			});
			okButton.setBounds(313, 6, 128, 32);
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}

		JLabel4j_title lblNewLabel = new JLabel4j_title(JUtility.padString("Library", true, JLicenseInfo.width_description, " ") + JUtility.padString("Version", true, JLicenseInfo.width_version, " ") + "Licence");
		lblNewLabel.setBounds(6, 5, 727, 16);
		lblNewLabel.setFont(Common.font_list);
		contentPanel.add(lblNewLabel);

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

		DefaultComboBoxModel<JLicenseInfo> DefComboBoxMod = new DefaultComboBoxModel<JLicenseInfo>();

		JLicenseInfo tempCust = new JLicenseInfo();

		LinkedList<JLicenseInfo> licenceList = tempCust.getLicences();

		for (int j = 0; j < licenceList.size(); j++)
		{
			JLicenseInfo t = (JLicenseInfo) licenceList.get(j);
			DefComboBoxMod.addElement(t);

		}

		ListModel<JLicenseInfo> jList1Model = DefComboBoxMod;
		list.setModel(jList1Model);
		list.setSelectedIndex(0);
		list.setCellRenderer(Common.renderer_list);
		list.ensureIndexIsVisible(0);
	}
}
