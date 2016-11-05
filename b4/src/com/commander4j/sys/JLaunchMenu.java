package com.commander4j.sys;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import com.commander4j.app.JDialogAbout;
import com.commander4j.app.JDialogAssignLabelDataToLine;
import com.commander4j.app.JDialogMaterialCustomerDataProperties;
import com.commander4j.app.JDialogPalletRePrintLabel;
import com.commander4j.app.JDialogQMActivityProperties;
import com.commander4j.app.JDialogQMDictionaryProperties;
import com.commander4j.app.JDialogQMInspectionProperties;
import com.commander4j.app.JDialogQMSelectListProperties;
import com.commander4j.app.JDialogShiftProperties;
import com.commander4j.app.JInternalFrameAutoLabellerLines;
import com.commander4j.app.JDialogAutoLabellerProperties;
import com.commander4j.app.JInternalFrameCustomerAdmin;
import com.commander4j.app.JInternalFrameCustomerProperties;
import com.commander4j.app.JInternalFrameDataIDAdmin;
import com.commander4j.app.JDialogDataIDProperties;
import com.commander4j.app.JInternalFrameDespatch;
import com.commander4j.app.JInternalFrameJourneyAdmin;
import com.commander4j.app.JInternalFrameJourneyProperties;
import com.commander4j.app.JInternalFrameLabelHistory;
import com.commander4j.app.JInternalFrameLocationAdmin;
import com.commander4j.app.JInternalFrameLocationProperties;
import com.commander4j.app.JInternalFrameMHNAdmin;
import com.commander4j.app.JInternalFrameMHNAssign;
import com.commander4j.app.JInternalFrameMHNDecisionAdmin;
import com.commander4j.app.JInternalFrameMHNDecisionProperties;
import com.commander4j.app.JInternalFrameMHNProperties;
import com.commander4j.app.JInternalFrameMHNReasonAdmin;
import com.commander4j.app.JInternalFrameMHNReasonProperties;
import com.commander4j.app.JInternalFrameMaterialAdmin;
import com.commander4j.app.JInternalFrameMaterialBatchAdmin;
import com.commander4j.app.JInternalFrameMaterialBatchProperties;
import com.commander4j.app.JInternalFrameMaterialCustomerDataAdmin;
import com.commander4j.app.JInternalFrameMaterialLocationAdmin;
import com.commander4j.app.JInternalFrameMaterialLocationProperties;
import com.commander4j.app.JInternalFrameMaterialProperties;
import com.commander4j.app.JInternalFrameMaterialTypeAdmin;
import com.commander4j.app.JInternalFrameMaterialTypeProperties;
import com.commander4j.app.JInternalFrameMaterialUomAdmin;
import com.commander4j.app.JInternalFrameMaterialUomProperties;
import com.commander4j.app.JInternalFramePackLabelPrint;
import com.commander4j.app.JInternalFramePalletAdmin;
import com.commander4j.app.JInternalFramePalletHistoryAdmin;
import com.commander4j.app.JInternalFramePalletProperties;
import com.commander4j.app.JInternalFramePalletSplit;
import com.commander4j.app.JInternalFrameProcessOrderAdmin;
import com.commander4j.app.JInternalFrameProcessOrderLabel;
import com.commander4j.app.JInternalFrameProcessOrderProperties;
import com.commander4j.app.JInternalFrameProcessOrderResourceAdmin;
import com.commander4j.app.JInternalFrameProductionConfirmation;
import com.commander4j.app.JInternalFrameProductionDeclaration;
import com.commander4j.app.JInternalFrameQMDictionaryAdmin;
import com.commander4j.app.JInternalFrameQMInspectionAdmin;
import com.commander4j.app.JInternalFrameQMResultEnquiry;
import com.commander4j.app.JInternalFrameQMSampleLabel;
import com.commander4j.app.JInternalFrameQMSampleRecord;
import com.commander4j.app.JInternalFrameQMSampleResults;
import com.commander4j.app.JDialogQMTestProperties;
import com.commander4j.app.JInternalFrameQMSelectListAdmin;
import com.commander4j.app.JInternalFrameUomAdmin;
import com.commander4j.app.JInternalFrameUomProperties;
import com.commander4j.app.JInternalFrameUserReportAdmin;
import com.commander4j.app.JInternalFrameUserReportProperties;
import com.commander4j.app.JInternalFrameUserReportSchema;
import com.commander4j.db.JDBModule;
import com.commander4j.interfaces.JInternalFrameInterfaceAdmin;
import com.commander4j.interfaces.JInternalFrameInterfaceControl;
import com.commander4j.interfaces.JInternalFrameInterfaceLog;
import com.commander4j.interfaces.JInternalFrameInterfaceProperties;
import com.commander4j.interfaces.JInternalFrameInterfaceRequestAdmin;
import com.commander4j.util.JUtility;

/**
 * @author David
 * 
 */
public class JLaunchMenu {

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
						} else
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
				} else
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
		} catch (Exception ex)
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

		} catch (Exception ex)
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
		u.setFrameIcon(JDBModule.getModuleIcon(mod.getIconFilename(), mod.getType()));

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
		} catch (Exception ex)
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
		} catch (Exception ex)
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
		} catch (Exception ex)
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

		if (optionName.equals("FRM_SYSTEM_PROPERTIES"))
		{
			final JDialogSysInfo u;
			u = new JDialogSysInfo(Common.mainForm);

			displayDialog(u, optionName);
		}
	}

	public static void runDialog(String optionName, String strParam) {

		mod.setModuleId(optionName);
		mod.getModuleProperties();
		
		if (optionName.equals("FRM_ADMIN_DATA_IDS_EDIT"))
		{
			final JDialogDataIDProperties u;
			u = new JDialogDataIDProperties(Common.mainForm,strParam);
			displayDialog(u, optionName);

		}
		
		if (optionName.equals("FRM_ADMIN_CONTROL_EDIT"))
		{
			final JDialogControlProperties u;
			u = new JDialogControlProperties(Common.mainForm, strParam);
			//u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}

		if (optionName.equals("FRM_ADMIN_USER_EDIT"))
		{
			final JDialogUserProperties u;
			u = new JDialogUserProperties(Common.mainForm, strParam);
			//u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}
		
		
		
		if (optionName.equals("FRM_ADMIN_GROUP_EDIT"))
		{
			final JDialogeGroupProperties u;
			u = new JDialogeGroupProperties(Common.mainForm, strParam);
			//u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}
		
		
		if (optionName.equals("FRM_ADMIN_ARCHIVE_EDIT"))
		{
			final JDialogArchiveProperties u;
			u = new JDialogArchiveProperties(Common.mainForm, strParam);
			//u.setTitle(mod.getDescription());
			displayDialog(u, optionName);
		}
		
		if (optionName.equals("FRM_LABEL_DATA_ASSIGN"))
		{
			final JDialogAssignLabelDataToLine u;
			u = new JDialogAssignLabelDataToLine(Common.mainForm, strParam);
			//u.setTitle(mod.getDescription());
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
				u = new JInternalFrameQMSampleLabel();
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
				u = new JInternalFramePackLabelPrint();
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

	}

	public static void runForm(String optionName, String StrParam1, String StrParam2)
	{
		mod.setModuleId(optionName);
		mod.getModuleProperties();

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
				setVisible(JInternalFrameQMSampleLabel.class);
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

		if (optionName.equals("FRM_ADMIN_MATERIAL_BATCH_EDIT"))
		{
			final JInternalFrameMaterialBatchProperties u;

			{
				u = new JInternalFrameMaterialBatchProperties(StrParam1, StrParam2);
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
		
		
		if (optionName.equals("FRM_ADMIN_MODULE_MEMBERS"))
		{
			final JInternalFrameModuleGroups u;
			if (isLoaded(JInternalFrameModuleGroups.class))
			((JInternalFrameModuleGroups) isLoadedInstance(JInternalFrameModuleGroups.class)).setModuleID(StrParam);
			// setVisible(JInternalFrameUserReportProperties.class);
			else
			{
				u = new JInternalFrameModuleGroups(StrParam);
				//u.setTitle(mod.getDescription() + " [" + StrParam + "]");
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

		if (optionName.equals("FRM_ADMIN_MATERIAL_CUST_DATA"))
		{
			final JInternalFrameMaterialCustomerDataAdmin u;
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
				setVisible(JInternalFrameProductionDeclaration.class);
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
			((JInternalFramePalletProperties) isLoadedInstance(JInternalFramePalletProperties.class)).setPalletSSCC(StrParam);
			else
			{
				u = new JInternalFramePalletProperties(StrParam);
				displayForm(u, optionName);
			}
		}	

		if (optionName.equals("FRM_PROCESS_ORDER_LABEL"))
		{
			final JInternalFrameProcessOrderLabel u;
			// if (isLoaded(JInternalFrameProcessOrderLabel.class))
			// setVisible(JInternalFrameProcessOrderLabel.class);
			// else
			{
				u = new JInternalFrameProcessOrderLabel(StrParam);
				u.setTitle(mod.getDescription() + " [" + StrParam + "]");
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
				u = new JInternalFrameProductionConfirmation(StrParam);
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
		
		if (optionName.equals("FRM_ADMIN_MATERIAL_EDIT"))
		{
			final JInternalFrameMaterialProperties u;
			if (isLoaded(JInternalFrameMaterialProperties.class))
			((JInternalFrameMaterialProperties) isLoadedInstance(JInternalFrameMaterialProperties.class)).setMaterialID(StrParam);
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
			// if (isLoaded(JInternalFrameLocationProperties.class))
			// setVisible(JInternalFrameLocationProperties.class);
			// else
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
			((JInternalFrameProcessOrderProperties) isLoadedInstance(JInternalFrameProcessOrderProperties.class)).setProcessOrderNo(StrParam);
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
				} catch (Exception e)
				{
				}
			}
		}
		return result;
	}
}
