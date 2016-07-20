package com.commander4j.cfg;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

public class Setup
{

	public static void main(String[] args) {
		final Logger logger = Logger.getLogger(Setup.class);
		//JUtility.setLookandFeel();
		JUtility.initLogging("");
		Common.base_dir = System.getProperty("user.dir");
		Common.applicationMode = "SwingClient";
		logger.info("Application starting");
		JPrint.init();
		JFrameHostAdmin hostadmin = new JFrameHostAdmin();
		hostadmin.setIconImage(Common.imageIconloader.getImageIcon(Common.image_osx_setup4j).getImage());
		hostadmin.setVisible(true);

	}

}
