<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:p="http://com.sage/X3/WWIMPFHITMCM"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	xmlns:c4j_XSLT_Ext="http://xml.apache.org/xalan/java/com.commander4j.Transformation.XSLTExtension"
	exclude-result-prefixes="xs c4j c4j_XSLT_Ext p" version="2.0">

	<xsl:output encoding="UTF-8" indent="yes" method="xml"/>
	<xsl:strip-space elements="*"/>

	<!-- CONFIG DATA -->
	<xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config', 'HostRef')"/></xsl:variable>
	<xsl:variable name="PLANT"><xsl:value-of select="c4j:getConfigItem('config', 'Plant')"/></xsl:variable>
	<xsl:variable name="WAREHOUSE"><xsl:value-of select="c4j:getConfigItem('config', 'Warehouse')"/></xsl:variable>

	<xsl:template match="p:WWIMPFHITMCM">
		<root>
		<xsl:apply-templates select='I' />
		</root>
	</xsl:template>

	<xsl:template match="I">

		<xsl:variable name="message_date_time" select="c4j_XSLT_Ext:getISODateTimeString()"
			xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>
		
		<xsl:variable name="filename_date_time" select="c4j_XSLT_Ext:getISODateTimeFilenameString()"
			xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/>

		<xsl:variable name="shelf_life_rule" select="shelf_life_rule"/>
		<xsl:variable name="shelf_life_uom" select="shelf_life_uom"/>
		<xsl:variable name="base_uom" select="string(base_uom)"/>
		<xsl:variable name="prod_uom" select="prod_uom"/>
		<xsl:variable name="tuc_outbar" select="tuc_outbar"/>
		<xsl:variable name="material" select="material"/>
		<xsl:variable name="description" select="description"/>

		<xsl:variable name="customerID" select="customerID"/>
		<xsl:variable name="base_per_prod" select="Pks_Per_Case"/>
		<xsl:variable name="prod_per_layer" select="Case_Per_Layer"/>
		<xsl:variable name="base_per_layer" select="number($base_per_prod) * number($prod_per_layer)"/>
		<xsl:variable name="layer_per_pallet" select="Layer_Per_Pallet"/>
		<xsl:variable name="base_per_pallet" select="number($base_per_layer) * number($layer_per_pallet)"/>
		<xsl:text>&#10;</xsl:text>
        <xsl:comment>*START* filename MAT_<xsl:value-of select="$material"/>_<xsl:value-of select="$filename_date_time"/>.xml</xsl:comment>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<message>
			<xsl:comment>Message Header</xsl:comment>
			<plant><xsl:value-of select="$PLANT"/></plant>
			<hostRef><xsl:value-of select="$HOSTREF"/></hostRef>
			<messageRef>SAGE Material <xsl:value-of select="$message_date_time"/></messageRef>
			<interfaceType>Material Definition</interfaceType>
			<messageInformation>Material=<xsl:value-of select="$material"/></messageInformation>
			<interfaceDirection>Input</interfaceDirection>
			<messageDate><xsl:value-of select="$message_date_time"/></messageDate>

			<xsl:comment>Message Data</xsl:comment>
			<messageData>
				<materialDefinition>
					<material><xsl:value-of select="$material"/></material>
					<materialType>FERT</materialType>
					<description><xsl:value-of select="c4j_XSLT_Ext:trim(replace($description,'~~BRI',''))"/></description>
					<enabled>Y</enabled>
					<base_uom><xsl:value-of select="c4j:getReferenceItem('SageUOM', $base_uom)"/></base_uom>
					<gross_weight><xsl:value-of select="gross_weight"/></gross_weight>
					<net_weight><xsl:value-of select="gross_weight"/></net_weight>
					<weight_uom><xsl:value-of select="weight_uom"/></weight_uom>
					<old_material/>
					<shelf_life><xsl:value-of select="shelf_life"/></shelf_life>
					<shelf_life_rule><xsl:value-of select="c4j:getReferenceItem('SageShelfLifeRounding', $shelf_life_rule)"/></shelf_life_rule>
					<shelf_life_uom><xsl:value-of select="c4j:getReferenceItem('SageShelfLifeUom', $shelf_life_uom)"/></shelf_life_uom>
					<equipment_Type><xsl:value-of select="Pallet_Type"/></equipment_Type>
					
					<xsl:if test="$base_uom!=''">
							<xsl:comment>Base Unit of Measure (EA)</xsl:comment>
							<materialUOMDefinition>
								<uom>
									<xsl:value-of select="c4j:getReferenceItem('SageUOM', $base_uom)"/>
								</uom>
								<numerator>1</numerator>
								<denominator>1</denominator>
								<ean/>
								<variant>00</variant>
							</materialUOMDefinition>
						
							<xsl:if test="$prod_uom!=''">
								<xsl:comment>Production Unit of Measure (CS)</xsl:comment>
								<materialUOMDefinition>
									<uom>
										<xsl:value-of select="c4j:getReferenceItem('SageUOM', $prod_uom)"/>
									</uom>
									<numerator>
										<xsl:value-of select="$base_per_prod"/>
									</numerator>
									<denominator>1</denominator>
									<ean>
										<xsl:value-of select="$tuc_outbar"/>
									</ean>
									<variant>00</variant>
								</materialUOMDefinition>
							</xsl:if>
					</xsl:if>
					
					
					<xsl:comment>Layer Unit of Measure (LAG)</xsl:comment>
					<materialUOMDefinition>
						<uom>LAG</uom>
						<numerator>
							<xsl:value-of select="$base_per_layer"/>
						</numerator>
						<denominator>1</denominator>
						<ean/>
						<variant/>
					</materialUOMDefinition>					
					
					<xsl:comment>Pallet Unit of Measure (PAL)</xsl:comment>
					<materialUOMDefinition>
						<uom>D97</uom>
						<numerator>
							<xsl:value-of select="$base_per_pallet"/>
						</numerator>
						<denominator>1</denominator>
						<ean/>
						<variant/>
					</materialUOMDefinition>

				</materialDefinition>
			</messageData>
		</message>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:comment>*END* filename MAT_<xsl:value-of select="$material"/>_<xsl:value-of select="$filename_date_time"/>.xml</xsl:comment>
		<xsl:text>&#10;</xsl:text>

	</xsl:template>

	<!-- ================
		FUNCTION get config data 
        ================ -->

	<xsl:function name="c4j:getConfigItem">
		<xsl:param name="type"/>
		<xsl:param name="string1"/>
		<xsl:variable name="item_info" select="document('configData.xml')/lookup"/>
		<xsl:value-of select="$item_info/item[@type = $type][@id = $string1]/value"/>
	</xsl:function>

	<!-- ================
        FUNCTION get reference data 
        ================ -->
	
	<xsl:function name="c4j:getReferenceItem">
		<xsl:param name="type"/>
		<xsl:param name="string1"/>
		<xsl:variable name="item_info" select="document('referenceData.xml')/lookup"/>
		<xsl:value-of select="$item_info/item[@type = $type][@id = $string1]/value"/>
	</xsl:function>
	
</xsl:stylesheet>
