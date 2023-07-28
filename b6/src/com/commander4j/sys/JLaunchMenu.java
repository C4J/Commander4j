package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLaunchMenu.java
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
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import com.commander4j.app.*;
import com.commander4j.db.JDBModule;
import com.commander4j.interfaces.JInternalFrameInterfaceAdmin;
import com.commander4j.interfaces.JInternalFrameInterfaceControl;
import com.commander4j.interfaces.JInternalFrameInterfaceLog;
import com.commander4j.interfaces.JInternalFrameInterfaceProperties;
import com.commander4j.interfaces.JInternalFrameInterfaceRequestAdmin;
import com.commander4j.util.JUtility;

public class JLaunchMenu
{

	public static JDBModule mod = new JDBModule(Common.selectedHostID, Common.sessionID);

	public static Rectangle getNextWindowPosition()
	{
		Rectangle result = Common.mainForm.treeMenu.getBounds();
		Rectangle max = Common.mainForm.treeMenu.getBounds();

		if (Common.mainForm.treeMenu.isIcon() == false)
		{
			max.x = max.x + max.width;
			result.x = result.x + result.width;
		}

		int framecount = 0;
		int maxcount = 0;

		try
		{
			JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();
			for (int k = frames.length - 1; k >= 0; k--)
			{
				if (frames[k].getClass().equals(JInternalFrameMenuTree.class) == false)
				{
					if (frames[k].isIcon() == false)
					{
						if (frames[k].isSelected())
						{
							result = frames[k].getBounds();
							framecount++;
						}
						else
						{
							maxcount++;
							if (frames[k].getBounds().x > max.x)
							{
								max.x = frames[k].getBounds().x;
							}
							if (frames[k].getBounds().y > max.y)
							{
								max.y = frames[k].getBounds().y;
							}
						}
					}
					// framecount++;
				}
				if (framecount > 0)
				{
					result.x = result.x + 30;
					result.y = result.y + 30;
				}
				else
				{
					result.x = max.x;
					result.y = max.y;
					if (maxcount > 0)
					{
						result.x = result.x + 30;
						result.y = result.y + 30;
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return result;
	}

	public static void cascadeFrames()
	{

		try
		{
			// restoreAll();
			JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();

			Rectangle rect = Common.mainForm.treeMenu.getBounds();

			int x = rect.x;
			int y = rect.y;

			if (Common.mainForm.treeMenu.isIcon() == false)
			{
				x = rect.x + rect.width;
				y = rect.y;

			}

			for (int k = frames.length - 1; k >= 0; k--)
			{
				if (frames[k].getClass().equals(JInternalFrameMenuTree.class) == false)
				{
					if (frames[k].isIcon() == false)
					{
						frames[k].setIcon(false);
						frames[k].setSelected(true);
						frames[k].setLocation(x, y);
						x += 30;
						y += 30;
					}
				}
			}

		}
		catch (Exception ex)
		{

		}
	}

	private static void displayDialog(JDialog u, String optionName)
	{

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = u.getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);

		u.setLocation(leftmargin, topmargin);
		u.setModal(true);
		u.setVisible(true);
	}

	private static void displayForm(JInternalFrame u, String optionName)
	{
		u.setFrameIcon(JDBModule.getModuleIcon16x16(mod.getIconFilename(), mod.getType()));

		Dimension screensize = Common.mainForm.getSize();

		Dimension formsize = u.getSize();
		int leftmargin = ((screensize.width - formsize.width) / 2);
		int topmargin = ((screensize.height - formsize.height) / 2);
		leftmargin = getNextWindowPosition().x;
		topmargin = getNextWindowPosition().y;
		u.setLocation(leftmargin, topmargin);

		Common.mainForm.desktopPane.add(u);
		try
		{
			u.setSelected(true);
		}
		catch (Exception ex)
		{
			JUtility.errorBeep();
		}
	}

	private static boolean isLoaded(Class<?> u)
	{
		boolean result = false;
		JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();

		for (int k = frames.length - 1; k >= 0; k--)
		{
			if (frames[k].getClass().equals(u))
			{
				result = true;
			}
		}
		return result;
	}

	private static JInternalFrame isLoadedInstance(Class<?> u)
	{
		JInternalFrame result = new JInternalFrame();
		JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();

		for (int k = frames.length - 1; k >= 0; k--)
		{
			if (frames[k].getClass().equals(u))
			{
				result = frames[k];
			}
		}
		return result;
	}

	public static void minimizeAll()
	{
		try
		{
			JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();
			for (int k = frames.length - 1; k >= 0; k--)
			{
				if (frames[k].getClass().equals(JInternalFrameMenuTree.class) == false)
				{
					frames[k].setIcon(true);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void restoreAll()
	{
		try
		{
			JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();

			for (int k = 0; k <= (frames.length - 1); k++)
			{
				if (frames[k].getClass().equals(JInternalFrameMenuTree.class) == false)
				{
					frames[k].setIcon(false);
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void runDialog(String optionName)
	{

		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_ABOUT"))
		{
			final JDialogAbout u;
			u = new JDialogAbout(Common.mainForm);

			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_LICENCES"))
		{
			final JDialogLicenses u;
			u = new JDialogLicenses(Common.mainForm);

			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_SYSTEM_PROPERTIES"))
		{
			final JDialogSysInfo u;
			u = new JDialogSysInfo(Common.mainForm);

			displayDialog(u, optionName);
		}
	}

	public static void runDialog(String optionName, String strParam)
	{

		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_ADMIN_MODULE_ALTERNATE"))
		{
			final JDialogModuleAlternative u;
			u = new JDialogModuleAlternative(Common.mainForm, strParam);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_DESPATCH_EQUIPMENT"))
		{
			final JDialogDespatchEquipment u;
			u = new JDialogDespatchEquipment(Common.mainForm, strParam);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_DATA_IDS_EDIT"))
		{
			final JDialogDataIDProperties u;
			u = new JDialogDataIDProperties(Common.mainForm, strParam);
			displayDialog(u, optionName);

		}

		if (optionName.equals("FRM_PROCESS_ORDER_LABEL"))
		{
			final JInternalFrameProcessOrderLabel u;
			u = new JInternalFrameProcessOrderLabel(Common.mainForm, strParam);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_WEIGHT_ERROR"))
		{
			final JDialogWTError u;
			u = new JDialogWTError(Common.mainForm, strParam);
			displayDialog(u, optionName);

		}

		if (optionName.equals("FRM_ADMIN_CONTROL_EDIT"))
		{
			final JDialogControlProperties u;
			u = new JDialogControlProperties(Common.mainForm, strParam);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_USER_EDIT"))
		{
			final JDialogUserProperties u;
			u = new JDialogUserProperties(Common.mainForm, strParam);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_GROUP_EDIT"))
		{
			final JDialogeGroupProperties u;
			u = new JDialogeGroupProperties(Common.mainForm, strParam);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_ARCHIVE_EDIT"))
		{
			final JDialogArchiveProperties u;
			u = new JDialogArchiveProperties(Common.mainForm, strParam);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_LABEL_DATA_ASSIGN"))
		{
			final JDialogAssignLabelDataToLine u;
			u = new JDialogAssignLabelDataToLine(Common.mainForm, strParam);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_SHIFT"))
		{
			final JDialogShiftProperties u;
			u = new JDialogShiftProperties(Common.mainForm, strParam);
			u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_QM_INSPECTION"))
		{
			final JDialogQMInspectionProperties u;
			u = new JDialogQMInspectionProperties(Common.mainForm, strParam);
			u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_QM_DICTIONARY"))
		{
			final JDialogQMDictionaryProperties u;
			u = new JDialogQMDictionaryProperties(Common.mainForm, strParam);
			u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_PAL_LABEL_COPIES"))
		{
			final JDialogPalletRePrintLabel u;
			u = new JDialogPalletRePrintLabel(Common.mainForm, strParam);
			u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

	}

	public static void runDialog(String optionName, String strParam1, String strParam2, String strParam3)
	{

		mod.setModuleId(optionName);
		mod.getModuleProperties();
		if (optionName.equals("FRM_QM_TEST"))
		{
			final JDialogQMTestProperties u;
			u = new JDialogQMTestProperties(Common.mainForm, strParam1, strParam2, strParam3);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT"))
		{
			final JDialogMaterialCustomerDataProperties u;
			u = new JDialogMaterialCustomerDataProperties(Common.mainForm, strParam1, strParam2, strParam3);
			displayDialog(u, optionName);
		}

	}

	public static void runDialog(String optionName, String strParam1, String strParam2)
	{

		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_WEIGHT_REPORTS_DETAILS"))
		{
			final JDialogWTReportDetails u;
			u = new JDialogWTReportDetails(Common.mainForm, strParam1, strParam2);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_QM_ACTIVITY"))
		{
			final JDialogQMActivityProperties u;
			u = new JDialogQMActivityProperties(Common.mainForm, strParam1, strParam2);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_QM_SELECTLIST"))
		{
			final JDialogQMSelectListProperties u;
			u = new JDialogQMSelectListProperties(Common.mainForm, strParam1, strParam2);
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_AUTO_LAB_EDIT"))
		{

			final JDialogAutoLabellerProperties u;
			u = new JDialogAutoLabellerProperties(Common.mainForm, strParam1, strParam2);
			u.setTitle(mod.getDescription());
			displayDialog(u, optionName);

		}

		if (optionName.equals("FRM_ADMIN_PRINTER_EDIT"))
		{
			final JDialogPrinterProperties u;
			u = new JDialogPrinterProperties(Common.mainForm, strParam1, strParam2);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_BATCH_EDIT"))
		{
			final JDialogMaterialBatchProperties u;
			u = new JDialogMaterialBatchProperties(Common.mainForm, strParam1, strParam2);
			// u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}
	}

	public static void runForm(String optionName)
	{

		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_ADMIN_AUDIT_PERM"))
		{
			final JInternalFrameAuditPermissionsAdmin u;
			if (isLoaded(JInternalFrameAuditPermissionsAdmin.class))
				setVisible(JInternalFrameAuditPermissionsAdmin.class);
			else
			{
				u = new JInternalFrameAuditPermissionsAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_JOURNEY"))
		{
			final JInternalFrameJourneyAdmin u;
			if (isLoaded(JInternalFrameJourneyAdmin.class))
				setVisible(JInternalFrameJourneyAdmin.class);
			else
			{
				u = new JInternalFrameJourneyAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_DATA_IDS"))
		{
			final JInternalFrameDataIDAdmin u;
			if (isLoaded(JInternalFrameDataIDAdmin.class))
				setVisible(JInternalFrameDataIDAdmin.class);
			else
			{
				u = new JInternalFrameDataIDAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SCHEMA_BROWSE"))
		{
			final JInternalFrameUserReportSchema u;
			if (isLoaded(JInternalFrameUserReportSchema.class))
				setVisible(JInternalFrameUserReportSchema.class);
			else
			{
				u = new JInternalFrameUserReportSchema();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_SPLIT"))
		{
			final JInternalFramePalletSplit u;
			if (isLoaded(JInternalFramePalletSplit.class))
				setVisible(JInternalFramePalletSplit.class);
			else
			{
				u = new JInternalFramePalletSplit();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_RESULTS"))
		{
			final JInternalFrameQMSampleResults u;
			if (isLoaded(JInternalFrameQMSampleResults.class))
				setVisible(JInternalFrameQMSampleResults.class);
			else
			{
				u = new JInternalFrameQMSampleResults();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_LABEL"))
		{
			final JInternalFrameQMSampleLabel u;
			if (isLoaded(JInternalFrameQMSampleLabel.class))
				setVisible(JInternalFrameQMSampleLabel.class);
			else
			{
				u = new JInternalFrameQMSampleLabel("");
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_EDIT"))
		{
			final JInternalFrameQMSampleRecord u;
			if (isLoaded(JInternalFrameQMSampleRecord.class))
				setVisible(JInternalFrameQMSampleRecord.class);
			else
			{
				u = new JInternalFrameQMSampleRecord();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_QM_INSPECTION"))
		{
			final JInternalFrameQMInspectionAdmin u;
			if (isLoaded(JInternalFrameQMInspectionAdmin.class))
				setVisible(JInternalFrameQMInspectionAdmin.class);
			else
			{
				u = new JInternalFrameQMInspectionAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_RESULT_ENQUIRY"))
		{
			final JInternalFrameQMResultEnquiry u;
			if (isLoaded(JInternalFrameQMResultEnquiry.class))
				setVisible(JInternalFrameQMResultEnquiry.class);
			else
			{
				u = new JInternalFrameQMResultEnquiry();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_RESULT_ANALYSIS"))
		{
			final JInternalFrameQMResultAnalysis u;
			if (isLoaded(JInternalFrameQMResultAnalysis.class))
				setVisible(JInternalFrameQMResultAnalysis.class);
			else
			{
				u = new JInternalFrameQMResultAnalysis();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_PANEL_RESULTS"))
		{
			final JInternalFrameQMPanelResultsAdmin u;
			if (isLoaded(JInternalFrameQMPanelResultsAdmin.class))
				setVisible(JInternalFrameQMPanelResultsAdmin.class);
			else
			{
				u = new JInternalFrameQMPanelResultsAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_USER_REPORT"))
		{
			final JInternalFrameUserReportAdmin u;
			if (isLoaded(JInternalFrameUserReportAdmin.class))
				setVisible(JInternalFrameUserReportAdmin.class);
			else
			{
				u = new JInternalFrameUserReportAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_QM_SELECTLIST"))
		{
			final JInternalFrameQMSelectListAdmin u;
			if (isLoaded(JInternalFrameQMSelectListAdmin.class))
				setVisible(JInternalFrameQMSelectListAdmin.class);
			else
			{
				u = new JInternalFrameQMSelectListAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_QM_DICTIONARY"))
		{
			final JInternalFrameQMDictionaryAdmin u;
			if (isLoaded(JInternalFrameQMDictionaryAdmin.class))
				setVisible(JInternalFrameQMDictionaryAdmin.class);
			else
			{
				u = new JInternalFrameQMDictionaryAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_PROD_DEC"))
		{
			final JInternalFrameProductionDeclaration u;
			if (isLoaded(JInternalFrameProductionDeclaration.class))
				setVisible(JInternalFrameProductionDeclaration.class);
			else
			{
				u = new JInternalFrameProductionDeclaration("");
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_CM_PRINTERS"))
		{
			final JInternalFramePrinterSelect u;
			if (isLoaded(JInternalFramePrinterSelect.class))
				setVisible(JInternalFramePrinterSelect.class);
			else
			{
				u = new JInternalFramePrinterSelect();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE_PROCESS"))
		{
			final JInternalFrameInterfaceControl u;
			if (isLoaded(JInternalFrameInterfaceControl.class))
				setVisible(JInternalFrameInterfaceControl.class);
			else
			{
				u = new JInternalFrameInterfaceControl();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_LANGUAGE"))
		{
			final JInternalFrameLanguageAdmin u;
			if (isLoaded(JInternalFrameLanguageAdmin.class))
				setVisible(JInternalFrameLanguageAdmin.class);
			else
			{
				u = new JInternalFrameLanguageAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE"))
		{
			final JInternalFrameInterfaceAdmin u;
			if (isLoaded(JInternalFrameInterfaceAdmin.class))
				setVisible(JInternalFrameInterfaceAdmin.class);
			else
			{
				u = new JInternalFrameInterfaceAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE_REQUEST"))
		{
			final JInternalFrameInterfaceRequestAdmin u;
			if (isLoaded(JInternalFrameInterfaceRequestAdmin.class))
				setVisible(JInternalFrameInterfaceRequestAdmin.class);
			else
			{
				u = new JInternalFrameInterfaceRequestAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE_LOG"))
		{
			final JInternalFrameInterfaceLog u;
			if (isLoaded(JInternalFrameInterfaceLog.class))
				setVisible(JInternalFrameInterfaceLog.class);
			else
			{
				u = new JInternalFrameInterfaceLog();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_TOOLBAR"))
		{
			final JInternalFrameToolbar u;
			if (isLoaded(JInternalFrameToolbar.class))
				setVisible(JInternalFrameToolbar.class);
			else
			{
				u = new JInternalFrameToolbar();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_RF_MENU"))
		{
			final JInternalFrameRFMenu u;
			if (isLoaded(JInternalFrameRFMenu.class))
				setVisible(JInternalFrameRFMenu.class);
			else
			{
				u = new JInternalFrameRFMenu();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PROCESS_ORDER"))
		{
			final JInternalFrameProcessOrderAdmin u;
			if (isLoaded(JInternalFrameProcessOrderAdmin.class))
				setVisible(JInternalFrameProcessOrderAdmin.class);
			else
			{
				u = new JInternalFrameProcessOrderAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_DESPATCH"))
		{
			final JInternalFrameDespatch u;
			if (isLoaded(JInternalFrameDespatch.class))
				setVisible(JInternalFrameDespatch.class);
			else
			{
				u = new JInternalFrameDespatch();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIALS"))
		{
			final JInternalFrameMaterialAdmin u;
			if (isLoaded(JInternalFrameMaterialAdmin.class))
				setVisible(JInternalFrameMaterialAdmin.class);
			else
			{
				u = new JInternalFrameMaterialAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_BATCH"))
		{
			final JInternalFrameMaterialBatchAdmin u;
			if (isLoaded(JInternalFrameMaterialBatchAdmin.class))
				setVisible(JInternalFrameMaterialBatchAdmin.class);
			else
			{
				u = new JInternalFrameMaterialBatchAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_WASTE_LOG"))
		{
			final JInternalFrameWasteLogAdmin u;
			if (isLoaded(JInternalFrameWasteLogAdmin.class))
				setVisible(JInternalFrameWasteLogAdmin.class);
			else
			{
				u = new JInternalFrameWasteLogAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_WASTE_REPORTING"))
		{
			final JInternalFrameWasteReporting u;
			if (isLoaded(JInternalFrameWasteReporting.class))
				setVisible(JInternalFrameWasteReporting.class);
			else
			{
				u = new JInternalFrameWasteReporting();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_LOCATION"))
		{
			final JInternalFrameMaterialLocationAdmin u;
			if (isLoaded(JInternalFrameMaterialLocationAdmin.class))
				setVisible(JInternalFrameMaterialLocationAdmin.class);
			else
			{
				u = new JInternalFrameMaterialLocationAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PO_RESOURCE"))
		{
			final JInternalFrameProcessOrderResourceAdmin u;
			if (isLoaded(JInternalFrameProcessOrderResourceAdmin.class))
				setVisible(JInternalFrameProcessOrderResourceAdmin.class);
			else
			{
				u = new JInternalFrameProcessOrderResourceAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_TYPE"))
		{
			final JInternalFrameMaterialTypeAdmin u;
			if (isLoaded(JInternalFrameMaterialTypeAdmin.class))
				setVisible(JInternalFrameMaterialTypeAdmin.class);
			else
			{
				u = new JInternalFrameMaterialTypeAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN_REASON"))
		{
			final JInternalFrameMHNReasonAdmin u;
			if (isLoaded(JInternalFrameMHNReasonAdmin.class))
				setVisible(JInternalFrameMHNReasonAdmin.class);
			else
			{
				u = new JInternalFrameMHNReasonAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_REASON"))
		{
			final JInternalFrameSampleReasonAdmin u;
			if (isLoaded(JInternalFrameSampleReasonAdmin.class))
				setVisible(JInternalFrameSampleReasonAdmin.class);
			else
			{
				u = new JInternalFrameSampleReasonAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SUPPLIER"))
		{
			final JInternalFrameSupplierAdmin u;
			if (isLoaded(JInternalFrameSupplierAdmin.class))
				setVisible(JInternalFrameSupplierAdmin.class);
			else
			{
				u = new JInternalFrameSupplierAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_DEFECT_ID"))
		{
			final JInternalFrameSampleDefectIDAdmin u;
			if (isLoaded(JInternalFrameSampleDefectIDAdmin.class))
				setVisible(JInternalFrameSampleDefectIDAdmin.class);
			else
			{
				u = new JInternalFrameSampleDefectIDAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_DEFECT_TYPE"))
		{
			final JInternalFrameSampleDefectTypeAdmin u;
			if (isLoaded(JInternalFrameSampleDefectTypeAdmin.class))
				setVisible(JInternalFrameSampleDefectTypeAdmin.class);
			else
			{
				u = new JInternalFrameSampleDefectTypeAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN_DECISION"))
		{
			final JInternalFrameMHNDecisionAdmin u;
			if (isLoaded(JInternalFrameMHNDecisionAdmin.class))
				setVisible(JInternalFrameMHNDecisionAdmin.class);
			else
			{
				u = new JInternalFrameMHNDecisionAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_LOCATIONS"))
		{
			final JInternalFrameLocationAdmin u;
			if (isLoaded(JInternalFrameLocationAdmin.class))
				setVisible(JInternalFrameLocationAdmin.class);
			else
			{
				u = new JInternalFrameLocationAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN"))
		{
			final JInternalFrameMHNAdmin u;
			if (isLoaded(JInternalFrameMHNAdmin.class))
				setVisible(JInternalFrameMHNAdmin.class);
			else
			{
				u = new JInternalFrameMHNAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLETS"))
		{
			final JInternalFramePalletAdmin u;
			if (isLoaded(JInternalFramePalletAdmin.class))
				setVisible(JInternalFramePalletAdmin.class);
			else
			{
				u = new JInternalFramePalletAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_LABEL_PRINT"))
		{
			final JInternalFramePackLabelPrint u;
			if (isLoaded(JInternalFramePackLabelPrint.class))
				setVisible(JInternalFramePackLabelPrint.class);
			else
			{
				u = new JInternalFramePackLabelPrint("");
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLET_HISTORY"))
		{
			final JInternalFramePalletHistoryAdmin u;
			if (isLoaded(JInternalFramePalletHistoryAdmin.class))
				setVisible(JInternalFramePalletHistoryAdmin.class);
			else
			{
				u = new JInternalFramePalletHistoryAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLET_SAMPLE"))
		{
			final JInternalFramePalletSampleAdmin u;
			if (isLoaded(JInternalFramePalletSampleAdmin.class))
				setVisible(JInternalFramePalletSampleAdmin.class);
			else
			{
				u = new JInternalFramePalletSampleAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_UOM"))
		{
			final JInternalFrameUomAdmin u;
			if (isLoaded(JInternalFrameUomAdmin.class))
				setVisible(JInternalFrameUomAdmin.class);
			else
			{
				u = new JInternalFrameUomAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_SAMPLEPOINT"))
		{
			final JInternalFrameWTSamplePointAdmin u;
			if (isLoaded(JInternalFrameWTSamplePointAdmin.class))
				setVisible(JInternalFrameWTSamplePointAdmin.class);
			else
			{
				u = new JInternalFrameWTSamplePointAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_CONTAINERCODE"))
		{
			final JInternalFrameWTContainerCodeAdmin u;
			if (isLoaded(JInternalFrameWTContainerCodeAdmin.class))
				setVisible(JInternalFrameWTContainerCodeAdmin.class);
			else
			{
				u = new JInternalFrameWTContainerCodeAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_CAPTURE"))
		{
			final JInternalFrameWTWeightCapture u;
			if (isLoaded(JInternalFrameWTWeightCapture.class))
				setVisible(JInternalFrameWTWeightCapture.class);
			else
			{
				u = new JInternalFrameWTWeightCapture();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_REPORTS"))
		{
			final JInternalFrameWTReport u;
			if (isLoaded(JInternalFrameWTReport.class))
				setVisible(JInternalFrameWTReport.class);
			else
			{
				u = new JInternalFrameWTReport();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_WORKSTATION"))
		{
			final JInternalFrameWTWorkstationAdmin u;
			if (isLoaded(JInternalFrameWTWorkstationAdmin.class))
				setVisible(JInternalFrameWTWorkstationAdmin.class);
			else
			{
				u = new JInternalFrameWTWorkstationAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_SCALE"))
		{
			final JInternalFrameWTScaleAdmin u;
			if (isLoaded(JInternalFrameWTScaleAdmin.class))
				setVisible(JInternalFrameWTScaleAdmin.class);
			else
			{
				u = new JInternalFrameWTScaleAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_TNE"))
		{
			final JInternalFrameWTTNEAdmin u;
			if (isLoaded(JInternalFrameWTTNEAdmin.class))
				setVisible(JInternalFrameWTTNEAdmin.class);
			else
			{
				u = new JInternalFrameWTTNEAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_PRODUCT_GROUP"))
		{
			final JInternalFrameWTProductGroupAdmin u;
			if (isLoaded(JInternalFrameWTProductGroupAdmin.class))
				setVisible(JInternalFrameWTProductGroupAdmin.class);
			else
			{
				u = new JInternalFrameWTProductGroupAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_LOCATION"))
		{
			final JInternalFrameWasteLocationAdmin u;
			if (isLoaded(JInternalFrameWasteLocationAdmin.class))
				setVisible(JInternalFrameWasteLocationAdmin.class);
			else
			{
				u = new JInternalFrameWasteLocationAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_CONTAINER"))
		{
			final JInternalFrameWasteContainerAdmin u;
			if (isLoaded(JInternalFrameWasteContainerAdmin.class))
				setVisible(JInternalFrameWasteContainerAdmin.class);
			else
			{
				u = new JInternalFrameWasteContainerAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_EQUIPMENT_TYPE"))
		{
			final JInternalFrameEquipmentAdmin u;
			if (isLoaded(JInternalFrameEquipmentAdmin.class))
				setVisible(JInternalFrameEquipmentAdmin.class);
			else
			{
				u = new JInternalFrameEquipmentAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_LOCATION_REPORTING"))
		{
			final JInternalFrameWasteLocationReporting u;
			if (isLoaded(JInternalFrameWasteLocationReporting.class))
				setVisible(JInternalFrameWasteLocationReporting.class);
			else
			{
				u = new JInternalFrameWasteLocationReporting();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REPORTING_LOCATION"))
		{
			final JInternalFrameWasteReportingLocation u;
			if (isLoaded(JInternalFrameWasteReportingLocation.class))
				setVisible(JInternalFrameWasteReportingLocation.class);
			else
			{
				u = new JInternalFrameWasteReportingLocation();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REPORT_ID"))
		{
			final JInternalFrameWasteReportIDAdmin u;
			if (isLoaded(JInternalFrameWasteReportIDAdmin.class))
				setVisible(JInternalFrameWasteReportIDAdmin.class);
			else
			{
				u = new JInternalFrameWasteReportIDAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_TYPE"))
		{
			final JInternalFrameWasteTypeAdmin u;
			if (isLoaded(JInternalFrameWasteTypeAdmin.class))
				setVisible(JInternalFrameWasteTypeAdmin.class);
			else
			{
				u = new JInternalFrameWasteTypeAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_TRANSACTION"))
		{
			final JInternalFrameWasteTransactionAdmin u;
			if (isLoaded(JInternalFrameWasteTransactionAdmin.class))
				setVisible(JInternalFrameWasteTransactionAdmin.class);
			else
			{
				u = new JInternalFrameWasteTransactionAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_MATERIAL"))
		{
			final JInternalFrameWasteMaterialAdmin u;
			if (isLoaded(JInternalFrameWasteMaterialAdmin.class))
				setVisible(JInternalFrameWasteMaterialAdmin.class);
			else
			{
				u = new JInternalFrameWasteMaterialAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REASON"))
		{
			final JInternalFrameWasteReasonAdmin u;
			if (isLoaded(JInternalFrameWasteReasonAdmin.class))
				setVisible(JInternalFrameWasteReasonAdmin.class);
			else
			{
				u = new JInternalFrameWasteReasonAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SHIFT_NAMES"))
		{
			final JInternalFrameShiftNameAdmin u;
			if (isLoaded(JInternalFrameShiftNameAdmin.class))
				setVisible(JInternalFrameShiftNameAdmin.class);
			else
			{
				u = new JInternalFrameShiftNameAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}
		
		if (optionName.equals("FRM_ADMIN_OPERATIVES"))
		{
			final JInternalFrameOperativeAdmin u;
			if (isLoaded(JInternalFrameOperativeAdmin.class))
				setVisible(JInternalFrameOperativeAdmin.class);
			else
			{
				u = new JInternalFrameOperativeAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}
		
		if (optionName.equals("FRM_ADMIN_PACKING_LINES"))
		{
			final JInternalFramePackingLineAdmin u;
			if (isLoaded(JInternalFramePackingLineAdmin.class))
				setVisible(JInternalFramePackingLineAdmin.class);
			else
			{
				u = new JInternalFramePackingLineAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REPORTING_GROUP"))
		{
			final JInternalFrameWasteReportingGroupAdmin u;
			if (isLoaded(JInternalFrameWasteReportingGroupAdmin.class))
				setVisible(JInternalFrameWasteReportingGroupAdmin.class);
			else
			{
				u = new JInternalFrameWasteReportingGroupAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_CUSTOMER"))
		{
			final JInternalFrameCustomerAdmin u;
			if (isLoaded(JInternalFrameCustomerAdmin.class))
				setVisible(JInternalFrameCustomerAdmin.class);
			else
			{
				u = new JInternalFrameCustomerAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_AUTO_LABELLER"))
		{
			final JInternalFrameAutoLabellerLines u;
			if (isLoaded(JInternalFrameAutoLabellerLines.class))
				setVisible(JInternalFrameAutoLabellerLines.class);
			else
			{
				u = new JInternalFrameAutoLabellerLines();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MENU"))
		{
			final JInternalFrameMenuStructure u;
			if (isLoaded(JInternalFrameMenuStructure.class))
				setVisible(JInternalFrameMenuStructure.class);
			else
			{
				u = new JInternalFrameMenuStructure();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_SYSTEM_KEYS"))
		{
			final JInternalFrameControlAdmin u;
			if (isLoaded(JInternalFrameControlAdmin.class))
				setVisible(JInternalFrameControlAdmin.class);
			else
			{
				u = new JInternalFrameControlAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_ARCHIVE"))
		{
			final JInternalFrameArchiveAdmin u;
			if (isLoaded(JInternalFrameArchiveAdmin.class))
				setVisible(JInternalFrameArchiveAdmin.class);
			else
			{
				u = new JInternalFrameArchiveAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PRINTERS"))
		{
			final JInternalFramePrinterAdmin u;
			if (isLoaded(JInternalFramePrinterAdmin.class))
				setVisible(JInternalFramePrinterAdmin.class);
			else
			{
				u = new JInternalFramePrinterAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MODULES"))
		{
			final JInternalFrameModuleAdmin u;
			if (isLoaded(JInternalFrameModuleAdmin.class))
				setVisible(JInternalFrameModuleAdmin.class);
			else
			{
				u = new JInternalFrameModuleAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_USERS"))
		{
			final JInternalFrameUserAdmin u;
			if (isLoaded(JInternalFrameUserAdmin.class))
				setVisible(JInternalFrameUserAdmin.class);
			else
			{
				u = new JInternalFrameUserAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_GROUPS"))
		{
			final JInternalFrameGroupAdmin u;
			if (isLoaded(JInternalFrameGroupAdmin.class))
				setVisible(JInternalFrameGroupAdmin.class);
			else
			{
				u = new JInternalFrameGroupAdmin();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_PROD_CONFIRM"))
		{
			final JInternalFrameProductionConfirmation u;
			if (isLoaded(JInternalFrameProductionConfirmation.class))
				setVisible(JInternalFrameProductionConfirmation.class);
			else
			{
				u = new JInternalFrameProductionConfirmation();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_SAMPLE"))
		{
			final JInternalFramePalletSample u;
			if (isLoaded(JInternalFramePalletSample.class))
				setVisible(JInternalFramePalletSample.class);
			else
			{
				u = new JInternalFramePalletSample();
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

	}

	public static void runForm(String optionName, String StrParam1, String StrParam2, String StrParam3)
	{
		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_ADMIN_MATERIAL_CUST_DATA_EDIT"))
		{
			final JInternalFrameLabelHistory u;

			{
				u = new JInternalFrameLabelHistory(StrParam1, StrParam2);
				u.setTitle(mod.getDescription() + " [" + StrParam1 + "] [" + StrParam2 + "] [" + StrParam3 + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLET_HISTORY"))
		{
			final JInternalFramePalletHistoryAdmin u;

			if (isLoaded(JInternalFramePalletHistoryAdmin.class))
			{
				((JInternalFramePalletHistoryAdmin) isLoadedInstance(JInternalFramePalletHistoryAdmin.class)).updateSearch(StrParam1, StrParam2, StrParam3);
				setVisible(JInternalFramePalletHistoryAdmin.class);
			}
			else
			{
				u = new JInternalFramePalletHistoryAdmin(StrParam1, StrParam2, StrParam3);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLETS"))
		{
			final JInternalFramePalletAdmin u;

			if (isLoaded(JInternalFramePalletAdmin.class))
			{
				((JInternalFramePalletAdmin) isLoadedInstance(JInternalFramePalletAdmin.class)).updateSearch(StrParam1, StrParam2, StrParam3);
				setVisible(JInternalFramePalletAdmin.class);
			}
			else
			{
				u = new JInternalFramePalletAdmin(StrParam1, StrParam2, StrParam3);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

	}

	public static void runForm(String optionName, String StrParam1, Long StrParam2)
	{
		if (optionName.equals("FRM_PAL_SAMPLE_EDIT"))
		{
			final JInternalFramePalletSampleProperties u;
			if (isLoaded(JInternalFramePalletSampleProperties.class))
			{
				((JInternalFramePalletSampleProperties) isLoadedInstance(JInternalFramePalletSampleProperties.class)).setSampleID(StrParam1, StrParam2);
				setVisible(JInternalFramePalletSampleProperties.class);
			}
			else
			{
				u = new JInternalFramePalletSampleProperties(StrParam1, StrParam2);
				displayForm(u, optionName);
			}
		}
	}

	public static void runForm(String optionName, String StrParam1, String StrParam2)
	{
		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_WEIGHT_TNE_EDIT"))
		{
			final JInternalFrameWTTNEProperties u;
			if (isLoaded(JInternalFrameWTTNEProperties.class))
				((JInternalFrameWTTNEProperties) isLoadedInstance(JInternalFrameWTTNEProperties.class)).setTNE(StrParam1, StrParam2);
			else
			{
				u = new JInternalFrameWTTNEProperties(StrParam1, StrParam2);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE_LOG"))
		{
			final JInternalFrameInterfaceLog u;
			if (isLoaded(JInternalFrameInterfaceLog.class))
			{
				((JInternalFrameInterfaceLog) isLoadedInstance(JInternalFrameInterfaceLog.class)).updateSearch(StrParam1, StrParam2);
				setVisible(JInternalFrameInterfaceLog.class);
			}
			else
			{
				u = new JInternalFrameInterfaceLog(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PROCESS_ORDER"))
		{
			final JInternalFrameProcessOrderAdmin u;

			if (isLoaded(JInternalFrameProcessOrderAdmin.class))
			{
				((JInternalFrameProcessOrderAdmin) isLoadedInstance(JInternalFrameProcessOrderAdmin.class)).updateSearch(StrParam1, StrParam2);
				setVisible(JInternalFrameProcessOrderAdmin.class);
			}
			else
			{
				u = new JInternalFrameProcessOrderAdmin(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLET_HISTORY"))
		{
			final JInternalFramePalletHistoryAdmin u;

			if (isLoaded(JInternalFramePalletHistoryAdmin.class))
			{
				((JInternalFramePalletHistoryAdmin) isLoadedInstance(JInternalFramePalletHistoryAdmin.class)).updateSearch(StrParam1, StrParam2);
				setVisible(JInternalFramePalletHistoryAdmin.class);
			}
			else
			{
				u = new JInternalFramePalletHistoryAdmin(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLETS"))
		{
			final JInternalFramePalletAdmin u;

			if (isLoaded(JInternalFramePalletAdmin.class))
			{
				((JInternalFramePalletAdmin) isLoadedInstance(JInternalFramePalletAdmin.class)).updateSearch(StrParam1, StrParam2);
				setVisible(JInternalFramePalletAdmin.class);
			}
			else
			{
				u = new JInternalFramePalletAdmin(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_LABELLER_HISTORY"))
		{
			final JInternalFrameLabelHistory u;

			{
				u = new JInternalFrameLabelHistory(StrParam1, StrParam2);
				u.setTitle(mod.getDescription() + " [" + StrParam1 + "] " + StrParam2);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_LABEL"))
		{
			final JInternalFrameQMSampleLabel u;
			if (isLoaded(JInternalFrameQMSampleLabel.class))
			{
				((JInternalFrameQMSampleLabel) isLoadedInstance(JInternalFrameQMSampleLabel.class)).processOrderChanged(StrParam1);
				setVisible(JInternalFrameQMSampleLabel.class);
			}
			else
			{
				u = new JInternalFrameQMSampleLabel(StrParam1);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_LANGUAGE_EDIT"))
		{
			final JInternalFrameLanguageProperties u;
			if (isLoaded(JInternalFrameLanguageProperties.class))
				setVisible(JInternalFrameLanguageProperties.class);
			else
			{
				u = new JInternalFrameLanguageProperties(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_UOM"))
		{
			final JInternalFrameMaterialUomAdmin u;
			if (isLoaded(JInternalFrameMaterialUomAdmin.class))
				setVisible(JInternalFrameMaterialUomAdmin.class);
			else
			{
				u = new JInternalFrameMaterialUomAdmin(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_UOM_EDIT"))
		{
			final JInternalFrameMaterialUomProperties u;

			{
				u = new JInternalFrameMaterialUomProperties(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_WASTE_LOG_EDIT"))
		{
			final JInternalFrameWasteLogProperties u;

			{
				u = new JInternalFrameWasteLogProperties(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_LOCATION_EDIT"))
		{
			final JInternalFrameMaterialLocationProperties u;
			if (isLoaded(JInternalFrameMaterialLocationProperties.class))
				setVisible(JInternalFrameMaterialLocationProperties.class);
			else
			{
				u = new JInternalFrameMaterialLocationProperties(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_INTERFACE_EDIT"))
		{
			final JInternalFrameInterfaceProperties u;
			if (isLoaded(JInternalFrameInterfaceProperties.class))
				setVisible(JInternalFrameInterfaceProperties.class);
			else
			{
				u = new JInternalFrameInterfaceProperties(StrParam1, StrParam2);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

	}

	public static void runForm(JInternalFrame parent, String optionName, String StrParam)
	{
		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_ADMIN_MHN_ASSIGN"))
		{
			final JInternalFrameMHNAssign u;
			if (isLoaded(JInternalFrameMHNAssign.class))
				setVisible(JInternalFrameMHNAssign.class);
			else
			{
				u = new JInternalFrameMHNAssign((JInternalFrameMHNProperties) parent, StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}
	}

	public static void runForm(String optionName, String StrParam)
	{
		mod.setModuleId(optionName);
		mod.getModuleProperties();

		if (optionName.equals("FRM_WASTE_LOCATION_EDIT"))
		{
			final JInternalFrameWasteLocationProperties u;
			if (isLoaded(JInternalFrameWasteLocationProperties.class))
			{
				((JInternalFrameWasteLocationProperties) isLoadedInstance(JInternalFrameWasteLocationProperties.class)).setLocationID(StrParam);
				setVisible(JInternalFrameWasteLocationProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteLocationProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_CONTAINER_EDIT"))
		{
			final JInternalFrameWasteContainerProperties u;
			if (isLoaded(JInternalFrameWasteContainerProperties.class))
			{
				((JInternalFrameWasteContainerProperties) isLoadedInstance(JInternalFrameWasteContainerProperties.class)).setContainerID(StrParam);
				setVisible(JInternalFrameWasteContainerProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteContainerProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_EQUIPMENT_TYPE_EDIT"))
		{
			final JInternalFrameEquipmentProperties u;
			if (isLoaded(JInternalFrameEquipmentProperties.class))
			{
				((JInternalFrameEquipmentProperties) isLoadedInstance(JInternalFrameEquipmentProperties.class)).setEquipmentType(StrParam);
				setVisible(JInternalFrameEquipmentProperties.class);
			}
			else
			{
				u = new JInternalFrameEquipmentProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_TYPE_EDIT"))
		{
			final JInternalFrameWasteTypeProperties u;
			if (isLoaded(JInternalFrameWasteTypeProperties.class))
			{
				((JInternalFrameWasteTypeProperties) isLoadedInstance(JInternalFrameWasteTypeProperties.class)).setType(StrParam);
				setVisible(JInternalFrameWasteTypeProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteTypeProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REPORT_ID_EDIT"))
		{
			final JInternalFrameWasteReportIDProperties u;
			if (isLoaded(JInternalFrameWasteReportIDProperties.class))
			{
				((JInternalFrameWasteReportIDProperties) isLoadedInstance(JInternalFrameWasteReportIDProperties.class)).setReportID(StrParam);
				setVisible(JInternalFrameWasteReportIDProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteReportIDProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REPORTING_GROUP_EDIT"))
		{
			final JInternalFrameWasteReportingGroupProperties u;
			if (isLoaded(JInternalFrameWasteReportingGroupProperties.class))
			{
				((JInternalFrameWasteReportingGroupProperties) isLoadedInstance(JInternalFrameWasteReportingGroupProperties.class)).setGroup(StrParam);
				setVisible(JInternalFrameWasteReportingGroupProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteReportingGroupProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_TRANSACTION_EDIT"))
		{
			final JInternalFrameWasteTransactionProperties u;
			if (isLoaded(JInternalFrameWasteTransactionProperties.class))
			{
				((JInternalFrameWasteTransactionProperties) isLoadedInstance(JInternalFrameWasteTransactionProperties.class)).setTransactionType(StrParam);
				setVisible(JInternalFrameWasteTransactionProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteTransactionProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_REASON_EDIT"))
		{
			final JInternalFrameWasteReasonProperties u;
			if (isLoaded(JInternalFrameWasteReasonProperties.class))
			{
				((JInternalFrameWasteReasonProperties) isLoadedInstance(JInternalFrameWasteReasonProperties.class)).setReasonID(StrParam);
				setVisible(JInternalFrameWasteReasonProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteReasonProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WASTE_MATERIAL_EDIT"))
		{
			final JInternalFrameWasteMaterialProperties u;
			if (isLoaded(JInternalFrameWasteMaterialProperties.class))
			{
				((JInternalFrameWasteMaterialProperties) isLoadedInstance(JInternalFrameWasteMaterialProperties.class)).setMaterialID(StrParam);
				setVisible(JInternalFrameWasteMaterialProperties.class);
			}
			else
			{
				u = new JInternalFrameWasteMaterialProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_PRODUCT_GROUP_EDIT"))
		{
			final JInternalFrameWTProductGroupProperties u;
			if (isLoaded(JInternalFrameWTProductGroupProperties.class))
				((JInternalFrameWTProductGroupProperties) isLoadedInstance(JInternalFrameWTProductGroupProperties.class)).setMaterialGroup(StrParam);
			else
			{
				u = new JInternalFrameWTProductGroupProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_RESULT_ANALYSIS_EDIT"))
		{
			final JInternalFrameQMResultAnalysisProperties u;
			if (isLoaded(JInternalFrameQMResultAnalysisProperties.class))
				setVisible(JInternalFrameQMResultAnalysisProperties.class);
			else
			{
				u = new JInternalFrameQMResultAnalysisProperties(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_LABEL"))
		{
			final JInternalFrameQMSampleLabel u;
			if (isLoaded(JInternalFrameQMSampleLabel.class))
			{
				((JInternalFrameQMSampleLabel) isLoadedInstance(JInternalFrameQMSampleLabel.class)).processOrderChanged(StrParam);
				setVisible(JInternalFrameQMSampleLabel.class);
			}
			else
			{
				u = new JInternalFrameQMSampleLabel(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_LABEL_PRINT"))
		{
			final JInternalFramePackLabelPrint u;
			if (isLoaded(JInternalFramePackLabelPrint.class))
			{
				((JInternalFramePackLabelPrint) isLoadedInstance(JInternalFramePackLabelPrint.class)).processOrderChanged(StrParam);
				setVisible(JInternalFramePackLabelPrint.class);
			}
			else
			{
				u = new JInternalFramePackLabelPrint(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MODULE_MEMBERS"))
		{
			final JInternalFrameModuleGroups u;
			if (isLoaded(JInternalFrameModuleGroups.class))
				((JInternalFrameModuleGroups) isLoadedInstance(JInternalFrameModuleGroups.class)).setModuleID(StrParam);
			// setVisible(JInternalFrameUserReportProperties.class);
			else
			{
				u = new JInternalFrameModuleGroups(StrParam);
				// u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_GROUP_MEMBERS"))
		{
			final JInternalFrameGroupUsers u;
			if (isLoaded(JInternalFrameGroupUsers.class))
				((JInternalFrameGroupUsers) isLoadedInstance(JInternalFrameGroupUsers.class)).setGroupID(StrParam);
			else
			{
				u = new JInternalFrameGroupUsers(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_JOURNEY_EDIT"))
		{
			final JInternalFrameJourneyProperties u;
			if (isLoaded(JInternalFrameJourneyProperties.class))
				((JInternalFrameJourneyProperties) isLoadedInstance(JInternalFrameJourneyProperties.class)).setJourneyRef(StrParam);
			else
			{
				u = new JInternalFrameJourneyProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PO_RESOURCE_EDIT"))
		{
			final JInternalFrameProcessOrderResourceProperties u;
			if (isLoaded(JInternalFrameJourneyProperties.class))
				((JInternalFrameProcessOrderResourceProperties) isLoadedInstance(JInternalFrameProcessOrderResourceProperties.class)).setResource(StrParam);
			else
			{
				u = new JInternalFrameProcessOrderResourceProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_CUST_DATA"))
		{

			final JInternalFrameMaterialCustomerDataAdmin u;
			if (isLoaded(JInternalFrameMaterialCustomerDataAdmin.class))
				setVisible(JInternalFrameMaterialCustomerDataAdmin.class);
			// ((JInternalFrameMaterialCustomerDataAdmin)
			// isLoadedInstance(JInternalFrameMaterialCustomerDataAdmin.class)).setJourneyRef(StrParam);
			else
			{
				u = new JInternalFrameMaterialCustomerDataAdmin(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_USER_REPORT_PROP"))
		{
			final JInternalFrameUserReportProperties u;
			// if (isLoaded(JInternalFrameUserReportProperties.class))
			// setVisible(JInternalFrameUserReportProperties.class);
			// else
			{
				u = new JInternalFrameUserReportProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_SPLIT"))
		{
			final JInternalFramePalletSplit u;
			// if (isLoaded(JInternalFramePalletSplit.class))
			// setVisible(JInternalFramePalletSplit.class);
			// else
			{
				u = new JInternalFramePalletSplit(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_QM_SAMPLE_EDIT"))
		{
			final JInternalFrameQMSampleRecord u;
			// if (isLoaded(JInternalFrameQMSampleRecord.class))
			// setVisible(JInternalFrameQMSampleRecord.class);
			// else
			{
				u = new JInternalFrameQMSampleRecord(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_CUSTOMER_EDIT"))
		{
			final JInternalFrameCustomerProperties u;
			// if (isLoaded(JInternalFrameCustomerProperties.class))
			// setVisible(JInternalFrameCustomerProperties.class);
			// else
			{
				u = new JInternalFrameCustomerProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN_EDIT"))
		{
			final JInternalFrameMHNProperties u;
			// if (isLoaded(JInternalFrameMHNProperties.class))
			// setVisible(JInternalFrameMHNProperties.class);
			// else
			{
				u = new JInternalFrameMHNProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_PROD_DEC"))
		{
			final JInternalFrameProductionDeclaration u;
			if (isLoaded(JInternalFrameProductionDeclaration.class))
			{
				((JInternalFrameProductionDeclaration) isLoadedInstance(JInternalFrameProductionDeclaration.class)).processOrderChanged(StrParam);
				setVisible(JInternalFrameProductionDeclaration.class);
			}
			else
			{
				u = new JInternalFrameProductionDeclaration(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MODULE_EDIT"))
		{
			final JInternalFrameModuleProperties u;
			// if (isLoaded(JInternalFrameModuleProperties.class))
			// setVisible(JInternalFrameModuleProperties.class);
			// else
			{
				u = new JInternalFrameModuleProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PALLET_EDIT"))
		{
			final JInternalFramePalletProperties u;
			if (isLoaded(JInternalFramePalletProperties.class))
			{
				setVisible(JInternalFramePalletProperties.class);
				((JInternalFramePalletProperties) isLoadedInstance(JInternalFramePalletProperties.class)).setPalletSSCC(StrParam);
			}
			else
			{
				u = new JInternalFramePalletProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_PROD_CONFIRM"))
		{
			final JInternalFrameProductionConfirmation u;

			if (isLoaded(JInternalFrameProductionConfirmation.class))
				((JInternalFrameProductionConfirmation) isLoadedInstance(JInternalFrameProductionConfirmation.class)).setSSCC(StrParam);
			else
			{
				u = new JInternalFrameProductionConfirmation(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_PAL_SAMPLE"))
		{
			final JInternalFramePalletSample u;
			if (isLoaded(JInternalFramePalletSample.class))
			{
				((JInternalFramePalletSample) isLoadedInstance(JInternalFramePalletSample.class)).setPalletSSCC(StrParam);
				setVisible(JInternalFramePalletSample.class);
			}
			else
			{
				u = new JInternalFramePalletSample(StrParam);
				u.setTitle(mod.getDescription());
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_UOM_EDIT"))
		{
			final JInternalFrameUomProperties u;
			if (isLoaded(JInternalFrameUomProperties.class))
				((JInternalFrameUomProperties) isLoadedInstance(JInternalFrameUomProperties.class)).setUOMID(StrParam);
			else
			{
				u = new JInternalFrameUomProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_SAMPLEPOINT_EDIT"))
		{
			final JInternalFrameWTSamplePointProperties u;
			if (isLoaded(JInternalFrameWTSamplePointProperties.class))
				((JInternalFrameWTSamplePointProperties) isLoadedInstance(JInternalFrameWTSamplePointProperties.class)).setSamplePointID(StrParam);
			else
			{
				u = new JInternalFrameWTSamplePointProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_CONTAINERCODE_EDIT"))
		{
			final JInternalFrameWTContainerCodeProperties u;
			if (isLoaded(JInternalFrameWTContainerCodeProperties.class))
				((JInternalFrameWTContainerCodeProperties) isLoadedInstance(JInternalFrameWTContainerCodeProperties.class)).setContainerCode(StrParam);
			else
			{
				u = new JInternalFrameWTContainerCodeProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_WORKSTATION_EDIT"))
		{
			final JInternalFrameWTWorkstationProperties u;
			if (isLoaded(JInternalFrameWTWorkstationProperties.class))
				((JInternalFrameWTWorkstationProperties) isLoadedInstance(JInternalFrameWTWorkstationProperties.class)).setWorkstationID(StrParam);
			else
			{
				u = new JInternalFrameWTWorkstationProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_WEIGHT_SCALE_EDIT"))
		{
			final JInternalFrameWTScaleProperties u;
			if (isLoaded(JInternalFrameWTScaleProperties.class))
				((JInternalFrameWTScaleProperties) isLoadedInstance(JInternalFrameWTScaleProperties.class)).setScaleID(StrParam);
			else
			{
				u = new JInternalFrameWTScaleProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_TYPE_EDIT"))
		{
			final JInternalFrameMaterialTypeProperties u;
			// if (isLoaded(JInternalFrameMaterialTypeProperties.class))
			// setVisible(JInternalFrameMaterialTypeProperties.class);
			// else
			{
				u = new JInternalFrameMaterialTypeProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN_REASON_EDIT"))
		{
			final JInternalFrameMHNReasonProperties u;
			// if (isLoaded(JInternalFrameMHNReasonProperties.class))
			// setVisible(JInternalFrameMHNReasonProperties.class);
			// else
			{
				u = new JInternalFrameMHNReasonProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_REASON_EDIT"))
		{
			final JInternalFrameSampleReasonProperties u;
			// if (isLoaded(JInternalFrameMHNReasonProperties.class))
			// setVisible(JInternalFrameMHNReasonProperties.class);
			// else
			{
				u = new JInternalFrameSampleReasonProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_DEFECT_ID_EDIT"))
		{
			final JInternalFrameSampleDefectIDProperties u;
			// if (isLoaded(JInternalFrameMHNReasonProperties.class))
			// setVisible(JInternalFrameMHNReasonProperties.class);
			// else
			{
				u = new JInternalFrameSampleDefectIDProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SAMPLE_DEFECT_TYPE_EDIT"))
		{
			final JInternalFrameSampleDefectTypeProperties u;
			// if (isLoaded(JInternalFrameMHNReasonProperties.class))
			// setVisible(JInternalFrameMHNReasonProperties.class);
			// else
			{
				u = new JInternalFrameSampleDefectTypeProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SUPPLIER_EDIT"))
		{
			final JInternalFrameSupplierProperties u;
			// if (isLoaded(JInternalFrameMHNReasonProperties.class))
			// setVisible(JInternalFrameMHNReasonProperties.class);
			// else
			{
				u = new JInternalFrameSupplierProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MHN_DECISION_EDIT"))
		{
			final JInternalFrameMHNDecisionProperties u;
			// if (isLoaded(JInternalFrameMHNDecisionProperties.class))
			// setVisible(JInternalFrameMHNDecisionProperties.class);
			// else
			{
				u = new JInternalFrameMHNDecisionProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_SHIFT_NAMES_EDIT"))
		{
			final JInternalFrameShiftNameProperties u;
			// if (isLoaded(JInternalFrameMHNDecisionProperties.class))
			// setVisible(JInternalFrameMHNDecisionProperties.class);
			// else
			{
				u = new JInternalFrameShiftNameProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}
		
		if (optionName.equals("FRM_ADMIN_OPERATIVES_EDIT"))
		{
			final JInternalFrameOperativeProperties u;
			// if (isLoaded(JInternalFrameMHNDecisionProperties.class))
			// setVisible(JInternalFrameMHNDecisionProperties.class);
			// else
			{
				u = new JInternalFrameOperativeProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}
		
		if (optionName.equals("FRM_ADMIN_PACKING_LINES_EDIT"))
		{
			final JInternalFramePackingLineProperties u;
			// if (isLoaded(JInternalFrameMHNDecisionProperties.class))
			// setVisible(JInternalFrameMHNDecisionProperties.class);
			// else
			{
				u = new JInternalFramePackingLineProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_EDIT"))
		{
			final JInternalFrameMaterialProperties u;
			if (isLoaded(JInternalFrameMaterialProperties.class))
			{
				((JInternalFrameMaterialProperties) isLoadedInstance(JInternalFrameMaterialProperties.class)).setMaterialID(StrParam);
				setVisible(JInternalFrameMaterialProperties.class);
			}
			else
			{
				u = new JInternalFrameMaterialProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_BATCH"))
		{
			final JInternalFrameMaterialBatchAdmin u;
			// if (isLoaded(JInternalFrameMaterialBatchAdmin.class))
			// setVisible(JInternalFrameMaterialBatchAdmin.class);
			// else
			{
				u = new JInternalFrameMaterialBatchAdmin(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_MATERIAL_LOCATION"))
		{
			final JInternalFrameMaterialLocationAdmin u;
			// if (isLoaded(JInternalFrameMaterialLocationAdmin.class))
			// setVisible(JInternalFrameMaterialLocationAdmin.class);
			// else
			{
				u = new JInternalFrameMaterialLocationAdmin(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_LOCATION_EDIT"))
		{
			final JInternalFrameLocationProperties u;
			if (isLoaded(JInternalFrameLocationProperties.class))
			{
				((JInternalFrameLocationProperties) isLoadedInstance(JInternalFrameLocationProperties.class)).setLocationID(StrParam);
				setVisible(JInternalFrameLocationProperties.class);
			}
			else
			{
				u = new JInternalFrameLocationProperties(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_PROCESS_ORDER_EDIT"))
		{
			final JInternalFrameProcessOrderProperties u;
			if (isLoaded(JInternalFrameProcessOrderProperties.class))
			{
				((JInternalFrameProcessOrderProperties) isLoadedInstance(JInternalFrameProcessOrderProperties.class)).setProcessOrderNo(StrParam);
				setVisible(JInternalFrameProcessOrderProperties.class);
			}
			else
			{
				u = new JInternalFrameProcessOrderProperties(StrParam);
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_USER_PERM"))
		{
			final JInternalFrameUserPermissions u;
			// if (isLoaded(JInternalFrameUserPermissions.class))
			// setVisible(JInternalFrameUserPermissions.class);
			// else
			{
				u = new JInternalFrameUserPermissions(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}

		if (optionName.equals("FRM_ADMIN_GROUP_PERM"))
		{
			final JInternalFrameGroupPermissions u;
			// if (isLoaded(JInternalFrameGroupPermissions.class))
			// setVisible(JInternalFrameGroupPermissions.class);
			// else
			{
				u = new JInternalFrameGroupPermissions(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
				displayForm(u, optionName);
			}
		}
	}

	private static boolean setVisible(Class<?> u)
	{
		boolean result = false;
		JInternalFrame[] frames = Common.mainForm.desktopPane.getAllFrames();

		for (int k = frames.length - 1; k >= 0; k--)
		{
			if (frames[k].getClass().equals(u))
			{
				frames[k].setVisible(true);
				try
				{
					frames[k].setIcon(false);
					frames[k].setSelected(true);
				}
				catch (Exception e)
				{
				}
			}
		}
		return result;
	}
}
