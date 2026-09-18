package com.commander4j.web.api;

/**
 * Waste Log #22 (module FRM_ADMIN_WASTE_LOG):
 *   GET /api/waste            combo lists (wasteComboRefesh) + current values
 *   PUT /api/waste {wasteTransactionCombo, wasteLocationCombo, wasteContainerCombo, wasteMaterialCombo,
 *                   wasteReasonCombo, wasteProcessOrder, wasteQuantity, wasteBarcode}
 *                             Process.wasteLog Submit: barcode present -> parse AIs 91-95/00 into the
 *                             combos; barcode blank -> write the log. Returns the refreshed lists.
 *   PUT /api/waste/cancel     wasteLogClearLastUsed -> menu
 * As in web_react, choosing a combo value is sent as a barcode "<AI><value>" so the same
 * parsing path runs (the JSP's onchange did exactly that).
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBWasteContainer;
import com.commander4j.db.JDBWasteLocation;
import com.commander4j.db.JDBWasteLog;
import com.commander4j.db.JDBWasteMaterial;
import com.commander4j.db.JDBWasteReasons;
import com.commander4j.db.JDBWasteTransactionType;
import com.commander4j.util.JUtility;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/waste", "/api/waste/*" })
public class WasteServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	private static final String[] VALUE_KEYS = { "wasteTransactionID", "wasteLocationID", "wasteContainerID", "wasteMaterialID", "wasteReasonID", "wasteProcessOrder", "wasteQuantity", "wasteMaterialUOM" };

	@Override
	protected String moduleId()
	{
		return "FRM_ADMIN_WASTE_LOG";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			return lists(ws, ApiResponse.ok());
		}
		if (method.equals("PUT") && path.equals("/cancel"))
		{
			clearLastUsed(ws);
			return ApiResponse.goTo(SessionServlet.PAGE_MENU);
		}
		if (method.equals("PUT"))
		{
			return submit(ws, body);
		}
		return ApiResponse.fail("Method not supported");
	}

	// ---- wasteComboRefesh as data -------------------------------------------------------

	private ApiResponse lists(WebSession ws, ApiResponse r)
	{
		String host = ws.getSelectedHost(), sid = ws.getId();

		List<String> transactions = new ArrayList<String>();
		for (JDBWasteTransactionType t : new JDBWasteTransactionType(host, sid).getWasteTransactionTypesList(true, JDBWasteTransactionType.displayModeShort))
		{
			transactions.add(t.getWasteTransactionType());
		}
		List<String> locations = new ArrayList<String>();
		for (JDBWasteLocation l : new JDBWasteLocation(host, sid).getWasteLocationsList(true, JDBWasteLocation.displayModeShort))
		{
			locations.add(l.getWasteLocationID());
		}
		List<String> containers = new ArrayList<String>();
		for (JDBWasteContainer c : new JDBWasteContainer(host, sid).getWasteContainerList(true, JDBWasteContainer.displayModeShort))
		{
			containers.add(c.getWasteContainerID());
		}
		List<String> materials = new ArrayList<String>();
		for (JDBWasteMaterial m : new JDBWasteMaterial(host, sid).getWasteMaterialIDsforlLocationList(ws.get("wasteLocationID"), true, JDBWasteMaterial.displayModeShort))
		{
			materials.add(m.getWasteMaterialID());
		}
		List<String> reasons = new ArrayList<String>();
		for (JDBWasteReasons rs : new JDBWasteReasons(host, sid).getWasteReasonssList(true, JDBWasteReasons.displayModeShort))
		{
			reasons.add(rs.getWasteReasonID());
		}

		r.put("transactions", transactions).put("locations", locations).put("containers", containers).put("materials", materials).put("reasons", reasons);
		for (String k : VALUE_KEYS)
		{
			r.put(k, ws.get(k));
		}
		return r;
	}

	private void saveLastUsed(WebSession ws, JsonObject body)
	{
		ws.set("wasteTransactionID", str(body, "wasteTransactionCombo").toUpperCase());
		ws.set("wasteLocationID", str(body, "wasteLocationCombo").toUpperCase());
		ws.set("wasteContainerID", str(body, "wasteContainerCombo").toUpperCase());
		ws.set("wasteMaterialID", str(body, "wasteMaterialCombo").toUpperCase());
		ws.set("wasteReasonID", str(body, "wasteReasonCombo").toUpperCase());
	}

	private void clearLastUsed(WebSession ws)
	{
		for (String k : new String[] { "wasteTransactionID", "wasteLocationID", "wasteContainerID", "wasteMaterialID", "wasteReasonID", "wasteProcessOrder", "wasteMaterialUOM" })
		{
			ws.set(k, "");
		}
		ws.set("wasteQuantity", ".000");
	}

	// ---- Submit  (Process.wasteLog) ------------------------------------------------------

	private ApiResponse submit(WebSession ws, JsonObject body)
	{
		saveLastUsed(ws, body);
		String message = "";
		String wasteBarcode = str(body, "wasteBarcode").toUpperCase();
		String host = ws.getSelectedHost(), sid = ws.getId();

		if (wasteBarcode.isEmpty() == false)
		{
			JEANBarcode bcode = ws.barcode();
			if (bcode.parseBarcodeData(wasteBarcode))
			{
				String wasteLocationID = bcode.getStringforAppID("91");
				String wasteMaterialID = bcode.getStringforAppID("92");
				String wasteReasonID = bcode.getStringforAppID("93");
				String wasteProcessOrder = bcode.getStringforAppID("94");
				String wasteContainerID = bcode.getStringforAppID("95");
				String wasteSSCC = bcode.getStringforAppID("00");

				if (wasteSSCC.isEmpty() == false)
				{
					JDBPallet wpal = new JDBPallet(host, sid);
					if (wpal.getPalletProperties(wasteSSCC))
					{
						wasteProcessOrder = wpal.getProcessOrder();
					}
				}
				if (wasteProcessOrder.isEmpty() == false)
				{
					ws.set("wasteProcessOrder", wasteProcessOrder);
				}
				if (wasteLocationID.isEmpty() == false)
				{
					ws.set("wasteLocationID", wasteLocationID);
				}
				if (wasteContainerID.isEmpty() == false)
				{
					ws.set("wasteContainerID", wasteContainerID);
				}
				if (wasteMaterialID.isEmpty() == false)
				{
					ws.set("wasteMaterialID", wasteMaterialID);
					JDBWasteMaterial wm = new JDBWasteMaterial(host, sid);
					ws.set("wasteMaterialUOM", wm.getWasteMaterialProperties(wasteMaterialID) ? "KG" : "");
				}
				else if (ws.get("wasteMaterialID").isEmpty())
				{
					ws.set("wasteMaterialUOM", "");
				}
				if (wasteReasonID.isEmpty() == false)
				{
					ws.set("wasteReasonID", wasteReasonID);
				}
			}
			else
			{
				message = bcode.getErrorMessage();
			}
		}
		else
		{
			logger.debug("Write Waste_Log");
			String processOrder = str(body, "wasteProcessOrder");
			String quantity = str(body, "wasteQuantity");

			JDBWasteLog wlog = new JDBWasteLog(host, sid);
			wlog.setTransactionType(ws.get("wasteTransactionID"));
			wlog.setLocationID(ws.get("wasteLocationID"));
			wlog.setContainerID(ws.get("wasteContainerID"));
			wlog.setMaterialID(ws.get("wasteMaterialID"));
			wlog.setReasonID(ws.get("wasteReasonID"));
			wlog.setComment("");
			wlog.setProcessOrder(processOrder);
			wlog.setWasteReportTime(JUtility.getSQLDateTime());
			try
			{
				wlog.setWeightKg(new BigDecimal(quantity.isEmpty() ? "0" : quantity));
			}
			catch (NumberFormatException e)
			{
				return lists(ws, ApiResponse.fail("Invalid quantity [" + quantity + "]"));
			}

			if (wlog.write())
			{
				message = "Log " + wlog.getTransactionRef() + " created.";
				ws.set("wasteQuantity", ".000");
			}
			else
			{
				message = wlog.getErrorMessage();
				ws.set("wasteQuantity", quantity);
			}
			ws.set("wasteProcessOrder", wlog.getProcessOrder());
		}

		return lists(ws, ApiResponse.ok().withMessage(message));
	}
}
