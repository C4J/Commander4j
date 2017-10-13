package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogAbout.java
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


/**
 * JDialogAbout is a modal dialog box which displays the application name and program version.
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogAbout.jpg" >
 */
public class JDialogAbout extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelBy;
	private JLabel4j_std jLabelVersion;
	private JLabel4j_std jLabelWebPage;
	private JLabel4j_std jLabelAuthor;
	private JButton4j jButtonOk;
	private URI helpURI;
	private URI mailtoURI;
	private Image i;
	private static boolean HelpAvailable = false;
	public static Desktop desktop;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std jLabelEmail;

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
				helpURI = new URI(Common.appWebsite);
				mailtoURI = new URI("mailto:"+Common.appSupportEmail);
				HelpAvailable = true;
			} else
			{
				HelpAvailable = false;
			}

		} catch (Exception e)
		{
			HelpAvailable = false;
		}

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

	}

	private void initGUI()
	{
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
						jButtonOk.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								dispose();
							}
						});
					}
					{
						jLabelBy = new JLabel4j_std();
						jDesktopPane1.add(jLabelBy);
						jLabelBy.setText("by");
						jLabelBy.setBounds(147, 112, 56, 21);
						jLabelBy.setFont(new java.awt.Font("Dialog", 0, 12));
						jLabelBy.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						jLabelAuthor = new JLabel4j_std();
						jDesktopPane1.add(jLabelAuthor);
						jLabelAuthor.setText(Common.appAuthor);
						jLabelAuthor.setBounds(14, 133, 322, 14);
						jLabelAuthor.setFont(new java.awt.Font("Dialog", 0, 12));
						jLabelAuthor.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						jLabelWebPage = new JLabel4j_std();
						jLabelWebPage.setHorizontalAlignment(SwingConstants.CENTER);
						jDesktopPane1.add(jLabelWebPage);
						jLabelWebPage.setText(Common.appWebsite);
						jLabelWebPage.setBounds(14, 175, 322, 14);
						jLabelWebPage.setForeground(new java.awt.Color(0, 0, 255));
						jLabelWebPage.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (HelpAvailable)
								{
									try
									{
										desktop.browse(helpURI);
									} catch (Exception ex)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
									}
								}
							}

							public void mouseExited(MouseEvent evt)
							{
								Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
							}

							public void mouseEntered(MouseEvent evt)
							{
								Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
								setCursor(hourglassCursor);
							}
						});
					}
					{
						jLabelEmail = new JLabel4j_std();
						jLabelEmail.setHorizontalAlignment(SwingConstants.CENTER);
						jDesktopPane1.add(jLabelEmail);
						jLabelEmail.setText("Email : "+Common.appSupportEmail);
						jLabelEmail.setBounds(24, 154, 303, 14);
						jLabelEmail.setForeground(new java.awt.Color(0, 0, 255));
						jLabelEmail.addMouseListener(new MouseAdapter()
						{
							public void mouseClicked(MouseEvent evt)
							{
								if (HelpAvailable)
								{
									try
									{
										desktop.mail(mailtoURI);
									} catch (Exception ex)
									{
										JUtility.errorBeep();
										JOptionPane.showMessageDialog(Common.mainForm, ex.getMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE, Common.icon_confirm);
									}
								}
							}

							public void mouseExited(MouseEvent evt)
							{
								Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
							}

							public void mouseEntered(MouseEvent evt)
							{
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
						jLabelVersion = new JLabel4j_std();
						jDesktopPane1.add(jLabelVersion);
						jLabelVersion.setText(Common.appDisplayName+" " + JVersion.getProgramVersion());
						jLabelVersion.setBounds(14, 7, 322, 35);
						jLabelVersion.setFont(new java.awt.Font("Serif", 1, 28));
						jLabelVersion.setForeground(new java.awt.Color(255, 0, 0));
						jLabelVersion.setHorizontalAlignment(SwingConstants.CENTER);
					}

					{
						final JBarcodePanel panelBarcode = new JBarcodePanel();
						panelBarcode.setBackground(Color.WHITE);
						panelBarcode.setLayout(null);
						panelBarcode.setBounds(14, 48, 314, 60);
						panelBarcode.setImage(i);
						jDesktopPane1.add(panelBarcode);
					}
				}
			}
			this.setSize(356, 264);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
