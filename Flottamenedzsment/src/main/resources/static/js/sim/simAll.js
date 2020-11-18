function filter() {
	let trs = document.querySelector("#sim-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != 0) {
		let counter = 0;
		for(let tr of trs) {
			if(filterCondicion(tr)) {
				tr.classList.remove("collapse");
				counter++;
			} else {
				tr.classList.add("collapse");
			}
		}
		if(counter == 0) {
			document.querySelector("#sim-table").querySelector("#no-result-body").classList.remove("collapse");
		} else {
			document.querySelector("#sim-table").querySelector("#no-result-body").classList.add("collapse");
		}
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#imei").value = "";
	showAll();
}

function filterCondicion(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let imei = filterOptionsWrapper.querySelector("#imei");
	return tr.querySelector("[name=imei]").innerHTML.toLowerCase().includes(imei.value.toLowerCase());
}

function showAll() {
	let trs = document.querySelector("#sim-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != null) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#sim-table").querySelector("#no-result-body").classList.add("collapse");
	}
}