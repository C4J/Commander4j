package com.commander4j.web.api;

/**
 * Pallet Return (module FRM_PAL_RETURN) - c4j_web_Issue's return flow moved as-is (step 7, 2026-09-11):
 * palletReturnSelect -> processOrderReturnSelect -> palletReturnConfirm. Logic = the copied JQMPalletDB /
 * JQMReturnableDB (web.db), decisions A/B/C of 2026-09-10 included (FROM = the picked (order, location)
 * row, per-row cap, no status check).
 *
 *   PUT /api/return/sscc   {sscc}                       query -> processOrderReturnSelect
 *   GET /api/return/orders                              returnable (order, location, quantity) rows for the SSCC
 *   PUT /api/return/order  {processOrder, location, quantity}   pick a row -> palletReturnConfirm
 *   GET /api/return/pallet                              pallet properties + the row's quantity as the default
 *   PUT /api/return/confirm {quantity}                  return
 *   PUT /api/return/exit                                clear -> menu
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.web.db.JQMPalletDB;
import com.commander4j.web.db.JQMReturnableDB;
import com.commander4j.web.entity.JQMPalletEntity;
import com.commander4j.web.entity.JQMReturnableEntity;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/return/*" })
public class ReturnServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_SSCC = "palletReturnSelect";
	public static final String PAGE_ORDERS = "processOrderReturnSelect";
	public static final String PAGE_CONFIRM = "palletReturnConfirm";

	@Override
	protected String moduleId()
	{
		return "FRM_PAL_RETURN";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			if (path.equals("/orders"))
			{
				return orders(ws);
			}
			if (path.equals("/pallet"))
			{
				return pallet(ws, ApiResponse.ok());
			}
		}
		if (method.equals("PUT"))
		{
			switch (path)
			{
				case "/sscc":
					return sscc(ws, str(body, "sscc"));
				case "/order":
					return selectOrder(ws, str(body, "processOrder"), str(body, "location"), str(body, "quantity"));
				case "/confirm":
					return confirm(ws, str(body, "quantity"));
				case "/exit":
					IssueServlet.clearIssueState(ws);
					return ApiResponse.goTo(SessionServlet.PAGE_MENU);
				default:
					break;
			}
		}
		return ApiResponse.fail("Unknown return action " + method + " " + path);
	}

	/** web_Issue returnSSCCSelect -> querySSCC(Pallets?action=query) -> processOrderReturnSelect */
	private ApiResponse sscc(WebSession ws, String scanned)
	{
		String[] err = new String[1];
		String sscc = normaliseSSCC(ws, scanned, err);
		if (sscc == null)
		{
			return ApiResponse.fail(err[0]);
		}
		JQMPalletDB pdb = new JQMPalletDB(ws.getSelectedHost(), ws.getId());
		if (pdb.isValid(sscc) == false)
		{
			return ApiResponse.fail("Invalid SSCC " + sscc);
		}
		ws.set("selectedSSCC", sscc);
		ws.set("selectedProcessOrder", "");
		ws.set("selectedReturnLocation", "");
		ws.set("selectedReturnQuantity", "");
		return ApiResponse.goTo(PAGE_ORDERS);
	}

	/** web_Issue buildOrderTable (Returnable?sscc=) */
	private ApiResponse orders(WebSession ws)
	{
		String sscc = ws.get("selectedSSCC");
		String selOrder = ws.get("selectedProcessOrder"), selLoc = ws.get("selectedReturnLocation");
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		for (JQMReturnableEntity r : new JQMReturnableDB(ws.getSelectedHost(), ws.getId()).getReturnableBySSCC(sscc))
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("processOrder", r.getProcessOrderID());
			m.put("location", r.getLocationID());
			m.put("quantity", r.getQuantity() == null ? "0" : r.getQuantity().toPlainString());
			m.put("uom", r.getUom());
			m.put("selected", r.getProcessOrderID().equals(selOrder) && r.getLocationID().equals(selLoc));
			rows.add(m);
		}
		String message = rows.isEmpty() ? "No orders with returnable quantity." : rows.size() + " order(s) available for return.";
		return ApiResponse.ok().withMessage(message).put("username", userId(ws)).put("sscc", sscc).put("orders", rows);
	}

	private ApiResponse selectOrder(WebSession ws, String order, String location, String quantity)
	{
		if (order.isEmpty())
		{
			return ApiResponse.fail("Select an order.");
		}
		// take the quantity from the returnable rows, not the client
		String qty = quantity;
		for (JQMReturnableEntity r : new JQMReturnableDB(ws.getSelectedHost(), ws.getId()).getReturnableBySSCC(ws.get("selectedSSCC")))
		{
			if (r.getProcessOrderID().equals(order) && r.getLocationID().equals(location))
			{
				qty = r.getQuantity().toPlainString();
			}
		}
		ws.set("selectedProcessOrder", order);
		ws.set("selectedReturnLocation", location);
		ws.set("selectedReturnQuantity", qty);
		return ApiResponse.goTo(PAGE_CONFIRM);
	}

	private ApiResponse pallet(WebSession ws, ApiResponse r)
	{
		String sscc = ws.get("selectedSSCC");
		JQMPalletDB pdb = new JQMPalletDB(ws.getSelectedHost(), ws.getId());
		JQMPalletEntity p = pdb.getProperties(sscc);
		if (pdb.getErrorMessage() != null && pdb.getErrorMessage().isEmpty() == false)
		{
			return ApiResponse.fail(pdb.getErrorMessage()).withNext(PAGE_SSCC);
		}
		r.put("username", userId(ws)).put("processOrder", ws.get("selectedProcessOrder")).put("returnLocation", ws.get("selectedReturnLocation")).put("returnQuantity", ws.get("selectedReturnQuantity"));
		r.put("sscc", p.getSSCC()).put("palletProcessOrder", p.getProcessOrder()).put("material", p.getMaterial()).put("description", p.getDescription()).put("palletStatus", p.getPalletStatus()).put("batchNumber", p.getBatchNumber()).put("batchStatus", p.getBatchStatus());
		r.put("quantity", p.getQuantity() == null ? "0" : p.getQuantity().toPlainString()).put("uom", p.getUom()).put("bomId", p.getBomdId()).put("bomVersion", p.getBomVersion()).put("lotNumber", (p.getOldMaterial() == null ? "" : p.getOldMaterial()) + (sscc.length() >= 18 ? sscc.substring(8, 18) : "")).put("location", p.getLocationId());
		return r;
	}

	/** web_Issue preReturnChecks -> returnSSCC (Pallets?action=return) */
	private ApiResponse confirm(WebSession ws, String quantityText)
	{
		String sscc = ws.get("selectedSSCC"), order = ws.get("selectedProcessOrder"), location = ws.get("selectedReturnLocation");
		BigDecimal quantity;
		try
		{
			quantity = new BigDecimal(quantityText.isEmpty() ? "0" : quantityText);
		}
		catch (NumberFormatException e)
		{
			return pallet(ws, ApiResponse.fail("Invalid quantity [" + quantityText + "]"));
		}
		JQMPalletDB pdb = new JQMPalletDB(ws.getSelectedHost(), ws.getId());
		Long txn;
		try
		{
			txn = pdb.returnFromOrder_rest(sscc, order, quantity, location, userId(ws));
		}
		catch (RuntimeException e)
		{
			logger.error("return failed for SSCC [" + sscc + "]", e);
			return pallet(ws, ApiResponse.fail("Return from order failed - check SSCC [" + sscc + "]"));
		}
		if (txn == null || txn <= 0)
		{
			String msg = pdb.getErrorMessage();
			return pallet(ws, ApiResponse.fail(msg == null || msg.isEmpty() ? "Return from order failed." : msg));
		}
		logger.info("Returned " + quantity.toPlainString() + " to " + sscc + " from " + order + " at " + location + " by " + userId(ws) + " txn " + txn);
		ws.set("selectedReturnQuantity", "");   // web_Issue clears it after a successful return
		return pallet(ws, ApiResponse.ok().withMessage(quantity.toPlainString() + " " + pdb.getProperties(sscc).getUom() + " returned to " + sscc));
	}
}
