package com.commander4j.clone;

import org.apache.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

public class Clone
{

	public static void main(String[] args) {
		final Logger logger = Logger.getLogger(Clone.class);
		//JUtility.setLookandFeel();
		JUtility.initLogging("");
		Common.base_dir = System.getProperty("user.dir");
		Common.applicationMode = "SwingClient";
		logger.info("Application starting");
		JPrint.init();
		JFrameCloneDB clonedb = new JFrameCloneDB();
		clonedb.setIconImage(Common.imageIconloader.getImageIcon(Common.image_osx_clone4j).getImage());
		clonedb.setVisible(true);

	}

}
