function filter() {
	let trs = document.querySelector("#user-table").querySelector("tbody").querySelectorAll("tr");
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
	let trs = document.querySelector("#user-table").querySelector("tbody").querySelectorAll("tr");
	for(let tr of trs) {
		tr.classList.remove("collapse");
	}
}