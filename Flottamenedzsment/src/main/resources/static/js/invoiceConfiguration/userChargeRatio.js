function selectUser(selectedUser) {
	let userId = selectedUser.value;
	if(userId != 0) {
		sendData("POST", "/invoiceConfiguration/getChargeRatioOfUser", "userId=" + userId, callbackOfSelectUser);
	}
}

function callbackOfSelectUser(chargeRatio) {
	if(!chargeRatio.error) {
		document.querySelector("#selectedChargeRatio").value = chargeRatio.id;
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

function callbackOfModifyChargeRatioOfUser(user) {
	if(!user.error) {
		document.querySelector("#selectedChargeRatio").value = user.chargeRatio.id;
	}
}