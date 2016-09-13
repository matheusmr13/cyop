var Config = require('./../config/Config.js');
var BaseService = require('./../services/BaseService.js');
var Formatter = require('./Formatter.js');
var doT = require('dot');
var Bundle = require('./Bundle.js');
var Loader = require('./Loader.js');
var Renderer = {};

Renderer.requestObj = function (url, objectName, isMainObject, settings) {
	return {
		url: url,
		pageObject: objectName,
		mainObject: isMainObject,
		settings: settings
	};
};

Renderer.requestPage = function (url, pageName) {
	return {
		url: url,
		pageObject: pageName,
		isPage: true
	};
};

Renderer.showPage = function (pageUrl, settings) {
	settings = settings || {};
	var d = $.Deferred(),
		container = Renderer.getMainContainer(settings);
	BaseService.GET_HTML(pageUrl).done(function (html) {
		var load = function () {
			Renderer.loadPreloadedHtml(html, container, settings).done(function (container) {
				d.resolve(container);
			});
		};

		var waitTimeout = Navigate.extractParameter('waitTimeout'); //TODO only for testing assync solutions
		if (waitTimeout) {
			setTimeout(load, parseInt(waitTimeout));
		} else {
			load();
		}
	}).fail(function () {
		d.reject();
	});
	return d.promise();
};

Renderer.loadPreloadedHtml = function(html, container, settings) {
	settings = settings || {};
	container.html(html);
	settings.container = container;
	var d = $.Deferred();
	if (settings.doNotChangeDOM) {
		d.resolve(container);
	} else {
		changeDOMIfNecessary(container, settings).done(function() {
			d.resolve(container);
		});
	}
	return d.promise();
};

Renderer.populatePage = function (pagesUrl, data, settings) {
	settings = settings || {};
	var d = $.Deferred(),
		container = Renderer.getMainContainer(settings),
		pagesRequest = [];

	if (typeof pagesUrl == 'string') {
		pagesRequest.push(BaseService.GET_HTML(pagesUrl));
	} else {
		for (var i = 0; i < pagesUrl.length; i++) {
			pagesRequest.push(BaseService.GET_HTML(pagesUrl[i].url));
		}
	}

	$.when.apply(this, pagesRequest).done(function () {
		for (var i = 1; i < arguments.length; i++) {
			data[pagesUrl[i].pageObject + 'Template'] = doT.template(arguments[i]);
		}

		var html = arguments[0];
		var load = function () {
			container.html(doT.template(html)(data));
			if (!settings.doNotChangeDOM) {
				changeDOMIfNecessary(container, settings, settings.mainObject ? data[settings.mainObject] : data).done(function () {
					d.resolve(container);
				});
			} else {
				d.resolve(container);
			}
		}

		load();
	});
	return d.promise();
};

Renderer.openNewPage = function (pageUrl, dataPromises, settings) {
	settings = settings || {};
	var d = $.Deferred(),
		container = Renderer.getMainContainer(settings),
		mainObject = '';
	Loader.pushLoad(container);

	var requests = [BaseService.GET_HTML(pageUrl)];
	for (var i = 0; i < dataPromises.length; i++) {
		if (dataPromises[i].isPage) {
			requests.push(BaseService.GET_HTML(dataPromises[i].promise));
		} else {
			requests.push(BaseService.GET(dataPromises[i].promise, dataUrls[i].settings ));
		}

		if (dataUrls[i].mainObject) {
			mainObject = dataUrls[i].pageObject;
		}
	}

	$.when.apply(this, requests).done(function () {
		var page = arguments[0],
			objectToDot = createItObject(arguments, dataUrls, settings);
		var load = function () {
			container.html(doT.template(page)(objectToDot));

			if (!settings.doNotChangeDOM) {
				changeDOMIfNecessary(container, settings, objectToDot[mainObject]).done(function () {
					d.resolve(container, objectToDot);
				});
			} else {
				d.resolve(container);
			}

		}

		load();
	}).fail(function (e) {
		Loader.popLoad(container);
		d.reject();
	});

	return d.promise();
};

var createItObject = function (requestReturns, dataUrls, settings) {
	var objectToDot = {};
	for (var i = 1; i < requestReturns.length; i++) {
		if (dataUrls[i - 1].isPage) {
			objectToDot[dataUrls[i - 1].pageObject + 'Template'] = doT.template(requestReturns[i]);
		} else {
			objectToDot[dataUrls[i - 1].pageObject] = requestReturns[i];
		}
	}

	if (settings.injectObjects) {
		for (var i = 0; i < settings.injectObjects.length; i++) {
			var injectObject = settings.injectObjects[i];
			objectToDot[injectObject.name] = injectObject.obj;
		}
	}

	return objectToDot;
};

Renderer.getMainContainer = function (settings) {
	if (settings) {
		return settings.containerJquery || $(settings.container || '#content');
	}
	return $('#content');
};

var changeDOMIfNecessary = function (container, settings, mainObject) {
	var d = [$.Deferred()];
	d[0].resolve();
	if (!settings.container && !settings.containerJquery) {
		$('body').scrollTop(0);
	}
	if (mainObject) {
		Renderer.populateTemplateWithObject(container, mainObject);
	}

	if (!settings.shouldNotApplyMask) {
		Formatter.applyMask(container.find('input'));
	}

	if (settings.bundleModule) {
		Bundle.applyBundleToElement(settings.bundleModule, container[0]);
	}

	$('body > .hint').remove();

	if (!settings.shouldNotSetAutocompletes) {
		var Autocomplete = require('./../components/Autocomplete.js');
		d.push(Autocomplete.checkNewAutocompleteElements(container));
	}

	HelpHint.initializeComponents(container, settings);

	overrideDefaultInvalidElements(container);

	return $.when.apply($, d);
};

var overrideDefaultInvalidElements = function (container) {
	var elements = container.find('input,select,textarea');
	for (var i = elements.length; i--;) {
		elements[i].addEventListener('invalid', function () {
			this.scrollIntoView(false);
		});
	}
};

var getValueOnJson = function (jsonObject, path) {
	var pathSplit = path.split('.');
	if (typeof jsonObject === 'undefined') {
		jsonObject = {};
	}
	if (pathSplit.length == 1) {
		return jsonObject[path];
	} else if (pathSplit.length > 1) {
		var jsonParent = jsonObject[pathSplit[0]];
		var key = pathSplit.shift();
		return getValueOnJson(jsonParent, pathSplit.toString().replace(/\,/g, '.'));
	}
}

Renderer.populateTemplateWithObject = function (container, mainObject) {
	container.find('[name], [data-name]').each(function (a) {
		var value = getValueOnJson(mainObject, $(this).prop('name') || $(this).data('name'));
		if (typeof value != 'undefined') {
			if ($(this).is('[type="radio"]')) {
				if (value === $(this).data('value')) {
					$(this).prop('checked', true);
				}
			} else if ($(this).is('[type="checkbox"]')) {
				if (value && value == $(this).data('value')) {
					$(this).prop('checked', true);
				}
			} else if ($(this).is('input')) {
				if ($(this).attr('data-mask') === 'money' || $(this).attr('data-mask') === 'percentage') {
					$(this).val(Formatter.formatFloat(value));
				} else if ($(this).attr('data-mask') === 'money-four-digits') {
					$(this).val(Formatter.formatFloat(value, 4));
				} else {
					$(this).val(value);
				}

				if ($(this).attr('data-mask') === 'date') {
					$(this).removeClass('invalid-field').addClass('valid-field');
				}
			} else if ($(this).is('select')) {
				if (typeof value === 'object') {
					$(this).val(value.id);
				} else {
					$(this).val(value);
				}
			} else if ($(this).is('textarea')) {
				$(this).val(value);
			} else if ($(this).is('label') || $(this).is('span')) {
				if ($(this).attr('data-mask') === 'money' || $(this).attr('data-mask') === 'percentage') {
					$(this).text(Formatter.formatFloat(value));
				} else if ($(this).attr('data-mask') === 'money-four-digits') {
					$(this).text(Formatter.formatFloat(value, 4));
				} else {
					$(this).text(value);
				}
				$(this).data('value', value);
			}
		} else {
			$(this).val($(this).data('default-value') || '');
			if ($(this).attr('data-mask') === 'date') {
				$(this).addClass('invalid-field').removeClass('valid-field');
			}
		}
	});
	container.find('form').data('id', mainObject.id || '');
};

module.exports = Renderer;
