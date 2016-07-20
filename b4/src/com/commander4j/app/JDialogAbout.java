package com.commander4j.app;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.bar.JBarcodePanel;
import com.commander4j.bar.JEANImage;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

public class JDialogAbout extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabel2;
	private JLabel4j_std jLabel5;
	private JLabel4j_std jLabel1;
	private JLabel4j_std jLabel3;
	private JButton4j jButtonOk;
	private URI helpURI;
	private URI mailtoURI;
	private Image i;
	private static boolean HelpAvailable = false;
	public static Desktop desktop;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std jLabel4;

	public JDialogAbout(JFrame frame)
	{
		super(frame);
		getContentPane().setLayout(null);
		initGUI();

		try
		{
			if (Desktop.isDesktopSupported())
			{
				desktop = Desktop.getDesktop();
				helpURI = new URI("http://www.commander4j.com");
				mailtoURI = new URI("mailto:support@commander4j.com");
				HelpAvailable = true;
			}
			else
			{
				HelpAvailable = false;
			}

		}
		catch (Exception e)
		{
			HelpAvailable = false;
		}

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	private void initGUI() {
		try
		{
			{
				this.setModal(true);
				this.setResizable(false);
				this.setTitle("About");
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				{
					jDesktopPane1 = new JDesktopPane();
					jDesktopPane1.setBackground(Common.color_edit_properties);
					jDesktopPane1.setBounds(0, 0, 356, 242);
					this.getContentPane().add(jDesktopPane1);
					jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 231));
					jDesktopPane1.setLayout(null);
					{
						jButtonOk = new JButton4j(Common.icon_ok);
						jDesktopPane1.add(jButtonOk);
						jButtonOk.setText(lang.get("btn_Ok"));
						jButtonOk.setMnemonic(lang.getMnemonicChar());
						jButtonOk.setBounds(126, 196, 91, 28);
						jButtonOk.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								dispose();
							}
						});
					}
					{
						jLabel2 = new JLabel4j_std();
						jDesktopPane1.add(jLabel2);
						jLabel2.setText("by");
						jLabel2.setBounds(147, 112, 56, 21);
						jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
						jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						jLabel3 = new JLabel4j_std();
						jDesktopPane1.add(jLabel3);
						jLabel3.setText("David Garratt");
						jLabel3.setBounds(119, 133, 105, 14);
						jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
						jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						jLabel1 = new JLabel4j_std();
						jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
						jDesktopPane1.add(jLabel1);
						jLabel1.setText("http://www.commander4j.com");
						jLabel1.setBounds(48, 175, 241, 14);
						jLabel1.setForeground(new java.awt.Color(0, 0, 255));
						jLabel1.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (HelpAvailable)
								{
									try
									{
										desktop.browse(helpURI);
									}
									catch (Exception ex)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
									}
								}
							}

							public void mouseExited(MouseEvent evt) {
								Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
							}

							public void mouseEntered(MouseEvent evt) {
								Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
								setCursor(hourglassCursor);
							}
						});
					}
					{
						jLabel4 = new JLabel4j_std();
						jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
						jDesktopPane1.add(jLabel4);
						jLabel4.setText("Email : support@commander4j.com");
						jLabel4.setBounds(44, 154, 242, 14);
						jLabel4.setForeground(new java.awt.Color(0, 0, 255));
						jLabel4.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								if (HelpAvailable)
								{
									try
									{
										desktop.mail(mailtoURI);
									}
									catch (Exception ex)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
									}
								}
							}

							public void mouseExited(MouseEvent evt) {
								Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
							}

							public void mouseEntered(MouseEvent evt) {
								Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
								setCursor(hourglassCursor);
							}
						});
					}
					{
						String msg = "4001234567890123456789012345678901234567890";
						i = JEANImage.getAWTImage("xml/barcode/about.xml", msg);
						Graphics gph = i.getGraphics();
						jDesktopPane1.paintComponents(gph.create(0, 0, 200, 50));
					}
					{
						jLabel5 = new JLabel4j_std();
						jDesktopPane1.add(jLabel5);
						jLabel5.setText("Commander4j " + JVersion.getProgramVersion());
						jLabel5.setBounds(14, 7, 322, 35);
						jLabel5.setFont(new java.awt.Font("Serif", 1, 28));
						jLabel5.setForeground(new java.awt.Color(255, 0, 0));
						jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
					}

					{
						final JBarcodePanel panel = new JBarcodePanel();
						panel.setBackground(Color.WHITE);
						panel.setLayout(null);
						panel.setBounds(14, 48, 314, 60);
						panel.setImage(i);
						jDesktopPane1.add(panel);
					}
				}
			}
			this.setSize(356, 264);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
