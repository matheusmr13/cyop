var DateUtils = {};

DateUtils.dateInputToDate = function (input) {
	var inputValue = input.val(),
		parts = inputValue.split('/'),
		year = parts[2],
		month = parts[1],
		day = parts[0];
	return new Date(year, parseInt(month) - 1, day);
};


DateUtils.formattedStringToDate = function (string) {
	var inputValue = string;
	if (!inputValue) {
		return '';
	}

	var splitDate = inputValue.split(' ');
	if (splitDate.length > 1) {
		inputValue = splitDate[0];
	}
	var parts = inputValue.split('/'),
		year = parts[2],
		month = parts[1],
		day = parts[0];
	return new Date(year, parseInt(month) - 1, day);
};

DateUtils.formattedStringToDateTime = function (string /* dd/MM/yyyy HH:mm:ss */) {
	var date = DateUtils.formattedStringToDate(string),
		time = string.split(' ')[1].split(':'),
		hour = parseInt(time[0]),
		minute = parseInt(time[1]),
		second = parseInt(time[2]);

	date.setHours(hour);
	date.setMinutes(minute);
	date.setSeconds(second);

	return date;
};

DateUtils.dateTimeToFormattedString = function (date) { /* dd/MM/yyyy HH:mm */
	return (date.getDate() < 10 ? '0' : '') + date.getDate() + '/' + (date.getMonth() + 1 < 10 ? '0' : '') + (date.getMonth() + 1) + '/' + date.getFullYear() + ' ' +
		 (date.getHours() < 10 ? '0' : '') + date.getHours() + ':' + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
}

DateUtils.dateTimeToFormattedStringToSave = function (date) { /* dd/MM/yyyy HH:mm */
	return DateUtils.dateTimeToFormattedString(date) + ':' + (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
}

DateUtils.plusDays = function (date, days) {
	var newDate = new Date(date.getTime());
	newDate.setDate(date.getDate() + days);
	return newDate;
};

DateUtils.plusMonths = function (date, months) {
	var newDate = new Date(date.getTime());
	newDate.setMonth(date.getMonth() + months);
	return newDate;
};

DateUtils.daysBetween = function (dateBegin, dateEnd) {
	var timeDiff = Math.abs(dateBegin.getTime() - dateEnd.getTime());
	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
	return diffDays;
};

module.exports = DateUtils;
