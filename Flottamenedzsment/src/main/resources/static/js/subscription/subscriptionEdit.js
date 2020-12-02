function simChange(imeiSelectBtn) {
	imeiSelectBtn.parentElement.parentElement.style.display = 'none';
	let selectedImei = imeiSelectBtn.parentElement.parentElement.querySelector("[name=imei]").innerText;
	document.querySelector("#imei").value = selectedImei;
	document.querySelector("#reasonRow").style.display = null;
	document.querySelector("#simChangeResetBtn").disabled = false;
	document.querySelector("#simChangeReason").required = true;
	console.log(selectedImei);
}

function resetSimChange() {
	console.log("resetSimChange");
	document.querySelector("#imei").value = document.querySelector("#oldImei").value;
	document.querySelector("#reasonRow").style.display = 'none';
	document.querySelector("#simChangeResetBtn").disabled = true;
	document.querySelector("#simChangeReason").required = false;
	document.querySelector("#simChangeReason").value = null;
}

function selectUser(userSelect) {
	let min = document.querySelector("#minDate").value;
	document.querySelector("#beginDate").min = min;
	clearDeviceSelect();
	if(userSelect.value > 0) {
		sendData("POST", "/subscription/getDevicesByUser", "userId=" + userSelect.value, setDeviceSelect);
	}
}

function setDeviceSelect(devices) {
	let select = document.querySelector("#deviceSelect");
	devices.forEach(obj => {
		let option = document.createElement("option");
		option.text = obj.typeName + ' (' + obj.serialNumber + ')';
		option.value = obj.id;
		select.add(option);
	});
}

function clearDeviceSelect() {
	let select = document.querySelector("#deviceSelect");
	while(select.length > 1) {
		select.remove(select.length - 1);
	}
}

function selectDevice(deviceSelect) {
	let min = document.querySelector("#minDate").value;
	document.querySelector("#beginDate").min = min;
	if(deviceSelect.value > 0) {
		sendData("POST", "/subscription/getDeviceById", "id=" + deviceSelect.value, setDateMinimal);
	}
}

function setDateMinimal(device) {
	let min = document.querySelector("#minDate").value;
	console.log(device);
	if(device.error) {
		document.querySelector("#beginDate").min = min
	} else {
		let dateInput = document.querySelector("#beginDate");
		
		if(device.beginDate > min) {
			dateInput.min = device.beginDate;
		} else {
			dateInput.min = min;
		}
	}
}