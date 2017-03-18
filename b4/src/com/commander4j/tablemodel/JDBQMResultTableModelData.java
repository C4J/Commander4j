/*
 * Created on 02-Mar-2005
 *
 */
package com.commander4j.tablemodel;

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

	public JDBQMResultTableModelData(String hostid, String sessionid, String processOrder, String inspectionid, String activityid)
	{
		super();
		setHost(hostid);
		setSession(sessionid);
		JDBQMDictionary tempDict;
		sample = new JDBQMSample(hostid, sessionid);
		test = new JDBQMTest(hostid, sessionid);

		res = new JDBQMResult(hostid, sessionid);
		select = new JDBQMSelectList(hostid, sessionid);
		testPropertiesList = test.getTestsPropertiesList(inspectionid, activityid);

		sampleList = sample.getSamples(processOrder, inspectionid, activityid);
		testList = test.getTests(inspectionid, activityid);

		for (int x = 0; x < testPropertiesList.size(); x++)
		{
			tempDict = ((JDBQMDictionary) testPropertiesList.get(x));

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

		sampleID = sampleList.get(row).getSampleID();
		testID = testList.get(col).getTestID();

		if (testPropertiesList.get(col).getDataType().equals("list"))
		{
			try
			{
				newValue = ((JDBQMSelectList) value).getValue();
			} catch (Exception ex)
			{
				newValue = "";
			}
		} else
		{
			newValue = value.toString();
		}

		if (res.getResultsProperties(sampleID, testID))
		{
			if (newValue.equals(res.getValue()) == false)
			{
				res.setStatus("Updated");
				res.setUserID(Common.userList.getUser(getSession()).getUserId());
				res.setUpdated(JUtility.getSQLDateTime());
				res.setValue(newValue);
				res.update();
			}
		} else
		{

			if (newValue.equals("") == false)
			{
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
			// Ensure the result Object is populated with correct type;
			if (testPropertiesList.get(col).getDataType().equals("numeric"))
			{
				try
				{
					result = new BigDecimal(res.getValue());
				} catch (Exception ex)
				{
					result = null;
				}
			}

			if (testPropertiesList.get(col).getDataType().equals("boolean"))
			{
				try
				{
					result = Boolean.valueOf(res.getValue());
				} catch (Exception ex)
				{
					result = false;
				}
			}

			if (testPropertiesList.get(col).getDataType().equals("list"))
			{
				try
				{
					JDBQMSelectList sl = new JDBQMSelectList();
					sl.setSelectListID(listID.get(col));
					//System.out.println("Cell value is :["+res.getValue()+  "] row "+String.valueOf(row)+" col "+String.valueOf(col));
					sl.setValue(res.getValue());
					select.setSelectListID(listID.get(col));
					select.setValue(res.getValue());
					select.getProperties();
					sl.setDescription(select.getDescription());
					result = sl;
				} catch (Exception ex)
				{
					result = null;
				}
			}

			if (testPropertiesList.get(col).getDataType().equals("string"))
			{
				try
				{
					result = String.valueOf(res.getValue());
				} catch (Exception ex)
				{
					result = null;
				}
			}

			if (testPropertiesList.get(col).getDataType().equals("integer"))
			{
				try
				{
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

					result = false;

			}
		}

		return result;
	}
}
