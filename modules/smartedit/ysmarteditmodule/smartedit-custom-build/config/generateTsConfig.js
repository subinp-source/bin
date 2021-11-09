/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint esversion: 6 */
module.exports = function() {
    return {
        config: function(data, conf) {
            const lodash = require('lodash');

            const ysmarteditmodulePaths = {
                'ysmarteditmodule/*': ['web/features/ysmarteditmodule/*'],
                ysmarteditmodulecommons: ['web/features/ysmarteditmodulecommons'],
                'ysmarteditmodulecommons*': ['web/features/ysmarteditmodulecommons*']
            };

            const yssmarteditmoduleContainerPaths = {
                'ysmarteditmodulecontainer/*': ['web/features/ysmarteditmoduleContainer/*'],
                ysmarteditmodulecommons: ['web/features/ysmarteditmodulecommons'],
                'ysmarteditmodulecommons*': ['web/features/ysmarteditmodulecommons*']
            };

            const commonsLayerInclude = [
                '../../jsTarget/web/features/ysmarteditmodulecommons/**/*'
            ];
            const innerLayerInclude = commonsLayerInclude.concat([
                '../../jsTarget/web/features/ysmarteditmodule/**/*'
            ]);
            const outerLayerInclude = commonsLayerInclude.concat([
                '../../jsTarget/web/features/ysmarteditmoduleContainer/**/*'
            ]);
            const commonsLayerTestInclude = [
                '../../jsTests/tests/ysmarteditmodulecommons/unit/**/*'
            ];
            const innerLayerTestInclude = commonsLayerTestInclude.concat([
                '../../jsTests/tests/ysmarteditmodule/unit/features/**/*'
            ]);
            const outerLayerTestInclude = commonsLayerTestInclude.concat([
                '../../jsTests/tests/ysmarteditmoduleContainer/unit/features/**/*'
            ]);

            function addYsmarteditmodulePaths(conf) {
                lodash.merge(conf.compilerOptions.paths, lodash.cloneDeep(ysmarteditmodulePaths));
            }

            function addYsmarteditmoduleContainerPaths(conf) {
                lodash.merge(
                    conf.compilerOptions.paths,
                    lodash.cloneDeep(yssmarteditmoduleContainerPaths)
                );
            }

            // PROD
            addYsmarteditmodulePaths(conf.generateProdSmarteditTsConfig.data);
            addYsmarteditmoduleContainerPaths(conf.generateProdSmarteditContainerTsConfig.data);
            conf.generateProdSmarteditTsConfig.data.include = innerLayerInclude;
            conf.generateProdSmarteditContainerTsConfig.data.include = outerLayerInclude;

            // DEV
            addYsmarteditmodulePaths(conf.generateDevSmarteditTsConfig.data);
            addYsmarteditmoduleContainerPaths(conf.generateDevSmarteditContainerTsConfig.data);
            conf.generateDevSmarteditTsConfig.data.include = innerLayerInclude;
            conf.generateDevSmarteditContainerTsConfig.data.include = outerLayerInclude;

            // KARMA
            addYsmarteditmodulePaths(conf.generateKarmaSmarteditTsConfig.data);
            addYsmarteditmoduleContainerPaths(conf.generateKarmaSmarteditContainerTsConfig.data);
            conf.generateKarmaSmarteditTsConfig.data.include = innerLayerInclude.concat(
                innerLayerTestInclude
            );
            conf.generateKarmaSmarteditContainerTsConfig.data.include = outerLayerInclude.concat(
                outerLayerTestInclude
            );

            // IDE
            addYsmarteditmodulePaths(conf.generateIDETsConfig.data);
            addYsmarteditmoduleContainerPaths(conf.generateIDETsConfig.data);
            conf.generateIDETsConfig.data.include = conf.generateIDETsConfig.data.include.concat([
                '../../jsTests/tests/**/unit/**/*'
            ]);

            return conf;
        }
    };
};
