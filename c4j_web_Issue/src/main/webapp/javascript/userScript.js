/*jshint esversion: 6 */
/*jshint esversion: 8 */
	
/**
 * @param {HTMLElement} errMsg
 * @param {string} username
 * @param {string} password
 */
	async function logon2(errMsg,username,password)
	{
		
		let payload = {
			action: "logon",
			userID: username.toUpperCase(),
			userPassword: password,
			commandStatus: ""
		};
	
		console.log(JSON.stringify(payload));
	
		let response = await fetch(getContextPath()+"/Users", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				setResultMessage(errMsg,"",true);
			    sessionStorage.setItem("selectedUsername", data.userID.toUpperCase());
			    if (data.commandStatus == "Success")
			   	{
					setResultMessage(errMsg,"",true);
			   		 window.location.href=getContextPath()+'/html/menu.html';
			   	}
			   	else
			   	{
					setResultMessage(errMsg,data.commandStatus,false);
				}
			})
			.catch((error) => {
				sessionStorage.setItem("selectedUsername", "");
				setResultMessage(errMsg,error.message,false);
			});
	
		console.log(response);
	}

	function refreshUser()
	{
		console.log('refreshUser'); 
		location.reload();
	}
	

	
