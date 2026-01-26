package com.commander4j.sys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JScrollPane4j;
import com.commander4j.util.JUtility;

public class JTextInputDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel4j contentPanel = new JPanel4j();
	private static boolean isTextEntered = false;
	private static JTextArea textArea;
	private int maxCharacters = 0;
	private JLabel4j_std displayCharacterCount = new JLabel4j_std();
	private JButton4j okButton;
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
			

			if (getTextEntered().length() > 0)
			{
				okButton.setEnabled(true);
				displayCharacterCount.setForeground(Color.BLACK);
			}
			else
			{
				okButton.setEnabled(false);	
				displayCharacterCount.setForeground(Color.RED);
			}
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
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		setResizable(false);
		
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
		setTitle("Text Input");

		setSize(569, 213);
		
		Dimension screensize = Common.mainForm.getSize();
		Point parentPos = Common.mainForm.getLocation();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(parentPos.x + leftmargin , parentPos.y+ topmargin);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane4j scrollPane = new JScrollPane4j();
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


		JPanel4j buttonPane = new JPanel4j();
		buttonPane.setBounds(0, 144, 569, 46);
		contentPanel.add(buttonPane);
		{
			okButton = new JButton4j(Common.icon_ok_16x16);
			okButton.setText(lang.get("btn_Ok"));
			okButton.setBounds(226, 5, 116, 29);
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
		
		textArea.setCaretPosition(textArea.getDocument().getLength());
		displayCharCount();

	}

}
