package com.commander4j.ftp;

public class FTP_Request
{
	public String server = "";
	public int port = 21;
	public String remotePath = "";
	public String remoteFilename = "";
	public String username = "";
	public String password = "";

	public FTP_Request(String server, int port, String path, String filename, String username, String password)
	{
		this.server = server;
		this.remotePath = path;
		this.remoteFilename = filename;
		this.username = username;
		this.password = password;
	}
}
