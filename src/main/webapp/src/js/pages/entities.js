var Entitie = require('./../objs/Entitie.js');

var Entities = {};
var loadedEntities = {};
var selectedTable;
var getLi = function(entity) {
	return '<li class="collection-item" data-id="' + entity.id + '">' +
		'<div><span class="entity-name">' + entity.name + '</span>' +
		'<a href="#!" class="secondary-content remove-endpoint"><i class="material-icons">delete_forever</i></a>' +
		'<a href="#!" class="secondary-content edit-endpoint"><i class="material-icons">edit</i></a>' +
		'</div>';
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

var getTableColumn = function(entity, column) {
	var isId = (column == 'id'),
		elementId = (entity.id + column),
		hasValue = entity[column];
	return '<td>' +
			'<div class="row">' +
				'<div class="input-field col s12">' +
					( !isId ?
						'<input id="' + elementId +'" type="text" value="' + (entity[column] || '') + '" />' +
						'<label ' + (hasValue ? 'class="active"' : '') + 'for="' + elementId +'">' + column + '</label>'
						:
						'<span>' + entity[column] + '</span>'
					) +
				'</div>' +
			'</div>' +
		'</td>';
	return '<td><input value="' + (entity[column] || '') + '" ' + (column == 'id' ? 'disabled="disabled"' : '') + '"/></td>';
};

var getTableActions = function() {
	return '<td class="center">'+
				'<a class="btn-floating waves-effect waves-light edit-line"><i class="material-icons">save</i></a>' +
				'<a class="btn-floating waves-effect waves-light remove-line margin-left"><i class="material-icons">delete</i></a>' +
			'</td>';
};

var newLine = function(instance) {
	var newLine = '<tr>',
		header = $('#entity-table thead > tr > th:not(:last-child)');
	header.each(function() {
		newLine += getTableColumn(instance, $(this).find('span').text());
	});
	newLine += getTableActions();
	return newLine + '</tr>';
};

var newEntityClick = function() {
	var name = $('[name="entity-name"]').val();
	if (!name) {
		return;
	}
	new Entitie({
		name
	}).save().then(function(entity) {
		loadedEntities[entity.id] = entity;
		$('#entities').append(getLi(entity));
		$('[name="entity-name"]').val('').blur();
	});
};

var editEndpoint = function(button) {
	var li = button.parents('li'),
		id = li.data('id');
	li.addClass('active').siblings().removeClass('active');
	selectedTable = loadedEntities[id];
	$('#selected-table').html(li.find('.entity-name').text());
	var columns = selectedTable.properties || [];
	var columnsText = '<tr>';
	for (var i = 0; i < columns.length; i++) {
		columnsText += getHeaderColumn(columns[i]);
	}
	columnsText += getHeaderActions() + '</tr>';
	$('#entity-table thead').html(columnsText);

	$.ajax({
		type: 'GET',
		url: '/api/' + selectedTable.name,
		dataType: 'json'
	}).then(function(instances) {
		var table = $('#entity-table tbody');
		var lines = '';
		for (var i = 0; i < instances.length; i++) {
			lines += newLine(instances[i]);
		}
		table.html(lines);
	});
	$('select').material_select();
};

var removeEndpoint = function() {
	var li = $(this).parents('li');
	yawp(li.data('id')).destroy();
	li.remove();
};

var addColumn = function() {
	selectedTable.properties = selectedTable.properties || [];
	selectedTable.properties.push({
		type: $('#column-type').val(),
		name: $('#column-name').val(),
		defaultValue: $('#column-default').val()
	});
	yawp(selectedTable.id).update(selectedTable).then(function(updatedEntity) {
		loadedEntities[selectedTable.id] = updatedEntity;
		$('li[data-id="' + updatedEntity.id + '"] .edit-endpoint').click();
	});
};

var removeColumn = function(button) {
	var th = button.parents('th');
	selectedTable.properties.splice(th.index(), 1 );
	yawp(selectedTable.id).update(selectedTable).then(function(updatedEntity) {
		loadedEntities[selectedTable.id] = updatedEntity;
		$('#entity-table').find('tbody tr').find('td:eq(' + th.index() + ')').remove();
		th.remove();
	});
};

var editLine = function(button) {
	var header = $('#entity-table thead > tr > th:not(:last-child) span'),
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
		url: '/api/' + selectedTable.name + '/' + newObject.id,
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
		url: '/api/' + selectedTable.name + '/' + tr.find('td:first-child span').text()
	}).then(function() {
		tr.remove();
	});
};

var addNewLine = function() {
	$.ajax({
		url: '/api/' + selectedTable.name,
		type: 'POST',
		data: {},
		dataType: 'json'
	}).then(function(instance) {
		$('#entity-table tbody').append(newLine(instance));
	});
};
Entities.init = function(params) {
	Entitie.list().then(function(entities) {
		var list = '';
		loadedEntities = {};
		for (var i = 0; i < entities.length; i++) {
			list += getLi(entities[i]);
			loadedEntities[entities[i].id] = entities[i];
		}
		$('#entities').html(list);
	});

	$('#new-entity').click(function() {
		newEntityClick();
	});
	$('#entities').on('click', '.edit-endpoint', function() {
		editEndpoint($(this));
	}).on('click', '.remove-endpoint', function() {
		removeEndpoint($(this));
	});

	$('#entity-table').on('click', '.add-column', function() {
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
};

module.exports = Entities;
