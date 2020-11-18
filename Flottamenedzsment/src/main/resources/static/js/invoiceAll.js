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

function sendRawRemove(btn) {
	let tr = btn.parentElement.parentElement.parentElement;
	let invoiceNumber = tr.querySelector("[name=invoiceNumber]").innerHTML;
	sendData("POST", "/rawInvoice/delete", "invoiceNumber=" + invoiceNumber, processRawRemove)
}

function processRawRemove(data) {
	let tr = document.querySelector("#invoiceNumber" + data.text);
	tr.parentElement.parentElement.deleteRow(tr.rowIndex);
}

function browseFile() {
	let input = document.querySelector("#file");
	input.click();
}

function selectFile(input) {
	let fileName = input.value.substr(input.value.lastIndexOf("\\") + 1);
	document.querySelector("#fileName").innerHTML = fileName;
}


function filterDateSet(input) {
	if(input.id === "begin-date") {
		let end = input.parentElement.parentElement.querySelector("#end-date");
		if(!input.value) {
			end.removeAttribute("min");
		} else {
			end.setAttribute("min", input.value);
		}
	} else {
		let begin = input.parentElement.parentElement.querySelector("#begin-date");
		if(!input.value) {
			begin.removeAttribute("max");
		} else {
			begin.setAttribute("max", input.value);
		}
	}
}

function resetFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#invoice-number").value = "";
	filterOptionsWrapper.querySelector("#begin-date").value = "";
	filterOptionsWrapper.querySelector("#begin-date").removeAttribute("max");
	filterOptionsWrapper.querySelector("#end-date").value = "";
	filterOptionsWrapper.querySelector("#end-date").removeAttribute("min");
	showAll();
}

function filter() {
	let trs = document.querySelector("#invoice-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		console.log(tr);
		if(filterCondicion(tr)) {
			tr.classList.remove("collapse");
		} else {
			tr.classList.add("collapse");
		}
	}
}

function filterCondicion(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let invoiceNumber = filterOptionsWrapper.querySelector("#invoice-number");
	let minDate = filterOptionsWrapper.querySelector("#begin-date");
	let maxDate = filterOptionsWrapper.querySelector("#end-date");
	
	return tr.querySelector("[name=invoiceNumber]").innerHTML.includes(invoiceNumber.value) && 
		(!minDate.value || Date.parse(tr.querySelector("[name=beginDate]").innerHTML) >= Date.parse(minDate.value)) &&
		(!maxDate.value || Date.parse(tr.querySelector("[name=endDate]").innerHTML) <= Date.parse(maxDate.value));
}

function showAll() {
	let trs = document.querySelector("#invoice-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
}