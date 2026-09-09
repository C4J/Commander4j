
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
	private transient boolean committingEdit = false;
	private boolean inError = false;
	private transient JFormattedTextField highlightedField = null;

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

				// applyFocusHighlight() also runs from setEnabled()/setEditable(), which do not
				// replace the text field - only attach the listener once per field.
				if (textField == highlightedField)
				{
					return;
				}

				highlightedField = textField;

				textField.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusGained(FocusEvent e)
					{
						applyStyle();
					}

					@Override
					public void focusLost(FocusEvent e)
					{
						applyStyle();
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
			tf.setBackground(inError ? Common.color_textfield_background_disabled_error : Common.color_textfield_background_disabled);
			tf.setForeground(Common.color_textfield_foreground_disabled);
		}
		else if (!isEditable())
		{
			tf.setBackground(inError ? Common.color_textfield_background_disabled_error : Common.color_textfield_background_disabled);
			tf.setForeground(Common.color_textfield_foreground_disabled);
		}
		else if (tf.isFocusOwner())
		{
			tf.setBackground(inError ? Common.color_textfield_background_focus_error_color : Common.color_textfield_background_focus_color);
			tf.setForeground(Common.color_textfield_foreground_focus_color);
		}
		else
		{
			tf.setBackground(inError ? Common.color_textfield_background_nofocus_error_color : Common.color_textfield_background_nofocus_color);
			tf.setForeground(Common.color_textfield_foreground_nofocus_color);
		}
	}

	/**
	 * Flag the spinner as holding a value which was not accepted. Mirrors JTextField4j.inError().
	 */
	public void inError(boolean error)
	{
		inError = error;
		applyStyle();
	}

	public boolean isInError()
	{
		return inError;
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
		applyStyle();
	}
	
	/**
	 * Push a value the user has typed but not yet committed into the spinner's model.
	 *
	 * JButton4j calls setFocusable(false), so clicking a button never moves focus off the
	 * spinner and the normal focus-lost commit never happens. Any code that reads the typed
	 * value from somewhere other than getValue() - e.g. straight off the SpinnerModel - must
	 * call this first or it will see the previous committed value.
	 *
	 * Re-entrant safe: commitEdit() fires a "value" property change which routes back through
	 * setValue(), so the flag stops a second commit part way through the first.
	 */
	public void commitPendingEdit()
	{
		if (committingEdit)
			return;

		committingEdit = true;

		try
		{
			if (getEditor() instanceof DefaultEditor editor)
			{
				try
				{
					editor.getTextField().commitEdit();
					inError = false;
				}
				catch (java.text.ParseException ex)
				{
					// Invalid or out of range input. Keep what the user typed rather than
					// discarding their keystrokes, but flag the field so it is obvious the
					// value was not accepted and the previous one is still in force.
					inError = true;
				}

				applyStyle();
			}
		}
		finally
		{
			committingEdit = false;
		}
	}

	@Override
	public Object getValue()
	{
		commitPendingEdit();

		return super.getValue();
	}

}
