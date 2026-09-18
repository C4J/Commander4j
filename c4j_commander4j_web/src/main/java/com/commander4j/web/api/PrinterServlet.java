package com.commander4j.web.api;

/**
 * Printer select #23 (module FRM_CM_PRINTERS):
 *   GET /api/printers                          queue names (JPrint.getPrinterNames) + the session's defaultPrinter
 *   PUT /api/printers {selectedPrintQueue}     Process.printerSelect -> menu
 * No core change needed: JMenuRFPrinterList only wraps JPrint.getPrinterNames() in a <select>.
 */

import com.commander4j.util.JPrint;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/printers" })
public class PrinterServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected String moduleId()
	{
		return "FRM_CM_PRINTERS";
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET"))
		{
			return ApiResponse.ok().put("printers", JPrint.getPrinterNames()).put("selected", ws.get("defaultPrinter"));
		}
		if (method.equals("PUT"))
		{
			String queue = str(body, "selectedPrintQueue");
			ws.set("defaultPrinter", queue);
			logger.debug("User selected printer :" + queue);
			return ApiResponse.goTo(SessionServlet.PAGE_MENU);
		}
		return ApiResponse.fail("Method not supported");
	}
}
