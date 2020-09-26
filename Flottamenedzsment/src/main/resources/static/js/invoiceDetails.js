function prepare_editing(editButton) {
	let tr = editButton.parentElement.parentElement.parentElement;
    tdTextChangeToInput(tr.querySelector('[name=userGrossAmount]'));
    tdTextChangeToInput(tr.querySelector('[name=compGrossAmount]'));
    tr.querySelector('#prepareEditingButton').style.display = 'none';
    tr.querySelector('#acceptOrCancelEditingButton').style.display = null;
}
       		
function accept_editing(acceptButton) {
	let tr = acceptButton.parentElement.parentElement.parentElement;
	
	let userGrossAmount;
	let compGrossAmount;
	
    let td = tr.querySelector('[name=userGrossAmount]');
    let input = td.querySelector('input');
    userGrossAmount = input.value;
    inputValueToTdTextAndRemove(td, input);
    
    td = tr.querySelector('[name=compGrossAmount]');
    input = td.querySelector('input');
    compGrossAmount = input.value;
    inputValueToTdTextAndRemove(td, input);
    
    tr.querySelector('#prepareEditingButton').style.display = null;
    tr.querySelector('#acceptOrCancelEditingButton').style.display = 'none';
    
    let id = tr.querySelector('[name=id]').value
    sendData('POST', '/invoice/modifyFeeItemGrossAmountRatio', 'id=' + id + '&userGrossAmount=' + userGrossAmount + '&compGrossAmount=' + compGrossAmount, function () {});
}

function cancel_editing(cancelButton) {
    let tr = cancelButton.parentElement.parentElement.parentElement;
    
    let td = tr.querySelector('[name=userGrossAmount]');
    let input = td.querySelector('input');
    inputDefaultValueToTdTextAndRemove(td, input);
    
    td = tr.querySelector('[name=compGrossAmount]');
    input = td.querySelector('input');
    inputDefaultValueToTdTextAndRemove(td, input);
    
    tr.querySelector('#prepareEditingButton').style.display = null;
    tr.querySelector('#acceptOrCancelEditingButton').style.display = 'none';
}
       		
function tdTextChangeToInput(td) {
    let userInput = document.createElement('input');
    userInput.setAttribute('type', 'text');
    userInput.setAttribute('class', 'form-control form-control-sm');
    userInput.setAttribute('style', 'text-align:right');
    userInput.setAttribute('onchange', 'modifyAmountRatio(this)');
    userInput.value = td.textContent;
    userInput.defaultValue = td.textContent;
    userInput.setAttribute('size', '1');
    td.textContent = '';
    td.appendChild(userInput);
}
       		
function inputValueToTdTextAndRemove(td, input) {
	td.textContent = input.value;
	input.remove();
}
       		
function inputDefaultValueToTdTextAndRemove(td, input) {
	td.textContent = input.defaultValue;
	input.remove();
}
       		
function modifyAmountRatio(input) {
	if(isDecimal(input.value)) {
		let td = input.parentElement;
		let tr = td.parentElement;
		let otherInput = td.getAttribute('name') == 'userGrossAmount' ? 
				tr.querySelector('[name=compGrossAmount]').querySelector('input') :
       			tr.querySelector('[name=userGrossAmount]').querySelector('input');
        let fullAmount = tr.querySelector('[name=totalGrossAmount]').textContent;

        if(parseFloat(input.value) > parseFloat(fullAmount)) {
           	input.value = fullAmount;
           	otherInput.value = 0.0;
        } else if(parseFloat(input.value) < 0) {
        	input.value = 0.0;
           	otherInput.value = fullAmount;
        } else {
           	otherInput.value = (fullAmount - input.value).toFixed(2);
           	input.value = parseFloat(input.value).toFixed(2);
        }
    } else {
       	input.value = input.defaultValue;
    }
}
       		
function isDecimal(number) {
	let num = parseFloat(number);
	if(num == 'NaN' || number.length != num.toString().length) {
		return false;
	}
	return true;
 }

function show_details(btn) {
	btn.setAttribute("onclick", "hide_details(this)");
	let tr = btn.parentElement.parentElement;
	let id = tr.id.substr("part".length);
	let rows = document.querySelectorAll("[name=feeItem" + id + "]");
	for(let row of rows) {
		row.style.visibility = "visible";
	}
	btn.innerHTML  = "Less";
}

function hide_details(btn) {
	btn.setAttribute("onclick", "show_details(this)");
	let tr = btn.parentElement.parentElement;
	let id = tr.id.substr("part".length);
	let rows = document.querySelectorAll("[name=feeItem" + id + "]");
	for(let row of rows) {
		row.style.visibility = "collapse";
	}
	btn.innerHTML  = "More";
}

function show_revision_note(btn) {
	let tr = btn.parentElement.parentElement;
	let index = tr.rowIndex + 1;
	let table = tr.parentElement;
	table.rows[index].style.visibility = "visible";
	btn.querySelector("img").src = "/img/up-arrow.png";
	btn.setAttribute("onclick", "hide_revision_note(this)");
}

function hide_revision_note(btn) {
	let tr = btn.parentElement.parentElement;
	let index = tr.rowIndex + 1;
	let table = tr.parentElement;
	table.rows[index].style.visibility = "collapse";
	btn.querySelector("img").src = "/img/down-arrow.png";
	btn.setAttribute("onclick", "show_revision_note(this)");
}