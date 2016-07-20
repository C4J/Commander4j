package com.commander4j.tablemodel;

import java.sql.ResultSet;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.commander4j.db.JDBLanguage;
import com.commander4j.sys.Common;

/**
 */
public class JDBLanguageTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1;
	public static final int resource_key_col = 0;
	public static final int language_id_col = 1;
	public static final int text_col = 2;
	public static final int mnemonic_col = 3;

	private String[] mcolNames = { "Resource Key", "Language ID", "Text", "Mnemonic" };
	private ResultSet mResultSet;

	private int prowCount = -1;
	private HashMap<Integer,JDBLanguage> cache = new HashMap<Integer,JDBLanguage>();

	public JDBLanguageTableModel()
	{

	}

	public JDBLanguageTableModel(ResultSet rs)
	{
		super();
		prowCount = -1;
		mResultSet = rs;
	}

	public int getColumnCount() {
		return mcolNames.length;
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
				final JDBLanguage prow = new JDBLanguage(Common.selectedHostID, Common.sessionID);
				prow.getPropertiesfromResultSet(mResultSet);
				cache.put(row, prow);
			}
			switch (col)
			{
			case resource_key_col:
				return cache.get(row).getKey();
			case language_id_col:
				return cache.get(row).getLanguage();
			case text_col:
				return cache.get(row).getText();
			case mnemonic_col:
				return cache.get(row).getMnemonicString();
			}
		}
		catch (Exception ex)
		{
			return "Error";
		}

		return new String();
	}
}
