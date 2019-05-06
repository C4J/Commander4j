package com.commander4j.mw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.commander4j.Interface.Mapping.Map;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.util.Utility;
import javax.swing.ListSelectionModel;

public class StartGUI extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// private StartMW smw = new StartMW();
	private JButton btnStart;
	private JButton btnStop;
	private JPanel panelStatus = new JPanel();
	private JLabel lblStatus = new JLabel("Ready");
	private JPanel progressBarInterface = new JPanel();
	private final JLabel lblInterfaceStatus = new JLabel("Interface Status :");
	private JLabel label_NoOfMaps = new JLabel("");
	private JList4j<Map> listMaps = new JList4j<Map>();
	private static StartGUI frame;

	/**
	 * Launch the application.
	 */

	private void ConfirmExit()
	{
		if (Common.smw.isRunning())
		{
			int question = JOptionPane.showConfirmDialog(frame, "Closing application with stop interfaces ?", "Confirm", JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

			if (question == 0)
			{
				Common.smw.StopMiddleware();
				System.exit(0);
			}
		} else
		{
			System.exit(0);
		}
	}

	class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			ConfirmExit();
		}
	}

	private void populateList(String defaultitem)
	{

		DefaultComboBoxModel<Map> defComboBoxMod = new DefaultComboBoxModel<Map>();
		int sel = -1;
		if (Common.smw.isRunning())
		{

			for (int j = 0; j < Common.smw.cfg.getMaps().size(); j++)
			{
				defComboBoxMod.addElement(Common.smw.cfg.getMaps().get(j));

			}
		}
		ListModel<Map> jList1Model = defComboBoxMod;
		listMaps.setModel(jList1Model);

		listMaps.setCellRenderer(Common.renderer_list);
		listMaps.ensureIndexIsVisible(sel);
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					StartGUI.frame = new StartGUI();
					StartGUI.frame.setVisible(true);

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
		setResizable(false);
		setTitle("Commander4j Middleware" + " " + StartMain.version);
		Utility.initLogging("");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1319, 765);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		addWindowListener(new WindowListener());

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		JButton btnClose = new JButton(Common.icon_close);
		btnClose.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnClose.setText("Close");
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ConfirmExit();
			}
		});
		btnClose.setBounds(911, 657, 150, 38);
		contentPane.add(btnClose);

		btnStart = new JButton(Common.icon_ok);
		btnStart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Common.smw.StartMiddleware();
				if (Common.smw.cfg.getMapDirectoryErrorCount() > 0)
				{
					String errorMessage = "";

					for (int x = 0; x < Common.smw.cfg.getMapDirectoryErrorCount(); x++)
					{
						errorMessage = errorMessage + Common.smw.cfg.getMapDirectoryErrors().get(x) + "\n";
					}

					JOptionPane.showMessageDialog(frame, errorMessage, "Map Errors", JOptionPane.ERROR_MESSAGE);

				} else
				{
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					lblStatus.setText("Running");
					label_NoOfMaps.setText(String.valueOf(Common.smw.cfg.getMaps().size()));
					populateList("");
					btnClose.setEnabled(false);
					progressBarInterface.setBackground(new Color(0, 128, 0));
				}
			}
		});
		btnStart.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStart.setMnemonic(KeyEvent.VK_ENTER);
		btnStart.setText("Start");
		btnStart.setSelectedIcon(Common.icon_cancel);
		btnStart.setOpaque(true);
		btnStart.setBounds(261, 657, 150, 38);
		contentPane.add(btnStart);

		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelStatus.setBounds(0, 707, 1320, 30);
		contentPane.add(panelStatus);
		panelStatus.setLayout(null);

		lblStatus.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblStatus.setBounds(4, 0, 1320, 25);
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		panelStatus.add(lblStatus);

		progressBarInterface.setForeground(new Color(0, 128, 0));
		progressBarInterface.setBounds(151, 15, 15, 15);
		progressBarInterface.setPreferredSize(new Dimension(40, 40));
		progressBarInterface.setBackground(Color.RED);
		contentPane.add(progressBarInterface);
		lblInterfaceStatus.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblInterfaceStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		lblInterfaceStatus.setBounds(12, 12, 131, 22);

		contentPane.add(lblInterfaceStatus);

		JLabel lblNumberOfMaps = new JLabel("Number of Maps :");
		lblNumberOfMaps.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNumberOfMaps.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNumberOfMaps.setBounds(181, 12, 131, 22);
		contentPane.add(lblNumberOfMaps);

		label_NoOfMaps.setBounds(319, 12, 60, 22);
		contentPane.add(label_NoOfMaps);

		JScrollPane scrollPaneMaps = new JScrollPane();
		scrollPaneMaps.setBounds(0, 65, 1313, 580);
		contentPane.add(scrollPaneMaps);
		listMaps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPaneMaps.setViewportView(listMaps);

		JButton buttonHelp = new JButton((Icon) null);
		buttonHelp.setFont(new Font("Dialog", Font.PLAIN, 12));
		buttonHelp.setText("Help");
		buttonHelp.setBounds(749, 657, 150, 38);
		contentPane.add(buttonHelp);

		JLabel lblIdDescriptionType = new JLabel(" Map Id  Description                                                             Input       Output(s)         In      Out  Input Path");
		lblIdDescriptionType.setForeground(Color.BLUE);
		lblIdDescriptionType.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblIdDescriptionType.setBounds(0, 46, 1300, 22);
		contentPane.add(lblIdDescriptionType);

		JButton btnRefresh = new JButton((Icon) null);
		btnRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				populateList("");
			}
		});
		btnRefresh.setText("Refresh");
		btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRefresh.setBounds(587, 657, 150, 38);
		contentPane.add(btnRefresh);

		btnStop = new JButton(Common.icon_cancel);
		btnStop.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				populateList("");
				Common.smw.StopMiddleware();

				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				lblStatus.setText("Stopped");
				btnClose.setEnabled(true);
				progressBarInterface.setBackground(Color.RED);
			}
		});
		btnStop.setEnabled(false);
		btnStop.setText("Stop");
		btnStop.setOpaque(true);
		btnStop.setMnemonic(KeyEvent.VK_ENTER);
		btnStop.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnStop.setBounds(423, 657, 150, 38);
		contentPane.add(btnStop);
	}
}
