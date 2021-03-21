package com.commander4j.Transformation;

import com.commander4j.util.Utility;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class padEAN extends ExtensionFunctionDefinition
{
	Utility util = new Utility();
	
	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{SequenceType.OPTIONAL_STRING};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext", "http://com.commander4j.Transformation", "padEAN");
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
	            	
	            	String value;
	            	try
	            	{
	            		value =  arguments[0].head().getStringValue().trim();
	            	} catch (ClassCastException ex)
	            	{
	            	   value = "";	
	            	}
	            	catch (NullPointerException ex)
	            	{
	            		 value = "";	
	            	}
	                
	        		String result = value;

	        		if (result.equals("") == false)
	        		{

	        			result = util.padString(value, false, 14, "0");
	        		}

	                return StringValue.makeStringValue(result);
	            }
	        };
	}

}
