package com.commander4j.web.listener;

/**
 * Mirrors Process.sessionCreated() / sessionDestroyed() from c4j_web_react.
 * No host is connected here - the user chooses one on the hosts screen.
 */

import org.apache.logging.log4j.Logger;

import com.commander4j.web.session.WebSession;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class AppServletSessionListener implements HttpSessionListener
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AppServletSessionListener.class);

	public void sessionCreated(HttpSessionEvent se)
	{
		String sessionID = se.getSession().getId();
		WebSession.markSilentExceptions(sessionID);
		logger.debug("sessionCreated [" + sessionID + "]");
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
		String sessionID = se.getSession().getId();
		WebSession.releaseResources(sessionID);
		WebSession.releaseLock(sessionID);
		logger.debug("sessionDestroyed [" + sessionID + "]");
	}
}
