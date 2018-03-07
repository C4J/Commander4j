<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xs"  version="2.0">
  <xsl:output encoding="UTF-8" indent='yes' method="xml" />
  <xsl:strip-space  elements="*"/>
  <xsl:template match="text() | @*"/>


  <!-- ========================================
          PALLET TRACKING MESSAGE (MPS SYSTEM)
         ====================================== -->

  <xsl:template match='/message/messageData[1]/despatchPreAdvice[1]/contents[1]'>
    <raw>
      <xsl:apply-templates select="pallet"/>
    </raw>
  </xsl:template>
  
  
  <xsl:template match='pallet'>
 
    <xsl:variable name="despatchNo" select='/message[1]/messageData[1]/despatchPreAdvice[1]/despatchNo[1]'/>
    <xsl:variable name="haulier" select='/message[1]/messageData[1]/despatchPreAdvice[1]/haulier[1]'/>
    <xsl:variable name="trailer" select='/message[1]/messageData[1]/despatchPreAdvice[1]/trailer[1]'/>
    <xsl:variable name="despatchDate" select='/message[1]/messageData[1]/despatchPreAdvice[1]/despatchDate[1]'/>
    <xsl:variable name="fromLocation" select='/message[1]/messageData[1]/despatchPreAdvice[1]/fromEquipmentTrackingID[1]'/>
    <xsl:variable name="toLocation" select='/message[1]/messageData[1]/despatchPreAdvice[1]/toEquipmentTrackingID[1]'/>

     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='$despatchNo'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='$haulier'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>    
     <xsl:value-of select='$trailer'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>    
     <xsl:value-of select='$despatchDate'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='$fromLocation'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='$toLocation'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='item' />
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='SSCC' />
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='material' />
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='variant'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='ean'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='batch' />
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>    
     <xsl:value-of select='quantity'/>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='UOM'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='status'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='batchStatus'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='productionDate'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select="','"/>
     <xsl:text>&#34;</xsl:text>
     <xsl:value-of select='bestBefore'/>
     <xsl:text>&#34;</xsl:text>
     <xsl:text>&#13;</xsl:text>
     <xsl:text>&#10;</xsl:text>
    
  </xsl:template>


</xsl:stylesheet>
