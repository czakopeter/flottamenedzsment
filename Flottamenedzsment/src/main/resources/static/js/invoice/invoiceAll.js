function sendRawRemove(btn) {
	let tr = btn.parentElement.parentElement.parentElement;
	let invoiceNumber = tr.querySelector("[name=invoiceNumber]").innerHTML;
	sendData("POST", "/rawInvoice/delete", "invoiceNumber=" + invoiceNumber, processRawRemove)
}

function processRawRemove(data) {
	let tr = document.querySelector("#invoiceNumber" + data.text);
	tr.parentElement.parentElement.deleteRow(tr.rowIndex);
	if(document.querySelector("#raw-invoice-table").querySelector("[name=content-body]").querySelectorAll("tr").length == 0) {
		document.querySelector("#raw-invoice-table").querySelector("[name=no-element-body]").classList.remove('collapse');
	}
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

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#invoice-number").value = "";
	filterOptionsWrapper.querySelector("#begin-date").value = "";
	filterOptionsWrapper.querySelector("#begin-date").removeAttribute("max");
	filterOptionsWrapper.querySelector("#end-date").value = "";
	filterOptionsWrapper.querySelector("#end-date").removeAttribute("min");
	showAll();
}

function filter() {
	let trs = document.querySelector("#invoice-table").querySelector("[name=content-body]").querySelectorAll("tr");
	if(trs.length != 0) {
		let counter = 0;
		for(let tr of trs) {
			console.log(tr);
			if(filterCondicion(tr)) {
				tr.classList.remove("collapse");
				counter++;
			} else {
				tr.classList.add("collapse");
			}
		}
		if(counter == 0) {
			document.querySelector("#invoice-table").querySelector("[name=no-result-body]").classList.remove("collapse");
		} else {
			document.querySelector("#invoice-table").querySelector("[name=no-result-body]").classList.add("collapse");
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
	let trs = document.querySelector("#invoice-table").querySelector("[name=content-body]").querySelectorAll("tr");
	if(trs.length != 0) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#invoice-table").querySelector("[name=no-result-body]").classList.add("collapse");
	}
}

function acceptInvoiceByCompany(btn) {
	let invoiceNumber = btn.parentElement.parentElement.querySelector("[name=invoiceNumber]").innerHTML;
	sendData("POST", "/invoice/acceptByCompany", "invoiceNumber=" + invoiceNumber, callbackOfAcceptInvoiceByCompany);
}

function callbackOfAcceptInvoiceByCompany(data) {
	if(!data.error) {
		document.querySelector("#invoiceNumber" + data.text).classList.remove("hasRevisionNote");
		document.querySelector("#invoiceNumber" + data.text).querySelector("[name=acceptBtn]").classList.add('collapse');
		document.querySelector("#invoiceNumber" + data.text).querySelector("[name=deleteBtn]").classList.add('collapse');
	}
}

function deleteInvoice(btn) {
	let invoiceNumber = btn.parentElement.parentElement.querySelector("[name=invoiceNumber]").innerHTML;
	sendData("POST", "/invoice/delete", "invoiceNumber=" + invoiceNumber, callbackOfDeleteInvoice);
}

function callbackOfDeleteInvoice(data) {
	console.log(data);
	if(!data.error) {
		let table = document.querySelector("#invoice-table");
		let tr = table.querySelector("#invoiceNumber" + data.text);
		table.deleteRow(tr.rowIndex);
		if(table.querySelector("[name=content-body]").querySelectorAll("tr").length == 0) {
			table.querySelector("[name=no-element-body]").classList.remove("collapse");
		}
	}
}