package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.commander4j.db.JDBJourney;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBModule;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.commander4j.util.JDateControl;
import com.commander4j.calendar.JCalendarButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class JInternalFrameJourneyProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;
	private String ljourney;
	private String lbatch;
	private JDBJourney journeys = new JDBJourney(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JTextField4j jTextFieldDespatchNo;
	private JLabel4j_std jLabel2;
	private JComboBox4j<String> jComboBoxStatus;
	private JLabel4j_std jLabel3;
	private JTextField4j jTextFieldJourneyRef;
	private JLabel4j_std jLabel1;
	private JDBLanguage lang;
	private String title;
	private JLabel4j_std jStatusText;
	private JTextField4j jTextFieldLocation = new JTextField4j(15);
	private JDateControl dateControlTimeslot = new JDateControl();
	private JCalendarButton calendarButton;

	public JInternalFrameJourneyProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		mod.getModuleProperties("FRM_ADMIN_JOURNEY_REF_EDIT");
		title = mod.getDescription();

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_JOURNEY_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDespatchNo.requestFocus();
			}
		});
	}

	public void setJourneyRef(String journey)
	{
		ljourney = journey;

		setTitle(title + " [" + journey + "]");

		jTextFieldJourneyRef.setText(ljourney);

		journeys.setJourneyRef(ljourney);
		jTextFieldJourneyRef.setText(ljourney);

		if (journeys.getJourneyRefProperties(ljourney))
		{
			jTextFieldDespatchNo.setText(journeys.getDespatchNo());

			jComboBoxStatus.setSelectedItem(journeys.getStatus());

			try
			{
				dateControlTimeslot.setDate(journeys.getTimeslot());
			} catch (Exception ex)
			{
				dateControlTimeslot.setDate(JUtility.getSQLDateTime());
			}

			jTextFieldLocation.setText(journeys.getLocationTo());
			jButtonUpdate.setEnabled(false);
		} else
		{
			jTextFieldDespatchNo.setText("");
			jComboBoxStatus.setSelectedItem("Unassigned");

			jButtonUpdate.setEnabled(true);
		}
	}

	public JInternalFrameJourneyProperties(String journey)
	{
		this();
		setJourneyRef(journey);

	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 387, 269);
			setVisible(true);
			this.setClosable(true);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
				jDesktopPane1.setPreferredSize(new Dimension(350, 200));
				jDesktopPane1.setLayout(null);
				{
					jButtonUpdate = new JButton4j(Common.icon_update);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setMnemonic(lang.getMnemonicChar());
					jButtonUpdate.setBounds(21, 172, 112, 32);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{

							Boolean result = false;
							try
							{
								journeys.setStatus((String) jComboBoxStatus.getSelectedItem());
							} catch (Exception e)
							{
								journeys.setStatus("");
							}

							journeys.setDespatchNo(jTextFieldDespatchNo.getText());
							journeys.setLocationTo(jTextFieldLocation.getText());
							journeys.setTimeslot(JUtility.getTimestampFromDate(dateControlTimeslot.getDate()));

							if (journeys.isValidJourneyRef())
							{
								result = journeys.update();
							} else
							{
								result = journeys.create();
							}
							if (result)
							{
								jStatusText.setText("");
								jButtonUpdate.setEnabled(false);
							} else
							{
								jStatusText.setText(journeys.getErrorMessage());
							}
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setMnemonic(lang.getMnemonicChar());
					jButtonHelp.setBounds(133, 172, 112, 32);
				}
				{
					jButtonCancel = new JButton4j(Common.icon_close);
					jDesktopPane1.add(jButtonCancel);
					jButtonCancel.setText(lang.get("btn_Close"));
					jButtonCancel.setMnemonic(lang.getMnemonicChar());
					jButtonCancel.setBounds(245, 172, 112, 32);
					jButtonCancel.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jLabel1 = new JLabel4j_std();
					jDesktopPane1.add(jLabel1);
					jLabel1.setText(lang.get("lbl_Journey_Ref"));
					jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel1.setBounds(49, 13, 70, 21);
				}
				{
					jTextFieldJourneyRef = new JTextField4j(JDBMaterial.field_material);
					jDesktopPane1.add(jTextFieldJourneyRef);
					jTextFieldJourneyRef.setText(ljourney);
					jTextFieldJourneyRef.setBounds(126, 13, 126, 21);
					jTextFieldJourneyRef.setEnabled(false);
					jTextFieldJourneyRef.setEditable(false);
				}
				{
					jLabel3 = new JLabel4j_std();
					jDesktopPane1.add(jLabel3);
					jLabel3.setText(lang.get("lbl_Despatch_No"));
					jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel3.setBounds(49, 41, 70, 21);
				}
				{
					jTextFieldDespatchNo = new JTextField4j(JDBMaterialBatch.field_batch_number);
					jTextFieldDespatchNo.addKeyListener(new KeyAdapter()
					{
						@Override
						public void keyTyped(KeyEvent e)
						{

							jButtonUpdate.setEnabled(true);
						}

						@Override
						public void keyReleased(KeyEvent e)
						{
							if (jTextFieldDespatchNo.getText().equals(""))
							{
								jComboBoxStatus.setSelectedItem("Unassigned");
							} else
							{
								jComboBoxStatus.setSelectedItem("Assigned");
							}
						}
					});
					jDesktopPane1.add(jTextFieldDespatchNo);
					jTextFieldDespatchNo.setText(lbatch);
					jTextFieldDespatchNo.setBounds(126, 41, 126, 21);
				}
				{
					ComboBoxModel<String> jComboBoxStatusModel = new DefaultComboBoxModel<String>(
							Common.JourneyRefStatus);
					jComboBoxStatus = new JComboBox4j<String>();
					jDesktopPane1.add(jComboBoxStatus);
					jComboBoxStatus.setModel(jComboBoxStatusModel);
					jComboBoxStatus.setBounds(126, 69, 150, 21);
					jComboBoxStatus.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jLabel2 = new JLabel4j_std();
					jDesktopPane1.add(jLabel2);
					jLabel2.setText(lang.get("lbl_Journey_Status"));
					jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
					jLabel2.setBounds(42, 69, 77, 21);
				}
				{
					jStatusText = new JLabel4j_std();
					jStatusText.setForeground(new Color(255, 0, 0));
					jStatusText.setBackground(Color.GRAY);
					jStatusText.setBounds(0, 216, 377, 21);
					jDesktopPane1.add(jStatusText);
				}
				JLabel4j_std label4j_std = new JLabel4j_std();
				label4j_std.setText(lang.get("lbl_Location_ID"));
				label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std.setBounds(45, 99, 74, 21);
				jDesktopPane1.add(label4j_std);

				jTextFieldLocation.setBounds(126, 99, 103, 21);
				jDesktopPane1.add(jTextFieldLocation);

				JButton4j button4j = new JButton4j(Common.icon_lookup);
				button4j.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = "Y";
						if (JLaunchLookup.locations())
						{
							jTextFieldLocation.setText(JLaunchLookup.dlgResult);
							jButtonUpdate.setEnabled(true);
						}
					}
				});
				button4j.setBounds(230, 99, 21, 21);
				jDesktopPane1.add(button4j);

				JLabel4j_std label4j_std1 = new JLabel4j_std();
				label4j_std1.setText(lang.get("lbl_Timeslot"));
				label4j_std1.setHorizontalAlignment(SwingConstants.TRAILING);
				label4j_std1.setBounds(11, 127, 108, 25);
				jDesktopPane1.add(label4j_std1);
				dateControlTimeslot.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				dateControlTimeslot.setBounds(126, 127, 128, 25);
				jDesktopPane1.add(dateControlTimeslot);

				calendarButton = new JCalendarButton(dateControlTimeslot);
				calendarButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				calendarButton.setBounds(254, 130, 21, 21);
				jDesktopPane1.add(calendarButton);

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
