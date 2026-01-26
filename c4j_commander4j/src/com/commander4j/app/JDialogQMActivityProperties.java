package com.commander4j.app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMActivity;
import com.commander4j.db.JDBQMInspection;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDialogQMActivityProperties is part of the Quality Module within the
 * application. The structure of the screens and tables is loosely based upon
 * the SAP Inspection Lot system. The tables APP_QM_INSPECTION, APP_QM_ACTIVITY
 * and APP_QM_TEST are arranged in a 3 layer hierarchy. For each Inspection
 * there can be one or more Activities. For each Activity there will typically
 * be more than one Test.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogQMActivityProperties.jpg" >
 *
 * @see com.commander4j.db.JDBQMInspection JDBQMInspection
 * @see com.commander4j.db.JDBQMActivity JDBQMActivity
 * @see com.commander4j.db.JDBQMTest JDBQMTest
 * @see com.commander4j.db.JDBQMSample JDBQMSample
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.app.JInternalFrameQMInspectionAdmin
 *      JInternalFrameQMInspectionAdmin
 * @see com.commander4j.app.JDialogQMInspectionProperties
 *      JDialogQMInspectionProperties
 * @see com.commander4j.app.JDialogQMActivityProperties
 *      JDialogQMActivityProperties
 * @see com.commander4j.app.JDialogQMTestProperties JDialogQMTestProperties
 *
 */
public class JDialogQMActivityProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JButton4j btnClose;
	private JButton4j btnSave;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMActivity active = new JDBQMActivity(Common.selectedHostID, Common.sessionID);
	private JDBQMInspection inspect = new JDBQMInspection(Common.selectedHostID, Common.sessionID);
	private JTextField4j textFieldActivityDescription;
	private JTextField4j textFieldActivityID;
	private JTextField4j textFieldInspectionDescription;
	private JTextField4j textFieldInspectionID;

	public JDialogQMActivityProperties(JFrame frame, String inspectionid, String activityid)
	{

		super(frame, "Activity Properties", ModalityType.APPLICATION_MODAL);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Activity Properties");
		this.setResizable(false);
		this.setSize(855, 151);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Common.color_app_window);
		getContentPane().setLayout(null);

		JDesktopPane4j desktopPane = new JDesktopPane4j();
		desktopPane.setBounds(0, 0, 855, 162);
		getContentPane().add(desktopPane);

		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Inspection_ID"));
		lblInspectionID.setBounds(8, 8, 87, 22);
		desktopPane.add(lblInspectionID);
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel4j_std lblActivityID = new JLabel4j_std(lang.get("lbl_Activity_ID"));
		lblActivityID.setBounds(6, 40, 89, 22);
		desktopPane.add(lblActivityID);
		lblActivityID.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldInspectionID = new JTextField4j(JDBQMInspection.field_inspection_id);
		textFieldInspectionID.setEnabled(false);
		textFieldInspectionID.setBounds(108, 8, 153, 22);
		desktopPane.add(textFieldInspectionID);
		textFieldInspectionID.setColumns(10);

		textFieldInspectionDescription = new JTextField4j(JDBQMInspection.field_description);
		textFieldInspectionDescription.setEnabled(false);
		textFieldInspectionDescription.setBounds(377, 8, 463, 22);
		desktopPane.add(textFieldInspectionDescription);
		textFieldInspectionDescription.setColumns(10);

		btnSave = new JButton4j(lang.get("btn_Save"));
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				save();
			}
		});
		btnSave.setIcon(Common.icon_update_16x16);
		btnSave.setBounds(292, 74, 117, 32);
		desktopPane.add(btnSave);

		btnClose = new JButton4j(lang.get("btn_Close"));
		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
		btnClose.setIcon(Common.icon_close_16x16);
		btnClose.setBounds(413, 74, 117, 32);
		desktopPane.add(btnClose);

		JLabel4j_std label4j_std_1 = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(273, 8, 92, 22);
		desktopPane.add(label4j_std_1);

		textFieldActivityID = new JTextField4j(JDBQMActivity.field_activity_id);
		textFieldActivityID.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				enableSave();
			}
		});
		textFieldActivityID.setEnabled(false);
		textFieldActivityID.setText("<dynamic>");
		textFieldActivityID.setColumns(10);
		textFieldActivityID.setBounds(108, 40, 153, 22);
		desktopPane.add(textFieldActivityID);

		JLabel4j_std label4j_std_2 = new JLabel4j_std(lang.get("lbl_Description"));
		label4j_std_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_2.setBounds(267, 40, 98, 22);
		desktopPane.add(label4j_std_2);

		textFieldActivityDescription = new JTextField4j(JDBQMActivity.field_description);
		textFieldActivityDescription.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				enableSave();
			}
		});
		textFieldActivityDescription.setColumns(10);
		textFieldActivityDescription.setBounds(377, 40, 463, 22);
		desktopPane.add(textFieldActivityDescription);

		inspectionid = JUtility.replaceNullStringwithBlank(inspectionid);
		activityid = JUtility.replaceNullStringwithBlank(activityid);

		textFieldInspectionID.setText(inspectionid);
		inspect.getProperties(inspectionid);
		textFieldInspectionDescription.setText(inspect.getDescription());

		textFieldActivityID.setText(activityid);
		active.getProperties(inspectionid, activityid);
		textFieldActivityDescription.setText(active.getDescription());

		if (activityid.equals(""))
		{
			textFieldActivityID.setEnabled(true);
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					textFieldActivityID.requestFocus();
					textFieldActivityID.setCaretPosition(textFieldActivityID.getText().length());
				}
			});
		}
		else
		{
			textFieldActivityID.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					textFieldActivityDescription.requestFocus();
					textFieldActivityDescription.setCaretPosition(textFieldActivityDescription.getText().length());
				}
			});
		}

	}

	private void enableSave()
	{
		if (textFieldActivityID.getText().equals("") == false)
		{
			if (textFieldActivityDescription.getText().equals("") == false)
			{
				btnSave.setEnabled(true);
			}
		}
	}

	private void save()
	{
		String insp = textFieldInspectionID.getText();
		String act = textFieldActivityID.getText();
		String description = textFieldActivityDescription.getText();

		if (active.isValid(insp, act) == false)
		{
			active.create(insp, act, description);
		}
		else
		{
			active.setDescription(textFieldActivityDescription.getText());
			active.update();
		}
	}
}
