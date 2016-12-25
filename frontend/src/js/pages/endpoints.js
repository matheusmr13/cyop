var Endpoint = require('./../objs/Endpoint.js');
var Renderer = require('./../general/Renderer.js');

var Endpoints = {};
var loadedEndpoints = {};
var selectedTable;
var getLi = function(endpoint) {
	return '<li class="collection-item" data-id="' + endpoint.id + '">' +
		'<div><span class="endpoint-name">' + endpoint.url + '</span>' +
		'<a href="#!" class="secondary-content remove-endpoint"><i class="material-icons">delete_forever</i></a>' +
		'<a href="#!" class="secondary-content edit-endpoint"><i class="material-icons">edit</i></a>' +
		'</div></li>';
};

var getHeaderColumn = function(column) {
	var isId = (column.name == 'id');
	return '<th>' +
				'<div class="row">' +
					'<span>' + column.name + '</span>' +
					(!isId ?
						'<a class="btn-floating-small waves-effect waves-light remove-column margin-left"><i class="material-icons">delete</i></a>'
						:
						''
					) +
				'</div>' +
			'</th>';
};

var getHeaderActions = function() {
	return '<th>' +
			'<div class="row new-column">' +
				'<div class="input-field col s5">' +
					'<input id="column-name" type="text" class="validate"/>' +
					'<label for="column-name">Column Name</label>' +
				'</div>' +
				'<div class="input-field col s5">'+
					'<select id="column-type">'+
						'<option value="INTEGER">Integer</option>' +
						'<option value="STRING">String</option>' +
						'<option value="TEXT">Text</option>' +
						'<option value="DECIMAL">Decimal</option>' +
						'<option value="BOOLEAN">Boolean</option>' +
					'</select>'+
					'<label>Materialize Select</label>'+
				'</div>' +
				'<div class="input-field col s2">' +
					'<a class="btn-floating waves-effect waves-light add-column"><i class="material-icons">add</i></a>' +
				'</div>'+
			'</div>' +
		'</th>';
};

var getTableColumn = function(endpoint, column) {
	var isId = (column == 'id'),
		elementId = (endpoint.id + column),
		hasValue = endpoint[column];
	return '<td>' +
			'<div class="row">' +
				'<div class="input-field col s12">' +
					( !isId ?
						'<input id="' + elementId +'" type="text" value="' + (endpoint[column] || '') + '" />' +
						'<label ' + (hasValue ? 'class="active"' : '') + 'for="' + elementId +'">' + column + '</label>'
						:
						'<span>' + endpoint[column] + '</span>'
					) +
				'</div>' +
			'</div>' +
		'</td>';
};

var getTableActions = function() {
	return '<td class="center">'+
				'<a class="btn-floating waves-effect waves-light edit-line"><i class="material-icons">save</i></a>' +
				'<a class="btn-floating waves-effect waves-light remove-line margin-left"><i class="material-icons">delete</i></a>' +
			'</td>';
};

var newLine = function(instance) {
	var newLine = '<tr>',
		header = $('#endpoint-table thead > tr > th:not(:last-child)');
	header.each(function() {
		newLine += getTableColumn(instance, $(this).find('span').text());
	});
	newLine += getTableActions();
	return newLine + '</tr>';
};

var newEndpointClick = function() {
	var url = $('[name="endpoint-name"]').val();
	if (!url) {
		return;
	}
	new Endpoint({
		url
	}).save().then(function(endpoint) {
		loadedEndpoints[endpoint.id] = endpoint;
		$('#endpoints').append(getLi(endpoint));
		$('[name="endpoint-name"]').val('').blur();
	});
};

var editEndpoint = function(button) {
	var li = button.parents('li'),
		id = li.data('id');
	li.addClass('active').siblings().removeClass('active');
	selectedTable = loadedEndpoints[id];
	$('#selected-table').html(li.find('.endpoint-name').text());
	var columns = selectedTable.properties || [];
	var columnsText = '<tr>';
	for (var i = 0; i < columns.length; i++) {
		columnsText += getHeaderColumn(columns[i]);
	}
	columnsText += getHeaderActions() + '</tr>';
	$('#endpoint-table thead').html(columnsText);

	$.ajax({
		type: 'GET',
		url: '/api/v1/' + selectedTable.url,
		dataType: 'json'
	}).then(function(instances) {
		var table = $('#endpoint-table tbody');
		var lines = '';
		for (var i = 0; i < instances.length; i++) {
			lines += newLine(instances[i]);
		}
		table.html(lines);
	});
	$('select').material_select();
};

var removeEndpoint = function(li) {
	var id = li.data('id');
	yawp(id).destroy();
	li.remove();
};

var addColumn = function() {
	selectedTable.properties = selectedTable.properties || [];
	selectedTable.properties.push({
		type: $('#column-type').val(),
		name: $('#column-name').val(),
		defaultValue: $('#column-default').val()
	});
	yawp(selectedTable.id).update(selectedTable).then(function(updatedEndpoint) {
		loadedEndpoints[selectedTable.id] = updatedEndpoint;
		$('li[data-id="' + updatedEndpoint.id + '"] .edit-endpoint').click();
	});
};

var removeColumn = function(button) {
	var th = button.parents('th');
	selectedTable.properties.splice(th.index(), 1 );
	yawp(selectedTable.id).update(selectedTable).then(function(updatedEndpoint) {
		loadedEndpoints[selectedTable.id] = updatedEndpoint;
		$('#endpoint-table').find('tbody tr').find('td:eq(' + th.index() + ')').remove();
		th.remove();
	});
};

var editLine = function(button) {
	var header = $('#endpoint-table thead > tr > th:not(:last-child) span'),
		columnNumber = 0,
		newObject = {},
		tds = button.parents('tr').find('td input, td span');
	header.each(function(i) {
		if (!i) {
			newObject.id = tds.eq(columnNumber++).text();
		} else {
			newObject[$(this).text()] = tds.eq(columnNumber++).val();
		}
	});

	$.ajax({
		type: 'PUT',
		url: '/api/v1/' + selectedTable.url + '/' + newObject.id,
		dataType: 'json',
		data: {
			instance: JSON.stringify(newObject)
		}
	}).then(function() {
	});
};

var removeLine = function(button) {
	var tr = button.parents('tr');

	$.ajax({
		type: 'DELETE',
		url: '/api/v1/' + selectedTable.url + '/' + tr.find('td:first-child span').text()
	}).then(function() {
		tr.remove();
	});
};

var addNewLine = function() {
	$.ajax({
		url: '/api/v1/' + selectedTable.url,
		type: 'POST',
		data: {},
		dataType: 'json'
	}).then(function(instance) {
		$('#endpoint-table tbody').append(newLine(instance));
	});
};
Endpoints.init = function(params) {
	var requests = [Renderer.requestPage('/endpoints/endpoint-list-item', 'endpointListItem'),
				Renderer.yawpRequest(Endpoint.list(), 'endpoints')];
	loadedEndpoints = {};
	Renderer.openNewPage('/endpoints/endpoints-home', requests).then(function(result) {
		var endpoints = result.it.endpoints;
		for (var i = 0; i < endpoints.length; i++) {
			loadedEndpoints[endpoints[i].id] = endpoints[i];
		}

		$('#new-endpoint').click(function() {
			newEndpointClick();
		});
		$('#endpoints').on('click', '.edit-endpoint', function() {
			editEndpoint($(this));
		}).on('click', '.remove-endpoint', function() {
			removeEndpoint($(this).parents('li'));
		});

		$('#endpoint-table').on('click', '.add-column', function() {
			addColumn();
		}).on('click', '.remove-column', function() {
			removeColumn($(this));
		}).on('click', '.edit-line', function() {
			editLine($(this));
		}).on('click', '.remove-line', function() {
			removeLine($(this));
		}).parents('.panel').on('click', '.add-line', function() {
			addNewLine();
		});
	});
};

module.exports = Endpoints;
