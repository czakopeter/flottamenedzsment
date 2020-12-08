function prepare_editing(editButton) {
	let tr = editButton.parentElement.parentElement.parentElement;
    tdTextChangeToInput(tr.querySelector('[name=userGrossAmount]'));
    tdTextChangeToInput(tr.querySelector('[name=compGrossAmount]'));
    tr.querySelector('#prepareEditingButton').style.display = 'none';
    tr.querySelector('#acceptOrCancelEditingButton').style.display = null;
}
       		
function accept_editing(acceptButton) {
	let tr = acceptButton.parentElement.parentElement.parentElement;
	
    let td = tr.querySelector('[name=userGrossAmount]');
    let input = td.querySelector('input');
    let userGrossAmount = input.value;
    inputValueToTdTextAndRemove(td, input);
    
    td = tr.querySelector('[name=compGrossAmount]');
    input = td.querySelector('input');
    let compGrossAmount = input.value;
    inputValueToTdTextAndRemove(td, input);
    
    tr.querySelector('#prepareEditingButton').style.display = null;
    tr.querySelector('#acceptOrCancelEditingButton').style.display = 'none';
    
    let id = tr.id.substr("feeItem".length);
    sendData('POST', '/invoice/modifyFeeItemGrossAmount', 'id=' + id + '&userGrossAmount=' + userGrossAmount + '&compGrossAmount=' + compGrossAmount, function () {});
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
    userInput.value = amountToFloatConverter(td.textContent);
    userInput.defaultValue = amountToFloatConverter(td.textContent);
    userInput.setAttribute('size', '1');
    td.textContent = '';
    td.appendChild(userInput);
}
       		
function inputValueToTdTextAndRemove(td, input) {
	td.textContent = floatToAmountConverter(input.value);
	input.remove();
}
       		
function inputDefaultValueToTdTextAndRemove(td, input) {
	td.textContent = floatToAmountConverter(input.defaultValue);
	input.remove();
}
       		
function modifyAmountRatio(input) {
	if(isDecimal(input.value)) {
		let td = input.parentElement;
		let tr = td.parentElement;
		let otherInput = td.getAttribute('name') == 'userGrossAmount' ? 
				tr.querySelector('[name=compGrossAmount]').querySelector('input') :
       			tr.querySelector('[name=userGrossAmount]').querySelector('input');
        let fullAmount = amountToFloatConverter(tr.querySelector('[name=totalGrossAmount]').textContent);
        
        if(parseFloat(input.value) > parseFloat(fullAmount)) {
           	input.value = fullAmount;
           	otherInput.value = 0.0;
        } else if(parseFloat(input.value) < 0) {
        	input.value = 0.0;
           	otherInput.value = fullAmount;
        } else {
           	otherInput.value = (parseFloat(fullAmount) - parseFloat(input.value)).toFixed(2);
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

function change(a) {
	if(a.getAttribute('aria-expanded') == 'true') {
		a.querySelector('img').src = "/img/down-arrow.png";
	} else {
		a.querySelector('img').src = "/img/up-arrow.png";
	}
}

function amountToFloatConverter(text) {
	return text.replace(" Ft", "").replace(",",".");
}

function floatToAmountConverter(float) {
	return (float + " Ft").replace(".", ",");
}