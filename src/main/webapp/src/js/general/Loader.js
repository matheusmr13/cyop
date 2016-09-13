var Loader = {};
var loadTemplate,
	spinnerTemplate;

var spinner = function () {
	if (spinnerTemplate) {
		return spinnerTemplate;
	}

	spinnerTemplate = ['<div class="spinner">'];
	for (var i = 0; i < 3; i++) {
		spinnerTemplate.push('<div class="spinner-container">');

		for (var j = 0; j < 4; j++) {
			spinnerTemplate.push('<div></div>');
		}

		spinnerTemplate.push('</div>');
	}

	spinnerTemplate = spinnerTemplate.join('');

	return spinner();
};

var load = function () {
	if (loadTemplate) {
		return loadTemplate;
	}

	loadTemplate = '<div class="spinner-wrapper">' + spinner() + '</div>';

	return load();
};

Loader.pushLoad = function (container, position) {
	position = position || 'relative';
	container.each(function(){
		var $this = $(this);
		if ($this.find(' > .loading-spinner-container').size() === 0) {
			var $loader = $('<div class="loading-spinner-container"></div>').css('z-index', '1000');

			if ($this.prop('id') == 'content') {
				$loader.css('position', 'absolute');
			} else {
				$loader.css('position', position);
			}

			$this.append($loader);
			$loader.append(load());
		}
	});
};

Loader.popLoad = function (container) {
	container.find(' > .loading-spinner-container').remove();
};

module.exports = Loader;
