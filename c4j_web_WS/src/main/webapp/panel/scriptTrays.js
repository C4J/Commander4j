/*jshint esversion: 6 */
/*jshint esversion: 8 */

	/**
	* @param {string} selectedPanel
	* @param {string} selectedTraySeq
	*/
	function getTrayIDFromSequence(selectedPanel,selectedTraySeq) {

		trayListSave('selectedPanel',selectedPanel);
		trayListSave('selectedTraySeq',selectedTraySeq);

		console.debug('buildTrayEdit  selected panel =' + selectedPanel);
		console.debug('buildTrayEdit  selected tray =' + selectedTraySeq);

		fetch(getContextPath()+"/Trays?panelID=" + Number(selectedPanel) + "&traySequence=" + Number(selectedTraySeq)+"&queryType=TraySequence")
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {


				trayListSave('selectedTray',data[0].trayID);

				return data;
			})
			.catch((error) => {
				console.log("Error " + error);
			});

	}

	function getContextPath() {
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}

	function editTrayRecord() {

		var selectedPanel = sessionStorage.getItem('selectedPanel');
		var selectedTray = sessionStorage.getItem('selectedTray');

		if (selectedTray)
		{
			if (selectedTray !== null)
			{
				console.debug('editTrayRecord  selected panel ='+selectedPanel);
				console.debug('editTrayRecord  selected tray ='+selectedTray);
				window.location.href=getContextPath()+'/panel/trayEdit.html';
			}
		}
	}

	function trayMenu() {

		window.location.href=getContextPath()+'/panel/trays.html';

	}

	function panelEdit() {

		window.location.href=getContextPath()+'/panel/panelEdit.html';

	}

	function panelMenu() {

		window.location.href=getContextPath()+'/panel/panels.html';

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


	/**
	* @param {number} panelID
	* @param {number} trayID
	* @param {string} description
	*/
	async function updateTray(panelID,trayID,description) {


		console.log('update Tray clicked');

		const dataObject = {
			panelID : Number(panelID),
			trayID: Number(trayID),
			description: String(description)
		};

		console.log(JSON.stringify(dataObject));

		let response = await fetch(getContextPath()+"/Trays", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
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

	}

	/**
 * @param {string} key
 * @param {any} val
 */
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

	/**
 * @param {string} key
 * @param {string} val
 * @param {any} seq
 */
	function trayListGet(key,val,seq)
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);

		console.log('trayListGet key=' + key);
		console.log('trayListGet value=' + val);
		console.log('trayListGet selected=' + selected);

		if (selected == null || selected == undefined || selected == "")
		{
			selected = val;
			trayListSave('selectedTray',selected);
			trayListSave('selectedTraySeq',seq);
		}

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
			panelID: Number(selectedPanel),
			description: ''
		};

		console.log(JSON.stringify(dataObject));

		let response = await fetch(getContextPath()+"/Trays", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value panelID=" + data.panelID + " trayID="+data.trayID+ " traySequence="+data.traySequence);
				trayListSave('selectedTray', data.trayID);
				trayListSave('selectedTraySeq', data.traySequence);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});

		console.log(response);

		refreshTray();

		window.location.href=getContextPath()+'/panel/trayEdit.html';
	}

	function sampleMenu()
	{
		var selectedTray = sessionStorage.getItem('selectedTray');

		if (selectedTray)
		{
			if (selectedTray !== null)
			{
				window.location.href=getContextPath()+'/panel/samples.html';
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

		console.log(getContextPath()+"/Trays?panelID="+selectedPanel+"&trayID="+selectedTray);

		try {
		  const response = await fetch(getContextPath()+"/Trays?trayID="+selectedTray+"&panelID="+selectedPanel+"&queryType=TrayID", {	method: "DELETE"  });

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
		  console.log('Error: ' + error);
		}

		refreshTray();

		window.location.href=getContextPath()+'/panel/trays.html';
	}
