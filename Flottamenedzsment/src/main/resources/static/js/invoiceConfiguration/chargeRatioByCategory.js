let lastId = document.querySelector('tbody').children.length;

refreshAllView();

function refreshAllView() {
	for(let i = 1; i <= lastId; i++) {
		refreshView(i);
	}
}

function refreshView(id) {
	let ratio = document.getElementById('ratio' + id);
	if(ratio.value == 0) {
		document.getElementById('company' + id).checked = true;
	} else if(ratio.value == 100) {
		document.getElementById('user' + id).checked = true;
	} else {
		document.getElementById('company' + id).checked = false;
		document.getElementById('user' + id).checked = false;
	}
	document.getElementById('userPercent' + id).innerHTML = ratio.value;
	document.getElementById('companyPercent' + id).innerHTML = 100 - ratio.value;
}

function setRangeInput(id) {
	refreshView(id);
}

function clickOnRadioButton(group, id) {
	var ratio = document.getElementById('ratio' + id);
	ratio.value = group == 'user' ? 100 : 0;
	
	refreshView(id);
}

function addCategory() {
	lastId++;
	let table = document.querySelector('tbody');
	
	let row = document.createElement('tr');
	row.className = 'table-warning';
	table.appendChild(row);
	createToCategoryColumn(lastId, row);
	createSetterPart(lastId, row);
	
	refreshView(lastId);
	
	if(document.querySelector('#selectUnusedCategory').length == 0) {
		document.querySelector('#category-adder').style.display = 'none';
	}
}

function createCell(parent) {
	let cell = document.createElement('td');
	cell.className = 'align-middle';
	parent.appendChild(cell);
	return cell;
}

function createToCategoryColumn(id, parent) {
	let cell = createCell(parent);
	
	let select = document.querySelector('#selectUnusedCategory');
	let selected = select.options[select.selectedIndex];
	select.remove(select.selectedIndex);
	
	let label = document.createElement('label');
	label.innerHTML = selected.text;
	cell.appendChild(label);
	
	let hiddenCategory = document.createElement('input');
	hiddenCategory.type = 'hidden';
	hiddenCategory.name = 'category';
	hiddenCategory.value = selected.value;
	cell.appendChild(hiddenCategory);
}

function createSetterPart(id, parent) {
	createPercentViewColumn(id, 'companyPercent', parent);
	createRadioInputColumn(id, 'company', parent);
	createRangeColumn(id, parent);
	createRadioInputColumn(id, 'user', parent);
	createPercentViewColumn(id, 'userPercent', parent);
}

function createPercentViewColumn(id, preId, parent) {
	let cell = createCell(parent);
	cell.style.width = '4em';
	
	let value = document.createElement('label');
	value.id = preId + id;
	value.className = 'form-check-label';
	cell.appendChild(value);
	
	let percent = document.createElement('label');
	percent.innerHTML = '%';
	cell.appendChild(percent);
}

function createRadioInputColumn(id, preId, parent) {
	let cell = createCell(parent)
	cell.className +=' text-center';
	
	let radio = document.createElement('input');
	radio.type = 'radio';
	radio.id = preId + id;
	radio.name = 'radio' + id;
	radio.setAttribute('onclick', "clickOnRadioButton('" + preId + "'," + id + ")");
	cell.appendChild(radio);
}

function createRangeColumn(id, parent) {
	
	let cell = createCell(parent);
	
	let rangeInput = document.createElement('input');
	rangeInput.type = 'range';
	rangeInput.className = 'form-control-range';
	rangeInput.id = 'ratio' + id;
	rangeInput.name = 'ratio';
	rangeInput.setAttribute('oninput', 'refreshView(' + id + ')');
	rangeInput.min = 0;
	rangeInput.max = 100;
	rangeInput.step = 5;
	rangeInput.value = 0;
	cell.appendChild(rangeInput);
}
