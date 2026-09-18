package com.commander4j.web.api;

/**
 * GET /api/lang?keys=web_Submit,web_Cancel,mod_root
 * Replaces the <jsp:useBean JLanguage> + Lang.getText() calls in every JSP.
 * Needs a logged-on user: JDBLanguage.get(key, language) only answers from the
 * cache that JDBLanguage.preLoad("%") fills at logon (an uncached lookup queries
 * the table with hostID + " " + key and always misses). web_react has the same
 * constraint - no JSP before menu.jsp calls Lang.getText().
 */

import java.util.LinkedHashMap;
import java.util.Map;

import com.commander4j.db.JDBLanguage;
import com.commander4j.web.session.WebSession;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet(urlPatterns = { "/api/lang" })
public class LangServlet extends JsonServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected ApiResponse handle(String method, String path, JsonObject body, WebSession ws, HttpServletRequest request)
	{
		String keys = request.getParameter("keys");
		String language = ws.get("language");

		Map<String, String> text = new LinkedHashMap<String, String>();
		if (keys != null && keys.isEmpty() == false)
		{
			JDBLanguage lang = new JDBLanguage(ws.getSelectedHost(), ws.getId());
			for (String key : keys.split(","))
			{
				key = key.trim();
				if (key.isEmpty() == false)
				{
					text.put(key, JsonServlet.langText(lang, ws.getSelectedHost(), key, language));
				}
			}
		}
		return ApiResponse.ok().put("language", language).put("text", text);
	}
}
