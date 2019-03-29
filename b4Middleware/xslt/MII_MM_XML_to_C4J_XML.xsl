<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>

    <!-- CONFIG DATA -->
    <!--
    <xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
    <xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config','Plant')"/></xsl:variable>
    <xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config','Warehouse')"/></xsl:variable>
    <xsl:variable name="LANGUAGE"><xsl:value-of select="c4j:getConfigItem('config','Language')"/></xsl:variable>
    <xsl:variable name="LOCATION"><xsl:value-of select="c4j:getConfigItem('config','Location')"/></xsl:variable>
    
    MARA            - General Material Data     (Material)                                  E1MARAM  E2MARMM002GRP  E2MARAM005GRP
      MAKT          - Material Descriptions.    (Material - Language)
      MARC          - Plant Data                (Material - Plant)                          E1MARCM
        MARD        - Storage Location Data     (Material - Plant - Storage Location)
      MARM          - Units of Measure          (Material - Unit of Measure)
        MEAN        - EAN's                     (Material - Unit of Measure - Sequence No)
      MLGN          - Warehouse Data            (Material - Warehouse)                      E2MLGNM001GRP
      
      
     MARA E1MARAM - Master material general data 
     MARC E1MARCM - Master material C segment
     MLGN E1MLGNM - Master material material data per warehouse number
     
     -->
    
    <!-- DEBUG DATA -->
    <xsl:variable name="HOSTREF"   >HULIVE</xsl:variable>
    <xsl:variable name="PLANT"     >0917</xsl:variable>
    <xsl:variable name="WAREHOUSE" >HU3</xsl:variable>
    <xsl:variable name="LANGUAGE"  >E</xsl:variable>
    <xsl:variable name="LOCATION"  >BUK</xsl:variable>
  
    <!-- Local Variables -->
    
    <xsl:variable name="USE_PLANT"          select="/ZMATMAS03/IDOC/E1MARAM/_-NESGLB_-DISTR000/WERKS[.=$PLANT]/../AD_PLANT_DATA"/>
    <xsl:variable name="FOUND_PLANT"        select="/ZMATMAS03/IDOC/E1MARAM/E1MARCM/WERKS[.=$PLANT]"/>
    <xsl:variable name="FOUND_WAREHOUSE"    select="/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]" />

    <xsl:variable name="LE_QTY"             select="number(concat('0',string(/ZMATMAS03/IDOC/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHMG1)))" />
    <xsl:variable name="LE_UOM"             select="c4j_XSLT_Ext:trim(string(/ZMATMAS03/IDOC/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHME1))" />
    <xsl:variable name="LE_UOM_NOTRIM"      select="/ZMATMAS03/IDOC/E2MARAM005GRP/E2MLGNM001GRP/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHME1" />
    <xsl:variable name="LE_NUMERATOR"       select="number(concat('0',string(/ZMATMAS03/IDOC/E2MARAM005GRP/E2MARMM002GRP/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREZ)))" />
    <xsl:variable name="LE_DENOMINATOR"     select="number(concat('0',string(/ZMATMAS03/IDOC/E2MARAM005GRP/E2MARMM002GRP/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREN)))" />
   
    <xsl:template match="/ZMATMAS03">
        <message>
            <xsl:apply-templates select="IDOC"/>
        </message>
    </xsl:template>
   
    <xsl:template match="IDOC">
        <xsl:apply-templates select="EDI_DC40"/>
        <messageData>
            <materialDefinition>
                  <xsl:apply-templates select="E1MARAM"/>
            </materialDefinition>
        </messageData>
    </xsl:template>
    
    <xsl:template match="EDI_DC40">
        <plant><xsl:value-of select="$PLANT"/></plant>
        <warehouse><xsl:value-of select="$WAREHOUSE"/></warehouse>
        <hostRef><xsl:value-of select="$HOSTREF"/></hostRef>
        <interfaceType>Material Definition</interfaceType>
        <interfaceDirection>Input</interfaceDirection>
        <xsl:variable name="SAPDOCNUM_LONG" select="DOCNUM" />
        <xsl:variable name="SAPDOCNUM_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPDOCNUM_LONG)" />
        <messageRef>DOCNUM <xsl:value-of select="$SAPDOCNUM_SHORT"/></messageRef>
        <xsl:variable name="CREATE_DATE" select="c4j_XSLT_Ext:trim(string(CREDAT))" />
        <xsl:variable name="CREATE_TIME" select="c4j_XSLT_Ext:trim(string(CRETIM))" />
        <xsl:variable name="CREATE_DATETIME" select="c4j_XSLT_Ext:concat($CREATE_DATE,$CREATE_TIME)"  />
        <messageDate><xsl:value-of select="c4j_XSLT_Ext:formatDate($CREATE_DATETIME, 'yyyyMMddHHmmss', 'yyyy-MM-dd''T''HH:mm:ss')"/></messageDate>
    </xsl:template>
    
    <xsl:template match="E1MARAM">
        <xsl:variable name="MATERIAL_LONG" select="MATNR" />
        <xsl:variable name="MATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($MATERIAL_LONG)" />
        <material><xsl:value-of select="$MATERIAL_SHORT" /></material>
        <old_material><xsl:value-of select="BISMT" /></old_material>
        <materialType><xsl:value-of select="MTART" /></materialType>
        <base_uom><xsl:value-of select="MEINS" /></base_uom>
        <gross_weight><xsl:value-of select="BRGEW" /></gross_weight>
        <net_weight><xsl:value-of select="NTGEW" /></net_weight>
        <weight_uom><xsl:value-of select="GEWEI" /></weight_uom>
        <shelf_life_rule><xsl:value-of select="RDMHD" /></shelf_life_rule>

        <xsl:apply-templates select="E1MARCM/_-GLB_-RGTE1MARCMBBD"/>
        <xsl:comment>Description for language <xsl:value-of select="$LANGUAGE" /></xsl:comment>
        
        <xsl:variable name="DESCRIPTION" select="E1MAKTM/SPRAS[.=$LANGUAGE]/../MAKTX" />
        <description><xsl:value-of select="$DESCRIPTION"/></description>
        
        <xsl:if test="starts-with($DESCRIPTION,'ZZ')">
            <enabled>N</enabled>
        </xsl:if> 
        <xsl:if test="not(starts-with($DESCRIPTION,'ZZ'))">
            <enabled>Y</enabled>
        </xsl:if> 
        
        <equipment_Type><xsl:value-of select=" /ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LETY1"/></equipment_Type>

        <xsl:apply-templates select="E1MARMM"/>

        <xsl:if test="$USE_PLANT='X'"> 
            <xsl:comment>Use Plant Segment <xsl:value-of select="$USE_PLANT" /></xsl:comment>

            <xsl:if test="$FOUND_PLANT=$PLANT">
                <xsl:comment>Found Plant Segment <xsl:value-of select="$FOUND_PLANT"/></xsl:comment>
            </xsl:if> 

            <xsl:if test="$FOUND_PLANT!=$PLANT">
                <xsl:comment>Plant Segment Not Found for <xsl:value-of select="$PLANT"/></xsl:comment>
            </xsl:if> 
        </xsl:if>
        
        <xsl:if test="$USE_PLANT!='X'"> 
            <xsl:comment>Don't use Plant Segment <xsl:value-of select="$USE_PLANT" /></xsl:comment>
            <rounding_rule5><xsl:value-of select="/ZMATMAS03/IDOC[1]/E1MARAM[1]/RDMHD[1]" /></rounding_rule5>
            <!--    /ZMATMAS03/IDOC[1]/E1MARAM[1]/RDMHD[1]-->
        </xsl:if>
        
        <xsl:if test="$FOUND_WAREHOUSE=$WAREHOUSE">
            <xsl:comment>Found Warehouse Segment <xsl:value-of select="$FOUND_WAREHOUSE"/></xsl:comment>
        </xsl:if> 
        
        
        <xsl:if test="$FOUND_WAREHOUSE!=$WAREHOUSE">
            <xsl:comment>Warehouse Segment Not Found for <xsl:value-of select="$WAREHOUSE"/></xsl:comment>
        </xsl:if> 
        
        <validLocations>
             <xsl:apply-templates select="E1MARCM"/>
        </validLocations>
    </xsl:template>

    <xsl:template match="E1MARCM/_-GLB_-RGTE1MARCMBBD">
        <shelf_life> <xsl:value-of select="c4j_XSLT_Ext:trim(PLANT_MHDHB)" /></shelf_life>
        <xsl:variable name="shelf_life_uom" select="c4j_XSLT_Ext:trim(PLANT_IPRKZ)" />
        <shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('ShelfLifeUom',$shelf_life_uom)" /></shelf_life_uom>
    </xsl:template>
    
    <!-- Get Locations -->
    <xsl:template match="E1MARCM">
        <xsl:variable name="current_plant" select="WERKS"/>
        <xsl:if test="$current_plant=$PLANT">
            <xsl:apply-templates select="E1MARDM"/>
        </xsl:if>
    </xsl:template>
    
    <!-- Get Units -->
    <xsl:template match="E1MARMM">
        <materialUOMDefinition>
            <uom> <xsl:value-of select="c4j_XSLT_Ext:trim(MEINH)" /></uom>
            <denominator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREZ)" /></denominator>
            <numerator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREN)" /></numerator>
            <ean><xsl:value-of select="c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(EAN11))" /></ean>
            <variant><xsl:value-of select="c4j_XSLT_Ext:padVariant(c4j_XSLT_Ext:trim(_-NESGLB_-E1MARMM000[1]/GTIN_VARIANT))" /></variant>
        </materialUOMDefinition>
    </xsl:template>
    
    <xsl:template match="E1MARDM">

        <xsl:variable name="current_sloc" select="LGORT"/>
        <xsl:variable name="current_status" select="./../MMSTA"/>
        <xsl:variable name="temp1" select="c4j_XSLT_Ext:concat($PLANT,'-')"  />
        <xsl:variable name="temp2" select="c4j_XSLT_Ext:concat($temp1,$current_sloc)"  />
        <xsl:variable name="current_locn"><xsl:value-of select="c4j:getReferenceItem('PlantSLOCtoLocation',$temp2)"/></xsl:variable>  

        <xsl:if test="$current_locn!=''">     
            <location>
                <location><xsl:value-of select="$current_locn"/></location>
                
                <status><xsl:value-of select="c4j:getReferenceItem('SAPMaterialStatus',$current_status)" /></status>
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

