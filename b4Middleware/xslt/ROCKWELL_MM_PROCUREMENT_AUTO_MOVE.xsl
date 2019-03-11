<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
    <!-- CONFIG DATA -->
    <xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
    <xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config','Plant')"/></xsl:variable>
    <xsl:variable name="LOCATION_LOC"><xsl:value-of select="c4j:getConfigItem('config','Location')"/></xsl:variable>
    <xsl:variable name="LOCATION_CTR"><xsl:value-of select="c4j:getConfigItem('config','Location_Central')"/></xsl:variable>
    <xsl:variable name="PROCUREMENT_IND"><xsl:value-of select="c4j:getConfigItem('config','Procurement_Ind')"/></xsl:variable>

    <xsl:variable name="SAPMATERIAL_LONG" select="string(/MESSAGE/DATA[@type='Basic']/FIELD[@name='Material']/@value)" />
    <xsl:variable name="SAPMATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPMATERIAL_LONG)" />
    <xsl:variable name="PLANT_FOUND" select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='Plant']/@value)" />
    
    <xsl:variable name="PROCUREMENT" select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='SpecialProcessing']/@value)" />
    
    <xsl:variable name="DATENOW" select="current-dateTime()"/>
    <xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
  
    <xsl:template match="MESSAGE">
         
        <message>
            <plant><xsl:value-of select="$PLANT" /></plant>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>MM <xsl:value-of select='$SAPMATERIAL_LONG' /></messageRef>
            <interfaceType>Material Auto Move</interfaceType>
            <messageInformation>Material=<xsl:value-of select='$SAPMATERIAL_SHORT' /></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            <messageData>
                <materialAutoMove>

                    
                    <xsl:if test="$PLANT=$PLANT_FOUND">
                        
                        <xsl:if test="$PROCUREMENT_IND=''">
                            <xsl:comment>>*WARNING* 'Procurement_Ind' not defined in configData.xml</xsl:comment>
                        </xsl:if>
                        
                        <xsl:comment>>Searching Plant [<xsl:value-of select="$PLANT"/>] for Procurement Indicator [<xsl:value-of select="$PROCUREMENT_IND"/>]</xsl:comment>
                        
                        <xsl:comment>>Procurement Indicator found Plant [<xsl:value-of select="$PLANT"/>] is [<xsl:value-of select="$PROCUREMENT"/>]</xsl:comment>
                        
                        <material><xsl:value-of select="$SAPMATERIAL_SHORT" /></material>
                        
                        <xsl:if test="$PROCUREMENT=$PROCUREMENT_IND">
                            <moveAfterMake>Y</moveAfterMake>
                            
                            <xsl:if test="$LOCATION_CTR=''">
                                <xsl:comment>>*WARNING* 'Location_Central' not defined in configData.xml using 'Location' instead</xsl:comment>
                                <moveLocationID><xsl:value-of select="$LOCATION_LOC" /></moveLocationID>
                            </xsl:if>  
                            
                            <xsl:if test="$LOCATION_CTR!=''">
                                <moveLocationID><xsl:value-of select="$LOCATION_CTR" /></moveLocationID>
                            </xsl:if>  
                        </xsl:if>  
                        
                        <xsl:if test="$PROCUREMENT!=$PROCUREMENT_IND">
                            <moveAfterMake>N</moveAfterMake>
                            <moveLocationID><xsl:value-of select="$LOCATION_LOC" /></moveLocationID>
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

