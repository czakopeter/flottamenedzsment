//document.querySelector("#admin").addEventListener("click", adminCheckboxClick());

function adminCheckboxClick(cb) {
	
	for(let input of document.querySelector("#normal-roles-wrapper").querySelectorAll("input")) {
		if(cb.checked) {
			input.disabled = true;
		} else {
			input.disabled = false;
		}
	}
}