/*
 * Created on 27-May-2005
 *
 */
package com.commander4j.bar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBControl;
import com.commander4j.sys.Common;

/**
 * @author David
 * 
 * @version $Revision: 1.0 $
 */
public class JEANBarcode
{

	private String barcodeDebug;

	private String db_error_message;
	private LinkedList<String> parsedappIDs = new LinkedList<String>(); /*
																		 * Index
																		 * of
																		 * Parsed
																		 * App
																		 * ID's
																		 */
	private LinkedList<JEANdata> parsedEANdata = new LinkedList<JEANdata>(); /*
																			 * Results
																			 * of
																			 * Parse
																			 * Data
																			 */

	private LinkedList<String> refappIDs = new LinkedList<String>(); /*
																	 * Index of
																	 * App ID's
																	 * loaded
																	 * from
																	 * EAN_DEFS
																	 * table
																	 */
	private LinkedList<JEANdef> refEANdefs = new LinkedList<JEANdef>(); /*
																		 * Index
																		 * of
																		 * EAN
																		 * definitions
																		 * loaded
																		 * EAN_DEFS
																		 * table
																		 */
	final Logger logger = Logger.getLogger(JEANBarcode.class);
	private String hostID;
	private String sessionID;

	public JEANBarcode() {

	}

	public JEANBarcode(String host, String session) {
		setHostID(host);
		setSessionID(session);
		loadEANdefs();
	}

	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getSessionID() {
		return sessionID;
	}

	private String getHostID() {
		return hostID;
	}

	/**
	 * Method calcCheckdigit.
	 * 
	 * @param barcodeData
	 *            String
	 * @return String
	 */
	public String calcCheckdigit(String barcodeData) {
		String result = "";

		int barcodeSize = barcodeData.length();

		char barcodeCharacter;
		int barcodeASCIIvalue;
		int step1 = 0;
		int step2 = 0;
		int step3 = 0;
		int step4 = 0;
		int step5 = 0;

		for (int parseLocation = (barcodeSize - 1); parseLocation >= 0; parseLocation = parseLocation - 2) {
			barcodeCharacter = barcodeData.charAt(parseLocation);
			barcodeASCIIvalue = Integer.valueOf(String.valueOf(barcodeCharacter)).intValue();
			step1 = step1 + barcodeASCIIvalue;

			if (parseLocation > 0) {
				barcodeCharacter = barcodeData.charAt(parseLocation - 1);
				barcodeASCIIvalue = Integer.valueOf(String.valueOf(barcodeCharacter)).intValue();
				step3 = step3 + barcodeASCIIvalue;
			}
		}

		step2 = (step1 * 3);

		step4 = step2 + step3;

		while (((step4 + step5) % 10) != 0) {
			step5++;
		}

		result = String.valueOf(step5);

		return result;
	}

	/**
	 * Method displayDebug.
	 * 
	 * @return String
	 */
	public String displayDebug() {
		return barcodeDebug;
	}

	
	public LinkedList<String> generateNewSSCCs(int requiredQuantity)
	{
		LinkedList<String> result = new LinkedList<String>();
		result.clear();
		
		/**********************/
		
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		int SeqNumber = 0;
		
		String SSCCPrefix = "";
		String SSCCSequence = "1";
		String SSCC = "";

		int firstSSCC = 0;
		int lastSSCC = 0;

		if (ctrl.getProperties("SSCC PREFIX") == true) {
			SSCCPrefix = ctrl.getKeyValue();

			if (ctrl.lockRecord("SSCC SEQUENCE") == true) {
				if (ctrl.getProperties("SSCC SEQUENCE") == true) {
					
					//Get Next SSCC Sequence Number //
					SSCCSequence = ctrl.getKeyValue();
					SeqNumber = Integer.parseInt(SSCCSequence);
					firstSSCC = SeqNumber;
					
					//Add number of pallets required to Sequence//
					SeqNumber=SeqNumber+requiredQuantity;
					lastSSCC = firstSSCC + requiredQuantity-1;
					
					//Write back amended sequence to Control Table//
					ctrl.setKeyValue(String.valueOf(SeqNumber));

					if (ctrl.update()) {
						
						for (int x=firstSSCC;x<=lastSSCC;x++)
						{
							SSCCSequence=String.valueOf(x);
							
							SSCCSequence = com.commander4j.util.JUtility.padString(SSCCSequence, false, 18 - SSCCPrefix.length() - 1, "0");
							SSCC = SSCCPrefix + SSCCSequence;
							SSCC = SSCC + calcCheckdigit(SSCC);	
							result.add(SSCC);
						}
					}
				}
				else {
					logger.error(ctrl.getErrorMessage());
					setErrorMessage(ctrl.getErrorMessage());
				}
			}
			else {
				logger.error(ctrl.getErrorMessage());
				setErrorMessage(ctrl.getErrorMessage());
			}
		}
		
		
		/**********************/
		
		return result;
	}
	/**
	 * Method generateNewSSCC.
	 * 
	 * @return String
	 */
	public String generateNewSSCC() {
		String result = "error";
		JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
		String temp = "";
		int SeqNumber = 0;
		String SSCCPrefix = "";
		String SSCCSequence = "1";
		String SSCC = "";

		if (ctrl.getProperties("SSCC PREFIX") == true) {
			SSCCPrefix = ctrl.getKeyValue();

			if (ctrl.lockRecord("SSCC SEQUENCE") == true) {
				if (ctrl.getProperties("SSCC SEQUENCE") == true) {
					SSCCSequence = ctrl.getKeyValue();
					SeqNumber = Integer.parseInt(SSCCSequence);
					SeqNumber++;
					temp = String.valueOf(SeqNumber);
					ctrl.setKeyValue(temp);

					if (ctrl.update()) {
						SSCCSequence = com.commander4j.util.JUtility.padString(SSCCSequence, false, 18 - SSCCPrefix.length() - 1, "0");
						SSCC = SSCCPrefix + SSCCSequence;
						SSCC = SSCC + calcCheckdigit(SSCC);
					}
				}
				else {
					result = "";
					logger.error(ctrl.getErrorMessage());
					setErrorMessage(ctrl.getErrorMessage());
				}
			}
			else {
				result = "";
				logger.error(ctrl.getErrorMessage());
				setErrorMessage(ctrl.getErrorMessage());
			}

		}

		result = SSCC;

		logger.debug("SSCC :" + result);
		return result;
	}

	/**
	 * Method getDataTypeforAppID.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String getDataTypeforAppID(String key) {
		String result;
		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			result = ((JEANdef) refEANdefs.get(j)).getDataType();
		}
		else {
			result = "";
		}

		return result;
	}

	/**
	 * Method getDateforAppID.
	 * 
	 * @param key
	 *            String
	 * @return Date
	 */
	public Date getDateforAppID(String key) {
		final Logger logger = Logger.getLogger(JEANBarcode.class);

		Date result = null;

		if (isDataAvailableforAppID(key) == true) {
			if (isDataAvailableforAppID(key) == true) {
				int j = parsedappIDs.indexOf(key);

				String dateString = ((JEANdata) parsedEANdata.get(j)).getData();
				Date dateValue;

				DateFormat formatter = new SimpleDateFormat("yyMMdd HH.mm.ss");

				try {
					dateValue = (Date) formatter.parse(dateString + " 00.00.00");
				}
				catch (Exception e) {
					dateValue = null;
				}
				result = dateValue;
			}
		}
		logger.debug("getDateforAppID (" + key + ") :" + result);

		return result;
	}

	/**
	 * Method getDecimalIndicatorforAppID.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String getDecimalIndicatorforAppID(String key) {
		String result;
		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			result = ((JEANdef) refEANdefs.get(j)).getDecimalIndicator();
		}
		else {
			result = "";
		}

		return result;
	}

	/**
	 * Method getDescriptionforAppID.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String getDescriptionforAppID(String key) {
		String result;
		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			result = ((JEANdef) refEANdefs.get(j)).getDescription();
		}
		else {
			result = "";
		}

		return result;
	}

	/**
	 * Method getErrorMessage.
	 * 
	 * @return String
	 */
	public String getErrorMessage() {
		return db_error_message;
	}

	/**
	 * Method getMaxLengthforAppID.
	 * 
	 * @param key
	 *            String
	 * @return int
	 */
	public int getMaxLengthforAppID(String key) {
		int result;
		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			result = ((JEANdef) refEANdefs.get(j)).getMaxLength();
		}
		else {
			result = -1;
		}

		return result;
	}

	/**
	 * Method getNumberforAppID.
	 * 
	 * @param key
	 *            String
	 * @return double
	 */
	public double getNumberforAppID(String key) {
		final Logger logger = Logger.getLogger(JEANBarcode.class);
		double result = 0;

		if (isDataAvailableforAppID(key) == true) {
			String decInd = getDecimalIndicatorforAppID(key);

			/* String value for app id */
			String tempData = getStringforAppID(key);
			String decPlace = "";
			int count = 0;

			if (decInd.equals("Y")) {
				/* Decimal place as first char of data */
				decPlace = tempData.substring(0, 1);
				tempData = tempData.substring(1, tempData.length() - 1);
			}
			else {
				/* No Decimal places use all of data */
				decPlace = "0";
			}

			result = Double.valueOf(tempData).doubleValue();
			count = Integer.valueOf(decPlace).intValue();

			for (int l = 0; l < count; l++) {
				result = (result / (double) 10);
			}
		}
		logger.debug("getNumberforAppID (" + key + ") :" + result);
		return result;
	}

	/**
	 * Method getNumberofAppIDsParsed.
	 * 
	 * @return int
	 */
	public int getNumberofAppIDsParsed() {
		return parsedappIDs.size();
	}

	/**
	 * Method getStringforAppID.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String getStringforAppID(String key) {
		final Logger logger = Logger.getLogger(JEANBarcode.class);

		String result = "";

		if (isDataAvailableforAppID(key) == true) {
			int j = parsedappIDs.indexOf(key);

			result = ((JEANdata) parsedEANdata.get(j)).getData();
		}
		logger.debug("getStringforAppID (" + key + ") :" + result);
		return result;
	}

	/**
	 * Method isCheckdigitRequired.
	 * 
	 * @param key
	 *            String
	 * @return boolean
	 */
	public boolean isCheckdigitRequired(String key) {
		boolean result = false;
		String cd;

		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			cd = ((JEANdef) refEANdefs.get(j)).getCheckDigit();
			if (cd.equals("Y") == true) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * Method isDataAvailableforAppID.
	 * 
	 * @param key
	 *            String
	 * @return boolean
	 */
	public boolean isDataAvailableforAppID(String key) {
		boolean result;
		int j = parsedappIDs.indexOf(key);

		if (j >= 0) {
			result = true;
		}
		else {
			result = false;
		}
		return result;
	}

	/**
	 * Method isValidAppID.
	 * 
	 * @param key
	 *            String
	 * @return boolean
	 */
	public boolean isValidAppID(String key) {
		boolean result;
		int j = refappIDs.indexOf(key);

		if (j >= 0) {
			result = true;
		}
		else {
			result = false;
		}

		return result;
	}

	private void loadEANdefs() {

		PreparedStatement stmt;
		ResultSet rs;
		// boolean result = false;
		setErrorMessage("");
		final Logger logger = Logger.getLogger(JEANBarcode.class);

		logger.debug("loadEANdefs");
		try {
			stmt = Common.hostList.getHost(getHostID()).getConnection(getSessionID()).prepareStatement("select * from bar_ean_defs order by application_id");
			rs = stmt.executeQuery();
			while (rs.next()) {
				JEANdef jeanDef = new JEANdef(rs.getString("application_id"), rs.getString("description"), rs.getString("data_type"), rs.getString("decimal_indicator"), rs.getInt("max_data_length"), rs.getString("fixed_variable"), rs
						.getString("check_digit"));
				refappIDs.addLast(jeanDef.getApplicationID());
				refEANdefs.addLast(jeanDef);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}

	}

	/**
	 * Method parseBarcodeData.
	 * 
	 * @param barcodeData
	 *            String
	 * @return boolean
	 */
	public boolean parseBarcodeData(String barcodeData) {

		final Logger logger = Logger.getLogger(JEANBarcode.class);

		logger.debug("parseBarcodeData :" + barcodeData);

		boolean result = true;
		boolean endofdata = false;

		char barcodeCharacter;
		int barcodeASCIIvalue;

		int appMode = 1;
		int dataMode = 2;
		int delimiterVal = 29;
		char delimiterChar = (char) delimiterVal;

		int parseMode = appMode;

		parsedappIDs.clear();
		parsedEANdata.clear();
		setErrorMessage("");

		// JEANdata EANRecord = new JEANdata();
		String applicationID = "";
		String data = "";

		int barcodeSize = barcodeData.length();

		barcodeData = barcodeData.replace('^', delimiterChar);

		barcodeDebug = "";

		if (barcodeSize > 2) {
			for (int parseLocation = 0; parseLocation < barcodeSize; parseLocation++) {

				barcodeCharacter = barcodeData.charAt(parseLocation);
				barcodeASCIIvalue = (int) barcodeCharacter;

				endofdata = false;

				if (barcodeASCIIvalue == delimiterVal) {
					endofdata = true;
					barcodeDebug = barcodeDebug + data + "^";
				}

				if (parseLocation == (barcodeSize - 1)) {
					endofdata = false;
				}

				if (endofdata == false) {
					if (parseMode == appMode) {
						/* Parsing application id */

						applicationID = applicationID + barcodeCharacter;

						if (applicationID.length() < 5) {
							if (isValidAppID(applicationID) == true) {
								data = "";
								parseMode = dataMode;
								barcodeDebug = barcodeDebug + "(" + applicationID + ")";
							}
						}
						else {
							result = false;
							setErrorMessage("No application ID found in barcode [" + applicationID + "]");
							break;
						}
					}

					else

					{
						data = data + barcodeCharacter;
						/* Parsing application data */
						if ((data.length() == getMaxLengthforAppID(applicationID)) || (parseLocation == (barcodeSize - 1))) {
							parseMode = appMode;
							endofdata = true;
							barcodeDebug = barcodeDebug + data;
						}
					}
				}

				if (endofdata == true) {

					/* Add record to results list */
					if (applicationID.length() >= 2) {
						if (data.length() > 0) {
							parsedappIDs.addLast(applicationID);

							JEANdata EANRecord = new JEANdata(applicationID, data);
							parsedEANdata.addLast(EANRecord);

							logger.debug("parseBarcodeData :(" + EANRecord.getApplicationID() + ")" + EANRecord.getData());
							if (isCheckdigitRequired(applicationID) == true) {
								if (validateCheckdigit(data) == false) {
									result = false;
									setErrorMessage("Invalid check digit (" + applicationID + ")" + data);
									break;
								}
							}
							applicationID = "";
							data = "";
							parseMode = appMode;
							/* Record valid */
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Method setErrorMessage.
	 * 
	 * @param ErrorMsg
	 *            String
	 */
	private void setErrorMessage(String ErrorMsg) {
		db_error_message = ErrorMsg;
	}

	/**
	 * Method validateCheckdigit.
	 * 
	 * @param barcodeData
	 *            String
	 * @return boolean
	 */
	public boolean validateCheckdigit(String barcodeData) {
		boolean result = false;

		int barcodeSize = barcodeData.length();

		if (barcodeSize >= 2) {
			String base = barcodeData.substring(0, barcodeSize - 1);
			String actual = barcodeData.substring(barcodeSize - 1, barcodeSize);
			String correct = calcCheckdigit(base);
			if (actual.equals(correct) == true) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * Method isValidSSCCformat.
	 * 
	 * @param SSCC
	 *            String
	 * @return boolean
	 */
	public boolean isValidSSCCformat(String SSCC) {
		boolean result = false;

		if (SSCC == null) {
			SSCC = "";
		}

		if (SSCC.equals("") == true) {
			if (Common.applicationMode.equals("SwingClient")) {
				setErrorMessage("Blank SSCC, type AUTO for next number.");
			}
			else {
				setErrorMessage("No SSCC Entered.");
			}
		}
		else {
			if (SSCC.length() == 18) {

				if (validateCheckdigit(SSCC)) {
					result = true;
				}
				else {
					setErrorMessage("Invalid Check Digit");
				}
			}
			else {
				setErrorMessage("SSCC wrong size.");
			}
		}

		return result;
	}

}
