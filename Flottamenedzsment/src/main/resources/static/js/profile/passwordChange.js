function newPswCheck() {
	let op = document.querySelector("#old-password");
	let np = document.querySelector("#new-password");
	let cnp = document.querySelector("#confirm-new-password");
	if(np.value != cnp.value) {
		document.querySelector("#error").style.display = null;
		document.querySelector("#saveBtn").disabled = true;
	} else if(np.value.trim().length == 0 || op.trim().length == 0) {
		document.querySelector("#error").style.display = 'none';
		document.querySelector("#saveBtn").disabled = true;
	} else {
		document.querySelector("#error").style.display = 'none';
		document.querySelector("#saveBtn").disabled = false;
	}
}