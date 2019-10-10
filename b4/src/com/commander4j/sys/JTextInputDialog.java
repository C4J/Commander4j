package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.util.JUtility;

public class JTextInputDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static boolean isTextEntered = false;
	private static JTextArea textArea;
	private int maxCharacters = 0;
	private JLabel4j_std displayCharacterCount = new JLabel4j_std();
	private JButton4j okButton;
	private JButton4j cancelButton;
	private JDBLanguage lang;


	public boolean isTextEntered()
	{
		return isTextEntered;
	}

	public String getTextEntered()
	{

		return JUtility.replaceNullStringwithBlank(textArea.getText().toString());
	}

	/**
	 * Create the dialog.
	 */
	private void displayCharCount()
	{
		String msg = String.valueOf(getTextEntered().length()) + " of " + String.valueOf(maxCharacters);
		if (getTextEntered().length() > maxCharacters)
		{
			displayCharacterCount.setForeground(Color.RED);
			okButton.setEnabled(false);
		}
		else
		{
			displayCharacterCount.setForeground(Color.BLACK);
			okButton.setEnabled(true);
		}
		displayCharacterCount.setFont(new Font("Arial", Font.BOLD, 11));
		displayCharacterCount.setText(msg);
	}

	public JTextInputDialog(JFrame frame, String textValue, int maxCharacters)
	{
		super(frame);
		isTextEntered = false;
		this.maxCharacters = maxCharacters;
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
		setTitle("Text Input");

		setSize(569, 213);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 557, 136);
		contentPanel.add(scrollPane);

		textArea = new JTextArea(textValue);
		textArea.setFont(new Font("Arial", Font.PLAIN, 13));
		textArea.setLineWrap(true);
		textArea.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				displayCharCount();
			}
		});

		scrollPane.setViewportView(textArea);


		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 144, 725, 46);
		contentPanel.add(buttonPane);
		{
			okButton = new JButton4j(Common.icon_ok_16x16);
			okButton.setText(lang.get("btn_Ok"));
			okButton.setBounds(321, 5, 116, 29);
			okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					isTextEntered = true;
					dispose();
				}
			});
			buttonPane.setLayout(null);
			{
				displayCharacterCount.setBounds(16, 5, 126, 29);
				buttonPane.add(displayCharacterCount);
			}
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			cancelButton = new JButton4j(Common.icon_cancel_16x16);
			cancelButton.setText(lang.get("btn_Close"));
			cancelButton.setBounds(437, 5, 116, 29);
			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					isTextEntered = false;
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		
		textArea.setCaretPosition(textArea.getDocument().getLength());
		displayCharCount();

	}

}
