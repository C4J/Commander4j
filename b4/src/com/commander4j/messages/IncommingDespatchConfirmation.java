package com.commander4j.messages;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : IncommingDespatchConfirmation.java
 * 
 * Package Name : com.commander4j.messages
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

import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBInterface;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBPallet;
import com.commander4j.util.JUtility;

/**
 * IncommingDespatchConfirmation class allows an external system to generate a despatch transaction.
 * 
 * @see com.commander4j.db.JDBDespatch JDBDespatch
 * @see com.commander4j.db.JDBJourney JDBJourney
 * @see com.commander4j.app.JInternalFrameJourneyAdmin JInternalFrameJourneyAdmin
 * @see com.commander4j.app.JInternalFrameJourneyProperties JInternalFrameJourneyProperties
 * @see com.commander4j.app.JInternalFrameDespatch JInternalFrameDespatch
 */
public class IncommingDespatchConfirmation
{

	private String hostID;
	private String sessionID;
	private String errorMessage;
	private String despatchNo;
	private String trailer;
	private String haulier;
	private String loadNo;
	private String fromLocation;
	private String toLocation;
	private String journeyRef;
	private String despatchDate;
	private String numberOfPallets;
	private Boolean repeat = true;
	private String key;
	private int seq;
	private String sscc;
	private int validate = 0;
	private int update = 1;

	public String getErrorMessage() {
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHostID() {
		return hostID;
	}

	public void setHostID(String hostID) {
		this.hostID = hostID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public IncommingDespatchConfirmation(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh) {
		Boolean result = true;

		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		JDBPallet pal = new JDBPallet(getHostID(), getSessionID());
		JDBLocation loc = new JDBLocation(getHostID(), getSessionID());
		JDBInterface inter = new JDBInterface(getHostID(), getSessionID());
		Integer palletCount = 0;

		inter.getInterfaceProperties("Despatch Confirmation", "Input");

		despatchNo = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/despatchNo").trim());
		trailer = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/trailer").trim());
		haulier = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/haulier").trim());
		loadNo = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/loadNo").trim());
		fromLocation = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/fromLocation").trim());
		toLocation = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/toLocation").trim());
		journeyRef = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/journeyRef").trim());
		despatchDate = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/despatchDate").trim());
		numberOfPallets = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/despatchConfirmation/numberOfPallets").trim());

		for (int run = 0; run <= 1; run++)
		{

			if (result == true)
			{

				if (desp.getDespatchProperties(despatchNo) == false)
				{

					if (run == validate)
					{
						// Check if Despatch already Exists and fail if it does.
						if (desp.getDespatchProperties(despatchNo) == true)
						{
							result = false;
							setErrorMessage("Despatch " + despatchNo + " already exists error.");
						}
					}
					else
					{
						// Attempt to create Despatch and fail if unsuccessful
						if (desp.create() == false)
						{
							result = false;
							setErrorMessage("Despatch " + despatchNo + " create error. " + desp.getErrorMessage());
						}
					}

					if (result == true)
					{

						if (run == validate)
						{
							// Check if FROM Location is valid
							if (loc.getLocationProperties(fromLocation) == false)
							{
								result = false;
								setErrorMessage("Invalid FROM Location : " + fromLocation);
							}
							// Check if TO Location is valid
							if (loc.getLocationProperties(toLocation) == false)
							{
								result = false;
								setErrorMessage("Invalid TO Location : " + toLocation);
							}
						}
						else
						{
							desp.setTrailer(trailer);
							desp.setHaulier(haulier);
							desp.setLoadNo(loadNo);
							desp.setDespatchDate(JUtility.getTimeStampFromISOString(despatchDate));
							desp.setTotalPallets(Integer.valueOf(numberOfPallets));
							desp.setLocationIDFrom(fromLocation);
							desp.setLocationIDTo(toLocation);
							desp.setJourneyRef(journeyRef);

							if (desp.update() == false)
							{
								result = false;
								setErrorMessage("Despatch " + despatchNo + " update error. " + desp.getErrorMessage());
							}
						}

						if (result == true)
						{

							repeat = true;
							seq = 1;

							do
							{
								key = "//message/messageData/despatchConfirmation/contents/pallet[" + String.valueOf(seq) + "]/SSCC";
								sscc = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath(key).trim());

								if (sscc.length() > 0)
								{
									if (pal.getPalletProperties(sscc) == true)
									{
										
										boolean sourceLocationOK = true;
										if (pal.getLocationID().equals(fromLocation) == false)
										{
											sourceLocationOK = false;
										}

										if (sourceLocationOK)
										{
											boolean alreadyAssigned = false;
											if (pal.getDespatchNo().equals("") == false)
											{
												JDBDespatch altDesp = new JDBDespatch(getHostID(), getSessionID());
												altDesp.getDespatchProperties(pal.getDespatchNo());
												if (altDesp.getStatus().equals("Unconfirmed"))
												{
													alreadyAssigned = true;
												}
											}
											
											if (!alreadyAssigned)
											{
												if (pal.isConfirmed())
												{
													if (run == validate)
													{
														if (loc.isPalletStatusValidforLocation(pal.getStatus()) == false)
														{
															result = false;
															setErrorMessage("SSCC " + pal.getSSCC() + " Pallet status " + pal.getStatus() + " invalid for location " + loc.getLocationID());
														}

														if (loc.isBatchStatusValidforLocation(pal.getMaterialBatchStatus()) == false)
														{
															result = false;
															setErrorMessage("SSCC " + pal.getSSCC() + " Batch status " + pal.getMaterialBatchStatus() + " invalid for location " + loc.getLocationID());
														}
													}
													else
													{
														if (desp.assignSSCC(sscc) == false)
														{
															result = false;
															setErrorMessage(desp.getErrorMessage());
														}
														else
														{
															palletCount++;
														}
													}
												}
												else
												{
													result = false;
													setErrorMessage("SSCC " + sscc + " has not been confirmed as made.");
												}
											}
											else
											{
												result = false;
												setErrorMessage("SSCC " + sscc + " already assigned to despatch " + pal.getDespatchNo());
											}
										}
										else
										{
											result = false;
											setErrorMessage("SSCC " + sscc + " is not in location " + fromLocation);
										}
									}
									else
									{
										result = false;
										setErrorMessage("SSCC " + sscc + " not found in database.");
									}
								}
								else
								{
									repeat = false;
								}
								seq++;

							}

							while (repeat);
						}
						if (run == update)
						{
							desp.setTotalPallets(palletCount);
							desp.confirm();
						}
					}
				}
			}
		}

		desp = null;

		return result;
	}
}
