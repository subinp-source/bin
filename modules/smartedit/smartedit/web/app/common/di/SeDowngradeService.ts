/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';
import { TypedMap } from '@smart/utils';
import { SeConstructor } from './types';
import { diBridgeUtils } from './DIBridgeUtils';
import { diNameUtils } from './DINameUtils';

export const servicesToBeDowngraded: TypedMap<SeConstructor> = {};
/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeDowngradeService
 *
 * @description
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to require an Angular service to be downgraded
 * This decorator must always be at the top/furthest from the class unless a token is provided
 * @param {token=} an `InjectionToken` that identifies a service provided from Angular.
 * Will default to using the constructor itself
 */

export const SeDowngradeService = function(token?: any | InjectionToken<any>) {
    return function<T extends SeConstructor>(serviceConstructor: T) {
        diBridgeUtils.downgradeService(
            diNameUtils.buildServiceName(serviceConstructor),
            serviceConstructor,
            token
        );
        return serviceConstructor;
    };
};
