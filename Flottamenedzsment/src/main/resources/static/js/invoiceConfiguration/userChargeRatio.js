function selectUser(selectedUser) {
	let userId = selectedUser.value;
	if(userId != 0) {
		sendData("POST", "/invoiceConfiguration/getChargeRatioOfUser", "userId=" + userId, callbackOfSelectUser);
	}
}

function callbackOfSelectUser(data) {
	if(!data.error) {
		console.log(data);
		document.querySelector("#selectedChargeRatio").value = data.text;
	}
}

function modifyChargeRatioOfUser(btn) {
	let container = btn.parentElement;
	let userId = container.querySelector("#selectedUser").value;
	let chargeRatioId = container.querySelector("#selectedChargeRatio").value;
	if(userId != 0 && chargeRatioId != 0) {
		sendData("POST", "/invoiceConfiguration/modifyChargeRatioOfUser", "userId=" + userId + "&chargeRatioId=" + chargeRatioId, callbackOfModifyChargeRatioOfUser)
	}
}

function callbackOfModifyChargeRatioOfUser(data) {
	if(!data.error) {
		document.querySelector("#selectedChargeRatio").value = date.text;
	}
}