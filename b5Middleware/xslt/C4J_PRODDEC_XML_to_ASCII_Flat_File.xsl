<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://com.commander4j.Transformation"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	<xsl:template match="text() | @*"/>
	<xsl:variable name="sscc" select="/message/messageData[1]/productionDeclaration[1]/SSCC"/>

	<xsl:template match="message/messageData/productionDeclaration">
		<data cols="15" rows="1" type="CSV">
		<row id="1">

			<col id="1"><xsl:value-of select="processOrder"/></col>
			<col id="2"><xsl:value-of select="material"/></col>
			<col id="3"><xsl:value-of select="c4j_XSLT_Ext:removeCommas(description)"/></col>
			<col id="4"><xsl:value-of select="floor(number(productionQuantity))"/></col>
			<col id="5"><xsl:value-of select='name' /></col>
			
			<col id="6"><xsl:value-of select="c4j_XSLT_Ext:formatDate(productionDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')"/></col>
			<col id="7"><xsl:value-of select="batch"/></col>
			<col id="8"><xsl:value-of select="SSCC"/></col>
			<col id="9"><xsl:value-of select="c4j_XSLT_Ext:formatDate(expiryDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')" /></col>
			
			<col id="10"><xsl:value-of select="warehouse"/></col>
			
			<col id="11"><xsl:value-of select="storageLocation"/></col>
			<col id="12"><xsl:value-of select="c4j_XSLT_Ext:formatDate(expiryDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')" /></col>
			<col id="13"><xsl:value-of select="c4j_XSLT_Ext:formatDate(productionDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')"/></col>
			<col id="14"><xsl:value-of select="old_code"/></col>
			<col id="15"><xsl:value-of select="c4j_XSLT_Ext:subString($sscc, 8, 9)"/></col>
		</row>
		</data>			
	</xsl:template>

</xsl:stylesheet>
