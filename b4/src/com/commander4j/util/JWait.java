package com.commander4j.util;

/**
 */
public class JWait
{

	/**
	 * Method milliSec.
	 * 
	 * @param ms
	 *            long
	 */
	public static void milliSec(long ms) {
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void oneSec() {
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method manySec.
	 * 
	 * @param s
	 *            long
	 */
	public static void manySec(long s) {
		try
		{
			Thread.sleep(s * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
