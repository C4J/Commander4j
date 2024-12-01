package com.commander4j.bom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class JTreePrintPreview
{

	public static void showPrintPreview(JTree tree)
	{
		JDialog previewDialog = new JDialog((Frame) null, "Print Preview", true);

		JPanel previewPanel = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
				double scale = Math.min(getWidth() / pf.getImageableWidth(), getHeight() / pf.getImageableHeight());
				g2d.translate(50, 50); // Offset for the preview panel's margin
				g2d.scale(scale, scale);

				tree.printAll(g2d);
			}
		};

		previewPanel.setPreferredSize(new Dimension(600, 800)); 
		JScrollPane scrollPane = new JScrollPane(previewPanel);

		JPanel buttonPanel = new JPanel();
		JButton printButton = new JButton("Print");
		printButton.addActionListener(e -> {
			previewDialog.dispose();
			printTree(tree);
		});

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(e -> previewDialog.dispose());

		buttonPanel.add(printButton);
		buttonPanel.add(closeButton);

		previewDialog.setLayout(new BorderLayout());
		previewDialog.add(scrollPane, BorderLayout.CENTER);
		previewDialog.add(buttonPanel, BorderLayout.SOUTH);
		previewDialog.pack();
		previewDialog.setLocationRelativeTo(null);
		previewDialog.setVisible(true);
	}

	public static void printTree(JTree tree)
	{
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable((g, pf, pageIndex) -> {
			if (pageIndex > 0)
				return Printable.NO_SUCH_PAGE;

			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			double scale = Math.min(pf.getImageableWidth() / tree.getWidth(), pf.getImageableHeight() / tree.getHeight());
			g2d.scale(scale, scale);
			tree.printAll(g2d);

			return Printable.PAGE_EXISTS;
		});

		if (job.printDialog())
		{
			try
			{
				job.print();
			}
			catch (PrinterException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(tree, "Printing failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
