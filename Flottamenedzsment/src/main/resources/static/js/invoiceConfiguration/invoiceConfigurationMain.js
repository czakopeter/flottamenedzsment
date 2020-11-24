function test(a) {
	for(let c of a.parentElement.children) {
		if(a != c  && c.getAttribute('aria-expanded') == 'true') {
			console.log(c);
			c.click();
		}
	}
}

function selectCategory(selectedCategory) {
	if(selectedCategory.selectedIndex != 0) {
		document.querySelector("#categoryName").value = selectedCategory[selectedCategory.selectedIndex].innerHTML;
	} else {
		document.querySelector("#categoryName").value = "";
	}
}

function addOrModifyCategory(btn) {
	let container = btn.parentElement;
	let categorySelect = container.querySelector("#selectedCategory");
	let id = categorySelect[categorySelect.selectedIndex].value;
	let name = container.querySelector("#categoryName").value.trim();
	if(name !== "") {
		sendData("POST", "/invoiceConfiguration/category/addOrModify", "id=" + id + "&name=" + name, callbackOfAddOrModifyCategory);
	}
}


function callbackOfAddOrModifyCategory(data) {
	console.log(data);
	let categorySelect = document.querySelector("#selectedCategory");
	if(categorySelect.selectedIndex == 0) {
		let option = document.createElement("option");
		option.value = data.id;
		option.text = data.name;
		categorySelect.add(option);
		categorySelect.value = data.id;
	} else {
		let option = categorySelect[categorySelect.selectedIndex];
		option.text = data.name;
	}
}