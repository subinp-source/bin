module.exports = function(grunt) {

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

    // -------------------------------------------------------------------------------------------------
    // Linting
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('linting', ['jshint', 'tslint']);

    // -------------------------------------------------------------------------------------------------
    // Compilation
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('compile_only', ['sanitize', 'linting', 'multipleCopySources', 'injectExtensionsImports', 'multipleNGTemplates', 'checkNoFocus', 'checkNoForbiddenNameSpaces', 'checkI18nKeysCompliancy', 'styling_only']);
    grunt.registerTask('compile', ['clean:target', 'compile_only']);

    grunt.registerTask('concatAndPushDev', ['instrumentSeInjectable', 'webpack:devSmartedit', 'webpack:devSmarteditContainer', 'copy:dev']);

    // -------------------------------------------------------------------------------------------------
    // Unit Tests
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('test_only', ['generate', 'instrumentSeInjectable', 'multiKarma']);
    grunt.registerTask('test', ['compile', 'test_only']);

    // -------------------------------------------------------------------------------------------------
    // Dev - For development code
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('dev', ['compile', 'concatAndPushDev']);

    // -------------------------------------------------------------------------------------------------
    // Packaging - For production ready code
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('concatAndPushProd', ['instrumentSeInjectable', 'webpack:prodSmartedit', 'webpack:prodSmarteditContainer', 'copy:dev']);

    grunt.registerTask('package_only', ['concatAndPushProd', 'ngdocs']);
    grunt.registerTask('package', ['compile', 'package_only', 'test_only']);

    grunt.registerTask('packageSkipTests', ['generate', 'compile_only', 'package_only']);

    // -------------------------------------------------------------------------------------------------
    // E2E Tests
    // -------------------------------------------------------------------------------------------------
    grunt.registerTask('setupE2e', ['generateSmarteditIndexHtml:e2eSetup', 'generateStorefrontIndexHtml', 'connect:test']);
    grunt.registerTask('e2e', ['setupE2e', 'multiProtractor']); //any change to the e2e should be adapted to e2e_max task
    grunt.registerTask('e2e_max', ['setupE2e', 'multiProtractorMax']);
    grunt.registerTask('verify_only', ['e2e']);

    // Full PROD build
    grunt.registerTask('verify', ['generate', 'package', 'verify_only']); //any change to the verify tash should be adapted to verify_max task
    grunt.registerTask('verify_max', ['generate', 'package', 'e2e_max']);

    grunt.registerTask('styling_only', ['less']);
    grunt.registerTask('styling', ['clean', 'styling_only']);

};
