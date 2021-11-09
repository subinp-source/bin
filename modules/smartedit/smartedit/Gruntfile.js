/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint esversion: 6 */
module.exports = (grunt) => {
    require('time-grunt')(grunt);
    require('./smartedit-build')(grunt).load();

    // -------------------------------------------------------------------------------------------------
    // FILE GENERATION
    grunt.registerTask('generate', [
        'shell:installPreCommitGitHook',
        'generateTsConfig',
        'generateSmarteditIndexHtml:landingPage',
        'generateSmarteditIndexHtml:smarteditE2e',
        'generateStorefrontIndexHtml',
        'copy:ckeditor',
        'copy:images',
        'compileTs',
        'webpack:angularStorefront'
    ]);

    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('formatCode', ['shell:prettierRun']);

    // LINTING + SANITIZING
    grunt.registerTask('checkFiles', [
        'jshint',
        'tslint',
        'checkNoForbiddenNameSpaces',
        'checkI18nKeysCompliancy',
        'checkNoFocus'
    ]);

    // -------------------------------------------------------------------------------------------------
    // PREPARE JSTARGET
    grunt.registerTask('prepareJsTarget_Base', [
        'clean:target',
        'styling_dev',
        'copy:sources',
        'copy:fonts',
        'ngtemplates:run',
        'uglify:uglifyThirdparties',
        'instrumentSeInjectable'
    ]);

    grunt.registerTask('prepareJsTarget_Dev', 'prepareJsTarget_Dev', () => {
        const webpackCommonsTasks = [
            'webpack:libSmarteditCommons',
            'shell:internalSmarteditCommons',
            'webpack:devSmarteditCommons'
        ];
        let webpackTasks = [];
        let target = grunt.option('target') ? grunt.option('target').split(',') : [];
        if (target && target.length) {
            target.forEach((t) => {
                if (t === 'inner') {
                    webpackTasks.push('webpack:devSmartedit');
                } else if (t === 'outer') {
                    webpackTasks.push('webpack:devSmarteditContainer');
                } else if (t === 'commons') {
                    webpackTasks = webpackCommonsTasks;
                }
            });
        } else {
            webpackTasks = webpackCommonsTasks.concat([
                'webpack:devSmartedit',
                'webpack:devSmarteditContainer'
            ]);
        }
        grunt.task.run(['webpack:devVendor', 'prepareJsTarget_Base'].concat(webpackTasks));
    });

    grunt.registerTask('prepareJsTarget_Prod', [
        'webpack:prodVendor',
        'prepareJsTarget_Base',
        'webpack:libSmarteditCommons',
        'shell:internalSmarteditCommons',
        'webpack:prodSmarteditCommons',
        'webpack:prodSmartedit',
        'webpack:prodSmarteditContainer',
        'compodoc'
    ]);

    grunt.registerTask('buildE2EScripts', [
        'clean:e2e',
        'copy:e2e',
        'instrumentSeInjectable',
        'webpack:e2eSmartedit',
        'webpack:e2eSmarteditContainer',
        'webpack:e2eScripts'
    ]);

    // -------------------------------------------------------------------------------------------------
    // TEST
    grunt.registerTask('unit', [
        'karma:unitSmarteditcommons',
        'karma:unitSmartedit',
        'karma:unitSmarteditContainer'
    ]);
    grunt.registerTask('test_only', ['generate', 'unit']); // Legacy - see yunit macro in buildcallbacks.xml
    grunt.registerTask('e2e', [
        'buildE2EScripts',
        'connect:dummystorefront',
        'connect:test',
        'protractorRun'
    ]);
    grunt.registerTask('e2e_max', [
        'buildE2EScripts',
        'connect:dummystorefront',
        'connect:test',
        'protractorMaxrun'
    ]);
    grunt.registerTask('e2e_dev', 'e2e local development mode', () => {
        grunt.option('keepalive_dummystorefront', true);
        // un-comment following line once smartedit e2e tests are aligned (/test folder renamed to /jsTests).
        // grunt.option('open_browser', 'http://localhost:7000/' + global.smartedit.bundlePaths.test.e2e.listDest);
        grunt.option('open_browser', 'http://localhost:7000/test/e2e/list.html');
        grunt.task.run([
            'generateE2eListHtml',
            'buildE2EScripts',
            'connect:test',
            'connect:dummystorefront'
        ]);
    });
    grunt.registerTask('e2e_debug', 'e2e local debug mode', () => {
        grunt.option('browser_debug', true);
        grunt.task.run('e2e');
    });

    grunt.registerTask('e2e_max_debug', 'e2e local debug mode', () => {
        grunt.option('browser_debug', true);
        grunt.task.run('e2e_max');
    });

    // -------------------------------------------------------------------------------------------------
    // PREPARE STYLING
    grunt.registerTask('styling_dev', ['webfont', 'less']);

    // -------------------------------------------------------------------------------------------------
    // PREPARE WEBROOT
    // - Must prepare jsTarget first
    grunt.registerTask('prepareWebroot_Base', ['clean:webroot', 'copy:dev']);
    grunt.registerTask('prepareWebroot_Dev', [
        'prepareWebroot_Base',
        'concat:containerThirdpartiesDev',
        'concat:smarteditThirdpartiesDev'
    ]);
    grunt.registerTask('prepareWebroot_Prod', [
        'prepareWebroot_Base',
        'concat:containerThirdparties',
        'concat:smarteditThirdparties'
    ]);

    // -------------------------------------------------------------------------------------------------
    // PREPARE SMARTEDIT BUNDLE
    // - Must prepare jstarget and webroot first
    //
    grunt.registerTask('declareTypes', [
        'concat:smarteditTypes',
        'bundleTypes:smartedit',
        'concat:smarteditcontainerTypes',
        'bundleTypes:smarteditcontainer'
    ]);
    grunt.registerTask('prepareBundle', [
        'clean:bundleForNewSymlinks',
        'declareTypes',
        'symlink:appToBundle',
        'copy:thirdPartySourceMaps',
        'copy:toDummystorefront'
    ]);

    grunt.registerTask('prod', [
        'checkFiles',
        'clean:lib',
        'prepareJsTarget_Prod',
        'prepareWebroot_Prod'
    ]);

    /**
     * To speed up build time, you can use the --target argument (optional).
     * Examples:
     * grunt dev --target=inner
     * grunt dev --target=outer
     * grunt dev --target=inner,outer
     * grunt dev --target=inner,commons
     * etc...
     */
    grunt.registerTask('dev', 'dev', () => {
        const tasks = ['checkFiles', 'prepareJsTarget_Dev', 'prepareWebroot_Dev', 'declareTypes'];
        if (!grunt.option('target') || /^(commons)$/.test(grunt.option('target'))) {
            tasks.unshift('clean:lib');
        }
        grunt.task.run(tasks);
        grunt.log.writeln(
            'To speed up build time, you can run:\n`grunt dev --target=inner`\n`grunt dev --target=outer`\n`grunt dev --target=inner,outer`\n`grunt dev --target=inner,commons`\netc...'
        );
    });

    grunt.registerTask('packageSkipTests', [
        'generate',
        'prod',
        'prepareBundle',
        'shell:prettierCheck'
    ]);

    grunt.registerTask('verify', ['generate', 'prod', 'prepareBundle', 'unit', 'e2e']);
    grunt.registerTask('verify_max', ['generate', 'prod', 'prepareBundle', 'unit', 'e2e_max']);

    grunt.registerTask('test', 'run unit tests', function() {
        let target = grunt.option('target') ? grunt.option('target').split(',') : [];
        let tasks = [];
        if (target && target.length) {
            target.forEach((t) => {
                if (t === 'inner') {
                    tasks.push('karma:unitSmartedit');
                } else if (t === 'outer') {
                    tasks.push('karma:unitSmarteditContainer');
                } else if (t === 'commons') {
                    tasks.push('karma:unitSmarteditcommons');
                }
            });
        } else {
            tasks = ['unit'];
        }
        if (grunt.option('browser') && !/^(inner|outer|commons)$/.test(grunt.option('target'))) {
            grunt.fail.fatal('Please set --target=outer, --target=inner or --target=commons');
        }
        grunt.task.run(['prepareJsTarget_Base'].concat(tasks).concat(['test_tips']));
    });
    grunt.registerTask('coverage', 'run unit tests with coverage report', () => {
        grunt.option('coverage', true);
        grunt.task.run(['generate', 'unit', 'connect:coverage']);
    });
    grunt.registerTask('test_tips', function() {
        grunt.log.writeln(
            'Did you know?\n- You can run unit tests on one or multiple layers with `grunt test --target=inner,outer,commons`\n- You can also debug the unit tests in your browser (for one specific layer only) with `grunt test --browser --target=(inner|outer|commons)`.\n- You can see the code coverage with `grunt coverage`.'
        );
    });

    // Please use prod or dev, these should only be used for watch: tasks
    // because people keep committing code that has not been run through 'checkFiles' task
    grunt.registerTask('package', ['prepareJsTarget_Prod', 'prepareWebroot_Prod']);
    grunt.registerTask('packageDev', ['prepareJsTarget_Dev', 'prepareWebroot_Dev']);

    // Webappinjector - some shorthand aliases that are easier to remember
    grunt.registerTask('dev-wai', ['shell:devbuildwebappinjector']);
    grunt.registerTask('prod-wai', ['shell:prodbuildwebappinjector']);

    // COMPODOC

    grunt.registerTask('compodoc', ['compodoc_smartedit']);
    grunt.registerTask('compodoc:serve', ['compodoc:serve_smartedit']);
};
