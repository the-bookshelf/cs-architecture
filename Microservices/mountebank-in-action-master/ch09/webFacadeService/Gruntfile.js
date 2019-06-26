'use strict';

module.exports = function (grunt) {
  grunt.loadNpmTasks('grunt-mocha-test');
  grunt.loadNpmTasks('grunt-mountebank');

  grunt.initConfig({
    mochaTest: {
      unit: {
        options: {
          reporter: 'spec'
        },
        src: ['unitTest/**/*.js']
      },
      service: {
        options: {
          reporter: 'spec',
          path: 'node_modules/mountebank/bin/mb',
          pathEnvironmentVariable: 'MB_EXECUTABLE'
        },
        src: ['serviceTest/**/*.js']
      },
      contract: {
        options: {
          reporter: 'spec'
        },
        src: ['contractTest/**/*.js']
      }
    },
    mb: {
      restart: [],
      stop: []
    }
  });

  grunt.registerTask('test:unit', 'Run the unit tests', ['mochaTest:unit']);

  // Expects Web Facade to be running ('npm start')
  grunt.registerTask('test:service', 'Run the service tests',
    ['mb:restart', 'try', 'mochaTest:service', 'finally', 'mb:stop', 'checkForErrors']);

  // Expects Product service to be running
  grunt.registerTask('test:contract', 'Run the service tests', ['mochaTest:contract']);

  grunt.registerTask('default', ['test:unit']);
};
