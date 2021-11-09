/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { TypedMap } from '@smart/utils';

import { SeModule } from '../di';

const pathRegExp: RegExp = /web.+\/(\w+)\.html/;

const delegator = ($delegate: angular.ITemplateCacheService): angular.ITemplateCacheService => {
    'ngInject';

    const namePathMap: TypedMap<string> = {};
    const originalPut = $delegate.put;

    $delegate.put = function() {
        const path: string = arguments[0];
        const template: string = arguments[1];

        if (pathRegExp.test(path)) {
            const fileName = pathRegExp.exec(path)[1] + '.html';

            if (!namePathMap[fileName]) {
                originalPut.apply($delegate, [fileName, template]);
                namePathMap[fileName] = path;
            } else {
                throw new Error(
                    `[templateCacheDecorator] html templates ${
                        namePathMap[fileName]
                    } and ${path} are conflicting, you must give them different filenames`
                );
            }
        }
        return originalPut.apply($delegate, arguments);
    };

    // ============== UNCOMMENT THIS TO DEBUG TEMPLATECACHE ==============
    // ========================== DO NOT COMMIT ==========================
    // const originalGet = $delegate.get;
    //
    // $delegate.get = function() {
    //     const path: string = arguments[0];
    //     const $log: angular.ILogService = angular.injector(['ng']).get('$log');
    //
    //     $log.debug("$templateCache GET: " + path);
    //     return originalGet.apply($delegate, arguments);
    // };

    return $delegate;
};

@SeModule({
    config: ($provide: angular.auto.IProvideService) => {
        'ngInject';

        $provide.decorator('$templateCache', ['$delegate', delegator]);
    }
})
export class TemplateCacheDecoratorModule {}
