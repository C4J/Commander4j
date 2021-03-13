<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  
  exclude-result-prefixes="xs"
  version="2.0">
  
  <xsl:output encoding="UTF-8" indent='yes' method="xml" />
  
  <xsl:template match='/message'>
    <data>
      <xsl:attribute name="type">CSV</xsl:attribute>
      <xsl:apply-templates select="messageData"/>
    </data>
  </xsl:template>
  
  <xsl:template match='messageData'>
    <xsl:apply-templates select="despatchPreAdvice"/>
  </xsl:template>
  
  <xsl:template match='despatchPreAdvice'>
    <xsl:apply-templates select="contents"/>
  </xsl:template>
  
  <xsl:template match='contents'>
    <xsl:apply-templates select="pallet"/>
  </xsl:template>
  
  <xsl:template match='pallet'>
    <row>
      <xsl:attribute name="id"><xsl:value-of select="item"/></xsl:attribute>
      <xsl:attribute name="cols">11</xsl:attribute>   
      <col>
        <xsl:attribute name="id">1</xsl:attribute>
        <xsl:value-of select='SSCC'/>
      </col>
      <col>
        <xsl:attribute name="id">2</xsl:attribute>
        <xsl:value-of select='material'/>
      </col>
      <col>
        <xsl:attribute name="id">3</xsl:attribute>
        <xsl:value-of select='ean'/>
      </col>
      <col>
        <xsl:attribute name="id">4</xsl:attribute>
        <xsl:value-of select='variant'/>
      </col>
      <col>
        <xsl:attribute name="id">5</xsl:attribute>
        <xsl:value-of select='quantity'/>
      </col>
      <col>
        <xsl:attribute name="id">6</xsl:attribute>
        <xsl:value-of select='UOM'/>
      </col>
      <col>
        <xsl:attribute name="id">7</xsl:attribute>
        <xsl:value-of select='status'/>
      </col>
      <col>
        <xsl:attribute name="id">8</xsl:attribute>
        <xsl:value-of select='bestBefore'/>
      </col> 
      <col>
        <xsl:attribute name="id">9</xsl:attribute>
        <xsl:value-of select='productionDate'/>
      </col>
      <col>
        <xsl:attribute name="id">10</xsl:attribute>
        <xsl:value-of select='batch'/>
      </col>
      <col>
        <xsl:attribute name="id">11</xsl:attribute>
        <xsl:value-of select='batchStatus'/>
      </col>
    </row>
  </xsl:template>
  
  <xsl:template match='col'>
    
    <col>
      <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
      <xsl:value-of select='.'/>
    </col>
  </xsl:template>
  
</xsl:stylesheet>
