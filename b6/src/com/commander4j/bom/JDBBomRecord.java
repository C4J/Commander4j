package com.commander4j.bom;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.commander4j.util.JUtility;

public class JDBBomRecord
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(JDBBomRecord.class);
	
	private boolean modified =  false;
	
	private String hostID;
	private String sessionID;
	
	private String bom_id = "";

	private String bom_version = "";
	private Integer bom_sequence = 0;

	private String data_id = "";
	private String data_type = "";
	private String data_string = "";
	private BigDecimal data_decimal = new BigDecimal("0");
	private Date data_date;
	private Timestamp data_timestamp;
	private String uuid = "";
	private String parent_uuid = "";
	private ImageIcon icon;
	private String iconFilename = "";
	private String imagePath16 = System.getProperty("user.dir") + File.separator + "images" + File.separator + "16x16" + File.separator;
	private UUID uuidnew = UUID.randomUUID();
	private JDBBomElement element;
	
	
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
	
	public JDBBomRecord(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
		
		element = new JDBBomElement(getHostID(),getSessionID());
	}

	public void clear()
	{
		setBOMId("");
		setBOMVersion("");
		setBOMSequence(0);
		setDataId("");
		setDataType("");
		setDataString(null);
		setDataDecimal(null);
		setDataDate(null);
		setDataTimestamp(null);
		setUuid("");
		setParent_uuid(null);
	}
	
	public boolean isModified()
	{
		return modified;
	}

	public void setModified(boolean modified)
	{
		this.modified = modified;
	}
	
	public JDBBomElement getElement()
	{
		return element;
	}
	
	public String newUUID()
	{
		String result = "";
		result = uuidnew.toString();
		
		return result;
	}
	
	public void setBOMSequence(Integer seq)
	{
		bom_sequence = seq;
	}
	
	public Integer getBOMSequence()
	{
		return bom_sequence;
	}
	
	
	public Timestamp getDataTimestamp()
	{
		return data_timestamp;
	}

	public void setDataTimestamp(Timestamp datevalue)
	{
		this.data_timestamp = datevalue;
	}

	public Date getDataDate()
	{
		return data_date;
	}

	public void setDataDate(Date datevalue)
	{
		this.data_date = datevalue;
	}

	public void setIconFilename(String filename)
	{
		iconFilename = JUtility.replaceNullStringwithBlank(filename);
	}

	public String getIconFilename()
	{
		return iconFilename;
	}

	public String getBOMId()
	{
		return bom_id;
	}

	public void setBOMId(String id)
	{
		this.bom_id = JUtility.replaceNullStringwithBlank(id);
	}
	
	public String getBOMVersion()
	{
		return bom_version;
	}

	public void setBOMVersion(String ver)
	{
		this.bom_version = JUtility.replaceNullStringwithBlank(ver);
	}

	public String getDataType()
	{
		return data_type;
	}

	public void setDataType(String data)
	{
		this.data_type = JUtility.replaceNullStringwithBlank(data);
	}

	public String getDataId()
	{
		return data_id;
	}

	public void setDataId(String id)
	{
		this.data_id = JUtility.replaceNullStringwithBlank(id);
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uid)
	{
		this.uuid = JUtility.replaceNullStringwithBlank(uid);
	}

	public String getDataString()
	{
		return data_string;
	}

	public void setDataString(String data)
	{
		this.data_string = JUtility.replaceNullStringwithBlank(data);
	}

	public BigDecimal getDataDecimal()
	{
		return data_decimal;
	}

	public void setDataDecimal(BigDecimal data)
	{
		this.data_decimal = data;
	}

	public String getParent_uuid()
	{
		return parent_uuid;
	}

	public void setParent_uuid(String parent_uid)
	{
		this.parent_uuid = JUtility.replaceNullStringwithBlank(parent_uid);
	}

	public ImageIcon getIcon()
	{

		String filename = element.getElementRecord().getIcon_filename();
		
		if (filename.equals(""))
		{
			filename = "default.png";
		}
		
		setIconFilename(imagePath16 + filename);
		
		
		icon = new ImageIcon(getIconFilename());
		
		return icon;
	}

	public String getString()
	{
		String result = "";


			if (getDataId().equals("root"))
			{
				result = getBOMId()+"/"+getBOMVersion();
			}
			else
			{

				switch (getDataType())
				{
				case "string":
					result = getDataString();
					break;
				case "decimal":
					result = getDataDecimal().toString();
					break;
				case "date":
					result = JUtility.getISODateStringFormat(getDataDate());
					break;
				case "timestamp":
					result = JUtility.getISOTimeStampStringFormat(getDataTimestamp());
					result = result.replace("T", " ");
					break;
				default:
				}
			}

		return result;
	}

	public String toString()
	{
		String result = "";
		
			if (getDataId().equals("root"))
			{
				result =  element.getElementRecord().getDescription() + " = [" +  getBOMId()+"/"+getBOMVersion() + "]";
			}
			else
			{
				if (getString().equals(""))
				{
					result = element.getElementRecord().getDescription();
				}
				else
				{
				result = element.getElementRecord().getDescription() + " = [" + getString() + "]";
				}
			}

		return result;
	}

	public void getPropertiesFromResultSet(ResultSet rs)
	{
		clear();
		try
		{
			setBOMId(JUtility.replaceNullStringwithBlank(rs.getString("bom_id")));
			setBOMVersion(JUtility.replaceNullStringwithBlank(rs.getString("bom_version")));
			setBOMSequence(rs.getInt("bom_sequence"));

			setDataId(JUtility.replaceNullStringwithBlank(rs.getString("data_id")));
			setDataType(JUtility.replaceNullStringwithBlank(rs.getString("data_type")));

			switch (getDataType())
			{
			case "string":
				setDataString(JUtility.replaceNullStringwithBlank(rs.getString("data_string")));
				break;
			case "decimal":
				setDataDecimal(rs.getBigDecimal("data_decimal"));
				break;
			case "date":
				setDataDate(rs.getDate("data_date"));
				break;
			case "timestamp":
				setDataTimestamp(rs.getTimestamp("data_timestamp"));
				break;
			default:
			}

			setUuid(JUtility.replaceNullStringwithBlank(rs.getString("uuid")));

			setParent_uuid(JUtility.replaceNullStringwithBlank(rs.getString("parent_uuid")));
			
			element.getProperties( getDataId());

		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
	}

}
