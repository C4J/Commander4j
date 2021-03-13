<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>

    <xsl:template match='message'>

        <message>
            <hostRef><xsl:value-of select="hostRef" /></hostRef>
            <messageRef><xsl:value-of select='messageRef' /></messageRef>
            <interfaceType><xsl:value-of select='interfaceType' /></interfaceType>
            <messageInformation><xsl:value-of select='messageInformation' /></messageInformation>
            <interfaceDirection><xsl:value-of select='interfaceDirection' /></interfaceDirection>
            <messageDate><xsl:value-of select="messageDate"/></messageDate>
            <messageData>
                    <xsl:for-each select="/message/messageData/journeyDefinition">
                        <xsl:if test="destination[text()='HAMS HALL']">
                            <journeyDefinition>
                                <ref><xsl:value-of select='ref' /></ref>
                                <timeslot><xsl:value-of select='timeslot' /></timeslot>
                                <destination>HAMS HALL_PA</destination>
                                <haulier><xsl:value-of select='haulier' /></haulier>
                                <loadType><xsl:value-of select='loadType' /></loadType>
                                <loadTypeDesc><xsl:value-of select='loadTypeDesc' /></loadTypeDesc>
                                <action><xsl:value-of select='action' /></action>
                            </journeyDefinition>
                        </xsl:if>  
                        <xsl:if test="destination[text()='HAMS HALL_PA']">
                            <journeyDefinition>
                                <ref><xsl:value-of select='ref' /></ref>
                                <timeslot><xsl:value-of select='timeslot' /></timeslot>
                                <destination><xsl:value-of select='destination' /></destination>
                                <haulier><xsl:value-of select='haulier' /></haulier>
                                <loadType><xsl:value-of select='loadType' /></loadType>
                                <loadTypeDesc><xsl:value-of select='loadTypeDesc' /></loadTypeDesc>
                                <action><xsl:value-of select='action' /></action>
                            </journeyDefinition>
                        </xsl:if>  
<!--                        <xsl:if test="destination[text()='NESTLE BARDON CFG STORE']">
                            <journeyDefinition>
                                <ref><xsl:value-of select='ref' /></ref>
                                <timeslot><xsl:value-of select='timeslot' /></timeslot>
                                <destination>BARDON</destination>
                                <haulier><xsl:value-of select='haulier' /></haulier>
                                <loadType><xsl:value-of select='loadType' /></loadType>
                                <loadTypeDesc><xsl:value-of select='loadTypeDesc' /></loadTypeDesc>
                                <action><xsl:value-of select='action' /></action>
                            </journeyDefinition>
                        </xsl:if> -->
                    </xsl:for-each>
            </messageData>
        </message>
    </xsl:template>

       
</xsl:stylesheet>

