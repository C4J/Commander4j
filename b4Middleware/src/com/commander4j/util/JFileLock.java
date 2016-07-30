package com.commander4j.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class JFileLock
{
	public boolean isLockable(String filename)
	{
		boolean result = false;

		try
		{

			// Get a file channel for the file
			File file = new File("filename");
			@SuppressWarnings("resource")
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();

			// Use the file channel to create a lock on the file.
			// This method blocks until it can retrieve the lock.
			FileLock lock = channel.lock();

			// Try acquiring the lock without blocking. This method returns
			// null or throws an exception if the file is already locked.
			try
			{
				lock = channel.tryLock();
				result=true;
			} catch (OverlappingFileLockException e)
			{
				// File is already locked in this thread or virtual machine
			}

			// Remember to release the lock
			lock.release();

			// Close the file
			channel.close();

		} catch (Exception e)
		{
		}

		return result;
	}
}
