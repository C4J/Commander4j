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

public class XSLT_Ext_concat extends ExtensionFunctionDefinition
{

	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext_concat", "http://com.commander4j.Transformation.XSLT_Ext_concat", "concat");
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
	            	
	            	String value1;
	            	try
	            	{
	            		value1 =  arguments[0].head().getStringValue();
	            	} catch (ClassCastException ex)
	            	{
	            	   value1 = "";	
	            	}

	                String value2;
	            	try
	            	{
	            		value2 =  arguments[1].head().getStringValue();
	            	} catch (ClassCastException ex)
	            	{
	            		value2 = "";	
	            	}
	                
	        		String result = Utility.replaceNullStringwithBlank(value1) + Utility.replaceNullStringwithBlank(value2);


	                return StringValue.makeStringValue(result);
	            }
	        };
	}

}
