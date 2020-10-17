function filter() {
	let trs = document.querySelector("#sim-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		console.log(tr);
		if(filterCondicion(tr)) {
			tr.classList.remove("collapse");
		} else {
			tr.classList.add("collapse");
		}
	}
}

function resetFilter() {
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
	let trs = document.querySelector("#sim-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
}