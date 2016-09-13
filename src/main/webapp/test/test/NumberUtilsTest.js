var assert = require('chai').assert;

describe('Simple test', function () {
	describe('first', function () {
		it('should pass', function () {
			assert.equal(-1, [1,3,4].indexOf(2));
		});
	});
});
