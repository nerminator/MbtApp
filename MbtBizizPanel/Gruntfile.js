module.exports = function (grunt) {
    grunt.initConfig({
        uglify: {
            options: {
                mangle: true
            },
            scripts: {
                src: 'resources/assets/js/mbt-biziz.js',
                dest: 'public/js/mbt-biziz.js'
            }
        }
    });
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.registerTask('prepare', ['uglify']);

};