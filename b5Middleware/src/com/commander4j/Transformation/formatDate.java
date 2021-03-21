package com.commander4j.Transformation;

import java.text.SimpleDateFormat;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class formatDate extends ExtensionFunctionDefinition
{

	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{SequenceType.OPTIONAL_STRING,SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext", "http://com.commander4j.Transformation", "formatDate");
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
	            	
	            	String inputString;
	            	try
	            	{
	            		inputString = arguments[0].head().getStringValue();

	            	} catch (ClassCastException ex)
	            	{
	            		inputString = "";	
	            	}
	            	catch (NullPointerException ex)
	            	{
	            		inputString = "";	
	            	}
	            	
	            	String formatFrom;
	            	try
	            	{
	            		formatFrom = arguments[1].head().getStringValue();

	            	} catch (ClassCastException ex)
	            	{
	            		formatFrom = "";	
	            	}
	            	
	            	
	            	String formatTo;
	            	try
	            	{
	            		formatTo = arguments[2].head().getStringValue();

	            	} catch (ClassCastException ex)
	            	{
	            		formatTo = "";	
	            	}
	            	
	            	
	        		String result = inputString;

	        		SimpleDateFormat fromUser = new SimpleDateFormat(formatFrom);
	        		SimpleDateFormat myFormat = new SimpleDateFormat(formatTo);

	        		try
	        		{

	        			result = myFormat.format(fromUser.parse(inputString));

	        		}
	        		catch (Exception e)
	        		{

	        			result = e.getMessage() + "[" + inputString + "],[" + formatFrom + "],[" + formatTo + "]";

	        		}

	                return StringValue.makeStringValue(result);
	            }
	        };
	}

}
