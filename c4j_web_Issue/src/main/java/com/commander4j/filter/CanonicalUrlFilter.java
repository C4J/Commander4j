package com.commander4j.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Redirects case-insensitive paths to a canonical mixed-case version using a mapping
 * loaded at startup from a .properties file.
 *
 * Keys (left side):   request path WITHOUT context path, recommended with a leading '/'.
 *                     Keys are matched case-insensitively (stored lowercase).
 * Values (right side):canonical path WITH desired case (must start with '/').
 *
 * Example entry:
 *   /html/userlogon.html=/html/userLogon.html
 *
 * Init params (set in web.xml):
 *   mapLocation : where to load the properties from. Supported forms:
 *                 1) /WEB-INF/canon-paths.properties  (default; inside the WAR)
 *                 2) classpath:canon-paths.properties (from application classpath)
 *                 3) /absolute/path/to/canon-paths.properties (filesystem)
 */
public class CanonicalUrlFilter implements Filter {

    private volatile Map<String, String> canonMap = Collections.emptyMap(); // keys are lowercase
    private ServletContext servletContext;
    private String mapLocation;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.mapLocation = valueOrDefault(filterConfig.getInitParameter("mapLocation"),
                                          "/WEB-INF/canon-paths.properties");
        try {
            this.canonMap = loadMapping(this.mapLocation);
            servletContext.log("CanonicalUrlFilter: loaded " + canonMap.size()
                    + " mappings from " + this.mapLocation);
        } catch (IOException e) {
            servletContext.log("CanonicalUrlFilter: failed to load mappings from "
                    + this.mapLocation + " (continuing with empty map): " + e, e);
            this.canonMap = Collections.emptyMap();
        }
    }

    private static String valueOrDefault(String v, String def) {
        return (v == null || v.isBlank()) ? def : v;
    }

    private Map<String, String> loadMapping(String location) throws IOException {
        Properties props = new Properties();

        if (location.startsWith("classpath:")) {
            String resource = location.substring("classpath:".length());
            if (resource.startsWith("/")) resource = resource.substring(1);
            try (InputStream in = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(resource)) {
                if (in == null) {
                    throw new IOException("Classpath resource not found: " + resource);
                }
                try (InputStreamReader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    props.load(r);
                }
            }
        } else if (location.startsWith("/WEB-INF/")) {
            try (InputStream in = servletContext.getResourceAsStream(location)) {
                if (in == null) {
                    throw new IOException("WEB-INF resource not found: " + location);
                }
                try (InputStreamReader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    props.load(r);
                }
            }
        } else {
            // Absolute filesystem path
            Path p = Paths.get(location);
            try (BufferedReader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
                props.load(r);
            }
        }

        Map<String, String> map = new LinkedHashMap<>();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key, "").trim();
            if (value.isEmpty()) continue;

            String k = key.trim();
            if (!k.startsWith("/")) k = "/" + k;
            String v = value;
            if (!v.startsWith("/")) v = "/" + v;

            map.put(k.toLowerCase(Locale.ROOT), v); // store key lowercase
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx  = req.getContextPath();             // e.g. "/c4j_web_Issue"
        String uri  = req.getRequestURI();              // e.g. "/c4j_web_Issue/HTML/USERLOGON.HTML"
        String path = uri.substring(ctx.length());      // e.g. "/HTML/USERLOGON.HTML"

        // Avoid interfering with path parameters like ;jsessionid
        if (path.indexOf(';') >= 0) {
            chain.doFilter(request, response);
            return;
        }

        String target = canonMap.get(path.toLowerCase(Locale.ROOT));
        if (target != null && !path.equals(target)) {
            String qs = req.getQueryString();
            String location = ctx + target + (qs == null ? "" : "?" + qs);
            res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
            res.setHeader("Location", location);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
