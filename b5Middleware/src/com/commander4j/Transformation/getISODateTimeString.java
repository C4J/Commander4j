package com.commander4j.Transformation;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class getISODateTimeString extends ExtensionFunctionDefinition
{

	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext", "http://com.commander4j.Transformation", "getISODateTimeString");
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0)
	{
		 return SequenceType.SINGLE_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression()
	{
	       return new ExtensionFunctionCall() {
	            @Override
	            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
	            	
	        		String result = "";
	        		Timestamp ts = getSQLDateTime();
	        		try
	        		{
	        			String temp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(ts);
	        			result = temp.substring(0, 4);
	        			result = result + "-";
	        			result = result + temp.substring(5, 7);
	        			result = result + "-";
	        			result = result + temp.substring(8, 10);
	        			result = result + "T";
	        			result = result + temp.substring(11, 13);
	        			result = result + ":";
	        			result = result + temp.substring(14, 16);
	        			result = result + ":";
	        			result = result + temp.substring(17, 19);
	        		}
	        		catch (Exception ex)
	        		{
	        			result = "Error";
	        		}

	                return StringValue.makeStringValue(result);
	            }
	        };
	}
	
	public Timestamp getSQLDateTime()
	{
		Calendar caldate = Calendar.getInstance();
		Timestamp t = new Timestamp(caldate.getTimeInMillis());
		t.setTime(caldate.getTimeInMillis());
		return t;
	}

}
