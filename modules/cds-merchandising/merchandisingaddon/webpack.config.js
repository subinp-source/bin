/*
	Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
*/
const path = require('path');

module.exports = {
	mode: 'production',
	entry: path.resolve(__dirname, './acceleratoraddon/web/features/carouselinitialiser/main.ts'),
	resolve: {
		extensions: ['.tsx', '.ts', '.js']
	},
	output: {
		filename: 'bundle.js',
		path: path.resolve(__dirname, './jsTarget/dest')
	},
	 // Add the loader for .ts files.
	module: {
		rules: [
			{
				test: /\.ts?$/,
				loader: 'ts-loader',
				options: {
					configFile:path.resolve(__dirname, './acceleratoraddon/web/features/tsconfig.json')
				}
			}
		]
	}
};
