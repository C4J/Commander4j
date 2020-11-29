package com.commander4j.sftp;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import com.commander4j.email.EmailQueue;
import com.commander4j.email.EmailThread;
import com.commander4j.util.EncryptData;
import com.commander4j.util.JCipher;
import com.commander4j.util.JUtility;
import com.commander4j.xml.JXMLDocument;

import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.Config;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

public class Transfer extends Thread
{
	public static Transfer sftpsend;
	public static Logger logger = org.apache.logging.log4j.LogManager.getLogger((Transfer.class));
	public static LoggerContextFactory factory = LogManager.getFactory();
	public static Hashtable<String, String> general = new Hashtable<String, String>();
	public static Hashtable<String, String> security = new Hashtable<String, String>();
	public static Hashtable<String, String> source = new Hashtable<String, String>();
	public static Hashtable<String, String> destination = new Hashtable<String, String>();
	public static boolean run = true;;
	public static SSHClient sshClient;
	public static SFTPClient sftpClient;
	public static JUtility utils = new JUtility();
	public static EmailQueue emailqueue = new EmailQueue();
	public static EmailThread emailthread;

	public static void main(String[] args)
	{

		Transfer.initLogging("");

		logger.info("sftpSend Starting");
		
		ShutdownHook shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		
		sftpsend = new Transfer();
		sftpsend.loadConfigXML();
		
		emailqueue.setEnabled(Boolean.valueOf(utils.replaceNullStringwithDefault(general.get("emailEnabled"), "no")));
		
		emailthread = new EmailThread();
		
		emailthread.start();

		emailqueue.addToQueue("Monitor", utils.replaceNullStringwithDefault(general.get("title"), "SFTP Send"), "Program started on "+utils.getClientName() , "");
		
		logger.info("currentDirectory :" + source.get("localDir"));
		logger.info("Scan for files...");

		while (run)
		{
			sftpsend.transferFiles();

			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				run = false;
			}
		}

		
		emailthread.shutdown();

		while (emailthread.isAlive())
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{

			}
		}
		
		logger.info("sftpSend Shutdown");
		
		emailqueue.addToQueue("Monitor", utils.replaceNullStringwithDefault(general.get("title"), "SFTP Send"), "Program stopped on "+utils.getClientName() , "");
		
		System.exit(0);
	}

	private boolean connect()
	{
		boolean result = false;

		try
		{

			Config config = new DefaultConfig();
			config.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
			sshClient = new SSHClient(config);

			if (security.get("checkKnownHosts").equals("no"))
			{
				sshClient.addHostKeyVerifier(new PromiscuousVerifier());
			}

			logger.info("authType=" + security.get("authType"));

			// * Method 1* //
			if (security.get("authType").equals("user password"))
			{

				sshClient.connect(security.get("remoteHost"), Integer.valueOf(security.get("remotePort")));

				sshClient.authPassword(security.get("username"), security.get("password"));

			}

			// * Method 1* //
			if (security.get("authType").equals("user public key"))
			{
				String username = security.get("username");
				File privateKey = new File(security.get("privateKeyFile"));

				KeyProvider keys;
				keys = sshClient.loadKeys(privateKey.getPath());

				sshClient.connect(security.get("remoteHost"), Integer.valueOf(security.get("remotePort")));

				sshClient.authPublickey(username, keys);
			}
			
			sftpClient = sshClient.newSFTPClient();

			result = true;
		}
		catch (UserAuthException e)
		{
			logger.error(e.getMessage());	
		}
		catch (TransportException e)
		{
			logger.error(e.getMessage());
		}

		catch (IOException e)
		{
			logger.error(e.getMessage());
		}

		return result;
	}

	private boolean disconnect()
	{
		boolean result = false;
		try
		{
			sftpClient.close();
			sshClient.disconnect();
			result = true;
		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
		}
		return result;
	}

	private boolean send(String localFile)
	{
		boolean result=false;

		try
		{
			// * Transfer file *//
			
			logger.info("put "+source.get("localDir") + localFile+","+destination.get("remoteDir") + localFile + destination.get("tempFileExtension"));

			sftpClient.put(source.get("localDir") + localFile, destination.get("remoteDir") + localFile + destination.get("tempFileExtension"));

			try
			{
				logger.info("rm "+destination.get("remoteDir") + localFile);
				sftpClient.rm(destination.get("remoteDir") + localFile);
			}
			catch (Exception e)
			{

			}

			logger.info("rename "+destination.get("remoteDir") + localFile + destination.get("tempFileExtension")+","+ destination.get("remoteDir") + localFile);
			sftpClient.rename(destination.get("remoteDir") + localFile + destination.get("tempFileExtension"), destination.get("remoteDir") + localFile);

			logger.info("delete "+source.get("localDir") + localFile);
			FileUtils.deleteQuietly(new File(source.get("localDir") + localFile));
			
			result=true;

		}
		catch (IOException e)
		{
			logger.error(e.getMessage());
		}
		
		return result;

	}

	public void requestStop()
	{
		run = false;
	}

	private void transferFiles()
	{
		
		File sourceDirectory = new File(source.get("localDir"));
		
		String[] filesNames = sourceDirectory.list(new WildcardFileFilter(source.get("localFileMask")));

		if (filesNames.length > 0)
		{
			String sendList = utils.getClientName() + " transferred the following file(s) \n\n";
			
			logger.info("Files found :" + filesNames.length);
			
			if (connect())
			{

				for (int i = 0; i < filesNames.length; i++)
				{

					File xxx = new File(source.get("localDir") + filesNames[i]);
					logger.info("File :" + filesNames[i]);

					if (xxx.isFile())
					{

						if (send(filesNames[i]))
						{
							sendList = sendList + filesNames[i]+"\n";
						}
					}

					xxx = null;
				}
				
				sendList = sendList + "\n\nto "+security.get("remoteHost");
				
				disconnect();
				
				emailqueue.addToQueue("Monitor", utils.replaceNullStringwithDefault(general.get("title"), "SFTP Send"), sendList , "");
			}
		}

	}

	private boolean loadConfigXML()
	{
		boolean result = false;

		logger.info("loadConfigXML");
		JCipher cipher = new JCipher(EncryptData.key);

		String xmlConfig = (System.getProperty("user.dir") + File.separator + "xml" + File.separator + "config" + File.separator + "sftpSend.xml");

		JXMLDocument xmlDoc;
		xmlDoc = new JXMLDocument(xmlConfig);

		// Config //

		int seq = 1;
		String id = "";
		String value = "";
		String encrypted = "";
		
		// General //
		
		seq = 1;

		while (xmlDoc.findXPath("/config/sftpSend2/general/value[" + String.valueOf(seq) + "]/@id").equals("") == false)
		{
			id = xmlDoc.findXPath("/config/sftpSend2/general/value[" + String.valueOf(seq) + "]/@id");
			value = xmlDoc.findXPath("/config/sftpSend2/general/value[" + String.valueOf(seq) + "]");
			encrypted = xmlDoc.findXPath("/config/sftpSend2/general/value[" + String.valueOf(seq) + "]/@encrypted");
			
			if (encrypted.equals("yes"))
			{
				value = cipher.decode(value);
			}

			if (id.equals("") == false)
			{
				general.put(id, value);
			}
			seq++;
		}

		// Security //

		seq = 1;

		while (xmlDoc.findXPath("/config/sftpSend2/security/value[" + String.valueOf(seq) + "]/@id").equals("") == false)
		{
			id = xmlDoc.findXPath("/config/sftpSend2/security/value[" + String.valueOf(seq) + "]/@id");
			value = xmlDoc.findXPath("/config/sftpSend2/security/value[" + String.valueOf(seq) + "]");
			encrypted = xmlDoc.findXPath("/config/sftpSend2/security/value[" + String.valueOf(seq) + "]/@encrypted");
			
			if (encrypted.equals("yes"))
			{
				value = cipher.decode(value);
			}

			if (id.equals("") == false)
			{
				security.put(id, value);
			}
			seq++;
		}

		// Source //

		seq = 1;

		while (xmlDoc.findXPath("/config/sftpSend2/source/value[" + String.valueOf(seq) + "]/@id").equals("") == false)
		{
			id = xmlDoc.findXPath("/config/sftpSend2/source/value[" + String.valueOf(seq) + "]/@id");
			value = xmlDoc.findXPath("/config/sftpSend2/source/value[" + String.valueOf(seq) + "]");
			encrypted = xmlDoc.findXPath("/config/sftpSend2/source/value[" + String.valueOf(seq) + "]/@encrypted");

			if (encrypted.equals("yes"))
			{
				value = cipher.decode(value);
			}

			if (id.equals("") == false)
			{
				source.put(id, value);
			}
			seq++;
		}

		// Destination //

		seq = 1;

		while (xmlDoc.findXPath("/config/sftpSend2/destination/value[" + String.valueOf(seq) + "]/@id").equals("") == false)
		{
			id = xmlDoc.findXPath("/config/sftpSend2/destination/value[" + String.valueOf(seq) + "]/@id");
			value = xmlDoc.findXPath("/config/sftpSend2/destination/value[" + String.valueOf(seq) + "]");
			encrypted = xmlDoc.findXPath("/config/sftpSend2/destination/value[" + String.valueOf(seq) + "]/@encrypted");

			if (encrypted.equals("yes"))
			{
				value = cipher.decode(value);
			}
			
			if (id.equals("") == false)
			{
				destination.put(id, value);
			}
			seq++;
		}

		
		source.put("localDir", Transfer.utils.formatPathTerminator(source.get("localDir")));
		destination.put("remoteDir", destination.get("remoteDir"));
		
		return result;
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
