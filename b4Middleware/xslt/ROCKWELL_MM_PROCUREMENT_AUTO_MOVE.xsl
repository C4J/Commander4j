<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
    <!-- CONFIG DATA -->
    
    <xsl:variable name="HOSTREF"                select="string(c4j:getConfigItem('config','HostRef'))"/>
    <xsl:variable name="PLANT"                  select="string(c4j:getConfigItem('config','Plant_Central'))"/>
    <xsl:variable name="WAREHOUSE"              select="string(c4j:getConfigItem('config','Warehouse'))"/>
    <xsl:variable name="LOCATION_LOC"           select="string(c4j:getConfigItem('config','Location'))"/>
    <xsl:variable name="LOCATION_CTR"           select="string(c4j:getConfigItem('config','Location_Central'))"/>
    <xsl:variable name="PROCUREMENT_IND"        select="string(c4j:getConfigItem('config','Procurement_Ind'))"/>
    
    <xsl:variable name="DATENOW"                select="current-dateTime()"/>
    <xsl:variable name="MESSAGEDATE"            select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
    
    <!-- Local Variables -->
    
    <xsl:variable name="SAPMATERIAL_LONG"       select="string(/MESSAGE/DATA[@type='Basic']/FIELD[@name='Material']/@value)" />
    <xsl:variable name="MATERIAL"               select="string(c4j_XSLT_Ext:removeLeadingZeros($SAPMATERIAL_LONG))" />

    <xsl:variable name="MTYPE"                  select="string(/MESSAGE/DATA[@type='Basic']/FIELD[@name='MaterialType']/@value)" />   
    <xsl:variable name="USE_PLANT"              select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='UsePlantFields']/@value)" />
    <xsl:variable name="FOUND_PLANT"            select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='Plant']/@value)" />
    <xsl:variable name="FOUND_WAREHOUSE"        select="string(/MESSAGE/DATA[@type='Warehouse']/FIELD[@name='Warehouse']/parent::*/FIELD[@name='Plant']/@value)" />
    <xsl:variable name="PROCUREMENT_FOUND"      select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='SpecialProcessing']/@value)" />
    <xsl:variable name="PROCUREMENT_COUNT"      select="count(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='SpecialProcessing'])"/>   
    
    <xsl:template match="MESSAGE">
         
        <message>
            
            <plant><xsl:value-of select="$PLANT"/></plant>
            <warehouse><xsl:value-of select="$WAREHOUSE"/></warehouse>
            <hostRef><xsl:value-of select="$HOSTREF"/></hostRef>
            <interfaceType>Material Auto Move</interfaceType>
            <interfaceDirection>Input</interfaceDirection>
            <messageRef>MM <xsl:value-of select='$MATERIAL' /></messageRef>
            <messageInformation>Material=<xsl:value-of select="$MATERIAL"/>/<xsl:value-of select="$MTYPE"/></messageInformation>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            
            <messageData>
                <materialAutoMove>

                    <xsl:if test="$PLANT = $FOUND_PLANT">
                        
                        <xsl:if test="$PROCUREMENT_COUNT = 1">
                            
                            <xsl:if test="$PROCUREMENT_FOUND = $PROCUREMENT_IND">
                                <material><xsl:value-of select="$MATERIAL"/></material>
                                <moveAfterMake>Y</moveAfterMake>
                                <moveLocationID><xsl:value-of select="$LOCATION_CTR"/></moveLocationID>
                            </xsl:if>
                            
                            <xsl:if test="$PROCUREMENT_FOUND != $PROCUREMENT_IND">
                                <material><xsl:value-of select="$MATERIAL"/></material>
                                <moveAfterMake>N</moveAfterMake>
                                <moveLocationID><xsl:value-of select="$LOCATION_LOC"/></moveLocationID>
                            </xsl:if>
                            
                        </xsl:if>
                        
                    </xsl:if>   
                </materialAutoMove>
            </messageData>
        </message>

    </xsl:template>


    <!-- ================
        FUNCTION get config data 
        ================ -->
    
    <xsl:function name="c4j:getConfigItem">
        <xsl:param name="type"/>
        <xsl:param name="string1"/>
        
        <xsl:variable name="item_info" select="document('configData.xml')/lookup"/>

        <xsl:value-of select="$item_info/item[@type=$type][@id=$string1]/value"/>
        
    </xsl:function>
    
		<!-- ================
        FUNCTION get reference data 
        ================ -->
	
	<xsl:function name="c4j:getReferenceItem">
		<xsl:param name="type"/>
		<xsl:param name="string1"/>
		
		<xsl:variable name="item_info" select="document('referenceData.xml')/lookup"/>
		
		<xsl:value-of select="$item_info/item[@type=$type][@id=$string1]/value"/>
		
	</xsl:function>
</xsl:stylesheet>

