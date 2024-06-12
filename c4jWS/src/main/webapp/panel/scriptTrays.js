/*jshint esversion: 6 */
/*jshint esversion: 8 */
	
	function editTrayRecord() {
		
		var selectedPanel = sessionStorage.getItem('selectedPanel');
		var selectedTray = sessionStorage.getItem('selectedTray');
		
		if (selectedTray)
		{
			if (selectedTray !== null)
			{
				console.debug('editTrayRecord  selected panel ='+selectedPanel);
				console.debug('editTrayRecord  selected tray ='+selectedTray);
				window.location='trayEdit.html';
			}
		}
	}
	
	function trayMenu() {
		
		window.location='trays.html';

	}
	
	function panelEdit() {
		
		window.location='panelEdit.html';
	
	}
	
	function panelMenu() {
		
		window.location='panels.html';

	}
	
	function formatDate(isoDate)
	{
		var result = "";
		
		result = result + isoDate.substr(8,2)+"/";
		
		result = result + isoDate.substr(5,2)+"/";
		
		result = result + isoDate.substr(0,4)+" ";
		
		result = result + isoDate.substr(11,5)+" ";
		
		return result;
	}
			
	function updateTray(description)
	{
	   var selectedPanel = sessionStorage.getItem('selectedPanel');
	   var selectedTray = sessionStorage.getItem('selectedTray');
		
		var payload = {
			panelID: selectedPanel,
			trayID: selectedTray,
			description: description
		};
		
		fetch("/c4jWS/Trays",
		{
			headers: {
			  'Accept': 'application/json',
			  'Content-Type': 'application/json'
			},
			method: "PUT",
			body: JSON.stringify(payload)
		})
		.then(function(res){ console.log(res); })
		.catch(function(res){ console.log(res); });
		
		refreshTray();
	
	}
	
	function trayListSave(key,val) 
	{
		if (val === null) 
		{
			val="";
		}
		console.log('trayListSave key='+key);
		console.log('trayListSave value='+val);
		sessionStorage.setItem(key, val);
	}
	
	function trayListGet(key,val) 
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);
	
		console.log('trayListGet key=' + key);
		console.log('trayListGet value=' + val);
		console.log('trayListGet selected=' + selected);
	
		if (selected == val) {
			checkedOrNot = 'checked="checked"';
		} else {
			checkedOrNot = "";
		}
		console.log('trayListGet checkedOrNot [' + checkedOrNot + ']');
	
		return checkedOrNot;
	}	
	
	async function newTray() {
	
	
		console.log('newTray clicked');
		var selectedPanel = sessionStorage.getItem('selectedPanel');
	
		const dataObject = { 
			panelID: selectedPanel,
			description: ''
		};
	
		console.log(JSON.stringify(dataObject));
	
		let response = await fetch("/c4jWS/Trays", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value panelID=" + data.panelID + " trayID="+data.trayID);
				trayListSave('selectedTray', data.trayID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});
	
		console.log(response);
	
		refreshTray();
		
		window.location='trayEdit.html';
	}
	
	function sampleMenu()
	{
		var selectedTray = sessionStorage.getItem('selectedTray');
		
		if (selectedTray)
		{
			if (selectedTray !== null)
			{
				window.location='samples.html';
			}
		}
	}
	
	function refreshTray()
	{
		console.log('refreshTray'); 
		location.reload();
	}
	
	async function deleteTray() {
	
		console.log('deleteTray clicked');
		
		var selectedPanel = sessionStorage.getItem('selectedPanel');
		var selectedTray = sessionStorage.getItem('selectedTray');
		
		console.log("/c4jWS/Trays?panelID="+selectedPanel+"&trayID="+selectedTray);	
		
		try {
		  const response = await fetch("/c4jWS/Trays?trayID="+selectedTray+"&panelID="+selectedPanel, {
			method: "delete"
		  });
		
		  if (!response.ok) {
			const message = 'Error with Status Code: ' + response.status;
			throw new Error(message);
		  }
		  else
		  {
			  trayListSave('selectedTray','');
		  }
		
		  const data = await response.json();
		  console.log(data);
		} catch (error) {
		  console.log('Error: ' + err);
		}
				
		refreshTray();
		
		window.location='trays.html';
	}
