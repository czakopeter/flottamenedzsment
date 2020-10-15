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

function browseFile() {
	let input = document.querySelector("#file");
	input.click();
}

function selectFile(input) {
	let fileName = input.value.substr(input.value.lastIndexOf("\\") + 1);
	document.querySelector("#fileName").innerHTML = fileName;
}

function search() {
	
}

function filterDateSet(input) {
	if(input.id === "begin-date") {
		let end = input.parentElement.querySelector("#end-date");
		if(!input.value) {
			end.removeAttribute("min");
		} else {
			end.setAttribute("min", input.value);
		}
	} else {
		let begin = input.parentElement.querySelector("#begin-date");
		if(!input.value) {
			begin.removeAttribute("max");
		} else {
			begin.setAttribute("max", input.value);
		}
	}
}

function resetFilter() {
	document.querySelector("#end-date").value = "";
	document.querySelector("#begin-date").value = "";
}