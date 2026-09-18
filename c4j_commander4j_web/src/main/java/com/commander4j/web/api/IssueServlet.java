package com.commander4j.web.api;

/**
 * Pallet Issue (module FRM_PAL_ISSUE) - c4j_web_Issue's issue flow moved as-is (step 7, 2026-09-11):
 * processOrderIssueSelect -> palletIssueSelect -> palletIssueConfirm. The business logic is the copied
 * JQMPalletDB / JQMViewBomDB / JQMProcessOrderDB classes (web.db); this servlet only replaces
 * web_Issue's controllers + sessionStorage with one endpoint per page step and HttpSession state.
 *
 *   GET /api/issue/orders                 resources + Ready orders for the selected resource   (processOrderIssueSelect)
 *   PUT /api/issue/resource {resource}    change resource (clears the order)
 *   PUT /api/issue/order    {processOrder}  select order -> palletIssueSelect
 *   GET /api/issue/bom                    stages for the order's BOM (+ materials of the selected stage)   (palletIssueSelect)
 *   PUT /api/issue/stage    {stage}       change stage -> materials
 *   PUT /api/issue/sscc     {sscc}        validateMaterial then query -> palletIssueConfirm
 *   GET /api/issue/pallet                 the selected pallet's properties + default issue quantity     (palletIssueConfirm)
 *   PUT /api/issue/confirm  {location, quantity}   validateLocation then issue (one request; web_Issue fired
 *                                         validateLocation, issue and a query in parallel)
 *   PUT /api/issue/exit                   clear the flow's state -> menu
 *
 * Bridge pulses are exactly web_Issue's four: the scanned location on a wrong-lane refusal,
 * INVALID_MATERIAL, INVALID_QUANTITY (zero pallet quantity at either validation, or the issue
 * quantity guard), INVALID_STATUS (via JQMPalletDB.getBridgePulseId on the issue failure branch).
 * The user id written to the pallet and history rows is the logged-on user (web_Issue trusted the
 * username the browser sent).
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBProcessOrder;
import com.commander4j.web.db.JQMPalletDB;
import com.commander4j.web.db.JQMProcessOrderDB;
import com.commander4j.web.db.JQMResourcesDB;
import com.commander4j.web.db.JQMViewBomDB;
import com.commander4j.web.entity.JQMPalletEntity;
import com.commander4j.web.entity.JQMProcessOrderEntity;
import com.commander4j.web.entity.JQMResourceEntity;
import com.commander4j.web.entity.JQMViewBOMEntity;
import com.commander4j.web.session.WebSession;
import com.commander4j.web.util.JQMBridgeClient;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/issue/*" })
public class IssueServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_ORDERS = "processOrderIssueSelect";
	public static final String PAGE_SSCC = "palletIssueSelect";
	public static final String PAGE_CONFIRM = "palletIssueConfirm";

	/** web_Issue's sessionStorage keys, now HttpSession attributes (shared with ReturnServlet). */
	static final String[] STATE_KEYS = { "selectedResource", "selectedProcessOrder", "selectedProcessOrderMaterial", "selectedBOMId", "selectedBOMVersion", "selectedStage", "selectedSSCC", "selectedReturnQuantity", "selectedReturnLocation" };

	static void clearIssueState(WebSession ws)
	{
		for (String k : STATE_KEYS)
		{
			ws.set(k, "");
		}
	}

	@Override
	protected String moduleId()
	{
		return "FRM_PAL_ISSUE";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			switch (path)
			{
				case "/orders":
					return orders(ws);
				case "/bom":
					return bom(ws);
				case "/pallet":
					return pallet(ws, ApiResponse.ok());
				default:
					break;
			}
		}
		if (method.equals("PUT"))
		{
			switch (path)
			{
				case "/resource":
					ws.set("selectedResource", str(body, "resource"));
					ws.set("selectedProcessOrder", "");
					ws.set("selectedProcessOrderMaterial", "");
					ws.set("selectedBOMId", "");
					ws.set("selectedBOMVersion", "");
					return orders(ws);
				case "/order":
					return selectOrder(ws, str(body, "processOrder"));
				case "/stage":
					ws.set("selectedStage", str(body, "stage"));
					return bom(ws);
				case "/sscc":
					return sscc(ws, str(body, "sscc"));
				case "/confirm":
					return confirm(ws, str(body, "location"), str(body, "quantity"));
				case "/exit":
					clearIssueState(ws);
					return ApiResponse.goTo(SessionServlet.PAGE_MENU);
				default:
					break;
			}
		}
		return ApiResponse.fail("Unknown issue action " + method + " " + path);
	}

	// ---- processOrderIssueSelect --------------------------------------------------------

	private ApiResponse orders(WebSession ws)
	{
		String host = ws.getSelectedHost(), sid = ws.getId();
		String resource = ws.get("selectedResource");
		String selected = ws.get("selectedProcessOrder");

		List<Map<String, Object>> resources = new ArrayList<Map<String, Object>>();
		for (JQMResourceEntity r : new JQMResourcesDB(host, sid).getResources())
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("resource", r.getRequiredResource());
			m.put("description", r.getDescription());
			resources.add(m);
		}

		JQMProcessOrderDB odb = new JQMProcessOrderDB(host, sid);
		List<JQMProcessOrderEntity> list = resource.isEmpty() ? odb.getProcessOrdersByStatus("Ready") : odb.getProcessOrdersByStatusByResource("Ready", resource);
		List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
		for (JQMProcessOrderEntity o : list)
		{
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			m.put("processOrder", o.getProcessOrderID());
			m.put("material", o.getMaterial());
			m.put("description", o.getDescription());
			m.put("selected", o.getProcessOrderID().equals(selected));
			orders.add(m);
		}
		return ApiResponse.ok().put("resources", resources).put("selectedResource", resource).put("orders", orders).put("selectedProcessOrder", selected).put("username", userId(ws));
	}

	private ApiResponse selectOrder(WebSession ws, String order)
	{
		if (order.isEmpty())
		{
			return ApiResponse.fail("Select an order.");
		}
		JQMProcessOrderDB odb = new JQMProcessOrderDB(ws.getSelectedHost(), ws.getId());
		List<JQMProcessOrderEntity> rows = odb.getProcessOrderByID(order);
		if (rows.isEmpty())
		{
			return ApiResponse.fail("Unknown Process Order [" + order + "]");
		}
		JQMProcessOrderEntity o = rows.get(0);
		ws.set("selectedProcessOrder", o.getProcessOrderID());
		ws.set("selectedProcessOrderMaterial", o.getMaterial());
		ws.set("selectedBOMId", o.getBomID());
		ws.set("selectedBOMVersion", o.getBomVersion());
		ws.set("selectedStage", "");
		ws.set("selectedSSCC", "");
		return ApiResponse.goTo(PAGE_SSCC);
	}

	// ---- palletIssueSelect ----------------------------------------------------------------

	private ApiResponse bom(WebSession ws)
	{
		String host = ws.getSelectedHost(), sid = ws.getId();
		String bomId = ws.get("selectedBOMId"), bomVersion = ws.get("selectedBOMVersion");
		JQMViewBomDB bdb = new JQMViewBomDB(host, sid);

		List<String> stages = new ArrayList<String>();
		for (JQMViewBOMEntity s : bdb.getStagesForBOM(bomId, bomVersion))
		{
			stages.add(s.getStage());
		}
		String stage = ws.get("selectedStage");
		if ((stage.isEmpty() || stages.contains(stage) == false) && stages.isEmpty() == false)
		{
			stage = stages.get(0);   // web_Issue: first stage is selected when none is
			ws.set("selectedStage", stage);
		}

		List<Map<String, Object>> materials = new ArrayList<Map<String, Object>>();
		for (JQMViewBOMEntity m : bdb.getValidMaterialsForBOM(bomId, bomVersion, stage, "input"))
		{
			Map<String, Object> r = new LinkedHashMap<String, Object>();
			r.put("material", m.getMaterial());
			r.put("description", m.getDescription());
			r.put("location", m.getLocation_id());
			materials.add(r);
		}
		return ApiResponse.ok().put("username", userId(ws)).put("processOrder", ws.get("selectedProcessOrder")).put("bomId", bomId).put("bomVersion", bomVersion).put("stages", stages).put("selectedStage", stage).put("materials", materials);
	}

	/** web_Issue: validateSSCCMaterial (Pallets?action=validateMaterial) then querySSCC -> palletIssueConfirm */
	private ApiResponse sscc(WebSession ws, String scanned)
	{
		String[] err = new String[1];
		String sscc = normaliseSSCC(ws, scanned, err);
		if (sscc == null)
		{
			return ApiResponse.fail(err[0]);
		}
		String host = ws.getSelectedHost(), sid = ws.getId();
		String order = ws.get("selectedProcessOrder"), stage = ws.get("selectedStage");

		JDBPallet pallet = new JDBPallet(host, sid);
		if (pallet.getPalletProperties(sscc) == false)
		{
			return ApiResponse.fail(pallet.getErrorMessage());
		}
		JDBProcessOrder po = new JDBProcessOrder(host, sid);
		if (po.getProcessOrderProperties(order) == false)
		{
			return ApiResponse.fail(po.getErrorMessage());
		}
		if (pallet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
		{
			JQMBridgeClient.pulseAsync("INVALID_QUANTITY");
			return ApiResponse.fail("Pallet Quantity is ZERO");
		}
		JQMViewBomDB bdb = new JQMViewBomDB(host, sid);
		if (bdb.isMaterialValidForBOM(po.getRecipe(), po.getRecipeVersion(), stage, "input", pallet.getMaterial()) == false)
		{
			JQMBridgeClient.pulseAsync("INVALID_MATERIAL");
			return ApiResponse.fail("Material [" + pallet.getMaterial() + "] is not valid for order [" + po.getProcessOrder() + "]");
		}
		// querySSCC: JQMPalletDB.isValid - already proven by getPalletProperties above
		ws.set("selectedSSCC", sscc);
		return ApiResponse.goTo(PAGE_CONFIRM);
	}

	// ---- palletIssueConfirm ----------------------------------------------------------------

	/** web_Issue getPalletProperties (Pallets?action=query) + the page's derived fields */
	private ApiResponse pallet(WebSession ws, ApiResponse r)
	{
		String sscc = ws.get("selectedSSCC");
		JQMPalletDB pdb = new JQMPalletDB(ws.getSelectedHost(), ws.getId());
		JQMPalletEntity p = pdb.getProperties(sscc);
		if (pdb.getErrorMessage() != null && pdb.getErrorMessage().isEmpty() == false)
		{
			return ApiResponse.fail(pdb.getErrorMessage()).withNext(PAGE_SSCC);
		}
		r.put("username", userId(ws)).put("processOrder", ws.get("selectedProcessOrder")).put("bomId", ws.get("selectedBOMId")).put("bomVersion", ws.get("selectedBOMVersion")).put("stage", ws.get("selectedStage"));
		r.put("sscc", p.getSSCC()).put("palletProcessOrder", p.getProcessOrder()).put("material", p.getMaterial()).put("description", p.getDescription()).put("palletStatus", p.getPalletStatus()).put("batchNumber", p.getBatchNumber()).put("batchStatus", p.getBatchStatus());
		r.put("quantity", p.getQuantity() == null ? "0" : p.getQuantity().toPlainString()).put("uom", p.getUom()).put("lotNumber", (p.getOldMaterial() == null ? "" : p.getOldMaterial()) + (sscc.length() >= 18 ? sscc.substring(8, 18) : "")).put("location", p.getLocationId());
		r.put("issueQuantity", p.getQuantity() == null ? "0" : p.getQuantity().toPlainString());   // web_Issue defaults the issue quantity to the pallet quantity
		return r;
	}

	/** web_Issue preIssueChecks: validateSSCCLocation (Pallets?action=validateLocation) then issueSSCC (action=issue) */
	private ApiResponse confirm(WebSession ws, String location, String quantityText)
	{
		String host = ws.getSelectedHost(), sid = ws.getId();
		String sscc = ws.get("selectedSSCC"), order = ws.get("selectedProcessOrder"), stage = ws.get("selectedStage");
		String user = userId(ws);

		// --- validateLocation (as JQMPalletController) ---
		JDBPallet pallet = new JDBPallet(host, sid);
		if (pallet.getPalletProperties(sscc) == false)
		{
			return pallet(ws, ApiResponse.fail(pallet.getErrorMessage()));
		}
		JDBProcessOrder po = new JDBProcessOrder(host, sid);
		if (po.getProcessOrderProperties(order) == false)
		{
			return pallet(ws, ApiResponse.fail(po.getErrorMessage()));
		}
		if (pallet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
		{
			JQMBridgeClient.pulseAsync("INVALID_QUANTITY");
			return pallet(ws, ApiResponse.fail("Pallet Quantity is ZERO"));
		}
		JQMViewBomDB bdb = new JQMViewBomDB(host, sid);
		if (bdb.isValidMaterialForLocation(po.getRecipe(), po.getRecipeVersion(), stage, "input", pallet.getMaterial(), location) == false)
		{
			JQMBridgeClient.pulseAsync(location);
			return pallet(ws, ApiResponse.fail("Material [" + pallet.getMaterial() + "] is not valid for Location [" + location + "]"));
		}

		// --- issue (as JQMPalletController action=issue) ---
		BigDecimal quantity;
		try
		{
			quantity = new BigDecimal(quantityText.isEmpty() ? "0" : quantityText);
		}
		catch (NumberFormatException e)
		{
			return pallet(ws, ApiResponse.fail("Invalid quantity [" + quantityText + "]"));
		}
		JQMPalletDB pdb = new JQMPalletDB(host, sid);
		Long txn;
		try
		{
			txn = pdb.issueToOrder_rest(sscc, order, quantity, location, user);
		}
		catch (RuntimeException e)
		{
			logger.error("issue failed for SSCC [" + sscc + "]", e);
			return pallet(ws, ApiResponse.fail("Issue to order failed - check SSCC [" + sscc + "]"));
		}
		if (txn == null || txn <= 0)
		{
			String msg = pdb.getErrorMessage();
			if (msg == null || msg.isEmpty())
			{
				msg = "Issue to order failed.";
			}
			if (pdb.getBridgePulseId().isEmpty() == false)
			{
				JQMBridgeClient.pulseAsync(pdb.getBridgePulseId());
			}
			return pallet(ws, ApiResponse.fail(msg));
		}
		logger.info("Issued " + quantity.toPlainString() + " from " + sscc + " to " + order + " at " + location + " by " + user + " txn " + txn);
		return pallet(ws, ApiResponse.ok().withMessage(quantity.toPlainString() + " " + pallet.getUom() + " from " + sscc + " issued."));
	}
}
