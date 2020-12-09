function test(a) {
	if(a.getAttribute('aria-expanded') == 'false') {
		a.classList.add('active');
	} else {
		a.classList.remove('active');
	}
	for(let c of a.parentElement.children) {
		if(a != c  && c.getAttribute('aria-expanded') == 'true') {
			console.log(c);
			c.click();
			c.classList.remove('active');
		}
	}
}
