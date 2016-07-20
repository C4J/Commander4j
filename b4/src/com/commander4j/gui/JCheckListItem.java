package com.commander4j.gui;

public class JCheckListItem
{
   private Object  label;
   private boolean isSelected = false;

   public JCheckListItem(Object label)
   {
      this.label = label;
   }

   public boolean isSelected()
   {
      return isSelected;
   }

   public void setSelected(boolean isSelected)
   {
      this.isSelected = isSelected;
   }
   
   public Object getValue()
   {
	   return this.label;
   }

   public String toString()
   {
      return label.toString();
   }
}
