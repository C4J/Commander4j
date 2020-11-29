package com.commander4j.util;


public class JWait
{

	public void milliSec(long ms) {
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			System.out.println("JWait.millisec Exception :"+e.getLocalizedMessage());
		}
	}

	public void oneSec() {
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			System.out.println("JWait.oneSec Exception :"+e.getLocalizedMessage());
		}
	}

	public void manySec(long s) {
		try
		{
			Thread.sleep(s * 1000);
		}
		catch (InterruptedException e)
		{
			System.out.println("JWait.manySec Exception :"+e.getLocalizedMessage());
		}
	}

}
