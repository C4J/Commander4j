package com.commander4j.web.api;

/**
 * Despatch screens #18-#21 (module FRM_ADMIN_DESPATCH), one path per Process method:
 *
 *   GET /api/despatch/list                      despatchMenuDisplay: page of unconfirmed despatches (core getDespatches)
 *   PUT /api/despatch/page   {dir: prev|next}   PreviousPage / NextPage  (returns the new list)
 *   PUT /api/despatch/create                    despatchDataCreateNew + retrieve  -> despatchHeader
 *   PUT /api/despatch/amend  {despatchNo}       retrieve + despatchCheckUser      -> despatchHeader | message
 *   PUT /api/despatch/exit                      despatchSelect Exit               -> menu
 *   GET /api/despatch/header                    header fields + location list
 *   PUT /api/despatch/header {action, despatchFromLocation, despatchToLocation, despatchTrailer, despatchHaulier, despatchLoadNo, despatchJourneyRef}
 *                                               despatchDataSavetoDB then action = addPallets | confirm | print | exit
 *   PUT /api/despatch/pallets {sscc, addRemoveMode}   assign / unassign SSCC
 *   PUT /api/despatch/pallets/cancel            retrieve -> despatchHeader | despatchSelect
 *   PUT /api/despatch/confirm {yes, printSTNonConfirm}   Yes: confirm (+ print) -> despatchSelect;  No -> despatchHeader
 *
 * Session keys are the ones Process used, so despatchListSize (set at logon), despatchNo,
 * currentDespatchListPage etc. behave the same.
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBReportRequest;
import com.commander4j.html.JMenuRFDespatchList;
import com.commander4j.util.JUtility;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/despatch/*" })
public class DespatchServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_SELECT = "despatchSelect";
	public static final String PAGE_HEADER = "despatchHeader";
	public static final String PAGE_PALLET = "despatchPallet";
	public static final String PAGE_CONFIRM = "despatchConfirm";

	@Override
	protected String moduleId()
	{
		return "FRM_ADMIN_DESPATCH";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			if (path.equals("/list"))
			{
				return list(ws);
			}
			if (path.equals("/header"))
			{
				return header(ws);
			}
		}
		if (method.equals("PUT"))
		{
			switch (path)
			{
				case "/page":
					return page(ws, str(body, "dir"));
				case "/create":
					return create(ws);
				case "/amend":
					return amend(ws, str(body, "despatchNo"));
				case "/exit":
					ws.set("sscc", "");
					ws.set("despatchNo", "");
					return ApiResponse.goTo(SessionServlet.PAGE_MENU);
				case "/header":
					return saveHeader(ws, body);
				case "/pallets":
					return pallets(ws, str(body, "sscc"), str(body, "addRemoveMode"));
				case "/pallets/cancel":
					ws.set("sscc", "");
					return retrieve(ws, ws.get("despatchNo")) ? ApiResponse.goTo(PAGE_HEADER) : ApiResponse.goTo(PAGE_SELECT);
				case "/confirm":
					return confirm(ws, body.has("yes") && body.get("yes").getAsBoolean(), body.has("printSTNonConfirm") && body.get("printSTNonConfirm").getAsBoolean());
				default:
					break;
			}
		}
		return ApiResponse.fail("Unknown despatch action " + method + " " + path);
	}

	// ---- despatchSelect list  (Process.despatchMenuDisplay) -----------------------

	private ApiResponse list(WebSession ws)
	{
		ws.set("sscc", "");
		int currentPage = toInt(ws.get("currentDespatchListPage"), 0);
		int listSize = toInt(ws.get("despatchListSize"), 8);

		JMenuRFDespatchList dl = new JMenuRFDespatchList(ws.getSelectedHost(), ws.getId());
		List<JDBDespatch> page = dl.getDespatches("Unconfirmed", ws.get("despatchNo"), currentPage, listSize);

		ws.set("currentDespatchListPage", String.valueOf(dl.getReturnedPage()));
		ws.set("maxDespatchPages", String.valueOf(dl.getMaxPages()));

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		int x = 0;
		for (JDBDespatch d : page)
		{
			Map<String, Object> r = new LinkedHashMap<String, Object>();
			r.put("despatchNo", d.getDespatchNo());
			r.put("trailer", JUtility.replaceNullStringwithBlank(d.getTrailer()));
			r.put("selected", x == Math.max(dl.getCheckedIndex(), 0) && page.size() > 0);
			rows.add(r);
			x++;
		}
		return ApiResponse.ok().put("page", dl.getReturnedPage()).put("maxPages", dl.getMaxPages()).put("despatches", rows);
	}

	private ApiResponse page(WebSession ws, String dir)
	{
		int currentPage = toInt(ws.get("currentDespatchListPage"), 0);
		currentPage = dir.equals("prev") ? currentPage - 1 : currentPage + 1;
		if (currentPage <= 0)
		{
			currentPage = 1;
		}
		ws.set("currentDespatchListPage", String.valueOf(currentPage));
		ws.set("despatchNo", "");
		return list(ws);
	}

	// ---- Create  (despatchDataCreateNew + despatchDataRetrieveFromDB) --------------

	private ApiResponse create(WebSession ws)
	{
		ws.set("despatchNo", "");
		JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
		String number = desp.generateNewDespatchNo();

		if (number.isEmpty())
		{
			return ApiResponse.fail(desp.getErrorMessage());
		}
		desp.setDespatchNo(number);
		if (desp.create() == false)
		{
			return ApiResponse.fail(desp.getErrorMessage());
		}
		desp.updateUserID(number, ws.get("username").toUpperCase());
		logger.debug("Despatch " + number + " created.");
		ws.set("despatchNo", number);
		clearHeaderFields(ws);

		return retrieve(ws, number) ? ApiResponse.goTo(PAGE_HEADER) : ApiResponse.fail(ws.get("_despatchError"));
	}

	// ---- Amend  (retrieve + despatchCheckUser) --------------------------------------

	private ApiResponse amend(WebSession ws, String selected)
	{
		String despNo = selected.trim().split("[ ]+")[0];
		ws.set("despatchNo", despNo);
		logger.debug("Selected despatch no = [" + despNo + "]");

		if (despNo.isEmpty())
		{
			return ApiResponse.ok();
		}
		if (retrieve(ws, despNo) == false)
		{
			return ApiResponse.fail(ws.get("_despatchError"));
		}
		JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
		if (desp.getDespatchProperties(despNo))
		{
			String assignedUser = desp.getUserID();
			if (assignedUser.equals(ws.get("username").toUpperCase()) == false)
			{
				return ApiResponse.fail("Despatch is assigned to " + assignedUser);
			}
		}
		return ApiResponse.goTo(PAGE_HEADER);
	}

	// ---- header  (despatchDataRetrieveFromDB / despatchHeader) ----------------------

	private void clearHeaderFields(WebSession ws)
	{
		for (String k : new String[] { "despatchFromLocation", "despatchToLocation", "despatchTrailer", "despatchHaulier", "despatchLoadNo", "despatchJourneyRef" })
		{
			ws.set(k, "");
		}
		ws.set("despatchPalletCount", "0");
	}

	/** Process.despatchDataRetrieveFromDB; on failure the message is left in _despatchError */
	private boolean retrieve(WebSession ws, String despNo)
	{
		ws.set("despatchNo", despNo);
		ws.set("_despatchError", "");
		if (despNo.isEmpty())
		{
			return false;
		}
		JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
		if (desp.getDespatchProperties(despNo) == false)
		{
			clearHeaderFields(ws);
			ws.set("_despatchError", desp.getErrorMessage());
			return false;
		}
		ws.set("despatchFromLocation", desp.getLocationIDFrom());
		ws.set("despatchToLocation", desp.getLocationIDTo());
		ws.set("despatchTrailer", desp.getTrailer());
		ws.set("despatchHaulier", desp.getHaulier());
		ws.set("despatchLoadNo", desp.getLoadNo());
		ws.set("despatchJourneyRef", desp.getJourneyRef());
		ws.set("despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()));
		return true;
	}

	private ApiResponse header(WebSession ws)
	{
		List<String> locations = new ArrayList<String>();
		for (JDBLocation l : new JDBLocation(ws.getSelectedHost(), ws.getId()).getLocationList())
		{
			locations.add(l.getLocationID());
		}
		ApiResponse r = ApiResponse.ok().put("locations", locations);
		for (String k : new String[] { "despatchNo", "despatchFromLocation", "despatchToLocation", "despatchTrailer", "despatchHaulier", "despatchLoadNo", "despatchJourneyRef", "despatchPalletCount" })
		{
			r.put(k, ws.get(k));
		}
		return r;
	}

	/** Process.despatchDataSavetoDB; returns null when saved, else the failure response */
	private ApiResponse save(WebSession ws, JsonObject body)
	{
		String despNo = ws.get("despatchNo");
		ws.set("despatchFromLocation", str(body, "despatchFromLocation").toUpperCase());
		ws.set("despatchToLocation", str(body, "despatchToLocation").toUpperCase());
		ws.set("despatchTrailer", str(body, "despatchTrailer").toUpperCase());
		ws.set("despatchHaulier", str(body, "despatchHaulier").toUpperCase());
		ws.set("despatchLoadNo", str(body, "despatchLoadNo").toUpperCase());
		ws.set("despatchJourneyRef", str(body, "despatchJourneyRef"));
		logger.debug("Updating Despatch No " + despNo);

		JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
		if (desp.getDespatchProperties(despNo) == false)
		{
			return ApiResponse.fail(desp.getErrorMessage());
		}
		if (desp.getStatus().equals("Unconfirmed"))
		{
			ws.set("despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()));
			desp.setLocationIDTo(ws.get("despatchToLocation"));
			desp.setLocationIDFrom(ws.get("despatchFromLocation"));
			desp.setTrailer(ws.get("despatchTrailer"));
			desp.setHaulier(ws.get("despatchHaulier"));
			desp.setLoadNo(ws.get("despatchLoadNo"));
			desp.setJourneyRef(ws.get("despatchJourneyRef"));
			desp.setDespatchDate(JUtility.getSQLDateTime());
			if (desp.update() == false)
			{
				return ApiResponse.fail(desp.getErrorMessage());
			}
			logger.debug("Despatch " + despNo + " updated.");
		}
		return null;
	}

	private ApiResponse saveHeader(WebSession ws, JsonObject body)
	{
		ApiResponse failed = save(ws, body);
		if (failed != null)
		{
			return failed;
		}
		switch (str(body, "action"))
		{
			case "addPallets":
				if (ws.get("despatchFromLocation").isEmpty())
				{
					return ApiResponse.fail("Select FROM Location");
				}
				if (ws.get("despatchToLocation").isEmpty())
				{
					return ApiResponse.fail("Select TO Location");
				}
				return ApiResponse.goTo(PAGE_PALLET);
			case "confirm":
				return ApiResponse.goTo(PAGE_CONFIRM);
			case "print":
				print(ws);
				return ApiResponse.ok();
			case "exit":
				return ApiResponse.goTo(PAGE_SELECT);
			default:
				return ApiResponse.fail("Unknown header action");
		}
	}

	// ---- despatchPallet  (Process.despatchPallet) -----------------------------------

	private ApiResponse pallets(WebSession ws, String scanned, String addRemoveMode)
	{
		ws.set("addRemoveMode", addRemoveMode);
		String message = "";

		if (scanned.isEmpty() == false)
		{
			JEANBarcode bcode = ws.barcode();
			if (bcode.parseBarcodeData(scanned) == false)
			{
				message = "Invalid barcode.";
			}
			else
			{
				String sscc = bcode.getStringforAppID("00");
				if (bcode.isValidSSCCformat(sscc) == false)
				{
					message = "Invalid SSCC format.";
				}
				else
				{
					String despNo = ws.get("despatchNo");
					JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
					if (desp.getDespatchProperties(despNo) == false)
					{
						message = "Despatch not found.";
					}
					else if (addRemoveMode.equals("remove"))
					{
						if (desp.unassignSSCC(sscc))
						{
							logger.debug(sscc + " sscc removed from despatch " + despNo);
							ws.set("despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()));
						}
						else
						{
							message = desp.getErrorMessage();
						}
					}
					else
					{
						if (desp.assignSSCC(sscc))
						{
							logger.debug(sscc + " added to despatch " + despNo);
							ws.set("despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()));
						}
						else
						{
							message = desp.getErrorMessage();
						}
					}
				}
			}
		}
		ws.set("sscc", "");
		return ApiResponse.ok().withMessage(message).put("despatchPalletCount", ws.get("despatchPalletCount"));
	}

	// ---- despatchConfirm  (Process.despatchConfirm) ---------------------------------

	private ApiResponse confirm(WebSession ws, boolean yes, boolean printSTN)
	{
		if (yes == false)
		{
			return ApiResponse.goTo(PAGE_HEADER);
		}
		String despNo = ws.get("despatchNo");
		if (despNo.isEmpty())
		{
			return ApiResponse.goTo(PAGE_SELECT);
		}
		JDBDespatch desp = new JDBDespatch(ws.getSelectedHost(), ws.getId());
		desp.getDespatchProperties(despNo);
		if (desp.confirm() == false)
		{
			logger.debug("Cannot confirm Despatch " + despNo + " - " + desp.getErrorMessage());
			return ApiResponse.fail(desp.getErrorMessage()).withNext(PAGE_HEADER);
		}
		logger.debug("Despatch " + despNo + " Confirmed");
		ws.set("printSTNonConfirm", printSTN ? "on" : "");
		if (printSTN)
		{
			print(ws);
		}
		ws.set("despatchNo", "");
		return ApiResponse.goTo(PAGE_SELECT);
	}

	private void print(WebSession ws)
	{
		JDBReportRequest rr = new JDBReportRequest(ws.getSelectedHost(), ws.getId());
		rr.defineReport("RPT_DESPATCH_SERVICE", "ParameterOnly", ":", "", ws.get("defaultPrinter"), 1);
		rr.addParameter("p_despatch_no", "String", ws.get("despatchNo"));
		rr.create();
	}

	private static int toInt(String s, int dflt)
	{
		try
		{
			return s.isEmpty() ? dflt : Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return dflt;
		}
	}
}
