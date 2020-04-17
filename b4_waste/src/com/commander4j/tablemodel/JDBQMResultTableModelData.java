package com.commander4j.tablemodel;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDBQMResultTableModelData.java
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

import java.math.BigDecimal;

import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import com.commander4j.db.JDBQMDictionary;
import com.commander4j.db.JDBQMResult;
import com.commander4j.db.JDBQMSample;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.db.JDBQMTest;
import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * The JDBQMResultTableModelData class is used in conjunction with the
 * JDBQMResultTableModelIndex class. Within the Quality Module results entry
 * screen the number and type of columns which can be used to record data is
 * user configurable. The left hand of the table displayed on screen which is
 * read only is displayed using JDBQMResultTableModelIndex and the right hand of
 * the table which allows the user to type / pick values is controlled by this
 * class.
 */
public class JDBQMResultTableModelData extends AbstractTableModel
{

	private static final long serialVersionUID = 1;

	private JDBQMSample sample;
	private JDBQMTest test;

	private JDBQMResult res;
	private String session;
	private String host;
	private LinkedList<JDBQMSample> sampleList = new LinkedList<JDBQMSample>();
	private LinkedList<JDBQMTest> testList = new LinkedList<JDBQMTest>();
	private LinkedList<String> listID = new LinkedList<String>();
	private Long sampleID;
	private String testID;
	private String newValue;
	private JDBQMSelectList select;
	private LinkedList<JDBQMDictionary> testPropertiesList = new LinkedList<JDBQMDictionary>();

	/**
	 * @param row
	 *            Expects the table row number
	 *            
	 * @return    Returns the unique sample id for the record on the specified
	 *            table row. This is part of the key for getting data from the
	 *            APP_QM_RESULT table.
	 */
	public Long getSampleID(int row)
	{
		Long result = (long) -1;
		result = sampleList.get(row).getSampleID();
		return result;
	}

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

	public Class<?> getColumnClass(int columnIndex)
	{
		return testPropertiesList.get(columnIndex).getDataClass();
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * @param hostid
	 *            Used to access the database
	 * @param sessionid
	 *            Used to access the database
	 * @param processOrder
	 * 			  The Process Order selected
	 * @param inspectionid
	 * 			  The Inspection ID
	 * @param activityid
	 * 			  The Activity ID
	 */
	public JDBQMResultTableModelData(String hostid, String sessionid, String processOrder, String inspectionid, String activityid)
	{
		super();
		setHost(hostid);
		setSession(sessionid);
	
		JDBQMDictionary tempDict;
		
		sample = new JDBQMSample(hostid, sessionid);		// Create a sample object with database connection details
		test = new JDBQMTest(hostid, sessionid);			// Create a test object with database connection details
		res = new JDBQMResult(hostid, sessionid);			// Create a result object with database connection details
		select = new JDBQMSelectList(hostid, sessionid); 	// Create a select list object with database connection details

		// Initialize the Linked List of type JDBQMDictionary for all the Tests relevant to the Inspection/Activity
		testPropertiesList = test.getTestsPropertiesList(inspectionid, activityid);

		// Initialize the Linked List of type JDBQMSample for all the Tests relevant to the Inspection/Activity
		sampleList = sample.getSamples(processOrder, inspectionid, activityid);
		
		// Initialize the Linked List of type JDBQMTest for all the Tests relevant to the Inspection/Activity
		testList = test.getTests(inspectionid, activityid);

		for (int x = 0; x < testPropertiesList.size(); x++)
		{
			// Create a new instance of class JDBQMDictionary
			tempDict = ((JDBQMDictionary) testPropertiesList.get(x));
			// Add to the class listID of type LinkedList<String>
			listID.addLast(tempDict.getSelectListID());
		}
	}

	public int getColumnCount()
	{
		return testPropertiesList.size();
	}

	public int getRowCount()
	{
		return sampleList.size();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	public void setValueAt(Object value, int row, int col)
	{

		// Get the Sample ID for the row
		sampleID = sampleList.get(row).getSampleID();
		
		// Get the Test ID for the row
		testID = testList.get(col).getTestID();

		// Check to see if the data type of the column is list ( ComboBox )
		if (testPropertiesList.get(col).getDataType().equals("list"))
		{
			try
			{
				// Value is the value of the Select List (not the full record including description).
				newValue = ((JDBQMSelectList) value).getValue();
			} catch (Exception ex)
			{
				// If all else fails return blank.
				newValue = "";
			}
		} else
		{
			// If not a list (comboxBox) then return value as a String.
			newValue = value.toString();
		}

		
		// Search for a result which has a matching Sample ID and Test ID
		if (res.getResultsProperties(sampleID, testID))
		{
			// Check if the newValue returned above is different to the existing value in the results table. 
			if (newValue.equals(res.getValue()) == false)
			{
				// Update the results table.
				res.setStatus("Updated");
				res.setUserID(Common.userList.getUser(getSession()).getUserId());
				res.setUpdated(JUtility.getSQLDateTime());
				res.setValue(newValue);
				res.update();
			}
		} else
		{
			// A result for the Sample ID and Test ID was not found in the database
			if (newValue.equals("") == false)
			{
				// If the result is not blank then create a new result in the table.
				res.setSampleID(sampleID);
				res.setTestID(testID);
				res.setProcessed(null);
				res.setStatus("Created");
				res.setUpdated(JUtility.getSQLDateTime());
				res.setUserID(Common.userList.getUser(getSession()).getUserId());
				res.setValue(newValue);
				res.create(sampleID, testID, newValue, "Created", Common.userList.getUser(getSession()).getUserId());
			}
		}
	}

	public String getColumnName(int col)
	{

		return testPropertiesList.get(col).getDescription();
	}

	public Object getValueAt(int row, int col)

	{
		Object result = "";

		sampleID = sampleList.get(row).getSampleID();
		testID = testList.get(col).getTestID();

		// Check if a result exists for the selected row and column.
		if (res.getResultsProperties(sampleID, testID))
		{
			// Check if the test defines a result of type numeric
			if (testPropertiesList.get(col).getDataType().equals("numeric"))
			{
				try
				{
					// Convert the data from the table into the expected type of numeric 
					result = new BigDecimal(res.getValue());
				} catch (Exception ex)
				{
					result = null;
				}
			}

			// Check if the test defines a result of type boolean
			if (testPropertiesList.get(col).getDataType().equals("boolean"))
			{
				try
				{
					// Convert the data from the table into the expected type of boolean 
					result = Boolean.valueOf(res.getValue());
				} catch (Exception ex)
				{
					result = false;
				}
			}

			// Check if the test defines a result of type list
			if (testPropertiesList.get(col).getDataType().equals("list"))
			{
				try
				{
					// Convert the data from the table into the expected type of list 
					
					JDBQMSelectList sl = new JDBQMSelectList();			// Create a new select list
					
					sl.setSelectListID(listID.get(col));				// Store the List ID
					sl.setValue(res.getValue());						// Store the Value

					
					select.setSelectListID(listID.get(col));			// Specify the List ID to find
																		// JDBQMSelectList contains SELECT_LIST_ID, VALUE, DESCRIPTION & SEQUENCE
					
					select.setValue(res.getValue());					// Specify the Value to find.
					select.getProperties();								// Find the select list item for the specified List ID and Value
					
					sl.setDescription(select.getDescription());			// Store the Description
					sl.setSequence(select.getSequence());				// Store the Sequence
					
					result = sl;
				} catch (Exception ex)
				{
					result = null;
				}
			}

			// Check if the test defines a result of type string
			if (testPropertiesList.get(col).getDataType().equals("string"))
			{
				try
				{
					// Convert the data from the table into the expected type of string 
					result = String.valueOf(res.getValue());
				} catch (Exception ex)
				{
					result = null;
				}
			}

			// Check if the test defines a result of type integer
			if (testPropertiesList.get(col).getDataType().equals("integer"))
			{
				try
				{
					// Convert the data from the table into the expected type of integer 
					result = Integer.valueOf(res.getValue());
				} catch (Exception ex)
				{
					result = null;
				}
			}

		} else
		{
			// if no result found return nothing.

			result = null;

			if (testPropertiesList.get(col).getDataType().equals("boolean"))
			{
				// Convert the data from the table into the expected type of boolean 
				result = false;

			}
		}

		return result;
	}
}
