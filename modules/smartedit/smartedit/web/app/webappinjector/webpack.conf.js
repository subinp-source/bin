/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint esversion: 6 */
const path = require('path');
const paths = require('../../../smartedit-custom-build/paths');

module.exports = {
    // ########################################
    // ##### Configured through CLI with ######
    // "mode": "production",
    // "devtool": "source-map",
    // ########################################
    entry: path.resolve(__dirname, './src/webapp-injector.ts'),

    output: {
        path: path.resolve(process.cwd(), paths.bundlePaths.test.e2e.webappinjectors.root),
        filename: 'webApplicationInjector.js'
    },
    resolve: {
        modules: [path.resolve(process.cwd(), './node_modules'), path.resolve(__dirname, './src')],
        extensions: ['.ts', '.js']
    },
    stats: {
        assets: true,
        colors: true,
        modules: true,
        reasons: true,
        errorDetails: true
    },
    module: {
        rules: [
            {
                test: /\.ts$/,
                loader: 'ts-loader',
                options: {
                    configFile: path.resolve(__dirname, './tsconfig.json')
                }
            }
        ]
    }
};
