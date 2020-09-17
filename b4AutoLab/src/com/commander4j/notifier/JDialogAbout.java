package com.commander4j.notifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;

import javax.swing.SwingConstants;

public class JDialogAbout extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			JDialogAbout dialog = new JDialogAbout();
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
	public JDialogAbout()
	{
		setTitle("About");
		setResizable(false);
		setAlwaysOnTop(true);
		setSize(257, 236);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JButton okClose = new JButton("Close");
		okClose.setBounds(90, 178, 79, 29);
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

		JLabel labelLogo = new JLabel();
		labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogo.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "images" + File.separator + "image_about.png"));
		labelLogo.setBounds(4, 0, 247, 114);
		contentPanel.add(labelLogo);
		
		JLabel lblNewLabel = new JLabel("Version "+AutoLab.version);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(4, 115, 247, 16);
		contentPanel.add(lblNewLabel);
		
		JLabel lblSupportSupportcommanderjcom = new JLabel("support@commander4j.com");
		lblSupportSupportcommanderjcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblSupportSupportcommanderjcom.setBounds(4, 143, 247, 16);
		contentPanel.add(lblSupportSupportcommanderjcom);
	}
}
