package com.commander4j.web.api;

/**
 * The RF main menu (menu.jsp / Process.displayMenu + menu):
 *
 *   GET /api/menu                 options directly under the session's current menu (core JMenuRFMenu.getMenuOptions(menuID))
 *                                 + selected, menu (id), title (its description), isRoot
 *   PUT /api/menu/select {selectedMenuOption}   a MENU-type row descends into it; otherwise the same per-module
 *                                 preparation + next page as Process.menu()
 *   PUT /api/menu/back            inside a sub-menu: climb one level (the menu just left is re-selected);
 *                                 at the top level: next = the logoutConfirm page ("Logout ?" Yes / No, 2026-09-20)
 *   PUT /api/menu/exit            logoutConfirm Yes (was the menu Exit button): drop the user, back to login
 *                                 (host stays connected)
 *
 * Since schema 217 SYS_RF_MENU is a tree like SYS_MENUS (MENU_ID = parent, top level = root); one level is shown
 * at a time (MC9400 320x405). The current path is the session value MENU_PATH: menu ids from the top, "/"-joined,
 * blank at the top level. MENU rows whose children the user cannot see are left out (permissions are shared with the
 * desktop, where a group holding MENU_QM sees the Quality Management pull-down - empty on the scanner otherwise).
 *
 * Server-side permission: the chosen module must be in the user's loaded module list
 * (JDBUser.isModuleAllowed), whatever the client sent. Modules whose screen is not
 * ported yet answer with a message instead of a 404.
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBUser;
import com.commander4j.html.JMenuRFMenu;
import com.commander4j.sys.Common;
import com.commander4j.sys.JMenuOption;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/menu", "/api/menu/*" })
public class MenuServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	/** session key: the sub-menu path from the top level, "/"-joined menu ids, blank at the top level (schema 217) */
	static final String K_MENU_PATH = "menuPath";
	private static final String PATH_SEP = "/";

	/** module id -> page; grows as screens are ported (see design doc inventory). */
	private static final Map<String, String> PAGE_FOR_MODULE = new LinkedHashMap<String, String>();
	/** pages that exist in src/main/webapp so far */
	private static final Set<String> PORTED = Set.of("changePassword", "menu", "productionConfirm", "productionConfirmPlusSSCC", "palletDelete", "palletInfo", "validateDUSelect", "printerSelect", "sysInfo", "despatchSelect", "wasteLog", "processOrderIssueSelect", "palletReturnSelect", "palletHistory", QMState.PAGE_SCORE_USER, QMState.PAGE_PANELS, QMState.PAGE_USERS);

	static
	{
		PAGE_FOR_MODULE.put("FRM_PAL_PROD_CONFIRM", "productionConfirm");
		PAGE_FOR_MODULE.put("FRM_PAL_PROD_CONFIRM+", "productionConfirmPlusSSCC");
		PAGE_FOR_MODULE.put("FRM_ADMIN_WASTE_LOG", "wasteLog");
		PAGE_FOR_MODULE.put("FRM_BARCODE_VALIDATE", "validateDUSelect");
		PAGE_FOR_MODULE.put("FRM_PAL_DELETE", "palletDelete");
		PAGE_FOR_MODULE.put("FRM_ADMIN_DESPATCH", "despatchSelect");
		PAGE_FOR_MODULE.put("FRM_PAL_INFO", "palletInfo");
		PAGE_FOR_MODULE.put("SYS_INFO", "sysInfo");
		PAGE_FOR_MODULE.put("FRM_CM_PRINTERS", "printerSelect");
		PAGE_FOR_MODULE.put("FRM_USER_PASS_CHANGE", "changePassword");
		// c4j_web_Issue screens (step 7, 2026-09-11); SSCC Info = the existing palletInfo under FRM_PAL_INFO
		PAGE_FOR_MODULE.put("FRM_PAL_ISSUE", "processOrderIssueSelect");
		PAGE_FOR_MODULE.put("FRM_PAL_RETURN", "palletReturnSelect");
		PAGE_FOR_MODULE.put("FRM_ADMIN_PALLET_HISTORY", "palletHistory");
		// c4j_web_WS panel grading (step 8, 2026-09-12; schema 217 2026-09-17): the three options are children of the
		// MENU_QM row on the RF menu (the FRM_QM_PANEL stand-in and its fixed qmMenu page are gone)
		PAGE_FOR_MODULE.put(QMState.MODULE_SCORES, QMState.PAGE_SCORE_USER);
		PAGE_FOR_MODULE.put(QMState.MODULE_SETUP, QMState.PAGE_PANELS);
		PAGE_FOR_MODULE.put(QMState.MODULE_ADMIN, QMState.PAGE_USERS);
	}

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		if (method.equals("GET") && (path.equals("") || path.equals("/")))
		{
			return list(ws);
		}
		if (method.equals("PUT") && path.equals("/select"))
		{
			return select(ws, str(body, "selectedMenuOption"));
		}
		if (method.equals("PUT") && path.equals("/back"))
		{
			return back(ws);
		}
		if (method.equals("PUT") && path.equals("/exit"))
		{
			return exit(ws);
		}
		return ApiResponse.fail("Unknown menu action " + method + " " + path);
	}

	// ---- menu path (schema 217) ----------------------------------------------

	/** Back to the top level with nothing selected: login, password change, exit. */
	static void resetMenu(WebSession ws)
	{
		ws.set(K_MENU_PATH, "");
		ws.set("selectedMenuOption", "");
	}

	private static List<String> menuPath(WebSession ws)
	{
		List<String> path = new ArrayList<String>();
		for (String id : ws.get(K_MENU_PATH).split(PATH_SEP))
		{
			if (id.isEmpty() == false)
			{
				path.add(id);
			}
		}
		return path;
	}

	private static void setMenuPath(WebSession ws, List<String> path)
	{
		ws.set(K_MENU_PATH, String.join(PATH_SEP, path));
	}

	/** the menu whose rows are shown: the last id on the path, or root */
	private static String currentMenu(WebSession ws)
	{
		List<String> path = menuPath(ws);
		return path.isEmpty() ? JMenuRFMenu.ROOT_MENU_ID : path.get(path.size() - 1);
	}

	// ---- GET /api/menu  (Process.displayMenu) --------------------------------

	private ApiResponse list(WebSession ws)
	{
		String selected = ws.get("selectedMenuOption");
		String menuId = currentMenu(ws);
		boolean isRoot = menuId.equals(JMenuRFMenu.ROOT_MENU_ID);
		List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();

		JMenuRFMenu rfm = new JMenuRFMenu(ws.getSelectedHost(), ws.getId());
		JDBModule mod = new JDBModule(ws.getSelectedHost(), ws.getId());
		com.commander4j.db.JDBLanguage lang = new com.commander4j.db.JDBLanguage(ws.getSelectedHost(), ws.getId());
		String language = ws.get("language");

		// the page shows the sub-menu's own description as its title; blank at the top level = the page's mod_root text
		String title = "";
		if (isRoot == false && mod.getModuleProperties(menuId))
		{
			title = moduleDescription(mod, lang, ws.getSelectedHost(), ws.getId(), menuId, language);
		}

		for (JMenuOption opt : rfm.getMenuOptions(menuId))
		{
			String type = opt.moduleType == null ? "" : opt.moduleType.trim();
			if (type.equals("MENU") && rfm.getMenuOptions(opt.moduleID).isEmpty())
			{
				continue;   // a sub-menu with nothing this user may run: do not offer an empty page
			}
			if (selected.isEmpty())
			{
				selected = opt.moduleID;
			}
			// core JMenuOption.load: description = lang.get(resource_key), which is "<host> <key>" when untranslated;
			// re-derive it with the HINT fallback the desktop lacks (Dave, 2026-09-12) only when that happened
			String description = opt.description == null ? "" : opt.description;
			if (description.isEmpty() || description.startsWith(ws.getSelectedHost() + " "))
			{
				description = mod.getModuleProperties(opt.moduleID) ? moduleDescription(mod, lang, ws.getSelectedHost(), ws.getId(), opt.moduleID, language) : opt.moduleID;
			}
			Map<String, Object> o = new LinkedHashMap<String, Object>();
			o.put("moduleID", opt.moduleID);
			o.put("description", description);
			o.put("icon", opt.iconFilename == null ? "" : opt.iconFilename.trim());   // SYS_MODULES.icon_filename, served from images/menu/
			o.put("moduleType", opt.moduleType == null ? "" : opt.moduleType.trim()); // the page picks the desktop's default icon by type when icon is blank
			o.put("selected", opt.moduleID.equals(selected));
			options.add(o);
		}

		return ApiResponse.ok().put("options", options).put("selected", selected).put("menu", menuId).put("title", title).put("isRoot", isRoot);
	}

	// ---- PUT /api/menu/select  (Process.menu Submit) --------------------------

	private ApiResponse select(WebSession ws, String option)
	{
		logger.debug("Selected Menu Option = " + option);

		if (option.isEmpty())
		{
			return ApiResponse.ok();
		}

		JDBUser usr = Common.userList.getUser(ws.getId());
		if (usr.isModuleAllowed(option) == false)
		{
			logger.warn("User " + usr.getUserId() + " not permitted module " + option);
			return ApiResponse.fail("Not authorised");
		}

		// a MENU-type row (schema 217): descend into it, first child focused
		JDBModule mod = new JDBModule(ws.getSelectedHost(), ws.getId());
		if (mod.getModuleProperties(option) && mod.getType() != null && mod.getType().trim().equals("MENU"))
		{
			if (new JMenuRFMenu(ws.getSelectedHost(), ws.getId()).getMenuOptions(option).isEmpty())
			{
				logger.warn("User " + usr.getUserId() + " has nothing to run under menu " + option);
				return ApiResponse.fail("Not authorised");   // list() never offers such a menu; refuse a hand-made request too
			}
			List<String> path = menuPath(ws);
			path.add(option);
			setMenuPath(ws, path);
			ws.set("selectedMenuOption", "");
			return ApiResponse.goTo(SessionServlet.PAGE_MENU);
		}

		String page = PAGE_FOR_MODULE.get(option);
		if (page == null)
		{
			return ApiResponse.fail("Unknown menu option " + option);
		}

		ws.set("selectedMenuOption", option);

		// per-module preparation, exactly as Process.menu()
		switch (option)
		{
			case "FRM_PAL_PROD_CONFIRM":
			case "FRM_PAL_PROD_CONFIRM+":
				ws.set("confirmCount", "0");
				break;
			case "FRM_PAL_DELETE":
				ws.set("deleteCount", "0");
				break;
			case "FRM_ADMIN_DESPATCH":
				ws.set("currentDespatchListPage", "1");
				break;
			case "FRM_ADMIN_WASTE_LOG":
				if (ws.get("wasteQuantity").isEmpty())
				{
					ws.set("wasteQuantity", "0");
					ws.set("wasteMaterialUOM", "");
				}
				// wasteComboRefresh happens on the waste page's own GET (step 4)
				break;
			case "FRM_USER_PASS_CHANGE":
				ws.set("changePasswordCancelScreen", SessionServlet.PAGE_MENU);
				break;
			case "FRM_PAL_ISSUE":
			case "FRM_PAL_RETURN":
				IssueServlet.clearIssueState(ws);   // web_Issue's menu() resetValues(): fresh flow each time
				break;
			case QMState.MODULE_SCORES:
			case QMState.MODULE_SETUP:
			case QMState.MODULE_ADMIN:
				QMState.clear(ws);   // web_WS menu.html cleared its sessionStorage on every option
				ws.set(QMState.K_OPTION, option);
				break;
			default:
				break;
		}

		if (PORTED.contains(page) == false)
		{
			return ApiResponse.fail("Screen '" + page + "' not ported yet");
		}

		return ApiResponse.goTo(page);
	}

	// ---- PUT /api/menu/back  (footer Back: up one level, or confirm logout at the top) --

	/** page shown when Back is pressed at the top level: "Logout ?" with Yes (PUT /exit) and No (back to menu) */
	static final String PAGE_LOGOUT_CONFIRM = "logoutConfirm";

	private ApiResponse back(WebSession ws)
	{
		List<String> path = menuPath(ws);
		if (path.isEmpty())
		{
			return ApiResponse.goTo(PAGE_LOGOUT_CONFIRM);   // nothing changes in the session until Yes calls /exit
		}
		String left = path.remove(path.size() - 1);
		setMenuPath(ws, path);
		ws.set("selectedMenuOption", left);   // the menu just left keeps the focus, as the desktop tree does
		return ApiResponse.goTo(SessionServlet.PAGE_MENU);
	}

	// ---- PUT /api/menu/exit  (logoutConfirm Yes; was Process.menu Cancel) ---------

	private ApiResponse exit(WebSession ws)
	{
		ws.setLoggedOn(false);
		ws.set("username", "");
		resetMenu(ws);
		Common.userList.removeUser(ws.getId());
		return ApiResponse.goTo(SessionServlet.PAGE_LOGIN);
	}
}
