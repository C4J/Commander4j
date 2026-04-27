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
	
		await fetch(getContextPath()+"/Users", { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then(async (response) => {
				const data = await response.json().catch(() => ({}));
				if (response.ok && data.commandStatus === "Success")
				{
					setResultMessage(errMsg,"",true);
					sessionStorage.setItem("selectedUsername", (data.userID || "").toUpperCase());
					window.location.href=getContextPath()+'/html/menu.html';
				}
				else if (response.status === 401)
				{
					sessionStorage.setItem("selectedUsername", "");
					setResultMessage(errMsg,data.commandStatus || "Invalid credentials",false);
				}
				else if (!response.ok)
				{
					sessionStorage.setItem("selectedUsername", "");
					setResultMessage(errMsg,`HTTP error, status = ${response.status}`,false);
				}
				else
				{
					setResultMessage(errMsg,data.commandStatus || "Logon failed",false);
				}
			})
			.catch((error) => {
				sessionStorage.setItem("selectedUsername", "");
				setResultMessage(errMsg,error.message,false);
			});
	}

	function refreshUser()
	{
		console.log('refreshUser'); 
		location.reload();
	}
	

	
