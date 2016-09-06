var SAASAPI = SAASAPI || {};
SAASAPI.Template = SAASAPI.Template || {};

SAASAPI.Template.getLi = function(entity) {
	return '<li class="collection-item" data-id="' + entity.id + '">' +
		'<div><span class="entity-name">' + entity.name + '</span>' +
		'<a href="#!" class="secondary-content remove-endpoint"><i class="material-icons">delete_forever</i></a>' +
		'<a href="#!" class="secondary-content edit-endpoint"><i class="material-icons">edit</i></a>' +
		'</div>';
};

SAASAPI.Template.getHeaderColumn = function(column) {
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

SAASAPI.Template.getHeaderActions = function() {
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

SAASAPI.Template.getTableColumn = function(entity, column) {
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

SAASAPI.Template.getTableActions = function() {
	return '<td class="center">'+
				'<a class="btn-floating waves-effect waves-light edit-line"><i class="material-icons">save</i></a>' +
				'<a class="btn-floating waves-effect waves-light remove-line margin-left"><i class="material-icons">delete</i></a>' +
			'</td>';
};

SAASAPI.Template.newLine = function(instance) {
	var newLine = '<tr>',
		header = $('#entity-table thead > tr > th:not(:last-child)');
	header.each(function() {
		newLine += SAASAPI.Template.getTableColumn(instance, $(this).find('span').text());
	});
	newLine += SAASAPI.Template.getTableActions();
	return newLine + '</tr>';
};

window.SAASAPI = SAASAPI;
