let selectedNumber = 0;

function selectAll() {
	let result = document.querySelector("#select-all").checked;
	let cbs = document.querySelectorAll("tbody input[type=checkbox]");
	for(let cb of cbs) {
		cb.checked = result;
	}
	if(result) {
		setToMultiAcceptMode();
	} else {
		setToSingleAcceptMode();
	}
	selectedNumber = result ? cbs.length : 0;
}

function selectSingleCheckbox(cb) {
	if(cb.checked) {
		selectedNumber++;
		if(selectedNumber == 1) {
			setToMultiAcceptMode();
		}
	} else {
		selectedNumber--;
		if(selectedNumber == 0) {
			setToSingleAcceptMode();
		}
	}
}

function setToMultiAcceptMode() {
	document.querySelector("#select-all").checked = true;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'hidden';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'visible';
}

function setToSingleAcceptMode() {
	document.querySelector("#select-all").checked = false;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'visible';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'hidden';
}

function acceptOneInvoice(btn) {
	let number = btn.parentElement.parentElement.parentElement.querySelector("[name=number]").textContent;
	let invoiceNumber = btn.parentElement.parentElement.parentElement.querySelector("[name=invoiceNumber]".textContent);
	console.log(number);
	console.log(invoiceNumber);
	sendData("POST", "/profile/finance/accept", "invoiceNumbers=" + invoiceNumber.value + "&numbers=" + number, afterAccept);
}

function acceptAllSelectedInvoice() {
	let data= "";
	for(let checkbox of document.querySelectorAll("[name=numberCheckBox]")) {
		console.log(checkbox);
		if(checkbox.checked) {
			if(data != "") {
				data += "&";
			}
			data += "invoiceNumbers=" + checkbox.parentElement.querySelector("[name=invoiceNumber]").value;
			data += "&numbers=" + checkbox.parentElement.parentElement.querySelector("[name=number]").textContent;
		}
	}
	console.log(data);
	sendData("POST", "/profile/finance/accept", data, afterAccept);
}

function afterAccept() {
	let table = document.querySelector("table");
	for(let checkbox in document.querySelectorAll("[name=numberCheckBox]")) {
		if(checkbox.checked) {
			table.deleteRow(checkbox.parentElement.parentElement.rowIndex);
		}
	}
}