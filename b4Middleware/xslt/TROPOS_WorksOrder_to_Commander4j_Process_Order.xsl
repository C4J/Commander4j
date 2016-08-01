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
		  <xsl:param name="status" select='WORKS_ORDER_STATUS' />
		  <xsl:param name="due_date" select='DUE_DATE' />		
		  <xsl:param name="uom" select='UOM' />		   
		  <xsl:param name="site" select='SITE_ID' />		  

		  <message>
		  	  <plant><xsl:value-of select='$PLANT' /></plant>
		  	  <hostRef><xsl:value-of select='$HOSTREF' /></hostRef>
			  <messageRef>TROPOS <xsl:value-of select='EXTRACT_DATE_TIME' /></messageRef>
			  <interfaceType>Process Order</interfaceType>
			  <messageInformation>Process Order=<xsl:value-of select='ORDER_NUMBER' /></messageInformation>
			  <interfaceDirection>Input</interfaceDirection>
			  <messageDate><xsl:value-of select='EXTRACT_DATE_TIME' /></messageDate>

			  <messageData>

				  <processOrder>
				  	<orderNo><xsl:value-of select='ORDER_NUMBER'></xsl:value-of></orderNo>
				  	<material><xsl:value-of select='ITEM_NUMBER' /></material>
				  	<description><xsl:value-of select='DESCRIPTION' /></description>
				  	<status><xsl:value-of select="c4j:getConfigItem('TroposStatus',$status)"/></status>
				  	<location><xsl:value-of select='REC_STOR' />-<xsl:value-of select='REC_LOC' /></location>
				  	<requiredResource><xsl:value-of select='MACHINE_NUMBER' /></requiredResource>
					  <receipeId><xsl:value-of select='PROCESS_VERSION' /></receipeId>
				  	<dueDate><xsl:value-of select="c4j_XSLT_Ext:date_DD_MMM_YY_to_ISO_Date($due_date)" xmlns:c4j_XSLT_Ext="com.commander4j.Transformation.XSLTExtension"/></dueDate>				  
				      <requiredQuantity><xsl:value-of select='REQD_QTY' /></requiredQuantity>
				      <requiredUom><xsl:value-of select="c4j:getConfigItem('TroposUOM',$uom)"/></requiredUom>			  	
				  	  <defaultPalletStatus>Unrestricted</defaultPalletStatus>
				  	  <customerID><xsl:value-of select='CUST_CODE'/></customerID>
				  	  <customerName><xsl:value-of select='CUST_NAME'/></customerName>
				  </processOrder>
				  
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
