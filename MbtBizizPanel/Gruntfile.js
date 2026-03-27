module.exports = function (grunt) {
    grunt.initConfig({
        uglify: {
            options: { mangle: true },
            scripts: {
                src: 'resources/assets/js/mbt-biziz.js',
                dest: 'public/js/mbt-biziz.js'
            }
        },

        // ✅ Copy + minify CSS
        cssmin: {
            target: {
                files: [{
                    expand: true,
                    cwd: 'resources/assets/css/',
                    src: ['*.css'],
                    dest: 'public/css/',
                    ext: '.css'
                }]
            }
        }
    });
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    // ✅ Add cssmin into the pipeline
    grunt.registerTask('prepare', ['uglify', 'cssmin']);

};