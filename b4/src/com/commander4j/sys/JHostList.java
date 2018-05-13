package com.commander4j.sys;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JHostList.java
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

import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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

	public void addHost(JHost hst)
	{
		if (isValidSite(hst.getSiteNumber()) == false)
		{
			hosts.put(hst.getSiteNumber(), hst);

			// numberOfHosts++;
		}
	}

	public void clear()
	{
		// numberOfHosts = 0;
		hosts.clear();
	}

	public int getCheckedIndex()
	{
		return checkedIndex;
	}

	public String getCheckedIndexString()
	{
		return String.valueOf(checkedIndex);
	}

	public String getHTMLmenu(String defaultItem)
	{
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

					data = "<label><input type=\"radio\" name=\"selectedHost\" value=\"" + hst.getSiteNumber() + "\" " + selected + "/>" + hst.getSiteDescription() + "</label><br/>" + CRLF;
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

	public void loadHosts(String filename)
	{
		addHosts(JXMLHost.loadHosts(filename, true));
	}

	public boolean checkUpdatedHosts()
	{
		boolean result = false;

		Double currentHostVersion = Double.valueOf(Common.hostVersion);
		logger.debug("Current Host File Version = " + String.valueOf(currentHostVersion));

		String hostUpdatePath = Common.hostUpdatePath;
		if (hostUpdatePath.equals("") == true)
		{
			logger.debug("No hosts file update location specified, checking application update url.");
			
			hostUpdatePath = Common.updateURL;
			if (hostUpdatePath.equals("") == true)
			{
				logger.debug("No application update location specified. Hosts update will not occur.");
			}
			else
			{
				hostUpdatePath = StringUtils.removeIgnoreCase(hostUpdatePath, "file:");
				hostUpdatePath = JUtility.formatPath(hostUpdatePath);
				hostUpdatePath = StringUtils.replaceIgnoreCase(hostUpdatePath, "updates.xml", "hosts.xml");
				logger.debug("Using application update url to check for updated hosts.");
			}
		}
		
		Common.hostUpdatePath = hostUpdatePath;

		// See if updatedHosts location specified
		if (hostUpdatePath.equals("") == false)
		{
			logger.debug("Updated Host Path = [" + hostUpdatePath + "]");
			if (Files.exists(Paths.get(hostUpdatePath)))
			{
				logger.debug("Updated Host Path = [" + hostUpdatePath + "] found.");

				Double updatedHostVersion = JXMLHost.checkHostVersion(hostUpdatePath);
				logger.debug("Updated Host File Version = " + String.valueOf(updatedHostVersion));

				if (updatedHostVersion > currentHostVersion)
				{
					logger.debug("Copying Updated Host File [" + hostUpdatePath + "]");
					try
					{
						File destDir = new File(System.getProperty("user.dir") + File.separator + "xml" + File.separator + "hosts");
						File srcFile = new File(hostUpdatePath);
						FileUtils.copyFileToDirectory(srcFile, destDir);
						result = true;
					}
					catch (Exception e)
					{
						logger.debug("Error Copying Updated Host File :" + e.getMessage());
					}
				}
				else
				{
					logger.debug("Current hosts file is up to date "+currentHostVersion.toString());
				}
			}
			else
			{
				logger.debug("Updated Host Path = " + hostUpdatePath + " not found.");
			}
		}

		return result;
	}

	public void loadHosts()
	{
		loadHosts("");
	}

	public int size()
	{
		return hosts.size();
	}

	public void addHosts(LinkedList<JHost> hsts)
	{
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

	public boolean isValidSite(String siteNumber)
	{
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

	public String getHostIDforUniqueId(String uniqueid)
	{
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

	public JHost getHost(String siteNumber)
	{
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

	public void disconnectAll()
	{
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

	public void disconnectSessionAllHosts(String sessionID)
	{
		logger.debug("disconnectSessionAllHosts size=" + String.valueOf(size()));
		if (size() > 0)
		{
			for (int x = 1; x <= size(); x++)
			{
				Common.hostList.getHost(String.valueOf(x)).disconnect(sessionID);
			}
		}
	}

	public LinkedList<JHost> getHosts()
	{
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
