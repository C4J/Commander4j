/*jshint esversion: 6 */
/*jshint esversion: 8 */

 	async function getHostProperties()
	{
		
		let errMsg = document.getElementById("hostDescription");
	
		
		let payload = {
			hostID: "",
			description: ""
		};

		console.log(JSON.stringify(payload));

		let response = await fetch(getContextPath()+"/Host", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				errMsg.innerHTML = data.description;
				sessionStorage.setItem("hostID", data.hostID);
			    sessionStorage.setItem("hostDescription", data.description);
			})
			.catch((error) => {
				sessionStorage.setItem("hostID", "");
				sessionStorage.setItem("hostDescription", "");
				errMsg.innerHTML = "Error";
				console.log(error);
			});

		console.log(response);

	}