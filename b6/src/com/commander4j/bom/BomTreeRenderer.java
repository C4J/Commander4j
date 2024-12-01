package com.commander4j.bom;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.commander4j.sys.Common;


public class BomTreeRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1;

	public BomTreeRenderer()
	{

	}


	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		try
		{
			setFont(Common.font_tree);
			
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();

			setIcon(((JDBBomRecord) nodeObj).getIcon());
			
			setToolTipText(getMenuHint(value));
		}
		catch (Exception ex)
		{

		}

		return this;
	}
	

	protected String getMenuHint(Object value) {

		Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();

		String type =  "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;uuid="+((JDBBomRecord) nodeObj).getUuid() +"<br>parent_uuid="+((JDBBomRecord) nodeObj).getParent_uuid()+"<br>sequence="+((JDBBomRecord) nodeObj).getBOMSequence()+"</html>";
		return type;
	}

}
