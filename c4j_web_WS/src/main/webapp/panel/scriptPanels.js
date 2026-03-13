/*jshint esversion: 6 */
/*jshint esversion: 8 */

	function getContextPath() {
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}

	function editPanelRecord() {

		var selected = sessionStorage.getItem('selectedPanel');

		if (selected)
		{
			if (selected !== null)
			{
				console.debug('editPanelRecord  selected panel ='+selected);
				console.debug(window.location.href);
				window.location.href=getContextPath()+'/panel/panelEdit.html';
			}
		}
	}

	/**
 * @param {string} status
 */
	function getStatusColour(status)
	{
		var colorname="";

		if (status == "Ready")
		{
			colorname="DarkGreen";
		}
		if (status == "Prepare")
		{
			colorname="Blue";
		}
		if (status == "Complete")
		{
			colorname="Tomato";
		}

		return colorname;
	}

	function panelMenu() {

		var filter = sessionStorage.getItem('panelStatus');

		if ((filter === undefined) || (filter === null) || (filter === "") || (filter === "Complete"))
	    {
	      sessionStorage.setItem('panelStatus', 'Prepare');
	    }

		window.location.href=getContextPath()+'/panel/panels.html';

	}

	function loadTrays() {

		var selected = sessionStorage.getItem('selectedPanel');

		if (selected)
		{
			if (selected !== null)
			{
				window.location.href=getContextPath()+'/panel/trays.html';
			}
		}
	}

	function fastLoad() {

		var selected = sessionStorage.getItem('selectedPanel');

		if (selected)
		{
			if (selected !== null)
			{
				window.location.href=getContextPath()+'/panel/samples.html';
			}
		}
	}

	/**
	* @param {number} panelID
	* @param {string} plant
	* @param {string} description
	* @param {string} status
	*/
	async function updatePanel(panelID,plant,description,status) {


		console.log('update Panel clicked');

		const dataObject = {
			panelID : panelID,
			plant: plant,
			description: description,
			status: status
		};

		console.log(JSON.stringify(dataObject));

		let response = await fetch(getContextPath()+"/Panels", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value panelID=" + data.userID);
				userListSave('selectedUser', data.userID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});

		console.log(response);

		panelListSave('panelStatus',status);

		panelListSave("selectedPanelPlant",plant);
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

	/**
 * @param {string} key
 * @param {string} val
 * @param {string} plant
 */
	function panelListGet(key,val,plant)
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);

		console.log('panelListGet key=' + key);
		console.log('panelListGet value=' + val);
		console.log('panelListGet selected=' + selected);

		if (selected == null || selected == undefined || selected == "")
		{
			selected = val;
			panelListSave('selectedPanel',selected);
			panelListSave('selectedPanelPlant',plant);
		}

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

		let response = await fetch(getContextPath()+"/Panels", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
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

		window.location.href=getContextPath()+'/panel/panelEdit.html';

	}

	function refreshPanel()
	{
		console.log('refreshPanel');
		location.reload();
	}

	/**
 * @param {string} isoDate
 */
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

				console.log(getContextPath()+"/Panels?panelID="+selectedPanel);

				try {
				  const response = await fetch(getContextPath()+"/Panels?panelID="+selectedPanel, {
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
				  console.log('Error: ' + error);
				}

				refreshPanel();

				window.location.href=getContextPath()+'/panel/panels.html';
			}

		}
	}
