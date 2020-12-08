function modifyNotes() {
	let fees = document.querySelectorAll("[name=fee]");
	
	if(document.querySelector("[name=textarea]").value.length > 0) {
		showReviewBtn();
		return;
	}
	for(fee of fees) {
		if(fee.querySelector("input").value.length > 0) {
			showReviewBtn();
			return;
		}
	}
	hideReviewBtn();
}

function showReviewBtn() {
	document.querySelector("#reviewBtn").disabled = false;
}

function hideReviewBtn() {
	document.querySelector("#reviewBtn").disabled = true;
}


