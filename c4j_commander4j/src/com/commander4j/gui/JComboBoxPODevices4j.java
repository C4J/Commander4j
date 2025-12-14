package com.commander4j.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.commander4j.bar.JLabelPrint;
import com.commander4j.db.JDBModule;
import com.commander4j.db.JDBModuleAlternative;
import com.commander4j.db.JDBWorkstationPreferences;
import com.commander4j.print.JPrintDevice;
import com.commander4j.print.JPrintDevices;
import com.commander4j.renderer.JPrintDeviceRenderer;
import com.commander4j.sys.Common;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

public class JComboBoxPODevices4j extends JComboBox<JPrintDevice> implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Border EMPTY_BORDER = new LineBorder(Color.GRAY);
    private JPrintDeviceRenderer renderer;
    private LinkedList<JPrintDevice> printDeviceList ;
    private DefaultComboBoxModel<JPrintDevice> model;
	private String workstation;
	private JPrintDevices printDevices;
	private JDBWorkstationPreferences workstationPrefs;
	private JDBModule mod;
	private String moduleid = "";
	private JDBModuleAlternative modalt;
	private JLabelPrint lblprint;
	private String groupid="";
	private String hostID="";
	private String SessionID="";

    public String getHostID()
	{
		return hostID;
	}

	public void setHostID(String hostID)
	{
		this.hostID = hostID;
	}

	public String getSessionID()
	{
		return SessionID;
	}

	public void setSessionID(String sessionID)
	{
		SessionID = sessionID;
	}

	private void init()
    {
        setUI(new FlatComboBoxUI());
		setFont(Common.font_combo);
        setFocusable(false);
        setBorder(EMPTY_BORDER);
    }
    
	public JComboBoxPODevices4j(JPrintDevice[] items) {
        super(items);
        init();
    }
	
	public JComboBoxPODevices4j() {
		super();
        init();
	}
	
	public JComboBoxPODevices4j(String host, String session,String module,String processOrder) {
		super();
        init();
        
        setHostID(host);
        setSessionID(session);
        
        refreshData(module,processOrder);
        
        addActionListener(this); 
	}
	
	public void refreshData(String module,String processOrder)
	{
		mod = new JDBModule(getHostID(), getSessionID());
        modalt = new JDBModuleAlternative(getHostID(), getSessionID());
		workstationPrefs = new JDBWorkstationPreferences(getHostID(), getSessionID());
        printDevices = new JPrintDevices(getHostID(), getSessionID());
        lblprint = new JLabelPrint(getHostID(), getSessionID());
        
        if (mod.getModuleProperties(module))
        {
        	groupid = mod.getGroupID();
        }
        else
        {
        	groupid = "Other";
        }
        
        switch (groupid) {
        case "Pallet":
        	moduleid = lblprint.getPalletLabelReportName(processOrder);
        	break;
        case "Pack":
        	moduleid = lblprint.getPackLabelReportName(processOrder);
        	break;
        default:
        	moduleid = module;
        	break;
        }
        
        moduleid = modalt.substituteAlternative(moduleid);
        
		workstation = JUtility.getClientName();
		
        printDeviceList = printDevices.getDevices(moduleid);
        
        model = getModelData(moduleid);
        
        renderer = new JPrintDeviceRenderer();
        
        setModel(model);
        setRenderer(renderer);
        int sel = getIndex();
        setSelectedIndex(sel);
        
        if (sel>=0)
        {
        	saveSelection(printDeviceList.get(sel));
        }
        
	}
	
	private DefaultComboBoxModel<JPrintDevice>  getModelData(String moduleid)
	{
		DefaultComboBoxModel<JPrintDevice> result = new DefaultComboBoxModel<JPrintDevice>();

		for (int j = 0; j < printDeviceList.size(); j++)
		{			
			result.addElement(printDeviceList.get(j));
		}
		
		return result;
	}
	
	private int getIndex()
	{
		
		String printerId = "";
		String deviceType = "";
		
		int sel = -1;

		if (workstationPrefs.getProperties(workstation, moduleid))
		{
			deviceType = workstationPrefs.getDeviceType();
			printerId = workstationPrefs.getPrinterId();
		}
		else
		{
			deviceType = "queue";
			printerId = JPrint.getDefaultPrinterQueueName();
		}

		for (int j = 0; j < printDeviceList.size(); j++)
		{
			if (deviceType.equals("printer"))
			{
				if (printDeviceList.get(j).getPrinter() != null)
				{
					if (printDeviceList.get(j).getPrinter().getPrinterID().equals(printerId))
					{
						sel = j;
					}
				}
			}
			if (deviceType.equals("queue"))
			{
				if (printDeviceList.get(j).getQueue() != null)
				{
					if (printDeviceList.get(j).getQueue().getName().equals(printerId))
					{
						sel = j;
					}
				}

			}
		}
		
		if (sel == -1)
		{
			if (printDeviceList.size()>0)
			{
				sel = 0;
			}
		}
		
		return sel;
	}

	public JComboBoxPODevices4j(String[] fieldAlignment)
	{
		super();
        init();
	}

    private static class FlatComboBoxUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton(new FlatArrowIcon());

            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(EMPTY_BORDER);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(230, 230, 230));
                    button.setOpaque(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setOpaque(false);
                }
            });

            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Optional: remove highlight rectangle if desired
        }
    }

    private static class FlatArrowIcon implements Icon {
        private final int size = 10;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = size;
            int h = size / 2;

            int[] xPoints = { x, x + w / 2, x + w };
            int[] yPoints = { y, y + h, y };

            g2.setColor(Color.DARK_GRAY);
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size / 2;
        }
    }
    
    private void saveSelection(JPrintDevice selected)
    {
		if (selected.getType().equals("printer"))
		{
		    workstationPrefs.save(workstation, moduleid, selected.getType(), selected.getPrinter().getPrinterID());
		}
		else
		{
			workstationPrefs.save(workstation, moduleid, selected.getType(), selected.toString());
		}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
		JPrintDevice selected = (JPrintDevice) getSelectedItem();
		
		saveSelection(selected);
    }
}
