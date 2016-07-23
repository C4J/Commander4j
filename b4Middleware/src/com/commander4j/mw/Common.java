package com.commander4j.mw;

import java.io.File;

import javax.swing.Icon;

import com.commander4j.util.JImageIconLoader;

public class Common {
	
	public static final JImageIconLoader imageIconloader = new JImageIconLoader();
	
	public static String logDir="";
	public final static String image_path = System.getProperty("user.dir")+File.separator+"images"+File.separator;
	
	public final static String image_cancel = "cancel.gif";
	public final static String image_ok = "ok.gif";
	public final static String image_error = "error.gif";
	public final static String image_close = "exit.gif";


	public static Icon icon_ok = Common.imageIconloader.getImageIcon(Common.image_ok);
	public final static Icon icon_cancel = Common.imageIconloader.getImageIcon(Common.image_cancel);
	public final static Icon icon_error = Common.imageIconloader.getImageIcon(Common.image_error);
	public final static Icon icon_close = Common.imageIconloader.getImageIcon(Common.image_close);
}
