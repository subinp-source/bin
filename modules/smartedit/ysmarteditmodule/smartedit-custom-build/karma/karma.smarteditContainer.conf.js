/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
const lodash = require('lodash');

const base = require('../../smartedit-build/config/karma/karma.ext.smartedit.conf');
const bundlePaths = require('../../smartedit-build/bundlePaths');

const { compose, merge, add } = require('../../smartedit-build/builders');

const karma = compose(
    merge({
        junitReporter: {
            outputDir: 'jsTarget/tests/ysmarteditmoduleContainer/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
        },

        // list of files / patterns to load in the browser
        files: lodash.concat(
            bundlePaths.test.unit.smarteditContainerUnitTestFiles,
            bundlePaths.test.unit.commonUtilModules,
            [
                'jsTarget/web/features/ysmarteditmodulecommons/**/*.js',
                'jsTarget/web/features/ysmarteditmoduleContainer/**/*.js',
                'jsTarget/web/features/ysmarteditmoduleContainer/templates.js',
                'jsTests/tests/ysmarteditmoduleContainer/unit/specBundle.ts'
            ]
        ),

        webpack: require('../webpack/webpack.karma.smarteditContainer.config')
    }),
    add(
        'exclude',
        [
            'jsTarget/web/features/ysmarteditmoduleContainer/ysmarteditmodulecontainerModule.ts',
            '**/*.d.ts',
            '*.d.ts'
        ],
        true
    )
)(base);

module.exports = function(config) {
    config.set(karma);
};
