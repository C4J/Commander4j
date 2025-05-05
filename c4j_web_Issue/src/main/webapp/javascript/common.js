/*jshint esversion: 6 */
/*jshint esversion: 8 */

/**
 * @param {string} sscc
 */
function formatSSCC(sscc)
{
	let result = sscc;
	
	if (sscc != null)
	{
		if (sscc != "")
		{
			if (sscc.length == 20)
			{
				result = sscc.substring(2, 20);
			}
		}
	}
	
	return result;
}

/**
 * @param {HTMLElement} errMsg
 * @param {string} message
 * @param {boolean} successFail
 */
	function setResultMessage(errMsg,message,successFail)
	{
		sessionStorage.setItem("errorMessage",message);
		sessionStorage.setItem("errorMessageSuccessFail",successFail.toString());
		
		if (successFail == true)
		{
			errMsg.style.color = 'lime';
			errMsg.style.backgroundColor = 'black';
		}
		else
		{
			errMsg.style.color = 'yellow';
			errMsg.style.backgroundColor = 'black';
		}
		
		errMsg.innerHTML = message;
	}
	
	/**
 * @param {HTMLElement} errMsg
 */
	function setLastResultMessage(errMsg)
	{
		let message = sessionStorage.getItem("errorMessage");
		let temp = sessionStorage.getItem("errorMessageSuccessFail");
		let successFail = (temp === 'true');
		
		if (successFail == true)
		{
			errMsg.style.color = 'lime';
			errMsg.style.backgroundColor = 'black';
		}
		else
		{
			errMsg.style.color = 'black';
			errMsg.style.backgroundColor = 'red';
		}
		
		errMsg.innerHTML = message;
	}
	
/**
 * @param {string} isoDate
 */
	function formatDate(isoDate)
	{
		let result = "";
		
		result = result + isoDate.substring(8,2)+"/";
		
		result = result + isoDate.substring(5,2)+"/";
		
		result = result + isoDate.substring(0,4)+" ";
		
		result = result + isoDate.substring(11,5)+" ";
		
		return result;
	}

	function checkLogon()
	{
		let selectedUser = sessionStorage.getItem('selectedUsername');
		
		if (selectedUser !== null)
		{

			if (selectedUser == '')
			{
				window.location.href=getContextPath()+'/html/userLogon.html';	
			}
		}
		else
		{
			window.location.href=getContextPath()+'/html/userLogon.html';	
		}
	}
	
	function logon()
	{
		window.location.href=getContextPath()+'/html/userLogon.html';
	}
	
    function menu()
	{
	    resetValues();
		window.location.href=getContextPath()+'/html/menu.html';	
	}
	
	function resetValues()
	{
	    sessionStorage.setItem("selectedProcessOrder","");
	    sessionStorage.setItem("selectedSSCC","");
	    sessionStorage.setItem("selectedLane","");
	    sessionStorage.setItem("selectedResource","");
	    sessionStorage.setItem("selectedStage","");	
	}

	function logout()
	{
		resetValues();
		sessionStorage.setItem("selectedUsername", "");
		window.location.href=getContextPath()+'/html/userLogon.html';	
	}
	
	function getContextPath() 
	{
	   let result = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	   return result;
	}
	
