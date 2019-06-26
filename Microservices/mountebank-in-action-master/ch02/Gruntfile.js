'use strict';

module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-mocha-test');
    grunt.loadNpmTasks('grunt-mountebank');

    grunt.initConfig({
        mochaTest: {
            service: {
                options: {
                    reporter: 'spec'
                },
                src: ['test/**/*.js']
            }
        },
        mb: {
            restart: [],
            stop: []
        }
    });

    grunt.registerTask('test', 'Run the unit tests',
        ['mb:restart', 'try', 'mochaTest:service', 'finally', 'mb:stop', 'checkForErrors']);
    grunt.registerTask('default', ['test']);
};
