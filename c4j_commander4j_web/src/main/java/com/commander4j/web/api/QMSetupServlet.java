package com.commander4j.web.api;

/**
 * Panel setup (module FRM_QM_PANEL_SETUP) - c4j_web_WS panels / panelEdit / trays / trayEdit / samples pages,
 * step 8 (2026-09-12). Same DAOs as web_WS (JQMPanelDB, JQMTrayDB, JQMTraySampleDB), same SQL ids.
 *
 *   GET /api/qm/setup/panels                    newest 24 panels + the selected one
 *   PUT /api/qm/setup/panels/new                create (status Prepare; the DAO defaults plant/description) -> qmPanelEdit
 *   PUT /api/qm/setup/panels/edit    {panelID}  -> qmPanelEdit
 *   PUT /api/qm/setup/panels/trays   {panelID}  -> qmTrays
 *   PUT /api/qm/setup/panels/samples {panelID}  -> qmSamples with no tray yet ("Scan Samples": the operator scans TRAYn first)
 *   PUT /api/qm/setup/panels/delete  {panelID}  delete panel + trays + samples + results (DAO cascade)
 *   GET /api/qm/setup/panel                     the selected panel's fields
 *   PUT /api/qm/setup/panel {plant, description, status, action: save | trays}
 *   GET /api/qm/setup/trays                     trays of the selected panel
 *   PUT /api/qm/setup/trays/new                 create (next sequence, "Tray n") -> qmTrayEdit
 *   PUT /api/qm/setup/trays/edit    {trayID}    -> qmTrayEdit
 *   PUT /api/qm/setup/trays/samples {trayID}    -> qmSamples
 *   PUT /api/qm/setup/trays/delete  {trayID}
 *   GET /api/qm/setup/tray                      the selected tray's fields
 *   PUT /api/qm/setup/tray {description, action: save | samples}
 *   GET /api/qm/setup/samples                   samples on the selected tray (by panel + tray sequence)
 *   PUT /api/qm/setup/samples/scan   {data}     "TRAY<n>" selects tray sequence n; a number adds that sample
 *                                               (tray created on first sample if missing - web_WS TraySamples POST)
 *   PUT /api/qm/setup/samples/delete {sampleID}
 *
 * Selection is sent with every action and remembered in the session, so a page reload re-selects the same row.
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.web.db.JQMPanelDB;
import com.commander4j.web.db.JQMTrayDB;
import com.commander4j.web.db.JQMTraySampleDB;
import com.commander4j.web.entity.JQMPanelEntity;
import com.commander4j.web.entity.JQMTrayEntity;
import com.commander4j.web.entity.JQMTraySampleEntity;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/qm/setup/*" })
public class QMSetupServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String moduleId()
	{
		return QMState.MODULE_SETUP;
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		boolean get = method.equals("GET");
		boolean put = method.equals("PUT");

		switch (path)
		{
			case "/panels":
				return get ? panels(ws) : unknown(method, path);
			case "/panels/new":
				return put ? panelNew(ws) : unknown(method, path);
			case "/panels/edit":
				return put ? panelGo(ws, str(body, "panelID"), QMState.PAGE_PANEL_EDIT, false) : unknown(method, path);
			case "/panels/trays":
				return put ? panelGo(ws, str(body, "panelID"), QMState.PAGE_TRAYS, false) : unknown(method, path);
			case "/panels/samples":
				return put ? panelGo(ws, str(body, "panelID"), QMState.PAGE_SAMPLES, true) : unknown(method, path);
			case "/panels/delete":
				return put ? panelDelete(ws, str(body, "panelID")) : unknown(method, path);
			case "/panel":
				return get ? panel(ws) : put ? panelSave(ws, body) : unknown(method, path);
			case "/trays":
				return get ? trays(ws) : unknown(method, path);
			case "/trays/new":
				return put ? trayNew(ws) : unknown(method, path);
			case "/trays/edit":
				return put ? trayGo(ws, str(body, "trayID"), QMState.PAGE_TRAY_EDIT) : unknown(method, path);
			case "/trays/samples":
				return put ? trayGo(ws, str(body, "trayID"), QMState.PAGE_SAMPLES) : unknown(method, path);
			case "/trays/delete":
				return put ? trayDelete(ws, str(body, "trayID")) : unknown(method, path);
			case "/tray":
				return get ? tray(ws) : put ? traySave(ws, body) : unknown(method, path);
			case "/samples":
				return get ? samples(ws) : unknown(method, path);
			case "/samples/scan":
				return put ? sampleScan(ws, str(body, "data")) : unknown(method, path);
			case "/samples/delete":
				return put ? sampleDelete(ws, str(body, "sampleID")) : unknown(method, path);
			default:
				return unknown(method, path);
		}
	}

	private ApiResponse unknown(String method, String path)
	{
		return ApiResponse.fail("Unknown setup action " + method + " " + path);
	}

	// ---- panels ---------------------------------------------------------------

	private ApiResponse panels(WebSession ws)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_PANEL);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		String plant = "";
		for (JQMPanelEntity p : db.getPanelsListLimit(QMState.PANEL_LIST_LIMIT))
		{
			String id = String.valueOf(p.getPanelID());
			if (selected.isEmpty())
			{
				selected = id;   // web_WS panelListGet: nothing stored -> the first row becomes the selection
			}
			if (id.equals(selected))
			{
				plant = p.getPlant();
			}
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("panelID", id);
			m.put("plant", p.getPlant());
			m.put("description", p.getDescription());
			m.put("status", p.getStatus());
			m.put("selected", id.equals(selected));
			rows.add(m);
		}
		if (rows.stream().noneMatch(m -> Boolean.TRUE.equals(m.get("selected"))))
		{
			selected = rows.isEmpty() ? "" : (String) rows.get(0).get("panelID");
			if (rows.isEmpty() == false)
			{
				rows.get(0).put("selected", true);
				plant = (String) rows.get(0).get("plant");
			}
		}
		ws.set(QMState.K_PANEL, selected);
		ws.set(QMState.K_PANEL_PLANT, plant);
		return ApiResponse.ok().put("panels", rows).put("selectedPanel", selected);
	}

	/** Remember the chosen panel (must exist) and move on; clearTray for "Scan Samples" from the panel list. */
	private ApiResponse panelGo(WebSession ws, String panelID, String page, boolean clearTray)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(panelID);
		if (id <= 0 || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a panel.");
		}
		JQMPanelEntity p = db.getProperties(id);
		ws.set(QMState.K_PANEL, String.valueOf(id));
		ws.set(QMState.K_PANEL_PLANT, p.getPlant());
		if (clearTray)
		{
			ws.set(QMState.K_TRAY, "");
			ws.set(QMState.K_TRAY_SEQ, "");
		}
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.goTo(page);
	}

	private ApiResponse panelNew(WebSession ws)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		JQMPanelEntity p = new JQMPanelEntity();
		p.setStatus("Prepare");   // web_WS newPanel(): { status: 'Prepare' }; plant/description defaulted by the DAO
		if (db.create(p) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		ws.set(QMState.K_PANEL, String.valueOf(p.getPanelID()));
		ws.set(QMState.K_PANEL_PLANT, p.getPlant());
		ws.set(QMState.K_TRAY, "");
		ws.set(QMState.K_TRAY_SEQ, "");
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.goTo(QMState.PAGE_PANEL_EDIT).withMessage("Panel " + p.getPanelID() + " created.");
	}

	private ApiResponse panelDelete(WebSession ws, String panelID)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(panelID);
		if (id <= 0 || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a panel.");
		}
		JQMPanelEntity p = db.getProperties(id);
		if (db.delete(p) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		ws.set(QMState.K_PANEL, "");
		ws.set(QMState.K_PANEL_PLANT, "");
		ws.set(QMState.K_TRAY, "");
		ws.set(QMState.K_TRAY_SEQ, "");
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.ok().withMessage("Panel " + id + " deleted.");
	}

	private ApiResponse panel(WebSession ws)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(ws.get(QMState.K_PANEL));
		if (id <= 0 || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		JQMPanelEntity p = db.getProperties(id);
		return ApiResponse.ok().put("panelID", String.valueOf(p.getPanelID())).put("plant", p.getPlant()).put("description", p.getDescription()).put("status", p.getStatus()).put("created", QMState.ts(p.getCreated())).put("updated", QMState.ts(p.getUpdated()));
	}

	private ApiResponse panelSave(WebSession ws, JsonObject body)
	{
		JQMPanelDB db = new JQMPanelDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(ws.get(QMState.K_PANEL));
		if (id <= 0 || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		String status = str(body, "status");
		if (status.equals("Prepare") == false && status.equals("Ready") == false && status.equals("Complete") == false)
		{
			return ApiResponse.fail("Invalid status [" + status + "].");
		}
		JQMPanelEntity p = db.getProperties(id);
		p.setPlant(str(body, "plant"));
		p.setDescription(str(body, "description"));
		p.setStatus(status);
		if (db.update(p) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		ws.set(QMState.K_PANEL_PLANT, p.getPlant());
		String action = str(body, "action");
		if (action.equals("trays"))
		{
			return ApiResponse.goTo(QMState.PAGE_TRAYS);
		}
		return ApiResponse.ok().withMessage("Panel " + id + " saved.");
	}

	// ---- trays ----------------------------------------------------------------

	private long selectedPanel(WebSession ws)
	{
		return QMState.asLong(ws.get(QMState.K_PANEL));
	}

	private ApiResponse trays(WebSession ws)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_TRAY);
		String seq = "";
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMTrayEntity t : db.getTraysByPanel(panelID))
		{
			String id = String.valueOf(t.getTrayID());
			if (selected.isEmpty())
			{
				selected = id;
			}
			if (id.equals(selected))
			{
				seq = String.valueOf(t.getTraySequence());
			}
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("trayID", id);
			m.put("traySequence", String.valueOf(t.getTraySequence()));
			m.put("description", t.getDescription());
			m.put("selected", id.equals(selected));
			rows.add(m);
		}
		if (rows.stream().noneMatch(m -> Boolean.TRUE.equals(m.get("selected"))))
		{
			selected = "";
			seq = "";
			if (rows.isEmpty() == false)
			{
				rows.get(0).put("selected", true);
				selected = (String) rows.get(0).get("trayID");
				seq = (String) rows.get(0).get("traySequence");
			}
		}
		ws.set(QMState.K_TRAY, selected);
		ws.set(QMState.K_TRAY_SEQ, seq);
		return ApiResponse.ok().put("panelID", String.valueOf(panelID)).put("plant", ws.get(QMState.K_PANEL_PLANT)).put("trays", rows).put("selectedTray", selected);
	}

	private ApiResponse trayGo(WebSession ws, String trayID, String page)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(trayID);
		if (id <= 0 || db.isValid(id, panelID, "TrayID") == false)
		{
			return ApiResponse.fail("Select a tray.");
		}
		JQMTrayEntity t = db.getProperties(panelID, id, "TrayID");
		ws.set(QMState.K_TRAY, String.valueOf(id));
		ws.set(QMState.K_TRAY_SEQ, String.valueOf(t.getTraySequence()));
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.goTo(page);
	}

	private ApiResponse trayNew(WebSession ws)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		JQMTrayEntity t = new JQMTrayEntity();
		t.setPanelID(panelID);
		t.setTrayID((long) -1);         // web_WS Trays POST: no trayID / traySequence in the URL -> -1 -> DAO allocates both
		t.setTraySequence((long) -1);
		t.setDescription("");
		if (db.create(t) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		ws.set(QMState.K_TRAY, String.valueOf(t.getTrayID()));
		ws.set(QMState.K_TRAY_SEQ, String.valueOf(t.getTraySequence()));
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.goTo(QMState.PAGE_TRAY_EDIT).withMessage("Tray " + t.getTraySequence() + " created.");
	}

	private ApiResponse trayDelete(WebSession ws, String trayID)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long id = QMState.asLong(trayID);
		if (id <= 0 || db.isValid(id, panelID, "TrayID") == false)
		{
			return ApiResponse.fail("Select a tray.");
		}
		JQMTrayEntity t = db.getProperties(panelID, id, "TrayID");
		t.setqueryType("TrayID");   // JQMTrayDB.delete builds its SQL id from this (web_WS set it from the URL's queryType)
		if (db.delete(t) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		ws.set(QMState.K_TRAY, "");
		ws.set(QMState.K_TRAY_SEQ, "");
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.ok().withMessage("Tray " + t.getTraySequence() + " deleted.");
	}

	private ApiResponse tray(WebSession ws)
	{
		long panelID = selectedPanel(ws);
		long id = QMState.asLong(ws.get(QMState.K_TRAY));
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		if (panelID <= 0 || id <= 0 || db.isValid(id, panelID, "TrayID") == false)
		{
			return ApiResponse.fail("Select a tray.").withNext(QMState.PAGE_TRAYS);
		}
		JQMTrayEntity t = db.getProperties(panelID, id, "TrayID");
		return ApiResponse.ok().put("panelID", String.valueOf(panelID)).put("plant", ws.get(QMState.K_PANEL_PLANT)).put("trayID", String.valueOf(t.getTrayID())).put("traySequence", String.valueOf(t.getTraySequence())).put("description", t.getDescription()).put("created", QMState.ts(t.getCreated())).put("updated", QMState.ts(t.getUpdated()));
	}

	private ApiResponse traySave(WebSession ws, JsonObject body)
	{
		long panelID = selectedPanel(ws);
		long id = QMState.asLong(ws.get(QMState.K_TRAY));
		JQMTrayDB db = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		if (panelID <= 0 || id <= 0 || db.isValid(id, panelID, "TrayID") == false)
		{
			return ApiResponse.fail("Select a tray.").withNext(QMState.PAGE_TRAYS);
		}
		JQMTrayEntity t = db.getProperties(panelID, id, "TrayID");
		t.setDescription(str(body, "description"));
		if (db.update(t) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		if (str(body, "action").equals("samples"))
		{
			ws.set(QMState.K_SAMPLE, "");
			return ApiResponse.goTo(QMState.PAGE_SAMPLES);
		}
		return ApiResponse.ok().withMessage("Tray " + t.getTraySequence() + " saved.");
	}

	// ---- samples (web_WS samples.html: tray identified by panel + tray SEQUENCE) --------------

	/** The tray id for the session's panel + tray sequence, or -1 when no such tray yet. */
	private long trayIdForSequence(WebSession ws, JQMTrayDB db, long panelID, long seq)
	{
		if (panelID <= 0 || seq <= 0)
		{
			return -1;
		}
		JQMTrayEntity t = db.getProperties(panelID, seq, "TraySequence");
		return t.getTrayID() == null || t.getTrayID() <= 0 ? -1 : t.getTrayID();
	}

	private ApiResponse samples(WebSession ws)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		long seq = QMState.asLong(ws.get(QMState.K_TRAY_SEQ));
		JQMTrayDB tdb = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long trayID = trayIdForSequence(ws, tdb, panelID, seq);
		String selected = ws.get(QMState.K_SAMPLE);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (trayID > 0)
		{
			JQMTraySampleDB sdb = new JQMTraySampleDB(ws.getSelectedHost(), ws.getId());
			for (JQMTraySampleEntity s : sdb.getSamplesByTray(trayID))
			{
				String id = String.valueOf(s.getSampleID());
				if (selected.isEmpty())
				{
					selected = id;
				}
				Map<String, Object> m = new LinkedHashMap<String, Object>();
				m.put("sampleID", id);
				m.put("sequenceID", String.valueOf(s.getSequenceID()));
				m.put("sequenceLetter", s.getSequenceLetter() == null ? "" : s.getSequenceLetter());
				m.put("selected", id.equals(selected));
				rows.add(m);
			}
		}
		if (rows.stream().noneMatch(m -> Boolean.TRUE.equals(m.get("selected"))))
		{
			selected = "";
			if (rows.isEmpty() == false)
			{
				rows.get(0).put("selected", true);
				selected = (String) rows.get(0).get("sampleID");
			}
		}
		ws.set(QMState.K_SAMPLE, selected);
		ws.set(QMState.K_TRAY, trayID > 0 ? String.valueOf(trayID) : "");
		return ApiResponse.ok().put("panelID", String.valueOf(panelID)).put("plant", ws.get(QMState.K_PANEL_PLANT)).put("traySequence", seq > 0 ? String.valueOf(seq) : "").put("samples", rows).put("selectedSample", selected);
	}

	private ApiResponse sampleScan(WebSession ws, String data)
	{
		long panelID = selectedPanel(ws);
		if (panelID <= 0)
		{
			return ApiResponse.fail("Select a panel.").withNext(QMState.PAGE_PANELS);
		}
		String v = data == null ? "" : data.trim().toUpperCase();
		if (v.isEmpty())
		{
			return ApiResponse.fail("Scan a tray or a sample.");
		}
		if (v.startsWith("TRAY"))
		{
			java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(v);
			if (m.find() == false)
			{
				return ApiResponse.fail("Invalid tray barcode [" + v + "].");
			}
			long seq = Long.parseLong(m.group());
			ws.set(QMState.K_TRAY_SEQ, String.valueOf(seq));
			ws.set(QMState.K_TRAY, "");
			ws.set(QMState.K_SAMPLE, "");
			return ApiResponse.ok().withMessage("Tray " + seq + " selected.");
		}
		if (v.matches("[+-]?\\d+(\\.\\d+)?") == false)
		{
			return ApiResponse.fail("Invalid barcode [" + v + "].");
		}
		long seq = QMState.asLong(ws.get(QMState.K_TRAY_SEQ));
		if (seq <= 0)
		{
			return ApiResponse.fail("Scan a tray first.");
		}
		long sampleID;
		try
		{
			sampleID = Long.parseLong(v);
		}
		catch (NumberFormatException ex)
		{
			return ApiResponse.fail("Invalid sample [" + v + "].");
		}

		// web_WS TraySamples POST ?queryType=TraySequence: find the tray by sequence, create it if missing
		JQMTrayDB tdb = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long trayID = trayIdForSequence(ws, tdb, panelID, seq);
		if (trayID <= 0)
		{
			JQMTrayEntity t = new JQMTrayEntity();
			t.setPanelID(panelID);
			t.setTrayID(tdb.getNewTrayID());
			t.setTraySequence(seq);
			t.setDescription("");
			if (t.getTrayID() <= 0 || tdb.create(t) == false)
			{
				return ApiResponse.fail(tdb.getErrorMessage().isEmpty() ? "Could not create tray " + seq + "." : tdb.getErrorMessage());
			}
			trayID = t.getTrayID();
		}
		ws.set(QMState.K_TRAY, String.valueOf(trayID));

		JQMTraySampleDB sdb = new JQMTraySampleDB(ws.getSelectedHost(), ws.getId());
		JQMTraySampleEntity s = new JQMTraySampleEntity();
		s.setTrayID(trayID);
		s.setSampleID(sampleID);
		s.setSequenceID(sdb.getNextSequenceID(trayID));
		if (s.getSequenceID() == null || s.getSequenceID() <= 0)
		{
			return ApiResponse.fail("Could not allocate a sample sequence.");
		}
		if (sdb.isSampleAssignedToTray(s))
		{
			return ApiResponse.fail(sdb.getErrorMessage().isEmpty() ? "Sample " + sampleID + " is already on tray " + seq + "." : sdb.getErrorMessage());
		}
		if (sdb.create(s) == false)
		{
			return ApiResponse.fail(sdb.getErrorMessage());
		}
		ws.set(QMState.K_SAMPLE, String.valueOf(sampleID));
		return ApiResponse.ok().withMessage("Sample " + sampleID + " added to tray " + seq + ".");
	}

	private ApiResponse sampleDelete(WebSession ws, String sampleID)
	{
		long panelID = selectedPanel(ws);
		long seq = QMState.asLong(ws.get(QMState.K_TRAY_SEQ));
		JQMTrayDB tdb = new JQMTrayDB(ws.getSelectedHost(), ws.getId());
		long trayID = trayIdForSequence(ws, tdb, panelID, seq);   // resolved here: web_WS deleted against a stale selectedTray after a TRAY scan
		long id = QMState.asLong(sampleID);
		if (trayID <= 0 || id <= 0)
		{
			return ApiResponse.fail("Select a sample.");
		}
		JQMTraySampleDB sdb = new JQMTraySampleDB(ws.getSelectedHost(), ws.getId());
		if (sdb.isValid(trayID, id) == false)
		{
			return ApiResponse.fail("Sample " + id + " is not on tray " + seq + ".");
		}
		JQMTraySampleEntity s = sdb.getProperties(trayID, id);
		if (sdb.delete(s) == false)
		{
			return ApiResponse.fail(sdb.getErrorMessage());
		}
		ws.set(QMState.K_SAMPLE, "");
		return ApiResponse.ok().withMessage("Sample " + id + " removed from tray " + seq + ".");
	}
}
