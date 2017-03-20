package com.commander4j.calendar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JCalendarPanel.java
 * 
 * Package Name : com.commander4j.calendar
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
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.sys.Common;
import com.commander4j.util.JDateControl;

public class JCalendarPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JLabel year;
	private Calendar selectedDate;

	private JComboBox<String> comboBoxMonth = new JComboBox<String>();
	private LinkedList<JLabel> buttonsList = new LinkedList<JLabel>();
	private Font standard_font = new Font("Arial", Font.PLAIN, 10);
	private Font font_bold = new Font("Arial", Font.BOLD, 10);
	private Border empty = BorderFactory.createEmptyBorder();
	private Border line = BorderFactory.createLineBorder(Color.BLACK);
	
	private JSpinner spinnerHH;
	private JSpinner spinnerMM;
	private JSpinner spinnerSS;

	protected Color background;
	protected Color foreground;
	protected Color selectedBackground;
	protected Color selectedForeground;
	private JLabel lblHr;
	private JLabel lblMm;
	private JLabel lblSs;
	private JComboBox<String> comboBox = new JComboBox<String>();
	
	public Calendar getCalendarDate()
	{
		return selectedDate;
	}
	
	public Date getDate()
	{
		return (Date) selectedDate.getTime();
	}
	
	public JCalendarPanel(Calendar cal)
	{

		init(cal);
	}
	
	
	public JCalendarPanel(JDateControl datetimecontrol)
	{

		Calendar temp = Calendar.getInstance();
		temp.setTime(datetimecontrol.getDate());
		init(temp);
	}
	
	public void init(Calendar caldate) {

		setSize(270, 233);
		setLocation(0,0);
	    
		background = UIManager.getColor("ComboBox.background");
	    foreground = UIManager.getColor("ComboBox.foreground");
	    selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
	    selectedForeground = UIManager.getColor("ComboBox.selectionForeground");
	    
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getSize().width) / 2 - 100, (screenSize.height - getSize().height) / 2 - 1);
		initGUI();
		selectedDate = caldate;
		updateCalendar(selectedDate);
	}

	private String getMonthFromInt(int iMonth) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (iMonth >= 0 && iMonth <= 11)
			month = months[iMonth];
		return month;
	}

	private void updateCalendar(Calendar caldate) {
		year.setText(String.valueOf(caldate.get(Calendar.YEAR)));

		int days_in_month = caldate.getActualMaximum(Calendar.DATE);
		int month_in_year = caldate.get(Calendar.MONTH);
		int current_day_of_month = caldate.get(Calendar.DAY_OF_MONTH);
		int hour_of_day = caldate.get(Calendar.HOUR_OF_DAY);
		int minute_of_day = caldate.get(Calendar.MINUTE);
		int second_of_day = caldate.get(Calendar.SECOND);

		spinnerHH.setValue(hour_of_day);
		spinnerMM.setValue(minute_of_day);
		spinnerSS.setValue(second_of_day);
		comboBoxMonth.setForeground(Color.BLUE);
		comboBoxMonth.setFont(standard_font);
		comboBoxMonth.setSelectedIndex(month_in_year);

		int day = 0;

		Calendar temp = (Calendar) caldate.clone();
		temp.set(Calendar.DAY_OF_MONTH, 1);
		int first_day_of_week = temp.get(Calendar.DAY_OF_WEEK);
		int first_week_of_month = 0;
		temp.set(Calendar.DAY_OF_MONTH, days_in_month);

		int firstIndex = ((first_week_of_month * 7) + first_day_of_week) - 1;
		int lastIndex = firstIndex + days_in_month;

		day = 1;
		for (int d = 0; d < 42; d++) {
			if ((d >= firstIndex) & (d < lastIndex)) {
				((JLabel) buttonsList.get(d)).setVisible(true);
				((JLabel) buttonsList.get(d)).setText(String.valueOf(day));
				if (day == current_day_of_month) {
					((JLabel) buttonsList.get(d)).setForeground(Color.BLACK);
					((JLabel) buttonsList.get(d)).setBackground(Color.ORANGE);
					((JLabel) buttonsList.get(d)).setFont(font_bold);
					
				}
				else {
					((JLabel) buttonsList.get(d)).setForeground(Color.BLACK);
					((JLabel) buttonsList.get(d)).setBackground(Color.LIGHT_GRAY);
					((JLabel) buttonsList.get(d)).setFont(standard_font);
				}
				day++;
			}
			else {
				((JLabel) buttonsList.get(d)).setBackground(Color.BLACK);
				((JLabel) buttonsList.get(d)).setVisible(false);
				((JLabel) buttonsList.get(d)).setText("");
			}
		}
	}
	
	private void displayDayNames()
	{
		int offsetX = 5;
		int offsetY = 35;
		int width = 36;
		int height = 22;
		int gap = 0;
		int currentX = offsetX;
		String name="";
		
		DateFormatSymbols symbols = new DateFormatSymbols();
		String[] dayNames = symbols.getShortWeekdays(); 
		
		for (int dn=1;dn<=7;dn++)
		{
			JTextField dayLabel = new JTextField();
			dayLabel.setEditable(false);
			dayLabel.setFont(standard_font);
			dayLabel.setForeground(new Color(51, 51, 51));
			dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
			dayLabel.setBackground(SystemColor.activeCaption);
			name = (String) dayNames[dn];
			if (name.length()>3)
			{
				name = ((String) name.subSequence(0,3));
			}
				
			dayLabel.setText(name);
			dayLabel.setBounds(currentX, offsetY, width, height);
			currentX = currentX + (width + gap);
			add(dayLabel);
		}
	}
	
	private void displayButtons()
	{
		int offsetX = 5;
		int offsetY = 58;
		int width = 35;
		int height = 20;
		int gap = 1;
		int currentX = 0;
		int currentY = 0;
		int week = 1;
		int day = 1;

		for (int l = 0; l < 42; l++) {

			if (day == 8) {
				day = 1;
				week++;
			}

			currentX = ((day - 1) * (width + gap) + offsetX);
			currentY = ((week - 1) * (height + gap) + offsetY);
			
		    final JLabel label = new JLabel();
		    final Border selectedBorder = new EtchedBorder();
		    final Border unselectedBorder = new EmptyBorder(selectedBorder.getBorderInsets(new JLabel()));
		    label.setBorder(unselectedBorder);
		    label.setFont(standard_font);
		    label.setForeground(foreground);
		    label.setOpaque(true);
		    label.setHorizontalAlignment(SwingConstants.CENTER);
		    label.setBounds(currentX, currentY, width, height);
		    label.addMouseListener(new MouseAdapter() {
			    public void mouseReleased(MouseEvent e) {
			    	selectedDate.set(Calendar.DAY_OF_MONTH, Integer.valueOf(((JLabel) e.getSource()).getText()));
			    	updateCalendar(selectedDate);
			    }
			    public void mouseEntered(MouseEvent e) {
					   label.setBorder(line);
				    }
				    public void mouseExited(MouseEvent e) {
					   label.setBorder(empty);
				    }
			});
		    add(label);
		    buttonsList.addLast(label);
			day++;
		}	
	}
	
	private void displayMonths()
	{
		//comboBoxMonth.setBackground(UIManager.getColor("CheckBox.background"));
		comboBoxMonth.setFont(font_bold);
		comboBoxMonth.setFocusable(false);
		comboBoxMonth.setMaximumRowCount(12);
		comboBoxMonth.setModel(new DefaultComboBoxModel<String>(new String[] { getMonthFromInt(0), getMonthFromInt(1), getMonthFromInt(2), getMonthFromInt(3), getMonthFromInt(4), getMonthFromInt(5), getMonthFromInt(6), getMonthFromInt(7),
				getMonthFromInt(8), getMonthFromInt(9), getMonthFromInt(10), getMonthFromInt(11) }));
		comboBoxMonth.setBounds(5, 5, 109, 25);
		comboBoxMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int month_in_year = comboBoxMonth.getSelectedIndex();
				selectedDate = setMonth(selectedDate,month_in_year);
				updateCalendar(selectedDate);
			}
		});
		add(comboBoxMonth);	
	}
	
	private Calendar setMonth(Calendar temp,int month_in_year)
	{
		int current_day_of_month = temp.get(Calendar.DAY_OF_MONTH);
		temp.set(Calendar.DATE, 1);

		temp.set(Calendar.MONTH, month_in_year);
		int days_in_month = temp.getActualMaximum(Calendar.DATE);
		if (current_day_of_month > days_in_month)
		{
			current_day_of_month = days_in_month;
		}
		temp.set(Calendar.DATE, current_day_of_month);
		return temp;

	}
	
	private void displayYears()
	{
		year = new JLabel();
		year.setForeground(Color.BLACK);
		year.setFont(standard_font);
		year.setText("2010");
		year.setHorizontalAlignment(SwingConstants.CENTER);
		year.setBorder(empty);
		year.setBackground(UIManager.getColor("Panel.background"));
		year.setBounds(199, 5, 34, 23);
		add(year);

		JButton button = new JButton(Common.icon_arrow_right);
		button.setBorder(empty);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDate.add(Calendar.YEAR, 1);
				updateCalendar(selectedDate);
			}
		});
		button.setBounds(232, 7, 18, 18);
		add(button);

		JButton button_1 = new JButton(Common.icon_arrow_left);
		button_1.setBorder(empty);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedDate.add(Calendar.YEAR, -1);
				updateCalendar(selectedDate);
			}
		});
		button_1.setBounds(181, 7, 18, 18);
		add(button_1);
		
		
		
		spinnerHH = new JSpinner();
		
		JSpinner.NumberEditor neHH = new JSpinner.NumberEditor(spinnerHH);
		neHH.getTextField().setFont(Common.font_std);
		spinnerHH.setEditor(neHH);
		
		spinnerHH.setForeground(Color.BLUE);
		spinnerHH.getEditor().setBackground(UIManager.getColor("Panel.background"));
		spinnerHH.setBorder(new LineBorder(UIManager.getColor("Button.darkShadow")));
		spinnerHH.setModel(new SpinnerNumberModel(23, 0, 23, 1));
		spinnerHH.setFont(standard_font);
		spinnerHH.setBounds(5, 190, 50, 18);
        JTextField tf1 = ((JSpinner.DefaultEditor)spinnerHH.getEditor()).getTextField();  
        tf1.setBackground(Color.WHITE);
        tf1.setForeground(Color.BLACK);
		spinnerHH.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				selectedDate.set(Calendar.HOUR_OF_DAY,Integer.valueOf(spinnerHH.getValue().toString()));
				updateCalendar(selectedDate);
			}
		});
		add(spinnerHH);
		
		spinnerMM= new JSpinner();
		
		JSpinner.NumberEditor neMM = new JSpinner.NumberEditor(spinnerMM);
		neMM.getTextField().setFont(Common.font_std);
		spinnerMM.setEditor(neMM);
		
		spinnerMM.setForeground(Color.BLUE);
		spinnerMM.getEditor().setBackground(UIManager.getColor("Panel.background"));
		spinnerMM.setBorder(new LineBorder(UIManager.getColor("Button.darkShadow")));
		spinnerMM.setModel(new SpinnerNumberModel(59, 0, 59, 1));
		spinnerMM.setFont(standard_font);
		spinnerMM.setBounds(55, 190, 49, 18);
        JTextField tf2 = ((JSpinner.DefaultEditor)spinnerMM.getEditor()).getTextField();  
        tf2.setBackground(Color.WHITE);
        tf2.setForeground(Color.BLACK);
		spinnerMM.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				selectedDate.set(Calendar.MINUTE,Integer.valueOf(spinnerMM.getValue().toString()));
				updateCalendar(selectedDate);
			}
		});
		add(spinnerMM);
		
		spinnerSS= new JSpinner();
		
		JSpinner.NumberEditor neSS = new JSpinner.NumberEditor(spinnerSS);
		neSS.getTextField().setFont(Common.font_std);
		spinnerSS.setEditor(neSS);
		
		spinnerSS.getEditor().setBackground(UIManager.getColor("Panel.background"));
		spinnerSS.setBorder(new LineBorder(UIManager.getColor("Button.darkShadow")));
		spinnerSS.setModel(new SpinnerNumberModel(59, 0, 59, 1));
		spinnerSS.setFont(standard_font);
		spinnerSS.setBounds(102, 190, 49, 18);
        JTextField tf3 = ((JSpinner.DefaultEditor)spinnerSS.getEditor()).getTextField();  
        tf3.setBackground(Color.WHITE);
        tf3.setForeground(Color.BLACK);
		spinnerSS.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				selectedDate.set(Calendar.SECOND,Integer.valueOf(spinnerSS.getValue().toString()));
				updateCalendar(selectedDate);
			}
		});
		add(spinnerSS);
		

		
		lblHr = new JLabel("Hr");
		lblHr.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHr.setBounds(6, 172, 23, 16);
		lblHr.setFont(standard_font);
		add(lblHr);
		
		lblMm = new JLabel("Min");
		lblMm.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMm.setBounds(58, 172, 18, 16);
		lblMm.setFont(standard_font);
		add(lblMm);
		
		lblSs = new JLabel("Sec");
		lblSs.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSs.setBounds(109, 172, 23, 16);
		lblSs.setFont(standard_font);
		add(lblSs);
		

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerHH.setValue(Integer.valueOf(comboBox.getSelectedItem().toString().substring(0, 2)));
				spinnerMM.setValue(Integer.valueOf(comboBox.getSelectedItem().toString().substring(3, 5)));
				spinnerSS.setValue(Integer.valueOf(comboBox.getSelectedItem().toString().substring(6, 8)));
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"00:00:00", "05:59:59", "06:00:00", "11:59:59", "12:00:00", "17:59:59", "18:00:00", "23:59:59"}));
		comboBox.setFont(standard_font);
		comboBox.setBounds(158, 187, 103, 25);
		add(comboBox);
	}

	private void initGUI() {
		setLayout(null);

		displayDayNames();
		displayButtons();
		displayMonths();
		displayYears();

	}
}
