function filter() {
	let trs = document.querySelector("#device-type-table").querySelector("#content-body").querySelectorAll("tr");
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
			document.querySelector("#device-type-table").querySelector("#no-result-body").classList.remove("collapse");
		} else {
			document.querySelector("#device-type-table").querySelector("#no-result-body").classList.add("collapse");
		}
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#name").value = "";
	filterOptionsWrapper.querySelector("#brand").value = "";
	filterOptionsWrapper.querySelector("#model").value = "";
	filterOptionsWrapper.querySelector("#availability").value = filterOptionsWrapper.querySelector("#availability")[0].value;
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
	console.log(filterOptionsWrapper);
	let selectedAvailability = filterOptionsWrapper.querySelector("#availability").value;
	let availability = tr.querySelector("[name=availability]").innerHTML.toUpperCase();
	console.log(selectedAvailability);
	console.log(availability);
	return selectedAvailability == "ALL" || selectedAvailability == availability;
}

function showAll() {
	let trs = document.querySelector("#device-type-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != 0) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#device-type-table").querySelector("#no-result-body").classList.add("collapse");
	}
}