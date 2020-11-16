function modifyNotes() {
	let fees = document.querySelectorAll("[name=fee]");
	
	if(document.querySelector("[name=textarea]").value.length > 0) {
		showRevisionBtn();
		return;
	}
	for(fee of fees) {
		if(fee.querySelector("input").value.length > 0) {
			showRevisionBtn();
			return;
		}
	}
	hideRevisionBtn();
}

function showRevisionBtn() {
	document.querySelector("#revisionBtn").style.visibility = 'visible';
}

function hideRevisionBtn() {
	document.querySelector("#revisionBtn").style.visibility = 'hidden';
}


