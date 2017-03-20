package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JExcel.java
 * 
 * Package Name : com.commander4j.util
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

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JSpinner;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.commander4j.gui.JCheckBox4j;


public class JExcel
{

	private String dbErrorMessage;
	private final Logger logger = Logger.getLogger(JExcel.class);
	

	public void setExcelRowLimit(JCheckBox4j cb,JSpinner spin)
	{
		if (cb.isSelected())
		{
			if (Long.valueOf(spin.getValue().toString())>65535)
			{
				spin.setValue(65535);
			}
		}
		else
		{
			spin.setValue(65535);
			cb.setSelected(true);
		}
		
		if (cb.isSelected())
		{
			spin.setEnabled(true);
		}
		else
		{
			spin.setEnabled(false);
		}
		
		spin.repaint();
		cb.repaint();
	}
	
	public void saveAs(String defaultFilename, ResultSet rs, Component parent) {

		JFileChooser saveXLS = new JFileChooser();

		try
		{
			File f = new File(new File(System.getProperty("user.home")).getCanonicalPath());
			saveXLS.setCurrentDirectory(f);
			saveXLS.addChoosableFileFilter(new JFileFilterXLS());
			saveXLS.setSelectedFile(new File(defaultFilename));
		}
		catch (Exception ex)
		{
		}

		int result = saveXLS.showSaveDialog(parent);
		if (result == 0)
		{
			File selectedFile;
			selectedFile = saveXLS.getSelectedFile();
			if (selectedFile != null)
			{
				String filename = selectedFile.getAbsolutePath();
				JExcel export = new JExcel();
				export.exportToExcel(filename, rs);
			}
		}
	}

	public void exportToExcel(String filename, String sqlText, Connection connection) {
		PreparedStatement statement;
		try
		{
			statement = connection.prepareStatement(sqlText);
			exportToExcel(filename, statement);
			statement.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public void exportToExcel(String filename, PreparedStatement statement) {
		ResultSet rs;
		try
		{
			rs = statement.executeQuery();
			exportToExcel(filename, rs);
			rs.close();
		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	public void exportToExcel(String filename, ResultSet rs) {
		try
		{

			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			int columnType = 0;
			String columnTypeName = "";
			int recordNumber = 0;
			int passwordCol = -1;

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();

			HSSFCellStyle cellStyle_varchar = workbook.createCellStyle();
			cellStyle_varchar.setAlignment(HSSFCellStyle.ALIGN_LEFT);

			HSSFCellStyle cellStyle_nvarchar = workbook.createCellStyle();
			cellStyle_nvarchar.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			
			HSSFCellStyle cellStyle_varchar2 = workbook.createCellStyle();
			cellStyle_varchar2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			
			HSSFCellStyle cellStyle_title = workbook.createCellStyle();
			cellStyle_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle cellStyle_char = workbook.createCellStyle();
			cellStyle_char.setAlignment(HSSFCellStyle.ALIGN_LEFT);

			HSSFCellStyle cellStyle_date = workbook.createCellStyle();
			cellStyle_date.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle_date.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle cellStyle_timestamp = workbook.createCellStyle();
			cellStyle_timestamp.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle_timestamp.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

			HSSFCellStyle cellStyle_decimal = workbook.createCellStyle();
			cellStyle_decimal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

			HSSFFont font_title = workbook.createFont();
			font_title.setColor((short) 0xc);
			font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font_title.setItalic(true);
			font_title.setUnderline(HSSFFont.U_DOUBLE);
			cellStyle_title.setFont(font_title);

			HSSFCell cell;
			HSSFRow row;

			// rs.beforeFirst();

			while (rs.next())
			{
				recordNumber++;

				if (recordNumber == 1)
				{
					row = sheet.createRow((int) 0);
					for (int column = 1; column <= numberOfColumns; column++)
					{
						cell = row.createCell((int) (column - 1));
						String columnName = rsmd.getColumnLabel(column);
						columnName = columnName.replace("_", " ");
						columnName = JUtility.capitalize(columnName);
						cell.setCellStyle(cellStyle_title);
						cell.setCellValue(columnName);
						if (columnName.equals("Password"))
						{
							passwordCol = column;
						}
					}
				}

				row = sheet.createRow((int) recordNumber);

				for (int column = 1; column <= numberOfColumns; column++)
				{

					columnType = rsmd.getColumnType(column);
					columnTypeName = rsmd.getColumnTypeName(column);

					cell = row.createCell((int) (column - 1));

					try
					{
						switch (columnType)
						{
						case java.sql.Types.NVARCHAR:
							HSSFRichTextString rtf_nvarchar;
							if (column == passwordCol)
							{
								rtf_nvarchar = new HSSFRichTextString("*****");
							}
							else
							{
								rtf_nvarchar = new HSSFRichTextString(rs.getString(column));
							}
						
							cell.setCellStyle(cellStyle_nvarchar);
							cell.setCellValue(rtf_nvarchar);
							break;
						case java.sql.Types.VARCHAR:
							HSSFRichTextString rtf_varchar;
							if (column == passwordCol)
							{
								rtf_varchar = new HSSFRichTextString("*****");
							}
							else
							{
								rtf_varchar = new HSSFRichTextString(rs.getString(column));
							}
						
							cell.setCellStyle(cellStyle_varchar);
							cell.setCellValue(rtf_varchar);
							break;
						case java.sql.Types.CHAR:
							HSSFRichTextString rtf_char = new HSSFRichTextString(rs.getString(column));
							cell.setCellStyle(cellStyle_char);
							cell.setCellValue(rtf_char);
							break;
						case java.sql.Types.DATE:
							try
							{
								cell.setCellValue(rs.getTimestamp(column));
								cell.setCellStyle(cellStyle_date);
							}
							catch (Exception ex)
							{

							}
							break;
						case java.sql.Types.TIMESTAMP:
							try
							{
								cell.setCellValue(rs.getTimestamp(column));
								cell.setCellStyle(cellStyle_timestamp);
							}
							catch (Exception ex)
							{

							}
							break;
						case java.sql.Types.DECIMAL:
							HSSFRichTextString rtf_decimal = new HSSFRichTextString(rs.getBigDecimal(column).toString());
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_decimal);
							break;
						case java.sql.Types.NUMERIC:
							HSSFRichTextString rtf_decimaln = new HSSFRichTextString(rs.getBigDecimal(column).toString());
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_decimaln);
							break;
						case java.sql.Types.BIGINT:
							HSSFRichTextString rtf_bigint = new HSSFRichTextString(rs.getBigDecimal(column).toString());
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_bigint);
							break;
						case java.sql.Types.INTEGER:
							HSSFRichTextString rtf_int = new HSSFRichTextString(String.valueOf(rs.getInt(column)));
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_int);
							break;
						case java.sql.Types.FLOAT:
							HSSFRichTextString rtf_float = new HSSFRichTextString(String.valueOf(rs.getFloat(column)));
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_float);
							break;
						case java.sql.Types.DOUBLE:
							HSSFRichTextString rtf_double = new HSSFRichTextString(String.valueOf(rs.getDouble(column)));
							cell.setCellStyle(cellStyle_decimal);
							cell.setCellValue(rtf_double);
							break;
						default:
							cell.setCellValue(new HSSFRichTextString(columnTypeName));
							break;
						}
					}
					catch (Exception ex)
					{
						String errormessage = ex.getLocalizedMessage();
						HSSFRichTextString rtf_exception = new HSSFRichTextString(errormessage);
						cell.setCellStyle(cellStyle_varchar);
						cell.setCellValue(rtf_exception);
						break;
					}
				}
				
				if (recordNumber == 65535)
				{
					break;
				}
			}

			for (int column = 1; column <= numberOfColumns; column++)
			{
				sheet.autoSizeColumn((int) (column - 1));
			}

			if (recordNumber > 0)
			{
				try
				{
					FileOutputStream fileOut = new FileOutputStream(filename.toLowerCase());
					workbook.write(fileOut);
					fileOut.close();
				}
				catch (Exception ex)
				{
					setErrorMessage(ex.getMessage());
				}
			}

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	private void setErrorMessage(String errorMsg) {
		if (errorMsg.isEmpty() == false)
		{
			logger.error(errorMsg);
		}
		dbErrorMessage = errorMsg;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

}
