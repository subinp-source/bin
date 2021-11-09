/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeModule
 *
 * @description
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to declare a Smartedit module as en try point.
 * @param {object} id the module identifier used when loading it into Smartedit
 */

export const SeEntryModule = function(id: string) {
    return function(moduleConstructor: any) {
        window.__smartedit__.modules[id] = moduleConstructor;
        return moduleConstructor;
    };
};
