<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
 
    <xsl:template match='data'>
        <test>
            <xsl:apply-templates select="row"/>
        </test>

    </xsl:template>
    
    
    <xsl:template match='row'>

            <barcode><xsl:value-of select='col[1]'/>-<xsl:value-of select='col[2]'/></barcode>
        
    </xsl:template>
        

    
</xsl:stylesheet>