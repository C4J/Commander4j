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

	<!-- ========================================
          PRODUCTION DECLARATION (PROD DEC)
         ====================================== -->

	<xsl:template match='MESSAGE'>
		<message>
			<hostRef><xsl:value-of select='$HOSTREF' /></hostRef>
			<messageRef><xsl:value-of select='/MESSAGE[@type="ProdDec"]/@id' /></messageRef>
			<interfaceType>Production Declaration</interfaceType>
			<messageInformation>SSCC=<xsl:value-of select='DATA[@type="MMPA"]/FIELD[@name="SSCC"]/@value'/></messageInformation>
			<interfaceDirection>Input</interfaceDirection>
			<messageDate><xsl:value-of select="c4j_XSLT_Ext:getISODateTimeString()" /></messageDate>
			<messageData>
				<productionDeclaration>
					<xsl:apply-templates select='DATA [@type="MMPA"]' />
					<xsl:apply-templates select='DATA [@type="Header"]' />
					<confirmed>Y</confirmed>
				</productionDeclaration>
			</messageData>
		</message>
	</xsl:template>

	<!-- ====================================
         HEADER DETAILS (PROD DEC)
         =================================== -->

	<xsl:template match='DATA [@type="Header"]'>
		<processOrder><xsl:value-of select='FIELD[@name="OrderNumber"]/@value' /></processOrder>
	</xsl:template>	
	
	<!-- ====================================
          MPR DETAILS (PROD DEC)
         =================================== -->

	<xsl:template match='DATA [@type="MMPA"]'>
		<SSCC><xsl:value-of select='FIELD[@name="SSCC"]/@value' /></SSCC>
		<productionQuantity><xsl:value-of select='FIELD[@name="Quantity"]/@value' /></productionQuantity>		
		<productionUOM><xsl:value-of select='FIELD[@name="UOM"]/@value' /></productionUOM>
		<expiryDate><xsl:value-of select='FIELD[@name="ExpirationDate"]/@value' /></expiryDate>
		<productionDate><xsl:value-of select='FIELD[@name="ProductionDate"]/@value' /></productionDate>
		<batch><xsl:value-of select='FIELD[@name="BatchNumber"]/@value' /></batch>		
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
