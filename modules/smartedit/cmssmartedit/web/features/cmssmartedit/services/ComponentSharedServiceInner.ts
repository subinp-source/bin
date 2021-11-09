/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, SeInjectable } from 'smarteditcommons';
import { IComponentSharedService, ICMSComponent } from 'cmscommons';

@GatewayProxied()
@SeInjectable()
export class ComponentSharedService extends IComponentSharedService {
    constructor(private componentInfoService: any, private $q: angular.IQService) {
        super();
    }

    isComponentShared(componentParam: string | ICMSComponent): angular.IPromise<boolean> {
        let componentPromise: angular.IPromise<ICMSComponent>;
        if (typeof componentParam === 'string') {
            componentPromise = this.componentInfoService.getById(componentParam);
        } else {
            componentPromise = this.$q.when(componentParam);
        }

        return componentPromise.then((component: ICMSComponent) => {
            if (!component.slots) {
                return this.$q.reject(
                    'ComponentSharedService::isComponentShared - Component must have slots property.'
                );
            }

            return component.slots.length > 1;
        });
    }
}
