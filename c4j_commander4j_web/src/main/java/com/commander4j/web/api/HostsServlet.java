package com.commander4j.web.api;

/**
 * GET /api/hosts - the enabled hosts from hosts.xml, in the order the JSP
 * radio list showed them (JHostList.getHTMLmenu), plus which one is selected
 * (the session's current host, else the first enabled one).
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/hosts" })
public class HostsServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean requiresHost()
	{
		return false;
	}

	@Override
	protected boolean requiresLogin()
	{
		return false;
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET") == false)
		{
			return ApiResponse.fail("Method not supported");
		}

		String selected = ws.getSelectedHost();
		List<Map<String, Object>> hosts = new ArrayList<Map<String, Object>>();
		LinkedList<JHost> all = Common.hostList.getHosts();

		for (JHost hst : all)
		{
			if (hst.getEnabled().equals("Y"))
			{
				if (selected.isEmpty())
				{
					selected = hst.getSiteNumber();
				}
				Map<String, Object> h = new LinkedHashMap<String, Object>();
				h.put("siteNumber", hst.getSiteNumber());
				h.put("description", hst.getSiteDescription());
				h.put("selected", hst.getSiteNumber().equals(selected));
				hosts.add(h);
			}
		}

		return ApiResponse.ok().put("hosts", hosts).put("selected", selected);
	}
}
