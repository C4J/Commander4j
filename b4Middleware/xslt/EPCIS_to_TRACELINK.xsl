<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:c4j="http://www.commander4j.com"
    xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
    xmlns:epcis="urn:epcglobal:epcis:xsd:1" 
    xmlns:sbdh="http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader" 
    xmlns:cbvmda="urn:epcglobal:cbv:mda" 
    xmlns:tl="http://epcis.tracelink.com/ns"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="epcis xsi sbdh cbvmda tl xs c4j c4j_XSLT_Ext"  version="2.0">
    
    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
    <!-- Local Variables -->
    <xsl:variable name="creationDateTime" select="string(/epcis:EPCISDocument/EPCISHeader[1]/sbdh:StandardBusinessDocumentHeader[1]/sbdh:DocumentIdentification[1]/sbdh:CreationDateAndTime[1])"></xsl:variable>
    
    <xsl:variable name="creationDate" select="substring($creationDateTime,1,10)" />
    <xsl:variable name="creationTime" select="substring($creationDateTime,12,8)" />
  
    <xsl:template match='epcis:EPCISDocument'>
        
        <snx:SNXEndOfBatchMessage xmlns:snx="urn:tracelink:mapper:sl:serial_number_exchange" xmlns:cmn="urn:tracelink:mapper:sl:commontypes" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <snx:ControlFileHeader>
                <cmn:FileSenderNumber><xsl:value-of select='/epcis:EPCISDocument/EPCISHeader[1]/sbdh:StandardBusinessDocumentHeader[1]/sbdh:Sender[1]/sbdh:Identifier[1]'/></cmn:FileSenderNumber>
                <cmn:FileReceiverNumber><xsl:value-of select='/epcis:EPCISDocument/EPCISHeader[1]/sbdh:StandardBusinessDocumentHeader[1]/sbdh:Receiver[1]/sbdh:Identifier[1]'/></cmn:FileReceiverNumber>
                <cmn:FileControlNumber><xsl:value-of select='/epcis:EPCISDocument/EPCISHeader[1]/sbdh:StandardBusinessDocumentHeader[1]/sbdh:DocumentIdentification[1]/sbdh:InstanceIdentifier[1]'/></cmn:FileControlNumber>
                <cmn:FileDate><xsl:value-of select='$creationDate'/></cmn:FileDate>
                <cmn:FileTime><xsl:value-of select='$creationTime'/></cmn:FileTime>
            </snx:ControlFileHeader>
            <snx:MessageBody>
                <cmn:InternalMaterialCode><xsl:value-of select='/epcis:EPCISDocument/EPCISBody[1]/EventList[1]/ObjectEvent[1]/tl:dispositionAssignedEventExtensions[1]/tl:itemDetail[1]/tl:internalMaterialCode[1]'/></cmn:InternalMaterialCode>
                <cmn:LotNumber><xsl:value-of select='/epcis:EPCISDocument/EPCISBody[1]/EventList[1]/ObjectEvent[1]/extension[1]/ilmd[1]/cbvmda:lotNumber[1]'/></cmn:LotNumber>
                <snx:ProductionQuantity>
                    <cmn:PackagingItemCode type="GTIN-14"><xsl:value-of select='/epcis:EPCISDocument/EPCISBody[1]/EventList[1]/ObjectEvent[1]/tl:dispositionAssignedEventExtensions[1]/tl:itemDetail[1]/tl:packagingItemCode[1]'/></cmn:PackagingItemCode>
                    <cmn:PackagingLevel><xsl:value-of select='/epcis:EPCISDocument/EPCISBody[1]/EventList[1]/ObjectEvent[1]/tl:dispositionAssignedEventExtensions[1]/tl:packagingLevel[1]'/></cmn:PackagingLevel>
                    <cmn:QuantityReported><xsl:value-of select='count(/epcis:EPCISDocument/EPCISBody[1]/EventList[1]/ObjectEvent[1]/epcList[1]/*)'/></cmn:QuantityReported>
                </snx:ProductionQuantity>
            </snx:MessageBody>
        </snx:SNXEndOfBatchMessage>
        
    </xsl:template>


</xsl:stylesheet>

