<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://com.commander4j.Transformation"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	<xsl:template match="text() | @*"/>
	<xsl:variable name="rowcount" select="count(/message/messageData/equipmentTracking/contents/Equipment/equipmentID)"/>
	
	
	<xsl:template match="/message/messageData/equipmentTracking">
	
		<data cols="11" type="CSV"><xsl:attribute name="rows" select="$rowcount"/>
			<xsl:apply-templates select="/message/messageData/equipmentTracking/contents/Equipment"/>     
		</data>		
		
	</xsl:template>
	
	<xsl:template match="/message/messageData/equipmentTracking/contents/Equipment">

		<xsl:variable name="prefix" select="'FCTR'" />
		<xsl:variable name="despatchNo" select='/message/messageData/equipmentTracking/despatchNo'/>
		<xsl:variable name="despatchDate" select='/message/messageData/equipmentTracking/despatchDate'/>
		<xsl:variable name="haulier" select='/message/messageData/equipmentTracking/haulier'/>
		<xsl:variable name="despatchDate" select='/message/messageData/equipmentTracking/despatchDate'/>
		<xsl:variable name="fromLocation" select='/message/messageData/equipmentTracking/fromEquipmentTrackingID'/>
		<xsl:variable name="toLocation"   select='/message/messageData/equipmentTracking/toEquipmentTrackingID'/>
		<xsl:variable name="equipmentID" select='equipmentID'/>
		<xsl:variable name="count" select='count'/>
		<xsl:variable name="pos" select="position()"/>

		<row >
			<xsl:attribute name="id" select="$pos"/>
			<col id="1"><xsl:value-of select="$prefix"/></col>
			<col id="2"><xsl:value-of select="c4j_XSLT_Ext:padStringRight($despatchNo, 8, ' ')" /></col>
			<col id="3"><xsl:value-of select="c4j_XSLT_Ext:subString($despatchDate, 0, 4)" /></col>
			<col id="4"><xsl:value-of select="c4j_XSLT_Ext:subString($despatchDate, 5, 2)" /></col>
			<col id="5"><xsl:value-of select="c4j_XSLT_Ext:subString($despatchDate, 8, 2)" /></col>
			<col id="6"><xsl:value-of select="c4j_XSLT_Ext:subString($despatchDate, 11, 8)" /></col>					
			<col id="7"><xsl:value-of select="$toLocation" /></col>
			<col id="8"><xsl:value-of select="$fromLocation" /></col>	
			<col id="9"><xsl:value-of select="c4j_XSLT_Ext:padStringRight($equipmentID, 2, ' ')" /></col>
			<col id="10"><xsl:value-of select="c4j_XSLT_Ext:padStringRight($count, 6, ' ')" /></col>
			<col id="11"><xsl:value-of select="c4j_XSLT_Ext:padStringRight($haulier, 10, ' ')" /></col>	
		</row>
		
	</xsl:template>
	
</xsl:stylesheet>