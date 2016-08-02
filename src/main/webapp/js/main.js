$(document).ready(function() {
	var loadedEntities = {};
	var selectedTable;
	yawp('/entities').list(function(entities) {
		var list = '';
		loadedEntities = {};
		for (var i = 0; i < entities.length; i++) {
			list += SAASAPI.Template.getLi(entities[i]);
			loadedEntities[entities[i].id] = entities[i];
		}
		$('#entities').html(list);
	});

	$('#new-entity').click(function() {
		var name = $('[name="entity-name"]').val();
		if (!name) {
			return;
		}
		yawp('/entities').create({
			name: name
		}).done(function(entity) {
			loadedEntities[entity.id] = entity;
			$('#entities').append(SAASAPI.Template.getLi(entity));
			$('[name="entity-name"]').val('').blur();
		});
	});
	$('#entities').on('click', '.edit-endpoint', function() {
		var li = $(this).parents('li'),
			id = li.data('id');
		li.addClass('active').siblings().removeClass('active');
		selectedTable = loadedEntities[id];
		$('#selected-table').html(li.find('.entity-name').text());
		var columns = selectedTable.properties || [];
		var columnsText = '<tr>';
		for (var i = 0; i < columns.length; i++) {
			columnsText += SAASAPI.Template.getHeaderColumn(columns[i]);
		}
		columnsText += SAASAPI.Template.getHeaderActions() + '</tr>';
		$('#entity-table thead').html(columnsText);

		$.ajax({
			type: 'GET',
			url: '/api/' + selectedTable.name,
			dataType: 'json'
		}).done(function(instances) {
			var table = $('#entity-table tbody');
			var lines = '';
			for (var i = 0; i < instances.length; i++) {
				lines += SAASAPI.Template.newLine(instances[i]);
			}
			table.html(lines);
		});
		$('select').material_select();
	}).on('click', '.remove-endpoint', function() {
		var li = $(this).parents('li');
		yawp(li.data('id')).destroy();
		li.remove();
	});

	$('#entity-table').on('click', '.add-column', function() {
		selectedTable.properties = selectedTable.properties || [];
		selectedTable.properties.push({
			type: $('#column-type').val(),
			name: $('#column-name').val(),
			defaultValue: $('#column-default').val()
		});
		yawp(selectedTable.id).update(selectedTable).done(function(updatedEntity) {
			loadedEntities[selectedTable.id] = updatedEntity;
			$('li[data-id="' + updatedEntity.id + '"] .edit-endpoint').click();
		});
	}).on('click', '.remove-column', function() {
		var th = $(this).parents('th');
		selectedTable.properties.splice(th.index(), 1 );
		yawp(selectedTable.id).update(selectedTable).done(function(updatedEntity) {
			loadedEntities[selectedTable.id] = updatedEntity;
			$('#entity-table').find('tbody tr').find('td:eq(' + th.index() + ')').remove();
			th.remove();
		});

	}).on('click', '.edit-line', function() {
		var header = $('#entity-table thead > tr > th:not(:last-child) span'),
			columnNumber = 0,
			newObject = {},
			tds = $(this).parents('tr').find('td input, td span');
		console.info(tds);
		header.each(function(i) {
			console.info(i);
			console.info(tds.eq(0));
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
		}).done(function() {
			console.info('uhul');
		});
	}).on('click', '.remove-line', function() {
		var tr = $(this).parents('tr');
		$.ajax({
			type: 'DELETE',
			url: '/api/' + selectedTable.name + '/' + tr.find('td:first-child span').text()
		}).done(function() {
			tr.remove();
		})
	}).parents('.panel').on('click', '.add-line', function() {
		$.ajax({
			url: '/api/' + selectedTable.name,
			type: 'POST',
			data: {},
			dataType: 'json'
		}).done(function(instance) {
			console.info(instance);
			$('#entity-table tbody').append(SAASAPI.Template.newLine(instance));
		});
	});
});
