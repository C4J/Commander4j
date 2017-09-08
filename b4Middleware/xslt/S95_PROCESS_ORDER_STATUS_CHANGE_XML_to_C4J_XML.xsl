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
    
    <!-- Local Variables -->

    <xsl:variable name="CRID" select="string(/ProductionSchedule/ProductionRequest[1]/SegmentRequirement[1]/ID[1])"></xsl:variable>
    <xsl:variable name="SAPORDER_LONG" select="string(/ProductionSchedule/ProductionRequest[1]/ID[1])"></xsl:variable>
    <xsl:variable name="SAPORDER_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPORDER_LONG)" />
    <xsl:variable name="DATENOW" select="current-dateTime()"/>
    <xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
  
    <xsl:template match='ProductionSchedule'>

        <message>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>CRID <xsl:value-of select='$CRID' /></messageRef>
            <interfaceType>Process Order Status Change</interfaceType>
            <messageInformation>Process Order=<xsl:value-of select='ProductionRequest/ID'/></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            <messageData>
                <processOrder>
                    <orderNo><xsl:value-of select='$SAPORDER_SHORT' /></orderNo>
                    <receipeId><xsl:value-of select='ProductionRequest/SegmentRequirement/ID'/></receipeId>
                    <status><xsl:value-of select='/ProductionSchedule/ProductionRequest[1]/SegmentRequirement[1]/globe_SegmentState[1]'/></status>
                </processOrder>
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

