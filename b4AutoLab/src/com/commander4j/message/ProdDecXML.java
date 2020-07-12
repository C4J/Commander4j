package com.commander4j.message;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.commander4j.output.ProcDec_XML;

public class ProdDecXML
{

	ConcurrentLinkedQueue<ProcDec_XML> ouboundList = new ConcurrentLinkedQueue<ProcDec_XML>();
	
	public boolean addToQueue(ProcDec_XML msg)
	{
		boolean result = false;
		
		ouboundList.add(msg);
		
		return result;
	}
	
	public ProcDec_XML remoteFromQueue()
	{
		ProcDec_XML result = ouboundList.remove();
		
		return result;
	}
	
	public int queueSize()
	{
		int result = ouboundList.size();
		
		return result;
	}
	
}
