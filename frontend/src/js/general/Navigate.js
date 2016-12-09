var History = require('./../libs/History.js');
var Config = require('./../config/Config');
var Navigate = {};

Navigate.to = function(path, internalCall) {
	var pathSplit = path.split('?'),
		filePath = pathSplit[0].charAt(pathSplit[0].length - 1) === '/' ? pathSplit[0].substring(0, pathSplit[0].length - 1) : pathSplit[0],
		params = extractParameters(pathSplit[1]),
		hashParts = filePath.split('/'),
		requirePath = filePath.substring(1, filePath.lastIndexOf('/') || filePath.length),
		object;

	try {
		object = require('./../pages/' + requirePath + '.js');
	} catch(e) {
		Navigate.to(Config.defaultPage);
		return;
	}

	if (!windowTarget(filePath, params)) {
		return false;
	}

	if (!internalCall) {
		Navigate.replaceParams(params);
	}

	mixpanel.track('Page ' + requirePath, {
		'PARAMS': pathSplit[1]
	});
	if (hashParts.length > 2 && hashParts[2]) {
		object[hashParts[hashParts.length - 1]](params);
	} else {
		object['init'](params);
	}

	return true;
};

Navigate.initialize = function() {
	var proccessStateChange = function() {
		var State = History.getState();
		Navigate.to(State.hash, true);
	};

	History.Adapter.bind(window, 'statechange', function(a, b, c, d) {
		proccessStateChange();
	});
	proccessStateChange();
	changeLinkEvent();
};

Navigate.removeParams = function() {
	window.history.replaceState({}, 'Removing params', window.location.pathname);
};

Navigate.removeParam = function(param) {
	Navigate.addParam(param, '');
};

Navigate.addParam = function(key, value) {
	var split = window.location.href.split('?'),
		parameters = (split.length == 2 ? extractParameters(split[1]) : {});
	if (value) {
		parameters[key] = value;
	} else {
		delete parameters[key];
	}
	window.history.replaceState(parameters, 'Changing params', window.location.pathname + (Object.isEmpty(parameters) ?'' : '?' + $.param(parameters)));
};

Navigate.replaceParams = function(params) {
	window.history.replaceState({}, 'Adding params', window.location.pathname + (Object.isEmpty(params) ?'' : '?' + $.param(params)));
};

Navigate.extractParameter = function(paramName) {
	var split = window.location.href.split('?');
	return split.length == 2 ? extractParameters(split[1])[paramName] : '';
};

Navigate.toLocation = function(path) {
	Navigate.to(path, true);
	window.history.pushState({}, 'Navigating', path);
};

Navigate.refresh = function() {
	window.location.reload();
};

Navigate.back = function() {
	window.history.back();
};

var windowTarget = function(filePath, params) {

	var target = params["window-target"];

	if (target) {

		delete params["window-target"];

		var urlParameters  = toUrlParameters(params);
		var urlWithoutTarget = filePath + "?" + urlParameters;

		window.open(urlWithoutTarget, target);

		return false;
	}

	return true;
}

var toUrlParameters = function(parameters) {

	var urlParameters = "";

	for (var key in parameters) {
	  if (parameters.hasOwnProperty(key)) {
			urlParameters += key + "=" + parameters[key] + "&";
	  }
	}

	return urlParameters;
};

var extractParameters = function(paramsStr) {
	if (!paramsStr) {
		return {};
	}

	var paramsWithNames = paramsStr.split('&'),
		params = {};
	for (var i = 0; i < paramsWithNames.length; i++) {
		var param = paramsWithNames[i].split('=');
		params[param[0]] = param[1];
	}

	return params;
};

var changeLinkEvent = function() {
	$(document).on('click', 'a, [data-href]', function(e) {
		var href = $(this).attr('href') || $(this).data('href');
		if (!href) {
			return;
		}
		var indexOfHash = href.indexOf('#');
		if (indexOfHash > -1) {
			href = href.substring(0, indexOfHash);
		}
		var externalUrls = new RegExp('^(?:[a-z]+:)?//', 'i');
		if (!href || externalUrls.test(href) || href.indexOf('mailto:') === 0) {
			return false;
		}
		if (!e.ctrlKey) {
			if (Navigate.to(href)) {
				history.pushState({}, '', href);
			}
		} else {
			window.open(href, '_blank');
		}

		e.preventDefault();
	});
};

module.exports = Navigate;
