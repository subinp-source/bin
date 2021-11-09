/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { ICMSComponent } from 'cmscommons';

/**
 * @internal
 * @name cmsSmarteditServicesModule.service:componentSharedService
 *
 * @description
 * Service used to determine if a component is shared.
 */
export abstract class IComponentSharedService {
    /**
     * @internal
     * @name cmsSmarteditServicesModule.service:componentSharedService#isComponentShared
     * @methodOf cmsSmarteditServicesModule.service:componentSharedService
     *
     * @description
     * This method is used to determine if a component is shared.
     * A component is considered shared if it is used in two or more content slots.
     *
     * @param {String|ICMSComponent} component The component for which to check if it is shared or not.
     *
     * @returns {angular.IPromise<boolean>} A promise that resolves to a boolean value. True if the component is shared. False, otherwise.
     */
    isComponentShared(component: string | ICMSComponent): angular.IPromise<boolean> {
        'proxyFunction';
        return null;
    }
}
