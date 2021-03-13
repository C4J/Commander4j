package com.commander4j.Transformation;

import org.apache.commons.lang3.StringUtils;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class XSLT_Ext_removeLeadingZeros extends ExtensionFunctionDefinition
{

	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{SequenceType.SINGLE_STRING};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext_removeLeadingZeros", "http://com.commander4j.Transformation.XSLT_Ext_removeLeadingZeros", "removeLeadingZeros");
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
	            		value =  arguments[0].head().getStringValue();
	            	} catch (ClassCastException ex)
	            	{
	            	   value = "";	
	            	}
	                
	        		String result = StringUtils.stripStart(value, "0");

	                return StringValue.makeStringValue(result);
	            }
	        };
	}

}
