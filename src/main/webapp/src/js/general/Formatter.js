require('jquery-mask-plugin');
var Formatter = {};

var maskForType = function (type) {
	switch (type) {
	case 'two-number':
		return ['00', {
			reverse: true
		}];
	case 'number':
		return ['0.000.000', {
			reverse: true
		}];
	case 'money':
		return ['M', {
			translation: {
				'M': {
					pattern: /[0-9 , -]+/g,
					recursive: true
				}
			}
		}];
	case 'money-four-digits':
		return ['M', {
			translation: {
				'M': {
					pattern: /[0-9 , -]+/g,
					recursive: true
				}
			}
		}];
	case 'percentage':
		return ['S999,99', {
			translation: {
				'S': {
					pattern: /-/,
					optional: true
				}
			}
		}];
	case 'cpf':
		return ['000.000.000-00'];
	case 'cnpj':
		return ['00.000.000/0000-00'];
	case 'phone':
		return ['P', {
			translation: {
				'P': {
					pattern: /[0-9- ()+]+/g,
					recursive: true
				}
			}
		}];		
	case 'cep':
		return ['00000-000'];
	case 'card':
		return ['0000 0000 0000 0000'];
	case 'date':
		return ['00/00/0000'];
	case 'month-year':
		return ['00/0000'];
	case 'cnpj':
		return ['00.000.000/0000-00', {
			reverse: true
		}];
	case 'time':
		return ['00:00', {
			reverse: true
		}];
	case 'letters':
		return ['L', {
			translation: {
				'L': {
					pattern: /[À-ÿa-zA-Z _]+/g,
					recursive: true
				}
			}
		}];
	case 'letters-dot':
		return ['L', {
			translation: {
				'L': {
					pattern: /[À-ÿa-zA-Z _\.]+/g,
					recursive: true
				}
			}
		}];
	case 'alpha-numeric':
		return ['A', {
			translation: {
				'A': {
					pattern: /[0-9À-ÿa-zA-Z _]+/g,
					recursive: true
				}
			}
		}];
	case 'numbers-only':
		return ['N', {
			translation: {
				'N': {
					pattern: /[0-9]+/g,
					recursive: true
				}
			}
		}];
	};
};

var functionValidation = function (maskType) {
	switch (maskType) {
	case 'time':
		return function (input) {
			var val = input.val(),
				parts = val.split(':'),
				hours = parts[0],
				minutes = parts[1];
			if (parseInt(hours) > 24) {
				return false;
			}

			if (parseInt(minutes) > 59) {
				return false;
			}

			return true;
		};

	case 'date':
		return function (input) {
			var val = input.val(),
				range = input.data('yearrange') || '-10:+10',
				partsOfRange = range.split(':'),
				yearsBefore = parseInt(partsOfRange[0]),
				yearsAfter = parseInt(partsOfRange[1]),
				parts = val.split('/'),
				day = parts[0],
				month = parts[1],
				year = parts[2],
				monthMaxDays = {
					"01": 31,
					"02": year % 4 == 0 ? 29 : 28,
					"03": 31,
					"04": 30,
					"05": 31,
					"06": 30,
					"07": 31,
					"08": 31,
					"09": 30,
					"10": 31,
					"11": 30,
					"12": 31
				};

			if (parseInt(month) > 12) {
				return false;
			}

			if (parseInt(day) > monthMaxDays[month]) {
				return false;
			}

			var actualYear = new Date().getFullYear();
			if (parseInt(year) < actualYear + yearsBefore || parseInt(year) > actualYear + yearsAfter) {
				return false;
			}

			return true;
		};
	case 'month-year':
		return function (input) {
			var val = input.val(),
				parts = val.split('/'),
				month = parts[0],
				year = parts[1],
				actualYear = new Date().getFullYear();

			if (parseInt(month) > 12) {
				return false;
			}

			if (parseInt(year) > actualYear + 15 || parseInt(year) < actualYear) {
				return false;
			}

			return true;
		};
	};

	return function () {
		return true;
	};
};

var checkValidity = function (input, maskType) {
	var validationFunction = functionValidation(maskType);
	input.data('last-valid-value', input.val());
	input.blur(function () {
		if (!validationFunction(input)) {
			input.val(input.data('last-valid-value'));
			input.trigger('change');
		}

		input.data('last-valid-value', input.val());
	})
};

var moneyEventBlur = function (input) {
	input.blur(function () {
		var valueMoney = input.val();
		if (!valueMoney) {
			input.val('0,00');
		} else if (isNaN(valueMoney.replace(/\./g, '').replace(',', '.')) || valueMoney.split(',')[0].length > 15) {
			input.val('');
			input.attr('placeholder', 'Invalid number');
		} else {
			var indexOfComa = input.val().indexOf(',');
			if (indexOfComa < 0) {
				input.val(input.val() + ',00');
			} else if (indexOfComa == 0) {
				input.val('0' + input.val());
			} else {
				input.val(input.val().substring(0, indexOfComa + 3));
			}
		}
	});
};

var moneyFourDigitsEventBlur = function (input) {
	input.blur(function () {
		var valueMoney = input.val();
		if (!valueMoney) {
			input.val('0,0000');
		} else if (isNaN(valueMoney.replace(/\./g, '').replace(',', '.')) || valueMoney.split(',')[0].length > 15) {
			input.val('');
			input.attr('placeholder', 'Invalid number');
		} else {
			var indexOfComa = input.val().indexOf(',');
			if (indexOfComa < 0) {
				input.val(input.val() + ',0000');
			} else if (indexOfComa == 0) {
				input.val('0' + input.val());
			} else {
				input.val(input.val().substring(0, indexOfComa + 5));
			}
		}
	});
};

Formatter.applyMask = function (inputs) {
	inputs.each(function () {
		var maskType = $(this).data('mask');
		if (maskType) {
			if (maskType === 'money') {
				moneyEventBlur($(this));
			} else if (maskType === 'money-four-digits') {
				moneyFourDigitsEventBlur($(this));
			} else {
				var maskPattern = maskForType(maskType);
				if (maskPattern) {
					var input = $(this);
					input.mask.apply(input, maskPattern);
					checkValidity(input, maskType);
				}
			}
		}
	});
};

Formatter.formatNumber = function (numero) {
	if (isNaN(numero) || typeof numero !== 'number') {
		return undefined;
	}

	var n = '',
		sinal = numero < 0 ? '-' : '';
	numero = Math.abs(numero) + '';
	for (var i = 0; i < numero.length; i++) {
		if (i !== numero.length && i % 3 === 0 && i !== 0) {
			n = '.' + n;
		}
		n = numero.charAt(numero.length - 1 - i) + n;
	}

	return sinal + n;
};

Formatter.formatFloat = function (numero, casasDecimais) {
	if (typeof numero == 'string') {
		numero = require('./JsonUtils.js').toFloat(numero);
	}

	if (isNaN(numero)) {
		return undefined;
	}

	var sinal  = numero < 0 ? '-' : '',
		abs = Math.abs(numero),
		decimal = (abs - Math.floor(abs)).toFixed(casasDecimais || 2).replace(/^0\./, '');
	if (decimal === '1.00') {
		decimal = '00';
		numero++;
	}

	return sinal + Formatter.formatNumber(Math.floor(abs)) + ',' + decimal;
};


Formatter.formatDate = function (date) {
	if (typeof date == 'string') {
		var splitDate = date.split('/');
		date = new Date(splitDate[1] + '/' + splitDate[0] + '/' + splitDate[2]);
	}

	if (!date || date == 'Invalid Date' || typeof date == 'number') {
		return '';
	}

	return Formatter.completeWithZeros(date.getDate()) + '/' + Formatter.completeWithZeros(date.getMonth() + 1) + '/' + Formatter.completeWithZeros(date.getFullYear());
};

Formatter.formatHour = function (date) {
	if (typeof date == 'string') {
		var splitDate = date.split(' '),
			splitHour = splitDate[1].split(':');
		return splitHour[0] + ':' + splitHour[1];
	}

	if (!date || date == 'Invalid Date' || typeof date == 'number') {
		return '';
	}

	return Formatter.completeWithZeros(date.getHours()) + ':' + Formatter.completeWithZeros(date.getMinutes());
};


Formatter.formatHeaderDate = function (date, datepickerBundle) {
	var dayOfWeek = datepickerBundle.dayNames[date.getDay()],
		preposition = datepickerBundle.preposition,
		dayPosFixed = datepickerBundle.dayPosFixed,
		month = datepickerBundle.monthNames[date.getMonth()],
		year = date.getFullYear(),
		day = date.getDate();

	return dayOfWeek + dayPosFixed + ', ' + day + ' ' + preposition + ' ' + month + ' ' + preposition + ' ' + year;
};

Formatter.completeWithZeros = function (stringToComplete, length) {
	length = length || 2;
	var result = stringToComplete + '';
	while (result.length < length) {
		result = '0' + result;
	}

	return result;
};

module.exports = Formatter;