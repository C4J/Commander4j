package com.commander4j.c4jWS;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.tomcat.jakartaee.commons.io.FileUtils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener implements ServletContextListener
{
	private Logger logger = org.apache.logging.log4j.LogManager.getLogger(AppServletContextListener.class);
	
	public void contextInitialized(ServletContextEvent sce)
	{
		String uniqueID = "service";
		Common.applicationMode = "Servlet";
		String logfilename = sce.getServletContext().getRealPath("/xml/log/log4j2.xml");
		String xmlfilename = sce.getServletContext().getRealPath("/xml/hosts/hosts.xml");
		
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(logfilename);

		// this will force a reconfiguration
		context.setConfigLocation(file.toURI());
		
		logger.debug("contextInitialized ["+sce.getServletContext().getServletContextName()+"]");
		
		Common.paths.clear();

		Common.paths.put("sql.com.mysql.jdbc.Driver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("sql.oracle.jdbc.driver.OracleDriver.xml", sce.getServletContext().getRealPath("/xml/sql/sql.oracle.jdbc.driver.OracleDriver.xml"));

		Common.paths.put("view.com.mysql.jdbc.Driver.xml", sce.getServletContext().getRealPath("/xml/view/view.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", sce.getServletContext().getRealPath("/xml/view/view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("view.oracle.jdbc.driver.OracleDriver.xml", sce.getServletContext().getRealPath("/xml/view/view.oracle.jdbc.driver.OracleDriver.xml"));
		
		xmlfilename = getHostPath(sce);
		
		Common.hostList.loadHosts(xmlfilename);
		
		logger.debug(Common.hostList.getHosts().toString());
		
		Common.selectedHostID = Common.hostList.getHostIDforUniqueId(uniqueID);
		
		logger.debug("service host = "+Common.selectedHostID+" - "+Common.hostList.getHost(Common.selectedHostID).getSiteDescription());
	}

	public void contextDestroyed(ServletContextEvent sce)
	{
		Common.hostList.getHost(Common.selectedHostID).disconnectAll();
		
		logger.debug("contextDestroyed ["+sce.getServletContext().getServletContextName()+"]");

		logger = null;
	}
	
	private String getHostPath(ServletContextEvent sce)
	{
		String result = "";
		
		String catalinaHome = System.getProperty("catalina.home");
		String contextPath = sce.getServletContext().getContextPath().replace("/", "");

		File configPath = new File(catalinaHome + File.separator + "c4j_config");

		if (configPath.exists() == false)
		{
			configPath.mkdir();
		}

		configPath = new File(catalinaHome + File.separator + "c4j_config" + File.separator + contextPath);

		if (configPath.exists() == false)
		{
			configPath.mkdir();
		}

		configPath = new File(catalinaHome + File.separator + "c4j_config" + File.separator + contextPath + File.separator + "hosts.xml");
		
		result = configPath.getAbsolutePath();

		if (configPath.exists() == false)
		{

			try
			{
				File source = new File(sce.getServletContext().getRealPath("/xml/hosts/hosts.xml"));
				
				File destination = new File(catalinaHome + File.separator + "c4j_config" + File.separator + contextPath);
				
				FileUtils.copyFileToDirectory(source, destination);

				source = new File(sce.getServletContext().getRealPath("/xml/hosts/hosts.dtd"));
				
				FileUtils.copyFileToDirectory(source, destination);
				
			}
			catch (IOException e)
			{
				result = sce.getServletContext().getRealPath("/xml/hosts/hosts.xml");
			}

		}

		return result;
	}

}
