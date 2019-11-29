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
    
    <!--  
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
    
    <!-- DEBUG DATA
    <xsl:variable name="HOSTREF"   >HULIVE</xsl:variable>
    <xsl:variable name="PLANT"     >0917</xsl:variable>
    <xsl:variable name="WAREHOUSE" >HU3</xsl:variable>
    <xsl:variable name="LANGUAGE"  >E</xsl:variable>
    <xsl:variable name="LOCATION"  >BUK</xsl:variable>
    -->
  
    <!-- Local Variables -->
    
    <xsl:variable name="BASE_UOM"           select="string(/ZMATMAS03/IDOC/E1MARAM/MEINS)"/>
    <xsl:variable name="MATERIAL"           select="c4j_XSLT_Ext:removeLeadingZeros(string(/ZMATMAS03/IDOC/E1MARAM/MATNR))"/>
    <xsl:variable name="MTYPE"              select="string(/ZMATMAS03/IDOC/E1MARAM/MTART)"/>
    <xsl:variable name="USE_PLANT"          select="string(/ZMATMAS03/IDOC/E1MARAM/_-NESGLB_-DISTR000/WERKS[.=$PLANT]/../AD_PLANT_DATA)"/>
    <xsl:variable name="FOUND_PLANT"        select="string(/ZMATMAS03/IDOC/E1MARAM/E1MARCM/WERKS[.=$PLANT])"/>
    <xsl:variable name="FOUND_WAREHOUSE"    select="string(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE])" />
    <xsl:variable name="LE_QTY"             select="number(concat('0',string(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHMG1)))" />
    <xsl:variable name="LE_UOM_NOTRIM"      select="string(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LHME1)" />
    <xsl:variable name="LE_UOM"             select="c4j_XSLT_Ext:trim(string($LE_UOM_NOTRIM))" />

    <xsl:variable name="LE_NUMERATOR"       select="number(concat('0',c4j_XSLT_Ext:trim(string(/ZMATMAS03/IDOC/E1MARAM/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREZ))))" />                                             
    <xsl:variable name="LE_DENOMINATOR"     select="number(concat('0',c4j_XSLT_Ext:trim(string(/ZMATMAS03/IDOC/E1MARAM/E1MARMM/MEINH[.=$LE_UOM_NOTRIM]/../UMREN))))" />
   
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
        <xsl:variable name="SAPDOCNUM_LONG" select="string(DOCNUM)" />
        <xsl:variable name="SAPDOCNUM_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPDOCNUM_LONG)" />
        <messageRef>DOCNUM <xsl:value-of select="$SAPDOCNUM_SHORT"/></messageRef>
        <messageInformation>Mateial=<xsl:value-of select="$MATERIAL"/>/<xsl:value-of select="$MTYPE"/></messageInformation>
        <xsl:variable name="CREATE_DATE" select="c4j_XSLT_Ext:trim(string(CREDAT))" />
        <xsl:variable name="CREATE_TIME" select="c4j_XSLT_Ext:trim(string(CRETIM))" />
        <xsl:variable name="CREATE_DATETIME" select="c4j_XSLT_Ext:concat($CREATE_DATE,$CREATE_TIME)"  />
        <messageDate><xsl:value-of select="c4j_XSLT_Ext:formatDate($CREATE_DATETIME, 'yyyyMMddHHmmss', 'yyyy-MM-dd''T''HH:mm:ss')"/></messageDate>
    </xsl:template>
    
    <xsl:template match="E1MARAM">
        <xsl:variable name="MATERIAL_LONG" select="string(MATNR)" />
        <xsl:variable name="MATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($MATERIAL_LONG)" />
        <material><xsl:value-of select="$MATERIAL_SHORT" /></material>
        <old_material><xsl:value-of select="string(BISMT)" /></old_material>
        <materialType><xsl:value-of select="string(MTART)" /></materialType>
        <base_uom><xsl:value-of select="string(MEINS)" /></base_uom>
        <gross_weight><xsl:value-of select="BRGEW" /></gross_weight>
        <net_weight><xsl:value-of select="NTGEW" /></net_weight>
        <weight_uom><xsl:value-of select="string(GEWEI)" /></weight_uom>

        <xsl:comment>Description for language <xsl:value-of select="$LANGUAGE" /></xsl:comment>
        
        <xsl:variable name="DESCRIPTION" select="string(E1MAKTM/SPRAS[.=$LANGUAGE]/../MAKTX)" />
        <description><xsl:value-of select="$DESCRIPTION"/></description>
        
        <xsl:if test="starts-with($DESCRIPTION,'ZZ')">
            <enabled>N</enabled>
        </xsl:if> 
        
        <xsl:if test="not(starts-with($DESCRIPTION,'ZZ'))">
            <enabled>Y</enabled>
        </xsl:if> 
        
        <equipment_Type><xsl:value-of select=" /ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../LETY1"/></equipment_Type>
        
        <xsl:comment>Use PLANT = [<xsl:value-of select="$USE_PLANT"/>]</xsl:comment>
        
        <xsl:if test="$USE_PLANT = 'X'">
            <xsl:apply-templates select="E1MARCM/_-GLB_-RGTE1MARCMBBD"/>
        </xsl:if>
        
        <xsl:if test="$USE_PLANT = ''">
            <shelf_life> <xsl:value-of select="c4j_XSLT_Ext:trim(MHDHB)" /></shelf_life>
            <xsl:variable name="shelf_life_uom" select="c4j_XSLT_Ext:trim(string(IPRKZ))" />
            <shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('ShelfLifeUom',$shelf_life_uom)" /></shelf_life_uom>
            <!--<shelf_life_rule1><xsl:value-of select="RDMHD" /></shelf_life_rule1>-->
        </xsl:if>

        <shelf_life_rule><xsl:value-of select="RDMHD" /></shelf_life_rule>
        
        <xsl:apply-templates select="E1MARMM"/>
        
        <validLocations>
             <xsl:apply-templates select="E1MARCM"/>
        </validLocations>
    </xsl:template>

    <xsl:template match="E1MARCM/_-GLB_-RGTE1MARCMBBD">
        <shelf_life> <xsl:value-of select="c4j_XSLT_Ext:trim(PLANT_MHDHB)" /></shelf_life>
        <xsl:variable name="shelf_life_uom" select="c4j_XSLT_Ext:trim(string(PLANT_IPRKZ))" />
        <shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('ShelfLifeUom',$shelf_life_uom)" /></shelf_life_uom>
        <!--<shelf_life_rule2><xsl:value-of select="PLANT_RDMHD" /></shelf_life_rule2>-->
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
            <xsl:variable name="current_uom" select="c4j_XSLT_Ext:trim(string(MEINH))"/>
            <xsl:variable name="current_ean" select='c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(string(EAN11)))' />
            <xsl:variable name="current_variant" select='c4j_XSLT_Ext:padVariant(c4j_XSLT_Ext:trim(string(_-NESGLB_-E1MARMM000[1]/GTIN_VARIANT)))' />
            <xsl:variable name="current_numerator" select="c4j_XSLT_Ext:trim(UMREZ)" />
            <xsl:variable name="current_denominator" select="c4j_XSLT_Ext:trim(UMREN)" />
            
            <uom> <xsl:value-of select="$current_uom" /></uom>
            <ean><xsl:value-of select="$current_ean" /></ean>
            <variant><xsl:value-of select="$current_variant" /></variant>

            <xsl:if test="$current_uom='D97'">   
                
                <!--<xsl:comment>[<xsl:value-of select="$LE_UOM"/>]</xsl:comment>-->
                <xsl:if test="$LE_UOM!=''">
                    <xsl:comment>* LE VALUES *</xsl:comment>
                    <xsl:comment>LE Quantity    = <xsl:value-of select="$LE_QTY" /></xsl:comment>
                    <xsl:comment>LE Unit        = <xsl:value-of select="$LE_UOM" /></xsl:comment>
                    <xsl:comment>LE Numerator   = <xsl:value-of select="$LE_NUMERATOR" /></xsl:comment>
                    <xsl:comment>LE Denominator = <xsl:value-of select="$LE_DENOMINATOR" /></xsl:comment>
                    <xsl:comment>* BASE VALUES *</xsl:comment>
                    <xsl:comment>Base Unit      = <xsl:value-of select="$BASE_UOM" /></xsl:comment>
                    <xsl:comment>* CALCULATIONS *</xsl:comment>
                    <xsl:comment>Converting LE Quantity from <xsl:value-of select='$LE_UOM' /> to <xsl:value-of select='$BASE_UOM' /></xsl:comment>  
                    
                    <xsl:variable name="temp98" select="number($current_numerator)" />
                    <xsl:variable name="temp99" select="number($LE_QTY * $LE_NUMERATOR)" />
                    <xsl:comment>D97 (in <xsl:value-of select='$BASE_UOM' />) = <xsl:value-of select='$LE_QTY' />(<xsl:value-of select='$LE_UOM' />) x <xsl:value-of select='$LE_NUMERATOR' /> = <xsl:value-of select='$temp99' /></xsl:comment> 
                    
                    <xsl:comment>Calculated LE quantity is <xsl:value-of select="$temp99" /></xsl:comment>
                    
                    <xsl:if test="$temp99=0">
                        <numerator><xsl:value-of select="c4j_XSLT_Ext:trim(UMREZ)" /></numerator>
                    </xsl:if>
                    
                    <xsl:if test="$temp99>0">
    
                        <xsl:if test="$temp98!=$temp99">
                            <xsl:comment>LE Qty <xsl:value-of select='$temp99'/> is different to Normal Qty <xsl:value-of select='$temp98'/> - setting override flag</xsl:comment>
                            <numerator><xsl:value-of select='$temp99'/></numerator>
                            <override>X</override>
                        </xsl:if>

                        <xsl:if test="$temp98=$temp99">
                            <xsl:comment>LE Qty <xsl:value-of select='$temp99'/> is the same Normal Qty <xsl:value-of select='$temp98'/></xsl:comment>
                            <numerator><xsl:value-of select='$temp99'/></numerator>
                        </xsl:if>
                    </xsl:if>
                    
                </xsl:if>
                
                <xsl:if test="$LE_UOM=''">
                    <xsl:comment>LE Values NOT Found</xsl:comment>
                    <numerator><xsl:value-of select="$current_numerator" /></numerator>
                </xsl:if>
                
            </xsl:if>

            <xsl:if test="$current_uom!='D97'"> 
                <numerator><xsl:value-of select="$current_numerator" /></numerator>
            </xsl:if>
            
            <denominator><xsl:value-of select="$current_denominator" /></denominator>

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

