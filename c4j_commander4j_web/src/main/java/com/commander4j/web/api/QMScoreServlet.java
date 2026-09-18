package com.commander4j.web.api;

/**
 * Panel scores (module FRM_QM_PANEL_SCORES) - c4j_web_WS resultUserSelect / resultPanelSelect / resultTraySelect /
 * resultSampleSelect pages, step 8 (2026-09-12).
 *
 *   GET /api/qm/score/panelists              enabled APP_QM_USERS + the selected one
 *   PUT /api/qm/score/panelists {userID}     typed/scanned id or combo choice; must be an enabled panellist -> qmScorePanel
 *   GET /api/qm/score/panels                 panels with status Ready
 *   PUT /api/qm/score/panels {panelID}       -> qmScoreTray
 *   GET /api/qm/score/trays                  trays of the chosen panel
 *   PUT /api/qm/score/trays {trayID}         -> qmScoreSample
 *   GET /api/qm/score/samples                samples on the tray with this panellist's result + the decision list
 *   PUT /api/qm/score/samples {result, sampleIDs:[...]}   write one result per sample (create, else update)
 *
 * The panellist is deliberately NOT the logged-on Commander4j account: scores are keyed on APP_QM_USERS.USER_ID
 * (PK TRAY_ID + SAMPLE_ID + USER_ID, review 9.2) and the terminal is shared by the panel. The id is validated
 * against APP_QM_USERS here and kept in the session; the client never names the user on the write.
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.db.JDBQMSelectList;
import com.commander4j.web.db.JQMPanelDB;
import com.commander4j.web.db.JQMTrayDB;
import com.commander4j.web.db.JQMTrayResultDB;
import com.commander4j.web.db.JQMUserDB;
import com.commander4j.web.entity.JQMPanelEntity;
import com.commander4j.web.entity.JQMTrayEntity;
import com.commander4j.web.entity.JQMTrayResultEntity;
import com.commander4j.web.entity.JQMUserEntity;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/qm/score/*" })
public class QMScoreServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String moduleId()
	{
		return QMState.MODULE_SCORES;
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		boolean get = method.equals("GET");
		boolean put = method.equals("PUT");
		switch (path)
		{
			case "/panelists":
				return get ? panelists(ws) : put ? choosePanelist(ws, str(body, "userID")) : unknown(method, path);
			case "/panels":
				return get ? panels(ws) : put ? choosePanel(ws, str(body, "panelID")) : unknown(method, path);
			case "/trays":
				return get ? trays(ws) : put ? chooseTray(ws, str(body, "trayID")) : unknown(method, path);
			case "/samples":
				return get ? samples(ws) : put ? apply(ws, body) : unknown(method, path);
			default:
				return unknown(method, path);
		}
	}

	private ApiResponse unknown(String method, String path)
	{
		return ApiResponse.fail("Unknown score action " + method + " " + path);
	}

	private static String name(JQMUserEntity u)
	{
		return u.getUserID() + " - " + u.getFirstName() + " " + u.getSurname();
	}

	// ---- panellist ------------------------------------------------------------

	private ApiResponse panelists(WebSession ws)
	{
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_PANELIST);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMUserEntity u : db.getUsersByEnabled("Y"))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("userID", u.getUserID());
			m.put("name", name(u));
			m.put("selected", u.getUserID().equals(selected));
			rows.add(m);
		}
		return ApiResponse.ok().put("panelists", rows).put("selectedPanelist", selected);
	}

	private ApiResponse choosePanelist(WebSession ws, String userID)
	{
		String id = userID.trim().toUpperCase();
		if (id.isEmpty())
		{
			return ApiResponse.fail("Select a panelist.");
		}
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		if (db.isValid(id) == false)
		{
			return ApiResponse.fail("Panelist [" + id + "] not found.");
		}
		JQMUserEntity u = db.getProperties(id);
		if ("Y".equals(u.getEnabled()) == false)
		{
			return ApiResponse.fail("Panelist [" + id + "] is disabled.");
		}
		ws.set(QMState.K_PANELIST, u.getUserID());
		ws.set(QMState.K_PANELIST_NAME, name(u));
		ws.set(QMState.K_PANEL, "");       // web_WS newSelection(): a new panellist clears panel + tray
		ws.set(QMState.K_PANEL_PLANT, "");
		ws.set(QMState.K_TRAY, "");
		ws.set(QMState.K_TRAY_SEQ, "");
		return ApiResponse.goTo(QMState.PAGE_SCORE_PANEL);
	}

	private ApiResponse needPanelist(WebSession ws)
	{
		return ws.get(QMState.K_PANELIST).isEmpty() ? ApiResponse.fail("Select a panelist.").withNext(QMState.PAGE_SCORE_USER) : null;
	}

	// ---- panel ----------------------------------------------------------------

	private ApiResponse panels(WebSession ws)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_PANEL);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMPanelEntity p : db.getPanelsByStatus("Ready"))
		{
			String id = String.valueOf(p.getPanelID());
			if (selected.isEmpty())
			{
				selected = id;
			}
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("panelID", id);
			m.put("plant", p.getPlant());
			m.put("description", p.getDescription());
			m.put("selected", id.equals(selected));
			rows.add(m);
		}
		if (rows.stream().noneMatch(m -> Boolean.TRUE.equals(m.get("selected"))) && rows.isEmpty() == false)
		{
			rows.get(0).put("selected", true);
			selected = (String) rows.get(0).get("panelID");
		}
		return ApiResponse.ok().put("panelist", ws.get(QMState.K_PANELIST_NAME)).put("panels", rows).put("selectedPanel", selected);
	}

	private ApiResponse choosePanel(WebSession ws, String panelID)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(panelID);
		if (id <= 0 || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a panel.");
		}
		JQMPanelEntity p = db.getProperties(id);
		if ("Ready".equals(p.getStatus()) == false)
		{
			return ApiResponse.fail("Panel " + id + " is not Ready.");
		}
		ws.set(QMState.K_PANEL, String.valueOf(id));
		ws.set(QMState.K_PANEL_PLANT, p.getPlant());
		ws.set(QMState.K_TRAY, "");
		ws.set(QMState.K_TRAY_SEQ, "");
		return ApiResponse.goTo(QMState.PAGE_SCORE_TRAY);
	}

	// ---- tray -----------------------------------------------------------------

	private ApiResponse trays(WebSession ws)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		long panelID = QMState.asLong(ws.get(QMState.K_PANEL));
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_SCORE_PANEL);
		}
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_TRAY);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMTrayEntity t : db.getTraysByPanel(panelID))
		{
			String id = String.valueOf(t.getTrayID());
			if (selected.isEmpty())
			{
				selected = id;
			}
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("trayID", id);
			m.put("traySequence", String.valueOf(t.getTraySequence()));
			m.put("description", t.getDescription());
			m.put("selected", id.equals(selected));
			rows.add(m);
		}
		if (rows.stream().noneMatch(m -> Boolean.TRUE.equals(m.get("selected"))) && rows.isEmpty() == false)
		{
			rows.get(0).put("selected", true);
			selected = (String) rows.get(0).get("trayID");
		}
		return ApiResponse.ok().put("panelist", ws.get(QMState.K_PANELIST_NAME)).put("panelID", String.valueOf(panelID)).put("plant", ws.get(QMState.K_PANEL_PLANT)).put("trays", rows).put("selectedTray", selected);
	}

	private ApiResponse chooseTray(WebSession ws, String trayID)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		long panelID = QMState.asLong(ws.get(QMState.K_PANEL));
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(trayID);
		if (panelID <= 0 || id <= 0 || db.isValid(id, panelID, "TrayID") == false)
		{
			return ApiResponse.fail("Select a tray.");
		}
		JQMTrayEntity t = db.getProperties(panelID, id, "TrayID");
		ws.set(QMState.K_TRAY, String.valueOf(id));
		ws.set(QMState.K_TRAY_SEQ, String.valueOf(t.getTraySequence()));
		ws.set(QMState.K_RESULT, "");   // web_WS resultTraySelect: sessionStorage selectedResult = ""
		return ApiResponse.goTo(QMState.PAGE_SCORE_SAMPLE);
	}

	// ---- samples + decisions --------------------------------------------------

	private ApiResponse samples(WebSession ws)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		long trayID = QMState.asLong(ws.get(QMState.K_TRAY));
		if (trayID <= 0)
		{
			return ApiResponse.fail("Select a tray.").withNext(QMState.PAGE_SCORE_TRAY);
		}
		String panelist = ws.get(QMState.K_PANELIST);
		JQMTrayResultDB db = new JQMTrayResultDB(ws.getSelectedHost(), ws.getId());
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMTrayResultEntity r : db.getResultsByTrayUser(trayID, panelist))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("sampleID", String.valueOf(r.getSampleID()));
			m.put("sequenceID", r.getSequenceID() == null ? "" : String.valueOf(r.getSequenceID()));
			m.put("sequenceLetter", r.getSequenceLetter() == null ? "" : r.getSequenceLetter());
			m.put("value", r.getValue() == null ? "" : r.getValue());
			rows.add(m);
		}
		List<Map<String, Object>> decisions = new ArrayList<Map<String, Object>>();
		JDBQMSelectList sl = new JDBQMSelectList(ws.getSelectedHost(), ws.getId());
		for (JDBQMSelectList d : sl.getSelectList(QMState.DECISION_LIST_ID))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("value", d.getValue());
			m.put("description", d.getDescription());
			decisions.add(m);
		}
		return ApiResponse.ok().put("panelist", ws.get(QMState.K_PANELIST_NAME)).put("panelID", ws.get(QMState.K_PANEL)).put("plant", ws.get(QMState.K_PANEL_PLANT)).put("traySequence", ws.get(QMState.K_TRAY_SEQ)).put("samples", rows).put("decisions", decisions).put("selectedResult", ws.get(QMState.K_RESULT));
	}

	private ApiResponse apply(WebSession ws, JsonObject body)
	{
		ApiResponse guard = needPanelist(ws);
		if (guard != null)
		{
			return guard;
		}
		long trayID = QMState.asLong(ws.get(QMState.K_TRAY));
		if (trayID <= 0)
		{
			return ApiResponse.fail("Select a tray.").withNext(QMState.PAGE_SCORE_TRAY);
		}
		String result = str(body, "result");
		ws.set(QMState.K_RESULT, result);
		if (result.isEmpty())
		{
			return ApiResponse.fail("Select a decision.");
		}
		boolean known = false;
		JDBQMSelectList sl = new JDBQMSelectList(ws.getSelectedHost(), ws.getId());
		for (JDBQMSelectList d : sl.getSelectList(QMState.DECISION_LIST_ID))
		{
			if (result.equals(d.getValue()))
			{
				known = true;
			}
		}
		if (known == false)
		{
			return ApiResponse.fail("Invalid decision [" + result + "].");
		}
		List<JQMTrayResultEntity> list = new ArrayList<JQMTrayResultEntity>();
		JsonElement ids = body.get("sampleIDs");
		if (ids != null && ids.isJsonArray())
		{
			JsonArray arr = ids.getAsJsonArray();
			for (JsonElement e : arr)
			{
				long sampleID = QMState.asLong(e.isJsonNull() ? "" : e.getAsString());
				if (sampleID <= 0)
				{
					continue;
				}
				JQMTrayResultEntity r = new JQMTrayResultEntity();   // exactly web_WS GetSelected()'s object per checked box
				r.setTrayID(trayID);
				r.setSampleID(sampleID);
				r.setUserID(ws.get(QMState.K_PANELIST));
				r.setTestID(QMState.TEST_ID);
				r.setValue(result);
				list.add(r);
			}
		}
		if (list.isEmpty())
		{
			return ApiResponse.fail("Select at least one sample.");
		}
		JQMTrayResultDB db = new JQMTrayResultDB(ws.getSelectedHost(), ws.getId());
		db.creates(list.toArray(new JQMTrayResultEntity[0]));   // create, else update - as web_WS (creates() itself always returns true)
		String err = db.getErrorMessage() == null ? "" : db.getErrorMessage().trim();
		if (err.isEmpty() == false)
		{
			return ApiResponse.fail(err);   // web_WS reported success regardless; the last failed update's message is surfaced here
		}
		return ApiResponse.ok().withMessage(list.size() + " result(s) saved.");
	}
}
