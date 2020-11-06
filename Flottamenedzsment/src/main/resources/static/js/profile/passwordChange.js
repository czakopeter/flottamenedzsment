function newPswCheck() {
	let np = document.querySelector("#new-password");
	let cnp = document.querySelector("#confirm-new-password");
	if(np.value != cnp.value) {
		document.querySelector("#error").style.display = null;
		document.querySelector("#saveBtn").disabled = true;
	} else {
		document.querySelector("#error").style.display = 'none';
		document.querySelector("#saveBtn").disabled = false;
	}
}