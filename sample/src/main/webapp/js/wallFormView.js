define([], function() {
	'use strict';
	
	var wallFormView = new function() {
		
		this.submit = function() {
			var toto = 42;
			if (toto == 43) {
				toto = 45;
			}
			return 42;
		};
	}
	
	
	return wallFormView;
});