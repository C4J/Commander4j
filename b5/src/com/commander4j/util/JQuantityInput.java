package com.commander4j.util;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JQuantityInput.java
 * 
 * Package Name : com.commander4j.util
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

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
	
	public void setQuantity(BigDecimal qty)
	{
		setText(qty.toString());
	}
		
}
