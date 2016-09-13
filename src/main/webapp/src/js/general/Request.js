var Config = require('./../config/Config.js');
var Request = {};

var pendingRequests = {};

var timestampCache = new Date().getTime();
var htmlCache = {};

Request.GET_HTML = function (htmlUrl, options) {
	options = options || {};
	if (options.shouldClearOther) {
		Request.clearPendingRequests();
	}
	if (htmlCache[htmlUrl]) {
		return $.Deferred().resolve(htmlCache[htmlUrl]).promise();
	}
	return $.get(Config.basePageUrl + htmlUrl + '.html?_=' + timestampCache).done(function(html) {
		htmlCache[htmlUrl] = html;
	});
};

Request.GET_BUNDLE = function (language) {
	return $.getJSON(Config.baseBundleUrl + '/' + language + '.json?_=' + timestampCache);
};

Request.DELETE = function (url, data) {
	return http('DELETE', url, data);
};

Request.GET = function (url, settings) {
	settings = settings || {};
	if (settings.shouldClearOther) {
		Request.clearPendingRequests(settings.shouldClearOther);
	}
	if (settings.force) {
		return _http('GET', url);
	}

	return http('GET', url, settings.data || {}, settings);
};

Request.POST = function (url, data, settings) {
	return http('POST', url, data, settings);
};

Request.PUT = function (url, data, settings) {
	return http('PUT', url, data, settings);
};

Request.clearPendingRequests = function (requestIdentifier) {
	var requests = pendingRequests[requestIdentifier] || [];
	for (var i = 0; i < requests.length; i++) {
		requests[i].abort();
	}
	pendingRequests[requestIdentifier] = [];
};

Request.clearMocks = function () {
	Request.mockGET = undefined;
	Request.mockPOST = undefined;
	Request.mockPUT = undefined;
	Request.mockDELETE = undefined;
};

var _http = function (type, url, data) {
	var token = '';//LoggedUser.getToken();

	var settings = {
		dataType: 'json',
		statusCode: {},
		type: type,
		url: url,
	};

	if (token) {
		settings.beforeSend = function (xhrObj) {
			xhrObj.setRequestHeader('Authentication', token);
		};
	}

	if (data) {
		settings.data = data;
	}

	return $.ajax(settings);
};

var http = function (type, url, data, settings) {
	if (Request['mock' + type]) {
		return Request['mock' + type](url, data);
	}
	settings = settings || {};
	if (!settings.noauth) {
		url = Config.apiUrl + url;
	}

	var now = (new Date()).getTime();
	var then = (new Date()).getTime(); //FIXME update expire settings
	var deltaToken = now - then;

	if (deltaToken > Config.expirationDelta) {
		// return LoggedUser.renewToken().then(function () {
		// 	return _http(type, url, data);
		// });

	} else {
		var newAjax = _http(type, url, data);
		if (settings.shouldClearOther) {
			if (!pendingRequests[settings.shouldClearOther]) {
				pendingRequests[settings.shouldClearOther] = [];
			}
			pendingRequests[settings.shouldClearOther].push(newAjax);
		}
		return newAjax
	}
};

module.exports = Request;
