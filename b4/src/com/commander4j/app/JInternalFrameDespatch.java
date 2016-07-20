package com.commander4j.app;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialBatch;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBQuery;
import com.commander4j.db.JDBUser;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JLabel4j_title;
import com.commander4j.gui.JList4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameDespatch extends JInternalFrame
{
	private static final long serialVersionUID = 1;
	private JButton4j jButtonLookupHaulier;
	private JButton4j jButtonLookupTrailer;
	private JTextField4j textFieldHaulier;
	private JTextField4j textFieldTrailer;
	private ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JButton4j jButtonLookupBatch;
	private JButton4j jButtonLookupMaterial;
	private JButton4j jButtonLookupLocationTo;
	private JButton4j jButtonLookupLocationFrom;
	private JButton4j jButtonLookupLoadNo;
	private JTextField4j jTextFieldDespatchDate;
	private JTextField4j textFieldNoOfPallets;
	private JTextField4j textFieldDespatchLocationTo;
	private JTextField4j textFieldDespatchStatus;
	private JTextField4j textFieldDespatchLocationFrom;
	private JLabel4j_std jLabel10_1;
	private JSpinner spinnerUnassignedLimit;
	private JCheckBox4j jCheckBoxLimit;
	private JLabel4j_std jLabel10;
	private JTextField4j textFieldBatch;
	private JTextField4j textFieldMaterial;
	private JTextField4j textFieldSSCC;
	private JComboBox<String> comboBoxPalletStatus;
	private JList4j<String> list_assigned;
	private JList4j<String> list_unassigned;
	private JList4j<JDBDespatch> list_despatch;
	private JSpinner spinnerDespatchLimit = new JSpinner();
	private JDBDespatch despatch = new JDBDespatch(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JButton4j confirmButton = new JButton4j(Common.icon_ok);
	private JButton4j deleteButton = new JButton4j(Common.icon_delete);
	private JButton4j buttonUnAssign = new JButton4j(Common.icon_arrow_right);
	private JButton4j buttonAssign = new JButton4j(Common.icon_arrow_left);
	private String currentdespatchno = "";
	private String previousdespatchno = "";
	private LinkedList<JDBDespatch> despList = new LinkedList<JDBDespatch>();
	private LinkedList<String> unassignedList = new LinkedList<String>();
	private LinkedList<String> assignedList = new LinkedList<String>();
	private JRadioButton confirmedRadioButton = new JRadioButton();
	private JRadioButton unconfirmedRadioButton = new JRadioButton();
	private JButton4j findButton = new JButton4j(Common.icon_find);
	private JButton4j jButtonHelp = new JButton4j(Common.icon_help);
	private JButton4j newButton = new JButton4j(Common.icon_add);
	private JLabel4j_std jStatusText;
	private JTextField4j textFieldLoadNo;
	private JTextField4j textFieldUserID;
	private JButton4j buttonSetUserID;
	private JTextField4j textFieldDespatchNo;
	private String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
	private PreparedStatement listStatement;
	private JButton4j jButtonLookupJourneyRef;
	private JTextField4j textFieldJourneyRef;

	public JInternalFrameDespatch()
	{
		super();
		setIconifiable(true);
		setClosable(true);
		setTitle("Despatch");
		setVisible(true);
		getContentPane().setLayout(null);
		setBounds(100, 100, 856, 519);

		final JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Common.color_app_window);
		desktopPane.setBounds(0, 0, 833, 453);
		getContentPane().add(desktopPane);
		desktopPane.setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 30, 150, 290);
		desktopPane.add(scrollPane);

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();
		query.addText(JUtility.substSchemaName(schemaName, "select * from {schema}APP_PALLET where 1=2"));
		query.bindParams();
		listStatement = query.getPreparedStatement();

		list_despatch = new JList4j<JDBDespatch>();
		list_despatch.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (list_despatch.getModel().getSize() > 0)
				{
					if (list_despatch.getSelectedIndex() >= 0)
					{
						JDBDespatch d = list_despatch.getSelectedValue();
						currentdespatchno = d.getDespatchNo();

						if (currentdespatchno.equals(previousdespatchno) == false)
						{
							previousdespatchno = currentdespatchno;
							textFieldDespatchLocationFrom.setText(d.getLocationIDFrom());
							textFieldDespatchLocationTo.setText(d.getLocationIDTo());
							textFieldDespatchStatus.setText(d.getStatus());

							if (d.getStatus().equals("Confirmed"))
							{
								deleteButton.setEnabled(false);
								jButtonLookupLocationTo.setEnabled(false);
								jButtonLookupLocationFrom.setEnabled(false);
								buttonSetUserID.setEnabled(false);
								jButtonLookupLoadNo.setEnabled(false);
								jButtonLookupTrailer.setEnabled(false);
								jButtonLookupHaulier.setEnabled(false);
								buttonUnAssign.setEnabled(false);
								buttonAssign.setEnabled(false);
								newButton.setEnabled(false);
								jButtonLookupJourneyRef.setEnabled(false);
							}
							else
							{
								deleteButton.setEnabled(true);
								jButtonLookupLocationFrom.setEnabled(true);
								buttonSetUserID.setEnabled(true);
								jButtonLookupLocationTo.setEnabled(true);
								jButtonLookupLoadNo.setEnabled(true);
								jButtonLookupTrailer.setEnabled(true);
								jButtonLookupHaulier.setEnabled(true);
								buttonUnAssign.setEnabled(true);
								buttonAssign.setEnabled(true);
								newButton.setEnabled(true);
								if (d.isJourneyRefReqd().equals("Y"))
								{
									jButtonLookupJourneyRef.setEnabled(true);
								}
								else
								{
									jButtonLookupJourneyRef.setEnabled(false);
									d.setJourneyRef("");
								}
							}

							try
							{
								jTextFieldDespatchDate.setText(d.getDespatchDate().toString().substring(0, 19));
							}
							catch (Exception ee)
							{
								jTextFieldDespatchDate.setText("N/A");
							}

							textFieldHaulier.setText(d.getHaulier());
							textFieldTrailer.setText(d.getTrailer());
							textFieldLoadNo.setText(d.getLoadNo());
							textFieldUserID.setText(d.getUserID());
							textFieldJourneyRef.setText(d.getJourneyRef());

							textFieldNoOfPallets.setText(Integer.toString(d.getTotalPallets()));
							setConfirmButtonStatus();
							setFindButtonStatus(d.getLocationIDFrom());

							populateAssignedList(d.getDespatchNo(), "");
							clearUnAssignedList();
						}
					}
					else
					{
						blankDespatchFields();
					}
				}
				else
				{
					jButtonLookupLocationFrom.setEnabled(false);
					buttonSetUserID.setEnabled(false);
					jButtonLookupLocationTo.setEnabled(false);
					jButtonLookupTrailer.setEnabled(false);
					jButtonLookupHaulier.setEnabled(false);
					buttonUnAssign.setEnabled(false);
					buttonAssign.setEnabled(false);
					populateAssignedList("", "");
				}
			}
		});
		list_despatch.setCellRenderer(Common.renderer_list);
		list_despatch.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list_despatch);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(510, 30, 150, 375);
		desktopPane.add(scrollPane_1);

		list_unassigned = new JList4j<String>();
		list_unassigned.setLocation(0, -3);
		list_unassigned.setCellRenderer(Common.renderer_list_unassigned);
		list_unassigned.setBackground(Common.color_list_unassigned);
		scrollPane_1.setViewportView(list_unassigned);

		final JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(310, 30, 150, 375);
		desktopPane.add(scrollPane_2);

		list_assigned = new JList4j<String>();
		list_assigned.setCellRenderer(Common.renderer_list_assigned);
		list_assigned.setBackground(Common.color_list_assigned);
		scrollPane_2.setViewportView(list_assigned);

		comboBoxPalletStatus = new JComboBox<String>();

		comboBoxPalletStatus.setFont(Common.font_std);
		comboBoxPalletStatus.setBounds(672, 135, 143, 21);

		ComboBoxModel<String> jComboBoxDefaultPalletStatusModel = new DefaultComboBoxModel<String>(Common.palletStatusIncBlank);
		comboBoxPalletStatus.setModel(jComboBoxDefaultPalletStatusModel);
		comboBoxPalletStatus.setSelectedIndex(0);
		desktopPane.add(comboBoxPalletStatus);

		textFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
		textFieldSSCC.setBounds(672, 50, 143, 21);
		desktopPane.add(textFieldSSCC);

		final JLabel4j_std palletStatusLabel = new JLabel4j_std();
		palletStatusLabel.setText(lang.get("lbl_Pallet_Status"));
		palletStatusLabel.setBounds(672, 115, 128, 20);
		desktopPane.add(palletStatusLabel);

		final JLabel4j_std ssccLabel = new JLabel4j_std();
		ssccLabel.setText(lang.get("lbl_Pallet_SSCC"));
		ssccLabel.setBounds(672, 35, 128, 16);
		desktopPane.add(ssccLabel);

		textFieldMaterial = new JTextField4j(JDBMaterial.field_material);
		textFieldMaterial.setBounds(672, 90, 124, 21);
		desktopPane.add(textFieldMaterial);

		final JLabel4j_std materialLabel = new JLabel4j_std();
		materialLabel.setText(lang.get("lbl_Material"));
		materialLabel.setBounds(672, 75, 128, 16);
		desktopPane.add(materialLabel);

		textFieldBatch = new JTextField4j(JDBMaterialBatch.field_batch_number);
		textFieldBatch.setBounds(672, 175, 124, 21);
		desktopPane.add(textFieldBatch);

		final JLabel4j_std batchLabel = new JLabel4j_std();
		batchLabel.setText(lang.get("lbl_Batch"));
		batchLabel.setBounds(672, 160, 128, 16);
		desktopPane.add(batchLabel);
		buttonAssign.setEnabled(false);

		buttonAssign.setBounds(470, 150, 25, 25);
		buttonAssign.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (textFieldDespatchLocationTo.getText().equals(""))
				{
					jStatusText.setText("Please define Destination Location");
				}
				else
				{
					jStatusText.setText("");
					if (list_unassigned.getSelectedIndex() > -1)
					{
						String item = "";
						JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();

						for (int j = list_unassigned.getMaxSelectionIndex(); j >= list_unassigned.getMinSelectionIndex(); j--)
						{
							if (list_unassigned.isSelectedIndex(j))
							{
								item = (String) list_unassigned.getModel().getElementAt(j);

								if (d.assignSSCC(item))
								{
									addtoList(assignedList, item);
									removefromList(unassignedList, item);
									jStatusText.setText("");
								}
								else
								{
									jStatusText.setText(d.getErrorMessage());
								}
							}
						}

						list_assigned.setModel(addListtoModel(assignedList));
						list_unassigned.setModel(addListtoModel(unassignedList));
						textFieldNoOfPallets.setText(String.valueOf(assignedList.size()));

						d.setTotalPallets(assignedList.size());
						updateDespatch(d);
					}
				}
			}
		});
		desktopPane.add(buttonAssign);
		buttonUnAssign.setEnabled(false);

		buttonUnAssign.setBounds(470, 185, 25, 25);
		buttonUnAssign.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (list_assigned.getSelectedIndex() > -1)
				{
					String item = "";
					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();

					for (int j = list_assigned.getMaxSelectionIndex(); j >= list_assigned.getMinSelectionIndex(); j--)
					{
						if (list_assigned.isSelectedIndex(j))
						{
							item = (String) list_assigned.getModel().getElementAt(j);

							if (d.unassignSSCC(item))
							{
								removefromList(assignedList, item);
								addtoList(unassignedList, item);
							}
						}
					}

					list_assigned.setModel(addListtoModel(assignedList));
					list_unassigned.setModel(addListtoModel(unassignedList));
					textFieldNoOfPallets.setText(String.valueOf(assignedList.size()));

					d.setTotalPallets(assignedList.size());
					updateDespatch(d);
				}
			}
		});
		desktopPane.add(buttonUnAssign);

		newButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Despatch_Create"), lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

				if (question == 0)
				{
					JDBDespatch d = new JDBDespatch(Common.selectedHostID, Common.sessionID);
					String number = "";
					number = d.generateNewDespatchNo();

					if (number.equals("") == false)
					{

						if (d.create())
						{
							d.updateUserID(number, Common.userList.getUser(Common.sessionID).getUserId());
							populateDespatchList(number);
							setConfirmButtonStatus();
						}
						else
						{
							JUtility.errorBeep();
							JOptionPane.showMessageDialog(Common.mainForm, d.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
						}
					}
					else
					{
						JUtility.errorBeep();
						JOptionPane.showMessageDialog(Common.mainForm, d.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		newButton.setText(lang.get("btn_New"));
		newButton.setBounds(6, 417, 116, 30);
		desktopPane.add(newButton);

		deleteButton.setText(lang.get("btn_Delete"));
		deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (list_despatch.getModel().getSize() > 0)
				{
					if (list_despatch.getSelectedIndex() >= 0)
					{
						JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();

						int question = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Despatch_Delete") + " " + d.getDespatchNo() + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

						if (question == 0)
						{
							d.delete();
							populateDespatchList("");
							setConfirmButtonStatus();
						}
					}
				}
			}
		});
		deleteButton.setBounds(242, 417, 116, 30);
		desktopPane.add(deleteButton);

		findButton.setText(lang.get("btn_Find"));
		findButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buildUnassignedSQL();
				populateUnassignedList(listStatement, "");
			}
		});
		findButton.setBounds(672, 298, 143, 30);
		desktopPane.add(findButton);

		final JButton4j printButton = new JButton4j(Common.icon_report);
		printButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (list_despatch.getModel().getSize() > 0)
				{
					if (list_despatch.getSelectedIndex() >= 0)
					{
						JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
						HashMap<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("p_despatch_no", d.getDespatchNo());
						JLaunchReport.runReport("RPT_DESPATCH", parameters, "", null, "");
					}
				}
			}
		});
		printButton.setText(lang.get("btn_Print"));
		printButton.setBounds(478, 417, 116, 30);
		desktopPane.add(printButton);

		jLabel10 = new JLabel4j_std();
		jLabel10.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		jLabel10.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel10.setText(lang.get("lbl_Limit"));
		jLabel10.setBounds(672, 244, 128, 21);
		desktopPane.add(jLabel10);

		jCheckBoxLimit = new JCheckBox4j();
		jCheckBoxLimit.setSelected(true);
		jCheckBoxLimit.setBackground(new Color(255, 255, 255));
		jCheckBoxLimit.setBounds(672, 264, 21, 21);
		desktopPane.add(jCheckBoxLimit);

		spinnerUnassignedLimit = new JSpinner();
		JSpinner.NumberEditor ne1 = new JSpinner.NumberEditor(spinnerUnassignedLimit);
		ne1.getTextField().setFont(Common.font_std);
		spinnerUnassignedLimit.setEditor(ne1);
		spinnerUnassignedLimit.setBounds(701, 264, 114, 20);
		spinnerUnassignedLimit.setValue(1000);
		desktopPane.add(spinnerUnassignedLimit);

		final JButton4j closeButton = new JButton4j(Common.icon_close);

		closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		closeButton.setText(lang.get("btn_Close"));
		closeButton.setMnemonic(lang.getMnemonicChar());
		closeButton.setBounds(714, 417, 116, 30);
		desktopPane.add(closeButton);

		jButtonHelp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		jButtonHelp.setText(lang.get("btn_Help"));
		jButtonHelp.setBounds(596, 417, 116, 30);
		desktopPane.add(jButtonHelp);

		//
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_DESPATCH"));

		confirmButton.setText(lang.get("btn_Confirm"));
		confirmButton.setEnabled(false);
		confirmButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (list_despatch.getModel().getSize() > 0)
				{
					if (list_despatch.getSelectedIndex() >= 0)
					{
						JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();

						int n = JOptionPane.showConfirmDialog(Common.mainForm, lang.get("dlg_Despatch_Confirm") + " " + d.getDespatchNo() + " ?", lang.get("dlg_Confirm"), JOptionPane.YES_NO_OPTION, 0, Common.icon_confirm);

						if (n == 0)
						{
							d.getDespatchProperties();
							if (d.confirm())
							{
								populateDespatchList("");
								setConfirmButtonStatus();
							}
							else
							{
								refresh();
								JUtility.errorBeep();
								JOptionPane.showMessageDialog(Common.mainForm, d.getErrorMessage(), lang.get("dlg_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);

							}
						}
					}
				}
			}
		});

		confirmButton.setBounds(360, 417, 116, 30);
		desktopPane.add(confirmButton);

		final JLabel4j_title despatchesLabel = new JLabel4j_title();
		despatchesLabel.setText(lang.get("lbl_Despatches"));
		despatchesLabel.setBounds(10, 10, 75, 16);
		desktopPane.add(despatchesLabel);

		final JLabel4j_title unassignedLabel = new JLabel4j_title();
		unassignedLabel.setText(lang.get("lbl_Unassigned"));
		unassignedLabel.setBounds(510, 10, 150, 16);
		desktopPane.add(unassignedLabel);

		final JLabel4j_title assignedLabel = new JLabel4j_title();
		assignedLabel.setText(lang.get("lbl_Assigned"));
		assignedLabel.setBounds(310, 10, 150, 16);
		desktopPane.add(assignedLabel);

		final JLabel4j_title palletFilterCriteriaLabel = new JLabel4j_title();
		palletFilterCriteriaLabel.setText(lang.get("lbl_Unassigned_Filter"));
		palletFilterCriteriaLabel.setBounds(672, 15, 125, 16);
		desktopPane.add(palletFilterCriteriaLabel);

		final JButton4j refreshButton = new JButton4j(Common.icon_refresh);
		refreshButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				refresh();
			}
		});
		refreshButton.setText(lang.get("btn_Refresh"));
		refreshButton.setBounds(124, 417, 116, 30);
		desktopPane.add(refreshButton);

		SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
		jSpinnerIntModel.setMinimum(1);
		jSpinnerIntModel.setMaximum(50000);
		jSpinnerIntModel.setStepSize(1);
		JSpinner.NumberEditor ne2 = new JSpinner.NumberEditor(spinnerDespatchLimit);
		ne2.getTextField().setFont(Common.font_std);
		spinnerDespatchLimit.setEditor(ne2);
		spinnerDespatchLimit.setModel(jSpinnerIntModel);
		spinnerDespatchLimit.setBounds(100, 332, 60, 20);
		spinnerDespatchLimit.setValue(50);
		desktopPane.add(spinnerDespatchLimit);

		jLabel10_1 = new JLabel4j_std();
		jLabel10_1.setHorizontalAlignment(SwingConstants.TRAILING);
		jLabel10_1.setText(lang.get("lbl_Limit"));
		jLabel10_1.setBounds(15, 331, 70, 21);
		desktopPane.add(jLabel10_1);

		textFieldDespatchLocationFrom = new JTextField4j(JDBLocation.field_location_id);
		textFieldDespatchLocationFrom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (JUtility.isNullORBlank(textFieldDespatchLocationFrom.getText()) == true)
				{
					findButton.setEnabled(false);
				}
				else
				{
					findButton.setEnabled(true);
				}
			}
		});
		textFieldDespatchLocationFrom.setEditable(false);
		textFieldDespatchLocationFrom.setBounds(172, 30, 105, 20);
		desktopPane.add(textFieldDespatchLocationFrom);

		final JLabel4j_std locationLabel_1 = new JLabel4j_std();
		locationLabel_1.setText(lang.get("lbl_From_Location"));
		locationLabel_1.setBounds(172, 12, 100, 16);
		desktopPane.add(locationLabel_1);

		final JLabel4j_std palletStatusLabel_1 = new JLabel4j_std();
		palletStatusLabel_1.setText(lang.get("lbl_Despatch_Status"));
		palletStatusLabel_1.setBounds(172, 268, 128, 20);
		desktopPane.add(palletStatusLabel_1);

		textFieldDespatchStatus = new JTextField4j(JDBDespatch.field_status);
		textFieldDespatchStatus.setEditable(false);
		textFieldDespatchStatus.setBounds(172, 290, 128, 20);
		desktopPane.add(textFieldDespatchStatus);

		textFieldDespatchLocationTo = new JTextField4j(JDBLocation.field_location_id);
		textFieldDespatchLocationTo.setEditable(false);
		textFieldDespatchLocationTo.setBounds(172, 73, 105, 20);
		desktopPane.add(textFieldDespatchLocationTo);

		final JLabel4j_std locationLabel_1_1 = new JLabel4j_std();
		locationLabel_1_1.setText(lang.get("lbl_To_Location"));
		locationLabel_1_1.setBounds(172, 54, 100, 16);
		desktopPane.add(locationLabel_1_1);

		final JLabel4j_std palletStatusLabel_1_1 = new JLabel4j_std();
		palletStatusLabel_1_1.setText(lang.get("lbl_Despatch_Date"));
		palletStatusLabel_1_1.setBounds(172, 312, 125, 20);
		desktopPane.add(palletStatusLabel_1_1);

		final JLabel4j_std palletStatusLabel_1_1_1 = new JLabel4j_std();
		palletStatusLabel_1_1_1.setText(lang.get("lbl_No_Of_Pallets"));
		palletStatusLabel_1_1_1.setBounds(172, 358, 120, 20);
		desktopPane.add(palletStatusLabel_1_1_1);

		textFieldNoOfPallets = new JTextField4j();
		textFieldNoOfPallets.setEditable(false);
		textFieldNoOfPallets.setBounds(172, 382, 60, 20);
		desktopPane.add(textFieldNoOfPallets);

		jTextFieldDespatchDate = new JTextField4j();
		jTextFieldDespatchDate.setEditable(false);
		jTextFieldDespatchDate.setBounds(172, 335, 125, 20);
		desktopPane.add(jTextFieldDespatchDate);

		jButtonLookupLocationFrom = new JButton4j(Common.icon_lookup);
		jButtonLookupLocationFrom.setEnabled(false);
		jButtonLookupLocationFrom.setBounds(277, 29, 21, 21);
		jButtonLookupLocationFrom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "Y";

				if (JLaunchLookup.locations())
				{
					textFieldDespatchLocationFrom.setText(JLaunchLookup.dlgResult);

					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.setLocationIDFrom(JLaunchLookup.dlgResult);
					updateDespatch(d);
				}
			}
		});
		desktopPane.add(jButtonLookupLocationFrom);

		jButtonLookupLocationTo = new JButton4j(Common.icon_lookup);
		jButtonLookupLocationTo.setEnabled(false);
		jButtonLookupLocationTo.setBounds(277, 72, 21, 21);
		jButtonLookupLocationTo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "Y";

				if (JLaunchLookup.locations())
				{
					textFieldDespatchLocationTo.setText(JLaunchLookup.dlgResult);

					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.setLocationIDTo(JLaunchLookup.dlgResult);
					textFieldJourneyRef.setText(d.getJourneyRef());
					if (d.isJourneyRefReqd().equals("Y"))
					{
						jButtonLookupJourneyRef.setEnabled(true);
					}
					else
					{
						jButtonLookupJourneyRef.setEnabled(false);
					}
					updateDespatch(d);
				}
			}
		});
		desktopPane.add(jButtonLookupLocationTo);

		jButtonLookupMaterial = new JButton4j(Common.icon_lookup);
		jButtonLookupMaterial.setBounds(795, 90, 21, 21);
		jButtonLookupMaterial.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgAutoExec = false;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.materials())
				{
					textFieldMaterial.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		desktopPane.add(jButtonLookupMaterial);

		jButtonLookupBatch = new JButton4j(Common.icon_lookup);
		jButtonLookupBatch.setBounds(795, 175, 21, 21);
		jButtonLookupBatch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				JLaunchLookup.dlgCriteriaDefault = textFieldMaterial.getText();
				JLaunchLookup.dlgAutoExec = true;

				if (JLaunchLookup.materialBatches())
				{
					textFieldBatch.setText(JLaunchLookup.dlgResult);
				}
			}
		});
		desktopPane.add(jButtonLookupBatch);

		confirmedRadioButton.setBackground(Common.color_app_window);
		confirmedRadioButton.setText(lang.get("lbl_Confirmed"));
		confirmedRadioButton.setBounds(10, 356, 129, 24);
		confirmedRadioButton.setFont(Common.font_std);
		confirmedRadioButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				com.commander4j.util.JWait.milliSec(100);
				populateDespatchList("");
				refresh();
			}
		});

		desktopPane.add(confirmedRadioButton);

		unconfirmedRadioButton.setBackground(Common.color_app_window);
		unconfirmedRadioButton.setText(lang.get("lbl_Unconfirmed"));
		unconfirmedRadioButton.setBounds(10, 381, 129, 24);
		unconfirmedRadioButton.setSelected(true);
		unconfirmedRadioButton.setFont(Common.font_std);
		unconfirmedRadioButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				com.commander4j.util.JWait.milliSec(100);
				populateDespatchList("");
				refresh();
				newButton.setEnabled(true);
			}
		});
		desktopPane.add(unconfirmedRadioButton);

		buttonGroup_1.add(unconfirmedRadioButton);
		buttonGroup_1.add(confirmedRadioButton);

		final JLabel4j_std palletStatusLabel_1_2 = new JLabel4j_std();
		palletStatusLabel_1_2.setText(lang.get("lbl_Trailer"));
		palletStatusLabel_1_2.setBounds(172, 93, 105, 20);
		desktopPane.add(palletStatusLabel_1_2);

		textFieldTrailer = new JTextField4j(JDBDespatch.field_trailer);
		textFieldTrailer.setEditable(false);
		textFieldTrailer.setBounds(172, 116, 105, 20);

		desktopPane.add(textFieldTrailer);

		final JLabel4j_std palletStatusLabel_1_2_1 = new JLabel4j_std();
		palletStatusLabel_1_2_1.setText(lang.get("lbl_Haulier"));
		palletStatusLabel_1_2_1.setBounds(172, 137, 105, 20);
		desktopPane.add(palletStatusLabel_1_2_1);

		textFieldHaulier = new JTextField4j(JDBDespatch.field_haulier);
		textFieldHaulier.setEditable(false);
		textFieldHaulier.setBounds(172, 160, 105, 20);
		desktopPane.add(textFieldHaulier);

		jButtonLookupTrailer = new JButton4j();
		jButtonLookupTrailer.setEnabled(false);
		jButtonLookupTrailer.setText("...");
		jButtonLookupTrailer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String trailer;
				trailer = JOptionPane.showInputDialog(Common.mainForm, "Enter Trailer Reference");

				if (trailer != null)
				{
					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.setTrailer(trailer.toUpperCase());
					textFieldTrailer.setText(trailer.toUpperCase());
					updateDespatch(d);
				}
			}
		});
		jButtonLookupTrailer.setBounds(277, 115, 21, 21);
		desktopPane.add(jButtonLookupTrailer);

		jButtonLookupHaulier = new JButton4j();
		jButtonLookupHaulier.setEnabled(false);
		jButtonLookupHaulier.setText("...");
		jButtonLookupHaulier.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String haulier;
				haulier = JOptionPane.showInputDialog(Common.mainForm, "Enter Haulier Reference");

				if (haulier != null)
				{
					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.setHaulier(haulier.toUpperCase());
					textFieldHaulier.setText(haulier.toUpperCase());
					updateDespatch(d);
				}
			}
		});
		jButtonLookupHaulier.setBounds(277, 159, 21, 21);
		desktopPane.add(jButtonLookupHaulier);

		JLabel4j_std lblLoadNo = new JLabel4j_std();
		lblLoadNo.setText(lang.get("lbl_Load_No"));
		lblLoadNo.setBounds(172, 180, 105, 20);
		desktopPane.add(lblLoadNo);

		textFieldLoadNo = new JTextField4j(JDBDespatch.field_load_no);
		textFieldLoadNo.setText("");
		textFieldLoadNo.setEditable(false);
		textFieldLoadNo.setBounds(172, 203, 105, 20);
		desktopPane.add(textFieldLoadNo);

		jButtonLookupLoadNo = new JButton4j();
		jButtonLookupLoadNo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String load;
				load = JOptionPane.showInputDialog(Common.mainForm, "Enter Load Number");

				if (load != null)
				{
					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.setLoadNo(load.toUpperCase());
					textFieldLoadNo.setText(load.toUpperCase());
					updateDespatch(d);

				}

			}
		});
		jButtonLookupLoadNo.setText("...");
		jButtonLookupLoadNo.setEnabled(false);
		jButtonLookupLoadNo.setBounds(277, 202, 21, 21);
		desktopPane.add(jButtonLookupLoadNo);

		JLabel4j_std label = new JLabel4j_std();
		label.setText(lang.get("lbl_User_ID"));
		label.setBounds(672, 338, 128, 16);
		desktopPane.add(label);

		textFieldUserID = new JTextField4j(JDBUser.field_user_id);
		textFieldUserID.setText("");
		textFieldUserID.setEditable(false);
		textFieldUserID.setBounds(672, 356, 122, 20);
		desktopPane.add(textFieldUserID);

		buttonSetUserID = new JButton4j((Icon) null);
		buttonSetUserID.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "";

				if (JLaunchLookup.users())
				{
					textFieldUserID.setText(JLaunchLookup.dlgResult);

					JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
					d.updateUserID(d.getDespatchNo(), JLaunchLookup.dlgResult);

				}
			}
		});
		buttonSetUserID.setEnabled(false);
		buttonSetUserID.setBounds(795, 355, 21, 21);
		desktopPane.add(buttonSetUserID);

		textFieldDespatchNo = new JTextField4j(JDBDespatch.field_despatch_no);
		textFieldDespatchNo.setBounds(672, 215, 143, 21);
		desktopPane.add(textFieldDespatchNo);

		JLabel4j_std despatchNolabel = new JLabel4j_std();
		despatchNolabel.setText(lang.get("lbl_Despatch_No"));
		despatchNolabel.setBounds(672, 200, 143, 16);
		desktopPane.add(despatchNolabel);
		
		JLabel4j_std label4j_std = new JLabel4j_std();
		label4j_std.setText(lang.get("lbl_Journey_Ref"));
		label4j_std.setBounds(172, 225, 105, 20);
		desktopPane.add(label4j_std);
		
		textFieldJourneyRef = new JTextField4j(20);
		textFieldJourneyRef.setText("");
		textFieldJourneyRef.setEditable(false);
		textFieldJourneyRef.setBounds(172, 246, 105, 20);
		desktopPane.add(textFieldJourneyRef);
		
		jButtonLookupJourneyRef = new JButton4j();
		jButtonLookupJourneyRef.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLaunchLookup.dlgAutoExec = true;
				JLaunchLookup.dlgCriteriaDefault = "Y";
				
				JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
				
				if (JLaunchLookup.journeys())
				{
					textFieldJourneyRef.setText(JLaunchLookup.dlgResult);

					d.setJourneyRef(JLaunchLookup.dlgResult);
					updateDespatch(d);
					
				}
			}
		});
		jButtonLookupJourneyRef.setText("...");
		jButtonLookupJourneyRef.setEnabled(false);
		jButtonLookupJourneyRef.setBounds(277, 245, 21, 21);
		desktopPane.add(jButtonLookupJourneyRef);
		
				jStatusText = new JLabel4j_std("");
				jStatusText.setBounds(0, 460, 830, 21);
				getContentPane().add(jStatusText);
				jStatusText.setForeground(new java.awt.Color(255, 0, 0));
				jStatusText.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		populateDespatchList("");
	}

	public DefaultComboBoxModel<String> addListtoModel(LinkedList<String> list)
	{
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		model.removeAllElements();

		int s = list.size();

		for (int j = 0; j < s; j++)
		{
			model.addElement(list.get(j));
		}

		return model;
	}

	public void addtoList(LinkedList<String> List, String newValue)
	{
		List.add(newValue);
		Collections.sort(List);
	}

	private void refresh()
	{
		if (list_despatch.getModel().getSize() > 0)
		{
			if (list_despatch.getSelectedIndex() >= 0)
			{
				JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();
				previousdespatchno = "";
				populateDespatchList(d.getDespatchNo());
			}
		}
	}
	
	private void blankDespatchFields()
	{
		textFieldDespatchLocationFrom.setText("");
		textFieldDespatchLocationTo.setText("");
		textFieldDespatchStatus.setText("");
		jTextFieldDespatchDate.setText("");
		textFieldNoOfPallets.setText("");
		textFieldHaulier.setText("");
		textFieldTrailer.setText("");
		textFieldLoadNo.setText("");
		textFieldUserID.setText("");
		jStatusText.setText("");
		textFieldJourneyRef.setText("");
	}

	private void buildUnassignedSQL()
	{

		try
		{
			listStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		JDBQuery q = new JDBQuery(Common.selectedHostID, Common.sessionID);
		String temp = "";
		q.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.select");

		q.addText(temp);

		if (list_despatch.getModel().getSize() > 0)
		{
			if (list_despatch.getSelectedIndex() >= 0)
			{
				JDBDespatch d = (JDBDespatch) list_despatch.getSelectedValue();

				q.addParamtoSQL("despatch_no <> ", d.getDespatchNo());

			}
		}

		if (textFieldDespatchNo.getText().equals("") == false)
		{
			q.addParamtoSQL("despatch_No = ", textFieldDespatchNo.getText());
		}

		if (textFieldDespatchLocationFrom.getText().equals("") == false)
		{
			q.addParamtoSQL("location_id = ", textFieldDespatchLocationFrom.getText());
		}

		if (textFieldSSCC.getText().equals("") == false)
		{
			q.addParamtoSQL("sscc like ", textFieldSSCC.getText());
		}

		if (textFieldMaterial.getText().equals("") == false)
		{
			q.addParamtoSQL("material like ", textFieldMaterial.getText());
		}

		if (textFieldBatch.getText().equals("") == false)
		{
			q.addParamtoSQL("batch_number like ", textFieldBatch.getText());
		}

		q.addParamtoSQL("Confirmed = ", "Y");

		q.addParamtoSQL("status=", ((String) comboBoxPalletStatus.getSelectedItem()).toString());


		q.appendSort("sscc", "asc");
		q.applyRestriction(jCheckBoxLimit.isSelected(), Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSelectLimit(), spinnerUnassignedLimit.getValue());
		q.bindParams();

		listStatement = q.getPreparedStatement();
	}

	private void clearUnAssignedList()
	{
		DefaultComboBoxModel<String> DefComboBoxMod = new DefaultComboBoxModel<String>();

		ListModel<String> jList1Model = DefComboBoxMod;

		list_unassigned.setModel(jList1Model);

		list_unassigned.setCellRenderer(Common.renderer_list);
		unassignedList.clear();
	}

	private void populateAssignedList(String despatchno, String defaultitem)
	{
		int counter = 0;

		DefaultComboBoxModel<String> DefComboBoxMod = new DefaultComboBoxModel<String>();

		JDBDespatch temp = new JDBDespatch(Common.selectedHostID, Common.sessionID);
		assignedList.clear();
		assignedList.addAll(temp.getAssignedSSCCs(despatchno));

		int sel = -1;

		if (despatchno.isEmpty() == false)
		{
			for (int j = 0; j < assignedList.size(); j++)
			{
				DefComboBoxMod.addElement(assignedList.get(j));
				counter++;

				if (assignedList.get(j).toString().equals(defaultitem))
				{
					sel = j;
				}
			}
		}

		ListModel<String> jList1Model = DefComboBoxMod;

		list_assigned.setModel(jList1Model);

		list_assigned.setCellRenderer(Common.renderer_list);
		list_assigned.setSelectedIndex(sel);
		list_assigned.ensureIndexIsVisible(sel);
		textFieldNoOfPallets.setText(String.valueOf(counter));
		jStatusText.setText("");
	}

	private void populateDespatchList(String defaultitem)
	{
		String status = "";
		blankDespatchFields();

		DefaultComboBoxModel<JDBDespatch> DefComboBoxMod = new DefaultComboBoxModel<JDBDespatch>();

		if (confirmedRadioButton.isSelected())
		{
			status = "Confirmed";
		}
		else
		{
			status = "Unconfirmed";
		}

		despList.clear();
		despList.addAll(despatch.browseDespatchData(status, Integer.valueOf(spinnerDespatchLimit.getModel().getValue().toString())));

		int sel = despList.size() - 1;

		for (int j = 0; j < despList.size(); j++)
		{
			DefComboBoxMod.addElement(despList.get(j));

			if (despList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<JDBDespatch> jList1Model = DefComboBoxMod;

		list_despatch.setModel(jList1Model);

		list_despatch.setCellRenderer(Common.renderer_list);
		list_despatch.setSelectedIndex(sel);
		list_despatch.ensureIndexIsVisible(sel);
	}

	private void populateUnassignedList(PreparedStatement criteria, String defaultitem)
	{
		JDBPallet pallet = new JDBPallet(Common.selectedHostID, Common.sessionID);

		DefaultComboBoxModel<String> DefComboBoxMod = new DefaultComboBoxModel<String>();

		unassignedList.clear();

		unassignedList.addAll(pallet.getPalletList(criteria));

		@SuppressWarnings("unused")
		int sel = unassignedList.size() - 1;

		for (int j = 0; j < unassignedList.size(); j++)
		{
			DefComboBoxMod.addElement(unassignedList.get(j));

			if (unassignedList.get(j).toString().equals(defaultitem))
			{
				sel = j;
			}
		}

		ListModel<String> jList1Model = DefComboBoxMod;

		list_unassigned.setModel(jList1Model);

		list_unassigned.setCellRenderer(Common.renderer_list);
		// list_unassigned.setSelectedIndex(sel);
		// list_unassigned.ensureIndexIsVisible(sel);
	}

	public void removefromList(LinkedList<String> List, String oldValue)
	{
		List.remove(List.indexOf(oldValue));
		Collections.sort(List);
	}

	public void setConfirmButtonStatus()
	{
		confirmButton.setEnabled(false);

		if ((textFieldDespatchLocationTo.getText().equals("") == false) | (textFieldDespatchLocationFrom.getText().equals("") == false))
		{
			if (unconfirmedRadioButton.isSelected() == true)
			{
				if (list_despatch.getModel().getSize() > 0)
				{
					if (list_despatch.getSelectedIndex() >= 0)
					{
						confirmButton.setEnabled(true);
					}
				}
			}
		}
	}

	public void setFindButtonStatus(String fromlocation)
	{
		if (JUtility.isNullORBlank(fromlocation) == true)
		{
			findButton.setEnabled(false);
		}
		else
		{
			findButton.setEnabled(true);
		}
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public boolean updateDespatch(JDBDespatch d)
	{
		boolean result = true;

		if (d.update() == false)
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(Common.mainForm, d.getErrorMessage(), lang.get("err_Error"), JOptionPane.ERROR_MESSAGE,Common.icon_confirm);
			result = false;
		}

		setConfirmButtonStatus();
		setFindButtonStatus(d.getLocationIDFrom());

		return result;
	}
}
