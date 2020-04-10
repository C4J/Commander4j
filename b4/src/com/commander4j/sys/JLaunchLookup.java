package com.commander4j.sys;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLaunchLookup.java
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

import com.commander4j.db.JDBTable;
import com.commander4j.util.JUtility;


public class JLaunchLookup
{
	public String dlgKeyField;
	public String dlgCriteriaField;
	public String dlgOrderField;

	public static String dlgCriteriaDefault = "";

	public static boolean dlgAutoExec = false;

	public static String dlgResult = "";

	public JLaunchLookup()
	{
		dlgKeyField = "";
		dlgCriteriaField = "";
		dlgOrderField = "";
	}


	public JLaunchLookup(String key, String criteria, String order)
	{
		dlgKeyField = key;
		dlgCriteriaField = criteria;
		dlgOrderField = order;
	}


	public static boolean qmInspections() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_QM_INSPECTION"));
		JDialogLookup.dlg_title = "Inspections";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "inspection_id";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "inspection_id";
		JDialogLookup.dlg_sort_descending = true;

		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean wtProductGroups() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WEIGHT_PRODUCT_GROUP"));
		JDialogLookup.dlg_title = "Product Groups";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "product_group";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "product_group";
		JDialogLookup.dlg_sort_descending = false;

		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean wtContainerCode() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WEIGHT_CONTAINER_CODE"));
		JDialogLookup.dlg_title = "Container Codes";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "container_code";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "container_code";
		JDialogLookup.dlg_sort_descending = false;

		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	

	public static boolean processOrders() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PROCESS_ORDER"));
		JDialogLookup.dlg_title = "Process Orders";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "process_order";
		JDialogLookup.dlg_criteria_field_name_default = "status";
		JDialogLookup.dlg_orderBy_name_default = "due_date";
		JDialogLookup.dlg_sort_descending = true;


		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}

	
	public static boolean processOrdersResources() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PROCESS_ORDER"));
		JDialogLookup.dlg_title = "Process Orders";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "process_order";
		JDialogLookup.dlg_criteria_field_name_default = "required_resource";
		JDialogLookup.dlg_orderBy_name_default = "due_date";
		JDialogLookup.dlg_sort_descending = true;


		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean users() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}SYS_USERS"));
		JDialogLookup.dlg_title = "Users";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "user_id";
		JDialogLookup.dlg_criteria_field_name_default = "USER_ID";
		JDialogLookup.dlg_orderBy_name_default = "USER_ID";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean journeys() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_JOURNEY"));
		JDialogLookup.dlg_title = "Journeys";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "journey_ref";
		JDialogLookup.dlg_criteria_field_name_default = "LOCATION_ID_TO";
		JDialogLookup.dlg_orderBy_name_default = "TIMESLOT";
		JDialogLookup.dlg_sort_descending = false;

		//dlgCriteriaDefault = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}	

	public static boolean groups() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}SYS_GROUPS"));
		JDialogLookup.dlg_title = "Groups";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "group_id";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "description";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	

	public static boolean locations() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_LOCATION"));
		JDialogLookup.dlg_title = "Locations";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "location_id";
		JDialogLookup.dlg_criteria_field_name_default = "enabled";
		JDialogLookup.dlg_orderBy_name_default = "description";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean waste_locations() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WASTE_LOCATIONS"));
		JDialogLookup.dlg_title = "Waste Locations";

		JDialogLookup.hideDisabled=true;
		JDialogLookup.dlg_key_field_name = "waste_location_id";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "waste_location_id";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	

	public static boolean resources() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PROCESS_ORDER_RESOURCE"));
		JDialogLookup.dlg_title = "Resources";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "required_resource";
		JDialogLookup.dlg_criteria_field_name_default = "enabled";
		JDialogLookup.dlg_orderBy_name_default = "required_resource";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}	

	public static boolean modules() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}SYS_MODULES"));
		JDialogLookup.dlg_title = "Modules";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "MODULE_ID";
		JDialogLookup.dlg_criteria_field_name_default = "MODULE_TYPE";
		JDialogLookup.dlg_orderBy_name_default = "MODULE_ID";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}	
	
	public static boolean reasons() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MHN_REASONS"));
		JDialogLookup.dlg_title = "Reasons";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "reason";
		JDialogLookup.dlg_criteria_field_name_default = "DESCRIPTION";
		JDialogLookup.dlg_orderBy_name_default = "REASON";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean customers() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_CUSTOMER"));
		JDialogLookup.dlg_title = "Customers";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "customer_id";
		JDialogLookup.dlg_criteria_field_name_default = "customer_id";
		JDialogLookup.dlg_orderBy_name_default = "customer_name";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}	
	
	public static boolean weightSamplePoint() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WEIGHT_SAMPLE_POINT"));
		JDialogLookup.dlg_title = "Sample Points";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "sample_point";
		JDialogLookup.dlg_criteria_field_name_default = "sample_point";
		JDialogLookup.dlg_orderBy_name_default = "sample_point";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}	

	public static boolean materials() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MATERIAL"));
		JDialogLookup.dlg_title = "Materials";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "material";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "material_type";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}

	public static boolean waste_materials() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WASTE_MATERIAL"));
		JDialogLookup.dlg_title = "Waste Materials";

		JDialogLookup.hideDisabled=true;
		JDialogLookup.dlg_key_field_name = "waste_material_id";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "waste_material_id";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}
	
	public static boolean waste_reasons() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_WASTE_REASONS"));
		JDialogLookup.dlg_title = "Waste Reasons";

		JDialogLookup.hideDisabled=true;
		JDialogLookup.dlg_key_field_name = "waste_reason_id";
		JDialogLookup.dlg_criteria_field_name_default = "description";
		JDialogLookup.dlg_orderBy_name_default = "waste_reason_id";
		JDialogLookup.dlg_sort_descending = false;

		// dlg_criteria_default = "";
		dlgAutoExec = true;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}

	public static boolean materialBatches() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MATERIAL_BATCH"));
		JDialogLookup.dlg_title = "Material Batches";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "batch_number";
		JDialogLookup.dlg_criteria_field_name_default = "material";
		JDialogLookup.dlg_orderBy_name_default = "expiry_date";
		JDialogLookup.dlg_sort_descending = true;

		// dlg_criteria_default = "";
		// dlg_auto_exec = false;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}


	public static boolean pallets() {
		String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
		JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PALLET"));
		JDialogLookup.dlg_title = "Pallets";

		JDialogLookup.hideDisabled=false;
		JDialogLookup.dlg_key_field_name = "sscc";
		JDialogLookup.dlg_criteria_field_name_default = "MATERIAL";
		JDialogLookup.dlg_orderBy_name_default = "DATE_OF_MANUFACTURE";
		JDialogLookup.dlg_sort_descending = true;

		// dlg_criteria_default = "";
		// dlg_auto_exec = false;

		JDialogLookup inst = new JDialogLookup(Common.mainForm);

		inst.setVisible(true);

		dlgResult = JDialogLookup.dlg_selected_var.trim();

		return JDialogLookup.dlg_selected;
	}

}
