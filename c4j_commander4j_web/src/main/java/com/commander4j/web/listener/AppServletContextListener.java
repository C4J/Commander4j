package com.commander4j.web.listener;

/**
 * Mirrors Process.init() / Process.destroy() from c4j_web_react: one context,
 * many selectable hosts, no host connected until the user picks one.
 */

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;
import com.commander4j.util.JPlaySound;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;
import com.commander4j.web.util.JQMBridgeClient;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AppServletContextListener.class);

	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext ctx = sce.getServletContext();

		JPlaySound.disable();
		Common.applicationMode = "Servlet";

		Common.paths.clear();
		Common.paths.put("sql.com.mysql.jdbc.Driver.xml", ctx.getRealPath("/xml/sql/sql.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", ctx.getRealPath("/xml/sql/sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("sql.oracle.jdbc.driver.OracleDriver.xml", ctx.getRealPath("/xml/sql/sql.oracle.jdbc.driver.OracleDriver.xml"));
		Common.paths.put("view.com.mysql.jdbc.Driver.xml", ctx.getRealPath("/xml/view/view.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", ctx.getRealPath("/xml/view/view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("view.oracle.jdbc.driver.OracleDriver.xml", ctx.getRealPath("/xml/view/view.oracle.jdbc.driver.OracleDriver.xml"));

		JUtility.initLogging(ctx.getRealPath("/xml/log/log4j2.xml"));

		logger.debug("contextInitialized [" + ctx.getServletContextName() + "]");

		Common.hostList.loadHosts(getHostPath(ctx));
		logger.debug("hosts loaded: " + Common.hostList.size());

		JPrint.init();

		// modbusBridge relay pulses (carried over from c4j_web_Issue); shipped disabled in xml/bridge/bridge.xml
		JQMBridgeClient.init(getBridgePath(ctx));
	}

	public void contextDestroyed(ServletContextEvent sce)
	{
		JQMBridgeClient.shutdown();
		Common.hostList.disconnectAll();
		Common.userList.clear();
		JHost.deRegisterDrivers();
		logger.debug("contextDestroyed [" + sce.getServletContext().getServletContextName() + "]");
	}

	/**
	 * bridge.xml: same external-copy rule as hosts.xml (c4j_web_Issue.getBridgePath).
	 */
	private String getBridgePath(ServletContext ctx)
	{
		String catalinaHome = System.getProperty("catalina.home");
		String contextPath = ctx.getContextPath().replace("/", "");
		File configDir = new File(catalinaHome + File.separator + "c4j_config" + File.separator + contextPath);
		File bridgeFile = new File(configDir, "bridge.xml");
		String result = bridgeFile.getAbsolutePath();
		if (bridgeFile.exists() == false)
		{
			try
			{
				configDir.mkdirs();
				FileUtils.copyFileToDirectory(new File(ctx.getRealPath("/xml/bridge/bridge.xml")), configDir);
			}
			catch (IOException e)
			{
				result = ctx.getRealPath("/xml/bridge/bridge.xml");
			}
		}
		return result;
	}

	/**
	 * hosts.xml lives in $CATALINA_HOME/c4j_config/<context>/ so that a redeploy of
	 * the war does not overwrite the customer's host definitions. Seeded from the
	 * war's /xml/hosts on first start. Same behaviour as Process.getHostPath().
	 */
	private String getHostPath(ServletContext ctx)
	{
		String catalinaHome = System.getProperty("catalina.home");
		String contextPath = ctx.getContextPath().replace("/", "");

		File configDir = new File(catalinaHome + File.separator + "c4j_config" + File.separator + contextPath);
		configDir.mkdirs();

		File hostsFile = new File(configDir, "hosts.xml");
		String result = hostsFile.getAbsolutePath();

		if (hostsFile.exists() == false)
		{
			try
			{
				FileUtils.copyFileToDirectory(new File(ctx.getRealPath("/xml/hosts/hosts.xml")), configDir);
				FileUtils.copyFileToDirectory(new File(ctx.getRealPath("/xml/hosts/hosts.dtd")), configDir);
			}
			catch (IOException e)
			{
				logger.warn("Unable to seed " + result + " - using war copy: " + e.getMessage());
				result = ctx.getRealPath("/xml/hosts/hosts.xml");
			}
		}

		return result;
	}
}
