package com.commander4j.web.filter;

/**
 * Makes the browser revalidate every static page, script and stylesheet on
 * each load. Tomcat's DefaultServlet sends Last-Modified but no Cache-Control,
 * so Chrome heuristically reuses cached copies (found 2026-09-11: an updated
 * c4j.js was never re-requested). "no-cache" still allows the cheap 304
 * conditional round-trip; it does not disable caching outright.
 *
 * Exception (2026-09-13): assets whose name carries a content hash, as written by
 * the "stage" target in build.xml (js/c4j.<md5 8>.js etc.), are immutable by
 * construction - a new build gives changed content a new name - so they are
 * served with a one-year cache header instead. The name pattern is the only
 * contract between the build and this filter.
 */

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

public class NoCacheFilter implements Filter
{
	private static final Pattern HASHED_ASSET = Pattern.compile(".*\\.[0-9a-f]{8}\\.(js|css)$");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();
		if (HASHED_ASSET.matcher(path).matches() && exists(req, path))
		{
			res.setHeader("Cache-Control", "public, max-age=31536000, immutable");
		}
		else
		{
			res.setHeader("Cache-Control", "no-cache, must-revalidate");
			res.setHeader("Pragma", "no-cache");
			res.setDateHeader("Expires", 0);
		}
		chain.doFilter(request, response);
	}

	/** Never send the long-lived header for a name that is not in the war (a 404 must stay uncached). */
	private static boolean exists(HttpServletRequest req, String requestURI)
	{
		try
		{
			return req.getServletContext().getResource(requestURI.substring(req.getContextPath().length())) != null;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
