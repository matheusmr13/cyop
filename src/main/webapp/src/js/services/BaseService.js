var Request = require('./../general/Request.js');
var JsonUtils = require('./../general/JsonUtils.js');

var BaseService = {};

BaseService.checkForm = function(form, inputsContainer) {
	var d = $.Deferred();

	if (!form[0].checkValidity()) {
		var submitButton = form.find(':submit');
		if (submitButton.size() == 0) {
			submitButton = $('<input type="submit" style="display: none"/>');
			form.append(submitButton);
		}

		submitButton.click();
		d.reject();
		return d.promise();
	}
	var jsonForm = JsonUtils.getJsonFromForm(inputsContainer ? form.find(inputsContainer) : form);
	d.resolve(jsonForm);
	return d.promise();
};

BaseService.saveEntity = function(entityService, entity, settings) {
	if (entity.id) {
		return BaseService.updateEntity(entityService, entity, settings);
	} else {
		return BaseService.createEntity(entityService, entity, settings);
	}
};

BaseService.createEntity = function(entityService, entity, settings) {
	return BaseService.POST(entityService, entity, settings);
};

BaseService.updateEntity = function(entityService, entity, settings) {
	return BaseService.PUT(entityService + '/' + entity.id, entity, settings);
};

BaseService.DELETE = function(url, data, settings) {
	return processPossibleErrors(Request.DELETE(url, data), settings);
};

BaseService.GET = function(url, settings) {
	return processPossibleErrors(Request.GET(url, settings), settings);
};

BaseService.POST = function(url, data, settings) {
	return processPossibleErrors(Request.POST(url, data, settings), settings);
};

BaseService.PUT = function(url, data, settings) {
	return processPossibleErrors(Request.PUT(url, data, settings), settings);
};

BaseService.GET_HTML = function(htmlUrl) {
	return processPossibleErrors(Request.GET_HTML(htmlUrl));
};

BaseService.GET_BUNDLE = function(language) {
	return processPossibleErrors(Request.GET_BUNDLE(language));
};

var processPossibleErrors = function(ajax, settings) {
	settings = settings || {};
	var d = $.Deferred();
	ajax.done(function(serverResponse) {
		d.resolve(serverResponse);
	}).fail(function(xhrError) {
		if (xhrError.status === 0 || xhrError.readyState === 0) {
			return;
		}
		var messageCode;
		if (xhrError.status === 401) {
			messageCode = 'should-logout';
		} else if (xhrError.status === 404) {
			messageCode = 'default.not-found';
		} else if (xhrError.status === 412) {
			messageCode = 'default.not-authorized';
		} else if (xhrError.responseText) {
			var serverErrorObj = JSON.parse(xhrError.responseText);
			messageCode = serverErrorObj.messageCode;
		} else {
			messageCode = 'default.default-error-message';
		}
		if (!settings.notShowModal) {
			alert(messageCode);
		}
		d.reject(serverErrorObj);
	});
	return d.promise();
};

module.exports = BaseService;
