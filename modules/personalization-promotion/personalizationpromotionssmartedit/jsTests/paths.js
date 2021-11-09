/* jshint unused:false, undef:false */
module.exports = function() {

    /***
     *  Naming:
     *  File or Files masks should end in File or Files,
     *  ex: someRoot.path.myBlaFiles = /root/../*.*
     *
     *  General rules:
     *  No copy paste
     *  No duplicates
     *  Avoid specific files when possible, try to specify folders
     *  What happens to personalizationpromotionssmartedit, happens to personalizationpromotionssmarteditContainer
     *  Try to avoid special cases and exceptions
     */
    var lodash = require('lodash');
    var paths = {};

    // ################## CONFIG ##################

    paths.config = {};
    paths.config.root = 'jsTests/config';
    paths.config.protractorConf = paths.config.root + '/protractor-conf.js';

    // ################## TESTS ##################

    paths.tests = {};

    paths.tests.root = 'jsTests';
    paths.tests.reports = paths.tests.root + '/reports';
    paths.tests.testsRoot = paths.tests.root + '/tests';
    paths.tests.personalizationpromotionssmarteditTestsRoot = paths.tests.testsRoot + '/personalizationpromotionssmartedit';
    paths.tests.personalizationpromotionssmarteditContainerTestsRoot = paths.tests.testsRoot + '/personalizationpromotionssmarteditContainer';
    paths.tests.personalizationpromotionssmarteditUnitTestsRoot = paths.tests.personalizationpromotionssmarteditTestsRoot + '/unit';
    paths.tests.personalizationpromotionssmarteditContainerUnitTestsRoot = paths.tests.personalizationpromotionssmarteditContainerTestsRoot + '/unit';
    paths.tests.personalizationpromotionssmartedite2eTestsRoot = paths.tests.personalizationpromotionssmarteditTestsRoot + '/e2e';
    paths.tests.personalizationpromotionssmarteditContainere2eTestsRoot = paths.tests.personalizationpromotionssmarteditContainerTestsRoot + '/e2e';

    paths.tests.personalizationpromotionssmarteditTSUnitTestFiles = paths.tests.personalizationpromotionssmarteditUnitTestsRoot + '/features/**/*.ts';
    paths.tests.personalizationpromotionssmarteditContainerTSUnitTestFiles = paths.tests.personalizationpromotionssmarteditContainerUnitTestsRoot + '/features/**/*.ts';

    paths.tests.personalizationpromotionssmarteditSpecBundle = paths.tests.personalizationpromotionssmarteditUnitTestsRoot + '/specBundle.ts';
    paths.tests.personalizationpromotionssmarteditContainerSpecBundle = paths.tests.personalizationpromotionssmarteditContainerUnitTestsRoot + '/specBundle.ts';

    // ################## SOURCES ##################

    paths.sources = {};

    paths.sources.root = 'web';
    paths.sources.features = paths.sources.root + '/features';

    paths.sources.images = paths.sources.root + '/webroot/icons/**/*';
    paths.sources.commonsTSFiles = paths.sources.features + '/personalizationpromotionssmarteditcommons/**/*.ts';
    paths.sources.personalizationpromotionssmarteditTSFiles = paths.sources.features + '/personalizationpromotionssmartedit/**/*.ts';
    paths.sources.personalizationpromotionssmarteditContainerTSFiles = paths.sources.features + '/personalizationpromotionssmarteditContainer/**/*.ts';

    // ################## TARGET ##################

    paths.target = {};

    paths.target.features = 'jsTarget/web/features';

    paths.target.commonsTemplatesFile = paths.target.features + '/personalizationpromotionssmarteditcommons/**/templates.js';
    paths.target.personalizationpromotionssmarteditTemplatesFile = paths.target.features + '/personalizationpromotionssmartedit/**/templates.js';
    paths.target.personalizationpromotionssmarteditContainerTemplatesFile = paths.target.features + '/personalizationpromotionssmarteditContainer/**/templates.js';
    paths.target.featureExtensionsSmartEditImport = paths.target.features + '/personalizationpromotionssmartedit/PersonalizationpromotionssmarteditApp.ts';
    paths.target.featureExtensionsSmartEditContainerImport = paths.target.features + '/personalizationpromotionssmarteditContainer/PersonalizationpromotionssmarteditContainerApp.ts';


    // ################## MOCKS ##################

    paths.mocks = {};
    paths.mocks.root = 'jsTests';

    paths.mocks.dataRoot = paths.mocks.root + '/mockData';
    paths.mocks.serviceRoot = paths.mocks.root + '/mockServices';
    paths.mocks.daoRoot = paths.mocks.root + '/mockDao';

    paths.mocks.dataFiles = paths.mocks.dataRoot + '/**/*.ts';
    paths.mocks.serviceFiles = paths.mocks.serviceRoot + '/**/*.ts';
    paths.mocks.daoFiles = paths.mocks.daoRoot + '/**/*.ts';


    // ########## PAGE OBJECTS / COMPONENT OBJECTS ##########

    paths.testObjects = {};

    paths.testObjects.pageObjectsRoot = 'jsTests/pageObjects';
    paths.testObjects.componentObjectsRoot = 'jsTests/componentObjects';

    paths.testObjects.pageObjectsFiles = paths.testObjects.pageObjectsRoot + '/**/*.ts';
    paths.testObjects.componentObjectFiles = paths.testObjects.componentObjectsRoot + '/**/*.ts';



    // ################## MISC ##################

    paths.thirdPartiesRoot = 'buildArtifacts/static-resources/thirdparties';
    paths.seLibrariRoot = 'buildArtifacts/seLibraries';

    // ================================================================================================================
    // ================================================================================================================
    // ================================================================================================================

    paths.getPersonalizationpromotionssmarteditKarmaConfFiles = function getPersonalizationpromotionssmarteditKarmaConfFiles() {
        return lodash.concat(
            global.smartedit.bundlePaths.test.unit.smarteditThirdPartyJsFiles,
            global.smartedit.bundlePaths.test.unit.commonUtilModules,
            paths.mocks.dataFiles,
            paths.mocks.daoFiles,
            paths.mocks.serviceFiles,
            paths.testObjects.componentObjectFiles,
            paths.target.commonsTemplatesFile,
            paths.target.personalizationpromotionssmarteditTemplatesFile,
            paths.sources.commonsTSFiles,
            paths.sources.personalizationpromotionssmarteditTSFiles,
            paths.tests.personalizationpromotionssmarteditTSUnitTestFiles,
            paths.tests.personalizationpromotionssmarteditSpecBundle,
            // Images
            {
                pattern: paths.sources.images,
                watched: false,
                included: false,
                served: true
            }
        );
    };

    paths.getPersonalizationpromotionssmarteditContainerKarmaConfFiles = function getPersonalizationpromotionssmarteditContainerKarmaConfFiles() {
        return lodash.concat(
            global.smartedit.bundlePaths.test.unit.smarteditContainerUnitTestFiles,
            global.smartedit.bundlePaths.test.unit.commonUtilModules,
            paths.mocks.dataFiles,
            paths.mocks.daoFiles,
            paths.mocks.serviceFiles,
            paths.testObjects.componentObjectFiles,
            paths.target.personalizationpromotionssmarteditContainerTemplatesFile,
            paths.target.commonsTemplatesFile,
            paths.sources.personalizationpromotionssmarteditContainerTSFiles,
            paths.sources.commonsTSFiles,
            paths.tests.personalizationpromotionssmarteditContainerTSUnitTestFiles,
            paths.tests.personalizationpromotionssmarteditContainerSpecBundle,
            // Images
            {
                pattern: paths.sources.images,
                watched: false,
                included: false,
                served: true
            }
        );
    };

    paths.getE2eFiles = function getE2eFiles() {
        return [
            // paths.tests.personalizationpromotionssmartedite2eTestFiles,
            // paths.tests.personalizationpromotionssmarteditContainere2eTestFiles
        ];
    };

    /**
     * Code coverage
     */
    paths.coverage = {
        dir: './jsTarget/test/coverage',
        personalizationpromotionssmarteditDirName: 'personalizationpromotionssmartedit',
        personalizationpromotionssmarteditContainerDirName: 'personalizationpromotionssmarteditContainer'
    };


    return paths;

}();
