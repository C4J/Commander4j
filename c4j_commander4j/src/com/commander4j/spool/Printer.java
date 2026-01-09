package com.commander4j.spool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

public class Printer extends Thread
{
	private final Logger logger = org.apache.logging.log4j.LogManager.getLogger(Printer.class);
	
	public String ip = "";
	private int port = -1;
	public ConcurrentLinkedQueue<PrintJob> queue = new ConcurrentLinkedQueue<PrintJob>();
	
	public boolean running = true;
	
	public boolean abort = false;
	
	private Duration connectTimeout = Duration.ofSeconds(3);
	private Duration readTimeout = Duration.ofSeconds(10);
		
	private Path filename;
	private PrintJob pj = new PrintJob("", 0, "");

	public Printer(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
		setName("Device " + ip + ":" + String.valueOf(port));
	}

	public void run()
	{
		try
		{
			while (running == true)
			{

				if (queue.size() > 0)
				{

					pj = getJob();
					
					logger.info("printJob " + pj);
					
					boolean success = false;

					while ((!success) && (running))
					{

						filename = Paths.get(pj.getFileName());



						try (Socket socket = new Socket())
						{
							socket.connect(new InetSocketAddress(ip, port), (int) connectTimeout.toMillis());
							socket.setSoTimeout((int) readTimeout.toMillis());

							try (OutputStream out = socket.getOutputStream(); InputStream in = Files.newInputStream(filename))
							{
								byte[] buffer = new byte[8192];
								int read;
								while ((read = in.read(buffer)) != -1)
								{
									boolean sent = false;
									while (!sent)
									{
										try
										{
											out.write(buffer, 0, read);
											sent = true;
											removeJob(pj);
										}
										catch (IOException e)
										{
											//running = false;
										}
									}
								}
								out.flush();
								success = true;
								in.close();

							}

						}
						catch (IOException e)
						{
							//running = false;
						}
						finally
						{
							if (abort)
							{
								running = false;
							}
						}
					}

					Thread.sleep(1000);
				}
			}
		}
		catch (InterruptedException e)
		{
			running = false;
		}

		archiveUnprocessedJobs();
	}

	public void archiveUnprocessedJobs()
	{
		while (queue.size() > 0)
		{
			PrintJob cleanup = queue.poll();
			removeJob(cleanup);
		}
	}

	public int getJobCount()
	{
		return queue.size();
	}

	public PrintJob getActiveJob()
	{
		return pj;
	}

	public int shutdown()
	{
		int result = queue.size();

		if (result == 0)
		{
			logger.info("Shutdown "+getName());
			running = false;
		}
		else
		{
			logger.warn("Shutdown Cancelled Queue size = " + result);
		}

		return result;
	}

	public int abort()
	{
		int result = queue.size();

		logger.info("Abort "+getName());
		abort = true;
		running=false;

		return result;
	}

	public void submitJob(PrintJob rqt)
	{

		if (new File(rqt.getFileName()).isFile())
		{
			logger.info("submitJob " + rqt + " to "+getName());
			queue.add(rqt);
		}
		else
		{
			logger.error("submitJob " + rqt.getFileName() + " not found !");
		}
	}

	public PrintJob getJob()
	{
		PrintJob result;
		result = queue.peek();
		logger.info("getJob " + result);
		return result;
	}

	public void removeJob(PrintJob job)
	{
		File inputFile = new File(job.getFileName());
		File archivePath = new File(PrintManager.archiveFolder);

		logger.info("removeJob " + job);

		try
		{
			FileUtils.moveToDirectory(inputFile, archivePath, true);
		}
		catch (IOException e)
		{

			logger.error("Error moving spool file to archive folder");
			logger.error(e.getMessage());

			FileUtils.deleteQuietly(inputFile);

		}

		queue.remove(job);

	}

}
