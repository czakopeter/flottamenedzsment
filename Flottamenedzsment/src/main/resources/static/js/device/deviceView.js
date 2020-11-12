function changeDate(dateInput) {
	let id = document.querySelector("#id").value;
	let date = dateInput.value;
	sendData("POST", "/device/" + id + "/view", "date=" + date, setViewData);
}

function setViewData(data) {
	console.log(data);
	document.querySelector("#userName").value = data.userName;
	document.querySelector("#note").value = data.note;
	document.querySelector("#beginDate").value = data.beginDate;
}