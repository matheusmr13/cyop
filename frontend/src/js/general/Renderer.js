var JsonUtils = require('./../general/JsonUtils.js');
var BaseService = require('./../services/BaseService.js');
var Formatter = require('./Formatter.js');
var doT = require('dot');
var Loader = require('./Loader.js');
var Renderer = {};

var ObjectTypes = {
	OBJECT: 'OBJECT',
	PAGE: 'PAGE',
	YAWP: 'YAWP'
};

Renderer.requestObj = function (url, objectName, settings) {
	return {
		url: url,
		pageObject: objectName,
		type: ObjectTypes.OBJECT
	};
};

Renderer.requestPage = function (url, pageName) {
	return {
		url: url,
		pageObject: pageName,
		type: ObjectTypes.PAGE
	};
};

Renderer.yawpRequest = function (deferred, objectName) {
	return {
		deferred,
		pageObject: objectName,
		type: ObjectTypes.YAWP
	};
};

Renderer.showPage = function (pageUrl, settings) {
	return Renderer.openNewPage(pageUrl, null, settings);
};

Renderer.populatePage = function (pageUrl, data, settings) {
	settings.injectElements = settings.injectElements || {};
	for (var propertie in data) {
		settings.injectElements[propertie] = data[propertie];
	}
	return Renderer.openNewPage(pageUrl, null, settings);
};

Renderer.openNewPage = function (pageUrl, dataRequests, settings) {
	settings = settings || {};
	return new Promise(function(resolve, reject){
		var container = getMainContainer(settings),
			mainObject = settings.mainObject;
		Loader.pushLoad(container);

		var requests = [BaseService.GET_HTML(pageUrl)];
		if (dataRequests) {
			requests = requests.concat(getRequestPromises(dataRequests));
		}

		Promise.all(requests).then(function () {
			var returnObj = arguments[0],
				page = returnObj[0],
				objectToDot = createItObject(returnObj, dataRequests, settings);
			
			container.html(doT.template(page)(objectToDot));

			if (!settings.doNotChangeDOM) {
				changeDOMIfNecessary(container, settings, objectToDot[mainObject]);
			}
			resolve({
				container: container,
				it: objectToDot
			});
		}).catch(function (e) {
			Loader.popLoad(container);
			reject();
		});
	});
};

var getRequestPromises = function(dataRequests) {
	var requests = [];
	for (var i = 0; i < dataRequests.length; i++) {
		if (dataRequests[i].type == ObjectTypes.PAGE) {
			requests.push(BaseService.GET_HTML(dataRequests[i].url));
		} else if (dataRequests[i].type == ObjectTypes.OBJECT) {
			requests.push(BaseService.GET(dataRequests[i].url, dataUrls[i].settings ));
		} else if (dataRequests[i].type == ObjectTypes.YAWP) {
			requests.push(dataRequests[i].deferred);
		}
	}
	return requests;
};

var createItObject = function (requestReturns, dataRequests, settings) {
	var objectToDot = {};
	if (dataRequests && dataRequests.length) {
		for (var i = 1; i < requestReturns.length; i++) {
			if (dataRequests[i - 1].type == ObjectTypes.PAGE) {
				objectToDot[dataRequests[i - 1].pageObject + 'Template'] = doT.template(requestReturns[i]);
			} else {
				objectToDot[dataRequests[i - 1].pageObject] = requestReturns[i];
			}
		}
	}

	if (settings.injectObjects) {
		for (var propertie in settings.injectObjects) {
			objectToDot[propertie] = settings.injectObjects[propertie];
		}
	}

	return objectToDot;
};

var getMainContainer = function (settings) {
	if (settings) {
		return settings.containerJquery || $(settings.container || '#content');
	}
	return $('#content');
};

var changeDOMIfNecessary = function (container, settings, mainObject) {
	if (!settings.container && !settings.containerJquery) {
		$('body').scrollTop(0);
	}

	if (mainObject) {
		Renderer.populateTemplateWithObject(container, mainObject);
	}

	if (!settings.shouldNotApplyMask) {
		Formatter.applyMask(container.find('input'));
	}
};

Renderer.populateTemplateWithObject = function (container, mainObject) {
	container.find('[name], [data-name]').each(function (a) {
		var value = JsonUtils.getValueOnJson(mainObject, $(this).prop('name') || $(this).data('name'));
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
