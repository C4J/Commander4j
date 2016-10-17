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
    <xsl:variable name="LOCATION"><xsl:value-of select="c4j:getConfigItem('config','Location')"/></xsl:variable>
    
    <!-- Local Variables -->
    <xsl:variable name="SAPMATERIAL_LONG" select="string(/ZMATMAS03/E2MARAM005GRP/E1MARAM/MATNR)" />
    <xsl:variable name="SAPMATERIAL_SHORT" select="translate($SAPMATERIAL_LONG, '^0*', '' )" />
    <xsl:variable name="BASE_UOM" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/E2MARAM005GRP/E1MARAM/MEINS))" />
    <xsl:variable name="CREATE_DATE" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/EDI_DC40/CREDAT))" />
    <xsl:variable name="CREATE_TIME" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/EDI_DC40/CRETIM))" />
    <xsl:variable name="CREATE_DATETIME" select="c4j_XSLT_Ext:concat($CREATE_DATE,$CREATE_TIME)"  />
    <xsl:variable name="LE_QTY" select="number(concat('0',string(/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHMG1)))" />
    <xsl:variable name="LE_UOM" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHME1))" />
    <xsl:variable name="LE_UOM_NOTRIM" select="/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHME1" />
    <xsl:variable name="LE_NUMERATOR" select="number(concat('0',string(/ZMATMAS03/E2MARAM005GRP/E2MARMM002GRP/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREZ)))" />
    <xsl:variable name="LE_DENOMINATOR" select="number(concat('0',string(/ZMATMAS03/E2MARAM005GRP/E2MARMM002GRP/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREN)))" />
    
    <xsl:template match="ZMATMAS03">
         
        <message>
            <plant><xsl:value-of select="$PLANT" /></plant>
            <warehouse><xsl:value-of select="$WAREHOUSE" /></warehouse>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>MM <xsl:value-of select="/ZMATMAS03/EDI_DC40/DOCNUM"/></messageRef>
            <interfaceType>Material Definition</interfaceType>
            <messageInformation>Material=<xsl:value-of select="$SAPMATERIAL_SHORT" /></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="c4j_XSLT_Ext:formatDate($CREATE_DATETIME, 'yyyyMMddHHmmss', 'yyyy-MM-dd''T''HH:mm:ss')"/></messageDate>

            <messageData>
                <materialDefinition>
                    <material><xsl:value-of select="$SAPMATERIAL_SHORT" /></material>
                    <materialType><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP/E1MARAM/MTART" /></materialType>
                    
                    <!-- Get Description for Language E -->
                    <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP" mode="DESCRIPTION"/>

                    <LE_QUANTITY><xsl:value-of select="$LE_QTY" /></LE_QUANTITY>
                    <LE_UOM><xsl:value-of select="$LE_UOM" /></LE_UOM>
                    <LE_NUMERATOR><xsl:value-of select="$LE_NUMERATOR" /></LE_NUMERATOR>
                    <LE_DENOMINATOR><xsl:value-of select="$LE_DENOMINATOR" /></LE_DENOMINATOR>
                    
                    <base_uom><xsl:value-of select="$BASE_UOM" /></base_uom>
                    <gross_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/BRGEW)"/></gross_weight>	
                    <net_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/NTGEW)" /></net_weight>
                    <weight_uom><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP/E1MARAM/GEWEI" /></weight_uom>
                    <old_material><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP/E1MARAM/BISMT" /></old_material>
 
                    <!-- Get First Equipment Type -->
                    <equipment_Type><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM[1]/LETY1[1])" /></equipment_Type>
                     
                    <xsl:variable name="PLANT_PRESENT" select="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$PLANT]" />
                    <xsl:variable name="USE_PLANT" select="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$PLANT]/../../GLB_002F_RGTE1MARCMBBD/ADOPT_CLNT_FLD" />
 
                    <xsl:comment>Plant Data Flag = [<xsl:value-of select='$USE_PLANT' />] for Plant <xsl:value-of select='$PLANT' /></xsl:comment>  
                    
                    <xsl:if test="$USE_PLANT = 'X'">
                        <!-- Get Plant Data -->
                        <xsl:comment>Plant Flag is X></xsl:comment>
                        <shelf_life><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$PLANT]/../../GLB_002F_RGTE1MARCMBBD/PLANT_MHDHB)" /></shelf_life>
                        <shelf_life_rule><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$PLANT]/../../GLB_002F_RGTE1MARCMBBD/PLANT_RDMHD)" /></shelf_life_rule>
                        <xsl:variable name="SHELF_UOM" select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$PLANT]/../../GLB_002F_RGTE1MARCMBBD/PLANT_IPRKZ)" />
                        <shelf_life_uom><xsl:value-of select="c4j:getConfigItem('ShelfLifeUom',$SHELF_UOM)" /></shelf_life_uom>
                        
                    </xsl:if>
                    
                    <xsl:if test="$USE_PLANT != 'X'">
                        <xsl:comment>Plant Flag not X</xsl:comment>  
                        <shelf_life><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/MHDHB)" /></shelf_life>
                        <shelf_life_rule><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/RDMHD)" /></shelf_life_rule>
                        <xsl:variable name="SHELF_UOM" select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/IPRKZ)" />
                        <shelf_life_uom><xsl:value-of select="c4j:getConfigItem('ShelfLifeUom',$SHELF_UOM)" /></shelf_life_uom>
                    </xsl:if>
                    
                    <validLocations>
                        <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM"/>
                        
                        <location id="REWORK">
                            <id>REWORK</id>
                            <status>Valid</status>
                        </location>
                        <location id="EXPORT">
                            <id>EXPORT</id>
                            <status>Valid</status>
                        </location>
                    </validLocations>
                    
                    <!-- Get Units of Measure -->  
                    <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP/E2MARMM002GRP/E1MARMM"/>

                </materialDefinition>
            </messageData>
        </message>
    </xsl:template>
     
    <!-- Get Description for Language E -->
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP" mode="DESCRIPTION">     
        <xsl:for-each select="E1MAKTM">
            <xsl:variable name="DESC_LANGUAGE" select="string(SPRAS)" />
            <xsl:if test="$DESC_LANGUAGE = $LANGUAGE">
                <description><xsl:value-of select="c4j_XSLT_Ext:trim(MAKTX)" /></description>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>   
 
    <!-- Get Units of Measure -->  
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MARMM002GRP/E1MARMM">
        <xsl:variable name="CURRENT_UOM" select='c4j_XSLT_Ext:trim(string(MEINH))' />
        <xsl:variable name="CURRENT_EAN" select='c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(EAN11))' />
        <xsl:variable name="CURRENT_VARIANT" select='c4j_XSLT_Ext:padVariant(c4j_XSLT_Ext:trim(GTIN_VARIANT))' />
        <xsl:variable name="CURRENT_NUMERATOR" select="c4j_XSLT_Ext:trim(UMREZ)" />
        <xsl:variable name="CURRENT_DENOMINATOR" select="c4j_XSLT_Ext:trim(UMREN)" />
        
        <materialUOMDefinition><xsl:attribute name="id" select="$CURRENT_UOM" />       
            <uom><xsl:value-of select="$CURRENT_UOM" /></uom>
            <ean><xsl:value-of select="$CURRENT_EAN" /></ean>
            <variant><xsl:value-of select="$CURRENT_VARIANT" /></variant>
     
        
            <xsl:if test="$CURRENT_UOM='D97'">           
                <xsl:comment>Converting LE Quantity from <xsl:value-of select='$LE_UOM' /> into <xsl:value-of select='$BASE_UOM' /> before inserting calculated D97</xsl:comment> 
                <xsl:comment>LE QTY is <xsl:value-of select='$LE_QTY' /></xsl:comment> 
                <xsl:comment>LE UOM is <xsl:value-of select='$LE_UOM' /></xsl:comment> 
                <xsl:comment><xsl:value-of select='$LE_UOM' /> numerator is <xsl:value-of select='$LE_NUMERATOR' /></xsl:comment> 
                <xsl:variable name="temp99" select="number($LE_QTY * $LE_NUMERATOR)" />
                <xsl:comment>D97 (in <xsl:value-of select='$BASE_UOM' />) = <xsl:value-of select='$LE_QTY' />(<xsl:value-of select='$LE_UOM' />) x <xsl:value-of select='$LE_NUMERATOR' /> = <xsl:value-of select='$temp99' /></xsl:comment> 
                <xsl:comment>Original numerator is <xsl:value-of select="$CURRENT_NUMERATOR" /></xsl:comment>
                
                <xsl:if test="$temp99=0">
                    <numerator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREZ)" /></numerator>
                </xsl:if>
                
                <xsl:if test="$temp99>0">
                    <numerator><xsl:value-of select='$temp99'/></numerator>
                </xsl:if>
                
            </xsl:if>
            
            <xsl:if test="$CURRENT_UOM!='D97'">  
                <numerator><xsl:value-of select="$CURRENT_NUMERATOR" /></numerator>
            </xsl:if>  
            
            <denominator><xsl:value-of select="$CURRENT_DENOMINATOR" /></denominator>
            
        </materialUOMDefinition>
    </xsl:template>
  
    <!-- Get Locations -->
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM">
        <xsl:variable name="CURRENT_PLANT" select="c4j_XSLT_Ext:trim(WERKS)" />
        <xsl:variable name="CURRENT_STATUS" select="c4j_XSLT_Ext:trim(MMSTA)" />
        <xsl:variable name="CURRENT_LOCATION" select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP/E1MARCM/WERKS[.=$CURRENT_PLANT]/../../E1MARDM[1]/LGORT )"/>
    
        <xsl:variable name="TEMP1" select="c4j_XSLT_Ext:concat($CURRENT_PLANT,'-')"  />
        <xsl:variable name="TEMP2" select="c4j_XSLT_Ext:concat($TEMP1,$CURRENT_LOCATION)"  />
        <xsl:variable name="LOCATION2"><xsl:value-of select="c4j:getConfigItem('PlantSLOCtoLocation',$TEMP2)"/></xsl:variable>  
        
        <xsl:if test="$LOCATION2 != ''">
            <location><xsl:attribute name="id" select="$LOCATION2" />
                <xsl:attribute name="id"><xsl:value-of select="c4j_XSLT_Ext:trim($LOCATION2)" /></xsl:attribute>
                <id><xsl:value-of select="c4j_XSLT_Ext:trim($LOCATION2)" /></id>
                <status><xsl:value-of select="c4j:getConfigItem('SAPMaterialStatus',$CURRENT_STATUS)" /></status>
            </location>
        </xsl:if>      
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

