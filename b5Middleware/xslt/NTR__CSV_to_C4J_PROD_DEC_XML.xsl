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
	
	<xsl:variable name="DATENOW" select="current-dateTime()"/>
	<xsl:variable name="EXPIRYDATE" select="/data/row[@id='1']/col[@id='11']"/>
	<xsl:variable name="PRODUCTIONDATE" select="/data/row[@id='1']/col[@id='9']"/>
	<xsl:variable name="QUANTITY" select="/data/row[@id='1']/col[@id='12']"/>
	<xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>

	<xsl:template match='/'>
		<message>
			<hostRef><xsl:value-of select='$HOSTREF' /></hostRef>
			<messageRef><xsl:value-of select='/data/row[@id="1"]/col[@id="1"]'/>_<xsl:value-of select='$MESSAGEDATE' /></messageRef>
			<interfaceType>Production Declaration</interfaceType>
			<messageInformation>SSCC=<xsl:value-of select='/data/row[@id="1"]/col[@id="1"]'/></messageInformation>
			<interfaceDirection>Input</interfaceDirection>
			<messageDate><xsl:value-of select="c4j_XSLT_Ext:getISODateTimeString()" /></messageDate>
			<messageData>
				<productionDeclaration>
					<SSCC><xsl:value-of select='/data/row[@id="1"]/col[@id="1"]'/></SSCC>
					<processOrder><xsl:value-of select='/data/row[@id="1"]/col[@id="8"]'/></processOrder>
					<productionQuantity><xsl:value-of select="c4j_XSLT_Ext:removeLeadingZeros($QUANTITY)"/></productionQuantity>
					<productionUOM><xsl:value-of select='/data/row[@id="1"]/col[@id="13"]'/></productionUOM>
					<expiryDate><xsl:value-of select="c4j_XSLT_Ext:formatDate($EXPIRYDATE, 'ddMMyyyy', 'yyyy-MM-dd''T''HH:mm:ss')"/></expiryDate>
					<productionDate><xsl:value-of select="c4j_XSLT_Ext:formatDate($PRODUCTIONDATE, 'dd/MM/yyyy HH:mm:ss', 'yyyy-MM-dd''T''HH:mm:ss')"/></productionDate>
					<batch><xsl:value-of select='/data/row[@id="1"]/col[@id="15"]'/></batch>	
					<confirmed><xsl:value-of select='/data/row[@id="1"]/col[@id="16"]'/></confirmed>
				</productionDeclaration>
			</messageData>
		</message>
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
