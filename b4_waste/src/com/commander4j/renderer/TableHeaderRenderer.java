package com.commander4j.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;

import com.commander4j.sys.Common;

/**
 * A simple renderer class for JTable component.
 * 
 * @author www.codejava.net
 *
 */
public class TableHeaderRenderer extends JLabel implements TableCellRenderer
{

	private static final long serialVersionUID = 1L;

    public TableHeaderRenderer() {
        setFont(Common.font_table_header);
        setOpaque(true);
        setForeground(Color.WHITE);
        setBackground(new Color(0,127,195));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setHorizontalAlignment(JLabel.CENTER);
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        return this;
    }

}