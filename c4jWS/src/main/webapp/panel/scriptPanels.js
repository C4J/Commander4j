	
	function editPanelRecord() {
		
		var selected = sessionStorage.getItem('selectedPanel');
		
		if (selected)
		{
			if (selected !== null)
			{
				console.debug('editPanelRecord  selected panel ='+selected);
				window.location='panelEdit.html';
			}
		}
	}
	
	function getStatusColour(status)
	{
		var colorname="";
		
		if (status == "Ready")
		{
			colorname="MediumSeaGreen";
		}
		if (status == "Prepare")
		{
			colorname="tomato";
		}
		if (status == "Complete")
		{
			colorname="DodgerBlue";
		}
		
		return colorname;
	}
	
	function panelMenu() {
		
		var filter = sessionStorage.getItem('panelStatus');

		if ((filter === undefined) || (filter === null) || (filter === "") || (filter === "Complete"))
	    {
	      sessionStorage.setItem('panelStatus', 'Prepare');
	    }
		
		window.location='panels.html';

	}
	
	function loadTrays() {
		
		var selected = sessionStorage.getItem('selectedPanel');
		
		if (selected)
		{
			if (selected !== null)
			{
				window.location='trays.html';
			}
		}
	}
			
	function updatePanel(plant,description,status)
	{
		var selected = sessionStorage.getItem('selectedPanel');
		
		 panelListSave('panelStatus',status);
		
		var payload = {
			panelID: selected,
			plant: plant,
			description: description,
			status: status
		};
		
		fetch("/c4jWS/Panels",
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
		
		panelListSave("selectedPanelPlant",plant);
		
		refreshPanel();
	
	}
	
	function panelListSave(key,val) 
	{
		if (val === null) 
		{
			val="";
		}
		console.log('panelListSave key='+key);
		console.log('panelListSave value='+val);
		sessionStorage.setItem(key, val);
	}
	
	function panelListGet(key,val) 
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);
	
		console.log('panelListGet key=' + key);
		console.log('panelListGet value=' + val);
		console.log('panelListGet selected=' + selected);
	
		if (selected == val) {
			checkedOrNot = 'checked="checked"';
		} else {
			checkedOrNot = "";
		}
		console.log('panelListGet checkedOrNot [' + checkedOrNot + ']');
	
		return checkedOrNot;
	}	
	
	async function newPanel() {
	
	
		console.log('newPanel2 clicked');
	
		const dataObject = { status: 'Prepare' };
	
		console.log(JSON.stringify(dataObject));
	
		let response = await fetch("/c4jWS/Panels", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value " + data.panelID);
				panelListSave('selectedPanel', data.panelID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});
	
		console.log(response);
	
		refreshPanel();
		
		window.location='panelEdit.html';
		
	}
	
	function refreshPanel()
	{
		console.log('refreshPanel'); 
		location.reload();
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
	
	async function deletePanel() {
	
		console.log('deletePanel clicked');
		
		var selectedPanel = sessionStorage.getItem('selectedPanel');
		
		if (selectedPanel)
		
		{
			if (selectedPanel !== '')
			{
		
				console.log("/c4jWS/Panels?panelID="+selectedPanel);	
				
				try {
				  const response = await fetch("/c4jWS/Panels?panelID="+selectedPanel, {
					method: "delete"
				  });
				
				  if (!response.ok) {
					const message = 'Error with Status Code: ' + response.status;
					throw new Error(message);
				  }
				  else
				  {
					  panelListSave('selectedPanel','');
				  }
				
				  const data = await response.json();
				  console.log(data);
				} catch (error) {
				  console.log('Error: ' + err);
				}
						
				refreshPanel();
				
				window.location='panels.html';
			}
		
		}
	}
