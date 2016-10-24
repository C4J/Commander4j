<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:c4j="http://www.commander4j.com"
	exclude-result-prefixes="xs"  version="2.0">
	
	<xsl:output encoding="UTF-8" indent='yes' method="xml" />
	<xsl:strip-space  elements="*"/>
	
	<!-- CONFIG DATA -->
	<xsl:variable name="HOSTREF"><xsl:value-of select="c4j:getConfigItem('config','HostRef')"/></xsl:variable>
	<xsl:variable name="SAPUsername"><xsl:value-of select="c4j:getConfigItem('config','SAPUsername')"/></xsl:variable>
	
	<!-- Local Variables -->
	
	<xsl:variable name="DATENOW" select="current-dateTime()"/>
	<xsl:variable name="MESSAGEDATE" select="format-dateTime($DATENOW, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"></xsl:variable>
	
	<xsl:template match='message'>
		
		<ProductionPerformance>
			<ID>
				<xsl:value-of select='/message/messageRef'/>
			</ID>
			
			<Location>
				<EquipmentID><xsl:value-of select='/message/messageData/productionDeclaration/plant'/></EquipmentID>
				<EquipmentElementLevel>Site</EquipmentElementLevel>
			</Location>
			
			<PublishedDate>
				<xsl:value-of select='$MESSAGEDATE' />
			</PublishedDate>
			
			<ProductionResponse>
				<ProductionRequestID>
					<xsl:value-of select='/message/messageData/productionDeclaration/processOrder'/>
				</ProductionRequestID>
				<SegmentResponse>
					<ID>
						<xsl:value-of select='/message/messageData/productionDeclaration/recipe'/>
					</ID>
					<MaterialProducedActual>
						<MaterialDefinitionID>
							<xsl:variable name="MATERIAL_SHORT1" select="/message/messageData/productionDeclaration/material"/>							
							<xsl:variable name="MATERIAL_SHORT2" select="concat('00000000000000000000',$MATERIAL_SHORT1)"/>
							<xsl:variable name="STRLEN" select="string-length($MATERIAL_SHORT2)"/>	
							<xsl:variable name="MATERIAL_LONG" select="substring($MATERIAL_SHORT2,$STRLEN - 18 + 1,18)"/>
							<xsl:value-of select='$MATERIAL_LONG'/>
						</MaterialDefinitionID>
						<MaterialLotID>
							<xsl:value-of select='/message/messageData/productionDeclaration/batch'/>
						</MaterialLotID>
						<MaterialSubLotID>00<xsl:value-of select='/message/messageData/productionDeclaration/SSCC'/></MaterialSubLotID>
						<Location>
							<EquipmentID>
								<xsl:value-of select='/message/messageData/productionDeclaration/plant'/>
							</EquipmentID>
							<EquipmentElementLevel>Site</EquipmentElementLevel>
							<Location>
								<EquipmentID>
									<xsl:value-of select='/message/messageData/productionDeclaration/warehouse'/>
								</EquipmentID>
								<EquipmentElementLevel>Area</EquipmentElementLevel>
								<Location>
									<EquipmentID>
										<xsl:value-of select='/message/messageData/productionDeclaration/storageType'/>
									</EquipmentID>
									<EquipmentElementLevel>storageZone</EquipmentElementLevel>
									<Location>
										<EquipmentID>
											<xsl:value-of select='/message/messageData/productionDeclaration/storageSection'/>
										</EquipmentID>
										<EquipmentElementLevel>StorageModule</EquipmentElementLevel>
										<Location>
											<EquipmentID>
												<xsl:value-of select='/message/messageData/productionDeclaration/storageBin'/>
											</EquipmentID>
											<EquipmentElementLevel>StorageModule</EquipmentElementLevel>
										</Location>
									</Location>
								</Location>
							</Location>
							
							<Globe_StorageLocation>
								<xsl:value-of select='/message/messageData/productionDeclaration/storageLocation'/>
							</Globe_StorageLocation>
						</Location>
						<Quantity>
							<QuantityString>
								<xsl:value-of select='/message/messageData/productionDeclaration/productionQuantity'/>
							</QuantityString>
							<DataType>decimal</DataType>
							<UnitOfMeasure>
								<xsl:value-of select='/message/messageData/productionDeclaration/productionUOM'/>
							</UnitOfMeasure>
						</Quantity>
						<globe_Item>0001</globe_Item>
						<xsl:variable name="STOCKTYPE" select="/message/messageData/productionDeclaration/status" />
						<globe_StockType>
                            <xsl:value-of select="c4j:getConfigItem('C4JStockType',$STOCKTYPE)"/>
						</globe_StockType>
						<globe_PostingDate>
							<xsl:value-of select='$MESSAGEDATE' />
						</globe_PostingDate>
						<globe_ExpirationDate>
							<xsl:value-of select='/message/messageData/productionDeclaration/expiryDate'/>
						</globe_ExpirationDate>
						<globe_ProductionDate>
							<xsl:value-of select='/message/messageData/productionDeclaration/productionDate'/>
						</globe_ProductionDate>
						<globe_GTIN>
							<xsl:value-of select='/message/messageData/productionDeclaration/ean'/>
						</globe_GTIN>
						<globe_GTINVariant>
							<xsl:value-of select='/message/messageData/productionDeclaration/variant'/>
						</globe_GTINVariant>
						<globe_ItemTexte></globe_ItemTexte>
					</MaterialProducedActual>
				</SegmentResponse>
			</ProductionResponse>
			<globe_UserName>
				<xsl:value-of select='$SAPUsername' />
			</globe_UserName>
		</ProductionPerformance>
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
