function filter() {
	let trs = document.querySelector("#subscription-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		console.log(tr);
		if(filterCondicion(tr)) {
			tr.classList.remove("collapse");
		} else {
			tr.classList.add("collapse");
		}
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#number").value = "";
	filterOptionsWrapper.querySelector("#imei").value = "";
	filterOptionsWrapper.querySelector("#userName").value = "";
	showAll();
}

function filterCondicion(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let number = filterOptionsWrapper.querySelector("#number");
	let imei = filterOptionsWrapper.querySelector("#imei");
	let userName = filterOptionsWrapper.querySelector("#userName");
	return tr.querySelector("[name=number]").innerHTML.toLowerCase().includes(number.value.toLowerCase()) &&
		   tr.querySelector("[name=imei]").innerHTML.toLowerCase().includes(imei.value.toLowerCase()) &&
				   tr.querySelector("[name=userName]").innerHTML.toLowerCase().includes(userName.value.toLowerCase());
}

function showAll() {
	let trs = document.querySelector("#subscription-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
}