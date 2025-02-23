
	function getC4JBarcodeProperties()
	{
		var props ={
			autoenter: true,
			allDecoders: false,
			code128:   true ,  
			code128ean128:  true 
	     	}	
		
		return props;
	}
	
	function getC4JWLANProperties()
	{
		var props ={
	        color:  "#cd0006",
	        layout: EB.SignalIndicators.SIGNAL_LAYOUT_RIGHT,
	        top:    EB.System.realScreenHeight - 25 ,  
	        left:   25  
	    }
		
		return props;
	}
	
	function getC4JBatteryProperties()
	{
		var props ={
	        color:  "#38cd00",
	        layout: EB.Battery.BATTERY_LAYOUT_RIGHT,
	        top:    EB.System.realScreenHeight - 25 ,  
	        left:   EB.System.realScreenWidth - 50  
	     }	
	     
		return props;
	}
	
	function initC4J()
	{
		initC4JBarcode();
		initC4JBattery();
		initC4JWLAN();
	}
	
	function initC4JBarcode()
	{
		EB.Barcode.resetToDefault();
	}
			
	function initC4JBattery()
	{
		EB.Battery.showIcon(getC4JBatteryProperties());
	}
			
	function initC4JWLAN()
	{
		EB.SignalIndicators.showIcon(getC4JWLANProperties());
	}
	

