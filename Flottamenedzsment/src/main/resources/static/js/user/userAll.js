function filter() {
	let trs = document.querySelector("#user-table").querySelector("#content-body").querySelectorAll("tr");
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
		document.querySelector("#user-table").querySelector("#no-result-body").classList.remove("collapse");
	} else {
		document.querySelector("#user-table").querySelector("#no-result-body").classList.add("collapse");
	}
}

function clearFilter() {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	filterOptionsWrapper.querySelector("#email").value = "";
	filterOptionsWrapper.querySelector("#fullName").value = "";
	showAll();
}

function filterCondicion(tr) {
	let filterOptionsWrapper = document.querySelector("#filter-options-wrapper");
	let email = filterOptionsWrapper.querySelector("#email");
	let fullName = filterOptionsWrapper.querySelector("#fullName");
	return tr.querySelector("[name=email]").innerHTML.toLowerCase().includes(email.value.toLowerCase()) &&
		   tr.querySelector("[name=fullName]").innerHTML.toLowerCase().includes(fullName.value.toLowerCase());
}

function showAll() {
	let trs = document.querySelector("#user-table").querySelector("#content-body").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
	document.querySelector("#user-table").querySelector("#no-result-body").classList.add("collapse");
}