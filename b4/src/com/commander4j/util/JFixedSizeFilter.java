package com.commander4j.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 */
public class JFixedSizeFilter extends DocumentFilter
{
	private int maxSize;

	public JFixedSizeFilter(int limit)
	{
		maxSize = limit;
	}

	public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
		replace(fb, offset, 0, str, attr);
	}

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
		int newLength = fb.getDocument().getLength() - length + str.length();
		if (newLength <= maxSize)
		{
			fb.replace(offset, length, str, attrs);
		}
		else
		{
			throw new BadLocationException("New characters exceeds max size of document", offset);
		}
	}
}
