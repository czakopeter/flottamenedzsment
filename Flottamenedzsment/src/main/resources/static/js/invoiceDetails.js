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

function no(data) {
	
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
// userInput.setAttribute('oninput', 'modifyAmountRatio(this)');
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
        } else {
           	otherInput.value = (fullAmount - input.value).toFixed(2);
           	input.value = parseFloat(input.value).toFixed(2);
        }
    } else {
       	input.value = input.defaultValue;
    }
}
       		
function isDecimal(number) {
	let hasDecimalPoint = false;
	for(let digit of number) {
		if(!Number.isInteger(parseInt(digit))) {
			if(!hasDecimalPoint && digit == '.') {
				hasDecimalPoint = true;
			} else {
				return false;
			}
		}
	}
  	return true;
 }