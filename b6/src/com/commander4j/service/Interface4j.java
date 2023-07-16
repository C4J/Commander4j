package com.commander4j.service;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : Interface4j.java
 * 
 * Package Name : com.commander4j.service
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import com.commander4j.thread.InterfaceThread;

public class Interface4j {
	InterfaceThread interfaceThread;

	public Interface4j()
	{
	}

	public Integer start(String[] args)
	{

		interfaceThread = new InterfaceThread(args);

		interfaceThread.start();

		AddShutdownHookSample sample = new AddShutdownHookSample();
		sample.attachShutDownHook();

		try
		{
			while (interfaceThread.isAlive())
			{
				com.commander4j.util.JWait.milliSec(100);
			}
		} catch (Exception ex)
		{
		}

		return null;
	}

	public int stop(int exitCode)
	{

		try
		{
			while (interfaceThread.isAlive())
			{
				interfaceThread.requestStop();
				com.commander4j.util.JWait.milliSec(100);
			}
		} catch (Exception ex)
		{
		}

		return exitCode;
	}

	public class AddShutdownHookSample {
		public void attachShutDownHook()
		{
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run()
				{
					System.out.println("Shutdown detected - requesting threads stop.");

					interfaceThread.requestStop();
					System.out.println("Waiting for threads to stop.");
					while (interfaceThread.isAlive())
					{
						com.commander4j.util.JWait.milliSec(100);
					}
					System.out.println("All threads have stopped.");
				}
			});
			System.out.println("Shut Down Hook Active.");
		}
	}

	public static void main(String[] args)
	{
		Interface4j int4j = new Interface4j();
		System.out.println("Starting");
		int4j.start(args);
		System.out.println("Stopped");
	}
}
