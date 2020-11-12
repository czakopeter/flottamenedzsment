function filter() {
	let trs = document.querySelector("#device-type-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		if(filterCondition(tr)) {
			tr.classList.remove("collapse");
		} else {
			tr.classList.add("collapse");
		}
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#name").value = "";
	filterOptionsWrapper.querySelector("#brand").value = "";
	filterOptionsWrapper.querySelector("#model").value = "";
	filterOptionsWrapper.querySelector("#status").value = filterOptionsWrapper.querySelector("#status")[0].value;
	showAll();
}

function filterCondition(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let name = filterOptionsWrapper.querySelector("#name");
	let brand = filterOptionsWrapper.querySelector("#brand");
	let model = filterOptionsWrapper.querySelector("#model");
	return tr.querySelector("[name=name]").innerHTML.toLowerCase().includes(name.value.toLowerCase()) &&
		   tr.querySelector("[name=brand]").innerHTML.toLowerCase().includes(brand.value.toLowerCase()) &&
		   tr.querySelector("[name=model]").innerHTML.toLowerCase().includes(model.value.toLowerCase()) &&
		   conditionForStatus(tr);
}

function conditionForStatus(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let status = filterOptionsWrapper.querySelector("#status").value;
	let visible = tr.querySelector("[name=visible]").innerHTML.toLowerCase();
	return status == "all" ||
			(status == "active" && visible == "true") ||
			(status == "archived" && visible == "false");
}

function showAll() {
	let trs = document.querySelector("#device-type-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
}