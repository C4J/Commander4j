package com.commander4j.notifier;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;
import com.commander4j.resources.JRes;
import com.commander4j.utils.JUtility;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JFrameNotifier extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea messageArea = new JTextArea();
	private LinkedList<String> allText = new LinkedList<String>();
	private String replacementText = "";
	private String delim="";
	private JUtility utils = new JUtility();
	private Calendar now;
	private String time24="";
	private String uuid = "";
	private ImageIcon img;
	private String titlebar="";

	public void setMessage(String message)
	{
		allText.clear();
		appendToMessage(message);
	}
	
	static class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e) {
			((JFrame) e.getSource()).setExtendedState(JFrame.ICONIFIED);
		}
	}

	public  synchronized void appendToMessage(String message)
	{
		
		now = Calendar.getInstance();
		time24 = utils.get24HourStringFromCalendar(now);
		
		if (allText.size()>50)
		{
			allText.remove(0);
		}


		allText.add(time24+" "+message);

		replacementText="";
		for (int x=0;x<allText.size();x++)
		{
			if (x==0)
			{
				delim = "";
			}
			else
			{
				delim="\n";
			}
			replacementText=replacementText+delim+allText.get(x);
		}
		
		messageArea.setText(replacementText);

		messageArea.setCaretPosition(messageArea.getDocument().getLength());

		
	}
	
	public  synchronized void clearMessage()
	{
		allText.clear();
		messageArea.setText("");

		messageArea.setCaretPosition(messageArea.getDocument().getLength());
	}

	public JFrameNotifier(String uuid,String title, String message)
	{
		super();
		
		titlebar = title;
		
		if (title.equals(JRes.getText("system_log")))
		{
			img = new ImageIcon("./images/windows/image_sys_control.gif");
		}
		else
		{
			img = new ImageIcon("./images/windows/image_ok.gif");
		}
		
		setIconImage(img.getImage());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener());
		setTitle(title);
		setMessage(message);
		setUuid(uuid);

		init();

	}
	
	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public JFrameNotifier()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener());
		init();
	}

	public void init()
	{
				
		setResizable(false);
		setBounds(100, 100, 684, 273);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 677, 262);
		contentPane.add(desktopPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 677, 215);
		desktopPane.add(scrollPane);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		
		scrollPane.setViewportView(messageArea);
		
		JButton btnSave = new JButton(JRes.getText("save"));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    BufferedWriter writer;
			    String filename = System.getProperty("user.home")+File.separator+titlebar+".log";
				try
				{
					writer = new BufferedWriter(new FileWriter(filename));
				    writer.write(messageArea.getText().toString());
				    writer.close();
				    appendToMessage(JRes.getText("log_saved_to")+" : "+filename);
				}
				catch (IOException e1)
				{

				}
				writer=null;

			}
		});
		btnSave.setBounds(1, 220, 167, 25);
		desktopPane.add(btnSave);
		
		JButton btnClear = new JButton(JRes.getText("clear"));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearMessage();
			}
		});
		btnClear.setBounds(169, 220, 167, 25);
		desktopPane.add(btnClear);
		
		JButton btnEmail = new JButton(JRes.getText("email"));
		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AutoLab.emailqueue.addToQueue("Logs", "AutoLab Log "+titlebar+".log from [" + utils.getClientName() + "]", messageArea.getText(),"");
			}
		});
		btnEmail.setBounds(337, 220, 167, 25);
		desktopPane.add(btnEmail);
		
		JButton btnMinimize = new JButton(JRes.getText("minimise"));
		btnMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 setState(Frame.ICONIFIED);
			}
		});
		btnMinimize.setBounds(505, 220, 167, 25);
		desktopPane.add(btnMinimize);

		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
				
		Xy xy = JUtility.restoreWindowLayout(getTitle());
		
		setLocation(xy.x,xy.y);	
	    setState(Frame.NORMAL);
	    toFront();
		
		setVisible(true);
	}
}
