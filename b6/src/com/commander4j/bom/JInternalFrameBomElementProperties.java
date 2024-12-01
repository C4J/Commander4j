package com.commander4j.bom;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JComboBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JTextField4j;
import com.commander4j.sys.Common;
import com.commander4j.sys.JLaunchMenu;
import com.commander4j.util.JFileFilterImages;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

public class JInternalFrameBomElementProperties extends JInternalFrame
{
	private static final long serialVersionUID = 1;

	private JDesktopPane jDesktopPane1;

	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JButton4j jButtonUpdate;

	private JLabel4j_std jLabel_Data_ID = new JLabel4j_std();
	private JLabel4j_std jLabel_Description = new JLabel4j_std();
	private JLabel4j_std jLabel_DataType = new JLabel4j_std();
	private JLabel4j_std jLabel_IconFilename = new JLabel4j_std();
	private JLabel4j_std jLabel_MaxOccurs = new JLabel4j_std();

	private JLabel4j_std jLabel_EnableCreate = new JLabel4j_std();
	private JLabel4j_std jLabel_EnableEdit = new JLabel4j_std();
	private JLabel4j_std jLabel_EnableDelete = new JLabel4j_std();
	private JLabel4j_std jLabel_EnableDuplicate = new JLabel4j_std();
	private JLabel4j_std jLabel_Enable_Clipboard = new JLabel4j_std();
	private JLabel4j_std jLabel_Enable_Lookup = new JLabel4j_std();

	private JLabel4j_std jLabel_LookupSQL = new JLabel4j_std();
	private JLabel4j_std jLabel_LookupField = new JLabel4j_std();

	private JTextField4j jTextField_DataID = new JTextField4j(JDBBomElement.field_data_id);
	private JTextField4j jTextField_Description = new JTextField4j(JDBBomElement.field_data_description);
	private JTextField4j jTextField_IconFilename = new JTextField4j(JDBBomElement.field_icon_filename);
	private JTextField4j jTextField_LookupField = new JTextField4j(JDBBomElement.field_lookup_field);

	private JCheckBox4j checkBox_EnableCreate = new JCheckBox4j();
	private JCheckBox4j checkBox_EnableEdit = new JCheckBox4j();
	private JCheckBox4j checkBox_EnableDelete = new JCheckBox4j();
	private JCheckBox4j checkBox_EnableDuplicate = new JCheckBox4j();
	private JCheckBox4j checkBox_EnableClipboard = new JCheckBox4j();
	private JCheckBox4j checkBox_EnableLookup = new JCheckBox4j();

	private JButton4j jButtonIconFileChooser;

	JScrollPane scrollPane = new JScrollPane();
	JTextArea textArea_LookupSQL = new JTextArea();

	private JSpinner jSpinnerLimit_MaxOccurences;

	private JComboBox4j<String> comboBoxDataType = new JComboBox4j<String>();
	ComboBoxModel<String> jComboBox1Model = new DefaultComboBoxModel<String>(new String[] { "", "decimal", "string", "timestamp" });

	private String ldata_id = "";

	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JDBBomElement elements = new JDBBomElement(Common.selectedHostID, Common.sessionID);

	public JInternalFrameBomElementProperties()
	{
		super();

		initGUI();
		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_BOM_ELEMENTS"));

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextField_Description.requestFocus();
				jTextField_Description.setCaretPosition(jTextField_Description.getText().length());

			}
		});
	}

	public JInternalFrameBomElementProperties(String id)
	{
		this();
		setDataID(id);
	}

	public void setDataID(String id)
	{
		jTextField_DataID.setText(id);

		setTitle(getTitle() + " - " + id);

		ldata_id = id;

		getProperties(ldata_id);

		setButtonState(false);
	}

	private boolean getProperties(String dataID)
	{
		boolean result = false;
		result = elements.getProperties(dataID);

		jTextField_Description.setText(elements.getElementRecord().getDescription());
		jTextField_IconFilename.setText(elements.getElementRecord().getIcon_filename());

		jSpinnerLimit_MaxOccurences.setValue(elements.getElementRecord().getMax_occur_level());

		textArea_LookupSQL.setText(elements.getElementRecord().getLookup_sql());

		jTextField_LookupField.setText(elements.getElementRecord().getLookup_field());

		checkBox_EnableCreate.setSelected(elements.getElementRecord().isEnable_create());
		checkBox_EnableEdit.setSelected(elements.getElementRecord().isEnable_edit());
		checkBox_EnableDelete.setSelected(elements.getElementRecord().isEnable_delete());
		checkBox_EnableDuplicate.setSelected(elements.getElementRecord().isEnable_duplicate());
		checkBox_EnableClipboard.setSelected(elements.getElementRecord().isEnable_clipboard());
		checkBox_EnableLookup.setSelected(elements.getElementRecord().isEnable_lookup());

		comboBoxDataType.setSelectedItem(elements.getElementRecord().getDataType());

		return result;
	}

	private void initGUI()
	{
		try
		{
			this.setPreferredSize(new java.awt.Dimension(389, 143));
			this.setBounds(0, 0, 552, 442);
			setVisible(true);
			this.setIconifiable(true);
			this.setClosable(true);
			getContentPane().setLayout(null);
			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 542, 410);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				getContentPane().add(jDesktopPane1);
				jDesktopPane1.setLayout(null);

				jLabel_Data_ID.setBounds(22, 8, 112, 21);
				jDesktopPane1.add(jLabel_Data_ID);
				jLabel_Data_ID.setText(lang.get("lbl_Data_ID"));
				jLabel_Data_ID.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_Data_ID.setHorizontalTextPosition(SwingConstants.RIGHT);

				jLabel_Description.setBounds(22, 37, 112, 21);
				jDesktopPane1.add(jLabel_Description);
				jLabel_Description.setText(lang.get("lbl_Description"));
				jLabel_Description.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_Description.setHorizontalTextPosition(SwingConstants.RIGHT);

				jLabel_DataType.setBounds(22, 66, 112, 21);
				jLabel_DataType.setText((String) null);
				jLabel_DataType.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_DataType.setHorizontalAlignment(SwingConstants.RIGHT);
				jLabel_DataType.setText(lang.get("lbl_Data_Type"));
				jDesktopPane1.add(jLabel_DataType);

				jLabel_IconFilename.setBounds(22, 95, 112, 21);
				jLabel_IconFilename.setText(lang.get("lbl_Module_Icon_Filename"));
				jLabel_IconFilename.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_IconFilename.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_IconFilename);

				jLabel_MaxOccurs.setBounds(22, 124, 112, 21);
				jLabel_MaxOccurs.setText(lang.get("lbl_Max_Occurrences"));
				jLabel_MaxOccurs.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_MaxOccurs.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_MaxOccurs);

				jLabel_EnableCreate.setBounds(22, 167, 112, 21);
				jLabel_EnableCreate.setText(lang.get("lbl_Create"));
				jLabel_EnableCreate.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_EnableCreate.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_EnableCreate);

				jLabel_EnableEdit.setBounds(22, 194, 112, 21);
				jLabel_EnableEdit.setText(lang.get("lbl_Edit"));
				jLabel_EnableEdit.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_EnableEdit.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_EnableEdit);

				jLabel_Enable_Clipboard.setBounds(22, 275, 112, 21);
				jLabel_Enable_Clipboard.setText(lang.get("lbl_Clipboard"));
				jLabel_Enable_Clipboard.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_Enable_Clipboard.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_Enable_Clipboard);

				jLabel_Enable_Lookup.setBounds(22, 302, 112, 21);
				jLabel_Enable_Lookup.setText(lang.get("lbl_Lookup"));
				jLabel_Enable_Lookup.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_Enable_Lookup.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_Enable_Lookup);

				jLabel_LookupSQL.setBounds(178, 150, 175, 21);
				jLabel_LookupSQL.setText(lang.get("lbl_Lookup_SQL"));
				jLabel_LookupSQL.setHorizontalTextPosition(SwingConstants.LEFT);
				jLabel_LookupSQL.setHorizontalAlignment(SwingConstants.LEFT);
				jDesktopPane1.add(jLabel_LookupSQL);

				jLabel_LookupField.setBounds(52, 325, 112, 21);
				jLabel_LookupField.setText(lang.get("lbl_Lookup_Field"));
				jLabel_LookupField.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_LookupField.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_LookupField);

				jLabel_EnableDelete.setBounds(22, 221, 112, 21);
				jLabel_EnableDelete.setText(lang.get("lbl_Delete"));
				jLabel_EnableDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_EnableDelete.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_EnableDelete);

				jLabel_EnableDuplicate.setBounds(22, 248, 112, 21);
				jLabel_EnableDuplicate.setText(lang.get("lbl_Duplicate"));
				jLabel_EnableDuplicate.setHorizontalTextPosition(SwingConstants.RIGHT);
				jLabel_EnableDuplicate.setHorizontalAlignment(SwingConstants.RIGHT);
				jDesktopPane1.add(jLabel_EnableDuplicate);

				jTextField_DataID.setBounds(143, 8, 141, 21);
				jDesktopPane1.add(jTextField_DataID);
				jTextField_DataID.setHorizontalAlignment(SwingConstants.LEFT);
				jTextField_DataID.setEditable(false);
				jTextField_DataID.setEnabled(false);

				jTextField_Description.setBounds(143, 37, 332, 21);
				jDesktopPane1.add(jTextField_Description);
				jTextField_Description.setFocusCycleRoot(true);
				jTextField_Description.addKeyListener(new KeyAdapter()
				{
					public void keyTyped(KeyEvent evt)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				comboBoxDataType.setBounds(143, 66, 141, 21);
				comboBoxDataType.setEnabled(true);
				comboBoxDataType.setModel(jComboBox1Model);
				jDesktopPane1.add(comboBoxDataType);
				comboBoxDataType.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				jTextField_IconFilename.setBounds(143, 95, 184, 21);
				jDesktopPane1.add(jTextField_IconFilename);
				jTextField_IconFilename.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						setButtonState(true);
					}
				});

				SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
				jSpinnerIntModel.setMinimum(0);
				jSpinnerIntModel.setMaximum(99);
				jSpinnerIntModel.setStepSize(1);

				jSpinnerLimit_MaxOccurences = new JSpinner();
				jSpinnerLimit_MaxOccurences.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						setButtonState(true);
					}
				});
				jDesktopPane1.add(jSpinnerLimit_MaxOccurences);

				jSpinnerLimit_MaxOccurences.setModel(jSpinnerIntModel);
				JSpinner.NumberEditor ne_jSpinnerLimit_MaxOccurences = new JSpinner.NumberEditor(jSpinnerLimit_MaxOccurences);
				ne_jSpinnerLimit_MaxOccurences.getTextField().setFont(Common.font_input);
				jSpinnerLimit_MaxOccurences.setEditor(ne_jSpinnerLimit_MaxOccurences);
				jSpinnerLimit_MaxOccurences.setBounds(143, 123, 46, 21);
				jSpinnerLimit_MaxOccurences.getEditor().setSize(45, 21);
				jSpinnerLimit_MaxOccurences.getEditor().setFont(Common.font_input);

				checkBox_EnableCreate = new JCheckBox4j();
				checkBox_EnableCreate.setBounds(143, 164, 21, 24);
				checkBox_EnableCreate.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableCreate);
				checkBox_EnableCreate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonState(true);
					}
				});

				checkBox_EnableEdit.setBounds(143, 191, 21, 24);
				checkBox_EnableEdit.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableEdit);
				checkBox_EnableEdit.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonState(true);
					}
				});

				checkBox_EnableDelete.setBounds(143, 218, 21, 24);
				checkBox_EnableDelete.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableDelete);
				checkBox_EnableDelete.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						setButtonState(true);
					}
				});

				checkBox_EnableDuplicate.setBounds(143, 245, 21, 24);
				checkBox_EnableDuplicate.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableDuplicate);
				checkBox_EnableDuplicate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonState(true);
					}
				});

				checkBox_EnableClipboard.setBounds(143, 272, 21, 24);
				checkBox_EnableClipboard.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableClipboard);
				checkBox_EnableClipboard.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonState(true);
					}
				});

				checkBox_EnableLookup.setBounds(143, 299, 21, 24);
				checkBox_EnableLookup.setBackground(Color.WHITE);
				jDesktopPane1.add(checkBox_EnableLookup);
				checkBox_EnableLookup.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setButtonState(true);
					}
				});
				jTextField_LookupField.setBounds(178, 325, 119, 21);

				scrollPane.setBounds(180, 170, 332, 149);
				jDesktopPane1.add(scrollPane);
				textArea_LookupSQL.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						setButtonState(true);
					}
				});
				textArea_LookupSQL.setLineWrap(true);
				scrollPane.setViewportView(textArea_LookupSQL);

				jTextField_LookupField.setColumns(80);
				jTextField_LookupField.setFocusCycleRoot(true);
				jTextField_LookupField.setCaretPosition(0);
				jDesktopPane1.add(jTextField_LookupField);
				jTextField_LookupField.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						setButtonState(true);
					}
				});

				jButtonUpdate = new JButton4j(Common.icon_update_16x16);
				jButtonUpdate.setBounds(97, 358, 112, 28);
				jButtonUpdate.setEnabled(false);
				jButtonUpdate.setText(lang.get("btn_Save"));
				jButtonUpdate.setMnemonic(lang.getMnemonicChar());
				jButtonUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
				jDesktopPane1.add(jButtonUpdate);
				jButtonUpdate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{

						setButtonState(false);
						createUpdate(ldata_id);
					}
				});

				jButtonClose = new JButton4j(Common.icon_close_16x16);
				jButtonClose.setBounds(361, 358, 112, 28);
				jDesktopPane1.add(jButtonClose);
				jButtonClose.setText(lang.get("btn_Close"));
				jButtonClose.setMnemonic(lang.getMnemonicChar());
				jButtonClose.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						dispose();
					}
				});

				jButtonHelp = new JButton4j(Common.icon_help_16x16);
				jButtonHelp.setBounds(230, 358, 112, 28);
				jDesktopPane1.add(jButtonHelp);
				jButtonHelp.setText(lang.get("btn_Help"));
				jButtonHelp.setMnemonic(lang.getMnemonicChar());

				jButtonIconFileChooser = new JButton4j();
				jDesktopPane1.add(jButtonIconFileChooser);
				jButtonIconFileChooser.setText("..");
				jButtonIconFileChooser.setBounds(327, 95, 17, 21);
				jButtonIconFileChooser.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{

						JFileChooser loadIco = new JFileChooser();

						try
						{
							File f = new File(new File("./images/16x16").getCanonicalPath());
							loadIco.setCurrentDirectory(f);
							loadIco.addChoosableFileFilter(new JFileFilterImages());
							loadIco.setAcceptAllFileFilterUsed(false);
							loadIco.setSelectedFile(new File(jTextField_IconFilename.getText()));
						}
						catch (Exception e)
						{
						}

						if (loadIco.showOpenDialog(jButtonIconFileChooser) == JFileChooser.APPROVE_OPTION)
						{
							File selectedFile;
							selectedFile = loadIco.getSelectedFile();

							if (selectedFile != null)
							{
								if (jTextField_IconFilename.getText().compareTo(selectedFile.getName()) != 0)
								{
									jTextField_IconFilename.setText(selectedFile.getName());
									setButtonState(true);
								}
							}
						}
					}
				});
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void createUpdate(String id)
	{

		JDBBomElementRecord elrec = new JDBBomElementRecord();

		elrec.setDataId(ldata_id);

		elrec.setDataType(comboBoxDataType.getSelectedItem().toString());
		elrec.setDescription(jTextField_Description.getText());
		elrec.setIcon_filename(jTextField_IconFilename.getText());

		elrec.setMax_occur_level(Integer.valueOf(jSpinnerLimit_MaxOccurences.getValue().toString()));

		elrec.setEnable_create(checkBox_EnableCreate.isSelected());
		elrec.setEnable_edit(checkBox_EnableEdit.isSelected());
		elrec.setEnable_delete(checkBox_EnableDelete.isSelected());
		elrec.setEnable_duplicate(checkBox_EnableDuplicate.isSelected());
		elrec.setEnable_clipboard(checkBox_EnableClipboard.isSelected());
		elrec.setEnable_lookup(checkBox_EnableLookup.isSelected());

		elrec.setLookup_sql(textArea_LookupSQL.getText());
		elrec.setLookup_field(jTextField_LookupField.getText());

		if (elements.isValid(id))
		{
			elements.update(id, elrec);
		}
		else
		{
			elements.create(id, elrec);
		}

		JLaunchMenu.runForm("FRM_BOM_ELEMENTS", id);
	}

	private void setButtonState(boolean modified)
	{
		jButtonUpdate.setEnabled(modified);

	}
}
