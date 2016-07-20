package com.commander4j.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;

import com.commander4j.sys.Common;

public class JQuantityInput extends JFormattedTextField
{

	private static final long serialVersionUID = 1L;
	
	public JQuantityInput(BigDecimal arg0)
	{
		super(new DecimalFormat("###,##0.000"));
		this.setValue(arg0);
		this.setFont(Common.font_std);
	}
	
	public BigDecimal getQuantity()
	{
		BigDecimal result;

		result = JUtility.stringToBigDecimal(getText());

		return result;
	}
		
}
