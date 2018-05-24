<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	exclude-result-prefixes="xs"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	
	<!-- CONFIG DATA -->
	<xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
	<xsl:variable name="SAPUsername"><xsl:value-of select="c4j:getConfigItem('config','SAPUsername')"/></xsl:variable>
	
	<!-- Local Variables -->
	
	<xsl:variable name="DATENOW" select="current-dateTime()"/>
	<xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
	<xsl:variable name="EXPIRY_DATE" select="/message/messageData/productionDeclaration/expiryDate"/>							

	<xsl:variable name="EXPIRY_SHORT" select="substring($EXPIRY_DATE,1,10)"/>
	
	<xsl:template match='message'>
		
		<message>
			<hostRef>
				<xsl:value-of select='/message/hostRef'/>
			</hostRef>
			<messageRef>
				<xsl:value-of select='/message/messageRef'/>
			</messageRef>
			<interfaceType>
				<xsl:value-of select='/message/interfaceType'/>
			</interfaceType>
			<messageInformation>
				<xsl:value-of select='/message/messageInformation'/>
			</messageInformation>	
			<interfaceDirection>
				<xsl:value-of select='/message/interfaceDirection'/>
			</interfaceDirection>				
			<messageDate>
				<xsl:value-of select='/message/messageDate'/>
			</messageDate>	
		
			<messageData>
				<productionDeclaration>
					<SSCC><xsl:value-of select='/message/messageData/productionDeclaration/SSCC'/></SSCC>
					<processOrder><xsl:value-of select='/message/messageData/productionDeclaration/processOrder'/></processOrder>
					<batch><xsl:value-of select='/message/messageData/productionDeclaration/batch'/></batch>
					<expiryDate><xsl:value-of select='$EXPIRY_SHORT'/>T23:59:59</expiryDate>
					<productionQuantity><xsl:value-of select='/message/messageData/productionDeclaration/productionQuantity'/></productionQuantity>
					<productionUOM><xsl:value-of select='/message/messageData/productionDeclaration/productionUOM'/></productionUOM>
					<confirmed><xsl:value-of select='/message/messageData/productionDeclaration/confirmed'/></confirmed>
					<productionDate><xsl:value-of select='/message/messageData/productionDeclaration/productionDate'/></productionDate>
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
