package com.commander4j.clone;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JFrameCloneDB.java
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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.commander4j.app.JVersion;
import com.commander4j.cfg.JDialogSetupPassword;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBField;
import com.commander4j.db.JDBStructure;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JList4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JUnique;
import com.commander4j.util.JUtility;
import com.commander4j.util.JWait;

/**
 * The JFrameCloneDB class allows a user copy a commander4j schema between two
 * databases which can be different type if required (MS SQL, mySQL or Oracle).
 * The commander4j schema version number needs to be the same revision in the
 * source and destination database. The database schema and program version are
 * updated using the Setup4j program.
 * 
 * <p>
 * <img alt="" src="./doc-files/JFrameCloneDB.jpg" >
 *
 */
public class JFrameCloneDB extends JFrame
{
	private static final long serialVersionUID = 1;
	private JDesktopPane desktopPane = new JDesktopPane();
	private JButton4j jButtonClose;
	private JButton4j jButtonClone;
	private JList4j<JHost> jListHostFrom;
	private JList4j<JHost> jListHostTo;
	private JScrollPane jScrollPaneFrom;
	private JScrollPane jScrollPaneTo;
	private LinkedList<JHost> hostListFrom = new LinkedList<JHost>();
	private LinkedList<JHost> hostListTo = new LinkedList<JHost>();
	final Logger logger = Logger.getLogger(JFrameCloneDB.class);
	private JPanel contentPane;
	private JProgressBar progressBar = new JProgressBar();
	private JLabel4j_std labelCommand = new JLabel4j_std("");
	private JHost hstFrom = new JHost();
	private JHost hstTo = new JHost();
	private String sessionFrom = JUnique.getUniqueID();
	private String sessionTo = JUnique.getUniqueID();
	private String hostIDFrom = "";
	private String hostIDTo = "";
	String lastError="";
	String currentError="";

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					JFrameCloneDB frame = new JFrameCloneDB();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public JFrameCloneDB()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		desktopPane.setBounds(0, 0, 574, 549);
		desktopPane.setBackground(Color.WHITE);
		contentPane.add(desktopPane);

		Common.sessionID = JUnique.getUniqueID();
		Common.sd.setData(Common.sessionID, "silentExceptions", "No", true);
		JUtility.initLogging("");
		logger.debug("JFrameCloneDB starting...");

		initGUI();

		setIconImage(Common.imageIconloader.getImageIcon(Common.image_osx_clone4j).getImage());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);

		getHosts();

		if (Common.setupPassword.equals("") == false)
		{
			JDialogSetupPassword u = new JDialogSetupPassword(null, Common.setupPassword);
			u.setModal(true);
		}

		populateListFrom("");
		populateListTo("");
	}

	private void getHosts()
	{
		hostListFrom.clear();
		hostListTo.clear();
		Common.hostList.loadHosts();
		hostListFrom = Common.hostList.getHosts();
		hostListTo = Common.hostList.getHosts();
	}

	private void populateListFrom(String defaultitem)
	{
		int sel = 0;

		DefaultComboBoxModel<JHost> DefComboBoxMod = new DefaultComboBoxModel<JHost>();

		for (int j = 0; j < hostListFrom.size(); j++)
		{
			DefComboBoxMod.addElement(hostListFrom.get(j));
			if (hostListFrom.get(j).getSiteDescription().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JHost> jList1Model = DefComboBoxMod;
		jListHostFrom.setModel(jList1Model);
		jListHostFrom.setCellRenderer(Common.renderer_list);
		jListHostFrom.setSelectedIndex(sel);
	}

	private void populateListTo(String defaultitem)
	{
		int sel = 0;

		DefaultComboBoxModel<JHost> DefComboBoxMod = new DefaultComboBoxModel<JHost>();

		for (int j = 0; j < hostListTo.size(); j++)
		{
			DefComboBoxMod.addElement(hostListTo.get(j));
			if (hostListTo.get(j).getSiteDescription().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JHost> jList1Model = DefComboBoxMod;
		jListHostTo.setModel(jList1Model);
		jListHostTo.setCellRenderer(Common.renderer_list);
		jListHostTo.setSelectedIndex(sel);
	}

	private void getHostDataFrom()
	{
		int j = jListHostFrom.getSelectedIndex();
		if (j >= 0)
		{
			hstFrom = new JHost();
			hstFrom = (JHost) jListHostFrom.getModel().getElementAt(j);
			hostIDFrom = hstFrom.getSiteNumber();

		} else
		{

		}
		enableClone();
	}

	private void setStatusBarText(String tzt)
	{
		labelCommand.setText(tzt);
		labelCommand.paintImmediately(labelCommand.getVisibleRect());
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException ie)
		{
		}
	}

	private void getHostDataTo()
	{
		int j = jListHostTo.getSelectedIndex();
		if (j >= 0)
		{
			hstTo = new JHost();
			hstTo = (JHost) jListHostTo.getModel().getElementAt(j);
			hostIDTo = hstTo.getSiteNumber();

		} else
		{

		}
		enableClone();
	}

	private void enableClone()
	{
		if (hostIDFrom.equals(hostIDTo) == false)
		{
			jButtonClone.setEnabled(true);
		} else
		{
			jButtonClone.setEnabled(false);
		}
	}

	private void initGUI()
	{
		try
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Database Clone " + JVersion.getProgramVersion());
			desktopPane.setLayout(null);

			jScrollPaneFrom = new JScrollPane();
			desktopPane.add(jScrollPaneFrom);
			jScrollPaneFrom.setBounds(20, 37, 258, 395);

			ListModel<JHost> jListHostsModelFrom = new DefaultComboBoxModel<JHost>();
			jListHostFrom = new JList4j<JHost>();
			jScrollPaneFrom.setViewportView(jListHostFrom);
			jListHostFrom.setModel(jListHostsModelFrom);
			jListHostFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListHostFrom.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent evt)
				{
					getHostDataFrom();
				}
			});

			jScrollPaneTo = new JScrollPane();
			desktopPane.add(jScrollPaneTo);
			jScrollPaneTo.setBounds(295, 37, 258, 395);

			ListModel<JHost> jListHostsModelTo = new DefaultComboBoxModel<JHost>();
			jListHostTo = new JList4j<JHost>();
			jScrollPaneTo.setViewportView(jListHostTo);
			jListHostTo.setModel(jListHostsModelTo);
			jListHostTo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListHostTo.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent evt)
				{
					getHostDataTo();
				}
			});

			jButtonClone = new JButton4j(Common.icon_clone);
			desktopPane.add(jButtonClone);
			jButtonClone.setText("Clone Database");
			jButtonClone.setBounds(118, 444, 160, 36);
			jButtonClone.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{

					String schemaFrom = "";
					String schemaTo = "";
					JDBControl ctrl;

					// Source & Destination must be different Hosts//
					setStatusBarText("Validating selected hosts");
					if (hostIDFrom.equals(hostIDTo) == false)
					{

						// Check we can connect to Source //
						setStatusBarText("Connecting to source...");
						if (hstFrom.connect(sessionFrom, hostIDFrom))
						{

							// Check we can connect to Destination //
							setStatusBarText("Connecting to destination...");
							if (hstTo.connect(sessionTo, hostIDTo))
							{

								// Check Application Schema Versions are the
								// same in Source and Destination //
								setStatusBarText("Checking schema versions...");
								ctrl = new JDBControl(hostIDFrom, sessionFrom);
								schemaFrom = ctrl.getKeyValue("SCHEMA VERSION");
								ctrl = new JDBControl(hostIDTo, sessionTo);
								schemaTo = ctrl.getKeyValue("SCHEMA VERSION");

								if (schemaFrom.equals(schemaTo))
								{
									// OK //
									setStatusBarText("Getting source table names...");
									int tableCountFrom = 0;
									JDBStructure strucFrom = new JDBStructure(hostIDFrom, sessionFrom);
									LinkedList<String> tablesFrom = strucFrom.getTableNames();
									tableCountFrom = tablesFrom.size();
									progressBar.setMinimum(1);
									progressBar.setMaximum(tableCountFrom);

									setStatusBarText("Getting destination table names...");
									int tableCountTo = 0;
									JDBStructure strucTo = new JDBStructure(hostIDTo, sessionTo);
									LinkedList<String> tablesTo = strucTo.getTableNames();
									tableCountTo = tablesTo.size();

									if (tableCountFrom == tableCountTo)
									{
										String table = "";
										for (int tf = 0; tf < tableCountFrom; tf++)
										{
											table = tablesFrom.get(tf);
											//table = "APP_QM_ANALYSIS";
											progressBar.setValue(tf + 1);
											progressBar.paintImmediately(progressBar.getVisibleRect());
											if (tablesTo.contains(table))
											{

												// GET FIELDS FOR CURRENT TABLE
												setStatusBarText("Getting field names for " + table);
												LinkedList<JDBField> fieldNames = strucFrom.getFieldNames(table);
												JWait.milliSec(100);

												// CREATE INSERT STATEMENT FOR
												// TARGET DATABASE //
												setStatusBarText("Generating insert SQL for " + table);
												String insertTable = "insert into " + table;
												String insertFields = "";
												String insertPlaceMarkers = "";
												String comma = "";

												for (int temp = 0; temp < fieldNames.size(); temp++)
												{
													if (temp == 0)
													{
														comma = "";
													} else
													{
														comma = ",";
													}
													insertFields = insertFields + comma + fieldNames.get(temp).getfieldName();
													insertPlaceMarkers = insertPlaceMarkers + comma + "?";
												}

												String insertStatement = insertTable + " (" + insertFields + ") values (" + insertPlaceMarkers + ")";

												// SELECT ALL SOURCE RECORDS //
												setStatusBarText("Copying table " + table);
												Long recordsCopied = (long) 0;
												try
												{
													hstFrom.getConnection(sessionFrom).setAutoCommit(false);
													hstTo.getConnection(sessionTo).setAutoCommit(false);
													PreparedStatement truncateData = hstTo.getConnection(sessionTo).prepareStatement("truncate table " + table);
													PreparedStatement destinationData = hstTo.getConnection(sessionTo).prepareStatement(insertStatement);
													PreparedStatement sourceData = hstFrom.getConnection(sessionFrom).prepareStatement("select * from " + table);
													sourceData.setFetchDirection(ResultSet.FETCH_FORWARD);
													destinationData.setFetchDirection(ResultSet.FETCH_FORWARD);
													sourceData.setFetchSize(1);
													destinationData.setFetchSize(1);
													truncateData.setFetchSize(1);
													ResultSet sourceResultset = sourceData.executeQuery();

													truncateData.execute();
													truncateData.close();
													truncateData = null;

													while (sourceResultset.next())
													{
														for (int fldfrom = 0; fldfrom < fieldNames.size(); fldfrom++)
														{
															JDBField field = fieldNames.get(fldfrom);
															if (field.getfieldType().toLowerCase().equals("varchar"))
															{
																String value;
																value = sourceResultset.getString(field.getfieldName());
																destinationData.setString(fldfrom + 1, value);
																// System.out.println("Table
																// = \"" + table
																// + "\" Field =
																// \"" +
																// field.getfieldName()
																// + "\" Value =
																// \"" + value +
																// "\"");
															}
															if (field.getfieldType().toLowerCase().equals("nvarchar"))
															{
																String value;
																value = sourceResultset.getString(field.getfieldName());
																destinationData.setString(fldfrom + 1, value);
																// System.out.println("Table
																// = \"" + table
																// + "\" Field =
																// \"" +
																// field.getfieldName()
																// + "\" Value =
																// \"" + value +
																// "\"");
															}
															if ((field.getfieldType().toLowerCase().equals("decimal")) | (field.getfieldType().toLowerCase().equals("numeric")) | (field.getfieldType().toLowerCase().equals("float")))
															{
																BigDecimal value;
																value = sourceResultset.getBigDecimal(field.getfieldName());
																destinationData.setBigDecimal(fldfrom + 1, value);
																// System.out.println("Table
																// = \"" + table
																// + "\" Field =
																// \"" +
																// field.getfieldName()
																// + "\" Value =
																// \"" +
																// JUtility.replaceNullObjectwithBlank(value).toString()
																// + "\"");
															}
															if (field.getfieldType().toLowerCase().equals("datetime"))
															{
																Timestamp value;
																value = sourceResultset.getTimestamp(field.getfieldName());
																destinationData.setTimestamp(fldfrom + 1, value);
																// System.out.println("Table
																// = \"" + table
																// + "\" Field =
																// \"" +
																// field.getfieldName()
																// + "\" Value =
																// \""
																// +
																// JUtility.replaceNullObjectwithBlank(value).toString()
																// + "\"");
															}
															if ((field.getfieldType().toLowerCase().equals("int")) | (field.getfieldType().toLowerCase().equals("bigint")))
															{
																Integer value;
																value = sourceResultset.getInt(field.getfieldName());
																destinationData.setInt(fldfrom + 1, value);
																// System.out.println("Table
																// = \"" + table
																// + "\" Field =
																// \"" +
																// field.getfieldName()
																// + "\" Value =
																// \""
																// +
																// JUtility.replaceNullObjectwithBlank(value).toString()
																// + "\"");
															}
															field = null;
														}
														try
														{
															destinationData.execute();
															hstTo.getConnection(sessionTo).commit();
															destinationData.clearParameters();
															recordsCopied++;

														} catch (Exception ex)
														{
															
															currentError = table+ " "+ex.getMessage();
															if (currentError.equals(lastError)==false)
															{
																setStatusBarText(currentError);
																logger.debug(currentError);
																lastError = currentError;
															}
														}
													}

													sourceResultset.close();
													sourceResultset = null;
													destinationData.close();
													destinationData = null;
													sourceData.close();
													sourceData = null;

													setStatusBarText("Copying complete");


												} catch (SQLException e)
												{
													logger.error("Error reading " + table + " ["+e.getMessage()+"]");
													labelCommand.setText("Error reading " + table+ " ["+e.getMessage()+"]");
												}

											} else
											{
												labelCommand.setText("Table " + table + " does not exist in destination");
											}

										}
									} else
									{

										labelCommand.setText("Number of tables mismatch " + String.valueOf(tableCountFrom) + "/" + String.valueOf(tableCountTo));
									}

								} else
								{
									labelCommand.setText("Schema version mismatch " + String.valueOf(schemaFrom) + "/" + String.valueOf(schemaTo));
								}

							} else
							{
								labelCommand.setText("Cannot connect to destination.");
							}

						} else
						{
							labelCommand.setText("Cannot connect to source.");
						}
						hstFrom.disconnectAll();
						hstTo.disconnectAll();
					} else
					{
						labelCommand.setText("Cannot clone to self.");
					}
				}
			});

			jButtonClose = new JButton4j(Common.icon_close);
			desktopPane.add(jButtonClose);
			jButtonClose.setText("Close");
			jButtonClose.setBounds(295, 444, 160, 36);
			jButtonClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					logger.debug("JFrameCloneDB closed");
					dispose();
				}
			});

			progressBar.setBounds(0, 490, 573, 28);
			progressBar.setBackground(Color.WHITE);
			progressBar.setForeground(Color.BLUE);
			desktopPane.add(progressBar);

			labelCommand.setBounds(0, 522, 573, 23);
			labelCommand.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			desktopPane.add(labelCommand);

			JLabel4j_std label4j_std = new JLabel4j_std();
			label4j_std.setForeground(Color.BLACK);
			label4j_std.setFont(new Font("Arial", Font.PLAIN, 16));
			label4j_std.setText("Source");
			label4j_std.setHorizontalTextPosition(SwingConstants.LEFT);
			label4j_std.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std.setBounds(20, 15, 258, 21);
			desktopPane.add(label4j_std);

			JLabel4j_std label4j_std_1 = new JLabel4j_std();
			label4j_std_1.setForeground(Color.BLACK);
			label4j_std_1.setText("Destination");
			label4j_std_1.setHorizontalTextPosition(SwingConstants.LEFT);
			label4j_std_1.setHorizontalAlignment(SwingConstants.CENTER);
			label4j_std_1.setFont(new Font("Arial", Font.PLAIN, 16));
			label4j_std_1.setBounds(295, 15, 248, 21);
			desktopPane.add(label4j_std_1);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
