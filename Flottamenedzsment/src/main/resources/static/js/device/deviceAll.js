function filter() {
	let trs = document.querySelector("#device-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != 0) {
		let counter = 0;
		for(let tr of trs) {
			if(filterCondition(tr)) {
				tr.classList.remove("collapse");
				counter++;
			} else {
				tr.classList.add("collapse");
			}
		}
		if(counter == 0) {
			document.querySelector("#device-table").querySelector("#no-result-body").classList.remove("collapse");
		} else {
			document.querySelector("#device-table").querySelector("#no-result-body").classList.add("collapse");
		}
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#serialNumber").value = "";
	filterOptionsWrapper.querySelector("#typeName").value = "";
	filterOptionsWrapper.querySelector("#userName").value = "";
	showAll();
}

function filterCondition(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let serialNumber = filterOptionsWrapper.querySelector("#serialNumber");
	let typeName = filterOptionsWrapper.querySelector("#typeName");
	let userName = filterOptionsWrapper.querySelector("#userName");
	return tr.querySelector("[name=serialNumber]").innerHTML.toLowerCase().includes(serialNumber.value.toLowerCase()) &&
		   tr.querySelector("[name=typeName]").innerHTML.toLowerCase().includes(typeName.value.toLowerCase()) &&
		   tr.querySelector("[name=userName]").innerHTML.toLowerCase().includes(userName.value.toLowerCase());
}

function showAll() {
	let trs = document.querySelector("#device-table").querySelector("tbody").querySelectorAll("tr");
	if(trs.length != 0) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#device-table").querySelector("#no-result-body").classList.add("collapse");
	}
}