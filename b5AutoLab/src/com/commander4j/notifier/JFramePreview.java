package com.commander4j.notifier;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.commander4j.autolab.AutoLab;
import com.commander4j.prodLine.ProdLine;
import com.commander4j.resources.JRes;
import com.commander4j.utils.JUtility;

public class JFramePreview extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String uuid = "";
	private ImageIcon img;
	private String titlebar = "";
	private JLabel lblImage = new JLabel("");
	private int w = 420;
	private int h = 600;
	private JDesktopPane desktopPane = new JDesktopPane();
	private String ImageFilename = "./labelary/blank.png";
	private String lastMessage = "";

	public boolean setData(String zpl)
	{
		boolean result = false;
		ImageFilename = "./labelary/blank.png";

		URL url;
		try
		{
			url = new URL(AutoLab.config.getLabelaryURL());
			
			appendNotification("Requesting image from "+AutoLab.config.getLabelaryURL());

			HttpURLConnection connection;
			try
			{
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(2000);
				connection.setReadTimeout(2000);
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Accept-Charset", "UTF-8");
				connection.setRequestProperty("User-Agent", "Commander4j AutoLab4j");
				connection.setRequestProperty("Content-Length", String.valueOf(zpl.length()));

				// connection.setRequestProperty("Accept", "application/png");

				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),Charset.forName("UTF-8"));
				writer.write(zpl);
				writer.flush();

				InputStream inputStream = connection.getInputStream();

				OutputStream outputStream = new FileOutputStream("./labelary/" + titlebar + ".png");

				int bytesRead = -1;
				byte[] buffer = new byte[10240];
				while ((bytesRead = inputStream.read(buffer)) != -1)
				{
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();

				ImageFilename = "./labelary/" + titlebar + ".png";

				setImage();

			}
			catch (IOException e)
			{
				ImageFilename = "./labelary/unavailabe.png";
				appendNotification("Unable to connect "+AutoLab.config.getLabelaryURL());
				appendNotification("Error "+e.getMessage());
			}

		}
		catch (MalformedURLException e)
		{
			ImageFilename = "./labelary/unavailabe.png";
			appendNotification("Error returned from "+AutoLab.config.getLabelaryURL());
			appendNotification("Error "+e.getMessage());
		}
		return result;
	}
	
	public void appendNotification(String message)
	{
		if (message.equals(lastMessage) == false)
		{
			((ProdLine) AutoLab.threadList_ProdLine.get(uuid)).appendNotification(message);
			lastMessage = message;
		}
	}

	private Dimension getScaledDimension(Dimension imgSize, Dimension boundary)
	{

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width)
		{
			// scale width to fit
			new_width = bound_width;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height)
		{
			// scale height to fit instead
			new_height = bound_height;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);

	}

	public void setImage()
	{
		BufferedImage originalImage;

		try
		{

			originalImage = ImageIO.read(new File(ImageFilename));
			
			Dimension newImageSize = getScaledDimension(new Dimension(originalImage.getWidth(),originalImage.getHeight()), new Dimension(w,h));

			desktopPane.setBounds(0, 0, w, h);

			lblImage.setIcon(new javax.swing.ImageIcon(originalImage.getScaledInstance(newImageSize.width, newImageSize.height, Image.SCALE_SMOOTH)));

			lblImage.setSize(w, h);

			lblImage.setPreferredSize(new Dimension(w, h));

			lblImage.repaint();

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			((JFrame) e.getSource()).setExtendedState(JFrame.ICONIFIED);
		}
	}

	ComponentAdapter ca = new ComponentAdapter()
	{
		public void componentResized(ComponentEvent e)
		{
			// This is only called when the user releases the mouse button.

			w = getSize().width;
			h = getSize().height;

			System.out.println("w=" + w);
			System.out.println("h=" + h);

			setImage();
		}
	};

	public JFramePreview(String uuid, String title, String message)
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

		setTitle(title + " Label");

		setUuid(uuid);

		init();

		setImage();
		addComponentListener(ca);

	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public JFramePreview()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener());
		init();
	}

	public void init()
	{

		// setResizable(false);
		setBounds(100, 100, w, h);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		desktopPane.setBounds(0, 0, w, h);
		contentPane.add(desktopPane);
		lblImage.setBounds(0, 0, w, h);
		desktopPane.add(lblImage);

		setAlwaysOnTop(true);
		setLocationRelativeTo(null);

		Xy xy = JUtility.restoreWindowLayout(getTitle());

		setLocation(xy.x, xy.y);
		setState(Frame.NORMAL);
		toFront();

		setVisible(true);
	}
}
