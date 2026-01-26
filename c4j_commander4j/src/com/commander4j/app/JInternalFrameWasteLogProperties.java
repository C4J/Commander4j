package com.commander4j.app;

/**
 * @author David Garratt
 *
 * Project Name : Commander4j
 *
 * Filename     : JInternalFrameMaterialBatchProperties.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.calendar.JCalendarButton;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.db.JDBUser;
import com.commander4j.db.JDBWasteContainer;
import com.commander4j.db.JDBWasteLocation;
import com.commander4j.db.JDBWasteLog;
import com.commander4j.db.JDBWasteMaterial;
import com.commander4j.db.JDBWasteReasons;
import com.commander4j.db.JDBWasteTransactionType;
import com.commander4j.db.JDBWasteTypes;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JDateControl;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_status;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JPanel4j;
import com.commander4j.gui.JQuantityInput;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchLookup;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JInternalFrameWasteLogProperties class allows you to update a record in
 * the APP_WASTE_LOG table and is called from the parent form
 * JInternalFrameWasteLogAdmin
 *
 * <p>
 * <img alt="" src="./doc-files/JInternalFrameWasteLogProperties.jpg" >
 *
 * @see com.commander4j.db.JDBWasteLog JDBWasteLog
 */
public class JInternalFrameWasteLogProperties extends JInternalFrame
{
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonLookupProcessOrder;
	private JButton4j jButtonLookupWasteContainer;
	private JButton4j jButtonLookupWasteLocation;
	private JButton4j jButtonLookupWasteMaterial;
	private JButton4j jButtonLookupWasteReason;
	private JButton4j jButtonNew;
	private JButton4j jButtonSave;
	private JButton4j jButtonUndo;
	private JCalendarButton button_CalendarTransaction;
	private JCheckBox4j chckbx_Hazard = new JCheckBox4j("");
	private JCheckBox4j chckbx_IncludeInTotals = new JCheckBox4j("");
	private JCheckBox4j chckbx_PO_Reqd = new JCheckBox4j("");
	private JCheckBox4j chckbx_PPEReqd = new JCheckBox4j("");
	private JCheckBox4j chckbx_Reason_Reqd = new JCheckBox4j("");
	private JCheckBox4j chckbx_Recycle = new JCheckBox4j("");
	private JCheckBox4j chckbx_StoreAsNegative = new JCheckBox4j("");
	private JDBLanguage lang;
	private JDBProcessOrder processOrder = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBWasteContainer wasteContainer = new JDBWasteContainer(Common.selectedHostID, Common.sessionID);
	private JDBWasteLocation wasteLocation = new JDBWasteLocation(Common.selectedHostID, Common.sessionID);
	private JDBWasteLog wasteLog = new JDBWasteLog(Common.selectedHostID, Common.sessionID);
	private JDBWasteMaterial wasteMaterial = new JDBWasteMaterial(Common.selectedHostID, Common.sessionID);
	private JDBWasteReasons wasteReason = new JDBWasteReasons(Common.selectedHostID, Common.sessionID);
	private JDBWasteTransactionType wasteTransactionTypes = new JDBWasteTransactionType(Common.selectedHostID, Common.sessionID);
	private JDBWasteTypes wasteType = new JDBWasteTypes(Common.selectedHostID, Common.sessionID);
	private JDateControl transactionDate;
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_status jStatusText;
	private JLabel4j_std jLabelComment;
	private JLabel4j_std jLabelProcessOrder;
	private JLabel4j_std jLabelQuantity;
	private JLabel4j_std jLabelTransactionDate;
	private JLabel4j_std jLabelUser;
	private JLabel4j_std jLabelWasteContainer;
	private JLabel4j_std jLabelWasteLocation;
	private JLabel4j_std jLabelWasteMaterial;
	private JLabel4j_std jLabelWasteReason;
	private JLabel4j_std jLabelWasteTransactionRef;
	private JLabel4j_std jLabelWasteTransactionType;
	private JLabel4j_std jLabel_WasteType;
	private JLabel4j_std jTextFieldUOM;
	private JPanel4j panel = new JPanel4j();
	private JQuantityInput jFormattedTextFieldQuantity;
	private JTextField4j jTextFieldContainerDescription;
	private JTextField4j jTextFieldLocationDescription;
	private JTextField4j jTextFieldMaterialDescription;
	private JTextField4j jTextFieldMaterialTypeDescription;
	private JTextField4j jTextFieldProcessOrder;
	private JTextField4j jTextFieldProcessOrderDescription;
	private JTextField4j jTextFieldReasonDescription;
	private JTextField4j jTextFieldTransactionRef;
	private JTextField4j jTextFieldTransactionType;
	private JTextField4j jTextFieldTypeID;
	private JTextField4j jTextFieldUserID;
	private JTextField4j jTextFieldWasteComment;
	private JTextField4j jTextFieldWasteContainer;
	private JTextField4j jTextFieldWasteLocation;
	private JTextField4j jTextFieldWasteMaterial;
	private JTextField4j jTextFieldWasteReason;
	private Long lref;
	private String ltype;
	private String mode = "";
	private static final long serialVersionUID = 1;

	public JInternalFrameWasteLogProperties()
	{
		super();
		lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_WASTE_LOG_EDIT"));

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle window = getBounds();
		setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldWasteLocation.requestFocus();
			}
		});
	}

	private void undo()
	{
		loadWasteLog();
		jButtonSave.setEnabled(false);
		jButtonUndo.setEnabled(false);
		jButtonNew.setEnabled(true);
	}

	private void addRecord()
	{
		mode = "NEW";
		ltype = "ADD";
		lref = Long.valueOf(-1);
		clear();
		jTextFieldTransactionRef.setText(mode);
		jButtonSave.setEnabled(false);
		jButtonUndo.setEnabled(false);
		jButtonNew.setEnabled(false);
	}

	private void clear()
	{
		jTextFieldWasteMaterial.setText("");
		jTextFieldMaterialDescription.setText("");
		jTextFieldTypeID.setText("");
		jTextFieldMaterialTypeDescription.setText("");
		jTextFieldWasteLocation.setText("");
		jTextFieldWasteComment.setText("");
		jTextFieldWasteContainer.setText("");
		jTextFieldLocationDescription.setText("");
		jTextFieldContainerDescription.setText("");
		jTextFieldWasteReason.setText("");
		jTextFieldReasonDescription.setText("");
		jTextFieldProcessOrder.setText("");
		jTextFieldProcessOrderDescription.setText("");
		jFormattedTextFieldQuantity.setValue(0);
		transactionDate.setDate(JUtility.getSQLDateTime());
		jTextFieldUOM.setText("");
		jTextFieldUserID.setText(Common.userList.getUser(Common.sessionID).getUserId());
		jTextFieldWasteLocation.requestFocus();

	}

	private void save()
	{

		boolean result = false;

		wasteLog.setTransactionType(ltype);
		wasteLog.setMaterialID(jTextFieldWasteMaterial.getText());
		wasteLog.setLocationID(jTextFieldWasteLocation.getText());
		wasteLog.setComment(jTextFieldWasteComment.getText());
		wasteLog.setContainerID(jTextFieldWasteContainer.getText());
		wasteLog.setReasonID(jTextFieldWasteReason.getText());
		wasteLog.setProcessOrder(jTextFieldProcessOrder.getText());
		wasteLog.setWasteReportTime(JUtility.getTimestampFromDate(transactionDate.getDate()));
		wasteLog.setWeightKg((jFormattedTextFieldQuantity.getQuantity()));

		if (mode.equals("EDIT"))
		{
			result = wasteLog.update();

			if (result == true)
			{
				jStatusText.setText("Updated log " + String.valueOf(wasteLog.getTransactionRef()));
			}
		}

		if (mode.equals("NEW"))
		{
			result = wasteLog.write();

			if (result == true)
			{
				lref = wasteLog.getTransactionRef();
				mode = "EDIT";
				jStatusText.setText("New log " + String.valueOf(wasteLog.getTransactionRef()) + " created.");
				jTextFieldTransactionRef.setText(String.valueOf(wasteLog.getTransactionRef()));
			}
		}

		if (result == true)
		{

			jButtonSave.setEnabled(false);
			jButtonUndo.setEnabled(false);
			jButtonNew.setEnabled(true);
		}
		else
		{
			jStatusText.setText(wasteLog.getErrorMessage());

		}
	}

	private boolean processOrderChanged()
	{
		boolean result = false;
		String ord = jTextFieldProcessOrder.getText();

		if (chckbx_PO_Reqd.isSelected())
		{

			result = processOrder.getProcessOrderProperties(ord);
			if (result)
			{
				jTextFieldProcessOrderDescription.setDisabledTextColor(Color.BLACK);
				jTextFieldProcessOrderDescription.setText(processOrder.getDescription());
				jButtonSave.setEnabled(true);
				jButtonUndo.setEnabled(true);
			}
			else
			{
				jTextFieldProcessOrderDescription.setDisabledTextColor(Color.RED);
				jTextFieldProcessOrderDescription.setText(processOrder.getErrorMessage());
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(true);
			}
			jButtonNew.setEnabled(false);
		}
		else
		{
			if (ord.equals("") == false)
			{
				jTextFieldProcessOrderDescription.setDisabledTextColor(Color.RED);
				jTextFieldProcessOrderDescription.setText("Process Order optional");
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(true);
			}
			else
			{
				jTextFieldProcessOrderDescription.setDisabledTextColor(Color.BLACK);
				jTextFieldProcessOrderDescription.setText("");
				jButtonSave.setEnabled(true);
				jButtonUndo.setEnabled(true);
			}
		}

		return result;
	}

	private boolean locationChanged()
	{
		boolean result = false;
		String loc = jTextFieldWasteLocation.getText();
		result = wasteLocation.getWasteLocationProperties(loc);
		if (result)
		{
			jTextFieldLocationDescription.setDisabledTextColor(Color.BLACK);
			jTextFieldLocationDescription.setText(wasteLocation.getDescription());
			chckbx_PO_Reqd.setSelected(wasteLocation.isProcessOrderRequired());
			chckbx_Reason_Reqd.setSelected(wasteLocation.isReasonIDRequired());
			jButtonSave.setEnabled(true);
			jButtonUndo.setEnabled(true);
		}
		else
		{
			jTextFieldLocationDescription.setDisabledTextColor(Color.RED);
			jTextFieldLocationDescription.setText(wasteLocation.getErrorMessage());
			chckbx_PO_Reqd.setSelected(false);
			chckbx_Reason_Reqd.setSelected(false);
			jButtonSave.setEnabled(false);
			jButtonUndo.setEnabled(true);
		}
		jButtonNew.setEnabled(false);

		return result;
	}

	private boolean containerChanged()
	{
		boolean result = false;
		String loc = jTextFieldWasteContainer.getText();
		result = wasteContainer.getWasteContainerProperties(loc);
		if (result)
		{
			jTextFieldContainerDescription.setDisabledTextColor(Color.BLACK);
			jTextFieldContainerDescription.setText(wasteContainer.getDescription());
			jButtonSave.setEnabled(true);
			jButtonUndo.setEnabled(true);
		}
		else
		{
			jTextFieldContainerDescription.setDisabledTextColor(Color.RED);
			jTextFieldContainerDescription.setText(wasteContainer.getErrorMessage());
			jButtonSave.setEnabled(false);
			jButtonUndo.setEnabled(true);
		}
		jButtonNew.setEnabled(false);

		return result;
	}

	private boolean materialChanged()
	{
		boolean result = false;
		String mat = jTextFieldWasteMaterial.getText();
		String matType = "";

		result = wasteMaterial.getWasteMaterialProperties(mat);

		if (result)
		{
			matType = wasteMaterial.getWasteTypeID();
			jTextFieldTypeID.setText(matType);
			jTextFieldMaterialDescription.setDisabledTextColor(Color.BLACK);
			jTextFieldMaterialDescription.setText(wasteMaterial.getDescription());

			if (wasteType.getWasteTypeProperties(matType))
			{
				jTextFieldMaterialTypeDescription.setText(wasteType.getDescription());

				chckbx_Recycle.setSelected(wasteType.isRecyclable());
				chckbx_Hazard.setSelected(wasteType.isHazardous());
				chckbx_PPEReqd.setSelected(wasteType.isPPEReqd());

				jButtonSave.setEnabled(true);
				jButtonUndo.setEnabled(true);
			}
			else
			{
				jTextFieldMaterialTypeDescription.setText(wasteType.getErrorMessage());

				chckbx_Recycle.setSelected(false);
				chckbx_Hazard.setSelected(false);
				chckbx_PPEReqd.setSelected(false);

				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(true);
			}
		}
		else
		{
			jTextFieldMaterialDescription.setDisabledTextColor(Color.RED);
			jTextFieldMaterialDescription.setText(wasteMaterial.getErrorMessage());

			jTextFieldMaterialTypeDescription.setText("");
			jTextFieldUOM.setText("");

			chckbx_Recycle.setSelected(false);
			chckbx_Hazard.setSelected(false);
			chckbx_PPEReqd.setSelected(false);

			jButtonSave.setEnabled(false);
			jButtonUndo.setEnabled(true);
		}
		jButtonNew.setEnabled(false);

		return result;
	}

	private boolean reasonChanged()
	{
		boolean result = false;
		String reas = jTextFieldWasteReason.getText();

		if (chckbx_Reason_Reqd.isSelected())
		{

			result = wasteReason.getWasteReasonProperties(reas);

			if (result)
			{
				jTextFieldReasonDescription.setDisabledTextColor(Color.BLACK);
				jTextFieldReasonDescription.setText(wasteReason.getDescription());
				jButtonSave.setEnabled(true);
				jButtonUndo.setEnabled(true);
			}
			else
			{
				jTextFieldReasonDescription.setDisabledTextColor(Color.RED);
				jTextFieldReasonDescription.setText(wasteReason.getErrorMessage());
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(true);
			}
			jButtonNew.setEnabled(false);
		}
		else
		{
			if (reas.equals("") == false)
			{
				jTextFieldReasonDescription.setDisabledTextColor(Color.RED);
				jTextFieldReasonDescription.setText("Reason optional");
				jButtonSave.setEnabled(false);
				jButtonUndo.setEnabled(true);
			}
			else
			{
				jTextFieldReasonDescription.setDisabledTextColor(Color.BLACK);
				jTextFieldReasonDescription.setText("");
				jButtonSave.setEnabled(true);
				jButtonUndo.setEnabled(true);
			}
		}

		return result;
	}

	public JInternalFrameWasteLogProperties(String ref, String txn)
	{
		this();
		lref = Long.valueOf(ref);
		ltype = txn;

		wasteTransactionTypes.getWasteTransactionProperties(ltype);

		chckbx_IncludeInTotals.setSelected(wasteTransactionTypes.isIncludedInTotals());
		chckbx_StoreAsNegative.setSelected(wasteTransactionTypes.isStoreAsNegative());

		if (lref == -1)
		{
			mode = "NEW";
			jTextFieldTransactionRef.setText("NEW");
			jTextFieldTransactionType.setText(ltype);
			jTextFieldUserID.setText(Common.userList.getUser(Common.sessionID).getUserId());
		}
		else
		{
			mode = "EDIT";
			loadWasteLog();
		}
	}

	private boolean loadWasteLog()
	{
		boolean result = false;

		jTextFieldTransactionRef.setText(String.valueOf(lref));
		jTextFieldTransactionType.setText(ltype);

		wasteLog.setTransactionRef(lref);
		wasteLog.setTransactionType(ltype);

		if (wasteLog.getWasteLogProperties())
		{
			jTextFieldTransactionRef.setText(String.valueOf(wasteLog.getTransactionRef()));
			jTextFieldTransactionType.setText(wasteLog.getTransactionType());

			jTextFieldWasteComment.setText(wasteLog.getComment());

			jTextFieldWasteLocation.setText(wasteLog.getLocationID());
			locationChanged();

			jTextFieldWasteContainer.setText(wasteLog.getContainerID());
			containerChanged();

			jTextFieldWasteMaterial.setText(wasteLog.getMaterialID());
			materialChanged();

			jTextFieldWasteReason.setText(wasteLog.getReasonID());
			reasonChanged();

			jTextFieldProcessOrder.setText(wasteLog.getProcessOrder());
			processOrderChanged();

			jTextFieldUserID.setText(wasteLog.getUserID());
			jFormattedTextFieldQuantity.setValue(wasteLog.getWeightKg());
			transactionDate.setDate(wasteLog.getWasteReportTime());

			jStatusText.setText("");
			result = true;

		}
		else
		{
			jStatusText.setText(wasteLog.getErrorMessage());
			result = false;
		}

		jButtonSave.setEnabled(false);
		jButtonUndo.setEnabled(false);
		jButtonNew.setEnabled(true);

		return result;
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(358, 207));
			this.setBounds(0, 0, 876, 401);
			setVisible(true);
			this.setClosable(true);

			jDesktopPane1 = new JDesktopPane4j();

			getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
			jDesktopPane1.setPreferredSize(new java.awt.Dimension(350, 182));
			jDesktopPane1.setLayout(null);

			panel.setBounds(637, 40, 216, 187);
			jDesktopPane1.add(panel);
			panel.setLayout(null);

			jButtonSave = new JButton4j(Common.icon_update_16x16);
			jButtonSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					save();
				}
			});
			jDesktopPane1.add(jButtonSave);
			jButtonSave.setEnabled(false);
			jButtonSave.setText(lang.get("btn_Save"));
			jButtonSave.setMnemonic(lang.getMnemonicChar());
			jButtonSave.setBounds(255, 296, 112, 32);

			jButtonNew = new JButton4j(Common.icon_add_16x16);
			jButtonNew.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					addRecord();
				}
			});
			jDesktopPane1.add(jButtonNew);
			jButtonNew.setEnabled(false);
			jButtonNew.setText(lang.get("btn_Add"));
			jButtonNew.setMnemonic(lang.getMnemonicChar());
			jButtonNew.setBounds(142, 296, 112, 32);

			jButtonUndo = new JButton4j(Common.icon_undo_16x16);
			jButtonUndo.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					undo();
				}
			});
			jDesktopPane1.add(jButtonUndo);
			jButtonUndo.setEnabled(false);
			jButtonUndo.setText(lang.get("btn_Undo"));
			jButtonUndo.setMnemonic(lang.getMnemonicChar());
			jButtonUndo.setBounds(368, 296, 112, 32);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(481, 296, 112, 32);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(594, 296, 112, 32);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabelWasteTransactionRef = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteTransactionRef);
			jLabelWasteTransactionRef.setText(lang.get("lbl_Transaction_Ref"));
			jLabelWasteTransactionRef.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelWasteTransactionRef.setBounds(0, 8, 130, 22);

			jTextFieldTransactionRef = new JTextField4j();
			jDesktopPane1.add(jTextFieldTransactionRef);
			jTextFieldTransactionRef.setBounds(142, 8, 101, 22);
			jTextFieldTransactionRef.setEnabled(false);
			jTextFieldTransactionRef.setEditable(false);

			jLabelWasteTransactionType = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteTransactionType);
			jLabelWasteTransactionType.setText(lang.get("lbl_Transaction_Type"));
			jLabelWasteTransactionType.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelWasteTransactionType.setBounds(242, 8, 156, 22);

			jTextFieldTransactionType = new JTextField4j();
			jDesktopPane1.add(jTextFieldTransactionType);
			jTextFieldTransactionType.setText(ltype);
			jTextFieldTransactionType.setBounds(410, 8, 126, 22);
			jTextFieldTransactionType.setEnabled(false);
			jTextFieldTransactionType.setEditable(false);
			jTextFieldTransactionType.setFont(Common.font_bold);

			jTextFieldUOM = new JLabel4j_std();
			jDesktopPane1.add(jTextFieldUOM);
			jTextFieldUOM.setText("KG");
			jTextFieldUOM.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldUOM.setHorizontalTextPosition(SwingConstants.LEFT);
			jTextFieldUOM.setBounds(226, 233, 45, 21);

			jTextFieldWasteMaterial = new JTextField4j(JDBWasteLog.field_MaterialID);
			jTextFieldWasteMaterial.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					materialChanged();
				}
			});
			jDesktopPane1.add(jTextFieldWasteMaterial);
			jTextFieldWasteMaterial.setBounds(142, 104, 156, 22);

			jTextFieldWasteReason = new JTextField4j(JDBWasteLog.field_ReasonID);
			jTextFieldWasteReason.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					reasonChanged();
				}
			});
			jDesktopPane1.add(jTextFieldWasteReason);
			jTextFieldWasteReason.setBounds(142, 168, 156, 22);

			jTextFieldWasteLocation = new JTextField4j(JDBWasteLog.field_LocationID);
			jTextFieldWasteLocation.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					locationChanged();
				}
			});
			jDesktopPane1.add(jTextFieldWasteLocation);
			jTextFieldWasteLocation.setBounds(142, 40, 156, 22);

			jTextFieldWasteComment = new JTextField4j(JDBWasteLog.field_Comment);
			jTextFieldWasteComment.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					jButtonSave.setEnabled(true);
					jButtonUndo.setEnabled(true);
					jButtonNew.setEnabled(false);
				}
			});
			jDesktopPane1.add(jTextFieldWasteComment);
			jTextFieldWasteComment.setBounds(142, 264, 712, 22);

			jTextFieldWasteContainer = new JTextField4j(JDBWasteLog.field_ContainerID);
			jTextFieldWasteContainer.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					locationChanged();
				}
			});
			jDesktopPane1.add(jTextFieldWasteContainer);
			jTextFieldWasteContainer.setBounds(142, 72, 156, 22);

			jLabelWasteMaterial = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteMaterial);
			jLabelWasteMaterial.setText(lang.get("lbl_Material"));
			jLabelWasteMaterial.setBounds(0, 104, 130, 22);
			jLabelWasteMaterial.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelWasteReason = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteReason);
			jLabelWasteReason.setText(lang.get("lbl_Reason"));
			jLabelWasteReason.setBounds(0, 168, 130, 22);
			jLabelWasteReason.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelWasteLocation = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteLocation);
			jLabelWasteLocation.setText(lang.get("lbl_Location_ID"));
			jLabelWasteLocation.setBounds(0, 40, 130, 22);
			jLabelWasteLocation.setHorizontalAlignment(SwingConstants.TRAILING);

			jLabelWasteContainer = new JLabel4j_std();
			jDesktopPane1.add(jLabelWasteContainer);
			jLabelWasteContainer.setText(lang.get("lbl_Container_ID"));
			jLabelWasteContainer.setBounds(0, 72, 130, 22);
			jLabelWasteContainer.setHorizontalAlignment(SwingConstants.TRAILING);

			jButtonLookupWasteMaterial = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupWasteMaterial.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (jTextFieldWasteLocation.getText().trim().equals("") == false)
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = jTextFieldWasteLocation.getText().trim();
						if (JLaunchLookup.waste_materials_for_location())
						{
							jTextFieldWasteMaterial.setText(JLaunchLookup.dlgResult);
							materialChanged();
						}
					}
					else
					{
						JLaunchLookup.dlgAutoExec = true;
						JLaunchLookup.dlgCriteriaDefault = jTextFieldWasteLocation.getText().trim();
						if (JLaunchLookup.waste_materials_all())
						{
							jTextFieldWasteMaterial.setText(JLaunchLookup.dlgResult);
							materialChanged();
						}
					}
				}
			});
			jButtonLookupWasteMaterial.setBounds(298, 104, 21, 22);
			jDesktopPane1.add(jButtonLookupWasteMaterial);

			jButtonLookupWasteReason = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupWasteReason.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgAutoExec = false;
					JLaunchLookup.dlgCriteriaDefault = "";
					if (JLaunchLookup.waste_reasons())
					{
						jTextFieldWasteReason.setText(JLaunchLookup.dlgResult);
						reasonChanged();
					}
				}
			});
			jButtonLookupWasteReason.setBounds(298, 168, 21, 22);
			jDesktopPane1.add(jButtonLookupWasteReason);

			jButtonLookupWasteLocation = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupWasteLocation.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgCriteriaDefault = "";
					JLaunchLookup.dlgAutoExec = true;
					if (JLaunchLookup.waste_locations())
					{
						jTextFieldWasteLocation.setText(JLaunchLookup.dlgResult);
						locationChanged();
					}

				}
			});
			jButtonLookupWasteLocation.setBounds(298, 40, 21, 22);
			jDesktopPane1.add(jButtonLookupWasteLocation);

			jButtonLookupWasteContainer = new JButton4j(Common.icon_lookup_16x16);
			jButtonLookupWasteContainer.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JLaunchLookup.dlgCriteriaDefault = "";
					JLaunchLookup.dlgAutoExec = true;
					if (JLaunchLookup.waste_containers())
					{
						jTextFieldWasteContainer.setText(JLaunchLookup.dlgResult);
						containerChanged();
					}

				}
			});
			jButtonLookupWasteContainer.setBounds(298, 72, 21, 22);
			jDesktopPane1.add(jButtonLookupWasteContainer);

			jTextFieldUserID = new JTextField4j(JDBUser.field_user_id);
			jTextFieldUserID.setEditable(false);
			jDesktopPane1.add(jTextFieldUserID);
			jTextFieldUserID.setBounds(396, 232, 229, 22);

			jLabelUser = new JLabel4j_std();
			jDesktopPane1.add(jLabelUser);
			jLabelUser.setText(lang.get("lbl_User_ID"));
			jLabelUser.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelUser.setBounds(219, 232, 156, 22);

			jTextFieldProcessOrder = new JTextField4j(JDBWasteLog.field_ProcessOrder);
			jTextFieldProcessOrder.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					processOrderChanged();
				}
			});
			jDesktopPane1.add(jTextFieldProcessOrder);
			jTextFieldProcessOrder.setBounds(142, 200, 156, 22);

			jButtonLookupProcessOrder = new JButton4j(Common.icon_lookup_16x16);
			jDesktopPane1.add(jButtonLookupProcessOrder);
			jButtonLookupProcessOrder.setBounds(298, 200, 21, 22);
			jButtonLookupProcessOrder.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JLaunchLookup.dlgCriteriaDefault = "Ready";
					JLaunchLookup.dlgAutoExec = true;
					if (JLaunchLookup.processOrders())
					{
						jTextFieldProcessOrder.setText(JLaunchLookup.dlgResult);
						processOrderChanged();
					}
				}
			});

			jLabelProcessOrder = new JLabel4j_std();
			jDesktopPane1.add(jLabelProcessOrder);
			jLabelProcessOrder.setText(lang.get("lbl_Process_Order"));
			jLabelProcessOrder.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelProcessOrder.setBounds(0, 200, 130, 22);

			jFormattedTextFieldQuantity = new JQuantityInput(new BigDecimal("0"));
			jDesktopPane1.add(jFormattedTextFieldQuantity);
			jFormattedTextFieldQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jFormattedTextFieldQuantity.setBounds(142, 232, 77, 22);
			jFormattedTextFieldQuantity.addKeyListener(new KeyAdapter()
			{
				public void keyReleased(KeyEvent evt)
				{
					jButtonSave.setEnabled(true);
					jButtonUndo.setEnabled(true);
					jButtonNew.setEnabled(false);
				}
			});

			jLabelQuantity = new JLabel4j_std();
			jDesktopPane1.add(jLabelQuantity);
			jLabelQuantity.setText(lang.get("lbl_Pallet_Quantity"));
			jLabelQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelQuantity.setBounds(0, 232, 130, 22);

			jLabelComment = new JLabel4j_std();
			jDesktopPane1.add(jLabelComment);
			jLabelComment.setText(lang.get("lbl_Comment"));
			jLabelComment.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelComment.setBounds(0, 264, 130, 22);

			jLabelTransactionDate = new JLabel4j_std();
			jLabelTransactionDate.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabelTransactionDate.setText(lang.get("lbl_Transaction_Date"));
			jLabelTransactionDate.setBounds(540, 8, 130, 22);
			jDesktopPane1.add(jLabelTransactionDate);

			transactionDate = new JDateControl();
			transactionDate.setDisplayMode(JDateControl.mode_disable_visible);
			transactionDate.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					jButtonSave.setEnabled(true);
					jButtonUndo.setEnabled(true);
					jButtonNew.setEnabled(false);
				}
			});
			transactionDate.setBounds(680, 8, 120, 22);
			jDesktopPane1.add(transactionDate);

			jTextFieldMaterialDescription = new JTextField4j(JDBWasteMaterial.field_Description);
			jTextFieldMaterialDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldMaterialDescription);
			jTextFieldMaterialDescription.setBounds(340, 104, 285, 22);
			jTextFieldMaterialDescription.setEnabled(false);

			jTextFieldReasonDescription = new JTextField4j(JDBWasteReasons.field_Description);
			jTextFieldReasonDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldReasonDescription);
			jTextFieldReasonDescription.setBounds(340, 168, 285, 22);
			jTextFieldReasonDescription.setEnabled(false);

			jTextFieldLocationDescription = new JTextField4j(JDBWasteLocation.field_Description);
			jTextFieldLocationDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldLocationDescription);
			jTextFieldLocationDescription.setBounds(340, 40, 285, 22);
			jTextFieldLocationDescription.setEnabled(false);

			jTextFieldContainerDescription = new JTextField4j(JDBWasteContainer.field_Description);
			jTextFieldContainerDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldContainerDescription);
			jTextFieldContainerDescription.setBounds(340, 72, 285, 22);
			jTextFieldContainerDescription.setEnabled(false);

			jTextFieldProcessOrderDescription = new JTextField4j(JDBProcessOrder.field_description);
			jTextFieldProcessOrderDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldProcessOrderDescription);
			jTextFieldProcessOrderDescription.setBounds(340, 200, 285, 22);
			jTextFieldProcessOrderDescription.setEnabled(false);

			jStatusText = new JLabel4j_status();
			jDesktopPane1.add(jStatusText);
			jStatusText.setBounds(5, 340, 858, 21);

			JLabel4j_std jLabel_Description = new JLabel4j_std();
			jLabel_Description.setText(lang.get("lbl_PO_Required"));
			jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Description.setBounds(12, 55, 149, 22);
			panel.add(jLabel_Description);

			JLabel4j_std jLabel_Reason_Reqd = new JLabel4j_std();
			jLabel_Reason_Reqd.setText(lang.get("lbl_Reason_Reqd"));
			jLabel_Reason_Reqd.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Reason_Reqd.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Reason_Reqd.setBounds(12, 81, 149, 22);
			panel.add(jLabel_Reason_Reqd);
			chckbx_PO_Reqd.setEnabled(false);

			chckbx_PO_Reqd.setBounds(175, 55, 29, 22);
			panel.add(chckbx_PO_Reqd);
			chckbx_Reason_Reqd.setEnabled(false);

			chckbx_Reason_Reqd.setBounds(175, 81, 29, 22);
			panel.add(chckbx_Reason_Reqd);

			jLabel_WasteType = new JLabel4j_std();
			jDesktopPane1.add(jLabel_WasteType);
			jLabel_WasteType.setText(lang.get("lbl_Type_ID"));
			jLabel_WasteType.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_WasteType.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_WasteType.setBounds(0, 136, 130, 22);

			jTextFieldTypeID = new JTextField4j(JDBWasteLocation.field_WasteLocationID);
			jDesktopPane1.add(jTextFieldTypeID);
			jTextFieldTypeID.setHorizontalAlignment(SwingConstants.LEFT);
			jTextFieldTypeID.setEditable(false);
			jTextFieldTypeID.setPreferredSize(new java.awt.Dimension(100, 20));
			jTextFieldTypeID.setBounds(142, 136, 156, 22);
			jTextFieldTypeID.setEnabled(false);

			chckbx_IncludeInTotals.setEnabled(false);

			chckbx_IncludeInTotals.setBounds(175, 3, 29, 22);
			panel.add(chckbx_IncludeInTotals);
			chckbx_StoreAsNegative.setEnabled(false);

			chckbx_StoreAsNegative.setBounds(175, 29, 29, 22);
			panel.add(chckbx_StoreAsNegative);

			JLabel4j_std jLabel_IncludeInTotals = new JLabel4j_std();
			jLabel_IncludeInTotals.setText(lang.get("lbl_Include_In_Totals"));
			jLabel_IncludeInTotals.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_IncludeInTotals.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_IncludeInTotals.setBounds(12, 3, 149, 22);
			panel.add(jLabel_IncludeInTotals);

			JLabel4j_std jLabel_StoreAsNegative = new JLabel4j_std();
			jLabel_StoreAsNegative.setText(lang.get("lbl_Store_As_Negative"));
			jLabel_StoreAsNegative.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_StoreAsNegative.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_StoreAsNegative.setBounds(12, 29, 149, 22);
			panel.add(jLabel_StoreAsNegative);
			chckbx_Recycle.setEnabled(false);

			chckbx_Recycle.setBounds(175, 159, 29, 22);
			panel.add(chckbx_Recycle);
			chckbx_Hazard.setEnabled(false);

			chckbx_Hazard.setBounds(175, 133, 29, 22);
			panel.add(chckbx_Hazard);
			chckbx_PPEReqd.setEnabled(false);

			chckbx_PPEReqd.setBounds(175, 107, 29, 22);
			panel.add(chckbx_PPEReqd);

			JLabel4j_std jLabel_Recycle = new JLabel4j_std();
			jLabel_Recycle.setText(lang.get("lbl_Recycle"));
			jLabel_Recycle.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Recycle.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Recycle.setBounds(12, 159, 149, 22);
			panel.add(jLabel_Recycle);

			JLabel4j_std jLabel_Hazard = new JLabel4j_std();
			jLabel_Hazard.setText(lang.get("lbl_Hazard"));
			jLabel_Hazard.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_Hazard.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_Hazard.setBounds(12, 133, 149, 22);
			panel.add(jLabel_Hazard);

			JLabel4j_std jLabel_PPEReqd = new JLabel4j_std();
			jLabel_PPEReqd.setText(lang.get("lbl_PPERequired"));
			jLabel_PPEReqd.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel_PPEReqd.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel_PPEReqd.setBounds(12, 107, 149, 22);
			panel.add(jLabel_PPEReqd);

			jTextFieldMaterialTypeDescription = new JTextField4j(JDBWasteMaterial.field_Description);
			jTextFieldMaterialTypeDescription.setEditable(false);
			jDesktopPane1.add(jTextFieldMaterialTypeDescription);
			jTextFieldMaterialTypeDescription.setBounds(340, 136, 285, 22);
			jTextFieldMaterialTypeDescription.setEnabled(false);

			button_CalendarTransaction = new JCalendarButton(transactionDate);
			button_CalendarTransaction.setSize(21, 21);
			button_CalendarTransaction.setLocation(800, 9);
			jDesktopPane1.add(button_CalendarTransaction);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
