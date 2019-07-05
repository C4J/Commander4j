package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.WindowConstants;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import java.awt.Color;
import java.awt.Font;

public class JDialogDisplayLicense extends JDialog
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JTextPane textPane = new JTextPane();
	private JScrollPane scrollPane = new JScrollPane();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JLicenseInfo license = new JLicenseInfo();
			license.description = "Test";
			license.licenceFilename = "GNU General Public License V2.txt";
			JDialogDisplayLicense dialog = new JDialogDisplayLicense(null, license);
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

	public JDialogDisplayLicense(JDialogLicenses lll, JLicenseInfo license)
	{

		super(lll);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setTitle(license.getDescription()+ " ("+license.type+")");
		setBounds(100, 100, 767, 587);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);


		scrollPane.setBounds(6, 24, 755, 492);
		contentPanel.add(scrollPane);

		loadLicense(license.licenceFilename);
		textPane.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		textPane.setBackground(new Color(250, 250, 210));

		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);


		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(6, 520, 755, 39);
		contentPanel.add(buttonPane);
		buttonPane.setLayout(null);
		{
			JButton4j okButton = new JButton4j(Common.icon_close_16x16);
			okButton.setText(lang.get("btn_Close"));
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

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		setResizable(false);
		setModal(true);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}

	private boolean loadLicense(String filename)
	{
		boolean result = false;

		String getfilename = "." + File.separator + "lib" + File.separator + "license" + File.separator + filename;

		try
		{
            String content = new String ( Files.readAllBytes( Paths.get(getfilename) ) );
			textPane.setText(content);
			textPane.setCaretPosition(0);
			JViewport jv = scrollPane.getViewport();
			jv.setViewPosition(new Point(0,0));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}


}
