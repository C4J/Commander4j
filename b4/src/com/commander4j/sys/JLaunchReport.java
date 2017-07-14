
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLaunchReport.java
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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleAlternative;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;
import com.commander4j.util.OSValidator;

public class JLaunchReport {

	public static boolean silentExceptions = false;
	public static JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);
	public static JDBModuleAlternative modalt = new JDBModuleAlternative(Common.selectedHostID, Common.sessionID);
	public static Map<String, String> stdparams = new HashMap<String, String>();

	public static void runReportToPDF(String moduleId, HashMap<String, Object> parameterValues, String sql, PreparedStatement preparedstatement, String filename)
	{
		ResultSet resultset;
		Statement statement;
		JRResultSetDataSource jasperresultset;
		JasperPrint jasperPrint = new JasperPrint();
		Connection connection;
		String reportFilename = "";

		init();

		moduleId = modalt.substituteAlternative(moduleId);
		
		mod.setModuleId(moduleId);

		if (mod.getModuleProperties() == true)
		{

			reportFilename = Common.report_path + JUtility.removeExtensionFromFilename(mod.getReportFilename(), ".jrxml") + ".jasper";

			if (parameterValues == null)
			{
				parameterValues = new HashMap<String, Object>();
			}
			parameterValues.putAll(stdparams);

			try
			{

				connection = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID);

				sql = JUtility.replaceNullStringwithBlank(sql);

				if (sql.isEmpty())
				{
					if (preparedstatement == null)
					{
						parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
						jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, connection);
					} else
					{
						resultset = preparedstatement.executeQuery();
						jasperresultset = new JRResultSetDataSource(resultset);
						parameterValues.put(JRParameter.REPORT_DATA_SOURCE, jasperresultset);
						parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
						jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, jasperresultset);
					}
				} else
				{
					statement = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).createStatement();
					statement.setFetchSize(1);
					resultset = statement.executeQuery(sql);
					jasperresultset = new JRResultSetDataSource(resultset);
					parameterValues.put(JRParameter.REPORT_DATA_SOURCE, jasperresultset);
					parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
					jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, jasperresultset);
				}

				System.out.println(filename);
				JasperExportManager.exportReportToPdfFile(jasperPrint, filename);

			} catch (Exception e)
			{
				reportError(e.getMessage());
			}

			try
			{
				jasperresultset = null;
				preparedstatement = null;
				resultset = null;
				connection = null;
				jasperPrint = null;

			} catch (Exception e)
			{
			}

		} else
		{
			reportError("Cannot find module [" + moduleId + "]");
		}
	}

	public static void init()
	{
		JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
		ctrl.getProperties("COMPANY NAME");
		stdparams.put("COMPANY_NAME", ctrl.getKeyValue());
		ctrl.getProperties("PLANT");
		stdparams.put("PLANT", ctrl.getKeyValue());
		stdparams.put("SUBREPORT_DIR", System.getProperty("user.dir") + File.separator + "reports" + File.separator);
		System.getProperty("user.dir");
		ctrl.getProperties("LABEL_HEADER_COMMENT");

		DefaultJasperReportsContext jasperContext = DefaultJasperReportsContext.getInstance();
		JRPropertiesUtil.getInstance(jasperContext).setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
		JRPropertiesUtil.getInstance(jasperContext).setProperty("net.sf. jasperreports.properties", Common.report_path + "default.jasperreports.properties");

	}

	public static void runReport(String moduleId, HashMap<String, Object> parameterValues, String sql, PreparedStatement preparedstatement, String printQueue)
	{
		ResultSet resultset;
		Statement statement;
		JRResultSetDataSource jasperresultset;
		JasperPrint jasperPrint = new JasperPrint();
		Connection connection;
		JInternalFrameReportViewer reportviewer;
		Icon reportIcon;
		JRPrintServiceExporter exporter;
		// PrintServiceAttributeSet serviceAttributeSet;
		String reportFilename = "";
		
		moduleId = modalt.substituteAlternative(moduleId);

		mod.setModuleId(moduleId);

		if (mod.getModuleProperties() == true)
		{
			reportFilename = Common.report_path + JUtility.removeExtensionFromFilename(mod.getReportFilename(), ".jrxml") + ".jasper";

			if (parameterValues == null)
			{
				parameterValues = new HashMap<String, Object>();
			}
			parameterValues.putAll(stdparams);

			printQueue = JUtility.replaceNullStringwithBlank(printQueue);

			if (printQueue.equals(""))
			{
				printQueue = JPrint.getDefaultPrinterQueueName();
			} else
			{
				if (JPrint.getDefaultPrinterQueueName().equals(printQueue) == false)
				{
					JPrint.setPreferredPrinterQueueName(printQueue);
				}
			}

			try
			{

				connection = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID);

				sql = JUtility.replaceNullStringwithBlank(sql);

				if (sql.isEmpty())
				{
					if (preparedstatement == null)
					{
						parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
						jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, connection);
					} else
					{
						resultset = preparedstatement.executeQuery();
						jasperresultset = new JRResultSetDataSource(resultset);
						parameterValues.put(JRParameter.REPORT_DATA_SOURCE, jasperresultset);
						parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
						jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, jasperresultset);
					}
				} else
				{
					statement = Common.hostList.getHost(Common.selectedHostID).getConnection(Common.sessionID).createStatement();
					statement.setFetchSize(1);
					resultset = statement.executeQuery(sql);
					jasperresultset = new JRResultSetDataSource(resultset);
					parameterValues.put(JRParameter.REPORT_DATA_SOURCE, jasperresultset);
					parameterValues.put(JRParameter.REPORT_CONNECTION, connection);
					jasperPrint = JasperFillManager.fillReport(reportFilename, parameterValues, jasperresultset);
				}

				if (mod.isPrintPreview() == true)
				{
					reportviewer = new JInternalFrameReportViewer(jasperPrint, false);
					reportIcon = Common.imageIconloader.getImageIcon(Common.image_report);
					reportviewer.setFrameIcon(reportIcon);
					reportviewer.setLocation(JLaunchMenu.getNextWindowPosition().x, JLaunchMenu.getNextWindowPosition().y);
					Common.mainForm.desktopPane.add(reportviewer);
					reportviewer.setVisible(true);
					reportviewer.setSelected(true);

				} else
				{

					exporter = new JRPrintServiceExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

					PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
					printRequestAttributeSet.add(new Copies(mod.getPrintCopies()));


					PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
					
					if (OSValidator.isWindows())
					{
						printServiceAttributeSet.add(new PrinterName(JPrint.getPrinterServicebyName(printQueue).getName(), Locale.getDefault()));
					}
					else
					{
						printServiceAttributeSet.add(new PrinterName(JPrint.correctPrinterQueuename(JPrint.getPrinterServicebyName(printQueue).getName()), Locale.getDefault()));
					}
					
					SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
					configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
					configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
					configuration.setDisplayPageDialog(false);
					configuration.setDisplayPrintDialog(mod.isPrintDialog());

					exporter.setConfiguration(configuration);
					exporter.exportReport();

				}

			} catch (Exception e)
			{
				reportError(e.getMessage());
			}

			try
			{
				jasperresultset = null;
				preparedstatement = null;
				resultset = null;
				connection = null;
				reportviewer = null;
				jasperPrint = null;

			} catch (Exception e)
			{
			}

		} else
		{
			reportError("Cannot find module [" + moduleId + "]");
		}
	}

	public static void runReport(String ModuleId, PreparedStatement ps, boolean preview, String printQueue, int labelCopies, boolean incHeaderText)
	{
		
		ModuleId = modalt.substituteAlternative(ModuleId);
		mod.setModuleId(ModuleId);
		if (mod.getModuleProperties() == true)
		{
			if (mod.getReportType().equals("Standard"))
			{

				runReport(ModuleId, null, "", ps, printQueue);
			} else
			{
				JLabelPrint lp = new JLabelPrint(Common.selectedHostID, Common.sessionID);
				lp.initLabelStaticData(printQueue, mod.getReportFilename(), ps);
				lp.print(labelCopies, incHeaderText);
			}

		} else
		{
			reportError("Cannot find module [" + ModuleId + "]");

		}
	}

	public static void reportError(String errorMessage)
	{
		if (silentExceptions == false)
		{
			JOptionPane.showMessageDialog(Common.mainForm, errorMessage, "Report Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
