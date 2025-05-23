package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDialogArchiveProperties.java
 * 
 * Package Name : com.commander4j.sys
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.commander4j.db.JDBArchive;
import com.commander4j.db.JDBLanguage;
import com.commander4j.gui.JButton4j;
import com.commander4j.gui.JCheckBox4j;
import com.commander4j.gui.JLabel4j_std;
import com.commander4j.gui.JSpinner4j;
import com.commander4j.gui.JTextField4j;
import com.commander4j.util.JHelp;
import com.commander4j.util.JUtility;

/**
 * The class JDialogArchiveProperties allows you to edit records in the
 * SYS_ARCHIVE table. This screen is invoked from the form
 * JInternalFrameArchiveAdmin. 
 * 
 * <p>
 * <img alt="" src="./doc-files/JDialogArchiveProperties.jpg" >
 * 
 * @see com.commander4j.sys.JInternalFrameArchiveAdmin JInternalFrameArchiveAdmin
 * @see com.commander4j.db.JDBArchive JDBArchive
 */
public class JDialogArchiveProperties extends JDialog
{
	private static final long serialVersionUID = 1;
	private JDesktopPane jDesktopPane1;
	private JLabel4j_std jLabelDescription;
	private JButton4j jButtonClose;
	private JButton4j jButtonHelp;
	private JTextField4j jTextFieldSQLTable;
	private JTextField4j jTextFieldDescription;
	private JTextField4j jTextFieldArchiveID;
	private JButton4j jButtonUpdate;
	private JLabel4j_std jLabelArchiveID;
	private JLabel4j_std jLabelSQLStatement;
	private String larchiveID;
	private JCheckBox4j chckbxEnabled = new JCheckBox4j();
	private JCheckBox4j chckbxBackgroundTask = new JCheckBox4j();
	private JLabel4j_std labelEnabled = new JLabel4j_std();
	private JSpinner4j jSpinnerRetention = new JSpinner4j();
	private JSpinner4j jSpinnerSequence = new JSpinner4j();
	private JSpinner4j jSpinnerMaxDelete = new JSpinner4j();
	private JTextField4j jTextFieldSQLCriteria = new JTextField4j(JDBArchive.field_sql_criteria);
	private JDBArchive arch = new JDBArchive(Common.selectedHostID, Common.sessionID);
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);
	private JLabel4j_std label4jArchiveBefore = new JLabel4j_std();

	public JDialogArchiveProperties(JFrame parent, String archiveID)
	{

		super(parent);

		initGUI();

		final JHelp help = new JHelp();
		help.enableHelpOnButton(jButtonHelp, JUtility.getHelpSetIDforModule("FRM_ADMIN_ARCHIVE_EDIT"));

		jTextFieldArchiveID.setText(archiveID);
		setTitle(getTitle() + " - " + archiveID);
		larchiveID = archiveID;

		arch.setArchiveID(larchiveID);
		arch.getArchiveProperties();

		jTextFieldDescription.setText(arch.getDescription());
		jTextFieldSQLTable.setText(arch.getSQLTable());

		jTextFieldSQLCriteria.setText(arch.getSQLCriteria());

		chckbxEnabled.setSelected(arch.isEnabled());
		chckbxBackgroundTask.setSelected(arch.isBackgroundTask());

		jSpinnerSequence.setValue(arch.getSequence());
		jSpinnerRetention.setValue(arch.getRetentionDays());
		jSpinnerMaxDelete.setValue(arch.getMaxDelete());
		jButtonUpdate.setEnabled(false);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jTextFieldDescription.requestFocus();
				jTextFieldDescription.setCaretPosition(jTextFieldDescription.getText().length());
				jButtonUpdate.setEnabled(false);

			}
		});
	}

	private boolean update()
	{
		boolean result = true;
		arch.setDescription(jTextFieldDescription.getText());
		arch.setSQLTable(jTextFieldSQLTable.getText());
		arch.setSQLCriterial(jTextFieldSQLCriteria.getText());
		arch.setEnabled(chckbxEnabled.isSelected());
		arch.setBackgroundTask(chckbxBackgroundTask.isSelected());
		arch.setSequence(Integer.valueOf(jSpinnerSequence.getValue().toString()));
		arch.setRetentionDays(Integer.valueOf(jSpinnerRetention.getValue().toString()));
		arch.setMaxDelete(Long.valueOf(jSpinnerMaxDelete.getValue().toString()));
		arch.update();
		jButtonUpdate.setEnabled(false);
		return result;
	}

	private void initGUI()
	{
		try
		{
			// setDefaultLookAndFeelDecorated(true);
			setPreferredSize(new java.awt.Dimension(460, 163));
			this.setBounds(25, 25, 678, 351);
			setModal(true);
			this.setTitle("Archive Properties");
			getContentPane().setLayout(null);

			{
				jDesktopPane1 = new JDesktopPane();
				jDesktopPane1.setBounds(0, 0, 678, 329);
				jDesktopPane1.setBackground(Common.color_edit_properties);
				this.getContentPane().add(jDesktopPane1);
				jDesktopPane1.setPreferredSize(new Dimension(452, 140));
				jDesktopPane1.setLayout(null);
				{
					jLabelSQLStatement = new JLabel4j_std();
					jDesktopPane1.add(jLabelSQLStatement);
					jLabelSQLStatement.setText(lang.get("lbl_Table"));
					jLabelSQLStatement.setBounds(5, 84, 185, 22);
					jLabelSQLStatement.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelSQLStatement.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				{
					jLabelArchiveID = new JLabel4j_std();
					jDesktopPane1.add(jLabelArchiveID);
					jLabelArchiveID.setText(lang.get("lbl_Archive_ID"));
					jLabelArchiveID.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelArchiveID.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelArchiveID.setBounds(5, 15, 185, 22);
				}
				{
					jLabelDescription = new JLabel4j_std();
					jDesktopPane1.add(jLabelDescription);
					jLabelDescription.setText(lang.get("lbl_Description"));
					jLabelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
					jLabelDescription.setHorizontalTextPosition(SwingConstants.RIGHT);
					jLabelDescription.setBounds(5, 49, 185, 22);
				}
				{

					jButtonUpdate = new JButton4j(Common.icon_update_16x16);
					jDesktopPane1.add(jButtonUpdate);
					jButtonUpdate.setText(lang.get("btn_Save"));
					jButtonUpdate.setBounds(90, 264, 126, 32);
					jButtonUpdate.setMnemonic(java.awt.event.KeyEvent.VK_S);
					jButtonUpdate.setEnabled(false);
					jButtonUpdate.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							update();
						}
					});
				}
				{
					jButtonClose = new JButton4j(Common.icon_close_16x16);
					jDesktopPane1.add(jButtonClose);
					jButtonClose.setText(lang.get("btn_Close"));
					jButtonClose.setBounds(490, 264, 126, 32);
					jButtonClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
					jButtonClose.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							dispose();
						}
					});
				}
				{
					jTextFieldArchiveID = new JTextField4j(JDBArchive.field_archive_id);
					jTextFieldArchiveID.setFocusable(false);
					jDesktopPane1.add(jTextFieldArchiveID);
					jTextFieldArchiveID.setHorizontalAlignment(SwingConstants.LEFT);
					jTextFieldArchiveID.setEditable(false);
					jTextFieldArchiveID.setPreferredSize(new java.awt.Dimension(100, 20));
					jTextFieldArchiveID.setBounds(202, 15, 151, 22);
					jTextFieldArchiveID.setEnabled(false);
				}
				{
					jTextFieldDescription = new JTextField4j(JDBArchive.field_description);
					jDesktopPane1.add(jTextFieldDescription);
					jTextFieldDescription.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldDescription.setFocusCycleRoot(true);
					jTextFieldDescription.setBounds(202, 49, 260, 22);
					jTextFieldDescription.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});

				}
				{
					jTextFieldSQLTable = new JTextField4j(JDBArchive.field_sql_table);
					jDesktopPane1.add(jTextFieldSQLTable);
					jTextFieldSQLTable.setPreferredSize(new java.awt.Dimension(40, 20));
					jTextFieldSQLTable.setFocusCycleRoot(true);
					jTextFieldSQLTable.setBounds(202, 84, 260, 22);
					jTextFieldSQLTable.addKeyListener(new KeyAdapter()
					{
						public void keyTyped(KeyEvent evt)
						{
							jButtonUpdate.setEnabled(true);
						}
					});
				}
				{
					jButtonHelp = new JButton4j(Common.icon_help_16x16);
					jDesktopPane1.add(jButtonHelp);
					jButtonHelp.setText(lang.get("btn_Help"));
					jButtonHelp.setBounds(355, 264, 126, 32);
					jButtonHelp.setMnemonic(java.awt.event.KeyEvent.VK_H);
				}

				labelEnabled.setText(lang.get("lbl_Enabled"));

				labelEnabled.setHorizontalTextPosition(SwingConstants.RIGHT);
				labelEnabled.setHorizontalAlignment(SwingConstants.RIGHT);
				labelEnabled.setBounds(468, 15, 151, 22);
				jDesktopPane1.add(labelEnabled);

				chckbxEnabled.setBounds(624, 15, 28, 22);
				chckbxEnabled.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(chckbxEnabled);

				chckbxBackgroundTask.setBounds(624, 49, 28, 22);
				chckbxBackgroundTask.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(chckbxBackgroundTask);

				JLabel4j_std label4jRetention = new JLabel4j_std();
				label4jRetention.setText(lang.get("lbl_Retention_Days"));
				label4jRetention.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4jRetention.setHorizontalAlignment(SwingConstants.RIGHT);
				label4jRetention.setBounds(5, 153, 185, 21);
				jDesktopPane1.add(label4jRetention);

				JLabel4j_std label4j_Sequence = new JLabel4j_std();
				label4j_Sequence.setText(lang.get("lbl_Sequence_ID"));
				label4j_Sequence.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Sequence.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Sequence.setBounds(5, 185, 185, 21);
				jDesktopPane1.add(label4j_Sequence);
				
				
				JLabel4j_std label4j_MaxDelete = new JLabel4j_std();
				label4j_MaxDelete.setText(lang.get("lbl_Max_Delete"));
				label4j_MaxDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_MaxDelete.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_MaxDelete.setBounds(5, 218, 185, 21);
				jDesktopPane1.add(label4j_MaxDelete);

				SpinnerNumberModel jSpinnerIntModel = new SpinnerNumberModel();
				jSpinnerIntModel.setMinimum(1);
				jSpinnerIntModel.setMaximum(9999);

				jSpinnerIntModel.setStepSize(1);
				jSpinnerRetention = new JSpinner4j();
				jSpinnerRetention.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jSpinnerRetention.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonUpdate.setEnabled(true);
						arch.setRetentionDays(Integer.valueOf(jSpinnerRetention.getValue().toString()));
						label4jArchiveBefore.setText(arch.getSQLArchiveDate().toString().substring(0, 16));
					}
				});
				jSpinnerRetention.setModel(jSpinnerIntModel);
				jSpinnerRetention.setBounds(202, 153, 79, 21);

				jSpinnerRetention.getEditor().setSize(45, 21);
				JSpinner4j.NumberEditor ne = new JSpinner4j.NumberEditor(jSpinnerRetention);
				jSpinnerRetention.setEditor(ne);
				jDesktopPane1.add(jSpinnerRetention);

				SpinnerNumberModel jSpinnerSeqModel = new SpinnerNumberModel();
				jSpinnerSeqModel.setMinimum(0);
				jSpinnerSeqModel.setMaximum(9990);

				jSpinnerSeqModel.setStepSize(10);
				jSpinnerSequence = new JSpinner4j();
				jSpinnerSequence.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jSpinnerSequence.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});

				JSpinner4j.NumberEditor ne2 = new JSpinner4j.NumberEditor(jSpinnerSequence);
				jSpinnerSequence.setEditor(ne2);
				jSpinnerSequence.setModel(jSpinnerSeqModel);
				jSpinnerSequence.setBounds(202, 185, 79, 21);

				jSpinnerSequence.getEditor().setSize(45, 21);

				jDesktopPane1.add(jSpinnerSequence);
				
		
				SpinnerNumberModel jSpinnerIntModel2 = new SpinnerNumberModel();
				jSpinnerIntModel2.setMinimum(-1);
				jSpinnerIntModel2.setMaximum(999999);

				jSpinnerIntModel2.setStepSize(1000);
				jSpinnerMaxDelete = new JSpinner4j();
				jSpinnerMaxDelete.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jSpinnerMaxDelete.addChangeListener(new ChangeListener()
				{
					public void stateChanged(ChangeEvent e)
					{
						jButtonUpdate.setEnabled(true);
						arch.setMaxDelete(Long.valueOf(jSpinnerMaxDelete.getValue().toString()));
						label4jArchiveBefore.setText(arch.getSQLArchiveDate().toString().substring(0, 16));
					}
				});
				jSpinnerMaxDelete.setModel(jSpinnerIntModel2);
				jSpinnerMaxDelete.setBounds(202, 218, 79, 21);

				jSpinnerMaxDelete.getEditor().setSize(45, 21);
				JSpinner4j.NumberEditor ne3 = new JSpinner4j.NumberEditor(jSpinnerMaxDelete);
				jSpinnerMaxDelete.setEditor(ne3);
				jDesktopPane1.add(jSpinnerMaxDelete);

		
				jTextFieldSQLCriteria.setText("");
				jTextFieldSQLCriteria.setPreferredSize(new Dimension(40, 20));
				jTextFieldSQLCriteria.setFocusCycleRoot(true);
				jTextFieldSQLCriteria.setBounds(202, 117, 450, 21);
				jTextFieldSQLCriteria.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyTyped(KeyEvent e)
					{
						jButtonUpdate.setEnabled(true);
					}
				});
				jDesktopPane1.add(jTextFieldSQLCriteria);

				JLabel4j_std label4j_Criteria = new JLabel4j_std();
				label4j_Criteria.setText(lang.get("lbl_Criteria"));
				label4j_Criteria.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4j_Criteria.setHorizontalAlignment(SwingConstants.RIGHT);
				label4j_Criteria.setBounds(5, 117, 185, 21);
				jDesktopPane1.add(label4j_Criteria);

				JButton4j jButtonRun = new JButton4j(Common.icon_execute_16x16);
				jButtonRun.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						runArchive();
					}
				});
				jButtonRun.setText(lang.get("btn_Run"));
				jButtonRun.setMnemonic(KeyEvent.VK_H);
				jButtonRun.setBounds(222, 264, 126, 32);
				jDesktopPane1.add(jButtonRun);

				JLabel4j_std label4jBackgroundTask = new JLabel4j_std();
				label4jBackgroundTask.setText(lang.get("lbl_Background_Task"));
				label4jBackgroundTask.setHorizontalTextPosition(SwingConstants.RIGHT);
				label4jBackgroundTask.setHorizontalAlignment(SwingConstants.RIGHT);
				label4jBackgroundTask.setBounds(468, 49, 151, 22);
				jDesktopPane1.add(label4jBackgroundTask);

				label4jArchiveBefore.setText((String) null);
				label4jArchiveBefore.setHorizontalTextPosition(SwingConstants.LEFT);
				label4jArchiveBefore.setHorizontalAlignment(SwingConstants.LEFT);
				label4jArchiveBefore.setBounds(293, 153, 185, 21);
				jDesktopPane1.add(label4jArchiveBefore);

			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void runArchive()
	{
		update();
		JDBArchive c = new JDBArchive(Common.selectedHostID, Common.sessionID);
		c.runManual(this,larchiveID);
	}
}
