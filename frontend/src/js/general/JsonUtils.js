var JsonUtils = {};

JsonUtils.getJsonFromForm = function (form, settings) {
	var jsonToApply = {};
	form.find('input[name], select[name], textarea[name], div.autocomplete-component[data-name], label[data-name]').each(function () {
		JsonUtils.getJsonFromElement(this, settings, jsonToApply);
	});
	return jsonToApply;
};

JsonUtils.getJsonFromElement = function(element, settings, jsonToApply) {
	settings = settings || {};
	if (!element.name && !$(element).data('name')) {
		return;
	}
	var maskType = $(element).attr('data-mask');
	if ($(element).is('div') || $(element).is('label')) {
		var value = $(element).data('value');

		if (!settings.shouldGetFullObj && typeof value == 'object') {
			value = [];
			for (var i = 0; i < $(element).data('value').length; i++) {
				value.push($(element).data('value')[i].id);
			}
		}
		putValueOnJson(jsonToApply, $(element).data('name'), value);
	} else if ($(element).is(':text')) {
		if (maskType === 'number') {
			putValueOnJson(jsonToApply, element.name, JsonUtils.toNumber($(element).val()));
		} else if (maskType === 'money' || maskType === 'percentage' || maskType === 'money-four-digits') {
			putValueOnJson(jsonToApply, element.name, JsonUtils.toFloat($(element).val()));
		} else {
			putValueOnJson(jsonToApply, element.name, $(element).val());
		}
	} else if ($(element).is('[type="number"]')) {
		putValueOnJson(jsonToApply, element.name, $(element).val());
	} else if ($(element).is('[type="tel"]')) {
		putValueOnJson(jsonToApply, element.name, $(element).val());
	} else if ($(element).is('[type="radio"]')) {
		if ($(element).prop('checked')) {
			putValueOnJson(jsonToApply, element.name, $(element).data('value'));
		}
	} else if ($(element).is('[type="email"]')) {
		putValueOnJson(jsonToApply, element.name, $(element).val());
	} else if ($(element).is('[type="date"]')) {
		putValueOnJson(jsonToApply, element.name, $(element).val());
	} else if ($(element).is(':checkbox')) {
		if ($(element).is(':checked')) {
			putValueOnJson(jsonToApply, element.name, $(element).data('value'));
		}
	} else if ($(element).is('select')) {
		if ($(element).attr('multiple')) {
			var optionsValues = [];
			$(element).find(':selected').each(function () {
				optionsValues.push($(element).val());
			});
			putValueOnJson(jsonToApply, element.name, JSON.stringify(optionsValues));
		} else {
			putValueOnJson(jsonToApply, element.name, $(element).val());
		}
	} else if ($(element).is('textarea')) {
		putValueOnJson(jsonToApply, element.name, $(element).val());
	} else {
		throw 'Incompatible input/select/textarea, check your elements on form';
	}
};

JsonUtils.toNumber = function (val) {
	val = val || '';
	return parseInt(val.replace(/\./g, '') || 0);
};

JsonUtils.toFloat = function (val, decimal) {
	val = val || '';
	var replacedString = val.replace(/\./g, '').replace(',', '.');
	var fixed = (replacedString.indexOf('.') > -1 ? replacedString.split('.')[1].length : 0),
		floatNumber = parseFloat(parseFloat(replacedString || 0).toFixed(fixed));
	return floatNumber;
};

var putValueOnJson = function (jsonObject, path, val) {
	var pathSplit = path.split('.');
	if (typeof jsonObject === 'undefined') {
		jsonObject = {};
	}
	if (pathSplit.length == 1) {
		if (val !== '') {
			jsonObject[path] = val;
		} else {
			delete jsonObject[path];
		}
	} else if (pathSplit.length > 1) {
		var jsonParent = jsonObject[pathSplit[0]];
		var key = pathSplit.shift();
		jsonObject[key] = putValueOnJson(jsonParent, pathSplit.toString().replace(/\,/g, '.'), val);
	}
	return jsonObject;
};

JsonUtils.getValueOnJson = function (jsonObject, path) {
	var pathSplit = path.split('.');
	if (typeof jsonObject === 'undefined') {
		jsonObject = {};
	}
	if (pathSplit.length == 1) {
		return jsonObject[path];
	} else if (pathSplit.length > 1) {
		var jsonParent = jsonObject[pathSplit[0]];
		var key = pathSplit.shift();
		return JsonUtils.getValueOnJson(jsonParent, pathSplit.toString().replace(/\,/g, '.'));
	}
}

module.exports = JsonUtils;
