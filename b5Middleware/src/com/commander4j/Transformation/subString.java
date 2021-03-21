package com.commander4j.Transformation;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class subString extends ExtensionFunctionDefinition
{

	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{SequenceType.OPTIONAL_STRING,SequenceType.SINGLE_INTEGER, SequenceType.SINGLE_INTEGER};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext", "http://com.commander4j.Transformation", "subString");
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
	            	
	            	String inputStr;
	            	try
	            	{
	            	    inputStr = arguments[0].head().getStringValue();

	            	} catch (ClassCastException ex)
	            	{
	            	   inputStr = "";	
	            	}
	            	catch (NullPointerException ex)
	            	{
	            		inputStr = "";	
	            	}

	            	
	                long startPos;
	            	try
	            	{
	            		startPos = Integer.valueOf(arguments[1].head().getStringValue());
	            	} catch (ClassCastException ex)
	            	{
	            		startPos = 1;	
	            	}
	            	
	                long strLength;
	            	try
	            	{
	            		strLength = Integer.valueOf(arguments[2].head().getStringValue());
	            	} catch (ClassCastException ex)
	            	{
	            		strLength = 1;	
	            	}
	            	
	            	String result = inputStr;
	            	

	        		result = result.substring((int) startPos,(int) (startPos + strLength));

	                return StringValue.makeStringValue(result);
	            }
	        };
	}

}
