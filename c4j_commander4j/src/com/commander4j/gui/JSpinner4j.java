
package com.commander4j.gui;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.commander4j.sys.Common;

public class JSpinner4j extends JSpinner
{

	private static final long serialVersionUID = 1L;
	private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);

	public JSpinner4j()
	{
		super();
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
		applyStyle();
	}

	public JSpinner4j(NumberEditor ne)
	{
		super();
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
		applyStyle();
	}
	
	public JSpinner4j(SpinnerModel model)
	{
		super(model);
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
		applyStyle();
	}

	private void applyFocusHighlight()
	{
		SwingUtilities.invokeLater(() -> {
			JComponent editor = getEditor();
			if (editor instanceof DefaultEditor)
			{
				JFormattedTextField textField = ((DefaultEditor) editor).getTextField();
				
				textField.setFont(Common.font_std);
				
				textField.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusGained(FocusEvent e)
					{
						textField.setBackground(Common.color_textfield_background_focus_color);
					}

					@Override
					public void focusLost(FocusEvent e)
					{
						textField.setBackground(Common.color_textfield_background_nofocus_color);
					}
				});
			}
		});
	}
	

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
	}

	@Override
	public void setEditor(JComponent editor)
	{
		super.setEditor(editor);
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
	}

	public void setEditable(boolean editable)
	{
		JTextField tf = getEditorTextField();
		if (tf != null)
		{
			tf.setEditable(editable);
		}
		setBorder(EMPTY_BORDER);
		applyFocusHighlight();
	}

	public boolean isEditable()
	{
		JTextField tf = getEditorTextField();
		return tf != null && tf.isEditable();
	}

	private JTextField getEditorTextField()
	{
		if (getEditor() instanceof DefaultEditor de)
		{
			return de.getTextField();
		}
		return null;
	}

	private void applyStyle()
	{
		JTextField tf = getEditorTextField();
		if (tf == null)
			return;

		tf.setDisabledTextColor(Common.color_textfield_foreground_disabled);

		if (!isEnabled())
		{
			tf.setBackground(Common.color_textfield_background_disabled);
			tf.setForeground(Common.color_textfield_foreground_disabled);
		}
		else if (!isEditable())
		{
			tf.setBackground(Common.color_textfield_background_disabled);
			tf.setForeground(Common.color_textfield_foreground_disabled);
		}
		else
		{
			tf.setBackground(Common.color_textfield_background_nofocus_color);
			tf.setForeground(Common.color_textfield_forground_nofocus_color);
		}
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
		applyStyle();
	}
	
	@Override
	public Object getValue() {
	    try {
	        if (getEditor() instanceof DefaultEditor editor) {
	            editor.getTextField().commitEdit();
	        }
	    } catch (java.text.ParseException ex) {
	        // Ignore or handle invalid input gracefully
	    }
	    return super.getValue();
	}

}
