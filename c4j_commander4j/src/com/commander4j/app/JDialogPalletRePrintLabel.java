package com.commander4j.app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBQuery;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBoxPODevices4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.print.JPrintDevice;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchReport;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The JDialogPalletRePrintLabel class is a Dialog box loaded from the
 * JInternalFramePalletAdmin class which allows a user to reprint a pallet
 * label.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogPalletRePrintLabel.jpg" >
 *
 * @see com.commander4j.app.JInternalFramePalletAdmin JInternalFramePalletAdmin
 *
 */
public class JDialogPalletRePrintLabel extends javax.swing.JDialog
{
	private static final long serialVersionUID = 1;
	private JButton4j jButtonCancel;
	private JButton4j jButtonHelp;
	private JButton4j jButtonPrint;
	private JCheckBox4j checkBoxIncHeaderText = new JCheckBox4j();
	private JCheckBox4j jCheckBoxAutoPreview;
	private JComboBoxPODevices4j comboBoxPrintQueue;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	private JDBPallet pal = new JDBPallet(Common.selectedHostID, Common.sessionID);
	private JDesktopPane4j jDesktopPane1;
	private JLabel4j_std jLabel_Preview;
	private JLabel4j_std jLabel_SSCC;
	private JLabelPrint lab = new JLabelPrint(Common.selectedHostID, Common.sessionID);
	private JSpinner4j jSpinnerCopies = new JSpinner4j();
	private JSpinner4j jSpinnerQuantity = new JSpinner4j();
	private JTextField4j jTextFieldSSCC;
	private PreparedStatement listStatement;
	private String defaultlabel = "";
	private String lsscc;

	public JDialogPalletRePrintLabel(JFrame frame, String sscc)
	{

		super(frame, "Pallet Label Reprint", ModalityType.DOCUMENT_MODAL);
		lsscc = sscc;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Pallet Label Reprint");
		this.setResizable(false);
		this.setSize(769, 205);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		jDesktopPane1 = new JDesktopPane4j();
		jDesktopPane1.setBounds(0, 200, 671, -200);

		this.getContentPane().add(jDesktopPane1);
		jDesktopPane1.setPreferredSize(new java.awt.Dimension(462, 497));
		jDesktopPane1.setLayout(null);

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_PAL_LABEL_COPIES"));

		pal.getPalletProperties(lsscc);
		defaultlabel = lab.getPalletLabelReportName(pal.getProcessOrder());

		initGUI();

		jTextFieldSSCC.setText(lsscc);

	}

	private void buildSQL1Record(String lsscc)
	{

		String temp = "";

		JDBQuery query = new JDBQuery(Common.selectedHostID, Common.sessionID);
		query.clear();

		temp = Common.hostList.getHost(Common.selectedHostID).getSqlstatements().getSQL("JDBPallet.selectWithExpiry");

		query.addText(temp);

		if (lsscc.equals("") == false)
		{
			query.addParamtoSQL("sscc = ", lsscc);
		}

		query.bindParams();
		query.applyRestriction(false, "none", 0);
		listStatement = query.getPreparedStatement();
	}

	private void initGUI()
	{
		try
		{

			jTextFieldSSCC = new JTextField4j(JDBPallet.field_sscc);
			jTextFieldSSCC.setHorizontalAlignment(SwingConstants.CENTER);
			jDesktopPane1.add(jTextFieldSSCC);
			jTextFieldSSCC.setEditable(false);
			jTextFieldSSCC.setEnabled(false);
			jTextFieldSSCC.setBounds(139, 8, 137, 22);

			jButtonPrint = new JButton4j(Common.icon_print_16x16);
			jButtonPrint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JPrintDevice pq = (JPrintDevice) comboBoxPrintQueue.getSelectedItem();
					buildSQL1Record(jTextFieldSSCC.getText());
					JLaunchReport.runReport(defaultlabel, listStatement, jCheckBoxAutoPreview.isSelected(), pq, Integer.valueOf(jSpinnerCopies.getValue().toString()), checkBoxIncHeaderText.isSelected());
					dispose();
				}
			});
			jDesktopPane1.add(jButtonPrint);
			jButtonPrint.setText(lang.get("btn_Print"));
			jButtonPrint.setMnemonic(lang.getMnemonicChar());
			jButtonPrint.setBounds(231, 136, 111, 32);

			jButtonHelp = new JButton4j(Common.icon_help_16x16);
			jDesktopPane1.add(jButtonHelp);
			jButtonHelp.setText(lang.get("btn_Help"));
			jButtonHelp.setMnemonic(lang.getMnemonicChar());
			jButtonHelp.setBounds(344, 136, 111, 32);

			jButtonCancel = new JButton4j(Common.icon_close_16x16);
			jDesktopPane1.add(jButtonCancel);
			jButtonCancel.setText(lang.get("btn_Close"));
			jButtonCancel.setMnemonic(lang.getMnemonicChar());
			jButtonCancel.setBounds(457, 136, 111, 32);
			jButtonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					dispose();
				}
			});

			jLabel_SSCC = new JLabel4j_std();
			jDesktopPane1.add(jLabel_SSCC);
			jLabel_SSCC.setText(lang.get("lbl_Pallet_SSCC"));
			jLabel_SSCC.setBounds(6, 8, 125, 22);
			jLabel_SSCC.setHorizontalAlignment(SwingConstants.TRAILING);

			JLabel4j_std jLabel_Header = new JLabel4j_std();
			jLabel_Header.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Header.setText(lang.get("lbl_Label_Header_Text"));
			jLabel_Header.setBounds(6, 39, 125, 22);
			jDesktopPane1.add(jLabel_Header);

			checkBoxIncHeaderText.setSelected(true);

			checkBoxIncHeaderText.setBounds(139, 40, 21, 21);
			jDesktopPane1.add(checkBoxIncHeaderText);

			JLabel4j_std JLabel_NoOfSSCCs = new JLabel4j_std();
			JLabel_NoOfSSCCs.setBounds(212, 39, 182, 22);
			JLabel_NoOfSSCCs.setHorizontalAlignment(SwingConstants.RIGHT);
			JLabel_NoOfSSCCs.setText(lang.get("lbl_Number_of_SSCCs"));
			jDesktopPane1.add(JLabel_NoOfSSCCs);

			JLabel4j_std jLabels_LabelsPerSSCC = new JLabel4j_std();
			jLabels_LabelsPerSSCC.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabels_LabelsPerSSCC.setBounds(212, 71, 182, 22);
			jLabels_LabelsPerSSCC.setText(lang.get("lbl_Labels_Per_SSCC"));
			jDesktopPane1.add(jLabels_LabelsPerSSCC);
			jSpinnerQuantity.setEnabled(false);

			jSpinnerQuantity.setModel(new SpinnerNumberModel(Integer.valueOf(1), null, null, Integer.valueOf(1)));
			jSpinnerQuantity.setBounds(399, 39, 39, 22);
			jDesktopPane1.add(jSpinnerQuantity);

			jSpinnerCopies.setModel(new SpinnerNumberModel(Integer.valueOf(2), null, null, Integer.valueOf(1)));
			jSpinnerCopies.setBounds(399, 71, 39, 22);
			jDesktopPane1.add(jSpinnerCopies);

			JLabel4j_std jLabel_PrintQueue = new JLabel4j_std(lang.get("lbl_Print_Queue"));
			jLabel_PrintQueue.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_PrintQueue.setBounds(6, 104, 125, 22);
			jDesktopPane1.add(jLabel_PrintQueue);

			comboBoxPrintQueue = new JComboBoxPODevices4j(Common.selectedHostID, Common.sessionID, "RPT_PALLET_LABEL", "");
			comboBoxPrintQueue.setBounds(139, 104, 610, 22);
			jDesktopPane1.add(comboBoxPrintQueue);
			comboBoxPrintQueue.refreshData("RPT_PALLET_LABEL", pal.getProcessOrder());

			jCheckBoxAutoPreview = new JCheckBox4j();
			jCheckBoxAutoPreview.setToolTipText("Auto SSCC");

			jCheckBoxAutoPreview.setBounds(139, 72, 21, 21);
			jDesktopPane1.add(jCheckBoxAutoPreview);

			jLabel_Preview = new JLabel4j_std();
			jLabel_Preview.setBounds(6, 71, 125, 22);
			jLabel_Preview.setHorizontalTextPosition(SwingConstants.CENTER);
			jLabel_Preview.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel_Preview.setText(lang.get("lbl_Preview"));
			jDesktopPane1.add(jLabel_Preview);

			mod.setModuleId(defaultlabel);
			mod.getModuleProperties();

			if (mod.getReportType().equals("Label"))
			{
				jCheckBoxAutoPreview.setSelected(false);
				jCheckBoxAutoPreview.setEnabled(false);
				jSpinnerCopies.setEnabled(true);

			}
			else
			{
				jSpinnerCopies.setEnabled(false);
				jCheckBoxAutoPreview.setSelected(true);
				jCheckBoxAutoPreview.setEnabled(true);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
