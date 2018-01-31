<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:c4j="http://www.commander4j.com"
    exclude-result-prefixes="xs"  version="2.0">
    
    <xsl:output encoding="UTF-8" indent='yes' method="xml" />
    <xsl:strip-space  elements="*"/>
    
    <!-- CONFIG DATA -->
    <xsl:variable name="despatchNo" select="/message/messageData[1]/despatchPreAdvice[1]/despatchNo[1]"></xsl:variable>
    <xsl:variable name="journeyRef" select="/message/messageData[1]/despatchPreAdvice[1]/journeyRef[1]"></xsl:variable>  
    <xsl:variable name="numberOfPallets" select="/message/messageData[1]/despatchPreAdvice[1]/numberOfPallets[1]"></xsl:variable>  
    <!-- Local Variables -->
    
 
    <xsl:template match='message'>

        <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
            xmlns:o="urn:schemas-microsoft-com:office:office"
            xmlns:x="urn:schemas-microsoft-com:office:excel"
            xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
            xmlns:html="http://www.w3.org/TR/REC-html40">
            <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
                <Author>Microsoft Office User</Author>
                <LastAuthor>Microsoft Office User</LastAuthor>
                <Created>2018-01-31T14:02:36Z</Created>
                <Version>16.00</Version>
            </DocumentProperties>
            <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
                <AllowPNG/>
            </OfficeDocumentSettings>
            <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
                <WindowHeight>17440</WindowHeight>
                <WindowWidth>28040</WindowWidth>
                <WindowTopX>5180</WindowTopX>
                <WindowTopY>3060</WindowTopY>
                <ProtectStructure>False</ProtectStructure>
                <ProtectWindows>False</ProtectWindows>
            </ExcelWorkbook>
            <Styles>
                <Style ss:ID="Default" ss:Name="Normal">
                    <Alignment ss:Vertical="Bottom"/>
                    <Borders/>
                    <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="12" ss:Color="#000000"/>
                    <Interior/>
                    <NumberFormat/>
                    <Protection/>
                </Style>
                <Style ss:ID="s63">
                    <Font ss:FontName="Calibri" x:Family="Swiss" ss:Size="12" ss:Color="#000000"
                        ss:Bold="1"/>
                </Style>
            </Styles>
            <Worksheet ss:Name="Sheet1">
                <Table ss:ExpandedColumnCount="3" ss:ExpandedRowCount="99" x:FullColumns="1"
                    x:FullRows="1" ss:DefaultColumnWidth="65" ss:DefaultRowHeight="16">
                    <Column ss:AutoFitWidth="0" ss:Width="79"/>
                    <Column ss:AutoFitWidth="0" ss:Width="89"/>
                    <Column ss:AutoFitWidth="0" ss:Width="124"/>
                    <Row>
                        <Cell ss:StyleID="s63"><Data ss:Type="String">Despatch No</Data></Cell>
                        <Cell ss:StyleID="s63"><Data ss:Type="String">Journey Ref</Data></Cell>
                        <Cell ss:StyleID="s63"><Data ss:Type="String">HU</Data></Cell>
                    </Row>
                    
                    <xsl:apply-templates select="/message/messageData/despatchPreAdvice/contents/pallet"/>
                    
                </Table>
                <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
                    <PageSetup>
                        <Header x:Margin="0.3"/>
                        <Footer x:Margin="0.3"/>
                        <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
                    </PageSetup>
                    <Print>
                        <ValidPrinterInfo/>
                        <PaperSizeIndex>9</PaperSizeIndex>
                        <VerticalResolution>0</VerticalResolution>
                    </Print>
                    <Selected/>
                    <Panes>
                        <Pane>
                            <Number>3</Number>
                            <ActiveRow>5</ActiveRow>
                            <ActiveCol>4</ActiveCol>
                        </Pane>
                    </Panes>
                    <ProtectObjects>False</ProtectObjects>
                    <ProtectScenarios>False</ProtectScenarios>
                </WorksheetOptions>
            </Worksheet>
        </Workbook>
        
        
    </xsl:template>
 
    <xsl:template match='/message/messageData/despatchPreAdvice/contents/pallet'>
        <Row  xmlns="urn:schemas-microsoft-com:office:spreadsheet"
            xmlns:o="urn:schemas-microsoft-com:office:office"
            xmlns:x="urn:schemas-microsoft-com:office:excel"
            xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
            xmlns:html="http://www.w3.org/TR/REC-html40">
            <Cell><Data ss:Type="String"><xsl:value-of select='$despatchNo'/></Data></Cell>
            <Cell><Data ss:Type="String"><xsl:value-of select='$journeyRef'/></Data></Cell>
            <Cell><Data ss:Type="String" x:Ticked="1"><xsl:value-of select='SSCC'/></Data></Cell>

        </Row>

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
