package com.commander4j.web.api;

/**
 * GET /api/ping - the only endpoint that needs neither host nor login.
 * Backs the welcome page (index.jsp in web_react): program version + server name.
 */

import com.commander4j.app.JVersion;
import com.commander4j.bean.JServerName;
import com.commander4j.sys.Common;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/ping" })
public class PingServlet extends JsonServlet
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
		return ApiResponse.ok()
				.put("version", JVersion.getProgramVersion())
				.put("schema", JVersion.getSchemaVersion())
				.put("server", JServerName.getServername())
				.put("hosts", Common.hostList.size())
				.put("hasHost", ws.hasHost())
				.put("loggedOn", ws.isLoggedOn());
	}
}
