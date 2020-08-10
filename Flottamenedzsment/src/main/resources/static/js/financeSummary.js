let selectedNumber = 0;

function selectAll() {
	let result = document.querySelector("#select-all").checked;
	let selectors = document.querySelectorAll("tbody input[type=checkbox]");
	for(let s of selectors) {
		s.checked = result;
	}
	if(result) {
		setToMultiMode();
	} else {
		setToSingleMode();
	}
	selectedNumber = result ? selectors.length : 0;
}

function selectSingleCheckbox(cb) {
	if(cb.checked) {
		selectedNumber++;
		if(selectedNumber == 1) {
			setToMultiMode();
		}
	} else {
		selectedNumber--;
		if(selectedNumber == 0) {
			setToSingleMode();
		}
	}
}

function setToMultiMode() {
	document.querySelector("#select-all").checked = true;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'hidden';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'visible';
}

function setToSingleMode() {
	document.querySelector("#select-all").checked = false;
	for(let b of document.querySelectorAll(".btn-group")) {
		b.style.visibility = 'visible';
	}
	document.querySelector("#accept-all-selected").style.visibility = 'hidden';
}

function acceptOneInvoice(btn) {
	let number = btn.parentElement.parentElement.parentElement.querySelector("[name=number]");
	sendData("POST", "/profile/finance/accept", "numbers=" + number.value, afterAccept);
}

function acceptAllSelected() {
	let numbers = "";
	for(let checkbox in document.querySelectorAll("[name=numberCheckBox]")) {
		if(checkbox.checked) {
			if(numbers != "") {
				numbers += "," +
			}
			numbers += checkbox.parentElement.querySelector("[name=number]").value;
		}
	}
	sendData("POST", "/profile/finance/accept", "numbers=" + numbers, afterAccept);
}

function afterAccept() {
	let table = document.querySelector("table");
	for(let checkbox in document.querySelectorAll("[name=numberCheckBox]")) {
		if(checkbox.checked) {
			table.deleteRow(checkbox.parentElement.parentElement.rowIndex);
		}
	}
}