package com.commander4j.bom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;


public class JDBViewBomRecord
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBViewBomRecord.class);
	private String bom_id = "";
	private String bom_version = "";
	private Integer display_order = 0;
	private String input_output = "";
	private String stage = "";
	private String sequence = "";
	private String material = "";
	private String material_uuid = "";
	private String description = "";
	private BigDecimal quantity = new BigDecimal(0);
	private String type = "";
	private String uom = "";
	private String gtin = "";
	private String variant = "";
	private String location_id = "";
	private String location_uuid = "";
	
	private String hostID;
	private String sessionID;

	private void setHostID(String host)
	{
		hostID = host;
	}
	
	private String getHostID()
	{
		return hostID;
	}
	
	private String getSessionID()
	{
		return sessionID;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}
	
	public JDBViewBomRecord(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		
	}
	
	public void clear()
	{
		setBom_id("");
		setBom_version("");
		setDescription("");
		setInputOutput("");
		setDisplay_order(0);
		setGtin("");
		setLocation_id("");
		setLlocation_uuid("");
		setMaterial("");
		setQuantity(new BigDecimal(0));
		setSequence("");
		setStage("");
		setType("");
		setUom("");
		setVariant("");
	}
	
	public void getPropertiesFromResultSet(ResultSet rs)
	{
		clear();
		
		try
		{
			setBom_id(rs.getString("bom_id"));
			setBom_version(rs.getString("bom_version"));
			setDescription(rs.getString("description"));
			setInputOutput(rs.getString("input_output"));
			setDisplay_order(rs.getInt("display_order"));
			setGtin(rs.getString("gtin"));
			setLocation_id(rs.getString("location_id"));
			setLlocation_uuid(rs.getString("location_uuid"));
			setMaterial(rs.getString("material"));
			setMaterial_uuid(rs.getString("material_uuid"));
			setQuantity(rs.getBigDecimal("quantity"));
			setSequence(rs.getString("sequence"));
			setStage(rs.getString("stage"));
			setType(rs.getString("type"));
			setUom(rs.getString("uom"));
			setVariant(rs.getString("variant"));

		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
	}
	
	public LinkedList<JDBViewBomRecord> getBomMaterials(String bom_id, String bom_version,String inputoutput,String stage)
	{
		LinkedList<JDBViewBomRecord> result = new LinkedList<JDBViewBomRecord>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			//stmt = Common.host.con.prepareStatement("select * from VIEW_BOM where bom_id = ? and bom_version = ? and input_output = ? and stage = ?");
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.getBomMaterials"));

			
			stmt.setString(1, bom_id);
			stmt.setString(2, bom_version);
			stmt.setString(3, inputoutput);
			stmt.setString(4, stage);

			rs = stmt.executeQuery();

			while (rs.next())
			{

				JDBViewBomRecord vb = new JDBViewBomRecord(getHostID(),getSessionID());
				vb.getPropertiesFromResultSet(rs);
				
				result.addLast(vb);
	
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}
	
	public LinkedList<JDBViewBomRecord> getBomIndex()
	{
		LinkedList<JDBViewBomRecord> result = new LinkedList<JDBViewBomRecord>();

		try
		{
			PreparedStatement stmt;
			ResultSet rs;

			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement(Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("JDBViewBomRecord.getIndex"));

			rs = stmt.executeQuery();

			while (rs.next())
			{
				JDBViewBomRecord vb = new JDBViewBomRecord(getHostID(),getSessionID());
				vb.getPropertiesFromResultSet(rs);
				result.addLast(vb);
			}

			stmt.clearParameters();
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}
	
	public void setInputOutput(String val)
	{
		this.input_output = val;
	}
	
	public String getInputOutput()
	{
		return this.input_output;
	}
	
	public String getBom_id()
	{
		return bom_id;
	}
	public void setBom_id(String bom_id)
	{
		this.bom_id = bom_id;
	}
	public String getBom_version()
	{
		return bom_version;
	}
	public void setBom_version(String bom_version)
	{
		this.bom_version = bom_version;
	}
	public Integer getDisplay_order()
	{
		return display_order;
	}
	public void setDisplay_order(Integer display_order)
	{
		this.display_order = display_order;
	}
	public String getStage()
	{
		return stage;
	}
	public void setStage(String stage)
	{
		this.stage = stage;
	}
	public String getSequence()
	{
		return sequence;
	}
	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}
	public String getMaterial()
	{
		return material;
	}
	public void setMaterial(String material)
	{
		this.material = material;
	}
	public String getMaterial_uuid()
	{
		return material_uuid;
	}
	public void setMaterial_uuid(String material_uuid)
	{
		this.material_uuid = material_uuid;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public BigDecimal getQuantity()
	{
		return quantity;
	}
	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getUom()
	{
		return uom;
	}
	public void setUom(String uom)
	{
		this.uom = uom;
	}
	public String getGtin()
	{
		return gtin;
	}
	public void setGtin(String gtin)
	{
		this.gtin = gtin;
	}
	public String getVariant()
	{
		return variant;
	}
	public void setVariant(String variant)
	{
		this.variant = variant;
	}
	public String getLocation_id()
	{
		return location_id;
	}
	public void setLocation_id(String location_id)
	{
		this.location_id = location_id;
	}
	public String getLocation_uuid()
	{
		return location_uuid;
	}
	public void setLlocation_uuid(String location_uuid)
	{
		this.location_uuid = location_uuid;
	}
	
	public ResultSet getViewBomResultSet(PreparedStatement criteria)
	{

		ResultSet rs;

		try
		{
			rs = criteria.executeQuery();

		}
		catch (Exception e)
		{
			rs = null;

		}

		return rs;
	}
}
