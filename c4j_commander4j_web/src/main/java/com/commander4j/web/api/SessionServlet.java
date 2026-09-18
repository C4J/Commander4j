package com.commander4j.web.api;

/**
 * Session lifecycle - the index / hosts / login / changePassword screens of
 * c4j_web_react, one path per Process method:
 *
 *   GET    /api/session           current state (used by every page on load)
 *   PUT    /api/session/start     index Start / sessionTimeout Restart  -> next hosts
 *   PUT    /api/session/host      hosts Select   {selectedHost}          -> next login
 *   PUT    /api/session/login     login Submit   {username,password}     -> next menu | changePassword
 *   PUT    /api/session/password  changePassword {password,newPassword1,newPassword2} | {cancel:true}
 *   DELETE /api/session           login Cancel / index Quit              -> next hosts
 *
 * Session keys are the same names Process.saveData() used so the later
 * screens port unchanged: selectedHost, siteDescription, username, language,
 * screenAfterLogon, changePasswordCancelScreen, defaultPrinter, despatchListSize.
 * The password is NOT kept in the session (Process did; nothing read it back).
 */

import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBUser;
import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JPrint;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/session", "/api/session/*" })
public class SessionServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_HOSTS = "hosts";
	public static final String PAGE_LOGIN = "login";
	public static final String PAGE_MENU = "menu";
	public static final String PAGE_CHANGE_PASSWORD = "changePassword";

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
		if (path.equals("") || path.equals("/"))
		{
			if (method.equals("GET"))
			{
				return state(ws);
			}
			if (method.equals("DELETE"))
			{
				return logout(ws);
			}
		}
		if (method.equals("PUT"))
		{
			if (path.equals("/start"))
			{
				return start(ws);
			}
			if (path.equals("/host"))
			{
				return selectHost(ws, str(body, "selectedHost"), request);
			}
			if (path.equals("/login"))
			{
				return login(ws, str(body, "username"), str(body, "password"));
			}
			if (path.equals("/password"))
			{
				if (body.has("cancel") && body.get("cancel").getAsBoolean())
				{
					return changePasswordCancel(ws);
				}
				return changePassword(ws, str(body, "password"), str(body, "newPassword1"), str(body, "newPassword2"));
			}
		}
		return ApiResponse.fail("Unknown session action " + method + " " + path);
	}

	// ---- GET /api/session ------------------------------------------------------

	private ApiResponse state(WebSession ws)
	{
		return ApiResponse.ok()
				.put("hasHost", ws.hasHost())
				.put("selectedHost", ws.getSelectedHost())
				.put("siteDescription", ws.get("siteDescription"))
				.put("loggedOn", ws.isLoggedOn())
				.put("username", ws.get("username"))
				.put("language", ws.get("language"));
	}

	// ---- index Start / sessionTimeout Restart -------------------------------

	private ApiResponse start(WebSession ws)
	{
		ws.set("selectedHost", "");
		return ApiResponse.goTo(PAGE_HOSTS);
	}

	// ---- hosts Select  (Process.displayHosts) --------------------------------

	private ApiResponse selectHost(WebSession ws, String selectedHost, HttpServletRequest request)
	{
		if (selectedHost.isEmpty())
		{
			return ApiResponse.fail("").withNext(PAGE_HOSTS);
		}
		if (Common.hostList.isValidSite(selectedHost) == false)
		{
			return ApiResponse.fail("Unknown host " + selectedHost);
		}

		String sessionID = ws.getId();
		ws.set("selectedHost", selectedHost);
		ws.setLoggedOn(false);
		ws.remove("_barcode");

		// same as Process: a new host choice drops any previous connection + user
		WebSession.releaseResources(sessionID);
		WebSession.markSilentExceptions(sessionID);

		JHost host = Common.hostList.getHost(selectedHost);

		// hosts.xml can point a site at another web server instead of a database
		if ((host.getSiteURL().equals("http://") == false) && host.getDatabaseParameters().getjdbcDriver().equals("http"))
		{
			return ApiResponse.ok().put("redirect", host.getSiteURL());
		}

		ws.set("siteDescription", host.getSiteDescription());

		String driver = host.getDatabaseParameters().getjdbcDriver();
		String sqlPath = request.getServletContext().getRealPath("/xml/sql/sql." + driver + ".xml");
		String viewPath = request.getServletContext().getRealPath("/xml/view/view." + driver + ".xml");

		if (WebSession.connectHost(sessionID, selectedHost, sqlPath, viewPath))
		{
			return ApiResponse.goTo(PAGE_LOGIN);
		}

		return ApiResponse.fail("Unable to connect to database (host)").withNext(PAGE_HOSTS);
	}

	// ---- login Submit  (Process.logon + logonValidate) ----------------------

	private ApiResponse login(WebSession ws, String username, String password)
	{
		String sessionID = ws.getId();
		String hostID = ws.getSelectedHost();

		if (hostID.isEmpty() || Common.hostList.getHost(hostID).isConnected(sessionID) == false)
		{
			logger.debug("Logon - Host Connect Failure");
			return ApiResponse.fail("Not connected to host").withNext(PAGE_HOSTS);
		}

		JDBControl control = new JDBControl(hostID, sessionID);

		control.setSystemKey("PASSWORD EXPIRY");
		if (control.getProperties() == true)
		{
			Common.user_password_expiry_days = Integer.parseInt(control.getKeyValue());
		}

		control.setSystemKey("PASSWORD ATTEMPTS");
		if (control.getProperties() == true)
		{
			Common.user_max_password_attempts = Integer.parseInt(control.getKeyValue());
		}

		ws.set("despatchListSize", control.getKeyValueWithDefault("DESPATCH_LIST_SIZE", "8", "Mobile Device Despatch List Size"));

		JDBUser usr = new JDBUser(hostID, sessionID);
		usr.setUserId(username.toUpperCase());
		usr.setLoginPassword(password);

		if (usr.login() == false)
		{
			logger.debug("User " + usr.getUserId() + " logon error : " + usr.getErrorMessage());
			ws.setLoggedOn(false);
			ws.set("username", "");
			ws.set("screenAfterLogon", PAGE_LOGIN);
			return ApiResponse.fail(usr.getErrorMessage());
		}

		Common.userList.addUser(sessionID, usr);
		ws.setLoggedOn(true);
		logger.debug("User " + usr.getUserId() + " logged on.");

		ws.set("language", usr.getLanguage());
		ws.set("username", usr.getUserId());
		new JDBLanguage(hostID, sessionID).preLoad("%");
		ws.set("defaultPrinter", JPrint.getPreferredPrinterQueueName());

		if (usr.isPasswordExpired() || usr.isPasswordChangeRequired())
		{
			logger.debug("User " + usr.getUserId() + " password expired.");
			ws.set("changePasswordCancelScreen", PAGE_LOGIN);
			ws.set("screenAfterLogon", PAGE_CHANGE_PASSWORD);
			return ApiResponse.fail("Password expired.").withNext(PAGE_CHANGE_PASSWORD);
		}

		ws.set("screenAfterLogon", PAGE_MENU);
		MenuServlet.resetMenu(ws);
		return ApiResponse.goTo(PAGE_MENU);
	}

	// ---- login Cancel / index Quit -------------------------------------------

	private ApiResponse logout(WebSession ws)
	{
		ws.setLoggedOn(false);
		ws.set("username", "");
		ws.set("selectedHost", "");
		WebSession.releaseResources(ws.getId());
		WebSession.markSilentExceptions(ws.getId());
		return ApiResponse.goTo(PAGE_HOSTS);
	}

	// ---- changePassword  (Process.changeUserPassword) -----------------------

	private ApiResponse changePassword(WebSession ws, String currentPass, String new1, String new2)
	{
		String sessionID = ws.getId();
		String hostID = ws.getSelectedHost();

		if (ws.isLoggedOn() == false)
		{
			return ApiResponse.fail("").withNext(PAGE_SESSION_TIMEOUT);
		}

		JDBUser usr = new JDBUser(hostID, sessionID);
		usr.setUserId(ws.get("username").toUpperCase());

		// Core JDBUser.changePassword() validates only the NEW password; it never compares
		// the current one, so web_react's "Current" field is decorative. Found 2026-09-11
		// when a wrong current password changed a DEV user. Check it here (no core change).
		// isValidPassword() compares against the loaded row, so load it first as login()
		// does - and load BEFORE setting the passwords, because loading clears them.
		if (usr.isValidUserId() == false || usr.getUserProperties() == false)
		{
			return ApiResponse.fail("Invalid username or password");
		}

		usr.setLoginPassword(currentPass);
		if (usr.isValidPassword() == false)
		{
			return ApiResponse.fail("Invalid username or password");
		}

		usr.setPasswordNew(new1);
		usr.setPasswordVerify(new2);

		if (usr.changePassword())
		{
			ws.set("sscc", "");
			MenuServlet.resetMenu(ws);
			return ApiResponse.goTo(PAGE_MENU).withMessage("Password changed");
		}

		return ApiResponse.fail(usr.getErrorMessage());
	}

	private ApiResponse changePasswordCancel(WebSession ws)
	{
		String next = ws.get("changePasswordCancelScreen");
		if (next.equals(PAGE_LOGIN))
		{
			// expired-password flow abandoned: same as Process, drop the user
			ws.setLoggedOn(false);
			ws.set("username", "");
			Common.userList.removeUser(ws.getId());
			return ApiResponse.goTo(PAGE_LOGIN);
		}
		return ApiResponse.goTo(PAGE_MENU);
	}
}
