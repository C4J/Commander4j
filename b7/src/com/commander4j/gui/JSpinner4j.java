
package com.commander4j.gui;

import javax.swing.*;

import com.commander4j.sys.Common;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JSpinner4j extends JSpinner
{

	private static final long serialVersionUID = 1L;


	public JSpinner4j()
	{
		super();
		applyFocusHighlight();
	}

	public JSpinner4j(SpinnerModel model)
	{
		super(model);
		applyFocusHighlight();
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

}
