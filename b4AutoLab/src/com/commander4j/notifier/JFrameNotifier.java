package com.commander4j.notifier;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.commander4j.utils.JUtility;

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

		setState(Frame.NORMAL);
		toFront();
		
	}

	public JFrameNotifier(String uuid,String title, String message)
	{
		super();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener());
		setTitle(title);
		setMessage(message);
		setUuid(uuid);
		init();
		setState(Frame.NORMAL);
		toFront();

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
		setBounds(100, 100, 581, 246);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 615, 236);
		contentPane.add(desktopPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 557, 200);
		desktopPane.add(scrollPane);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		
		scrollPane.setViewportView(messageArea);

		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		
		Xy xy = JUtility.getNotifierLocation();
		setLocation(xy.x,xy.y);
		
		
		
	}
	
}
