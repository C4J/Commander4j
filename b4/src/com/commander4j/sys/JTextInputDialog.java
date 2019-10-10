package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
		displayCharacterCount.setText(msg);
	}

	public JTextInputDialog(JFrame frame, String textValue, int maxCharacters)
	{
		super(frame);
		isTextEntered = false;
		this.maxCharacters = maxCharacters;
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Text Input");
		setResizable(false);
		
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		setSize(725, 244);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 713, 171);
		contentPanel.add(scrollPane);

		textArea = new JTextArea(textValue);
		textArea.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				displayCharCount();
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				displayCharCount();
			}
		});

		scrollPane.setViewportView(textArea);


		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(-6, 176, 725, 46);
		contentPanel.add(buttonPane);
		{
			okButton = new JButton4j(Common.icon_ok_16x16);
			okButton.setText(lang.get("btn_Ok"));
			okButton.setBounds(485, 5, 116, 29);
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
				displayCharacterCount.setBounds(21, 10, 126, 16);
				buttonPane.add(displayCharacterCount);
			}
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			cancelButton = new JButton4j(Common.icon_cancel_16x16);
			cancelButton.setText(lang.get("btn_Close"));
			cancelButton.setBounds(601, 5, 116, 29);
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
