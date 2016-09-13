var yawp = require('yawp');
yawp.config(function (c) {
	c.baseUrl('/admin');
});
window.yawp = yawp;

if (!String.prototype.startsWith) {
	String.prototype.startsWith = function (searchString, position) {
		position = position || 0;
		return this.substr(position, searchString.length) === searchString;
	};
}
Object.isEmpty = function(obj) {
	return !obj || !Object.keys(obj).length;
};

window.$ = require('jquery');
$.fn.hasScrollBar = function() {
	return this.get(0).scrollHeight > this.height();
}

require('./libs/mixpanel.js');
require('./libs/materialize.min.js');
var Navigate = require('./general/Navigate.js');
Navigate.initialize();