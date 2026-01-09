package com.commander4j.docfilter;

import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class JDocFilterLowercase extends DocumentFilter {

    private final Locale locale;

    public JDocFilterLowercase() {
        this(Locale.getDefault());
    }

    public JDocFilterLowercase(Locale locale) {
        this.locale = (locale == null) ? Locale.getDefault() : locale;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        if (text == null) return;
        super.insertString(fb, offset, text.toLowerCase(locale), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) return;
        super.replace(fb, offset, length, text.toLowerCase(locale), attrs);
    }
}
