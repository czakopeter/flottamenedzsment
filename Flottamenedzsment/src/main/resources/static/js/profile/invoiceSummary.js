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
	sendData("POST", "/profile/finance/accept", "ids=" + btn.parentElement.parentElement.parentElement.querySelector("[name=id]").value, afterAccept);
}

function acceptAllSelectedInvoice() {
	let data= "";
	for(let checkbox of document.querySelectorAll("[name=numberCheckBox]")) {
		if(checkbox.checked) {
			if(data != "") {
				data += "&";
			}
			data += "ids=" + checkbox.parentElement.querySelector("[name=id]").value;
		}
	}
	sendData("POST", "/profile/finance/accept", data, afterAccept);
}

function afterAccept(data) {
	console.log(data);
	let table = document.querySelector("table");
	for(let i = 0; i < data.length; i++) {
		table.deleteRow(table.querySelector("#p" + data[i]).parentElement.parentElement.rowIndex);
	}
}
