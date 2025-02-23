package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBViewQMPanelResults;
import com.commander4j.sys.Common;

public class JDBViewQMPanelResultsTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;

	public static final int PanelID_Col = 0;
	public static final int TrayID_Col = 1;
	public static final int SampleID_Col = 2;
	public static final int SampleDate_Col = 3;
	public static final int PanelDate_Col = 4;
	public static final int Status_Col = 5;
	public static final int TrayDescription_Col = 6;
	public static final int Plant_Col = 7;
	public static final int UserID_Col = 8;
	public static final int Result_Col = 9;
	public static final int ResultDescription_Col = 10;
	public static final int ProcessOrder_Col = 11;
	public static final int Material_Col = 12;
    public static final int ContainerCode_Col = 13;
    public static final int UserData2_Col = 14;
    public static final int UserData3_Col = 15;
    public static final int ProductGroup_Col = 16;
	public static final int FirstName_Col =17;
	public static final int Surname_Col =18;
	private JDBLanguage lang = new JDBLanguage(Common.selectedHostID, Common.sessionID);

	private String[] mcolNames = {  "Panel ID","Tray ID", "Sample ID","Sample Date",  "Panel Date","Status","Tray Description","Plant","User ID","Result","Result Description","Process Order",  "Material","Recipe",lang.get("lbl_User_Data2"),lang.get("lbl_User_Data3"),"Product Group","First Name","Surname"};
	private ResultSet mResultSet;

	private int prowCount = -1;

	private HashMap<Integer,JDBViewQMPanelResults> cache = new HashMap<Integer,JDBViewQMPanelResults>();

	public JDBViewQMPanelResultsTableModel(ResultSet rs)
	{
		super();
		try
		{
			rs.setFetchSize(1000);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount() {
		int count = mcolNames.length;

		return count;
	}

	public int getRowCount() {

		try
		{
			if (prowCount <= 0)
			{
				mResultSet.last();
				prowCount = mResultSet.getRow();
				mResultSet.beforeFirst();
			}
			return prowCount;

		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public void setValueAt(Object value, int row, int col) {

	}

	public String getColumnName(int col) {
		return mcolNames[col];
	}

	public Object getValueAt(int row, int col) {

		try
		{
			if (cache.containsKey(row)==false)
			{
				mResultSet.absolute(row + 1);
				final JDBViewQMPanelResults prow = new JDBViewQMPanelResults(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}

			switch (col)
			{
			case PanelID_Col:
				return cache.get(row).getPanelID();
			case TrayID_Col:
				return cache.get(row).getTrayID();
			case SampleID_Col:
				return cache.get(row).getSampleID();
			case SampleDate_Col:
				return cache.get(row).getSampleDate().toString().substring(0, 16);
			case PanelDate_Col:
				return cache.get(row).getPanelDate().toString().substring(0, 16);
			case Status_Col:
				return cache.get(row).getStatus();
			case TrayDescription_Col:
				return cache.get(row).getTrayDescription();
			case Plant_Col:
				return cache.get(row).getPlant();
			case UserID_Col:
				return cache.get(row).getUserID();
			case Result_Col:
				return cache.get(row).getResult();
			case ResultDescription_Col:
				return cache.get(row).getResultDescription();
			case ProcessOrder_Col:
				return cache.get(row).getProcessOrder();
			case Material_Col:
				return cache.get(row).getMaterial();
			case ContainerCode_Col:
				return cache.get(row).getContainerCode();
			case UserData2_Col:
				return cache.get(row).getUserData2();
			case UserData3_Col:
				return cache.get(row).getUserData3();
			case ProductGroup_Col:
				return cache.get(row).getProductGroup();
			case FirstName_Col:
				return cache.get(row).getFirstName();
			case Surname_Col:
				return cache.get(row).getSurname();

			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}

}
