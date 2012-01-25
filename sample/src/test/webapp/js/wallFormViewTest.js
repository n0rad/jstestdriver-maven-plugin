require([ 'wallFormView' ], function(wallFormView) {

	
//	TestCase('salut', {
//
//	    testSalut : function() {
//	        var p = new Person('John', 'Doe', 'P.');
//	        assertEquals('Should have responded with full name', 'John P. Doe', p.whoAreYou());
//	    },
//
//	    
//	    testSalut2 : function() {
//	        var p = new Person('John', 'Doe');
//	        assertEquals(43, wallFormView.submit());
//	        assertEquals('Should have used only first and last name', 'John Doe', p.whoAreYou());
//	    }
//
//	});
	
	describe('runs', function() {
	it('should execute a runs block', function() {
		runs(function() {
			this.runsFunction = function() {
				return true;
			};
			spyOn(this, 'runsFunction');
		});

		runs(function() {
			this.runsFunction();
		});

		runs(function() {
			expect(wallFormView.submit()).toBe(42);
			expect(true).toBe(true);
			expect(this.runsFunction).wasCalled();
		});
	});
});

});

// describe("Scene", function() {
// var scene;
//
// beforeEach(function() {
// scene = new Scene();
// });
//
// it("should instantiate ok", function() {
// expect(scene).toBeTruthy();
// });
//
// });
