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

    <xsl:variable name="ID" select="string(/MaterialInformation/ID[1])"></xsl:variable>
    <xsl:variable name="MATERIAL_LONG" select="string(/MaterialInformation/MaterialLot[1]/MaterialDefinitionID[1])"></xsl:variable>
    <xsl:variable name="MATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($MATERIAL_LONG)" />
    <xsl:variable name="BATCH" select="/MaterialInformation/MaterialLot[1]/ID[1]" />
    <xsl:variable name="DATENOW" select="current-dateTime()"/>
    <xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
  
    <xsl:template match='MaterialInformation'>

        <message>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>ID <xsl:value-of select='$ID' /></messageRef>
            <interfaceType>Batch Status Change</interfaceType>
            <messageInformation>Material=<xsl:value-of select='$MATERIAL_SHORT'/>, Batch=<xsl:value-of select='$BATCH'/></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            <messageData>
                <batchStatusChange>
                    <xsl:choose>
                        <xsl:when test="MaterialLot"> <!-- if a MaterialLot exists, it's a QAChange -->
                            <xsl:apply-templates select="MaterialLot"/>
                        </xsl:when>
                        <xsl:otherwise>
                        </xsl:otherwise>
                    </xsl:choose>
                </batchStatusChange>
            </messageData>
        </message>
    </xsl:template>

    <!-- ==================================================================================
    =                      Material Produced Requirement (MPA)                          =
    ================================================================================== -->
    <xsl:template match="MaterialLot">
        <batch><xsl:value-of select='ID'/></batch>
        <xsl:variable name="materialL" select="MaterialDefinitionID"/>
        <xsl:variable name="materialS" select="translate($materialL, '^0*', '' )" />
        <material><xsl:value-of select='$materialS'/></material>
        <xsl:variable name="batchStatus" select="globe_BatchRestrictedStatus"/>
        <status><xsl:value-of select="c4j:getConfigItem('SAPStockStatus',string($batchStatus))"/></status>
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

</xsl:stylesheet>

