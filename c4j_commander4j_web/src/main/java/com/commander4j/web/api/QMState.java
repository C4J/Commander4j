package com.commander4j.web.api;

/**
 * Shared constants + session state for the c4j_web_WS (panel grading) screens, step 8 (2026-09-12).
 *
 * Modules: the three options are modules of their own (schema 216) and, since schema 217, children of the MENU_QM
 * row on the nested RF menu (SYS_RF_MENU.MENU_ID = MENU_QM). MenuServlet lists them and opens their first page;
 * the FRM_QM_PANEL stand-in and its fixed qmMenu page went with 217.
 *
 * State that web_WS kept in the browser's sessionStorage (selectedPanel, selectedTray, selectedTraySeq,
 * selectedSample, selectedUser/Username, selectedResult, userStatus) lives in the HttpSession here and is
 * cleared whenever a QM menu option is chosen, as the old menu.html cleared it.
 */

import com.commander4j.web.session.WebSession;

public final class QMState
{
	public static final String MODULE_SCORES = "FRM_QM_PANEL_SCORES";   // web_WS "Panel Scores"  -> resultUserSelect...
	public static final String MODULE_SETUP = "FRM_QM_PANEL_SETUP";     // web_WS "Setup Panel"   -> panels...
	public static final String MODULE_ADMIN = "FRM_QM_PANEL_ADMIN";     // web_WS "Admin"         -> users (the one-button admin.html is dropped)

	public static final String PAGE_SCORE_USER = "qmScoreUser";
	public static final String PAGE_SCORE_PANEL = "qmScorePanel";
	public static final String PAGE_SCORE_TRAY = "qmScoreTray";
	public static final String PAGE_SCORE_SAMPLE = "qmScoreSample";
	public static final String PAGE_PANELS = "qmPanels";
	public static final String PAGE_PANEL_EDIT = "qmPanelEdit";
	public static final String PAGE_TRAYS = "qmTrays";
	public static final String PAGE_TRAY_EDIT = "qmTrayEdit";
	public static final String PAGE_SAMPLES = "qmSamples";
	public static final String PAGE_USERS = "qmUsers";
	public static final String PAGE_USER_EDIT = "qmUserEdit";

	/** The APP_QM_SELECTLIST list that holds the panel decisions (web_WS resultSampleSelect.html). */
	public static final String DECISION_LIST_ID = "ZWSIPANE";
	/** The test id every panel score is written under (web_WS GetSelected). */
	public static final String TEST_ID = "PANEL";
	/** web_WS panels.html listed the newest 24 panels. */
	public static final long PANEL_LIST_LIMIT = 24;

	// session keys
	public static final String K_OPTION = "qmOption";
	public static final String K_PANEL = "qmPanel";
	public static final String K_PANEL_PLANT = "qmPanelPlant";
	public static final String K_TRAY = "qmTray";
	public static final String K_TRAY_SEQ = "qmTraySeq";
	public static final String K_SAMPLE = "qmSample";
	public static final String K_PANELIST = "qmPanelist";
	public static final String K_PANELIST_NAME = "qmPanelistName";
	public static final String K_RESULT = "qmResult";
	public static final String K_USER_FILTER = "qmUserFilter";
	public static final String K_USER = "qmUser";
	public static final String K_USER_NEW = "qmUserNew";

	static final String[] STATE_KEYS = { K_OPTION, K_PANEL, K_PANEL_PLANT, K_TRAY, K_TRAY_SEQ, K_SAMPLE, K_PANELIST, K_PANELIST_NAME, K_RESULT, K_USER_FILTER, K_USER, K_USER_NEW };

	private QMState()
	{
	}

	static void clear(WebSession ws)
	{
		for (String k : STATE_KEYS)
		{
			ws.set(k, "");
		}
	}

	/** Session value as a Long, -1 when blank or not numeric (the web_WS JURL convention). */
	static long asLong(String value)
	{
		try
		{
			return value == null || value.trim().isEmpty() ? -1 : Long.parseLong(value.trim());
		}
		catch (NumberFormatException ex)
		{
			return -1;
		}
	}

	static String ts(java.sql.Timestamp t)
	{
		return t == null ? "" : t.toString().substring(0, 16);
	}
}
