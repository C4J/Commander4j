<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	<xsl:template match="text() | @*"/>
	<xsl:variable name="sscc" select="/message/messageData[1]/productionDeclaration[1]/SSCC"/>

	<xsl:template match="message/messageData/productionDeclaration">
		<data cols="9" rows="2" type="CSV">
			
		<row id="1">

			<col id="1">H</col>
			<col id="2">LIV</col>
			<col id="3"><xsl:value-of select="processOrder"/></col>
			<col id="4"><xsl:value-of select="material"/></col>
			<col id="5"><xsl:value-of select="floor(number(productionQuantity))"/></col>
			<col id="6"><xsl:value-of select="productionUOM"/></col>
			<col id="7">1</col>
		</row>
			
		<row id="2">
			<col id="1">S</col>
			<col id="2"><xsl:value-of select="productionUOM"/></col>
			<col id="3"><xsl:value-of select="floor(number(productionQuantity))"/></col>
			<col id="4">1</col>
			<col id="5"><xsl:value-of select="warehouse"/></col>
			<col id="6">A</col>
			<col id="7"><xsl:value-of select="batch"/><xsl:value-of  select="substring($sscc,12,3)"/></col>
			<col id="8"><xsl:value-of  select="substring($sscc,15,3)"/></col>
			<col id="9"><xsl:value-of select="ean"/></col>

		</row>
			
		</data>			
	</xsl:template>

</xsl:stylesheet>
