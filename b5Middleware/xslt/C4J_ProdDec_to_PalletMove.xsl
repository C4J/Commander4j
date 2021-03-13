<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">

    <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    
    <xsl:variable name="moveAfterMake"><xsl:value-of select="/message/messageData[1]/productionDeclaration[1]/moveAfterMake[1]"/></xsl:variable>

    <xsl:template match="message">
        
        <message>
            <hostRef><xsl:value-of select="hostRef" /></hostRef>
            <messageRef><xsl:value-of select="messageRef" /></messageRef>
            <interfaceType>Pallet Move</interfaceType>
            <messageInformation><xsl:value-of select="messageInformation" /></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="messageDate" /></messageDate>
            
            <xsl:apply-templates select="messageData"/>  
            
        </message>
        
    </xsl:template>
    
    <xsl:template match="messageData">
        <messageData>
            
        <xsl:apply-templates select="productionDeclaration"/>
            
        </messageData>
    </xsl:template>
    
    <xsl:template match="productionDeclaration">
        <palletMove>
            <pallet>
                <sscc><xsl:value-of select="SSCC" /></sscc>
                <fromLocation><xsl:value-of select="location" /></fromLocation>
                
                <moveAfterMake><xsl:value-of select="$moveAfterMake" /></moveAfterMake>
                
                <xsl:if test="$moveAfterMake = 'Y'">
                    <toLocation><xsl:value-of select="moveLocationID" /></toLocation>
                </xsl:if>
                
                <xsl:if test="$moveAfterMake = 'N'">
                    <toLocation><xsl:value-of select="location" /></toLocation>
                </xsl:if>
            </pallet>
        </palletMove>
    </xsl:template>

</xsl:stylesheet>
