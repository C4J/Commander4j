package com.commander4j.util;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Fire-and-forget client for the util_modbusBridge REST service. On a wrong-lane issue
 * attempt the controller calls {@link #pulseAsync(String)} with the scanned location id;
 * the bridge's own restapi id list decides whether that location maps to a relay (an
 * unknown location returns 404 and is simply ignored) and the bridge's per-point
 * defaultHoldMs decides the relay timing. The call never blocks the servlet request and
 * a failure never changes what the operator sees - outcomes only go to the log.
 */
public final class JQMBridgeClient
{
	private static final Logger logger = LogManager.getLogger(JQMBridgeClient.class);

	private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(2);

	// A pulse queued behind others on the same relay only gets its HTTP response when
	// its turn comes (queue depth x hold time), so the request timeout must exceed the
	// worst case or we log a failure for a pulse that still fires bridge-side.
	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(90);

	private static volatile boolean enabled = false;
	private static volatile String baseURL = "";
	private static volatile HttpClient client = null;

	private JQMBridgeClient()
	{
	}

	public static void init(String xmlfilename)
	{
		enabled = false;
		baseURL = "";

		try
		{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlfilename));
			Element root = doc.getDocumentElement();

			boolean enabledAttr = Boolean.parseBoolean(root.getAttribute("enabled"));

			String url = "";
			NodeList urls = root.getElementsByTagName("url");
			if (urls.getLength() > 0)
			{
				url = urls.item(0).getTextContent().trim();
			}
			while (url.endsWith("/"))
			{
				url = url.substring(0, url.length() - 1);
			}

			if (enabledAttr && url.equals(""))
			{
				logger.warn("Bridge enabled but no <url> configured in [" + xmlfilename + "] - bridge calls disabled");
				return;
			}

			baseURL = url;
			enabled = enabledAttr;

			if (enabled)
			{
				client = HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT).build();
				logger.info("Bridge pulse calls enabled - url [" + baseURL + "]");
			}
			else
			{
				logger.debug("Bridge pulse calls disabled in [" + xmlfilename + "]");
			}
		}
		catch (Exception e)
		{
			logger.warn("Unable to read bridge config [" + xmlfilename + "] - bridge calls disabled : " + e.getMessage());
		}
	}

	public static void shutdown()
	{
		enabled = false;

		if (client != null)
		{
			client.close();
			client = null;
		}
	}

	public static void pulseAsync(String locationId)
	{
		if (enabled == false)
		{
			return;
		}

		if (locationId == null || locationId.trim().equals(""))
		{
			return;
		}

		final String location = locationId.trim();
		String name = URLEncoder.encode(location, StandardCharsets.UTF_8).replace("+", "%20");

		HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(baseURL + "/api/points/" + name + "/pulse")).timeout(REQUEST_TIMEOUT).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString("{\"value\":1}")).build();

		client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).whenComplete((response, error) -> {
			if (error != null)
			{
				logger.warn("Bridge pulse for [" + location + "] failed : " + error.getMessage());
			}
			else if (response.statusCode() == 200)
			{
				logger.info("Bridge pulse for [" + location + "] accepted");
			}
			else if (response.statusCode() == 404)
			{
				logger.debug("Bridge has no point or id for [" + location + "] - no pulse");
			}
			else
			{
				logger.warn("Bridge pulse for [" + location + "] rejected : HTTP " + response.statusCode() + " " + response.body());
			}
		});
	}

}
