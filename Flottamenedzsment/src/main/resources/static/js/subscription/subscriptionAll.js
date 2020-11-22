function filter() {
	let trs = document.querySelector("#subscription-table").querySelector("#content-body").querySelectorAll("tr");
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
			document.querySelector("#subscription-table").querySelector("#no-result-body").classList.remove("collapse");
		} else {
			document.querySelector("#subscription-table").querySelector("#no-result-body").classList.add("collapse");
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

function filterCondition(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let number = filterOptionsWrapper.querySelector("#number");
	let imei = filterOptionsWrapper.querySelector("#imei");
	let userName = filterOptionsWrapper.querySelector("#userName");
	return tr.querySelector("[name=number]").innerHTML.toLowerCase().includes(number.value.toLowerCase()) &&
		   tr.querySelector("[name=imei]").innerHTML.toLowerCase().includes(imei.value.toLowerCase()) &&
		   tr.querySelector("[name=userName]").innerHTML.toLowerCase().includes(userName.value.toLowerCase());
}

function showAll() {
	let trs = document.querySelector("#subscription-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != 0) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#subscription-table").querySelector("#no-result-body").classList.add("collapse");
	}
}