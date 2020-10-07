package com.commander4j.notifier;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.commander4j.resources.JRes;
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
	private ImageIcon img;

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

	public JFrameNotifier(String uuid,String title, String message)
	{
		super();
		
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
		setBounds(100, 100, 677, 246);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 677, 236);
		contentPane.add(desktopPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 653, 200);
		desktopPane.add(scrollPane);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		
		scrollPane.setViewportView(messageArea);

		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
				
		Xy xy = JUtility.restoreWindowLayout(getTitle());
		
		setLocation(xy.x,xy.y);	
	    setState(Frame.NORMAL);
	    toFront();
		
		setVisible(true);
	}
	
}
