<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    
    <xsl:template match='data'>
        <data>
            <xsl:attribute name="type">CSV</xsl:attribute>
            <xsl:apply-templates select="row"/>
        </data>
    </xsl:template>
    
    <xsl:template match='row'>
        <row>
            <xsl:attribute name="cols"><xsl:value-of select="count(col)"/></xsl:attribute>
            <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--            /data/row[1]/@id-->
            <xsl:apply-templates select="col"/>
        </row>
    </xsl:template>
    
    <xsl:template match='col'>
        
        <col>
            <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            <xsl:value-of select='.'/>
        </col>
    </xsl:template>
    
    
</xsl:stylesheet>