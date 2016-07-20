package com.commander4j.sys;

import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLHost;

public class JHostList
{
	private Hashtable<String, JHost> hosts = new Hashtable<String, JHost>();
	private int checkedIndex;
	private String CRLF = "\n";

	// private int numberOfHosts = 0;
	private final Logger logger = Logger.getLogger(JHostList.class);

	public void addHost(JHost hst) {
		if (isValidSite(hst.getSiteNumber()) == false)
		{
			hosts.put(hst.getSiteNumber(), hst);

			// numberOfHosts++;
		}
	}

	public void clear() {
		// numberOfHosts = 0;
		hosts.clear();
	}

	public int getCheckedIndex() {
		return checkedIndex;
	}

	public String getCheckedIndexString() {
		return String.valueOf(checkedIndex);
	}

	public String getHTMLmenu(String defaultItem) {
		String selected = "";
		String data = "";
		String result = "";
		LinkedList<JHost> temp = new LinkedList<JHost>();
		JHost hst = new JHost();
		temp = Common.hostList.getHosts();
		defaultItem = JUtility.replaceNullStringwithBlank(defaultItem);

		checkedIndex = 0;

		if (temp.size() > 0)
		{

			for (int j = 0; j < temp.size(); j++)
			{
				hst = (JHost) temp.get(j);

				logger.debug(hst.getSiteNumber() + " " + hst.getSiteDescription());

				if (hst.getEnabled().equals("Y"))
				{
					if (defaultItem.equals("") == true)
					{
						defaultItem = Common.hostList.getHost(String.valueOf(j + 1)).getSiteNumber();
					}

					if (Common.hostList.getHost(String.valueOf(j + 1)).getSiteNumber().equals(defaultItem))
					{
						selected = "checked=\"checked\"";
						checkedIndex = j;
					}
					else
					{
						selected = "";
					}

					data = "<label><input type=\"radio\" name=\"selectedHost\" value=\"" + hst.getSiteNumber() + "\" " + selected + "/>" + hst.getSiteDescription() + "</label><br/>"+CRLF;
					result = result + data;
				}
			}
		}
		else
		{
			checkedIndex = -1;
		}

		return result;
	}

	public void loadHosts(String filename) {
		addHosts(JXMLHost.loadHosts(filename, true));
	}

	public void loadHosts() {
		loadHosts("");
	}

	public int size() {
		return hosts.size();
	}

	public void addHosts(LinkedList<JHost> hsts) {
		int s = hsts.size();
		hosts.clear();

		if (s > 0)
		{
			for (int x = 1; x <= s; x++)
			{
				hosts.put(hsts.get(x - 1).getSiteNumber(), hsts.get(x - 1));

				// numberOfHosts++;
			}
		}
	}

	public boolean isValidSite(String siteNumber) {
		boolean result = false;

		if (hosts.size() > 0)
		{
			if (hosts.containsKey(siteNumber) == true)
			{
				result = true;
			}
		}

		return result;
	}

	public String getHostIDforUniqueId(String uniqueid) {
		String result = "";

		if (size() > 0)
		{
			for (int x = 1; x <= size(); x++)
			{
				if (Common.hostList.getHost(String.valueOf(x)).getUniqueID().equals(uniqueid))
				{
					result = String.valueOf(x);

					break;
				}
			}
		}

		return result;
	}

	public JHost getHost(String siteNumber) {
		JHost hst = new JHost();

		if (isValidSite(siteNumber))
		{
			hst = hosts.get(siteNumber);
		}
		else
		{
			logger.error("JHostList error during getHost - cannot find Host ID: " + siteNumber);
		}

		return hst;
	}

	public void disconnectAll() {
		LinkedList<JHost> hl = new LinkedList<JHost>();
		hl = getHosts();

		if (hl.size() > 0)
		{
			for (int x = 0; x < hl.size(); x++)
			{
				String siteID = hl.get(x).getSiteNumber();

				if (Common.hostList.getHost(siteID).getConnectionCount() > 0)
				{
					Common.hostList.getHost(siteID).disconnectAll();
				}
			}
		}
	}

	public void disconnectSessionAllHosts(String sessionID) {
		logger.debug("disconnectSessionAllHosts size="+String.valueOf(size()));
		if (size() > 0)
		{
			for (int x = 1; x <= size(); x++)
			{
				Common.hostList.getHost(String.valueOf(x)).disconnect(sessionID);
			}
		}
	}

	public LinkedList<JHost> getHosts() {
		final LinkedList<JHost> temphostlist = new LinkedList<JHost>();
		temphostlist.clear();

		int s = hosts.size();

		if (s > 0)
		{
			for (int x = 1; x <= s; x++)
			{
				temphostlist.addLast(hosts.get(String.valueOf(x)));
			}
		}

		return temphostlist;
	}
}
