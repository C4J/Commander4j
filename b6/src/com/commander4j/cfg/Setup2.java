package com.commander4j.cfg;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : Setup.java
 * 
 * Package Name : com.commander4j.cfg
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import org.apache.logging.log4j.Logger;

import com.commander4j.sys.Common;
import com.commander4j.util.JPlaySound;
import com.commander4j.util.JPrint;
import com.commander4j.util.JUtility;

public class Setup2
{

	public static void main(String[] args) {
		final Logger logger = org.apache.logging.log4j.LogManager.getLogger(Setup2.class);
		//JUtility.setLookandFeel();
		JUtility.initLogging("");
		Common.base_dir = System.getProperty("user.dir");
		Common.applicationMode = "SwingClient";
		JPlaySound.enable();
		logger.info("Application starting");
		JPrint.init();
		JFrameHostAdmin hostadmin = new JFrameHostAdmin();
		hostadmin.setIconImage(Common.imageIconloader.getImageIcon16x16(Common.image_osx_setup4j).getImage());
		hostadmin.setVisible(true);

	}

}
