package com.commander4j.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.commander4j.Interface.Mapping.Maps;
import com.commander4j.mw.Common;
import com.commander4j.mw.StartMW;
import com.commander4j.util.Utility;

public class StartGUI extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private StartMW smw = new StartMW();
	private JToggleButton tglbtnStopStart;
	private JPanel panelStatus = new JPanel();
	private JLabel lblStatus = new JLabel("Ready");
	private JPanel progressBarInterface = new JPanel();
	private final JLabel lblInterfaceStatus = new JLabel("Interface Status :");
	private JLabel label_NoOfMaps = new JLabel("");
	private JList<Maps> listMaps = new JList<Maps>();
	/**
	 * Launch the application.
	 */
		
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					StartGUI frame = new StartGUI();
					frame.setVisible(true);
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartGUI()
	{
		setTitle("Commander4j Middleware");
		Utility.initLogging("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 467);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		
		JButton btnClose = new JButton(Common.icon_close);
		btnClose.setText("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(306, 352, 150, 38);
		contentPane.add(btnClose);
		
		tglbtnStopStart = new JToggleButton(Common.icon_ok);
		tglbtnStopStart.setMnemonic(KeyEvent.VK_ENTER);
		tglbtnStopStart.setText("Start");
		tglbtnStopStart.setSelectedIcon(Common.icon_cancel);
		tglbtnStopStart.setOpaque(true);
		tglbtnStopStart.repaint();
		tglbtnStopStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnStopStart.isSelected())
				{
					
					smw.StartMiddleware();
					tglbtnStopStart.setBackground(Color.RED);
					tglbtnStopStart.setText("Stop");
					lblStatus.setText("Running");
					label_NoOfMaps.setText(String.valueOf(smw.cfg.getMaps().size()));

					btnClose.setEnabled(false);
					progressBarInterface.setBackground(new Color(0,128,0));
				}
				else
				{
					smw.StopMiddleware();
					
					tglbtnStopStart.setBackground(Color.GREEN);
					tglbtnStopStart.setText("Start");
					lblStatus.setText("Stopped");
					btnClose.setEnabled(true);
					progressBarInterface.setBackground(Color.RED);
				}
			}
		});
		tglbtnStopStart.setBounds(120, 352, 150, 38);
		contentPane.add(tglbtnStopStart);
		

		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelStatus.setBounds(0, 415, 573, 30);
		contentPane.add(panelStatus);
		panelStatus.setLayout(null);
		
		lblStatus.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblStatus.setBounds(12, 0, 573, 25);
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		panelStatus.add(lblStatus);
		
		progressBarInterface.setForeground(new Color(0, 128, 0));
		progressBarInterface.setBounds(312, 26, 15, 15);
		progressBarInterface.setPreferredSize(new Dimension(40,40));
		progressBarInterface.setBackground(Color.RED);
		contentPane.add(progressBarInterface);
		lblInterfaceStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		lblInterfaceStatus.setBounds(173, 23, 131, 22);
		
		contentPane.add(lblInterfaceStatus);
		
		JLabel lblNumberOfMaps = new JLabel("Number of Maps :");
		lblNumberOfMaps.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNumberOfMaps.setBounds(173, 57, 131, 22);
		contentPane.add(lblNumberOfMaps);
		
		label_NoOfMaps.setBounds(311, 61, 60, 15);
		contentPane.add(label_NoOfMaps);
		
		JScrollPane scrollPaneMaps = new JScrollPane();
		scrollPaneMaps.setBounds(100, 108, 392, 224);
		contentPane.add(scrollPaneMaps);
		
		scrollPaneMaps.setViewportView(listMaps);
	}
}
