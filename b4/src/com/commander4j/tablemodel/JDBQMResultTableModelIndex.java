/*
 * Created on 02-Mar-2005
 *
 */
package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMResultTableModelIndex.java
 * 
 * Package Name : com.commander4j.tablemodel
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

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBQMSample;

public class JDBQMResultTableModelIndex extends AbstractTableModel
{

	private static final long serialVersionUID = 1;
	private JDBQMSample sample;
	private String session;
	private String host;
	private LinkedList<JDBQMSample> sampleList = new LinkedList<JDBQMSample>();
	private JDBLanguage lang;
	private String[] colnames ={"Sample ID","Sample Date & Time","User Data1","User Data 2"};
	
	public Long getSampleID(int row) 
	{
		Long result = (long) -1;
		result = sampleList.get(row).getSampleID();
		return result;
	}
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getHost() {
		return host;
	}

	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public JDBQMResultTableModelIndex(String hostid,String sessionid,String processOrder,String inspectionid,String activityid)
	{
		super();
		setHost(hostid);
		setSession(sessionid);
		sample = new JDBQMSample(hostid, sessionid);
		lang = new JDBLanguage(hostid, sessionid);
		colnames[2]=lang.get("lbl_User_Data1");
		colnames[3]=lang.get("lbl_User_Data2");
		sampleList = sample.getSamples(processOrder,inspectionid, activityid);

	}
	
	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return sampleList.size();
	}

	public boolean isCellEditable(int rowIndex,int columnIndex)
	{

		return false;
	}
	
	public void setValueAt(Object value, int row, int col) {
		
	
	}

	public String getColumnName(int col) {

		return colnames[col];
	}

	public Object getValueAt(int row, int col) 
	
	{
		Object result="";

		switch (col) {
		case 0:
			result =sampleList.get(row).getSampleID().toString();
			break;
		case 1:
			result =sampleList.get(row).getSampleDate().toString().substring(0, 16);
			break;
		case 2:
			result =sampleList.get(row).getUserData1();
			break;
		case 3:
			result =sampleList.get(row).getUserData2();
			break;
		default:
			result="";
			break;
		}
	
		return result;
	}
}
