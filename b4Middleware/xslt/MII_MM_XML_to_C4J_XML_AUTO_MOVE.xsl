<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
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
    
    <!-- CONFIG DATA -->
    
    <xsl:variable name="HOSTREF"                select="string(c4j:getConfigItem('config','HostRef'))"/>
    <xsl:variable name="PLANT"                  select="string(c4j:getConfigItem('config','Plant'))"/>
    <xsl:variable name="WAREHOUSE"              select="string(c4j:getConfigItem('config','Warehouse'))"/>
    <xsl:variable name="LOCATION_LOC"           select="string(c4j:getConfigItem('config','Location'))"/>
    <xsl:variable name="LOCATION_CTR"           select="string(c4j:getConfigItem('config','Location_Central'))"/>
    <xsl:variable name="PROCUREMENT_IND"        select="string(c4j:getConfigItem('config','Procurement_Ind'))"/>
    
    <!-- Local Variables -->

    <xsl:variable name="MATERIAL"               select="string(c4j_XSLT_Ext:removeLeadingZeros(string(/ZMATMAS03/IDOC/E1MARAM/MATNR)))"/>
    <xsl:variable name="MTYPE"                  select="string(/ZMATMAS03/IDOC/E1MARAM/MTART)"/>
    <xsl:variable name="USE_PLANT"              select="string(/ZMATMAS03/IDOC/E1MARAM/_-NESGLB_-DISTR000/WERKS[.=$PLANT]/../AD_PLANT_DATA)"/>
    <xsl:variable name="FOUND_PLANT"            select="string(/ZMATMAS03/IDOC/E1MARAM/E1MARCM/WERKS[.=$PLANT])"/>
    <xsl:variable name="FOUND_WAREHOUSE"        select="string(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE])" />
    <xsl:variable name="PROCUREMENT_FOUND"      select="string(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../E1MKALM/SOBSL)" />
    <xsl:variable name="PROCUREMENT_COUNT"      select="count(/ZMATMAS03/IDOC/E1MARAM/E1MLGNM/LGNUM[.=$WAREHOUSE]/../E1MKALM/SOBSL)"/>

    <xsl:template match="/ZMATMAS03">
        <message>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>USE PLANT         = [<xsl:value-of select="$USE_PLANT"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>FOUND PLANT       = [<xsl:value-of select="$FOUND_PLANT"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>FOUND WAREHOUSE   = [<xsl:value-of select="$WAREHOUSE"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>LOCATION_LOC      = [<xsl:value-of select="$LOCATION_LOC"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>LOCATION_CTR      = [<xsl:value-of select="$LOCATION_CTR"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>PROCUREMENT_IND   = [<xsl:value-of select="$PROCUREMENT_IND"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>PROCUREMENT_FOUND = [<xsl:value-of select="$PROCUREMENT_FOUND"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
            <xsl:comment>>PROCUREMENT_COUNT = [<xsl:value-of select="$PROCUREMENT_COUNT"/>]</xsl:comment>
            <xsl:text>&#x0A;</xsl:text>
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
        <interfaceType>Material Auto Move</interfaceType>
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

