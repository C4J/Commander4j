package com.commander4j.web.session;

/**
 * The ONLY place this web app touches per-session state.
 *
 * Per-screen values (selectedHost, username, language, sscc, ...) are stored as
 * HttpSession attributes, which Tomcat keeps in a thread-safe map. Nothing goes
 * into Common.sd except the single "silentExceptions" flag that core's
 * JHost.connect() reads, and that write plus every JHost.connect() happen under
 * SD_LOCK because Common.sd is two unsynchronised parallel LinkedLists shared by
 * every session in the JVM.
 *
 * Locking model chosen 2026-09-11: per-session. See lock() below.
 */

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.commander4j.sys.Common;
import com.commander4j.util.JUtility;

import jakarta.servlet.http.HttpSession;

public class WebSession
{
	/** Guards Common.sd and JHost.connect(). Held only for those short calls. */
	public static final Object SD_LOCK = new Object();

	/** One lock per HttpSession id so one device cannot double-submit, while different devices run in parallel. */
	private static final ConcurrentHashMap<String, ReentrantLock> LOCKS = new ConcurrentHashMap<String, ReentrantLock>();

	private final HttpSession session;

	public WebSession(HttpSession session)
	{
		this.session = session;
	}

	public HttpSession getHttpSession()
	{
		return session;
	}

	public String getId()
	{
		return session.getId();
	}

	public static ReentrantLock lock(String sessionID)
	{
		return LOCKS.computeIfAbsent(sessionID, _ -> new ReentrantLock());
	}

	// ---- per-session values -------------------------------------------------

	public String get(String key)
	{
		Object value = session.getAttribute(key);
		return JUtility.replaceNullObjectwithBlank(value == null ? "" : value.toString());
	}

	/** Same contract as Process.saveData(): blank values are stored only when allowBlank is true. */
	public void set(String key, String value, boolean allowBlank)
	{
		String v = JUtility.replaceNullObjectwithBlank(value);
		if (v.isEmpty() && allowBlank == false)
		{
			return;
		}
		session.setAttribute(key, v);
	}

	public void set(String key, String value)
	{
		set(key, value, true);
	}

	public void remove(String key)
	{
		session.removeAttribute(key);
	}

	public Object getObject(String key)
	{
		return session.getAttribute(key);
	}

	public void setObject(String key, Object value)
	{
		session.setAttribute(key, value);
	}

	/**
	 * One JEANBarcode per session, created on first use. Process kept ONE for the
	 * whole app (created on the first host connect, never for a later host);
	 * per-session is correct because the parser keeps state between calls and
	 * loads its AI definitions from the session's host.
	 */
	public com.commander4j.bar.JEANBarcode barcode()
	{
		Object b = session.getAttribute("_barcode");
		if (b == null)
		{
			b = new com.commander4j.bar.JEANBarcode(getSelectedHost(), getId());
			session.setAttribute("_barcode", b);
		}
		return (com.commander4j.bar.JEANBarcode) b;
	}

	public String getSelectedHost()
	{
		return get("selectedHost");
	}

	public boolean hasHost()
	{
		return getSelectedHost().isEmpty() == false;
	}

	/**
	 * Tracked as a session attribute because JUserList.getUser() never returns
	 * null - it hands back a blank JDBUser (and logs an error) for an unknown id.
	 */
	public boolean isLoggedOn()
	{
		return hasHost() && get("loggedOn").equals("Y");
	}

	public void setLoggedOn(boolean on)
	{
		set("loggedOn", on ? "Y" : "");
	}

	// ---- Common.sd bridge --------------------------------------------------------

	public static void markSilentExceptions(String sessionID)
	{
		synchronized (SD_LOCK)
		{
			Common.sd.setData(sessionID, "silentExceptions", "Yes", true);
		}
	}

	/** Connect this session to a host; serialised on SD_LOCK because JHost.connect reads Common.sd. */
	public static boolean connectHost(String sessionID, String hostID, String sqlPath, String viewPath)
	{
		synchronized (SD_LOCK)
		{
			return Common.hostList.getHost(hostID).connect(sessionID, hostID, sqlPath, viewPath);
		}
	}

	/** Same as Process.releaseSessionResources(). */
	public static void releaseResources(String sessionID)
	{
		// note: the per-session "_barcode" parser is dropped by SessionServlet.selectHost
		Common.hostList.disconnectSessionAllHosts(sessionID);
		Common.userList.removeUser(sessionID);
		synchronized (SD_LOCK)
		{
			Common.sd.deleteData(sessionID, "silentExceptions");
		}
	}

	/**
	 * Only from sessionDestroyed. Never from a request handler: the handler is
	 * holding this lock, and removing it would let a concurrent request on the
	 * same session mint a fresh one and run in parallel.
	 */
	public static void releaseLock(String sessionID)
	{
		LOCKS.remove(sessionID);
	}
}
