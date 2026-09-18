package com.commander4j.web.api;

/**
 * Pallet screens #7-#13 of c4j_web_react, one path per Process method:
 *
 *   GET /api/pallets/state                        everything the pallet pages show on load
 *   PUT /api/pallets/confirm          {sscc}      Process.palletConfirm
 *   PUT /api/pallets/confirmPlus/sscc {sscc}      Process.palletConfirmPlusSSCC  -> productionConfirmPlusDU
 *   PUT /api/pallets/confirmPlus/du   {trayDU}    Process.palletConfirmPlusDU    -> productionConfirmPlusSSCC | productionConfirmPlusError
 *   PUT /api/pallets/confirmPlus/reset            error-page Exit / DU Cancel: clear sscc -> productionConfirmPlusSSCC
 *   PUT /api/pallets/confirmPlus/cancel           SSCC-page Cancel: clear validateOrder+sscc -> menu
 *   PUT /api/pallets/delete           {sscc}      Process.palletDelete
 *   PUT /api/pallets/info             {sscc}      Process.palletInformation      -> palletInfoDisplay
 *
 * Messages are the exact strings web_react shows. Every write path checks the
 * module the screen belongs to.
 */

import java.util.LinkedHashMap;
import java.util.Map;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBViewBarcodeValidate;
import com.commander4j.util.JUtility;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/pallets/*" })
public class PalletServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_CONFIRM = "productionConfirm";
	public static final String PAGE_PLUS_SSCC = "productionConfirmPlusSSCC";
	public static final String PAGE_PLUS_DU = "productionConfirmPlusDU";
	public static final String PAGE_PLUS_ERROR = "productionConfirmPlusError";
	public static final String PAGE_DELETE = "palletDelete";
	public static final String PAGE_INFO = "palletInfo";
	public static final String PAGE_INFO_DISPLAY = "palletInfoDisplay";

	private static final String[] STATE_KEYS = { "confirmCount", "deleteCount", "validateOrder", "material", "materialDescription", "materialDU_EAN", "materialDU_VARIANT", "palletGTIN", "palletVariant", "trayGTIN", "trayVariant", "palletGTINColor",
			"palletVariantColor", "trayGTINColor", "trayVariantColor", "validateSSCC", "resultImage", "resultMessage", "sscc", "processOrder", "location", "despatchNo", "batch", "palletStatus", "batchStatus", "quantity", "uom", "dom", "expiry", "description", "lotNumber" };

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET") && path.equals("/state"))
		{
			Map<String, String> data = new LinkedHashMap<String, String>();
			for (String k : STATE_KEYS)
			{
				data.put(k, ws.get(k));
			}
			return ApiResponse.ok(data);
		}

		if (method.equals("PUT") == false)
		{
			return ApiResponse.fail("Method not supported");
		}

		ApiResponse refused;
		switch (path)
		{
			case "/confirm":
				refused = refuseUnlessAllowed(ws, "FRM_PAL_PROD_CONFIRM");
				return refused != null ? refused : confirm(ws, str(body, "sscc"));
			case "/confirmPlus/sscc":
				refused = refuseUnlessAllowed(ws, "FRM_PAL_PROD_CONFIRM+");
				return refused != null ? refused : confirmPlusSSCC(ws, str(body, "sscc"));
			case "/confirmPlus/du":
				refused = refuseUnlessAllowed(ws, "FRM_PAL_PROD_CONFIRM+");
				return refused != null ? refused : confirmPlusDU(ws, str(body, "trayDU"));
			case "/confirmPlus/reset":
				ws.set("sscc", "");
				return ApiResponse.goTo(PAGE_PLUS_SSCC);
			case "/confirmPlus/cancel":
				ws.set("validateOrder", "");
				ws.set("sscc", "");
				return ApiResponse.goTo(SessionServlet.PAGE_MENU);
			case "/delete":
				refused = refuseUnlessAllowed(ws, "FRM_PAL_DELETE");
				return refused != null ? refused : delete(ws, str(body, "sscc"));
			case "/info":
				refused = refuseUnlessAllowed(ws, "FRM_PAL_INFO");
				return refused != null ? refused : info(ws, str(body, "sscc"));
			default:
				return ApiResponse.fail("Unknown pallet action " + method + " " + path);
		}
	}

	/** parse + AI 00 + format check, shared by every SSCC screen; null = ok, else the failing message */
	private String parseSSCC(JEANBarcode bcode, String scanned, String[] ssccOut, boolean plainMessages)
	{
		if (bcode.parseBarcodeData(scanned) == false)
		{
			return plainMessages ? "Invalid barcode." : bcode.getErrorMessage();
		}
		String sscc = bcode.getStringforAppID("00");
		if (bcode.isValidSSCCformat(sscc) == false)
		{
			return plainMessages ? "Invalid SSCC format." : bcode.getErrorMessage();
		}
		ssccOut[0] = sscc;
		return null;
	}

	// ---- productionConfirm  (Process.palletConfirm) ------------------------------

	private ApiResponse confirm(WebSession ws, String scanned)
	{
		String message = "";
		if (scanned.isEmpty() == false)
		{
			String[] out = new String[1];
			message = parseSSCC(ws.barcode(), scanned, out, false);
			if (message == null)
			{
				String sscc = out[0];
				JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
				if (pallet.getPalletProperties(sscc))
				{
					pallet.setDateOfManufacture(JUtility.getSQLDateTime());
					if (pallet.confirm())
					{
						int confirmCount = Integer.valueOf(ws.get("confirmCount").isEmpty() ? "0" : ws.get("confirmCount"));
						confirmCount++;
						ws.set("confirmCount", String.valueOf(confirmCount));
						message = "SSCC " + sscc + " confirmed.";
						logger.debug(sscc + " confirmed. (" + confirmCount + ")");
					}
					else
					{
						message = pallet.getErrorMessage();
					}
				}
				else
				{
					message = "SSCC not found.";
				}
			}
		}
		ws.set("sscc", "");
		return ApiResponse.ok().withMessage(message).put("confirmCount", ws.get("confirmCount"));
	}

	// ---- productionConfirmPlusSSCC  (Process.palletConfirmPlusSSCC) ---------------

	private ApiResponse confirmPlusSSCC(WebSession ws, String scanned)
	{
		if (scanned.isEmpty())
		{
			ws.set("sscc", "");
			return ApiResponse.ok();
		}

		String[] out = new String[1];
		String message = parseSSCC(ws.barcode(), scanned, out, false);
		if (message != null)
		{
			ws.set("sscc", "");
			return ApiResponse.fail(message);
		}
		String sscc = out[0];

		JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
		if (pallet.getPalletProperties(sscc) == false)
		{
			ws.set("sscc", "");
			return ApiResponse.fail("SSCC not found.");
		}
		if (pallet.isConfirmed())
		{
			ws.set("sscc", "");
			return ApiResponse.fail("Pallet already confirmed.");
		}

		JDBViewBarcodeValidate pOrder = new JDBViewBarcodeValidate(ws.getSelectedHost(), ws.getId());
		pOrder.getProperties(pallet.getProcessOrder());

		ws.set("material", pOrder.getMaterial(), false);
		ws.set("materialDescription", pOrder.getDescription(), false);
		ws.set("sscc", String.valueOf(pallet.getSSCC()), false);
		ws.set("validateOrder", String.valueOf(pallet.getProcessOrder()), false);
		ws.set("palletGTIN", pallet.getEAN());
		ws.set("palletVariant", pallet.getVariant());
		ws.set("materialDU_EAN", pOrder.getProdEan());
		ws.set("materialDU_VARIANT", pOrder.getProdVariant());

		return ApiResponse.goTo(PAGE_PLUS_DU);
	}

	// ---- productionConfirmPlusDU  (Process.palletConfirmPlusDU) -------------------

	private ApiResponse confirmPlusDU(WebSession ws, String barcodeData)
	{
		JEANBarcode bcode = ws.barcode();

		if (bcode.parseBarcodeData(barcodeData) == false)
		{
			return ApiResponse.fail("Invalid Barcode. Not EAN128");
		}
		if (bcode.isDataAvailableforAppID("01") == false)
		{
			ws.set("barcodeData", "");
			return ApiResponse.fail("Wrong Barcode.");
		}

		String trayGTIN = bcode.getStringforAppID("01");
		String trayVariant = bcode.getStringforAppID("20");
		ws.set("trayGTIN", trayGTIN);
		ws.set("trayVariant", trayVariant);
		String materialDU_EAN = ws.get("materialDU_EAN");
		String materialDU_Variant = ws.get("materialDU_VARIANT");
		String palletGTIN = ws.get("palletGTIN");
		String palletVariant = ws.get("palletVariant");

		boolean trayGTINCorrect = trayGTIN.equals(materialDU_EAN);
		boolean trayVariantCorrect = trayVariant.equals(materialDU_Variant);
		boolean palletGTINCorrect = palletGTIN.equals(materialDU_EAN);
		boolean palletVariantCorrect = palletVariant.equals(materialDU_Variant);

		if (trayGTINCorrect && trayVariantCorrect && palletGTINCorrect && palletVariantCorrect)
		{
			String sscc = ws.get("sscc");
			JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
			if (pallet.getPalletProperties(sscc) == false)
			{
				return ApiResponse.fail("SSCC not found.").withNext(PAGE_PLUS_SSCC);
			}
			pallet.setDateOfManufacture(JUtility.getSQLDateTime());
			if (pallet.confirm())
			{
				int confirmCount = Integer.valueOf(ws.get("confirmCount").isEmpty() ? "0" : ws.get("confirmCount"));
				confirmCount++;
				ws.set("confirmCount", String.valueOf(confirmCount));
				ws.set("sscc", "");
				return ApiResponse.goTo(PAGE_PLUS_SSCC).withMessage("SSCC " + sscc + " confirmed.");
			}
			// Process shows the pallet error then immediately blanks it; keep the useful version
			ws.set("sscc", "");
			return ApiResponse.fail(pallet.getErrorMessage());
		}

		ws.set("palletGTINColor", palletGTINCorrect ? "green" : "red");
		ws.set("palletVariantColor", palletVariantCorrect ? "green" : "red");
		ws.set("trayGTINColor", trayGTINCorrect ? "green" : "red");
		ws.set("trayVariantColor", trayVariantCorrect ? "green" : "red");
		return ApiResponse.fail("Barcodes inconsistent").withNext(PAGE_PLUS_ERROR);
	}

	// ---- palletDelete  (Process.palletDelete) -------------------------------------

	private ApiResponse delete(WebSession ws, String scanned)
	{
		String message = "";
		if (scanned.isEmpty() == false)
		{
			String[] out = new String[1];
			message = parseSSCC(ws.barcode(), scanned, out, false);
			if (message == null)
			{
				String sscc = out[0];
				JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
				if (pallet.delete(sscc))
				{
					int deleteCount = Integer.valueOf(ws.get("deleteCount").isEmpty() ? "0" : ws.get("deleteCount"));
					deleteCount++;
					ws.set("deleteCount", String.valueOf(deleteCount));
					message = "SSCC " + sscc + " deleted.";
					logger.debug(sscc + " deleted. (" + deleteCount + ")");
				}
				else
				{
					message = pallet.getErrorMessage();
				}
			}
		}
		ws.set("sscc", "");
		return ApiResponse.ok().withMessage(message).put("deleteCount", ws.get("deleteCount"));
	}

	// ---- palletInfo  (Process.palletInformation) ----------------------------------

	private ApiResponse info(WebSession ws, String scanned)
	{
		if (scanned.isEmpty())
		{
			ws.set("sscc", "");
			return ApiResponse.ok();
		}

		String[] out = new String[1];
		String message = parseSSCC(ws.barcode(), scanned, out, true);
		if (message != null)
		{
			ws.set("sscc", "");
			return ApiResponse.fail(message);
		}
		String sscc = out[0];

		JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
		pallet.setDateOfManufacture(JUtility.getSQLDateTime());
		if (pallet.getPalletProperties(sscc) == false)
		{
			ws.set("sscc", "");
			return ApiResponse.fail("SSCC not found.");
		}
		if (pallet.isConfirmed() == false)
		{
			ws.set("sscc", "");
			return ApiResponse.fail("SSCC not confirmed.");
		}

		ws.set("sscc", sscc);
		ws.set("processOrder", pallet.getProcessOrder());
		ws.set("material", pallet.getMaterial());
		ws.set("location", pallet.getLocationID());
		ws.set("despatchNo", pallet.getDespatchNo());
		ws.set("batch", pallet.getBatchNumber());
		ws.set("palletStatus", pallet.getStatus());
		ws.set("batchStatus", pallet.getMaterialBatchStatus());
		ws.set("quantity", String.valueOf(pallet.getQuantity()));
		ws.set("uom", pallet.getUom());
		ws.set("dom", pallet.getDateOfManufacture() == null ? "" : pallet.getDateOfManufacture().toString().substring(0, 16));
		ws.set("expiry", pallet.getMaterialBatchExpiryDate() == null ? "" : pallet.getMaterialBatchExpiryDate().toString().substring(0, 16));
		// the two fields web_Issue's SSCC Info showed that web_react's Pallet Info did not (merged 2026-09-11):
		// material description, and the "lot number" = old material code + SSCC digits 8-18 (web_Issue's formula)
		ws.set("description", pallet.getMaterialObj().getDescription());
		ws.set("lotNumber", JUtility.replaceNullStringwithBlank(pallet.getMaterialObj().getOldMaterial()) + (sscc.length() >= 18 ? sscc.substring(8, 18) : ""));

		return ApiResponse.goTo(PAGE_INFO_DISPLAY);
	}
}
