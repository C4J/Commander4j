package com.commander4j.web.api;

/**
 * SSCC History (module FRM_ADMIN_PALLET_HISTORY) - c4j_web_Issue's palletHistory page (step 7, 2026-09-11).
 *   PUT /api/history {sscc}   rows from JQMPalletHistoryDB.getPalletHistoryBySSCC (core SQL JDBPalletHistory.selectWithSSCC)
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.web.db.JQMPalletHistoryDB;
import com.commander4j.web.entity.JQMPalletHistoryEntity;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/history", "/api/history/*" })
public class HistoryServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String moduleId()
	{
		return "FRM_ADMIN_PALLET_HISTORY";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			return ApiResponse.ok().put("username", userId(ws));
		}
		if (method.equals("PUT") == false)
		{
			return ApiResponse.fail("Method not supported");
		}
		String[] err = new String[1];
		String sscc = normaliseSSCC(ws, str(body, "sscc"), err);
		if (sscc == null)
		{
			return ApiResponse.fail(err[0]);
		}
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMPalletHistoryEntity h : new JQMPalletHistoryDB(ws.getSelectedHost(), ws.getId()).getPalletHistoryBySSCC(sscc))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("date", h.getTransactionDate() == null ? "" : h.getTransactionDate().toString().substring(0, 19));
			m.put("type", h.getTransactionType());
			m.put("subtype", h.getTransactionSubtype());
			m.put("processOrder", h.getProcessOrder());
			m.put("location", h.getLocationId());
			m.put("quantity", h.getQuantity() == null ? "" : h.getQuantity().toPlainString());
			m.put("uom", h.getUom());
			rows.add(m);
		}
		String message = rows.isEmpty() ? "SSCC " + sscc + " not found." : rows.size() + " records displayed for SSCC " + sscc;
		return ApiResponse.ok().withMessage(message).put("sscc", sscc).put("history", rows);
	}
}
