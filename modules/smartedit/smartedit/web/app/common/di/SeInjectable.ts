/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeInjectable()
 * @deprecated since 1905
 * @description
 * @deprecated since 1905
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to declare a Smartedit injectable service from a Dependency injection standpoint.
 * When multiple class annotations are used, {@link smarteditServicesModule.object:@SeInjectable() @SeInjectable()} must be closest to the class declaration.
 */

export const SeInjectable = function() {
    return function(providerConstructor: any) {
        return providerConstructor;
    };
};
