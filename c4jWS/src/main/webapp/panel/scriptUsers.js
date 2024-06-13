/*jshint esversion: 6 */
/*jshint esversion: 8 */

	function getContextPath() {
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}
	
	function editUserRecord() {
		
		var selectedUser = sessionStorage.getItem('selectedUser');
		
		if (selectedUser !== null)
		{
			console.debug('editUserRecord  selected user ='+selectedUser);
			window.location='userEdit.html';
		}
	}
	
	function userMenu() {
		
		window.location='users.html';

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
			
	function getChecked(input)
	{
		var result = "";
		
		if (input == "Y")
		{
			result = 'checked="checked"';
		}
		
		return result;
	}
			
	function updateUser(firstname,surname,enabled)
	{
	    var selectedUser = sessionStorage.getItem('selectedUser');
	    
	    var enableChar = "";
	    
	    if (enabled==true)
	   	{
			enableChar="Y";
		}
		else
		{
			enableChar="N";
		}
		
		var payload = {
			userID: selectedUser,
			firstname: firstname,
			surname: surname,
			enabled: enableChar
		};
		
		fetch(getContextPath()+"/Users",
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
	
	}
	
	function userListSave(key,val) 
	{
		if (val === null) 
		{
			val="";
		}
		console.log('userListSave key='+key);
		console.log('userListSave value='+val);
		sessionStorage.setItem(key, val);
	}
	
	function userListGet(key,val) 
	{
		var checkedOrNot = "";
		var selected = sessionStorage.getItem(key);
	
		console.log('userListGet key=' + key);
		console.log('userListGet value=' + val);
		console.log('userListGet selected=' + selected);
	
		if (selected == val) {
			checkedOrNot = 'checked="checked"';
		} else {
			checkedOrNot = "";
		}
		console.log('userListGet checkedOrNot [' + checkedOrNot + ']');
	
		return checkedOrNot;
	}	
	
	async function saveUser(userID,firstname,surname,enabled) {
	
	
		console.log('newUser clicked');
	
		const dataObject = { 
			userID: userID,
			firstname: firstname, 
			surname: surname,
			enabled: enabled
		};
	
		console.log(JSON.stringify(dataObject));
	
		let response = await fetch(getContextPath()+"/Users", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
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
	
		refreshUser();
		
		window.location='userEdit.html';
	}
	
	
	function refreshUser()
	{
		console.log('refreshUser'); 
		location.reload();
	}
	
	async function newUser() {
	
	
		console.log('newUser clicked');
		var selectedUser = sessionStorage.getItem('selectedUser');
	
		const dataObject = { 
			userID: selectedUser,
			firstname: '', 
			surname: '',
			enabled: 'Y'
		};
	
		console.log(JSON.stringify(dataObject));
	
		let response = await fetch(getContextPath()+"/Users", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dataObject) })
			.then((response) => {
				if (!response.ok) {
					throw new Error(`HTTP error, status = ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				/*for (const panel of data) {*/
				console.log("Returned  value userID=" + data.userID);
				trayListSave('selectedUser', data.userID);
				/*}*/
			})
			.catch((error) => {
				console.log("Error " + error);
			});
	
		console.log(response);
	
		refreshUser();
		
		window.location='userEdit.html';
	}
	
