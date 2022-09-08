/*
 * Created on 05-Apr-2005
 *
 */
package com.commander4j.html;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JMenuRFDespatchList.java
 * 
 * Package Name : com.commander4j.html
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

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.commander4j.db.JDBDespatch;
import com.commander4j.util.JUtility;

/**
 * @author David
 * 
 */
public class JMenuRFDespatchList
{

	final Logger logger = Logger.getLogger(JMenuRFDespatchList.class);
	private String hostID;
	private String sessionID;
	private String CRLF = "\n";
	private int checkedIndex;
	private int despatchCount=0;
	private int returnedPage=1;
	private int maxPages=1;
	private int itemsPerPage=8;

	public JMenuRFDespatchList(String host, String session)
	{
		setHostID(host);
		setSessionID(session);
	}

	public int getDespatchCount()
	{
		return despatchCount;
	}
	
	private void setSessionID(String session) {
		sessionID = session;
	}

	private void setHostID(String host) {
		hostID = host;
	}

	private String getHostID() {
		return hostID;
	}

	private String getSessionID() {
		return sessionID;
	}

	public int getCheckedIndex() {
		return checkedIndex;
	}

	public String getCheckedIndexString() {
		return String.valueOf(checkedIndex);
	}

	public int getReturnedPage()
	{
		return returnedPage;
	}
	
	private void setReturnedPage(int page)
	{
		returnedPage = page;
	}
	
	public int getMaxPages()
	{
		return maxPages;
	}
	
	private void setMaxPages(int page)
	{
		maxPages = page;
	}
	
	private LinkedList<JDBDespatch> paginateList(LinkedList<JDBDespatch> list,int currentPage,int itemsPerPage)
	{
		LinkedList<JDBDespatch> result = new LinkedList<JDBDespatch>();
		result.clear();

		Double temp = java.lang.Math.ceil(Double.valueOf((list.size()/Double.valueOf(itemsPerPage))));
		int pages = temp.intValue();
		
		if (pages==0)
		{
			pages = 1;
		}

		setMaxPages(pages);
		
		if (currentPage > pages)
		{
			currentPage = pages;
		}
		
		if (currentPage <= 0)
		{
			currentPage = 1;
		}
		
		setReturnedPage(currentPage);

		int firstItem = (currentPage - 1) * itemsPerPage;
		int lastItem = (currentPage * itemsPerPage);
		
		for (int x = firstItem; x < lastItem; x++)
		{
			if (x < list.size())
			{
				result.addLast(list.get(x));
			}
		}
				
		return result;
	}
	
	
	public String buildDespatchList(String status, String defaultItem, int currentPage,int maxitems) {
		String result = "";
		itemsPerPage = maxitems;
		result = buildDespatchList(status, defaultItem,currentPage);
		return result;
	}
	
	public String buildDespatchList(String status, String defaultItem, int currentPage) {

		String result = "";
		String selected = "";

		JDBDespatch desp = new JDBDespatch(getHostID(), getSessionID());
		LinkedList<JDBDespatch> list = new LinkedList<JDBDespatch>();

		checkedIndex = -1;
		despatchCount = 0;

		list = desp.browseDespatchData(status, 9999);
		
		list = paginateList(list,currentPage,itemsPerPage);
		
		if (list.size() > 0)
		{
			for (int x = 0; x < list.size(); x++)
			{
				if (defaultItem.equals("") == true)
				{
					defaultItem = list.get(x).getDespatchNo();
				}

				if (list.get(x).getDespatchNo().equals(defaultItem)==true)
				{
					selected = "checked=\"checked\"";
					checkedIndex = x;
				}
				else
				{
					selected = "";
				}

				despatchCount++;
				result = result + "<label><input type=\"radio\" name=\"despatchNo\" value=\"" + list.get(x).getDespatchNo() + "\" " + selected + "/>" + list.get(x).getDespatchNo() + " - " + JUtility.replaceNullStringwithBlank(list.get(x).getTrailer()) + "</label><br/>"+CRLF;
			}
		}
		else
		{
			checkedIndex = -1;
		}

		return result;

	}

}
