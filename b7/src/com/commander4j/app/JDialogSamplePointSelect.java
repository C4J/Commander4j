package com.commander4j.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.commander4j.db.JDBWTSamplePoint;
import com.commander4j.gui.JCheckListItem;
import com.commander4j.gui.JList4j;
import com.commander4j.renderer.MultiItemCheckListRenderer;
import com.commander4j.sys.Common;

public class JDialogSamplePointSelect extends JDialog
{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JDBWTSamplePoint samp = new JDBWTSamplePoint(Common.selectedHostID, Common.sessionID);
	private JList4j<JCheckListItem> list;
	private ComboBoxModel<JCheckListItem> model;
	private LinkedList<String> selected = new LinkedList<String>();
	private LinkedList<String> selectedOriginal = new LinkedList<String>();

	/**
	 * Create the dialog.
	 */
	public JDialogSamplePointSelect(LinkedList<String> defaultselected)
	{
		selectedOriginal = defaultselected;
		selected = defaultselected;
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Sample Point Selection");
		
		setSize(271, 303);
		
		Dimension screensize = Common.mainForm.getSize();
		Point parentPos = Common.mainForm.getLocation();

		Dimension formsize = getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		setLocation(parentPos.x + leftmargin , parentPos.y+ topmargin);
		

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(Common.color_edit_properties);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 270, 230);
		contentPanel.add(scrollPane);
		
		scrollPane.setRowHeaderView(list);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 237, 271, 26);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("Select");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int s = list.getModel().getSize();
						JCheckListItem item;
						JDBWTSamplePoint sp;
						selected.clear();
						
						if (s > 0)
						{
							for (int x=0;x<s;x++)
							{
								item = list.getModel().getElementAt(x);
								if (item.isSelected())
								{
									sp = (JDBWTSamplePoint) item.getValue();
									String id = sp.getSamplePoint();
									selected.addLast(id);
								}
							}
						}
						dispose();
					}
				});
				okButton.setBounds(53, 0, 75, 25);
				okButton.setActionCommand("Select");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selected = selectedOriginal;
						dispose();
					}
				});
				cancelButton.setBounds(133, 0, 75, 25);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		model = new DefaultComboBoxModel<JCheckListItem>(samp.getSamplePointCheckList(selected));
		
		list = new JList4j<JCheckListItem>();
		list.setCellRenderer(new MultiItemCheckListRenderer());
		

		list.setModel(model);
		list.setSelectedIndices(new int[] {0});
		list.setBorder(new EmptyBorder(0, 0, 0, 0));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		
		list.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				JList4j<?> list = (JList4j<?>) event.getSource();

				// Get index of item clicked

				int index = list.locationToIndex(event.getPoint());
				JCheckListItem item = (JCheckListItem) list.getModel().getElementAt(index);

				// Toggle selected state

				item.setSelected(!item.isSelected());

				// Repaint cell

				list.repaint(list.getCellBounds(index, index));
				
				//jButtonSave.setEnabled(true);
			}
		});
		
		scrollPane.setViewportView(list);
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				list.requestFocus();
				list.setSelectedIndex(0);
			}
		});
	}
	
	public LinkedList<String> getSelected()
	{
		return selected;
	}
}
