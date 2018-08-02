package com.commander4j.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.Logger;

public class FTP_Send
{
	Logger logger = org.apache.logging.log4j.LogManager.getLogger((FTP_Send.class));
	FTPClient ftp = null;

	public synchronized boolean send(String server, int port, String path, String shortFilename, String filename, String username, String password)
	{
		boolean result = true;

		try
		{
			ftp = new FTPClient();
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			int reply;
			
			logger.error("FTPClient connecting to " +server);
			ftp.connect(server);
			
			reply = ftp.getReplyCode();
			
			if (!FTPReply.isPositiveCompletion(reply))
			{
				result = false;
				logger.error("FTPClient disconnecting");
				ftp.disconnect();
			}
			
			logger.error("FTPClient logging in as " +username);
			ftp.login(username, password);
			
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			
			ftp.enterLocalPassiveMode();
			
			logger.error("FTPClient uploading file :"+shortFilename);
			uploadFile(filename, shortFilename, path);
			
			logger.error("FTPClient disconnecting");
			disconnect();
			ftp = null;
		}
		catch (Exception ex)
		{
			logger.error("FTPClient error : " +ex.getMessage());
			result = false;
		}

		return result;
	}

	public void uploadFile(String localFileFullName, String fileName, String hostDir) throws Exception
	{
		try (InputStream input = new FileInputStream(new File(localFileFullName)))
		{
			this.ftp.storeFile(hostDir + fileName, input);
		}
	}

	public void disconnect()
	{
		if (this.ftp.isConnected())
		{
			try
			{
				this.ftp.logout();
				this.ftp.disconnect();
			}
			catch (IOException f)
			{
				// do nothing as file is already saved to server
			}
		}
	}
}