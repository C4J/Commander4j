
package com.commander4j.sys;

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
import com.commander4j.util.JImageIconLoader;
import com.commander4j.util.JSessionData;

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
	public static String setupPassword = "";
	public static int buttonToolbarSize = 55;
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
	public static String[] printerTypes = new String[] { "Logopak","Zebra"};
	public static String[] printerDPI = new String[] { "200","300"};
	public static String[] printerGroup = new String[] { "Pack","Pallet"};
	public static String[] linePrinterType = new String[] {"Both", "Pack","Pallet"};
	public static String[] printerLanguage = new String[] { "LEAP","ZPL"};
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
	public static String[] messageTypesexclBlank = new String[] { "QM Inspection Result","QM Inspection Request","Despatch Confirmation", "Equipment Tracking", "Material Definition", "Process Order", "Pallet Status Change","Pallet Delete","Pallet Split", "Batch Status Change", "Process Order Status Change",
		"Production Declaration", "Despatch Pre Advice", "Equipment Tracking","Label Data","Journey Defintion" };
	
	public static String[] messageTypesincBlank = new String[] { "", "QM Inspection Result","QM Inspection Request","Despatch Confirmation", "Equipment Tracking", "Material Definition", "Process Order", "Pallet Status Change","Pallet Delete","Pallet Split", "Batch Status Change", "Process Order Status Change",
			"Production Declaration", "Despatch Pre Advice", "Equipment Tracking","Label Data","Journey Definition" };
	public static String[] transactionTypes = new String[] { "", "DESPATCH", "EDIT", "PRINT","PROD DEC", "STATUS CHANGE","MHN","SPLIT"};
	public static String[] transactionSubTypes = new String[] { "", "ADD","REMOVE","CREATE","PRINT", "CONFIRM", "FROM", "TO", "MANUAL", "LABEL","DECISION","BEFORE","AFTER" };
	public static String[] auditEventActions = new String[] { "", "ADD", "REMOVE", "CREATE", "DELETE", "RENAME", "ENABLE", "DISABLE" };
	public static String[] auditEventTypes = new String[] { "", "USER", "GROUP", "USER_GROUP", "GROUP_MODULE" };
	public static final JImageIconLoader imageIconloader = new JImageIconLoader();

	public static final JDBListRenderer renderer_list = new JDBListRenderer();
	public static final JDBListRenderer renderer_list_assigned = new JDBListRenderer(Common.color_list_assigned);
	public static final JDBListRenderer renderer_list_unassigned = new JDBListRenderer(Common.color_list_unassigned);
	public static final TableCellRenderer_Default renderer_table = new TableCellRenderer_Default();

	public static final String UOM_Convert_Internal_to_ISO = "INTERNAL to ISO";
	public static final String UOM_Convert_Internal_to_Local = "INTERNAL to Local";
	public static final String UOM_Convert_None = "None";
	public static final String UOM_Convert_ISO_to_INTERNAL = "ISO to INTERNAL";
	public static final String UOM_Convert_ISO_to_Local = "ISO to Local";
	public static final String UOM_Convert_Local_to_ISO = "Local to ISO";
	public static final String UOM_Convert_Local_to_Internal = "Local to INTERNAL";

	public final static String requiredJavaVersion = "1.7";
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
	public final static Font font_combo = new java.awt.Font("Monospaced", 0, 11);
	public final static Font font_table_header = new Font("Arial", Font.BOLD, 10);
	public final static Font font_table = new java.awt.Font("Monospaced", 0, 11);
	public final static Font font_textArea = new java.awt.Font("Monospaced", 0, 14);
	
	public final static Color color_textfield_foreground_focus_color = Color.BLACK;
	public final static Color color_textfield_forground_nofocus_color = Color.BLACK;
	//public final static Color color_textfield_background_focus_color = new Color(255, 212, 121);
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
	public final static String image_path = System.getProperty("user.dir")+File.separator+"images"+File.separator;
	public final static String auto_label_command = System.getProperty("user.dir")+File.separator+"autolabeller"+File.separator+"commands"+File.separator;
	public final static String auto_label_labels = System.getProperty("user.dir")+File.separator+"autolabeller"+File.separator+"labels"+File.separator;
	
	public final static Icon icon_resend = Common.imageIconloader.getImageIcon(Common.image_resend);
	public final static Icon icon_history = Common.imageIconloader.getImageIcon(Common.image_history);
	public final static Icon icon_pdf = Common.imageIconloader.getImageIcon(Common.image_pdf);
	public final static Icon icon_dictionary = Common.imageIconloader.getImageIcon(Common.image_dictionary);
	public final static Icon icon_clock = Common.imageIconloader.getImageIcon(Common.image_clock);
	public final static Icon icon_copy = Common.imageIconloader.getImageIcon(Common.image_copy);
	public final static Icon icon_calendar = Common.imageIconloader.getImageIcon(Common.image_calendar);
	public final static Icon icon_clear = Common.imageIconloader.getImageIcon(Common.image_clear);
	public final static Icon icon_ascending = Common.imageIconloader.getImageIcon(Common.image_ascending);
	public final static Icon icon_descending = Common.imageIconloader.getImageIcon(Common.image_descending);
	public final static Icon icon_home = Common.imageIconloader.getImageIcon(Common.image_home);
	public final static Icon icon_execute = Common.imageIconloader.getImageIcon(Common.image_execute);
	public final static Icon icon_spacer = Common.imageIconloader.getImageIcon(Common.image_spacer);
	public final static Icon icon_connect = Common.imageIconloader.getImageIcon(Common.image_connect);
	public final static Icon icon_cascade = Common.imageIconloader.getImageIcon(Common.image_cascade);
	public final static Icon icon_minimize = Common.imageIconloader.getImageIcon(Common.image_minimize);
	public final static Icon icon_restore = Common.imageIconloader.getImageIcon(Common.image_restore);
	//public final static Icon icon_save = Common.imageIconloader.getImageIcon(Common.image_save);
	public final static Icon icon_find = Common.imageIconloader.getImageIcon(Common.image_find);
	public final static Icon icon_close = Common.imageIconloader.getImageIcon(Common.image_close);
	public final static Icon icon_lookup = Common.imageIconloader.getImageIcon(Common.image_lookup);
	public final static Icon icon_print = Common.imageIconloader.getImageIcon(Common.image_print);
	public final static Icon icon_uom = Common.imageIconloader.getImageIcon(Common.image_units);
	public final static Icon icon_customer = Common.imageIconloader.getImageIcon(Common.image_customer);
	public final static Icon icon_mhn = Common.imageIconloader.getImageIcon(Common.image_mhn);
	public final static Icon icon_help = Common.imageIconloader.getImageIcon(Common.image_help);
	public final static Icon icon_delete = Common.imageIconloader.getImageIcon(Common.image_delete);
	public final static Icon icon_edit = Common.imageIconloader.getImageIcon(Common.image_edit);
	public final static Icon icon_add = Common.imageIconloader.getImageIcon(Common.image_add);
	public final static Icon icon_permissions = Common.imageIconloader.getImageIcon(Common.image_permissions);
	public final static Icon icon_rename = Common.imageIconloader.getImageIcon(Common.image_rename);
	public final static Icon icon_refresh = Common.imageIconloader.getImageIcon(Common.image_refresh);
	public final static Icon icon_arrow_right = Common.imageIconloader.getImageIcon(Common.image_arrow_right);
	public final static Icon icon_arrow_left = Common.imageIconloader.getImageIcon(Common.image_arrow_left);
	public final static Icon icon_arrow_up = Common.imageIconloader.getImageIcon(Common.image_arrow_up);
	public final static Icon icon_arrow_down = Common.imageIconloader.getImageIcon(Common.image_arrow_down);
	public final static Icon icon_undo = Common.imageIconloader.getImageIcon(Common.image_undo);
	public final static Icon icon_blank_icon = Common.imageIconloader.getImageIcon(Common.image_blank_icon);
	public final static Icon icon_menu = Common.imageIconloader.getImageIcon(Common.image_menu);
	public final static Icon icon_form = Common.imageIconloader.getImageIcon(Common.image_form);
	public final static Icon icon_scanner = Common.imageIconloader.getImageIcon(Common.image_scanner);
	public final static Icon icon_report = Common.imageIconloader.getImageIcon(Common.image_report);
	public final static Icon icon_function = Common.imageIconloader.getImageIcon(Common.image_function);
	public final static Icon icon_error = Common.imageIconloader.getImageIcon(Common.image_error);
	public final static Icon icon_select = Common.imageIconloader.getImageIcon(Common.image_select);
	public final static Icon icon_expand_all = Common.imageIconloader.getImageIcon(Common.image_expand_all);
	public final static Icon icon_collapse_all = Common.imageIconloader.getImageIcon(Common.image_collapse_all);
	public final static Icon icon_expand_node = Common.imageIconloader.getImageIcon(Common.image_expand_node);
	public final static Icon icon_collapse_node = Common.imageIconloader.getImageIcon(Common.image_collapse_node);
	public final static Icon icon_splash = Common.imageIconloader.getImageIcon(Common.image_splash);
	public final static Icon icon_osx_commander4j = Common.imageIconloader.getImageIcon(Common.image_osx_commander4j);
	public final static Icon icon_ok = Common.imageIconloader.getImageIcon(Common.image_ok);
	public final static Icon icon_cancel = Common.imageIconloader.getImageIcon(Common.image_cancel);
	public final static Icon icon_search = Common.imageIconloader.getImageIcon(Common.image_search);
	public final static Icon icon_user = Common.imageIconloader.getImageIcon(Common.image_user);
	public final static Icon icon_update = Common.imageIconloader.getImageIcon(Common.image_update);
	public final static Icon icon_user_disabled = Common.imageIconloader.getImageIcon(Common.image_user_disabled);
	public final static Icon icon_user_locked = Common.imageIconloader.getImageIcon(Common.image_user_locked);
	public final static Icon icon_user_expired = Common.imageIconloader.getImageIcon(Common.image_user_expired);
	public final static Icon icon_interface = Common.imageIconloader.getImageIcon(Common.image_interface);
	public final static Icon icon_XLS = Common.imageIconloader.getImageIcon(Common.image_XLS);
	public final static Icon icon_hold = Common.imageIconloader.getImageIcon(Common.image_hold);
	public final static Icon icon_release = Common.imageIconloader.getImageIcon(Common.image_release);
	public final static Icon icon_process_order = Common.imageIconloader.getImageIcon(Common.image_process_order);
	public final static Icon icon_material = Common.imageIconloader.getImageIcon(Common.image_material);
	public final static Icon icon_location = Common.imageIconloader.getImageIcon(Common.image_location);
	public final static Icon icon_batch = Common.imageIconloader.getImageIcon(Common.image_batch);
	public final static Icon icon_pallet = Common.imageIconloader.getImageIcon(Common.image_pallet);
	public final static Icon icon_split = Common.imageIconloader.getImageIcon(Common.image_split);
	public final static Icon icon_clone = Common.imageIconloader.getImageIcon(Common.image_clone);
	public final static Icon icon_auto_labeller = Common.imageIconloader.getImageIcon(Common.image_auto_label);
	public final static Icon icon_confirm = Common.imageIconloader.getImageIcon(Common.image_confirm);
	public final static Icon icon_groups = Common.imageIconloader.getImageIcon(Common.image_groups);
	public final static Icon icon_remove = Common.imageIconloader.getImageIcon(Common.image_remove);
	
	public final static String image_remove = "remove.gif";
	public final static String image_resend = "resend.gif";
	public final static String image_history = "pallet_history.gif";
	public final static String image_pdf = "pdf.gif";
	public final static String image_csv = "csv.gif";
	public final static String image_jasperreport = "jasperreport.gif";
	public final static String image_msaccess = "msaccess.jpg";
	public final static String image_clone = "clone.gif";
	public final static String image_dictionary = "dictionary.gif";
	public final static String image_clear = "edit_clear.png";
	public final static String image_clock = "clock.gif";
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
	public final static String image_spacer = "spacer.gif";
	public final static String image_connect = "connect.gif";
	public final static String image_cascade = "cascade.gif";
	public final static String image_minimize = "minimize.gif";
	public final static String image_restore = "restore.gif";
	public final static String image_save = "save.gif";
	public final static String image_find = "find.gif";
	public final static String image_close = "exit.gif";
	public final static String image_mhn = "mhn.gif";
	public final static String image_lookup = "lookup.gif";
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
	public final static String image_scanner = "scanner.gif";
	public final static String image_interface = "interface.gif";
	public final static String image_units = "units.gif";
	public final static String image_customer = "customer.gif";
	public final static String image_report = "report.gif";
	public final static String image_function = "function.gif";
	public final static String image_error = "error.gif";
	public final static String image_select = "ok.gif";
	public final static String image_expand_all = "expandall.gif";
	public final static String image_collapse_all = "collapseall.gif";
	public final static String image_expand_node = "expandnode.gif";
	public final static String image_collapse_node = "collapsenode.gif";
	public final static String image_splash = "splash.gif";
	public final static String image_osx_commander4j = "osx_commander4j.gif";
	public final static String image_osx_setup4j = "osx_setup4j.gif";
	public final static String image_osx_clone4j = "osx_clone4j.gif";
	public final static String image_ok = "ok.gif";
	public final static String image_cancel = "cancel.gif";
	public final static String image_split = "split.gif";
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
	public final static String image_auto_label = "auto_labeller.png";
	public final static String image_confirm = "CMD_Icon.gif";

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

	}

}
