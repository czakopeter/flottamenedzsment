function nameAutoComplete() {
	if (document.querySelector('#autoNameGeneration').checked) {
		document.querySelector('#name').value = document.querySelector('#brand').value + ' ' + document.querySelector('#model').value;
	}
}

function autoNameGenerattionChange() {
	let name = document.querySelector('#name');
	if (document.querySelector('#autoNameGeneration').checked) {
		name.readOnly = true;
		name.value = document.querySelector('#brand').value + ' ' + document.querySelector('#model').value;
	} else {
		name.readOnly = false;
	}
}