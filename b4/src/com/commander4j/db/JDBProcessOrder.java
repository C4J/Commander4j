// $codepro.audit.disable numericLiterals
package com.commander4j.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

/**
 * @author David
 */
public class JDBProcessOrder
{
	private BigDecimal dbRequiredQuantity;
	private String dbRequiredUom;
	private String dbDescription;
	private Timestamp dbDueDate;
	private String dbErrorMessage;
	private String dbLocationId;
	private String dbMaterial;
	private String dbProcessOrder;
	private String dbRecipeId;
	private String dbRequiredResource;
	private String dbCustomerID;
	private String dbStatus;
	private String dbDefaultPalletStatus;
	private String dbInspectionID;
	private final Logger logger = Logger.getLogger(JDBProcessOrder.class);
	public static int field_process_order = 10;
	public static int field_recipe_id = 20;
	public static int field_description = 60;
	public static int field_status = 15;
	public static int field_required_resource = 50;
	private JDBMaterial material;
	private JDBMaterialUom matuom;
	private JDBLocation location;
	private JDBCustomer customer;
	private JDBControl ctrl;
	private JDBProcessOrderResource poRes;

	private String hostID;
	private String sessionID;

	public JDBProcessOrder(ResultSet rs)
	{
		clear();
		getPropertiesfromResultSet(rs);
	}

	public JDBProcessOrder(String host, String session)
	{
		clear();
		setHostID(host);
		setSessionID(session);
		material = new JDBMaterial(getHostID(), getSessionID());
		location = new JDBLocation(getHostID(), getSessionID());
		customer = new JDBCustomer(getHostID(), getSessionID());
		matuom = new JDBMaterialUom(getHostID(), getSessionID());
		poRes = new JDBProcessOrderResource(getHostID(), getSessionID());
		ctrl = new JDBControl(getHostID(), getSessionID());
	}

	private JDBProcessOrder(String process_order, String material, String description, String status, String location, Timestamp due_date, String recipe, String palletStatus, String reqdResource)
	{
		clear();
		setProcessOrder(process_order);
		setMaterial(material);
		setDescription(description);
		setStatus(status);
		setLocation(location);
		setDueDate(due_date);
		setRecipe(recipe);
		setDefaultPalletStatus(palletStatus);
		setRequiredResource(reqdResource);
	}

	public LinkedList<String> getResourceList(String line,String group) {
		LinkedList<String> resourceList = new LinkedList<String>();
		PreparedStatement stmt;
		ResultSet rs;
		setErrorMessage("");

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.getResources"));
			stmt.setFetchSize(250);
			stmt.setString(1, line);
			stmt.setString(2, group);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				String resource = new String();
				resource = rs.getString("required_resource");
				resourceList.add(resource);
			}
			rs.close();
			stmt.close();

		}
		catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return resourceList;
	}
	
	public void clear()
	{
		setProcessOrder("");
		setMaterial("");
		setDescription("");
		setStatus("");
		setDefaultPalletStatus("");
		setLocation("");
		setDueDate(null);
		setRecipe("");
		setRequiredQuantity(new BigDecimal(0));
	}

	/**
	 * Method create.
	 * 
	 * @return boolean
	 */
	public boolean create()
	{

		logger.debug("create [" + getProcessOrder() + "]");

		boolean result = false;

		if (isValid() == true)
		{

			if (isValidProcessOrder(getProcessOrder()) == true)
			{
				setErrorMessage("Key violation - material [" + getMaterial() + "] already exists !");
			} else
			{
				try
				{
					PreparedStatement stmtupdate;
					stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.create"));
					stmtupdate.setString(1, getProcessOrder());
					stmtupdate.execute();
					stmtupdate.clearParameters();
					Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
					result = update();
					stmtupdate.close();
				} catch (SQLException e)
				{
					setErrorMessage(e.getMessage());
				}
			}
		}

		return result;
	}

	/**
	 * Method delete.
	 * 
	 * @return boolean
	 */
	public boolean delete()
	{

		PreparedStatement stmtupdate;
		boolean result = false;
		setErrorMessage("");
		logger.debug("delete");

		try
		{
			if (isValidProcessOrder() == true)
			{
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.delete"));
				stmtupdate.setString(1, getProcessOrder());
				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();

				stmtupdate.close();
				result = true;
			}
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		return result;
	}

	public String formatProcessOrderNo(String processOrderNo)
	{
		String result = "error";
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String processOrderNoFormat = "{NNNNNNNN}";
		if (ctrl.getProperties("PROCESS ORDER NUMBER FORMAT") == true)
		{
			processOrderNoFormat = ctrl.getKeyValue();
		}
		result = JUtility.formatNumber(processOrderNo, processOrderNoFormat);
		return result;
	}

	/**
	 * Method generateNewProcessOrderNo.
	 * 
	 * @return String
	 */
	public String generateNewProcessOrderNo()
	{
		String result = "error";
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String processOrderNo = "1";
		int SeqNumber = 0;
		boolean retry = true;

		do
		{
			if (ctrl.lockRecord("PROCESS ORDER NUMBER") == true)
			{

				if (ctrl.getProperties("PROCESS ORDER NUMBER") == true)
				{
					processOrderNo = ctrl.getKeyValue();
					SeqNumber = Integer.parseInt(processOrderNo);

					result = formatProcessOrderNo(processOrderNo);
					setProcessOrder(result);
					retry = false;

					SeqNumber++;
					processOrderNo = String.valueOf(SeqNumber);
					ctrl.setKeyValue(processOrderNo);
					ctrl.update();
				}
			}

		} while (retry);

		return result;
	}

	public String getCustomerID()
	{
		String result = JUtility.replaceNullStringwithBlank(dbCustomerID);
		if (result.equals(""))
		{
			result = "SELF";
		}
		return result;
	}

	public String getDefaultPalletStatus()
	{

		String result = "";
		result = JUtility.replaceNullStringwithBlank(dbDefaultPalletStatus);
		if (result.equals("") == true)
		{
			JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
			ctrl.getProperties("DEFAULT PALLET STATUS");
			result = ctrl.getKeyValue();
		}
		return result;

	}

	/**
	 * Method getDescription.
	 * 
	 * @return String
	 */
	public String getDescription()
	{
		return dbDescription;
	}

	/**
	 * Method getDueDate.
	 * 
	 * @return Timestamp
	 */
	public Timestamp getDueDate()
	{
		return dbDueDate;
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage()
	{
		return dbErrorMessage;
	}

	private String getHostID()
	{
		return hostID;
	}

	public String getInspectionID()
	{
		return JUtility.replaceNullStringwithBlank(dbInspectionID);
	}

	/**
	 * Method getLocation.
	 * 
	 * @return String
	 * @uml.property name="location"
	 */
	public String getLocation()
	{
		return dbLocationId;
	}

	/**
	 * Method getMaterial.
	 * 
	 * @return String
	 * @uml.property name="material"
	 */
	public String getMaterial()
	{
		return dbMaterial;
	}

	/**
	 * Method getProcessOrder.
	 * 
	 * @return String
	 */
	public String getProcessOrder()
	{
		return dbProcessOrder;
	}

	/**
	 * Method getProcessOrderData.
	 * 
	 * @param criteria
	 *            PreparedStatement
	 * @return Vector<JDBProcessOrder>
	 */
	public Vector<JDBProcessOrder> getProcessOrderData(PreparedStatement criteria)
	{
		ResultSet rs;
		Vector<JDBProcessOrder> result = new Vector<JDBProcessOrder>();

		if (Common.hostList.getHost(getHostID()).toString().equals(null))
		{
			result.addElement(new JDBProcessOrder("process_order", "material", "description", "status", "location_id", null, "recipe_id", "pallet_status", "resource"));
		} else
		{
			try
			{
				rs = criteria.executeQuery();

				while (rs.next())
				{
					result.addElement(new JDBProcessOrder(rs.getString("process_order"), rs.getString("material"), rs.getString("description"), rs.getString("status"), rs.getString("location_id"), rs.getTimestamp("due_date"), rs
							.getString("recipe_id"), rs.getString("default_pallet_status"), rs.getString("required_resource")));
				}
				rs.close();

			} catch (Exception e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

	public ResultSet getProcessOrderDataResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		} catch (Exception e)
		{
			rs = null;
			setErrorMessage(e.getMessage());
		}

		return rs;
	}

	/**
	 * Method getProcessOrderProperties.
	 * 
	 * @return boolean
	 */
	public boolean getProcessOrderProperties()
	{
		boolean result;
		result = getProcessOrderProperties(getProcessOrder());
		return result;
	}

	/**
	 * Method getProcessOrderProperties.
	 * 
	 * @param process_order
	 *            String
	 * @return boolean
	 */
	public boolean getProcessOrderProperties(String process_order)
	{
		boolean result = false;

		setErrorMessage("");

		clear();
		try
		{
			setProcessOrder(process_order);

			if (getProcessOrder().equals("") == false)
			{
				logger.debug("find [" + process_order + "]");
				PreparedStatement stmt;
				ResultSet rs;
				stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.getProcessOrderProperties"));
				stmt.setString(1, process_order);
				stmt.setFetchSize(1);
				rs = stmt.executeQuery();

				if (rs.next())
				{
					if (rs.getString("process_order").equals(process_order) == true)
					{
						getPropertiesfromResultSet(rs);
						result = true;
					}
				} else
				{
					setErrorMessage("Invalid Process Order [" + process_order + "]");
				}
				rs.close();
				stmt.close();
			}

		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
		return result;

	}
	
	public String getRequiredUOMQuantity()
	{
		String result = "0";
		Double dbReqdDenom = new Double(0);
		Double dbReqdNumer = new Double(0);
		Double dbCaseQuantity = new Double(0);
		
		if (matuom.getMaterialUomProperties(getMaterial(), getRequiredUom()))
		{
			dbReqdNumer = Double.valueOf(matuom.getNumerator());
			dbReqdDenom = Double.valueOf(matuom.getDenominator());
			dbCaseQuantity = dbReqdNumer / dbReqdDenom;
		}
		
		NumberFormat formatter = new DecimalFormat("#.000");
		result = formatter.format(dbCaseQuantity.doubleValue()); // -1234.567000
		return result;

	}

	public String getFullPalletQuantity()
	{

		String result = "";

		Double dbProductionQuantity = new Double(0);
		Double dbReqdDenom = new Double(0);
		Double dbReqdNumer = new Double(0);
		Double dbFullDenom = new Double(0);
		Double dbFullNumer = new Double(0);
		Double step1 = new Double(0);
		Double step2 = new Double(0);
		String dbFullUOM = "PAL";

		try
		{
			if (matuom.getMaterialUomProperties(getMaterial(), getRequiredUom()))
			{
				dbReqdDenom = Double.valueOf(matuom.getDenominator());
				dbReqdNumer = Double.valueOf(matuom.getNumerator());

				dbFullUOM = ctrl.getKeyValueWithDefault("UOM FULL PALLET QTY", "PAL", "Uom for full pallet");
				if (matuom.getMaterialUomProperties(getMaterial(), dbFullUOM))
				{
					dbFullDenom = Double.valueOf(matuom.getDenominator());
					dbFullNumer = Double.valueOf(matuom.getNumerator());

					step1 = dbFullNumer / dbFullDenom;
					step2 = dbReqdDenom / dbReqdNumer;
					dbProductionQuantity = step1 * step2;
				}
			}
		} catch (Exception ex)
		{
			dbProductionQuantity = new Double(0);
		}
		NumberFormat formatter = new DecimalFormat("#.000");
		result = formatter.format(dbProductionQuantity.doubleValue()); // -1234.567000
		return result;
	}

	public void getPropertiesfromResultSet(ResultSet rs)
	{
		try
		{
			clear();
			setProcessOrder(rs.getString("process_order"));
			setMaterial(rs.getString("material"));
			setDescription(rs.getString("description"));
			setStatus(rs.getString("status"));
			setDefaultPalletStatus(rs.getString("default_pallet_status"));
			setLocation(rs.getString("location_id"));
			setDueDate(rs.getTimestamp("due_date"));
			setRecipe(rs.getString("recipe_id"));
			setRequiredQuantity(rs.getBigDecimal("required_quantity"));
			setRequiredUom(rs.getString("required_uom"));
			setRequiredResource(rs.getString("required_resource"));
			setCustomerID(rs.getString("customer_id"));
			setInspectionID(rs.getString("inspection_id"));
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}
	}

	/**
	 * Method getRecipe.
	 * 
	 * @return String
	 */
	public String getRecipe()
	{
		return dbRecipeId;
	}

	public BigDecimal getRequiredQuantity()
	{
		return dbRequiredQuantity;
	}

	public String getRequiredResource()
	{
		return JUtility.replaceNullStringwithBlank(dbRequiredResource);
	}

	public String getRequiredUom()
	{
		return dbRequiredUom;
	}

	private String getSessionID()
	{
		return sessionID;
	}

	/**
	 * Method getStatus.
	 * 
	 * @return String
	 */
	public String getStatus()
	{
		return dbStatus;
	}

	/**
	 * Method isValid.
	 * 
	 * @return boolean
	 */
	public boolean isValid()
	{
		boolean result = true;

		if (material.isValidMaterial(getMaterial()) == false)
		{
			result = false;
			setErrorMessage(material.getErrorMessage());
		} else
		{
			if (location.isValidLocation(getLocation()) == false)
			{
				result = false;
				setErrorMessage(location.getErrorMessage());
			} else
			{
				if (matuom.isValidMaterialUom(getMaterial(), getRequiredUom()) == false)
				{
					result = false;
					setErrorMessage(matuom.getErrorMessage());
				} else
				{
					if (customer.isValidCustomer(getCustomerID()) == false)
					{
						result = false;
						setErrorMessage(customer.getErrorMessage());
					}
				}
			}
		}

		return result;
	}

	/**
	 * Method isValidProcessOrder.
	 * 
	 * @return boolean
	 */
	public boolean isValidProcessOrder()
	{

		PreparedStatement stmt;
		ResultSet rs;
		boolean result = false;

		try
		{
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.isValidProcessOrder"));
			stmt.setString(1, getProcessOrder());
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				result = true;
			} else
			{
				setErrorMessage("Invalid Process Order " + getProcessOrder());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e)
		{
			setErrorMessage(e.getMessage());
		}

		logger.debug("isValidProcessOrder :" + result);

		return result;

	}

	/**
	 * Method isValidProcessOrder.
	 * 
	 * @param processOrder
	 *            String
	 * @return boolean
	 */
	public boolean isValidProcessOrder(String processOrder)
	{
		setProcessOrder(processOrder);
		return isValidProcessOrder();
	}

	public void setCustomerID(String custID)
	{
		dbCustomerID = JUtility.replaceNullStringwithBlank(custID);
		if (dbCustomerID.equals(""))
		{
			dbCustomerID = "SELF";
		}
	}

	public void setDefaultPalletStatus(String palletStatus)
	{
		dbDefaultPalletStatus = palletStatus;
	}

	/**
	 * Method setDescription.
	 * 
	 * @param description
	 *            String
	 */
	public void setDescription(String description)
	{
		dbDescription = description;
	}

	/**
	 * Method setDueDate.
	 * 
	 * @param due_date
	 *            Timestamp
	 */
	public void setDueDate(Timestamp due_date)
	{
		dbDueDate = due_date;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param ErrorMsg
	 *            String
	 */
	private void setErrorMessage(String ErrorMsg)
	{
		if (ErrorMsg.isEmpty() == false)
		{
			logger.debug(ErrorMsg);
		}
		dbErrorMessage = ErrorMsg;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	public void setInspectionID(String inspectionID)
	{
		dbInspectionID = JUtility.replaceNullStringwithBlank(inspectionID);
	}

	/**
	 * Method setLocation.
	 * 
	 * @param location
	 *            String
	 */
	public void setLocation(String location)
	{
		dbLocationId = location;
	}

	/**
	 * Method setMaterial.
	 * 
	 * @param material
	 *            String
	 */
	public void setMaterial(String material)
	{
		dbMaterial = material;
	}

	/**
	 * Method setProcessOrder.
	 * 
	 * @param process_order
	 *            String
	 */
	public void setProcessOrder(String process_order)
	{
		dbProcessOrder = process_order;
	}

	/**
	 * Method setRecipe.
	 * 
	 * @param recipe
	 *            String
	 */
	public void setRecipe(String recipe)
	{
		dbRecipeId = recipe;
	}

	public void setRequiredQuantity(BigDecimal quantity)
	{
		if (quantity == null)
		{
			quantity = new BigDecimal("0");
		}

		dbRequiredQuantity = quantity;

	}

	public void setRequiredResource(String reqdRes)
	{
		dbRequiredResource = JUtility.replaceNullStringwithBlank(reqdRes);
	}

	public void setRequiredUom(String u)
	{
		dbRequiredUom = JUtility.replaceNullStringwithBlank(u);
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	/**
	 * Method setStatus.
	 * 
	 * @param status
	 *            String
	 */
	public void setStatus(String status)
	{
		dbStatus = status;
	}

	/**
	 * Method update.
	 * 
	 * @return boolean
	 */
	public boolean update()
	{
		boolean result = false;

		logger.debug("update [" + getProcessOrder() + "]");

		if (isValid() == true)
		{
			try
			{
				PreparedStatement stmtupdate;
				stmtupdate = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBProcessOrder.update"));

				stmtupdate.setString(1, getMaterial());
				stmtupdate.setString(2, getDescription());
				stmtupdate.setString(3, getStatus());
				stmtupdate.setString(4, getLocation());
				stmtupdate.setTimestamp(5, getDueDate());
				stmtupdate.setString(6, getRecipe());
				stmtupdate.setBigDecimal(7, getRequiredQuantity());
				stmtupdate.setString(8, getRequiredUom());
				stmtupdate.setString(9, getDefaultPalletStatus());
				stmtupdate.setTimestamp(10, JUtility.getSQLDateTime());
				stmtupdate.setString(11, getRequiredResource());
				stmtupdate.setString(12, getCustomerID());
				stmtupdate.setString(13, getInspectionID());
				stmtupdate.setString(14, getProcessOrder());

				stmtupdate.execute();
				stmtupdate.clearParameters();
				Common.hostList.getHost(getHostID()).getConnection(getSessionID()).commit();
				stmtupdate.close();
				result = true;
				
				poRes.create(getRequiredResource());
				
			} catch (SQLException e)
			{
				setErrorMessage(e.getMessage());
			}
		}

		return result;
	}

}
