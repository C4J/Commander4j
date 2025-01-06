package com.commander4j.exception;

import java.util.LinkedList;

import com.commander4j.email.EmailHTML;

public class ExceptionHTML
{

	LinkedList<ExceptionMsg> msgList = new LinkedList<ExceptionMsg>();
	String caption = "";
	String col1Title = "";
	String col2Title = "";
	String t1Width = "";
	String t2Width = "";
	
	public ExceptionHTML(String caption,String title1,String t1Width,String title2,String t2Width)
	{
		this.caption = caption;
		this.col1Title = title1;
		this.col2Title = title2;
		this.t1Width = t1Width;
		this.t2Width = t2Width;
	}
	
	public String getHTML()
	{
		
		String  result = EmailHTML.header 
				+ "<div id=\"errorReport\" >\n"
				+ " <table border=\"3\">\n"
				+ "  <thead>\n"
				+ "    <caption>"+caption+"</caption>\n"
				+ "    <tr>\n"
				+ "      <th style=\"width:"+t1Width+"; text-align: center\">"+col1Title+"</th>\n"
				+ "      <th style=\"width:"+t2Width+"; text-align: center\">"+col2Title+"</th>\n"
				+ "    </tr>\n"
				+ "  </thead>\n"
				+ "  <tbody>\n";
		
		if (msgList.size() > 0)
		{
			for (int x=0;x<msgList.size();x++)
			{
				result = result 
				+ "   <tr>\n"
				+ "  	<td style=\"width:"+t1Width+"; text-align: left\">"+msgList.get(x).description+"</td>\n"
				+ "  	<td style=\"width:"+t2Width+"; text-align: left\">"+msgList.get(x).content+"</td>\n"
				+ "   </tr>\n";
			}
		}
		
		result = result 
				+ "   </tbody>\n"
				+ " </table>\n"
				+ "</div>" 
				+ EmailHTML.footer;

		return result;
	}
	
	public void clear()
	{
		msgList.clear();
	}
	
	public void addRow(ExceptionMsg msg)
	{
		msgList.addLast(msg);
	}
	
}
