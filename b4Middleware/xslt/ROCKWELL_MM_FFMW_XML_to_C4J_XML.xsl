<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:c4j="http://www.commander4j.com"
                xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
                exclude-result-prefixes="xs c4j c4j_XSLT_Ext"  version="2.0">

    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
    <!-- CONFIG DATA -->
    <xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
    <xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config','Plant')"/></xsl:variable>
    <xsl:variable name="LOCATION_LOC"><xsl:value-of select="c4j:getConfigItem('config','Location')"/></xsl:variable>
    <xsl:variable name="LOCATION_CTR"><xsl:value-of select="c4j:getConfigItem('config','Location_Central')"/></xsl:variable>
    <xsl:variable name="PROCUREMENT_IND"><xsl:value-of select="c4j:getConfigItem('config','Procurement_Ind')"/></xsl:variable>
    <xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config','Warehouse')"/></xsl:variable>
    <xsl:variable name="LANGUAGE"><xsl:value-of select="c4j:getConfigItem('config','Language')"/></xsl:variable>
    
    <!-- Local Variables -->
    <xsl:variable name="BASE_UOM" select="string(/MESSAGE/DATA[@type='Basic']/FIELD[@name='BaseUOM']/@value)" />
    <xsl:variable name="D97_EAN" select="string(/MESSAGE/DATA[@type='UOM']/FIELD[@name='UOM'][@value='D97']/parent::*/FIELD[@name='EANItemCode']/@value)" />
    <xsl:variable name="D97_VARIANT" select="string(/MESSAGE/DATA[@type='UOM']/FIELD[@name='UOM'][@value='D97']/parent::*/FIELD[@name='EANVariant']/@value)" />
    <xsl:variable name="D97_DENOMINATOR" select="string(/MESSAGE/DATA[@type='UOM']/FIELD[@name='UOM'][@value='D97']/parent::*/FIELD[@name='Denominator']/@value)" />
    <xsl:variable name="D97_NUMERATOR" select="string(/MESSAGE/DATA[@type='UOM']/FIELD[@name='UOM'][@value='D97']/parent::*/FIELD[@name='Numerator']/@value)" />
    
    <xsl:variable name="temp1" select="string(/MESSAGE/DATA[@type='Warehouse']/FIELD[@name='Warehouse'][@value=$WAREHOUSE]/parent::*/FIELD[@name='LEQuantity']/@value)" />
    <xsl:variable name="temp2" select="concat('0',$temp1)"/>
    <xsl:variable name="LE_QUANTITY" select="number($temp2)" />
    <xsl:variable name="LE_UOM" select="string(/MESSAGE/DATA[@type='Warehouse']/FIELD[@name='Warehouse'][@value=$WAREHOUSE]/parent::*/FIELD[@name='LEUnit']/@value)"/>
    <xsl:variable name="LE_NUMERATOR" select="number(/MESSAGE/DATA[@type='UOM']/FIELD[@name='UOM'][@value=$LE_UOM]/parent::*/FIELD[@name='Numerator']/@value)" />
    
    
    <xsl:variable name="SAPMATERIAL_LONG" select="string(/MESSAGE/DATA[@type='Basic']/FIELD[@name='Material']/@value)" />
    <xsl:variable name="SAPMATERIAL_SHORT" select="c4j_XSLT_Ext:removeLeadingZeros($SAPMATERIAL_LONG)" />
    <xsl:variable name="USE_PLANT" select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='UsePlantFields']/@value)" />
    <xsl:variable name="PLANT_FOUND" select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='Plant']/@value)" />
    
    <xsl:variable name="PROCUREMENT" select="string(/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$PLANT]/parent::*/FIELD[@name='SpecialProcessing']/@value)" />
    
    <xsl:variable name="USE_WAREHOUSE" select="string(/MESSAGE/DATA[@type='Warehouse']/FIELD[@name='Warehouse'][@value=$WAREHOUSE]/parent::*/FIELD[@name='Warehouse']/@value)" />
    <xsl:variable name="DATENOW" select="current-dateTime()"/>
    <xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
  
    <xsl:template match="MESSAGE">
         
        <message>
            <plant><xsl:value-of select="$PLANT" /></plant>
            <hostRef><xsl:value-of select="$HOSTREF" /></hostRef>
            <messageRef>MM <xsl:value-of select='$SAPMATERIAL_LONG' /></messageRef>
            <interfaceType>Material Definition</interfaceType>
            <messageInformation>Material=<xsl:value-of select='$SAPMATERIAL_SHORT' /></messageInformation>
            <interfaceDirection>Input</interfaceDirection>
            <messageDate><xsl:value-of select="$MESSAGEDATE"/></messageDate>
            <messageData>
                <materialDefinition>
                    <xsl:apply-templates select='DATA [@type="Basic"]' />
                    <xsl:apply-templates select='DATA [@type="Text"]' />
                    <!--<debug><xsl:value-of select="$USE_PLANT" /></debug>-->
                    
                    <xsl:if test="$USE_PLANT='X'">
                        <xsl:comment>>Data from Plant [<xsl:value-of select="$PLANT"/>] Segment</xsl:comment>
                        <xsl:apply-templates select='DATA [@type="Plant"]' />
                     </xsl:if>
                    
                    <xsl:if test="$USE_PLANT!='X'">
                        <xsl:comment>>Data from Basic Segment</xsl:comment>
                        <shelf_life><xsl:value-of select='DATA[@type="Basic"]/FIELD[@name="ShelfLife"]/@value' /></shelf_life>
                        
                        <xsl:variable name="temp1" select='string(DATA[@type="Basic"]/FIELD[@name="RoundingRuleSLED"]/@value)'/>
                        <shelf_life_rule><xsl:value-of select="c4j:getReferenceItem('ShelfLifeRounding',$temp1)"/></shelf_life_rule>    
                                               
                        <xsl:variable name="temp2" select='string(DATA[@type="Basic"]/FIELD[@name="PeriodIndforSLED"]/@value)'/>
                        <shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('ShelfLifeDuration',$temp2)"/></shelf_life_uom>
                        
                    </xsl:if>
                    
                    <xsl:if test="$PLANT=$PLANT_FOUND">
                    
                        <xsl:comment>>Procurement Indicator for Plant [<xsl:value-of select="$PLANT"/>]</xsl:comment>
                        
                        <xsl:if test="$PROCUREMENT=$PROCUREMENT_IND">
                            <moveAfterMake>Y</moveAfterMake>
                            <moveLocationID><xsl:value-of select="$LOCATION_CTR" /></moveLocationID>
                        </xsl:if>  
                        
                        <xsl:if test="$PROCUREMENT!=$PROCUREMENT_IND">
                            <moveAfterMake>N</moveAfterMake>
                            <moveLocationID><xsl:value-of select="$LOCATION_LOC" /></moveLocationID>
                        </xsl:if>      
                     
                    </xsl:if>
                    
                    <xsl:if test="$USE_WAREHOUSE=$WAREHOUSE">
                        <xsl:comment>>Equipment from Warehouse Segment [<xsl:value-of select="$WAREHOUSE"/>]</xsl:comment>
                        <equipment_Type><xsl:value-of select="string(/MESSAGE/DATA[@type='Warehouse']/FIELD[@name='Warehouse'][@value=$WAREHOUSE]/parent::*/FIELD[@name='SUType']/@value)" /></equipment_Type>
                    </xsl:if>

                    <xsl:if test="$USE_WAREHOUSE!=$WAREHOUSE">
                        <xsl:comment>>Equipment undefined - No Warehouse Segment [<xsl:value-of select="$WAREHOUSE"/>]</xsl:comment>
                        <equipment_Type></equipment_Type>
                    </xsl:if>

                    <validLocations>
                        <location>
                            <id>REWORK</id>
                            <status>Valid</status>
                        </location>
                        <xsl:apply-templates select='DATA [@type=&quot;Location&quot;]' />
                    </validLocations>

                    <xsl:apply-templates select='DATA [@type="UOM"]' />
                    
                    <validUOMs>
                        <xsl:comment>>New XML structure for future enhancement</xsl:comment>
                        <xsl:apply-templates select='DATA [@type="UOM"]' />
                    </validUOMs>
                </materialDefinition>
            </messageData>
        </message>

    </xsl:template>
    
    <!-- ================
           BASIC DATA
         ================ -->
    
    <xsl:template match='DATA [@type="Basic"]'>
        <material><xsl:value-of select="$SAPMATERIAL_SHORT" /></material>
        <old_material><xsl:value-of select='FIELD[@name="OldMaterialCode"]/@value' /></old_material>
        <materialType><xsl:value-of select='FIELD[@name="MaterialType"]/@value' /></materialType>
        <base_uom><xsl:value-of select='$BASE_UOM' /></base_uom>
        <gross_weight><xsl:value-of select='FIELD[@name="GrossWeight"]/@value' /></gross_weight>
        <net_weight><xsl:value-of select='FIELD[@name="NetWeight"]/@value' /></net_weight>
        <weight_uom><xsl:value-of select='FIELD[@name="WeightUnit"]/@value' /></weight_uom>
    </xsl:template>
    
    <!-- =====================
          DESCRIPTION 
         ==================== -->
    
    <xsl:template match='DATA [@type="Text"]/FIELD[@name="Language"][@value=$LANGUAGE]'>
        <xsl:comment>>Text Description for Language [<xsl:value-of select="$LANGUAGE"/>]</xsl:comment>
        <description><xsl:value-of select="parent::*/FIELD[@name='Description']/@value" /></description>
        <xsl:variable name="temp1" select='parent::*/FIELD[@name="Description"]/@value'/>
        <xsl:if test="starts-with($temp1,'ZZ')">
            <enabled>N</enabled>
        </xsl:if> 
        <xsl:if test="not(starts-with($temp1,'ZZ'))">
            <enabled>Y</enabled>
        </xsl:if> 
    </xsl:template>
    
    <!-- ================
          PLANT DETAILS
         ================ -->
    
    <xsl:template match='DATA [@type="Plant"]/FIELD[@name="Plant"][@value=$PLANT]'>
        <shelf_life><xsl:value-of select="parent::*/FIELD[@name='ShelfLife']/@value" /></shelf_life>
        <xsl:variable name="temp1" select='parent::*/FIELD[@name="RoundingRuleSLED"]/@value'/>
        <shelf_life_rule><xsl:value-of select="c4j:getReferenceItem('ShelfLifeRounding',$temp1)"/></shelf_life_rule>     
        <xsl:variable name="temp2" select='parent::*/FIELD[@name="PeriodIndforSLED"]/@value'/>
        <shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('ShelfLifeDuration',$temp2)"/></shelf_life_uom>
    </xsl:template>
      
    <!-- ================
           UOM DETAILS
         ================ -->
    
    <xsl:template match='DATA [@type="UOM"]'>
        <materialUOMDefinition>
            <xsl:variable name="CURRENT_UOM" select='string(FIELD[@name="UOM"]/@value)' />
            
            <!-- Special Logic for D97 -->
            <xsl:if test="$CURRENT_UOM='D97'">  
                
                <xsl:if test="$LE_QUANTITY=0">
                    <!-- Using UOM Quantity for D97 -->
                    <uom><xsl:value-of select='FIELD[@name="UOM"]/@value' /></uom>
                    <xsl:comment>Using UOM Quantity for D97</xsl:comment>
                    <numerator><xsl:value-of select='FIELD[@name="Numerator"]/@value' /></numerator> 
                    <denominator><xsl:value-of select='FIELD[@name="Denominator"]/@value' /></denominator>
                    <ean><xsl:value-of select='c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(FIELD[@name="EANItemCode"]/@value))' /></ean>
                    <variant><xsl:value-of select='FIELD[@name="EANVariant"]/@value' /></variant>      
                </xsl:if>  
                
                <xsl:if test="$LE_QUANTITY>0">
                    <!-- Using LE Quantity for D97 --> 
                    <uom><xsl:value-of select='$CURRENT_UOM' /></uom>
               
                    <xsl:if test="$LE_UOM != $CURRENT_UOM">
                        <xsl:variable name="temp97" select='FIELD[@name="Numerator"]/@value' />
                        <xsl:variable name="temp98" select="number($temp97)" />  
                        <xsl:comment>Converting LE Quantity from <xsl:value-of select='$LE_UOM' /> into <xsl:value-of select='$BASE_UOM' /> before inserting calculated D97</xsl:comment> 
                        <xsl:comment>LE QTY is <xsl:value-of select='$LE_QUANTITY' /></xsl:comment> 
                        <xsl:comment>LE UOM is <xsl:value-of select='$LE_UOM' /></xsl:comment> 
                        <xsl:comment><xsl:value-of select='$LE_UOM' /> numerator is <xsl:value-of select='$LE_NUMERATOR' /></xsl:comment> 
                        <xsl:variable name="temp99" select="number($LE_QUANTITY * $LE_NUMERATOR)" />
                        <xsl:comment>D97 (in <xsl:value-of select='$BASE_UOM' />) = <xsl:value-of select='$LE_QUANTITY' />(<xsl:value-of select='$LE_UOM' />) x <xsl:value-of select='$LE_NUMERATOR' /> = <xsl:value-of select='$temp99' /></xsl:comment> 
                        <xsl:comment>Original Quantity is <xsl:value-of select='$temp98'/></xsl:comment>
                        <xsl:comment>Calculated Quantity is <xsl:value-of select='$temp99'/></xsl:comment>
                        <numerator><xsl:value-of select='$temp99'/></numerator>
                        <xsl:if test="$temp98!=$temp99">
                            <override>X</override>
                        </xsl:if>
                    </xsl:if>
                    
                    <xsl:if test="$LE_UOM = $CURRENT_UOM">
                        <xsl:comment>LE Quantity UOM <xsl:value-of select='$LE_UOM' /> is same as <xsl:value-of select='$BASE_UOM' /></xsl:comment>
                        <numerator><xsl:value-of select='$LE_QUANTITY' /></numerator> 
                    </xsl:if>

                    <denominator><xsl:value-of select='$D97_DENOMINATOR' /></denominator>
                    <ean><xsl:value-of select='c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim($D97_EAN))' /></ean>
                    <variant><xsl:value-of select='$D97_VARIANT' /></variant>
                </xsl:if>     
            </xsl:if>
            
            <xsl:if test="$CURRENT_UOM!='D97'">
                <uom><xsl:value-of select='FIELD[@name="UOM"]/@value' /></uom>
                <numerator><xsl:value-of select='FIELD[@name="Numerator"]/@value' /></numerator>
                <denominator><xsl:value-of select='FIELD[@name="Denominator"]/@value' /></denominator>
                <ean><xsl:value-of select='c4j_XSLT_Ext:padEAN(c4j_XSLT_Ext:trim(FIELD[@name="EANItemCode"]/@value))' /></ean>
                <variant><xsl:value-of select='FIELD[@name="EANVariant"]/@value' /></variant>        
            </xsl:if>           
            
        </materialUOMDefinition>
    </xsl:template>
    
    <!-- ================
       LOCATION DETAILS
       ================ -->
    
    <xsl:template match='DATA [@type="Location"]'>

        
        <xsl:variable name="LOCATION_PLANT" select='string(FIELD[@name="Plant"]/@value)' />
        <xsl:variable name="LOCATION_SLOC" select='string(FIELD[@name="StorageLocation"]/@value)' />
        <xsl:variable name="LOCATION_BIN" select='string(FIELD[@name="StorageBin"]/@value)' />
        <xsl:variable name="LOCATION_STATUS" select="/MESSAGE/DATA[@type='Plant']/FIELD[@name='Plant'][@value=$LOCATION_PLANT]/parent::*/FIELD[@name='MaterialStatus']/@value" />
        <xsl:variable name="ALIAS" select="c4j:getReferenceItem('PlantSLOCtoLocation',concat(string($LOCATION_PLANT),'-',string($LOCATION_SLOC)))" />
    
        <xsl:if test="$ALIAS!=''">
        <location>
            <id><xsl:value-of select="c4j:getReferenceItem('PlantSLOCtoLocation',concat(string($LOCATION_PLANT),'-',string($LOCATION_SLOC)))"/></id>
            <status><xsl:value-of select="c4j:getReferenceItem('SAPMaterialStatus',$LOCATION_STATUS)"/></status>
        </location>
        </xsl:if>
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

