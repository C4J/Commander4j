<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext" version="2.0">

	<xsl:output encoding="UTF-8" indent="yes" method="xml"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="text() | @*"/>

	<xsl:template match="message/messageData">
		<data cols="2" rows="2" type="CSV">
			<row id="1">
				<col id="1">Despatch Ref</col>
				<col id="2">Journey Ref"</col>
			</row>
			<row id="2">
				<col id="1">
					<xsl:value-of
						select="/message/messageData[1]/despatchPreAdvice[1]/despatchNo[1]"/>
				</col>
				<col id="2">
					<xsl:value-of
						select="/message/messageData[1]/despatchPreAdvice[1]/journeyRef[1]"/>
				</col>
			</row>
		</data>
	</xsl:template>

</xsl:stylesheet>
