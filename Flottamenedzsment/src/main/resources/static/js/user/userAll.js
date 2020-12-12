function filter() {
	let trs = document.querySelector("#user-table").querySelector("#content-body").querySelectorAll("tr");
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
			document.querySelector("#user-table").querySelector("#no-result-body").classList.remove("collapse");
		} else {
			document.querySelector("#user-table").querySelector("#no-result-body").classList.add("collapse");
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
	let trs = document.querySelector("#user-table").querySelector("#content-body").querySelectorAll("tr");
	if(trs.length != 0) {
		for(let tr of trs) {
			tr.classList.remove("collapse");
		}
		document.querySelector("#user-table").querySelector("#no-result-body").classList.add("collapse");
	}
}

function deleteUser(deleteBtn) {
	let id = deleteBtn.parentElement.parentElement.id.substr('id'.length);
	console.log(id)
	sendData("DELETE", "/admin/user/delete", "id=" + id, callbackOfDeleteUser);
}

function callbackOfDeleteUser(data) {
	if(data.text != 0) {
		let row = document.querySelector("#id" + data.text);
		document.querySelector("#user-table").deleteRow(row.rowIndex);
	} else {
		location.reload();
	}
}