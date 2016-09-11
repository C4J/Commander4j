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
    <xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config','Warehouse')"/></xsl:variable>
    <xsl:variable name="LANGUAGE"><xsl:value-of select="c4j:getConfigItem('config','Language')"/></xsl:variable>
    
    <xsl:variable name="BASE_UOM"><xsl:value-of select="string(/message/messageData/materialDefinition/base_uom)"/></xsl:variable>
    
    <xsl:variable name="D97_EAN" select="string(/message/messageData/materialDefinition/materialUOMDefinition[@uom='D97']/ean)" />
    <xsl:variable name="D97_VARIANT" select="string(/message/messageData/materialDefinition/materialUOMDefinition[@uom='D97']/variant)" />
    <xsl:variable name="D97_DENOMINATOR" select="number(concat('0',string(/message/messageData/materialDefinition/materialUOMDefinition[@uom='D97']/denominator)))" />
    <xsl:variable name="D97_NUMERATOR" select="number(concat('0',string(/message/messageData/materialDefinition/materialUOMDefinition[@uom='D97']/numerator)))" />
    
    <xsl:variable name="LE_QUANTITY"><xsl:value-of select="number(concat('0',string(/message/messageData/materialDefinition/LE_quantity)))" /></xsl:variable>
    <xsl:variable name="LE_UOM"><xsl:value-of select="string(/message/messageData/materialDefinition/LE_uom)" /></xsl:variable>
    <xsl:variable name="LE_NUMERATOR" select="number(concat('0',string(/message/messageData/materialDefinition/materialUOMDefinition[@uom=$LE_UOM]/numerator)))" />
    
    <xsl:template match="message">
         
        <message>

            <hostRef><xsl:value-of select="hostRef" /></hostRef>
            <messageRef><xsl:value-of select="messageRef"/></messageRef>
            <interfaceType><xsl:value-of select="interfaceType"/></interfaceType>
            <messageInformation><xsl:value-of select="interfaceType"/></messageInformation>
            <interfaceDirection><xsl:value-of select="interfaceDirection"/></interfaceDirection>
            <messageDate><xsl:value-of select="messageDate"/></messageDate>
            
            <messageData>
                
                <materialDefinition>
                    
                    <material><xsl:value-of select="messageData/materialDefinition/material" /></material>
                    <materialType><xsl:value-of select="messageData/materialDefinition/materialType" /></materialType>
                    <description><xsl:value-of select="messageData/materialDefinition/description" /></description>
                    <base_uom><xsl:value-of select="messageData/materialDefinition/base_uom" /></base_uom>
                    <gross_weight><xsl:value-of select="messageData/materialDefinition/gross_weight" /></gross_weight>
                    <net_weight><xsl:value-of select="messageData/materialDefinition/net_weight" /></net_weight>
                    <weight_uom><xsl:value-of select="messageData/materialDefinition/weight_uom" /></weight_uom>
                    <old_material><xsl:value-of select="messageData/materialDefinition/old_material" /></old_material>
                    <equipment_Type><xsl:value-of select="messageData/materialDefinition/equipment_Type" /></equipment_Type>
                    <shelf_life><xsl:value-of select="messageData/materialDefinition/shelf_life" /></shelf_life>
                    <shelf_life_rule><xsl:value-of select="messageData/materialDefinition/shelf_life_rule" /></shelf_life_rule>
                    
                    <!-- Locations -->
                    <xsl:apply-templates select="messageData/materialDefinition/validLocations"/>   
                    
                    <!-- Units of Measure -->
                    <xsl:apply-templates select="messageData/materialDefinition"/>  
                    
                </materialDefinition>
                
            </messageData>

        </message>

    </xsl:template>

    <!-- Locations -->
    <xsl:template match="messageData/materialDefinition/validLocations">
        <xsl:for-each select="location">
            <location>
                <id><xsl:value-of select="id" /></id>
                <status><xsl:value-of select="status" /></status>
            </location>
        </xsl:for-each>
    </xsl:template>

    <!-- Units of Measure -->
    <xsl:template match="messageData/materialDefinition">
        <xsl:for-each select="materialUOMDefinition">
            <materialUOMDefinition>
                
                <xsl:variable name="CURRENT_UOM"><xsl:value-of select="uom"/></xsl:variable>
     
                <uom><xsl:value-of select="uom" /></uom>
                <ean><xsl:value-of select="ean" /></ean>  
                <variant><xsl:value-of select="variant" /></variant>
                
                <xsl:if test="$CURRENT_UOM='D97'">  
                    <xsl:comment>Converting LE Quantity from <xsl:value-of select='$LE_UOM' /> into <xsl:value-of select='$BASE_UOM' /> before inserting calculated D97</xsl:comment> 
                    <xsl:comment>LE QTY is <xsl:value-of select='$LE_QUANTITY' /></xsl:comment> 
                    <xsl:comment>LE UOM is <xsl:value-of select='$LE_UOM' /></xsl:comment> 
                    <xsl:comment><xsl:value-of select='$LE_UOM' /> numerator is <xsl:value-of select='$LE_NUMERATOR' /></xsl:comment> 
                    <xsl:variable name="temp99" select="number($LE_QUANTITY * $LE_NUMERATOR)" />
                    <xsl:comment>D97 (in <xsl:value-of select='$BASE_UOM' />) = <xsl:value-of select='$LE_QUANTITY' />(<xsl:value-of select='$LE_UOM' />) x <xsl:value-of select='$LE_NUMERATOR' /> = <xsl:value-of select='$temp99' /></xsl:comment> 
                    <xsl:comment>Original numerator is <xsl:value-of select='numerator' /></xsl:comment>
                    <numerator><xsl:value-of select='$temp99'/></numerator>
                </xsl:if>
                
                <xsl:if test="$CURRENT_UOM!='D97'">  
                    <numerator><xsl:value-of select="numerator" /></numerator>
                </xsl:if>    
                 
                <denominator><xsl:value-of select="denominator" /></denominator>
                
            </materialUOMDefinition>
        </xsl:for-each>
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

