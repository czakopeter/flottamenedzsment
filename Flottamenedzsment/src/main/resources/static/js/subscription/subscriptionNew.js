const number = RegExp('^[0-9]*$');

function testPhone(input) {
	if(number.test(input.value)) {
		input.defaultValue = input.value;
	} else {
		console.log('not ok');
		input.value = input.defaultValue;
	}
}