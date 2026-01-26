package com.commander4j.app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JDesktopPane4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDialogQMDictionaryProperties class allows you to edit a single record in
 * the APP_QM_DICTIONARY table. The records in this table define the input
 * column types which are used for data entry within the JDBQMResult input
 * table/grid.
 *
 * <p>
 * <img alt="" src="./doc-files/JDialogQMDictionaryProperties.jpg" >
 *
 * @see com.commander4j.db.JDBQMDictionary JDBQMDictionary
 * @see com.commander4j.app.JInternalFrameQMDictionaryAdmin
 *      JInternalFrameQMDictionaryAdmin
 * @see com.commander4j.db.JDBQMResult JDBQMResult
 *
 */
public class JDialogQMDictionaryProperties extends javax.swing.JDialog
{

	private static final long serialVersionUID = 1L;
	private JButton4j btnClose;
	private JButton4j btnSave;
	private JCheckBox4j chckbxVisible;
	private JComboBox4j<String> comboBoxAlignment;
	private JComboBox4j<String> comboBoxDataType;
	private JComboBox4j<String> comboBoxSelectList;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBQMDictionary dict = new JDBQMDictionary(Common.selectedHostID, Common.sessionID);
	private JDBQMSelectList slist = new JDBQMSelectList(Common.selectedHostID, Common.sessionID);
	private JSpinner4j spinnerWidth;
	private JTextField4j textFieldDefaultValue;
	private JTextField4j textFieldDescription;
	private JTextField4j textFieldITestD;
	private String[] fieldAlignment = new String[]
	{ "Left", "Right", "Center" };

	public JDialogQMDictionaryProperties(JFrame frame, String testid)
	{

		super(frame, "Dictionary Properties", ModalityType.APPLICATION_MODAL);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Dictionary Properties");
		this.setResizable(false);
		this.setSize(593, 282);

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(leftmargin, topmargin);

		getContentPane().setBackground(Common.color_app_window);
		getContentPane().setLayout(null);

		testid = JUtility.replaceNullStringwithBlank(testid);
		dict.getProperties(testid);

		JDesktopPane4j desktopPane = new JDesktopPane4j();

		desktopPane.setBounds(0, 0, 593, 314);
		getContentPane().add(desktopPane);

		JLabel4j_std lblInspectionID = new JLabel4j_std(lang.get("lbl_Test_ID"));
		lblInspectionID.setBounds(8, 8, 92, 22);
		desktopPane.add(lblInspectionID);
		lblInspectionID.setHorizontalAlignment(SwingConstants.TRAILING);

		textFieldITestD = new JTextField4j(JDBQMDictionary.field_test_id);
		textFieldITestD.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				enableSave();
			}
		});
		textFieldITestD.setEnabled(false);
		textFieldITestD.setBounds(112, 8, 231, 22);
		desktopPane.add(textFieldITestD);
		textFieldITestD.setColumns(10);

		textFieldDescription = new JTextField4j(JDBQMDictionary.field_description);
		textFieldDescription.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				enableSave();
			}
		});
		textFieldDescription.setBounds(112, 40, 463, 22);
		desktopPane.add(textFieldDescription);
		textFieldDescription.setColumns(10);

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
		btnSave.setBounds(175, 203, 117, 32);
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
		btnClose.setBounds(304, 203, 117, 32);
		desktopPane.add(btnClose);

		JLabel4j_std lblDescription = new JLabel4j_std(lang.get("lbl_Description"));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(8, 40, 92, 22);
		desktopPane.add(lblDescription);

		JLabel4j_std lblDataType = new JLabel4j_std(lang.get("lbl_DataType"));
		lblDataType.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDataType.setBounds(8, 72, 92, 22);
		desktopPane.add(lblDataType);

		ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(Common.dataTypes);
		comboBoxDataType = new JComboBox4j<String>();
		comboBoxDataType.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				enableSave();
			}
		});
		comboBoxDataType.setModel(jComboBox1Model);
		comboBoxDataType.setBounds(111, 72, 153, 22);
		desktopPane.add(comboBoxDataType);

		comboBoxSelectList = new JComboBox4j<String>();
		comboBoxSelectList.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enableSave();
			}
		});
		comboBoxSelectList.addItem("");
		LinkedList<String> items = new LinkedList<String>();
		items = slist.getSelectListSummary();

		if (dict.getSelectListID().equals("") == false)
		{
			if (items.contains(dict.getSelectListID()) == false)
			{
				items.addFirst(dict.getSelectListID());
			}
		}

		for (int x = 0; x < items.size(); x++)
		{
			comboBoxSelectList.addItem(items.get(x));
		}

		comboBoxSelectList.setBounds(112, 104, 153, 22);
		desktopPane.add(comboBoxSelectList);

		JLabel4j_std lblSelectListID = new JLabel4j_std(lang.get("lbl_List_ID"));
		lblSelectListID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSelectListID.setBounds(8, 104, 92, 22);
		desktopPane.add(lblSelectListID);

		chckbxVisible = new JCheckBox4j("");
		chckbxVisible.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enableSave();
			}
		});
		chckbxVisible.setSelected(true);
		chckbxVisible.setBounds(112, 136, 28, 23);
		desktopPane.add(chckbxVisible);

		JLabel4j_std lblVisible = new JLabel4j_std(lang.get("lbl_Visible"));
		lblVisible.setHorizontalAlignment(SwingConstants.TRAILING);
		lblVisible.setBounds(8, 137, 92, 22);
		desktopPane.add(lblVisible);

		JLabel4j_std lblUOM = new JLabel4j_std(lang.get("lbl_default"));
		lblUOM.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUOM.setBounds(8, 168, 92, 22);
		desktopPane.add(lblUOM);

		textFieldDefaultValue = new JTextField4j(JDBQMDictionary.field_uom);
		textFieldDefaultValue.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				enableSave();
			}
		});
		textFieldDefaultValue.setText("");
		textFieldDefaultValue.setColumns(10);
		textFieldDefaultValue.setBounds(112, 168, 143, 22);
		desktopPane.add(textFieldDefaultValue);

		spinnerWidth = new JSpinner4j();

		spinnerWidth.setBounds(494, 104, 68, 22);
		JSpinner4j.NumberEditor ne_spinnerWidth = new JSpinner4j.NumberEditor(spinnerWidth);
		ne_spinnerWidth.getTextField().addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				enableSave();
			}
		});

		spinnerWidth.setEditor(ne_spinnerWidth);
		spinnerWidth.setValue(50);
		spinnerWidth.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				enableSave();
			}
		});

		desktopPane.add(spinnerWidth);

		JLabel4j_std label4j_std = new JLabel4j_std("Width");
		label4j_std.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std.setBounds(392, 104, 92, 22);
		desktopPane.add(label4j_std);

		ComboBoxModel<String> jComboBox1ModelTypes = new DefaultComboBoxModel<String>(fieldAlignment);
		comboBoxAlignment = new JComboBox4j<String>();
		comboBoxAlignment.setModel(jComboBox1ModelTypes);
		comboBoxAlignment.setBounds(464, 72, 98, 22);
		desktopPane.add(comboBoxAlignment);

		// ****************** //

		textFieldITestD.setText(testid);

		textFieldDescription.setText(dict.getDescription());

		comboBoxDataType.setSelectedItem(dict.getDataType());
		comboBoxSelectList.setSelectedItem(dict.getSelectListID());

		if (dict.getVisible().equals("Y"))
			chckbxVisible.setSelected(true);
		else
			chckbxVisible.setSelected(false);

		textFieldDefaultValue.setText(dict.getDefaultValue());

		spinnerWidth.setValue(dict.getFieldWidth());

		if (testid.equals(""))
		{
			textFieldITestD.setEnabled(true);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					textFieldITestD.requestFocus();
					textFieldITestD.setCaretPosition(textFieldITestD.getText().length());
				}
			});

		}
		else
		{
			textFieldITestD.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					textFieldDescription.requestFocus();
					textFieldDescription.setCaretPosition(textFieldDescription.getText().length());
				}
			});

		}

		comboBoxAlignment.setSelectedItem(dict.getFieldAlignment());

		btnSave.setEnabled(false);

		JLabel4j_std label4j_std_1 = new JLabel4j_std("Width");
		label4j_std_1.setText("Align");
		label4j_std_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label4j_std_1.setBounds(364, 72, 92, 22);
		desktopPane.add(label4j_std_1);

	}

	private void enableSave()
	{
		if (textFieldITestD.getText().equals("") == false)
		{
			if (textFieldDescription.getText().equals("") == false)
			{
				btnSave.setEnabled(true);
			}
		}
	}

	private void save()
	{
		String insp = textFieldITestD.getText();
		String description = textFieldDescription.getText();
		String visible;
		if (chckbxVisible.isSelected())
		{
			visible = "Y";

		}
		else
		{
			visible = "N";

		}

		if (dict.isValid(insp) == false)
		{
			// dict.create(insp,description);
			dict.create(insp, JLabel4j_std.TRAILING, comboBoxDataType.getSelectedItem().toString(), "", "Y", description, visible, Integer.valueOf(spinnerWidth.getValue().toString()), textFieldDefaultValue.getText());
		}

		dict.setFieldAlignment(comboBoxAlignment.getSelectedItem().toString());
		dict.setDescription(description);
		dict.setDataType(comboBoxDataType.getSelectedItem().toString());
		dict.setSelectListID(comboBoxSelectList.getSelectedItem().toString());
		dict.setVisible(visible);
		dict.setUOM("");
		dict.setDefaultValue(textFieldDefaultValue.getText());
		dict.setFieldWidth(Integer.valueOf(spinnerWidth.getValue().toString()));
		dict.update();

		btnSave.setEnabled(false);
	}
}
