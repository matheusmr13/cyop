module.exports = function(grunt) {

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		uglify: {
			all_src: {
				options: {
					sourceMap: true,
					sourceMapName: 'sourceMap.map'
				},
				src: ['src/main/webapp/js/external/jquery-3.0.0.min.js','src/main/webapp/js/external/*.js', 'src/main/webapp/js/general/*.js', 'src/main/webapp/js/**/*.js'],
				dest: 'src/main/webapp/target/composite.all.min.js'
			}
		},
		watch: {
			css: {
				files: 'src/main/webapp/js/**/*.js',
				tasks: ['compile'],
				options: {
					livereload: true,
				},
			},
		}
	});

	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-watch');

	// Default task(s).
	grunt.registerTask('default', ['uglify']);
	grunt.registerTask('compile', ['uglify']);

};
