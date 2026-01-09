package com.commander4j.docfilter;

import java.awt.Toolkit;
import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class JDocFilterUpperAlphaNum extends DocumentFilter {

    private final boolean beepOnReject;

    public JDocFilterUpperAlphaNum() {
        this(false);
    }

    public JDocFilterUpperAlphaNum(boolean beepOnReject) {
        this.beepOnReject = beepOnReject;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        if (text == null) return;

        String filtered = filter(text);
        if (filtered.isEmpty() && !text.isEmpty()) {
            if (beepOnReject) Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(fb, offset, filtered, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) {
            super.replace(fb, offset, length, null, attrs);
            return;
        }

        String filtered = filter(text);
        if (filtered.isEmpty() && !text.isEmpty()) {
            if (beepOnReject) Toolkit.getDefaultToolkit().beep();
            // still allow the replace if user is deleting (text empty) — but here text wasn't empty
            return;
        }
        super.replace(fb, offset, length, filtered, attrs);
    }

    private static String filter(String input) {
        // Locale.ROOT avoids Turkish-i style casing surprises
        String up = input.toUpperCase(Locale.ROOT);

        StringBuilder sb = new StringBuilder(up.length());
        for (int i = 0; i < up.length(); i++) {
            char c = up.charAt(i);
            if ((c >= 'A' && c <= 'Z') ||
                (c >= '0' && c <= '9') ||
                c == '.' || c == ',') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
