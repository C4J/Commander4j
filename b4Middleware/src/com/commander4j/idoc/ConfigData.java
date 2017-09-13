package com.commander4j.idoc;

/**
 * @author James Farrer
 *
 *	Simple class which holds the structure of the IDOC config file
 *
 */
public class ConfigData {

	public String SegmentDefinition;
    public String IDOCField;
    public int    nFieldOffset;
    public int    nLength;
    public String BIFSection; 
    public boolean   bNewSection;
    public String BIFField;
    public int position;
    
}
