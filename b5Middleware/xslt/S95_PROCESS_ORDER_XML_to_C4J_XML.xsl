<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:c4j="http://www.commander4j.com"
    xmlns:c4j_XSLT_Ext="http://com.commander4j.Transformation"
    exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>

    <!-- CONFIG DATA -->
    <xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
    
    <!-- Local Variables -->

    <xsl:variable name="CRID" select="string(/ProductionSchedule/ID[1])"></xsl:variable>
    <xsl:variable name="SAPPLANT" select="string(/ProductionSchedule/Location[1]/EquipmentID[1])"></xsl:variable>
    <xsl:variable name="SAPMATERIAL_LONG" select="string(/ProductionSchedule/ProductionRequest[1]/SegmentRequirement[1]/MaterialProducedRequirement[1]/MaterialDefinitionID[1])" /> 
    <xsl:variable name="SAPMATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPMATERIAL_LONG)" />
    <xsl:variable name="SAPORDER_LONG" select="string(/ProductionSchedule/ProductionRequest[1]/ID[1])"></xsl:variable>
    <xsl:variable name="SAPORDER_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPORDER_LONG)" />
    <xsl:variable name="DATENOW" select="current-dateTime()" />

    <xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
  
    <xsl:template match='ProductionSchedule'>

        <message>
            <plant><xsl:value-of select='Location/EquipmentID'/></plant>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>CRID <xsl:value-of select='$CRID' /></messageRef>
            <interfaceType>Process Order</interfaceType>
            <messageInformation>Process Order=<xsl:value-of select='$SAPORDER_SHORT'/></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            <messageData>
                <processOrder>
                    <orderNo><xsl:value-of select='$SAPORDER_SHORT' /></orderNo>
                    <receipeId><xsl:value-of select='ProductionRequest/SegmentRequirement/ID'/></receipeId>
                    <dueDate><xsl:value-of select='ProductionRequest/StartTime'/></dueDate>
                    <status><xsl:value-of select='ProductionRequest/SegmentRequirement/globe_SegmentState'/></status>
                    <plant><xsl:value-of select='$SAPPLANT'/></plant>
                    <xsl:apply-templates select='ProductionRequest/SegmentRequirement/MaterialProducedRequirement'/>
                    <xsl:apply-templates select='ProductionRequest/SegmentRequirement/SegmentRequirement'/>
                </processOrder>
            </messageData>
        </message>

    </xsl:template>
    
    <xsl:template match='ProductionRequest/SegmentRequirement/MaterialProducedRequirement'>
            <warehouse><xsl:value-of select='Location/EquipmentID'/></warehouse>
            <xsl:variable name="sloc" select='Location/Globe_StorageLocation'></xsl:variable>
            <storageLocation><xsl:value-of select='$sloc'/></storageLocation>
            <storageBin><xsl:value-of select='Location/Location/Location/EquipmentID'/></storageBin>
            <storageType><xsl:value-of select='Location/Location/EquipmentID'/></storageType>
            <location><xsl:value-of select="c4j:getReferenceItem('PlantSLOCtoLocation',concat(string($SAPPLANT),'-',string($sloc)))"/></location>
            <material><xsl:value-of select='$SAPMATERIAL_SHORT' /></material>
            <description><xsl:value-of select='Description'/></description>
            <requiredQuantity><xsl:value-of select='Quantity/QuantityString'/></requiredQuantity>
            <requiredUom><xsl:value-of select='Quantity/UnitOfMeasure'/></requiredUom>
            <xsl:variable name="STOCKTYPE" select="globe_StockType" />
            <defaultPalletStatus><xsl:value-of select="c4j:getReferenceItem('SAPStockType',$STOCKTYPE)"/></defaultPalletStatus>
    </xsl:template>
    
    <xsl:template match='ProductionRequest/SegmentRequirement/SegmentRequirement'>
        <requiredResource><xsl:value-of select='EquipmentRequirement/EquipmentID'/></requiredResource>         
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

