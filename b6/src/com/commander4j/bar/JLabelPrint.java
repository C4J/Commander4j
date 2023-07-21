package com.commander4j.bar;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JLabelPrint.java
 * 
 * Package Name : com.commander4j.bar
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBCustomer;
import com.commander4j.db.JDBMaterial;
import com.commander4j.db.JDBMaterialType;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.sys.Common;
import com.commander4j.util.JFileIO;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

public class JLabelPrint
{
	private byte[] v_bytes;
	private String v_string = "";
	private PrintService v_printService = null;
	private DocPrintJob v_job;
	private DocFlavor v_flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	private Doc v_doc;
	private String v_queuename;
	private File v_template;
	private PreparedStatement v_ps;
	private HashMap<String, String> variables = new HashMap<String, String>();
	private HashMap<String, String> expanded_variables = new HashMap<String, String>();
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(JLabelPrint.class);
	private JDBControl ctrl = new JDBControl(Common.selectedHostID, Common.sessionID);
	private JDBProcessOrder po = new JDBProcessOrder(Common.selectedHostID, Common.sessionID);
	private JDBMaterial mat = new JDBMaterial(Common.selectedHostID, Common.sessionID);
	private JDBMaterialType mattype = new JDBMaterialType(Common.selectedHostID, Common.sessionID);
	private JDBCustomer cust = new JDBCustomer(Common.selectedHostID, Common.sessionID);
	private String hostID;
	private String sessionID;
	Boolean suppressUnicode = false;
	Boolean suppressASCIIEncode = false;
	
	public JLabelPrint(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public String getHostID()
	{
		return hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	private void setHostID(String host)
	{
		hostID = host;
	}

	private void setSessionID(String session)
	{
		sessionID = session;
	}

	public String getPalletLabelReportName(String processOrder)
	{
		String result = "";

		// Get Global Default Report ID
		result = ctrl.getKeyValue("DEFAULT_PALLET_REPORT");

		if (po.getProcessOrderProperties(processOrder))
		{

			if (mat.getMaterialProperties(po.getMaterial()))
			{

				// Check if there is a Material Type Override
				if (mattype.getMaterialTypeProperties(mat.getMaterialType()))
				{
					if (mattype.isOverridePalletLabel())
					{
						if (mattype.getPalletLabelModuleID().equals("") == false)
						{
							result = mattype.getPalletLabelModuleID();
						}
					} else
					{
						// Check if there is a Material Override
						if (mat.isOverridePalletLabel())
						{
							if (mat.getPalletLabelModuleID().equals("") == false)
							{
								result = mat.getPalletLabelModuleID();
							}
						}
					}
				}
			}

			// Check if there is a Customer Override
			if (po.getCustomerID().equals("") == false)
			{
				if (cust.getCustomerProperties(po.getCustomerID()))
				{
					if (cust.isOverridePalletLabel())
					{
						if (cust.getPalletLabelModuleID().equals("") == false)
						{
							result = cust.getPalletLabelModuleID();
						}
					}
				}
			}
		}

		logger.debug("getPalletLabelReportName = " + result);
		return result;
	}

	public String getPackLabelReportName(String processOrder)
	{
		String result = "";

		// Get Global Default Report ID
		result = ctrl.getKeyValue("DEFAULT_PACK_REPORT");

		if (po.getProcessOrderProperties(processOrder))
		{

			if (mat.getMaterialProperties(po.getMaterial()))
			{

				// Check if there is a Material Type Override
				if (mattype.getMaterialTypeProperties(mat.getMaterialType()))
				{
					if (mattype.isOverridePackLabel())
					{
						if (mattype.getPackLabelModuleID().equals("") == false)
						{
							result = mattype.getPackLabelModuleID();
						}
					} else
					{
						// Check if there is a Material Override
						if (mat.isOverridePackLabel())
						{
							if (mat.getPackLabelModuleID().equals("") == false)
							{
								result = mat.getPackLabelModuleID();
							}
						}
					}
				}
			}

			// Check if there is a Customer Override
			if (po.getCustomerID().equals("") == false)
			{
				if (cust.getCustomerProperties(po.getCustomerID()))
				{
					if (cust.isOverridePackLabel())
					{
						if (cust.getPackLabelModuleID().equals("") == false)
						{
							result = cust.getPackLabelModuleID();
						}
					}
				}
			}
		}

		logger.debug("getPacktLabelReportName = " + result);
		return result;
	}

	public void initLabelStaticData(String queuename, String filename, PreparedStatement ps)
	{

		try
		{
			v_ps = ps;
			v_queuename = queuename;
			v_printService = JPrint.getPrinterServicebyName(v_queuename);

			v_template = new File("labels/" + filename);
			v_string = getTemplate(v_template);

		} catch (Exception ex)
		{
			JUtility.errorBeep();
			JOptionPane.showMessageDialog(null, "Unable to print to selected printer : " + ex.getMessage(),
					"Printing Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void print(int copiesOfEachLabel, boolean incLabelHeaderText)
	{

		String all_labels = "";

		try
		{
			ResultSet rs;
			rs = v_ps.executeQuery();
			String current_label = "";

			while (rs.next())
			{

				expanded_variables = expandVariables(rs, variables);
				expanded_variables.put("EXPIRY_DATE_MODE", ctrl.getKeyValue("EXPIRY DATE MODE"));
				expanded_variables.put("PLANT", ctrl.getKeyValue("PLANT"));
				
				//Put number of copies into memory so it can be resolved within the label template.
				expanded_variables.put("ZPL_LABEL_COUNT",String.valueOf(copiesOfEachLabel));
				
				if (JUtility.replaceNullStringwithBlank(expanded_variables.get("COUNT_MODE")).equals("ZPL"))
				{
					//Override output to send only 1 label to printer and let ZPL provide required copies.
					copiesOfEachLabel=1;
				}
				
				if (JUtility.replaceNullStringwithBlank(expanded_variables.get("COUNT_MODE")).equals(""))
				{
					//For backwards compatibility = if mode not found in label template make sure ZPL_LABEL_COUNT = 1 as this function will send label x times
					expanded_variables.put("ZPL_LABEL_COUNT",String.valueOf(1));
				}
				
				if (incLabelHeaderText)
				{
					expanded_variables.put("HEADER_COMMENT", ctrl.getKeyValue("LABEL_HEADER_COMMENT"));
				} else
				{
					expanded_variables.put("HEADER_COMMENT", "");
				}

				optimiseEAN128();

				parseData pd = new parseData(getHostID(), getSessionID());
				pd.init(rs, expanded_variables);
				parseFunction pf = new parseFunction(getHostID(), getSessionID());
				pf.init(rs);

				for (int labelNo = 1; labelNo <= copiesOfEachLabel; labelNo++)
				{
					expanded_variables.put("LABEL_NO", String.valueOf(labelNo).toString().trim());

					current_label = v_string;

					// +++++++++++++++++++++++
					current_label = pd.doParsing(current_label);
					current_label = pf.doParsing(current_label);

					// current_label = replaceTokens(labelNo, rs, current_label,
					// incLabelHeaderText);
					all_labels = all_labels + current_label;
				}

			}
			rs.close();
			v_ps.close();

			v_job = v_printService.createPrintJob();

			String filename = System.getProperty("java.io.tmpdir") + UUID.randomUUID().toString() + ".cmd4j";

			if (suppressUnicode == false){
				all_labels = escapeUnicode(all_labels);
			}

			// Added for Intermec IPL control codes.
			if (suppressASCIIEncode == false)
			{
				all_labels = JUtility.encodeControlChars(all_labels);
			}
			
			v_bytes = all_labels.getBytes();

			logger.debug("Writing print job to : " + filename);
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(v_bytes);
			fos.close();

			DocAttributeSet das = new HashDocAttributeSet();

			try
			{
				PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
				pras.add(new Copies(1));

				FileInputStream fis = new FileInputStream(filename);
				v_doc = new SimpleDoc(fis, v_flavor, das);
				v_job.print(v_doc, pras);
				logger.debug("Submitting file to spooler : " + filename);
				fis.close();

				JFileIO deleteFile = new JFileIO();
				logger.debug("Deleting file : " + filename);
				deleteFile.deleteFile(filename);
			} catch (Exception ex)
			{

				if (Common.applicationMode.equals("SwingClient"))
				{
					JUtility.errorBeep();
					JOptionPane.showMessageDialog(null, "Unable to print to selected printer : " + ex.getMessage(),
							"Printing Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public HashMap<String, String> expandVariables(ResultSet rs, HashMap<String, String> hm)
	{
		HashMap<String, String> result = new HashMap<String, String>();

		Set<Entry<String, String>> set = hm.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();

		expanded_variables.put("EXPIRY_DATE_MODE", ctrl.getKeyValue("EXPIRY DATE MODE"));

		parseData pd = new parseData(getHostID(), getSessionID());
		pd.init(rs, expanded_variables);
		parseFunction pf = new parseFunction(getHostID(), getSessionID());
		pf.init(rs);

		String temp = "";

		while (i.hasNext())
		{
			Map.Entry<String, String> me = i.next();

			// ++++++++++++
			temp = pd.doParsing((String) me.getValue());
			temp = pf.doParsing(temp);

			result.put((String) me.getKey(), temp);

		}

		return result;
	}

	public void optimiseEAN128()
	{

		Set<Entry<String, String>> set = expanded_variables.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();
		String key = "";
		String originalBarcode = "";
		String optimisedBarcode = "";
		String currentMode = "";
		String newMode = "";
		String initialFNCReqd = "^";
		String pair = "";
		int start = 0;
		int end = 0;
		int size = 0;

		while (i.hasNext())
		{
			Map.Entry<String, String> me = i.next();

			key = (String) me.getKey();
			if (key.startsWith("BARCODE") == true)
			{
				optimisedBarcode = "";
				currentMode = "";
				originalBarcode = (String) me.getValue();
				size = originalBarcode.length();

				for (int x = 0; x < size; x = x + 2)
				{
					start = x;
					end = start + 2;
					if (end > size)
					{
						end = size;
					}
					pair = originalBarcode.substring(start, end);

					if (pair.startsWith("^") == false)
					{

						if ((pair.length() < 2) | (pair.endsWith("^")))
						{
							// Odd (single char at end of string //
							newMode = "alphanumeric";
						} else
						{
							try
							{
								Integer.valueOf(pair);
								newMode = "numeric";
							} catch (Exception ex)
							{
								// Not numeric //
								newMode = "alphanumeric";
							}

						}
						if (newMode.equals(currentMode) == false)
						{
							if (optimisedBarcode.equals(""))
							{
								initialFNCReqd = "^";
							} else
							{
								initialFNCReqd = "";
							}
							if (newMode.equals("numeric"))
							{
								optimisedBarcode = optimisedBarcode + expanded_variables.get("CODEC") + initialFNCReqd;
							}
							if (newMode.equals("alphanumeric"))
							{
								optimisedBarcode = optimisedBarcode + expanded_variables.get("CODEB") + initialFNCReqd;
							}

							currentMode = newMode;
						}
						optimisedBarcode = optimisedBarcode + pair;
					} else
					{
						optimisedBarcode = optimisedBarcode + "^";
						x--;
					}
				}

				optimisedBarcode = optimisedBarcode.replace("^", expanded_variables.get("FNC1"));

				expanded_variables.put(key, optimisedBarcode);
			}
		}
	}

	public String substVariables(String line)
	{
		String result = line;

		Set<Entry<String, String>> set = expanded_variables.entrySet();
		Iterator<Entry<String, String>> i = set.iterator();

		while (i.hasNext())
		{
			Map.Entry<String, String> me = i.next();
			result = result.replace((String) me.getKey(), (String) me.getValue());
		}

		return result;
	}

	public String getRSValue(ResultSet rs, String fieldname)
	{
		String result = "";
		if (isValidRSColumn(rs, fieldname))
		{
			try
			{
				result = rs.getString(fieldname);
			} catch (SQLException e)
			{

			}
		}
		return result;
	}

	public String replaceRSValue(String data, String placeMarker, ResultSet rs, String fieldname)
	{
		String result = data;

		if (data.contains(placeMarker))
		{

			if (isValidRSColumn(rs, fieldname))
			{
				try
				{
					result = result.replace(placeMarker, rs.getString(fieldname));
				} catch (SQLException e)
				{

				}
			}
		}
		return result;
	}

	public Boolean isValidRSColumn(ResultSet rs, String fieldname)
	{
		Boolean result = false;

		try
		{
			rs.findColumn(fieldname);
			result = true;
		} catch (SQLException e)
		{
			result = false;
		}

		return result;
	}

	public String getTemplate(File aFile)
	{

		StringBuilder contents = new StringBuilder();
		Boolean suppressEOL = false;
		suppressUnicode = false;
		suppressASCIIEncode = false;
		Boolean commandMode = false;

		try
		{
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try
			{
				String line = null;
				while ((line = input.readLine()) != null)
				{
					line = line.trim();
					if ((line.startsWith("/*") == false) & (line.length() > 0))
					{
						commandMode = false;
						
						if (line.startsWith("DEFINE BARCODE "))
						{
							commandMode = true;
							String parse = line.substring(15);
							String delims = "[ ]+";
							String[] tokens = parse.split(delims);

							variables.put(tokens[0], tokens[1]);
						}
						
						if (line.startsWith("SUPPRESS EOL"))
						{
							commandMode = true;
							suppressEOL = true;
						}

						if (line.startsWith("SUPPRESS UNICODE"))
						{
							commandMode = true;
							suppressUnicode = true;
						}

						if (line.startsWith("SUPPRESS ASCII ENCODE"))
						{
							commandMode = true;
							suppressASCIIEncode = true;
						}
						
						if (commandMode == false)
						{
							contents.append(line);

							if (suppressEOL == false)
							{
								contents.append(System.getProperty("line.separator"));
							}
						}
					}
				}
			} finally
			{
				input.close();
			}
		} catch (IOException ex)
		{
			if (Common.applicationMode.equals("SwingClient"))
			{
				JUtility.errorBeep();
				JOptionPane.showMessageDialog(null, "Unable to load label template " + aFile.getName(),
						"Printing Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		return contents.toString();
	}

	public String escapeUnicode(String input)
	{
		StringBuilder b = new StringBuilder(input.length());
		Formatter f = new Formatter(b);
		for (char c : input.toCharArray())
		{
			if (c < 128)
			{
				b.append(c);
			} else
			{
				b.append("_");
				f.format("%02X", (int) c);
			}
		}
		f.close();
		return b.toString();
	}
}
