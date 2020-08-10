function refreshInvoice(refreshLink) {
	let data = "invoiceNumber=" + refreshLink.parentElement.parentElement.querySelector("[name=invoiceNumber]").innerHTML;
	let xhr = new XMLHttpRequest();
	xhr.open("POST", "/invoice/continueProcessing", true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.onreadystatechange = function() { // Call a function when the state changes.
	    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
	    	document.querySelector('#example').innerHTML = xhr.responseText;
	    	let response = JSON.parse(xhr.responseText);
	    	alert(response);
	    	alert(response["hasProblem"]);
	    	location.reload();
	    }
	}
	xhr.send(data);
	
}
