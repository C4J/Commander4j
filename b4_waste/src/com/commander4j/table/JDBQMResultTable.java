package com.commander4j.table;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMResultTable.java
 * 
 * Package Name : com.commander4j.table
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

import java.util.LinkedList;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.commander4j.celledit.CheckBoxCellEditor;
import com.commander4j.celledit.NumberCellEditor;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.db.JDBQMTest;
import com.commander4j.gui.JComboBox4jAW;
import com.commander4j.renderer.BigDecimalRenderer;
import com.commander4j.renderer.QMIndexRenderer;
import com.commander4j.renderer.SelectListRenderer;
import com.commander4j.renderer.StringRenderer;
import com.commander4j.renderer.TableCellRenderer_Default;
import com.commander4j.sys.Common;

public class JDBQMResultTable extends JTable
{

	private static final long serialVersionUID = 1L;
	private JDBQMTest test;
	private JDBQMSelectList selectlist;
	private String session;
	private String host;
	private LinkedList<JDBQMDictionary> testPropertiesList = new LinkedList<JDBQMDictionary>();
	private TableCellRenderer_Default defaultRenderer = new TableCellRenderer_Default();
	private BigDecimalRenderer bigDecimalRenderer = new BigDecimalRenderer(defaultRenderer);
	private SelectListRenderer selectListRenderer = new SelectListRenderer(defaultRenderer);
	private StringRenderer stringRenderer = new StringRenderer(defaultRenderer);
	private QMIndexRenderer indexRenderer = new QMIndexRenderer(defaultRenderer);
	private String tableMode;

	public String getSession()
	{
		return session;
	}

	public void setSession(String session)
	{
		this.session = session;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public JDBQMResultTable(String hostid, String sessionid, String inspectionid, String activityid, String mode)
	{
		super();
		setHost(hostid);
		setSession(sessionid);

		tableMode = mode;
		test = new JDBQMTest(getHost(), getSession());
		new JDBQMDictionary(getHost(), getSession());
		selectlist = new JDBQMSelectList(getHost(), getSession());

		testPropertiesList = test.getTestsPropertiesList(inspectionid, activityid);

		
		setDefaultRenderer(Object.class, Common.renderer_table);
		getTableHeader().setDefaultRenderer(Common.renderer_tableheader);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}

	public void setColumnWidths()
	{
		if (tableMode.equals("data"))
		{
			int cols = getColumnCount();
			int width = 0;

			for (int x = 0; x < cols; x++)
			{

				width = ((JDBQMDictionary) testPropertiesList.get(x)).getFieldWidth();
				setColumnWidth(x, width);
			}
		} else
		{
			if (tableMode.equals("index"))
			{
				setColumnWidth(0, 70);
				setColumnWidth(1, 125);
				setColumnWidth(2, 85);
				setColumnWidth(3, 85);
			} else
			{
				if (tableMode.equals("result"))
				{
					setColumnWidth(0, 70);
					setColumnWidth(1, 125);
					setColumnWidth(2, 70);
					setColumnWidth(3, 80);
					setColumnWidth(4, 85);
					setColumnWidth(5, 85);
					setColumnWidth(6, 85);
					setColumnWidth(7, 85);
				}
			}
		}
	}

	private void setColumnWidth(int col, int width)
	{
		JTableHeader th = getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(col);

		tc.setWidth(width);
		tc.setPreferredWidth(width);

		th.repaint();
	}

	public void setCellRenderers(String processOrder, String inspectionid, String activityid, String mode)
	{
		tableMode = mode;
		if (tableMode.equals("data"))
		{
			JDBQMDictionary tempDict;
			for (int x = 0; x < testPropertiesList.size(); x++)
			{
				tempDict = ((JDBQMDictionary) testPropertiesList.get(x));
				// TableColumn column = getColumnModel().getColumn(x);

				// SELECT LIST
				if (tempDict.getDataType().equals("list"))
				{
					setRenderer(x, selectListRenderer);

				}

				if (tempDict.getDataType().equals("numeric"))
				{
					setRenderer(x, bigDecimalRenderer);

				}

				if (tempDict.getDataType().equals("string"))
				{
					setRenderer(x, stringRenderer);

				}

				if (tempDict.getDataType().equals("boolean"))
				{
					setRenderer(x, defaultRenderer);

				}
				
				

			}
		} else
		{
			if (tableMode.equals("index"))
			{
				setRenderer(0, indexRenderer);
				setRenderer(1, indexRenderer);
				setRenderer(2, indexRenderer);
				setRenderer(3, indexRenderer);
			} else
			{
				if (tableMode.equals("result"))
				{
					setRenderer(0, indexRenderer);
					setRenderer(1, indexRenderer);
					setRenderer(2, indexRenderer);
					setRenderer(3, indexRenderer);
					setRenderer(4, indexRenderer);
					setRenderer(5, indexRenderer);
					setRenderer(6, indexRenderer);
					setRenderer(7, indexRenderer);
					if (getColumnModel().getColumnCount() > 8)
					{
						for (int y=8;y<getColumnModel().getColumnCount();y++)
						{
							setRenderer(y, defaultRenderer);
							setColumnWidth(y, 130);
						}
					}
				}
			}
		}

	}

	private void setRenderer(int col, TableCellRenderer rend)
	{
		TableColumn column = getColumnModel().getColumn(col);
		column.setCellRenderer(rend);

	}

	public void setColumnEditors(String processOrder, String inspectionid, String activityid)
	{

		JDBQMDictionary tempDict;
		for (int x = 0; x < testPropertiesList.size(); x++)
		{
			tempDict = ((JDBQMDictionary) testPropertiesList.get(x));
			TableColumn column = getColumnModel().getColumn(x);

			// SELECT LIST
			if (tempDict.getDataType().equals("list"))
			{

				LinkedList<JDBQMSelectList> listValues = new LinkedList<JDBQMSelectList>();
				listValues = selectlist.getSelectList(tempDict.getSelectListID());
				JDBQMSelectList blank = new JDBQMSelectList();
				JComboBox4jAW<JDBQMSelectList> comboBox = new JComboBox4jAW<JDBQMSelectList>();
				comboBox.addItem(blank);

				for (int y = 0; y < listValues.size(); y++)
				{
					comboBox.addItem(listValues.get(y));
				}
				comboBox.setFont(Common.font_table);
				comboBox.setMaximumRowCount(35);
				column.setCellEditor(new DefaultCellEditor(comboBox));
				comboBox.setWide(true);

			}

			if (tempDict.getDataType().equals("numeric"))
			{
				column.setCellEditor(new NumberCellEditor());
			}

			if (tempDict.getDataType().equals("string"))
			{

				JTextField tf = new JTextField();
				tf.setFont(Common.font_table);
				column.setCellEditor(new DefaultCellEditor(tf));
			}
			if (tempDict.getDataType().equals("boolean"))
			{

				column.setCellEditor(new CheckBoxCellEditor());
			}
		}
	}
}
