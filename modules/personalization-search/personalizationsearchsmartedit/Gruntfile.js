/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
/* jshint esversion: 6 */
module.exports = (grunt) => {

    require('time-grunt')(grunt);
    require('./smartedit-build')(grunt).load();

    // -------------------------------------------------------------------------------------------------
    // FILE GENERATION
    grunt.registerTask('generate', [
        'generateTsConfig'
    ]);

    // -------------------------------------------------------------------------------------------------
    // Beautify
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('sanitize', ['jsbeautifier', 'tsformatter']);


    // webpack
    grunt.registerTask('webpackDev', ['webpack:devSmartedit', 'webpack:devSmarteditContainer']);
    grunt.registerTask('webpackProd', ['webpack:prodSmartedit', 'webpack:prodSmarteditContainer']);

    // -------------------------------------------------------------------------------------------------
    // Linting
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('linting', ['jshint', 'tslint']);

    grunt.registerTask('sanitize', ['jsbeautifier', 'tsformatter']);

    grunt.registerTask('compile_only', ['sanitize', 'linting', 'copy:sources', 'multiNGTemplates', 'checkNoForbiddenNameSpaces', 'checkI18nKeysCompliancy', 'checkNoFocus', 'styling_only']);
    grunt.registerTask('compile', ['clean:target', 'compile_only']);

    grunt.registerTask('multiKarma', ['karma:personalizationsearchsmartedit', 'karma:personalizationsearchsmarteditContainer']);

    grunt.registerTask('test_only', ['generate', 'multiKarma']);
    grunt.registerTask('test', ['generate', 'compile', 'instrumentSeInjectable', 'multiKarma']);

    grunt.registerTask('test', 'run unit tests', function() {
        let unitTaskName = 'multiKarma';
        if (grunt.option('target') === 'inner') {
            unitTaskName = 'karma:personalizationsearchsmartedit';
        } else if (grunt.option('target') === 'outer') {
            unitTaskName = 'karma:personalizationsearchsmarteditContainer';
        }
        if (grunt.option('browser') && !/^(inner|outer|commons)$/.test(grunt.option('target'))) {
            grunt.fail.fatal('Please set --target=outer, --target=inner or --target=commons');
        }
        grunt.task.run(['generate', 'compile', 'instrumentSeInjectable', unitTaskName]);
    });
    grunt.registerTask('coverage', 'run unit tests with coverage report', () => {
        grunt.option('coverage', true);
        grunt.task.run(['generate', 'multiKarma', 'connect:coverage']);
    });

    grunt.registerTask('concatAndPushDev', ['instrumentSeInjectable', 'webpackDev', 'copy:dev']);
    grunt.registerTask('concatAndPushProd', ['instrumentSeInjectable', 'webpackProd', 'copy:dev']);

    grunt.registerTask('dev', ['compile', 'concatAndPushDev']);

    grunt.registerTask('package_only', ['concatAndPushProd', 'ngdocs']);
    grunt.registerTask('package', ['test', 'package_only']);
    grunt.registerTask('packageSkipTests', ['generate', 'compile_only', 'package_only']);

    grunt.registerTask('e2e', ['connect:dummystorefront', 'connect:test', 'multiProtractor']);
    grunt.registerTask('e2e_max', ['connect:dummystorefront', 'connect:test', 'multiProtractorMax']);
    grunt.registerTask('e2e_dev', 'e2e local development mode', () => {
        grunt.option('keepalive_dummystorefront', true);
        grunt.option('open_browser', true);
        grunt.task.run(['connect:test', 'connect:dummystorefront']);
    });
    grunt.registerTask('e2e_debug', 'e2e local debug mode', () => {
        grunt.option('browser_debug', true);
        grunt.task.run('e2e');
    });
    grunt.registerTask('verify_only', ['e2e']);
    grunt.registerTask('verify', ['generate', 'package', 'verify_only']);
    grunt.registerTask('verify_max', ['generate', 'package', 'e2e_max']);

    grunt.registerTask('styling_only', ['less']);
    grunt.registerTask('styling', ['clean', 'styling_only']);

};
