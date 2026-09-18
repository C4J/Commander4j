package com.commander4j.web.api;

/**
 * Barcode Validation screens #14-#17 (module FRM_BARCODE_VALIDATE):
 *
 *   PUT /api/validate/select {validateOrder, validateSSCC}  Process.validateDUSelect -> validateDUPallet
 *   PUT /api/validate/pallet {palletDU}                     Process.validateDUPallet -> validateDUTray
 *   PUT /api/validate/tray   {trayDU}                       Process.validateDUTray   -> validateDUResult
 *   PUT /api/validate/cancel {to: "menu" | "select"}        select Cancel -> menu; pallet Cancel -> select (both clear order + sscc)
 *
 * Page state comes from GET /api/pallets/state (same session keys as web_react).
 * Result Exit and tray Cancel are client-only navigation (Process only blanked the message).
 */

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBViewBarcodeValidate;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/validate/*" })
public class ValidateServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	public static final String PAGE_SELECT = "validateDUSelect";
	public static final String PAGE_PALLET = "validateDUPallet";
	public static final String PAGE_TRAY = "validateDUTray";
	public static final String PAGE_RESULT = "validateDUResult";

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("PUT") == false)
		{
			return ApiResponse.fail("Method not supported");
		}
		ApiResponse refused = refuseUnlessAllowed(ws, "FRM_BARCODE_VALIDATE");
		if (refused != null)
		{
			return refused;
		}
		switch (path)
		{
			case "/select":
				return select(ws, str(body, "validateOrder"), str(body, "validateSSCC"));
			case "/pallet":
				return pallet(ws, str(body, "palletDU"));
			case "/tray":
				return tray(ws, str(body, "trayDU"));
			case "/cancel":
				ws.set("validateOrder", "");
				ws.set("validateSSCC", "");
				return ApiResponse.goTo(str(body, "to").equals("select") ? PAGE_SELECT : SessionServlet.PAGE_MENU);
			default:
				return ApiResponse.fail("Unknown validate action " + method + " " + path);
		}
	}

	// ---- validateDUSelect ---------------------------------------------------------

	private ApiResponse select(WebSession ws, String validateOrder, String validateSSCC)
	{
		ws.set("validateOrder", validateOrder);
		ws.set("validateSSCC", validateSSCC);

		if (validateSSCC.isEmpty() == false)
		{
			JEANBarcode bcode = ws.barcode();
			if (bcode.parseBarcodeData(validateSSCC) && bcode.isDataAvailableforAppID("00"))
			{
				JDBPallet pallet = new JDBPallet(ws.getSelectedHost(), ws.getId());
				if (pallet.getPalletProperties(bcode.getStringforAppID("00")))
				{
					validateOrder = pallet.getProcessOrder();
				}
			}
		}

		if (validateOrder.isEmpty())
		{
			ws.set("validateOrder", "");
			ws.set("validateSSCC", "");
			return ApiResponse.ok();
		}

		JDBViewBarcodeValidate pOrder = new JDBViewBarcodeValidate(ws.getSelectedHost(), ws.getId());
		if (pOrder.getProperties(validateOrder) == false)
		{
			ws.set("validateOrder", "");
			ws.set("validateSSCC", "");
			return ApiResponse.fail(pOrder.getErrorMessage());
		}

		ws.set("validateOrder", String.valueOf(validateOrder), false);
		ws.set("material", pOrder.getMaterial(), false);
		ws.set("materialDU_UOM", pOrder.getProdUom(), false);
		ws.set("materialDescription", pOrder.getDescription(), false);
		ws.set("materialCU_UOM", pOrder.getBaseUom(), false);
		ws.set("materialDU_EAN", pOrder.getProdEan());
		ws.set("materialDU_VARIANT", pOrder.getProdVariant());
		ws.set("materialCU_EAN", pOrder.getBaseEan());
		ws.set("materialCU_VARIANT", pOrder.getBaseVariant());
		return ApiResponse.goTo(PAGE_PALLET);
	}

	// ---- validateDUPallet ---------------------------------------------------------

	private ApiResponse pallet(WebSession ws, String barcodeData)
	{
		JEANBarcode bcode = ws.barcode();
		if (bcode.parseBarcodeData(barcodeData) == false)
		{
			return ApiResponse.fail("Invalid Barcode. Not EAN128");
		}
		if (bcode.isDataAvailableforAppID("02") == false)
		{
			return ApiResponse.fail("Wrong Barcode.");
		}
		ws.set("palletGTIN", bcode.getStringforAppID("02"));
		ws.set("palletVariant", bcode.getStringforAppID("20"));
		return ApiResponse.goTo(PAGE_TRAY).withMessage("Pallet DU scanned.");
	}

	// ---- validateDUTray -----------------------------------------------------------

	private ApiResponse tray(WebSession ws, String barcodeData)
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

		boolean trayGTINCorrect = trayGTIN.equals(ws.get("materialDU_EAN"));
		boolean trayVariantCorrect = trayVariant.equals(ws.get("materialDU_VARIANT"));
		boolean palletGTINCorrect = ws.get("palletGTIN").equals(ws.get("materialDU_EAN"));
		boolean palletVariantCorrect = ws.get("palletVariant").equals(ws.get("materialDU_VARIANT"));

		ws.set("trayGTINColor", trayGTINCorrect ? "green" : "red");
		ws.set("trayVariantColor", trayVariantCorrect ? "green" : "red");
		ws.set("palletGTINColor", palletGTINCorrect ? "green" : "red");
		ws.set("palletVariantColor", palletVariantCorrect ? "green" : "red");

		boolean consistent = trayGTINCorrect && trayVariantCorrect && palletGTINCorrect && palletVariantCorrect;
		ws.set("resultImage", consistent ? "images/valid.gif" : "images/invalid.gif");
		ws.set("resultMessage", consistent ? "Barcodes consistent" : "Barcodes inconsistent");
		return ApiResponse.goTo(PAGE_RESULT).withMessage(ws.get("resultMessage"));
	}
}
