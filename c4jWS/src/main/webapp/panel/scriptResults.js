/*jshint esversion: 6 */
/*jshint esversion: 8 */
	
	function getContextPath() {
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}

	async function newResult(trayID,sampleID,userID,testID,resultID) {
	
		const dataObject = { 
			trayID: trayID,
			sampleID: sampleID,
			userID: userID,
			testID: testID,
			value: resultID
		};
	
		console.log(JSON.stringify(dataObject));
	
		let response = await fetch(getContextPath()+"/TrayResults", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value trayID=" + data.trayID + " sampleID="+data.sampleID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});
	
		console.log(response);
	
	}
	

