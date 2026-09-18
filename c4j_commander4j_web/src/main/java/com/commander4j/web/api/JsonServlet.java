package com.commander4j.web.api;

/**
 * Base class for every /api/* endpoint.
 *
 *  - Parses the JSON request body (if any) into a JsonObject.
 *  - Runs the handler under the caller's per-session lock (WebSession.lock).
 *  - Applies the same session guards Process.doPost() applies: a brand-new
 *    session, or no host selected, sends the client to sessionTimeout unless
 *    the endpoint opts out with requiresHost() == false.
 *  - Endpoints that belong to one menu module return its id from moduleId();
 *    the request is refused unless JDBUser.isModuleAllowed() says so. Hiding
 *    a button on the client is not security.
 *  - Serialises the ApiResponse with Gson and sends the same no-store headers
 *    Process sets.
 *
 * Subclasses override handle() and, optionally, requiresHost() / requiresLogin().
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.web.session.WebSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class JsonServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected final Logger logger = org.apache.logging.log4j.LogManager.getLogger(getClass());

	private static final Gson GSON = new GsonBuilder().serializeNulls().create();

	public static final String PAGE_SESSION_TIMEOUT = "sessionTimeout";

	/** Endpoint needs a selected, connected host (everything after the hosts screen). */
	protected boolean requiresHost()
	{
		return true;
	}

	/** Endpoint needs a logged-on user (everything after the login screen). */
	protected boolean requiresLogin()
	{
		return true;
	}

	/** SYS_MODULES id this endpoint belongs to, or null when it is not module-gated. */
	protected String moduleId()
	{
		return null;
	}

	/**
	 * @param method GET / POST / PUT / DELETE
	 * @param path   the part after the servlet mapping ("" when none), e.g. "/1234"
	 * @param body   JSON body, or an empty object for GET / no body
	 */
	protected abstract ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request) throws Exception;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.addHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		WebSession ws = new WebSession(session);
		ApiResponse result;

		if (session.isNew() && (requiresHost() || requiresLogin()))
		{
			result = ApiResponse.fail("").withNext(PAGE_SESSION_TIMEOUT);
		}
		else if (requiresHost() && ws.hasHost() == false)
		{
			result = ApiResponse.fail("").withNext(PAGE_SESSION_TIMEOUT);
		}
		else if (requiresLogin() && ws.isLoggedOn() == false)
		{
			result = ApiResponse.fail("").withNext(PAGE_SESSION_TIMEOUT);
		}
		else if (moduleId() != null && Common.userList.getUser(session.getId()).isModuleAllowed(moduleId()) == false)
		{
			logger.warn("Module " + moduleId() + " refused for session " + session.getId());
			result = ApiResponse.fail("Not authorised").withNext(SessionServlet.PAGE_MENU);
		}
		else
		{
			String method = request.getMethod().toUpperCase();
			String path = request.getPathInfo() == null ? "" : request.getPathInfo();
			JsonObject body = readBody(request);

			ReentrantLock lock = WebSession.lock(session.getId());
			lock.lock();
			try
			{
				logger.debug(method + " " + request.getRequestURI() + " [" + session.getId() + "]");
				result = handle(method, path, body, ws, request);
				if (result == null)
				{
					result = ApiResponse.fail("No response from " + getClass().getSimpleName());
				}
			}
			catch (Exception ex)
			{
				logger.error("Unhandled error in " + getClass().getSimpleName(), ex);
				result = ApiResponse.fail("Server error: " + ex.getMessage());
			}
			finally
			{
				lock.unlock();
			}
		}

		PrintWriter out = response.getWriter();
		out.print(GSON.toJson(result));
		out.flush();
	}

	private JsonObject readBody(HttpServletRequest request) throws IOException
	{
		String ct = request.getContentType();
		if (ct == null || ct.toLowerCase().contains("json") == false)
		{
			return new JsonObject();
		}
		StringBuilder sb = new StringBuilder();
		String line;
		try (java.io.BufferedReader reader = request.getReader())
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
		}
		if (sb.length() == 0)
		{
			return new JsonObject();
		}
		try
		{
			return JsonParser.parseString(sb.toString()).getAsJsonObject();
		}
		catch (Exception ex)
		{
			logger.warn("Bad JSON body: " + ex.getMessage());
			return new JsonObject();
		}
	}

	/** For servlets that serve several modules: refuse unless the user may use this one. */
	protected ApiResponse refuseUnlessAllowed(WebSession ws, String moduleId)
	{
		if (Common.userList.getUser(ws.getId()).isModuleAllowed(moduleId) == false)
		{
			logger.warn("Module " + moduleId + " refused for session " + ws.getId());
			return ApiResponse.fail("Not authorised").withNext(SessionServlet.PAGE_MENU);
		}
		return null;
	}

	/** The logged-on user's id from the server-side user list - never from the request body. */
	protected static String userId(WebSession ws)
	{
		return Common.userList.getUser(ws.getId()).getUserId();
	}

	/**
	 * SSCC from a scan or a typed value, as both web apps accepted it: web_Issue took an 18-digit
	 * SSCC or a 20-character "00"+SSCC scan (formatSSCC); web_react ran the GS1 parser. Returns the
	 * 18-digit SSCC, or null with the reason in err[0].
	 */
	protected static String normaliseSSCC(WebSession ws, String scanned, String[] err)
	{
		String v = scanned == null ? "" : scanned.trim();
		if (v.isEmpty())
		{
			err[0] = "SSCC required.";
			return null;
		}
		if (v.length() == 20 && v.startsWith("00") && v.substring(2).matches("\\d{18}"))
		{
			return v.substring(2);
		}
		if (v.matches("\\d{18}"))
		{
			return v;
		}
		com.commander4j.bar.JEANBarcode bcode = ws.barcode();
		if (bcode.parseBarcodeData(v) == false)
		{
			err[0] = bcode.getErrorMessage().isEmpty() ? "Invalid barcode." : bcode.getErrorMessage();
			return null;
		}
		String sscc = bcode.getStringforAppID("00");
		if (bcode.isValidSSCCformat(sscc) == false)
		{
			err[0] = "Invalid SSCC format.";
			return null;
		}
		return sscc;
	}

	/**
	 * SYS_LANGUAGE text for a key, or "" when the site has no row for it. Core JDBLanguage.get() answers a miss with
	 * hostID + " " + key (that is what the desktop shows for an untranslated module, e.g. "3 mod_FRM_QM_PANEL_SCORES");
	 * the web pages keep their English fallback instead, so that pattern is mapped to blank here (2026-09-12).
	 */
	protected static String langText(com.commander4j.db.JDBLanguage lang, String hostID, String key, String language)
	{
		String t = lang.get(key, language);
		if (t == null || t.trim().isEmpty() || t.equals(hostID + " " + key))
		{
			return "";
		}
		return t;
	}

	/**
	 * A module's description the way the desktop derives it: SYS_MODULES.RESOURCE_KEY looked up in SYS_LANGUAGE for the
	 * user's language (the column is a legacy misnomer - it holds the language key, not a language id). When the key is
	 * blank or has no translation the module's HINT is the fallback, then the module id itself (Dave, 2026-09-12).
	 */
	protected static String moduleDescription(com.commander4j.db.JDBModule mod, com.commander4j.db.JDBLanguage lang, String hostID, String sessionID, String moduleID, String language)
	{
		String key = mod.getResourceKey() == null ? "" : mod.getResourceKey().trim();
		String text = key.isEmpty() ? "" : langText(lang, hostID, key, language);
		if (text.isEmpty())
		{
			text = moduleHint(hostID, sessionID, moduleID);
		}
		return text.isEmpty() ? moduleID : text;
	}

	/**
	 * SYS_MODULES.HINT for a module. Core JDBModule has no getter for the column (its getHint() returns the language
	 * description), so the row is read here with core's own "JDBModule.getModuleProperties" statement. Blank on any error.
	 */
	protected static String moduleHint(String hostID, String sessionID, String moduleID)
	{
		try (java.sql.PreparedStatement stmt = Common.hostList.getHost(hostID).getConnection(sessionID).prepareStatement(Common.hostList.getHost(hostID).getSqlstatements().getSQL("JDBModule.getModuleProperties")))
		{
			stmt.setString(1, moduleID);
			try (java.sql.ResultSet rs = stmt.executeQuery())
			{
				if (rs.next())
				{
					String hint = rs.getString("hint");
					return hint == null ? "" : hint.trim();
				}
			}
		}
		catch (Exception ex)
		{
			org.apache.logging.log4j.LogManager.getLogger(JsonServlet.class).warn("moduleHint " + moduleID + ": " + ex.getMessage());
		}
		return "";
	}

	/** Body field as trimmed string, blank when absent. */
	protected static String str(JsonObject body, String key)
	{
		if (body == null || body.has(key) == false || body.get(key).isJsonNull())
		{
			return "";
		}
		return body.get(key).getAsString().trim();
	}
}
