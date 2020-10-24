function test(a) {
	for(let c of a.parentElement.children) {
		if(a != c  && c.getAttribute('aria-expanded') == 'true') {
			console.log(c);
			c.click();
		}
	}
}