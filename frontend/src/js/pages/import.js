var ImportJson = require('./../objs/ImportJson.js');
var Renderer = require('./../general/Renderer.js');
var ImportJsons = {};

var newVersionClick = function() {
	var json = $('#json').val();
	if (!json) {
		return;
	}
	new ImportJson({
		json: JSON.parse(json)
	}).save().then(function(importjson) {
		//Show created urls
	});
};

var prettyJson = function() {
	return JSON.stringify({
				"endpoints": [{
					"url": "person",
					"properties": [{
						"name": "name",
						"type": "text"
						}, {
						"name": "age",
						"type": "integer"
						}, {
						"name": "mother",
						"type": "endpoint",
						"referer": "person"
						}, {
						"name": "father",
						"type": "endpoint",
						"referer": "person"
						}, {
						"name": "actual_job",
						"type": "enumerator",
						"referer": "job"
						}]
				}],
				"enumerators": [{
					"name": "job",
					"values": ["teacher", "cientist", "officer", "developer"]
				}],
				"api_config": {
					"version_url": Math.floor(Math.random()*1000000000) + ""
				}
			}, undefined, 4);
};
ImportJsons.init = function(params) {
	Renderer.openNewPage('/importjson/importjson', []).then(function(result) {
		$('#json').val(prettyJson());
		$('#new-version').click(function() {
			newVersionClick();
		});
	});
};

module.exports = ImportJsons;
