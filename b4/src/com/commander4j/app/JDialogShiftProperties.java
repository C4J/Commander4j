package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogShiftProperties.java
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBShifts;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

/**
 * JDialogShiftProperties allows you to define shifts. These shifts are then
 * used within the User Definable Reports module to select start and end times
 * quickly. The data is stored in a table called APP_SHIFTS
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogShiftProperties.jpg" >
 * 
 * @see com.commander4j.db.JDBShifts JDBShifts
 * @see com.commander4j.app.JInternalFrameUserReportAdmin JInternalFrameUserReportAdmin
 * @see com.commander4j.app.JInternalFrameUserReportProperties JInternalFrameUserReportProperties
 */
public class JDialogShiftProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JTextField4j textFieldShiftID;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBShifts shifts = new JDBShifts(Common.selectedHostID, Common.sessionID);
	private JTextField4j textFieldDescription;
	private Format timeFormat = new SimpleDateFormat("HH:mm:ss");
	private JFormattedTextField textField4jStartTime = new JFormattedTextField(timeFormat);
	private JFormattedTextField textField4jEndTime = new JFormattedTextField(timeFormat);
	private JButton4j btnSave;
	private JButton4j btnClose;
	private String shiftid;
	private JLabel4j_std statusBar = new JLabel4j_std();

	private void enableSave()
	{
		if (textFieldShiftID.getText().equals("") == false)
		{
			if (textFieldDescription.getText().equals("") == false)
			{
				if (textField4jStartTime.getText().equals("") == false)
				{
					if (textField4jStartTime.getText().equals("") == false)
					{

						btnSave.setEnabled(true);

					}
				}
			}
		}
	}

	private void save()
	{

		boolean startDateValid = true;
		boolean endDateValid = true;
		@SuppressWarnings("unused")
		Date testDate;

		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		try
		{
			testDate = (Date) formatter.parse(textField4jStartTime.getText());
		} catch (ParseException e)
		{
			startDateValid = false;
		}

		try
		{
			testDate = (Date) formatter.parse(textField4jEndTime.getText());
		} catch (ParseException e)
		{
			endDateValid = false;
		}

		if (startDateValid)
		{
			if (endDateValid)
			{
				if (shifts.isValid(shiftid) == false)
				{
					shifts.create(shiftid, textField4jStartTime.getText(), textField4jEndTime.getText(), textFieldDescription.getText());
				} else
				{
					statusBar.setText("");
					shifts.setShiftID(textFieldShiftID.getText());
					shifts.setStartTime(textField4jStartTime.getText());
					shifts.setEndTime(textField4jEndTime.getText());
					shifts.setDescription(textFieldDescription.getText());
					shifts.update();
				}
			} else
			{
				statusBar.setText("Invalid End Time");
			}
		} else
		{
			statusBar.setText("Invalid Start Time");
		}
	}

	public JDialogShiftProperties(JFrame frame, String shift)
	{

		super(frame, "Shift Properties", ModalityType.APPLICATION_MODAL);

		shiftid = shift;

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Shift Properties");
		this.setResizable(false);
		this.setSize(455, 196);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_edit_properties);
		desktopPane.setBounds(0, 0, 468, 184);
		getContentPane().add(desktopPane);

		JLabel4j_std lblShiftID = new JLabel4j_std(lang.get("lbl_Shift_ID"));
		lblShiftID.setBounds(6, 6, 126, 28);
		desktopPane.add(lblShiftID);
		lblShiftID.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldShiftID = new JTextField4j(JDBShifts.field_shift_id);
		textFieldShiftID.setEnabled(false);
		textFieldShiftID.setBounds(145, 6, 110, 28);
		desktopPane.add(textFieldShiftID);
		textFieldShiftID.setColumns(10);

		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				save();
			}
		});
		btnSave.setIcon(Common.icon_update);
		btnSave.setBounds(116, 106, 117, 29);
		desktopPane.add(btnSave);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close);
		btnClose.setBounds(247, 106, 117, 29);
		desktopPane.add(btnClose);

		JLabel4j_std label4j_std_Description = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_Description.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_Description.setBounds(6, 66, 126, 28);
		desktopPane.add(label4j_std_Description);

		textFieldDescription = new JTextField4j(JDBShifts.field_description);
		textFieldDescription.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				enableSave();
			}
		});
		textFieldDescription.setColumns(10);
		textFieldDescription.setBounds(145, 66, 286, 28);
		desktopPane.add(textFieldDescription);

		shiftid = JUtility.replaceNullStringwithBlank(shiftid);

		textFieldShiftID.setText(shiftid);
		shifts.getProperties(shiftid);

		textFieldDescription.setText(shifts.getDescription());
		textField4jStartTime.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				enableSave();
			}
		});

		((DateFormat) timeFormat).setLenient(false);

		try
		{
			MaskFormatter dateMask1 = new MaskFormatter("##:##:##");
			dateMask1.install(textField4jStartTime);
			MaskFormatter dateMask2 = new MaskFormatter("##:##:##");
			dateMask2.install(textField4jStartTime);
		} catch (ParseException ex)
		{

		}

		textField4jStartTime.setColumns(10);
		textField4jStartTime.setBounds(145, 36, 85, 28);
		textField4jStartTime.setFont(Common.font_std);

		textField4jStartTime.setText(shifts.getStartTime());

		desktopPane.add(textField4jStartTime);

		textField4jEndTime.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				enableSave();
			}
		});
		textField4jEndTime.setText(shifts.getEndTime());
		textField4jEndTime.setColumns(10);
		textField4jEndTime.setBounds(344, 36, 85, 28);
		textField4jEndTime.setFont(Common.font_std);
		desktopPane.add(textField4jEndTime);

		JLabel4j_std label4j_std = new JLabel4j_std(lang.get("lbl_Start_Time"));
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(6, 36, 126, 28);
		desktopPane.add(label4j_std);

		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_End_Time"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(232, 36, 99, 28);
		desktopPane.add(label4j_std_1);

		statusBar.setText("");
		statusBar.setForeground(Color.RED);
		statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusBar.setBounds(0, 147, 451, 21);
		desktopPane.add(statusBar);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				textField4jStartTime.requestFocus();
				textField4jStartTime.setCaretPosition(textField4jStartTime.getText().length());

			}
		});

	}
}
