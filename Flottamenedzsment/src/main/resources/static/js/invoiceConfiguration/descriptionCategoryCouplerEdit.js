let descriptions;

function getDescriptionsOfInvoice() {
	let invoiceNumber = document.querySelector("#selectedInvoice").value;
	sendData("POST", "/invoice/getDescriptionsOfInvoice", "invoiceNumber=" + invoiceNumber, test);
	
	let formData = new FormData();
	formData.append('id', id);
	fetch("/invoice/getDescriptionsOfInvoice?id=" + id, {
		method: 'POST',
		body: formData
	})
	.then(response => {response.json(),
		console.log(response)})
	.then(result => {
		console.log('Success:', result);
	})
	.catch(error => {
	  console.error('Error:', error);
	});
}

function setDescription(data) {
	let descriptions = document.querySelectorAll("[name=description]");
}