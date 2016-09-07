<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>


    <!-- CONFIG DATA -->
    <xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
    <xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config','Plant')"/></xsl:variable>
    <xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config','Warehouse')"/></xsl:variable>
    <xsl:variable name="LANGUAGE"><xsl:value-of select="c4j:getConfigItem('config','Language')"/></xsl:variable>
    
    <!-- Local Variables -->
    <xsl:variable name="SAPMATERIAL_LONG" select="string(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/MATNR[1])" />
    <xsl:variable name="SAPMATERIAL_SHORT" select="translate($SAPMATERIAL_LONG, '^0*', '' )" />
    
    <xsl:variable name="BASE_UOM" select="string(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/MEINS[1])" />
  
    <xsl:template match="ZMATMAS03">
         
        <message>
            <plant><xsl:value-of select="$PLANT" /></plant>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>

            <messageData>
                <materialDefinition>
                    <material><xsl:value-of select="$SAPMATERIAL_SHORT" /></material>
                    <materialType><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/MTART[1]" /></materialType>
                    <gross_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/BRGEW[1])"/></gross_weight>	
                    <net_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/NTGEW[1])" /></net_weight>
                    <weight_uom><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/GEWEI[1]" /></weight_uom>
                </materialDefinition>
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

</xsl:stylesheet>

