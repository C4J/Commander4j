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
    
    <!-- Local Variables -->
    <xsl:variable name="SAPMATERIAL_LONG" select="string(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/MATNR[1])" />
    <xsl:variable name="SAPMATERIAL_SHORT" select="translate($SAPMATERIAL_LONG, '^0*', '' )" />
    <xsl:variable name="BASE_UOM" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/E2MARAM005GRP/E1MARAM/MEINS))" />
    <xsl:variable name="CREATE_DATE" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/EDI_DC40/CREDAT))" />
    <xsl:variable name="CREATE_TIME" select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/EDI_DC40/CRETIM))" />
    <xsl:variable name="CREATE_DATETIME" select="c4j_XSLT_Ext:concat($CREATE_DATE,$CREATE_TIME)"  />
    
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
                    
                    <!-- Get LE Quantity from Warehouse -->
                    <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP" mode="LE"/>     
                    
                    <base_uom><xsl:value-of select="$BASE_UOM" /></base_uom>
                    <gross_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/BRGEW)"/></gross_weight>	
                    <net_weight><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E1MARAM/NTGEW)" /></net_weight>
                    <weight_uom><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP/E1MARAM/GEWEI" /></weight_uom>
                    <old_material><xsl:value-of select="/ZMATMAS03/E2MARAM005GRP/E1MARAM/BISMT" /></old_material>
 
                    <!-- Get First Equipment Type -->
                    <equipment_Type><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM[1]/LETY1[1])" /></equipment_Type>
                    
                    <!-- Get Plant Data -->
                    <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP" mode="SHELF"/>  
                    
                </materialDefinition>
            </messageData>
        </message>

    </xsl:template>
   
    <!-- Get Plant Data -->
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP" mode="SHELF">
        <xsl:variable name="CURRENT_PLANT" select="c4j_XSLT_Ext:trim(E1MARCM/WERKS)" />
       
        <xsl:if test="$CURRENT_PLANT = $PLANT"> 
            
            <xsl:comment>Data for Plant = <xsl:value-of select='$CURRENT_PLANT' /></xsl:comment>
            
            <!-- Get Shelf Life -->
            <xsl:apply-templates select="GLB_002F_RGTE1MARCMBBD[1]/ADOPT_CLNT_FLD[1]"/>
            
            <validLocations>
                <!-- Get Locations -->
                <xsl:apply-templates select="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP" mode="LOCATION"/> 
                
                <!-- Add Static Locations -->
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
            
        </xsl:if>   
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
    
    <!-- Get LE Quantity from Warehouse -->
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MLGNM001GRP" mode="LE">     
        <xsl:for-each select="E1MLGNM">
            <xsl:variable name="CURRENT_WAREHOUSE" select="string(LGNUM)" />
            <xsl:if test="$CURRENT_WAREHOUSE = $WAREHOUSE">
                <xsl:comment>LE Data for Warehouse = <xsl:value-of select='$CURRENT_WAREHOUSE' /></xsl:comment>
                <LE_quantity><xsl:value-of select="number(concat('0',string(LHMG1)))" /></LE_quantity>
                <LE_uom><xsl:value-of select="c4j_XSLT_Ext:trim(LHME1)" /></LE_uom>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>      
    
    <!-- Get Units of Measure -->  
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MARMM002GRP/E1MARMM">
  
        <!--  /ZMATMAS03/E2MARAM005GRP[1]/E2MLGNM001GRP[1]/E1MLGNM[1]/LHMG1[1] -->  
        
        <materialUOMDefinition>
            <xsl:attribute name="uom"><xsl:value-of select="c4j_XSLT_Ext:trim(MEINH)" /></xsl:attribute>
            <xsl:variable name="CURRENT_UOM" select='c4j_XSLT_Ext:trim(string(MEINH))' />
            <xsl:variable name="LE_QUANTITY" select="number(concat('0',string(LHMG1)))" />
            <xsl:variable name="LE_UOM" select="c4j_XSLT_Ext:trim(string(LHME1))" />
            
            <uom><xsl:value-of select="c4j_XSLT_Ext:trim(MEINH)" /></uom>
            <ean><xsl:value-of select="c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(EAN11))" /></ean>
            <variant><xsl:value-of select="c4j_XSLT_Ext:padVariant(c4j_XSLT_Ext:trim(GTIN_VARIANT))" /></variant>
            <numerator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREZ)" /></numerator>
            <denominator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREN)" /></denominator>

            <xsl:if test="$LE_UOM != ''">
                <LEQuantity><xsl:value-of select="$LE_QUANTITY" /></LEQuantity>
                <LEuom><xsl:value-of select="$LE_UOM" /></LEuom>
            </xsl:if>
            
        </materialUOMDefinition>
    </xsl:template>
  
    <!-- Get Locations -->
    <xsl:template match="/ZMATMAS03/E2MARAM005GRP/E2MARCM004GRP" mode="LOCATION">
        
        <xsl:variable name="CURRENT_PLANT" select="c4j_XSLT_Ext:trim(E1MARCM/WERKS)" />
        <xsl:variable name="CURRENT_LOCATION" select="c4j_XSLT_Ext:trim(E1MARDM[1]/LGORT[1])" />
        <xsl:variable name="CURRENT_STATUS" select="c4j_XSLT_Ext:trim(E1MARCM/MMSTA[1])" />
        
        <xsl:variable name="TEMP1" select="c4j_XSLT_Ext:concat($CURRENT_PLANT,'-')"  />
        <xsl:variable name="TEMP2" select="c4j_XSLT_Ext:concat($TEMP1,$CURRENT_LOCATION)"  />
        <xsl:variable name="LOCATION"><xsl:value-of select="c4j:getConfigItem('PlantSLOCtoLocation',$TEMP2)"/></xsl:variable>
       
        <location>
            <xsl:attribute name="id"><xsl:value-of select="c4j_XSLT_Ext:trim($LOCATION)" /></xsl:attribute>
            <id><xsl:value-of select="c4j_XSLT_Ext:trim($LOCATION)" /></id>
            <status><xsl:value-of select="c4j:getConfigItem('SAPMaterialStatus',$CURRENT_STATUS)" /></status>
        </location>
    </xsl:template>
      
    <!-- Get Shelf Life -->
    <xsl:template match="GLB_002F_RGTE1MARCMBBD[1]/ADOPT_CLNT_FLD[1]">
        <xsl:variable name="USE_PLANT" select="c4j:getConfigItem('UsePlant',c4j_XSLT_Ext:trim(ADOPT_CLNT_FLD))" />
       
        <xsl:if test="$USE_PLANT = 'Y'">
            <xsl:comment>Using Plant Data = Y</xsl:comment>
            <shelf_life><xsl:value-of select="c4j_XSLT_Ext:trim(PLANT_MHDHB)" /></shelf_life>
            <shelf_life_rule><xsl:value-of select="c4j_XSLT_Ext:trim(PLANT_RDMHD)" /></shelf_life_rule>
            <xsl:variable name="SHELF_UOM" select="c4j_XSLT_Ext:trim(PLANT_IPRKZ)" />
            <shelf_life_uom><xsl:value-of select="c4j:getConfigItem('ShelfLifeUom',$SHELF_UOM)" /></shelf_life_uom>
        </xsl:if>
        
        <xsl:if test="$USE_PLANT = 'N'">
            <xsl:comment>Using Plant Data = N</xsl:comment>
            <shelf_life><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/MHDHB[1])" /></shelf_life>
            <shelf_life_rule><xsl:value-of select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/RDMHD[1])" /></shelf_life_rule>
            <xsl:variable name="SHELF_UOM" select="c4j_XSLT_Ext:trim(/ZMATMAS03/E2MARAM005GRP[1]/E1MARAM[1]/IPRKZ[1])" />
            <shelf_life_uom><xsl:value-of select="c4j:getConfigItem('ShelfLifeUom',$SHELF_UOM)" /></shelf_life_uom>
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

