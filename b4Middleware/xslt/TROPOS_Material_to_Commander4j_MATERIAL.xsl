<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	
	
	<!-- CONFIG DATA -->
	<xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
	<xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config','Plant')"/></xsl:variable>
	<xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config','Warehouse')"/></xsl:variable>
	<xsl:variable name="LANGUAGE"><xsl:value-of select="c4j:getConfigItem('config','Language')"/></xsl:variable>

      <xsl:template match='xml'>
		  <xsl:param name="stock_conv" select='STOCK_UOM_PER_PALL' />
		  <xsl:param name="pack_conv_temp" select='UOM_STOCK_BASE_UOM' />
      	
      	<xsl:param name="pack_conv"  select="c4j_XSLT_Ext:nvl($pack_conv_temp,'1')" xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>
      	
		  <xsl:param name="base_uom" select='UOM_BASE' />
		  <xsl:param name="stock_uom" select='UOM_STOCK' />
		  <xsl:param name="pallet_uom" select='UOM_PACK' />
		  <xsl:param name="variant" select='VAR_CODE' />
		  <xsl:param name="pallet_quantity" select="number($stock_conv) * number($pack_conv)" />
		  <xsl:param name="layers_per_pallet_temp" select='LAYER_PER_PALLET' />
      	<xsl:param name="layers_per_pallet"  select="c4j_XSLT_Ext:nvl($layers_per_pallet_temp,'1')" xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>
		  <xsl:param name="layers_per_pallet_numerator" select="number($pallet_quantity) div number($layers_per_pallet)" />
		  <xsl:param name="customerID" select="CUST_CODE" />



		  <message>
			  <plant><xsl:value-of select='$PLANT' /></plant>
			  <hostRef><xsl:value-of select='$HOSTREF' /></hostRef>
			  <messageRef>TROPOS <xsl:value-of select='EXTRACT_DATE_TIME' /></messageRef>
			  <interfaceType>Material Definition</interfaceType>
			  <messageInformation>Material=<xsl:value-of select='ITEM_NUMBER' /></messageInformation>
			  <interfaceDirection>Input</interfaceDirection>
			  <messageDate><xsl:value-of select='EXTRACT_DATE_TIME' /></messageDate>

			  <messageData>
			  	<materialDefinition>
				  <material><xsl:value-of select='ITEM_NUMBER' /></material>
				  <materialType>FERT</materialType>
				  <description><xsl:value-of select='DESCRIPTION' /></description>
			  	  <base_uom><xsl:value-of select="c4j:getConfigItem('TroposUOM',$base_uom)"/></base_uom>
				  <gross_weight>0</gross_weight>
				  <net_weight>0</net_weight>
				  <weight_uom>G</weight_uom>
				  <old_material><xsl:value-of select='LOT_PREFIX' /></old_material>
				  <shelf_life><xsl:value-of select='SHELF_LIFE' /></shelf_life>
				  <shelf_life_rule>=</shelf_life_rule>
				  <shelf_life_uom>D</shelf_life_uom>
				  <equipment_Type></equipment_Type>
				  <materialUOMDefinition>
				     	<uom><xsl:value-of select="c4j:getConfigItem('TroposUOM',$base_uom)"/></uom>
						<numerator>1</numerator>
						<denominator>1</denominator>
						<ean></ean>
						<variant></variant>
				  </materialUOMDefinition>
				  <materialUOMDefinition>
				  	     <uom><xsl:value-of select="c4j:getConfigItem('TroposUOM',$stock_uom)"/></uom>
					    <numerator><xsl:value-of select='$pack_conv' /></numerator>
					    <denominator>1</denominator>
					    <ean><xsl:value-of select='EAN_CASE' /></ean>
				  	<variant><xsl:value-of select="c4j_XSLT_Ext:padVariant($variant)" xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/></variant>		  	    
				  </materialUOMDefinition>
				  <materialUOMDefinition>
				  	  <uom><xsl:value-of select="c4j:getConfigItem('TroposUOM',$pallet_uom)"/></uom>
					  <numerator><xsl:value-of select="number($stock_conv) * number($pack_conv)" /></numerator>
					  <denominator>1</denominator>
					  <ean></ean>
					  <variant></variant>
				  </materialUOMDefinition>
					<materialUOMDefinition>
						<uom>LAG</uom>
						<numerator>
							<xsl:value-of select="$layers_per_pallet_numerator" />
						</numerator>
						<denominator>1</denominator>
						<ean></ean>
						<variant></variant>
					</materialUOMDefinition>
					<materialCustomerData>
					   <customer><xsl:attribute name="ID"><xsl:value-of select='CUST_CODE' /></xsl:attribute>
						   <data><xsl:attribute name="dataType">PART_NO</xsl:attribute> <xsl:attribute name="value"><xsl:value-of select='CUST_PART_NO' /></xsl:attribute></data>
						   <data><xsl:attribute name="dataType">NAME</xsl:attribute> <xsl:attribute name="value"><xsl:value-of select='CUST_NAME' /></xsl:attribute></data>
					   	<data><xsl:attribute name="dataType">CU_EAN</xsl:attribute> <xsl:attribute name="value"><xsl:value-of select='EAN_UNIT' /></xsl:attribute></data>
					   	<data><xsl:attribute name="dataType">CU_QUANTITY</xsl:attribute><xsl:attribute name="value"><xsl:value-of select='CASE_UNIT_FAC' /></xsl:attribute></data>
					   	
					   </customer>
					</materialCustomerData>
				</materialDefinition>
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

</xsl:stylesheet>
