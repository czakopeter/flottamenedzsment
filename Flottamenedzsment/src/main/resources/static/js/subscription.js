function dateChange(dateSelect) {
	let id = document.querySelector("#id").value;
	sendData("POST", "/subscription/" + id + "/view", "date=" + dateSelect.value, setViewData)
}

function setViewData(data) {
	document.querySelector("#userName").value = data.userName;
	document.querySelector("#deviceName").value = data.deviceName;
	document.querySelector("#note").value = data.note;
	document.querySelector("#date").value = jsonDateToString(data.date);
}

function jsonDateToString(date) {
	let inputDate = date.year + '-';
	if(date.monthValue < 10) {
		inputDate += '0';
	}
	inputDate += date.monthValue + '-';
	if(date.dayOfMonth < 10) {
		inputDate += '0';
	}
	inputDate += date.dayOfMonth;
	return inputDate
}

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
	sendData("POST", "/subscription/getDevicesByUser", "userId=" + userSelect.value, setDeviceSelect);
}

function setDeviceSelect(devices) {
	let select = document.querySelector("#deviceSelect");
	while(select.length > 1) {
		select.remove(1);
	}
	devices.forEach(obj => {
		let option = document.createElement("option");
		option.text = obj.typeName + ' (' + obj.serialNumber + ')';
		option.value = obj.id;
		select.add(option);
	});
}

function selectDevice(deviceSelect) {
	sendData("POST", "/subscription/getDeviceById", "id=" + deviceSelect.value, setDateMinimal);
}

function setDateMinimal(device) {
	let min = document.querySelector("#min").value;
	if(device.error) {
		document.querySelector("#date").min = min
	} else {
		console.log('ok');
		let deviceDate = jsonDateToString(device.date);
		let dateInput = document.querySelector("#date");
		
		if(deviceDate > min) {
			dateInput.min = deviceDate;
		} else {
			dateInput.min = min;
		}
	}
}