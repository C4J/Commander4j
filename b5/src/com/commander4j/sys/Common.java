
package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JDatabaseParameters.java
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

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.apache.xalan.xsltc.runtime.Hashtable;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBQMSelectList;
import com.commander4j.renderer.JDBListRenderer;
import com.commander4j.renderer.RenderColumnPrefs;
import com.commander4j.renderer.TableCellRenderer_Default;
import com.commander4j.renderer.TableHeaderRenderer;
import com.commander4j.renderer.WeightSampleDetailCellRenderer;
import com.commander4j.util.JImageIconLoader;
import com.commander4j.util.JSessionData;

/**
 * The Common classed is the place where all global variables are located.
 *
 */
public class Common
{
	//LOGON STATUS
	public static boolean logonValidated = false;
	public static boolean passwordChangeRequired = false;
	public static boolean passwordChangeRequested = false;
	public static boolean passwordExpired = false;
	public static String validatedUsername = "";
	public static String validatedPassword = "";
	public static String encryptionKey = "C0mm4nd3r4jP455w";
	
	public static HashMap<Integer, RenderColumnPrefs> defaultColumnPrefs = new HashMap<Integer, RenderColumnPrefs>();

	public static String appDisplayName="Commander4j";
	public static String appWebsite="http://www.commander4j.com";
	public static String appAuthor="David Garratt";
	public static String appSupportEmail="support@commander4j.com";
	
	
	
	public static int LFAdjustWidth=0;
	public static int LFAdjustHeight=0;
	public static int LFTreeMenuAdjustWidth=0;
	public static int LFTreeMenuAdjustHeight=0;

	private static HashMap<String, String> hashMap1 = new HashMap<String, String>();
	private static HashMap<String, String> hashMap2 = new HashMap<String, String>();
	public static Map<String, String> translation_Text = Collections.synchronizedMap(hashMap1);
	public static Map<String, String> translation_Mnemonic = Collections.synchronizedMap(hashMap2);
	public static String base_dir = "";
	public static JFrameMain mainForm;
	public static String defaultHelpSetID;
	public static JHostList hostList = new JHostList();
	public static JUserList userList = new JUserList();
	public static String selectedHostID = "";
	public static JEANBarcode barcode;
	public static String sessionID = "";
	public static String applicationMode = "";
	public static boolean displaySplashScreen = true;
	public static String updateURL = "";
	public static String updateMODE = "";
	public static String updateInstallDir = "";
	public static String hostVersion = "";
	public static String hostUpdatePath = "";
	public static String setupPassword = "";
	public static int buttonToolbarSize = 65;
	public static int spacerToolBarSize = 10;
	public static int comboToolBarSize = 275;

	public static String helpURL = "http://www.commander4j.com";
	public static String interface_recovery_path = "xml/interface/recovery/";
	public static String interface_error_path = "xml/interface/error/";
	public static String interface_backup_path = "xml/interface/backup/";
	public static String interface_output_path = "xml/interface/output/";
	
	public static Hashtable paths = new Hashtable();
	public static JSessionData sd = new JSessionData();

	public static String[] dataTypes = new String[] { "string", "numeric","boolean", "date", "time", "timestamp","integer" ,"list"};
	public static String[] printerTypes = new String[] { "Logopak","Videojet","Zebra"};
	public static String[] printerTypesAll = new String[] { "","Logopak","Videojet","Zebra"};
	public static String[] printerDPI = new String[] { "200","300"};
	public static String[] printerGroup = new String[] { "Pack","Pallet"};
	public static String[] linePrinterType = new String[] {"Both", "Pack","Pallet"};
	public static String[] printerLanguage = new String[] { "LEAP","ZPL","Zipher"};
	public static String[] printerExportFormat = new String[] { "CSV","LQF","XML"};
	public static String[] languages = new String[] {"EN", "ES", "DE", "FR", "HU", "IT", "NL", "PL"};
	public static String[] customerDataTypes = new String[] {"PART_NO"};
	
	@SuppressWarnings("rawtypes")
	public static Class[] dataClasses = new Class[] { String.class,BigDecimal.class,boolean.class,Date.class,Time.class,Timestamp.class,Integer.class,JDBQMSelectList.class };
	@SuppressWarnings("rawtypes")
	public static HashMap<String,Class> datatypeClass = new HashMap<String,Class>();
	
	public static String[] palletStatus = new String[] { "Blocked", "Quality Inspection", "Unrestricted" };
	public static String[] batchStatus = new String[] { "Restricted", "Unrestricted" };
	public static String[] palletStatusIncBlank = new String[] { "", "Blocked", "Quality Inspection", "Unrestricted" };
	public static String[] batchStatusIncBlank = new String[] { "", "Restricted", "Unrestricted" };
	public static String[] JourneyRefStatusIncBlank = new String[] { "", "Assigned", "Unassigned" };
	public static String[] JourneyRefStatus = new String[] { "Assigned", "Unassigned" };
	public static String[] locationStatusIncBlank = new String[] { "", "Valid", "Invalid" };
	public static String[] processOrderStatus = new String[] { "Discarded", "Finished", "Held", "Ready", "Running" };
	public static String[] processOrderStatusincBlank = new String[] { "", "Discarded", "Finished", "Held", "Ready", "Running" };
	public static String[] messageTypesexclBlank = new String[] {"Batch Status Change", 	"Despatch Confirmation", 	"Despatch Email", 	"Despatch Pre Advice", 	"Equipment Tracking", 	"Journey Definition",	"Location",	"Material Auto Move", 	"Material Definition", 	"Pallet Delete",	"Pallet Move",	"Pallet Split", 	"Pallet Status Change",	"Process Order", 	"Process Order Status Change",	"Production Declaration", 	"QM Inspection Request",	"QM Inspection Result" };
	public static String[] messageTypesincBlank = new String[] { "", 	"Batch Status Change", 	"Despatch Confirmation", 	"Despatch Email", 	"Despatch Pre Advice", 	"Equipment Tracking", "Journey Definition",	"Location",	"Material Auto Move", 	"Material Definition", 	"Pallet Delete",	"Pallet Move",	"Pallet Split", 	"Pallet Status Change",	"Process Order", 	"Process Order Status Change",	"Production Declaration", 	"QM Inspection Request",	"QM Inspection Result"	};

	public static String[] transactionTypes = new String[] { "", "DESPATCH", "EDIT", "PRINT","PROD DEC", "STATUS CHANGE","MHN","SPLIT","MOVE","MANUAL"};
	public static String[] transactionSubTypes = new String[] { "", "ADD","REMOVE","CREATE","PRINT", "CONFIRM", "FROM", "TO", "MANUAL", "LABEL","DECISION","BEFORE","AFTER","DELETE" };
	public static String[] auditEventActions = new String[] { "", "ADD", "REMOVE", "CREATE", "DELETE", "RENAME", "ENABLE", "DISABLE" };
	public static String[] auditEventTypes = new String[] { "", "USER", "GROUP", "USER_GROUP", "GROUP_MODULE" };
	public static String[] locationSortBy = new String[] { "LOCATION_ID", "PLANT", "WAREHOUSE", "DESCRIPTION", "GLN", "STORAGE_LOCATION", "STORAGE_TYPE", "STORAGE_SECTION", "STORAGE_BIN"};
	public static String[] materialSortBy =new String[] {"MATERIAL", "MATERIAL_TYPE", "DESCRIPTION", "BASE_UOM", "PRODUCTION_UOM", "ISSUE_UOM", "SHELF_LIFE", "SHELF_LIFE_RULE", "DEFAULT_PALLET_STATUS", "DEFAULT_BATCH_STATUS","EQUIPMENT_TYPE"};
	public static String[] processSortBy = new String[] { "PROCESS_ORDER", "MATERIAL", "DESCRIPTION", "STATUS", "LOCATION_ID", "DUE_DATE", "RECIPE_ID" };
	public static final JImageIconLoader imageIconloader = new JImageIconLoader();

	public static final WeightSampleDetailCellRenderer weight_sample_list = new WeightSampleDetailCellRenderer();
	public static final JDBListRenderer renderer_list = new JDBListRenderer();
	public static final JDBListRenderer renderer_list_assigned = new JDBListRenderer(Common.color_list_assigned);
	public static final JDBListRenderer renderer_list_unassigned = new JDBListRenderer(Common.color_list_unassigned);
	
	public static final String UOM_Convert_Internal_to_ISO = "INTERNAL to ISO";
	public static final String UOM_Convert_Internal_to_Local = "INTERNAL to Local";
	public static final String UOM_Convert_None = "None";
	public static final String UOM_Convert_ISO_to_INTERNAL = "ISO to INTERNAL";
	public static final String UOM_Convert_ISO_to_Local = "ISO to Local";
	public static final String UOM_Convert_Local_to_ISO = "Local to ISO";
	public static final String UOM_Convert_Local_to_Internal = "Local to INTERNAL";

	public final static String requiredJavaVersion = "1.8";
	public final static int splashDelay = 50;

	public final static Font font_dates = new Font("Arial", Font.PLAIN, 11);
	public final static Font font_small = new Font("Arial", Font.PLAIN, 9);
	public final static Font font_std = new Font("Arial", Font.PLAIN, 11);
	public final static Font font_input = new Font("Arial", Font.PLAIN, 11);
	public final static Font font_popup = new Font("Arial", Font.PLAIN, 11);
	public final static Font font_bold = new Font("Arial", Font.BOLD, 11);
	public final static Font font_italic = new Font("Arial", Font.ITALIC, 11);
	public final static Font font_btn = new Font("Arial", Font.PLAIN, 11);
	public final static Font font_title = new Font("Arial", Font.ITALIC, 12);
	public final static Font font_tree = new Font("Arial", Font.PLAIN, 12);
	public final static Font font_menu = new Font("Arial", Font.PLAIN, 12);
	public final static Font font_list = new Font("Monospaced", 0, 11);
	public final static Font font_list_weights = new Font("Monospaced", 0, 14);
	public final static Font font_combo = new Font("Monospaced", Font.PLAIN, 11);
	public final static Font font_table_header = new java.awt.Font("Arial", Font.PLAIN, 11);
	public final static Font font_table = new java.awt.Font("Monospaced", 0, 11);
	public final static Font font_textArea = new java.awt.Font("Monospaced", 0, 14);
	
	public static final TableCellRenderer_Default renderer_table = new TableCellRenderer_Default();
	public static final TableHeaderRenderer renderer_tableheader = new TableHeaderRenderer();
	
	public final static Color color_textfield_foreground_focus_color = Color.BLACK;
	public final static Color color_textfield_forground_nofocus_color = Color.BLACK;
	public final static Color color_textfield_background_focus_color = Color.WHITE;
	public final static Color color_textfield_background_nofocus_color = Color.WHITE;
	public final static Color color_text_maxsize_color = Color.RED;
	public final static Color color_list_assigned = new Color(233, 255, 233);
	public final static Color color_list_unassigned = new Color(255, 240, 255);
	public final static Color color_listFontStandard = Color.BLUE;
	public final static Color color_listFontSelected = Color.BLACK;
	public final static Color color_listBackground = new Color(243,251,255);
	public final static Color color_listHighlighted = new Color(184, 207, 229);
	public final static Color color_tablerow1 = new Color(248, 226, 226);
	public final static Color color_tablerow2 = new Color(240,255,240);
	public final static Color color_tablerow3 = new Color(204, 255, 204);
	public final static Color color_tablebackground = new Color(233, 240, 249);
	public final static Color color_tableHeaderFont = Color.BLACK;
	public final static Color color_text_disabled = Color.BLACK;
	public final static Color color_edit_properties = new Color(241, 241, 241);
	public final static Color color_app_window = new Color(241, 241, 241);


	public final static int menuTreeWidth = 250;
	public static JWindowSplash splash;
	public static JWindowProgress progress;

	public final static String report_path = System.getProperty("user.dir")+File.separator+"reports"+File.separator;
	public final static String label_path = System.getProperty("user.dir")+File.separator+"labels"+File.separator;
	public final static String image_path_16x16 = System.getProperty("user.dir")+File.separator+"images"+File.separator+"16x16"+File.separator;
	public final static String image_path_24x24 = System.getProperty("user.dir")+File.separator+"images"+File.separator+"24x24"+File.separator;
	public final static String image_path_32x32 = System.getProperty("user.dir")+File.separator+"images"+File.separator+"32x32"+File.separator;
	public final static String auto_label_command = System.getProperty("user.dir")+File.separator+"autolabeller"+File.separator+"commands"+File.separator;
	public final static String auto_label_labels = System.getProperty("user.dir")+File.separator+"autolabeller"+File.separator+"labels"+File.separator;
	
	public final static Icon icon_file_save_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_file_save);
	public final static Icon icon_export_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_export);	
	public final static Icon icon_resend_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_resend);
	public final static Icon icon_history_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_history);
	public final static Icon icon_dictionary_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_dictionary);
	public final static Icon icon_copy_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_copy);
	public final static Icon icon_calendar_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_calendar);
	public final static Icon icon_clear_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_clear);
	public final static Icon icon_details_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_details);
	public final static Icon icon_ascending_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_ascending);
	public final static Icon icon_descending_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_descending);
	public final static Icon icon_home_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_home);
	public final static Icon icon_home_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_home);
	public final static Icon icon_execute_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_execute);
	public final static Icon icon_connect_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_connect);
	public final static Icon icon_cascade_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_cascade);
	public final static Icon icon_minimize_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_minimize);
	public final static Icon icon_restore_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_restore);
	public final static Icon icon_open_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_open);
	public final static Icon icon_find_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_find);
	public final static Icon icon_close_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_close);
	public final static Icon icon_close_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_close);
	public final static Icon icon_lookup_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_lookup);
	public final static Icon icon_print_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_print);
	public final static Icon icon_uom_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_units);
	public final static Icon icon_customer_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_customer);
	public final static Icon icon_mhn_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_mhn);
	public final static Icon icon_help_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_help);
	public final static Icon icon_help_32x32 = Common.imageIconloader.getImageIcon32x32(Common.image_help);
	public final static Icon icon_delete_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_delete);
	public final static Icon icon_edit_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_edit);
	public final static Icon icon_add_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_add);
	public final static Icon icon_permissions_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_permissions);
	public final static Icon icon_rename_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_rename);
	public final static Icon icon_refresh_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_refresh);
	public final static Icon icon_arrow_right_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_arrow_right);
	public final static Icon icon_arrow_left_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_arrow_left);
	public final static Icon icon_arrow_up_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_arrow_up);
	public final static Icon icon_arrow_down_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_arrow_down);
	public final static Icon icon_undo_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_undo);
	public final static Icon icon_blank_icon = Common.imageIconloader.getImageIcon16x16(Common.image_blank_icon);
	public final static Icon icon_menu_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_menu);
	public final static Icon icon_form = Common.imageIconloader.getImageIcon16x16(Common.image_form);
	public final static Icon icon_scanner_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_scanner);
	public final static Icon icon_report_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_report);
	public final static Icon icon_label_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_label);
	public final static Icon icon_pallet_label_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_pallet_label);
	public final static Icon icon_function_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_function);
	public final static Icon icon_error = Common.imageIconloader.getImageIcon16x16(Common.image_error);
	public final static Icon icon_select_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_select);
	public final static Icon icon_expand_all_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_expand_all);
	public final static Icon icon_collapse_all_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_collapse_all);
	public final static Icon icon_expand_node_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_expand_node);
	public final static Icon icon_collapse_node_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_collapse_node);
	public final static Icon icon_ok_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_ok);
	public final static Icon icon_cancel_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_cancel);
	public final static Icon icon_search_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_search);
	public final static Icon icon_user_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_user);
	public final static Icon icon_update_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_update);
	public final static Icon icon_user_disabled_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_user_disabled);
	public final static Icon icon_user_locked_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_user_locked);
	public final static Icon icon_user_expired_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_user_expired);
	public final static Icon icon_interface_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_interface);
	public final static Icon icon_XLS_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_XLS);
	public final static Icon icon_hold_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_hold);
	public final static Icon icon_release_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_release);
	public final static Icon icon_process_order_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_process_order);
	public final static Icon icon_material_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_material);
	public final static Icon icon_location_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_location);
	public final static Icon icon_batch_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_batch);
	public final static Icon icon_pallet_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_pallet);
	public final static Icon icon_split_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_split);
	public final static Icon icon_move_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_move);
	public final static Icon icon_clone_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_clone);
	public final static Icon icon_case_label_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_case_label);
	public final static Icon icon_sample_label_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_sample_label);
	public final static Icon icon_confirm_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_confirm);
	public final static Icon icon_groups_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_groups);
	public final static Icon icon_despatch_remove_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_despatch_remove);
	public final static Icon icon_despatch_add_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_despatch_add);
	public final static Icon icon_alternative_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_alternative);
	public final static Icon icon_lock_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_lock);
	public final static Icon icon_production_line_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_production_line);
	public final static Icon icon_weight_capture_16x16 = Common.imageIconloader.getImageIcon16x16(Common.image_weight_capture);

	public final static String image_weight_capture = "weight-capture.gif";
	public final static String image_production_line = "prod_lines.gif";
	public final static String image_lock = "lock.gif";
	public final static String image_file_save = "file_save.gif";
	public final static String image_export = "export.gif";
	public final static String image_alternative = "alternative.gif";
	public final static String image_resend = "resend.gif";
	public final static String image_details = "details.gif";
	public final static String image_history = "pallet_history.gif";
	public final static String image_pdf = "pdf.gif";
	public final static String image_csv = "csv.gif";
	public final static String image_jasperreport = "jasperreport.gif";
	public final static String image_msaccess = "msaccess.jpg";
	public final static String image_clone = "clone.gif";
	public final static String image_dictionary = "dictionary.gif";
	public final static String image_clear = "edit_clear.png";
	public final static String image_copy = "copy.gif";
	public final static String image_calendar = "calendar.gif";
	public final static String image_pallet = "pallet.gif";
	public final static String image_process_order = "process_order.gif";
	public final static String image_material = "materials.gif";
	public final static String image_location = "locations.gif";
	public final static String image_batch = "batches.gif";
	public final static String image_hold = "hold.gif";
	public final static String image_groups = "admingroups.gif";
	public final static String image_release = "release.gif";
	public final static String image_ascending = "ascending.gif";
	public final static String image_descending = "descending.gif";
	public final static String image_home = "home.gif";
	public final static String image_execute = "execute.gif";
	public final static String image_connect = "connect.gif";
	public final static String image_cascade = "cascade.gif";
	public final static String image_minimize = "minimize.gif";
	public final static String image_restore = "restore.gif";
	public final static String image_find = "find.gif";
	public final static String image_close = "exit.gif";
	public final static String image_mhn = "mhn.gif";
	public final static String image_lookup = "lookup.gif";
	public final static String image_open = "open.gif";
	public final static String image_print = "print.gif";
	public final static String image_help = "help.gif";
	public final static String image_delete = "delete.gif";
	public final static String image_edit = "edit.gif";
	public final static String image_add = "add.gif";
	public final static String image_permissions = "permissions.gif";
	public final static String image_rename = "rename.gif";
	public final static String image_refresh = "refresh.gif";
	public final static String image_arrow_right = "arrow_right.gif";
	public final static String image_arrow_left = "arrow_left.gif";
	public final static String image_arrow_up = "arrow_up.gif";
	public final static String image_arrow_down = "arrow_down.gif";
	public final static String image_undo = "undo.gif";
	public final static String image_blank_icon = "blankicon.gif";
	public final static String image_menu = "menu.gif";
	public final static String image_form = "form.gif";
	public final static String image_scanner = "pallet_confirm.gif";
	public final static String image_interface = "interface.gif";
	public final static String image_units = "units.gif";
	public final static String image_customer = "customer.gif";
	public final static String image_report = "report.gif";
	public final static String image_label = "label.gif";
	public final static String image_pallet_label = "pallet_label.gif";
	public final static String image_function = "function.gif";
	public final static String image_error = "error.gif";
	public final static String image_select = "ok.gif";
	public final static String image_expand_all = "expandall.gif";
	public final static String image_collapse_all = "collapseall.gif";
	public final static String image_expand_node = "expandnode.gif";
	public final static String image_collapse_node = "collapsenode.gif";
	public static String image_splash = "splash.gif";
	public final static String image_osx_commander4j = "osx_commander4j.gif";
	public final static String image_osx_setup4j = "osx_setup4j.gif";
	public final static String image_osx_clone4j = "osx_clone4j.gif";
	public final static String image_ok = "ok.gif";
	public final static String image_cancel = "cancel.gif";
	public final static String image_split = "split.gif";
	public final static String image_move = "move.gif";
	public final static String image_search = "search.gif";
	public final static String image_user = "user.gif";
	public final static String image_update = "update.gif";
	public final static String image_user_disabled = "userdisabled.gif";
	public final static String image_user_locked = "userlocked.gif";
	public final static String image_user_expired = "userexpired.gif";
	public final static String image_XLS = "xls.gif";
	public final static String image_user_report = "userreports.gif";
	public final static String image_printer_enabled = "ok.gif";
	public final static String image_printer_disabled = "cancel.gif";
	public final static String image_case_label = "case_label.gif";
	public final static String image_sample_label = "sample_label.gif";
	public final static String image_confirm = "CMD_Icon.gif";
	public final static String image_despatch_add = "journey_add.gif";
	public final static String image_despatch_remove = "journey_remove.gif";
	public static String locale_language="GB";
	public static String locale_region="en";
	
	public static String statusReportTime = "00:00:00";
	public static int user_password_expiry_days = 14;
	public static int user_max_password_attempts = 3;
	public static boolean active_mq_enabled = false;

	public static void init() {
		
		defaultColumnPrefs.put(999, new RenderColumnPrefs(JLabel.CENTER,Common.color_listFontStandard,Common.color_tablerow3,Common.color_tablerow2,100));
		
		JDBControl control = new JDBControl(Common.selectedHostID, Common.sessionID);

		control.setSystemKey("PASSWORD EXPIRY");
		if (control.getProperties() == true)
		{
			user_password_expiry_days = Integer.parseInt(control.getKeyValue());
		}

		control.setSystemKey("PASSWORD ATTEMPTS");
		if (control.getProperties() == true)
		{
			user_max_password_attempts = Integer.parseInt(control.getKeyValue());
		}
		
		for (int x=0;x<dataTypes.length;x++)
		{
			datatypeClass.put(dataTypes[x], dataClasses[x]);
		}
		
		statusReportTime = control.getKeyValueWithDefault("INTERFACE MAINTENANCE TIME","09:00:00", "Time of day for interface maintenance");

	}

}
