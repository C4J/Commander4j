<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:p="http://com.sage/X3/WWIMPFHMFGCM"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext p" version="2.0">

	<xsl:output encoding="UTF-8" indent="yes" method="xml"/>
	<xsl:strip-space elements="*"/>

	<!-- CONFIG DATA -->
	<xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config', 'HostRef')"/></xsl:variable>
	<xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config', 'Plant')"/></xsl:variable>
	<xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config', 'Warehouse')"/></xsl:variable>

	<xsl:template match="p:WWIMPFHMFGCM">
		<root>
			<xsl:apply-templates select='I' />
		</root>
	</xsl:template>
	
	<xsl:template match="I">

		<xsl:variable name="message_date_time" select="c4j_XSLT_Ext:getISODateTimeString()"
			xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>
		
		<xsl:variable name="filename_date_time" select="c4j_XSLT_Ext:getISODateTimeFilenameString()"
			xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>


		<xsl:variable name="reqdUom" select="requiredUom"/>
		<xsl:variable name="material" select="material"/>
		<xsl:variable name="order" select="orderNo"/>
		<xsl:variable name="reqdQuantity" select="requiredQuantity"/>
		<xsl:variable name="dueDate" select="dueDate"/>
		<xsl:variable name="customerID" select="customerID"/>
		<xsl:variable name="status" select="status"/>
		<xsl:variable name="customerName" select="customerName"/>

		<xsl:text>&#10;</xsl:text>
		<xsl:comment>*START* filename ORD<xsl:value-of select="$material"/>_<xsl:value-of select="$filename_date_time"/>.xml</xsl:comment>
		<xsl:text>&#10;</xsl:text>
		<message>
			<xsl:comment>Message Header</xsl:comment>
			<plant><xsl:value-of select="$PLANT"/></plant>
			<hostRef><xsl:value-of select="$HOSTREF"/></hostRef>
			<messageRef>SAGE Works Order <xsl:value-of select="$message_date_time"/></messageRef>
			<interfaceType>Process Order</interfaceType>
			<messageInformation>Order=<xsl:value-of select="$order"/></messageInformation>
			<interfaceDirection>Input</interfaceDirection>
			<messageDate><xsl:value-of select="$message_date_time"/></messageDate>

			<xsl:comment>Message Data</xsl:comment>
			<messageData>
				<processOrder>
					<orderNo><xsl:value-of select="$order"/></orderNo>
					<dueDate><xsl:value-of select="$dueDate"/>T00:00:00</dueDate>
					<material><xsl:value-of select="$material"/></material>
					<materialType>FERT</materialType>
					<description><xsl:value-of select="description"/></description>
					<status><xsl:value-of select="c4j:getReferenceItem('SageWorksOrderStatus', $status)"/></status>
					<location>AINTREE</location>
					<requiredResource></requiredResource>
					<recipeID></recipeID>
					<requiredQuantity><xsl:value-of select="$reqdQuantity"/></requiredQuantity>
					<requiredUom><xsl:value-of select="c4j:getReferenceItem('SageUOM', $reqdUom)"/></requiredUom>
					<defaultPalletStatus>Unrestricted</defaultPalletStatus>
					<xsl:if test="$customerID!=''">
						<customerID><xsl:value-of select="$customerID"/></customerID>
						<customerName><xsl:value-of select="$customerName"/></customerName>
					</xsl:if>
				</processOrder>
			</messageData>
		</message>
		<xsl:text>&#10;</xsl:text>
		<xsl:comment>*END* filename ORD_<xsl:value-of select="$material"/>_<xsl:value-of select="$filename_date_time"/>.xml</xsl:comment>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>

	<!-- ================
		FUNCTION get config data 
        ================ -->

	<xsl:function name="c4j:getConfigItem">
		<xsl:param name="type"/>
		<xsl:param name="string1"/>
		<xsl:variable name="item_info" select="document('configData.xml')/lookup"/>
		<xsl:value-of select="$item_info/item[@type = $type][@id = $string1]/value"/>
	</xsl:function>

	<!-- ================
        FUNCTION get reference data 
        ================ -->
	
	<xsl:function name="c4j:getReferenceItem">
		<xsl:param name="type"/>
		<xsl:param name="string1"/>
		<xsl:variable name="item_info" select="document('referenceData.xml')/lookup"/>
		<xsl:value-of select="$item_info/item[@type = $type][@id = $string1]/value"/>
	</xsl:function>
	
</xsl:stylesheet>
