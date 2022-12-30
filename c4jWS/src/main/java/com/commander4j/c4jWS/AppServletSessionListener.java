package com.commander4j.c4jWS;

import org.apache.log4j.Logger;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class AppServletSessionListener implements HttpSessionListener
{

	private Logger logger = Logger.getLogger(AppServletSessionListener.class);
	boolean connectedOK = false;
	
	public void sessionCreated(HttpSessionEvent se)
	{
		Common.sessionCount++;
		String sessionId = se.getSession().getId();
		Common.sd.setData(sessionId, "silentExceptions", "Yes", true);
		logger.debug("sessionCreated ["+sessionId+"]");
		logger.debug("sessionCount "+Common.sessionCount);
		
		String sqlPath = "/xml/sql/sql." + Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver() + ".xml";
		sqlPath = se.getSession().getServletContext().getRealPath(sqlPath);

		String viewPath = "/xml/view/view." + Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDriver() + ".xml";
		viewPath = se.getSession().getServletContext().getRealPath(viewPath);
		
		if (Common.hostList.getHost(Common.selectedHostID).connect(sessionId, Common.selectedHostID, sqlPath, viewPath))		
		{
			connectedOK = true;

		}
		else
		{
			//TODO - figure out how to send fail back to rest client.
			connectedOK = false;

			se.getSession().invalidate();
		}
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
		Common.sessionCount--;
		String sessionId = se.getSession().getId();
		
		Common.hostList.getHost(Common.selectedHostID).disconnect(sessionId);

		logger.debug("sessionDestroyed  ["+sessionId+"]");
		logger.debug("sessionCount "+Common.sessionCount);
	}

}
