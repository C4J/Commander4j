package com.commander4j.web.api;

/**
 * Panel admin (module FRM_QM_PANEL_ADMIN) - c4j_web_WS admin / users / userEdit pages, step 8 (2026-09-12).
 * The one-button admin.html ("Panelists") is dropped: the module opens the panellist list directly.
 *
 *   GET /api/qm/admin/users                    APP_QM_USERS filtered by the session's enabled flag (default Y)
 *   PUT /api/qm/admin/users/filter {enabled}   Y | N
 *   PUT /api/qm/admin/users/new                -> qmUserEdit in "new" mode (web_WS used a prompt() for the id)
 *   PUT /api/qm/admin/users/edit {userID}      -> qmUserEdit
 *   GET /api/qm/admin/user                     the selected panellist (blank fields + isNew when creating)
 *   PUT /api/qm/admin/user {userID, firstname, surname, enabled}   create (new mode) or update
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.web.db.JQMUserDB;
import com.commander4j.web.entity.JQMUserEntity;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/qm/admin/*" })
public class QMAdminServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String moduleId()
	{
		return QMState.MODULE_ADMIN;
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		boolean get = method.equals("GET");
		boolean put = method.equals("PUT");
		switch (path)
		{
			case "/users":
				return get ? users(ws) : unknown(method, path);
			case "/users/filter":
				return put ? filter(ws, str(body, "enabled")) : unknown(method, path);
			case "/users/new":
				return put ? userNew(ws) : unknown(method, path);
			case "/users/edit":
				return put ? userEdit(ws, str(body, "userID")) : unknown(method, path);
			case "/user":
				return get ? user(ws) : put ? userSave(ws, body) : unknown(method, path);
			default:
				return unknown(method, path);
		}
	}

	private ApiResponse unknown(String method, String path)
	{
		return ApiResponse.fail("Unknown admin action " + method + " " + path);
	}

	private String filterValue(WebSession ws)
	{
		String f = ws.get(QMState.K_USER_FILTER);
		return f.equals("N") ? "N" : "Y";   // web_WS users.html: default filter Y
	}

	private ApiResponse users(WebSession ws)
	{
		String filter = filterValue(ws);
		ws.set(QMState.K_USER_FILTER, filter);
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		String selected = ws.get(QMState.K_USER);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMUserEntity u : db.getUsersByEnabled(filter))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("userID", u.getUserID());
			m.put("firstname", u.getFirstName());
			m.put("surname", u.getSurname());
			m.put("enabled", u.getEnabled());
			m.put("selected", u.getUserID().equals(selected));
			rows.add(m);
		}
		return ApiResponse.ok().put("filter", filter).put("users", rows).put("selectedUser", selected);
	}

	private ApiResponse filter(WebSession ws, String enabled)
	{
		ws.set(QMState.K_USER_FILTER, enabled.equals("N") ? "N" : "Y");
		return ApiResponse.ok();
	}

	private ApiResponse userNew(WebSession ws)
	{
		ws.set(QMState.K_USER, "");
		ws.set(QMState.K_USER_NEW, "Y");
		return ApiResponse.goTo(QMState.PAGE_USER_EDIT);
	}

	private ApiResponse userEdit(WebSession ws, String userID)
	{
		String id = userID.trim().toUpperCase();
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		if (id.isEmpty() || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a user.");
		}
		ws.set(QMState.K_USER, id);
		ws.set(QMState.K_USER_NEW, "");
		return ApiResponse.goTo(QMState.PAGE_USER_EDIT);
	}

	private ApiResponse user(WebSession ws)
	{
		boolean isNew = ws.get(QMState.K_USER_NEW).equals("Y");
		if (isNew)
		{
			return ApiResponse.ok().put("isNew", true).put("userID", "").put("firstname", "").put("surname", "").put("enabled", "Y");
		}
		String id = ws.get(QMState.K_USER);
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		if (id.isEmpty() || db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a user.").withNext(QMState.PAGE_USERS);
		}
		JQMUserEntity u = db.getProperties(id);
		return ApiResponse.ok().put("isNew", false).put("userID", u.getUserID()).put("firstname", u.getFirstName()).put("surname", u.getSurname()).put("enabled", u.getEnabled());
	}

	private ApiResponse userSave(WebSession ws, JsonObject body)
	{
		boolean isNew = ws.get(QMState.K_USER_NEW).equals("Y");
		JQMUserDB db = new JQMUserDB(ws.getSelectedHost(), ws.getId());
		String id = isNew ? str(body, "userID").toUpperCase() : ws.get(QMState.K_USER);
		if (id.isEmpty())
		{
			return ApiResponse.fail("User ID required.");
		}
		String enabled = str(body, "enabled").equalsIgnoreCase("Y") || str(body, "enabled").equalsIgnoreCase("true") ? "Y" : "N";
		JQMUserEntity u = new JQMUserEntity();
		u.setUserID(id);
		u.setFirstName(str(body, "firstname"));
		u.setSurname(str(body, "surname"));
		u.setEnabled(enabled);
		if (isNew)
		{
			if (db.isValid(id))
			{
				return ApiResponse.fail("User [" + id + "] already exists.");
			}
			if (db.create(u) == false)
			{
				return ApiResponse.fail(db.getErrorMessage());
			}
			ws.set(QMState.K_USER, id);
			ws.set(QMState.K_USER_NEW, "");
			return ApiResponse.ok().withMessage("User " + id + " created.").put("isNew", false).put("userID", id);
		}
		if (db.isValid(id) == false)
		{
			return ApiResponse.fail("Select a user.").withNext(QMState.PAGE_USERS);
		}
		if (db.update(u) == false)
		{
			return ApiResponse.fail(db.getErrorMessage());
		}
		return ApiResponse.ok().withMessage("User " + id + " saved.").put("isNew", false).put("userID", id);
	}
}
