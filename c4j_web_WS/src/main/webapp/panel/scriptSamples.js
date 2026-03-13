/*jshint esversion: 6 */
/*jshint esversion: 8 */

	function getContextPath() {
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}

	function editSampleRecord() {

		var selectedSample = sessionStorage.getItem('selectedSample');
		var selectedTray = sessionStorage.getItem('selectedTray');

		if (selectedTray)
		{
			if (selectedTray !== null)
			{
				console.debug('editSampleRecord  selected sample ='+selectedSample);
				console.debug('editSampleRecord  selected tray ='+selectedTray);
				window.location.href=getContextPath()+'/panel/samples.html';
			}
		}
	}

	function sampleMenu() {

		window.location.href=getContextPath()+'/panel/samples.html';

	}

	function trayEdit() {

		window.location.href=getContextPath()+'/panel/trayEdit.html';

	}

	function trayMenu() {

		window.location.href=getContextPath()+'/panel/trays.html';

	}

	/**
 * @param {string} key
 * @param {any} val
 */
	function sampleListSave(key,val)
	{
		console.log('sampleListSave key='+key);
		console.log('sampleListSave value='+val);
		sessionStorage.setItem(key, val);
	}

	/**
	* @param {string} key
	* @param {any} val
	*/
	function sampleListGet(key,val)
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);

		console.log('sampleListGet key=' + key);
		console.log('sampleListGet value=' + val);
		console.log('sampleListGet selected=' + selected);

		if (selected == null || selected == undefined || selected == "")
		{
			selected = val;
			sampleListSave('selectedSample',selected);
		}

		if (selected == val) {
			checkedOrNot = 'checked="checked"';
		} else {
			checkedOrNot = "";
		}
		console.log('sampleListGet checkedOrNot [' + checkedOrNot + ']');

		return checkedOrNot;
	}

	/**
 * @param {number} sampleID
 */
	async function newSample(sampleID) {

		console.log('newSample clicked');
		var selectedTray = sessionStorage.getItem('selectedTray');

		const dataObject = {
			trayID: Number(selectedTray),
			sampleID: Number(sampleID)
		};

		console.log(JSON.stringify(dataObject));

		let response = await fetch(getContextPath()+"/TraySamples", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value trayID=" + data.trayID + " sampleID="+data.sampleID);
				sampleListSave('selectedSample', data.sampleID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});

		console.log(response);

		refreshSample();

		window.location.href=getContextPath()+'/panel/samples.html';
	}

	/**
 * @param {any} sampleID
 * @param {string} returnPage
 */
	async function loadSample(sampleID,returnPage) {

		if (sampleID.startsWith("TRAY"))
		{
			const m = sampleID.match(/\d+/);
			const n = m ? Number(m[0]) : null;   // 124 (or null if no number found)

			if (n != null)
			{
				sampleListSave('selectedTray', n);
				refreshSample();
			}
		}
	    else
		{
			console.log('loadSample clicked');

			//URL Params
			const panelID = Number(sessionStorage.getItem("selectedPanel"));
			const traySequence = Number(sessionStorage.getItem("selectedTraySeq"));

			//JSON Params
			const sample = Number(sampleID);
			const dataObject = { sampleID: sample };

			console.log(JSON.stringify(dataObject));

			let response = await fetch(getContextPath()+"/TraySamples?queryType=TraySequence&panelID="+panelID+"&traySequence="+traySequence, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
				.then((response) => {
					if (!response.ok) {
						throw new Error(`HTTP error, status = ${response.status}`);
					}
					return response.json();
				})
				.then((data) => {
					/*for (const panel of data) {*/
					console.log("Returned  value trayID=" + data.trayID + " sampleID="+data.sampleID);
					sampleListSave('selectedSample', data.sampleID);
					/*}*/
				})
				.catch((error) => {
					console.log("Error " + error);
				});

			console.log(response);

			refreshSample();

			window.location.href=getContextPath()+'/panel/'+returnPage;
		}
	}

	function refreshSample()
	{
		console.log('refreshSample');
		location.reload();
		document.getElementById("scanData").focus();
	}

	async function deleteSample() {

		console.log('deleteSample clicked');
		var selectedTray = sessionStorage.getItem('selectedTray');
		var selectedSample = sessionStorage.getItem('selectedSample');

		let response = await fetch(getContextPath()+"/TraySamples?trayID="+selectedTray+"&sampleID="+selectedSample, { method: 'DELETE' })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value trayID=" + data.trayID + " sampleID="+data.sampleID);
				sampleListSave('selectedSample', '');
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});

		console.log(response);

		refreshSample();

		window.location.href=getContextPath()+'/panel/samples.html';
	}
