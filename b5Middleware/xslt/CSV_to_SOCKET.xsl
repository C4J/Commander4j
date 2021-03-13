<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
 
    <xsl:template match='data'>
        <data>
            <xsl:apply-templates select="row"/>
            <line>*EOF*</line>
        </data>

    </xsl:template>
    
    <xsl:template match='row'>

        <line eol="cr">^XA</line>
        <line eol="cr">^FX Top section with company logo, name and address.</line>
        <line eol="cr">^CF0,60</line>
        <line eol="cr">^FO50,50^GB100,100,100^FS</line>
        <line eol="cr">^FO75,75^FR^GB100,100,100^FS</line>
        <line eol="cr">^FO88,88^GB50,50,50^FS</line>
        <line eol="cr">^FO220,50^FDInternational Shipping, Inc.^FS</line>
        <line eol="cr">^CF0,40</line>
        <line eol="cr">^FO220,100^FD1000 Shipping Lane^FS</line>
        <line eol="cr">^FO220,135^FDShelbyville TN 38102^FS</line>
        <line eol="cr">^FO220,170^FDUnited States (USA)^FS</line>
        <line eol="cr">^FO50,250^GB700,1,3^FS</line>
        <line eol="cr">^FX Second section with recipient address and permit information.</line>
        <line eol="cr">^CFA,30</line>
        <line eol="cr">^FO50,300^FD<xsl:value-of select='col[1]'/>^FS</line>
        <line eol="cr">^FO50,340^FD<xsl:value-of select='col[2]'/>^FS</line>
        <line eol="cr">^FO50,380^FD<xsl:value-of select='col[3]'/>^FS</line>
        <line eol="cr">^FO50,420^FD<xsl:value-of select='col[4]'/>^FS</line>
        <line eol="cr">^CFA,15</line>
        <line eol="cr">^FO600,300^GB150,150,3^FS</line>
        <line eol="cr">^FO638,340^FDPermit^FS</line>
        <line eol="cr">^FO638,390^FD123456^FS</line>
        <line eol="cr">^FO50,500^GB700,1,3^FS</line>
        <line eol="cr">^FX Third section with barcode.</line>
        <line eol="cr">^BY5,2,270</line>
        <line eol="cr">^FO100,550^BC^FD12345678^FS</line>
        <line eol="cr">^FX Fourth section (the two boxes on the bottom).</line>
        <line eol="cr">^FO50,900^GB700,250,3^FS</line>
        <line eol="cr">^FO400,900^GB1,250,3^FS</line>
        <line eol="cr">^CF0,40</line>
        <line eol="cr">^FO100,960^FDShipping Ctr. X34B-1^FS</line>
        <line eol="cr">^FO100,1010^FDREF1 F00B47^FS</line>
        <line eol="cr">^FO100,1060^FDREF2 BL4H8^FS</line>
        <line eol="cr">^CF0,190</line>
        <line eol="cr">^FO485,965^FDCA^FS</line>
        <line eol="cr">^XZ</line>
    
    </xsl:template>

</xsl:stylesheet>