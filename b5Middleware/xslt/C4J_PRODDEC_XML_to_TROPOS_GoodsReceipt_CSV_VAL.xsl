<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext_padStringLeft="http://com.commander4j.Transformation.XSLT_Ext_padStringLeft"
	xmlns:c4j_XSLT_Ext_padStringRight="http://com.commander4j.Transformation.XSLT_Ext_padStringRight"
	xmlns:c4j_XSLT_Ext_subString="http://com.commander4j.Transformation.XSLT_Ext_subString"
	xmlns:c4j_XSLT_Ext_removeCommas="http://com.commander4j.Transformation.XSLT_Ext_removeCommas"
	xmlns:c4j_XSLT_Ext_formatDate="http://com.commander4j.Transformation.XSLT_Ext_formatDate"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext_subString c4j_XSLT_Ext_padStringLeft c4j_XSLT_Ext_padStringRight c4j_XSLT_Ext_removeCommas c4j_XSLT_Ext_formatDate"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	<xsl:template match="text() | @*"/>
	<xsl:variable name="sscc" select="/message/messageData[1]/productionDeclaration[1]/SSCC"/>

	<xsl:template match="message/messageData/productionDeclaration">
		<data type="CSV">
			
			<row id="1" cols="13">
	
				<col id="1">P/<xsl:value-of select="processOrder"/></col>
				<col id="2"><xsl:value-of select="material"/></col>
				<col id="3"><xsl:value-of select="c4j_XSLT_Ext_removeCommas:removeCommas(description)"/></col>
				<col id="4"><xsl:value-of select="floor(number(productionQuantity))"/></col>
				<col id="5"><xsl:value-of select='name' /></col>
				
				<col id="6"><xsl:value-of select="c4j_XSLT_Ext_formatDate:formatDate(productionDate, 'yyyy-MM-dd''T''HH:mm:ss', 'ddMMyy')"/></col>
				<col id="7"><xsl:value-of select="batch"/></col>
				<col id="8"><xsl:value-of select="SSCC"/></col>
				<col id="9"><xsl:value-of select="c4j_XSLT_Ext_formatDate:formatDate(expiryDate, 'yyyy-MM-dd''T''HH:mm:ss', 'ddMMyy')" /></col>
				
				<col id="10"><xsl:value-of select="warehouse"/></col>
				
				<col id="11"><xsl:value-of select="storageLocation"/></col>
				<col id="12"><xsl:value-of select="c4j_XSLT_Ext_formatDate:formatDate(productionDate, 'yyyy-MM-dd''T''HH:mm:ss', 'dd-MM-yy HH:mm:ss')"/></col>
	
				<col id="13"><xsl:value-of select="old_code"/><xsl:value-of select="c4j_XSLT_Ext_subString:subString($sscc, 8, 9)"/></col>
			</row>
			
		</data>			
	</xsl:template>

</xsl:stylesheet>
