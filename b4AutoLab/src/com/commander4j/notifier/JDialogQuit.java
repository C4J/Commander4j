package com.commander4j.notifier;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.swing.WindowConstants;

public class JDialogQuit
{

	@SafeVarargs
	static <T> T showDialogAndSelectOption(Object message, String title, int messageType, T... options)
	{
		ImageIcon icon = new ImageIcon("./images/image_confirm.png");

		JOptionPane optionPane = new JOptionPane(message, messageType, 0, icon, options);

		JDialog dialog = optionPane.createDialog(title);

		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setModalityType(JDialog.ModalityType.TOOLKIT_MODAL);
		dialog.setVisible(true);

		@SuppressWarnings("unchecked")
		T selectedOption = (T) optionPane.getValue();
		return selectedOption;
	}
}