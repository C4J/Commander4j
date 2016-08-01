<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension">

	<!-- <xsl:output method="text" indent="no" encoding="UTF-8"/> -->
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:template match="text() | @*"/>

	<xsl:template match="message/messageData/productionDeclaration">

		<xsl:variable name="productionDate" select="productionDate"/>
		<xsl:variable name="productionQuantity" select="productionQuantity"/>
		<xsl:variable name="expiryDate" select="expiryDate"/>
		<xsl:variable name="description" select="description"/>
		<xsl:variable name="sscc" select="SSCC"/>

		<xsl:value-of select="processOrder"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="material"/>
		<xsl:value-of select="','"/>
		<xsl:value-of select="c4j_XSLT_Ext:padVariant($description)"
			xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>

		<xsl:value-of select="','"/>

		<xsl:value-of select="floor(number($productionQuantity))"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="name"/>
		<xsl:value-of select="','"/>

		<xsl:value-of
			select="c4j_XSLT_Ext:formatDate($productionDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="batch"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="SSCC"/>
		<xsl:value-of select="','"/>
		<xsl:value-of
			select="c4j_XSLT_Ext:formatDate($expiryDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="warehouse"/>
		<xsl:value-of select="','"/>

		<xsl:value-of select="storageLocation"/>

		<xsl:value-of select="','"/>
		<xsl:value-of
			select="c4j_XSLT_Ext:formatDate($productionDate, 'yyyy-MM-dd''T''HH:mm', 'dd-MMM-yyyy')"/>

		<xsl:value-of select="','"/>
		<xsl:value-of select="old_code"/>
		<xsl:value-of select="c4j_XSLT_Ext:subString($sscc, 8, 9)"/>

	</xsl:template>


</xsl:stylesheet>
