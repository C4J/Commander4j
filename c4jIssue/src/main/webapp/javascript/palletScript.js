/*jshint esversion: 6 */
/*jshint esversion: 8 */

/**
 * @param {HTMLElement} errMsg
 * @param {HTMLInputElement} inputFocus
 * @param {string} sscc
 * @param {string} issue_order
 * @param {string} stage
 * @param {string} userId
 */
	async function validateSSCCMaterial(errMsg,inputFocus,sscc,issue_order,stage,userId)
	{
		let payload = 
		{
			action: "validateMaterial",
			sscc: sscc,
			processOrder: issue_order,
			userId : userId,
			commandStatus : "",
			errorMessage : ""
		};
	
	   await fetch(getContextPath()+"/Pallets?stage="+stage, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				if (data.commandStatus != "valid")
				{
					setResultMessage(errMsg, data.errorMessage,false);
					inputFocus.value = "";
					inputFocus.focus();
				}
				else
				{
					setResultMessage(errMsg, "",true);
					querySSCC(errMsg,sscc,'/html/palletIssueConfirm.html');
				}
			})
			.catch((error) => {
				console.log(error);
			});
	}
	
/**
 * @param {HTMLElement} errMsg
 * @param {HTMLInputElement} inputFocus
 * @param {string} sscc
 * @param {string} issue_order
 * @param {number} issue_quantity
 * @param {string} stage
 * @param {string} userId
 * @param {string} location_id
 */
	async function validateSSCCLocation(errMsg,inputFocus,sscc,issue_order,issue_quantity,stage,userId,location_id)
	{
		let payload = 
		{
			action: "validateLocation",
			sscc: sscc,
			processOrder: issue_order,
			userId : userId,
			locationId : location_id,
			commandStatus : "",
			errorMessage : ""
		};
	
	   await fetch(getContextPath()+"/Pallets?stage="+stage, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				if (data.commandStatus != "valid")
				{
					setResultMessage(errMsg,data.errorMessage,false);
					inputFocus.value = "";
					inputFocus.focus();
				}
				else
				{
					setResultMessage(errMsg,data.quantity + " " + data.uom + " from " + data.sscc + " issued.",true);
					issueSSCC(errMsg,sscc,issue_order,issue_quantity,userId,location_id)
				}
			})
			.catch((error) => {
				console.log(error);
			});
	}

/**
 * @param {HTMLElement} errMsg
 * @param {string} sscc
 * @param {string} nextPage
 */
	async function querySSCC(errMsg,sscc,nextPage)
	{
		console.log(sscc);
		
		sessionStorage.setItem("sscc", sscc);
	
		
		let payload = {
			action: "query",
			sscc: sscc,
			processOrder: "",
			quantity: 0.000	
		};
	
		console.log(JSON.stringify(payload));
	
		let response = await fetch(getContextPath()+"/Pallets", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				setResultMessage(errMsg,"",true);
			    sessionStorage.setItem("selectedSSCC", data.sscc);
			    window.location.href=getContextPath()+nextPage;
			})
			.catch((error) => {
				sessionStorage.setItem("selectedSSCC", "");
				setResultMessage(errMsg,"Invalid SSCC "+sscc,false);
				console.log(error);
			});
	
		console.log(response);
	
	}


/**
 * @param {HTMLElement} errMsg
 * @param {string} sscc
 * @param {string} order
 * @param {number} quantity
 * @param {string} userId
 * @param {string} barcode_id
 */
	async function issueSSCC(errMsg,sscc,order,quantity,userId,barcode_id)
	{
		console.log(sscc);
		
		sessionStorage.setItem("sscc", sscc);
	
		let payload = {
			action: "issue",
			sscc: sscc,
			processOrder: order,
			quantity : quantity,
			material: "material",
			palletStatus: "palletStatus",
			batchNumber: "batchNumber",
			batchStatus :"batchStatus",
			uom: "uom",
			confirmed : "confirmed",
			bomId: "bomId",
			bomVersion : "bomVersion",
			oldMaterial: "oldMaterial",
			userId : userId,
			locationId : barcode_id,
			description : "description"
		};
	
		console.log(JSON.stringify(payload));
	
		let response = await fetch(getContextPath()+"/Pallets", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				setResultMessage(errMsg,data.quantity + " " + data.uom + " from " + data.sscc + " issued.",true);
			    sessionStorage.setItem("selectedSSCC", data.sscc);
			    window.location.href=getContextPath()+'/html/palletIssueConfirm.html';
			})
			.catch((error) => {
				sessionStorage.setItem("selectedSSCC", "");
				setResultMessage(errMsg, "Invalid SSCC "+sscc,false);
				console.log(error);
			});
	
		console.log(response);
	
	}
	
	function refreshPallet()
	{
		console.log('refreshPallet'); 
		location.reload();
	}
	

	
