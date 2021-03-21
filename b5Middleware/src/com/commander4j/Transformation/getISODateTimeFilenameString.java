package com.commander4j.Transformation;

import java.sql.Timestamp;
import java.util.Calendar;

import com.commander4j.util.Utility;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

public class getISODateTimeFilenameString extends ExtensionFunctionDefinition
{
	Utility util = new Utility();
	
	@Override
	public SequenceType[] getArgumentTypes()
	{
		return new SequenceType[]{};
	}

	@Override
	public StructuredQName getFunctionQName()
	{
		return new StructuredQName("c4j_XSLT_Ext", "http://com.commander4j.Transformation", "getISODateTimeFilenameString");
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
	            	
	        		String result = util.getISODateTimeString();
	        		result = result.replace(":", "_");
	        		result = result.replace("-", "_");
	        		result = result.replace(" ", "_");
	        		result = result.replace("T", "_");

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
