package com.commander4j.messages;

import java.sql.Timestamp;

import com.commander4j.db.JDBJourney;
import com.commander4j.util.JUtility;

public class IncommingJourney
{

	private String hostID;
	private String sessionID;
	private String errorMessage;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	private void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public IncommingJourney(String host, String session)
	{
		setSessionID(session);
		setHostID(host);
	}

	public Boolean processMessage(GenericMessageHeader gmh)
	{
		Boolean result = false;

		JDBJourney journey = new JDBJourney(getHostID(), getSessionID());
		String ref = "12345";
		String action = "";
		String timestampString = "";
		String location_to = "";
		Timestamp timeslot;
		int occur = 1;
		int created = 0;
		int deleted = 0;
		int updated = 0;

		while (ref.length() > 0)

		{
			ref = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/journeyDefinition[" + String.valueOf(occur) + "]/ref").trim());
			action = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/journeyDefinition[" + String.valueOf(occur) + "]/action").trim()).toLowerCase();
		    timestampString = gmh.getXMLDocument().findXPath("//message/messageData/journeyDefinition[" + String.valueOf(occur) + "]/timeslot").trim();			
			timeslot = JUtility.getTimeStampFromISOString(timestampString);	
			location_to = JUtility.replaceNullStringwithBlank(gmh.getXMLDocument().findXPath("//message/messageData/journeyDefinition[" + String.valueOf(occur) + "]/destination").trim());

			if (ref.length() > 0)
			{

				if (action.equals("create"))
				{
					if (journey.getJourneyRefProperties(ref) == false)
					{
						journey.setJourneyRef(ref);
						journey.setStatus("Unassigned");
						journey.setTimeslot(timeslot);
						journey.setLocationTo(location_to);
						result = journey.create();
						if (result)
						{
							created++;
						}
					} else
					{
						result = true;
					}
				}

				if (action.equals("delete"))
				{
					if (journey.getJourneyRefProperties(ref) == true)
					{
						if (journey.getStatus().equals("Unassigned"))
						{
							journey.setJourneyRef(ref);
							journey.delete();
							result = true;
							deleted++;
						}
					}
				}

				if (action.equals("update"))
				{
					if (journey.getJourneyRefProperties(ref) == true)
					{
						if (journey.getStatus().equals("Unassigned"))
						{
							journey.setJourneyRef(ref);
							journey.setStatus("Unassigned");
							journey.setTimeslot(timeslot);
							journey.setLocationTo(location_to);
							journey.update();
							result = true;
							updated++;
						}
					}
				}
				
				occur++;
			}
		}
		setErrorMessage(
				String.valueOf(created) + " Journey(s) created, " + String.valueOf(updated) + " Journey(s) updated, " +String.valueOf(deleted) + " Journey(s) deleted");

		journey = null;
		return result;

	}
}
