<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">	
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>	
	
	<!-- ========================================
          PRODUCTION DECLARATION (PROD DEC)
         ====================================== -->

	<xsl:template match='message'>

	    <xsl:variable name="compdate" select='/message/messageData/productionDeclaration/productionDate'/>
		<xsl:variable name="expirydate" select='/message/messageData/productionDeclaration/expiryDate'/>
	
		<MESSAGE>
			<xsl:attribute name="number"><xsl:value-of select='/message/messageData/productionDeclaration/SSCC'/>_<xsl:value-of select='/message/messageDate'/></xsl:attribute>
			<xsl:attribute name="type">Cmd4j Production Declaration</xsl:attribute>
			<xsl:attribute name="id"><xsl:value-of select='/message/messageData/productionDeclaration/SSCC'/>_<xsl:value-of select='/message/messageDate'/></xsl:attribute>
			<SOURCE name="Commander4j"></SOURCE>
			<DATA type="Pallet">
				<FIELD name="SSCC">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/SSCC'/>
					</xsl:attribute>			
				</FIELD>
				<FIELD name="Plant">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/plant'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="Warehouse">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/warehouse'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="Material">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/material'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="LegacyMaterial">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/old_code'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="EANCode">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/ean'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="Variant">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/variant'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="OrderNumber">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/processOrder'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="CRID">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/recipe'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="productionDate">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/productionDate'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="CompletionDate">
					<xsl:attribute name="value">
						<xsl:value-of select="c4j_XSLT_Ext:ISO_Date_to_date_DD_MM_YYYY_HH_MM_SS($compdate)" />
					</xsl:attribute>
				</FIELD>
				<FIELD name="expiryDate">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/expiryDate'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="BBE">
					<xsl:attribute name="value">
						<xsl:value-of select="c4j_XSLT_Ext:ISO_Date_to_date_DD_MM_YYYY_HH_MM_SS($expirydate)" />
					</xsl:attribute>
				</FIELD>				
				<FIELD name="BBE1">
					<xsl:attribute name="value">
						<xsl:value-of select="c4j_XSLT_Ext:ISO_Date_to_date_MMYYYY($expirydate)" />
					</xsl:attribute>
				</FIELD>
				<FIELD name="BBE2">
					<xsl:attribute name="value">
						<xsl:value-of select="c4j_XSLT_Ext:ISO_Date_to_date_DDMMYYYY($expirydate)" />
					</xsl:attribute>
				</FIELD>
				<FIELD name="Quantity">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/productionQuantity'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="UOM">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/productionUOM'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="PalletType">
					<xsl:attribute name="value">FG</xsl:attribute>
				</FIELD>
				<FIELD name="BatchNumber">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/batch'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="InterfaceMakeIndicator">
					<xsl:attribute name="value">
						<xsl:value-of select='/message/messageData/productionDeclaration/confirmed'/>
					</xsl:attribute>
				</FIELD>
				<FIELD name="PrinterID">
					<xsl:attribute name="value"></xsl:attribute>
				</FIELD>
				<FIELD name="PrintLabel">
					<xsl:attribute name="value">N</xsl:attribute>
				</FIELD>
				<FIELD name="Final">
					<xsl:attribute name="value">N</xsl:attribute>
				</FIELD>
				<FIELD name="Line">
					<xsl:attribute name="value"></xsl:attribute>
				</FIELD>		
			</DATA>
		</MESSAGE>
	</xsl:template>

</xsl:stylesheet>
