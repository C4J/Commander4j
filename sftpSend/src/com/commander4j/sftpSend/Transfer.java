package com.commander4j.sftpSend;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class Transfer extends Thread
{
	
	public static Transfer sftpsend;
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((Transfer.class));
	public static LoggerContextFactory factory = LogManager.getFactory();
	public static boolean run = true;;
	
	public static void main(String[] args)
	{
		
		
		Transfer.initLogging("");
		
		sftpsend = new Transfer();


		while (run)
		{
			sftpsend.transferFiles();
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				run=false;
			}
		}
		
		System.exit(0);

	}

	public void requestStop()
	{
		run = false;
	}
	
	private void transferFiles()
	{

		String localDir = "./send/";
		String authType = "userPass";
		String username = "dgadm";
		String password = "a4nQ/czt}7{>76+7";
		String remoteHost = "ns2.6info.fr";
		String remoteDir = "//home/dgadm/transfer/centurylogistics.co.uk/";
		String StrictHostKeyChecking = "no";
		String knownHostsFile = "./ssh/known_hosts";
		String localFileMask = "*.xml";

		File currentDirectory = new File(localDir);

		String[] filesNames = currentDirectory.list(new WildcardFileFilter(localFileMask));

		for (int i = 0; i < filesNames.length; i++)
		{

			File xxx = new File(localDir + filesNames[i]);

			if (xxx.isFile())
			{
				// System.out.println(filesNames[i]);

				send(localDir, filesNames[i], username, password, remoteHost, remoteDir, StrictHostKeyChecking, knownHostsFile);
			}

			xxx = null;
		}

	}

	private void send(String localDir, String localFile, String username, String password, String remoteHost, String remoteDir, String StrictHostKeyChecking, String knownHostsFile)
	{

		String tempExt = ".tmp";

		try
		{
			JSch jsch = new JSch();
			jsch.setKnownHosts(knownHostsFile);
			Session jschSession = jsch.getSession(username, remoteHost);
			jschSession.setPassword(password);

			JSch.setConfig("StrictHostKeyChecking", StrictHostKeyChecking);

			jschSession.connect();

			ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");

			channelSftp.connect();

			boolean transferSucceeded = false;

			try
			{

				SftpProgressMonitor progress = new SftpProgressMonitor()
				{
					private long max = 0;
					private long count = 0;
					private long percent = 0;

					public void end()
					{
						logger.debug("Finished.");
					}

					public void init(int op, String src, String dest, long max)
					{
						this.max = max;
						logger.debug("");
						logger.debug("Starting");

						logger.debug("File :" + src); // Origin
						logger.debug("Size :" + max + " bytes."); // Total
					}

					public boolean count(long bytes)
					{
						this.count += bytes;
						long percentNow = this.count * 100 / max;

						if (percentNow > this.percent)
						{
							this.percent = percentNow;

							logger.debug(String.valueOf(this.percent) + "%");

						}

						return (true);
					}
				};

				channelSftp.put(localDir + localFile, remoteDir + localFile + tempExt, progress);

				try
				{
					channelSftp.rm(remoteDir + localFile);
				}
				catch (SftpException ex)
				{

				}

				channelSftp.rename(remoteDir + localFile + tempExt, remoteDir + localFile);

				transferSucceeded = true;
			}
			catch (SftpException e)
			{
				logger.debug("Transfer failed." + e.getMessage());
				logger.debug("");
			}

			if (transferSucceeded)
			{
				logger.debug("Transfer completed.");
				logger.debug("");
				FileUtils.deleteQuietly(new File(localDir + localFile));
			}

			channelSftp.disconnect();

			channelSftp.exit();

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static void initLogging(String filename)
	{
		if (filename.isEmpty())
		{
			filename = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "log4j2.xml";
		}

		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(filename);

		context.setConfigLocation(file.toURI());

		if (factory instanceof Log4jContextFactory)
		{
			// LOG.info("register shutdown hook");
			Log4jContextFactory contextFactory = (Log4jContextFactory) factory;

			((DefaultShutdownCallbackRegistry) contextFactory.getShutdownCallbackRegistry()).stop();
		}

	}
}