function dateChange(dateSelect) {
	let id = document.querySelector("#id").value;
	sendData("POST", "/subscription/" + id + "/view", "date=" + dateSelect.value, setViewData)
}

function setViewData(data) {
	document.querySelector("#userName").value = data.userName;
	document.querySelector("#deviceName").value = data.deviceName;
	document.querySelector("#note").value = data.note;
	document.querySelector("#beginDate").value = data.beginDate;
}