package com.commander4j.c4jWS;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener implements ServletContextListener
{
	private Logger logger = Logger.getLogger(AppServletContextListener.class);
	
	public void contextInitialized(ServletContextEvent sce)
	{
		String uniqueID = "service";
		Common.applicationMode = "Servlet";
		String logfilename = sce.getServletContext().getRealPath("/xml/log/log4j.xml");
		String xmlfilename = sce.getServletContext().getRealPath("/xml/hosts/hosts.xml");
		
		DOMConfigurator.configure(logfilename);
		
		logger.debug("contextInitialized ["+sce.getServletContext().getServletContextName()+"]");
		
		Common.paths.clear();

		Common.paths.put("sql.com.mysql.jdbc.Driver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("sql.oracle.jdbc.driver.OracleDriver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.oracle.jdbc.driver.OracleDriver.xml"));

		Common.paths.put("view.com.mysql.jdbc.Driver.xml", sce.getServletContext().getRealPath("/xml/view/view.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", sce.getServletContext().getRealPath("/xml/view/view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("view.oracle.jdbc.driver.OracleDriver.xml", sce.getServletContext().getRealPath("/xml/view/view.oracle.jdbc.driver.OracleDriver.xml"));
		
		Common.hostList.loadHosts(xmlfilename);
		
		logger.debug(Common.hostList.getHosts().toString());
		
		Common.selectedHostID = Common.hostList.getHostIDforUniqueId(uniqueID);
		
		logger.debug("service host = "+Common.selectedHostID+" - "+Common.hostList.getHost(Common.selectedHostID).getSiteDescription());
	}

	public void contextDestroyed(ServletContextEvent sce)
	{
		logger.debug("contextDestroyed ["+sce.getServletContext().getServletContextName()+"]");
		logger.removeAllAppenders();
		logger = null;
	}

}
