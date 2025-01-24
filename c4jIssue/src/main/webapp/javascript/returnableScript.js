/*jshint esversion: 6 */
/*jshint esversion: 8 */
	
 /**
 * @param {string} key
 * @param {string} val
 */
	function processOrderListSave(key,val) 
	{
		if (val === null) 
		{
			val="";
		}
		
		let encodedStr = encodeURIComponent(val);
		console.log('processOrderListSave key='+key);
		console.log('processOrderListSave value='+encodedStr);
		sessionStorage.setItem(key, val);
	}
	
 /**
 * @param {string} key
 * @param {string} val
 */
	function processOrderListGet(key,val) 
	{
		let checkedOrNot = "";
		let selected = decodeURIComponent(sessionStorage.getItem(key));
	
		console.log('processOrderListGet key=' + key);
		console.log('processOrderListGet value=' + val);
		console.log('processOrderListGet selected=' + selected);
	
		if (selected == val) {
			checkedOrNot = 'checked="checked"';
		} else {
			checkedOrNot = "";
		}
		console.log('processOrderListGet checkedOrNot [' + checkedOrNot + ']');
	
		return checkedOrNot;
	}	
	
	function refreshPanel()
	{
		location.reload();
	}
	
